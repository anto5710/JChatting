package chat.client.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chat.protocol.AbstractProtocol;

public class PrivateMessage extends AbstractProtocol{

	@Override
	public String getCommand() {
		return "PRV_MSG";
	}
	
	@Override
	public void write(DataOutputStream dos, Object data) throws IOException {
		String [] datas = (String[]) data;
		super.writeString(dos, datas);
	}
	
	@Override
	public Object read(DataInputStream dis) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		int length = dis.readInt()-1;
		String msg = dis.readUTF();
		
		List<String> receivers = new ArrayList<String>();
		for(;length>=0;length--){
			receivers.add(dis.readUTF());
		}
		map.put("msg", msg);
		map.put("receivers", receivers );
		return map;
		// msg : "dkdkdkdkdk"
		// receivers : [a, c, d]
	}
}	
