/*
*@author : Jules LE BRIS
 */

package view;

import java.awt.event.KeyEvent;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import controleur.*;
import model.Analyseur;
//proxyPort 1 à 65535
//http://proxyetu.iut-nantes.univ-nantes.prive:3128


public class View {
  private JButton stop;
  private JButton start;
  private JLabel txtprox, txtproxy, txtport, chargement;
  private JRadioButton proxy_oui, proxy_non, param_auto, param_man;
  private JTextField adressProxySysteme;
  private JSpinner proxyPort, proxyPortLogiciel;
  private JLabel nbChargedPages;
  private JLabel nbCookie; //affichage
  private JTabbedPane onglets;
  private JPanel JPtmpCapture, JPnbChargedPages, JPweigtChargedPages, JPnbCookie, JPmostViewedSite, JPmethode;
  private JComboBox data;

  //modele
  private Analyseur analyseur;

  public View(){
    analyseur=new Analyseur();

    JFrame f = new JFrame("Projet AnalyseurHTTP");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.onglets = new JTabbedPane(SwingConstants.TOP);

    JPanel onglet1_1 = new JPanel();
    onglet1_1.setLayout(new GridLayout(2, 3));//l,c

    JLabel txtportlog = new JLabel("Port à écouter : ");
    onglet1_1.add(txtportlog);

    proxyPortLogiciel = new JSpinner();
    proxyPortLogiciel.setValue(9999);
    onglet1_1.add(proxyPortLogiciel);

    JLabel bricolage2 = new JLabel("");
    onglet1_1.add(bricolage2);

    JLabel txtparam =new JLabel("Configuration :");
    onglet1_1.add(txtparam);

    ButtonGroup bgProxy = new ButtonGroup();
    param_auto = new JRadioButton("Automatique");
    param_auto.setSelected(true);
    param_man = new JRadioButton("Manuel");
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

    txtprox =new JLabel("Utiliser un autre proxy ?");
    txtprox.setVisible(false);
    onglet1_2.add(txtprox);

    ButtonGroup bgAutoMan = new ButtonGroup();
    proxy_oui = new JRadioButton("Oui");
    proxy_non = new JRadioButton("Non");
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

    txtproxy = new JLabel("Adresse du proxy : ");
    txtproxy.setVisible(false);
    onglet1_2.add(txtproxy);

    adressProxySysteme = new JTextField("");
    adressProxySysteme.setVisible(false);
    onglet1_2.add(adressProxySysteme);

    JLabel bricolage = new JLabel("");
    onglet1_2.add(bricolage);

    txtport = new JLabel("Port du proxy : ");
    txtport.setVisible(false);
    onglet1_2.add(txtport);

    proxyPort = new JSpinner();
    proxyPort.setVisible(false);
    onglet1_2.add(proxyPort);


    JPanel onglet1_3 = new JPanel();
    onglet1_3.setLayout(new FlowLayout());

    stop = new JButton("Stop");
    stop.setPreferredSize(new Dimension(100, 25));
    stop.setVisible(false);
    onglet1_3.add(stop);
    CtrlStop ctrlStop = new CtrlStop(stop, this, analyseur);
    stop.addActionListener(ctrlStop);

    start = new JButton("Start");
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

    onglets.addTab("Analyseur", onglet1_G);


    JTabbedPane ongletsGraph = new JTabbedPane(SwingConstants.TOP);

    //todo LISTE D2ROULANTE

    ArrayList<String> ar = analyseur.getCapturesNames();

    String[] tab = new String[ar.size()];
    tab = ar.toArray(tab);

    data = new JComboBox(tab);


    JButton actu = new JButton("Rafraichir");
    actu.setPreferredSize(new Dimension(100, 25));
    CtrlActualisation ctrlActualisation = new CtrlActualisation(actu, this, analyseur);
    actu.addActionListener(ctrlActualisation);


    JLabel txtcap = new JLabel("Temps des captures");
    JLabel txtpagechar = new JLabel("Nombre de pages chargées");
    JLabel txtcookie = new JLabel("Nombre de cookies créés");

    JLabel tmpCapture = new JLabel("<valeur>");
    nbChargedPages = new JLabel("<valeur>");
    nbCookie = new JLabel("<valeur>");

    JButton bcap =new javax.swing.JButton("Plus d'info");
    JButton bpagechar =new javax.swing.JButton("Plus d'info");
    JButton bcookie =new javax.swing.JButton("Plus d'info");

    JPtmpCapture = new JPanel();
    JPtmpCapture.setLayout(new FlowLayout());
    JPtmpCapture.add(txtcap);
    JPtmpCapture.add(tmpCapture);
    JPtmpCapture.add(bcap);

    JPnbChargedPages = new JPanel();
    JPnbChargedPages.setLayout(new FlowLayout());
    JPnbChargedPages.add(txtpagechar);
    JPnbChargedPages.add(nbChargedPages);
    JPnbChargedPages.add(bpagechar);

    JPweigtChargedPages = new JPanel();
    JPweigtChargedPages.setLayout(new FlowLayout());

    JPnbCookie = new JPanel();
    JPnbCookie.setLayout(new FlowLayout());
    JPnbCookie.add(txtcookie);
    JPnbCookie.add(nbCookie);
    JPnbCookie.add(bcookie);

    JPmostViewedSite = new JPanel();
    JPmostViewedSite.setLayout(new FlowLayout());

    JPmethode = new JPanel();
    JPmethode.setLayout(new FlowLayout());


    JPanel onglet2_2 = new JPanel();
    onglet2_2.setLayout(new BorderLayout());
    onglet2_2.add(ongletsGraph, BorderLayout.CENTER);
    JPanel JPactu = new JPanel();
    JPactu.add(data);
    JPactu.add(actu);
    onglet2_2.add(JPactu, BorderLayout.SOUTH);


    onglets.addTab("Affichage", onglet2_2);

    ongletsGraph.addTab("Temps des captures", JPtmpCapture);
    ongletsGraph.addTab("Nombre de pages chargées", JPnbChargedPages);
    ongletsGraph.addTab("Poid des pages chargées", JPweigtChargedPages);
    ongletsGraph.addTab("Nombre de cookies créés", JPnbCookie);
    ongletsGraph.addTab("Le site le plus utlisé", JPmostViewedSite);
    ongletsGraph.addTab("Methode HTTP la plus utilisée", JPmethode);


    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem, ouvrir;

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
    CtrlOpen ctrlOpen = new CtrlOpen(ouvrir, this, analyseur);
    ouvrir.addActionListener(ctrlOpen);

    menuItem = new JMenuItem("Sauvegarder");
    submenu.add(menuItem);
    menu.add(submenu);

    menuItem = new JMenuItem("Aide",KeyEvent.VK_T);


    CtrlHelp ctrlHelp = new CtrlHelp(menuItem, this);
    menuItem.addActionListener(ctrlHelp);

    menuItem.getAccessibleContext().setAccessibleDescription("Cela ne sert à rien");
    menu.add(menuItem);

    f.setJMenuBar(menuBar);

    ongletsGraph.setOpaque(true);
    onglets.setOpaque(true);
    f.getContentPane().add(onglets);
    f.setVisible(true);
    f.pack();
    f.setPreferredSize(new Dimension(1000, 800));
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
    stop.setEnabled(false);
    JOptionPane.showMessageDialog(null, "Patientez SVP");
  }

