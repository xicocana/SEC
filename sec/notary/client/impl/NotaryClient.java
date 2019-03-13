package sec.notary.client.impl;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.ws.Endpoint;

import sec.notary.client.domain.Client;
import sec.notary.client.ws.*;

public class NotaryClient {

    /**
     * Starts the web service client.
     */
    public static void main(String[] args) {
        NotaryWebServiceService client = new NotaryWebServiceService();
        NotaryWebService notaryWebservice = client.getNotaryWebServicePort();

        System.out.print("Please insert Client ID: ");
        String input = System.console().readLine();
        Client notaryClient = new Client();
        notaryClient.setId(input);

        System.out.println("NotaryClient running with id: " + input);

        System.out.print("Please insert Client server port: ");
        String port = System.console().readLine();

        String bindingURI = "http://localhost:" + port + "/" + input + "WebService";
        ClientWebService webService = new ClientWebService();
        Endpoint.publish(bindingURI, webService);
        System.out.println("Server started at: " + bindingURI);

        System.out.println("------------------------------ ");
        System.out.println("NotaryClient option:");
        System.out.println("1 -> intentToSell(owner, good)");
        System.out.println("2 -> getStateGood(good)");
        System.out.println("3 -> buyGood(sellerId, buyerId, goodId)");
        System.out.println("4 -> exit");

        System.out.print("select option: ");
        String opt = System.console().readLine();

        boolean flag = true;
        while (flag) {
            String goodid;
            switch (Integer.parseInt(opt)) {
            case 1:
                System.out.print("insert good ID: ");
                goodid = System.console().readLine();
                boolean b = notaryWebservice.intentionToSell(input, goodid);
                System.out.println(b);
                System.out.println(" ");
                break;
            case 2:
                System.out.print("insert good ID: ");
                goodid = System.console().readLine();
                String res = notaryWebservice.getStateOfGood(goodid);
                System.out.println(res);
                System.out.println(" ");
                break;
            case 3:
                System.out.print("Please insert seller server ID: ");
                String name2 = System.console().readLine();
                System.out.print("Please insert seller port: ");
                String port2 = System.console().readLine();
                System.out.print("Please insert goodId: ");
                String goodId = System.console().readLine();
                

                String mywebserviceURL = "http://localhost:"+port2+"/"+name2+"WebService?wsdl";
                URL WsURL;
                try {
                    WsURL = new URL(mywebserviceURL);
                    ClientWebServiceService webService2 = new ClientWebServiceService(WsURL);
                    sec.notary.client.ws.ClientWebService clientWebservice = webService2.getClientWebServicePort();
        
                    Boolean bb = clientWebservice.buyGood(name2, input, goodId);
                    System.out.println(bb);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                        
                System.out.println(" ");
                break;
            case 4:
                flag = false;
                System.out.println("notary client terminated.");
                System.out.println(" ");
                break;
            default :
                System.out.println("Invalid option");
                System.out.println(" ");
            }
            if(flag){
                System.out.print("select option: ");
                opt = System.console().readLine();
            }
        }
    }


}
