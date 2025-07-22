package org.example.exception;

public class CustomException {

    // This is a nested static class that extends RuntimeException
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }//used to handle validation based errors
    }
}
