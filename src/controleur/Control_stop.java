package controleur;

import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import model.Analyseur;
import view.*;

public class Control_stop implements ActionListener {

	//private JButton stop;
	private JCoolButton stop;
	private Vue v;
	private Analyseur analyseur;

	public Control_stop(JCoolButton sp, Vue vu, Analyseur a) {
		v=vu;
    	stop=sp;
		analyseur=a;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(stop)){

			v.stop();
			try {
				analyseur.finEcoute();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
