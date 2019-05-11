package ws.Interfaces;


import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService
public interface NotaryWebService {

    @WebMethod
    List<String> intentionToSell(List<String> args) throws Exception;

    @WebMethod
    List<String> getStateOfGood(List<String> args) throws Exception;

    @WebMethod
    List<String> transferGood(List<String> args) throws Exception;

}
