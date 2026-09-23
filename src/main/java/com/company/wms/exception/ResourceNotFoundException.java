package com.company.wms.exception;

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String resource, Object id) {
        super(resource + " not found: " + id);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}