package model;

import java.net.*;
import java.io.*;
import java.util.*;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ProxyHTTP extends Thread {
    private Socket socket = null;
    private static final int BUFFER_SIZE = 32768;

    public ProxyHTTP(Socket socket) {
        super("ProxyHTTP");
        //Instanciation du SocketServeur avec le port passé en paramètre

        this.socket = socket;
    }

    public void run() {
        //get input from user
        //send request to server
        //get response from server
        //send response to user

        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine, reponseHeader = "", reponseHTML="";



            ///////////////////////////////////
            //Début de la récupération de la demande du client
            ///////////////////////////////////
            ArrayList<String> UserRequest = new ArrayList<>();
            String UserRequestString = "";
            while ((inputLine = in.readLine()) != null) {
                try {
                    UserRequestString += inputLine + "\n";
                    UserRequest.add(inputLine);
                } catch (Exception e) {
                    break;
                }
            }

            String urlToCall = "", tmp;
            int portToCall = 80;
            int indexHost = UserRequestString.indexOf("Host: ")+("Host: ").length();

            tmp = UserRequestString.substring(indexHost, UserRequestString.indexOf("\n", indexHost));
            urlToCall = tmp.split(":")[0];

            try {
                portToCall = Integer.parseInt(tmp.split(":")[1]);
            }catch(NumberFormatException nfe)
            {
                //nothing here
            }

            System.out.println("----------Demande utilisateur----------");
            System.out.println(UserRequestString);
            System.out.println("url:port "+urlToCall+":"+portToCall);
            System.out.println("---------------------------------------");
            ///////////////////////////////////
            //Fin de la récupération de la demande du client
            ///////////////////////////////////

            ///////////////////////////////////
            //Envoie de la requete au serveur + récupération de la réponse
            ///////////////////////////////////
            /**
             * IMPORTANT : ce n'est pas forcement comme cela qu'il faut procéder
             * cf exemple d'utilisation de la librairie HTTPComponent :
             * http://hc.apache.org/httpcomponents-client-4.5.x/examples.html
             * Plusieurs methodes sont disponible pour charger une page
             * a voir ....
             */
            CloseableHttpClient httpclient = HttpClients.createDefault();
            try{
                /*ajout de l'url à appeller, Port sur lequel demander , String reprsentant la demande ex :HTTP HTTPS*/

                HttpHost target = new HttpHost(urlToCall, portToCall);

                //TODO ajouter configuration dynamique de proxy (avec ou sans proxy)
                HttpHost proxy = new HttpHost("proxyetu.iut-nantes.univ-nantes.prive", 3128, "http");

                RequestConfig config = RequestConfig.custom()
                        .setProxy(proxy)
                        .build();

                //TODO la page exacte à charger
                HttpGet request = new HttpGet("/");
                request.setConfig(config);

                System.out.println("Execution de la requete " + request.getRequestLine() + " vers " + target + " via " + proxy);

                CloseableHttpResponse response = httpclient.execute(target, request);
                reponseHeader = response.getStatusLine().toString();
                reponseHTML = EntityUtils.toString(response.getEntity());

                try {
                    System.out.println();
                    System.out.println("----------Requete récupérée----------");


                    System.out.println(response.getStatusLine());
                    System.out.println(EntityUtils.toString(response.getEntity()));

                    System.out.println("-------------------------------------");
                } finally {
                    response.close();
                }
            } finally {
                httpclient.close();
            }
            ///////////////////////////////////
            //Fin Envoie de la requete au serveur + récupération de la réponse
            ///////////////////////////////////


            BufferedReader rd = null;
            try {
                ///////////////////////////////////
                //Envoie de la réponse au client
                ///////////////////////////////////

                // byte by[] = new byte[ BUFFER_SIZE ];
                // int index = is.read( by, 0, BUFFER_SIZE );
                // while ( index != -1 )
                // {
                //   out.write( by, 0, index );
                //   index = is.read( by, 0, BUFFER_SIZE );
                // }
                // //out.writeBytes("<h1>TEST</h1>");
                //out.flush();
                out.writeBytes(reponseHeader);
                out.writeBytes(reponseHTML);
                out.flush();


                ///////////////////////////////////
                //Fin de l'envoie au client
                ///////////////////////////////////
            } catch (Exception e) {
                //can redirect this to error log
                System.err.println("Encountered exception: " + e);
                //e.printStackTrace();
                //encountered error - just send nothing back, so
                //processing can continue
                out.writeBytes("");
            }

            //close out all resources
            if (rd != null) {
                rd.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
