package model;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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
	private ArrayList<Trio<String, String, Long>> buffer;


	public static String captureName;

	public DataBase(String path) throws ClassNotFoundException, SQLException {
		//On setup le SQL lite
		Class.forName("org.sqlite.JDBC");

		if(path == null || path.isEmpty()){
			path = this.generatePath();

			try {
				Files.createFile(Paths.get(path));
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		conn = DriverManager.getConnection("jdbc:sqlite:"+path);
		Statement stmt = conn.createStatement();


		//creation de la table d'enregistrement en fonction de si elle existe ou pas
		//Pour ne pas supprimer de table existante
		//La table est définie par son nom de capture (généré par le pogramme à chaque lancement
		String sql = "CREATE TABLE IF NOT EXISTS enregistrement (" +
                    "captureName TEXT PRIMARY KEY NOT NULL, " +
                    "siteName TEXT NOT NULL," +
                    "methode TEXT NOT NULL," +
                    "cookies TEXT NOT NULL," +
                    "poid INTEGER NOT NULL," +
                    "timeUsed INTEGER NOT NULL )";
		stmt.execute(sql);
		stmt.close();

        conn.setAutoCommit(false);
        conn.commit();
		//on setup les variables de classe
		this.buffer = new ArrayList<>();

		//on instancie le nom de la capture
		DataBase.captureName = this.generateCaptureName();

		System.out.println("DataBase linked to file : "+path);
	}

	/**
	 * On doit fermer la connection avec la base de données apres l'avoir utilisé
	 */
	public void finalize()
	{
		try {
			super.finalize();
			conn.close();
		} catch (Throwable t) {
		}

	}

	/**
     * @deprecated
	 * Fonction utilisé pour set le fichier à utiliser
	 * @param path @Nullable Le fichier à utiliser
	 */
	private void setFile(String path){
	}

	private String generatePath(){
		return "data/database.db";

		/*Date d = new Date();
		DateFormat format = DateFormat.getDateTimeInstance(
				DateFormat.MEDIUM,
				DateFormat.MEDIUM);

		return "data/capture_"+format.format(d).replace(' ','_').replace(".","").replace(":","_")+".db";*/
	}

	private String generateCaptureName(){
		Date d = new Date();
//		DateFormat format = DateFormat.getDateTimeInstance(
//				DateFormat.MEDIUM,
//				DateFormat.MEDIUM);

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

		//genere un nom de capture du type :
		//  capture_20_févr_2017_10_22_59
		String result = "capture_"+format.format(d).replace(' ','_').replace(".","_").replace(":","_");
		System.out.println(result);
		return result;
	}

	/**
	 * Methode ajoutant les header reçu dans le buffer de headers
	 * Ce buffer sera écrit dans le fichier à la fin quand l'analyse sera finie
	 */
	public synchronized void enregistrement(String requete, String reponse, long timeUsed){
		this.buffer.add(new Trio<>(requete, reponse, timeUsed));
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
								methodeUsed         = new HashMap<>();

		//la map correspondant aux différentes valeurs récupérée à partir du fichier de base de données
		HashMap<String, HashMap<String, Object>> values = new HashMap<>();

		String count = "SELECT siteName, count(*) FROM enregistrement GROUP BY siteName";
		String poidPages = "SELECT siteName, sum(poid) FROM enregistrement GROUP BY siteName";
		String cookies = "SELECT siteName, cookies FROM enregistrement";
		String method = "SELECT methode, count(methode) FROM enregistrement GROUP BY methode";

		if(captureN.isEmpty()){
			count += " WHERE captureName="+DataBase.captureName;
			poidPages += " WHERE captureName="+DataBase.captureName;
			cookies += " WHERE captureName="+DataBase.captureName;
			method += " WHERE captureName="+DataBase.captureName;
		}

		try {
			Statement stmt = conn.createStatement();

			ResultSet resultSet = stmt.executeQuery(count);

			//on enregistre tous les tuples dans notre hashMap
			while(resultSet.next()){
                nbPagesCharged.put(resultSet.getString(1), resultSet.getInt(2));
			}

            resultSet = stmt.executeQuery(poidPages);

            //on enregistre le poid des pages pour tous le tuples de notre base
            while(resultSet.next()){
                poidPagesCharged.put(resultSet.getString(1), resultSet.getInt(2));
            }

            resultSet = stmt.executeQuery(cookies);
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

            resultSet = stmt.executeQuery(method);
            while (resultSet.next()){
	            key = resultSet.getString(1);
                value = resultSet.getString(2);
                methodeUsed.put(key, Integer.valueOf(value));
            }

		} catch (SQLException e) {
			System.err.println("Erreur de lecture de la base de données ("+e.getSQLState()+")");
			e.printStackTrace();
		}

		values.put("nbPagesCharged", nbPagesCharged);
        values.put("poidPagesCharged", poidPagesCharged);
        values.put("nbCookiesCreated", nbCookiesCreated);
        values.put("methodeUsed", methodeUsed);

		return values;
	}
	public synchronized void saveData(){
		this.saveData(DataBase.captureName);
	}

	/**
	 * Traitement des pages enregistrée dans la liste de buffer
	 *
	 * On parse le fichier et on ajoute des données dedans
	 */
	public synchronized void saveData(String captureName){
		PreparedStatement stmt = null;

		try {
			//on cré la requete à executer
			//elle est toujours la même donc pas besoin de tjrs la recréer
			String sql = "INSERT INTO enregistrement (captureName, siteName,methode,cookies, poid, timeUsed) " +
					"VALUES("+captureName+",?,?,?,?,?)";
			stmt = conn.prepareStatement(sql);

			for (Trio<String, String, Long> values : this.buffer) {
				String requete = values.one;
				String response = values.two;
				long timeUsed = values.three;

				String siteName = this.getWebSite(requete);
				String methode = this.getMethod(requete);
				String cookie = String.valueOf(this.getCookie(response));
				Integer poid = this.getLength(response);

				//on associe chaque parametre aux points d'intérogations dans le String "sql"
				stmt.setString(1, siteName);
				stmt.setString(2, methode);
				stmt.setString(3, cookie);
				stmt.setInt(4, poid);
				stmt.setLong(5, timeUsed);

				try {
					//on execute la requete sql
					stmt.executeUpdate();
				} catch (SQLException e) {
					System.err.println("Erreur d'enregistrement dans la base de données ("+e.getSQLState()+")");
				}
			}
			//on vide le buffer pour ne pas enregister 2 fois les même chose par la suite
			this.buffer.clear();
			assert this.buffer.size() == 0;
		} catch (SQLException e) {
			System.err.println("Erreur de création de l'état de la base de données ("+e.getSQLState()+")");
		}finally {
			try {
				if(stmt != null)
					stmt.close();
                conn.commit();
			} catch (SQLException e) {
				System.err.println("Erreur lors de la fermeture de l'état d'enregistrement ("+e.getSQLState()+")");
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

	public String[] getCapturesNames(){
		String sql = "SELECT captureName from enregistement";
		ArrayList<String> ar = new ArrayList<>();
		try {
			Statement stmt = this.conn.createStatement();

			stmt.execute(sql);
			ResultSet set = stmt.getResultSet();

			while(set.next()){
				ar.add(set.getString(1));
			}

			return (String[])ar.toArray();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new String[0];
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
}
