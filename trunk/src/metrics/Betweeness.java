package metrics;

import java.util.Arrays;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;

public class Betweeness implements IMetric{

	@Override
	public Object calculate(double[][] matrix, Object g) {
		
		if(!(g instanceof MultiGraph)){
			System.err.println("Graph not define.");
			return null;
		}
		
		int[] edgesBetweeness = new int[matrix.length];
		
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

						for(Node e : path.getNodeSet())
						{
							int id = Integer.parseInt(e.getId());
							edgesBetweeness[id]++;
						}
					}
				}
			}
		}	
		
		System.out.println(Arrays.toString(edgesBetweeness));
		
		double sum = 0;
		for(int i = 0 ; i < edgesBetweeness.length; i++)
		{
			sum += edgesBetweeness[i];
		}
		
		return sum/(edgesBetweeness.length);
	}
}
