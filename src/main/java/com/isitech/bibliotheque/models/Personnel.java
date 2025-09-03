package com.isitech.bibliotheque.models;

public class Personnel extends Utilisateur {
    private String service;
    private String poste;

    public Personnel(String nom, String email, String service, String poste) {
        super(nom, email);
        this.service = service;
        this.poste = poste;
        this.maxEmprunts = 5;
    }

    @Override
    public int getDureeEmpruntMax() {
        return 21; // 3 semaines
    }

    @Override
    public boolean peutEmprunterType(Livre livre) {
        return true;
    }

    @Override
    public String getTypeUtilisateur() {
        return "Personnel";
    }

    // Getters
    public String getService() { 
        return service; 
    }
    
    public String getPoste()  { 
        return poste; 
    }
}
