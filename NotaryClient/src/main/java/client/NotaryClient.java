package client;

import domain.*;
import serverWS.NotaryWebService;
import serverWS.NotaryWebServiceImplService;
import utils.RSAKeyGenerator;
import ws.impl.ClientWebServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

import clientWS.ClientWebServiceImplService;

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
                    goodId = scanner.next();
                    String[] args2 = new String[]{input, goodId};

                    List<String> result = notaryWebservice.intentionToSell(input, goodId, RSAKeyGenerator.writeSign(input, input + input, args2));
                    if (result.size() == 4) {
                        if (RSAKeyGenerator.verifySignWithCert(result.get(0), new String[]{result.get(1), result.get(2),result.get(3)})) {
                            if (Boolean.valueOf(result.get(1))) {
                                System.out.println("-> " + goodId + " is now for sale");
                                System.out.println(" ");
                            } else {
                                System.out.println("-> Something went wrong :( please try again later");
                                System.out.println(" ");
                            }
                        } else {
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

                    result = notaryWebservice.getStateOfGood(goodId);
                    try {
                        if (result.size() == 3) {
                            if (RSAKeyGenerator.verifySignWithCert(result.get(0), new String[]{result.get(1), result.get(2)})) {
                                System.out.println("-> " + goodId + " owner  : " + result.get(2));

                                String onSale = Boolean.valueOf(result.get(1)) ? "on-sale" : "not-on-sale";
                                System.out.println("-> " + goodId + " status : " + onSale);
                                System.out.println(" ");
                            } else {
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
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.print("Please insert sellerId: ");
                    String name2 = scanner.next();
                    System.out.print("Please insert goodId: ");
                    goodId = scanner.next();

                    String mywebserviceURL = "http://localhost:909" + name2.substring(name2.length() - 1) + "/" + name2 + "WebService?wsdl";
                    URL WsURL;
                    try {
                        WsURL = new URL(mywebserviceURL);
                        ClientWebServiceImplService webService2 = new ClientWebServiceImplService(WsURL);
                        clientWS.ClientWebServiceImpl clientWebservice = webService2.getClientWebServiceImplPort();
                        String[] args3 = new String[]{input, goodId};
                        Boolean bb = clientWebservice.buyGood(name2, input, goodId, RSAKeyGenerator.writeSign(input, input + input, args3));
                        //TODO
                        //System.out.println(bb);
                        if (bb) {
                            System.out.println("-> Purchase successful");
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


    private static Certificate getTrustedSigner(Certificate cert, KeyStore ks)
            throws Exception {
        if (ks.getCertificateAlias(cert) != null) {
            return cert;
        }
        for (Enumeration<String> aliases = ks.aliases();
             aliases.hasMoreElements(); ) {
            String name = aliases.nextElement();
            Certificate trustedCert = ks.getCertificate(name);
            if (trustedCert != null) {
                try {
                    cert.verify(trustedCert.getPublicKey());
                    return trustedCert;
                } catch (Exception e) {
                    // Not verified, skip to the next one
                }
            }
        }
        return null;
    }
}
