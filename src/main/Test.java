package main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import metrics.ClusteringCoeffient;
import metrics.Eigenvalues;
import metrics.Eigenvectors;
import metrics.Entropy;
import utils.DegreeDistribution;
import utils.DegreeMatrix;
import utils.Laplacian;

public class Test {

	public static void main(String[] args) {
		
		double[][] m = new double[][]{{0,1,0,1},{1,0,1,0},{0,1,0,0},{1,0,0,0}}; 
		System.out.println("Matrix:");
		for(int i = 0; i < m.length; i++)
			System.out.println(Arrays.toString(m[i]));
		
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
		
		System.out.println("\nEntropy: ");
		System.out.println(new Entropy().calculate(m));
		
		System.out.println("\nClustering Coefficient: ");
		System.out.println(new ClusteringCoeffient().calculate(m));
	}

}
