package com.company.wms.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
		return build(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), request);
	}

	@ExceptionHandler(DuplicateResourceException.class)
	public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex, HttpServletRequest request) {
		return build(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage(), request);
	}

	@ExceptionHandler(BusinessRuleException.class)
	public ResponseEntity<ErrorResponse> handleBusiness(BusinessRuleException ex, HttpServletRequest request) {
		return build(HttpStatus.UNPROCESSABLE_ENTITY, "BUSINESS_RULE_VIOLATION", ex.getMessage(), request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
			HttpServletRequest request) {
		List<String> details = ex.getBindingResult().getFieldErrors().stream()
				.map(f -> f.getField() + ": " + f.getDefaultMessage()).toList();
		return ResponseEntity.badRequest()
				.body(ErrorResponse.of("VALIDATION_ERROR", "Validation failed", request.getRequestURI(), details));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
		log.error("Unhandled exception", ex);
		return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "Unexpected error", request);
	}

	private ResponseEntity<ErrorResponse> build(HttpStatus status, String code, String message,
			HttpServletRequest request) {
		return ResponseEntity.status(status).body(ErrorResponse.of(code, message, request.getRequestURI(), List.of()));
	}
	@ExceptionHandler(InboundServiceUnavailableException.class)
	public ResponseEntity<ErrorResponse> handleInboundUnavailable(
	        InboundServiceUnavailableException ex, HttpServletRequest request) {
	    return build(HttpStatus.SERVICE_UNAVAILABLE, "INBOUND_UNAVAILABLE",
	            ex.getMessage(), request);
	}
}