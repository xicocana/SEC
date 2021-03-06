package ws.Implementacao;

import domain.Notary;
import ws.Interfaces.NotaryWebService;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;


@WebService(endpointInterface = "ws.Interfaces.NotaryWebService")
@HandlerChain(file = "/ServerChain.xml")
public class NotaryWebServiceImpl implements NotaryWebService {

    @WebMethod
    public List<String> intentionToSell(List<String> args) throws Exception{
        Notary notary = Notary.getInstance();
        return notary.intentionToSell(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4).equals("ack"));
    }

    @WebMethod
    public List<String> getStateOfGood(List<String> args) throws Exception {
        Notary notary = Notary.getInstance();
        return notary.getStateOfGood(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4).equals("ack"));
    }

    @WebMethod
    public List<String> transferGood(List<String> args) throws Exception{
        Notary notary = Notary.getInstance();
        return notary.transferGood(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4), args.get(5),
                args.get(6), args.get(7).equals("ack"));
    }


}