/*
 * ScontoComparator.java
 *
 * Created on 08 iunie 2007, 06:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package it.myrent.ee.beanutil;

import it.myrent.ee.db.MROldValiditaListinoFonte;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 *
 * @author jamess
 */
public class ValiditaListinoFonteComparator implements Comparator<MROldValiditaListinoFonte>, Serializable {

    /** Creates a new instance of ScontoComparator */
    public ValiditaListinoFonteComparator() {
    }

    public int compare(MROldValiditaListinoFonte v1, MROldValiditaListinoFonte v2) {
        return new CompareToBuilder()
                .append(v1.getListino().getId(), v2.getListino().getId())
                .append(v1.getInizioOfferta(), v2.getInizioOfferta()).
                append(v1.getFineOfferta(), v2.getFineOfferta()).
                append(v1.getInizioStagione(), v2.getInizioStagione()).
                append(v1.getFineStagione(), v2.getFineStagione()).
                append(v1.getApplicableLocation(), v2.getApplicableLocation()).
                toComparison();
    }
}
