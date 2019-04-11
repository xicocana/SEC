package client;

import domain.*;
import serverWS.NotaryWebService;
import serverWS.NotaryWebServiceImplService;
import utils.RSAKeyGenerator;
import ws.impl.ClientWebServiceImpl;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import clientWS.ClientWebServiceImplService;

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

        System.out.println("Server started at: " + bindingURI);
        pathToMessageIds = currentDir + "/../src/main/resources/message-ids/"+ input +".txt";

        message_id = Integer.parseInt(getMyMessageId(pathToMessageIds));


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
                    goodId = scanner.next();

                    //write my msg to File
                    message_id++;
                    WriteUsedMessageId(pathToMessageIds, message_id);

                    //Call SERVER METHOD
                    String[] args2 = new String[]{input, goodId,Integer.toString(message_id)};
                    List<String> argsToSend = Arrays.asList(input, goodId, RSAKeyGenerator.writeSign(input, input + input, args2), Integer.toString(message_id));
                    List<String> result = notaryWebservice.intentionToSell(argsToSend);

                    if (result.size() == 5) {
                        if (RSAKeyGenerator.verifySignWithCert(result.get(0), result.get(1), result.get(2), result.get(3), result.get(4))){
                            if(!readMessageIdFile("server", result.get(4))){
                                String path = currentDir + "/../src/main/resources/message-ids/other-users/server.txt";
                                WriteUsedMessageId(path, Integer.parseInt(result.get(4)));
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
                        if (RSAKeyGenerator.verifySignWithCert(result.get(0), new String[]{result.get(1)})) {
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
                    message_id++;
                    WriteUsedMessageId(pathToMessageIds, message_id);

                    //Call SERVER METHOD
                    args = new String[]{input, goodId, Integer.toString(message_id)};
                    argsToSend = Arrays.asList(input, goodId, RSAKeyGenerator.writeSign(input, input + input, args), Integer.toString(message_id));
                    result = notaryWebservice.getStateOfGood(argsToSend);

                    try {
                        if (result.size() == 4) {
                            if (RSAKeyGenerator.verifySignWithCert(result.get(0), result.get(1), result.get(2),result.get(3))) {
                                if(!readMessageIdFile("server", result.get(3))) {
                                    String path = currentDir + "/../src/main/resources/message-ids/other-users/server.txt";
                                    WriteUsedMessageId(path, Integer.parseInt(result.get(3)));

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

                    //write my msg to File
                    message_id++;
                    WriteUsedMessageId(pathToMessageIds, message_id);

                    String mywebserviceURL = "http://localhost:909" + name2.substring(name2.length() - 1) + "/" + name2 + "WebService?wsdl";
                    URL WsURL;
                    try {
                        WsURL = new URL(mywebserviceURL);
                        ClientWebServiceImplService webService2 = new ClientWebServiceImplService(WsURL);
                        clientWS.ClientWebServiceImpl clientWebservice = webService2.getClientWebServiceImplPort();

                        String sign = RSAKeyGenerator.writeSign(input, input + input, input, goodId, Integer.toString(message_id));
                        argsToSend = Arrays.asList(name2, input, goodId, sign, Integer.toString(message_id));
                        //Client(buyer) chama para outro cliente(seller)
                        result = clientWebservice.buyGood(argsToSend);

                        if (result.size() == 3) {
                            if (RSAKeyGenerator.verifySignWithCert(result.get(0), result.get(1)) &&
                                    RSAKeyGenerator.verifySign(name2, result.get(2), result.get(0), result.get(1))) {
                                System.out.println("-> Purchase successful");
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

    public static void WriteUsedMessageId(String path, int id) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        FileWriter fileWriter = new FileWriter(path, true);
        PrintWriter writer = new PrintWriter(fileWriter);
        writer.println(id);
        writer.close();
        fileWriter.close();
    }

    private static synchronized Boolean readMessageIdFile(String userId, String message_id) {
        try {
            String currentDir = System.getProperty("user.dir");
            String path = currentDir + "/../src/main/resources/message-ids/other-users/" + userId + ".txt";

            if (!Files.exists(Paths.get(path))) {
                Files.createFile(Paths.get(path));
            }

            Stream<String> stream = Files.lines(Paths.get(path));
            return stream.anyMatch(x -> x.equals(message_id));

        } catch (IOException e) {
            System.out.println("Caught exception while reading users file:");
            e.printStackTrace();
        }

        return false;
    }

    private static synchronized String getMyMessageId(String path){
        String currentDir = System.getProperty("user.dir");
        //String path = currentDir + "/../src/main/resources/message-ids/" + userId + ".txt";
        String res = "";
        try {

            if (!Files.exists(Paths.get(path))) {
                Files.createFile(Paths.get(path));
                return "0";
            }

            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line = reader.readLine();
            while (line != null) {
                // line
                res = line;
                // read next line
                line = reader.readLine();
            }
            reader.close();
            return res;
        } catch (IOException e) {
            System.out.println("Caught exception while reading users file:");
            e.printStackTrace();
            return "";
        }
    }
}
