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

    private JButton bmethode;
    private View v;
    private Analyseur analyseur;

    public CtrlChartMethode(JButton bmethode, View vu, Analyseur a) {
        v=vu;
        this.bmethode=bmethode;
        this.analyseur=a;
    }

    public void actionPerformed(ActionEvent e) {
        if ((e.getSource()).equals(bmethode)){
            try{
                System.out.println(this.analyseur.getData());
                HashMap<String, HashMap<String, Object>> data = this.analyseur.getData();

                HashMap<String, Object> methodeUsed = data.get("methodeUsed");
                HashMap<String, Object> nbPageCharged = data.get("nbPagesCharged");
                HashMap<String, Object> poidPageCharged = data.get("poidPagesCharged");
                HashMap<String, Object> nbCookiesCreated = data.get("nbCookiesCreated");

                System.out.println("methodeUsed : " + methodeUsed);
                System.out.println("nbPageCharged : " + nbPageCharged);
                System.out.println("poidPageCharged : " + poidPageCharged);
                System.out.println("nbCookiesCreated : " + nbCookiesCreated);

                ViewChart methode = new ViewChart( "methode", createDemoPanel(methodeUsed));
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
        for (Map.Entry<String,Object> entry : data.entrySet()) {
            dataset.setValue( entry.getKey() , (long)entry.getValue());
        }
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
