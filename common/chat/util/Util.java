package chat.util;

import java.io.DataInputStream;
import java.io.IOException;

public class Util {

	public static <K> K createInstance ( Class<K> cls) {
		try {
			return cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("fail to create instance", e);
		}
	}

	public static String string(DataInputStream in) {
		try {
			return  in.readUTF();
		} catch (IOException e) {
			throw new RuntimeException("fail to read string: ", e);
		}
	}
}
