package edu.weeia.ecodom.exceptions;

public class AuthenticationFailedException extends RuntimeException {

    // Default constructor
    public AuthenticationFailedException() {
        super("There was a problem while authenticating request.");
    }

    // Constructor that accepts a custom error message
    public AuthenticationFailedException(String message) {
        super(message);
    }

    // Constructor that accepts a custom error message and a cause
    public AuthenticationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that accepts a cause
    public AuthenticationFailedException(Throwable cause) {
        super(cause);
    }
}

