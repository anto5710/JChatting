package protocol;

import java.io.DataInputStream;
import java.io.IOException;

import chat.protocol.AbstractProtocol;

public class PrivateMessage extends AbstractProtocol{

	@Override
	public String getCommand() {
		return "PRV_MSG";
	}
	
	@Override
	public Object[] read(DataInputStream dis) throws IOException {
		String [] datas = (String[]) super.read(dis);
		String sender = datas[0];
		
		String[]names = new String[datas.length-1];
		for(int i = 0; i< datas.length -1; i++ ){
			names[i] = datas[i+1];
		}
		return new Object[]{sender, names};
	}
}
