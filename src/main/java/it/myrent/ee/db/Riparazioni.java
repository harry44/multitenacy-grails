package it.myrent.ee.db;

import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Riparazioni implements it.aessepi.utils.db.PersistentInstance {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private Integer idVeicolo;

    /** nullable persistent field */
    private Date dataIniziale;

    /** nullable persistent field */
    private Date dataFinale;

    /** nullable persistent field */
    private String annotazioni;

    /** full constructor */
    public Riparazioni(Integer idVeicolo, Date dataIniziale, Date dataFinale, String annotazioni) {
        this.idVeicolo = idVeicolo;
        this.dataIniziale = dataIniziale;
        this.dataFinale = dataFinale;
        this.annotazioni = annotazioni;
    }

    /** default constructor */
    public Riparazioni() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdVeicolo() {
        return this.idVeicolo;
    }

    public void setIdVeicolo(Integer idVeicolo) {
        this.idVeicolo = idVeicolo;
    }

    public Date getDataIniziale() {
        return this.dataIniziale;
    }

    public void setDataIniziale(Date dataIniziale) {
        this.dataIniziale = dataIniziale;
    }

    public Date getDataFinale() {
        return this.dataFinale;
    }

    public void setDataFinale(Date dataFinale) {
        this.dataFinale = dataFinale;
    }

    public String getAnnotazioni() {
        return this.annotazioni;
    }

    public void setAnnotazioni(String annotazioni) {
        this.annotazioni = annotazioni;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Riparazioni) ) return false;
        Riparazioni castOther = (Riparazioni) other;
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
