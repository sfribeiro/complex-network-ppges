package project.algorithms;

import java.util.HashMap;

import javax.swing.JFrame;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import project.gui.GenericMonitor;
import project.metapopulation.Metapopulation;

public abstract class MyAlgorithm extends Algorithm {

	protected GenericMonitor view_;
	protected boolean gui_ = false;
	protected Metapopulation struct;
	protected String[] demes_;
	
	public MyAlgorithm(Problem problem, boolean gui) {
		super(problem);
		this.gui_ = gui;
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

}
