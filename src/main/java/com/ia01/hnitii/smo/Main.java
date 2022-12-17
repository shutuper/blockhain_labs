package com.ia01.hnitii.smo;

public class Main {

	public static void main(String[] args) {
		double lambda = 35, t = 1.6 / 60; // t in hours
		printCharacteristics(lambda, t);
	}

	private static void printCharacteristics(double lambda, double t) {
		double p = lambda * t, s2 = 0;

		System.out.println("\nОпераційні характеристики СМО із довільним часом обслуговування:\n");

		double nc = p + p * p * (1 + s2) / (2 - 2 * p);
		double n4 = nc - p;

		System.out.printf("Середня кількість заявок у системі: %s\n", nc);
		System.out.printf("Довжина черги: %s\n", n4);
		System.out.printf("Середній час перебування заявки у системі (год.): %s\n", nc / lambda);
		System.out.printf(" Середній час перебування заявки у черзі (год.): %s\n", n4 / lambda);

		System.out.println("\nОпераційні характеристики пуассонівської СМО:\n");

		nc = p / (1 - p);
		n4 = p * p / (1 - p);

		System.out.printf("Середня кількість заявок у системі: %s\n", nc);
		System.out.printf("Довжина черги: %s\n", n4);
		System.out.printf("Середній час перебування заявки у системі (год.): %s\n", nc / lambda);
		System.out.printf("Середній час перебування заявки у черзі (год.): %s\n", n4 / lambda);
	}

}
