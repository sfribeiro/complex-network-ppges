package models;

import java.util.ArrayList;

import config.Config;

public class Watts_Strogatz {

	/**
	 * k is the mean degree with value even.
	 * beta is between 0 and 1 and is the probability to rewire an edge
	 */
	public static double[][] generate(int numberNodes, int k, double beta) {

		double[][] resp = new double[numberNodes][numberNodes];

		ArrayList<Integer> possibleLinks = new ArrayList<Integer>();
		
		for (int i = 0; i < numberNodes; i++) {
			if (getNodeDegree(resp, i) < k) {
				possibleLinks.clear();
				for (int j = 0; j < numberNodes; j++) {
					if (j != i && getNodeDegree(resp, j) < k) {
						possibleLinks.add(j);
					}
				}
				System.out.print("i = " + i + " - Tam = " + possibleLinks.size() + " - ");
				for (int j = 0; j < possibleLinks.size(); j++) {
					System.out.print(possibleLinks.get(j) + " ");
				}
				System.out.println();
				for (int j = 0; j < k - getNodeDegree(resp, i);) {
					int index = possibleLinks.remove(Config.random.nextInt(possibleLinks.size()));
					resp[i][index] = 1;
					resp[index][i] = 1;
				}
				
				for (int j = 0; j < resp.length; j++) {
					System.out.print("Linha " + j + ": ");
					for (int j2 = 0; j2 < resp.length; j2++) {
						System.out.print(resp[j][j2] + "  ");
					}
					System.out.println(" - " + getNodeDegree(resp, j));
				}
				System.out.println();
			}
		}
		
		return resp;
	}

	private static int getNodeDegree(double[][] resp, int index) {
		
		int nodeDegree = 0;
		
		for (int i = 0; i < resp.length; i++) {
			if (resp[index][i] == 1) {
				nodeDegree++;
			}
		}
		
		return nodeDegree;
	}
}
