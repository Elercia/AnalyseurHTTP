package model;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

/**
 * Created by E155399M on 21/11/16.
 */
public class ProxyHTTP implements Runnable {
    private Socket clientSocket = null;
    private int id;
    private BaseDeDonnees bdd;


    private static final int BUFFER_SIZE = 10000;
    public static int PROXY_NUMBERS = 0;

    private boolean isUsingProxy;
    private String proxyAdresse;
    private int proxyPort;

    public ProxyHTTP(Socket socket, int id, BaseDeDonnees bdd) {
        this.clientSocket = socket;
        this.id = id;
        this.bdd = bdd;

        System.out.println("------DEBUT thread id = "+id+"-------");
    }

    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            // Read request
            InputStream incommingIS = clientSocket.getInputStream();
            byte[] b = new byte[BUFFER_SIZE];
            byte[] b2 = new byte[BUFFER_SIZE];
            //on recupère la data du navigateur
            int len = incommingIS.read(b);

            if (len > 0) {//si ya de la data

                //représente le header de demande
                String h1 = new String(b, 0, len);
                String host = "";
                host = this.getHost(h1);
                int port = 80;
                if(host != null && !host.isEmpty()){
                    if(host.contains(":")){
                        try {
                            String tmp = host;
                            host = tmp.split(":", tmp.lastIndexOf(":")-1)[0];
                            port = Integer.parseInt(tmp.split(":", tmp.lastIndexOf(":")-1)[1]);

                        }catch(IndexOutOfBoundsException e) {
                            throw new Exception("Host impossible a determiner");

                        }catch(NumberFormatException e){
                            port = 80;
                        }catch(Exception e){
                            System.err.println("host introuvable");
                        }
                    }else{
                        port = 80;
                    }
                }else
                    throw new Exception("Host impossible a determiner");

                Socket socket = null;
                //HTTP
                SocketFactory sf = SocketFactory.getDefault();
                InetAddress inet = InetAddress.getByName(host);
                socket = sf.createSocket(inet, port);
                socket.setSoTimeout(5000);
                //socket.setSoTimeout(5000);
                OutputStream outgoingOS = socket.getOutputStream();

                outgoingOS.write(b, 0, len);

                OutputStream incommingOS = clientSocket.getOutputStream();
                InputStream outgoingIS = socket.getInputStream();

                String h2="";
                //On lit la réponse du serveur et on l'ecrit au client
                for (int length; (length = outgoingIS.read(b2)) != -1; ) {
                    incommingOS.write(b2, 0, length);
                    h2 = new String(b2, 0, length);
                }
                h2 = h2.split("\r\n\r\n")[0];
                long stopTime = System.currentTimeMillis();
                long timeUsed = stopTime-startTime;
                h1+="\ntimeUsed: "+ timeUsed+"\n";

                this.bdd.enregistrement(h1, h2);

                //faire enregistrement ici plz
                incommingOS.close();
                outgoingIS.close();
                outgoingOS.close();
                incommingIS.close();
                socket.close();
                clientSocket.close();
            } else {
                incommingIS.close();
            }
        } catch(TimeoutException | SocketTimeoutException toe ){

        } catch (IOException e) {
            e.printStackTrace();
        } catch(Exception e) {
            System.err.println("Erreur inconnue : "+e.getMessage());
            e.printStackTrace();
        }finally{
            System.out.println("------FIN thread id = "+id+"---------");
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
