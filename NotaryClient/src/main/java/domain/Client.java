package domain;


import serverWS.NotaryWebService;
import serverWS.NotaryWebServiceImplService;
import utils.RSAKeyGenerator;

import java.io.FileNotFoundException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public List<String> buyGood(String sellerId, String buyerId, String goodId, String secret) {
        String[] msg = new String[]{buyerId, goodId};
        try {
            if (RSAKeyGenerator.verifySign(buyerId, secret, msg)) {
                NotaryWebServiceImplService client = new NotaryWebServiceImplService();
                NotaryWebService notaryWebservice = client.getNotaryWebServiceImplPort();

                // Assinar o que vem do cliente(buyer) para enviar ao server
                String[] args = new String[]{sellerId, buyerId, goodId};
                String sellerSign = RSAKeyGenerator.writeSign(sellerId, sellerId + sellerId, args);

                // Chamar metodo TransferGood do NotaryServer
                List<String> result = notaryWebservice.transferGood(Arrays.asList(sellerId, buyerId, goodId, sellerSign, secret));

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
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Assinar o que vem do Server para enviar ao cliente(buyer)
        String sellerSignResponseFromServer = RSAKeyGenerator.writeSign(sellerId, sellerId + sellerId, new String[]{"false"});
        return Arrays.asList("false", sellerSignResponseFromServer);
    }
}