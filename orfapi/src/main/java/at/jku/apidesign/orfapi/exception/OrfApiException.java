package at.jku.apidesign.orfapi.exception;

/**
 * Wrapper exception class for exceptions during calling the ORF API
 * 
 * @author marku
 *
 */
public final class OrfApiException extends RuntimeException {

	private static final long serialVersionUID = -8849352003544504468L;
	private static final String orfApiStandardExceptionMessage = "An error orrured while requesting date over the ORF-Api. Maybe the ORF-Site is not available at the moment. Try it again later.";

	public OrfApiException(Throwable throwable) {
		super(orfApiStandardExceptionMessage, throwable);
	}

	public OrfApiException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
