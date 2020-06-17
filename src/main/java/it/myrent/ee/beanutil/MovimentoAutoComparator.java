/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.beanutil;

import it.myrent.ee.db.MROldMovimentoAuto;

import java.util.Comparator;

/**
 * @author Roberto Rossi
 */
public class MovimentoAutoComparator implements Comparator<MROldMovimentoAuto> {

    @Override
    public int compare(MROldMovimentoAuto m1, MROldMovimentoAuto m2) {
        if (m1 != null && m1.getInizio() != null) {
            if (m2 != null && m2.getInizio() != null) {
                return m1.getInizio().compareTo(m2.getInizio());
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

}
