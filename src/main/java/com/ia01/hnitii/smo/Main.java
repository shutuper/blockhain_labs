package com.ia01.hnitii.smo;

import java.util.Arrays;

import static java.lang.Math.min;
import static java.lang.Math.pow;

public class Main {

	public static void main(String[] args) {
		int n = 4, m = 20, lambda = 15, t = 15;
		// обрахунок 'u' приводимо до годин:
		double u = 1D / (t / 60D), p = lambda / u;

		System.out.println("\nОпераційні характеристики СМО із взаємодопомогою між каналами:\n");
		double[] pT = smo_2_with_help(n, m, p);
		System.out.printf("smo_2_with_help(n = %s, m = %s, p = %s) = \npT%s\n", n, m, p, Arrays.toString(pT));
		printCharacteristics(n, m, lambda, u, pT);


		System.out.println("\nОпераційні характеристики СМО без взаємодопомоги між каналами:\n");
		pT = smo_2_without_help(n, m, p);
		System.out.printf("smo_2_without_help(n = %s, m = %s, p = %s) = \npT%s\n", n, m, p, Arrays.toString(pT));
		printCharacteristics(n, m, lambda, u, pT);
	}

	private static double[] smo_2_with_help(int n, int m, double p) {
		double[] pT = new double[n + m + 1];
		double a = p / n;

		pT[0] = (1 - a) / (1 - pow(a, n + m + 1));
		for (int k = 1; k <= n + m; k++) {
			pT[k] = pow(a, k) * pT[0];
		}

		return pT;
	}

	private static double[] smo_2_without_help(int n, int m, double p) {
		int len = n + m + 1;
		double[] pT = new double[len];
		double[] q = new double[len];
		q[0] = 1;

		double s = 1;
		for (int k = 1; k < len; k++) {
			q[k] = q[k - 1] * p / min(k, n);
			s += q[k];
		}

		pT[0] = 1 / s;
		for (int k = 1; k < len; k++) {
			pT[k] = q[k] * pT[0];
		}

		return pT;
	}

	private static void printCharacteristics(int n, int m, int lambda, double u, double[] pT) {
		double p = pT[pT.length - 1];
		double q = 1 - p;
		double A = q * lambda;

		double avgBusyN = 0;
		for (int k = 0; k <= m; k++) {
			avgBusyN += k * pT[n + k];
		}

		System.out.println("Імовірність відмови в обслуговуванні = " + p);
		System.out.println("Відносна пропускна спроможность = " + q);
		System.out.println("Абсолютна пропускна спроможность = " + A);
		System.out.println("Середнє число зайнятих каналів = " + (lambda / u) * q);
		System.out.println("Середня кількість заявок у черзі = " + avgBusyN / n);
	}

}
