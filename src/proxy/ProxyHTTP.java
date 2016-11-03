package proxy;

import java.net.*;
import java.io.*;
import java.util.*;

public class ProxyHTTP extends Thread {
    private Socket socket = null;
    private static final int BUFFER_SIZE = 32768;


    public ProxyHTTP(Socket socket) {
        super("ProxyThread");
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

            String inputLine, outputLine;
            int cnt = 0;
            String urlToCall = "";

            ///////////////////////////////////
            //Début de la récupération de la demande du client
            ///////////////////////////////////

            while ((inputLine = in.readLine()) != null) {
                try {
                    System.out.println(inputLine);
                } catch (Exception e) {
                    break;
                }
            }
            ///////////////////////////////////
            //Fin de la récupération de la demande du client
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
