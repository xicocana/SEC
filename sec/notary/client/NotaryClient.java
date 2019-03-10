package sec.notary.client;
 
public class NotaryClient {
 
    /**
     * Starts the web service client.
     */
    public static void main(String[] args) {
        NotaryWebServiceService client = new NotaryWebServiceService();
        NotaryWebService notaryWebservice = client.getNotaryWebServicePort();
        String teste = notaryWebservice.test("admin");
        System.out.println("teste admin -> " + teste);
    }
}