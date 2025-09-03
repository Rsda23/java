package com.isitech.bibliotheque.services;

import com.isitech.bibliotheque.interfaces.Cherchable;
import com.isitech.bibliotheque.interfaces.Validateur;
import com.isitech.bibliotheque.models.Livre;
import com.isitech.bibliotheque.models.Utilisateur;
import com.isitech.bibliotheque.exceptions.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BibliothequeService implements Cherchable<Livre> {
    private final Map<String, Livre> catalogueISBN; // Recherche rapide par ISBN
    private final Map<String, Set<Livre>> catalogueAuteur; // Index par auteur
    private final Map<String, Set<Livre>> catalogueTitre; // Index par mots du titre
    private final Set<Livre> livresDisponibles; // Cache des disponibles

    private final ValidateurService validateur;
    private final String nomBibliotheque;

    public BibliothequeService(String nomBibliotheque) {
        this.nomBibliotheque = nomBibliotheque;
        this.catalogueISBN = new HashMap<>();
        this.catalogueAuteur = new HashMap<>();
        this.catalogueTitre = new HashMap<>();
        this.livresDisponibles = new HashSet<>();
        this.validateur = new ValidateurService();
    }
    
    // === GESTION DU CATALOGUE ===
    public void ajouterLivre(Livre livre) throws BibliothequeException {
        validateur.validerLivre(livre);

        if (catalogueISBN.containsKey(livre.getIsbn())) {
            throw new DonneesInvalidesException("Un livre avec l'ISBN " + livre.getIsbn() + " existe déjà");
        }

        // Ajout dans les différents index
        catalogueISBN.put(livre.getIsbn(), livre);
        // Index par auteur
        catalogueAuteur.computeIfAbsent(livre.getAuteur().toLowerCase(), k -> new HashSet<>()).add(livre);

        // Index par mots du titre
        String[] mots = livre.getTitre().toLowerCase().split("\\s+");
        for (String mot : mots) {
            if (mot.length() > 2) { // Ignorer les mots trop courts
            catalogueTitre.computeIfAbsent(mot, k -> new HashSet<>()).add(livre);
            }
        }

        if (livre.estDisponible()) {
            livresDisponibles.add(livre);
        }

        System.out.println("✅ Livre ajouté: " + livre.getTitre());
    }

    public boolean supprimerLivre(String isbn) {
        Livre livre = catalogueISBN.remove(isbn);
        if (livre == null) {
        return false;
        }

        // Vérifier qu'il n'est pas emprunté
        if (!livre.estDisponible()) {
            catalogueISBN.put(isbn, livre); // Remettre
            throw new IllegalStateException("Impossible de supprimer un livre emprunté");
        }

        // Supprimer des index
        Set<Livre> livresAuteur = catalogueAuteur.get(livre.getAuteur().toLowerCase());
        if (livresAuteur != null) {
            livresAuteur.remove(livre);
            if (livresAuteur.isEmpty()) {
                catalogueAuteur.remove(livre.getAuteur().toLowerCase());
            }
        }
        
        // Supprimer de l'index titre
        String[] mots = livre.getTitre().toLowerCase().split("\\s+");
        for (String mot : mots) {
            Set<Livre> livresMot = catalogueTitre.get(mot);
            if (livresMot != null) {
                livresMot.remove(livre);
                if (livresMot.isEmpty()) {
                catalogueTitre.remove(mot);
                }
            }
        }
        livresDisponibles.remove(livre);
        
        System.out.println("🗑 Livre supprimé: " + livre.getTitre());
        return true;
    }
    
    // === IMPLÉMENTATION CHERCHABLE ===
    @Override
    public Optional<Livre> rechercherParId(String isbn) {
        return Optional.ofNullable(catalogueISBN.get(isbn));
    }

    @Override
    public List<Livre> rechercherPar(Predicate<Livre> critere) {
        return catalogueISBN.values().stream()
            .filter(critere)
            .sorted() // Utilise Comparable<Livre>
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Livre> rechercherTexte(String texte) {
        if (texte == null || texte.trim().isEmpty()) {
            return new ArrayList<>();
        }

        Set<Livre> resultats = new HashSet<>();
        String texteMinuscule = texte.toLowerCase();

        // Recherche dans les titres (mots-clés)
        String[] mots = texteMinuscule.split("\\s+");
        for (String mot : mots) {
            Set<Livre> livresMot = catalogueTitre.get(mot);
            if (livresMot != null) {
                resultats.addAll(livresMot);
            }
        }

        // Recherche dans les auteurs
        catalogueAuteur.entrySet().stream()
            .filter(entry -> entry.getKey().contains(texteMinuscule))
            .forEach(entry -> resultats.addAll(entry.getValue()));

        // Recherche exacte dans titres et auteurs (fallback)
        catalogueISBN.values().stream()
            .filter(livre -> livre.getTitre().toLowerCase().contains(texteMinuscule) || livre.getAuteur().toLowerCase().contains(texteMinuscule))
            .forEach(resultats::add);

        return resultats.stream()
            .sorted()
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Livre> obtenirTous() {
        return catalogueISBN.values().stream()
            .sorted()
            .collect(Collectors.toList());
    }

    @Override
    public long compter(Predicate<Livre> critere) {
        return catalogueISBN.values().stream()
            .filter(critere)
            .count();
    }

    // === MÉTHODES DE COMMODITÉ ===
    public List<Livre> rechercherParAuteur(String auteur) {
        Set<Livre> livres = catalogueAuteur.get(auteur.toLowerCase());
            return livres != null ? livres.stream().sorted().collect(Collectors.toList()) : new ArrayList<>();
    }

    public List<Livre> obtenirLivresDisponibles() {
        return livresDisponibles.stream()
            .sorted()
            .collect(Collectors.toList());
    }

    public List<Livre> obtenirLivresEmpruntes() {
        return catalogueISBN.values().stream()
            .filter(livre -> !livre.estDisponible())
            .sorted()
            .collect(Collectors.toList());
    }

    public List<Livre> obtenirLivresEnRetard() {
        return catalogueISBN.values().stream()
            .filter(Livre::estEnRetard)
            .sorted((l1, l2) -> Long.compare(l2.joursRetard(), l1.joursRetard())) // Plus en retard en premier
            .collect(Collectors.toList());
    }
    
    // === STATISTIQUES ===
    public Map<String, Long> obtenirStatistiques() {
        Map<String, Long> stats = new HashMap<>();

        stats.put("Total livres", (long) catalogueISBN.size());
        stats.put("Livres disponibles", (long) livresDisponibles.size());
        stats.put("Livres empruntés", compter(livre -> !livre.estDisponible()));
        stats.put("Livres en retard", compter(Livre::estEnRetard));
        stats.put("Auteurs différents", (long) catalogueAuteur.size());

        return stats;
    }
    
    public void afficherStatistiques() {
        System.out.println("\n📊 === STATISTIQUES " + nomBibliotheque.toUpperCase() + " ===");
        Map<String, Long> stats = obtenirStatistiques();
        stats.forEach((cle, valeur) ->
        System.out.println(cle + ": " + valeur));
        System.out.println();
    }
    
    // Getters
    public String getNomBibliotheque() { return nomBibliotheque; }
    public int getTaileCatalogue() { return catalogueISBN.size(); }
}
