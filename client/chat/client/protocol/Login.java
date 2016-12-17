package chat.client.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import chat.protocol.IProtocol;

public class Login implements IProtocol {

	@Override
	public String getCommand() {
		return "LOGIN";
	}

	@Override
	public void write(DataOutputStream dos, Object data) throws IOException {
		String nickName = (String) data;
		dos.writeUTF("LOGIN");
		dos.writeUTF(nickName);
	}

	@Override
	public Object read(DataInputStream dis) throws IOException {
		String msg = dis.readUTF();
		return msg;
	}

}
