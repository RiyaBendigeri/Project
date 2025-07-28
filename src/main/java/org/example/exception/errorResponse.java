package org.example.exception;
/**
 * Represents a standardized error response containing
 * an error message and an HTTP status code.
 *
 * This class can be used to send error details in API responses.
 */
public class errorResponse {
    /** The error message describing what went wrong. */
    private String message;
    /** The HTTP status code associated with the error. */
    private int status;
    /**
     * Constructs an errorResponse with the specified message and status code.
     *
     * @param message the error message
     * @param status the HTTP status code
     */
    public errorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }
    /**
     * Gets the error message.
     *
     * @return the error message as a String
     */

    public String getMessage() {
        return message;
    }
    /**
     * Gets the HTTP status code.
     *
     * @return the HTTP status code as an int
     */
    public int getStatus() {
        return status;
    }
}