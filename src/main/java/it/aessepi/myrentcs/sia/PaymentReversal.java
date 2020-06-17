/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.myrentcs.sia;


import it.aessepi.utils.HibernateBridge;
import it.myrent.ee.db.MROldReversePayment;
import it.myrent.ee.db.MROldSetting;
import java.io.ByteArrayOutputStream;
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
public class PaymentReversal {

    public static Boolean processRequest(Session sx,String numOrder,String idTrans, String Importo, Integer warrantyId) {
        try {
            //Session sx = null;
            Transaction tx = null;
            String esitoReverse = null;
            sx = HibernateBridge.startNewSession();
            tx = sx.beginTransaction();
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

             Boolean isProduction=true;
            if (setting !=null && setting.getValue().equals("true"))
                isProduction=false;
            else
                isProduction=true;

            String xmlRequest ="<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>"+
                                    "<BPWXmlRichiesta>"+
                                          "<Release>02</Release>"+
                                          "<Richiesta>"+
                                            "<Operazione>STORNO</Operazione>"+
                                            "<Timestamp>"+timestamp+"</Timestamp>"+
                                          "</Richiesta>"+
                                          "<Dati>"+
                                            "<RicStorno>"+
                                              "<TestataRichiesta>"+
                                                (isProduction? "<IDnegozio>129287321400007</IDnegozio>":"<IDnegozio>129281292811114</IDnegozio>")+
                                                "<Operatore>AF06358551</Operatore>"+// userID
                                                "<ReqRefNum>"+refreqnum+"</ReqRefNum>"+
                                              "</TestataRichiesta>"+
                                              "<IDtrans>"+idTrans+"</IDtrans>"+
                                              "<NumOrdine>"+numOrder+"</NumOrdine>"+
                                              "<Importo>"+Importo+"</Importo>"+
                                              "<Valuta>978</Valuta>"+
                                            "</RicStorno>"+
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

           //deleted
//            String url = "https://payments-test.europassistance.it/ws/ServiceChDenv.asmx";
            String url = null;
            if (setting !=null && setting.getValue().equals("true")) {
                 url = "https://payments-test.europassistance.it/ws/ServiceChDenv.asmx";
            }
            else{
                url = "https://payments.europassistance.it/ws/ServiceChDenv.asmx";
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
            String responseString = new String(out.toByteArray());
            soapConnection.close();
            // parsing string to save data
           int index1 = responseString.indexOf(";OperazioneContabile");
           if(index1 != -1){
               String rentalAgreement = numOrder.replaceAll(".*_", "");
               String mains = responseString.substring(1, index1);
               String reverse = null ;
               String auth = null ;
               String logApiXml= null ;
               String timestampMain= null;
               String esitoMain= null;
               String macMain = null;
               String reqRefNum = null;
               String idTransMain = null;
               String numOrdine = null;
               String importo= null;
               String idTransReverse = null;
               String timestampRic = null;
               String timestampElab= null;
               String tipoOp = null;
               String statoReverse = null;
               String macReverse = null;
               String tipoPag = null;
               String tAutor = null;
               String circuito = null;
               String importoAutor = null;
               String importoTrans = null;
               String importoContab = null;
               String esitoTrans = null;
               String importoStornato = null;
               String timestampAuth = null;
               String numauth = null;
               String acqBIN = null;
               String codiceEsercente = null;
               String statoAuth = null;
               String macAuth = null;
               String idTransAuth = null;
               String firmaMD5 = null;
               String esitoRipristinoPlafond = null;

               if (responseString.indexOf(";Autorizzazione") != -1) {
                   reverse = responseString.substring(index1, responseString.indexOf(";Autorizzazione"));
                   auth = responseString.substring(responseString.indexOf(";Autorizzazione"));
               }
               if (mains.indexOf("<id_LogApiXml>") != -1) {
                   logApiXml = mains.substring(mains.indexOf("<id_LogApiXml>") + 14, mains.indexOf("</id_LogApiXml>"));
               }
               if (mains.indexOf(";Timestamp&gt;") != -1) {
                   timestampMain = mains.substring(mains.indexOf(";Timestamp&gt;") + 14, mains.indexOf("&lt;/Timestamp"));
               }
               if (mains.indexOf(";Esito&gt;") != -1) {
                   esitoMain = mains.substring(mains.indexOf(";Esito&gt;") + 10, mains.indexOf("&lt;/Esito"));
               }
               if (mains.indexOf(";MAC&gt;") != -1) {
                   macMain = mains.substring(mains.indexOf(";MAC&gt;") + 8, mains.indexOf("&lt;/MAC"));
               }
               if (mains.indexOf(";ReqRefNum&gt;") != -1) {
                   reqRefNum = mains.substring(mains.indexOf(";ReqRefNum&gt;") + 14, mains.indexOf("&lt;/ReqRefNum"));
               }
               if (mains.indexOf(";IDtrans&gt;") != -1) {
                   idTransMain = mains.substring(mains.indexOf(";IDtrans&gt;") + 12, mains.indexOf("&lt;/IDtrans"));
               }
               if (mains.indexOf(";NumOrdine&gt;") != -1) {
                   numOrdine = mains.substring(mains.indexOf(";NumOrdine&gt;") + 14, mains.indexOf("&lt;/NumOrdine"));
               }
               if (mains.indexOf(";Importo&gt;") != -1) {
                   importo = mains.substring(mains.indexOf(";Importo&gt;") + 12, mains.indexOf("&lt;/Importo"));
               }
               if (reverse.indexOf(";IDtrans&gt;") != -1) {
                   idTransReverse = reverse.substring(reverse.indexOf(";IDtrans&gt;") + 12, reverse.indexOf("&lt;/IDtrans"));
               }
               if (reverse.indexOf(";TimestampRic&gt;") != -1) {
                   timestampRic = reverse.substring(reverse.indexOf(";TimestampRic&gt;") + 17, reverse.indexOf("&lt;/TimestampRic"));
               }
               if (reverse.indexOf(";TimestampElab&gt;") != -1) {
                   timestampElab = reverse.substring(reverse.indexOf(";TimestampElab&gt;") + 18, reverse.indexOf("&lt;/TimestampElab"));
               }
               if (reverse.indexOf(";TipoOp&gt;") != -1) {
                   tipoOp = reverse.substring(reverse.indexOf(";TipoOp&gt;") + 11, reverse.indexOf("&lt;/TipoOp"));
               }
               if (reverse.indexOf(";Esito&gt;") != -1) {
                   esitoReverse = reverse.substring(reverse.indexOf(";Esito&gt;") + 10, reverse.indexOf("&lt;/Esito"));
               }
               if (reverse.indexOf(";Stato&gt;") != -1) {
                   statoReverse = reverse.substring(reverse.indexOf(";Stato&gt;") + 10, reverse.indexOf("&lt;/Stato"));
               }
               if (reverse.indexOf(";MAC&gt;") != -1) {
                   macReverse = reverse.substring(reverse.indexOf(";MAC&gt;") + 8, reverse.indexOf("&lt;/MAC"));
               }
               if (auth.indexOf(";TipoPag&gt;") != -1) {
                   tipoPag = auth.substring(auth.indexOf(";TipoPag&gt;") + 12, auth.indexOf("&lt;/TipoPag"));
               }
               if (auth.indexOf(";Tautor&gt;") != -1) {
                   tAutor = auth.substring(auth.indexOf(";Tautor&gt;") + 11, auth.indexOf("&lt;/Tautor"));
               }
               if (auth.indexOf(";IDtrans&gt;") != -1) {
                   idTransAuth = auth.substring(auth.indexOf(";IDtrans&gt;") + 12, auth.indexOf("&lt;/IDtrans"));
               }
               if (auth.indexOf(";Circuito&gt;") != -1) {
                   circuito = auth.substring(auth.indexOf(";Circuito&gt;") + 13, auth.indexOf("&lt;/Circuito"));
               }
               if (auth.indexOf(";ImportoAutor&gt;") != -1) {
                   importoAutor = auth.substring(auth.indexOf(";ImportoAutor&gt;") + 17, auth.indexOf("&lt;/ImportoAutor"));
               }
               if (auth.indexOf(";ImportoTrans&gt;") != -1) {
                   importoTrans = auth.substring(auth.indexOf(";ImportoTrans&gt;") + 17, auth.indexOf("&lt;/ImportoTrans"));
               }
               if (auth.indexOf(";ImportoContab&gt;") != -1) {
                   importoContab = auth.substring(auth.indexOf(";ImportoContab&gt;") + 18, auth.indexOf("&lt;/ImportoContab"));
               }
               if (auth.indexOf(";ImportoStornato&gt;") != -1) {
                   importoStornato = auth.substring(auth.indexOf(";ImportoStornato&gt;") + 20, auth.indexOf("&lt;/ImportoStornato"));
               }
               if (auth.indexOf(";EsitoTrans&gt;") != -1) {
                   esitoTrans = auth.substring(auth.indexOf(";EsitoTrans&gt;") + 15, auth.indexOf("&lt;/EsitoTrans"));
               }
               if (auth.indexOf(";Timestamp&gt;") != -1) {
                   timestampAuth = auth.substring(auth.indexOf(";Timestamp&gt;") + 14, auth.indexOf("&lt;/Timestamp"));
               }
               if (auth.indexOf(";NumAut&gt;") != -1) {
                   numauth = auth.substring(auth.indexOf(";NumAut&gt;") + 11, auth.indexOf("&lt;/NumAut"));
               }
               if (auth.indexOf(";AcqBIN&gt;") != -1) {
                   acqBIN = auth.substring(auth.indexOf(";AcqBIN&gt;") + 11, auth.indexOf("&lt;/AcqBIN"));
               }
               if (auth.indexOf(";CodiceEsercente&gt;") != -1) {
                   codiceEsercente = auth.substring(auth.indexOf(";CodiceEsercente&gt;") + 20, auth.indexOf("&lt;/CodiceEsercente"));
               }
               if (auth.indexOf(";Stato&gt;") != -1) {
                   statoAuth = auth.substring(auth.indexOf(";Stato&gt;") + 10, auth.indexOf("&lt;/Stato"));
               }
               if (auth.indexOf(";MAC&gt;") != -1) {
                   macAuth = auth.substring(auth.indexOf(";MAC&gt;") + 8, auth.indexOf("&lt;/MAC"));
               }
               if (auth.indexOf("<firmaMD5>") != -1) {
                   firmaMD5 = auth.substring(auth.indexOf("<firmaMD5>") + 10, auth.indexOf("</firmaMD5>"));
               }
               if(mains.indexOf(";EsitoRipristinoPlafond&gt;") != -1){
                 esitoRipristinoPlafond = mains.substring(mains.indexOf(";EsitoRipristinoPlafond&gt;")+27,mains.indexOf("&lt;/EsitoRipristinoPlafond"));
               }
               MROldReversePayment acc= new MROldReversePayment();
               acc.setAcqbin(acqBIN);
               acc.setCircuito(circuito);
               acc.setCodiceEsercente(codiceEsercente);
               acc.setEsitoReverse(esitoReverse);
               acc.setEsitoMain(esitoMain);
               acc.setEsitoTrans(esitoTrans);
               acc.setIdTransReverse(idTransReverse);
               acc.setIdTransAuth(idTransAuth);
               acc.setIdTransMain(idTransMain);
               acc.setImporto(importo);
               acc.setImportoAutor(importoAutor);
               acc.setImportoContab(importoContab);
               acc.setImportoStornato(importoStornato);
               acc.setImportoTrans(importoTrans);
               acc.setMacReverse(macReverse);
               acc.setMacAuth(macAuth);
               acc.setMacMain(macMain);
               acc.setNumOrdine(numOrdine);
               acc.setNumaut(numauth);
               acc.setReqrefnum(reqRefNum);
               acc.settAutor(tAutor);
               acc.setTipopag(tipoPag);
               acc.setTipoOp(tipoOp);
               acc.setTimstampMain(timestampMain);
               acc.setTimestampRic(timestampRic);
               acc.setTimestampElab(timestampElab);
               acc.setTimestampAuth(timestampAuth);
               acc.setStatoAuth(statoAuth);
               acc.setStatoReverse(statoReverse);
               acc.setEsitoRipristinoPlafond(esitoRipristinoPlafond);
               acc.setIdLogApiXml(logApiXml);
               acc.setFirmaMD5(firmaMD5);
               acc.setIdWarranty(warrantyId);
               acc.setIdRentalAgreement(rentalAgreement);
               sx.save(acc);
               tx.commit();
            }
           if(esitoReverse.equals("00")){
               return true;
           }
           return false;
        } catch (Exception ex) {
            Logger.getLogger(PaymentReversal.class.getName()).log(Level.SEVERE, null, ex);
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
            SOAPElement soapBodyElem = soapBody.addChildElement("RichiestaStornoPagamento", "");
            SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("sottosistemaChiamante", "");
            soapBodyElem1.addTextNode("MYRENT");
            SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("firmaMd5", "");
            soapBodyElem2.addTextNode(hashText);
            SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("xmlRequest", "");
            soapBodyElem3.addTextNode(xmlRequest);
            MimeHeaders headers = soapMessage.getMimeHeaders();
            headers.addHeader("SOAPAction", serverURI + "RichiestaStornoPagamento");
            soapMessage.saveChanges();
            /* Print the request message */
            System.out.print("Request SOAP Message:");
            soapMessage.writeTo(System.out);
            System.out.println();
            return soapMessage;

        }

}


