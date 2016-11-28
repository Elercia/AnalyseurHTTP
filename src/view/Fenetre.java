package view;

import model.BaseDeDonnees;
import model.ProxyHTTP;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class Fenetre {
    public static void main(String[] args) throws IOException {
        /*ServerSocket serverSocket = null;
        boolean listening = true;

        int port = 9999;	//default
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            //ignore me
        }

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Proxy démarré sur le port: " + port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + args[0]);
            return;
        }

        ProxyHTTP proxy;
        HashMap<Integer, ProxyHTTP> list = new HashMap<>();


        while (listening) {
            proxy = new ProxyHTTP(serverSocket.accept(), ProxyHTTP.PROXY_NUMBERS);
            proxy.setPriority(Thread.MAX_PRIORITY);
            list.put(ProxyHTTP.PROXY_NUMBERS++, proxy);
            proxy.start();
        }
        serverSocket.close();*/
	    BaseDeDonnees bd = new BaseDeDonnees(null);
	    bd.enregistrement("lol", "mdr1");
	    bd.enregistrement("lol", "mdr2");
	    bd.enregistrement("lol", "mdr3");

    }

}
