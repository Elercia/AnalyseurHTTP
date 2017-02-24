package controleur;

import java.awt.event.*;
import javax.swing.*;
import view.*;

import java.io.*;
//import ihm.modele.*;

public class CtrlHelp implements ActionListener {

	private JMenuItem aide;
	private View v;
	/*private CarnetAdresse c;*/


	public CtrlHelp(JMenuItem aide, View vu) {
		v=vu;
    this.aide=aide;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(aide)){
			try{
				File f=new File("help.html");
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

				JLabel msg = new JLabel(chaine);

				//JOptionPane.showMessageDialog(null,chaine ,"help", JOptionPane.PLAIN_MESSAGE);
				JOptionPane.showMessageDialog(null,msg ,"help", JOptionPane.PLAIN_MESSAGE);
			}
			catch (Exception exec){
				System.out.println(exec.toString());
			}
		}
	}
}
