package project;

import java.util.HashMap;

import javax.swing.JFrame;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.Ranking;

public class MyGeneticAlgorithm extends Algorithm {

	private static final long serialVersionUID = -8278394144577250395L;
	protected GenericMonitor view_;
	protected boolean gui_ = false;
	protected MethaPopulation struct;
	protected String[] demes_;
	protected String[] metrics_ = new String[] { "Spread", "Hypervolume", "GD",
			"IGD", "Epsilon" };

	public MyGeneticAlgorithm(Problem problem, boolean gui) {
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

		populationSize = ((Integer) getInputParameter("populationSize"))
				.intValue();
		maxEvaluations = ((Integer) getInputParameter("maxEvaluations"))
				.intValue();
		indicators = (QualityIndicator) getInputParameter("indicators");

		evaluations = 0;
		requiredEvaluations = 0;

		selection = operators_.get("selection");
		mutation = operators_.get("mutation");
		crossover = operators_.get("crossover");

		Solution newSolution;
		SolutionSet set = new SolutionSet(populationSize);
		for (int j = 0; j < populationSize; j++) {
			newSolution = new Solution(problem_);
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);

			set.add(newSolution);
		}

		struct = new MethaPopulation(set,problem_,selection, mutation, crossover, "Full");
		population = struct.union();
		int numDemes = struct.getNumDemes();

		demes_ = new String[numDemes];
		for (int i = 0; i < numDemes; i++) {
			demes_[i] = "D" + i;
		}

		if (gui_) {
			startMonitor();
		}

		// generations
		while (evaluations < maxEvaluations) {

			struct.execute();
			struct.migration();
			evaluations += populationSize;
			population = struct.union();

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
				updateMonitor(struct, metricsResults, evaluations);
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
			updateMonitor(struct, metricsResults, evaluations);
		}

		return bestFront;
	}

	protected void startMonitor() {
		view_ = new GenericMonitor(this.getClass().getSimpleName(), demes_,
				metrics_, problem_);
		view_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view_.pack();
		view_.setLocationRelativeTo(null);
		view_.setVisible(true);
		view_.setSize(1400, 600);
	}

	protected void updateMonitor(MethaPopulation struct,
			HashMap<String, Double> metricsResults, int evaluations) {

		HashMap<String, Object> dados = new HashMap<String, Object>();
		for (int i = 0; i < demes_.length; i++) {
			dados.put(demes_[i], struct.getDeme(i));
		}

		for (String s : metricsResults.keySet()) {
			dados.put(s, metricsResults.get(s));
		}

		view_.update(dados, evaluations);
	}

}
