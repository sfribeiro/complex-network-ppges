package utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class Eigenvectors {

	public static List<double[]> calculate(int[][] matrix) {

		double[][] realValues = new double[matrix.length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				realValues[i][j] = matrix[i][j];
			}
		}

		RealMatrix rm = new Array2DRowRealMatrix(realValues);

		List<double[]> resp = new ArrayList<double[]>();
		double[] eigenVector = null;
		try {
			EigenDecomposition solver = new EigenDecomposition(rm);
			for (int i = 0; i < matrix.length; i++) {

				eigenVector = solver.getEigenvector(i).toArray();

				resp.add(eigenVector);
			}
		} catch (Exception e) {
		}

		return resp;
	}

	public static List<double[]> calculate(double[][] matrix) {

		RealMatrix rm = new Array2DRowRealMatrix(matrix);

		List<double[]> resp = new ArrayList<double[]>();
		double[] eigenVector = null;
		try {
			EigenDecomposition solver = new EigenDecomposition(rm);
			for (int i = 0; i < matrix.length; i++) {

				eigenVector = solver.getEigenvector(i).toArray();

				resp.add(eigenVector);
			}
		} catch (Exception e) {
		}

		return resp;
	}
}
