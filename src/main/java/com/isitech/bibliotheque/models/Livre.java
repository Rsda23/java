package com.isitech.bibliotheque.models;

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
    
    public String getTitre() {
        return titre;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public String getAuteur() {
        return auteur;
    }
    
    public boolean getDisponible(){
        return disponible;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
    
    public void setIsbn(String isbn){
        this.isbn = isbn;
    }
    
    public void setAuteur (String auteur){
        this.auteur = auteur;
    }
    
    public void setDisponible(boolean disponible){
        this.disponible = disponible;
    }
    
    @Override
    public String toString() {
        String statut = disponible ? "Disponible" : "Emprunté";
        return String.format("'%s' par %s (ISBN: %s) - %s", titre, auteur, isbn, statut);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Livre livre){
            if (this.isbn == null){
                return false;
            } 
            return this.isbn.equals(livre.getIsbn());
        }
        
        return false;
    }
}
