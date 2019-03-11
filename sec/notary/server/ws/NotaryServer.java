package sec.notary.server.ws;

import javax.xml.ws.Endpoint;

import sec.notary.server.domain.Notary;
 
public class NotaryServer {
 
    /**
     * Starts a simple server to deploy the web service.
     */
    public static void main(String[] args) {
        String bindingURI = "http://localhost:9898/notaryService";
        NotaryWebService webService = new NotaryWebService();
        Endpoint.publish(bindingURI, webService);
        System.out.println("Server started at: " + bindingURI);

        System.out.print("Please insert Notary ID: ");
        String input = System.console().readLine();
        Notary notary = Notary.getInstance();
        notary.setId(Integer.parseInt(input));

        System.out.println("Notary running with id: " + input);
    }
}