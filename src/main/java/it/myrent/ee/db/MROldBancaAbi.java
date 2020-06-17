/*
 * Banca.java
 *
 * Created on 16 februarie 2007, 11:28
 *
 */

package it.myrent.ee.db;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author jamess
 */
public class MROldBancaAbi implements it.aessepi.utils.db.PersistentObject {
    
    /** Creates a new instance of Banca */
    public MROldBancaAbi() {
    }
    private String abi;
    private String descrizione;
    private Date aggiornamento;
    private Boolean acquisita;
    private MROldBancaAbi acquirente;
    
    public boolean equals(Object other) {
        if(other != null && other instanceof MROldBancaAbi) {
            return new EqualsBuilder().append(getAbi(), ((MROldBancaAbi)other).getAbi()).isEquals();
        }
        return false;
    }
    
    public Serializable getIdentifier() {
        return getAbi();
    }

    public String toString() {
        return getDescrizione();
    }
    
    public String getAbi() {
        return abi;
    }

    public void setAbi(String abi) {
        this.abi = abi;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getAggiornamento() {
        return aggiornamento;
    }

    public void setAggiornamento(Date aggiornamento) {
        this.aggiornamento = aggiornamento;
    }

    public Boolean getAcquisita() {
        return acquisita;
    }

    public void setAcquisita(Boolean acquisita) {
        this.acquisita = acquisita;
    }

    public MROldBancaAbi getAcquirente() {
        return acquirente;
    }

    public void setAcquirente(MROldBancaAbi acquirente) {
        this.acquirente = acquirente;
    }

}
