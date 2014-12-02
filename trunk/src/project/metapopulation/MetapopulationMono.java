package project.metapopulation;

import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;

public class MetapopulationMono extends Metapopulation {

	public MetapopulationMono(SolutionSet set, Problem problem, int numDemes,
			Operator selection, Operator mutation, Operator crossover,
			String type) throws ClassNotFoundException, JMException {
		super(set, problem, numDemes, selection, mutation, crossover, type);
	}

	public void executeByGA() throws JMException {

		for (SolutionSet deme : demes) {
			
			SolutionSet population;
			SolutionSet offspringPopulation;

			// Initialize the variables
			population = deme;
			offspringPopulation = new SolutionSet(populationSize);

			// Sort population
			population.sort(new ObjectiveComparator(0));// Single objective
														// comparator

			// Copy the best two individuals to the offspring population
			offspringPopulation.add(new Solution(population.get(0)));
			offspringPopulation.add(new Solution(population.get(1)));

			// Reproductive cycle
			for (int i = 0; i < (populationSize / 2 - 1); i++) {
				// Selection
				Solution[] parents = new Solution[2];

				parents[0] = (Solution) selection.execute(population);
				parents[1] = (Solution) selection.execute(population);

				// Crossover
				Solution[] offspring = (Solution[]) crossover.execute(parents);

				// Mutation
				mutation.execute(offspring[0]);
				mutation.execute(offspring[1]);

				// Evaluation of the new individual
				problem.evaluate(offspring[0]);
				problem.evaluate(offspring[1]);

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
			population.sort(new ObjectiveComparator(0));
		}

		groupSolutions(union());
	}
}
