package sec.notary.client.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sec.notary.client.domain.Client;

public class NotaryClient {
 
    /**
     * Starts the web service client.
     */
    public static void main(String[] args) {
        NotaryWebServiceService client = new NotaryWebServiceService();
        NotaryWebService notaryWebservice = client.getNotaryWebServicePort();

        System.out.print("Please insert Client ID: ");
        String input = System.console().readLine();
        Client notaryClient = new Client(input);
        notaryClient.setId(input);

        System.out.println("NotaryClient running with id: " + input);


        List<String> users = notaryWebservice.getUsers();

        for (String s : users) {
            System.out.println(s);
        }

        String res = notaryWebservice.getStateOfGood("good0");
        System.out.println(res);

        boolean b = notaryWebservice.intentionToSell(input,"good0");
        System.out.println(b);
    }
}