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

        System.out.println("------------------------------ ");
        System.out.println("NotaryClient option:");
        System.out.println("1 -> intentToSell(owner, good)");
        System.out.println("2 -> getStateGood(good)");
        System.out.println("3 -> buyGood(???)");
        System.out.println("4 -> exit");

        System.out.print("select option: ");
        String opt = System.console().readLine();

        boolean flag = true;
        while(flag){
            String goodid;
            switch(Integer.parseInt(opt)) {
                case 1 :
                    System.out.print("insert good ID: ");
                    goodid = System.console().readLine();
                    boolean b = notaryWebservice.intentionToSell(input,goodid);
                    System.out.println(b);
                    System.out.println(" ");
                    break;
                case 2 :
                    System.out.print("insert good ID: ");
                    goodid = System.console().readLine();
                    String res = notaryWebservice.getStateOfGood(goodid);
                    System.out.println(res);
                    System.out.println(" ");
                    break;
                case 3 :
                    System.out.println("selected option 3");
                    System.out.println("work in progress...");
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