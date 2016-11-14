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

    //JPanel onglet2_G = new JPanel();
    //onglet2_G.setLayout(new GridLayout(6,3));

    JPanel onglet2_G = new JPanel(new GridLayout(6,3, -1, -1));//l,c
    onglet2_G.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

    /*for (int i =0; i<(6*3); i++){
      final JLabel label = new JLabel("Label");
      label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
      onglet2_G.add(label);
    }*/


    JLabel txtcap = new JLabel("temps capture");
    txtcap.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    JLabel txtpagechar = new JLabel("nombre de page chargé");
    txtpagechar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    JLabel txtpoid = new JLabel("poid page chargé");
    txtpoid.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    JLabel txtcookie = new JLabel("nombre de cookie cree");
    txtcookie.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    JLabel txtsite = new JLabel("site le plus chargé");
    txtsite.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    JLabel txtmethode = new JLabel("methode la plus utilisé");
    txtmethode.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    JLabel cap = new JLabel("<valeur>");
    cap.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    JLabel pagechar = new JLabel("<valeur>");
    pagechar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    JLabel poid = new JLabel("<valeur>");
    poid.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    JLabel cookie = new JLabel("<valeur>");
    cookie.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    JLabel site = new JLabel("<valeur>");
    site.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    JLabel methode = new JLabel("<valeur>");
    methode.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    JButton bcap =new JButton("plus info");
    JButton bpagechar =new JButton("plus info");
    JButton bpoid =new JButton("plus info");
    JButton bcookie =new JButton("plus info");
    JButton bsite =new JButton("plus info");
    JButton bmethode =new JButton("plus info");

    onglet2_G.add(txtcap);
    onglet2_G.add(cap);
    onglet2_G.add(bcap);
    onglet2_G.add(txtpagechar);
    onglet2_G.add(pagechar);
    onglet2_G.add(bpagechar);
    onglet2_G.add(txtpoid);
    onglet2_G.add(poid);
    onglet2_G.add(bpoid);
    onglet2_G.add(txtcookie);
    onglet2_G.add(cookie);
    onglet2_G.add(bcookie);
    onglet2_G.add(txtsite);
    onglet2_G.add(site);
    onglet2_G.add(bsite);
    onglet2_G.add(txtmethode);
    onglet2_G.add(methode);
    onglet2_G.add(bmethode);


    onglets.addTab("affichage", onglet2_G);

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
