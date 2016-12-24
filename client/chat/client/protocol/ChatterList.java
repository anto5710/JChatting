package chat.client.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import chat.protocol.AbstractProtocol;
import chat.util.Util;

public class ChatterList extends AbstractProtocol{

	@Override
	public String getCommand() {
		return "CHATTER_LIST";
	}
	
	@Override
	public void write(DataOutputStream dos, Object data) throws IOException {
		String[]nickNames = (String[])data;
		int length = nickNames.length;
		System.out.println(
				String.format("[%s] %d %s", 
								getCommand(), length, 
								Arrays.toString(nickNames) ));
		
		dos.writeUTF(getCommand());
		dos.writeInt(length);
		for (int i = 0; i < length; i++) {
			dos.writeUTF(nickNames[i]);
		}
	}
	
	@Override
	public Object read(DataInputStream dis) throws IOException {
		int length = dis.readInt();
		String [] nickNames = new String[length];
		
		for(int i =0;i<length;i++){
			nickNames[i] = dis.readUTF();
		}
		return nickNames;
		/*
		return IntStream.range(0, length)
				         .mapToObj(idx-> Util.string(dis))
		                 .collect(Collectors.toList());
		 */
	}
}
