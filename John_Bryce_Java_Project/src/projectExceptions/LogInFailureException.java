package projectExceptions;

/**
 * This class is for creating customized exception to be thrown when the data to
 * be changed is does not exists.
 * 
 * @author Ehud Perlman
 * @version 1.0 February 20, 2017.
 */
public class LogInFailureException extends Exception {
	
	/**
	 * constructs the exception and calling the super on the given parameter.
	 * 
	 * @param message
	 *            - the message to show when the exception is thrown.
	 */
	public LogInFailureException(String message) {

		super(message);
	}

}

