package ws.Interfaces;


import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService
public interface NotaryWebService {
    @WebMethod
    String test(String input);

    @WebMethod
    List<String> intentionToSell(String owner, String goodId, String secret);

    @WebMethod
    List<String> getStateOfGood(String goodId) ;

    @WebMethod
    List<String> transferGood(String sellerId, String buyerId, String goodId, String secret, String secret2);

    @WebMethod
    int getNotaryId();

    @WebMethod
    ArrayList<String> getUsers();
}
