package com.isitech.bibliotheque.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprunt {
    private final Livre livre;
    private final Utilisateur utilisateur;
    private final LocalDate dateEmprunt;
    private final LocalDate dateRetourPrevue;
    private LocalDate  dateRetourReelle;
    
    public Emprunt(Livre livre, Utilisateur utilisateur) {
        this.livre = livre;
        this.utilisateur = utilisateur;
        this.dateEmprunt = LocalDate.now();
        this.dateRetourPrevue = dateEmprunt.plusDays(utilisateur.getDureeEmpruntMax());
    }
    
    public void marquerRetourne() {
        this.dateRetourReelle = LocalDate.now();
    }

    public boolean estEnRetard() {
        LocalDate dateReference = dateRetourReelle != null ? dateRetourReelle : LocalDate.now();
        return dateReference.isAfter(dateRetourPrevue);
    }

    public long joursRetard() {
        if (!estEnRetard()) {
            return 0;
        }
        LocalDate dateReference = dateRetourReelle != null ? dateRetourReelle : LocalDate.now();
        return ChronoUnit.DAYS.between(dateRetourPrevue, dateReference);
    }

    public boolean estTermine() {
        return dateRetourReelle != null;
    }

    public long dureeEmprunt() {
        LocalDate dateFin = dateRetourReelle != null ? dateRetourReelle : LocalDate.now();
        return ChronoUnit.DAYS.between(dateEmprunt, dateFin);
    }

    // Getters
    public Livre getLivre() { return livre; }
    public Utilisateur getUtilisateur() { return utilisateur; }
    public LocalDate getDateEmprunt() { return dateEmprunt; }
    public LocalDate getDateRetourPrevue() { return dateRetourPrevue; }
    public LocalDate getDateRetourReelle() { return dateRetourReelle; }

    @Override
    public String toString() {
        String statut = estTermine() ? "Terminé" : (estEnRetard() ? "En retard" : "En cours");
        return String.format("Emprunt: '%s' par %s (%s - %s) [%s]", 
                livre.getTitre(),
                utilisateur.getNom(),
                dateEmprunt,
                dateRetourPrevue,
                statut);
    }

}
