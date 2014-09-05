package main;

import java.util.Arrays;
import java.util.List;

import metrics.Eigenvalues;
import metrics.Eigenvectors;
import utils.DegreeMatrix;
import utils.Laplacian;

public class Test {

	public static void main(String[] args) {
		
		double[][] m = new double[][]{{1,2,1},{1,1,4},{1,1,1}}; 
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
	}

}
