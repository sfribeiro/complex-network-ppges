package project.algorithms;

import java.util.HashMap;

import javax.swing.JFrame;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import project.gui.GenericMonitor;
import project.metapopulation.Metapopulation;

public abstract class MyAlgorithm extends Algorithm {

	protected GenericMonitor view_;
	protected boolean gui_ = false;
	protected Metapopulation struct;
	protected String[] demes_;
	protected double migration = 0;
	protected int numDemes = 0;
	protected String model = "";
	
	public MyAlgorithm(Problem problem, int numDemes, double migration, String model, boolean gui) {
		super(problem);
		this.gui_ = gui;
		this.migration = migration;
		this.numDemes = numDemes;
		this.model = model;
	}

	private static final long serialVersionUID = -8007449271086695126L;
	
	protected void startMonitor(String[] metrics) {
		view_ = new GenericMonitor(this.getClass().getSimpleName(), demes_,
				metrics, problem_);
		view_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view_.pack();
		view_.setLocationRelativeTo(null);
		view_.setVisible(true);
		view_.setSize(1400, 600);
	}

	protected void updateMonitor(Metapopulation struct,
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
	
	protected void updateMonitor(SolutionSet population,
			HashMap<String, Double> metricsResults, int evaluations) {

		HashMap<String, Object> dados = new HashMap<String, Object>();
		dados.put(demes_[0], population);


		for (String s : metricsResults.keySet()) {
			dados.put(s, metricsResults.get(s));
		}

		view_.update(dados, evaluations);
	}
	
	protected double averageObjective(int index, SolutionSet set)
	{
		double avg = 0;
		for(int i = 0; i < set.size(); i++)
		{
			avg += set.get(i).getObjective(index);
		}
		
		return avg/set.size();
	}

}
