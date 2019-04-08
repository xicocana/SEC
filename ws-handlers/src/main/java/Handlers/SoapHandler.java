package Handlers;


import java.io.PrintStream;
import java.util.Date;
import java.util.Set;
import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

    static int i = 0;

    private boolean SOAPProcessing(SOAPMessageContext smc, PrintStream out) {

        Boolean isRequest = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);


        if (isRequest) {
            try {
                System.out.println();
                System.out.println("Entrou no Client SIDE : " + new Date().getTime());
                System.out.println();

                SOAPMessage soapMessage;
                soapMessage = smc.getMessage();
                SOAPPart soapPart = soapMessage.getSOAPPart();
                SOAPEnvelope soapEnvelope = soapPart.getEnvelope();

                SOAPHeader soapHeader = soapEnvelope.getHeader();
                SOAPHeaderElement headerElement = soapHeader.addHeaderElement(soapEnvelope.createName(
                        "Signature", "SOAP-SEC", "http://schemas.xmlsoap.org/soap/security/2000-12"));

                SOAPBody soapBody = soapEnvelope.getBody();
                soapBody.addAttribute(soapEnvelope.createName("id", "SOAP-SEC",
                        "http://schemas.xmlsoap.org/soap/security/2000-12"), "Body");
                Name bodyName = soapEnvelope.createName("FooBar", "z", "http://example.com");
                SOAPBodyElement gltp = soapBody.addBodyElement(bodyName);


            } catch (Exception e) {
                System.err.println("Caught exception on SOAPHandler OUTBOUND : " + e);
                e.printStackTrace();
            }

            return true;

        } else {

            try {
                System.out.println();
                System.out.println("Entrou no Server SIDE : " + new Date().getTime());
                System.out.println();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return true;
        }

    }
}
