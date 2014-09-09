package utils;

import java.util.HashMap;

public class DegreeDistribution {

	/**
	 * @param matrix is Connect Distribution
	 */
	public static HashMap<Integer, Integer> calculate(double[][] matrix)
	{
		HashMap<Integer, Integer> resp = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < matrix.length; i++)
		{
			int sum = 0;
			for(int j = 0; j < matrix[0].length; j++)
			{
				if(matrix[i][j] == 1) sum++;
			}
			
			if(resp.containsKey(sum))
			{
				int aux = resp.get(sum);
				aux++;
				
				resp.remove(sum);
				resp.put(sum, aux);
			}else
			{
				resp.put(sum, 1);
			}
		}
		
		return resp;
	}
	
	public static HashMap<Integer, Integer> calculate(int[][] matrix)
	{
		HashMap<Integer, Integer> resp = new HashMap<Integer, Integer>();
		
		for(int i = 0; i < matrix.length; i++)
		{
			int sum = 0;
			for(int j = 0; j < matrix[0].length; j++)
			{
				if(matrix[i][j] == 1) sum++;
			}
			
			if(resp.containsKey(sum))
			{
				int aux = resp.get(sum);
				aux++;
				
				resp.remove(sum);
				resp.put(sum, aux);
			}else
			{
				resp.put(sum, 1);
			}
		}
		
		return resp;
	}
}
