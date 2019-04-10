package ws.Interfaces;


import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService
public interface NotaryWebService {

    @WebMethod
    List<String> intentionToSell(List<String> args);

    @WebMethod
    List<String> getStateOfGood(List<String> args) ;

    @WebMethod
    List<String> transferGood(List<String> args);

}
