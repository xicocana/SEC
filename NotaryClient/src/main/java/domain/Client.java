package domain;


import serverWS.NotaryWebService;
import serverWS.NotaryWebServiceImplService;
import utils.RSAKeyGenerator;

public class Client{

    private String _id;

    private static class SingletonHolder {
        private static final Client INSTANCE = new Client();
    }

    public static synchronized Client getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Client(){
        //setId(id);
    }

    public void setId(String id) {
        _id = id;
    }

    public String getId(){
        return _id;
    }

    public String test(String Id){
        System.out.println("client called test");
        return Id+"executed at client side !";
    }

    public Boolean buyGood(String sellerId, String buyerId, String goodId, String secret) {
        String[] msg = new String[]{buyerId, goodId};
        if (RSAKeyGenerator.verifySign(buyerId, secret, msg)) {
            NotaryWebServiceImplService client = new NotaryWebServiceImplService();
            NotaryWebService notaryWebservice = client.getNotaryWebServiceImplPort();
            String[] args = new String[]{sellerId, buyerId, goodId};
            return notaryWebservice.transferGood(sellerId, buyerId, goodId, RSAKeyGenerator.writeSign(sellerId, sellerId+sellerId, args), secret);
        }
        else{
            System.out.println("Error: Message Tampered");
            return false;
        }
    }
}