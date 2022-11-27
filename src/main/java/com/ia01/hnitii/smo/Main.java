package com.ia01.hnitii.smo;

import java.util.Arrays;

import static java.lang.Math.*;

public class Main {

	public static void main(String[] args) {
		int n = 25, m = 2, lambda = 3;
		double u = 30, p = lambda / u;

		double[] pT = smo_4(n, m, p);
		System.out.printf("smo_4(n = %s, m = %s, p = %s) = \npT%s\n", n, m, p, Arrays.toString(pT));

		printCharacteristics(n, m, lambda, u, pT);
		printTobs(n, m, lambda);
	}

	private static double[] smo_4(int n, int m, double p) {
		int len = n + 1;
		double[] pT = new double[len], q = new double[len];
		q[0] = 1;
		double s = 1;

		for (int k = 1; k < len; k++) {
			q[k] = (q[k - 1] * p / min(k, m)) * (n + 1 - k);
			s += q[k];
		}

		pT[0] = 1d / s;

		for (int k = 1; k < len; k++) {
			pT[k] = q[k] * pT[0];
		}

		return pT;
	}

	private static void printCharacteristics(int n, int m, int lambda, double u, double[] pT) {
		double p = pT[pT.length - 1];
		double q = 1 - p;

		double avgBusyN = 0;
		for (int k = 0; k <= n; k++) {
			avgBusyN += k * pT[k];
		}


		System.out.println("\nОпераційні характеристики СМО:\n");
		System.out.println("Середнє число зайнятих каналів = " + (1 - pT[0]));
		System.out.println("Середня кількість не зайнятих каналів = " + (pT[0]));
		System.out.println("Абсолютна пропускна спроможність = " + avgBusyN * u);
		System.out.println("Коефіцієнт простою автомобілів = " + avgBusyN / n);
		System.out.println("Середнє число учнів у системі на обслуговування = " + (n - (1 - pT[0]) / (lambda / u)));
		System.out.println("Коефіцієнт простою студентів = " + (n - (1 - pT[0]) / (lambda / u)) / n);
	}

	private static void printTobs(int n, int m, int lambda) {
		double step = 0.01D;
		for (double t = 0.1; t > 0; t += step) {
			double p = lambda * t;

			double[] pT = smo_4(n, m, p);
			double p0 = pT[0];
			double n4er = n - ((1 + p) / p) * (1 - p0);

			if (n4er / (lambda * ((1 - p0) / p)) >= 5) {
				System.out.println("Час обслуговування студентів має бути = " + (t - step) +
						", щоб час чекання студентом підходу до нього викладача не перевищувала 5 хв");
				return;
			}
		}
	}

}
