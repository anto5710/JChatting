package chat.client;

public interface ServerDataListener {

	public void onDataReceived ( String type, Object...data);
}
