package metrics;

import utils.Eigenvalues;

public class AlgebraicConnectivity implements IMetric {

	@Override
	public Object calculate(double[][] matrix, Object aux) {

		double[] eigenValues = Eigenvalues.calculate(matrix);
		
		return eigenValues[1];
		
	}

}
