
package sec.notary.client.resources;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sec.notary.client.resources package. 
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
    private final static QName _GetUsers_QNAME = new QName("http://ws.server.notary.sec/", "getUsers");
    private final static QName _IntentionToSell_QNAME = new QName("http://ws.server.notary.sec/", "intentionToSell");
    private final static QName _TransferGoodResponse_QNAME = new QName("http://ws.server.notary.sec/", "transferGoodResponse");
    private final static QName _GetStateOfGoodResponse_QNAME = new QName("http://ws.server.notary.sec/", "getStateOfGoodResponse");
    private final static QName _GetStateOfGood_QNAME = new QName("http://ws.server.notary.sec/", "getStateOfGood");
    private final static QName _GetUsersResponse_QNAME = new QName("http://ws.server.notary.sec/", "getUsersResponse");
    private final static QName _TransferGood_QNAME = new QName("http://ws.server.notary.sec/", "transferGood");
    private final static QName _GetNotaryIdResponse_QNAME = new QName("http://ws.server.notary.sec/", "getNotaryIdResponse");
    private final static QName _TestResponse_QNAME = new QName("http://ws.server.notary.sec/", "testResponse");
    private final static QName _GetNotaryId_QNAME = new QName("http://ws.server.notary.sec/", "getNotaryId");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sec.notary.client.resources
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
     * Create an instance of {@link GetUsers }
     * 
     */
    public GetUsers createGetUsers() {
        return new GetUsers();
    }

    /**
     * Create an instance of {@link GetNotaryIdResponse }
     * 
     */
    public GetNotaryIdResponse createGetNotaryIdResponse() {
        return new GetNotaryIdResponse();
    }

    /**
     * Create an instance of {@link TestResponse }
     * 
     */
    public TestResponse createTestResponse() {
        return new TestResponse();
    }

    /**
     * Create an instance of {@link GetNotaryId }
     * 
     */
    public GetNotaryId createGetNotaryId() {
        return new GetNotaryId();
    }

    /**
     * Create an instance of {@link GetStateOfGood }
     * 
     */
    public GetStateOfGood createGetStateOfGood() {
        return new GetStateOfGood();
    }

    /**
     * Create an instance of {@link GetUsersResponse }
     * 
     */
    public GetUsersResponse createGetUsersResponse() {
        return new GetUsersResponse();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUsers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "getUsers")
    public JAXBElement<GetUsers> createGetUsers(GetUsers value) {
        return new JAXBElement<GetUsers>(_GetUsers_QNAME, GetUsers.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUsersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "getUsersResponse")
    public JAXBElement<GetUsersResponse> createGetUsersResponse(GetUsersResponse value) {
        return new JAXBElement<GetUsersResponse>(_GetUsersResponse_QNAME, GetUsersResponse.class, null, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNotaryIdResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "getNotaryIdResponse")
    public JAXBElement<GetNotaryIdResponse> createGetNotaryIdResponse(GetNotaryIdResponse value) {
        return new JAXBElement<GetNotaryIdResponse>(_GetNotaryIdResponse_QNAME, GetNotaryIdResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "testResponse")
    public JAXBElement<TestResponse> createTestResponse(TestResponse value) {
        return new JAXBElement<TestResponse>(_TestResponse_QNAME, TestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetNotaryId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.server.notary.sec/", name = "getNotaryId")
    public JAXBElement<GetNotaryId> createGetNotaryId(GetNotaryId value) {
        return new JAXBElement<GetNotaryId>(_GetNotaryId_QNAME, GetNotaryId.class, null, value);
    }

}
