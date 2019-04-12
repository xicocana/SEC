package client;

import domain.Client;
import ws.impl.ClientWebServiceImpl;

import javax.xml.ws.Endpoint;
import java.util.Scanner;

public class ClientDummy {

    public static void main(String[] args) {
        System.out.print("Client Dummy ");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please insert Client ID: ");
        String input = scanner.nextLine();
        Client notaryClient = new Client();
        notaryClient.setId(input);
        notaryClient.createDirs();


        System.out.println("NotaryClient running with id: " + input);

        System.out.print("Please insert Client server port: ");
        String port = scanner.nextLine();

        String bindingURI = "http://localhost:" + port + "/" + input + "/WebService";
        ClientWebServiceImpl webService = new ClientWebServiceImpl();
        Endpoint.publish(bindingURI, webService);

        System.out.println("ClientDummy running with id: " + input);
        System.out.println("ClientDummy started at: " + bindingURI);

    }
}
