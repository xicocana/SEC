package Handlers;

import org.w3c.dom.NodeList;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Set;
import javax.xml.soap.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class ClientToServerSoapHandler implements SOAPHandler<SOAPMessageContext> {
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

    static int i = 0;

    private boolean SOAPProcessing(SOAPMessageContext smc) {

        Boolean isRequest = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (isRequest) {
            try {
                
                SOAPMessage soapMessage;
                soapMessage = smc.getMessage();
                SOAPPart soapPart = soapMessage.getSOAPPart();
                SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
                SOAPBody soapBody = soapEnvelope.getBody();

                NodeList nodeListIntentToSell = soapBody.getElementsByTagName("ns2:intentionToSell");
                NodeList nodeListGetStateOfGood = soapBody.getElementsByTagName("ns2:getStateOfGood");
                NodeList nodeListTransferGood = soapBody.getElementsByTagName("ns2:transferGood");

                //intentToSell
                if(nodeListIntentToSell != null){
                    NodeList nodeList = soapBody.getElementsByTagName("arg0");
                    Node node_user = (Node) nodeList.item(0);
                    String userId = node_user.getTextContent();
                    node_user.setTextContent(userId + "1");
                }
                //getStateOfGood
                if(nodeListGetStateOfGood != null){
                    NodeList nodeList = soapBody.getElementsByTagName("arg0");
                    Node node_good = (Node) nodeList.item(0);
                    String goodId = node_good.getTextContent();
                    node_good.setTextContent(goodId + "1");
                }
                //transferGood
                if(nodeListTransferGood != null){
                    NodeList nodeList = soapBody.getElementsByTagName("arg0");
                    Node node_seller = (Node) nodeList.item(0);
                    String sellerId = node_seller.getTextContent();

                    NodeList nodeList2 = soapBody.getElementsByTagName("arg1");
                    Node node_buyer = (Node) nodeList2.item(0);
                    String buyerId = node_buyer.getTextContent();

                    NodeList nodeList3 = soapBody.getElementsByTagName("arg2");
                    Node node_good = (Node) nodeList3.item(0);
                    String goodId = node_good.getTextContent();

                    node_seller.setTextContent(sellerId + "1");
                    node_buyer.setTextContent(buyerId + "1");
                    node_good.setTextContent(goodId + "1");
                }

                soapMessage.saveChanges();


                //prints the soap message
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                soapMessage.writeTo(out);
                System.out.println(new String(out.toByteArray()));

            } catch (Exception e) {
                System.err.println("Caught exception on Client to Server SOAP Handler: " + e);
                e.printStackTrace();
            }
        }
        return true;
    }
}
