/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.beanutil;

import it.myrent.ee.db.MROldCCDepositByGroup;
import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 *
 * @author Madhvendra
 */
public class CCDepositByGroupFonteComparator implements Comparator<MROldCCDepositByGroup>, Serializable{
     public CCDepositByGroupFonteComparator() {
    }

    public int compare(MROldCCDepositByGroup c1, MROldCCDepositByGroup c2) {
        return new CompareToBuilder()
                .append(c1.getCarClassId().getId(), c2.getCarClassId().getId())
                .toComparison();
    }
}
