package com.isitech.bibliotheque.interfaces;

import com.isitech.bibliotheque.exceptions.BibliothequeException;

@FunctionalInterface
public interface Validateur<T> {
    /**
    * Valide un objet
    * @param objet à valider
    * @throws BibliothequeException si invalide
    */
    void valider(T objet) throws BibliothequeException;

    /**
    * Combine deux validateurs (ET logique)
    */
    default Validateur<T> et(Validateur<T> autre) {
        return objet -> {
            this.valider(objet);
            autre.valider(objet);
        };
    }

    /**
    * Validateur qui ne fait rien (pattern Null Object)
    */
    static <T> Validateur<T> vide() {
        return objet -> { /* ne fait rien */ };
    }
}
