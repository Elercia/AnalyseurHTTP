package model;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;

public class Analyseur extends Thread {

	private ArrayList<ProxyHTTP> proxysHTTP;
	private ArrayList<ProxyHTTPS> proxysHTTPS;
	private BaseDeDonnees bdd;

	private ServerSocket serverSocket;
	private SSLServerSocket serverSSLSocket;

	private boolean listening;

	private boolean usingProxy;
	private String proxyAdress;
	private int proxyPort;


	public Analyseur(){
		this.bdd = null;
		this.proxysHTTP = new ArrayList<>();
		this.proxysHTTPS = new ArrayList<>();


		this.serverSocket = null;
		this.serverSSLSocket = null;


		this.usingProxy = false;
		this.proxyAdress ="";
		this.proxyPort=0;
	}

	public void setPort(int port)throws IOException{
		this.serverSocket = new ServerSocket(port);

		SSLServerSocketFactory sf = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
		this.serverSSLSocket = (SSLServerSocket)sf.createServerSocket(port+1);
		this.serverSSLSocket.setEnableSessionCreation(true);

		System.out.println("Proxy HTTP lancé sur le port "+port+" et proxy HTTPS lancé sur le port " + (port+1));
	}

	public void setFile(File f){
		if(f == null) {
			this.bdd = new BaseDeDonnees((File)null);
		}
		else
			this.bdd= new BaseDeDonnees(f);
	}

	public void setProxy(String adress, int port){
		this.usingProxy = true;
		this.proxyAdress = adress;
		this.proxyPort = port;
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
			if(this.serverSocket == null)
				this.setPort(9999);

			this.debutEcoute();
		}catch(IOException e){
			System.err.println("Erreur dans le thread analyseur");
		}
	}

	/**
	 * Method permettant de démarrer des proxysHTTP en fonction des demandes du navigateur
	 * @throws IOException
	 */
	private void debutEcoute() throws IOException{
		this.listening = true;
		ProxyHTTP proxyHTTP = null;
		ProxyHTTPS proxyHTTPS = null;
		while(this.listening) {
			proxyHTTP = new ProxyHTTP(this.serverSocket.accept(), ProxyHTTP.PROXY_NUMBERS++, bdd);
			proxyHTTPS = new ProxyHTTPS((SSLSocket)this.serverSSLSocket.accept(), ProxyHTTPS.PROXY_NUMBERS++, bdd);

			if(usingProxy){
				proxyHTTP.setProxy(this.proxyAdress, this.proxyPort);
				proxyHTTPS.setProxy(this.proxyAdress, this.proxyPort);
			}

			proxyHTTP.setPriority(Thread.MAX_PRIORITY);
			proxyHTTPS.setPriority(Thread.MAX_PRIORITY);

			this.proxysHTTP.add(proxyHTTP);
			this.proxysHTTPS.add(proxyHTTPS);

			proxyHTTP.start();
			//proxyHTTPS.start();
		}
	}

	/**
	 * Methode permettant de mettre fin a l'écoute en cours
	 * Normalement elle ferme le socket utilisé pour ecouté le navigateur
	 * Elle regroupe aussi tous les threads dans un thread
	 * @throws IOException
	 */
	public void finEcoute() throws IOException {
		this.listening = false;

		Iterator<ProxyHTTP> it1 = this.proxysHTTP.iterator();

		ProxyHTTP p;
		while(it1.hasNext())
		{
			p = it1.next();
			try {
				p.join();
			}catch (InterruptedException e){
				//rien de plus
			}
		}

		Iterator<ProxyHTTPS> it2 = this.proxysHTTPS.iterator();
		ProxyHTTPS pp;
		while(it2.hasNext())
		{
			pp = it2.next();
			try {
				pp.join();
			}catch (InterruptedException e){
				//rien de plus
			}
		}

		this.serverSocket.close();
		this.serverSSLSocket.close();
	}

	public boolean estLance(){
		return this.listening;
	}
}
