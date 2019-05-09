package server;

import domain.Notary;
import ws.Implementacao.NotaryWebServiceImpl;

import javax.xml.ws.Endpoint;
import java.util.Scanner;


public class NotaryServer {

    /**
     * Starts a simple server to deploy the web service.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the server port : ");
        int port = scanner.nextInt();

        String bindingURI = "http://localhost:" + port + "/notaryService";
        NotaryWebServiceImpl webService = new NotaryWebServiceImpl();
        Endpoint.publish(bindingURI, webService);
        System.out.println("Server started at: " + bindingURI);
        Notary notary = Notary.getInstance();
        System.out.println("Do you want to use CC? yes or no");
        String value = scanner.next();
        if (value.equalsIgnoreCase("yes")){
            notary.startCC();
        }else if(value.equalsIgnoreCase("no")){
            notary.withCC(false);
        }

    }

}