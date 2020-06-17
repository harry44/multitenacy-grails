/*
 * ScontoComparator.java
 *
 * Created on 08 iunie 2007, 06:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.myrent.ee.beanutil;

import it.myrent.ee.db.MROldSconto;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 *
 * @author jamess
 */
public class ScontoComparator implements Comparator, Serializable {
    
    /** Creates a new instance of ScontoComparator */
    public ScontoComparator() {
    }

    public int compare(Object o1, Object o2) {
        return compare((MROldSconto)o1, (MROldSconto)o2);
    }
    
    public int compare(MROldSconto s1, MROldSconto s2) {
        return new CompareToBuilder()
        .append(s1.getInizioSconto(), s2.getInizioSconto())
        .append(s1.getFineSconto(), s2.getFineSconto())
        .append(s1.getInizioRiferimento(), s2.getInizioRiferimento())
        .append(s1.getFineRiferimento(), s2.getFineRiferimento())
        .append(s1.getPercentuale(), s2.getPercentuale())
        .append(s1.getDurataMinima(), s2.getDurataMinima())
        .toComparison();
    }
}
