package view;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RefineryUtilities;

public class Graph_bsite extends JFrame
{
    public Graph_bsite( String s )
    {
        super( s );
        JPanel jpanel = createDemoPanel( );
        jpanel.setPreferredSize(new Dimension( 560 , 370 ) );
        setContentPane( jpanel );
    }

    private static JFreeChart createChart( XYZDataset xyzdataset )
    {
        JFreeChart jfreechart = ChartFactory.createBubbleChart(
                "AGE vs WEIGHT vs WORK",
                "Weight",
                "AGE",
                xyzdataset,
                PlotOrientation.HORIZONTAL,
                true, true, false);

        XYPlot xyplot = ( XYPlot )jfreechart.getPlot( );
        xyplot.setForegroundAlpha( 0.65F );
        XYItemRenderer xyitemrenderer = xyplot.getRenderer( );
        xyitemrenderer.setSeriesPaint( 0 , Color.blue );
        NumberAxis numberaxis = ( NumberAxis )xyplot.getDomainAxis( );
        numberaxis.setLowerMargin( 0.2 );
        numberaxis.setUpperMargin( 0.5 );
        NumberAxis numberaxis1 = ( NumberAxis )xyplot.getRangeAxis( );
        numberaxis1.setLowerMargin( 0.8 );
        numberaxis1.setUpperMargin( 0.9 );

        return jfreechart;
    }

    public static XYZDataset createDataset( )
    {
        DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset();

        double ad[ ] = { 30 , 40 , 50 , 60 , 70 , 80 };
        double ad1[ ] = { 10 , 20 , 30 , 40 , 50 , 60 };
        double ad2[ ] = { 4 , 5 , 10 , 8 , 9 , 6 };
        double ad3[][] = { ad , ad1 , ad2 };
        defaultxyzdataset.addSeries( "Series 1" , ad3 );

        return defaultxyzdataset;
    }

    public static JPanel createDemoPanel( )
    {
        JFreeChart jfreechart = createChart( createDataset( ) );
        ChartPanel chartpanel = new ChartPanel( jfreechart );

        chartpanel.setDomainZoomable( true );
        chartpanel.setRangeZoomable( true );

        return chartpanel;
    }
}