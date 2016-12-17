package chat.client.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import chat.protocol.IProtocol;

public class PublicMessage implements IProtocol{

	@Override
	public String getCommand() {
		return "MSG";
	}

	@Override
	public void write(DataOutputStream dos, Object data) throws IOException {
		String msg = (String) data;
		dos.writeUTF( msg );
		dos.flush();
	}

	@Override
	public Object read(DataInputStream dis) throws IOException {
		String msg = dis.readUTF(); // xxxx:dkasdkfjksdjfd
		String [] data = msg.split(":");
		return data;
	}

	
}
