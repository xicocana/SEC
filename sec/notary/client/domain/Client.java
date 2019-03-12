package sec.notary.client.domain;

import sec.notary.server.domain.Notary;

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

    public Boolean buyGood(String sellerId, String buyerId, String GoodId) {
        Notary notary = Notary.getInstance();
        return notary.transferGood(sellerId, buyerId, GoodId);
    }
}