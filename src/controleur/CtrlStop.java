package controleur;

import java.awt.event.*;
import java.util.ArrayList;

import model.Analyseur;
import view.*;

import javax.swing.*;

public class CtrlStop implements ActionListener {

	//private JButton stop;
	private JButton stop;
	private View v;
	private Analyseur analyseur;

	public CtrlStop(JButton sp, View vu, Analyseur a) {
		v=vu;
    	stop=sp;
		analyseur=a;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(stop)){
			try {
				v.beforeStop();
				analyseur.finEcoute();
				Thread.sleep(1000);
				while(analyseur.isStoping()){

				}
				v.afterStop();
				v.stop();

				ArrayList<String> ar = analyseur.getCapturesNames();

				String[] tab = new String[ar.size()];
				tab = ar.toArray(tab);

				v.majData(tab);

				ArrayList<String> arST = analyseur.getCapturesNames();
				arST.remove("Toutes");
				String[] tabST = new String[arST.size()];
				tabST = arST.toArray(tabST);;

				v.majDataST(tabST);
			}catch (Exception ex){
				JOptionPane.showMessageDialog(null,"erreur pendant stop","ERREUR",JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
	}
}
