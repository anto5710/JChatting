package chat.protocol;

import java.io.DataInputStream;
import java.io.IOException;


public class PublicMessage extends AbstractProtocol{
	@Override
	public String getCommand() {
		return "MSG";
	}
	
	@Override
	public Object read(DataInputStream dis) throws IOException {
		String [] arr = readStrs(dis);
		return util.Util.map("sender,msg", arr);
	}
}
