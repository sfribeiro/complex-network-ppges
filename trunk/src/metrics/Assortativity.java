package metrics;

import utils.DegreeMatrix;

public class Assortativity implements IMetric {

	//Resolver
	@Override
	public Object calculate(double[][] matrix, Object o) {

		double sum = 0;
		double[][] matrixDegree = DegreeMatrix.calculateDouble(matrix);
		for(int i = 0 ; i < matrix.length; i++)
		{
			sum += matrixDegree[i][i];
		}
		
		double avg = sum/(Math.pow(matrix.length, 2));
		
		double sumTotal = 0;
		for(int i = 0; i < matrix.length; i++)
		{
			double sumLocal = 0;
			for(int j = 0; j < matrix[0].length; j++)
			{
				if(matrix[i][j] == 1)
				{
					sumLocal += (matrixDegree[j][j]-avg);
				}
			}
			sumTotal += sumLocal;
		}
		
		double resp = sumTotal/(Math.pow(matrix.length, 2));
		
		return resp;
	}

}
