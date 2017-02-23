package model;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.*;

/**
 * Classe permettant de lancer les captures
 * ansi que de faire le lien entre la base de données et la partie visuelle
 */
public class Analyseur implements Runnable {
	/**
	 * Le service d'ececution des Threads permettant le chargement des pages
	 */
	private ExecutorService executorService;

	/**
	 * La base de données où l'on enregistre toutes les infos que le programme récupère
	 */
	private DataBase bdd;

	/**
	 * Le server socket nous permettant de recevoir les demandes du client
	 */
	private ServerSocket serverSocket;

	/**
	 * Cette variable est utilisée pour avoir des informations sur l'état du programme
	 * Elle nous permet de demander qu programme de commencer une capture ainsi que de la stoper
	 */
	private boolean listening;

	/**
	 * Cette variable est utilisée pour avoir des informations sur l'état du programme
	 * Elle nous permet de savoir si le programme est en train de s'arreter
	 */
	private boolean isStoping;

	/**
	 * Les variables relatives à la configuration de l'utilisateur
	 * Utilise t-il un proxy ?
	 * Si oui lequel
	 */
	private boolean usingProxy;
	private String proxyAdress;
	private int proxyPort;

	/**
	 * Le constructeur de l'Analyseur
	 * Ce constructeur instancie les différentes varaibles de classe avec des valeurs par default
	 * Il cré une nouvelle base de données ainsi que le service d'execution des thread
	 */
	public Analyseur(){
		//Nous avons choisi un execteur nous permttant d'avoir un nombre indéfini de threads utilisable
		this.executorService = Executors.newCachedThreadPool();

		this.isStoping = false;

		this.serverSocket = null;

		//Par default l'utilisateur n'utilise pas de proxy
		this.usingProxy = false;
		this.proxyAdress ="";
		this.proxyPort=0;

		//On cré la base de données
		try {
			this.bdd = new DataBase("");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode utilisé pour changer le port sur lequel le programme écoute
	 * C'est a dire quel port le proxy du navigateur est configuré
	 * @param port Le port en question
	 * @throws IOException
	 */
	public void setPort(int port)throws IOException{
		if(serverSocket == null)
			this.serverSocket = new ServerSocket(port);

		System.out.println("Proxy HTTP lancé sur le port "+port);
	}

	/**
	 * Méthode réinitialisant la base de donnée en lui fournissant un nouveau chemin
	 * (un nouveau fichier dans lequel sauvegarder)
	 * @param path le chemin d'acces au fichier (relatif ou  absolu)
	 */
	public void setFile(String path){
		try {
			this.bdd = new DataBase(path);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Cette méthode change les configurations de l'utilisateur concernant le proxy déjà existant
	 * @param adress l'adresse du proxy au quel envoyer les données
	 * @param port Sur quel port les envoyer
	 */
	public void setProxy(String adress, int port){
		this.usingProxy = true;
		this.proxyAdress = adress;
		this.proxyPort = port;
	}

	/**
	 * Methode permettant de récupérer les données de la base de données concernant une capture données
	 * (Toutes, capture_****, ...)
	 * @param captureName le nom de la capture ou "Toutes"
	 * @return cf le retour de la methode actuValues dans la base de données
	 */
	public HashMap<String, HashMap<String, Object>> getData(String captureName){
		return this.bdd.actuValues(captureName);
	}

	/**
	 * Retourne les captures réalsiée
	 * @return une ArrayList contenant les noms des captures ainsi que le nom "Toutes"
	 */
	public ArrayList<String> getCapturesNames(){
		return this.bdd.getCapturesNames();
	}

	/**
	 * Methode executée lorsque l'on cré le thread de l'analyseur
	 * Cette methode lance la méthode débutecoute apres avoir initialisé les base de données (si cela n'a pas été fait)
	 * il initialise aussi le socket d'écoute (interface avec l'utilisateur)
	 * @see Analyseur::debutEcoute
	 */
	@Override
	public void run() {
		try {
			if(this.bdd == null)
				this.setFile(null);
			if(this.serverSocket == null)
				this.setPort(9999);

			this.debutEcoute();
		}catch(IOException e){
			System.err.println("Erreur dans le thread analyseur ("+e.getMessage()+")");
		}
	}

	/**
	 * Method permettant de démarrer des proxysHTTP en fonction des demandes du navigateur
	 * Ils sont soumit au service d'execution
	 * Cette methode boucle tout pendant que l'on ne demande pas l'arret avec la méthode finEcoute
	 * @throws IOException
	 */
	private void debutEcoute() throws IOException{
		this.listening = true;
		ProxyHTTP proxyHTTP = null;
		while(this.listening) {
			proxyHTTP = new ProxyHTTP(this.serverSocket.accept(), ProxyHTTP.PROXY_NUMBERS++, bdd);

			if(this.usingProxy)
				proxyHTTP.setProxy(this.proxyAdress, this.proxyPort);

			executorService.execute(proxyHTTP);
		}//fin while
	}

	/**
	 * Methode permettant de mettre fin a l'écoute en cours
	 * Le service d'execution arrete les threads avec un delai de maximum 30 second
	 * Elle enregistre enfin les données dans la base de données
	 * Cette methode mets à jour la variable "isStoping" pendant son execution
	 * @throws IOException
	 */
	public synchronized void finEcoute() throws IOException {
		this.isStoping =true;
		this.listening = false;

		try {
			executorService.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ProxyHTTP.PROXY_NUMBERS=0;

		this.saveData();
		this.isStoping= false;
	}

	/**
	 * @see DataBase::getCurrentFilePath
	 */
	public String getPath(){
		return this.bdd.getCurrentFilePath();
	}

	/**
	 * @see DataBase::supprimerCapture
	 */
	public void supprimerCapture(String captureN){
		this.bdd.supprimerCapture(captureN);
	}

	/**
	 * @see DataBase::modifierCaptureName
	 */
	public void modifierCaptureName(String oldCN, String newCN){
		this.bdd.modifierCaptureName(oldCN, newCN);
	}

	/**
	 * Permet de savoir si l'annalysuer s'arrete
	 * @return true si l'analyseur est dans la méthode finEcoute, False sinon
	 */
	public boolean isStoping(){
		return this.isStoping;
	}

	/**
	 * Permet de sauvegarder les données dans la base de données
	 */
	private void saveData(){
		this.bdd.saveData();
	}

	/**
	 * Permet de connaître l'état du programme
	 * @return true si la capture est lancée, false sinon
	 */
	public boolean estLance(){
		return this.listening;
	}
}
