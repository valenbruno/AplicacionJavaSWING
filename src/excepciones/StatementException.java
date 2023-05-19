package excepciones;

public class StatementException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StatementException(String message, Throwable cause) {
		super(message, cause);
	}

	public StatementException(String message) {
		super(message);
	}

	public StatementException(Throwable cause) {
		super(cause);
	}
	
	

}
