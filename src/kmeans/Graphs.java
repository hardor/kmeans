
package kmeans;

import java.awt.Color;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

/**
 *
 * @author hardor
 */
public class Graphs extends Main {
    
    private final int numclusters;
    
    public Graphs(int nClusters){        
        this.numclusters = nClusters;       
    }
    
    public XYSeriesCollection CreateXYDataset(double[][] data, int[] labels, double[][] centroides){
        
        XYSeries[] series = new XYSeries[numclusters+1];
        
        
        for(int k = 0; k < numclusters; k++){
            series[k] = new XYSeries("Кластер "+(k+1));
        }
        
        for(int i = 0; i<labels.length; i++){
            series[labels[i]].add(data[i][0], data[i][1]);
        }
        
        
        series[numclusters] = new XYSeries("Центроид");
        for(int i=0; i<centroides.length;i++){
             series[numclusters].add(centroides[i][0], centroides[i][1]);
        }
        
       
        XYSeriesCollection dataGraph = new XYSeriesCollection();
        
        for(int k=0; k<series.length; k++){
            dataGraph.addSeries(series[k]);
        }
              
        return dataGraph;
        
    }
    
    public void showGraphic(XYSeriesCollection data, int iteration, int exec){
        
        JFreeChart chart = createChart(data);
        TextTitle title = new TextTitle("Выполнение " + exec + " | Итерация " + iteration);
        chart.addSubtitle(title);
        chart.setBackgroundPaint(Color.decode("#FAFAD2"));
        Plot plot = chart.getPlot();
        plot.setBackgroundPaint(Color.white);
        
        ChartFrame chartPanel = new ChartFrame("График",chart);
        //ChartPanel chartPanel = new ChartPanel(chart);
        //chartPanel.setVerticalAxisTrace(true);
        //chartPanel.setHorizontalAxisTrace(true);
        // popup menu conflicts with axis trace
        //chartPanel.setPopupMenu(null);
       
        //chartPanel.setDomainZoomable(true);
        //chartPanel.setRangeZoomable(true);
        
        //chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        //setContentPane(chartPanel);
        chartPanel.setResizable(true);
        chartPanel.pack();
        //chartPanel.setVisible(true);
        try {        
            ChartUtilities.saveChartAsPNG(new File("data/" + exec + "-" + iteration + ".png"), chart, 1000, 500);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
       
    }
    
    private JFreeChart createChart(XYSeriesCollection data){
        
        JFreeChart chart =  ChartFactory.createScatterPlot("Распределение кластеров", "X1", "X2", data,PlotOrientation.VERTICAL, true, false, false);
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setNoDataMessage("NO DATA");
        plot.setDomainZeroBaselineVisible(true);
        plot.setRangeZeroBaselineVisible(true);
        
        XYLineAndShapeRenderer render = (XYLineAndShapeRenderer) plot.getRenderer();
        
        // -- para os centroides --
        render.setSeriesOutlinePaint(numclusters, Color.GREEN);
        Shape meuShape = ShapeUtilities.createDiagonalCross(7, 0.5f);
        render.setSeriesShape(numclusters, meuShape);

        // -- preenchimento dos pontos --
        render.setSeriesPaint(0, Color.yellow);
        
        render.setBaseFillPaint(Color.yellow);

        render.setUseOutlinePaint(true);
        NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setAutoRangeIncludesZero(false);
        domainAxis.setTickMarkInsideLength(2.0f);
        domainAxis.setTickMarkOutsideLength(0.0f);
       
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickMarkInsideLength(2.0f);
        rangeAxis.setTickMarkOutsideLength(0.0f);
        
        return chart;
    }
}
