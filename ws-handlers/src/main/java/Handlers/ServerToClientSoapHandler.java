package Handlers;

import org.w3c.dom.NodeList;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Set;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class ServerToClientSoapHandler implements SOAPHandler<SOAPMessageContext> {
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
        return SOAPProcessing(smc);
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

    private boolean SOAPProcessing(SOAPMessageContext smc) {

        Boolean isRequest = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (isRequest) {
            try {
                
                SOAPMessage soapMessage;
                soapMessage = smc.getMessage();
                SOAPPart soapPart = soapMessage.getSOAPPart();
                SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
                SOAPBody soapBody = soapEnvelope.getBody();

                NodeList nodeList = soapBody.getElementsByTagName("arg1");
                Node node_status = (Node) nodeList.item(0);
                String status = node_status.getTextContent();
                if(status.equals("true")){
                node_status.setTextContent("false");
                }
                else{
                    node_status.setTextContent("true");
                }
               
                soapMessage.saveChanges();

                //prints the soap message
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                soapMessage.writeTo(out);
                System.out.println(new String(out.toByteArray()));

            } catch (Exception e) {
                System.err.println("Caught exception on Server to Client SOAP Handler: " + e);
                e.printStackTrace();
            }
        }
        return true;
    }
}
