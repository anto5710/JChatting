package chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import chat.client.ui.ChatFrame;

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
	
	public ServerHandler( Socket server) {
		this.server = server ;
		this.t = new Thread(this);
		t.start();
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
				switch (type) {
				case "MSG":
					
					String msg = convertMessage(dis.readUTF());
					printMessage(msg);
					break;
					
				case "CHATTER_LIST" :
					List<String> chatterList = new ArrayList<>();
					int size = dis.readInt();
					for(int cnt = 0; cnt < size; cnt++){
						chatterList.add(dis.readUTF());
					}
					
					ChatFrame.INSTANCE.updateChatterList(chatterList);
					
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
		if(l==-1) return null; // can't find regex
		
		int lenght = input.length();
		String sender = input.substring(0, l);
		String msg = l<lenght-1? input.substring(l+1, lenght-1):"";
		return String.format("%s: %s\n", sender, msg); 
	}
	
	protected void printMessage(String input) {
		System.out.println(input);
	}

	public void sendNickName(String nickNam) {
		// Lock
		while ( ! running ) {
			try {
				Thread.sleep(100);
				System.out.println("wait");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		sendMessage(nickNam);
	}

	public void sendMessage(String msg) {
		try {
			dos.writeUTF( msg );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
