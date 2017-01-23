package model;

import javafx.util.Pair;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.text.DateFormat;
import java.util.*;

public class BaseDeDonnees{
	private File fichier;
	private static String FileExtension = ".json";

	/**
	 * Variable contenant tous les headers recu par les thread proxy
	 */
	private ArrayList<Pair<String, String>> headersBuffer;

	/** Variable utilsé pour stocker les données calculées
	 * On associe un nom de variables à avoir vers des groupes de données
	 *  par exemple pour le graphique de méthodes les plus utilisé on aura
	 *   method -> (GET -> 10, POSt -> 10 ,...)
	 *  pour le nombres de pages chargées on aura
	 *   pageCharge -> (site1 -> 100, site2 -> 3)
	 */
	private HashMap<String, HashMap<String, String>> values;

	/**
	 * Crée une base de données à partir d'un fichier
	 * Si le fichier est null alors un fichier sera cré dans le dossier "data/"
	 * Il aura comme nom : "capture_datdujour.csv
	 * @param f @Nullable Le fichier à considérer
	 */
	public BaseDeDonnees(File f){
		this.values = new HashMap<>();
		this.headersBuffer = new ArrayList<>();
		if(f != null)
			fichier = f;
		else {
			Date d = new Date();
			DateFormat format = DateFormat.getDateTimeInstance(
					DateFormat.MEDIUM,
					DateFormat.MEDIUM);
			String filename = "data/capture_"+format.format(d).replace(' ','_').replace(".","").replace(":","_")+BaseDeDonnees.FileExtension;
			fichier = new File(filename);
			try{
				fichier.createNewFile();
				System.out.println("Fichier "+filename+" créé avec succès");
			}catch (IOException e){
				System.err.println("Probleme de création de fichier de base de données : "+fichier.getName()+" erreur : "+e.getMessage());
			}
		}
	}

	/**
	 * Methode ajoutant les header reçu dans le buffer de headers
	 * Ce buffer sera écrit dans le fichier à la fin quand l'analyse sera finie
	 */
	public synchronized void enregistrement(String requete, String reponse){
		this.headersBuffer.add(new Pair<>(requete, reponse));
	}

	/**
	 * hashmap qui fait correspondre : site vers données
	 * les données sont représenté par une hashMap se string vers string
	 * le 1er string représente les noms des headers et le 2nd les valeurs
	 * @return une hashmap qui fait correspondre : site vers données
	 */
	public synchronized HashMap<String, HashMap<String, String>> actuValues(){
		//on doit lire le fichier
		//pour tous les couples Requetes, Reponse
		//on doit les synthetiser et remplire les différent nom que la vue vas utiliser
		//@see declaration de la variable

		HashMap<String, String> nbPagesCharged      = new HashMap<>(),
								poidPagesCharged    = new HashMap<>(),
								nbCookiesCreated    = new HashMap<>(),
								usedWebSite         = new HashMap<>(),
								methodeUsed         = new HashMap<>();

		return this.values;
	}

	private String readAllContent()throws FileNotFoundException{
		StringBuilder content = new StringBuilder();
		Scanner sc = new Scanner(this.fichier);

		while(sc.hasNextLine()){
			content.append(sc.nextLine()).append("\n");
		}

		return content.toString();
	}

