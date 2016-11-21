package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by E155399M on 21/11/16.
 */
public class ProxyHTTP extends Thread {
    private Socket clientSocket = null;
    private int id;
    private static final int BUFFER_SIZE = 32768;
    public static int PROXY_NUMBERS = 0;

    public ProxyHTTP(Socket socket, int id) {
        this.clientSocket = socket;
        this.id = id;
        System.out.println("Démarage Thread id = "+id);
    }

    public void run() {
        try {
            // Read request
            InputStream incommingIS = clientSocket.getInputStream();
            byte[] b = new byte[BUFFER_SIZE];
            byte[] b2 = new byte[BUFFER_SIZE];
            int len = incommingIS.read(b);//on recupère la data du navigateur

            if (len > 0) {//si ya de la data
                // Write request
                Socket socket = new Socket("proxyetu.iut-nantes.univ-nantes.prive", 3128);
                OutputStream outgoingOS = socket.getOutputStream();
                outgoingOS.write(b, 0, len);
                System.out.println("------DEMANDE-------");
                System.out.write(b, 0, len);
                System.out.println("------REPONSE-------");
                OutputStream incommingOS = clientSocket.getOutputStream();
                InputStream outgoingIS = socket.getInputStream();
                for (int length; (length = outgoingIS.read(b2)) != -1; ) {
                    incommingOS.write(b2, 0, length);
                    System.out.write(b, 0, length);
                }

                incommingOS.close();
                outgoingIS.close();
                outgoingOS.close();
                incommingIS.close();
                socket.close();

            } else {
                incommingIS.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Arret du thread id = " +this.id);
                System.out.println("------FIN-------");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
