package project.algorithms;

import java.util.HashMap;

import javax.swing.JFrame;

import project.MethaPopulation;
import project.gui.GenericMonitor;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Distance;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import jmetal.util.comparators.CrowdingComparator;

public class NSGAII extends Algorithm {

	private static final long serialVersionUID = 1983278493438573045L;
	protected GenericMonitor view_;
	protected boolean gui_ = false;
	protected String[] demes_;
	protected String[] algorithms_ = new String[] { "GA"};
	protected String[] metrics_ = new String[] { "Spread", "Hypervolume", "GD",
			"IGD", "Epsilon" };

	public NSGAII(Problem problem, boolean gui) {
		super(problem);
		this.gui_ = gui;
	}

	@Override
	public SolutionSet execute() throws JMException, ClassNotFoundException {
		int populationSize;
		int maxEvaluations;
		int evaluations;

		QualityIndicator indicators;
		int requiredEvaluations;

		Operator selection;
		Operator mutation;
		Operator crossover;

		SolutionSet population;
		SolutionSet offspringPopulation;
		SolutionSet union;

		Distance distance = new Distance();

		populationSize = ((Integer) getInputParameter("populationSize"))
				.intValue();
		maxEvaluations = ((Integer) getInputParameter("maxEvaluations"))
				.intValue();
		indicators = (QualityIndicator) getInputParameter("indicators");

		population = new SolutionSet(populationSize);
		evaluations = 0;
		requiredEvaluations = 0;

		selection = operators_.get("selection");
		mutation = operators_.get("mutation");
		crossover = operators_.get("crossover");

		// generate initial population
		Solution newSolution;
		for (int i = 0; i < populationSize; i++) {
			newSolution = new Solution(problem_);
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);
			evaluations++;

			population.add(newSolution);
		}

		// log_.append("Start Population", true);

		// Monitor
		if (gui_)
			startMonitor();

		// generations
		while (evaluations < maxEvaluations) {

			offspringPopulation = new SolutionSet(populationSize);
			Solution[] parents = new Solution[2];

			for (int i = 0; i < (population.size() / 2); i++) {
				parents[0] = (Solution) selection.execute(population);
				parents[1] = (Solution) selection.execute(population);

				Solution[] offspring = (Solution[]) crossover.execute(parents);

				mutation.execute(offspring[0]);
				mutation.execute(offspring[1]);

				problem_.evaluate(offspring[0]);
				problem_.evaluateConstraints(offspring[0]);
				problem_.evaluate(offspring[1]);
				problem_.evaluateConstraints(offspring[1]);

				offspringPopulation.add(offspring[0]);
				offspringPopulation.add(offspring[1]);

				evaluations += 2;
			}

			// create the solutionSet union of solutionSet with offspring
			union = population.union(offspringPopulation);

			Ranking ranking = new Ranking(union);

			int remain = populationSize;
			int index = 0;
			SolutionSet front = null;
			population.clear();

			front = ranking.getSubfront(index);

			// TODO: define selected
			while ((remain > 0) && (remain >= front.size())) {
				distance.crowdingDistanceAssignment(front,
						problem_.getNumberOfObjectives());

				// Add individuals this front
				for (int k = 0; k < front.size(); k++) {
					population.add(front.get(k));
				}

				remain = remain - front.size();

				index++;
				if (remain > 0) {
					front = ranking.getSubfront(index);
				}
			}

			if (remain > 0) {

				distance.crowdingDistanceAssignment(front,
						problem_.getNumberOfObjectives());
				front.sort(new CrowdingComparator());

				for (int k = 0; k < remain; k++) {
					population.add(front.get(k));
				}

				remain = 0;

			}

			// metrics result
			HashMap<String, Double> metricsResults = new HashMap<String, Double>();

			if ((indicators != null) && (requiredEvaluations == 0)) {
				double HV = indicators.getHypervolume(population);
				if (HV >= (0.98 * indicators.getTrueParetoFrontHypervolume())) {
					requiredEvaluations = evaluations;
				}

				double spread = indicators.getSpread(population);
				double hypervolume = indicators.getHypervolume(population);
				double gd = indicators.getGD(population);
				double igd = indicators.getIGD(population);
				double epsilon = indicators.getEpsilon(population);

				metricsResults.put("Spread", spread);
				metricsResults.put("Hypervolume", hypervolume);
				metricsResults.put("GD", gd);
				metricsResults.put("IGD", igd);
				metricsResults.put("Epsilon", epsilon);
			}

			if (gui_) {
				updateMonitor(population, metricsResults, evaluations);
			}
		}

		setOutputParameter("evaluations", requiredEvaluations);

		Ranking ranking = new Ranking(population);
		SolutionSet bestFront = ranking.getSubfront(0);

		// metrics result
		HashMap<String, Double> metricsResults = new HashMap<String, Double>();

		if (indicators != null) {
			double spread = indicators.getSpread(population);
			double hypervolume = indicators.getHypervolume(population);
			double gd = indicators.getGD(population);
			double igd = indicators.getIGD(population);
			double epsilon = indicators.getEpsilon(population);

			metricsResults.put("Spread", spread);
			metricsResults.put("Hypervolume", hypervolume);
			metricsResults.put("GD", gd);
			metricsResults.put("IGD", igd);
			metricsResults.put("Epsilon", epsilon);
		}

		if (gui_) {
			updateMonitor(population, metricsResults, evaluations);
		}

		return bestFront;
	}
	
	protected void startMonitor() {
		view_ = new GenericMonitor(this.getClass().getSimpleName(), algorithms_, metrics_, problem_);
		view_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view_.pack();
		view_.setLocationRelativeTo(null);
		view_.setVisible(true);
		view_.setSize(1400, 600);
	}

	protected void updateMonitor(SolutionSet population, 
			HashMap<String, Double> metricsResults,	int evaluations) {

		HashMap<String, Object> dados = new HashMap<String, Object>();
		dados.put("GA", population);

		for(String s: metricsResults.keySet())
		{
			dados.put(s, metricsResults.get(s));
		}

		view_.update(dados, evaluations);
	}

}
