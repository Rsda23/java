package com.isitech.bibliotheque.models;

public class Professeur extends Utilisateur {
    private String departement;
    private boolean accesRessourcesSpeciales;

    public Professeur(String nom, String email, String departement) {
        super(nom, email);
        this.departement = departement;
        this.accesRessourcesSpeciales = true;
        this.maxEmprunts = 10; // Plus d'emprunts pour les profs
    }

    @Override
    public int getDureeEmpruntMax() {
        return 30; // 1 mois pour les professeurs
    }

    @Override
    public boolean peutEmprunterType(Livre livre) {
        return true; // Accès à tous les types
    }

    @Override
    public String getTypeUtilisateur() {
        return "Professeur";
    }
    
    // Getters/Setters
    public String getDepartement() { return departement; }
    public boolean hasAccesRessourcesSpeciales() { 
        return accesRessourcesSpeciales;
    } 
}
