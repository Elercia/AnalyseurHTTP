package model;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

public class BaseDeDonnees{

    private File fichier;

	/**
	 * Se servir de ce constructeur si on veut continuer une capture antérieur
	 * @param fileName le nom du fichier ou null ou vide
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
		    fichier = new File("data/capture_"+format.format(d).replace(' ','_')+".csv");
	    }
	    try{
		    fichier.createNewFile();
	    }catch (IOException e){
		    System.err.println("Probleme de création de fichier de base de données : "+fichier.getName());
	    }
    }

	/**
	 * Enregistre au format json dans le fichier
	 * Il n'y a pas d'indication concernant si le header enregistré est la demande ou la réponse
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
    public synchronized void enregistrement(String values){
		//recup le contenu du fichier
	    String filecontent="";

	    try {
		    Scanner sc = new Scanner(this.fichier);
		    while (sc.hasNextLine())
			    filecontent+=sc.nextLine();
			sc.close();
	    } catch (FileNotFoundException e) {
		    System.err.println("Fichier de base de données non trouvé. Echec de la sauvegarde");
		    return;
	    }

		//on parse ce que l'on nous a fournis pour l'enregistrer
	    String[] lines = values.split("\n");

	    for (String line : lines){
	    	filecontent+="\n";
			String[] res = line.split(":", 1);
		    filecontent+=res[0]+";"+res[1]+";";
	    }

	    //on enregistre dans le fichier
	    try {
		    FileWriter fw = new FileWriter(this.fichier);
		    fw.write(filecontent);
		    fw.close();
	    } catch (IOException e) {
		    System.err.println("Fichier de base de données non trouvé. Echec de la sauvegarde");
		    return;
	    }
    }

    public String lecture(String s){
	    //TODO
        return "";
    }
}
