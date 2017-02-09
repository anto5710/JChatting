package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import util.Util;
import chat.client.ui.LoginDialog;
/**
 *  172.30.1.39:3076
 *  
 *  IP  172.30.1.39
 *     특정 기계마다 갖는 고유한 주소를 의미합니다.
 *     
 *  PORT 1 ~ 65535
 *  
 */
public class ChatServer {

	private static Map<String, ClientHandler> handlers = new HashMap<>();
	private static ClientCleaner cleaner ;
	
	public static void main(String[] args) throws IOException {
		cleaner = new ClientCleaner();
		
		ServerSocket serverSock = new ServerSocket( 8999 );
		boolean running = true;
		
		while ( running ) {
			System.out.println("starting server ... at " + 8999);
			Socket client = serverSock.accept(); // blocking method
			ClientHandler handler = new ClientHandler(client);
			registerClient(handler);
		}
		serverSock.close();
	}
	
	public static void registerClient(ClientHandler loginClient){
		loginClient.readNickName();
		loginClient.registerHandler(new DataHandle());

		String name = loginClient.getNickname();
		notifyLogin(name);// handler 제외한 기존의 채팅 참여자들한테 보낼 메세지가 따로 있어야 할 듯?
		handlers.put(name, loginClient);
		sendChatterList(loginClient); // 지금 로그인한 당사자한테
	}
	
	public static void unregisterClient(String name){
		if(handlers.remove(name)!=null)
			notifyLogout(name);
	}
	
	private static void notifyLogin ( String name){
		for(ClientHandler handler: getClients()){
			try {
				handler.notifyLogin(name);
			} catch (IOException e) {
				cleaner.registerDeadClient(handler);
			}
		}
	}
	
	private static void notifyLogout ( String name ) {
		for(ClientHandler handler: getClients()){
			try {
				handler.notifyLogout(name);
			} catch (IOException e) {
				cleaner.registerDeadClient(handler);
			}
		}
	}
	
	public static String[] getChatterList(){
		return handlers.keySet().toArray(new String[0]);
	}
	
	private static void sendChatterList(ClientHandler loginUser){
		try {
			loginUser.sendChatterList(getChatterList());
		} catch (IOException e) {
			cleaner.registerDeadClient(loginUser);
		}
	}
	
	public static Collection<ClientHandler> getClients(){
		return handlers.values();
	}

	public static void sendPrvMSG (String sender, String msg, String [] receivers){
		Arrays.stream(receivers).map(name->handlers.get(name)).
						   forEach(hdl->{
							   Util.tryDoing(()->hdl.sendPrvMSG(sender, msg, receivers));
						   });
	}
	
	public static void broadcastMSG (String sender, String msg ) {
		for(ClientHandler handler: getClients()){
			try {
				handler.sendMessage(msg);
			} catch (IOException e) {
				cleaner.registerDeadClient(handler);
				e.printStackTrace();
			}
		}
	}
	
	static class DataHandle implements CommandHandler {
	
		@Override
		public void handleData(ClientHandler client, String cmd, Object data) {
			switch ( cmd ) {
			case "PRV_MSG" :
				Map<String, Object> map = (Map<String, Object>)data;
				String sender = (String) map.get("sender");
				String msg = (String) map.get("msg");
				String [] names = (String[]) map.get("names");
				
				ChatServer.sendPrvMSG(client.getNickname(), msg, names);
				break;
			case "MSG" :
				String name = client.getNickname();
				msg = (String) data;
				System.out.printf( "%s: %s\n", name, msg);
				ChatServer.broadcastMSG( name, msg);
				
				break;
			case "LOGOUT" :
				unregisterClient(client.getNickname());
				client.stop();
				
				break;
			}
		}
	}
}
