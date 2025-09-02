package com.isitech.bibliotheque;

import java.util.ArrayList;
import java.util.List;

public class Bibliotheque {
    private List<Livre> livres;
    private String nom;

    public Bibliotheque(String nom) {
        this.nom = nom;
        this.livres = new ArrayList<>();
    }

    // Gestion des livres
    public void ajouterLivre(Livre livre) {
        if (rechercherParISBN(livre.getIsbn()) != null){
            System.out.println("le livre existe déjà");
        } else {
            livres.add(livre);
            System.out.println("livre ajouté");
        }
    }

    public boolean supprimerLivre(String isbn) {
        for (Livre livre : livres){
            if (livre.getIsbn().equals(isbn)){
                livres.remove(livre);
                
                return true;
            }
        }
        
        return false;
    }

    public Livre rechercherParTitre(String titre) {
        for (Livre livre : livres){
            if (livre.getTitre().trim().equalsIgnoreCase(titre.trim())){
                return livre;
            }
        }
        return null;
    }
    
    public Livre rechercherParAuteur(String auteur) {
        for (Livre livre : livres){
            if (livre.getAuteur().trim().equalsIgnoreCase(auteur.trim())){
                return livre;
            }
        }
        return null;
    }

    public Livre rechercherParISBN(String isbn) {
        for (Livre livre : livres){
            if ( livre.getIsbn().equals(isbn)) {
                return livre;
            }
        }
        
        return null;
    }

    public List<Livre> rechercherLivresDisponibles() {
        List<Livre> dispo = new ArrayList<>();
        
        for (Livre livre : livres){
            if (livre.getDisponible()){
                dispo.add(livre);
            }
        }
        
        return dispo;
    }

    // Affichage
    public void afficherCatalogue() {
        System.out.println("\n=== Catalogue de " + nom + " ===");
        if (livres.isEmpty()) {
        System.out.println("Aucun livre dans la bibliothèque.");
        } else {
            for (int i = 0; i < livres.size(); i++) {
            System.out.println((i + 1) + ". " + livres.get(i));
            }
        }
        System.out.println("Total: " + livres.size() + " livres\n");
    }
    
    public void afficherStatistiques() {
        long disponibles = livres.stream().filter(Livre::getDisponible).count();
        long empruntes = livres.size() - disponibles;

        System.out.println("\n=== Statistiques ===");
        System.out.println("Total livres: " + livres.size());
        System.out.println("Disponibles: " + disponibles);
        System.out.println("Empruntés: " + empruntes);
    }
}
