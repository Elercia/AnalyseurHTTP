package controleur;

import java.awt.event.*;
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
				HashMap<String, Object> nbPagesCharged = data.get("nbPagesCharged");
				HashMap<String, Object> poidPageCharged = data.get("poidPagesCharged");
				HashMap<String, Object> nbCookiesCreated = data.get("nbCookiesCreated");

				System.out.println("methodeUsed : " + methodeUsed);
				System.out.println("nbPageCharged : " + nbPagesCharged);
				System.out.println("poidPageCharged : " + poidPageCharged);
				System.out.println("nbCookiesCreated : " + nbCookiesCreated);

				this.nbPageCharged(nbPagesCharged);
				this.pageLeMostCharge(nbPagesCharged);
				this.nbCookiesCreated(nbCookiesCreated);
				this.poidPageCharged(poidPageCharged, nbPagesCharged);
				//v.maj_cap("3");

				//v.maj_site(nbPagesCharged.toString());
				v.maj_methode(methodeUsed.toString());
				JOptionPane.showMessageDialog(null,"actualisé");

			}catch(Exception exeption){
				JOptionPane.showMessageDialog(null,"recupération des données impossible","ERREUR",JOptionPane.ERROR_MESSAGE);
				exeption.printStackTrace();
			}
		}
	}

	public void poidPageCharged(HashMap<String, Object> poidPageCharged, HashMap<String, Object> nbPagesCharged){
		String expr="";
		for (String s : poidPageCharged.keySet()) {
			if(nbPagesCharged.containsKey(s)){
				long val =(long)poidPageCharged.get(s)/(long)nbPagesCharged.get(s);
				expr = expr + s +" : "+ val + ", ";
			}
		}
		if(!expr.isEmpty()){
			v.maj_poid(expr);
		}else{
			v.maj_poid("ERREUR");
		}
	}

	public void nbCookiesCreated(HashMap<String, Object> nbCookiesCreated){
		v.maj_cookie(nbCookiesCreated.toString());
	}

	public void pageLeMostCharge(HashMap<String, Object> nbPagesCharged){
		long max=0;
		String strMax ="";
		for (String s : nbPagesCharged.keySet()) {
			if(max < (long)nbPagesCharged.get(s)){
				max = (long)nbPagesCharged.get(s);
				strMax= s+" -> " + nbPagesCharged.get(s)+" ";
			}
		}
		if(!strMax.isEmpty()){
			v.maj_site(strMax);
		}else{
			v.maj_site("ERREUR");
		}
	}

	public void nbPageCharged(HashMap<String, Object> nbPagesCharged){
		String expr="";
		long res=0;
		for (String s : nbPagesCharged.keySet()) {
			res+=(long)nbPagesCharged.get(s);
		}
		expr="->"+res;
		v.maj_pagecharge(expr);
	}


}
