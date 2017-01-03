package view;

/**
 * Created by zapoutou on 13/12/16.
 */

// voir http://www.tutorialspoint.com/jfreechart/

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class Graph_bmethode extends JFrame
{
    public Graph_bmethode( String title )
    {
        super( title );
        setContentPane(createDemoPanel( ));
    }
    private static PieDataset createDataset( )
    {
        DefaultPieDataset dataset = new DefaultPieDataset( );
        dataset.setValue( "GET" , new Double( 11 ) );
        dataset.setValue( "POST" , new Double( 11 ) );
        dataset.setValue( "HEAD" , new Double( 11 ) );
        dataset.setValue( "OPTIONS" , new Double( 11 ) );
        dataset.setValue( "CONNECT" , new Double( 11 ) );
        dataset.setValue( "TRACE" , new Double( 11 ) );
        //if(true==false) {
            dataset.setValue("PUT", new Double(11));
        //}
        dataset.setValue( "PATCH" , new Double( 11 ) );
        dataset.setValue( "DELETE" , new Double( 12 ) );
        return dataset;
    }
    private static JFreeChart createChart( PieDataset dataset )
    {
        JFreeChart chart = ChartFactory.createPieChart(
                "methode",  // chart title
                dataset,        // data
                true,           // include legend
                true,
                false);

        return chart;
    }
    public static JPanel createDemoPanel( )
    {
        JFreeChart chart = createChart(createDataset( ) );
        return new ChartPanel( chart );
    }
}