  public void afterStop(){
    chargement.setVisible(false);
    stop.setEnabled(true);
  }

  public void addJPmostViewedSite(JPanel add){
    JPmostViewedSite.add(add);
  }

  public void addJPweigtChargedPages(JPanel add){
    JPweigtChargedPages.add(add);
  }

  public void addJPmethode(JPanel add){
    JPmethode.add(add);
  }

  //maj affichge
  //public void maj_cap(String s){
  //  tmpCapture.setText(s);
  //}

  public void maj_pagecharge(String s){
    nbChargedPages.setText(s);
  }


  public void maj_cookie(String s){
    nbCookie.setText(s);
  }

  public void clearJPMethode(){
    JPmethode.removeAll();
  }

  public void clearJPMostViewedSite(){
    JPmostViewedSite.removeAll();
  }

  public void clearJPweigtChargedPages(){
    JPweigtChargedPages.removeAll();
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

  public String getData(){
    System.out.println((String)data.getSelectedItem());
    return (String)data.getSelectedItem();
  }

  public void majData(String[] data){
    this.data.removeAllItems();
    for (String s : data) {
      this.data.addItem(s);
    }
  }


  private static void setBestLookAndFeelAvailable(){
    String system_lf = UIManager.getSystemLookAndFeelClassName().toLowerCase();
    if(system_lf.contains("metal")){
      try {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
      }catch (Exception e) {e.printStackTrace();}
    }else{
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }catch (Exception e) {e.printStackTrace();}
    }
  }

  public static void main(String[] args) {
    setBestLookAndFeelAvailable();
    new View();
  }
}
