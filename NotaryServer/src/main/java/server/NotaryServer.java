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
        String bindingURI = "http://localhost:9898/notaryService";
        NotaryWebServiceImpl webService = new NotaryWebServiceImpl();
        Endpoint.publish(bindingURI, webService);
        System.out.println("Server started at: " + bindingURI);

        System.out.print("Please insert Notary ID: ");

        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        Notary notary = Notary.getInstance();
        notary.setId(input);

        System.out.println("Notary running with id: " + input);
    }

}