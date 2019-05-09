package client;

import domain.*;
import utils.RSAKeyGenerator;
import utils.RoundRobin;
import utils.WriteReadUtils;
import ws.impl.ClientWebServiceImpl;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import ws.importWS.clientWS.ClientWebServiceImplService;

import javax.xml.ws.Endpoint;

public class NotaryClient {

    private static int message_id = 0;
    private static BufferedReader reader;
    private static String currentDir = System.getProperty("user.dir");
    private static String pathToMessageIds;

    /**
     * Starts the web service client.
     *
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please insert Client ID: ");
        String input = scanner.next();

        String currentDir = System.getProperty("user.dir");
        String dirPath = currentDir + "/../src/main/resources/message-ids/"+ input +"/other-users/";
        WriteReadUtils.createDir(dirPath);

        Client notaryClient = new Client();
        notaryClient.setId(input);

        System.out.println("NotaryClient running with id: " + input);
        String bindingURI = "http://localhost:909" + input.substring(input.length() - 1) + "/" + input + "WebService";
        ClientWebServiceImpl webService = new ClientWebServiceImpl();
        Endpoint.publish(bindingURI, webService);

        System.out.println("Server started at: " + bindingURI);
        pathToMessageIds = currentDir + "/../src/main/resources/message-ids/"+ input +"/" + input +".txt";

        String var = WriteReadUtils.getMyMessageId(pathToMessageIds);
        message_id = var.equals("") ? 0 : Integer.parseInt(var);

        System.out.println("------------------------------ ");
        System.out.println("NotaryClient option:");
        System.out.println("1 -> intentToSell(good)");
        System.out.println("2 -> getStateGood(good)");
        System.out.println("3 -> buyGood(seller, good)");
        System.out.println("4 -> exit");

        boolean flag = true;

        RoundRobin rr = RoundRobin.getInstance();
        int activeServers = rr.getActiveServers();

        List<String> result = new ArrayList<>();
        List<List<String>> results = new ArrayList<>();

        do {
            System.out.print("select option: ");
            int opt = scanner.nextInt();
            String goodId;
            switch (opt) {
                case 1:
                    System.out.print("Please insert good ID: ");
                    goodId = scanner.next();

                    //write my msg to File
                    var = WriteReadUtils.getMyMessageId(pathToMessageIds);
                    message_id = var.equals("") ? 0 : Integer.parseInt(var);
                    message_id++;
                    WriteReadUtils.writeUsedMessageId(pathToMessageIds, message_id);

                    //Call SERVER METHOD
                    String[] args2 = new String[]{input, goodId,Integer.toString(message_id)};
                    List<String> argsToSend = Arrays.asList(input, goodId, RSAKeyGenerator.writeSign(input, input + input, args2), Integer.toString(message_id));

                    for(int i=0; i<activeServers; i++){
                        result = rr.getServer().intentionToSell(argsToSend);
                        results.add(result);
                    }

                    if (result.size() == 5) {
                        if (RSAKeyGenerator.verifySignWithCert(result.get(0), result.get(1), result.get(2), result.get(3), result.get(4))){
                            dirPath = currentDir + "/../src/main/resources/message-ids/"+ input +"/other-users/server.txt";
                            if(!WriteReadUtils.readMessageIdFile(dirPath, result.get(4))){
                                String path = currentDir + "/../src/main/resources/message-ids/"+input+"/other-users/server.txt";
                                WriteReadUtils.writeUsedMessageId(path, Integer.parseInt(result.get(4)));
                                if (Boolean.valueOf(result.get(1))) {
                                    System.out.println("-> " + goodId + " is now for sale");
                                    System.out.println(" ");
                                } else {
                                    System.out.println("-> Something went wrong :( please try again later");
                                    System.out.println(" ");
                                }
                            } else {
                                System.out.println("Replay Attack !!");
                            }
                        }
                        else{
                            System.out.println("Error: NotaryServer Message Tampered");
                        }
                    } else if (result.size() == 2) {
                        if (RSAKeyGenerator.verifySignWithCert(result.get(0), result.get(1))) {
                            System.out.println("Error: Something Wrong with NotaryServer");
                        } else {
                            System.out.println("Error: NotaryServer Message Tampered");
                        }
                    } else {
                        System.out.println("Error: Something REALLY Wrong with NotaryServer");
                    }

                    break;
                case 2:
                    System.out.print("Please insert good ID: ");
                    goodId = scanner.next();

                    //write my msg to File
                    var = WriteReadUtils.getMyMessageId(pathToMessageIds);
                    message_id = var.equals("") ? 0 : Integer.parseInt(var);
                    message_id++;
                    WriteReadUtils.writeUsedMessageId(pathToMessageIds, message_id);
                    
                    //Call SERVER METHOD
                    args = new String[]{input, goodId, Integer.toString(message_id)};
                    argsToSend = Arrays.asList(input, goodId, RSAKeyGenerator.writeSign(input, input + input, args), Integer.toString(message_id));

                    for(int i=0; i<activeServers; i++){
                        result = rr.getStateOfGoodCommunication(argsToSend);
                        results.add(result);
                    }

                    try {
                        if (result.size() == 4) {
                            if (RSAKeyGenerator.verifySignWithCert(result.get(0), result.get(1), result.get(2),result.get(3))) {

                                dirPath = currentDir + "/../src/main/resources/message-ids/"+ input +"/other-users/server.txt";
                                if(!WriteReadUtils.readMessageIdFile(dirPath, result.get(3))) {
                                    String path = currentDir + "/../src/main/resources/message-ids/"+ input +"/other-users/server.txt";
                                    WriteReadUtils.writeUsedMessageId(path, Integer.parseInt(result.get(3)));

                                    System.out.println("-> " + goodId + " owner  : " + result.get(2));
                                    String onSale = Boolean.valueOf(result.get(1)) ? "on-sale" : "not-on-sale";
                                    System.out.println("-> " + goodId + " status : " + onSale);
                                    System.out.println(" ");
                                }else{
                                    System.out.println("Replay Attack !!");
                                }
                            } else {
                                System.out.println("Error: NotaryServer Message Tampered");
                            }

                        } else if (result.size() == 2) {
                            if (RSAKeyGenerator.verifySignWithCert(result.get(0), result.get(1))) {
                                System.out.println("Error: Something Wrong with NotaryServer");
                            } else {
                                System.out.println("Error: NotaryServer Message Tampered");
                            }
                        } else {
                            System.out.println("Error: Something REALLY Wrong with NotaryServer");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.print("Please insert sellerId: ");
                    String name2 = scanner.next();
                    System.out.print("Please insert goodId: ");
                    goodId = scanner.next();

                    //write my(Buyer) msg to  my File
                    var = WriteReadUtils.getMyMessageId(pathToMessageIds);
                    message_id = var.equals("") ? 0 : Integer.parseInt(var);
                    message_id++;
                    WriteReadUtils.writeUsedMessageId(pathToMessageIds, message_id);

                    String mywebserviceURL = "http://localhost:909" + name2.substring(name2.length() - 1) + "/" + name2 + "WebService?wsdl";

                    try {
                        ClientWebServiceImplService webService2 = new ClientWebServiceImplService(new URL(mywebserviceURL));
                        ws.importWS.clientWS.ClientWebServiceImpl clientWebservice = webService2.getClientWebServiceImplPort();

                        String sign = RSAKeyGenerator.writeSign(input, input + input, input, goodId, Integer.toString(message_id));
                        argsToSend = Arrays.asList(name2, input, goodId, sign, Integer.toString(message_id));

                        //Client(buyer) chama para outro cliente(seller)
                        result = clientWebservice.buyGood(argsToSend);

                        if (result.size() == 4) {
                            if (RSAKeyGenerator.verifySignWithCert(result.get(0), result.get(1)) &&
                                    RSAKeyGenerator.verifySign(name2, result.get(2), result.get(0), result.get(1))) {

                                dirPath = currentDir + "/../src/main/resources/message-ids/"+ input +"/other-users/server.txt";
                                if(!WriteReadUtils.readMessageIdFile(dirPath, result.get(3))) {
                                    String path = currentDir + "/../src/main/resources/message-ids/" + input + "/other-users/"+name2+".txt";
                                    WriteReadUtils.writeUsedMessageId(path, Integer.parseInt(result.get(3)));

                                    System.out.println("-> Purchase successful");

                                }else{
                                    System.out.println("Replay Attack !!");
                                }
                            } else {
                                System.out.println("-> Something went wrong :( please try again later");
                            }

                        } else {
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
                default:
                    System.out.println("Invalid option");
                    System.out.println(" ");
            }

        } while (flag);
    }
}
