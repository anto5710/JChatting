package util;

import java.io.PrintStream;


public class Logger {

	private static PrintStream out = System.out;
	
	public static void log ( String msg) {
		out.printf("[%s]%s\n", Thread.currentThread().getName() , msg);
	}
}
