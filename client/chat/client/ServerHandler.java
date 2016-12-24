package chat.client;

import static chat.client.ui.ChatFrame.INSTANCE;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chat.client.ui.ChatFrame;
import chat.protocol.IProtocol;
import chat.protocol.Login;
import chat.protocol.PublicMessage;
import chat.protocol.UnknownCommandException;
import chat.util.Util;

/**
 * 서버에서 전달되는 메시지를 처리합니다.
 * @author anto5710
 *
 */
public class ServerHandler implements Runnable{

	private Thread t;
	
	DataInputStream dis ;
	DataOutputStream dos ;
	private Socket server;
	private boolean running = false;
	
	private List<ServerDataListener> listeners = new ArrayList<>();
	
	public void addListener ( ServerDataListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener ( ServerDataListener listener) {
		this.listeners.remove(listener);
	}
	
	private Map<String, IProtocol> protocolMap =new HashMap<String, IProtocol>();
	{
		registerProtocols(PublicMessage.class, Login.class);
	}
	
	private void registerProtocols(Class <? extends IProtocol>...classes) {
		Arrays.stream(classes).forEach(cls -> {
			IProtocol prt = Util.createInstance(cls);
			protocolMap.put(prt.getCommand(), prt);			
		});
	}
	
	public ServerHandler( Socket server) throws IOException{
		this.server = server ;
		
		dis = new DataInputStream(server.getInputStream());
		dos = new DataOutputStream(server.getOutputStream());
		
		t = new Thread(this);
		t.start();
	}
	
	
	@Override
	public void run() {
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
				running = false;
			}
		}
		System.out.println("client finished");
	}

	private void notifyResponse(String cmd, Object data) {
		for ( ServerDataListener listener : listeners ) {
			listener.onDataReceived(cmd, data);
		}
	}

	
	
	public void sendData(String cmd, String data){
		try {
			dos.writeUTF(cmd);
			dos.writeUTF(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendLogin(String nickNam) {
		// Lock
		while ( ! running ) {
			try {
				Thread.sleep(100);
				System.out.println("wait");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		sendData("LOGIN", nickNam);
	}

	public void sendMessage(String msg) {
		sendData("MSG", msg);
	}
	
	/*
	public void sendMessageTo ( String msg, List<String> nicknames ) {
		// "MSG_PARTIAL"   "xxdslsadkfj;asdlkfjsda;lfkjd;lkdj" 3 "AA"  "BB" "xx"
		dos.writeUTF("MSG_PARTIAL");
		dos.writeUTF( msg );
		for (String receiver : nicknames) {
			dos.writeUTF(receiver);
		}
	}
	*/
}
