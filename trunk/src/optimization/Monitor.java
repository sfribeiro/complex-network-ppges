package optimization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class Monitor extends JFrame {
	private static int COUNT = 1;

	private static final long serialVersionUID = 1L;
	private static final String title = "Genetic Algorithm";
	private XYSeries ga = new XYSeries("Algorithm");

	public Monitor(String s) {
		super(s + " Amostra " + COUNT++ );
		final ChartPanel chartPanel = createPanelEvolutionAlgorithm();

		this.add(chartPanel, BorderLayout.CENTER);

		JPanel control = new JPanel();
		this.add(control, BorderLayout.SOUTH);
	}

	private ChartPanel createPanelEvolutionAlgorithm() {
		JFreeChart jfreechart = ChartFactory.createScatterPlot(title, "Diameter",
				"Clustering Coefficient", createDataEvolutionAlgorithm(), PlotOrientation.VERTICAL, true, true,
				false);

		XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);

		XYItemRenderer renderer = xyPlot.getRenderer();
		renderer.setSeriesPaint(0, Color.blue);		

		NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
		domain.setVerticalTickLabels(true);
		domain.setRange(-1.0, TestGA.numNodes);
		domain.setTickUnit(new NumberTickUnit(1));
		domain.setVerticalTickLabels(true);
		
		NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
		range.setRange(-0.1, 1.2);
		range.setTickUnit(new NumberTickUnit(0.1));

		return new ChartPanel(jfreechart);
	}

	private XYDataset createDataEvolutionAlgorithm() {
		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		
		xySeriesCollection.addSeries(ga);

		return xySeriesCollection;
	}

	public void update(SolutionSet setGA) {
		// XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		// XYSeries series = new XYSeries("GA");
		ga.clear();

		Solution s;
		for (int i = 0; i < setGA.size(); i++) {
			s = setGA.get(i);
			double x = s.getObjective(0);
			double y = s.getObjective(1);
			ga.add(x , y);
			//System.out.println(x + "," + y);
		}
		

		try {
			Thread.sleep(200);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// up.doClick();
		// added = series;
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				Monitor demo = new Monitor(title);
				demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				demo.pack();
				demo.setLocationRelativeTo(null);
				demo.setVisible(true);
				demo.setSize(1200, 400);
			}
		});
	}
}
