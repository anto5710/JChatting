package chat.protocol;


public class PublicMessage extends AbstractProtocol{
	@Override
	public String getCommand() {
		return "MSG";
	}
}
