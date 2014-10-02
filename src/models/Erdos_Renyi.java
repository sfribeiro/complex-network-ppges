package models;

import config.Config;

public class Erdos_Renyi {

	public static double[][] generate(int numberNodes, double p) {
		
		double[][] resp = new double[numberNodes][numberNodes];

		for (int i = 0; i < numberNodes - 1; i++) {
			for (int j = i + 1; j < numberNodes; j++) {
				if (Config.random.nextDouble() < p) {
					resp[i][j] = 1;
					resp[j][i] = 1;
				}
			}
		}

		return resp;
	}
}
