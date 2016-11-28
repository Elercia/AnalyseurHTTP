package model;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class BaseDeDonnees{

    private File fichier;

	/**
	 * Se servir de ce constructeur si on veut continuer une capture antérieur
	 * @param fileName
	 */
    public BaseDeDonnees(String fileName){
        //On instancie un Fichier pour pouvoir écrire et lire dans ce dernier.
	    if(fileName != null && !fileName.isEmpty())
            fichier = new File("data/"+fileName);
        else {
        	Date d = new Date();
		    DateFormat format = DateFormat.getDateTimeInstance(
				    DateFormat.MEDIUM,
				    DateFormat.MEDIUM);
		    fichier = new File("data/capture_"+format.format(d).replace(' ','_')+".json");
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
     *
	 * @param values Correspond au header recu ou envoyé du type :
	 *
	    Age: 2237
		Cache-Control: max-age=31536000, public
		Connection: keep-alive
		Date: Mon, 28 Nov 2016 15:10:11 GMT
		Expires: Tue, 28 Nov 2017 14:32:54 GMT
		Timing-Allow-Origin: https://github.com
		Vary: Accept-Encoding
		Via: 1.1 varnish
		X-Cache: HIT
		X-Cache-Hits: 895
		X-Fastly-Request-ID: ae9566e14b90571caa3f543702c583af98b63246
		X-Served-By: cache-ams4131-AMS
		X-Timer: S1480345811.559115,VS0,VE0
	 *
	 *	 Nous devons récupérer le host (la page qui a été demandé) et
	 *      y ajouter l'heure actuel pour l'ajouter
	 */
    public synchronized void enregistrement(String nom, String values){
	    try {
		    Scanner fr = new Scanner(this.fichier);
		    String fileContent = "";
		    JSONObject json = new JSONObject();
		    JSONParser parse = new JSONParser();

		    //on récupere le contenu du fichier
		    while(fr.hasNextLine())
				fileContent+=fr.nextLine();
			fr.close();

		    try {
		    	json = (JSONObject)parse.parse(fileContent);

		    } catch (ParseException e) {
			    e.printStackTrace();
		    }

		    FileWriter fw = new FileWriter(this.fichier, true);


		    /*String[] tab = values.split(":", 1);
		    if(json.containsKey(tab[0])){
			    JSONArray ar = new JSONArray();

				Object obj = json.get(tab[0]);

			    ar.add(tab[1]);
			    ar.add(obj);
			    json.put(tab[0], ar);
		    }else{
			    json.put(tab[0], tab[1]);
		    }*/

		    if(json.containsKey(nom)){
			    Object obj = json.get(nom);
		    }else{

		    }


		    fw.write(json.toJSONString());

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
