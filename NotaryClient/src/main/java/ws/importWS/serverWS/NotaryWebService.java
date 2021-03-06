
package ws.importWS.serverWS;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "NotaryWebService", targetNamespace = "http://Interfaces.ws/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface NotaryWebService {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "intentionToSell", targetNamespace = "http://Interfaces.ws/", className = "ws.importWS.serverWS.IntentionToSell")
    @ResponseWrapper(localName = "intentionToSellResponse", targetNamespace = "http://Interfaces.ws/", className = "ws.importWS.serverWS.IntentionToSellResponse")
    @Action(input = "http://Interfaces.ws/NotaryWebService/intentionToSellRequest", output = "http://Interfaces.ws/NotaryWebService/intentionToSellResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://Interfaces.ws/NotaryWebService/intentionToSell/Fault/Exception")
    })
    public List<String> intentionToSell(
        @WebParam(name = "arg0", targetNamespace = "")
        List<String> arg0)
        throws Exception_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "getStateOfGood", targetNamespace = "http://Interfaces.ws/", className = "ws.importWS.serverWS.GetStateOfGood")
    @ResponseWrapper(localName = "getStateOfGoodResponse", targetNamespace = "http://Interfaces.ws/", className = "ws.importWS.serverWS.GetStateOfGoodResponse")
    @Action(input = "http://Interfaces.ws/NotaryWebService/getStateOfGoodRequest", output = "http://Interfaces.ws/NotaryWebService/getStateOfGoodResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://Interfaces.ws/NotaryWebService/getStateOfGood/Fault/Exception")
    })
    public List<String> getStateOfGood(
        @WebParam(name = "arg0", targetNamespace = "")
        List<String> arg0)
        throws Exception_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns java.util.List<java.lang.String>
     * @throws Exception_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "transferGood", targetNamespace = "http://Interfaces.ws/", className = "ws.importWS.serverWS.TransferGood")
    @ResponseWrapper(localName = "transferGoodResponse", targetNamespace = "http://Interfaces.ws/", className = "ws.importWS.serverWS.TransferGoodResponse")
    @Action(input = "http://Interfaces.ws/NotaryWebService/transferGoodRequest", output = "http://Interfaces.ws/NotaryWebService/transferGoodResponse", fault = {
        @FaultAction(className = Exception_Exception.class, value = "http://Interfaces.ws/NotaryWebService/transferGood/Fault/Exception")
    })
    public List<String> transferGood(
        @WebParam(name = "arg0", targetNamespace = "")
        List<String> arg0)
        throws Exception_Exception
    ;

}
