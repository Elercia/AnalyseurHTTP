package controleur;

import java.awt.*;
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

        }

        if ((e.getSource()).equals(bpoid)){

        }

        if ((e.getSource()).equals(bcookie)){

        }

        if ((e.getSource()).equals(bsite)){

        }

        if ((e.getSource()).equals(bmethode)){

        }
    }
}
