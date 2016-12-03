package chat.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ClientCleaner implements Runnable{

	private Thread thisThread ;
	
//	private List<ClientHandler> deads = new ArrayList<ClientHandler>();

	private boolean running;
	
	private BlockingQueue<ClientHandler> deads = new ArrayBlockingQueue<>(1000);
	public ClientCleaner () {
		
		thisThread = new Thread ( this );
		thisThread.setName("T-CLEANER");
		thisThread.start();
		
	}
	
	public void registerDeadClient ( ClientHandler dead ) {
		// 'HEATBIT'  'OK'
		this.deads.add ( dead );
	}
	
	@Override
	public void run() {
		running = true;
		while ( running ) {
			
			ClientHandler dead;
			try {
				log ( Thread.currentThread(), "WAITING FOR DEAD CLIENT");
				dead = deads.take();
				log ( Thread.currentThread(), "DEAD CLIENT : " + dead.getNickname());
				ChatMain.unregisterClient(dead);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// here
	}

	private void log(Thread t, String msg) {
		System.out.printf("[%s] %s\n", t.getName(), msg );
		
	}
	
}