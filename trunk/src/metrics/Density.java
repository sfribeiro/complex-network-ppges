package metrics;

public class Density implements IMetric {

	@Override
	public Object calculate(double[][] m) {
		double sum = 0;
		
		for(int i = 0; i < m.length; i++)
		{
			for(int j = 0; j < m[0].length; j++)
			{
				if(m[i][j] == 1)
				{
					sum++;				
				}
			}
		}
		
		int n = m.length;
		
		return (sum/(n * (n - 1)));
	}

	@Override
	public Object calculate(int[][] matrix) {
		// TODO Auto-generated method stub
		return null;
	}

}
