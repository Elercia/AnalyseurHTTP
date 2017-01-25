package controleur;

import java.awt.event.*;
import java.io.IOException;

import model.Analyseur;
import view.*;

public class Control_stop implements ActionListener {

	//private JButton stop;
	private JBoutonMod stop;
	private View v;
	private Analyseur analyseur;

	public Control_stop(JBoutonMod sp, View vu, Analyseur a) {
		v=vu;
    	stop=sp;
		analyseur=a;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(stop)){


			try {
				v.beforeStop();
				analyseur.finEcoute();

				v.afterStop();
				v.stop();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
