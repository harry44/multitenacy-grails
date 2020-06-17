/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *
 * @author jamess
 */
public class MROldRataContratto implements PersistentInstance, Comparable<MROldRataContratto> {

    private Integer id;
    private MROldDocumentoFiscale fattura;
    private MROldContrattoNoleggio contratto;
    private Double totaleImponibile;
    private Double totaleIva;
    private Double totaleFattura;

    public int compareTo(MROldRataContratto o) {
        if(o == null) {
            return 1;
        }
        return new CompareToBuilder().
                append(getContratto().getSedeUscita(), o.getContratto().getSedeUscita()).
                append(getContratto().getInizio().getTime(), o.getContratto().getInizio().getTime()).
                append(getContratto().getId(), o.getContratto().getId()).
                append(getId(), o.getId()).
                toComparison();
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(getContratto()).append(getFattura()).toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldDocumentoFiscale getFattura() {
        return fattura;
    }

    public void setFattura(MROldDocumentoFiscale fattura) {
        this.fattura = fattura;
    }

    public MROldContrattoNoleggio getContratto() {
        return contratto;
    }

    public void setContratto(MROldContrattoNoleggio contratto) {
        this.contratto = contratto;
    }

    public Double getTotaleImponibile() {
        return totaleImponibile;
    }

    public void setTotaleImponibile(Double totaleImponibile) {
        this.totaleImponibile = totaleImponibile;
    }

    public Double getTotaleIva() {
        return totaleIva;
    }

    public void setTotaleIva(Double totaleIva) {
        this.totaleIva = totaleIva;
    }

    public Double getTotaleFattura() {
        return totaleFattura;
    }

    public void setTotaleFattura(Double totaleFattura) {
        this.totaleFattura = totaleFattura;
    }
}
