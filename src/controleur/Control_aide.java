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

			try{
        //text file, should be opening in default text editor
        File file = new File("./help");

         Desktop desktop = Desktop.getDesktop();
         if(file.exists()) desktop.open(file); 
			}
			catch (IOException f) {
				System.out.println(f.getMessage());
			}
		}
	}
}
