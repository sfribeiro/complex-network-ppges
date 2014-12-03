package project.util;

import java.util.ArrayList;
import java.util.Random;

import project.Config;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.core.Variable;
import jmetal.util.JMException;

//recebe o conjunto de soluções e retorna um vetor de clusters das soluções
public class KNN {

	public static SolutionSet[] generate(int k, SolutionSet set) {
		SolutionSet[] setReturn = new SolutionSet[k];
		int localSize = (set.size() / k) + 1;

		for (int i = 0; i < setReturn.length; i++) {
			setReturn[i] = new SolutionSet(localSize);
		}

		Random rand = new Random();

		double limitMin = Double.MAX_VALUE;
		double limitMax = -Double.MAX_VALUE;

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
				centers[i][j] = rand.nextDouble() * (limitMax - limitMin)
						+ limitMin;
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
					distance += Math
							.pow(s.getObjective(j2) - centers[j][j2], 2);
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

	public static SolutionSet[] generate(SolutionSet set) {
		return generate(10, set);
	}

	public static ArrayList<SolutionSet> generateByDecision(int k,
			SolutionSet set) throws JMException {
		ArrayList<SolutionSet> resp = new ArrayList<SolutionSet>(k);

		int localSize = set.size();

		for (int i = 0; i < k; i++) {
			resp.add(new SolutionSet(localSize));
		}

		Random rand = Config.random;
		Problem problem = set.get(0).getProblem();

		double[] limitMin = new double[problem.getNumberOfVariables()];
		double[] limitMax = new double[problem.getNumberOfVariables()];

		for (int i = 0; i < problem.getNumberOfVariables(); i++) {
			limitMin[i] = problem.getLowerLimit(i);
			limitMax[i] = problem.getUpperLimit(i);
		}

		Solution s;

		double[][] centers = new double[k][problem.getNumberOfVariables()];

		// generate centers
		for (int i = 0; i < k; i++) {
			for (int j = 0; j < centers[i].length; j++) {
				centers[i][j] = rand.nextDouble() * (limitMax[j] - limitMin[j])
						+ limitMin[j];
			}
		}

		double avgError = Double.MAX_VALUE;
		double lastAvgError = Double.MAX_VALUE;

		double distance = 0;
		double minDistance;
		int centerIndex = 0;

		Variable[] variable;
		do {
			for (int i = 0; i < set.size(); i++) {
				s = set.get(i);

				variable = s.getDecisionVariables();
				minDistance = Double.MAX_VALUE;

				for (int j = 0; j < k; j++) {

					double value;
					for (int j2 = 0; j2 < variable.length; j2++) {
						value = variable[j2].getValue();

						distance += Math.pow(value - centers[j][j2], 2);
					}
					distance = Math.sqrt(distance);

					if (distance < minDistance) {
						minDistance = distance;
						centerIndex = j;
					}
				}
				resp.get(centerIndex).add(s);
			}

			double[][] newCenters = recalculateCenters(resp, problem);

			if(avgError < lastAvgError)
				lastAvgError = avgError;
			
			avgError = calculeVariance(centers, newCenters);

			centers = newCenters;

			System.out.println(avgError);

		} while (lastAvgError > 1.1*avgError);
		
		//distribution individuals in island empty
		SolutionSet excedents = new SolutionSet(3*set.size());
		for(SolutionSet ss : resp)
		{
			while(ss.size() > (localSize/k))
			{
				excedents.add(ss.get(0));
				ss.remove(0);
			}
		}
		
		for(SolutionSet ss : resp)
		{
			while(ss.size() < (localSize/k))
			{
				ss.add(excedents.get(0));
				excedents.remove(0);
			}
		}

		return resp;
	}

	private static double[][] recalculateCenters(ArrayList<SolutionSet> list,
			Problem p) throws JMException {
		SolutionSet ss;
		Solution s;
		Problem problem = p;

		int size = list.size();
		double[][] resp = new double[size][];
		double[] newValues;
		Variable[] v;
		int numVariables = problem.getNumberOfVariables();

		for (int q = 0; q < list.size(); q++) {
			ss = list.get(q);
			newValues = new double[problem.getNumberOfVariables()];

			if (ss.size() > 0) {

				for (int i = 0; i < ss.size(); i++) {
					s = ss.get(i);
					v = s.getDecisionVariables();
					for (int j = 0; j < v.length; j++) {
						newValues[j] += (v[j].getValue()) / numVariables;
					}
				}
			}
			else
			{
				Random rand = Config.random;
				double[] limitMin = new double[problem.getNumberOfVariables()];
				double[] limitMax = new double[problem.getNumberOfVariables()];

				for (int i = 0; i < problem.getNumberOfVariables(); i++) {
					limitMin[i] = problem.getLowerLimit(i);
					limitMax[i] = problem.getUpperLimit(i);
				}
				
				for (int j = 0; j < newValues.length; j++) {
					newValues[j] = rand.nextDouble() * (limitMax[j] - limitMin[j])
							+ limitMin[j];
				}
			}

			resp[q] = newValues;
		}

		return resp;
	}

	private static double calculeVariance(double[][] old, double[][] nnew) {
		double var = 0;
		for (int i = 0; i < old.length; i++) {
			double distance = 0;
			for (int j = 0; j < old[0].length; j++) {
				distance += Math.pow(old[i][j] - nnew[i][j], 2);
			}
			var += Math.sqrt(distance);
		}

		return var / (old[0].length);
	}
}
