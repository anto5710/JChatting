package chat.client.protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import chat.protocol.AbstractProtocol;

public class PrivateMessage extends AbstractProtocol{

	@Override
	public String getCommand() {
		return "PRV_MSG";
	}
	
	@Override
	public void write(DataOutputStream dos, Object...data) throws IOException {
		String msg = (String) data[0];
		String [] receivers = (String[]) data[1];
		String [] datas = new String[1+receivers.length];
		datas[0] = msg;
		System.arraycopy(receivers, 0, datas, 1, receivers.length);
		super.write(dos, datas);
	}
	
//	@Override
//	public Object[] read(DataInputStream dis) throws IOException {
////		Map<String, Object> map = new HashMap<String, Object>();
//		int length = dis.readInt(); // 2
//		Object [] data = new Object[length];
//		String msg = dis.readUTF();
//		
//		for(int i = 0;i < data.length ;i ++ ){
//			data[i] = dis.readUTF();
//		}
////		map.put("msg", msg);
////		map.put("receivers", receivers );
////		return map;
//		
//		// msg : "dkdkdkdkdk"
//		// receivers : [a, c, d]
//		return data;
//	}
}	
