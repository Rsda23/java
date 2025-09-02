package com.isitech.bibliotheque;

public class Livre {
    private String titre;
    private String auteur;
    private String isbn;
    private boolean disponible;

    // Constructeurs
    public Livre() {
        this.disponible = true;
    }

    public Livre(String titre, String auteur, String isbn) {
        this.titre = titre;
        this.auteur = auteur;
        this.isbn = isbn;
        this.disponible = true;
    }

    // Getters et Setters
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    @Override
    public String toString() {
        String statut = disponible ? "Disponible" : "Emprunté";
        return String.format("'%s' par %s (ISBN: %s) - %s", titre, auteur, isbn, statut);
    }
    
    @Override
    public boolean equals(Object obj) {
    // TODO: Implémenter la comparaison par ISBN
    return false;
    }
}
