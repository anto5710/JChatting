package chat.protocol;

import java.io.DataInputStream;

import util.Util;
import util.Util.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
/**
 * c -> s
 * [B, msg, 3, A, B, C]
 * 
 *  s-> 3*c
 *  [sender, msg, 3, A, B, C]
 *  
 *  {
 *     sender : B
 *     msg : 'xxdkasdkfd'
 *     recervers : [A, B, C]
 *  }
 * 
 * @author anto5710
 *
 */
public class PrivateMessage extends AbstractProtocol{

	@Override
	public String getCommand() {
		return "PRV_MSG";
	}
	
	/* msg, nicknames배열
	 */
	
	@Override
	public void write(DataOutputStream dos, Object...data) throws IOException {
		String sender = (String) data[0];
		String msg = (String) data[1];
		String [] receivers = (String[]) data[2];
		
		String [] buf = new String[2+receivers.length];
		buf[0] = sender;
		buf[1] = msg;
		System.arraycopy(receivers, 0, buf, 2, receivers.length); // buf의 
		// FIXME writeStr 걷어내는 중! 
		super.write(dos, buf);
	}
	
	@Override
	public Object read(DataInputStream dis) throws IOException {
		String[]strs = readStrs(dis);
		String sender = strs[0];
		String msg = strs[1];
		String [] names = Arrays.copyOfRange(strs, 2, strs.length);
		
		return Util.map( "sender,msg,names", sender,msg,names);
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
