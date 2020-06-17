/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.aessepi.myrentcs.utils;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.MathUtils;
import it.myrent.ee.db.MROldCurrency;
import it.myrent.ee.db.MROldCurrencyConversionRate;
import it.myrent.ee.db.MROldSede;
import org.hibernate.Query;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author giacomo
 */
public class CurrencyUtil {
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");
    public static String myrentStartlanguage;
    public static int LUNGHEZZA_MASSIMA_CODICE = 3;
    public static String CODICE_EURO = "EUR";
    public static final String SPANISH_LANGUAGE = "es";
    public static final String ENGLISH_LANGUAGE = "en";
    public static final String ITALIAN_LANGUAGE = "it";
    public static final String URL_FEED_XMl_BCE = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    public static final String PATH_XLSX_FILE_BLACK_DOLLAR = "https://cloud-1411412419-cache.cdn-max.com/custom/dolartoday.xlsx";
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");


    public CurrencyUtil() {

    }

    public static  Double calcolaCambioValuta(Double importo, MROldCurrencyConversionRate rate) {
        if (importo !=null && rate != null) {
            return MathUtils.round(importo * (rate!=null?rate.getRate():1), 2);
        }

        return null;
    }

    public static MROldCurrency getDefaultCurrency(Session sx, MROldSede mrOldSede) {
        MROldCurrency c = null;
        if (mrOldSede != null && mrOldSede.getCurrency() != null) {
            c=mrOldSede.getCurrency();
        }else {
            try {
                c = (MROldCurrency) sx.createQuery("SELECT c FROM MROldCurrency c where c.isDefault = :true").setParameter("true", Boolean.TRUE).uniqueResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return c;
    }

    public static boolean codiceUnico(Session sx,String codice) {
        int numero = 0;
        try {

                numero = ((Number) sx.createQuery("SELECT count(c.code) FROM MROldCurrency c WHERE c.code = :codice")
                                     .setParameter("codice", codice).uniqueResult())
                                     .intValue();
            
            return numero == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List listaCurrency(Session sx) {
        List lista = null;
        try {
            lista = sx.createQuery("SELECT c FROM MROldCurrency c WHERE c.enabled = :true ORDER BY c.code")
                      .setParameter("true", Boolean.TRUE)
                      .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<String> listaCurrencyCodes(Session sx) {
        List<String> lista = null;
        try {
            lista = sx.createQuery("SELECT c.code FROM MROldCurrency c WHERE c.enabled = :true ORDER BY c.code")
                      .setParameter("true", Boolean.TRUE)
                      .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void impostaCurrencyNonDefault(Session sx,String codice) {
        Query query = sx.createQuery("UPDATE MROldCurrency c SET c.isDefault = :false WHERE c.code <> :codice")
                        .setParameter("false", Boolean.FALSE)
                        .setParameter("codice", codice);
        
        query.executeUpdate();
    }

    public static MROldCurrency getCurrencyEuro(Session sx) {
        MROldCurrency c = null;
        c = (MROldCurrency) sx.createQuery("SELECT c FROM MROldCurrency c where c.code = :euro")
                         .setParameter("euro", CODICE_EURO)
                         .uniqueResult();
        return c;
    }

    public static boolean esisteTassoDiCambio(Session sx, Date data, String codice, Double rate) {

        Number n = (Number) sx.createQuery("SELECT count(c.id) FROM MROldCurrencyConversionRate c WHERE c.rateDate = :data and c.codeTo = :codice and c.rate = :rate").setParameter("data", data).setParameter("codice", codice).setParameter("rate", rate).uniqueResult();
        if (n.intValue() == 0) {
            return false;
        }
        return true;
    }

    public static Double conversion(Session sx, String currencyFrom, String currencyTo, Date conversionDate, Double amount) {
        MROldCurrency valuta1 = null;
        MROldCurrency valuta2 = null;
        Double convertedAmount = null;

        try {

            valuta1 = (MROldCurrency) sx.createQuery("select c from MROldCurrency c where upper(c.code) = :code")
                    .setParameter("code", currencyFrom != null ? currencyFrom.toUpperCase() : null)
                    .uniqueResult();
            valuta2 = (MROldCurrency) sx.createQuery("select c from MROldCurrency c where upper(c.code) = :code")
                    .setParameter("code", currencyTo != null ? currencyTo.toUpperCase() : null)
                    .uniqueResult();
            if (valuta1 != null && valuta2 != null) {
                convertedAmount = conversion(sx, valuta1, valuta2, conversionDate, amount);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertedAmount;
    }

    public static Double conversion(Session sx, MROldCurrency currencyFrom, MROldCurrency currencyTo, Date conversionDate, Double amount) {

        Double convertedAmount = null;
        try {

            if (conversionDate != null && currencyFrom != null && currencyTo != null && amount != null) {
                Calendar calendar = Calendar.getInstance();

                calendar.setTime(conversionDate);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                Date fineGiorno = calendar.getTime();

                List<MROldCurrencyConversionRate> listaTassi = sx.createQuery("select ccr from MROldCurrencyConversionRate ccr "
                        + "where ccr.rateDate <= :fineGiorno and ccr.codeTo = :currencyTo and ccr.codeFrom = :currencyFrom "
                        + "order by ccr.rateDate desc")
                        .setParameter("fineGiorno", fineGiorno)
                        .setParameter("currencyTo", currencyTo.getCode())
                        .setParameter("currencyFrom", currencyFrom)
                        .setMaxResults(1).list();

                if (listaTassi != null && !listaTassi.isEmpty()) {
                    MROldCurrencyConversionRate tasso = listaTassi.get(0);
                    Double rate = tasso.getRate();
                    BigDecimal bd = BigDecimal.valueOf(amount * rate);
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    convertedAmount = bd.doubleValue();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertedAmount;
    }

//    public static MROldCurrency getDefaultCurrency(Session sx) {
//        MROldCurrency c = null;
//        try {
//            sx = HibernateBridge.startNewSession();
//            c = (MROldCurrency) sx.createQuery("SELECT c FROM MROldCurrency c where c.isDefault = :true").setParameter("true", Boolean.TRUE).uniqueResult();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return c;
//    }

    public static MROldCurrency getDefaultCurrency(Session sx) {
        return (MROldCurrency) sx.createQuery("SELECT c FROM MROldCurrency c where c.isDefault = :true").setParameter("true", Boolean.TRUE).uniqueResult();
    }

    public static List<String> getCurrencyConversionCodes(Session sx,String codeCurrencyFrom) {
        List<String> lista = new ArrayList<String>();
        try {
            lista.addAll(sx.createQuery("SELECT DISTINCT c.codeTo FROM MROldCurrencyConversionRate c WHERE c.codeFrom.code = :idCurrency ORDER BY c.codeTo ASC")
                             .setParameter("idCurrency", codeCurrencyFrom)
                             .list());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<MROldCurrencyConversionRate> getCurrencyRates(Session sx,MROldCurrency codeFrom,String codeTo) {
        List<MROldCurrencyConversionRate> lista = new ArrayList<MROldCurrencyConversionRate>();
        try {
            lista.addAll(sx.createQuery("SELECT c FROM MROldCurrencyConversionRate c WHERE c.codeFrom.code = :idCurrency and c.codeTo = :codeTo ORDER BY c.rateDate DESC")
                             .setParameter("idCurrency", codeFrom.getCode())
                             .setParameter("codeTo", codeTo)
                             .list());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static String getMyrentStartlanguage() {
        return myrentStartlanguage;
    }

    public static void setMyrentStartlanguage(String aMyrentStartlanguage) {
        myrentStartlanguage = aMyrentStartlanguage;
    }

//    public static void dollaroNigro(Session sx) {
//        User aUser = (User) Parameters.getUser();
//        File tmpFile = null;
//        try {
//            Map<String, String> data = new HashMap<String, String>();
//            URL url = new URL(PATH_XLSX_FILE_BLACK_DOLLAR);
//            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
//            tmpFile = File.createTempFile("dr" + new Date().getTime(), "xlsx");
//            FileOutputStream fos = new FileOutputStream(tmpFile);
//            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
//            FileInputStream fis = new FileInputStream(tmpFile);
//            // Finds the workbook instance for XLSX file
//            XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
//            // Return first sheet from the XLSX workbook
//            XSSFSheet mySheet = myWorkBook.getSheetAt(0);
//            // Get iterator to all the rows in current sheet
//            Iterator<Row> rowIterator = mySheet.iterator();
//            // Traversing over each row of XLSX file
//            while (rowIterator.hasNext()) {
//                Row row = rowIterator.next();
//                //get last row
//                while (rowIterator.hasNext()) {
//                    row = rowIterator.next();
//                }
//                String date = row.getCell(0).getStringCellValue();
//                Double rate = row.getCell(1).getNumericCellValue();
//                String dates[] = date.split("-");
//                date = dates[1] + "/" + dates[0] + "/" + dates[2];
//                if (!CurrencyUtil.esisteTassoDiCambio(sx, formatter.parse(date), "USD", rate)) {
//                    MROldCurrencyConversionRate ccr = new MROldCurrencyConversionRate(formatter.parse(date), "USD", CurrencyUtil.getDefaultCurrency(sx), rate, aUser, Boolean.TRUE);
//                    sx.save(ccr);
//                }
//            }
//        } catch (ConnectException ce) {
//            ce.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
