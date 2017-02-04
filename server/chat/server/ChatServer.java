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

	static Map<ClientHandler, String> handlers = new HashMap<>();
	
	private static ClientCleaner cleaner ;
	private static DataHandle dataHandler = new DataHandle();
	
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
		loginClient.registerHandler(dataHandler);
		//
		notifyLogin(loginClient);// handler 제외한 기존의 채팅 참여자들한테 보낼 메세지가 따로 있어야 할 듯?
		handlers.put(loginClient, loginClient.getNickname());
		sendChatterList(loginClient); // 지금 로그인한 당사자한테
		//
	}
	
	public static void unregisterClient(ClientHandler logoutClient){
		if(handlers.remove(logoutClient)!=null){ //제거했을떄
			notifyLogout(logoutClient);
		}
	}
	
	private static void notifyLogin ( ClientHandler loginUser){
		String nicknam = loginUser.getNickname();
		for(ClientHandler handler: getClients()){
			try {
				handler.notifyLogin(nicknam);
			} catch (IOException e) {
				cleaner.registerDeadClient(handler);
			}
		}
	}
	
	private static void notifyLogout ( ClientHandler logoutUser ) {
		String nicknam = logoutUser.getNickname();
		for(ClientHandler handler: getClients()){
			try {
				handler.notifyLogout(nicknam);
			} catch (IOException e) {
				cleaner.registerDeadClient(handler);
			}
		}
	}
	
	public static String[] getChatterList(){
		return handlers.values().toArray(new String[0]);
	}
	
	private static void sendChatterList(ClientHandler loginUser){
		// AA,BB,CC
		try {
			loginUser.sendChatterList(getChatterList());
		} catch (IOException e) {
			e.printStackTrace();
			cleaner.registerDeadClient(loginUser);
		}
	}
	
	public static Set<ClientHandler> getClients(){
		return handlers.keySet();
	}

	public static void sendPrvMSG (String sender, String msg, Collection<String> receivers){
		getClients().stream().
					 filter(h->receivers.contains(h.getNickname())).
				     forEach(h ->{
			Util.tryDoing(() ->h.sendPrvMSG(msg, sender));
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
	
	/* sun이라는 회사에서 만든 언어가 자바!
	 * 
	 * 상황 - 1990년도 
	 * c언어 / c++ - 기계어를 건드립니다.
	 * 
	 * if ( x ) {
	 *   if 
	 *  ;
	 *  else if ( y ) {
	 *  ;
	 *  } else {
	 *  ;
	 *  }
	 */
	
	static class DataHandle implements CommandHandler {
	
		@Override
		public void handleData(ClientHandler client, String cmd, Object [] data) {
			switch ( cmd ) {
			case "PRV_MSG" :
				String msg = (String) data[0];
				String [] receivers = (String[]) data[1];
				ChatServer.sendPrvMSG(client.getNickname(), msg, Arrays.asList(receivers));
				break;
			case "MSG" :
				String nickName = client.getNickname();
				msg = (String) data[0];
				System.out.printf( "%s: %s\n", nickName, msg);
				ChatServer.broadcastMSG( nickName, msg);
				
				break;
			case "LOGOUT" :
				unregisterClient(client);
				client.stop();
				
				break;
			}
		}
	}
}
