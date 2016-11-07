package model.proxy;

import java.net.*;
import java.io.*;

public class ProxyHTTP extends Thread {
    private Socket socketClient = null;
    private static final int BUFFER_SIZE = 32768;


    public ProxyHTTP(Socket socket) {
        super("ProxyHTTP");
        this.socketClient = socket;
    }

    public void run() {
        //get input from user
        //send request to server
        //get response from server
        //send response to user

        try {
            DataOutputStream outClient = new DataOutputStream(socketClient.getOutputStream());
            BufferedReader inClient = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

            String inputLine, outputLine;
            int cnt = 0;
            String urlToCall = "";

            ///////////////////////////////////
            //Début de la récupération de la demande du client
            ///////////////////////////////////
            String UserRequest = "";
            while ((inputLine = inClient.readLine()) != null) {
                try {
                    UserRequest += inputLine+"\n";
                } catch (Exception e) {
                    break;
                }
            }

            System.out.println("----------Demande utilisateur----------");
            System.out.println(UserRequest);
            System.out.println("---------------------------------------");
            ///////////////////////////////////
            //Fin de la récupération de la demande du client
            ///////////////////////////////////


            ///////////////////////////////////
            //Envoie de la requete au serveur + récupération de la réponse
            ///////////////////////////////////

            try {
                URL u = new URL("http://www.google.com");
                Proxy pr = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxyetu.iut-nantes.univ-nantes.prive", 3128));

                HttpURLConnection uc = (HttpURLConnection) u.openConnection(pr);
                uc.connect();
                System.out.println(uc);

                InputStream i = uc.getInputStream();
                InputStreamReader isr = new InputStreamReader(i);
                BufferedReader br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println(uc.getResponseMessage());
            }catch(IOException e){
                System.out.println("Probleme "+e.getMessage());
                e.printStackTrace();
            }

            ///////////////////////////////////
            //Fin Envoie de la requete au serveur + récupération de la réponse
            ///////////////////////////////////


            BufferedReader rd = null;
            try {
                ///////////////////////////////////
                //Envoie de la réponse au client
                ///////////////////////////////////




                ///////////////////////////////////
                //Fin de l'envoie au client
                ///////////////////////////////////
            } catch (Exception e) {
                //can redirect this to error log
                System.err.println("Encountered exception: " + e);
                //e.printStackTrace();
                //encountered error - just send nothing back, so
                //processing can continue
                outClient.writeBytes("");
            }

            //close out all resources
            if (rd != null) {
                rd.close();
            }
            if (outClient != null) {
                outClient.close();
            }
            if (inClient != null) {
                inClient.close();
            }
            if (socketClient != null) {
                socketClient.close();
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
