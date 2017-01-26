/*
*@author : Jules LE BRIS
 */

package view;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;
import controleur.*;
import model.Analyseur;
//proxyPort 1 à 65535
//http://proxyetu.iut-nantes.univ-nantes.prive:3128


public class View {
  private JBoutonMod stop, start;
  private JLabel txtprox, txtproxy, txtport, chargement;
  private JRadioButton proxy_oui, proxy_non, param_auto, param_man;
  private JTextField adressProxySysteme;
  private JSpinner proxyPort, proxyPortLogiciel;
  private JLabel tmpCapture, nbChargedPages, weigtChargedPages, nbCookie, mostViewedSite, methode; //affichage
  private ButtonGroup bgAutoMan, bgProxy;
  private JTabbedPane onglets;
  private JPanel onglet1_3;

  //modele
  private Analyseur analyseur;

  public View(){
    Analyseur analyseur=new Analyseur();

    JFrame f = new JFrame("Projet Analyseur");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel pannel = new JPanel();

    this.onglets = new JTabbedPane(SwingConstants.TOP);

    JPanel onglet1_1 = new JPanel();
    onglet1_1.setLayout(new GridLayout(2, 3));//l,c

    JLabel txtportlog = new JLabel("Port logiciel écouter : ");
    onglet1_1.add(txtportlog);

    proxyPortLogiciel = new JSpinner();
    proxyPortLogiciel.setValue(9999);
    onglet1_1.add(proxyPortLogiciel);

    JLabel bricolage2 = new JLabel("");
    onglet1_1.add(bricolage2);

    JLabel txtparam =new JLabel("configuration :");
    onglet1_1.add(txtparam);

    bgProxy = new ButtonGroup();
    param_auto = new JRadioButton("automatique");
    param_auto.setSelected(true);
    param_man = new JRadioButton("manuel");
    // ajout des boutons radio dans le groupe bgAutoMan
    bgProxy.add(param_auto);
    bgProxy.add(param_man);
    onglet1_1.add(param_auto);
    onglet1_1.add(param_man);
    CtrlConfiguration control_conf = new CtrlConfiguration(param_auto, param_man, this);
    param_auto.addActionListener(control_conf);
    param_man.addActionListener(control_conf);



    JPanel onglet1_2 = new JPanel();
    onglet1_2.setLayout(new GridLayout(3, 3));

    txtprox =new JLabel("Utiliser un proxy ?");
    txtprox.setVisible(false);
    onglet1_2.add(txtprox);

    bgAutoMan = new ButtonGroup();
    proxy_oui = new JRadioButton("oui");
    proxy_non = new JRadioButton("non");
    proxy_non.setSelected(true);
    proxy_non.setVisible(false);
    proxy_oui.setVisible(false);
    // ajout des boutons radio dans le groupe bgAutoMan
    bgAutoMan.add(proxy_oui);
    bgAutoMan.add(proxy_non);
    onglet1_2.add(proxy_oui);
    onglet1_2.add(proxy_non);

    CtrlProxy control_prox = new CtrlProxy(proxy_oui, proxy_non, this);
    proxy_oui.addActionListener(control_prox);
    proxy_non.addActionListener(control_prox);

    txtproxy = new JLabel("Adresse proxy : ");
    txtproxy.setVisible(false);
    onglet1_2.add(txtproxy);

    adressProxySysteme = new JTextField("");
    adressProxySysteme.setVisible(false);
    onglet1_2.add(adressProxySysteme);

    JLabel bricolage = new JLabel("");
    onglet1_2.add(bricolage);

    txtport = new JLabel("Proxy port : ");
    txtport.setVisible(false);
    onglet1_2.add(txtport);

    proxyPort = new JSpinner();
    proxyPort.setVisible(false);
    onglet1_2.add(proxyPort);


    onglet1_3 = new JPanel();
    FlowLayout layout1_3=new FlowLayout();
    layout1_3.setHgap(50);
    onglet1_3.setLayout(layout1_3);

    stop = new JBoutonMod("Stop");
    stop.setPreferredSize(new Dimension(100, 25));
    stop.setVisible(false);
    onglet1_3.add(stop);
    CtrlStop ctrlStop = new CtrlStop(stop, this, analyseur);
    stop.addActionListener(ctrlStop);

    start = new JBoutonMod("Start");
    start.setPreferredSize(new Dimension(100, 25));
    onglet1_3.add(start);
    CtrlStart ctrlStart = new CtrlStart(start, this, analyseur);
    start.addActionListener(ctrlStart);

    chargement=new JLabel("Chargement ....");
    chargement.setVisible(false);
    onglet1_3.add(chargement);

    JPanel onglet1_G = new JPanel();
    onglet1_G.setLayout(new GridLayout(3, 1));//l,c

    onglet1_G.add(onglet1_1);
    onglet1_G.add(onglet1_2);
    onglet1_G.add(onglet1_3);

    onglets.addTab("Proxy", onglet1_G);

    JPanel onglet2_1 = new JPanel(new GridLayout(6,3, -1, -1));//l,c
    onglet2_1.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));

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

    tmpCapture = new JLabel("<value>");
    tmpCapture.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    nbChargedPages = new JLabel("<value>");
    nbChargedPages.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    weigtChargedPages = new JLabel("<value>");
    weigtChargedPages.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    nbCookie = new JLabel("<value>");
    nbCookie.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    mostViewedSite = new JLabel("<value>");
    mostViewedSite.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    methode = new JLabel("<value>");
    methode.setBorder(BorderFactory.createLineBorder(Color.BLACK));

    JButton bcap =new JButton("more info");
    JButton bpagechar =new JButton("more info");
    JButton bpoid =new JButton("more info");
    JButton bcookie =new JButton("more info");
    JButton bsite =new JButton("more info");
    JButton bmethode =new JButton("more info");

    CtrlChartMethode ctrlChartMethode = new CtrlChartMethode(bmethode, this, analyseur);
    bmethode.addActionListener(ctrlChartMethode);

    CtrlChartViewSite ctrlChartViewSite = new CtrlChartViewSite(bsite, this, analyseur);
    bsite.addActionListener(ctrlChartViewSite);



    onglet2_1.add(txtcap);
    onglet2_1.add(tmpCapture);
    onglet2_1.add(bcap);
    onglet2_1.add(txtpagechar);
    onglet2_1.add(nbChargedPages);
    onglet2_1.add(bpagechar);
    onglet2_1.add(txtpoid);
    onglet2_1.add(weigtChargedPages);
    onglet2_1.add(bpoid);
    onglet2_1.add(txtcookie);
    onglet2_1.add(nbCookie);
    onglet2_1.add(bcookie);
    onglet2_1.add(txtsite);
    onglet2_1.add(mostViewedSite);
    onglet2_1.add(bsite);
    onglet2_1.add(txtmethode);
    onglet2_1.add(methode);
    onglet2_1.add(bmethode);

    JPanel onglet2_2 = new JPanel();
    JButton actu = new JButton("refresh");
    onglet2_2.add(actu);
    CtrlActualisation ctrlActualisation = new CtrlActualisation(actu, this, analyseur);
    actu.addActionListener(ctrlActualisation);



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
    submenu = new JMenu("File     ");
    submenu.setMnemonic(KeyEvent.VK_S);


    ouvrir = new JMenuItem("Open");
    submenu.add(ouvrir);
    CtrlOpen ctrlOpen = new CtrlOpen(ouvrir, this, analyseur);
    ouvrir.addActionListener(ctrlOpen);

    menuItem = new JMenuItem("Save");
    submenu.add(menuItem);
    menu.add(submenu);

    menuItem = new JMenuItem("help",KeyEvent.VK_T);


    CtrlHelp ctrlHelp = new CtrlHelp(menuItem, this);
    menuItem.addActionListener(ctrlHelp);

    menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
    menu.add(menuItem);

    f.setJMenuBar(menuBar);

    onglets.setOpaque(true);
    pannel.add(onglets);
    f.getContentPane().add(pannel);
    f.setVisible(true);
		//f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.pack();
    f.setExtendedState(Frame.MAXIMIZED_BOTH);
  }

  public void start(){
    this.start.setVisible(false);
    this.stop.setVisible(true);
    this.proxyPortLogiciel.setEnabled(false);
    this.proxyPort.setEnabled(false);
    this.adressProxySysteme.setEnabled(false);
    this.stop.setVisible(true);
    this.proxy_oui.setEnabled(false);
    this.proxy_non.setEnabled(false);
    this.param_auto.setEnabled(false);
    this.param_man.setEnabled(false);
    this.onglets.setEnabled(false);
  }


  public void stop(){
    this.start.setVisible(true);
    this.stop.setVisible(false);
    this.proxyPortLogiciel.setEnabled(true);
    this.proxyPort.setEnabled(true);
    this.adressProxySysteme.setEnabled(true);
    this.proxy_oui.setEnabled(true);
    this.proxy_non.setEnabled(true);
    this.param_auto.setEnabled(true);
    this.param_man.setEnabled(true);
    this.onglets.setEnabled(true);
  }

  public void ConfigMan(){
    txtprox.setVisible(true);
    proxy_oui.setVisible(true);
    proxy_non.setVisible(true);
  }

  public void ConfigAuto(){
    txtprox.setVisible(false);
    proxy_oui.setVisible(false);
    proxy_non.setVisible(false);
    proxy_non.setSelected(true);
    this.configProxyNon();
  }

  public void configProxyOui(){
    txtproxy.setVisible(true);
    adressProxySysteme.setVisible(true);
    adressProxySysteme.setText("http://proxyetu.iut-nantes.univ-nantes.prive");
    txtport.setVisible(true);
    proxyPort.setVisible(true);
    proxyPort.setValue(3128);
  }

  public void configProxyNon(){
    txtproxy.setVisible(false);
    adressProxySysteme.setVisible(false);
    txtport.setVisible(false);
    proxyPort.setVisible(false);
  }

  public void beforeStop(){
    chargement.setVisible(true);

    JOptionPane.showMessageDialog(null, "Patienter SVP");
  }

  public void afterStop(){
    chargement.setVisible(false);
  }

  //maj affichge
  public void maj_cap(String s){
    tmpCapture.setText(s);
  }

  public void maj_pagecharge(String s){
    nbChargedPages.setText(s);
  }

  public void maj_poid(String s){
    weigtChargedPages.setText(s);
  }

  public void maj_cookie(String s){
    nbCookie.setText(s);
  }

  public void maj_site(String s){
    mostViewedSite.setText(s);
  }

  public void maj_methode(String s){
    methode.setText(s);
  }

  public int getPortLog(){
    return (int) proxyPortLogiciel.getValue();
  }

  public int getPortProxy(){
    return (int) proxyPort.getValue();
  }

  public String getProxy(){
    return adressProxySysteme.getText();
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
    View fenetre = new View();
  }
}
