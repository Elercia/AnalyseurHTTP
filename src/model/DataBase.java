package model;

import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.util.*;

/**
 * Created by E155399M on 30/01/17.
 */
public class DataBase {
	private Connection conn = null;

	private ArrayList<Pair<String, String>> buffer;

	public DataBase(String path) throws ClassNotFoundException, SQLException {
		//On setup le SQL lite
		Class.forName("org.sqlite.JDBC");

		conn = DriverManager.getConnection("jdbc:sqlite:data/"+path);
		Statement stmt = conn.createStatement();


		//creation de la table d'enregistrement en focntion de si elle existe ou pas
		//Pour ne pas supprimer de table existante
		String sql = "CREATE TABLE IF NOT EXISTS enregistrement (" +
				"id INTEGER PRIMARY KEY NOT NULL, " +
				"siteName TEXT NOT NULL, " +
				//"consultation INTEGER NOT NULL," +
				"methode TEXT NOT NULL," +
				"cookies TEXT NOT NULL," +
				"poid INTEGER NOT NULL" +
				"timeUsed INTEGER NOT NULL)";
		stmt.execute(sql);
		stmt.close();

		//on setup les variables de classe
		this.buffer = new ArrayList<>();
	}

	public void finalize()
	{
		try {
			super.finalize();
			conn.close();
		} catch (Throwable t) {
		}

	}

	/**
	 * Fonction utilisé pour set le fichier à utiliser
	 * @param f @Nullable Le fichier à utiliser
	 */
	private void setFile(File f){

	}

	/**
	 * Methode ajoutant les header reçu dans le buffer de headers
	 * Ce buffer sera écrit dans le fichier à la fin quand l'analyse sera finie
	 * @see BaseDeDonnees::saveData()
	 */
	public synchronized void enregistrement(String requete, String reponse){
		this.buffer.add(new Pair<>(requete, reponse));
	}

	/**
	 * hashmap qui fait correspondre : site vers données
	 * les données sont représenté par une hashMap se string vers string
	 * @return une hashmap qui fait correspondre : site vers données
	 */
	public synchronized HashMap<String, HashMap<String, Object>> actuValues(){
		return null;
	}

	/**
	 * Traitement des pages enregistrée dans la liste de buffer
	 *
	 * On parse le fichier et on ajoute des données dedans
	 */
	public void saveData(){
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			for (Pair<String, String> pair : this.buffer) {
				String requete = pair.getKey();
				String response = pair.getValue();

				String siteName = this.getWebSite(requete);
				String methode = this.getMethod(requete);
				String cookie = String.valueOf(this.getCookie(response));
				Integer poid = this.getLength(response);
				Integer timeUsed = this.getTime(requete);

				try {

					String sql = "INSERT INTO enregistrement (siteName,methode,cookies, poid, timeUsed) " +
							"VALUES(" +
							siteName+","+
							methode+","+
							cookie+","+
							poid+","+
							timeUsed+","+
							")";

					stmt.execute(sql);

				} catch (SQLException e) {
					System.out.println("Erreur d'enregistrement dans la base de données");
				}finally {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
	 * @return
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
	 * Methode retournant le temps qu'a pris la requete
	 * @param header le header requete
	 * @return
	 */
	private int getTime(String header){
		header = header.toLowerCase();
		if (header.contains("timeused: ")) {
			//on recupère la valeur de la taille du contenu
			//conn'est la sous chaine a partir de l'index de "Content-Length: "
			// jusqu'aux premier "\n" (en partant du meme index)
			String time = (header.substring(header.indexOf("timeused: ") + ("timeused: ").length(),
					header.indexOf("\n", header.indexOf("timeused: ")))).trim();

			return Integer.parseInt(time);
		}
		return 0;
	}

	public static void main(String[] args) {
		try {
			DataBase db = new DataBase("projet.db");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
