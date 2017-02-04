package generics;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TTT {

	public static void main(String [] args) {
		Set<String> names = new HashSet<>();
		names.add("XXX");
		names.add("yyy");
		names.add("zzz");
		
		String [] arr = names.toArray(new String[0]);
		
		System.out.println(Arrays.toString(arr)); 
		
	}

}
