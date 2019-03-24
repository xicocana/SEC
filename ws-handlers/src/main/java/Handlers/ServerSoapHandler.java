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
import java.security.*;
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

    private boolean validateSignature(Node signatureNode, Node bodyTag, PublicKey publicKey) {
        boolean signatureIsValid = false;
        try {
            // Create a DOM XMLSignatureFactory that will be used to unmarshal the
            // document containing the XMLSignature
            String providerName = System.getProperty
                    ("jsr105Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
            XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM",
                    (Provider) Class.forName(providerName).newInstance());

            // Create a DOMValidateContext and specify a KeyValue KeySelector
            // and document context
            DOMValidateContext valContext = new DOMValidateContext(publicKey, signatureNode);
            valContext.setIdAttributeNS((Element) bodyTag, "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Id");

            // Unmarshal the XMLSignature.
            XMLSignature signature = fac.unmarshalXMLSignature(valContext);
            // Validate the XMLSignature.
            signatureIsValid = signature.validate(valContext);

        } catch (Exception ex) {

        }

        return signatureIsValid;
    }
}
