package chat.protocol;

public class Login extends AbstractProtocol {

	@Override
	public String getCommand() {
		return "LOGIN";
	}
}
