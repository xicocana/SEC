package ws.Implementacao;

import domain.Notary;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;


@WebService(endpointInterface = "ws.Interfaces.NotaryWebService")
public class NotaryWebServiceImpl {
    @WebMethod
    public String test(String input) {
        return input+" : executed at server side";
    }

    @WebMethod
    public boolean intentionToSell(String owner, String goodId){
        Notary notary = Notary.getInstance();
        return notary.intentionToSell(owner, goodId);
    }

    @WebMethod
    public String getStateOfGood(String goodId) {
        Notary notary = Notary.getInstance();
        return notary.getStateOfGood(goodId);
    }

    @WebMethod
    public boolean transferGood(String sellerId, String buyerId, String goodId){
        Notary notary = Notary.getInstance();
        return notary.transferGood(sellerId,buyerId,goodId);
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