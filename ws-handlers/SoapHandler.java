package ws-handler;

import java.io.PrintStream;
import java.io.StringWriter;
import java.security.Key;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.security.*;
import org.w3c.dom.Node;
import java.io.*;

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

    /**
     * Called at the conclusion of a message exchange pattern just prior to the
     * JAX-WS runtime dispatching a message, fault or exception.
     */
    public void close(MessageContext messageContext) {
    	// nothing to clean up
    }
    
    private boolean SOAPProcessing(SOAPMessageContext smc, PrintStream out){
   	 Boolean isRequest = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
   	 	if(isRequest){
   	 		//CLIENT SIDE
	   	 	try{
	            //get SOAP envelope
	            SOAPMessage msg = smc.getMessage();
	            SOAPPart sp = msg.getSOAPPart();
	            SOAPEnvelope se = sp.getEnvelope();
	            SOAPHeader header = se.getHeader();
	            SOAPBody body = se.getBody();
	            
	            if(header == null){
	                header = se.addHeader(); 
	            }
                
                Source source = sp.getContent();
                Node root = null;
                if (source instanceof DOMSource) {
                    root = ((DOMSource) source).getNode();
                } 
                else if (source instanceof SAXSource) {
                    InputSource inSource = ((SAXSource) source).getInputSource();
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setNamespaceAware(true);
                    DocumentBuilder db = null;

                    db = dbf.newDocumentBuilder();

                    Document doc = db.parse(inSource);
                    root = (Node) doc.getDocumentElement();
                }
                
                XMLSignatureFactory sigFactory = XMLSignatureFactory.getInstance();
                Reference ref = sigFactory.newReference("#Body", sigFactory.newDigestMethod(DigestMethod.SHA1,null));
                SignedInfo signedInfo = sigFactory.newSignedInfo(sigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, (C14NMethodParameterSpec) null), sigFactory.newSignatureMethod(SignatureMethod.DSA_SHA1, null), Collections.singletonList(ref));
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
                sigContext.setIdAttributeNS(getNextSiblingElement(header),"http://schemas.xmlsoap.org/soap/security/2000-12", "id");
                sig.sign(sigContext);

	   	 	}catch(Exception e){
    			System.err.println("Caught exception on SOAPHandler OUTBOUND : " + e);
            }

            return true;
	   	 	
   	 	}else {
   	 		//SERVER SIDE
	   	 	try{
	            //get SOAP envelope
	            SOAPMessage msg = smc.getMessage();
	            SOAPPart sp = msg.getSOAPPart();
	            SOAPEnvelope se = sp.getEnvelope();
	            SOAPHeader header = se.getHeader();
	            SOAPBody body = se.getBody();
	            
	            if(header == null){
	                header = se.addHeader(); 
	            }
	            
   	 		}catch(Exception e){
            	System.err.println("Caught exception on SOAPHandler INBOUND : " + e);
       	 	}
       	 	return true;
   	 	}
    }
	
	private String bodyConverter(Node node) throws Exception {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");        
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(node);
		transformer.transform(source, result);
		String xmlString = sw.toString();
		return xmlString;
		
	}
}