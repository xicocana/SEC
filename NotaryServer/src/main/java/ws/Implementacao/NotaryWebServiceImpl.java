package ws.Implementacao;

import domain.Notary;
import ws.Interfaces.NotaryWebService;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;


@WebService(endpointInterface = "ws.Interfaces.NotaryWebService")
@HandlerChain(file = "/ServerChain.xml")
public class NotaryWebServiceImpl implements NotaryWebService {
    @WebMethod
    public String test(String input) {
        return input+" : executed at server side";
    }

    @WebMethod
    public List<String> intentionToSell(String owner, String goodId, String secret){
        Notary notary = Notary.getInstance();
        return notary.intentionToSell(owner, goodId, secret);
    }

    @WebMethod
    public List<String> getStateOfGood(String goodId) {
        Notary notary = Notary.getInstance();
        return notary.getStateOfGood(goodId);
    }

    @WebMethod
    public List<String> transferGood(String sellerId, String buyerId, String goodId, String secret, String secret2){
        Notary notary = Notary.getInstance();
        return notary.transferGood(sellerId,buyerId,goodId, secret, secret2);
    }

    @WebMethod
    public int getNotaryId(){
        Notary notary = Notary.getInstance();
        return notary.getId();
    }

    @WebMethod
    public ArrayList<String> getUsers(){
        Notary notary = Notary.getInstance();
        return notary.getUsers();
    }

}