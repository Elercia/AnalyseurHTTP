package model;

import java.io.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class BaseDeDonnees{

    private File fichier;

	/**
	 * @deprecated
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

    public BaseDeDonnees(File f){
	    //On instancie un Fichier pour pouvoir écrire et lire dans ce dernier.
	    if(f != null)
		    fichier = f;
	    else {
		    Date d = new Date();
		    DateFormat format = DateFormat.getDateTimeInstance(
				    DateFormat.MEDIUM,
				    DateFormat.MEDIUM);
		    String filename = "data/capture_"+format.format(d).replace(' ','_')+".csv";
		    fichier = new File(filename);
		    try{
			    fichier.createNewFile();
			    System.out.println("Fichier "+filename+" créé avec succès");
		    }catch (IOException e){
			    System.err.println("Probleme de création de fichier de base de données : "+fichier.getName());
		    }
	    }
    }

	/**
	 * Enregistre les deux headers
	 * On ne verfie pas non plus quel est le site qui a été visitées
	 * !! il faut le rajouter en premier dans le csv sous la forme site;nomdusite;....values
     *
	 *
	 * @param values Correspond au headers qui ont étés recus ou envoyé du type :
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

	/**
	 * hashmap qui fait correspondre : site vers données
	 * les données sont représenté par une hashMap se string vers string
	 * le 1er string représente les noms des headers et le 2nd les valeurs
	 * @return une hashmap qui fait correspondre : site vers données
	 */
	public HashMap<String, HashMap<String, String>> lecture(){

	    //onlit le fichier on le split par "\n"
	    //pour toutes les lignes on les split par ";"
	    //  On a donc des couples de valeurs name -> values
	    //  Le premier coupl => les nom du site
	    //  tous les autres => les nom, valeurs des headers
	    HashMap<String, HashMap<String, String>> retour = new HashMap<>();
	    HashMap<String, String> values = new HashMap<>();
	    String sitename="";
	    try {

		    Scanner sc=new Scanner(this.fichier);
		    while(sc.hasNextLine()){//pour toute les lignes
		    	String[] couples = sc.nextLine().split(";");//on récupére les couples
			    if(!couples[0].equals("site"))//si le premier couple ne donne pas le nom du site
			    	return null;
				//on vérifie que la taille soit pair (un nom vers une values et pas de valeurs seuls)
			    if(couples.length%2 != 0)
			    	return null;


			    sitename = couples[1];
			    for(int i=2; i < couples.length; i+=2){
					values.put(couples[i], couples[i+1]);
			    }
			    retour.put(sitename, values);
			    values = new HashMap<>();
		    }
	    } catch (FileNotFoundException e) {
		    e.printStackTrace();
	    }
	    return retour;
    }
}
