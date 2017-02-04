package chat.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface IProtocol {

	public String getCommand();
	
	public void write ( DataOutputStream dos, Object...data ) throws IOException;
	
	public Object[] read ( DataInputStream dis ) throws IOException;
}
