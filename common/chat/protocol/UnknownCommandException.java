package chat.protocol;

public class UnknownCommandException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3373548266296728536L;

	public UnknownCommandException(String msg) {
		super ( msg );
	}
}
