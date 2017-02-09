package chat.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.crypto.Data;

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
	
	/**
	 * 
	 * 위의건 String  전용
	 * 
	 * 다양한 파라미터, String 하나
	 * 
	 * 보내는 건 기본적으로 String list 혹은 String
	 * 
	 * 받는 건 맵/배열/ String
	 * 
	 */
	
	protected String readStr(DataInputStream dis) throws IOException {
		return readStrs(dis)[0];
	}
	
	protected String [] readStrs(DataInputStream dis) throws IOException {
		int len = dis.readInt();		
		String [] datas = new String [len];
		for(int i=0;i<len;i++){
			datas[i] = dis.readUTF();
		}
		return datas;
	}

	@Override
	public abstract Object read(DataInputStream dis) throws IOException;
	
	@Override
	public void write(DataOutputStream dos, Object... objs) throws IOException {
		String cmd = getCommand();
		int len = objs.length;
		dos.writeUTF(cmd);
		dos.writeInt(len);
		for(int i = 0 ; i < len; i ++ ) {
			dos.writeUTF(objs[i].toString());
		}
	}
	/*
	protected void writeStr(DataOutputStream dos, String...strs) throws IOException {
		dos.writeUTF(getCommand());
		dos.writeInt(strs.length);
		
		for(String str : strs) dos.writeUTF(str);
		dos.flush();
	}
	*/
}