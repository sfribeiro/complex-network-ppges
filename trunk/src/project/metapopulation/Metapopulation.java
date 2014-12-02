package project.metapopulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.apache.commons.math3.distribution.PoissonDistribution;

import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.spea2.SPEA2;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.Spea2Fitness;
import jmetal.util.comparators.CrowdingComparator;
import jmetal.util.comparators.ObjectiveComparator;
import project.Config;

public class Metapopulation {

	protected Problem problem;
	protected int populationSize = 0;
	protected List<SolutionSet> demes;
	protected double[] fitness;
	protected int numDemes = 0;
	protected boolean[][] vizinhos;
	protected String type = "";

	protected Operator selection;
	protected Operator mutation;
	protected Operator crossover;

	protected double sum = 0;

	public Metapopulation(SolutionSet set, Problem problem, Operator selection,
			Operator mutation, Operator crossover, String type)
			throws ClassNotFoundException, JMException {
		this.problem = problem;
		// TODO define number
		this.numDemes = 100;
		this.demes = new Vector<SolutionSet>(numDemes);
		this.populationSize = set.size() / numDemes;
		groupSolutions(set);

		this.fitness = new double[numDemes];
		this.vizinhos = new boolean[numDemes][numDemes];

		this.selection = selection;
		this.mutation = mutation;
		this.crossover = crossover;

		init(type);
	}

	public Metapopulation(SolutionSet set, Problem problem, int numDemes,
			Operator selection, Operator mutation, Operator crossover,
			String type) throws ClassNotFoundException, JMException {
		this.problem = problem;
		// TODO define number
		this.numDemes = numDemes;
		this.demes = new Vector<SolutionSet>(numDemes);
		this.populationSize = set.size() / numDemes;
		groupSolutions(set);

		this.fitness = new double[numDemes];
		this.vizinhos = new boolean[numDemes][numDemes];

		this.selection = selection;
		this.mutation = mutation;
		this.crossover = crossover;

		init(type);
	}

	public SolutionSet getDeme(int index) {
		return demes.get(index);
	}

	public int getNumDemes() {
		return numDemes;
	}

	protected void init(String type) {
		this.type = type;
		if (type.equals("BA")) {
			BA();
		} else if (type.equals("Full")) {
			Full();
		} else if (type.equals("Ring")) {
			Ring();
		} else {
			Full();
		}
	}

	protected void BA() {
		calculateDemes();

		int numberNodes = numDemes;
		int m = 2;
		boolean[][] resp = new boolean[numberNodes][numberNodes];

		for (int node = 0; node < numberNodes; node++) {
			int node2 = -1;
			int links = 0;

			while (links < m) {
				node2 = getNext();
				if (node2 != node && !resp[node][node2]) {
					resp[node2][node] = true;
					resp[node][node2] = true;
					
					links++;
				}				
			}
		}

		vizinhos = resp;
	}

	protected void Full() {

		int numberNodes = numDemes;
		boolean[][] resp = new boolean[numberNodes][numberNodes];

		for (int node = 0; node < numberNodes - 1; node++) {
			for (int node2 = node + 1; node2 < numberNodes; node2++) {
				resp[node2][node] = true;
				resp[node][node2] = true;
			}
		}

		vizinhos = resp;
	}

	protected void Ring() {

		int numberNodes = numDemes;
		boolean[][] resp = new boolean[numberNodes][numberNodes];

		for (int node = 0; node < numberNodes - 1; node++) {
			resp[node + 1][node] = true;
			resp[node][node + 1] = true;
		}
		resp[0][numberNodes - 1] = true;
		resp[numberNodes - 1][0] = true;

		vizinhos = resp;
	}

	// calculate average fitness in each deme
	protected void calculateDemes() {
		Ranking ranking = new Ranking(union());
		Problem problem = ranking.getSubfront(0).get(0).getProblem();

		sum = 0;
		for (int d = 0; d < demes.size(); d++) {
			SolutionSet set = demes.get(d);

			double f = 0;

			for (int i = 0; i < set.size(); i++) {
				for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
					f += set.get(j).getRank();
				}
			}

			fitness[d] = f / (set.size());

			sum += fitness[d];
		}

