package com.isitech.bibliotheque;

import com.isitech.bibliotheque.models.*;
import com.isitech.bibliotheque.services.*;
import com.isitech.bibliotheque.exceptions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final BibliothequeService bibliotheque = new BibliothequeService("Bibliothèque ISITECH");
    private static final GestionnaireEmprunts gestionnaireEmprunts = new GestionnaireEmprunts(bibliotheque);
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🏛 Bienvenue dans le Système de Gestion de Bibliothèque POO");
        System.out.println("Version 2.0 - Architecture Orientée Objet\n");

        // Initialisation avec des données de test
        initialiserDonneesTest();

        // Boucle principale
        boolean continuer = true;
        while (continuer) {
            afficherMenuPrincipal();
            int choix = lireChoix();
            continuer = traiterChoixPrincipal(choix);
        }

        System.out.println("👋 Merci d'avoir utilisé notre système !");
        scanner.close();
    }
    
    private static void initialiserDonneesTest() {
        try {
            System.out.println("📚 Initialisation des données de test...");

            // Ajout de livres
            bibliotheque.ajouterLivre(new Livre("9780132350884", "Clean Code", "Robert Martin", 464, "Prentice Hall", LocalDate.of(2008, 8, 1)));
            bibliotheque.ajouterLivre(new Livre("9780201633612", "Design Patterns", "Gang of Four", 395, "Addison-Wesley", LocalDate.of(1994, 10, 21)));
            bibliotheque.ajouterLivre(new Livre("9780134685991", "Effective Java", "Joshua Bloch", 412, "Addison-Wesley", LocalDate.of(2017, 12, 27)));
            bibliotheque.ajouterLivre(new Livre("9781617294945", "Java 8 in Action", "Raoul-Gabriel Urma", 424, "Manning", LocalDate.of(2014, 8, 28)));
            bibliotheque.ajouterLivre(new Livre("9780596009205", "Head First Java", "Kathy Sierra", 688, "O'Reilly", LocalDate.of(2005, 2, 9)));
   
            // Ajout d'utilisateurs
            gestionnaireEmprunts.ajouterUtilisateur(new Etudiant("Alice Martin", "alice.martin@etu.isitech.fr", "E2023001", 4, "Informatique"));
            gestionnaireEmprunts.ajouterUtilisateur(new Etudiant("Bob Dupont", "bob.dupont@etu.isitech.fr","E2023002", 2, "Informatique"));
            gestionnaireEmprunts.ajouterUtilisateur(new Professeur("Dr. Claire Bernard","claire.bernard@isitech.fr", "Informatique"));
            gestionnaireEmprunts.ajouterUtilisateur(new Personnel("Jean Admin", "jean.admin@isitech.fr","Administration", "Bibliothécaire"));

            // Quelques emprunts de test
            gestionnaireEmprunts.emprunterLivre("9780132350884", "E2023001"); // Alice emprunte Clean Code
            gestionnaireEmprunts.emprunterLivre("9780201633612", "claire.bernard@isitech.fr"); // Prof emprunte Design Patterns

            System.out.println("✅ Données de test chargées\n");

            } catch (BibliothequeException e) {
                System.err.println("❌ Erreur lors de l'initialisation: " + e.getMessage());
            }
    }
    
    private static void afficherMenuPrincipal() {
        System.out.println("📖 === MENU PRINCIPAL ===");
        System.out.println("1. 📚 Gestion du catalogue");
        System.out.println("2. 👥 Gestion des utilisateurs");
        System.out.println("3. 🔄 Gestion des emprunts");
        System.out.println("4. 🔍 Recherches avancées");
        System.out.println("5. 📊 Rapports et statistiques");
        System.out.println("0. 🚪 Quitter");
        System.out.print("Votre choix: ");
    }

    private static boolean traiterChoixPrincipal(int choix) {
        scanner.nextLine(); // Nettoyer l
        switch (choix) {
            case 1 -> menuGestionCatalogue();
            case 2 -> menuGestionUtilisateurs();
            case 3 -> menuGestionEmprunts();
            case 4 -> menuRecherchesAvancees();
            case 5 -> menuRapportsStatistiques();
            case 0 -> {
                return false;
            }
            default -> System.out.println("❌ Choix invalide!");
        }
        return true;
    }
    
    // === MENU GESTION CATALOGUE ===
    private static void menuGestionCatalogue() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n📚 === GESTION DU CATALOGUE ===");
            System.out.println("1. Afficher le catalogue complet");
            System.out.println("2. Afficher les livres disponibles");
            System.out.println("3. Ajouter un livre");
            System.out.println("4. Supprimer un livre");
            System.out.println("5. Rechercher un livre");
            System.out.println("0. Retour au menu principal");
            System.out.print("Votre choix: ");

            int choix = lireChoix();
            scanner.nextLine();

            switch (choix) {
                case 1 -> afficherCatalogueComplet();
                case 2 -> afficherLivresDisponibles();
                case 3 -> ajouterNouveauLivre();
                case 4 -> supprimerLivre();
                case 5 -> rechercherLivreSimple();
                case 0 -> retour = true;
                default -> System.out.println("❌ Choix invalide!");
            }
        }
    }
    
    private static void afficherCatalogueComplet() {
        System.out.println("\n📖 === CATALOGUE COMPLET ===");
        List<Livre> livres = bibliotheque.obtenirTous();

        if (livres.isEmpty()) {
            System.out.println("📭 Aucun livre dans la bibliothèque.");
        } else {
            for (int i = 0; i < livres.size(); i++) {
                Livre livre = livres.get(i);
                String statut = livre.estDisponible() ? "✅ " : "❌ ";
                System.out.printf("%d. %s %s%n", i + 1, statut, livre);
                if (!livre.estDisponible()) {
                    System.out.printf(" 📅 Emprunté par %s, retour prévu le %s%n", livre.getEmprunteur().getNom(), livre.getDateRetourPrevue());
                }
                if (livre.estEnRetard()) {
                    System.out.printf(" ⚠EN RETARD de %d jours%n", livre.joursRetard());
                }
            }
        }
        bibliotheque.afficherStatistiques();
    }
    
    private static void afficherLivresDisponibles() {
        System.out.println("\n✅ === LIVRES DISPONIBLES ===");
        List<Livre> disponibles = bibliotheque.obtenirLivresDisponibles();

        if (disponibles.isEmpty()) {
            System.out.println("😔 Aucun livre disponible actuellement.");
        } else {
            for (int i = 0; i < disponibles.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, disponibles.get(i));
            }
            System.out.printf("%n✨ %d livres disponibles sur %d%n", disponibles.size(), bibliotheque.getTaileCatalogue());
        }
    }
    
    private static void ajouterNouveauLivre() {
        System.out.println("\n➕ === AJOUT D'UN LIVRE ===");

        try {
            System.out.print("📖 Titre: ");
            String titre = scanner.nextLine().trim();

            System.out.print("✍Auteur: ");
            String auteur = scanner.nextLine().trim();

            System.out.print("🔢 ISBN (13 chiffres): ");
            String isbn = scanner.nextLine().trim();

            System.out.print("📄 Nombre de pages (optionnel): ");
            String pagesStr = scanner.nextLine().trim();
            int pages = pagesStr.isEmpty() ? 0 : Integer.parseInt(pagesStr);

            System.out.print("🏢 Éditeur (optionnel): ");
            String editeur = scanner.nextLine().trim();
            editeur = editeur.isEmpty() ? null : editeur;
            // Création et ajout du livre
            Livre livre = new Livre(isbn, titre, auteur, pages, editeur, null);
            bibliotheque.ajouterLivre(livre);

            System.out.println("🎉 Livre ajouté avec succès!");

            } catch (NumberFormatException e) {
            System.out.println("❌ Nombre de pages invalide!");
            } catch (BibliothequeException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
            } catch (Exception e) {
            System.out.println("❌ Erreur inattendue: " + e.getMessage());
            }
    }
    
    private static void supprimerLivre() {
        System.out.println("\n🗑 === SUPPRESSION D'UN LIVRE ===");
        System.out.print("🔢 ISBN du livre à supprimer: ");
        String isbn = scanner.nextLine().trim();

        try {
            Optional<Livre> livre = bibliotheque.rechercherParId(isbn);
            if (livre.isEmpty()) {
                System.out.println("❌ Livre avec ISBN " + isbn + "introuvable.");
                return;
            }

            System.out.println("📖 Livre trouvé: " + livre.get());

            if (!livre.get().estDisponible()) {
                System.out.println("❌ Impossible de supprimer un livre emprunté.");
                return;
            }

            System.out.print("⚠Confirmer la suppression (o/N): ");
            String confirmation = scanner.nextLine().trim();

            if (confirmation.equalsIgnoreCase("o") || confirmation.equalsIgnoreCase("oui")) {
                boolean supprime = bibliotheque.supprimerLivre(isbn);
                if (supprime) {
                    System.out.println("✅ Livre supprimé avec succès!");
                } else {
                    System.out.println("❌ Erreur lors de la suppression.");
                }
            } else {
                System.out.println("🚫 Suppression annulée.");
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }

    private static void rechercherLivreSimple() {
        System.out.println("\n🔍 === RECHERCHE SIMPLE ===");
        System.out.println("1. Par ISBN");
        System.out.println("2. Par titre ou auteur");
        System.out.print("Type de recherche: ");

        int type = lireChoix();
        scanner.nextLine();

        switch (type) {
            case 1 -> {
                System.out.print("🔢 ISBN: ");
                String isbn = scanner.nextLine().trim();
                Optional<Livre> livre = bibliotheque.rechercherParId(isbn);

                if (livre.isPresent()) {
                    System.out.println("📖 Livre trouvé: " + livre.get());
                } else {
                System.out.println("❌ Aucun livre trouvé avec cet ISBN.");
                }
            }
            case 2 -> {
                System.out.print("🔤 Texte à rechercher: ");
                String texte = scanner.nextLine().trim();
                List<Livre> resultats = bibliotheque.rechercherTexte(texte);

                if (resultats.isEmpty()) {
                    System.out.println("❌ Aucun livre trouvé.");
                } else {
                    System.out.println("📚 " + resultats.size() + "livre(s) trouvé(s):");
                        for (int i = 0; i < resultats.size(); i++) {
                        String statut = resultats.get(i).estDisponible() ? "✅ " : "❌ ";
                            System.out.printf("%d. %s %s%n", i + 1, statut, resultats.get(i));
                        }
                }
            }
            default -> System.out.println("❌ Type de recherche invalide!");
        }
    }
    
    // === MENU GESTION UTILISATEURS ===
    private static void menuGestionUtilisateurs() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n👥 === GESTION DES UTILISATEURS ===");
            System.out.println("1. Afficher tous les utilisateurs");
            System.out.println("2. Ajouter un utilisateur");
            System.out.println("3. Rechercher un utilisateur");
            System.out.println("4. Historique d'un utilisateur");
            System.out.println("0. Retour au menu principal");
            System.out.print("Votre choix: ");

            int choix = lireChoix();
            scanner.nextLine();

            switch (choix) {
                case 1 -> afficherTousUtilisateurs();
                case 2 -> ajouterNouvelUtilisateur();
                case 3 -> rechercherUtilisateur();
                case 4 -> afficherHistoriqueUtilisateur();
                case 0 -> retour = true;
                default -> System.out.println("❌ Choix invalide!");
            }
        }
    }
    
    private static void afficherTousUtilisateurs() {
        System.out.println("\n👥 === TOUS LES UTILISATEURS ===");
        List<Utilisateur> utilisateurs = gestionnaireEmprunts.obtenirTousUtilisateurs();

        if (utilisateurs.isEmpty()) {
            System.out.println("👤 Aucun utilisateur enregistré.");
        } else {
            utilisateurs.stream()
                .sorted((u1, u2) -> u1.getNom().compareToIgnoreCase(u2.getNom()))
                .forEach(u -> System.out.println("🆔 " + u.getId() + "- " + u));
            System.out.printf("%n📊 Total: %d utilisateurs%n",utilisateurs.size());
        }
    }
    
     private static void ajouterNouvelUtilisateur() {
        System.out.println("\n➕ === AJOUT D'UN UTILISATEUR ===");

        try {
            System.out.print("👤 Nom complet: ");
            String nom = scanner.nextLine().trim();

            System.out.print("📧 Email: ");
            String email = scanner.nextLine().trim();

            System.out.println("\n📝 Type d'utilisateur:");
            System.out.println("1. Étudiant");
            System.out.println("2. Professeur");
            System.out.println("3. Personnel");
            System.out.print("Votre choix: ");

            int type = lireChoix();
            scanner.nextLine();

            Utilisateur utilisateur;

            switch (type) {
                case 1 -> {
                    System.out.print("🎓 Numéro étudiant: ");
                    String numero = scanner.nextLine().trim();

                    System.out.print("📚 Niveau (1=L1, 2=L2, 3=L3, 4=M1,5=M2): ");
                    int niveau = lireChoix();
                    scanner.nextLine();

                    System.out.print("🏫 Filière: ");
                    String filiere = scanner.nextLine().trim();

                    utilisateur = new Etudiant(nom, email, numero, niveau, filiere);
                }
                case 2 -> {
                    System.out.print("🏢 Département: ");
                    String departement = scanner.nextLine().trim();

                    utilisateur = new Professeur(nom, email, departement);
                }
                case 3 -> {
                    System.out.print("🏢 Service: ");
                    String service = scanner.nextLine().trim();

                    System.out.print("💼 Poste: ");
                    String poste = scanner.nextLine().trim();

                    utilisateur = new Personnel(nom, email, service, poste);
                }
                default -> {
                    System.out.println("❌ Type d'utilisateur invalide!");
                    return;
                }
            }
            
            gestionnaireEmprunts.ajouterUtilisateur(utilisateur);
            System.out.println("🎉 Utilisateur ajouté avec succès!");
            System.out.println("🆔 ID attribué: " + utilisateur.getId());
        } catch (UtilisateurInvalideException e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Erreur inattendue: " + e.getMessage());
        }
    }
    
    private static void rechercherUtilisateur() {
        System.out.println("\n🔍 === RECHERCHE D'UTILISATEUR ===");
        System.out.println("1. Par ID");
        System.out.println("2. Par nom");
        System.out.print("Type de recherche: ");

        int type = lireChoix();
        scanner.nextLine();

        switch (type) {
            case 1 -> {
                System.out.print("🆔 ID utilisateur: ");
                String id = scanner.nextLine().trim();
                Optional<Utilisateur> utilisateur = gestionnaireEmprunts.rechercherUtilisateur(id);

                if (utilisateur.isPresent()) {
                    System.out.println("👤 Utilisateur trouvé: " + utilisateur.get());
                } else {
                    System.out.println("❌ Aucun utilisateur trouvé avec cet ID.");
                }
            }
            case 2 -> {
                System.out.print("👤 Nom à rechercher: ");
                String nom = scanner.nextLine().trim();
                List<Utilisateur> resultats = gestionnaireEmprunts.rechercherUtilisateurParNom(nom);

                if (resultats.isEmpty()) {
                    System.out.println("❌ Aucun utilisateur trouvé.");
                } else {
                System.out.println("👥 " + resultats.size() + "utilisateur(s) trouvé(s):");
                resultats.forEach(u -> System.out.println("🆔 " + u.getId() + " - " + u));
                }
            }
            default -> System.out.println("❌ Type de recherche invalide!");
        }
        
    }
    
    private static void afficherHistoriqueUtilisateur() {
        System.out.print("🆔 ID de l'utilisateur: ");
        String id = scanner.nextLine().trim();

        Optional<Utilisateur> utilisateur = gestionnaireEmprunts.rechercherUtilisateur(id);
        if (utilisateur.isEmpty()) {
            System.out.println("❌ Utilisateur introuvable.");
            return;
        }

        System.out.println("👤 Utilisateur: " + utilisateur.get());
        List<Emprunt> historique = gestionnaireEmprunts.obtenirHistoriqueUtilisateur(id);

        if (historique.isEmpty()) {
            System.out.println("📭 Aucun emprunt dans l'historique.");
        } else {
            System.out.println("📚 Historique des emprunts:");
            historique.forEach(e -> {
                String statut = e.estTermine() ? "✅ " : (e.estEnRetard() ? "⚠ " : "📖 ");
                System.out.println(statut + " " + e);
            });
        }  
    }
     
    // === MENU GESTION EMPRUNTS ===
    private static void menuGestionEmprunts() {
        boolean retour = false;
        while (!retour) {
            System.out.println("\n🔄 === GESTION DES EMPRUNTS ===");
            System.out.println("1. Emprunter un livre");
            System.out.println("2. Retourner un livre");
            System.out.println("3. Afficher emprunts actuels");
            System.out.println("4. Afficher emprunts en retard");
            System.out.println("0. Retour au menu principal");
            System.out.print("Votre choix: ");

            int choix = lireChoix();
            scanner.nextLine();

            switch (choix) {
                case 1 -> emprunterLivre();
                case 2 -> retournerLivre();
                case 3 -> afficherEmpruntsActuels();
                case 4 -> afficherEmpruntsEnRetard();
                case 0 -> retour = true;
                default -> System.out.println("❌ Choix invalide!");
            }
        }
    }
    
    private static void emprunterLivre() {
        System.out.println("\n📖 === EMPRUNT DE LIVRE ===");

        try {
            System.out.print("🔢 ISBN du livre: ");
            String isbn = scanner.nextLine().trim();
            System.out.print("🆔 ID de l'utilisateur: ");
            String idUtilisateur = scanner.nextLine().trim();

            Emprunt emprunt = gestionnaireEmprunts.emprunterLivre(isbn,idUtilisateur);

            System.out.println("✅ Emprunt effectué avec succès!");
            System.out.println("📅 Date de retour prévue: " + emprunt.getDateRetourPrevue());

            } catch (BibliothequeException e) {
                System.out.println("❌ Erreur: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Erreur inattendue: " + e.getMessage());
            }
        
    }
    
    private static void retournerLivre() {
        System.out.println("\n📚 === RETOUR DE LIVRE ===");

        try {
            System.out.print("🔢 ISBN du livre: ");
            String isbn = scanner.nextLine().trim();

            Emprunt emprunt = gestionnaireEmprunts.retournerLivre(isbn);

            System.out.println("✅ Retour effectué avec succès!");
            System.out.println("📅 Durée d'emprunt: " + emprunt.dureeEmprunt() + " jours");

            if (emprunt.estEnRetard()) {
                System.out.println("⚠Retour en retard de " + emprunt.joursRetard() + " jours");
            }

            } catch (BibliothequeException e) {
                System.out.println("❌ Erreur: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ Erreur inattendue: " + e.getMessage());
            }
        
    }
    
    private static void afficherEmpruntsActuels() {
        System.out.println("\n📖 === EMPRUNTS ACTUELS ===");
        List<Emprunt> emprunts = gestionnaireEmprunts.obtenirEmpruntsActuels();

        if (emprunts.isEmpty()) {
            System.out.println("📭 Aucun emprunt en cours.");
        } else {
            emprunts.forEach(e -> {
                String statut = e.estEnRetard() ? "⚠ " : "📖 ";
                System.out.println(statut + " " + e);
            });
            System.out.printf("%n📊 Total: %d emprunts en cours%n",emprunts.size());
        }
    }
    
    private static void afficherEmpruntsEnRetard() {
        System.out.println("\n⚠=== EMPRUNTS EN RETARD ===");
        List<Emprunt> enRetard = gestionnaireEmprunts.obtenirEmpruntsEnRetard();

        if (enRetard.isEmpty()) {
            System.out.println("✅ Aucun emprunt en retard!");
        } else {
            enRetard.forEach(e ->
            System.out.printf("⚠%s (%d jours de retard)%n", e, e.joursRetard()));
            System.out.printf("%n📊 Total: %d emprunts en retard%n", enRetard.size());
        }
    }
    
    // === MENU RECHERCHES AVANCÉES ===

    private static void menuRecherchesAvancees() {
        System.out.println("\n🔍 === RECHERCHES AVANCÉES ===");
        System.out.println("1. Recherche par auteur");
        System.out.println("2. Livres disponibles seulement");
        System.out.println("3. Livres empruntés seulement");
        System.out.println("4. Historique d'un livre");
        System.out.println("5. Recherche multi-critères");
        System.out.print("Votre choix: ");

        int choix = lireChoix();
        scanner.nextLine();

        switch (choix) {
            case 1 -> rechercherParAuteur();
            case 2 -> afficherLivresDisponibles();
            case 3 -> afficherLivresEmpruntes();
            case 4 -> afficherHistoriqueLivre();
            case 5 -> rechercheMultiCriteres();
            default -> System.out.println("❌ Choix invalide!");
        }
    }
    
    private static void rechercherParAuteur() {
        System.out.print("✍Nom de l'auteur: ");
        String auteur = scanner.nextLine().trim();

        List<Livre> livres = bibliotheque.rechercherParAuteur(auteur);

        if (livres.isEmpty()) {
            System.out.println("❌ Aucun livre trouvé pour cet auteur.");
        } else {
            System.out.println("📚 Livres de " + auteur + ":");
            livres.forEach(livre -> {
                String statut = livre.estDisponible() ? "✅ " : "❌ ";
                System.out.println(statut + " " + livre);
            });
        }
    }
    
    private static void afficherLivresEmpruntes() {
        System.out.println("\n❌ === LIVRES EMPRUNTÉS ===");
        List<Livre> empruntes = bibliotheque.obtenirLivresEmpruntes();

        if (empruntes.isEmpty()) {
            System.out.println("📚 Tous les livres sont disponibles!");
        } else {
            empruntes.forEach(livre -> {
            String statut = livre.estEnRetard() ? "⚠ " : "📖 ";
            System.out.printf("%s %s%n", statut, livre);
            System.out.printf(" 👤 Par: %s, retour prévu: %s%n", livre.getEmprunteur().getNom(), livre.getDateRetourPrevue());
            });
        }
    }
    
    private static void afficherHistoriqueLivre() {
        System.out.print("🔢 ISBN du livre: ");
        String isbn = scanner.nextLine().trim();

        Optional<Livre> livre = bibliotheque.rechercherParId(isbn);
        if (livre.isEmpty()) {
            System.out.println("❌ Livre introuvable.");
            return;
        }

        System.out.println("📖 Livre: " + livre.get());
        List<Emprunt> historique = gestionnaireEmprunts.obtenirHistoriqueLivre(isbn);

        if (historique.isEmpty()) {
            System.out.println("📭 Aucun emprunt dans l'historique.");
        } else {
            System.out.println("📚 Historique des emprunts:");
            historique.forEach(e -> {
                String statut = e.estTermine() ? "✅ " : "📖 ";
                System.out.println(statut + " " + e);
            });
        }
    }
    
     private static void rechercheMultiCriteres() {
        System.out.println("\n🔍 === RECHERCHE MULTI-CRITÈRES ===");
        System.out.print("🔤 Texte libre (titre/auteur): ");
        String texte = scanner.nextLine().trim();

        System.out.print("✅ Disponibles seulement (o/N): ");
        boolean seulementDisponibles =
        scanner.nextLine().trim().equalsIgnoreCase("o");

        List<Livre> resultats = bibliotheque.rechercherTexte(texte);

        if (seulementDisponibles) {
            resultats = resultats.stream()
                .filter(Livre::estDisponible)
                .toList();
        }

        if (resultats.isEmpty()) {
            System.out.println("❌ Aucun livre trouvé avec ces critères.");
        } else {
            System.out.println("📚 " + resultats.size() + " livre(s) trouvé(s):");
            resultats.forEach(livre -> {
            String statut = livre.estDisponible() ? "✅ " : "❌ ";
            System.out.println(statut + " " + livre);
            });
        }
    }
     
     // === MENU RAPPORTS ET STATISTIQUES ===
     private static void menuRapportsStatistiques() {
        System.out.println("\n📊 === RAPPORTS ET STATISTIQUES ===");
        System.out.println("1. Statistiques générales");
        System.out.println("2. Rapport d'emprunts détaillé");
        System.out.println("3. Top des livres les plus empruntés");
        System.out.println("4. Utilisateurs les plus actifs");
        System.out.print("Votre choix: ");

        int choix = lireChoix();
        scanner.nextLine();

        switch (choix) {
            case 1 -> afficherStatistiquesGenerales();
            case 2 -> gestionnaireEmprunts.afficherRapportEmprunts();
            case 3 -> System.out.println("🚧 Fonctionnalité à venir dans la version 3.0");
            case 4 -> System.out.println("🚧 Fonctionnalité à venir dans la version 3.0");
            default -> System.out.println("❌ Choix invalide!");
        }
     }
        
        
        private static void afficherStatistiquesGenerales() {
            System.out.println("\n📈 === STATISTIQUES GÉNÉRALES ===");
            bibliotheque.afficherStatistiques();

            System.out.println("👥 === UTILISATSEURS ===");
            System.out.println("Total utilisateurs: " + gestionnaireEmprunts.getNombreUtilisateurs());
            System.out.println("Total emprunts: " + gestionnaireEmprunts.getNombreEmprunts());

            gestionnaireEmprunts.afficherRapportEmprunts();
        }
        
        // === UTILITAIRES ===
        private static int lireChoix() {
            try {
                return scanner.nextInt();
            } catch (Exception e) {
                scanner.nextLine(); // Nettoyer le buffer
                return -1; // Choix invalide
            }
        }
 }
