package client;

import domain.*;
import serverWS.NotaryWebService;
import serverWS.NotaryWebServiceImplService;
import sun.misc.BASE64Encoder;
import utils.RSAKeyGenerator;
import ws.impl.ClientWebServiceImpl;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Signature;
import java.util.Date;
import java.util.Scanner;
import clientWS.ClientWebServiceImplService;
import java.util.Base64;

import javax.xml.ws.Endpoint;

public class NotaryClient {

    /**
     * Starts the web service client.
     * 
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        NotaryWebServiceImplService client = new NotaryWebServiceImplService();
        NotaryWebService notaryWebservice = client.getNotaryWebServiceImplPort();
        String SYGN_KEY_WORD = "SYGN_KEY_WORD";

        Scanner scanner = new Scanner(System.in);

        System.out.print("Please insert Client ID: ");
        String input = scanner.next();
        Client notaryClient = new Client();
        notaryClient.setId(input);

        System.out.println("NotaryClient running with id: " + input);

        String bindingURI = "http://localhost:909" + input.substring(input.length() - 1) + "/" + input + "WebService";
        ClientWebServiceImpl webService = new ClientWebServiceImpl();
        Endpoint.publish(bindingURI, webService);

        System.out.println("Server started at: " + bindingURI);

        System.out.println("------------------------------ ");
        System.out.println("NotaryClient option:");
        System.out.println("1 -> intentToSell(good)");
        System.out.println("2 -> getStateGood(good)");
        System.out.println("3 -> buyGood(seller, good)");
        System.out.println("4 -> exit");

        boolean flag = true;

        do {
            System.out.print("select option: ");
            int opt = scanner.nextInt();
            
            String goodId;
            switch (opt) {
            case 1:
                System.out.print("Please insert good ID: ");
                goodId =  scanner.next();
                boolean b = notaryWebservice.intentionToSell(input, goodId, RSAKeyGenerator.writeSign(input, input+input));
                if(b){
                    System.out.println("-> " + goodId + " is now for sale");
                    System.out.println(" ");
                }else{
                    System.out.println("-> Something went wrong :( please try again later");
                    System.out.println(" ");
                }
                break;
            case 2:
                System.out.print("Please insert good ID: ");
                goodId =  scanner.next();
                String res = notaryWebservice.getStateOfGood(goodId);
                String[] ress = res.split(":");
                System.out.println("-> " + goodId + " owner  : " + ress[0].substring(1));
                System.out.println("-> " + goodId + " status : " + ress[1].substring(0, ress[1].length()-1));
                System.out.println(" ");
                break;
            case 3:
                System.out.print("Please insert sellerId: ");
                String name2 =  scanner.next();
                System.out.print("Please insert goodId: ");
                goodId = scanner.next();
                
                String mywebserviceURL = "http://localhost:909"+name2.substring(name2.length() -1)+"/"+name2+"WebService?wsdl";
                URL WsURL;
                try {
                    WsURL = new URL(mywebserviceURL);
                    ClientWebServiceImplService webService2 = new ClientWebServiceImplService(WsURL);
                    clientWS.ClientWebServiceImpl clientWebservice = webService2.getClientWebServiceImplPort();
                    Boolean bb = clientWebservice.buyGood(name2, input, goodId, RSAKeyGenerator.writeSign(input, input+input));
                    //System.out.println(bb);
                    if(bb){
                        System.out.println("-> Purchase successful");
                    }else{
                        System.out.println("-> Something went wrong :( please try again later");
                        System.out.println(" ");
                    }
                } catch (MalformedURLException e) {
                    System.out.println("Caught exception while contacting seller server: ");
                    e.printStackTrace();
                }
                        
                System.out.println(" ");
                break;
            case 4:
                flag = false;
                System.out.println("notary client terminated.");
                System.out.println(" ");
                System.exit(0);
                break;
            default :
                System.out.println("Invalid option");
                System.out.println(" ");
            }

        }while (flag);
    }
}
