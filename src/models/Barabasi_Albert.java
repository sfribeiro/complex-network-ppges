package models;

import java.util.ArrayList;

import config.Config;

public class Barabasi_Albert {

	/**
	 * m is the number of preference links that will create.
	 */
	public static double[][] generate(int numberNodes, int m) {

		double[][] resp = new double[numberNodes][numberNodes];

		ArrayList<Integer> restantes = new ArrayList<Integer>();
		for (int i = 0; i < numberNodes; i++) {
			restantes.add(i);
		}

		ArrayList<Integer> array = new ArrayList<Integer>();

		// Select initial nodes
		for (int i = 0; i < m; i++) {
			int index = Config.random.nextInt(restantes.size());
			Integer node = restantes.remove(index);
			array.add(node);

		}

		// Create click with initial nodes
		for (int i = 0; i < array.size() - 1; i++) {
			for (int j = i + 1; j < array.size(); j++) {
				Integer node1 = array.get(i);
				Integer node2 = array.get(j);

				resp[node1][node2] = 1;
				resp[node2][node1] = 1;
			}
		}

		// Increment probability in initial nodes
		for (int i = 0; i < m; i++) {
			for (int j = 1; j < m; j++)
				array.add(array.get(i));
		}

		int node = -1;

		int node2;
		while (restantes.size() > 0) {

			node2 = restantes
					.remove(Config.random.nextInt(restantes.size()));
			
			int links = 0;
			while (links < m) {
				
				node = array.get(Config.random.nextInt(array.size()));
				// System.out.println(next);

				if (node2 != node && resp[node][node2] != 1) {
					resp[node2][node] = 1;
					resp[node][node2] = 1;

					array.add(node);
					array.add(node2);
					
					links++;
				}
			}
		}

		return resp;
	}
}
