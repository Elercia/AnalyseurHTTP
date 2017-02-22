package model;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * Created by E155399M on 21/11/16.
 */
public class ProxyHTTP implements Runnable {
    private Socket clientSocket = null;
    private int id;
    private DataBase bdd;


    private static final int BUFFER_SIZE = 10000;
    public static int PROXY_NUMBERS = 0;

    private boolean isUsingProxy;
    private String proxyAdresse;
    private int proxyPort;

    public ProxyHTTP(Socket socket, int id, DataBase bdd) {
        this.clientSocket = socket;
        this.id = id;
        this.bdd = bdd;

        try {
            this.clientSocket.setSoTimeout(10000);
        } catch (SocketException e) {
            e.printStackTrace();
        }

        System.out.println("------DEBUT thread id = "+id+"-------");
    }

    public void run() {    	
    	try
		{
		    final byte[] request = new byte[8096];
		    final byte[] answer = new byte[8096];
		    final  InputStream fluxEntrantClient = this.clientSocket.getInputStream();
		    final OutputStream fluxSortantClient = this.clientSocket.getOutputStream();

		    final Socket socketServer = new Socket("proxyetu.iut-nantes.univ-nantes.prive", 3128);

		    final InputStream fluxEntrantServeur = socketServer.getInputStream();
		    final OutputStream fluxSortantServeur = socketServer.getOutputStream();
		    		    
		    Thread t = new Thread()
		    {
		    	public void run()
		    	{
		    		try
		    		{
		    			int length;
		    			System.out.println("--------\nRequete :");
		    			while ((length = fluxEntrantClient.read(request)) != -1)
			    		{
		    				System.out.println(new String(request, 0, length));
			    			fluxSortantServeur.write(request, 0, length);
			    			fluxSortantServeur.flush();
			            }
		    		}
		    		catch(IOException e){
		    			
		    		}
		    	}
		    };
		    t.start();
		    
		    int length;
		    while ((length = fluxEntrantServeur.read(answer)) != -1)
    		{
    			System.out.println("--------\nReponse :");
				System.out.println(new String(answer, 0, length));
    			fluxSortantClient.write(answer, 0, length);
    			fluxSortantClient.flush();
            }
		    socketServer.close();
		    this.clientSocket.close();
		    System.err.println("Requete traitee");
		}
		catch(IOException e)
		{
			System.err.println("Exception !");
		}
    }

    public void setProxy(String a, int p){
        this.isUsingProxy = true;
        a = a.trim();
        //on retire le http://
        if(a.contains("http://"))
            a = a.substring(7);
        if(a.contains("https://"))
            a = a.substring(8);

        this.proxyAdresse = a;
        this.proxyPort = p;
    }

    private String getHost(String header) {
        if(isUsingProxy){
            String s = this.proxyAdresse+":"+this.proxyPort;
            //System.out.println("L'host Utilisé est un proxy ("+s+")");
            return s;
        }else {
            header = header.toLowerCase();
            if (header.contains("host: ")) {
                //on recupère l'host
                //c'est la sous chaine a partir de l'index de "Host: " jusqu'aux premier "\n" (en partant du meme index)
                String host = (header.substring(header.indexOf("host: ") + ("host: ").length(), header.indexOf("\n", header.indexOf("host: ")))).trim();
                System.out.println("host reconnu : " + host);
                return host;
            }

            //on ne peut pas determiner l'host
            return "";
        }
    }
}
