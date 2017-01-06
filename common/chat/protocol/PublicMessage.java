package chat.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import chat.util.Util;

public class PublicMessage extends AbstractProtocol{
	@Override
	public String getCommand() {
		return "MSG";
	}

	@Override
	public Object read(DataInputStream dis) {
		String msg = Util.tryToDo(()->{return dis.readUTF();}); // xxxx:dkasdkfjksdjfd
		String [] data = msg.split(":");
		return data;
	}
}
