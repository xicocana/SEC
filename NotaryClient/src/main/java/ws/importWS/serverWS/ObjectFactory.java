
package ws.importWS.serverWS;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ws.importWS.serverWS package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetStateOfGood_QNAME = new QName("http://Interfaces.ws/", "getStateOfGood");
    private final static QName _Exception_QNAME = new QName("http://Interfaces.ws/", "Exception");
    private final static QName _TransferGood_QNAME = new QName("http://Interfaces.ws/", "transferGood");
    private final static QName _IntentionToSell_QNAME = new QName("http://Interfaces.ws/", "intentionToSell");
    private final static QName _TransferGoodResponse_QNAME = new QName("http://Interfaces.ws/", "transferGoodResponse");
    private final static QName _GetStateOfGoodResponse_QNAME = new QName("http://Interfaces.ws/", "getStateOfGoodResponse");
    private final static QName _IntentionToSellResponse_QNAME = new QName("http://Interfaces.ws/", "intentionToSellResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ws.importWS.serverWS
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link IntentionToSell }
     * 
     */
    public IntentionToSell createIntentionToSell() {
        return new IntentionToSell();
    }

    /**
     * Create an instance of {@link TransferGoodResponse }
     * 
     */
    public TransferGoodResponse createTransferGoodResponse() {
        return new TransferGoodResponse();
    }

    /**
     * Create an instance of {@link GetStateOfGoodResponse }
     * 
     */
    public GetStateOfGoodResponse createGetStateOfGoodResponse() {
        return new GetStateOfGoodResponse();
    }

    /**
     * Create an instance of {@link IntentionToSellResponse }
     * 
     */
    public IntentionToSellResponse createIntentionToSellResponse() {
        return new IntentionToSellResponse();
    }

    /**
     * Create an instance of {@link GetStateOfGood }
     * 
     */
    public GetStateOfGood createGetStateOfGood() {
        return new GetStateOfGood();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link TransferGood }
     * 
     */
    public TransferGood createTransferGood() {
        return new TransferGood();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStateOfGood }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Interfaces.ws/", name = "getStateOfGood")
    public JAXBElement<GetStateOfGood> createGetStateOfGood(GetStateOfGood value) {
        return new JAXBElement<GetStateOfGood>(_GetStateOfGood_QNAME, GetStateOfGood.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Interfaces.ws/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferGood }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Interfaces.ws/", name = "transferGood")
    public JAXBElement<TransferGood> createTransferGood(TransferGood value) {
        return new JAXBElement<TransferGood>(_TransferGood_QNAME, TransferGood.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IntentionToSell }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Interfaces.ws/", name = "intentionToSell")
    public JAXBElement<IntentionToSell> createIntentionToSell(IntentionToSell value) {
        return new JAXBElement<IntentionToSell>(_IntentionToSell_QNAME, IntentionToSell.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferGoodResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Interfaces.ws/", name = "transferGoodResponse")
    public JAXBElement<TransferGoodResponse> createTransferGoodResponse(TransferGoodResponse value) {
        return new JAXBElement<TransferGoodResponse>(_TransferGoodResponse_QNAME, TransferGoodResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStateOfGoodResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Interfaces.ws/", name = "getStateOfGoodResponse")
    public JAXBElement<GetStateOfGoodResponse> createGetStateOfGoodResponse(GetStateOfGoodResponse value) {
        return new JAXBElement<GetStateOfGoodResponse>(_GetStateOfGoodResponse_QNAME, GetStateOfGoodResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IntentionToSellResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://Interfaces.ws/", name = "intentionToSellResponse")
    public JAXBElement<IntentionToSellResponse> createIntentionToSellResponse(IntentionToSellResponse value) {
        return new JAXBElement<IntentionToSellResponse>(_IntentionToSellResponse_QNAME, IntentionToSellResponse.class, null, value);
    }

}
