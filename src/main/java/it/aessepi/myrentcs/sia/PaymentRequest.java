/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.myrentcs.sia;
import it.myrent.ee.db.MROldPaymentSia;
import it.myrent.ee.db.MROldSetting;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
/**
 *
 * @author rashmi
 */
public class PaymentRequest {

    public static String processRequest(Session sx,String pagamentoDesc, String importo, String numord, String ordDesc , String tContab ,Integer warrantyId,String customerEmail) {

        String url1=null;
        Transaction tx = null;
        try {
            System.setProperty("https.protocols", "TLSv1");
      //      sx = HibernateBridge.startNewSession();
        //    tx = sx.beginTransaction();

            SimpleDateFormat dateFormat2= new SimpleDateFormat("yyyyMMddmm");
            String num1= dateFormat2.format(new Date());
            numord= num1 + numord;

            if(customerEmail == null){
                customerEmail= "test@ext.europassistance.it";
            }
            //delete
   //         String input = "https://payments-test.europassistance.it/ui2/EffettuaTransazioneOnline?VALUTA=978&URLBACK=http://localhost:3333/booking/UrlBack.jsp?NUMORD=&URLDONE=http://localhost:3333/booking/UrlDone.jsp&EMAIL="+customerEmail+"&SOTTOSISTEMACHIAMANTE=MYRENT&ABILITAIMPORTOMODIFICABILE=true&OPERAZIONE=&TIMESTAMP=&NUMTRANS=1&PAGAMENTODESC="+pagamentoDesc+"&GENALIASPAN=True&ABILITASTORAGE=False&ABILITAPAYPAL=False&MODPAGINA=FULL&INFOORDINE=True&INFOEMAIL=True&RICHIESTOCVV2=False&MESSAGGIOSTORAGE=3&IDNEGOZIO_1=129281292811114&IMPORTO_1="+importo+"&NUMORD_1="+numord+"&ORDDESC_1="+ordDesc+"&TCONTAB_1="+tContab+"&TAUTOR_1=I&USERID_1=AF06358551&IDOPERATORE_1=AF06358551&AB=123&MAC=MYRENTTest3";
     //       String postDate = "VALUTA=978&URLBACK=http://localhost:3333/booking/UrlBack.jsp?NUMORD=&URLDONE=http://localhost:3333/booking/UrlDone.jsp&EMAIL="+customerEmail+"&SOTTOSISTEMACHIAMANTE=MYRENT&ABILITAIMPORTOMODIFICABILE=true&OPERAZIONE=&TIMESTAMP=&NUMTRANS=1&PAGAMENTODESC="+pagamentoDesc+"&GENALIASPAN=True&ABILITASTORAGE=False&ABILITAPAYPAL=False&MODPAGINA=FULL&INFOORDINE=True&INFOEMAIL=True&RICHIESTOCVV2=False&MESSAGGIOSTORAGE=3&IDNEGOZIO_1=129281292811114&IMPORTO_1="+importo+"&NUMORD_1="+numord+"&ORDDESC_1="+ordDesc+"&TCONTAB_1="+tContab+"&TAUTOR_1=I&USERID_1=AF06358551&IDOPERATORE_1=AF06358551&AB=123&MAC=";

            MROldSetting setting = (MROldSetting) sx.get(MROldSetting.class, "isSiaTest");
            String input = null;
            String postDate = null;
            if (setting !=null && setting.getValue().equals("true")) {
                input = "https://payments-test.europassistance.it/ui2/EffettuaTransazioneOnline?VALUTA=978&URLBACK=http://myrent-test.europassistance.it/booking/UrlBack.jsp?NUMORD=&URLDONE=http://myrent-test.europassistance.it/booking/UrlDone.jsp&EMAIL="+customerEmail+"&SOTTOSISTEMACHIAMANTE=MYRENT&ABILITAIMPORTOMODIFICABILE=true&OPERAZIONE=&TIMESTAMP=&NUMTRANS=1&PAGAMENTODESC="+pagamentoDesc+"&GENALIASPAN=True&ABILITASTORAGE=False&ABILITAPAYPAL=False&MODPAGINA=FULL&INFOORDINE=True&INFOEMAIL=True&RICHIESTOCVV2=False&MESSAGGIOSTORAGE=3&IDNEGOZIO_1=129281292811114&IMPORTO_1="+importo+"&NUMORD_1="+numord+"&ORDDESC_1="+ordDesc+"&TCONTAB_1="+tContab+"&TAUTOR_1=I&USERID_1=AF06358551&IDOPERATORE_1=AF06358551&MAC=MYRENTTest3";
                postDate = "VALUTA=978&URLBACK=http://myrent-test.europassistance.it/booking/UrlBack.jsp?NUMORD=&URLDONE=http://myrent-test.europassistance.it/booking/UrlDone.jsp&EMAIL="+customerEmail+"&SOTTOSISTEMACHIAMANTE=MYRENT&ABILITAIMPORTOMODIFICABILE=true&OPERAZIONE=&TIMESTAMP=&NUMTRANS=1&PAGAMENTODESC="+pagamentoDesc+"&GENALIASPAN=True&ABILITASTORAGE=False&ABILITAPAYPAL=False&MODPAGINA=FULL&INFOORDINE=True&INFOEMAIL=True&RICHIESTOCVV2=False&MESSAGGIOSTORAGE=3&IDNEGOZIO_1=129281292811114&IMPORTO_1="+importo+"&NUMORD_1="+numord+"&ORDDESC_1="+ordDesc+"&TCONTAB_1="+tContab+"&TAUTOR_1=I&USERID_1=AF06358551&IDOPERATORE_1=AF06358551&MAC=";

            //    input = "https://payments-test.europassistance.it/ui2/EffettuaTransazioneOnline?VALUTA=978&URLBACK=http://192.168.0.108:3333/booking/UrlBack.jsp?NUMORD=&URLDONE=http://192.168.0.108:3333/booking/UrlDone.jsp&EMAIL="+customerEmail+"&SOTTOSISTEMACHIAMANTE=MYRENT&ABILITAIMPORTOMODIFICABILE=true&OPERAZIONE=&TIMESTAMP=&NUMTRANS=1&PAGAMENTODESC="+pagamentoDesc+"&GENALIASPAN=True&ABILITASTORAGE=False&ABILITAPAYPAL=False&MODPAGINA=FULL&INFOORDINE=True&INFOEMAIL=True&RICHIESTOCVV2=False&MESSAGGIOSTORAGE=3&IDNEGOZIO_1=129281292811114&IMPORTO_1="+importo+"&NUMORD_1="+numord+"&ORDDESC_1="+ordDesc+"&TCONTAB_1="+tContab+"&TAUTOR_1=I&USERID_1=AF06358551&IDOPERATORE_1=AF06358551&MAC=MYRENTTest3";
             //   postDate = "VALUTA=978&URLBACK=http://192.168.0.108:3333/booking/UrlBack.jsp?NUMORD=&URLDONE=http://192.168.0.108:3333/booking/UrlDone.jsp&EMAIL="+customerEmail+"&SOTTOSISTEMACHIAMANTE=MYRENT&ABILITAIMPORTOMODIFICABILE=true&OPERAZIONE=&TIMESTAMP=&NUMTRANS=1&PAGAMENTODESC="+pagamentoDesc+"&GENALIASPAN=True&ABILITASTORAGE=False&ABILITAPAYPAL=False&MODPAGINA=FULL&INFOORDINE=True&INFOEMAIL=True&RICHIESTOCVV2=False&MESSAGGIOSTORAGE=3&IDNEGOZIO_1=129281292811114&IMPORTO_1="+importo+"&NUMORD_1="+numord+"&ORDDESC_1="+ordDesc+"&TCONTAB_1="+tContab+"&TAUTOR_1=I&USERID_1=AF06358551&IDOPERATORE_1=AF06358551&MAC=";


            }
            else{
                input = "https://payments.europassistance.it/ui2/EffettuaTransazioneOnline?VALUTA=978&URLBACK=http://myrent.europassistance.it/booking/UrlBack.jsp?NUMORD=&URLDONE=http://myrent.europassistance.it/booking/UrlDone.jsp&EMAIL="+customerEmail+"&SOTTOSISTEMACHIAMANTE=MYRENT&ABILITAIMPORTOMODIFICABILE=true&OPERAZIONE=&TIMESTAMP=&NUMTRANS=1&PAGAMENTODESC="+pagamentoDesc+"&GENALIASPAN=True&ABILITASTORAGE=False&ABILITAPAYPAL=False&MODPAGINA=FULL&INFOORDINE=True&INFOEMAIL=True&RICHIESTOCVV2=False&MESSAGGIOSTORAGE=3&IDNEGOZIO_1=129287321400007&IMPORTO_1="+importo+"&NUMORD_1="+numord+"&ORDDESC_1="+ordDesc+"&TCONTAB_1="+tContab+"&TAUTOR_1=I&USERID_1=AF06358551&IDOPERATORE_1=AF06358551&MAC=MYRENT9FAA7AAAD19011E1B4049CDB6088707G";
                postDate = "VALUTA=978&URLBACK=http://myrent.europassistance.it/booking/UrlBack.jsp?NUMORD=&URLDONE=http://myrent.europassistance.it/booking/UrlDone.jsp&EMAIL="+customerEmail+"&SOTTOSISTEMACHIAMANTE=MYRENT&ABILITAIMPORTOMODIFICABILE=true&OPERAZIONE=&TIMESTAMP=&NUMTRANS=1&PAGAMENTODESC="+pagamentoDesc+"&GENALIASPAN=True&ABILITASTORAGE=False&ABILITAPAYPAL=False&MODPAGINA=FULL&INFOORDINE=True&INFOEMAIL=True&RICHIESTOCVV2=False&MESSAGGIOSTORAGE=3&IDNEGOZIO_1=129287321400007&IMPORTO_1="+importo+"&NUMORD_1="+numord+"&ORDDESC_1="+ordDesc+"&TCONTAB_1="+tContab+"&TAUTOR_1=I&USERID_1=AF06358551&IDOPERATORE_1=AF06358551&MAC=";
            }

            //MD5 encryption
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            //delete
   //           String url = "https://payments-test.europassistance.it/ui2/EffettuaTransazioneOnline";

            String url = null;
            if (setting !=null && setting.getValue().equals("true")) {
                url = "https://payments-test.europassistance.it/ui2/EffettuaTransazioneOnline";
            }
            else{
                url = "https://payments.europassistance.it/ui2/EffettuaTransazioneOnline";
            }

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            String urlParameters = postDate + hashtext;

            con.setDoOutput(true);
            DataOutputStream wr=null;
            try{
             wr= new DataOutputStream(con.getOutputStream());
              }catch(Exception ex){
             System.setProperty("https.protocols", "TLSv1.2");
              wr= new DataOutputStream(con.getOutputStream());
            }
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            if(responseCode == 200){
                  MROldPaymentSia sia = (MROldPaymentSia) sx.createQuery("Select p From MROldPaymentSia p where p.numord=:numord").setParameter("numord", numord).uniqueResult();
                 if(sia == null){
                  String rentalAgreement = numord.replaceAll(".*_", "");
                  MROldPaymentSia payment = new MROldPaymentSia();
                  payment.setIdWarranty(warrantyId);
                  payment.setNumord(numord);
                  //payment.setOrderDescription(ordDesc);
                  //payment.setIdRentalAgreement(rentalAgreement);
                  sx.save(payment);
                }
            }
            url1=url + "?" + urlParameters;


        } catch (IOException ex) {
            Logger.getLogger(PaymentRequest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PaymentRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return url1;
    }
}
