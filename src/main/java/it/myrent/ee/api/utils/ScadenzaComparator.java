/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.api.utils;

import it.myrent.ee.db.MROldScadenze;

import java.util.Comparator;

/**
 *
 * @author giacomo
 */
public class ScadenzaComparator implements Comparator{

    /** Creates a new instance of VeicoloComparator */
    public ScadenzaComparator() {
    }

    @Override
    public int compare(Object o1, Object o2) {
        if(o1 instanceof MROldScadenze && o2 instanceof MROldScadenze) {
            MROldScadenze s1 = (MROldScadenze) o1;
            MROldScadenze s2 = (MROldScadenze) o2;
            if(s1.getDataScadenza().getTime() <= s2.getDataScadenza().getTime()) {
                return -1;
            } else {
                return 1;
            }
        }
        return -1;
    }
}