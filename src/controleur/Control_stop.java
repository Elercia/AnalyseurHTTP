package controleur;

import java.awt.event.*;
import javax.swing.*;
import view.*;
//import ihm.modele.*;

public class Control_stop implements ActionListener {

	private JButton stop;
	private Vue v;
	/*private CarnetAdresse c;*/


	public Control_stop(JButton sp, Vue vu) {
		v=vu;
    stop=sp;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(stop)){

			v.stop();
		}
	}
}
