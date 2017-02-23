package model;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * Created by E155399M on 30/01/17.
 */
public class DataBase {

	/**
	 * Variable representant la connection à la base de données
	 * La connection est ouverte à la création et fermée quand l'object est détruis
	 */
	private Connection conn = null;

	/**
	 * Variable permettant de stocker les headers de requete et reponse avant de les enregistrer
	 * dans la base de données (le fichier dans data)
	 */
	private ArrayList<Trio<String, String, Long>> buffer_http;

	private ArrayList<Duo<String, Long>> buffer_https;

	/**
	 * Le nom de la capture attaché à la base de données
	 */
	public static String captureName;

	private String filePath;

	public DataBase(String path) throws ClassNotFoundException, SQLException {
		//On setup le SQL lite
		Class.forName("org.sqlite.JDBC");

		if(path == null || path.isEmpty()){
			path = this.generatePath();

			try {
				if(!Files.exists(Paths.get(path)))
					Files.createFile(Paths.get(path));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		this.filePath = Paths.get(path).toAbsolutePath().toString();

		conn = DriverManager.getConnection("jdbc:sqlite:"+path);

		this.generateTable();

        conn.setAutoCommit(false);
        conn.commit();
		//on setup les variables de classe
		this.buffer_http = new ArrayList<>();
		this.buffer_https = new ArrayList<>();

		//on instancie le nom de la capture
		DataBase.captureName = this.generateCaptureName();

		System.out.println("Base de données attaché au fichier : "+path +" avec le nom de capture : "+DataBase.captureName);
	}

	/**
	 * Methode générant la table utilisée par notre programme si elle n'existe pas dans notre fichier
	 * @throws SQLException
	 */
	public void generateTable() throws SQLException {
		Statement stmt = conn.createStatement();
		//creation de la table d'enregistrement en fonction de si elle existe ou pas
		//Pour ne pas supprimer de table existante
		//La table est définie par son nom de capture (généré par le pogramme à chaque lancement
		String sql = "CREATE TABLE IF NOT EXISTS enregistrement (" +
				"id INTEGER PRIMARY KEY NOT NULL, " +
				"captureName TEXT NOT NULL," +
				"siteName TEXT NOT NULL," +
				"methode TEXT NOT NULL," +
				"cookies TEXT NOT NULL," +
				"poid INTEGER NOT NULL," +
				"timeUsed INTEGER NOT NULL )";
		stmt.execute(sql);
		stmt.close();
	}

	/**
	 * On doit fermer la connection avec la base de données apres l'avoir utilisé
	 */
	public void finalize()
	{
		try {
			super.finalize();
			this.conn.commit();
			conn.close();
		} catch (Throwable t) {
		}

	}

	/**
	 * Fonction utilisé pour set le fichier à utiliser
	 * @param path @Nullable Le fichier à utiliser
	 */
	private void setFile(String path) throws SQLException {
		this.conn = DriverManager.getConnection("jdbc:sqlite:"+path);
	}

	private String generatePath(){
		return "data/database.db";
	}

	private String generateCaptureName(){
		Date d = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd_MM_yy_HH:mm:ss");

		//genere un nom de capture du type :
		//  capture_20_févr_2017_10_22_59
		String result = "capture_"+format.format(d).replace(' ','_').replace(".","_").replace(":","_");
		return result;
	}

	/**
	 * Methode ajoutant les header reçu dans le buffer_http de headers
	 * Ce buffer_http sera écrit dans le fichier à la fin quand l'analyse sera finie
	 */
	public synchronized void enregistrement(String requete, String reponse, long timeUsed){
		//tester si ya connect
		//si requete encoadable ascii alors la rep aussi
		//  donc split avec double retour pour ne pas avoir le corps (seulement le header)
		CharsetEncoder ce = Charset.forName("US-ASCII").newEncoder();

		if(requete.contains("CONNECT")){
			//cas ou c'est le premier truc du https
			buffer_https.add(new Duo<>(requete, timeUsed));
		}else{
			//on peut avoir du https ou  du http ici
			if(ce != null && ce.canEncode(requete))
			{
				//ca veut dire que la rep est en http normal donc on la split
				reponse = reponse.split("\r\n\r\n", 2)[0];
				this.buffer_http.add(new Trio<>(requete, reponse, timeUsed));
			}
		}
	}

	public HashMap<String, HashMap<String, Object>> actuValues() {
		return actuValues("");
	}
	/**
	 * hashmap qui fait correspondre : site vers données
	 * les données sont représenté par une hashMap se string vers string
	 * @return une hashmap qui fait correspondre : site vers données
	 */
	public HashMap<String, HashMap<String, Object>> actuValues(String captureN){
        this.saveData();
		HashMap<String, Object> nbPagesCharged      = new HashMap<>(),
								poidPagesCharged    = new HashMap<>(),
								nbCookiesCreated    = new HashMap<>(),
								methodeUsed         = new HashMap<>(),
								timeUsed            = new HashMap<>();

		//la map correspondant aux différentes valeurs récupérée à partir du fichier de base de données
		HashMap<String, HashMap<String, Object>> values = new HashMap<>();

		//Si on cherche a avoir les données globales, c'est a dire si on demande à charger "Toutes" ou que l'on a pas spécifié de nom de capture
		String count_sql = "SELECT siteName, count(*) FROM enregistrement GROUP BY siteName";
		String poidPages_sql = "SELECT siteName, sum(poid) FROM enregistrement GROUP BY siteName";
		String cookies_sql = "SELECT siteName, cookies FROM enregistrement";
		String method_sql = "SELECT methode, count(methode) FROM enregistrement GROUP BY methode";
		String time_sql = "SELECT siteName, AVG(timeUsed) FROM enregistrement GROUP BY siteName";

		if(!captureN.isEmpty() && !captureN.equalsIgnoreCase("toutes")){
			//Si on cherche a charger une capture qui possede un nom
			System.out.println("Demande capture spécifique : "+captureN);
			count_sql = "SELECT siteName, count(*) FROM enregistrement WHERE captureName='"+captureN+"' GROUP BY siteName";
			poidPages_sql = "SELECT siteName, sum(poid) FROM enregistrement WHERE captureName='"+captureN+"' GROUP BY siteName";
			cookies_sql = "SELECT siteName, cookies FROM enregistrement WHERE captureName='"+captureN+"'";
			method_sql = "SELECT methode, count(methode) FROM enregistrement WHERE captureName='"+captureN+"' GROUP BY methode";
			time_sql = "SELECT siteName, AVG(timeUsed) FROM enregistrement WHERE captureName='"+captureN+"'GROUP BY siteName";
		}

		try {
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(count_sql);

			//on enregistre tous les tuples dans notre hashMap
			while(resultSet.next()){
                nbPagesCharged.put(resultSet.getString(1), resultSet.getInt(2));
			}

            resultSet = stmt.executeQuery(poidPages_sql);
            //on enregistre le poid des pages pour tous le tuples de notre base
            while(resultSet.next()){
                poidPagesCharged.put(resultSet.getString(1), resultSet.getInt(2));
            }

            resultSet = stmt.executeQuery(cookies_sql);
            String key, value;
            while (resultSet.next()){
                key = resultSet.getString(1);//SiteName
                value = resultSet.getString(2);//les valeurs de cookies

                /*
                    TODO Il faut changer ça pour permettre de stocker plusieurs cookies par site
                    ICI chaque cookies supprimera l'ancien
                */
                nbCookiesCreated.put(key, value);
            }

            resultSet = stmt.executeQuery(method_sql);
            while (resultSet.next()){
	            key = resultSet.getString(1);
                value = resultSet.getString(2);
                methodeUsed.put(key, Integer.valueOf(value));
            }

            int val;
            resultSet = stmt.executeQuery(time_sql);
			while (resultSet.next()){
				key = resultSet.getString(1);
				val = resultSet.getInt(2);
				timeUsed.put(key, val);
			}

		} catch (SQLException e) {
			System.err.println("Erreur de lecture de la base de données ("+e.getSQLState()+")");
			e.printStackTrace();
		}

		values.put("nbPagesCharged", nbPagesCharged);
        values.put("poidPagesCharged", poidPagesCharged);
        values.put("nbCookiesCreated", nbCookiesCreated);
        values.put("methodeUsed", methodeUsed);
		values.put("temps", timeUsed);

		return values;
	}

	public synchronized void saveData(){
		this.saveData(DataBase.captureName);
	}

	/**
	 * Traitement des pages enregistrée dans la liste de buffer_http
	 *
	 * On parse le fichier et on ajoute des données dedans
	 */
	public synchronized void saveData(String captureN){
		PreparedStatement stmt = null;

		try {
			//on cré la requete à executer
			//elle est toujours la même donc pas besoin de tjrs la recréer
			String sql = "INSERT INTO enregistrement (captureName, siteName,methode,cookies, poid, timeUsed) " +
					"VALUES(?,?,?,?,?,?)";
			stmt = conn.prepareStatement(sql);

			for (Trio<String, String, Long> values : this.buffer_http) {
				String requete = values.one;
				String response = values.two;
				long timeUsed = values.three;

				String siteName = this.getWebSite(requete);
				String methode = this.getMethod(requete);
				String cookie = String.valueOf(this.getCookie(response));
				Integer poid = this.getLength(response);

				//on associe chaque parametre aux points d'intérogations dans le String "sql"
				stmt.setString(1, captureN);
				stmt.setString(2, siteName);
				stmt.setString(3, methode);
				stmt.setString(4, cookie);
				stmt.setInt(5, poid);
				stmt.setLong(6, timeUsed);

				try {
					//on execute la requete sql
					stmt.executeUpdate();
				} catch (SQLException e) {
					System.err.println("Erreur d'enregistrement dans la base de données ("+e.getSQLState()+")");
					e.printStackTrace();
				}
			}//fin parcour bufferHTTP

			for (Duo<String, Long> duo : buffer_https) {
				String requ = duo.one+"\n";
				Long time = duo.two;

				String host = this.getWebSite(requ);

				stmt.setString(1, captureN);
				stmt.setString(2, host);
				stmt.setString(3, "GET");
				stmt.setString(4, "{}");
				stmt.setInt(5, 0);
				stmt.setLong(6, time);

				try {
					//on execute la requete sql
					stmt.executeUpdate();
				} catch (SQLException e) {
					System.err.println("Erreur d'enregistrement dans la base de données ("+e.getSQLState()+")");
					e.printStackTrace();
				}
			}

			//on vide le buffer_http pour ne pas enregister 2 fois les mêmes chose par la suite
			this.buffer_http.clear();
			this.buffer_https.clear();
		} catch (SQLException e) {
			System.err.println("Erreur de création de l'état de la base de données ("+e.getSQLState()+")");
			e.printStackTrace();
		}finally {
			try {
				if(stmt != null)
					stmt.close();
                conn.commit();
			} catch (SQLException e) {
				System.err.println("Erreur lors de la fermeture de l'état d'enregistrement ("+e.getSQLState()+")");
				e.printStackTrace();
			}
		}
	}

	private String getWebSite(String header){
		header = header.toLowerCase();
		if (header.contains("host: ")) {
			//on recupère l'host
			//conn'est la sous chaine a partir de l'index de "Host: "
			// jusqu'aux premier "\n" (en partant du meme index)
			String host = (header.substring(header.indexOf("host: ") + ("host: ").length(), header.indexOf("\n",
					header.indexOf("host: ")))).trim();
			if(host.contains(":"))
				host = host.split(":")[0];
			return host;
		}
		return "undefined";
	}

	/**
	 * Retourne la methode utilisé pour la requete
	 * @param header le header requete
	 * @return la methode utilisée par le header
	 */
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
			//conn'est la sous chaine a partir de l'index de "Content-Length: "
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

	/**
	 * Methode permettant de connaitre tous les nom de capture enregistrée dans la base de données
	 * @return un Arraylist correspondant
	 */
	public ArrayList<String> getCapturesNames(){
		String sql = "SELECT captureName from enregistrement GROUP BY captureName";
		ArrayList<String> ar = new ArrayList<>();
		ar.add("Toutes");
		try {
			Statement stmt = this.conn.createStatement();

			stmt.execute(sql);
			ResultSet set = stmt.getResultSet();

			while(set.next()){
				ar.add(set.getString(1));
			}

			return ar;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

	/**
	 * Methode permettant de supprimer une capture
	 * @param captureN le nom de la capture à supprimer ou "toutes" si on veut toutes les supprimer
	 */
	public void supprimerCapture(String captureN){
		String sql = "DELETE FROM enregistrement";

		if(!captureN.equalsIgnoreCase("toutes"))
			sql += " WHERE captureName=?";

		PreparedStatement stmt = null;
		try {
			stmt = this.conn.prepareStatement(sql);

			if(!captureN.equalsIgnoreCase("toutes"))
				stmt.setString(1, captureN);


			stmt.executeUpdate();
			this.conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getCurrentFilePath(){
		return this.filePath;
	}

	public void modifierCaptureName(String oldCN, String newCN){
		String sql;
		if(oldCN.equalsIgnoreCase("Toutes"))
			sql = "UPDATE enregistrement SET captureName=?";
		else
			sql = "UPDATE enregistrement SET captureName=? WHERE captureName=?";

		PreparedStatement stmt = null;
		try {
			stmt = this.conn.prepareStatement(sql);
			stmt.setString(1, newCN);

			if(!oldCN.equalsIgnoreCase("Toutes"))
				stmt.setString(2, oldCN);

			stmt.executeUpdate();
			this.conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Classe utilisée pour stocker les trio de header + temps
	 * @param <T1>
	 * @param <T2>
	 * @param <T3>
	 */
	private class Trio<T1, T2, T3> {
		public T1 one;
		public T2 two;
		public T3 three;

		public Trio(T1 t1, T2 t2, T3 t3){
			this.one = t1;
			this.two = t2;
			this.three = t3;
		}
	}

	private class Duo<T1, T2>{
		public T1 one;
		public T2 two;

		public Duo(T1 o, T2 t){
			this.one = o;
			this.two = t;
		}
	}
}
