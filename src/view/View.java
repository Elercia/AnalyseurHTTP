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


public class View{
  private JButton stop;
  private JButton start;
  private JLabel txtprox, txtproxy, txtport, chargement;
  private JRadioButton proxy_oui, proxy_non, param_auto, param_man;
  private JTextField adressProxySysteme, nouveauNom;
  private JSpinner proxyPort, proxyPortLogiciel;
  private JLabel nbChargedPages;
  private JTabbedPane onglets;
  private JPanel JPtmpCapture, JPnbChargedPages, JPweigtChargedPages, JPnbCookie, JPmostViewedSite, JPmethode;
  private JComboBox data, capName;

  //modele
  private Analyseur analyseur;

  public View(){
    analyseur=new Analyseur();

    JFrame f = new JFrame("Projet AnalyseurHTTP");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.onglets = new JTabbedPane(SwingConstants.TOP);

    JPanel onglet1_1 = new JPanel();
    //onglet1_1.setMinimumSize(new Dimension(800, 250));

    JScrollPane jSonglet1_1 = new JScrollPane(onglet1_1);
    jSonglet1_1.getVerticalScrollBar().setUnitIncrement(20);


    GridBagLayout layout = new GridBagLayout();
    onglet1_1.setLayout(layout);
    GridBagConstraints c = new GridBagConstraints();

    // TODO: 01/03/17


    JLabel txtportlog = new JLabel("Port à écouter : ");
    txtportlog.setPreferredSize(new Dimension(250,30));
    txtportlog.setMinimumSize(new Dimension(200,30));
    c.gridx=0;
    c.gridy=0;

    onglet1_1.add(txtportlog, c);

    proxyPortLogiciel = new JSpinner();
    proxyPortLogiciel.setValue(9999);
    proxyPortLogiciel.setPreferredSize(new Dimension(250,30));
    proxyPortLogiciel.setMinimumSize(new Dimension(100,30));
    c.gridx=1;
    c.gridy=0;
    onglet1_1.add(proxyPortLogiciel, c);


    JLabel txtparam =new JLabel("Configuration du réseaux :");
    txtparam.setPreferredSize(new Dimension(250,30));
    txtparam.setMinimumSize(new Dimension(200,30));
    c.gridx=0;
    c.gridy=1;
    onglet1_1.add(txtparam, c);

    ButtonGroup bgProxy = new ButtonGroup();
    param_auto = new JRadioButton("Automatique");
    param_auto.setPreferredSize(new Dimension(250,30));
    param_auto.setMinimumSize(new Dimension(150,30));
    param_auto.setSelected(true);
    param_man = new JRadioButton("Manuel");
    param_man.setPreferredSize(new Dimension(250,30));
    param_man.setMinimumSize(new Dimension(150,30));
    bgProxy.add(param_auto);
    bgProxy.add(param_man);

    c.gridx=1;
    c.gridy=1;
    onglet1_1.add(param_auto, c);

    c.gridx=2;
    c.gridy=1;
    onglet1_1.add(param_man, c);
    CtrlConfiguration control_conf = new CtrlConfiguration(param_auto, param_man, this);
    param_auto.addActionListener(control_conf);
    param_man.addActionListener(control_conf);



    // TODO: 01/03/17



    txtprox =new JLabel("Utilisez vous un autre proxy ?");
    txtprox.setPreferredSize(new Dimension(250,30));
    txtprox.setMinimumSize(new Dimension(220,30));
    txtprox.setEnabled(false);
    c.gridx=0;
    c.gridy=2;
    onglet1_1.add(txtprox, c);

    ButtonGroup bgAutoMan = new ButtonGroup();
    proxy_oui = new JRadioButton("Oui");
    proxy_oui.setPreferredSize(new Dimension(250,30));
    proxy_oui.setMinimumSize(new Dimension(150,30));
    proxy_non = new JRadioButton("Non");
    proxy_non.setPreferredSize(new Dimension(250,30));
    proxy_non.setMinimumSize(new Dimension(150,30));
    proxy_non.setSelected(true);
    proxy_non.setEnabled(false);
    proxy_oui.setEnabled(false);
    bgAutoMan.add(proxy_oui);
    bgAutoMan.add(proxy_non);

    c.gridx=1;
    c.gridy=2;
    onglet1_1.add(proxy_oui, c);
    c.gridx=2;
    c.gridy=2;
    onglet1_1.add(proxy_non, c);

    CtrlProxy control_prox = new CtrlProxy(proxy_oui, proxy_non, this);
    proxy_oui.addActionListener(control_prox);
    proxy_non.addActionListener(control_prox);

    txtproxy = new JLabel("Adresse du proxy : ");
    txtproxy.setPreferredSize(new Dimension(250,30));
    txtproxy.setMinimumSize(new Dimension(200,30));
    txtproxy.setEnabled(false);

    c.gridx=0;
    c.gridy=3;
    onglet1_1.add(txtproxy, c);

    adressProxySysteme = new JTextField("http://proxyetu.iut-nantes.univ-nantes.prive");
    adressProxySysteme.setEnabled(false);

    c.gridx=1;
    c.gridy=3;
    onglet1_1.add(adressProxySysteme, c);

    txtport = new JLabel("Port du proxy : ");
    txtport.setPreferredSize(new Dimension(250,30));
    txtport.setMinimumSize(new Dimension(200,30));
    txtport.setEnabled(false);

    c.gridx=0;
    c.gridy=4;
    onglet1_1.add(txtport, c);

    proxyPort = new JSpinner();
    proxyPort.setPreferredSize(new Dimension(250,30));
    proxyPort.setMinimumSize(new Dimension(200,30));
    proxyPort.setEnabled(false);

    c.gridx=1;
    c.gridy=4;
    onglet1_1.add(proxyPort, c);


    JPanel onglet1_3 = new JPanel();
    onglet1_3.setLayout(new FlowLayout());

    stop = new JButton("Stop");
    stop.setPreferredSize(new Dimension(250, 30));
    stop.setMinimumSize(new Dimension(100, 30));
    stop.setVisible(false);
    c.gridx=1;
    c.gridy=5;
    onglet1_1.add(stop, c);
    CtrlStop ctrlStop = new CtrlStop(stop, this, analyseur);
    stop.addActionListener(ctrlStop);

    start = new JButton("Start");
    start.setPreferredSize(new Dimension(250, 30));
    start.setMinimumSize(new Dimension(100, 30));
    c.gridx=1;
    c.gridy=5;
    onglet1_1.add(start, c);
    CtrlStart ctrlStart = new CtrlStart(start, this, analyseur);
    start.addActionListener(ctrlStart);

    chargement=new JLabel("Chargement ....");
    chargement.setVisible(false);
    c.gridx=2;
    c.gridy=5;
    onglet1_1.add(chargement, c);


    // TODO: 01/03/17

    onglets.addTab("Analyseur", jSonglet1_1);

    JTabbedPane ongletsGraph = new JTabbedPane(SwingConstants.TOP);

    ArrayList<String> ar = analyseur.getCapturesNames();

    String[] tab = new String[ar.size()];
    tab = ar.toArray(tab);

    data = new JComboBox(tab);


    JButton actu = new JButton("Rafraichir");
    actu.setPreferredSize(new Dimension(100, 25));
    CtrlActualisation ctrlActualisation = new CtrlActualisation(actu, this, analyseur);
    actu.addActionListener(ctrlActualisation);



    JPtmpCapture = new JPanel();
    JPtmpCapture.setLayout(new FlowLayout());
    JScrollPane JStmpCapture = new JScrollPane(JPtmpCapture);
    JStmpCapture.getVerticalScrollBar().setUnitIncrement(20);

    JLabel txtpagechar = new JLabel("Nombre de pages chargées");
    nbChargedPages = new JLabel("<valeur>");
    JPnbChargedPages = new JPanel();
    JPnbChargedPages.setLayout(new FlowLayout());
    JPnbChargedPages.add(txtpagechar);
    JPnbChargedPages.add(nbChargedPages);

    JPweigtChargedPages = new JPanel();
    JPweigtChargedPages.setLayout(new FlowLayout());
    JScrollPane jScrollPane = new JScrollPane(JPweigtChargedPages);
    jScrollPane.getVerticalScrollBar().setUnitIncrement(20);

    JPnbCookie = new JPanel();
    JPnbCookie.setLayout(new FlowLayout());
    JScrollPane jScrollPaneCookie = new JScrollPane(JPnbCookie);
    jScrollPaneCookie.getVerticalScrollBar().setUnitIncrement(20);

    JPmostViewedSite = new JPanel();
    JPmostViewedSite.setLayout(new FlowLayout());
    JScrollPane JSmostViewedSite = new JScrollPane(JPmostViewedSite);
    JSmostViewedSite.getVerticalScrollBar().setUnitIncrement(20);

    JPmethode = new JPanel();
    JPmethode.setLayout(new FlowLayout());
    JScrollPane JSmethode = new JScrollPane(JPmethode);
    JSmethode.getVerticalScrollBar().setUnitIncrement(20);


    JPanel onglet2_2 = new JPanel();
    onglet2_2.setLayout(new BorderLayout());
    onglet2_2.add(ongletsGraph, BorderLayout.CENTER);
    JPanel JPactu = new JPanel();
    JPactu.add(data);
    JPactu.add(actu);
    onglet2_2.add(JPactu, BorderLayout.SOUTH);


    onglets.addTab("Affichage", onglet2_2);

    ongletsGraph.addTab("Durée des captures", JStmpCapture);
    ongletsGraph.addTab("Nombre de pages chargées", JPnbChargedPages);
    ongletsGraph.addTab("Poid des pages chargées", jScrollPane);
    ongletsGraph.addTab("Nombre de cookies créés", jScrollPaneCookie);
    ongletsGraph.addTab("Le site le plus utlisé", JSmostViewedSite);
    ongletsGraph.addTab("Methode HTTP la plus utilisée", JSmethode);


    JPanel gestBD = new JPanel();
    JLabel bd = new JLabel("Capture sélectionée : ");
    gestBD.add(bd);
    capName = new JComboBox(tab);
    gestBD.add(capName);
    nouveauNom = new JTextField("nouveau nom", 20);
    gestBD.add(nouveauNom);
    JButton submit= new JButton("Supprimer");
    gestBD.add(submit);
    CtrlGestBD ctrlGestBD = new CtrlGestBD(submit, this, analyseur);
    submit.addActionListener(ctrlGestBD);


    JButton modifier = new JButton("Modifier le nom");
    CtrlModifCap ctrlModifCap = new CtrlModifCap(modifier, this, analyseur);
    modifier.addActionListener(ctrlModifCap);
    gestBD.add(modifier);

    onglets.addTab("Gestion BD", gestBD);


    JMenuBar menuBar;
    JMenu menu, submenu;
    JMenuItem menuItem, ouvrir, save;

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

    save = new JMenuItem("Enregistrer Sous");
    submenu.add(save);
    CtrlSave ctrlSave = new CtrlSave(save, this, analyseur);
    save.addActionListener(ctrlSave);



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
    f.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height-20);
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
    txtprox.setEnabled(true);
    proxy_oui.setEnabled(true);
    proxy_non.setEnabled(true);
  }

  public void ConfigAuto(){
    txtprox.setEnabled(false);
    proxy_oui.setEnabled(false);
    proxy_non.setEnabled(false);
    proxy_non.setSelected(true);
    this.configProxyNon();
  }

  public void configProxyOui(){
    txtproxy.setEnabled(true);
    adressProxySysteme.setEnabled(true);
    adressProxySysteme.setText("http://proxyetu.iut-nantes.univ-nantes.prive");
    txtport.setEnabled(true);
    proxyPort.setEnabled(true);
    proxyPort.setValue(3128);
  }

  public void configProxyNon(){
    txtproxy.setEnabled(false);
    adressProxySysteme.setEnabled(false);
    txtport.setEnabled(false);
    proxyPort.setEnabled(false);
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

  public void maj_pagecharge(String s){
    nbChargedPages.setText(s);
  }

  public void clearJPMethode(){
    JPmethode.removeAll();
  }

  public void addJPCookie(JPanel add){
    JPnbCookie.add(add);
  }

  public void clearJPCookie(){
    JPnbCookie.removeAll();
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

  public void cleartempsSite(){
    JPtmpCapture.removeAll();
  }

  public void addtempsSite(JPanel add){
    JPtmpCapture.add(add);
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
    return (String)data.getSelectedItem();
  }

  public String getCapture(){
    return (String)capName.getSelectedItem();
  }

  public void majData(String[] data){
    this.data.removeAllItems();
    this.capName.removeAllItems();
    for (String s : data) {
      this.data.addItem(s);
      this.capName.addItem(s);
    }
  }


  public String getNouveauNom(){
    return nouveauNom.getText();
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
