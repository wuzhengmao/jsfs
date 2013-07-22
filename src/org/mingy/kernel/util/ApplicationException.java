package org.mingy.kernel.util;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1138369345143405602L;

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getLocalizedMessage() {
		String message = Langs.getText(super.getMessage());
		return message != null ? message : super.getLocalizedMessage();
	}
}
