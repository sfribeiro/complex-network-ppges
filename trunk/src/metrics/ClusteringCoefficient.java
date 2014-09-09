package metrics;

import java.util.ArrayList;

public class ClusteringCoefficient implements IMetric {

	@Override
	public Object calculate(double[][] m) {
	
		double sum = 0;
		for(int i = 0; i < m.length; i++)
		{
			
			// calculate neighbors
			ArrayList<Integer> neighbors = new ArrayList<Integer>();
			for(int j = 0; j < m[0].length; j++)
			{
				if(m[i][j] == 1)
				{
					neighbors.add(j);					
				}
			}
			
			//calculate 'triangulações'
			double c = 0;
			for(int v=0; v < neighbors.size()-1; v++)
			{
				for(int v2=v+1; v2 < neighbors.size(); v2++)
				{
					if(m[neighbors.get(v)][neighbors.get(v2)]==1) c++;
				}
			}
			
			int di = neighbors.size();
			if(di > 1){
				double cci = 2*c/(di * (di - 1));
				System.out.println(i + " - " + cci);
				sum+= cci;
			}
		}
		
		return (sum/m.length);
	}

	@Override
	public Object calculate(int[][] matrix) {
		// TODO Auto-generated method stub
		return null;
	}

}
