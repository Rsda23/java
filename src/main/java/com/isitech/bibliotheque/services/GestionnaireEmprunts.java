package com.isitech.bibliotheque.services;

import com.isitech.bibliotheque.models.*;
import com.isitech.bibliotheque.exceptions.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GestionnaireEmprunts {
    private final Map<String, Utilisateur> utilisateurs;
    private final List<Emprunt> historiqueEmprunts;
    private final BibliothequeService bibliothequeService;
    private final ValidateurService validateur;

    public GestionnaireEmprunts(BibliothequeService bibliothequeService) {
        this.bibliothequeService = bibliothequeService;
        this.utilisateurs = new HashMap<>();
        this.historiqueEmprunts = new ArrayList<>();
        this.validateur = new ValidateurService();
    }
    
    // === GESTION DES UTILISATEURS ===
    public void ajouterUtilisateur(Utilisateur utilisateur) throws UtilisateurInvalideException {
        validateur.validerUtilisateur(utilisateur);

        if (utilisateurs.containsKey(utilisateur.getId())) {
            throw new UtilisateurInvalideException("Utilisateur avec l'ID" + utilisateur.getId() + " existe déjà");
        }

        // Vérifier unicité email
        boolean emailExiste = utilisateurs.values().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(utilisateur.getEmail()));

        if (emailExiste) {
            throw new UtilisateurInvalideException("Un utilisateur avec l'email " + utilisateur.getEmail() + " existe déjà");
        }
        
        utilisateurs.put(utilisateur.getId(), utilisateur);
        System.out.println("✅ Utilisateur ajouté: " + utilisateur.getNom() + " (" + utilisateur.getTypeUtilisateur() + ")");
    }
    
    public Optional<Utilisateur> rechercherUtilisateur(String id) {
        return Optional.ofNullable(utilisateurs.get(id));
    }

    public List<Utilisateur> rechercherUtilisateurParNom(String nom) {
        return utilisateurs.values().stream()
            .filter(u -> u.getNom().toLowerCase().contains(nom.toLowerCase()))
            .sorted(Comparator.comparing(Utilisateur::getNom))
            .collect(Collectors.toList());
    }

    public List<Utilisateur> obtenirTousUtilisateurs() {
        return new ArrayList<>(utilisateurs.values());
    }

    // === GESTION DES EMPRUNTS ===
    public Emprunt emprunterLivre(String isbnLivre, String idUtilisateur) throws BibliothequeException {
        // Récupérer le livre
        Livre livre = bibliothequeService.rechercherParId(isbnLivre)
            .orElseThrow(() -> new LivreIntrouvableException(isbnLivre));

        // Récupérer l'utilisateur
        Utilisateur utilisateur = rechercherUtilisateur(idUtilisateur)
            .orElseThrow(() -> new UtilisateurInvalideException("Utilisateur avec ID " + idUtilisateur + "introuvable"));

        // Effectuer l'emprunt (les validations sont dans Livre.emprunter())
        livre.emprunter(utilisateur);

        // Créer l'enregistrement d'emprunt
        Emprunt emprunt = new Emprunt(livre, utilisateur);
        historiqueEmprunts.add(emprunt);

        System.out.println("📖 Emprunt effectué: " + livre.getTitre() + "par " + utilisateur.getNom());
        return emprunt;
 }

    public Emprunt retournerLivre(String isbnLivre) throws BibliothequeException {
        // Récupérer le livre
        Livre livre = bibliothequeService.rechercherParId(isbnLivre).orElseThrow(() -> new LivreIntrouvableException(isbnLivre));

        if (livre.estDisponible()) {
            throw new EmpruntImpossibleException("Le livre n'est pas emprunté");
        }

        // Trouver l'emprunt actuel
        Emprunt emprunt = historiqueEmprunts.stream()
            .filter(e -> e.getLivre().equals(livre) && e.getDateRetourReelle() == null)
            .findFirst()
            .orElseThrow(() -> new EmpruntImpossibleException("Emprunt introuvable"));

        // Effectuer le retour
        livre.retourner();
        emprunt.marquerRetourne();

        System.out.println("📚 Retour effectué: " + livre.getTitre());

        if (emprunt.estEnRetard()) {
            System.out.println("⚠Retour en retard de " + emprunt.joursRetard() + " jours");
        }

        return emprunt;
    }
    
    // === RECHERCHES ET RAPPORTS ===
    public List<Emprunt> obtenirEmpruntsActuels() {
        return historiqueEmprunts.stream()
            .filter(e -> e.getDateRetourReelle() == null)
            .sorted(Comparator.comparing(Emprunt::getDateEmprunt))
            .collect(Collectors.toList());
    }

    public List<Emprunt> obtenirEmpruntsEnRetard() {
        return historiqueEmprunts.stream()
            .filter(e -> e.getDateRetourReelle() == null && e.estEnRetard())
            .sorted(Comparator.comparing(Emprunt::joursRetard).reversed())
            .collect(Collectors.toList());
    }
    
    public List<Emprunt> obtenirHistoriqueUtilisateur(String idUtilisateur) {
        return historiqueEmprunts.stream()
                .filter(e -> e.getUtilisateur().getId().equals(idUtilisateur))
                .sorted(Comparator.comparing(Emprunt::getDateEmprunt).reversed())
                .collect(Collectors.toList());
    }
    
    public List<Emprunt> obtenirHistoriqueLivre(String isbn) {
        return historiqueEmprunts.stream()
                .filter(e -> e.getLivre().getIsbn().equals(isbn))
                .sorted(Comparator.comparing(Emprunt::getDateEmprunt).reversed())
                .collect(Collectors.toList());
    }
    
    // === STATISTIQUES ===
    public Map<String, Object> obtenirStatistiquesEmprunts() {
        Map<String, Object> stats = new HashMap<>();

        long empruntsActuels = obtenirEmpruntsActuels().size();
        long empruntsEnRetard = obtenirEmpruntsEnRetard().size();
        long totalEmprunts = historiqueEmprunts.size();

        stats.put("Emprunts actuels", empruntsActuels);
        stats.put("Emprunts en retard", empruntsEnRetard);
        stats.put("Total emprunts", totalEmprunts);
        stats.put("Emprunts terminés", totalEmprunts - empruntsActuels);

        // Statistiques par type d'utilisateur
        Map<String, Long> parType = utilisateurs.values().stream()
            .collect(Collectors.groupingBy(Utilisateur::getTypeUtilisateur, Collectors.counting()));
        stats.put("Utilisateurs par type", parType);

        // Livre le plus emprunté
        Map<String, Long> empruntsParLivre = historiqueEmprunts.stream()
            .collect(Collectors.groupingBy(e -> e.getLivre().getTitre(), Collectors.counting()));

        String livePlusEmprunte = empruntsParLivre.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("Aucun");

        stats.put("Livre le plus emprunté", livePlusEmprunte);

        return stats;
    }
    
    public void afficherRapportEmprunts() {
        System.out.println("\n📋 === RAPPORT D'EMPRUNTS ===");

        Map<String, Object> stats = obtenirStatistiquesEmprunts();
        stats.forEach((cle, valeur) -> {
        if (valeur instanceof Map) {
            System.out.println(cle + ":");
            ((Map<?, ?>) valeur).forEach((k, v) ->
            System.out.println(" " + k + ": " + v));
            } else {
            System.out.println(cle + ": " + valeur);
            }
        });

        // Afficher emprunts en retard
        List<Emprunt> enRetard = obtenirEmpruntsEnRetard();
        if (!enRetard.isEmpty()) {
            System.out.println("\n⚠EMPRUNTS EN RETARD:");
            enRetard.forEach(e -> System.out.println(" " + e.getLivre().getTitre() +" par " + e.getUtilisateur().getNom() +" (" + e.joursRetard() + " jours de retard)"));
        }

        System.out.println();
    }
    
    // Getters
    public int getNombreUtilisateurs() { 
        return utilisateurs.size(); 
    }
    public int getNombreEmprunts() { 
        return historiqueEmprunts.size(); 
    }
}
