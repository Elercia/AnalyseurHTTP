package view;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;



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

    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem;
    JRadioButtonMenuItem rbMenuItem;
    JCheckBoxMenuItem cbMenuItem;

    //Create the menu bar.
    menuBar = new JMenuBar();

    //Build the first menu.
    menu = new JMenu("Menu");
    menu.getAccessibleContext().setAccessibleDescription(
            "The only menu in this program that has menu items");
    menuBar.add(menu);


    //a group of JMenuItems
    submenu = new JMenu("Fichier");
    submenu.setMnemonic(KeyEvent.VK_S);

    menuItem = new JMenuItem("Ouvrir");
    menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_2, ActionEvent.ALT_MASK));
    submenu.add(menuItem);

    menuItem = new JMenuItem("Enregistrer");
    submenu.add(menuItem);
    menu.add(submenu);

    menuItem = new JMenuItem("help",
                             KeyEvent.VK_T);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription(
            "This doesn't really do anything");
    menu.add(menuItem);

    f.setJMenuBar(menuBar);

    onglets.setOpaque(true);
    pannel.add(onglets);
    f.getContentPane().add(pannel);
    f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.pack();
  }
}
