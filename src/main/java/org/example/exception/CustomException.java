package org.example.exception;
/**
 * A container class for custom runtime exceptions used across the application
 * to represent specific error conditions related to validation, resource existence,
 * and duplication.
 */
public class CustomException {
    /**
     * Exception thrown when input validation fails or invalid data is encountered.
     */
    public static class ValidationException extends RuntimeException {
        /**
         * Constructs a new ValidationException with the specified detail message.
         *
         * @param msg the detail message explaining the validation error.
         */
        public ValidationException(String msg) { super(msg); }
    }
    /**
     * Exception thrown when a requested resource (e.g., entity or record) is not found.
     */
    public static class ResourceNotFoundException extends RuntimeException {
        /**
         * Constructs a new ResourceNotFoundException with the specified detail message.
         *
         * @param msg the detail message indicating which resource was not found.
         */
        public ResourceNotFoundException(String msg) { super(msg); }
    }
    /**
     * Exception thrown when attempting to create or update a resource
     * that would violate uniqueness constraints (duplicate resource).
     */
    public static class DuplicateResourceException extends RuntimeException {
        /**
         * Constructs a new DuplicateResourceException with the specified detail message.
         *
         * @param msg the detail message describing the duplication conflict.
         */
        public DuplicateResourceException(String msg) { super(msg); }
    }
}
