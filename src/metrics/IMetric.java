package metrics;

public interface IMetric {
	
	Object calculate(double[][] matrix);
	Object calculate(int[][] matrix);

}
