package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


public class MROldComuni implements it.aessepi.utils.db.PersistentInstance {

    private Integer id;
    
    private String nazione;

    private String comune;

    private String provincia;

    private String regione;
    
    private String cap;

    private Integer prefisso;

    private String codiceFiscale;

    private String codiceStatistico;

    public MROldComuni() {
    }
    
    public Integer getId() {
        return this.id;
    }

    private void setId(Integer id) {
        this.id = id;
    }
    
    public String getNazione() {
        return nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    public String getComune() {
        return this.comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getProvincia() {
        return this.provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getRegione() {
        return this.regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public String getCap() {
        return this.cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public Integer getPrefisso() {
        return this.prefisso;
    }

    public void setPrefisso(Integer prefisso) {
        this.prefisso = prefisso;
    }

    public String getCodiceFiscale() {
        return this.codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getCodiceStatistico() {
        return this.codiceStatistico;
    }

    public void setCodiceStatistico(String codiceStatistico) {
        this.codiceStatistico = codiceStatistico;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MROldComuni) ) return false;
        MROldComuni castOther = (MROldComuni) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }    
}
