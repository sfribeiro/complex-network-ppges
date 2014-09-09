package metrics;

import java.util.HashMap;

import utils.DegreeDistribution;

public class Entropy implements IMetric{

	@Override
	public Object calculate(double[][] matrix, Object aux) {
	
		HashMap<Integer, Integer> degreeDistribution = DegreeDistribution.calculate(matrix);
		
		double resp = 0;
		for(int hi : degreeDistribution.keySet())
			resp -= hi * Math.log(hi);
		
		return resp;
	}
}
