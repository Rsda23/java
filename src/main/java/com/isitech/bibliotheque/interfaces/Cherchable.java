package com.isitech.bibliotheque.interfaces;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Cherchable<T> {
    /**
    * Recherche par critère exact
    */
    Optional<T> rechercherParId(String id);

    /**
    * Recherche par prédicat personnalisé
    */
    
    List<T> rechercherPar(Predicate<T> critere);

    /**
    * Recherche textuelle (titre, auteur, etc.)
    */
    List<T> rechercherTexte(String texte);

    /**
    * Obtient tous les éléments
    */
    List<T> obtenirTous();

    /**
    * Compte les éléments correspondant au critère
    */
    long compter(Predicate<T> critere);
}
