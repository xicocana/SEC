package sec.notary.server.ws;

import javax.xml.ws.Endpoint;
 
public class NotaryServer {
 
    /**
     * Starts a simple server to deploy the web service.
     */
    public static void main(String[] args) {
        String bindingURI = "http://localhost:9898/notaryService";
        NotaryWebService webService = new NotaryWebService();
        Endpoint.publish(bindingURI, webService);
        System.out.println("Server started at: " + bindingURI);
    }
}