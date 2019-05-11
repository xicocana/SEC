package domain;

import utils.RSAKeyGenerator;
import utils.RoundRobin;
import utils.WriteReadUtils;
import ws.importWS.serverWS.NotaryWebService;
import ws.importWS.serverWS.NotaryWebServiceImplService;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Client {

    private String _id;

    private static class SingletonHolder {
        private static final Client INSTANCE = new Client();
    }

    public static synchronized Client getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Client() {

    }

    public void setId(String id) {
        _id = id;
    }

    public String getId() {
        return _id;
    }

    public String test(String Id) {
        System.out.println("client called test");
        return Id + "executed at client side !";
    }

    public void createDirs() {
        String currentDir = System.getProperty("user.dir");
        String dirPath = currentDir + "/classes/message-ids/" + _id + "/other-users/";
        WriteReadUtils.createDir(dirPath);
    }

    public List<String> buyGood(String sellerId, String buyerId, String goodId, String secret, String message_id_buyer) {
        String currentDir = System.getProperty("user.dir");

        RoundRobin rr = RoundRobin.getInstance();
        rr.setUser(sellerId);

        try {
            String dirPath = currentDir + "/classes/message-ids/" + sellerId + "/other-users/" + buyerId + ".txt";
            if (!WriteReadUtils.readMessageIdFile(dirPath, message_id_buyer)) {

                WriteReadUtils.writeUsedMessageId(dirPath, Integer.parseInt(message_id_buyer));

                String[] msg = new String[]{buyerId, goodId, message_id_buyer};
                if (RSAKeyGenerator.verifySign(buyerId, secret, msg)) {
                    NotaryWebServiceImplService client = new NotaryWebServiceImplService();
                    NotaryWebService notaryWebservice = client.getNotaryWebServiceImplPort();

                    String dirPath2 = currentDir + "/classes/message-ids/" + sellerId + "/" + sellerId + ".txt";

                    String my_message_id = WriteReadUtils.getMyMessageId(dirPath2);
                    int my_message_id2 = my_message_id.equals("") ? 0 : Integer.parseInt(my_message_id) + 1;
                    // Assinar o que vem do cliente(buyer) para enviar ao server
                    String[] args = new String[]{sellerId, buyerId, goodId, Integer.toString(my_message_id2)};
                    String sellerSign = RSAKeyGenerator.writeSign(sellerId, sellerId + sellerId, args);

                    dirPath = currentDir + "/classes/message-ids/" + sellerId + "/" + sellerId + ".txt";
                    WriteReadUtils.writeUsedMessageId(dirPath, my_message_id2);

                    // Chamar metodo TransferGood do NotaryServer

                    List<String> result = rr.transferGoodCommunication(sellerId, buyerId, goodId, secret, Integer.toString(my_message_id2), message_id_buyer);


                    if (result.size() == 4) {
                        if (RSAKeyGenerator.verifySignWithCert(result.get(0), result.get(1))) {
                            // Assinar o que vem do Server para enviar ao cliente(buyer)
                            String sellerSignResponseFromServer = RSAKeyGenerator.writeSign(sellerId, sellerId + sellerId, result.get(0), result.get(1));


                            //write messageid File to send buyer
                            my_message_id2++;
                            dirPath = currentDir + "/classes/message-ids/" + sellerId + "/" + sellerId + ".txt";
                            WriteReadUtils.writeUsedMessageId(dirPath, my_message_id2);

                            return Arrays.asList(result.get(0), result.get(1), sellerSignResponseFromServer, Integer.toString(my_message_id2));
                        } else {
                            System.out.println("Error: NotaryServer Message Tampered");
                        }
                    } else {
                        System.out.println("Error: Something REALLY Wrong with NotariuyServer");
                    }

                } else {
                    System.out.println("Error: Buyer Message Tampered ");
                }
            } else {
                System.out.println("Replay Attack !!");
            }
        } catch (CertificateException | IOException e) {
            e.printStackTrace();
        }

        // Assinar o que vem do Server para enviar ao cliente(buyer)
        String sellerSignResponseFromServer = RSAKeyGenerator.writeSign(sellerId, sellerId + sellerId, new String[]{"false"});
        return Arrays.asList("false", sellerSignResponseFromServer);
    }
}