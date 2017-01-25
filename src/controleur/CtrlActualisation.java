package controleur;

import java.awt.event.*;
import javax.swing.*;
import model.Analyseur;
import view.*;
//import ihm.modele.*;

public class CtrlActualisation implements ActionListener {

	private JButton actu;
	private View v;
	private Analyseur analyseur;
	/*private CarnetAdresse c;*/


	public CtrlActualisation(JButton actu, View vu, Analyseur a) {
		v=vu;
    	this.actu=actu;
		this.analyseur=a;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(actu)){
			System.out.println(this.analyseur.getData());

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
