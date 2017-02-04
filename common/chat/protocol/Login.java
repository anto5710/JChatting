package chat.protocol;

import java.io.DataOutputStream;
import java.io.IOException;

import util.Logger;

public class Login extends AbstractProtocol {
	@Override
	public String getCommand() {
		return "LOGIN";
	}
	
	public void xxx(DataOutputStream dos, Object data) throws IOException {
//		Map map = data;
		
	}
	@Override
	public void write(DataOutputStream dos, Object... datas) throws IOException {
		Logger.log("len) "+datas.length);
//		String [] names= new String[]{ (String)data[0] };
		super.write(dos, (String)datas[0]);
	}
}
