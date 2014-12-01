package project.algorithms;

import java.util.Comparator;
import java.util.HashMap;

import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;

public class MygGA extends MyAlgorithm {

	protected String[] metrics_ = new String[] { "Fitness", "Average Fitness" };

	public MygGA(Problem problem, boolean gui) {
		super(problem, gui);
	}

	private static final long serialVersionUID = -1777044651924111519L;

	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize;
		int maxEvaluations;
		int evaluations;

		SolutionSet population;
		SolutionSet offspringPopulation;

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
		offspringPopulation = new SolutionSet(populationSize);

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

		if (gui_) {
			startMonitor(metrics_);
		}

		// Sort population
		population.sort(comparator);
		while (evaluations < maxEvaluations) {
			if ((evaluations % 10) == 0) {
				System.out.println(evaluations + ": "
						+ population.get(0).getObjective(0));
			} //

			// Copy the best two individuals to the offspring population
			offspringPopulation.add(new Solution(population.get(0)));
			offspringPopulation.add(new Solution(population.get(1)));

			// Reproductive cycle
			for (int i = 0; i < (populationSize / 2 - 1); i++) {
				// Selection
				Solution[] parents = new Solution[2];

				parents[0] = (Solution) selectionOperator.execute(population);
				parents[1] = (Solution) selectionOperator.execute(population);

				// Crossover
				Solution[] offspring = (Solution[]) crossoverOperator
						.execute(parents);

				// Mutation
				mutationOperator.execute(offspring[0]);
				mutationOperator.execute(offspring[1]);

				// Evaluation of the new individual
				problem_.evaluate(offspring[0]);
				problem_.evaluate(offspring[1]);

				evaluations += 2;

				// Replacement: the two new individuals are inserted in the
				// offspring
				// population
				offspringPopulation.add(offspring[0]);
				offspringPopulation.add(offspring[1]);
			} // for

			// The offspring population becomes the new current population
			population.clear();
			for (int i = 0; i < populationSize; i++) {
				population.add(offspringPopulation.get(i));
			}
			offspringPopulation.clear();
			population.sort(comparator);

			// metrics result
			HashMap<String, Double> metricsResults = new HashMap<String, Double>();

			double fitness = population.get(0).getObjective(0);
			double averageFitness = 0;

			metricsResults.put("Fitness", fitness);
			metricsResults.put("Average Fitness", averageFitness);

			if (gui_) {
				updateMonitor(struct, metricsResults, evaluations);
			}
		} // while

		// // Return a population with the best individual
		// SolutionSet resultPopulation = new SolutionSet(1);
		// resultPopulation.add(population.get(0));
		//
		// System.out.println("Evaluations: " + evaluations);
		
		// metrics result		
		HashMap<String, Double> metricsResults = new HashMap<String, Double>();

		double fitness = population.get(0).getObjective(0);
		double averageFitness = 0;

		metricsResults.put("Fitness", fitness);
		metricsResults.put("Average Fitness", averageFitness);

		if (gui_) {
			updateMonitor(struct, metricsResults, evaluations);
		}

		return population;
	}

}
