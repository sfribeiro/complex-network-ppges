package project.metapopulation;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.math3.distribution.PoissonDistribution;

import project.Config;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.ObjectiveComparator;

public class MetapopulationMono extends Metapopulation {
	
	double elitismo = 0.1;

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
			offspringPopulation = new SolutionSet(demeSize);

			// Sort population
			population.sort(new ObjectiveComparator(0));// Single objective
														// comparator

			// Copy the best two individuals to the offspring population
			//offspringPopulation.add(new Solution(population.get(0)));
			//offspringPopulation.add(new Solution(population.get(1)));
			for(int i = 0; i < elitismo * population.size(); i++){
				offspringPopulation.add(new Solution(population.get(i)));
			}

			// Reproductive cycle
			//for (int i = 0; i < (demeSize / 2 - 1); i++) {
			for (; offspringPopulation.size() < population.size();) {
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
				if(offspringPopulation.size() < offspringPopulation.getCapacity())
				offspringPopulation.add(offspring[1]);
			} // for

			// The offspring population becomes the new current population
			population.clear();
			for (int i = 0; i < demeSize; i++) {
				population.add(offspringPopulation.get(i));
			}
			offspringPopulation.clear();
			population.sort(new ObjectiveComparator(0));
		}

		//groupSolutions(union());
		//if(type.equals("BA"))
		//{
		//	BA(2);
		//}
		
	}
	
	public void migrationBest(double tax) throws JMException {
		SolutionSet deme;
		SolutionSet vizinho;

		int out = -1, in = -1;
		Solution sOut, sIn;
		
		Ranking ranking;
		
		for (int i = 0; i < demes.size(); i++) {
			deme = demes.get(i);

			ArrayList<Integer> vizinhos = getVizinhos(i);
			double e = vizinhos.size() * tax * deme.size();

			PoissonDistribution poisson = new PoissonDistribution(e);
			int emigrantes = poisson.inverseCumulativeProbability(Config.random
					.nextDouble());
			
			if(emigrantes > deme.size()) emigrantes = deme.size();

			for (int n = 0; n < emigrantes; n++) {

				ranking = new Ranking(deme);
				SolutionSet front = ranking.getSubfront(0);
				
				sOut = front.get(Config.random.nextInt(front.size()));
				deme.remove(out);

				int nVizinho = Config.random.nextInt(vizinhos.size());
				vizinho = demes.get(nVizinho);
				in = Config.random.nextInt(vizinho.size());
				sIn = vizinho.get(in);
				vizinho.remove(in);

				deme.add(sIn);
				vizinho.add(sOut);
			}
		}
	}

	
	protected void calculateDemes() {
		Ranking ranking = new Ranking(union());
		Problem problem = ranking.getSubfront(0).get(0).getProblem();

		sum = 0;
		fitness = new double[demes.size()];
		
		for (int d = 0; d < demes.size(); d++) {
			SolutionSet set = demes.get(d);

			double f = 0;

			for (int i = 0; i < set.size(); i++) {
				for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
					f += set.get(j).getObjective(0);
				}
			}

			fitness[d] = f / (set.size());

			sum += fitness[d];
		}

	//	System.out.println(Arrays.toString(fitness));
	}
}
