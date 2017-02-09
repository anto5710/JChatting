package util;

import java.io.PrintStream;
import java.util.Arrays;


public class Logger {

	private static PrintStream out = System.out;
	
	public static void log ( String msg) {
		out.printf("[%s]%s\n", Thread.currentThread().getName() , msg);
	}
	
	public static void logR(String cmd, Object ... data ) {
		Thread t = Thread.currentThread();
		out.printf("[%s][ READ] CMD:%s, %d %s\n", t.getName(), cmd, data.length, Arrays.toString(data));
	}
	public static void logW(String cmd, Object ... data ) {
		Thread t = Thread.currentThread();
		out.printf("[%s][WRITE] CMD:%s, %d %s\n", t.getName(), cmd, data.length, Arrays.toString(data));
	}
}
