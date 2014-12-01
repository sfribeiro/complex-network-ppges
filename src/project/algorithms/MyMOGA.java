package project.algorithms;

import java.util.HashMap;

import javax.swing.JFrame;

import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.Ranking;
import project.MethaPopulation;
import project.gui.GenericMonitor;

public class MyMOGA extends MyAlgorithm {

	private static final long serialVersionUID = -8278394144577250395L;
//	protected MethaPopulation struct;
//	protected String[] demes_;
	protected String[] metrics_ = new String[] { "Spread", "Hypervolume", "GD",
			"IGD", "Epsilon" };

	public MyMOGA(Problem problem, boolean gui) {
		super(problem,gui);
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
		population = new SolutionSet(populationSize);
		for (int j = 0; j < populationSize; j++) {
			newSolution = new Solution(problem_);
			problem_.evaluate(newSolution);
			problem_.evaluateConstraints(newSolution);

			population.add(newSolution);
		}

		struct = new MethaPopulation(population,problem_,selection, mutation, crossover, "Full");
		population = struct.union();
		int numDemes = struct.getNumDemes();

		demes_ = new String[numDemes];
		for (int i = 0; i < numDemes; i++) {
			demes_[i] = "D" + i;
		}

		if (gui_) {
			startMonitor(metrics_);
		}

		// generations
		while (evaluations < maxEvaluations) {

			struct.executeByNSGAII();
			struct.migration(0.1);			
			evaluations += populationSize;
			population = struct.union();
			if(evaluations % 5000 == 0) struct.groupSolutions(population);

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

	/*
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
	}*/

}
