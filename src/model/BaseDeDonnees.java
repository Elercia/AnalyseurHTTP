package model;

import java.io.*;
import org.json.simple.JSONObject;



public class BaseDeDonnees{

    private File fichier;

    public BaseDeDonnees(String fileName){
        //On instancie un Fichier pour pouvoir écrire et lire dans ce dernier.
        fichier = new File("../../data/"+fileName);
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
	 * @param s Correspond au header recu ou envoyé
	 *
	 */
    public synchronized void enregistrement(String s){
	    try {
		    FileWriter fw = new FileWriter(this.fichier);
		    JSONObject json = new JSONObject();

			json.put("", "");

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
