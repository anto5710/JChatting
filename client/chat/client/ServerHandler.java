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

import chat.protocol.IProtocol;
import chat.protocol.Login;
import chat.protocol.Logout;
import chat.protocol.PublicMessage;
import chat.protocol.UnknownCommandException;
import chat.util.Util;

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
	
	private boolean running = false;
	
	private final Socket SERVER;
	private final String IP, NICKNAME;
	private final int PORT;
	
	private List<ServerDataListener> listeners = new ArrayList<>();
	
	public void addListener ( ServerDataListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener ( ServerDataListener listener) {
		this.listeners.remove(listener);
	}

	private Map<String, IProtocol> protocolMap =new HashMap<String, IProtocol>();
	{
		registerProtocols(PublicMessage.class, Login.class, Logout.class);
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
		sendLogin(nickName);
		
		t = new Thread(this);
		t.start();
	}
	
	private void sendLogin(String nickName) throws IOException {
		protocolMap.get("LOGIN").write(dos, nickName);
	}
	
	@Override
	public void run(){
		while ( running ) {
			try {
				String cmd = dis.readUTF();
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
		listeners.forEach(listner->listner.onDataReceived(cmd, data));
	}
	
	public void sendMessage(String msg) throws IOException {
		protocolMap.get("MSG").write(dos, msg);
	}
	
	public void sendLogout() throws IOException{ 
		protocolMap.get("LOGOUT").write(dos, "");
	}
}
