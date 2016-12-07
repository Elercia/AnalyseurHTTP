package model;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;

public class Analyseur extends Thread {

	private ArrayList<ProxyHTTP> proxys;
	private BaseDeDonnees bdd;
	private ServerSocket serverSocket;
	private boolean listening;

	public Analyseur(){
		this.bdd = null;
		this.proxys = new ArrayList<>();
		this.serverSocket = null;
	}

	public void setPort(int port)throws IOException{
		this.serverSocket = new ServerSocket(port);
	}

	//TODO a changer
	//ne récupérer que le file et pas le nom du fichier (récupéré partout)
	public void setFile(File f){
		if(f == null) {
			this.bdd = new BaseDeDonnees((File)null);
		}
		else
			this.bdd= new BaseDeDonnees(f);
	}

	@Override
	public void run() {
		try {
			if(this.bdd == null)
				this.setFile(null);
			if(this.serverSocket == null)
				this.setPort(9999);

			this.debutEcoute();
		}catch(IOException e){
			//rien pour l'instant
		}
	}

	/**
	 * Method permettant de démarrer des proxys en fonction des demandes du navigateur
	 * @throws IOException
	 */
	public void debutEcoute() throws IOException{
		this.listening = true;
		ProxyHTTP proxy = null;
		while(this.listening) {
			proxy = new ProxyHTTP(serverSocket.accept(), ProxyHTTP.PROXY_NUMBERS++, bdd);
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
	public void finEcoute() throws IOException {
		this.listening = false;
		this.serverSocket.close();
		Iterator<ProxyHTTP> it = this.proxys.iterator();
		ProxyHTTP p;
		while(it.hasNext())
		{
			p = it.next();
			try {
				p.join();
			}catch (InterruptedException e){
				//rien de plus
			}
		}
	}

	public boolean estLancer(){
		return this.listening;
	}
}
