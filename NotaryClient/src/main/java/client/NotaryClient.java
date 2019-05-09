package client;

import domain.*;
import utils.RSAKeyGenerator;
import utils.RoundRobin;
import utils.WriteReadUtils;
import ws.impl.ClientWebServiceImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import ws.importWS.clientWS.ClientWebServiceImplService;

import javax.xml.ws.Endpoint;

public class NotaryClient {

    private boolean withCC = true;

    public NotaryClient() {
        super();
    }

    private void initializeClient() throws IOException, CertificateException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please insert Client ID: ");
        String input = scanner.next();

        System.out.println("Do you want to use CC? yes or no");
        String value = scanner.next();
        withCC = value.equalsIgnoreCase("yes");

        String currentDir = System.getProperty("user.dir");
        String dirPath = currentDir + "/../src/main/resources/message-ids/" + input + "/other-users/";
        WriteReadUtils.createDir(dirPath);

        Client notaryClient = new Client();
        notaryClient.setId(input);

        System.out.println("NotaryClient running with id: " + input);
        String bindingURI = "http://localhost:909" + input.substring(input.length() - 1) + "/" + input + "WebService";
        ClientWebServiceImpl webService = new ClientWebServiceImpl();
        Endpoint.publish(bindingURI, webService);

        System.out.println("Server started at: " + bindingURI);
        String pathToMessageIds = currentDir + "/../src/main/resources/message-ids/" + input + "/" + input + ".txt";

        String var = WriteReadUtils.getMyMessageId(pathToMessageIds);
        int message_id = var.equals("") ? 0 : Integer.parseInt(var);

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
        String[] args2;
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
                    args2 = new String[]{input, goodId, Integer.toString(message_id)};
                    List<String> argsToSend = Arrays.asList(input, goodId, RSAKeyGenerator.writeSign(input, input + input, args2), Integer.toString(message_id));

                    for (int i = 0; i < activeServers; i++) {
                        result = rr.getServer().intentionToSell(argsToSend);
                        results.add(result);
                    }

                    if (result.size() == 5) {
                        if (verifyGeneric("server", result.get(0), result.get(1), result.get(2), result.get(3), result.get(4))) {
                            dirPath = currentDir + "/../src/main/resources/message-ids/" + input + "/other-users/server.txt";
                            if (!WriteReadUtils.readMessageIdFile(dirPath, result.get(4))) {
                                String path = currentDir + "/../src/main/resources/message-ids/" + input + "/other-users/server.txt";
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
                        } else {
                            System.out.println("Error: NotaryServer Message Tampered");
                        }
                    } else if (result.size() == 2) {
                        if (verifyGeneric("server", result.get(0), result.get(1))) {
                            System.out.println("Error: Something Wrong with NotaryServer");
                        } else {
                            System.out.println("Error: NotaryServer Message Tampered");
                        }
                    } else {
                        System.out.println("Error: Something REALLY Wrong with NotaryServer");
                    }

                    break;
                case 2:
                    AntiSpamFunction(10);
                    System.out.print("Please insert good ID: ");
                    goodId = scanner.next();

                    //write my msg to File
                    var = WriteReadUtils.getMyMessageId(pathToMessageIds);
                    message_id = var.equals("") ? 0 : Integer.parseInt(var);
                    message_id++;
                    WriteReadUtils.writeUsedMessageId(pathToMessageIds, message_id);

//                    //Call SERVER METHOD
//                    args2 = new String[]{input, goodId, Integer.toString(message_id)};
//                    argsToSend = Arrays.asList(input, goodId, RSAKeyGenerator.writeSign(input, input + input, args2), Integer.toString(message_id));


                    result = rr.getStateOfGoodCommunication(input, goodId, Integer.toString(message_id));


                    results.add(result);


                    try {
                        if (result.size() == 4) {
                            if (verifyGeneric("server", result.get(0), result.get(1), result.get(2), result.get(3))) {

                                dirPath = currentDir + "/../src/main/resources/message-ids/" + input + "/other-users/server.txt";
                                if (!WriteReadUtils.readMessageIdFile(dirPath, result.get(3))) {
                                    String path = currentDir + "/../src/main/resources/message-ids/" + input + "/other-users/server.txt";
                                    WriteReadUtils.writeUsedMessageId(path, Integer.parseInt(result.get(3)));

                                    System.out.println("-> " + goodId + " owner  : " + result.get(2));
                                    String onSale = Boolean.valueOf(result.get(1)) ? "on-sale" : "not-on-sale";
                                    System.out.println("-> " + goodId + " status : " + onSale);
                                    System.out.println(" ");
                                } else {
                                    System.out.println("Replay Attack !!");
                                }
                            } else {
                                System.out.println("Error: NotaryServer Message Tampered");
                            }

                        } else if (result.size() == 2) {
                            if (verifyGeneric("server", result.get(0), result.get(1))) {
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
                            if (verifyGeneric("server", result.get(0), result.get(1)) &&
                                    verifyGeneric(name2, result.get(2), result.get(0), result.get(1))) {

                                dirPath = currentDir + "/../src/main/resources/message-ids/" + input + "/other-users/server.txt";
                                if (!WriteReadUtils.readMessageIdFile(dirPath, result.get(3))) {
                                    String path = currentDir + "/../src/main/resources/message-ids/" + input + "/other-users/" + name2 + ".txt";
                                    WriteReadUtils.writeUsedMessageId(path, Integer.parseInt(result.get(3)));

                                    System.out.println("-> Purchase successful");

                                } else {
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

    private void AntiSpamFunction(int seconds) {


        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int a = random.nextInt(100);
        int b = random.nextInt(100);
        System.out.println("What is " + a + " + " + b + " ?");
        String input = scanner.next();

        System.out.println("Validating answer...");

        for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(seconds); stop > System.nanoTime(); ) {
            /*
             * Hammer the JVM with junk
             */

            int x = random.nextInt(100) + 100;
            List<String> lista = new ArrayList<>();
            for (int i = 0; i < x; i++) {
                lista.add("A");
            }
            String conc = "";
            for (int i = 0; i < lista.size(); i++) {
                conc = conc + lista.get(i);
            }
        }
        int res = a + b;

        if (input.equals(Integer.toString(res))) {
            System.out.println("Success");
        } else {
            System.out.println("Wrong, try again");
            AntiSpamFunction(10);
        }

    }

    public boolean verifyGeneric(String owner, String secret, String... args) throws CertificateException, FileNotFoundException {
        if (withCC && owner.equals("server")) {
            return RSAKeyGenerator.verifySignWithCert(secret, args);
        } else {
            return RSAKeyGenerator.verifySign(owner, secret, args);
        }
    }

    /**
     * Starts the web service client.
     */
    public static void main(String[] args) throws Exception {
        NotaryClient notaryClient = new NotaryClient();
        notaryClient.initializeClient();
    }
}
