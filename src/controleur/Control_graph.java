package controleur;

import java.awt.event.*;
import javax.swing.*;
import org.jfree.ui.RefineryUtilities;
import view.*;

public class Control_graph implements ActionListener {

    private JButton bcap, bpagechar, bpoid, bcookie, bsite, bmethode;
    private Vue v;
	/*private CarnetAdresse c;*/


    public Control_graph(JButton bcap, JButton bpagechar, JButton bpoid, JButton bcookie, JButton bsite, JButton bmethode, Vue vu) {
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
            Graph_bcap demo = new Graph_bcap( "Mobile Sales" );
            demo.setSize( 560 , 367 );
            RefineryUtilities.centerFrameOnScreen( demo );
            demo.setVisible( true );
            demo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if ((e.getSource()).equals(bpagechar)){
            Graph_bpagechar chart = new Graph_bpagechar("Car Usage Statistics", "Which car do you like?");
            chart.pack( );
            RefineryUtilities.centerFrameOnScreen( chart );
            chart.setVisible( true );
            chart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if ((e.getSource()).equals(bpoid)){
            Graph_bpoid chart = new Graph_bpoid(
                    "School Vs Years" ,
                    "Numer of Schools vs years");

            chart.pack( );
            RefineryUtilities.centerFrameOnScreen( chart );
            chart.setVisible( true );
            chart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if ((e.getSource()).equals(bcookie)){
            Graph_bcookie chart = new Graph_bcookie("Browser Usage Statistics", "Which Browser are you using?");
            chart.pack( );
            RefineryUtilities.centerFrameOnScreen( chart );
            chart.setVisible( true );
            chart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if ((e.getSource()).equals(bsite)){
            Graph_bsite bubblechart = new Graph_bsite( "Bubble Chart_frame" );

            bubblechart.pack( );
            RefineryUtilities.centerFrameOnScreen( bubblechart );
            bubblechart.setVisible( true );
            bubblechart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        }

        if ((e.getSource()).equals(bmethode)){

        }
    }
}
