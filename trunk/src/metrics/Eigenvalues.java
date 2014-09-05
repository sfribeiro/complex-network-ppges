package metrics;

import java.util.Arrays;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class Eigenvalues {
	
	static final double tolerance = 1E-8;
	
	private Eigenvalues() { }
	
	public static double[] calculate(int[][] matrix) {
		
		double[][] realValues = new double[matrix.length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				realValues[i][j] = matrix[i][j];
			}
		}
		
		RealMatrix rm = new Array2DRowRealMatrix(realValues);
		double[] eigenValues = null;
		try {
			@SuppressWarnings("deprecation")
			EigenDecomposition solver = new EigenDecomposition(rm, tolerance);
			eigenValues = solver.getRealEigenvalues();
			Arrays.sort(eigenValues);
			
			/*		double aux;
					for (int i = 0; i < eigenValues.length; i++) {
						for (int j = i; j < eigenValues.length; j++) {
							if (eigenValues[j] < eigenValues[i]) {
								aux = eigenValues[i];
								eigenValues[i] = eigenValues[j];
								eigenValues[j] = aux;
							}
						}
					}*/
		} catch (Exception e) {	}
		return eigenValues;
	}
	
	public static double[] calculate(double[][] matrix) {
		
		RealMatrix rm = new Array2DRowRealMatrix(matrix);
		double[] eigenValues = null;
		try {
			@SuppressWarnings("deprecation")
			EigenDecomposition solver = new EigenDecomposition(rm, tolerance);
			eigenValues = solver.getRealEigenvalues();
			
			Arrays.sort(eigenValues);
			
	/*		double aux;
			for (int i = 0; i < eigenValues.length; i++) {
				for (int j = i; j < eigenValues.length; j++) {
					if (eigenValues[j] < eigenValues[i]) {
						aux = eigenValues[i];
						eigenValues[i] = eigenValues[j];
						eigenValues[j] = aux;
					}
				}
			}*/
		} catch (Exception e) {	}
		return eigenValues;
	}
}