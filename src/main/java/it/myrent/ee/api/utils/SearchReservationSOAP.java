package it.myrent.ee.api.utils;

/**
 * Created by Madhvendra on 04/10/2017.
 */

import java.io.ByteArrayOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;


public class SearchReservationSOAP {
    public final static String TEST = "http://172.20.242.9:8040/wsnco2/service1.asmx";
    public final static String PRODUCTION = "http://nco2lb.milano.ea.it:8040/wsnco2/service1.asmx";

    public String processRequest(String numeroOrdine, Boolean isSiaTest) {
        SOAPMessage soapResponse = null;
        String response = null;
        try {
            System.setProperty("java.net.useSystemProxies", "true");
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            String url = TEST;
            if (!isSiaTest) {
                url = PRODUCTION;
            }
            soapResponse = soapConnection.call(createSOAPRequest(numeroOrdine), url);
            soapConnection.close();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapResponse.writeTo(out);
            response = new String(out.toByteArray());
            return response;
        } catch (Exception ex) {
            ex.getLocalizedMessage();
            Logger.getLogger(SearchReservationSOAP.class.getName()).log(Level.SEVERE, null, ex);
            return response;
        }
    }

    public static SOAPMessage createSOAPRequest(String numeroOrdine) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
//            SOAPMessage soapMessage = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String serverURI = "http://www.w3.org/2001/XMLSchema";
        String serverURI2 = "http://www.w3.org/2001/XMLSchema-instance";
        String exExitserver = "http://nco2web/nco2ws/";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.setPrefix("soap");
        envelope.removeNamespaceDeclaration("SOAP-ENV");

        //remove empty header
        soapMessage.getSOAPHeader().detachNode();

        soapMessage.getSOAPBody().setPrefix("soap");


        envelope.addNamespaceDeclaration("xsd", serverURI);
        envelope.addNamespaceDeclaration("xsi", serverURI2);


        // SOAP Body
        String element = "<parameters><numeroOrdine>" + numeroOrdine + "</numeroOrdine></parameters>";

        SOAPBody soapBody = envelope.getBody();
        SOAPElement executeWithExit = soapBody.addChildElement("executeWithExit", "", exExitserver);
        SOAPElement soapBodyElem1 = executeWithExit.addChildElement("businessObjectName", "");
        SOAPElement soapBodyElem2 = executeWithExit.addChildElement("methodName", "");
        SOAPElement soapBodyElem3 = executeWithExit.addChildElement("xmlParametersInput", "");
        SOAPElement soapBodyElem4 = executeWithExit.addChildElement("xmlCustomDataInput", "");

        soapBodyElem1.addTextNode("csEstensioneIncaricoAS");
        soapBodyElem2.addTextNode("retrievePrimaErogPerMercurio");
        if (numeroOrdine != null) {
            soapBodyElem3.addTextNode(element);
        } else {
            soapBodyElem3.addTextNode("");
        }
        soapBodyElem4.addTextNode("string");


        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", "http://nco2web/nco2ws/executeWithExit");
//
        soapMessage.saveChanges();
//
            /* Print the request message */
//            System.out.print("Request SOAP Message:");
//            soapMessage.writeTo(System.out);
//            System.out.println();

        return soapMessage;
    }

}
