package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

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
	
	public ClientHandler ( Socket client ) {
		sock = client ;
		t = new Thread( this );
		t.start(); 
	}
	
	@Override
	public void run() {
		try {
			dis = new DataInputStream(sock.getInputStream());
			dos = new DataOutputStream(sock.getOutputStream());
			// read nickname
			String nickNam = dis.readUTF();
			this.nickName = nickNam;
			ChatMain.updateChatterList();
			
		} catch (IOException e1) {
			throw new RuntimeException("fail to create stream");
		}
		while ( running ) {
			try {
				String msg = dis.readUTF(); // blciking
				System.out.printf( "%s: %s\n", nickName, msg);
				ChatMain.broadcast( this.nickName, msg);
			} catch (IOException e) {
				e.printStackTrace();
				running = false;
			} 
		}
		/**
		 * TODO
		 * 
		 * ChatMain.handlers에서 현재 ClientHandler를 제거해야 합니다.
		 * 원래는 아래처럼 제거하기 전에 다른 참여자들에게 로그아웃했다는 메세지를 보내줘야 합니다.
		 */
		ChatMain.handlers.remove(this);
	}
	
	public void sendChatterList(List<String>nicknames){
		try {
			dos.writeUTF("CHATTER_LIST");
			dos.writeInt( nicknames.size());
			for( String nick : nicknames ) {
				dos.writeUTF(nick);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage (String sender, String msg ) {
		try {
			dos.writeUTF("MSG");
			dos.writeUTF( sender + ":" + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getNickname() {
		return nickName;
	}
}
