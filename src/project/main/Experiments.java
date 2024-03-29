package project.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.DTLZ.DTLZ1;
import jmetal.problems.DTLZ.DTLZ2;
import jmetal.problems.DTLZ.DTLZ7;
import jmetal.problems.ZDT.ZDT4;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.qualityIndicator.fastHypervolume.FastHypervolume;
import jmetal.util.JMException;

import org.jfree.ui.RefineryUtilities;

import project.Config;
import project.algorithms.MyMOGA;

public class Experiments {
	
	//static Problem problem; // DTLZ1("Real", 100, 2);
	//static Algorithm algorithm;

	public static void main(String[] args) {

		Config.maxEvaluations = 100000;
		Config.populationSize = 1000;
		Config.archiveSize = Config.populationSize;
		Config.dimension = 10;
		
		Problem problem = 
			//	new ZDT4("ArrayReal",Config.dimension);
			// new ZDT1("ArrayReal",Config.dimension); 
			//	new DTLZ1("Real", Config.dimension, 2);
				new DTLZ7("Real", Config.dimension, 2);
		
		Algorithm algorithm =
				new project.algorithms.MyNSGAII(problem, true);
				//new MyMOGA(problem, true);
		
		try {
			
			Experiments e = new Experiments();					
			e.execute(algorithm, problem, "ParetoFronts/" + problem.getName() + ".txt", 5);

			HashMap<String, ArrayList<Double>> map = new HashMap<String, ArrayList<Double>>();

			for (int i = 0; i < e.hypervolumeList.size(); i++) {
				if (e.hypervolumeList.get(i).isNaN()) {
					e.hypervolumeList.remove(i);
					e.spreadList.remove(i);
					e.GDList.remove(i);
					e.IGDList.remove(i);
					i--;
				}
			}
			for (int i = 0; i < e.spreadList.size(); i++) {
				if (e.spreadList.get(i).isNaN()) {
					e.hypervolumeList.remove(i);
					e.spreadList.remove(i);
					e.GDList.remove(i);
					e.IGDList.remove(i);
					i--;
				}
			}
			
			String fileName = algorithm.getClass().getSimpleName() + "_"
					+ Config.populationSize + "_" + problem.getName() + "_"
					+ Config.maxEvaluations ;
			e.toFile(fileName);

			map.put("GD", e.GDList);
			map.put("IGD", e.IGDList);
			map.put("hyperVolume", e.hypervolumeList);
			map.put("spread", e.spreadList);

			// show results
			final project.gui.ExperimentsResults demo = new project.gui.ExperimentsResults(
					"Results from " + algorithm.getClass().getSimpleName()
							+ " in " + problem.getClass().getSimpleName(), map);
			demo.pack();
			demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			RefineryUtilities.centerFrameOnScreen(demo);
			demo.setVisible(true);
			demo.setSize(600, 600);

		} catch (Exception erro) {
			erro.getStackTrace();
		}
	}

	static final int[] seeds = new int[] { -1294378929, -205851191, 1915828043,
			-852597972, -2083975738, -761202340, -199583166, -1165248267,
			1046169348, 964389015, 1072368027, 594192429, 984963210,
			-365907976, -1080383453, 880806849, 1853954776, -1959961379,
			1876680320, 2091192809, 1702977386, 1218257258, 219208508,
			216054960, 1623474404, 1490399920, 2039513124, -1838520653,
			162382475, 1510925579, -222539565, -305573848, -1698139401,
			995470027, -2006042999, 166819874, -1527708154, -513943275,
			-77714112, -847362542, 927799828, -1011820789, 1217211905,
			1664075665, 718052456, -2047501526, -356259213, 1037534811,
			1509458160, -437216524 };

	public void execute(Algorithm algorithm, Problem problem, String fileParetoTrue, int numAmostras)
			throws ClassNotFoundException, JMException, IOException,
			InstantiationException, IllegalAccessException {
		QualityIndicator indicators;

		Solution base = new Solution(problem);
		base.setObjective(0, 11);
		base.setObjective(1, 11);
		
		for (int i = 0; i < numAmostras; i++) {
			int seed = seeds[i];

			System.out.println("Amostra " + (i + 1) + " seed=" + seed);
			jmetal.util.PseudoRandom.setRandomGenerator(new project.util.MyRandom(seed));

			//algorithm = new MyNSGAII(problem, false);

			loadSetup(algorithm, fileParetoTrue);
			indicators = (QualityIndicator) algorithm
					.getInputParameter("indicators");

			SolutionSet population = algorithm.execute();
			// population.printObjectivesToFile(Config.DIR+"ObjectivesGA_"+i);
			// population.printVariablesToFile(Config.DIR+"VariablesGA_"+i);
			
			//add list indicators
			
			if (indicators != null) {
				FastHypervolume hv = new FastHypervolume();
				//hypervolumeList.add(indicators.getHypervolume(population));
				hypervolumeList.add(hv.computeHypervolume(population, base));
				spreadList.add(indicators.getSpread(population));
				GDList.add(indicators.getGD(population));
				IGDList.add(indicators.getIGD(population));
			}
		}

		//print
		for(int p = 0; p < hypervolumeList.size(); p++)
		{
			System.out.println((hypervolumeList.get(p)+" "+ spreadList.get(p)).replace(".", ","));
		}

	}
	
	String DIR = "Results\\";

	public void toFile(String nameFile) throws IOException {
		FileWriter fw = new FileWriter(DIR + nameFile + ".csv");

		fw.write("Hypervolume;Spread;GD;IGD\n");

		for (int i = 0; i < hypervolumeList.size(); i++) {

			String line = (hypervolumeList.get(i) + ";" + spreadList.get(i) + ";"
					+ GDList.get(i) + ";" + IGDList.get(i));
			fw.write(line);
			fw.write("\n");
		}

		fw.close();
	}

	private void loadSetup(Algorithm algorithm, String file) throws JMException {

		Problem problem = Config.problem;

		Operator crossover;
		Operator mutation;
		Operator selection;
		//Operator diversity;

		HashMap<String, Object> parameters;
		QualityIndicator indicators = null;
		if (file != null && new File(file).exists())
			indicators = new QualityIndicator(problem, file);

		algorithm.setInputParameter("populationSize", Config.populationSize);
		algorithm.setInputParameter("maxEvaluations", Config.maxEvaluations);
		algorithm.setInputParameter("indicators", indicators);
		algorithm.setInputParameter("archiveSize", Config.archiveSize);

		parameters = new HashMap<String, Object>();
		parameters.put("probability", 0.9);
		parameters.put("distributionIndex", 20.0);
		crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover",
				parameters);

		parameters = new HashMap<String, Object>();
		parameters.put("probability", 1.0 / problem.getNumberOfVariables());
		parameters.put("distributionIndex", 20.0);
		mutation = MutationFactory.getMutationOperator("PolynomialMutation",
				parameters);

		parameters = null;
		selection = SelectionFactory.getSelectionOperator("BinaryTournament2",
				parameters);

		algorithm.addOperator("crossover", crossover);
		algorithm.addOperator("mutation", mutation);
		algorithm.addOperator("selection", selection);

		// return algorithm;
		// return null;
	}
	
	protected ArrayList<Double> hypervolumeList = new ArrayList<Double>();
	protected ArrayList<Double> spreadList = new ArrayList<Double>();
	protected ArrayList<Double> GDList = new ArrayList<Double>();
	protected ArrayList<Double> IGDList = new ArrayList<Double>();
}
