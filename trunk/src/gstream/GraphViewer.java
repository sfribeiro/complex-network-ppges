package gstream;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

public class GraphViewer {
	
	private MultiGraph grafo;
	
	public GraphViewer() {
		init();
	}
	
	public void addNode(int i) {
		Node node = this.grafo.getNode(i);
		if (node == null) {
			this.grafo.addNode(i+"");
			this.grafo.getNode(i).addAttribute("ui.label", i+"");
		}
	}

	public void addEdge(int i, int j) {
		Edge edge = this.grafo.getEdge(i+"-"+j);
		if (edge == null) {
			this.grafo.addEdge(edgeName(i, j), i+"", j+"");
		}
		
	}

	public void highlightEdge(int i, int j, boolean set) {
		Edge edge = this.grafo.getEdge(i+"-"+j);
		if (edge != null) {
			if (set) {
				edge.addAttribute("ui.class", "influence");
			} else {
				edge.removeAttribute("ui.class");
			}
		} else {
			System.err.print("Edge not found");
		}
	}

	public void init() {
		this.grafo = new MultiGraph("GraphViewer");
		this.grafo.addAttribute("ui.stylesheet", "url('gstream/style.css')");
	}
	
	public void display() {
		this.grafo.display();		
	}

	public MultiGraph getGrafo() {
		return grafo;
	}

	public void setGrafo(MultiGraph grafo) {
		this.grafo = grafo;
	}
	
	private String edgeName(int i, int j) {
		return i+"-"+j;
	}

}
