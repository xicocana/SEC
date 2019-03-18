package ws.Interfaces;


import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ClientWebServices {

    @WebMethod
    public String test(String input) ;

    @WebMethod
    public Boolean buyGood(String sellerId, String buyerId, String goodId) ;
}

