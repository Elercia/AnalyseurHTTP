package controleur;

import java.awt.event.*;
import javax.swing.*;
import view.*;
//import ihm.modele.*;

public class Control_start implements ActionListener {

	private JButton start;
	private Vue v;
	/*private CarnetAdresse c;*/


	public Control_start(JButton st, Vue vu) {
		v=vu;
    start=st;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(start)){
      /*
      - transformation du bouton en pause
      - bouton stop apparait
      - demarage de la capture

      a ajouter : lien avec le modele
      - si pause alors start
      - si start alors pause
      */

			v.start();
		}
	}
}
