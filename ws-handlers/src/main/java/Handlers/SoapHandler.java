package Handlers;


import java.io.PrintStream;
import java.util.Collections;
import java.util.Set;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
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
import java.security.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class SoapHandler implements SOAPHandler<SOAPMessageContext> {
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
        SOAPMessage msg;
        SOAPEnvelope se;
        SOAPPart sp = null;
        SOAPHeader header = null;
        SOAPBody body;
        if (header == null) {
            try {
                //get SOAP envelope
                msg = smc.getMessage();
                sp = msg.getSOAPPart();
                se = sp.getEnvelope();
                header = se.getHeader();
                body = se.getBody();
            } catch (SOAPException e) {
                System.err.println("Caught exception Getting SoapEnvelop : " + e);
            }
        }

        if (isRequest) {
            //CLIENT SIDE

            System.out.println("Entrou no Client SIDE");
            //SERVER SIDE
            try {



            } catch (Exception e) {
                System.err.println("Caught exception on SOAPHandler INBOUND : " + e);
            }

            return true;

        } else {


            try {

                Source source = sp.getContent();
                Node root = null;
                if (source instanceof DOMSource) {
                    root = ((DOMSource) source).getNode();
                } else if (source instanceof SAXSource) {
                    InputSource inSource = ((SAXSource) source).getInputSource();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setNamespaceAware(true);
                    DocumentBuilder db;

                    db = dbf.newDocumentBuilder();

                    Document doc = db.parse(inSource);
                    root = doc.getDocumentElement();
                }

                //keys generation
                String keysPath = "/../../NotaryClient/src/main/resources/keys/"+"user0";
                String currentDir = System.getProperty("user.dir");

                String pubPath = currentDir + keysPath+"/pub.key";
                String privPath = currentDir + keysPath+"/priv.key";

                PublicKey publicKey = (PublicKey) RSAKeyGenerator.read(pubPath);
                PrivateKey privateKey = (PrivateKey) RSAKeyGenerator.read(privPath);

                XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance();
                Reference ref = sigFactory.newReference("#Body", sigFactory.newDigestMethod(DigestMethod.SHA1, null));
                SignedInfo signedInfo = sigFactory.newSignedInfo(sigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,
                        (C14NMethodParameterSpec) null), sigFactory.newSignatureMethod(SignatureMethod.DSA_SHA1, null), Collections.singletonList(ref));
                KeyInfoFactory kif = sigFactory.getKeyInfoFactory();
                KeyValue kv = kif.newKeyValue(publicKey);
                KeyInfo keyInfo = kif.newKeyInfo(Collections.singletonList(kv));

                XMLSignature sig = sigFactory.newXMLSignature(signedInfo, keyInfo);

                System.out.println("Signing the message...");

                Element envelope = getFirstChildElement(root);
                Element headerElem = getFirstChildElement(envelope);

                DOMSignContext sigContext = new DOMSignContext(privateKey, headerElem);
                sigContext.putNamespacePrefix(XMLSignature.XMLNS, "ds");
                sigContext.setIdAttributeNS(getNextSiblingElement(headerElem), "http://schemas.xmlsoap.org/soap/security/2000-12", "id");
                sig.sign(sigContext);

                dumpDocument(root);

            } catch (Exception e) {
                System.err.println("Caught exception on SOAPHandler OUTBOUND : " + e);
            }

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
