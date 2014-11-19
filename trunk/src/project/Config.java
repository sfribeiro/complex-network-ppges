package project;

import java.util.Random;
import jmetal.core.Problem;
import jmetal.problems.ZDT.ZDT1;

public class Config {
	public static Random random = new Random(-178);
	public static String DIR = "Results/";

	// GA
	public static int populationSize = 30;
	public static int archiveSize = 30;
	public static int maxEvaluations = 5000;
	public static int dimension = 100;
	public static Problem problem = new ZDT1("ArrayReal", populationSize);
}
