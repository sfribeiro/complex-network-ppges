package project;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;
import jmetal.util.comparators.ObjectiveComparator;

public class MethaPopulation {

	private Problem problem;
	private int populationSize;
	private List<SolutionSet> demes;
	private double[] fitness;
	private int numDemes = 0;
	private boolean[][] vizinhos;
	private String type = "";

	private Operator selection;
	private Operator mutation;
	private Operator crossover;

	private double sum = 0;

	public MethaPopulation(SolutionSet set, Problem problem,
			Operator selection, Operator mutation, Operator crossover,
			String type) throws ClassNotFoundException, JMException {
		this.problem = problem;
		this.numDemes = 30;
		this.demes = new Vector<SolutionSet>(numDemes);
		this.populationSize = set.size() / numDemes;
		start(set);

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

	private void init(String type) {
		this.type = type;
		if (type.equals("BA")) {
			BA();
		} else if (type.equals("Full")) {
			Full();
		} else if (type.equals("Ring")) {
			Ring();
		}
	}

	private void BA() {
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
				}

				links++;
			}
		}

		vizinhos = resp;
	}

	private void Full() {

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

	private void Ring() {

		int numberNodes = numDemes;
		boolean[][] resp = new boolean[numberNodes][numberNodes];

		for (int node = 0; node < numberNodes - 1; node++) {
			resp[node+1][node] = true;
			resp[node][node+1] = true;
		}

		vizinhos = resp;
	}

	private void calculateDemes() {
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

		ranking.getNumberOfSubfronts();
	}

	private void start(SolutionSet set) {
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

	public void execute() throws JMException {
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

	//	init(type);
		start(union());
	}

	private int getNext() {
		double d = Config.random.nextDouble();
		double p = 0;

		for (int i = 0; i < demes.size(); i++) {
			p += (fitness[i] / sum);
			if (d <= p)
				return i;
		}

		return Config.random.nextInt(demes.size());
	}

	public void migration() throws JMException {
		SolutionSet deme;
		SolutionSet v;

		Operator s = new RouletteSelection(null);

		for (int i = 0; i < demes.size(); i++) {
			deme = demes.get(i);

			// if(Config.random.nextDouble() < 0.001){
			int conectividade = getVizinhos(i).size();

			for (int x = 0; x < conectividade; x++) {
				Solution s1 = (Solution) s.execute(deme);
				// deme.remove(s1);

				ArrayList<Integer> vizinhos = getVizinhos(i);
				int index = Config.random.nextInt(vizinhos.size());

				v = demes.get(vizinhos.get(index));
				Solution s2 = (Solution) s.execute(v);
				// v.remove(s2);

				deme.add(s2);
				v.add(s1);
			}
			// }
		}
	}

	private ArrayList<Integer> getVizinhos(int deme) {
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
