/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author giacomo
 */
public class MROldDocumentoContratto implements it.aessepi.utils.db.PersistentInstance {
    private Integer id;
    private String descrizione;
    private Boolean permettiVisualizzazioneImmagine;
    private Boolean permettiModifica;
    private Boolean permettiCancellazione;
    
    public MROldDocumentoContratto() {

    }

    /**
     * @return the id
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @param descrizione the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MROldDocumentoContratto) ) return false;
        MROldDocumentoContratto castOther = (MROldDocumentoContratto) other;
        return new EqualsBuilder()
        .append(this.getId(), castOther.getId())
        .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }

    public String toString() {
        return getDescrizione()==null?"":getDescrizione();
    }

    /**
     * @return the permettiVisualizzazioneImmagine
     */
    public Boolean getPermettiVisualizzazioneImmagine() {
        return permettiVisualizzazioneImmagine;
    }

    /**
     * @param permettiVisualizzazioneImmagine the permettiVisualizzazioneImmagine to set
     */
    public void setPermettiVisualizzazioneImmagine(Boolean permettiVisualizzazioneImmagine) {
        this.permettiVisualizzazioneImmagine = permettiVisualizzazioneImmagine;
    }

    /**
     * @return the permettiModifica
     */
    public Boolean getPermettiModifica() {
        return permettiModifica;
    }

    /**
     * @param permettiModifica the permettiModifica to set
     */
    public void setPermettiModifica(Boolean permettiModifica) {
        this.permettiModifica = permettiModifica;
    }

    /**
     * @return the permettiCancellazione
     */
    public Boolean getPermettiCancellazione() {
        return permettiCancellazione;
    }

    /**
     * @param permettiCancellazione the permettiCancellazione to set
     */
    public void setPermettiCancellazione(Boolean permettiCancellazione) {
        this.permettiCancellazione = permettiCancellazione;
    }
}
