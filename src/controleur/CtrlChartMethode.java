package controleur;

import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import model.Analyseur;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RefineryUtilities;
import view.*;

public class CtrlChartMethode implements ActionListener {

    private JButton /*bcap, bpagechar, bpoid, bcookie, bsite,*/ bmethode;
    private View v;
    private Analyseur analyseur;

    public CtrlChartMethode(/*JButton bcap, JButton bpagechar, JButton bpoid, JButton bcookie, JButton bsite, */JButton bmethode, View vu, Analyseur a) {
        v=vu;
        /*this.bcap=bcap;
        this.bpagechar=bpagechar;
        this.bpoid=bpoid;
        this.bcookie=bcookie;
        this.bsite=bsite;*/
        this.bmethode=bmethode;
        this.analyseur=a;
    }

    public void actionPerformed(ActionEvent e) {
        /*if ((e.getSource()).equals(bcap)){
            ChartTmpCapture demo = new ChartTmpCapture( "Mobile Sales" );
            demo.setSize( 560 , 367 );
            RefineryUtilities.centerFrameOnScreen( demo );
            demo.setVisible( true );
            demo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if ((e.getSource()).equals(bpagechar)){
            ChartNbChargedPage chart = new ChartNbChargedPage("Car Usage Statistics", "Which car do you like?");
            chart.pack( );
            RefineryUtilities.centerFrameOnScreen( chart );
            chart.setVisible( true );
            chart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if ((e.getSource()).equals(bpoid)){
            ChartWeightPage chart = new ChartWeightPage(
                    "School Vs Years" ,
                    "Numer of Schools vs years");

            chart.pack( );
            RefineryUtilities.centerFrameOnScreen( chart );
            chart.setVisible( true );
            chart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if ((e.getSource()).equals(bcookie)){
            ChartNbCookie chart = new ChartNbCookie("Browser Usage Statistics", "Which Browser are you using?");
            chart.pack( );
            RefineryUtilities.centerFrameOnScreen( chart );
            chart.setVisible( true );
            chart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if ((e.getSource()).equals(bsite)){
            ChartViewSite bubblechart = new ChartViewSite( "Bubble Chart_frame" );

            bubblechart.pack( );
            RefineryUtilities.centerFrameOnScreen( bubblechart );
            bubblechart.setVisible( true );
            bubblechart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }*/

        if ((e.getSource()).equals(bmethode)){


            try{
                System.out.println(this.analyseur.getData());
                HashMap<String, HashMap<String, Object>> data = this.analyseur.getData();

                HashMap<String, Object> methodeUsed = data.get("methodeUsed");
                HashMap<String, Object> nbPageCharged = data.get("nbPageCharged");
                HashMap<String, Object> poidPageCharged = data.get("poidPageCharged");
                HashMap<String, Object> nbCookiesCreated = data.get("nbCookiesCreated");

                System.out.println("methodeUsed : " + methodeUsed);
                System.out.println("nbPageCharged : " + nbPageCharged);
                System.out.println("poidPageCharged : " + poidPageCharged);
                System.out.println("nbCookiesCreated : " + nbCookiesCreated);

                ChartMethode methode = new ChartMethode( "methode", createDemoPanel(methodeUsed));
                methode.setSize( 560 , 367 );
                RefineryUtilities.centerFrameOnScreen( methode );
                methode.setVisible( true );
                methode.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            }catch(Exception exeption){
                JOptionPane.showMessageDialog(null,"recupération des données impossible","ERREUR",JOptionPane.ERROR_MESSAGE);
                exeption.printStackTrace();
            }
        }
    }

    private static PieDataset createDataset(HashMap<String, Object> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        //if(data.containsKey("GET")){
          //  dataset.setValue( "GET" , (long)data.get("GET"));
        //}
        for (Map.Entry<String,Object> entry : data.entrySet()) {
            dataset.setValue( entry.getKey() , (long)entry.getValue());
            
        }
//        dataset.setValue( "POST" , new Double( 11 ) );
//        dataset.setValue( "HEAD" , new Double( 11 ) );
//        dataset.setValue( "OPTIONS" , new Double( 11 ) );
//        dataset.setValue( "CONNECT" , new Double( 11 ) );
//        dataset.setValue( "TRACE" , new Double( 11 ) );
//        dataset.setValue("PUT", new Double(11));
//        dataset.setValue( "PATCH" , new Double( 11 ) );
//        dataset.setValue( "DELETE" , new Double( 12 ) );
        return dataset;
    }

    private static JFreeChart createChart(PieDataset dataset ) {
        JFreeChart chart = ChartFactory.createPieChart(
                "methode",  // chart title
                dataset,        // data
                true,           // include legend
                true,
                false);

        return chart;
    }

    public static JPanel createDemoPanel(HashMap<String, Object> data) {
        JFreeChart chart = createChart(createDataset(data) );
        return new ChartPanel( chart );
    }
}
