package metrics;

import utils.Eigenvalues;

public class SpectralRadius implements IMetric{


	public Object calculate(double[][] matrix, Object aux) {

		double[] eigenValues = Eigenvalues.calculate(matrix);
		
		return eigenValues[eigenValues.length-1];
	}

}
