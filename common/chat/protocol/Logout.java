package chat.protocol;

public class Logout extends AbstractProtocol{
	@Override
	public String getCommand() {
		return "LOGOUT";
	}
}
