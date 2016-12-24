package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import chat.client.protocol.ChatterList;
import chat.protocol.IProtocol;
import chat.protocol.Login;
import chat.protocol.PublicMessage;
import chat.protocol.UnknownCommandException;
import chat.util.Util;

/**
 * 특정 클라이언트가 보낸 메세지를 처리합니다.
 */
public class ClientHandler implements Runnable {

	private Thread t ;
	private Socket sock ;
	private boolean running = true;
	private DataInputStream dis = null; 
	private DataOutputStream dos = null;
	private String nickName ; 
	
	private Map<String, IProtocol> protocolMap = new HashMap<String, IProtocol>();
	{
		try {
			registerProtocols(PublicMessage.class);
		} catch (Exception e) {
			throw new RuntimeException("Error while registering protocols");
		}
	}
	
	@SafeVarargs
	final private void registerProtocols( Class <? extends IProtocol>...classes) {
//		IProtocol prt = null;
//		prt = null;
		Arrays.stream(classes).forEach(cls -> {
			IProtocol prt = Util.createInstance(cls);
			protocolMap.put(prt.getCommand(), prt);			
		});
	}
	
	private List<CommandHandler> handlers = new ArrayList<CommandHandler>(); 
	
	public void registerHandler ( CommandHandler handler) {
		
		if ( handlers.contains(handler)) {
			handlers.remove(handler);
		}
		this.handlers.add ( handler );
	}
	
	public void unregisterHandler ( CommandHandler handler) {
		this.handlers.remove(handler);
	}
	
	public ClientHandler ( Socket client ) throws IOException {
		sock = client ;
		//init
		dis = new DataInputStream(sock.getInputStream());
		dos = new DataOutputStream(sock.getOutputStream());
		
		t = new Thread( this );
		t.start(); 
	}
	
	private void readNickName() {
		try {
			// read nickname
			String cmd = dis.readUTF(); // "LOGIN"
			if( "LOGIN".equals(cmd) ) {
				this.nickName = dis.readUTF();
				t.setName("T-" + this.nickName);
			} else throw new ChatProtocolException ( "expected LOGIN but " + cmd );
			
		} catch (IOException e1) {
			throw new RuntimeException("fail to create stream");
		}
	}

	public void stop(){
		running = false;
		this.t.interrupt();
	}
	
	@Override
	public void run() {
		readNickName();
		while ( running ) {
			try {
				String cmd = dis.readUTF(); // MSG, LOGOUT
				IProtocol protocol = protocolMap.get(cmd);
				if ( protocol != null) {
					Object data = protocol.read(dis);
					notifyNewData (cmd, data );					
				} else {
					throw new UnknownCommandException("invalid CMD? " + cmd );
				}
				
			} catch (IOException e) {
				running = false;
			} 
		}
		/**
		 * TODO
		 * 
		 * ChatMain.handlers에서 현재 ClientHandler를 제거해야 합니다.
		 * 원래는 아래처럼 제거하기 전에 다른 참여자들에게 로그아웃했다는 메세지를 보내줘야 합니다.
		 */
		ChatServer.unregisterClient(this);
	}	
		
	private void notifyNewData(String cmd, Object data) {
		for (CommandHandler ch : handlers) {
			ch.handleData(this, cmd, data);
		}
	}
	
//	public void sendData( String cmd, String data) throws IOException{
//		// PRIV_MSG AA BB XXXXXXXXXXX
//		// MSG XXXXX
//		System.out.println(String.format("[%s] %d %s", cmd, 1, data));
//		dos.writeUTF(cmd);
////		dos.writeInt(1);
//		dos.writeUTF(data);
//	}
//	public void sendPublicMsg ( String msg) throws IOException {
//		dos.writeUTF("MSG");
//		dos.writeUTF(msg);
//		dos.flush();
//	}
//	
//	public void sendData(String cmd, String [] data) throws IOException{
//		System.out.println(String.format("[%s] %d %s", cmd, data.length, Arrays.toString(data) ));
//		dos.writeUTF(cmd);
//		dos.writeInt(data.length);
//		for (int i = 0; i < data.length; i++) {
//			dos.writeUTF(data[i]);
//		}
//	}
	
	public void sendChatterList (String[]nickNames) throws IOException{
		protocolMap.get("CHATTER_LIST").write(dos, nickNames);
	}
	
	public void sendMessage (String sender, String msg ) throws IOException{
		protocolMap.get("MSG").write(dos, msg);
		System.out.println("sent to " + this.nickName + " => " + sender + ":" + msg);
	}

	public String getNickname() {
		return nickName;
	}

	public void notifyLogin(String nicknam) throws IOException{
		protocolMap.get("LOGIN").write(dos, nicknam);
	}
	
	public void notifyLogout(String nicknam) throws IOException {
		protocolMap.get("LOGOUT").write(dos, nicknam);
	}
}
