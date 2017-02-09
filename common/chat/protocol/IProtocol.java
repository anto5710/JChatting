package chat.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

public interface IProtocol {

	public String getCommand();
	/**
	 * CMD, 3, AA,BB,CC
	 * 
	 * @param dos
	 * @param objs
	 * @throws IOException
	 */
	public void write( DataOutputStream dos, Object... objs ) throws IOException;
	/**
	 * [AA,BB,CC]
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	public Object read( DataInputStream dis ) throws IOException;
}
