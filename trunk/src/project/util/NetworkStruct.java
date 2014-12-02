package project.util;

import gstream.GraphViewer;

import java.util.HashMap;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;

public class NetworkStruct {

	private int[][] vizinhanca = null;
	HashMap<Solution, Integer> map = new HashMap<Solution, Integer>();
	
	public NetworkStruct(){}
	
	public void calculate(SolutionSet set) throws JMException
	{
		vizinhanca = new int[set.size()][set.size()];
		map.clear();
		for(int i = 0 ; i < set.size(); i++)
		{
			map.put(set.get(i), i);
		}
		
		Operator selection = SelectionFactory.getSelectionOperator("BinaryTournament2", null);
		
		for(int i = 0 ; i < set.size(); i++)
		{
			Solution current = set.get(i);
			Solution s = (Solution) selection.execute(set);
			
			int j = map.get(s);
			while(s == current || vizinhanca[i][j] == 1)
			{
				s = (Solution) selection.execute(set);
			}
			vizinhanca[i][j]=1;
			vizinhanca[j][i]=1;
		}
	}
	
	public void show()
	{
		int[][] m = vizinhanca;
		
		/* Graph Viewer BEGIN */
		GraphViewer graphViewer = new GraphViewer();

		for (int i = 0; i < m.length; i++) {
			graphViewer.addNode(i);
		}

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				if (m[i][j] == 1)
					graphViewer.addEdge(i, j);
			}
		}
		graphViewer.display();

		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				if (m[i][j] == 1) {
					graphViewer.highlightEdge(i, j, true);
				}
			}
		}
		/* Graph Viewer END */
	}
	
}
