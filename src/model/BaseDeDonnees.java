package model;

import java.io.*;

public class BaseDeDonnees{

  private File fichier;

  public BaseDeDonnees(String fileName){
    //On instancie un Fichier pour pouvoir écrire et lire dans ce dernier.
    fichier = new File("../../data/"+fileName);
  }

  public void enregistrement(String s){
    //TODO
  }

  public String lecture(String s){
    //TODO
    return "";
  }
}
