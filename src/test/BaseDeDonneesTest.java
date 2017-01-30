package test;

import junit.framework.TestCase;
import static org.junit.Assert.*;
import model.BaseDeDonnees;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by E155399M on 25/01/17.
 */
public class BaseDeDonneesTest{
	private BaseDeDonnees bdd;
	private ArrayList<String> headersRequete;
	private ArrayList<String> headersResponse;
	private int numbersOfRequests1;
	private int numbersOfRequests2;

	@Before
	public void setUp() throws Exception {
		bdd = new BaseDeDonnees(new File("data/test.json"));
		headersRequete = new ArrayList<>();
		headersResponse = new ArrayList<>();
		numbersOfRequests1 = 7;
		numbersOfRequests2 = 8;

		for (int i = 0 ; i < numbersOfRequests1; i++) {
			headersRequete.add("GET / HTTP/1.1\n" +
					"Host: example.com\n" +
					"User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:48.0) Gecko/20100101 Firefox/48.0\n" +
					"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
					"Accept-Language: en-US,en;q=0.5\n" +
					"Accept-Encoding: gzip, deflate\n" +
					"Connection: keep-alive\n" +
					"Upgrade-Insecure-Requests: 1\n" +
					"Pragma: no-cache\n" +
					"Cache-Control: no-cache");
			headersResponse.add("HTTP/1.0 200 OK\n" +
					"Content-Encoding: gzip\n" +
					"Cache-Control: max-age=604800\n" +
					"Content-Type: text/html\n" +
					"Date: Wed, 25 Jan 2017 15:09:14 GMT\n" +
					"Etag: \"359670651+gzip\"\n" +
					"Expires: Wed, 01 Feb 2017 15:09:14 GMT\n" +
					"Last-Modified: Fri, 09 Aug 2013 23:54:35 GMT\n" +
					"Server: ECS (iad/182A)\n" +
					"Vary: Accept-Encoding\n" +
					"X-Cache: HIT, MISS from squidetu1-cha.cpub.univ-nantes.fr, MISS from proxyetu2.iut-nantes.univ-nantes.prive\n" +
					"x-ec-custom-error: 1\n" +
					"Content-Length: 606\n" +
					"X-Cache-Lookup: HIT from squidetu1-cha.cpub.univ-nantes.fr:3128, MISS from proxyetu2.iut-nantes.univ-nantes.prive:3128\n" +
					"Via: 1.1 squidetu1-cha.cpub.univ-nantes.fr (squid), 1.0 proxyetu2.iut-nantes.univ-nantes.prive (squid)\n" +
					"Connection: keep-alive");
		}//end for

		for (int i = 0 ; i < numbersOfRequests2; i++) {
			headersRequete.add("GET / HTTP/1.1\n" +
					"Host: perdu.com\n" +
					"User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:48.0) Gecko/20100101 Firefox/48.0\n" +
					"Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\n" +
					"Accept-Language: en-US,en;q=0.5\n" +
					"Accept-Encoding: gzip, deflate\n" +
					"Connection: keep-alive\n" +
					"Upgrade-Insecure-Requests: 1\n" +
					"Pragma: no-cache\n" +
					"Cache-Control: no-cache");
			headersResponse.add("HTTP/1.0 200 OK\n" +
					"Date: Wed, 25 Jan 2017 15:43:34 GMT\n" +
					"Server: Apache\n" +
					"Last-Modified: Thu, 02 Jun 2016 06:01:08 GMT\n" +
					"Etag: \"cc-5344555136fe9\"\n" +
					"Accept-Ranges: bytes\n" +
					"Vary: Accept-Encoding\n" +
					"Content-Encoding: gzip\n" +
					"Content-Length: 163\n" +
					"Content-Type: text/html\n" +
					"X-Cache: MISS from squidetu1-cha.cpub.univ-nantes.fr, MISS from proxyetu2.iut-nantes.univ-nantes.prive\n" +
					"X-Cache-Lookup: HIT from squidetu1-cha.cpub.univ-nantes.fr:3128, MISS from proxyetu2.iut-nantes.univ-nantes.prive:3128\n" +
					"Via: 1.1 squidetu1-cha.cpub.univ-nantes.fr (squid), 1.0 proxyetu2.iut-nantes.univ-nantes.prive (squid)\n" +
					"Connection: keep-alive");
		}


		//on ajoute numbersOfRequests fois le meme header pour verifier
		for (int i = 0;i<numbersOfRequests1;  i++)
			bdd.enregistrement(headersRequete.get(i), headersResponse.get(i));
		for (int i = 0;i<numbersOfRequests2;  i++)
			bdd.enregistrement(headersRequete.get(i), headersResponse.get(i));
	}

	@Test
	public void saveData() throws Exception {
		bdd.saveData();

	}



	@Test
	public void actuValues() throws Exception {

	}


}