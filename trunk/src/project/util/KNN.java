package project.util;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;

//recebe o conjunto de soluções e retorna um vetor de clusters das soluções
public class KNN {
	
	public static SolutionSet[] generate(int k, SolutionSet set)
	{
		return null;
	}
	
	public static SolutionSet[] generate(SolutionSet set)
	{
		Solution s = set.get(0);
		Problem p = s.getProblem();
		s.getObjective(p.getNumberOfObjectives()-1);
		return null;
	}

}
