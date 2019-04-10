package ws.Interfaces;


import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ClientWebServices {


    @WebMethod
    List<String> buyGood(List<String> args) ;
}

