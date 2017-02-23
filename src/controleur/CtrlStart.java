package controleur;

import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

import model.Analyseur;
import view.*;


public class CtrlStart implements ActionListener {

	private JButton start;
	private View v;
	private Analyseur analyseur;


	public CtrlStart(JButton st, View vu, Analyseur a) {
		v=vu;
    	start=st;
		analyseur=a;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(start)){
			if(v.getPortLog()<1){
				JOptionPane.showMessageDialog(null,"Mauvaise configuration du port (plus petit que 1)","IMPOSSIBLE",JOptionPane.ERROR_MESSAGE);
			}
			else{
				if(v.getPortLog()>65535){
					JOptionPane.showMessageDialog(null,"Mauvaise configuration du port (suppérieur à la limite)","IMPOSSIBLE",JOptionPane.ERROR_MESSAGE);
				}
				else{
					if(v.getQuestion1().equalsIgnoreCase("manuel") && v.getQuestion2().equalsIgnoreCase("oui")){
						if(v.getPortProxy()<1){
							JOptionPane.showMessageDialog(null,"Mauvaise configuration du port (plus petit que 1)","IMPOSSIBLE",JOptionPane.ERROR_MESSAGE);
						}
						else{
							if(v.getPortProxy()>65535){
								JOptionPane.showMessageDialog(null,"Mauvaise configuration du port (suppérieur à la limite)","IMPOSSIBLE",JOptionPane.ERROR_MESSAGE);
							}
							else{
								System.out.println("Configuration de l'analyseur :\nManuelle avec utilisation de proxy");
								analyseur.setProxy(v.getProxy(), v.getPortProxy());
								System.out.println(v.getProxy()+" "+ v.getPortProxy());

								try {
									analyseur.setPort(v.getPortLog());
									System.out.println("Port logiciel utilisé : "+ v.getPortLog());
									Thread t = new Thread(analyseur);
									t.start();
									v.start();
								} catch (IOException e1) {
									e1.printStackTrace();
									System.err.println("Port voulu indisponible");
									JOptionPane.showMessageDialog(null,"Le port que vous avez rentré est indisponible","ERREUR",JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					}
					else{
						if(v.getQuestion1().equalsIgnoreCase("manuel") && v.getQuestion2().equalsIgnoreCase("mon")){
							System.out.println("Configuration de l'analyseur :\nManuelle");

						}
						else{
							//automatique
							System.out.println("auto");
							try {
								analyseur.setPort(v.getPortLog());
								System.out.println("Port logiciel utilisé : "+ v.getPortLog());
								Thread t = new Thread(analyseur);
								t.start();
								v.start();
							} catch (IOException e1) {
								e1.printStackTrace();
								System.err.println("Port voulu indisponible");
								JOptionPane.showMessageDialog(null,"Le port que vous avez rentré est indisponible","ERREUR",JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
			}
		}
	}
}
