package metrics;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;

public class AveragePathLength implements IMetric {
	
	public Object calculate(double[][] matrix, Object g) {
		
		if(!(g instanceof MultiGraph)){
			System.err.println("Graph not define.");
			return null;
		}
		
		double sum = 0;
		MultiGraph graph = (MultiGraph)g;

		if (graph != null) {
			Dijkstra dijkstra = new Dijkstra();
			dijkstra.init(graph);

			for (int i = 0; i < matrix.length; i++) {

				dijkstra.setSource(graph.getNode(i));
				dijkstra.compute();

				for (int j = 0; j < matrix[0].length; j++) {
					if (i != j) {
						Path path = dijkstra.getPath(graph.getNode(j));

						int current = path.size();
						sum += current;
					}
				}
			}
		}
		
		int n = matrix.length;
		return (sum/(n * (n-1 )));
	}

}
