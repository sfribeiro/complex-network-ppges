package project.util;

import java.util.Random;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;

//recebe o conjunto de soluções e retorna um vetor de clusters das soluções
public class KNN {
	
	public static SolutionSet[] generate(int k, SolutionSet set)
	{
		SolutionSet[] setReturn = new SolutionSet[k];
		
		for (int i = 0; i < setReturn.length; i++) {
			setReturn[i] = new SolutionSet();
		}
		
		Random rand = new Random();
		
		double limitMin = Double.MAX_VALUE;
		double limitMax = Double.MIN_VALUE;

		Solution s;
		
		double objective = 0;
		
		for (int i = 0; i < set.size(); i++) {
			 s = set.get(i);
			 for (int j = 0; j < s.getNumberOfObjectives(); j++) {
				objective = s.getObjective(j);
				if (objective < limitMin) {
					limitMin = objective;
				}
				if (objective > limitMax) {
					limitMax = objective;
				}
			}
		}
		
		double[][] centers = new double[k][set.get(0).getNumberOfObjectives()];
		
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < centers[i].length; j++) {
				centers[i][j] = rand.nextDouble() * (limitMax - limitMin) + limitMin;
			}
		}
		
		double distance = 0;
		double minDistance;
		int centerIndex = 0;
		
		for (int i = 0; i < set.size(); i++) {
			s = set.get(i);
			for (int j = 0; j < k; j++) {
				minDistance = Double.MAX_VALUE;
				for (int j2 = 0; j2 < s.getNumberOfObjectives(); j2++) {
					distance += Math.pow(s.getObjective(j2) - centers[j][j2], 2);
				}
				distance = Math.sqrt(distance);
				if (distance < minDistance) {
					minDistance = distance; 
					centerIndex = i;
				}
			}
			setReturn[centerIndex].add(s);
		}
		
		return setReturn;
	}
	
	public static SolutionSet[] generate(SolutionSet set)
	{
		return generate(10, set);
	}

}
