package domain;

import serverWS.NotaryWebService;
import serverWS.NotaryWebServiceImplService;
import utils.RSAKeyGenerator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Client {

    private String _id;

    private static class SingletonHolder {
        private static final Client INSTANCE = new Client();
    }

    public static synchronized Client getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Client() {
        //setId(id);
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

    public List<String> buyGood(String sellerId, String buyerId, String goodId, String secret, String message_id){
        String[] msg = new String[]{buyerId, goodId, message_id };
        try {
            if(!readMessageIdFile(buyerId, message_id)){
                writeMessageIdFile(buyerId, message_id);
                if (RSAKeyGenerator.verifySign(buyerId, secret, msg)) {
                    NotaryWebServiceImplService client = new NotaryWebServiceImplService();
                    NotaryWebService notaryWebservice = client.getNotaryWebServiceImplPort();

                    String my_message_id = getMyMessageId(sellerId);
                    // Assinar o que vem do cliente(buyer) para enviar ao server
                    String[] args = new String[]{sellerId, buyerId, goodId, my_message_id};
                    String sellerSign = RSAKeyGenerator.writeSign(sellerId, sellerId + sellerId, args);

                    // Chamar metodo TransferGood do NotaryServer
                    
                    List<String> result = notaryWebservice.transferGood(Arrays.asList(sellerId, buyerId, goodId, sellerSign, secret, my_message_id));

                    if (result.size() == 2) {
                        if (RSAKeyGenerator.verifySignWithCert(result.get(0), new String[]{result.get(1)})) {
                            // Assinar o que vem do Server para enviar ao cliente(buyer)
                            String sellerSignResponseFromServer = RSAKeyGenerator.writeSign(sellerId, sellerId + sellerId, new String[]{result.get(0), result.get(1)});
                            return Arrays.asList(result.get(0), result.get(1), sellerSignResponseFromServer);
                        } else {
                            System.out.println("Error: NotaryServer Message Tampered");
                        }
                    } else {
                        System.out.println("Error: Something REALLY Wrong with NotaryServer");
                    }

                } else {
                    System.out.println("Error: Buyer Message Tampered ");
                }
            }
            else{
                System.out.println("Replay Attack !!");
            }
        } catch (CertificateException | IOException e) {
            e.printStackTrace();
        } 

        // Assinar o que vem do Server para enviar ao cliente(buyer)
        String sellerSignResponseFromServer = RSAKeyGenerator.writeSign(sellerId, sellerId + sellerId, new String[]{"false"});
        return Arrays.asList("false", sellerSignResponseFromServer);
    }

    private synchronized Boolean readMessageIdFile(String userId, String message_id) {
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

    private synchronized void writeMessageIdFile(String userId, String message_id) throws IOException {
        String currentDir = System.getProperty("user.dir");
        String path = currentDir + "/../src/main/resources/message-ids/other-users/" + userId + ".txt";
        FileWriter fileWriter = new FileWriter(path, true);
        PrintWriter writer = new PrintWriter(fileWriter);
        writer.println(message_id);
        writer.close();
        fileWriter.close();
    }

    private synchronized String getMyMessageId(String userId){
        String currentDir = System.getProperty("user.dir");
        String path = currentDir + "/../src/main/resources/message-ids/" + userId + ".txt";
        String res = "";
        try {
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