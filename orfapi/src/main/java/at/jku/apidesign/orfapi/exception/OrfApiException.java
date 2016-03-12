package at.jku.apidesign.orfapi.exception;

/**
 * Wrapper exception class for exceptions during calling the ORF API
 * 
 * @author marku
 *
 */
public class OrfApiException extends RuntimeException {

	private static final long serialVersionUID = -8849352003544504468L;

	public OrfApiException(Throwable throwable) {
		super(throwable);
	}

	public OrfApiException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	
}
