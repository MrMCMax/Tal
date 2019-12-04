/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package talinterface;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

/**
 *
 * @author maximo
 */
public class StreamConnecter extends Connecter {
    
    StreamHandler sherr;
    
    @SuppressWarnings("CallToThreadStartDuringObjectConstruction")
    public StreamConnecter() throws IOException {
        super();
        try {
            String currentPath = System.getProperty("user.dir");
            String finalPath = currentPath.substring(0, currentPath.length() - 13);
            System.out.println(finalPath);
            finalPath = finalPath + "TalLogic/dist/TalLogic.jar";
            Runtime rt = Runtime.getRuntime();
            String[] command = new String[]{"java", "-jar", finalPath};
            Process server = rt.exec(command);
            //Program executed
            //Now we assign the streams
            Scanner s = new Scanner(server.getInputStream());
            PrintWriter pw = new PrintWriter(server.getOutputStream());
            sherr = new StreamHandler(server.getErrorStream());
            sherr.start();
            
            super.setPrintWriter(pw);
            super.setScanner(s);
            

        } catch (IOException e) {
            System.err.println("Program not found\n" + e.toString());
            throw e;
        }
    }

    private void launchServer() {

    }
    
    class StreamHandler extends Thread {

        InputStream is;

        public StreamHandler(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            InputStreamReader isr = null;
            BufferedReader br = null;
            try {
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String line = null;

                while ((line = br.readLine()) != null) {
                    System.err.println(line);
                }

            } catch (IOException e) {
                System.err.println(e);
            } finally {
                try {
                    if (isr != null) {
                        isr.close();
                    }
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                }
            }
        }
    }
}
