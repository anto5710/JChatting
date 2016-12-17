package chat.client;

import static chat.client.ui.ChatFrame.INSTANCE;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chat.client.protocol.Login;
import chat.client.ui.ChatFrame;
import chat.protocol.IProtocol;

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
	
	private Map<String, IProtocol> protocolMap =new HashMap<String, IProtocol>();
	{
		protocolMap.put("LOGIN", new Login());
//		protocolMap.put("LOGIN", new Login());
//		protocolMap.put("LOGIN", new Login());
//		protocolMap.put("LOGIN", new Login());
//		protocolMap.put("LOGIN", new Login());
//		protocolMap.put("LOGIN", new Login());
	}
	public ServerHandler( Socket server) {
		this.server = server ;
		this.t = new Thread(this);
		t.start();
	}
	
	public void addListener ( ServerDataListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener ( ServerDataListener listener) {
		this.listeners.remove(listener);
	}
	
	@Override
	public void run() {
		try {
			dis = new DataInputStream(server.getInputStream());
			dos = new DataOutputStream(server.getOutputStream());
			running = true;
		} catch (IOException e) {
			throw new RuntimeException("failed to create stream");
		}
		
		while ( running ) {
			try {
				String type = dis.readUTF();
				/* TODO 이와 같이 프로토콜 해석 구현체를 따로 분리해서 사용합니다. */
				IProtocol protocol = protocolMap.get(type);
				if ( protocol == null ) {
					;
				} else {
					Object data = protocol.read(dis); // String []
					for ( ServerDataListener listener : listeners ) {
						listener.onDataReceived(type, data);
					}
				}
				
				
				
				switch (type) {
				case "MSG":
//					int sz = dis.readInt();
					String msg;
					msg = dis.readUTF();
					INSTANCE.printMessage(convertMessage(msg));
					break;
					
				case "CHATTER_LIST" :
					// [CHATTER_LIST] AA,BB,CC
					List<String> chatterList = new ArrayList<>();
					int size = dis.readInt();
					
					for(int cnt = 0; cnt < size; cnt++){
						chatterList.add(dis.readUTF());
					}
					INSTANCE.updateChatterList(chatterList);
					break;
					
				case "LOGIN" :
					dis.readInt();
					String nicknam = dis.readUTF();
					INSTANCE.addNickName(nicknam);
					INSTANCE.printMessage(nicknam+" login");
					break;
					
				case "LOGOUT" :
					dis.readInt();
					nicknam = dis.readUTF();
					INSTANCE.removeNickName(nicknam);
					INSTANCE.printMessage(nicknam+ " logout");
					break;
					
				default:
					break;
				}
				/*
				 *  MSG, 
				 *  CHATER_LIST, 
				 *  LOGOUT, 
				 *  LOGIN
				 */
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
			}
		}
		System.out.println("client finished");
	}

	private String convertMessage(String input){
		int l = input.indexOf(":");
		if(l==-1) return ""; // can't find regex
		
		int length = input.length();
		String sender = input.substring(0, l);
		String msg = input.substring(l+1, length);
		return String.format("%s: %s", sender, msg); 
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
