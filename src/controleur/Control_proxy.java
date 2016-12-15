package controleur;

import java.awt.event.*;
import javax.swing.*;
import view.*;
//import ihm.modele.*;

public class Control_proxy implements ActionListener {

	private JRadioButton oui;
  private JRadioButton non;
	private Vue v;
	/*private CarnetAdresse c;*/


	public Control_proxy(JRadioButton oui, JRadioButton non, Vue vu) {
		v=vu;
    this.oui=oui;
    this.non=non;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(oui)){
      v.question2_oui();
		}
    else{
      v.question2_non();
    }
	}
}
