package com.sellics.recruiting.amazonautocompleteapi.service.amazon.exception;

/**
 * Specific exception to handle parsing exceptions
 * 
 * @author Mikail
 *
 */
public class AmazonResponseParserException extends Exception {
	private static final long serialVersionUID = 48811036456670540L;

	public AmazonResponseParserException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
