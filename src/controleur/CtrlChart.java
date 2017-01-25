package controleur;

import java.awt.event.*;
import javax.swing.*;
import org.jfree.ui.RefineryUtilities;
import view.*;

public class CtrlChart implements ActionListener {

    private JButton bcap, bpagechar, bpoid, bcookie, bsite, bmethode;
    private View v;

    public CtrlChart(JButton bcap, JButton bpagechar, JButton bpoid, JButton bcookie, JButton bsite, JButton bmethode, View vu) {
        v=vu;
        this.bcap=bcap;
        this.bpagechar=bpagechar;
        this.bpoid=bpoid;
        this.bcookie=bcookie;
        this.bsite=bsite;
        this.bmethode=bmethode;
    }

    public void actionPerformed(ActionEvent e) {
        if ((e.getSource()).equals(bcap)){
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
        }

        if ((e.getSource()).equals(bmethode)){
            ChartMethode methode = new ChartMethode( "methode" );
            methode.setSize( 560 , 367 );
            RefineryUtilities.centerFrameOnScreen( methode );
            methode.setVisible( true );
            methode.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }
    }
}
