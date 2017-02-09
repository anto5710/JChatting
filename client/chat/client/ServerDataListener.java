package chat.client;

public interface ServerDataListener {
	public void onDataReceived(String cmd, Object data);
}
