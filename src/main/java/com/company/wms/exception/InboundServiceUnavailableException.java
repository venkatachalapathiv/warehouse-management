package com.company.wms.exception;

public class InboundServiceUnavailableException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InboundServiceUnavailableException(String message) {
        super(message);
    }

    public InboundServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}