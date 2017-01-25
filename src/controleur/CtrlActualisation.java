package controleur;

import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
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
			try{
				System.out.println(this.analyseur.getData());
				HashMap<String, HashMap<String, Object>> data = this.analyseur.getData();

				HashMap<String, Object> methodeUsed = data.get("methodeUsed");
				HashMap<String, Object> nbPageCharged = data.get("nbPagesCharged");
				HashMap<String, Object> poidPageCharged = data.get("poidPagesCharged");
				HashMap<String, Object> nbCookiesCreated = data.get("nbCookiesCreated");

				System.out.println("methodeUsed : " + methodeUsed);
				System.out.println("nbPageCharged : " + nbPageCharged);
				System.out.println("poidPageCharged : " + poidPageCharged);
				System.out.println("nbCookiesCreated : " + nbCookiesCreated);


				v.maj_cap("3");
				v.maj_pagecharge("4");
				v.maj_poid("5");
				v.maj_cookie("5");
				//v.maj_site(nbPageCharged.toString());
				v.maj_site("nbPageCharged.toString()");
				v.maj_methode(methodeUsed.toString());
				JOptionPane.showMessageDialog(null,"actualisé");
			}catch(Exception exeption){
				JOptionPane.showMessageDialog(null,"recupération des données impossible","ERREUR",JOptionPane.ERROR_MESSAGE);
				exeption.printStackTrace();
			}
			//JOptionPane.showMessageDialog(null,"erreur","FATAL ERROR 404", JOptionPane.ERROR_MESSAGE);
		}
	}
}
