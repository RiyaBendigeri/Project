package org.example.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice// Makes this class handle exceptions across the whole application
public class GlobalExceptionHandler {
//special excaption handler
    @ExceptionHandler(CustomException.ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(CustomException.ValidationException ex) {
        //creates error response for validation errors
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);//status code 400
    }
    // Generic Exception Handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        //handles unhandled exceptions
        ErrorResponse error = new ErrorResponse("An internal server error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value());//general msg
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);//500 error
    }
}
