
package clientWS;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the clientWS package. 
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

    private final static QName _BuyGood_QNAME = new QName("http://impl.ws/", "buyGood");
    private final static QName _Test_QNAME = new QName("http://impl.ws/", "test");
    private final static QName _BuyGoodResponse_QNAME = new QName("http://impl.ws/", "buyGoodResponse");
    private final static QName _TestResponse_QNAME = new QName("http://impl.ws/", "testResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: clientWS
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Test }
     * 
     */
    public Test createTest() {
        return new Test();
    }

    /**
     * Create an instance of {@link BuyGood }
     * 
     */
    public BuyGood createBuyGood() {
        return new BuyGood();
    }

    /**
     * Create an instance of {@link TestResponse }
     * 
     */
    public TestResponse createTestResponse() {
        return new TestResponse();
    }

    /**
     * Create an instance of {@link BuyGoodResponse }
     * 
     */
    public BuyGoodResponse createBuyGoodResponse() {
        return new BuyGoodResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuyGood }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.ws/", name = "buyGood")
    public JAXBElement<BuyGood> createBuyGood(BuyGood value) {
        return new JAXBElement<BuyGood>(_BuyGood_QNAME, BuyGood.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Test }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.ws/", name = "test")
    public JAXBElement<Test> createTest(Test value) {
        return new JAXBElement<Test>(_Test_QNAME, Test.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BuyGoodResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.ws/", name = "buyGoodResponse")
    public JAXBElement<BuyGoodResponse> createBuyGoodResponse(BuyGoodResponse value) {
        return new JAXBElement<BuyGoodResponse>(_BuyGoodResponse_QNAME, BuyGoodResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.ws/", name = "testResponse")
    public JAXBElement<TestResponse> createTestResponse(TestResponse value) {
        return new JAXBElement<TestResponse>(_TestResponse_QNAME, TestResponse.class, null, value);
    }

}
