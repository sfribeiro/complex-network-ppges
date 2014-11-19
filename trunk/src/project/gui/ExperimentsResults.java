package project.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

public class ExperimentsResults extends JFrame {

	private static final long serialVersionUID = 1L;

	//private static final String title = "Results Metrics";
	private Color[] colors = new Color[] { Color.black, Color.red, Color.blue,
			 Color.orange, Color.green };

	public ExperimentsResults(String title,HashMap<String, ArrayList<Double>> series) {
		super(title);

		final BoxAndWhiskerCategoryDataset dataset = createDataQualityIndicator(series);

		final CategoryAxis xAxis = new CategoryAxis("Metric");
		final NumberAxis yAxis = new NumberAxis("Value");
		yAxis.setAutoRangeIncludesZero(false);
		final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		renderer.setFillBox(false);
		renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
		for (int i = 0; i < series.size(); i++) {
			renderer.setSeriesPaint(i, colors[i]);
		}
		final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis,
				renderer);

		final JFreeChart chart = new JFreeChart("Experiments Results",
				new Font("SansSerif", Font.BOLD, 14), plot, true);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(450, 270));
		setContentPane(chartPanel);
	}

	private BoxAndWhiskerCategoryDataset createDataQualityIndicator(
			HashMap<String, ArrayList<Double>> series) {

		final DefaultBoxAndWhiskerCategoryDataset dataset = 
				new DefaultBoxAndWhiskerCategoryDataset();

		for (String s : series.keySet()) {

			dataset.add(series.get(s), s, s);

		}

		return dataset;
	}
}
