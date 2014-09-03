package utils;

public class Laplacian {
	public static int[][] calculate(int[][] matrix) {
		int[][] degree = DegreeMatrix.calculate(matrix);

		int[][] laplacian = new int[matrix.length][matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				laplacian[i][j] = degree[i][j] - matrix[i][j];
			}
		}

		return laplacian;
	}
	
	public static double[][] calculateDouble(double[][] matrix) {
		double[][] degree = DegreeMatrix.calculateDouble(matrix);
		double[][] laplacian = new double[matrix.length][matrix.length];

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				laplacian[i][j] = degree[i][j] - matrix[i][j];
			}
		}

		return laplacian;
	}

}