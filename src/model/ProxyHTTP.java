package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by E155399M on 21/11/16.
 */
public class ProxyHTTP extends Thread {
    private Socket clientSocket = null;
    private int id;
    private BaseDeDonnees bdd;


    private static final int BUFFER_SIZE = 327680;
    public static int PROXY_NUMBERS = 0;

    public ProxyHTTP(Socket socket, int id, BaseDeDonnees bdd) {
        this.clientSocket = socket;
        this.id = id;
        this.bdd = bdd;
        System.out.println("------DEBUT thread id = "+id+"-------");
    }

    public void run() {
        try {

            // Read request
            InputStream incommingIS = clientSocket.getInputStream();
            byte[] b = new byte[BUFFER_SIZE];
            byte[] b2 = new byte[BUFFER_SIZE];
            //on recupère la data du navigateur
            int len = incommingIS.read(b);

            if (len > 0) {//si ya de la data
                //TODO
                //recupérer host et changer le socket
                //si l'utilisateur ustilise un proxy l'utiliser
                //sinon on cherche a connaitre le host avec lequel dialoguer et donc l'utiliser
                //echo $HTTP_PROXY
                //http://freeproxylists.net/fr/

                byte[] ipAddr = new byte[]{91,121,42,68};
                InetAddress addr = InetAddress.getByAddress(ipAddr);

                Socket socket = new Socket(addr, 80);


                //http://www.mon-ip.com/
                //Socket socket = new Socket("2pl44-1-78-219-187-126.fbx.proxad.net", 20618);
                //Socket socket = new Socket("8proxy.space", 80);
                //Socket socket = new Socket("proxyetu.iut-nantes.univ-nantes.prive", 3128);
                OutputStream outgoingOS = socket.getOutputStream();

                //on écrit la demande au serveur
                outgoingOS.write(b, 0, len);
                System.out.println("client -> serveur");
                String h1 = new String(b, 0, len);
                System.out.write(b, 0, len);
                System.out.println("serveur -> client");




                OutputStream incommingOS = clientSocket.getOutputStream();
                InputStream outgoingIS = socket.getInputStream();

                String h2="";
                //On lit la réponse du serveur et on l'ecrit au client
                for (int length; (length = outgoingIS.read(b2)) != -1; ) {
                    incommingOS.write(b2, 0, length);
                    System.out.write(b, 0, length);
                    h2 = new String(b, 0, len);
                }

                //faire enregistrement ici plz
                incommingOS.close();
                outgoingIS.close();
                outgoingOS.close();
                incommingIS.close();
                socket.close();

                String toSave = h1 +" "+ h2;
                this.bdd.enregistrement(toSave);

            } else {
                incommingIS.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("------FIN thread id = "+id+"---------");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
