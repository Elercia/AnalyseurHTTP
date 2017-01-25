package controleur;

import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

import model.Analyseur;
import view.*;


public class Control_start implements ActionListener {

	//private JButton start;
	private JBoutonMod start;
	private View v;
	private Analyseur analyseur;
	/*private CarnetAdresse c;*/


	public Control_start(JBoutonMod st, View vu, Analyseur a) {
		v=vu;
    	start=st;
		analyseur=a;
	}

	public void actionPerformed(ActionEvent e) {
		if ((e.getSource()).equals(start)){
			/*
      - transformation du bouton en pause
      - bouton stop apparait
      - demarage de la capture

      a ajouter : lien avec le modele
      - si pause alors start
      - si start alors pause
      */
			if(v.getPortLog()<1){
				JOptionPane.showMessageDialog(null,"port plus petit que 1","IMPOSSIBLE",JOptionPane.ERROR_MESSAGE);
			}
			else{
				if(v.getPortLog()>65535){
					JOptionPane.showMessageDialog(null,"port plus grand que 65535","IMPOSSIBLE",JOptionPane.ERROR_MESSAGE);
				}
				else{
					if(v.getQuestion1()=="manuel" && v.getQuestion2()=="oui"){
						if(v.getPortProxy()<1){
							JOptionPane.showMessageDialog(null,"port plus petit que 1","IMPOSSIBLE",JOptionPane.ERROR_MESSAGE);
						}
						else{
							if(v.getPortProxy()>65535){
								JOptionPane.showMessageDialog(null,"port plus grand que 65535","IMPOSSIBLE",JOptionPane.ERROR_MESSAGE);
							}
							else{
								System.out.println("man+prox");
								analyseur.setProxy(v.getProxy(), v.getPortProxy());
								System.out.println(v.getProxy()+" "+ v.getPortProxy());

								try {
									analyseur.setPort(v.getPortLog());
									System.out.println("port affecter : "+ v.getPortLog());
								} catch (IOException e1) {
									e1.printStackTrace();
									System.err.println("port indisponible");
								}


								Thread t = new Thread(analyseur);
								t.start();

								v.start();

							}
						}
					}
					else{
						if(v.getQuestion1()=="manuel" && v.getQuestion2()=="non"){
							System.out.println("man+0prox");

						}
						else{
							//automatique
							System.out.println("auto");
							try {
								analyseur.setPort(v.getPortLog());
								System.out.println("port affecter : "+ v.getPortLog());
							} catch (IOException e1) {
								e1.printStackTrace();
								System.err.println("port indisponible");
							}

							Thread t = new Thread(analyseur);
							t.start();
							v.start();
						}
					}
				}
			}
		}
	}
}
