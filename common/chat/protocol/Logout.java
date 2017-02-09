package chat.protocol;

import java.io.DataInputStream;
import java.io.IOException;

public class Logout extends AbstractProtocol{
	@Override
	public String getCommand() {
		return "LOGOUT";
	}
	
	@Override
	public Object read(DataInputStream dis) throws IOException {
		return readStr(dis);
	}
}
