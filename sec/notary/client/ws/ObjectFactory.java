
package sec.notary.client.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sec.notary.client.ws package. 
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

    private final static QName _IntentionToSellResponse_QNAME = new QName("http://ws.server.notary.sec/", "intentionToSellResponse");
    private final static QName _Test_QNAME = new QName("http://ws.server.notary.sec/", "test");
    private final static QName _IntentionToSell_QNAME = new QName("http://ws.server.notary.sec/", "intentionToSell");
    private final static QName _TransferGoodResponse_QNAME = new QName("http://ws.server.notary.sec/", "transferGoodResponse");
    private final static QName _GetStateOfGoodResponse_QNAME = new QName("http://ws.server.notary.sec/", "getStateOfGoodResponse");
    private final static QName _GetStateOfGood_QNAME = new QName("http://ws.server.notary.sec/", "getStateOfGood");
    private final static QName _TransferGood_QNAME = new QName("http://ws.server.notary.sec/", "transferGood");
    private final static QName _TestResponse_QNAME = new QName("http://ws.server.notary.sec/", "testResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sec.notary.client.ws
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
     * Create an instance of {@link Test }
     * 
     */
    public Test createTest() {
        return new Test();
    }

    /**
     * Create an instance of {@link IntentionToSellResponse }
     * 
     */
    public IntentionToSellResponse createIntentionToSellResponse() {
        return new IntentionToSellResponse();
    }

    /**
     * Create an instance of {@link TestResponse }
     * 
     */
    public TestResponse createTestResponse() {
        return new TestResponse();
    }

    /**
     * Create an instance of {@link GetStateOfGood }
     * 
     */
    public GetStateOfGood createGetStateOfGood() {
        return new GetStateOfGood();
    }

    /**
     * Create an instance of {@link TransferGood }
     * 
     */
    public TransferGood createTransferGood() {
        return new TransferGood();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IntentionToSellResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "intentionToSellResponse")
    public JAXBElement<IntentionToSellResponse> createIntentionToSellResponse(IntentionToSellResponse value) {
        return new JAXBElement<IntentionToSellResponse>(_IntentionToSellResponse_QNAME, IntentionToSellResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Test }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "test")
    public JAXBElement<Test> createTest(Test value) {
        return new JAXBElement<Test>(_Test_QNAME, Test.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IntentionToSell }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "intentionToSell")
    public JAXBElement<IntentionToSell> createIntentionToSell(IntentionToSell value) {
        return new JAXBElement<IntentionToSell>(_IntentionToSell_QNAME, IntentionToSell.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferGoodResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "transferGoodResponse")
    public JAXBElement<TransferGoodResponse> createTransferGoodResponse(TransferGoodResponse value) {
        return new JAXBElement<TransferGoodResponse>(_TransferGoodResponse_QNAME, TransferGoodResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStateOfGoodResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "getStateOfGoodResponse")
    public JAXBElement<GetStateOfGoodResponse> createGetStateOfGoodResponse(GetStateOfGoodResponse value) {
        return new JAXBElement<GetStateOfGoodResponse>(_GetStateOfGoodResponse_QNAME, GetStateOfGoodResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStateOfGood }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "getStateOfGood")
    public JAXBElement<GetStateOfGood> createGetStateOfGood(GetStateOfGood value) {
        return new JAXBElement<GetStateOfGood>(_GetStateOfGood_QNAME, GetStateOfGood.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferGood }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "transferGood")
    public JAXBElement<TransferGood> createTransferGood(TransferGood value) {
        return new JAXBElement<TransferGood>(_TransferGood_QNAME, TransferGood.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "testResponse")
    public JAXBElement<TestResponse> createTestResponse(TestResponse value) {
        return new JAXBElement<TestResponse>(_TestResponse_QNAME, TestResponse.class, null, value);
    }

}
