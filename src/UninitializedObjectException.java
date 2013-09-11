/**
 * UninitializedObjectException is used to throw an Exception when information is
 * requested from a MazeCell or MazeRoute that is currently invalid. This class of
 * Exception helps to prevent illegal calls to methods that would not provide any
 * valuable information without a valid object.
 * 
 * @author Derrick Tilsner dtt13
 *
 */
public class UninitializedObjectException extends RuntimeException {
	
	/**
	 * Basic constructor for the UninitializedObjectException class
	 * that allows the user to throw an Exception.
	 */
	public UninitializedObjectException() {
		super();
	}
	
	/**
	 * Constructor for the UninitializedObjectException class
	 * that allows the user to throw an Exception and specify
	 * an error message. The cause is uninitialized in this case.
	 * 
	 * @param message - detailed error message
	 */
	public UninitializedObjectException(String message) {
		super(message);
	}
	
	/**
	 * Constructor for the UninitializedObjectException class
	 * that allows the user to throw an Exception and specify
	 * an error message.
	 * 
	 * @param message - detailed error message
	 * @param cause - the cause of the error
	 */
	public UninitializedObjectException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructor for the UninitializedObjectException class
	 * that allows the user to throw an Exception and specify
	 * an error message.
	 * 
	 * @param cause - the cause of the error
	 */
	public UninitializedObjectException(Throwable cause) {
		super(cause);
	}
	
}
