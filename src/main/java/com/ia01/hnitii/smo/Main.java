package com.ia01.hnitii.smo;

import java.util.Arrays;

import static java.lang.Math.min;

public class Main {

	public static void main(String[] args) {
		int n = 4, m = 10, lambda = 35;
		double u = 8, p = lambda / u;

		double[] pT = smo_2(n, m, p);
		System.out.printf("smo_2(n = %s, m = %s, p = %s) = \npT%s\n", n, m, p, Arrays.toString(pT));

		printCharacteristics(n, m, lambda, u, pT);
		printUWhenRejectPLTE(n, m, lambda, 0.1);
	}

	private static double[] smo_2(int n, int m, double p) {
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
		double A = lambda * q;

		double k3 = 0;
		for (int k = 0; k <= n + m; k++) {
			k3 += min(k, n) * pT[k];
		}

		double n4 = 0;
		for (int k = 1; k <= m; k++) {
			n4 += k * pT[n + k];
		}

		double nc = k3 + n4;
		double t4ek = n4 / lambda;
		double tc = t4ek + 1 / u;

		double P3 = 0;
		for (int k = 0; k <= m; k++) {
			P3 += pT[n + k];
		}

		double P4 = 0;
		for (int k = 1; k <= m; k++) {
			P4 += pT[n + k];
		}

		System.out.println("\nOperational printCharacteristics:\n");
		System.out.println("Pвідм = " + p);
		System.out.println("q = " + q);
		System.out.println("A = " + A);
		System.out.println("kз = " + k3);
		System.out.println("nчер = " + n4);
		System.out.println("nc = " + nc);
		System.out.println("tчек = " + t4ek);
		System.out.println("tс = " + tc);
		System.out.println("Pз = " + P3);
		System.out.println("Pчер = " + P4);
	}

	private static void printUWhenRejectPLTE(int n, int m, double lambda, double lte) {
		for (int i = 1; ; i++) {
			double[] pT = smo_2(n, m, lambda / i);
			double rejectP = pT[pT.length - 1];

			if (rejectP <= lte) {
				System.out.println("\nP(відмови) <= " + lte + " при u >= " + i);
				return;
			}
		}
	}

}
