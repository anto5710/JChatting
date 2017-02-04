package chat.server;

public interface CommandHandler {

	public void handleData(ClientHandler client, String cmd, Object [] data);
	
}
