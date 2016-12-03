package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
/**
 *  172.30.1.39:3076
 *  
 *  IP  172.30.1.39
 *     특정 기계마다 갖는 고유한 주소를 의미합니다.
 *     
 *  PORT 1 ~ 65535
 *  
 */
public class ChatMain {

	static Map<ClientHandler, String> handlers = new HashMap<>();
	private static List<ClientHandler> deadClients = new ArrayList<>();
	
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
		notifyLogin(loginClient);// handler 제외한 기존의 채팅 참여자들한테 보낼 메세지가 따로 있어야 할 듯?
		handlers.put(loginClient, loginClient.getNickname());
		sendChatterList(loginClient); // 지금 로인한 당사자한테
		
	}
	
	public static void unregisterClient(ClientHandler logoutClient){
		if(handlers.remove(logoutClient)!=null){ //제거했을떄
			notifyLogout(logoutClient);
		}
	}
	
	/*
	}
	*/
	
	private static void sendChatterList(ClientHandler loginUser){
		// AA,BB,CC
		try {
			loginUser.sendData(
					"CHATTER_LIST",  
					handlers.values().toArray(new String[0]));
		} catch (IOException e) {
//			deadClients.add(loginUser);
			cleaner.registerDeadClient(loginUser);
		}
	}
	
	public static void notifyLogin ( ClientHandler loginUser){
		for(ClientHandler handler: getClients()){
			try {
				handler.sendData("LOGIN", handler.getNickname());
			} catch (IOException e) {
				cleaner.registerDeadClient(handler);
			}
		}
	}
	
	public static void notifyLogout ( ClientHandler logoutUsers ) {
		
	}
	
	public static Set<ClientHandler> getClients(){
		return handlers.keySet();
	}
	
	public static void broadcastMSG (String sender, String msg ) {
		System.out.println("###" + getClients().size());
		for(ClientHandler handler : getClients()){
			try {
				handler.sendMessage( sender, msg);
			} catch (IOException e) {
//				deadClients.add(handler);
				cleaner.registerDeadClient(handler);
			}
		}
//		removeDeadClients();
	}
}
