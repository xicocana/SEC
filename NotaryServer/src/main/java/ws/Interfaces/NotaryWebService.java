package ws.Interfaces;


import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;

@WebService
public interface NotaryWebService {
    @WebMethod public String test(String input);

    @WebMethod public boolean intentionToSell(String owner, String goodId,String secret);

    @WebMethod public String getStateOfGood(String goodId) ;

    @WebMethod  public boolean transferGood(String sellerId, String buyerId, String goodId,String secret);

    @WebMethod public int getNotaryId();

    @WebMethod public ArrayList<String> getUsers();
}
