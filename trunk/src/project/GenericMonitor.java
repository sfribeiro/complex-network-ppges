package project;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GenericMonitor extends JFrame {
	private static int COUNT = 1;

	private static final long serialVersionUID = 1L;
	private static final String title = "Monitor";
	//private Problem problem;

	private HashMap<String, XYSeries> algorithmResults;
	private HashMap<String, XYSeries> metricsResults;
	
	/*private Color[] colors = new Color[] { 
			Color.black, 
			Color.red, 
			Color.blue,
			Color.orange, 
			Color.green,
			Color.cyan,
			Color.magenta,
			Color.pink,
			Color.YELLOW,
			Color.WHITE};*/

	public GenericMonitor(String title, String[] algorithms, String[] metrics, Problem problem) {
		super(title + " Amostra " + COUNT++);
		//this.problem = problem;
		algorithmResults = new HashMap<String, XYSeries>();
		metricsResults = new HashMap<String, XYSeries>();
		
		final ChartPanel chartPanel = createPanelAlgorithms(algorithms);
		chartPanel.setSize(200,200);
		
		final ChartPanel chartPanelQualityIndicator = createPanelMetrics(metrics);
		chartPanelQualityIndicator.setSize(200, 200);

		this.add(chartPanel, BorderLayout.WEST);
		this.add(chartPanelQualityIndicator, BorderLayout.EAST);

		JPanel control = new JPanel();
		control.setSize(50,50);
		this.add(control, BorderLayout.SOUTH);		
	}

	private ChartPanel createPanelAlgorithms(String[] algorithms) {
		JFreeChart jfreechart = ChartFactory.createScatterPlot(
				"Monitor Algorithm", "F1", "F2", createData(algorithms,true),
				PlotOrientation.VERTICAL, true, true, false);
		
		XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);

		/*XYItemRenderer renderer = xyPlot.getRenderer();

		ShapeList sList = new ShapeList();
		
		for (int i = 0; i < algorithms.length; i++) {
			renderer.setSeriesPaint(i, colors[i]);
			renderer.setSeriesShape(i, sList.getShape(i));
		}*/

		return new ChartPanel(jfreechart);
	}

	private ChartPanel createPanelMetrics(String[] metrics) {
		JFreeChart jfreechart = ChartFactory.createScatterPlot(
				"Metrics values", "Gerations", "Valor", createData(metrics,false),
				PlotOrientation.VERTICAL, true, true, false);

		XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);

		/*	XYItemRenderer renderer = xyPlot.getRenderer();

		for (int i = 0; i < metrics.length; i++) {
			renderer.setSeriesPaint(i, colors[i]);
		}*/

		return new ChartPanel(jfreechart);
	}

	private XYDataset createData(String[] data, boolean isAlgorithm) {
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

		for (String name : data) {
			XYSeries s = new XYSeries(name);
			s.clear();
			xySeriesCollection.addSeries(s);
			if(isAlgorithm)
			{
				algorithmResults.put(name,s);
			}
			else
			{
				metricsResults.put(name,s);
			}
		}

		return xySeriesCollection;
	}

	public void update(HashMap<String, Object> dados, int geracao) {
		
		for(String name : algorithmResults.keySet())
		{
			if(dados.containsKey(name))
			{
				Object o = dados.get(name);
				
				if(o == null) continue;
				
				XYSeries serie = algorithmResults.get(name);
				
				if(o instanceof SolutionSet)
				{					
					serie.clear();
					
					SolutionSet set = (SolutionSet) o;
					Solution s;
					for (int i = 0; i < set.size(); i++) {
						s = set.get(i);
						double x = s.getObjective(0);
						double y = s.getObjective(1);
						serie.add(x, y);
					}
				}
				else if(o instanceof Number)
				{
					serie.add(geracao,(double)o);
				}
				else if(o instanceof ArrayList)
				{
					serie.clear();
					@SuppressWarnings("unchecked")
					ArrayList<double[]> list = (ArrayList<double[]>) o;
					double[] value;
					for (int i = 0; i < list.size(); i++) {
						value = list.get(i);
						double x = value[0];
						double y = value[1];
						serie.add(x, y);
					}
				}
				else
				{
					System.err.println(o.getClass() + " não tratada!");
				}
			}
		}
		
		for(String name : metricsResults.keySet())
		{
			if(dados.containsKey(name))
			{
				Object o = dados.get(name);
				XYSeries serie = metricsResults.get(name);
				
				if(o instanceof SolutionSet && serie != null)
				{					
					serie.clear();
					
					SolutionSet set = (SolutionSet) o;
					Solution s;
					for (int i = 0; i < set.size(); i++) {
						s = set.get(i);
						double x = s.getObjective(0);
						double y = s.getObjective(1);
						serie.add(x, y);
					}
				}
				else if(o instanceof Number)
				{
					serie.add(geracao,(double)o);
				}
			}
		}
		
		try {
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				GenericMonitor demo = 
						new GenericMonitor(
								title,
								new String[]{"GA"}, 
								new String[]{"Hypervolume","Spread"},
								null);
				demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				demo.pack();
				demo.setLocationRelativeTo(null);
				demo.setVisible(true);
				demo.setSize(1400, 600);
			}
		});
	}
}
