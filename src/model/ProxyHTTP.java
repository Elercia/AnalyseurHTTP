package model;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ProxyHTTP implements Runnable {
    private Socket clientSocket = null;
    private int id;
    private DataBase bdd;


    private static final int BUFFER_SIZE = 8096;
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
    }

    public void run() {
        try
        {
            System.out.println("------DEBUT thread id = "+id+"-------");

            //initialisation des variables
            final byte[] request = new byte[BUFFER_SIZE];
            final byte[] answer = new byte[BUFFER_SIZE];
            final InputStream fluxEntrantClient = this.clientSocket.getInputStream();
            final OutputStream fluxSortantClient = this.clientSocket.getOutputStream();
            Socket socketServer;

            //on recupére le temps de début
            long startTime = System.currentTimeMillis();

            //headers[0] represente la requete
            //headers[1] represente la reponse
            final String[] headers = new String[2];

            //TODO à changer
            //On a besoin de le definir plus tard car on doit savoir quel serveur intérroger
            // dans le cas ou il n'y a pas de proxy configuré sur la machine
            if(this.isUsingProxy)
                socketServer = new Socket(this.proxyAdresse, this.proxyPort);
            else
                socketServer = null;

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
                            headers[0] = new String(request, 0, length);
                            System.out.println(headers[0]);
                            fluxSortantServeur.write(request, 0, length);
                            fluxSortantServeur.flush();
                        }
                    }catch(IOException e){

                    }
                }
            };
            t.start();

            int length;
            while ((length = fluxEntrantServeur.read(answer)) != -1)
            {
                System.out.println("--------\nReponse :");
                headers[1] = new String(answer, 0, length);
                System.out.println(headers[1]);
                fluxSortantClient.write(answer, 0, length);
                fluxSortantClient.flush();
            }
            socketServer.close();
            this.clientSocket.close();

            //On prend le temps à la fin du chargement
            //On calcule le temps qu'a mis la capture à ce réaliser
            long endTime = System.currentTimeMillis();
            long delta = endTime-startTime;

            //TODO idée : Change un peu la methode d'enregistrement en lui ajoutant la verification
            //que les headers sont valide
            //C'est a dire qu'ils ne sont pas issu d'une requete https (qu'ils ne sont pas encrypter et donc en claire)
            this.bdd.enregistrement(headers[0], headers[1], delta);
        }
        catch(IOException e)
        {
            System.err.println("Exception !");
            e.printStackTrace();
        }finally {
            System.out.println("------FIN thread id = "+id+"-------");
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
