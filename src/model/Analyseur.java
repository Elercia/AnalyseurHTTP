package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;

public class Analyseur {

	private ArrayList<ProxyHTTP> proxys;
	private BaseDeDonnees bdd;
	private ServerSocket serverSocket;
	private boolean listening;

	public Analyseur(int port, String fileName) throws IOException{
		this.bdd = new BaseDeDonnees(fileName);
		this.proxys = new ArrayList<>();
		serverSocket = new ServerSocket(port);
	}

	/**
	 * Method permettant de démarrer des proxys en fonction des demandes du navigateur
	 * !! Cette method ne retourne pas pour l'insant
	 * @throws IOException
	 */
	public void debutEcoute() throws IOException{
		this.listening = true;
		ProxyHTTP proxy = null;
		while (this.listening) {
			proxy = new ProxyHTTP(serverSocket.accept(), ProxyHTTP.PROXY_NUMBERS++);
			proxy.setPriority(Thread.MAX_PRIORITY);
			this.proxys.add(proxy);
			proxy.start();
		}
	}

	/**
	 * Methode permettant de mettre fin a l'écoute en cours
	 * Normalement elle ferme le socket utilisé pour ecouté le navigateur
	 * Elle regroupe aussi tous les threads dans un thread
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void finEcoute() throws IOException, InterruptedException {
		this.serverSocket.close();
		Iterator<ProxyHTTP> it = this.proxys.iterator();
		ProxyHTTP p;
		while(it.hasNext())
		{
			p = it.next();
			p.join();
		}

	}
}
