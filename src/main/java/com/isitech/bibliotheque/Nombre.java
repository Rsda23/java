package com.isitech.bibliotheque;

public class Nombre {
    public static void main(String[] args) {
        int[] nombres = {2, 3, 4, 17, 25, 29, 97, 100};

        for (int nombre : nombres) {
        if (estPremier(nombre)) {
        System.out.println(nombre + " est premier");
        } else {
            System.out.println(nombre + " n'est pas premier");
        }
    }
 }

 public static boolean estPremier(int n) {
    // À compléter - optimiser avec √n
    return false;
    }
}
