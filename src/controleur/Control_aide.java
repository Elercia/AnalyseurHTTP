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

			//JOptionPane.showMessageDialog(null,"ceci cera le message d'aide" ,"help", JOptionPane.PLAIN_MESSAGE);
			try{
				File f=new File("help.txt");
				String chaine=new String("");
				InputStream ips=new FileInputStream(f);
				InputStreamReader ipsr=new InputStreamReader(ips);
				BufferedReader br=new BufferedReader(ipsr);
				String ligne;
				while ((ligne=br.readLine())!=null){
					//System.out.println(ligne);
					chaine+=ligne+"\n";
				}
				br.close();
				JOptionPane.showMessageDialog(null,chaine ,"help", JOptionPane.PLAIN_MESSAGE);
			}
			catch (Exception exec){
				System.out.println(exec.toString());
			}
		}
	}
}
