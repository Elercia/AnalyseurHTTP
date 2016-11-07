package model;

import model.proxy.ProxyHTTP.java;

public class Analyseur {

  private ProxyHTTP proxy;
  private BaseDeDonnees bdd;

  public Analyseur(){ }

  public void debutEcoute(int port, String fileName){
    //Instanciation d'une nouvelle BaseDeDonnees
    bdd = new BaseDeDonnees(fileName);
    //On instancie le proxy avec le port voulu
    proxy = new ProxyHTTP(port);
    //On démarre le proxy qui démarera dans un Thread
    proxy.start();
  }

  public void finEcoute(){
    //On stop le proxy
    proxy.stop();
  }

  public void traite(String requete){
    //TODO
  }

  public String recuperationDonnees(String donnees){
    //TODO
  }

  public String traiteAffichage(String s){
    //TODO
  }
}
