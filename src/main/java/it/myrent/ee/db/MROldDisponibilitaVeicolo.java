/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author jamess
 */
public class MROldDisponibilitaVeicolo implements Serializable {

    private MROldParcoVeicoli veicolo;
    private Date inizio;
    private Date fine;
    private MROldSede sede;
    private Boolean ingressoParco;
    private Boolean uscitaParco;

    public MROldDisponibilitaVeicolo() {
        setIngressoParco(Boolean.FALSE);
        setUscitaParco(Boolean.FALSE);
    }

    @Override
    public Object clone() {
        return new MROldDisponibilitaVeicolo(this);
    }
    

    public MROldDisponibilitaVeicolo(MROldDisponibilitaVeicolo otherDisponibilita) {
        this(
                otherDisponibilita.getVeicolo(),
                otherDisponibilita.getInizio(),
                otherDisponibilita.getFine(),
                otherDisponibilita.getSede(),
                otherDisponibilita.getIngressoParco(),
                otherDisponibilita.getUscitaParco());
    }

    public MROldDisponibilitaVeicolo(MROldParcoVeicoli veicolo, Date inizio, Date fine, MROldSede sede, Boolean ingressoParco, Boolean uscitaParco) {
        setVeicolo(veicolo);
        setInizio(inizio);
        setFine(fine);
        setSede(sede);
        setIngressoParco(ingressoParco);
        setUscitaParco(uscitaParco);
    }

    public boolean equals(Object other) {
        if (other != null && other instanceof MROldDisponibilitaVeicolo) {
            MROldDisponibilitaVeicolo otherDisponibilita = (MROldDisponibilitaVeicolo) other;
            return new EqualsBuilder().append(getVeicolo(), otherDisponibilita.getVeicolo()).
                    append(getInizio(), otherDisponibilita.getInizio()).
                    append(getFine(), otherDisponibilita.getFine()).
                    append(getSede(), otherDisponibilita.getSede()).
                    isEquals();
        }
        return false;
    }

//    public int hashCode() {
//        return new HashCodeBuilder().append(getVeicolo().
//                getId()).
//                append(getInizio().
//                getTime()).
//                append(getFine().
//                getTime()).
//                append(getSede().
//                getId()).
//                toHashCode();
//    }

    public MROldParcoVeicoli getVeicolo() {
        return veicolo;
    }

    public void setVeicolo(MROldParcoVeicoli veicolo) {
        this.veicolo = veicolo;
    }

    public Date getInizio() {
        return inizio;
    }

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }

    public Date getFine() {
        return fine;
    }

    public void setFine(Date fine) {
        this.fine = fine;
    }

    public MROldSede getSede() {
        return sede;
    }

    public void setSede(MROldSede sede) {
        this.sede = sede;
    }

    public Boolean getIngressoParco() {
        return ingressoParco;
    }

    public void setIngressoParco(Boolean ingressoParco) {
        this.ingressoParco = ingressoParco;
    }

    public Boolean getUscitaParco() {
        return uscitaParco;
    }

    public void setUscitaParco(Boolean uscitaParco) {
        this.uscitaParco = uscitaParco;
    }
}
