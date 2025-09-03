package com.isitech.bibliotheque.exceptions;

public class BibliothequeException extends Exception {
    public BibliothequeException(String message) {
        super(message);
    }
    
    public BibliothequeException(String message, Throwable cause) {
        super(message, cause);
    }
}
