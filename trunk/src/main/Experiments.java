package main;

import gstream.GraphViewer;
import metrics.ClusteringCoefficient;
import metrics.Diameter;
import models.Watts_Strogatz_2;

public class Experiments {

	public static void main(String[] args) {

		int N = 100;
		int K = 4;

		double[][] net = null;
		Diameter D = new Diameter();
		ClusteringCoefficient CC = new ClusteringCoefficient();
		
		double cc_max = Double.MIN_VALUE;
		double d_max = Double.MIN_VALUE;

		for (double p = 1e-9; p <= 1; p += 1e-2) {
			
			double cc = 0;
			double d = 0;
			
			for (int a = 0; a < 30; a++) {

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
				
				double dCurrent = ((double) D.calculate(net, graphViewer.getGrafo()));
				if(dCurrent > d_max)d_max = dCurrent;
				
				double ccCurrent = ((double) CC.calculate(net, null));
				if(ccCurrent > cc_max)cc_max = ccCurrent;
				
				d += dCurrent;
				cc += ccCurrent;
			}
				System.out.println((p + ";"
					+ d/(30 * d_max)
					+ ";" + cc/(30* cc_max) ).replace(".",
					","));
		}

		

	}

}
