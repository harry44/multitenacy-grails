/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import it.aessepi.utils.beans.FormattedDate;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author jamess
 */
public class MROldValiditaListinoFonte implements Serializable,Comparable<MROldValiditaListinoFonte> {

    private Integer id;
    private Date inizioStagione;
    private Date fineStagione;
    private Date inizioOfferta;
    private Date fineOfferta;
    private MROldListino listino;
    private MROldSede applicableLocation;
    private MROldFonteCommissione fonteCommissione;

    public MROldSede getApplicableLocation() {
        return applicableLocation;
    }

    public void setApplicableLocation(MROldSede applicableLocation) {
        this.applicableLocation = applicableLocation;
    }

    public boolean equals(Object other) {
        if (other == null || !(other instanceof MROldValiditaListinoFonte)) {
            return false;
        }

        MROldValiditaListinoFonte rhs = (MROldValiditaListinoFonte) other;
        return new EqualsBuilder().append(getId(), rhs.getId()).
                isEquals();
    }

    public boolean inStagione(Date dataRitiro) {
        if (dataRitiro == null) {
            return false;
        }
        dataRitiro = FormattedDate.extractDate(dataRitiro);
        return dataRitiro.compareTo(getInizioStagione()) * dataRitiro.compareTo(getFineStagione()) <= 0;
    }

    public boolean inOfferta(Date dataPrenotazione) {
        if (dataPrenotazione == null) {
            return false;
        }
        dataPrenotazione = FormattedDate.extractDate(dataPrenotazione);
        return dataPrenotazione.compareTo(getInizioOfferta()) * dataPrenotazione.compareTo(getFineOfferta()) <= 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getInizioStagione() {
        return inizioStagione;
    }

    public void setInizioStagione(Date inizioStagione) {
        this.inizioStagione = inizioStagione;
    }

    public Date getFineStagione() {
        return fineStagione;
    }

    public void setFineStagione(Date fineStagione) {
        this.fineStagione = fineStagione;
    }

    public Date getInizioOfferta() {
        return inizioOfferta;
    }

    public void setInizioOfferta(Date inizioOfferta) {
        this.inizioOfferta = inizioOfferta;
    }

    public Date getFineOfferta() {
        return fineOfferta;
    }

    public void setFineOfferta(Date fineOfferta) {
        this.fineOfferta = fineOfferta;
    }

    public MROldListino getListino() {
        return listino;
    }

    public void setListino(MROldListino listino) {
        this.listino = listino;
    }

    public MROldFonteCommissione getFonteCommissione() {
        return fonteCommissione;
    }

    public void setFonteCommissione(MROldFonteCommissione fonteCommissione) {
        this.fonteCommissione = fonteCommissione;
    }

    @Override
    public int compareTo(MROldValiditaListinoFonte o) {
        return getInizioStagione().compareTo(o.getInizioStagione());
    }
}
