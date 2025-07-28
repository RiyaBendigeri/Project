
package org.example.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.converter.HttpMessageNotReadableException;

/**
 * Global exception handler for REST controllers.
 *
 * This class handles specific custom exceptions and general exceptions,
 * providing consistent HTTP responses with meaningful error messages and status codes.
 * It uses @RestControllerAdvice to intercept exceptions thrown by controllers globally.
 */
@RestControllerAdvice
public class globalexceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<errorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        errorResponse error = new errorResponse(errorMsg, 400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
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
     * Handles deserialization (JSON parsing) errors for HTTP requests with a body,
     * specifically for PATCH/POST/PUT requests that convert JSON to DTOs.
     * <p>
     * - Returns 400 Bad Request if:
     *   - The request body is missing or empty.
     *   - The request contains an unknown/extra property which is not allowed (with strict Jackson mapping).
     * - Returns 500 Internal Server Error for any other unexpected JSON parse errors.
     * @param ex The {@link HttpMessageNotReadableException} thrown by Spring during JSON parsing.
     * @return A {@link ResponseEntity} containing a user-friendly error message and HTTP status.
     */

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<errorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        // Check for missing/empty body
        String msg = ex.getMessage();

        if (msg != null && (msg.contains("Required request body is missing") || msg.contains("No content to map"))) {
            return ResponseEntity.badRequest()
                    .body(new errorResponse("Request body is missing or empty", 400));
        }
        Throwable cause = ex.getCause();
        if (cause instanceof com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException) {
            String property = ((com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException) cause).getPropertyName();
            String message = "Invalid request. Property '" + property + "' is not allowed.";
            return ResponseEntity.badRequest().body(new errorResponse(message, 400));
        }
        // Fallback for other malformed JSON
        else{
            errorResponse error = new errorResponse("Something went wrong: " + ex.getMessage(), 500);
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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