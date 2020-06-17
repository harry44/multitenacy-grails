/*
 * MathUtils.java
 *
 * Created on 28 aprilie 2005, 09:00
 */

package it.myrent.ee.api.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author jamess
 */
public class MathUtils {
    
    /** Creates a new instance of MathUtils */
    private MathUtils() {
    }
    
    public static String format(Number number, String pattern) {
        return format(number, new DecimalFormat(pattern));
    }
    
    public static String format(Number number, NumberFormat formatter) {
        if(number != null && formatter != null) {
            return formatter.format(round(number.doubleValue(), formatter.getMaximumFractionDigits()));
        } else {
            return null;
        }
    }
    
    public static double round(double d) {
        return MathUtils.round(d, 2);
    }
    
    public static Double roundDouble(double d) {
        return new Double(round(d));
    }
    
    public static double round(double d, int places) {
        //Per evitare i problemi dovuti alla precisione del double,
        //facciamo un primo arrotondamento a places + 5 decimali.
        double e5 = Math.pow(10.0, places + 5);
        double d5 = Math.round(d * e5) / e5;
        double e = Math.pow(10.0, places);
        return Math.round(d5 * e) / e;
    }
    
    public static Double roundDouble(double d, int places) {
        return new Double(round(d, places));
    }
    
    public static Double absDouble(double d) {
        return new Double(Math.abs(d));
    }
    
    /**
     * Null safe.
     */
    public static Double absDouble(Double d) {
        Double retValue = null;
        if (d != null){
            retValue = new Double(Math.abs(d.doubleValue()));
        }
        return retValue;
    }
    
    
    public static double abs(double d) {
        return Math.abs(d);
    }
    
    /**
     * Sottrae l'iva dal totale. Non arrotonda.
     */
    //public static double scorporoIva(double totale) {
    //    return scorporoIva(Parameters.getAliquotaIva(), totale);
    //}
    
    /**
     * Aggiunge 20% di iva all'imponibile. Arrotonda a 2 decimali.
     */
   // public static double accorpamentoIva(double imponibile) {
   //     return round(accorpamentoIva(Parameters.getAliquotaIva(), imponibile));
   // }
    
    /**
     * Sottrae l'iva dal totale. Non arrotonda. Null safe.
     */
    public static Double scorporoIva(Double totale) {
        if(totale == null) {
            return null;
        } else {
            return new Double(scorporoIva(totale.doubleValue()));
        }
    }
    
    /**
     * Aggiunge 20% di iva all'imponibile. Arrotonda a 2 decimali. Null safe.
     */
    public static Double accorpamentoIva(Double imponibile) {
        if(imponibile == null) {
            return null;
        } else {
            return new Double(accorpamentoIva(imponibile.doubleValue()));
        }
    }
    
    /**
     * Sottrae l'iva dal totale. Non arrotonda. Null safe.
     */
    public static Double scorporoIva(Double aliquota, Double importo) {
        if(aliquota == null || importo == null) {
            return importo;
        }
        
        return new Double(importo.doubleValue() / (1.0 + aliquota.doubleValue()));
    }
    
    public static Double accorpamentoIva(Double aliquota, Double importo) {
        if(aliquota == null || importo == null) {
            return importo;
        }
        return new Double(importo.doubleValue() * (1.0 + aliquota.doubleValue()));
    }
    
    public static void main(String[] args) {
        System.out.println(round(1.3363323243543535));        
    }
}