	public void saveData(){
		JSONParser parser = new JSONParser();
		//Toutes les hashMap pour les différent graphiques


		try {
			//append all to the existing file (if there is something in

			String content = this.readAllContent();
			//si le fichier est vide en cré un Json vide sinon on utilise le contenu du fichier
			Object obj = parser.parse(content.isEmpty()?"{}":content);
			JSONObject jsonObjFileContent = (JSONObject) obj;

			JSONObject siteJsonObject = new JSONObject();

			for(Pair<String, String> p_header : this.headersBuffer){
				String requete = p_header.getKey();
				String response = p_header.getValue();

				String site = this.getWebSite(requete);

				//on verifie si le site à déjà été chargé
				if(jsonObjFileContent.containsKey(site)){
					//on recup les valeurs déja existantes

					//on met à ajour le nombre de consutlations
					siteJsonObject = (JSONObject)jsonObjFileContent.get(site);
					siteJsonObject.put("consultations", (Integer)siteJsonObject.get("consultations")+1);

					//on met a jour le nombre de methodes utilisées
					JSONObject jsonObjectMethod = (JSONObject)siteJsonObject.get("methodes");
					String methode = this.getMethod(requete);
					if(jsonObjectMethod.containsKey(methode))
						jsonObjectMethod.put(methode, (Integer)siteJsonObject.get(methode)+1);
					else
						jsonObjectMethod.put(methode, 1);
					siteJsonObject.put("methodes", jsonObjectMethod);

					//on mets a jour les cookies si il exites
					//sinon on cré un objet cookie pour les ajouter au json

					HashMap<String, String> map = this.getCookie(response);
					if(!map.isEmpty()){//il n'y a pas de cookie a faire on garde donc les meme valeurs que avant
						JSONObject jsonObjCookies;
						if(siteJsonObject.containsKey("cookies"))
							 jsonObjCookies = (JSONObject)siteJsonObject.get("cookies");
						else
							jsonObjCookies = new JSONObject();
						//on rempli les cookies
						for(Map.Entry<String, String> en : map.entrySet())
							jsonObjCookies.put(en.getKey(), en.getValue());
						siteJsonObject.put("cookies", jsonObjCookies);
					}

					//on mets a jour le poids total des pages chargées
					siteJsonObject.put("poidTotal", (Integer)siteJsonObject.get("poidTotal")+this.getLength(response));

				}else{
					//si la page n'à pas enore été chargée alors on initialise des valeurs par defauts
					// vallant celles du premeiere header

					//le nombre de consultations
					siteJsonObject.put("consultations", 1);

					//la methode utilisée
					JSONObject jsonObjectMethode = new JSONObject();
					jsonObjectMethode.put(this.getMethod(requete), 1);
					siteJsonObject.put("methodes", jsonObjectMethode);

					//si des cookies ont été utilisés
					if(this.getCookie(response) != null){
						JSONObject cookieJsonObject = new JSONObject();
						for(Map.Entry<String, String> en : this.getCookie(response).entrySet())
							cookieJsonObject.put(en.getKey(), en.getValue());
						siteJsonObject.put("cookies", cookieJsonObject);
					}

					//on ajoute le poid
					siteJsonObject.put("poidTotal", this.getLength(response));
				}

				//finalement on ajoute les valeurs dans un json pour le site
				jsonObjFileContent.put(site, siteJsonObject);
				System.out.println("Json modifié pour "+site+" en "+siteJsonObject.toJSONString());
			}
			System.out.println("Final json : "+jsonObjFileContent.toJSONString());
			jsonObjFileContent.writeJSONString(new BufferedWriter(new FileWriter(this.fichier)));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private String getWebSite(String header){
		header = header.toLowerCase();
		if (header.contains("host: ")) {
			//on recupère l'host
			//c'est la sous chaine a partir de l'index de "Host: " jusqu'aux premier "\n" (en partant du meme index)
			String host = (header.substring(header.indexOf("host: ") + ("host: ").length(), header.indexOf("\n", header.indexOf("host: ")))).trim();
			if(host.contains(":"))
				host = host.split(":")[0];
			return host;
		}
		return "undefined";
	}

	private String getMethod(String header){
		String methode = header.split(" ", 1)[0];

		return methode.isEmpty()?"undefined":methode;
	}

	private int getLength(String header){
		header = header.toLowerCase();
		if (header.contains("Content-Length: ")) {
			//on recupère l'host
			//c'est la sous chaine a partir de l'index de "Host: " jusqu'aux premier "\n" (en partant du meme index)
			String length = (header.substring(header.indexOf("Content-Length: ") + ("Content-Length: ").length(), header.indexOf("\n", header.indexOf("Content-Length: ")))).trim();

			return Integer.parseInt(length);
		}
		return 0;
	}

	/**
	 * Methode retournant tous les cookies set par le serveur
	 * @param header le header de reponse du serveur
	 * @return une hashMap assossiant nom -> valeur
	 */
	private HashMap<String, String> getCookie(String header){
		//TODO : à implementer
		return new HashMap<>();
	}
}
