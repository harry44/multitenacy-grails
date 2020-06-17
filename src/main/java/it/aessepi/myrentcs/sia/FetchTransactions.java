// accounting soap
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.myrentcs.sia;


import it.myrent.ee.db.MROldFetchTransaction;
import it.myrent.ee.db.MROldSetting;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.xml.soap.*;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author rashmi
 */
public class FetchTransactions {

    public static Boolean processRequest(Session sx,String numorder) {
        try {
            Transaction tx = null;
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

         //   Boolean isProduction=false;
            Boolean isProduction=true;
            if (setting !=null && setting.getValue().equals("true"))
                isProduction=false;
            else
                isProduction=true;

           String xmlRequest ="<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"+
                                 "<BPWXmlRichiesta>"+
                                          "<Release>02</Release>"+
                                          "<Richiesta>"+
                                            "<Operazione>SITUAZIONEORDINE</Operazione>"+
                                            "<Timestamp>"+timestamp+"</Timestamp>"+
                                            "<MAC/>"+
                                          "</Richiesta>"+
                                          "<Dati>"+
                                            "<RicSituazioneOrdine>"+
                                              "<NumOrdine>"+numorder+"</NumOrdine>"+
                                            "<TestataRichiesta>"+
                                                (isProduction? "<IDnegozio>129287321400007</IDnegozio>":"<IDnegozio>129281292811114</IDnegozio>")+
                                                "<Operatore>AF06358551</Operatore>"+// userID
                                                "<ReqRefNum>"+refreqnum+"</ReqRefNum>"+
                                              "</TestataRichiesta>"+
                                            "</RicSituazioneOrdine>"+
                                          "</Dati>"+
                                        "</BPWXmlRichiesta>";

            String input= xmlRequest+(isProduction? "MYRENT9FAA7AAAD19011E1B4049CDB6088707G":"MYRENTTest3");
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
 //            String url = "https://payments-test.europassistance.it/ws/ServiceChDenv.asmx";

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
               String rentalAgreement = numorder.replaceAll(".*_", "");
               String valuta = null;
               String timestampResponse = null;
               String oldTimestamp = null;
               String mac = null;
               String idTrans = null;
               String numOrdine = null;
               String tipoPag = null;
               String tAutor = null;
               String circuito = null;
               String importoAutor = null;
               String importoTrans = null;
               String importoContab = null;
               String esitoTrans = null;
               String importoStornato = null;
               String numaut = null;
               String acqBIN = null;
               String codiceEsercente = null;
               String stato = null;

               String responseString=responseString1.substring(index1);

               if (responseString1.indexOf(";Timestamp&gt;") != -1) {
                   timestampResponse = responseString1.substring(responseString1.indexOf(";Timestamp&gt;") + 14, responseString1.indexOf("&lt;/Timestamp"));
               }
               if (responseString.indexOf(";Timestamp&gt;") != -1) {
                   oldTimestamp = responseString.substring(responseString.indexOf(";Timestamp&gt;") + 14, responseString.indexOf("&lt;/Timestamp"));
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
                   acqBIN = responseString.substring(responseString.indexOf(";AcqBIN&gt;") + 11, responseString.indexOf("&lt;/AcqBIN"));
               }
               if (responseString.indexOf(";CodiceEsercente&gt;") != -1) {
                   codiceEsercente = responseString.substring(responseString.indexOf(";CodiceEsercente&gt;") + 20, responseString.indexOf("&lt;/CodiceEsercente"));
               }
               if (responseString.indexOf(";Valuta&gt;") != -1) {
                   valuta = responseString.substring(responseString.indexOf(";Valuta&gt;") + 11, responseString.indexOf("&lt;/Valuta"));
               }
               if (responseString.indexOf(";Stato&gt;") != -1) {
                   stato = responseString.substring(responseString.indexOf(";Stato&gt;") + 10, responseString.indexOf("&lt;/Stato"));
               }
               if (responseString.indexOf(";MAC&gt;") != -1) {
                   mac = responseString.substring(responseString.indexOf(";MAC&gt;") + 8, responseString.indexOf("&lt;/MAC"));
               }


               MROldFetchTransaction fetch= new MROldFetchTransaction();
               fetch.setAcqbin(acqBIN);
               fetch.setCircuito(circuito);
               fetch.setCodiceEsercente(codiceEsercente);
               fetch.setEsitoTrans(esitoTrans);
               fetch.setImportoAutor(importoAutor);
               fetch.setImportoContab(importoContab);
               fetch.setImportoStornato(importoStornato);
               fetch.setImportoTrans(importoTrans);
               fetch.setNumOrdine(numOrdine);
               fetch.setNumaut(numaut);
               fetch.settAutor(tAutor);
               fetch.setTipopag(tipoPag);
               fetch.setValuta(valuta);
               fetch.setTimestamp(timestampResponse);
               fetch.setIdTrans(idTrans);
               fetch.setMac(mac);
               fetch.setStato(stato);
               fetch.setOldTimestamp(oldTimestamp);
               fetch.setRentalAgreementId(rentalAgreement);
               sx.saveOrUpdate(fetch);
               sx.flush();
               return true;
          }
           return false;
        } catch (Exception ex) {
            Logger.getLogger(MROldFetchTransaction.class.getName()).log(Level.SEVERE, null, ex);
            return false;
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
            SOAPElement soapBodyElem = soapBody.addChildElement("RichiestaSituazioneOrdine", "");
            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("sottosistemaChiamante", "");
            soapBodyElem1.addTextNode("MYRENT");
            SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("firmaMd5", "");
            soapBodyElem2.addTextNode(hashText);
            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("xmlRequest", "");
            soapBodyElem3.addTextNode(xmlRequest);
            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", serverURI + "RichiestaSituazioneOrdine");
            soapMessage.saveChanges();
            /* Print the request message */
            System.out.print("Request SOAP Message:");
            soapMessage.writeTo(System.out);
            System.out.println();
            return soapMessage;

        }
}


