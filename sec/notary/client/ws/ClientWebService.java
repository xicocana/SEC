
package sec.notary.client.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "ClientWebService", targetNamespace = "http://impl.client.notary.sec/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface ClientWebService {


    /**
     * 
     * @param arg2
     * @param arg1
     * @param arg0
     * @return
     *     returns java.lang.Boolean
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "buyGood", targetNamespace = "http://impl.client.notary.sec/", className = "sec.notary.client.ws.BuyGood")
    @ResponseWrapper(localName = "buyGoodResponse", targetNamespace = "http://impl.client.notary.sec/", className = "sec.notary.client.ws.BuyGoodResponse")
    @Action(input = "http://impl.client.notary.sec/ClientWebService/buyGoodRequest", output = "http://impl.client.notary.sec/ClientWebService/buyGoodResponse")
    public Boolean buyGood(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        String arg1,
        @WebParam(name = "arg2", targetNamespace = "")
        String arg2);

    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "test", targetNamespace = "http://impl.client.notary.sec/", className = "sec.notary.client.ws.Test")
    @ResponseWrapper(localName = "testResponse", targetNamespace = "http://impl.client.notary.sec/", className = "sec.notary.client.ws.TestResponse")
    @Action(input = "http://impl.client.notary.sec/ClientWebService/testRequest", output = "http://impl.client.notary.sec/ClientWebService/testResponse")
    public String test(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

}
