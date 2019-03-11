package sec.notary.client.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotaryClient {
 
    /**
     * Starts the web service client.
     */
    public static void main(String[] args) {
        NotaryWebServiceService client = new NotaryWebServiceService();
        NotaryWebService notaryWebservice = client.getNotaryWebServicePort();
        List<String> users = notaryWebservice.getUsers();

        for (String s : users) {
            System.out.println(s);
        }

        String res = notaryWebservice.getStateOfGood("good0");
        System.out.println(res);
    }
}