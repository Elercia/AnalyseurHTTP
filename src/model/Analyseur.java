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

public class Analyseur implements Runnable {
	//remplacé par ca
	private ExecutorService executorService;

	private DataBase bdd;

	private ServerSocket serverSocket;
	private SSLServerSocket serverSSLSocket;

	private boolean listening;
	private boolean isStoping;

	private boolean usingProxy;
	private String proxyAdress;
	private int proxyPort;


	public Analyseur(){
		this.bdd = null;

		this.executorService = Executors.newCachedThreadPool();

		this.isStoping = false;

		this.serverSocket = null;
		this.serverSSLSocket = null;

		this.usingProxy = false;
		this.proxyAdress ="";
		this.proxyPort=0;

		try {
			this.bdd = new DataBase("");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public void setPort(int port)throws IOException{
		if(serverSocket == null)
			this.serverSocket = new ServerSocket(port);

		if(serverSSLSocket == null) {
			SSLServerSocketFactory sf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			int portSSL = port + 1;
			this.serverSSLSocket = (SSLServerSocket) sf.createServerSocket(portSSL);
			this.serverSSLSocket.setEnableSessionCreation(true);
		}
		System.out.println("Proxy HTTP lancé sur le port "+port);
	}

	public void setFile(String path){
		try {
			this.bdd = new DataBase(path);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		this.bdd.saveData();
	}

	public void setProxy(String adress, int port){
		this.usingProxy = true;
		this.proxyAdress = adress;
		this.proxyPort = port;
	}

	public HashMap<String, HashMap<String, Object>> getData(String captureName){
		return this.bdd.actuValues(captureName);
	}

	public ArrayList<String> getCapturesNames(){
		return this.bdd.getCapturesNames();
	}

	@Override
	/**
	 * @see Analyseur::debutEcoute
	 * lance le thread de l'analyseur
 	 * Cette methode lance la méthode débutecoute
	 */
	public void run() {
		try {
			if(this.bdd == null)
				this.setFile(null);
			if(this.serverSocket == null || this.serverSSLSocket == null)
				this.setPort(9999);

			this.debutEcoute();
		}catch(IOException e){
			System.err.println("Erreur dans le thread analyseur ("+e.getMessage()+")");
		}
	}

	/**
	 * Method permettant de démarrer des proxysHTTP en fonction des demandes du navigateur
	 * @throws IOException
	 */
	private void debutEcoute() throws IOException{
		this.listening = true;
		ProxyHTTP proxyHTTP = null;
		while(this.listening) {
			proxyHTTP = new ProxyHTTP(this.serverSocket.accept(), ProxyHTTP.PROXY_NUMBERS++, bdd);
			proxyHTTP.setProxy(this.proxyAdress, this.proxyPort);
			executorService.execute(proxyHTTP);
		}//fin while
	}

	/**
	 * Methode permettant de mettre fin a l'écoute en cours
	 * Normalement elle ferme le socket utilisé pour ecouté le navigateur
	 * Elle regroupe aussi tous les threads dans un thread
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

	public void modifierCaptureName(String oldCN, String newCN){
		this.bdd.modifierCaptureName(oldCN, newCN);
	}

	public boolean isStoping(){
		return this.isStoping;
	}

	private void saveData(){
		this.bdd.saveData();
	}

	public boolean estLance(){
		return this.listening;
	}
}
