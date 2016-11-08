package view;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;



public class Vue {

  public static void main(String[] args) {
    JFrame f = new JFrame("affichge");
    JPanel pannel = new JPanel();

    JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);

    JPanel onglet1 = new JPanel();
    onglet1.setLayout(new GridLayout(4, 3));

    JLabel txtprox =new JLabel("utiliser un proxy ?");
    onglet1.add(txtprox);

    ButtonGroup bg = new ButtonGroup();
    JRadioButton proxy_oui = new JRadioButton("oui");
    JRadioButton proxy_non = new JRadioButton("non");
    // ajout des boutons radio dans le groupe bg
    bg.add(proxy_oui);
    bg.add(proxy_non);
    onglet1.add(proxy_oui);
    onglet1.add(proxy_non);

    JLabel txtproxy = new JLabel("addresse : ");
    onglet1.add(txtproxy);

    JTextField proxy = new JTextField("");
    onglet1.add(proxy);

    JLabel bricolage = new JLabel("");
    onglet1.add(bricolage);

    JLabel txtport = new JLabel("port : ");
    onglet1.add(txtport);

    JSpinner port = new JSpinner();
    onglet1.add(port);

    JLabel bricolage0 = new JLabel("");
    onglet1.add(bricolage0);

    JLabel bricolage1 = new JLabel("");
    onglet1.add(bricolage1);

    JLabel bricolage2 = new JLabel("");
    onglet1.add(bricolage2);

    JButton valide = new JButton("VALIDE");
    onglet1.add(valide);


    onglets.addTab("Proxy", onglet1);

    JPanel onglet2 = new JPanel();
    JLabel contenu_onglet2 = new JLabel("Onglet 2");
    onglet2.add(contenu_onglet2);
    onglets.addTab("affichage", onglet2);

    onglets.setOpaque(true);
    pannel.add(onglets);
    f.getContentPane().add(pannel);
    f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.pack();
  }
}
