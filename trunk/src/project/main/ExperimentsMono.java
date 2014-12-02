package project.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.singleObjective.Griewank;
import jmetal.problems.singleObjective.Rosenbrock;
import jmetal.util.JMException;

import org.jfree.ui.RefineryUtilities;

import project.Config;
import project.algorithms.MyGA;
import project.algorithms.MyGAoriginal;

public class ExperimentsMono {

	public static void main(String[] args) throws ClassNotFoundException {

		Config.maxEvaluations = 100000;
		Config.populationSize = 1000;
		Config.archiveSize = Config.populationSize;
		Config.dimension = 100;

		Problem problem = 
				new Griewank("Real", Config.dimension);
		//		new Rosenbrock("Real", Config.dimension);
		Algorithm algorithm = 
				new MyGA(problem, false);
		//		new MygGA(problem, false);

		try {

			ExperimentsMono e = new ExperimentsMono();
			e.execute(algorithm, problem, 30);

			for (int i = 0; i < e.fitnessList.size(); i++) {
				if (e.fitnessList.get(i).isNaN()) {
					e.fitnessList.remove(i);
					e.averageFitnessList.remove(i);
					i--;
				}
			}
			for (int i = 0; i < e.averageFitnessList.size(); i++) {
				if (e.averageFitnessList.get(i).isNaN()) {
					e.fitnessList.remove(i);
					e.averageFitnessList.remove(i);
					i--;
				}
			}

			String fileName = algorithm.getClass().getSimpleName() + "_"
					+ Config.populationSize + "_" + problem.getClass().getSimpleName() + "_"
					+ Config.maxEvaluations;
			e.toFile(fileName);

			HashMap<String, ArrayList<Double>> map = new HashMap<String, ArrayList<Double>>();
			map.put("hyperVolume", e.fitnessList);
			map.put("spread", e.averageFitnessList);

			// show results
			final project.gui.ExperimentsResults demo = new project.gui.ExperimentsResults(
					"Results from " + algorithm.getClass().getSimpleName()
							+ " in " + problem.getClass().getSimpleName(), map);
			demo.pack();
			demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			RefineryUtilities.centerFrameOnScreen(demo);
			demo.setVisible(true);
			demo.setSize(600, 600);

		} catch (Exception erro) {
			erro.getStackTrace();
		}
	}

	static final int[] seeds = new int[] { -1294378929, -205851191, 1915828043,
			-852597972, -2083975738, -761202340, -199583166, -1165248267,
			1046169348, 964389015, 1072368027, 594192429, 984963210,
			-365907976, -1080383453, 880806849, 1853954776, -1959961379,
			1876680320, 2091192809, 1702977386, 1218257258, 219208508,
			216054960, 1623474404, 1490399920, 2039513124, -1838520653,
			162382475, 1510925579, -222539565, -305573848, -1698139401,
			995470027, -2006042999, 166819874, -1527708154, -513943275,
			-77714112, -847362542, 927799828, -1011820789, 1217211905,
			1664075665, 718052456, -2047501526, -356259213, 1037534811,
			1509458160, -437216524 };

	public void execute(Algorithm algorithm, Problem problem, int numAmostras)
			throws ClassNotFoundException, JMException, IOException,
			InstantiationException, IllegalAccessException {

		for (int i = 0; i < numAmostras; i++) {
			int seed = seeds[i];

			System.out.println("Amostra " + (i + 1) + " seed=" + seed);
			jmetal.util.PseudoRandom
					.setRandomGenerator(new project.util.MyRandom(seed));

			// algorithm = new MyNSGAII(problem, false);

			loadSetup(algorithm);

			SolutionSet population = algorithm.execute();
			// population.printObjectivesToFile(Config.DIR+"ObjectivesGA_"+i);
			// population.printVariablesToFile(Config.DIR+"VariablesGA_"+i);
			
			double best = population.get(0).getObjective(0);
			double avg = calculateAverage(population);

			// add list indicators
			fitnessList.add(best);
			averageFitnessList.add(avg);

		}

		// print
		for (int p = 0; p < fitnessList.size(); p++) {
			System.out.println((fitnessList.get(p) + " " + averageFitnessList
					.get(p)).replace(".", ","));
		}

	}
	
	private double calculateAverage(SolutionSet set)
	{
		double resp = 0;
		for(int i =0; i < set.size(); i++)
		{
			resp += set.get(i).getObjective(0);
		}
		
		return resp/set.size();
	}

	String DIR = "Results\\";
	public void toFile(String nameFile) throws IOException {
		FileWriter fw = new FileWriter(DIR + nameFile + ".csv");

		fw.write("Fitness;Average Fitness\n");

		for (int i = 0; i < fitnessList.size(); i++) {

			String line = (fitnessList.get(i) + ";" + averageFitnessList.get(i));
			fw.write(line);
			fw.write("\n");
		}

		fw.close();
	}

	private void loadSetup(Algorithm algorithm) throws JMException {

		Problem problem = Config.problem;

		Operator crossover;
		Operator mutation;
		Operator selection;
		// Operator diversity;

		HashMap<String, Object> parameters;

		algorithm.setInputParameter("populationSize", Config.populationSize);
		algorithm.setInputParameter("maxEvaluations", Config.maxEvaluations);
		algorithm.setInputParameter("archiveSize", Config.archiveSize);

		parameters = new HashMap<String, Object>();
		parameters.put("probability", 0.9);
		parameters.put("distributionIndex", 20.0);
		crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover",
				parameters);

		parameters = new HashMap<String, Object>();
		parameters.put("probability", 1.0 / problem.getNumberOfVariables());
		parameters.put("distributionIndex", 20.0);
		mutation = MutationFactory.getMutationOperator("PolynomialMutation",
				parameters);

		parameters = null;
		selection = SelectionFactory.getSelectionOperator("BinaryTournament2",
				parameters);

		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);
	}

	protected ArrayList<Double> fitnessList = new ArrayList<Double>();
	protected ArrayList<Double> averageFitnessList = new ArrayList<Double>();

}
