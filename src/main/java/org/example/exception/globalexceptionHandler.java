
package org.example.exception;

import org.springframework.http.*;
        import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
/**
 * Global exception handler for REST controllers.
 *
 * This class handles specific custom exceptions and general exceptions,
 * providing consistent HTTP responses with meaningful error messages and status codes.
 * It uses @RestControllerAdvice to intercept exceptions thrown by controllers globally.
 */
@RestControllerAdvice
public class globalexceptionHandler {
    /**
     * Handles ResourceNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the ResourceNotFoundException instance
     * @return a ResponseEntity containing the error details and HTTP status 404
     */

    @ExceptionHandler(customException.ResourceNotFoundException.class)
    public ResponseEntity<errorResponse> handleNotFound(customException.ResourceNotFoundException ex) {
        errorResponse error = new errorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    /**
     * Handles ValidationException and returns a 400 Bad Request response.
     *
     * @param ex the ValidationException instance
     * @return a ResponseEntity containing the error details and HTTP status 400
     */
    @ExceptionHandler(customException.ValidationException.class)
    public ResponseEntity<errorResponse> handleValidation(customException.ValidationException ex) {
        errorResponse error = new errorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles DuplicateResourceException and returns a 409 Conflict response.
     *
     * @param ex the DuplicateResourceException instance
     * @return a ResponseEntity containing the error details and HTTP status 409
     */
    @ExceptionHandler(customException.DuplicateResourceException.class)
    public ResponseEntity<errorResponse> handleDuplicate(customException.DuplicateResourceException ex) {
        errorResponse error = new errorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handles NoHandlerFoundException for requests with no matching handler
     * and returns a 404 Not Found response with the requested URL info.
     *
     * @param ex the NoHandlerFoundException instance
     * @return a ResponseEntity containing the error details and HTTP status 404
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<errorResponse> handleNoHandler(NoHandlerFoundException ex) {
        String message = "No resource found at " + ex.getRequestURL();
        errorResponse error = new errorResponse(message, 404);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    /**
     * Catch-all handler for any other uncaught exceptions.
     * Returns a 500 Internal Server Error response with a generic message.
     *
     * @param ex the Exception instance
     * @return a ResponseEntity containing the error details and HTTP status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<errorResponse> handleGeneric(Exception ex) {
        errorResponse error = new errorResponse("Something went wrong: " + ex.getMessage(), 500);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}