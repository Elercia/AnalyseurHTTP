package controleur;

import java.awt.event.*;
import javax.swing.*;
import view.*;
//import ihm.modele.*;

public class CtrlConfiguration implements ActionListener {

	private JRadioButton auto;
  	private JRadioButton man;
	private View v;
	/*private CarnetAdresse c;*/


	public CtrlConfiguration(JRadioButton auto, JRadioButton man, View vu) {
		v=vu;
    this.auto=auto;
    this.man=man;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(auto)){
      		v.ConfigAuto();
		}
    	else{
      		v.ConfigMan();
    	}
	}
}
