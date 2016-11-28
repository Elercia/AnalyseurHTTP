package model;

import java.io.*;
import java.util.Date;

import org.json.simple.JSONObject;



public class BaseDeDonnees{

    private File fichier;

	/**
	 * Se servir de ce constructeur si on veut continuer une capture antérieur
	 * @param fileName
	 */
    public BaseDeDonnees(String fileName){
        //On instancie un Fichier pour pouvoir écrire et lire dans ce dernier.
	    if(!fileName.isEmpty())
            fichier = new File("data/"+fileName);
        else {
        	Date d = new Date();
		    fichier = new File("data/" +d.toString());
	    }
    }

	/**
	 * Enregistre au format json dans le fichier
	 * {
	 *     "google":{
	 *         "length":100,
	 *
	 *     },
	 *     "autresite":{
	 *
	 *     }
	 * }
	 *   HTTP/1.1 200 OK
	 *	 Date: Sat, 20 Nov 2010 13:37:00 GMT
	 *	 Content-Length: 2644
	 *	 Content-Type: text/html; charset=utf-8
	 *	 Content-Language: fr
     *
	 * @param titre Correspond au header recu ou envoyé
	 * @param values Correspond aux valeur qui doivent etre enregistré
	 */
    public synchronized void enregistrement(String titre, String values){
	    try {
		    FileWriter fw = new FileWriter(this.fichier, true);
		    JSONObject json = new JSONObject();

			json.put(titre, values);

		    fw.write(json.toString());

		    fw.close();
	    } catch (IOException e) {
		    e.printStackTrace();
	    }
    }

    public String lecture(String s){
	    //TODO
        return "";
    }
}
