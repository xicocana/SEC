package Handlers;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.PrintStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Set;

public class ServerSoapHandler implements SOAPHandler<SOAPMessageContext> {
    /**
     * Gets the names of the header blocks that can be processed by this Handler instance.
     * If null, processes all.
     */
    public Set getHeaders() {
        return null;
    }

    /**
     * The handleMessage method is invoked for normal processing of inbound and
     * outbound messages.
     */
    public boolean handleMessage(SOAPMessageContext smc) {
        return SOAPProcessing(smc, System.out);
    }

    @Override
    public boolean handleFault(SOAPMessageContext soapMessageContext) {
        return false;
    }

    /**
     * Called at the conclusion of a message exchange pattern just prior to the
     * JAX-WS runtime dispatching a message, fault or exception.
     */
    public void close(MessageContext messageContext) {
        // nothing to clean up
    }

    private boolean SOAPProcessing(SOAPMessageContext smc, PrintStream out) {

        Boolean isRequest = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (!isRequest) {
            try {

                //System.out.println("Entrou no Client SIDE");

                SOAPMessage soapMessage = smc.getMessage();
                SOAPPart soapPart = soapMessage.getSOAPPart();
                SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

                SOAPHeader soapHeader = soapEnvelope.getHeader();


                SOAPBody soapBody = soapEnvelope.getBody();
                soapBody.addAttribute(soapEnvelope.createName("id", "SOAP-SEC",
                        "http://schemas.xmlsoap.org/soap/security/2000-12"), "Body");
                Name bodyName = soapEnvelope.createName("FooBar", "z", "http://example.com");
                SOAPBodyElement gltp = soapBody.addBodyElement(bodyName);

                Source source = soapPart.getContent();
                Node root = null;
                if (source instanceof DOMSource) {
                    root = ((DOMSource) source).getNode();
                } else if (source instanceof SAXSource) {
                    InputSource inSource = ((SAXSource) source).getInputSource();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setNamespaceAware(true);
                    DocumentBuilder db = null;

                    db = dbf.newDocumentBuilder();

                    Document doc = db.parse(inSource);
                    root = (Node) doc.getDocumentElement();
                }

                //dumpDocument(root);

                KeyPair keypair = RSAKeyGenerator.getKeyPairFromKeyStore();

                XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance();
                Reference ref = sigFactory.newReference("#Body", sigFactory.newDigestMethod(DigestMethod.SHA1,
                        null));

                SignedInfo signedInfo = sigFactory.newSignedInfo(sigFactory.newCanonicalizationMethod(
                        CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null), sigFactory
                        .newSignatureMethod(SignatureMethod.RSA_SHA1, null), Collections.singletonList(ref));

                KeyInfoFactory kif = sigFactory.getKeyInfoFactory();
                KeyValue kv = kif.newKeyValue(keypair.getPublic());
                KeyInfo keyInfo = kif.newKeyInfo(Collections.singletonList(kv));

                XMLSignature sig = sigFactory.newXMLSignature(signedInfo, keyInfo);


                System.out.println("Signing the message...");
                PrivateKey privateKey = keypair.getPrivate();
                Element envelope = getFirstChildElement(root);
                Element header = getFirstChildElement(envelope);
                DOMSignContext sigContext = new DOMSignContext(privateKey, header);
                sigContext.putNamespacePrefix(XMLSignature.XMLNS, "ds");
                sigContext.setIdAttributeNS(getNextSiblingElement(header),
                        "http://schemas.xmlsoap.org/soap/security/2000-12", "id");
                sig.sign(sigContext);

                //dumpDocument(root);

                System.out.println("Validate the signature...");

                Element sigElement = getFirstChildElement(header);
                //System.out.println("111");

                DOMValidateContext valContext = new DOMValidateContext(keypair.getPublic(), sigElement);
                valContext.setIdAttributeNS(getNextSiblingElement(header),
                        "http://schemas.xmlsoap.org/soap/security/2000-12", "id");
                //System.out.println("1.5");
                //System.out.println("222" + valContext.getNode().toString());

                boolean valid = sig.validate(valContext);

                System.out.println("Signature valid? " + valid);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return true;

        } else {
            //System.out.println("Entrou no Server SIDE - SERVER_SOAP_HANDLER");
            String currentDir = System.getProperty("user.dir");
            System.out.println(currentDir);

            return true;
        }
    }

    private static void dumpDocument(Node root) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(root), new StreamResult(System.out));
    }

    private static Element getFirstChildElement(Node node) {
        Node child = node.getFirstChild();
        while ((child != null) && (child.getNodeType() != Node.ELEMENT_NODE)) {
            child = child.getNextSibling();
        }
        return (Element) child;
    }

    public static Element getNextSiblingElement(Node node) {
        Node sibling = node.getNextSibling();
        while ((sibling != null) && (sibling.getNodeType() != Node.ELEMENT_NODE)) {
            sibling = sibling.getNextSibling();
        }
        return (Element) sibling;
    }
}
