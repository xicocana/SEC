package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ws.importWS.serverWS.NotaryWebService;
import ws.importWS.serverWS.NotaryWebServiceImplService;



public class RoundRobin {

    private static RoundRobin INSTANCE = null;
    private String currentDir = System.getProperty("user.dir");
    BufferedReader reader;
    private List<NotaryWebService> servers = new ArrayList<>();
    private int pos = 0;

    public static synchronized RoundRobin getInstance(){
        if(INSTANCE == null){
            INSTANCE = new RoundRobin();
        }
        return INSTANCE;
    }

    private RoundRobin(){

        String path = currentDir + "/../src/java/resources/replicas.txt";
        try {
            reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null) {
                // line
                URL url = null;
                try {
                    url = new URL(line);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NotaryWebServiceImplService client = new NotaryWebServiceImplService(url);
                NotaryWebService notaryWebservice = client.getNotaryWebServiceImplPort();
                servers.add(notaryWebservice);
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Caught exception while reading users file:");
            e.printStackTrace();
        }
    }

    public NotaryWebService getServer() {
        return servers.get( pos++ % servers.size());
    }

    public int getActiveServers(){
        return servers.size();
    }

    public List<String> getStateOfGoodCommunication(List<String> argsToSend) {
        return this.getServer().getStateOfGood(argsToSend);
    }
}