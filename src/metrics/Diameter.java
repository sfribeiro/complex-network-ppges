package metrics;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;

public class Diameter implements IMetric {

	@Override
	public Object calculate(double[][] matrix, Object g) {
		
		if(!(g instanceof MultiGraph)){
			System.err.println("Graph not define.");
			return null;
		}
		
		double size = -1;
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

						//System.out.println(path);

						int current = path.size();
						if (current > size)
							size = current;
					}
				}
			}
		}
		
		return size-1; //retorna o n�mero de enlaces n�o o n�mero de n�s
	}
}
