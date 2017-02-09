package chat.protocol;

import java.io.DataInputStream;
import java.io.IOException;


public class Login extends AbstractProtocol {
	@Override
	public String getCommand() {
		return "LOGIN";
	}
	
	@Override
	public Object read(DataInputStream dis) throws IOException {
		return readStr(dis);
	}
	
//	public void xxx(DataOutputStream dos, Object data) throws IOException {
//		Map map = data;
//		
//	}
}
