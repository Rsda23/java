package com.isitech.bibliotheque.exceptions;

public class LivreIntrouvableException extends BibliothequeException {
    public LivreIntrouvableException(String isbn) {
        super("Livre avec ISBN " + isbn + " introuvable");
    }
}
