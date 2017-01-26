package controleur;

import java.awt.event.*;
import java.io.IOException;

import model.Analyseur;
import view.*;

import javax.swing.*;

public class CtrlStop implements ActionListener {

	//private JButton stop;
	private JBoutonMod stop;
	private View v;
	private Analyseur analyseur;

	public CtrlStop(JBoutonMod sp, View vu, Analyseur a) {
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
					System.out.println("kzgjzipj");
				}
				v.afterStop();
				v.stop();
			}catch (Exception ex){
				JOptionPane.showMessageDialog(null,"erreur pendant stop","ERREUR",JOptionPane.ERROR_MESSAGE);
				ex.printStackTrace();
			}
		}
	}
}
