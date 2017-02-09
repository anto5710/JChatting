package rt;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ProcDemo {
	/*
	 * parent process 라고 합니다.
	 */
	static String cmd = "java -cp bin rt.SubProc";
	static String cmdServer = "java -cp bin chat.server.ChatServer";
	public static void main(String[] args) {
		
		Runtime r = Runtime.getRuntime();
		// nnw Runtime
		try {
			/*
			 * program - instruction(명령어) 들의모음
			 * process - 실행중인 program
			 * 
			 */
			Process proc = r.exec(cmdServer); // spawning
			InputStream in = proc.getInputStream(); // 자식 프로세스에서 write한 결과를 읽어들입니다.
			Scanner sc = new Scanner ( in );
			while ( true ) {
				try {
					String s = sc.nextLine();
					System.out.println("[PARENT] " + s);					
				} catch ( Exception e ) {
					break;
				}
			}
			System.out.println("end");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
