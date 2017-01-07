package chat.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

import util.Util;
/**
 * 채팅 프로토콜 공통 구현체입니다.
 * <ul>
 * <li> 공통 구현체에서는 하나의 문자열을 읽고 쓰는 구현이 들어갑니다.
 * <li> 만일 여러개의 문자열을 읽고 쓰는 프로토콜 구현체에서는 read, write를 overriding해야 합니다.
 * </ul>
 * 
 * <pre>
 * 여기다 치면 
 *   일단은
 *      쓰는 그대로 나옴!1
 *        3#
 * </pre>
 * 
 * ㅇㅇㅇㅌㅌㅌ
 * ㅌㅌㅌㅌㅌ
 * ㅎ아니ㅏ얼
 * 
 * @author anto5710
 *
 */
public abstract class AbstractProtocol implements IProtocol {
	@Override
	public void write(DataOutputStream dos, Object data) throws IOException {
		writeString(dos, (String) data);
	}
	
	@Override
	public Object read(DataInputStream dis) throws IOException {
		return dis.readUTF();
	}
	
	protected void writeString(DataOutputStream dos, String data) throws IOException{
		dos.writeUTF(getCommand());
		dos.writeUTF(data);
		dos.flush();
	}
	
	protected void writeString(DataOutputStream dos, String...datas) throws IOException{
		if(datas.length==1) writeString(dos, datas[0]);
	
		dos.writeUTF(getCommand());
		dos.writeInt(datas.length);
		
		for(String data : datas){
			dos.writeUTF(data);
		}
		dos.flush();
	}
}