package com.isitech.bibliotheque.interfaces;

import com.isitech.bibliotheque.models.Utilisateur;
import com.isitech.bibliotheque.exceptions.EmpruntImpossibleException;

public interface Empruntable {
    /**
    * Vérifie si l'objet peut être emprunté
    */
    boolean estDisponible();

    /**
    * Emprunte l'objet à un utilisateur
    */
    void emprunter(Utilisateur utilisateur) throws EmpruntImpossibleException;

    /**
    * Retourne l'objet emprunté
    */
    void retourner();

    /**
    * Obtient l'utilisateur actuel (null si disponible)
    */
    Utilisateur getEmprunteur();

    /**
    * Obtient la date d'emprunt (null si disponible)
    */
    java.time.LocalDate getDateEmprunt();

    /**
    * Calcule la date de retour prévue
    */
    java.time.LocalDate getDateRetourPrevue();
}
