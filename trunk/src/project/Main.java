package project;

import java.util.HashMap;

import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.DTLZ.DTLZ1;
import jmetal.problems.singleObjective.Griewank;
import jmetal.qualityIndicator.QualityIndicator;

public class Main {

	public static void main(String[] args) {
		try {
			Problem problem =
					//new Griewank("Real",100);
					new DTLZ1("Real",100,2);
					//new ZDT1("ArrayReal",100);

			Operator crossover;
			Operator mutation;
			Operator selection;

			HashMap<String, Object> parameters;
			QualityIndicator indicators = null;// new QualityIndicator(problem, "ParetoFronts/"+problem.getClass().getSimpleName()+".txt");

			MyGeneticAlgorithm algorithm = new MyGeneticAlgorithm(problem,true);
			algorithm
					.setInputParameter("populationSize", 300);
			algorithm
					.setInputParameter("maxEvaluations", 40000);
			algorithm.setInputParameter("indicators", indicators);

			parameters = new HashMap<String, Object>();
			parameters.put("probability", 0.9);
			parameters.put("distributionIndex", 20.0);
			crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover",
					parameters);

			parameters = new HashMap<String, Object>();
			parameters.put("probability", 1.0 / problem.getNumberOfVariables());
			parameters.put("distributionIndex", 20.0);
			mutation = MutationFactory.getMutationOperator(
					"PolynomialMutation", parameters);

			parameters = null;
			selection = SelectionFactory.getSelectionOperator(
					"BinaryTournament2", parameters);

			algorithm.addOperator("crossover", crossover);
			algorithm.addOperator("mutation", mutation);
			algorithm.addOperator("selection", selection);			
			
			SolutionSet result = algorithm.execute();
			result.printObjectivesToFile("objectives");
			result.printVariablesToFile("Variables");
			
			/*NetworkStruct ns = new NetworkStruct();
			ns.calculate(result);
			ns.show();*/
			
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

}
