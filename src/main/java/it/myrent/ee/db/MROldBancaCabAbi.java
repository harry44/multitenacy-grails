/*
 * Filiale.java
 *
 * Created on 16 februarie 2007, 11:42
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
public class MROldBancaCabAbi implements it.aessepi.utils.db.PersistentObject {
    
    private MROldBancaCoordinate coordinate;
    private MROldBancaAbi bancaAbi;
    private String descrizione;
    private String filiale;
    private String indirizzo;
    private String citta;
    private String cap;
    private String provincia;
    private Date aggiornamento;
    
    public boolean equals(Object other) {
        if(other != null && other instanceof MROldBancaCabAbi) {
            return new EqualsBuilder().append(getCoordinate(), ((MROldBancaCabAbi)other).getCoordinate()).isEquals();
        }
        return false;
    }
    
    public Serializable getIdentifier() {
        return getCoordinate();
    }
    
    public String toString() {
        return getDescrizione();
    }
    
    /** Creates a new instance of Filiale */
    public MROldBancaCabAbi() {
    }

    public MROldBancaAbi getBancaAbi() {
        return bancaAbi;
    }

    public void setBancaAbi(MROldBancaAbi bancaAbi) {
        this.bancaAbi = bancaAbi;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getFiliale() {
        return filiale;
    }

    public void setFiliale(String filiale) {
        this.filiale = filiale;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Date getAggiornamento() {
        return aggiornamento;
    }

    public void setAggiornamento(Date aggiornamento) {
        this.aggiornamento = aggiornamento;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public MROldBancaCoordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(MROldBancaCoordinate coordinate) {
        this.coordinate = coordinate;
    }

}
