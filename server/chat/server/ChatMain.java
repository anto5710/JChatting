package chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
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

	static List<ClientHandler > handlers = new ArrayList<ClientHandler>();
	public static void main(String[] args) throws IOException {
		
		ServerSocket serverSock = new ServerSocket( 8999 );
		boolean running = true;
		
		while ( running ) {
			System.out.println("starting server ... at " + 8999);
			Socket client = serverSock .accept(); // blocking method
			ClientHandler handler = new ClientHandler(client);
			handlers.add(handler);
//			handler.sendChatterList();
		}
		
//		client.close();
		serverSock.close();
	}
	
	public static void updateChatterList(){
		List<String> nicknames = handlers.stream()
				                         .map(ch -> ch.getNickname())
				                         .collect(Collectors.toList());
		for (ClientHandler handler : handlers){
			try {
				handler.sendChatterList(nicknames);
			} catch ( Exception e) {
				e.printStackTrace();
//				 handlers.remove(handler);
			}
		}
	}
	
	public static void broadcast (String sender, String msg ) {
		for(ClientHandler handler : handlers){
			handler.sendMessage( sender, msg);
		}
	}
}
