/*
 * DayUtils.java
 *
 * Created on 27 iunie 2005, 09:08
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package it.myrent.ee.api.utils;

import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.beanutil.DayOfWeek;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author jamess
 */
public class DayUtils {
    
    /** Creates a new instance of DayUtils */
    private DayUtils() {
    }
    
    public static Date closestWeekDay(Date referenceDate, int dayOfWeek, boolean before) {
        Date retValue = null;
        int amount;
        if(before) {
            amount = -1;
        } else {
            amount = 1;
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(referenceDate.getTime());
        boolean dayFound = false;
        while(!dayFound) {
            if(c.get(c.DAY_OF_WEEK) == dayOfWeek) {
                dayFound = true;
                retValue = c.getTime();
            } else {
                c.add(c.DAY_OF_MONTH, amount);
            }
        }
        return retValue;
    }
    
    public static boolean validitaPeriodica(int giornoInizio, int giornoFine, Date dataInizio, Date dataFine, Date oraInizio, Date oraFine, Date oraInizioTariffa, Date oraFineTariffa) {
        boolean periodoValido = true;
        Date dataInizioValidita = closestWeekDay(dataInizio, giornoInizio, true);
        Date dataFineValidita = closestWeekDay(dataFine, giornoFine, false);
        double giorniValidita = FormattedDate.numeroGiorni(dataInizioValidita, dataFineValidita, false);
        if(giorniValidita > 7) {
            periodoValido = false;
        }
        if (periodoValido && (oraInizio.compareTo(oraInizioTariffa) < 0 || oraFine.compareTo(oraFineTariffa) > 0)){
            periodoValido = false;
        }
        return periodoValido;
    }
    
    public static Double numeroGiorni(DayOfWeek giornoInizio, DayOfWeek giornoFine, Date oraInizio, Date oraFine) {
        Date referenceDate = FormattedDate.formattedDate();
        
        //find first date with given day of week.
        Date startDate = closestWeekDay(referenceDate, giornoInizio.getValue().intValue(), false);
        //find first date with given day of week, after the start date.
        Date endDate = closestWeekDay(startDate, giornoFine.getValue().intValue(), false);
        
        GregorianCalendar oi = new GregorianCalendar();
        oi.setTimeInMillis(oraInizio.getTime());
        GregorianCalendar di = new GregorianCalendar();
        di.setTimeInMillis(startDate.getTime());
        di.set(di.HOUR_OF_DAY, oi.get(oi.HOUR_OF_DAY));
        di.set(di.MINUTE, oi.get(oi.MINUTE));
        startDate = di.getTime();
        
        
        GregorianCalendar of = new GregorianCalendar();
        of.setTimeInMillis(oraFine.getTime());
        GregorianCalendar df = new GregorianCalendar();
        df.setTimeInMillis(endDate.getTime());
        df.set(df.HOUR_OF_DAY, of.get(of.HOUR_OF_DAY));
        df.set(df.MINUTE, of.get(of.MINUTE));
        endDate = df.getTime();
        
        double retValue = 0.0;
        retValue = FormattedDate.numeroGiorni(startDate, endDate, false);
        return new Double(retValue);
    }
    
    public static Integer getFieldFromDate(Date aDate, int field){
        Integer retValue = null;
        if (aDate != null){
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(aDate);
            try{
                retValue = new Integer(gc.get(field));
            } catch (ArrayIndexOutOfBoundsException ex){
                retValue = null;
                ex.printStackTrace();
            }
        }
        return retValue;
    }
    
}
