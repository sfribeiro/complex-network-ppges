package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.graphstream.algorithm.BetweennessCentrality;

import gstream.GraphViewer;
import metrics.AlgebraicConnectivity;
import metrics.AveragePathLength;
import metrics.Betweeness;
import metrics.ClusteringCoefficient;
import metrics.Density;
import metrics.Diameter;
import metrics.Entropy;
import metrics.SpectralRadius;
import models.Barabasi_Albert;
import models.Erdos_Renyi;
import models.Watts_Strogatz;
import utils.DegreeDistribution;
import utils.DegreeMatrix;
import utils.Eigenvalues;
import utils.Eigenvectors;
import utils.Laplacian;

public class Test {

	public static void main(String[] args) {
		
		//double[][] m = NetworkGenerate.scaleFree(30);
		
		/*double[][] m = NetworkGenerate.erdosReni(30,0.7); /*new double[][]{
				{0,0,1,1,0,0,1},
				{0,0,1,0,0,1,0},
				{1,1,0,0,1,0,1},
				{1,0,0,0,0,1,1},
				{0,0,1,0,0,1,1},
				{0,1,0,1,1,0,0},
				{1,0,1,1,1,0,0}};*/
		
		//double[][] m = Erdos_Renyi.generate(30, 0.5);
		//double[][] m = Barabasi_Albert.generate(100, 1);
		double[][] m = Watts_Strogatz.generate(20, 4, 0.5);
		
		/* Graph Viewer BEGIN */
		GraphViewer graphViewer = new GraphViewer();
		
		for (int i = 0; i < m.length; i++) {
			graphViewer.addNode(i);
		}
		
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				if(m[i][j] == 1)
					graphViewer.addEdge(i, j);
			}
		}
		graphViewer.display();
		
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				if (m[i][j] == 1) {
					graphViewer.highlightEdge(i, j, true);
				}
			}
		}		
		/* Graph Viewer END */
		
		System.out.println("Matrix:");
		for(int i = 0; i < m.length; i++)
			System.out.println(Arrays.toString(m[i]).replace(".", ","));
		
		System.out.println("\nDegreeMatrix: ");
		double[][] dm = DegreeMatrix.calculateDouble(m);
		for(int i = 0; i < m.length; i++)
			System.out.println(Arrays.toString(dm[i]));

		System.out.println("\nLaplacian: ");
		double[][] lm = Laplacian.calculateDouble(m);
		for(int i = 0; i < m.length; i++)
			System.out.println(Arrays.toString(lm[i]));
		
		System.out.println("\nEigenValues: ");
		double[] eigenValues = Eigenvalues.calculate(lm);
		System.out.println(Arrays.toString(eigenValues));
		
		System.out.println("\nEigenVectors: ");
		List<double[]> vectors = Eigenvectors.calculate(lm);
		for(int i = 0; i < vectors.size(); i++)
			System.out.println(Arrays.toString(vectors.get(i)));
		
		System.out.println("\nDegree Distribution: ");
		HashMap<Integer, Integer> degreeDistribution = DegreeDistribution.calculate(m);
		for(int i : degreeDistribution.keySet())
			System.out.println(i + " - " + degreeDistribution.get(i));
		
		
		System.out.println("###### Metrics ######");
		
		System.out.println("\nEntropy: ");
		System.out.println(new Entropy().calculate(m, null));
		
		System.out.println("\nClustering Coefficient: ");
		System.out.println(new ClusteringCoefficient().calculate(m, null));
		
		System.out.println("\nDensity: ");
		System.out.println(new Density().calculate(m, null));
		
		System.out.println("\nDiameter: ");
		System.out.println(new Diameter().calculate(m, graphViewer.getGrafo()));
		
		System.out.println("\nAPL: ");
		System.out.println(new AveragePathLength().calculate(m, graphViewer.getGrafo()));
		
		System.out.println("\nBetweeness: ");
		System.out.println(new Betweeness().calculate(m, graphViewer.getGrafo()));
		
		System.out.println("\nSpectral Radius: ");
		System.out.println(new SpectralRadius().calculate(lm, null));
		
		System.out.println("\nAlgebraic Connectivity: ");
		System.out.println(new AlgebraicConnectivity().calculate(lm, null));
		
	}

}
