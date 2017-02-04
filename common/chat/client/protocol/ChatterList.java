package chat.client.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.activation.DataSource;

import util.Logger;
import util.Util;
import chat.protocol.AbstractProtocol;

public class ChatterList extends AbstractProtocol{

	@Override
	public String getCommand() {
		return "CHATTER_LIST";
	}
	
	@Override
	public void write(DataOutputStream dos, Object...data) throws IOException {
		super.write(dos, data);
		System.out.println(
				String.format("[%s] %d %s", 
								getCommand(), data.length, 
								Arrays.toString(data)));
		
	}
	
	@Override
	public Object [] read(DataInputStream dis) throws IOException {
		int length = dis.readInt();
		Logger.log( "num of chatters : " + length );
		String [] nickNames = new String[length];
		
		for(int i =0;i<length;i++){
			nickNames[i] = dis.readUTF();
			Logger.log("logger read " + nickNames[i]);
		}
		Logger.log( "list of chatters " + Arrays.toString(nickNames));
		return nickNames;
	}
}
