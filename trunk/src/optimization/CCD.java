package optimization;

import org.graphstream.graph.implementations.MultiGraph;

import utils.Eigenvalues;
import utils.Laplacian;
import gstream.GraphViewer;
import metrics.AlgebraicConnectivity;
import metrics.ClusteringCoefficient;
import metrics.Diameter;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;

public class CCD extends Problem{

	private static final long serialVersionUID = 1L;
	private ClusteringCoefficient CC = new ClusteringCoefficient();
	private Diameter D = new Diameter();

	public CCD(String solutionType, Integer numberOfVariables) {
		numberOfVariables_ = 1;
		numberOfObjectives_ = 2;
		numberOfConstraints_ = 1;
		problemName_ = "CCD";
		length_ = new int[2];
		length_[0]= (numberOfVariables * (numberOfVariables - 1))/2;
		length_[1]= (numberOfVariables * (numberOfVariables - 1))/2;

		upperLimit_ = new double[numberOfVariables_];
		lowerLimit_ = new double[numberOfVariables_];

		// Establishes upper and lower limits for the variables
		for (int var = 0; var < numberOfVariables_; var++) {
			lowerLimit_[var] = 0.0;
			upperLimit_[var] = 1.0;
		} // for

		if (solutionType.compareTo("BinarySolution") == 0)
			solutionType_ = new BinarySolutionType(this);
		else {
			System.out.println("Error: solution type " + solutionType
					+ " invalid");
			System.exit(-1);
		}
	} 
	
	public void evaluate(Solution solution) throws JMException {
		Binary x = (Binary) solution.getDecisionVariables()[0];

		double[] f = new double[numberOfObjectives_];
		
		double[][] m = getMatrix(x);
		
		AlgebraicConnectivity algCon = new AlgebraicConnectivity();
		double ac = (double)algCon.calculate(Laplacian.calculateDouble(m), null);
		
		double f0 = Double.MIN_VALUE;
		double f1 = Double.MIN_VALUE;
		
		if (ac > 1E-15) {
			MultiGraph graph = getGraph(m);
			f0 =  (double) CC.calculate(m, graph);
			f1 =  (double) D.calculate(m, graph);
		}else
		{
			solution.setNumberOfViolatedConstraint(1);
		}
		
		// if(f0 > 0)
			f[0] = 1 / ( f0 + 1 );
		//else
		//	f[0] = 10;
		
		//if(f1 > 0)
			f[1] = 1 / ( f1 + 1 );
		//else
		//	f[1] = 10;

		//System.out.println("F0: " + f0 + " F1: " + f1);
		solution.setObjective(0, f[0]);
		solution.setObjective(1, f[1]);
	}
	
	private double[][] getMatrix(Binary x) throws JMException
	{
		int size = this.getSize(x);// (int) Math.sqrt(x.getNumberOfBits());

		double[][] resp = new double[size][size];
		
		for (int i = 0; i < size; i++)
			for (int j = i; j < size; j++){
				if(i == j) 
					resp[i][j] = 0;
				else if(x.getIth(i+j)){
					resp[i][j] = 1;
					resp[j][i] = 1;
				}
				else{
					resp[i][j] = 0;
					resp[j][i] = 0;
				}
			}
		
		return resp;
	}
	
	private MultiGraph getGraph(double[][] m)
	{
		GraphViewer graphViewer = new GraphViewer();
		
		for (int i = 0; i < m.length; i++) {
			graphViewer.addNode(i);
		}
		
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m.length; j++) {
				if(m[i][j] == 1)
					graphViewer.addEdge(i, j);
			}
		}
		
		return graphViewer.getGrafo();
	}
	
	public void evaluateConstraints(Solution solution) throws JMException {

		Binary x = (Binary) solution.getDecisionVariables()[0];
		
		double[][] m = getMatrix(x);
		
		if(Eigenvalues.calculate(m)[1] <= 0)
			solution.setNumberOfViolatedConstraint(1);
	}
	
	private int getSize(Binary x) {
		int numBits = x.getNumberOfBits();
		
		for (int i = 0; i < numBits - 1; i++) {
			int j = i + 1;
			if (i * j == numBits * 2) {
				return j;
			}
		}
		
		return 0;
	}

}
