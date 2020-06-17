/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.myrentcs.sia;


import it.aessepi.utils.HibernateBridge;
import it.myrent.ee.db.MROldAccountingSia;
import it.myrent.ee.db.MROldContrattoNoleggio;
import it.myrent.ee.db.MROldImmediateTransaction;
import it.myrent.ee.db.MROldSetting;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.*;


/**
 *
 * @author rashmi
 */
public class InstantTransactions {

    public static String processRequest(Session sx,String idOrdine, String pan, String dataScad , String importo, String tContab, String email) {
        String numorder="false";
        try {

            MROldSetting setting = (MROldSetting) sx.get(MROldSetting.class, "isSiaTest");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String timestamp = dateFormat.format(new Date());
            timestamp = timestamp.replaceAll("\\s", "T");

            //calculating refreqnum
            SimpleDateFormat dateFormat2= new SimpleDateFormat("yyyyMMddmmss");
            String num1= dateFormat2.format(new Date());
            String num2=Long.toString((long)(Math.random() * 1000000000000000000L));
            String refreqnum=num1+num2;
                while(refreqnum.length() < 32){
                    refreqnum=refreqnum+"9";
                }
            if(email == null){
                email= "test@ext.europassistance.it";
            }
            idOrdine= num1 + idOrdine;
            Boolean isProduction=true;
            if (setting !=null && setting.getValue().equals("true"))
                isProduction=false;
            else
                isProduction=true;

             String xmlRequest ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
                                 "<BPWXmlRichiesta>"+
                                   "<Release>02</Release>"+
                                   "<Richiesta>"+
                                      "<Operazione>AUTORIZZAZIONEONLINE</Operazione>"+
                                      "<Timestamp>"+timestamp+"</Timestamp>"+
                                      "<MAC />"+
                                   "</Richiesta>"+
                                   "<Dati>"+
                                      "<RicAutorizzazioneOnline>"+
                                         "<TestataRichiesta>"+
                                            (isProduction? "<IDnegozio>129287321400007</IDnegozio>":"<IDnegozio>129281292811114</IDnegozio>")+
                                            "<IDoperatore>AF06358551</IDoperatore>"+
                                            "<ReqRefNum>"+refreqnum+"</ReqRefNum>"+
                                        "</TestataRichiesta>"+
                                         "<IdOrdine>"+idOrdine+"</IdOrdine>"+
                                         "<Pan>"+pan+"</Pan>"+
                                         "<CVV2 />"+
                                         "<DataScad>"+dataScad+"</DataScad>"+
                                         "<Importo>"+importo+"</Importo>"+
                                         "<Valuta>978</Valuta>"+
                                         "<Tcontab>"+tContab+"</Tcontab>"+
                                         "<CodiceCircuito>98</CodiceCircuito>"+
                                         "<EmailTit>"+email+"</EmailTit>"+
                                         "<Userid>AF06358551</Userid>"+
                                      "</RicAutorizzazioneOnline>"+
                                   "</Dati>"+
                                "</BPWXmlRichiesta>";

            String input = xmlRequest+(isProduction? "MYRENT9FAA7AAAD19011E1B4049CDB6088707G":"MYRENTTest3");
            //calculate MAC
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] digest = m.digest(input.getBytes());
            String hashtext = new BigInteger(1, digest).toString(16);
            hashtext=hashtext.toUpperCase();
            System.out.println(hashtext);

            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            System.setProperty("https.protocols", "TLSv1");
//delete
//             String url = "https://payments-test.europassistance.it/ws/ServiceChDenv.asmx";

            String url = null;
            if (setting !=null && setting.getValue().equals("true")) {
                url = "https://payments-test.europassistance.it/ws/ServiceChDenv.asmx";
            }
            else{
                 url  = "https://payments.europassistance.it/ws/ServiceChDenv.asmx";
            }
            SOAPMessage soapResponse = null;
            try{
                soapResponse = soapConnection.call(createSOAPRequest(hashtext,xmlRequest), url);
            }catch(Exception ex){
                System.setProperty("https.protocols", "TLSv1.2");
                soapResponse = soapConnection.call(createSOAPRequest(hashtext,xmlRequest), url);
            }
            System.out.print("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            soapResponse.writeTo(out);
            String responseString1 = new String(out.toByteArray());
            soapConnection.close();
             // parsing string to save data
           int index1 = responseString1.indexOf(";Autorizzazione");
           if(index1 != -1){
             String rentalAgreement = idOrdine.replaceAll(".*_", "");
             String timestampResponse = null;
             String esito = null;
             String idOrdineReturn = null;
             String panreturn = null;
             String datascad = null;
             String importoReturn = null;
             String tContabReturn = null;
             String codiceCircuitoReturn = null;
             String emailReturn = null;
             String userid = null;
             String tipoPag = null;
             String tAutor = null;
             String idTrans = null;
             String circuito = null;
             String numOrdine = null;
             String importoTrans = null;
             String importoAutor = null;
             String importoContab = null;
             String importoStornato = null;
             String esitoTrans = null;
             String timestampAutor = null;
             String numaut = null;
             String acqBin = null;
             String codiceEsercente = null;
             String stato = null;
             String mac = null;

               String responseString=responseString1.substring(index1);

               if (responseString1.indexOf(";Timestamp&gt;") != -1) {
                   timestampResponse = responseString1.substring(responseString1.indexOf(";Timestamp&gt;") + 14, responseString1.indexOf("&lt;/Timestamp"));
               }
               if (responseString1.indexOf(";Esito&gt;") != -1) {
                   esito = responseString1.substring(responseString1.indexOf(";Esito&gt;") + 10, responseString1.indexOf("&lt;/Esito"));
               }
               if (responseString1.indexOf(";IDordine&gt;") != -1) {
                   idOrdineReturn = responseString1.substring(responseString1.indexOf(";IDordine&gt;") + 13, responseString1.indexOf("&lt;/IDordine"));
               }
               if (responseString1.indexOf(";Pan&gt;") != -1) {
                   panreturn = responseString1.substring(responseString1.indexOf(";Pan&gt;") + 8, responseString1.indexOf("&lt;/Pan"));
               }
               if (responseString1.indexOf(";DataScad&gt;") != -1) {
                   datascad = responseString1.substring(responseString1.indexOf(";DataScad&gt;") + 13, responseString1.indexOf("&lt;/DataScad"));
               }
               if (responseString1.indexOf(";Importo&gt;") != -1) {
                   importoReturn = responseString1.substring(responseString1.indexOf(";Importo&gt;") + 12, responseString1.indexOf("&lt;/Importo"));
               }
               if (responseString1.indexOf(";Tcontab&gt;") != -1) {
                   tContabReturn = responseString1.substring(responseString1.indexOf(";Tcontab&gt;") + 12, responseString1.indexOf("&lt;/Tcontab"));
               }
               if (responseString1.indexOf(";CodiceCircuito&gt;") != -1) {
                   codiceCircuitoReturn = responseString1.substring(responseString1.indexOf(";CodiceCircuito&gt;") + 19, responseString1.indexOf("&lt;/CodiceCircuito"));
               }
               if (responseString1.indexOf(";EmailTit&gt;") != -1) {
                   emailReturn = responseString1.substring(responseString1.indexOf(";EmailTit&gt;") + 13, responseString1.indexOf("&lt;/EmailTit"));
               }
               if (responseString1.indexOf(";Userid&gt;") != -1) {
                   userid = responseString1.substring(responseString1.indexOf(";Userid&gt;") + 11, responseString1.indexOf("&lt;/Userid"));
               }
               if (responseString.indexOf(";Timestamp&gt;") != -1) {
                   timestampAutor = responseString.substring(responseString.indexOf(";Timestamp&gt;") + 14, responseString.indexOf("&lt;/Timestamp"));
               }
               if (responseString.indexOf(";TipoPag&gt;") != -1) {
                   tipoPag = responseString.substring(responseString.indexOf(";TipoPag&gt;") + 12, responseString.indexOf("&lt;/TipoPag"));
               }
               if (responseString.indexOf(";MAC&gt;") != -1) {
                   mac = responseString.substring(responseString.indexOf(";MAC&gt;") + 8, responseString.indexOf("&lt;/MAC"));
               }
               if (responseString.indexOf(";IDtrans&gt;") != -1) {
                   idTrans = responseString.substring(responseString.indexOf(";IDtrans&gt;") + 12, responseString.indexOf("&lt;/IDtrans"));
               }
               if (responseString.indexOf(";NumOrdine&gt;") != -1) {
                   numOrdine = responseString.substring(responseString.indexOf(";NumOrdine&gt;") + 14, responseString.indexOf("&lt;/NumOrdine"));
               }
               if (responseString.indexOf(";Tautor&gt;") != -1) {
                   tAutor = responseString.substring(responseString.indexOf(";Tautor&gt;") + 11, responseString.indexOf("&lt;/Tautor"));
               }
               if (responseString.indexOf(";Circuito&gt;") != -1) {
                   circuito = responseString.substring(responseString.indexOf(";Circuito&gt;") + 13, responseString.indexOf("&lt;/Circuito"));
               }
               if (responseString.indexOf(";ImportoAutor&gt;") != -1) {
                   importoAutor = responseString.substring(responseString.indexOf(";ImportoAutor&gt;") + 17, responseString.indexOf("&lt;/ImportoAutor"));
               }
               if (responseString.indexOf(";ImportoTrans&gt;") != -1) {
                   importoTrans = responseString.substring(responseString.indexOf(";ImportoTrans&gt;") + 17, responseString.indexOf("&lt;/ImportoTrans"));
               }
               if (responseString.indexOf(";ImportoContab&gt;") != -1) {
                   importoContab = responseString.substring(responseString.indexOf(";ImportoContab&gt;") + 18, responseString.indexOf("&lt;/ImportoContab"));
               }
               if (responseString.indexOf(";ImportoStornato&gt;") != -1) {
                   importoStornato = responseString.substring(responseString.indexOf(";ImportoStornato&gt;") + 20, responseString.indexOf("&lt;/ImportoStornato"));
               }
               if (responseString.indexOf(";EsitoTrans&gt;") != -1) {
                   esitoTrans = responseString.substring(responseString.indexOf(";EsitoTrans&gt;") + 15, responseString.indexOf("&lt;/EsitoTrans"));
               }
               if (responseString.indexOf(";NumAut&gt;") != -1) {
                   numaut = responseString.substring(responseString.indexOf(";NumAut&gt;") + 11, responseString.indexOf("&lt;/NumAut"));
               }
               if (responseString.indexOf(";AcqBIN&gt;") != -1) {
                   acqBin = responseString.substring(responseString.indexOf(";AcqBIN&gt;") + 11, responseString.indexOf("&lt;/AcqBIN"));
               }
               if (responseString.indexOf(";CodiceEsercente&gt;") != -1) {
                   codiceEsercente = responseString.substring(responseString.indexOf(";CodiceEsercente&gt;") + 20, responseString.indexOf("&lt;/CodiceEsercente"));
               }
//               if (responseString.indexOf(";Valuta&gt;") != -1) {
//                   valuta = responseString.substring(responseString.indexOf(";Valuta&gt;") + 11, responseString.indexOf("&lt;/Valuta"));
//               }
               if (responseString.indexOf(";Stato&gt;") != -1) {
                   stato = responseString.substring(responseString.indexOf(";Stato&gt;") + 10, responseString.indexOf("&lt;/Stato"));
               }
               if (responseString.indexOf(";MAC&gt;") != -1) {
                   mac = responseString.substring(responseString.indexOf(";MAC&gt;") + 8, responseString.indexOf("&lt;/MAC"));
               }

               MROldImmediateTransaction acc= new MROldImmediateTransaction();
               acc.setEsito(esito);
               acc.setTimestamp(timestampResponse);
               acc.setTimestampAutor(timestampAutor);
               acc.setAcqBin(acqBin);
               acc.setDatascad(datascad);
               acc.setPan(panreturn);
               acc.setIdOrdine(idOrdineReturn);
               acc.setCircuito(circuito);
               acc.setCodiceCircuito(codiceCircuitoReturn);
               acc.settContab(tContabReturn);
               acc.setCodiceEsercente(codiceEsercente);
               acc.setEsitoTrans(esitoTrans);
               acc.setImporto(importoReturn);
               acc.setImportoAutor(importoAutor);
               acc.setImportoContab(importoContab);
               acc.setImportoStornato(importoStornato);
               acc.setImportoTrans(importoTrans);
               acc.setNumOrdine(numOrdine);
               acc.setNumaut(numaut);
               acc.settAutor(tAutor);
               acc.setTipoPag(tipoPag);
               acc.setIdTrans(idTrans);
               acc.setImportoAutor(importoAutor);
               acc.setImportoContab(importoContab);
               acc.setImportoStornato(importoStornato);
               acc.setImportoTrans(importoTrans);
               acc.setEsito(esito);
               acc.setUserid(userid);
               acc.setStato(stato);
               acc.setMac(mac);
               acc.setEmail(emailReturn);
               acc.setRentalAgreementId(rentalAgreement);
               acc.setExistingPan(pan);

               sx.save(acc);
               sx.flush();
            return numorder;
            }
           return numorder;
        } catch (Exception ex) {
            Logger.getLogger(InstantTransactions.class.getName()).log(Level.SEVERE, null, ex);
            return numorder;
        }
    }

    public static SOAPMessage createSOAPRequest(String hashText,String xmlRequest) throws Exception{
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            String serverURI = "http://chdenv.org/";
            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("", serverURI);
            // SOAP Body
            SOAPBody soapBody = envelope.getBody();
            SOAPElement soapBodyElem = soapBody.addChildElement("EffettuaAutorizzazioneOnlineConAliasPAN", "");
            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("sottosistemaChiamante", "");
            soapBodyElem1.addTextNode("MYRENT");
            SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("firmaMd5", "");
            soapBodyElem2.addTextNode(hashText);
            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("xmlRequest", "");
            soapBodyElem3.addTextNode(xmlRequest);
            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", serverURI + "EffettuaAutorizzazioneOnlineConAliasPAN");
            soapMessage.saveChanges();
            /* Print the request message */
            System.out.print("Request SOAP Message:");
            soapMessage.writeTo(System.out);
            System.out.println();
            return soapMessage;

        }
}


