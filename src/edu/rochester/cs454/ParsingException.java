package edu.rochester.cs454;

/**
 * The Class ParsingException. This exception is thrown if there is any parsing error in the input string
 */
public class ParsingException extends Exception {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new parsing exception.
	 *
	 * @param message the message
	 */
	public ParsingException(String message) {
		super(message);
	}
	
	/**
	 * Instantiates a new parsing exception.
	 *
	 * @param message the message
	 * @param t the t
	 */
	public ParsingException(String message, Throwable t) {
		super(message);
	}
}