		System.out.println(Arrays.toString(fitness));
	}

	// create demes
	// TODO
	public void groupSolutions(SolutionSet set) {
		demes.clear();

		set.sort(new ObjectiveComparator(0));

		int i = set.size();
		for (int d = 0; d < numDemes; d++) {
			SolutionSet deme = new SolutionSet(populationSize);
			for (int j = 0; j < populationSize; j++)
				deme.add(set.get(--i));
			demes.add(deme);
		}
	}

	public void executeByNSGAII() throws JMException {
		SolutionSet offspringPopulation;
		SolutionSet union;
		Distance distance = new Distance();

		for (SolutionSet deme : demes) {

			offspringPopulation = new SolutionSet(populationSize);
			Solution[] parents = new Solution[2];

			for (int i = 0; i < (populationSize / 2); i++) {

				parents[0] = (Solution) selection.execute(deme);
				parents[1] = (Solution) selection.execute(deme);

				Solution[] offspring = (Solution[]) crossover.execute(parents);

				mutation.execute(offspring[0]);
				mutation.execute(offspring[1]);

				problem.evaluate(offspring[0]);
				problem.evaluateConstraints(offspring[0]);
				problem.evaluate(offspring[1]);
				problem.evaluateConstraints(offspring[1]);

				offspringPopulation.add(offspring[0]);
				offspringPopulation.add(offspring[1]);
			}

			// create the solutionSet union of solutionSet with offspring
			union = deme.union(offspringPopulation);

			Ranking ranking = new Ranking(union);

			int remain = populationSize;
			int index = 0;
			SolutionSet front = null;
			deme.clear();

			front = ranking.getSubfront(index);

			while ((remain > 0) && (remain >= front.size())) {
				distance.crowdingDistanceAssignment(front,
						problem.getNumberOfObjectives());

				// Add individuals this front
				for (int k = 0; k < front.size(); k++) {
					deme.add(front.get(k));
				}

				remain = remain - front.size();

				index++;
				if (remain > 0) {
					front = ranking.getSubfront(index);
				}
			}

			if (remain > 0) {

				distance.crowdingDistanceAssignment(front,
						problem.getNumberOfObjectives());
				front.sort(new CrowdingComparator());

				for (int k = 0; k < remain; k++) {
					deme.add(front.get(k));
				}

				remain = 0;
			}
		}

		// init(type);
		groupSolutions(union());
	}

	// TODO
	public void executeBySPEA2() throws JMException, ClassNotFoundException {
		int archiveSize, maxEvaluations, evaluations;
		SolutionSet solutionSet, archive, offSpringSolutionSet;

		archiveSize = populationSize * demes.size();

		// Initialize the variables
		solutionSet = new SolutionSet(populationSize);
		archive = new SolutionSet(archiveSize);

		// -> Create the initial solutionSet
		Solution newSolution;
		for (int i = 0; i < populationSize; i++) {
			newSolution = new Solution(problem);
			problem.evaluate(newSolution);
			problem.evaluateConstraints(newSolution);
			solutionSet.add(newSolution);
		}

		SolutionSet union = solutionSet.union(archive);
		Spea2Fitness spea = new Spea2Fitness(union);
		spea.fitnessAssign();
		archive = spea.environmentalSelection(archiveSize);

		// Create a new offspringPopulation
		offSpringSolutionSet = new SolutionSet(populationSize);
		Solution[] parents = new Solution[2];
		while (offSpringSolutionSet.size() < populationSize) {
			int j = 0;
			do {
				j++;
				parents[0] = (Solution) selection.execute(archive);
			} while (j < SPEA2.TOURNAMENTS_ROUNDS); // do-while
			int k = 0;
			do {
				k++;
				parents[1] = (Solution) selection.execute(archive);
			} while (k < SPEA2.TOURNAMENTS_ROUNDS); // do-while

			// make the crossover
			Solution[] offSpring = (Solution[]) crossover.execute(parents);
			mutation.execute(offSpring[0]);
			problem.evaluate(offSpring[0]);
			problem.evaluateConstraints(offSpring[0]);
			offSpringSolutionSet.add(offSpring[0]);

		} // while
			// End Create a offSpring solutionSet
		solutionSet = offSpringSolutionSet;

		Ranking ranking = new Ranking(archive);
		groupSolutions(union());
	}

	protected int getNext() {
		double d = Config.random.nextDouble();
		double p = 0;

		for (int i = 0; i < demes.size(); i++) {
			p += (fitness[i] / sum);
			if (d <= p)
				return i;
		}

		return Config.random.nextInt(demes.size());
	}

	public void migrationRandom(double tax) throws JMException {
		SolutionSet deme;
		SolutionSet vizinho;

		int out = -1, in = -1;
		Solution sOut, sIn;

		for (int i = 0; i < demes.size(); i++) {
			deme = demes.get(i);

			ArrayList<Integer> vizinhos = getVizinhos(i);
			double e = vizinhos.size() * tax * deme.size();

			PoissonDistribution poisson = new PoissonDistribution(e);
			int emigrantes = poisson.inverseCumulativeProbability(Config.random
					.nextDouble());
			
			if(emigrantes > deme.size()) emigrantes = deme.size();

			for (int n = 0; n < emigrantes; n++) {

				out = Config.random.nextInt(deme.size());
				sOut = deme.get(out);
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

	protected ArrayList<Integer> getVizinhos(int deme) {
		ArrayList<Integer> resp = new ArrayList<Integer>();

		for (int i = 0; i < numDemes; i++) {
			if (vizinhos[deme][i])
				resp.add(i);
		}

		return resp;
	}

	public SolutionSet union() {
		SolutionSet resp = new SolutionSet(demes.size() * populationSize);
		for (SolutionSet set : demes) {
			resp = resp.union(set);
		}

		return resp;
	}
}
