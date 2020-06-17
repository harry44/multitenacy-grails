/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.beanutil;

import it.myrent.ee.db.MROldRataContratto;
import java.util.Comparator;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 *
 * @author bogdan
 */
public class RataContrattoComparator implements Comparator<MROldRataContratto> {

    public int compare(MROldRataContratto o1, MROldRataContratto o2) {
        if(o2 == null) {
            return 1;
        }
        return new CompareToBuilder().
                append(o1.getContratto().getSedeUscita(), o2.getContratto().getSedeUscita()).
                append(o1.getContratto().getInizio().getTime(), o2.getContratto().getInizio().getTime()).
                append(o1.getContratto().getId(), o2.getContratto().getId()).
                append(o1.getId(), o2.getId()).
                toComparison();
    }
}
