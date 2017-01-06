package chat.util;

@FunctionalInterface
public interface ReturnableAction<T>{
	public abstract T run() throws Exception;
}
