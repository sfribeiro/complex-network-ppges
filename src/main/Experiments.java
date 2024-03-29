package main;

import gstream.GraphViewer;

import java.util.Arrays;
import java.util.HashMap;

import metrics.ClusteringCoefficient;
import metrics.Diameter;
import models.Barabasi_Albert;
import models.Erdos_Renyi;
import models.Watts_Strogatz_2;
import utils.DegreeDistribution;

public class Experiments {

	public static void main(String[] args) {

		//testErdosRenyi();
		 testBA();
		// testBeta();

	}

	public static void testErdosRenyi() {
		
		int amostras = 30;		
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < amostras; i++) {
			
			double[][] net = Erdos_Renyi.generate(1000, 0.2);
			HashMap<Integer, Integer> degrees = DegreeDistribution
					.calculate(net);

			for(Integer k : degrees.keySet())
			{
				if(result.containsKey(k))
				{
					int num = result.get(k);
					result.put(k, num + degrees.get(k));
				}
				else
				{
					result.put(k, degrees.get(k));
				}
			}
		}

		Object[] d = result.keySet().toArray();
		Arrays.sort(d);
		for (int i = 0; i < d.length; i++) {
			System.out.println(d[i] + ";" + (result.get(d[i])/amostras));
		}
	}

	// Test degree distribution for Barabasi_Albert model
	public static void testBA() {
		int amostras = 30;		
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < amostras; i++) {
			
			double[][] net = Barabasi_Albert.generate(10000, 2);
			HashMap<Integer, Integer> degrees = DegreeDistribution
					.calculate(net);

			for(Integer k : degrees.keySet())
			{
				if(result.containsKey(k))
				{
					int num = result.get(k);
					result.put(k, num + degrees.get(k));
				}
				else
				{
					result.put(k, degrees.get(k));
				}
			}
		}

		Object[] d = result.keySet().toArray();
		Arrays.sort(d);
		for (int i = 0; i < d.length; i++) {
			System.out.println(d[i] + ";" + (result.get(d[i])/amostras));
		}
	}

	// Test degree distribution for various values of beta
	public static void testBeta() {
		int N = 100;
		int K = 10;
		int amostras = 30;

		double[][] net = null;
		Diameter D = new Diameter();
		ClusteringCoefficient CC = new ClusteringCoefficient();

		double cc_max = Double.MIN_VALUE;
		double d_max = Double.MIN_VALUE;

		for (double p = 1e-9; p <= 1; p += 1e-2) {

			double cc = 0;
			double d = 0;

			for (int a = 0; a < amostras; a++) {

				net = Watts_Strogatz_2.generate(N, K, p);

				GraphViewer graphViewer = new GraphViewer();

				for (int i = 0; i < N; i++) {
					graphViewer.addNode(i);
				}

				for (int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						if (net[i][j] == 1)
							graphViewer.addEdge(i, j);
					}
				}
				// graphViewer.display();

				double dCurrent = ((double) D.calculate(net,
						graphViewer.getGrafo()));
				if (dCurrent > d_max)
					d_max = dCurrent;

				double ccCurrent = ((double) CC.calculate(net, null));
				if (ccCurrent > cc_max)
					cc_max = ccCurrent;

				d += dCurrent;
				cc += ccCurrent;
			}

			System.out.println((p + ";" + d / (amostras * d_max) + ";" + cc
					/ (amostras)).replace(".", ","));
		}

	}
}
