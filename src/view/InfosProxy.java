package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import com.btr.proxy.search.ProxySearch;

public class InfosProxy {
        private String hote;
        private int port;

        public InfosProxy() {
            try {
                System.setProperty("java.net.useSystemProxies", "true");
                ProxySearch ps = ProxySearch.getDefaultProxySearch();
                ps.setPacCacheSettings(32, 1000 * 60 * 5);
                List<Proxy> l = ps.getProxySelector().select(new URI("http://www.google.com/")); // Pour pouvoir effectuer une requête d'essai

                for (Iterator<Proxy> iter = l.iterator(); iter.hasNext(); ) {
                    Proxy proxy = (Proxy) iter.next();
                    InetSocketAddress addr = (InetSocketAddress) proxy.address();

                    if (addr == null) {
                        this.hote = "null";
                    } else {
                        this.hote = proxy.type().toString().toLowerCase() + "://" + addr.getHostName();
                        this.port = addr.getPort();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // https://code.google.com/archive/p/proxy-vole/downloads
        public String getHote() {
            return this.hote;
        }

        public int getPort() {
            return this.port;
        }

        public static void main(String args[]) {
            InfosProxy i = new InfosProxy();
            System.out.println(i.getHote() + ":" + i.getPort());
        }

}


    /*public static void main(String[] args) throws IOException {
        Process p = Runtime.getRuntime().exec("echo $HTTP_PROXY");

        // read the standard output of the command
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p
                .getInputStream()));

        System.out.println("Here is the standard output of the command:\n");

        int count = 0;
        String s;
        String result = "";
        while (!procDone(p)) {
            while ((s = stdInput.readLine()) != null) {
                count++;
                result = result + s + "\n";
            }
        }

        System.out.println("result:" + count + ": " + result);
        stdInput.close();
    }

    private static boolean procDone(Process p) {
        try {
            int v = p.exitValue();
            return true;
        } catch (IllegalThreadStateException e) {
            return false;
        }*/

        /*System.out.println("1 "+System.getenv("HTTP_PROXY"));
        System.out.println("2 "+System.getProperty("HTTP_PROXY"));
        System.out.println("3 "+System.getenv("$HTTP_PROXY"));
        System.out.println("4 "+System.getProperty("$HTTP_PROXY"));

        Runtime runtime = Runtime.getRuntime();
        BufferedReader reader = new BufferedReader(new InputStreamReader(runtime.exec("echo $HTTP_PROXY").getInputStream()));
        String res = reader.readLine();
        System.out.println("5 "+res);

        Runtime runtim = Runtime.getRuntime();
        BufferedReader reade = new BufferedReader(new InputStreamReader(runtim.exec("echo HTTP_PROXY").getInputStream()));
        String ress = reade.readLine();
        System.out.println("6 "+ress);

        Map<String, String> maMap = System.getenv();
        System.out.println(maMap.toString());
        System.out.println(maMap.containsKey("HTTP_PROXY"));
        System.out.println(maMap.containsKey("$HTTP_PROXY"));
    }
}*/