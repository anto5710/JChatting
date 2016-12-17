package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
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
	
//	private Semaphore sem = new Semaphore(1);
	
	public ClientHandler ( Socket client ) throws IOException {
		sock = client ;
		//init
		dis = new DataInputStream(sock.getInputStream());
		dos = new DataOutputStream(sock.getOutputStream());
		
		readNickName();
		t = new Thread( this );
		t.start(); 
	}
	
	private void readNickName() {
		try {
			// read nickname
			String cmd = dis.readUTF(); // "LOGIN"
			if( "LOGIN".equals(cmd) ) {
				this.nickName = dis.readUTF();
			} else {
				throw new ChatProtocolException ( "expected LOGIN but " + cmd );
			}
		} catch (IOException e1) {
			throw new RuntimeException("fail to create stream");
		}
	}

	@Override
	public void run() {
		while ( running ) {
			try {
				String cmd = dis.readUTF();
				switch ( cmd) {
				case "LOGOUT" :
					ChatMain.unregisterClient(this);
					running = false;
					break;
				case "MSG" :
					String msg = dis.readUTF(); // blciking
					System.out.printf( "%s: %s\n", nickName, msg);
					ChatMain.broadcastMSG( this.nickName, msg);
					break;
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
		ChatMain.unregisterClient(this);
	}
	
	public void sendData( String cmd, String data) throws IOException{
		// PRIV_MSG AA BB XXXXXXXXXXX
		// MSG XXXXX
		System.out.println(String.format("[%s] %d %s", cmd, 1, data));
		dos.writeUTF(cmd);
		dos.writeInt(1);
		dos.writeUTF(data);
	}
	public void sendPublicMsg ( String msg) throws IOException {
		dos.writeUTF("MSG");
		dos.writeUTF(msg);
		dos.flush();
	}
	
	public void sendData(String cmd, String [] data) throws IOException{
		System.out.println(String.format("[%s] %d %s", cmd, data.length, Arrays.toString(data) ));
		dos.writeUTF(cmd);
		dos.writeInt(data.length);
		for (int i = 0; i < data.length; i++) {
			dos.writeUTF(data[i]);
		}
	}
	
	
	public void sendMessage (String sender, String msg ) throws IOException{
		
		sendPublicMsg( sender+":"+msg);
		System.out.println("sent to " + this.nickName + " => " + sender + ":" + msg);
	}

	public String getNickname() {
		return nickName;
	}
}
