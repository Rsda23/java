package com.isitech.bibliotheque.models;

import java.util.Objects;
import java.util.UUID;

public abstract class Utilisateur {
    protected final String id;
    protected String nom;
    protected String email;
    protected int maxEmprunts;
    protected int empruntsActuels;

    protected Utilisateur(String nom, String email) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.nom = nom;
        this.email = email;
        this.empruntsActuels = 0;
    }
    
    // Méthodes abstraites à implémenter
    public abstract int getDureeEmpruntMax();
    public abstract boolean peutEmprunterType(Livre livre);
    public abstract String getTypeUtilisateur();

    // Méthodes concrètes communes
    public boolean peutEmprunter() {
        return empruntsActuels < maxEmprunts;
    }

    public void incrementerEmprunts() {
        if (empruntsActuels >= maxEmprunts) {
            throw new IllegalStateException("Quota d'emprunts atteint");
        }
        empruntsActuels++;
    }

    public void decrementerEmprunts() {
        if (empruntsActuels > 0) {
            empruntsActuels--;
        }
    }

    // Getters/Setters
    public String getId() { return id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getMaxEmprunts() { return maxEmprunts; }
    public int getEmpruntsActuels() { return empruntsActuels; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Utilisateur that = (Utilisateur) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s: %s (%s) - %d/%d emprunts", getTypeUtilisateur(), nom, email, empruntsActuels, maxEmprunts);
    }
}
