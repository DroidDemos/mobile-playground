package cn.beriru.su;

@SuppressWarnings("serial")
public class ShellOnMainThreadException extends RuntimeException {
	public static final String EXCEPTION_COMMAND = "run command on main thread";
	
	public ShellOnMainThreadException(String message){
		super(message);
	}
	
}
