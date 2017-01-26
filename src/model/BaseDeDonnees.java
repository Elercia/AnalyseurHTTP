package model;

import javafx.util.Pair;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
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
	private HashMap<String, HashMap<String, Object>> values;

	/**
	 * Crée une base de données à partir d'un fichier
	 * Si le fichier est null alors un fichier sera cré dans le dossier "data/"
	 * Il aura comme nom : "capture_datdujour.csv
	 * @param f @Nullable Le fichier à considérer
	 */
	public BaseDeDonnees(File f){
		this.values = new HashMap<>();
		this.headersBuffer = new ArrayList<>();
		this.setFile(f);
	}

	/**
	 * Fonction utilisé pour set le fichier à utiliser
	 * @param f @Nullable Le fichier à utiliser
	 */
	private void setFile(File f){
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
	 * @see BaseDeDonnees::saveData()
	 */
	public synchronized void enregistrement(String requete, String reponse){
		this.headersBuffer.add(new Pair<>(requete, reponse));
	}

	/**
	 * hashmap qui fait correspondre : site vers données
	 * les données sont représenté par une hashMap se string vers string
	 * @return une hashmap qui fait correspondre : site vers données
	 */
	public synchronized HashMap<String, HashMap<String, Object>> actuValues(){
		//on doit lire le fichier
		//pour tous les couples Requetes, Reponse
		//on doit les synthetiser et remplire les différent nom que la vue vas utiliser
		//@see declaration de la variable

		HashMap<String, Object> nbPagesCharged      = new HashMap<>(),
								poidPagesCharged    = new HashMap<>(),
								nbCookiesCreated    = new HashMap<>(),
//								usedWebSite         = new HashMap<>(),
								methodeUsed         = new HashMap<>();

		//Si ya déjà des valeurs dans les hash map
		//je sais pas si c'est vraiment utile

//		if(this.values.containsKey("nbPagesCharged"))
//			nbPagesCharged.putAll(this.values.get("nbPagesCharged"));
//		if(this.values.containsKey("poidPagesCharged"))
//			poidPagesCharged.putAll(this.values.get("poidPagesCharged"));
//		if(this.values.containsKey("nbCookiesCreated"))
//			nbCookiesCreated.putAll(this.values.get("nbCookiesCreated"));
////		if(this.values.containsKey("usedWebSite"))
////			usedWebSite.putAll(this.values.get("usedWebSite"));
//		if(this.values.containsKey("methodeUsed"))
//			methodeUsed.putAll(this.values.get("methodeUsed"));

		JSONParser parser = new JSONParser();
		String content = null;
		try {
			content = this.readAllContent();
			Object obj = null;
			obj = parser.parse(content.isEmpty()?"{}":content);
			JSONObject jsonObjFileContent = (JSONObject) obj;

			//pour tous les objet dans notre fichier
			for(Object objs : jsonObjFileContent.keySet()){

				//on recupere le nom du site (la clé que l'on a)
				String site = (String)objs;
				//le JsonObject correspondant au site que l'on a visité
				JSONObject siteJsonObj = (JSONObject)jsonObjFileContent.get(objs);

				//on ajoute a la map du nombre de pages charge le site vers son nb de consultations
				nbPagesCharged.put(site, siteJsonObj.get("consultations"));

				//on ajoute a la map du des poids des pages chargé le poid total
				poidPagesCharged.put(site, siteJsonObj.get("poidTotal"));

				//si le site a mis en place des cookies
				if(siteJsonObj.containsKey("cookies"))
				{
					//on les recupere
					JSONObject cookie = (JSONObject)(siteJsonObj.get("cookies"));
					//pour tous les cookies qui on été set on prend leurs clé
					for(Object c : cookie.keySet()){
						//on les ajoute a la map des cookies créé
						nbCookiesCreated.put(site+"::"+c, cookie.get(c));
					}
				}

				//on recup je JSON des methodes utilisée pour charger la page
				JSONObject jsmethode = (JSONObject)siteJsonObj.get("methodes");
				//pour toutes les methodes utilisée pour chaque site
				for(Object methode : jsmethode.keySet()){
					//si on a deja enregistré la methode
					if(methodeUsed.containsKey(methode)){
						//on repren l'ancienne valeur et on ajoute la nouvelle
						methodeUsed.put((String)methode,
								(Long)methodeUsed.get(methode) +
								(Long)jsmethode.get(methode)
						);

					}else{
						methodeUsed.put((String)methode, jsmethode.get(methode));
					}
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//on les mets dans la map des valeurs
		this.values.put("nbPagesCharged", nbPagesCharged);
		this.values.put("poidPagesCharged", poidPagesCharged);
		this.values.put("nbCookiesCreated", nbCookiesCreated);
//		this.values.put("usedWebSite", usedWebSite);
		this.values.put("methodeUsed", methodeUsed);

		return this.values;
	}

	/**
	 * Methode permettant de lire tous le contenu du fichier
	 * @return Le contenu du fichier (lignes séparée par "\n")
	 * @throws FileNotFoundException
	 */
	private String readAllContent()throws FileNotFoundException{
		StringBuilder content = new StringBuilder();
		Scanner sc = new Scanner(this.fichier);

		while(sc.hasNextLine()){
			content.append(sc.nextLine()).append("\n");
		}

		return content.toString();
	}

	/**
	 * Traitement des pages enregistrée dans la liste de buffer
	 *
	 * On parse le fichier et on ajoute des données dedans
	 */
	public void saveData(){
		JSONParser parser = new JSONParser();
		//Toutes les hashMap pour les différent graphiques

		try {
			//append all to the existing file (if there is something in)
			String content = this.readAllContent();
			//si le fichier est vide on cré un Json vide sinon on utilise le contenu du fichier
			Object obj = parser.parse(content.isEmpty()?"{}":content);
			JSONObject JSonComplet = (JSONObject) obj;

			//une variable pour stocker les json object en rapport avec un site
			JSONObject siteJsonObject;

			//pour toute les paires dans le buffer
			for(Pair<String, String> p_header : this.headersBuffer){
				//on recupère la requete et la reponse
				String requete = p_header.getKey();
				String response = p_header.getValue();

				//le site correspond à l'host de la requete
				String site = this.getWebSite(requete);

				//on verifie si le site à déjà été chargé
				//donc que le fichier Json contient deja une clé qui est le nom du site
				if(JSonComplet.containsKey(site)){
					//on recup les valeurs déja existantes dans un objet Json
					siteJsonObject = (JSONObject)JSonComplet.get(site);

					//on met à ajour le nombre de consutlations
					Integer consultation = (Integer)(siteJsonObject.get("consultations"))+1;
					siteJsonObject.put("consultations", consultation);


					//on met a jour le nombre de methodes utilisées
					JSONObject jsonObjectMethod = (JSONObject)siteJsonObject.get("methodes");
					String methode = this.getMethod(requete);

					if(jsonObjectMethod.containsKey(methode)){
						Integer met = (Integer)(jsonObjectMethod.get(methode))+1;
						jsonObjectMethod.put(methode, met);
					}
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
					Integer poid = (Integer)(siteJsonObject.get("poidTotal"));
					siteJsonObject.put("poidTotal", (poid + this.getLength(response)));

					int temps = ((Integer)(siteJsonObject.get("timeUsed"))+this.getTime(requete))/consultation;
					siteJsonObject.put("timeUsed", temps);

					System.out.println("Deuxieme Json généré : "+site+" / "+siteJsonObject);
				}else{
					//si la page n'à pas enore été chargée alors on initialise des valeurs par defauts
					// vallant celles du premiere header

					siteJsonObject = new JSONObject();

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
					int length = this.getLength(response);
					siteJsonObject.put("poidTotal", length);


					//on ajoute le temps de la capture
					int temps = this.getTime(requete);
					siteJsonObject.put("timeUsed", temps);

					System.out.println("Premier Json généré : "+site+" / "+siteJsonObject);
				}

				//finalement on ajoute les valeurs dans un json pour le site
				JSonComplet.put(site, siteJsonObject);
				//System.out.println("Json modifié pour "+site+" en "+siteJsonObject.toJSONString());
			}//en foreach loop

			System.out.println("JSon sauvegarder: "+JSonComplet.toJSONString());

			BufferedWriter w = new BufferedWriter(new FileWriter(this.fichier));
			w.write(JSonComplet.toJSONString());
			w.close();

			//on clear le buffer pour faire d'autre captures
			headersBuffer.clear();

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
			//c'est la sous chaine a partir de l'index de "Host: "
			// jusqu'aux premier "\n" (en partant du meme index)
			String host = (header.substring(header.indexOf("host: ") + ("host: ").length(), header.indexOf("\n",
					header.indexOf("host: ")))).trim();
			if(host.contains(":"))
				host = host.split(":")[0];
			return host;
		}
		return "undefined";
	}

	private String getMethod(String header){
		String methode = header.split(" ", 2)[0];

		return methode == null || methode.isEmpty()?"undefined":methode;
	}

	/**
	 * Methode pour retrouver la taille d'une page dans un header grace à "content-length: "
	 * @param header le header reponse
	 * @return (int)
	 */
	private int getLength(String header){
		header = header.toLowerCase();
		if (header.contains("content-length: ")) {
			//on recupère la valeur de la taille du contenu
			//c'est la sous chaine a partir de l'index de "Content-Length: "
			// jusqu'aux premier "\n" (en partant du meme index)
			String length = (header.substring(header.indexOf("content-length: ") + ("content-length: ").length(),
					header.indexOf("\n", header.indexOf("content-length: ")))).trim();

			return Integer.parseInt(length);
		}
		return 0;
	}

	/**
	 * Methode retournant tous les cookies set par le serveur
	 *
	 * les cookies sont sous la forme
	 * Set-Cookie: theme=light
	 * Set-Cookie: sessionToken=abc123; Expires=Wed, 09 Jun 2021 10:18:14 GMT
	 * @param header le header de reponse du serveur
	 * @return une hashMap assossiant nom -> valeur
	 */
	private HashMap<String, String> getCookie(String header){
		HashMap<String, String> map = new HashMap<>();
		for(String line : header.split("\n")){
			if(line.contains("Set-Cookie: ")){
				String[] cookie = line.substring("Set-Cookie: ".length()).split("=",2);
				map.put(cookie[0], cookie[1]);
			}
			if(line.equals("\r\n") || line.equals("\n"))
				break;
		}
		return map;
	}

	private int getTime(String header){
		header = header.toLowerCase();
		if (header.contains("timeUsed: ")) {
			//on recupère la valeur de la taille du contenu
			//c'est la sous chaine a partir de l'index de "Content-Length: "
			// jusqu'aux premier "\n" (en partant du meme index)
			String time = (header.substring(header.indexOf("timeUsed: ") + ("timeUsed: ").length(),
					header.indexOf("\n", header.indexOf("timeUsed: ")))).trim();

			return Integer.parseInt(time);
		}
		return 0;
	}
}