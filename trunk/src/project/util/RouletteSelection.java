package project.util;

import java.util.HashMap;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.selection.Selection;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

public class RouletteSelection extends Selection {

	public RouletteSelection(HashMap<String, Object> parameters) {
		super(parameters);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public Object execute(Object object) throws JMException {
		SolutionSet population = (SolutionSet) object;
		double sumFitness = 0;
		
		Solution s = null;
		for (int i = 0; i< population.size(); i++) {
			s = population.get(i);
			sumFitness += s.getFitness();
		}
		
		double o = PseudoRandom.randDouble(0, sumFitness);
		double current = 0;
		
		Solution resp = null;
		for (int i = 0; i< population.size(); i++) {
			s = population.get(i);
			
			if(current < o){
				resp = s;
				population.remove(i);
				break;
			}
			
			current += s.getFitness();
			
		}
		
		return resp;
	}

}
