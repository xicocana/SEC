package client;

import javax.crypto.*;
import java.security.*;

import Handlers.RSAKeyGenerator;
import clientWS.ClientWebServiceImplService;
import domain.*;
import serverWS.NotaryWebService;
import serverWS.NotaryWebServiceImplService;
import ws.impl.ClientWebServiceImpl;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

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

        Scanner scanner = new Scanner(System.in);

        System.out.print("Please insert Client ID: ");
        String input = scanner.next();
        Client notaryClient = new Client();
        notaryClient.setId(input);

        System.out.println("NotaryClient running with id: " + input);

        String bindingURI = "http://localhost:909" + input.substring(input.length() - 1) + "/" + input + "WebService";
        ClientWebServiceImpl webService = new ClientWebServiceImpl();
        Endpoint.publish(bindingURI, webService);

        //keys generation
        String keysPath = "/../src/main/resources/keys/"+input;
        String currentDir = System.getProperty("user.dir");
        if(!new File(currentDir + keysPath).exists() && !new File(currentDir + keysPath).mkdirs()){
            throw new Exception("directory not created; please try again later");
        }
        String pubPath = currentDir + keysPath+"/pub.key";
        String privPath = currentDir + keysPath+"/priv.key";

        if(!new File(pubPath).exists() && !new File(privPath).exists() && !new File(pubPath).isDirectory() && !new File(privPath).isDirectory()){
            System.out.println("Generate and save keys");
            RSAKeyGenerator.write(privPath);
            RSAKeyGenerator.write(pubPath);
        }

        System.out.println("Server started at: " + bindingURI);

        System.out.println("------------------------------ ");
        System.out.println("NotaryClient option:");
        System.out.println("1 -> intentToSell(owner, good)");
        System.out.println("2 -> getStateGood(good)");
        System.out.println("3 -> buyGood(???)");
        System.out.println("4 -> exit");

        boolean flag = true;

        do {
            System.out.print("select option: ");
            int opt = scanner.nextInt();
            
            String goodId;
            switch (opt) {
            case 1:
                System.out.print("insert good ID: ");
                goodId =  scanner.next();
                boolean b = notaryWebservice.intentionToSell(input, goodId);
                System.out.println(b);
                System.out.println(" ");
                break;
            case 2:
                System.out.print("insert good ID: ");
                goodId =  scanner.next();
                String res = notaryWebservice.getStateOfGood(goodId);
                System.out.println(res);
                System.out.println(" ");
                break;
            case 3:
                System.out.print("Please insert seller server ID: ");
                String name2 =  scanner.next();
                System.out.print("Please insert goodId: ");
                goodId = scanner.next();
                

                String mywebserviceURL = "http://localhost:909"+name2.substring(name2.length() -1)+"/"+name2+"WebService?wsdl";
                URL WsURL;
                try {
                    WsURL = new URL(mywebserviceURL);
                    ClientWebServiceImplService webService2 = new ClientWebServiceImplService(WsURL);
                    clientWS.ClientWebServiceImpl clientWebservice = webService2.getClientWebServiceImplPort();

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

        }while (flag);
    }
}
