package chat.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PublicMessage extends AbstractProtocol{

	@Override
	public String getCommand() {
		return "MSG";
	}

	@Override
	public Object read(DataInputStream dis) throws IOException {
		String msg = dis.readUTF(); // xxxx:dkasdkfjksdjfd
		String [] data = msg.split(":");
		return data;
	}
}
