package controleur;

import java.awt.event.*;
import javax.swing.*;
import view.*;
//import ihm.modele.*;

public class Control_actu implements ActionListener {

	private JButton actu;
	private Vue v;
	/*private CarnetAdresse c;*/


	public Control_actu(JButton actu, Vue vu) {
		v=vu;
    this.actu=actu;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(actu)){
			v.maj_cap("3");
			v.maj_pagecharge("4");
			v.maj_poid("5");
			v.maj_cookie("5");
			v.maj_site("google");
			v.maj_methode("test");
			JOptionPane.showMessageDialog(null,"actualisé");
			//JOptionPane.showMessageDialog(null,"erreur","FATAL ERROR 404", JOptionPane.ERROR_MESSAGE);
		}
	}
}
