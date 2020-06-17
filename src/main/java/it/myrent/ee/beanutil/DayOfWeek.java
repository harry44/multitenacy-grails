/*
 * DayOfWeek.java
 *
 * Created on 20 giugno 2005, 14.33
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package it.myrent.ee.beanutil;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang.builder.EqualsBuilder;


/**
 *
 * @author luca
 */
public class DayOfWeek{
    
    private final Integer value;
    
    private static List listaOggetti;
    static {
        listaOggetti = new ArrayList();
        
        for(int i=1; i<8; i++){
            listaOggetti.add(new DayOfWeek(new Integer(i)));
        }
    }
    
    /** Creates a new instance of DayOfWeek */
    
    public DayOfWeek(Integer value) {
        this.value = value;
    }
    
    
    
    public Integer getValue(){
        return value;
    }
    
    
    public String toString(){
        
        Integer integerValue = getValue();
        if (integerValue == null){
            return new String();
        } else {
            DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
            String[] weekDaysArray = dfs.getWeekdays();
            
            if ((integerValue.intValue() > 0) && (integerValue.intValue() < 8)){
                return weekDaysArray[integerValue.intValue()];
            } else{
                return integerValue.toString();
            }
        }
    }
    
    public static String nameForIndex(int index) {
        String retValue = new String();
        if ((index > 0) && (index < 8)){
            DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
            String[] weekDaysArray = dfs.getWeekdays();
            retValue = weekDaysArray[index];
        }
        return retValue;
    }
    
    public static String shortNameForIndex(int index) {
        String retValue = new String();
        if ((index > 0) && (index < 8)){
            DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());
            String[] weekDaysArray = dfs.getShortWeekdays();
            retValue = weekDaysArray[index];
        }
        return retValue;
    }
    
    public static String nameForIndex(String s) {
        int index = -1;
        try {
            index = Integer.parseInt(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return nameForIndex(index);
    }
    
    public boolean equals(Object other) {
        if ( !(other instanceof DayOfWeek) ) return false;
        DayOfWeek castOther = (DayOfWeek) other;
        return new EqualsBuilder()
        .append(this.getValue(), castOther.getValue())
        .isEquals();
    }
    
    public static List getListaOggetti() {
        return listaOggetti;
    }
    
}
