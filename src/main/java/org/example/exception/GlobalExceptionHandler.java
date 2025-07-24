
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
public class GlobalExceptionHandler {
    /**
     * Handles ResourceNotFoundException and returns a 404 Not Found response.
     *
     * @param ex the ResourceNotFoundException instance
     * @return a ResponseEntity containing the error details and HTTP status 404
     */

    @ExceptionHandler(CustomException.ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(CustomException.ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    /**
     * Handles ValidationException and returns a 400 Bad Request response.
     *
     * @param ex the ValidationException instance
     * @return a ResponseEntity containing the error details and HTTP status 400
     */
    @ExceptionHandler(CustomException.ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(CustomException.ValidationException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
    /**
     * Handles DuplicateResourceException and returns a 409 Conflict response.
     *
     * @param ex the DuplicateResourceException instance
     * @return a ResponseEntity containing the error details and HTTP status 409
     */
    @ExceptionHandler(CustomException.DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(CustomException.DuplicateResourceException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
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
    public ResponseEntity<ErrorResponse> handleNoHandler(NoHandlerFoundException ex) {
        String message = "No resource found at " + ex.getRequestURL();
        ErrorResponse error = new ErrorResponse(message, 404);
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
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ErrorResponse error = new ErrorResponse("Something went wrong: " + ex.getMessage(), 500);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}