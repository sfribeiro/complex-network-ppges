package models;

import config.Config;

public class Watts_Strogatz_2 {

	public static double[][] generate(int N, int K, double beta) {
		if (K <= 1) {
			new Exception("Valor de K inválido!");
		}

		double[][] resp = new double[N][N];

		// create initial edges - circle
		for (int i = 0; i < N; i++) {

			for (int j = 1; j <= (K / 2); j++) {

				resp[i][(i + j) % N] = 1;
				resp[(i + j) % N][i] = 1;

				if ((i - j) > 0) {
					resp[i][(i - j) % N] = 1;
					resp[(i - j) % N][i] = 1;
				} else {
					resp[i][(N + i - j) % N] = 1;
					resp[(N + i - j) % N][i] = 1;
				}
			}
		}

		for (int i = 0; i < N; i++) {
			for (int j = 1; j <= (K / 2); j++) {

				double p = Config.random.nextDouble();

				if (p < beta) {
					resp[i][(i + j) % N] = 0;
					resp[(i + j) % N][i] = 0;

					if ((i - j) > 0) {
						resp[i][(i - j) % N] = 0;
						resp[(i - j) % N][i] = 0;
					} else {
						resp[i][(N + i - j) % N] = 0;
						resp[(N + i - j) % N][i] = 0;
					}
					
					int novo = i;
					while(novo == i)
						novo = Config.random.nextInt(N);
					
					resp[i][novo] = 1;
					resp[novo][i] = 1;
				}
			}
		}

		return resp;
	}
}
