package rt;

import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/*
 * Hooking Thread !!!!
 */
public class Runner {
	static String server = "java -cp bin chat.server.ChatServer";
	static String client = "java -cp bin chat.client.ui.LoginDialog";
	
	static List<Process> children = new ArrayList<Process>();
	static JFrame frame = new JFrame();
	
	public static void main(String[] args) throws IOException {
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				System.out.println("다 죽이자!!!!");
				destoryAll();
			}
		});
		initFrame();
		
		// background에서 계속 돌아갑니다.
		
		createProcess(server, "SERVER");
		createProcess(client, "C1");
		createProcess(client, "C2");
	}
	
	private static void initFrame(){
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 3));
		frame.setSize(800, 500);
		frame.setVisible(true);
	}
	
	public static void destoryAll() {
		children.stream()
		         .forEach(proc-> proc.destroy());
		frame.removeAll();
	}
	
	static void createProcess(String cmd, String name){
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			
			JTextArea console = new JTextArea();
			new ProcReader(proc, console, name);
			
			children.add(proc);
			
			frame.getContentPane().add(new JScrollPane(console));
			frame.revalidate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
