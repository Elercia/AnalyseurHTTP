package analyseur;

import java.net.*;
import java.io.*;
import proxy.*;

public class Analyseur {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;
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

        while (listening) {
            new ProxyHTTP(serverSocket.accept()).start();
        }
        serverSocket.close();
	}
}
