package view;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;
import java.io.File;

import controleur.*;
import model.Analyseur;
//port 1 à 65535


public class Vue {
  /*variable*/
  //private JButton start_pause, stop;
  //private JRoundButton start_pause;
  private JCoolButton stop, start_pause;
  private JLabel txtprox, txtproxy, txtport;
  private JRadioButton proxy_oui, proxy_non, param_auto, param_man;
  private JTextField proxy;
  private JSpinner port, portlog;
  private JLabel cap, pagechar,poid,cookie,site,methode; //affichage
  private ButtonGroup bg, bg2;

  //modele
  private Analyseur anal;

  public Vue(){
    Analyseur anal=new Analyseur();

    JFrame f = new JFrame("affichge");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    bg2 = new ButtonGroup();
    param_auto = new JRadioButton("automatique");
    param_auto.setSelected(true);
    param_man = new JRadioButton("manuel");
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

    bg = new ButtonGroup();
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
    FlowLayout layout1_3=new FlowLayout();
    layout1_3.setHgap(50);
    onglet1_3.setLayout(layout1_3);

    //stop = new JButton("Stop");

    stop = new JCoolButton("Stop");
    stop.setPreferredSize(new Dimension(100, 25));
    stop.setVisible(false);
    onglet1_3.add(stop);
    Control_stop control_stop = new Control_stop(stop, this, anal);
    stop.addActionListener(control_stop);


    //start_pause = new JButton("Start");
    start_pause = new JCoolButton("Start");
    start_pause.setPreferredSize(new Dimension(100, 25));
    onglet1_3.add(start_pause);
    Control_start control_start = new Control_start(start_pause, this, anal);
    start_pause.addActionListener(control_start);


    JPanel onglet1_G = new JPanel();
    onglet1_G.setLayout(new GridLayout(3, 1));//l,c

    onglet1_G.add(onglet1_1);
    onglet1_G.add(onglet1_2);
    onglet1_G.add(onglet1_3);

    onglets.addTab("Proxy", onglet1_G);

    //JPanel onglet2_G = new JPanel();
    //onglet2_G.setLayout(new GridLayout(6,3));

    JPanel onglet2_1 = new JPanel(new GridLayout(6,3, -1, -1));//l,c
    onglet2_1.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

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

    cap = new JLabel("<valeur>");
    cap.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    pagechar = new JLabel("<valeur>");
    pagechar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    poid = new JLabel("<valeur>");
    poid.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    cookie = new JLabel("<valeur>");
    cookie.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    site = new JLabel("<valeur>");
    site.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    methode = new JLabel("<valeur>");
    methode.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    JButton bcap =new JButton("plus info");
    JButton bpagechar =new JButton("plus info");
    JButton bpoid =new JButton("plus info");
    JButton bcookie =new JButton("plus info");
    JButton bsite =new JButton("plus info");
    JButton bmethode =new JButton("plus info");

    Control_graph control_graph = new Control_graph(bcap, bpagechar,bpoid, bcookie, bsite, bmethode, this);
    bcap.addActionListener(control_graph);
    bpagechar.addActionListener(control_graph);
    bpoid.addActionListener(control_graph);
    bcookie.addActionListener(control_graph);
    bsite.addActionListener(control_graph);
    bmethode.addActionListener(control_graph);

    onglet2_1.add(txtcap);
    onglet2_1.add(cap);
    onglet2_1.add(bcap);
    onglet2_1.add(txtpagechar);
    onglet2_1.add(pagechar);
    onglet2_1.add(bpagechar);
    onglet2_1.add(txtpoid);
    onglet2_1.add(poid);
    onglet2_1.add(bpoid);
    onglet2_1.add(txtcookie);
    onglet2_1.add(cookie);
    onglet2_1.add(bcookie);
    onglet2_1.add(txtsite);
    onglet2_1.add(site);
    onglet2_1.add(bsite);
    onglet2_1.add(txtmethode);
    onglet2_1.add(methode);
    onglet2_1.add(bmethode);

    JPanel onglet2_2 = new JPanel();
    JButton actu = new JButton("actualisé");
    onglet2_2.add(actu);
    Control_actu control_actu = new Control_actu(actu, this);
    actu.addActionListener(control_actu);



    JPanel onglet2_G=new JPanel();
    onglet2_G.setLayout(new BorderLayout()); //North, South, East, West
    onglet2_G.add("Center",onglet2_1);
    onglet2_G.add("East",onglet2_2);
    onglet2_G.add(onglet2_1);
    onglets.addTab("affichage", onglet2_G);

    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem, ouvrir;
    JRadioButtonMenuItem rbMenuItem;
    JCheckBoxMenuItem cbMenuItem;

    //Create the menu bar.
    menuBar = new JMenuBar();

    //Build the first menu.
    menu = new JMenu("Menu");
    menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
    menuBar.add(menu);


    //a group of JMenuItems
    submenu = new JMenu("Fichier     ");
    submenu.setMnemonic(KeyEvent.VK_S);


    ouvrir = new JMenuItem("Ouvrir");
    submenu.add(ouvrir);
    Control_ouvrir control_ouvrir = new Control_ouvrir(ouvrir, this);
    ouvrir.addActionListener(control_ouvrir);

    menuItem = new JMenuItem("Enregistrer");
    submenu.add(menuItem);
    menu.add(submenu);

    menuItem = new JMenuItem("help",KeyEvent.VK_T);


    Control_aide control_aide = new Control_aide(menuItem, this);
    menuItem.addActionListener(control_aide);

    menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
    menu.add(menuItem);

    f.setJMenuBar(menuBar);

    onglets.setOpaque(true);
    pannel.add(onglets);
    f.getContentPane().add(pannel);
    f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.pack();
    f.setExtendedState(Frame.MAXIMIZED_BOTH);
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

  //maj affichge
  public void maj_cap(String s){
    cap.setText(s);
  }

  public void maj_pagecharge(String s){
    pagechar.setText(s);
  }

  public void maj_poid(String s){
    poid.setText(s);
  }

  public void maj_cookie(String s){
    cookie.setText(s);
  }

  public void maj_site(String s){
    site.setText(s);
  }

  public void maj_methode(String s){
    methode.setText(s);
  }

  public int getPortLog(){
    return (int)portlog.getValue();
  }

  public int getPortProxy(){
    return (int)port.getValue();
  }

  public String getQuestion1(){
    if(param_auto.isSelected()){
      return param_auto.getText();
    }
    return param_man.getText();
  }

  public String getQuestion2(){
    if(proxy_oui.isSelected()){
      return proxy_oui.getText();
    }
    return proxy_non.getText();
  }

  public void setFile(File f){
    anal.setFile(f);
  }


  public static void setBestLookAndFeelAvailable(){
    String system_lf = UIManager.getSystemLookAndFeelClassName().toLowerCase();
    if(system_lf.contains("metal")){
      try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
      }catch (Exception e) {}
    }else{
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }catch (Exception e) {}
    }
  }

  public static void main(String[] args) {
    setBestLookAndFeelAvailable();
    Vue fenetre = new Vue();
  }
}
