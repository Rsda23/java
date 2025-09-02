package com.isitech.exercice;

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
        if (n <= 1) return false;
    
        for (int i = 2; i < n; i++) {
            if (n % i == 0) return false;
        }
    
        return true;
    }
 }
