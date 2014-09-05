package utils;
public class DegreeMatrix {
	
	public static int[][] calculate(int[][] matrix) {
		int[][] m = new int[matrix.length][matrix.length];
		int degree = 0;
		for (int i = 0; i < matrix.length; i++) {
			degree = 0;
			for (int j = 0; j < matrix[i].length; j++) {
				if (i != j) {
					m[i][j] = 0;
				}
				degree += matrix[i][j];
			}
			m[i][i] = degree;
		}

		return m;
	}
	
	public static double[][] calculateDouble(double[][] matrix) {
		double[][] m = new double[matrix.length][matrix.length];
		int degree = 0;
		for (int i = 0; i < matrix.length; i++) {
			degree = 0;
			for (int j = 0; j < matrix[i].length; j++) {
				if (i != j) {
					m[i][j] = 0;
				}
				degree += matrix[i][j];
			}
			m[i][i] = degree;
		}

		return m;
	}
}