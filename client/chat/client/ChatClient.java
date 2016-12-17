package chat.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
	private ServerHandler handler;
	private final String IP;
	private final int PORT;
	private final String NICKNAME; 
	
	public ChatClient(String IP, int port, String nickName) throws UnknownHostException, IOException{
		this.IP = IP;
		this.PORT = port;
		this.NICKNAME = nickName;
		
		Socket sock = new Socket(IP, port);
		// 이 시점에 연결이 잘 된것입니다.
		handler = new ServerHandler(sock);
	}
	
	public ServerHandler getServerHandler() {
		return handler;
	}

	public String getIP() {
		return IP;
	}

	public int getPort() {
		return PORT;
	}

	public String getNickname() {
		return NICKNAME;
	}

	public void sendMessage (String msg){
		handler.sendMessage(msg);
	}

	public void sendNickname() {
		handler.sendLogin(NICKNAME);
	}

	public void sendLogout() {
		handler.sendData("LOGOUT", "");
	}
}
