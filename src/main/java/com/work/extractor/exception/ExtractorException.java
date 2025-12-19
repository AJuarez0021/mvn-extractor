package com.work.extractor.exception;

/**
 * The Class ExtractorException.
 */
public class ExtractorException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2614749875840355488L;

	/**
	 * Instantiates a new extractor exception.
	 *
	 * @param message the message
	 */
	public ExtractorException(String message) {
        super(message);
    }

    /**
     * Instantiates a new extractor exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public ExtractorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new extractor exception.
     *
     * @param cause the cause
     */
    public ExtractorException(Throwable cause) {
        super(cause);
    }
}
