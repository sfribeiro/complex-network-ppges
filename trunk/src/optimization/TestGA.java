package optimization;

import gstream.GraphViewer;

import java.util.HashMap;

import utils.Laplacian;
import metrics.AlgebraicConnectivity;
import metrics.ClusteringCoefficient;
import metrics.Diameter;
import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.variable.Binary;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;

public class TestGA {
	
	private static int numNodes = 7;

	public static void main(String[] args) throws ClassNotFoundException, JMException {
		
		NSGAII algorithm = new NSGAII(new CCD("BinarySolution", numNodes));
		
		Operator crossover;
		Operator mutation;
		Operator selection;

		HashMap<String, Object> parameters;
		QualityIndicator indicators = null;

		algorithm.setInputParameter("populationSize", 30);
		algorithm.setInputParameter("maxEvaluations", 1000);
		algorithm.setInputParameter("indicators", indicators);

		parameters = new HashMap<String, Object>();
		parameters.put("probability", 0.9);
		parameters.put("distributionIndex", 20.0);
		crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover",
				parameters);

		parameters = new HashMap<String, Object>();
		parameters.put("probability", 1.0 / (numNodes * (numNodes -1)/2));
		parameters.put("distributionIndex", 20.0);
		mutation = MutationFactory.getMutationOperator("BitFlipMutation",
				parameters);

		parameters = null;
		selection = SelectionFactory.getSelectionOperator("BinaryTournament2",
				parameters);

		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);
		
		
		SolutionSet result = algorithm.execute();
		result.printFeasibleVAR("VAR");
		
		printSolutions(result);
	}
	
	public static void printSolutions(SolutionSet set)
	{
		Solution solution;
		ClusteringCoefficient CC = new ClusteringCoefficient();
		Diameter D = new Diameter();
		
		for(int i = 0; i < set.size(); i++){
			solution = set.get(i);
			Binary b = (Binary) solution.getDecisionVariables()[0];
			
			int size = numNodes;//(int) Math.sqrt(b.getNumberOfBits());
			
			double[][] m = new double[size][size];
			
			for (int x = 0; x < size; x++)
				for (int j = i; j < size; j++){
					if(x == j) 
						m[x][j] = 0;
					else if(b.getIth(x+j)){
						m[x][j] = 1;
						m[j][x] = 1;
					}
					else{
						m[x][j] = 0;
						m[j][x] = 0;
					}
				}
			
			GraphViewer graphViewer = new GraphViewer();
			
			for (int x = 0; x < m.length; x++) {
				graphViewer.addNode(x);
			}
			
			for (int x = 0; x < m.length; x++) {
				for (int j = 0; j < m.length; j++) {
					if(m[x][j] == 1 && m[j][x] == 1)
						graphViewer.addEdge(x, j);
				}
			}

			double cc =  (double) CC.calculate(m, graphViewer.getGrafo());
			double d = (double) D.calculate(m, graphViewer.getGrafo());
			
			AlgebraicConnectivity algCon = new AlgebraicConnectivity();
			double ac = (double)algCon.calculate(Laplacian.calculateDouble(m), null);
			
			if((cc >0 || d > 0) && ac > 1E-15){
				
				graphViewer.display();

				System.out.println("CC = " + cc);
				System.out.println("Diameter = " + d );
				System.out.println("AC = " + ac );
			}
			
		}
	}

}
