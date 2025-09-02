package com.isitech.bibliotheque;

import java.util.Scanner;

public class Calculatrice {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez le premier nombre : ");
        double a = scanner.nextDouble();

        System.out.print("Entrez l'opérateur (+, -, *, /) : ");
        char op = scanner.next().charAt(0);

        System.out.print("Entrez le second nombre : ");
        double b = scanner.nextDouble();

        double resultat = calculer(a, b, op);

        if (resultat != Double.NaN) {
        System.out.println("Résultat : " + resultat);
        }

        scanner.close();
    }
    
    public static double calculer(double a, double b, char op) {
        if (op == '+') return a + b;
        if (op == '-') return a - b;
        if (op == '*') return a * b;
        if (op == '/') return a / b;
        
        return 0;
    }
}
