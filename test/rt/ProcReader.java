package rt;

import java.util.Scanner;

import javax.swing.JTextArea;

public class ProcReader implements Runnable{
	private Thread t;
	
	private Process proc;
	private Scanner sc;
	private String name;
	private JTextArea console;
	
	public ProcReader ( Process proc, String name){
		this(proc, null, name);
	}
	
	public ProcReader( Process proc, JTextArea console, String name) {
		t = new Thread(this, "T-"+name);
		this.proc = proc;
		sc = new Scanner(proc.getInputStream());
		this.name = name;
		this.console = console;
		t.start();
	}
	
	@Override
	public void run() {
		while (proc.isAlive()){
			try {
				String output = sc.nextLine(); // blocking!
				print(output);
			} catch (Exception e) {}
		}
	}
	
	public void print(String str){
		String output = String.format("[%s] %s", name, str);
		if(console==null){
			System.out.println(output);
		}else{
			console.append(output+"\n");
		}
	}
}
