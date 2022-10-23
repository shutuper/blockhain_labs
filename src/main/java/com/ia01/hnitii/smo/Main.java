package com.ia01.hnitii.smo;

import java.util.Arrays;

import static java.lang.Math.*;

public class Main {

	public static void main(String[] args) {
		int n = 4, m = 10, lambda = 5;
		double u = 1D / 0.18, p = lambda / u;

		double[] pT = smo_2_b(n, m, p);
		System.out.printf("smo_2_b(n = %s, m = %s, p = %s) = \npT%s\n", n, m, p, Arrays.toString(pT));

		printCharacteristics(n, m, lambda, u, pT, 10);
		printUWhenRejectPLTE(n, m, lambda);
	}

	private static double[] smo_2_b(int n, int m, double p) {
		int len = n + m + 1;
		double[] pT = new double[len];
		pT[0] = 1;
		double s = 1;

		for (int k = 1; k < len; k++) {
			pT[k] = pT[k - 1] * p / min(k, n);
			if (k > n) {
				pT[k] *= pow(1d / n, k - n);
			}
			s += pT[k];
		}

		pT[0] = 1 / s;

		for (int k = 1; k < len; k++) {
			pT[k] *= pT[0];
		}

		return pT;
	}

	private static void printCharacteristics(int n, int m, int lambda, double u, double[] pT, int maxLen) {
		double p = pT[pT.length - 1];
		double q = 1 - p;

		double avgBusyN = 0;
		for (int k = 1; k <= n && k < pT.length; k++) {
			avgBusyN += k * pT[k];
		}

		double avgQueueLen = 0;
		for (int k = 1; (n + k < pT.length) && (k <= maxLen); k++) {
			avgQueueLen += k * pT[n + k];
		}

		double queueExistence = 0;
		for (int k = 1; (n + k < pT.length) && (k <= maxLen); k++) {
			queueExistence += pT[n + k];
		}

		double P3 = 0;
		for (int k = 0; (n + k < pT.length) && (k <= maxLen); k++) {
			P3 += pT[n + k];
		}

		double P4 = 0;
		for (int k = n; k < pT.length; k++) {
			P4 += pT[k];
		}

		System.out.println("\nОпераційні характеристики СМО:\n");
		System.out.println("Pвідм = " + p);
		System.out.println("Середня кількість зайнятих каналів = " + avgBusyN);
		System.out.println("Середня довжина черги = " + avgQueueLen);
		System.out.println("Середня кількість повідомлень = " + (avgBusyN + avgQueueLen));
		System.out.println("Середній час чекання у черзі = " + (avgQueueLen / lambda));
		System.out.println("Середній час перебування SMS у системі = " + (avgQueueLen / lambda + q / u));
		System.out.println("Імовірність наявності черги = " + queueExistence);
		System.out.println("Імовірність зайнятості каналів обслуговування = " + P3);
		System.out.println("Імовірність чекання SMS у черзі = " + P4);
	}

	private static void printUWhenRejectPLTE(int n, int m, int lambda) {
		for (double t = 2.55; t > 0; t -= 0.01D) {
			double u = lambda / (1D / t);
			double[] pT = smo_2_b(n, m * 10, u);

			double avgQueueLen = 0;
			for (int k = 1; k <= m; k++) {
				avgQueueLen += k * pT[n + k];
			}

			if (((int) avgQueueLen) <= m + 1) {
				System.out.println("\nМінімальний час обробки SMS-повідомлень, щоб черга не перевищувала "
						+ m + " повідомлень: " + (t + 0.01) + "хв");
				return;
			}
		}
	}

}
