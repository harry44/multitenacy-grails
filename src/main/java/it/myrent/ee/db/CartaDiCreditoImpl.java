/*
 * GaranziaCarta.java
 *
 * Created on 16 februarie 2007, 18:36
 *
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author jamess
 */
public class CartaDiCreditoImpl extends GaranziaImpl implements CartaDiCredito {
    
    /** Creates a new instance of GaranziaCarta */
    public CartaDiCreditoImpl() {
    }
    
    public CartaDiCreditoImpl(CartaDiCredito other) {
        if(other != null) {
            setDescrizione(other.getDescrizione());
            setNumero(other.getNumero());
            setCvv(other.getCvv());
            setIntestatario(other.getIntestatario());
            setMeseScadenza(other.getMeseScadenza());
            setAnnoScadenza(other.getAnnoScadenza());
            setAliaspan(other.getAliaspan());
            setPan(other.getPan());
        }
    }
    
    private Integer annoScadenza;
    private Integer meseScadenza;
    private String cvv;
    //@Madhvendra
    private String pan;
    private String aliaspan;
    private String numeroAutorizzazione;

    public String getNumeroAutorizzazione() {
        return numeroAutorizzazione;
    }

    public void setNumeroAutorizzazione(String numeroAutorizzazione) {
        this.numeroAutorizzazione = numeroAutorizzazione;
    }


    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAliaspan() {
        return aliaspan;
    }

    public void setAliaspan(String aliaspan) {
        this.aliaspan = aliaspan;
    }
    
    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE)
        .append(getNumeroOscurato())
        .append(getDescrizione())
        .append(getIntestatario())
        .append(getMeseScadenza())
        .append(getAnnoScadenza()).toString().trim();
    }

    public String getNumeroOscurato() {
        if (getNumero() != null) {
            char[] caratteri = getNumero().toCharArray();
            for (int i = 0; i < caratteri.length - 4; i++) {
                if (caratteri[i] != ' ') {
                    caratteri[i] = '*';
                }
            }
            return new String(caratteri);
        }
        return null;
    }
    
    public String getTipoGaranzia() {
        return CARTA_DI_CREDITO;
    }

    public Integer getAnnoScadenza() {
        return annoScadenza;
    }
    
    public void setAnnoScadenza(Integer annoScadenza) {
        this.annoScadenza = annoScadenza;
    }
    
    public Integer getMeseScadenza() {
        return meseScadenza;
    }
    
    public void setMeseScadenza(Integer meseScadenza) {
        this.meseScadenza = meseScadenza;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
