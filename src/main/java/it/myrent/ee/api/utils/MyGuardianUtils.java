package it.myrent.ee.api.utils;

import it.aessepi.utils.BundleUtils;
import it.myrent.ee.db.MROldAffiliato;
import it.myrent.ee.db.MROldParcoVeicoli;
import org.grails.web.json.JSONException;
import org.grails.web.json.JSONObject;
import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by shivangani on 16/10/2019.
 */
public class MyGuardianUtils {

    public static SimpleDateFormat myguardianDateFormat = new SimpleDateFormat("ddMMyyyy");
    public static SimpleDateFormat myguardianTimeFormat = new SimpleDateFormat("HHmm");
    public static String SECONDA_PARTE_URL = "/api/kilometer?username=myguardianws&password=dogmasystems&platenumber="; //aggiungre la targa
    public static String TERZA_PARTE_URL = "&starttime="; // aggiungere il timestamp inizio
    public static String QUARTA_PARTE_URL = "&stoptime="; // aggiungere il timestamp fine
    public static String REQUEST_METHOD = "GET";
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/utils/Bundle");
    public static String KM_TOTALI_SECONDA_PARTE_URL = "/api/totalKilometer?username=myguardianws&password=dogmasystems&platenumber=";
    public static String KM_TOTALI_TERZA_PARTE_URL = "&datetime=";

    public static String getUrlData(String targa,
                                    Date dataInizio,
                                    Date oraInizio,
                                    Date dataFine,
                                    Date oraFine,
                                    String myGaurdianUrl) {
        String url = null;

        String sDataIizio = myguardianDateFormat.format(dataInizio);
        String sOraInizio = myguardianTimeFormat.format(oraInizio);

        String sDataFine = myguardianDateFormat.format(dataFine);
        String sOraFine = myguardianTimeFormat.format(oraFine);

        url = myGaurdianUrl + SECONDA_PARTE_URL + targa + TERZA_PARTE_URL + sDataIizio + sOraInizio + QUARTA_PARTE_URL + sDataFine + sOraFine;

        return url;
    }

    public static Map getDetailKm(String aUrl) {
        Map result = new HashMap();
        String resultTemp = "";
        String errorMsg = bundle.getString("mygaurdian.error.msg");
        try {
            URL url = new URL(aUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(REQUEST_METHOD);
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                result.put("status", "N");
                result.put("msg", "Problemi di connessione al server MyGuardian");
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            System.out.println("=======decode=======");

            String output;
            while ((output = br.readLine()) != null) {
                resultTemp += output;
            }
            System.out.print("response:::" + resultTemp);
            br.close();
            conn.disconnect();
            JSONObject jsonObject = new JSONObject(resultTemp);

            // get a number from the JSON object
            result.put("status", "Y");
            Object km = jsonObject.get("km");
            Object data = jsonObject.get("gpstime");
            result.put("data", (String) data);
            if(km!=null && !km.toString().equals("null")) {
                result.put("km", km.toString());
            } else {
                result.put("km", "0");
            }
        } catch (FileNotFoundException ex) {
            result.put("status", "N");
            result.put("msg", errorMsg + "</br> "+ ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            result.put("status", "N");
            result.put("msg", errorMsg + "</br> " + ex.getMessage());
            ex.printStackTrace();
        } catch (JSONException ex) {
            result.put("status", "N");
            result.put("msg", errorMsg + "</br> " + ex.getMessage());
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            result.put("status", "N");
            result.put("msg", errorMsg + "</br> " + ex.getMessage());
            ex.printStackTrace();
        }

        return result;
    }

    public static String getUrlKmTotali(Session sx,String targa,Date data,Date ora, String myGaurdianUrl) {
        String url = null;

        String sData = myguardianDateFormat.format(data);
        String sOra = myguardianTimeFormat.format(ora);

        url = myGaurdianUrl + KM_TOTALI_SECONDA_PARTE_URL + targa.trim().replace(" ", "") + KM_TOTALI_TERZA_PARTE_URL + sData + sOra;

        return url;
    }

    public static List<MROldParcoVeicoli> listaVeicoliAbilitati(Session sx, MROldAffiliato affiliato) {
        List<MROldParcoVeicoli> lista = new ArrayList<MROldParcoVeicoli>();

        lista = sx.createQuery("SELECT p FROM MROldParcoVeicoli p WHERE p.impegnato = :false and p.affiliato.id = :idAffiliato")
                .setParameter("false", Boolean.FALSE)
                .setParameter("idAffiliato", affiliato.getId())
                .list();

        return lista;
    }
}
