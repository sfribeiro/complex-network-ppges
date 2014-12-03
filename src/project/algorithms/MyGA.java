package project.algorithms;

import java.util.Comparator;
import java.util.HashMap;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;
import project.metapopulation.MetapopulationMono;

public class MyGA extends MyAlgorithm {

	private static final long serialVersionUID = -5791329308513901741L;
	protected String[] metrics_ = new String[] { "Fitness", "Average Fitness" };

	public MyGA(Problem problem, int numDemes, double migration, String model, boolean gui) {
		super(problem, numDemes, migration, model,gui);
	} // GGA

	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize;
		int maxEvaluations;
		int evaluations;

		SolutionSet population;
		// SolutionSet offspringPopulation;

		Operator mutationOperator;
		Operator crossoverOperator;
		Operator selectionOperator;

		Comparator comparator;
		comparator = new ObjectiveComparator(0); // Single objective comparator

		// Read the params
		populationSize = ((Integer) this.getInputParameter("populationSize"))
				.intValue();
		maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations"))
				.intValue();

		// Initialize the variables
		population = new SolutionSet(populationSize);
		// offspringPopulation = new SolutionSet(populationSize);

		evaluations = 0;

		// Read the operators
		mutationOperator = this.operators_.get("mutation");
		crossoverOperator = this.operators_.get("crossover");
		selectionOperator = this.operators_.get("selection");

		// Create the initial population
		Solution newIndividual;
		for (int i = 0; i < populationSize; i++) {
			newIndividual = new Solution(problem_);
			problem_.evaluate(newIndividual);
			evaluations++;
			population.add(newIndividual);
		} // for

		try {
			struct = new MetapopulationMono(population, problem_, numDemes,
					selectionOperator, mutationOperator, crossoverOperator,	model);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		population = struct.union();
		int numDemes = struct.getNumDemes();

		demes_ = new String[numDemes];
		for (int i = 0; i < numDemes; i++) {
			demes_[i] = "D" + i;
		}

		if (gui_) {
			startMonitor(metrics_);
		}

		// Sort population
		population.sort(comparator);
		while (evaluations < maxEvaluations) {

			((MetapopulationMono) struct).executeByGA();
			struct.migrationRandom(migration);
			//struct.migrationBest(migration);
			evaluations += populationSize;
			population = struct.union();
			//if (evaluations % 5000 == 0){
			//	struct.groupSolutions(population);
			//}

			population.sort(comparator);
			// metrics result
			HashMap<String, Double> metricsResults = new HashMap<String, Double>();

			double fitness = population.get(0).getObjective(0);
			double averageFitness = averageObjective(0, population);

			metricsResults.put("Fitness", fitness);
			metricsResults.put("Average Fitness", averageFitness);

			if (gui_) {
				updateMonitor(struct, metricsResults, evaluations);
			}

		} // while

		// metrics result
		HashMap<String, Double> metricsResults = new HashMap<String, Double>();

		population.sort(comparator);
		double fitness = population.get(0).getObjective(0);
		double averageFitness = averageObjective(0, population);

		metricsResults.put("Fitness", fitness);
		metricsResults.put("Average Fitness", averageFitness);

		if (gui_) {
			updateMonitor(struct, metricsResults, evaluations);
		}

		return population;
	} // execute

	/*
	 * protected void startMonitor() { view_ = new
	 * GenericMonitor(this.getClass().getSimpleName(), demes_, metrics_,
	 * problem_); view_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 * view_.pack(); view_.setLocationRelativeTo(null); view_.setVisible(true);
	 * view_.setSize(1400, 600); }
	 * 
	 * protected void updateMonitor(Metapopulation struct, HashMap<String,
	 * Double> metricsResults, int evaluations) {
	 * 
	 * HashMap<String, Object> dados = new HashMap<String, Object>(); for (int i
	 * = 0; i < demes_.length; i++) { dados.put(demes_[i], struct.getDeme(i)); }
	 * 
	 * for (String s : metricsResults.keySet()) { dados.put(s,
	 * metricsResults.get(s)); }
	 * 
	 * view_.update(dados, evaluations); }
	 */
}
