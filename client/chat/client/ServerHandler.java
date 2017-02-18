package chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Logger;
import util.Util;
import chat.protocol.ChatterList;
import chat.protocol.IProtocol;
import chat.protocol.Login;
import chat.protocol.Logout;
import chat.protocol.PrivateMessage;
import chat.protocol.PublicMessage;
import chat.protocol.UnknownCommandException;

/**
 * 서버에서 전달되는 메시지를 처리합니다.
 * @author anto5710
 *
 */
@SuppressWarnings("unchecked")
public class ServerHandler implements Runnable{

	private Thread t;
	private DataInputStream dis ;
	private DataOutputStream dos ;
	
	private boolean running = true;
	private final Socket SERVER;
	private final String IP, NICKNAME;
	private final int PORT;

	private List<ServerDataListener> listeners = new ArrayList<>();
	
	public void addListener ( ServerDataListener listener) {
		Logger.log("registering listenr : " + listener);
		this.listeners.add(listener);
	}
	
	public void removeListener ( ServerDataListener listener) {
		this.listeners.remove(listener);
	}

	private Map<String, IProtocol> protocolMap =new HashMap<String, IProtocol>();
	{
		registerProtocols(PublicMessage.class, Login.class, Logout.class, ChatterList.class, PrivateMessage.class);
	}
	
	private void registerProtocols(Class <? extends IProtocol>...classes) {
		Arrays.stream(classes).forEach(cls -> {
			IProtocol prt = Util.createInstance(cls);
			protocolMap.put(prt.getCommand(), prt);			
		});
	}
	
	public ServerHandler( String ip, int port, String nickName ) throws IOException{
		this.IP=ip;
		this.PORT=port;
		this.NICKNAME=nickName;
		this.SERVER=new Socket(ip,port);
		
		dis = new DataInputStream(SERVER.getInputStream());
		dos = new DataOutputStream(SERVER.getOutputStream());
		t = new Thread(this);
		t.setName("T-SH");
		t.start();
	}
	
	public void sendLogin(String nickName) throws IOException {
		System.out.println("[" + Thread.currentThread().getName() + "]sending login name " + nickName);
		t.setName("T-" + nickName);
		protocolMap.get("LOGIN").write(dos, new Object[]{nickName});
		System.out.println("[" + Thread.currentThread().getName() + "sent nickname");
	}
	
	public void sendLogout() throws IOException{ 
		protocolMap.get("LOGOUT").write(dos);
	}
	
	@Override
	public void run(){
		System.out.println("[" + Thread.currentThread().getName() + "] starting server handler");
		while ( running ) {
			try {
				
				System.out.println("[" + Thread.currentThread().getName() + "] waiting from server .." );
				String cmd = dis.readUTF();
				System.out.println("[" + Thread.currentThread().getName() + "] comd from server : " + cmd);
				
				/* TODO 이와 같이 프로토콜 해석 구현체를 따로 분리해서 사용합니다. */
				IProtocol protocol = protocolMap.get(cmd);
				if ( protocol != null ) {
					Object data = protocol.read(dis); // String []
					notifyResponse(cmd, data);
					
				} else throw new UnknownCommandException("invalid CMD? " + cmd );
			} catch (IOException e) {
				e.printStackTrace();
				running=false;
			}
		}
		System.out.println("Client Finished");
		Util.tryDoing(()->{
			dos.close();
			dis.close();
		});
	}
	
	private void notifyResponse(String cmd, Object data) {
		Logger.log(" at notifyResponse : " + cmd + ", " + data.toString());
		Logger.log("num of listeners "  + listeners.size());
		listeners.forEach(listner->listner.onDataReceived(cmd, data));
	}
	
	public void sendPublicMSG(String msg) throws IOException {
		protocolMap.get("MSG").write(dos, NICKNAME, msg);
	}

	public void sendPrivateMSG(String msg, String []nickNames) throws IOException {
		protocolMap.get("PRV_MSG").write(dos, NICKNAME, msg, nickNames);
	}
	
	public String getIP() {
		return IP;
	}
	
	public String getNICKNAME() {
		return NICKNAME;
	}
	
	public int getPORT() {
		return PORT;
	}
}
