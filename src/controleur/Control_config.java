package controleur;

import java.awt.event.*;
import javax.swing.*;
import view.*;
//import ihm.modele.*;

public class Control_config implements ActionListener {

	private JRadioButton auto;
  private JRadioButton man;
	private Vue v;
	/*private CarnetAdresse c;*/


	public Control_config(JRadioButton auto, JRadioButton man, Vue vu) {
		v=vu;
    this.auto=auto;
    this.man=man;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(auto)){
      v.question1_non();
		}
    else{
      v.question1_oui();
    }
	}
}
