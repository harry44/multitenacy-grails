/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.nsa;
import it.myrent.ee.db.MROldSetting;
import java.io.ByteArrayOutputStream;
import javax.xml.soap.*;
import org.hibernate.Session;

/**
 * Created by Shivangani on 10/6/2017.
 */
public class SearchNsa {

    Session sx=null;
    public SearchNsa(Session sx){
        this.sx = sx;
    }
    public final static String TEST = "http://172.20.242.38:8080/Myrentnsa.asmx";
    public final static String PRODUCTION = "http://MINGRFWS01:8080/MyRentNSA.asmx";
    

    public String processRequest(String partitaIva,String codFiscale){
        SOAPMessage soapResponse=null;
        String response = null;
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            // Send SOAP Message to SOAP Server
            String url = "";
            MROldSetting setting = (MROldSetting) sx.get(MROldSetting.class, "isSiaTest");
            Boolean isSiaTest;
            if (setting !=null && setting.getValue().equals("true")){
                url = TEST;
            }                            
            else{
                url = PRODUCTION;                        
            }
//            if(sx != null && sx.isOpen()){
//                sx.close();
//            }
            soapResponse = soapConnection.call(createSOAPRequest(partitaIva,codFiscale), url);
            // print SOAP Response
//            System.out.print("Response SEARCH SOAP Message:");
//            soapResponse.writeTo(System.out);
            soapConnection.close();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapResponse.writeTo(out);
            response = new String(out.toByteArray());
            return response;
        } catch (Exception ex) {
            ex.getLocalizedMessage();
            return response;
        }
    }

     public static SOAPMessage createSOAPRequest(String partitaIva,String codFiscale) throws Exception {
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();

             String serverURI = "http://myrentnsa.europassistance.it/";

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("", serverURI);

            // SOAP Body
            SOAPBody soapBody = envelope.getBody();
            SOAPElement soapBodyElem = soapBody.addChildElement("VerificaEsistenzaNsa", "");
            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("infoNSA", "");
            SOAPElement soapBodyElem2 = soapBodyElem1.addChildElement("in_partita_iva", "");
            if(partitaIva != null){
                 soapBodyElem2.addTextNode(partitaIva);
            }else{
                 soapBodyElem2.addTextNode("");
            }
            SOAPElement soapBodyElem3 = soapBodyElem1.addChildElement("in_cod_fiscale", "");
            if(codFiscale != null){
                 soapBodyElem3.addTextNode(codFiscale);
            }else{
                 soapBodyElem3.addTextNode("");
            }

            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", serverURI  + "VerificaEsistenzaNsa");

            soapMessage.saveChanges();

            /* Print the request message */
//            System.out.print("Request SEARCH SOAP Message:");
//            soapMessage.writeTo(System.out);
//            System.out.println();

            return soapMessage;
        }

}


