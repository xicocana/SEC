package sec.notary.client.ws;
 
public class NotaryClient {
 
    /**
     * Starts the web service client.
     */
    public static void main(String[] args) {
        NotaryWebServiceService client = new NotaryWebServiceService();
        NotaryWebService notaryWebservice = client.getNotaryWebServicePort();
        boolean teste = notaryWebservice.intentionToSell();
        System.out.println("teste intenttosell -> " + teste);
    }
}