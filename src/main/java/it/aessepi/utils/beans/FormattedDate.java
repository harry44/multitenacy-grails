/*
 * Formattedsuper.java
 *
 * Created on 14 octombrie 2004, 09:36
 */
package it.aessepi.utils.beans;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.MathUtils;

import java.util.Date;
import java.util.ResourceBundle;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.GregorianCalendar;

/**
 *
 * @author  jamess
 */
public class FormattedDate {

    /** Creates a new instance of FormattedDate */
    private FormattedDate(Date date) throws ParseException {
    }
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/utils/Bundle"); //NOI18N
    public static final String DATE_PATTERN = bundle.getString("JAnagrafica.DATE_PATTERN");
    private static final SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);

    private FormattedDate() {
    }

    private FormattedDate(long time) {
    }

    private FormattedDate(String formattedDate) throws ParseException {
    }

    public String toString() {
        return formatter.format(this);
    }

    public static String format(Date date) {
        if (date != null) {
            return formatter.format(date);
        }
        return null;
    }

    public static String format(String format, Date date) {
        if (date != null) {
            return new SimpleDateFormat(format).format(date);
        }
        return null;
    }

    /**
     *Formatta la data odierna con i soli campi giorno, mese, anno.
     *@return long La data odierna in millisecondi, formattata con i soli campi giorno, mese, anno.
     */
    public static long getTodaysTime() {
        GregorianCalendar c = new GregorianCalendar();
        c.set(c.MILLISECOND, 0);
        c.set(c.SECOND, 0);
        c.set(c.MINUTE, 0);
        c.set(c.HOUR_OF_DAY, 0);
        return c.getTimeInMillis();
    }

    public static long getCurrentHour() {
        GregorianCalendar c = new GregorianCalendar();
        c.set(c.MILLISECOND, 0);
        c.set(c.SECOND, 0);
        return c.getTimeInMillis();
    }

    /**
     *Formatta il tempo con i soli campi giorno, mese, anno.
     *@param <code>long time</code> Il tempo in millisecondi da formattare.
     *@return long Il tempo in millisecondi, formattato con i soli campi giorno, mese, anno.
     */
    public static long getFormattedTime(long time) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(time);
        c.set(c.MILLISECOND, 0);
        c.set(c.SECOND, 0);
        c.set(c.MINUTE, 0);
        c.set(c.HOUR_OF_DAY, 0);
        return c.getTimeInMillis();
    }

    public static long getFormattedHour(long time) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(time);
        c.set(c.MILLISECOND, 0);
        c.set(c.SECOND, 0);
        return c.getTimeInMillis();
    }

    public static Date formattedDate(long time) {
        return new Date(getFormattedTime(time));
    }

    public static Date formattedHour(long time) {
        return new Date(getFormattedHour(time));
    }

    public static Date formattedDate() {
        return new Date(getTodaysTime());
    }

    public static Date formattedTimestamp() {
        return formattedTimestamp(false);
    }

    public static Date formattedTimestamp(boolean includeSeconds) {
        GregorianCalendar reference = new GregorianCalendar();
        reference.set(reference.MILLISECOND, 0);
        if (!includeSeconds) {
            reference.set(reference.SECOND, 0);
        }
        return reference.getTime();
    }

    public static Date formattedHour() {
        return new Date(getCurrentHour());
    }

    public static Date parse(Object source) throws ParseException {
        return formatter.parse(source.toString());
    }

    public static Date parse(String format, Object source) throws ParseException {
        return new SimpleDateFormat(format).parse(source.toString());
    }

    public static Date inizioMese(int mese, int anno) {
        GregorianCalendar c = new GregorianCalendar(anno, mese, 1);
        return c.getTime();
    }

    public static Date inizioMese(Date dataOdierna) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(formattedDate(dataOdierna.getTime()).getTime());
        c.set(c.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static Date fineMese(Date dataOdierna) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(formattedDate(dataOdierna.getTime()).getTime());
        c.set(c.DAY_OF_MONTH, c.getActualMaximum(c.DAY_OF_MONTH));
        return c.getTime();
    }

    public static Date fineMese(int mese, int anno) {
        GregorianCalendar c = new GregorianCalendar(anno, mese, 1);
        c.set(c.DAY_OF_MONTH, c.getActualMaximum(c.DAY_OF_MONTH));
        return c.getTime();
    }

    public static Integer annoCorrente() {
        return new Integer(new GregorianCalendar().get(GregorianCalendar.YEAR));
    }

    public static Integer annoCorrente(Date date) {
        Integer anno = null;
        if (date == null) {
            anno = annoCorrente();
        } else {
            GregorianCalendar c = new GregorianCalendar();
            c.setTimeInMillis(date.getTime());
            anno = new Integer(c.get(c.YEAR));
        }
        return anno;
    }

    /**
     * Given two date objects this method returns a timestamp wich is a combination of the two.
     *
     * @param data a Date object containing YEAR, MONTH and DATE of interest. (This parameter cannot be null).
     * @param ora a Date object containing HOUR_OF_DAY, MINUTE and SECOND of interest. (This parameter may be null).
     * @param includeSeconds if true, the field SECOND will be added to the result, otherwise only HOUR_OF_DAY and MINUTE will be added.
     * @return a Date object containing YEAR, MONTH and DATE of the first parameter and HOUR and MINUTE (and optionally SECOND) of the second parameter.
     */
    public static Date createTimestamp(Date data, Date ora, boolean includeSeconds) {
        GregorianCalendar reference = new GregorianCalendar();
        GregorianCalendar computed = new GregorianCalendar();
        computed.clear(); //Clearing all fields. Will set only required fields.

        reference.setTimeInMillis(data.getTime());//Set year, month and date
        computed.set(computed.YEAR, reference.get(reference.YEAR));
        computed.set(computed.MONTH, reference.get(reference.MONTH));
        computed.set(computed.DATE, reference.get(reference.DATE));
        if (ora != null) {
            reference.setTimeInMillis(ora.getTime());//Set hour and minute.
            computed.set(computed.HOUR_OF_DAY, reference.get(reference.HOUR_OF_DAY));
            computed.set(computed.MINUTE, reference.get(reference.MINUTE));
            if (includeSeconds) {
                computed.set(computed.SECOND, reference.get(reference.SECOND));
            }
        }
        return computed.getTime();
    }

    public static Date createTimestamp(Date data, Date ora) {
        return createTimestamp(data, ora, false);
    }

    /**
     * Given a timestamp constructs another date containing only year, month, and day. The other fields are cleared.
     */
    public static Date extractDate(Date timestamp) {
        GregorianCalendar reference = new GregorianCalendar();
        reference.setTimeInMillis(timestamp.getTime());
        GregorianCalendar computed = new GregorianCalendar();
        computed.clear();
        computed.set(computed.YEAR, reference.get(reference.YEAR));
        computed.set(computed.MONTH, reference.get(reference.MONTH));
        computed.set(computed.DATE, reference.get(reference.DATE));
        return computed.getTime();
    }

    /**
     * Given a timestamp constructs another date containing hour and minute. The other fields are cleared. Option for including seconds.
     */
    public static Date extractTime(Date timestamp, boolean includeSeconds) {
        GregorianCalendar reference = new GregorianCalendar();
        reference.setTimeInMillis(timestamp.getTime());
        GregorianCalendar computed = new GregorianCalendar();
        computed.clear();
        computed.set(computed.MINUTE, reference.get(reference.MINUTE));
        computed.set(computed.HOUR_OF_DAY, reference.get(reference.HOUR_OF_DAY));
        if (includeSeconds) {
            computed.set(computed.SECOND, reference.get(reference.SECOND));
        }
        return computed.getTime();
    }

    /**
     * Given a timestamp constructs another date containing hour and minute. The other fields are cleared.
     * Equivalent to calling extractTime(timestamp, false);
     */
    public static Date extractTime(Date timestamp) {
        return extractTime(timestamp, false);
    }

    /**
     * Counts the number of days between the two dates.
     *@param inizio start date
     *@param fine end date
     *@param ignoreDST_OFFSET If set to true, daylight saving time offset will not be considered.
     *@return the number of days between <code>inizio</code> and <code>fine</code> approximated at 4 decimals.
     */
    public static double numeroGiorni(Date inizio, Date fine, boolean ignoreDST_OFFSET) {
        GregorianCalendar tmpInizio = new GregorianCalendar();
        GregorianCalendar tmpFine = new GregorianCalendar();
        tmpInizio.setTimeInMillis(inizio.getTime());
        tmpFine.setTimeInMillis(fine.getTime());
        if (ignoreDST_OFFSET) {
            tmpInizio.set(tmpInizio.DST_OFFSET, 0);
            tmpFine.set(tmpFine.DST_OFFSET, 0);
        }
        return MathUtils.round((tmpFine.getTimeInMillis() - tmpInizio.getTimeInMillis()) / (1000.0 * 60.0 * 60.0 * 24.0), 4);
    }

    /**
     * Adds the specified (signed) amount of time to the given time field based on GregorianCalendar rules.
     * @return a new Date object with the specified field incremented by the specified amount.
     */
    public static Date add(Date aDate, int field, int amount) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(aDate.getTime());
        c.add(field, amount);
        return c.getTime();
    }

    /**
     * Sets the time field with the given value based on GregorianCalendar rules.
     * @return a new Date object with the specified field set to the specified value.
     */
    public static Date set(Date aDate, int field, int value) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTimeInMillis(aDate.getTime());
        c.set(field, value);
        return c.getTime();
    }

    public static double numeroGiorniConRitardo(Date inizio, Date fine) {
        GregorianCalendar tmpInizio = new GregorianCalendar();
        GregorianCalendar tmpFine = new GregorianCalendar();
        tmpInizio.setTimeInMillis(inizio.getTime());
        tmpFine.setTimeInMillis(fine.getTime());

        return Math.ceil((tmpFine.getTimeInMillis() - tmpInizio.getTimeInMillis() - 59*60*1000) / (1000.0 * 60.0 * 60.0 * 24.0));
    }

    public static Date getNextDate(Date curDate, int day) {
        GregorianCalendar startDate = new GregorianCalendar();
        GregorianCalendar computed = new GregorianCalendar();
        startDate.setTimeInMillis(curDate.getTime());
        startDate.add(computed.DAY_OF_YEAR, day);
        return startDate.getTime();
    }

    public static Date getDateWithoutTimeUsingCalendar() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
        calendar.set(GregorianCalendar.MINUTE, 0);
        calendar.set(GregorianCalendar.SECOND, 0);
        calendar.set(GregorianCalendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getAddedTime(Date curDate, int days, int hour, int minute) {
        GregorianCalendar startDate = new GregorianCalendar();
        GregorianCalendar computed = new GregorianCalendar();
        startDate.setTimeInMillis(curDate.getTime());
        if (days > 0) {
            startDate.add(computed.DAY_OF_YEAR, days);
        }
        if (hour > 0) {
            startDate.add(computed.HOUR_OF_DAY, hour);
        }
        if (minute > 0) {
            startDate.add(computed.MINUTE, minute);
        }
        return startDate.getTime();
    }
}
