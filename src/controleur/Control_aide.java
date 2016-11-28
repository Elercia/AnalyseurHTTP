package controleur;

import java.awt.event.*;
import javax.swing.*;
import view.*;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
//import ihm.modele.*;

public class Control_aide implements ActionListener {

	private JMenuItem aide;
	private Vue v;
	/*private CarnetAdresse c;*/


	public Control_aide(JMenuItem aide, Vue vu) {
		v=vu;
    this.aide=aide;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(aide)){

			// String command = "xdg-open help.txt";
			// Runtime runtime = Runtime.getRuntime();
			// Process process = null;
			//
			// try
			// {
			// 	process = runtime.exec(command);
			// } catch(Exception err) {
			// 	System.out.println(err.getMessage());
			// }

			JOptionPane.showMessageDialog(null,"ceci cera le message d'aide" ,"help", JOptionPane.PLAIN_MESSAGE);
		}
	}
}
