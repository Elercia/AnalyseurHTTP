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
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Analyseur implements Runnable {


	//aput changer bientot
	private ArrayList<ProxyHTTP> proxysHTTP;
	private ArrayList<ProxyHTTPS> proxysHTTPS;
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

		//à changer
		this.proxysHTTP = new ArrayList<>();
		this.proxysHTTPS = new ArrayList<>();

		this.executorService = Executors.newCachedThreadPool();

		this.isStoping = false;

		this.serverSocket = null;
		this.serverSSLSocket = null;

		this.usingProxy = false;
		this.proxyAdress ="";
		this.proxyPort=0;
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
		System.out.println("Proxy HTTP lancé sur le port "+port+" " +
				"et proxy HTTPS lancé sur le port " + (port+1));
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

	public HashMap<String, HashMap<String, Object>> getData(){
		return this.bdd.actuValues();
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
//		ProxyHTTPS proxyHTTPS = null;
		while(this.listening) {
//			//TODO : Probleme ici
//			//Si on ne lance pas une page HTTP on ne pourra pas lire de page HTTPS
//			//this.serverSocket.accept() bloque l'éxecution du programme
//			//on ne peut donc pas charger de page HTTPS
//			//pareille pour le HTTP normal si on ne demande pas de page HTTPS alors on ne pourra pas charger de page
//			//car le start est plus bas ... ennuyant
//
//			proxyHTTP = new ProxyHTTP(this.serverSocket.accept(), ProxyHTTP.PROXY_NUMBERS++, bdd);
////			proxyHTTPS = new ProxyHTTPS((SSLSocket)this.serverSSLSocket.accept(), ProxyHTTPS.PROXY_NUMBERS++, bdd);
//
//			if (usingProxy) {
//				proxyHTTP.setProxy(this.proxyAdress, this.proxyPort);
////				proxyHTTPS.setProxy(this.proxyAdress, this.proxyPort);
//			}
//
//			proxyHTTP.setPriority(Thread.MAX_PRIORITY);
////			proxyHTTPS.setPriority(Thread.MAX_PRIORITY);
//
//			this.proxysHTTP.add(proxyHTTP);
////			this.proxysHTTPS.add(proxyHTTPS);
//
//			proxyHTTP.start();
////			proxyHTTPS.start();
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

		/*Iterator<ProxyHTTP> it1 = this.proxysHTTP.iterator();

		ProxyHTTP p;
		while (it1.hasNext()) {
			try {
				p = it1.next();

				if (p != null)
					p.join();
			}catch (ConcurrentModificationException e){
				System.err.println("Erreur concurrente");
			} catch (InterruptedException e) {
				//rien de plus
			}
		}

		Iterator<ProxyHTTPS> it2 = this.proxysHTTPS.iterator();
		ProxyHTTPS pp;
		while (it2.hasNext()) {
			pp = it2.next();
			try {
				pp.join();
			} catch (InterruptedException e) {
				//rien de plus
			}
		}*/
		try {
			executorService.awaitTermination(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ProxyHTTP.PROXY_NUMBERS=0;
		ProxyHTTPS.PROXY_NUMBERS=0;

//		if(!serverSocket.isClosed())
//			this.serverSocket.close();
//		if(!serverSSLSocket.isClosed())
//			this.serverSSLSocket.close();

		this.saveData();
		this.isStoping= false;
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
