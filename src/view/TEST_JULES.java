package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class TEST_JULES {
    public static void main(String[] args) throws IOException {
        /*Process p = Runtime.getRuntime().exec("echo $HTTP_PROXY");

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
        System.out.println("6 "+ress);*/

        Map<String, String> maMap = System.getenv();
        System.out.println(maMap.toString());
        System.out.println(maMap.containsKey("HTTP_PROXY"));
        System.out.println(maMap.containsKey("$HTTP_PROXY"));
    }
}