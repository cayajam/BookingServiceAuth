package pw.io.booker.exception;

public class CustomException extends RuntimeException{

	private String errorMessage;

	public CustomException(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
	
}
