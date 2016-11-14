package view;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import controleur.*;



public class Vue {
  /*variable*/
  private JButton start_pause, stop;
  private JLabel txtprox, txtproxy, txtport;
  private JRadioButton proxy_oui, proxy_non;
  private JTextField proxy;
  private JSpinner port, portlog;

  public Vue(){
    JFrame f = new JFrame("affichge");
    JPanel pannel = new JPanel();

    JTabbedPane onglets = new JTabbedPane(SwingConstants.TOP);



    JPanel onglet1_1 = new JPanel();
    onglet1_1.setLayout(new GridLayout(2, 3));//l,c

    JLabel txtportlog = new JLabel("port logiciel ecouter : ");
    onglet1_1.add(txtportlog);

    portlog = new JSpinner();
    onglet1_1.add(portlog);

    JLabel bricolage2 = new JLabel("");
    onglet1_1.add(bricolage2);

    JLabel txtparam =new JLabel("configuration :");
    onglet1_1.add(txtparam);

    ButtonGroup bg2 = new ButtonGroup();
    JRadioButton param_auto = new JRadioButton("automatique");
    param_auto.setSelected(true);
    JRadioButton param_man = new JRadioButton("manuel");
    // ajout des boutons radio dans le groupe bg
    bg2.add(param_auto);
    bg2.add(param_man);
    onglet1_1.add(param_auto);
    onglet1_1.add(param_man);
    Control_config control_conf = new Control_config(param_auto, param_man, this);
    param_auto.addActionListener(control_conf);
    param_man.addActionListener(control_conf);



    JPanel onglet1_2 = new JPanel();
    onglet1_2.setLayout(new GridLayout(3, 3));

    txtprox =new JLabel("utiliser un proxy ?");
    txtprox.setVisible(false);
    onglet1_2.add(txtprox);

    ButtonGroup bg = new ButtonGroup();
    proxy_oui = new JRadioButton("oui");
    proxy_non = new JRadioButton("non");
    proxy_non.setSelected(true);
    proxy_non.setVisible(false);
    proxy_oui.setVisible(false);
    // ajout des boutons radio dans le groupe bg
    bg.add(proxy_oui);
    bg.add(proxy_non);
    onglet1_2.add(proxy_oui);
    onglet1_2.add(proxy_non);

    Control_proxy control_prox = new Control_proxy(proxy_oui, proxy_non, this);
    proxy_oui.addActionListener(control_prox);
    proxy_non.addActionListener(control_prox);

    txtproxy = new JLabel("addresse proxy : ");
    txtproxy.setVisible(false);
    onglet1_2.add(txtproxy);

    proxy = new JTextField("");
    proxy.setVisible(false);
    onglet1_2.add(proxy);

    JLabel bricolage = new JLabel("");
    onglet1_2.add(bricolage);

    txtport = new JLabel("port : ");
    txtport.setVisible(false);
    onglet1_2.add(txtport);

    port = new JSpinner();
    port.setVisible(false);
    onglet1_2.add(port);


    JPanel onglet1_3 = new JPanel();
    onglet1_3.setLayout(new GridLayout(1, 2));

    stop = new JButton("Stop");
    stop.setVisible(false);
    onglet1_3.add(stop);
    Control_stop control_stop = new Control_stop(stop, this);
    stop.addActionListener(control_stop);


    start_pause = new JButton("Start");
    onglet1_3.add(start_pause);
		Control_start control_start = new Control_start(start_pause, this);
		start_pause.addActionListener(control_start);


    JPanel onglet1_G = new JPanel();
    onglet1_G.setLayout(new GridLayout(3, 1));//l,c

    onglet1_G.add(onglet1_1);
    onglet1_G.add(onglet1_2);
    onglet1_G.add(onglet1_3);

    onglets.addTab("Proxy", onglet1_G);

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




  public static void main(String[] args) {
    Vue fenetre = new Vue();
  }

  public void start(){
    this.start_pause.setText("Pause");
    this.stop.setVisible(true);
  }

  public void pause(){
    this.start_pause.setText("Start");
  }

  public void stop(){
    this.start_pause.setText("Start");
    this.stop.setVisible(false);
  }

  public void question1_oui(){
    txtprox.setVisible(true);
    proxy_oui.setVisible(true);
    proxy_non.setVisible(true);
  }

  public void question1_non(){
    txtprox.setVisible(false);
    proxy_oui.setVisible(false);
    proxy_non.setVisible(false);
    proxy_non.setSelected(true);
    this.question2_non();
  }

  public void question2_oui(){
    txtproxy.setVisible(true);
    proxy.setVisible(true);
    txtport.setVisible(true);
    port.setVisible(true);
  }

  public void question2_non(){
    txtproxy.setVisible(false);
    proxy.setVisible(false);
    txtport.setVisible(false);
    port.setVisible(false);
  }
}
