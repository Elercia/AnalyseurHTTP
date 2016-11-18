package controleur;

import java.awt.event.*;
import javax.swing.*;
import view.*;
//import ihm.modele.*;

public class Control_start implements ActionListener {

	private JButton start;
	private Vue v;
	/*private CarnetAdresse c;*/


	public Control_start(JButton st, Vue vu) {
		v=vu;
    start=st;
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
								v.start();
							}
						}
					}
					else{
						if(v.getQuestion1()=="manuel" && v.getQuestion2()=="non"){
							//pas de proxy
							System.out.println("man+0prox");
							v.start();
						}
						else{
							//automatique
							System.out.println("auto");
							v.start();
						}
					}
				}
			}
		}
	}
}
