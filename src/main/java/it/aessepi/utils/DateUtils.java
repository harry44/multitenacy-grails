/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.aessepi.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author bogdan
 */
public class DateUtils {

    private static final Calendar CALENDAR = GregorianCalendar.getInstance();

    public static boolean isFineMese(Date date) {
        synchronized(CALENDAR) {
            CALENDAR.setTime(date);
            return CALENDAR.get(Calendar.DAY_OF_MONTH) == CALENDAR.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
    }

    public static int getDayOfMonth(Date date) {
        synchronized(CALENDAR) {
            CALENDAR.setTime(date);
            return CALENDAR.get(Calendar.DAY_OF_MONTH);            
        }
    }

    public static Date getFirstOfMonth(Date date) {
        synchronized(CALENDAR) {
            CALENDAR.setTime(date);
            CALENDAR.set(Calendar.DAY_OF_MONTH, 1);
            return CALENDAR.getTime();
        }
    }

    public static Date getLastOfMonth(Date date) {
        synchronized(CALENDAR) {
            CALENDAR.setTime(date);
            CALENDAR.set(Calendar.DAY_OF_MONTH, CALENDAR.getActualMaximum(Calendar.DAY_OF_MONTH));
            return CALENDAR.getTime();
        }
    }

    public static Date add(Date date, int field, int amount) {
        synchronized(CALENDAR) {
            CALENDAR.setTime(date);
            CALENDAR.add(field, amount);
            return CALENDAR.getTime();
        }
    }

    public static Date set(Date date, int field, int amount) {
        synchronized(CALENDAR) {
            CALENDAR.setTime(date);
            CALENDAR.set(field, amount);
            return CALENDAR.getTime();
        }
    }

    public static int get(Date date, int field) {
        synchronized(CALENDAR) {
            CALENDAR.setTime(date);
            return CALENDAR.get(field);            
        }
    }

    public static int getActualMaximum(Date date, int field) {
        synchronized(CALENDAR) {
            CALENDAR.setTime(date);
            return CALENDAR.getActualMaximum(field);
        }
    }

    public static Date addDays(Date date, int days) {
        return add(date, Calendar.DAY_OF_MONTH, days);
    }

    public static Date addMonths(Date date, int months) {
        return add(date, Calendar.MONTH, months);
    }
}
