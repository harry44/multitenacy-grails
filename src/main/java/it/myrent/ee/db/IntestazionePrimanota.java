package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class IntestazionePrimanota implements it.aessepi.utils.db.PersistentInstance {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private Integer imponibile;

    /** nullable persistent field */
    private Integer codiceIVA;

    /** nullable persistent field */
    private Integer importoIVA;

    /** nullable persistent field */
    private int idGestionePrimanotaSemplificata;

    /** full constructor */
    public IntestazionePrimanota(Integer imponibile, Integer codiceIVA, Integer importoIVA, int idGestionePrimanotaSemplificata) {
        this.imponibile = imponibile;
        this.codiceIVA = codiceIVA;
        this.importoIVA = importoIVA;
        this.idGestionePrimanotaSemplificata = idGestionePrimanotaSemplificata;
    }

    /** default constructor */
    public IntestazionePrimanota() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImponibile() {
        return this.imponibile;
    }

    public void setImponibile(Integer imponibile) {
        this.imponibile = imponibile;
    }

    public Integer getCodiceIVA() {
        return this.codiceIVA;
    }

    public void setCodiceIVA(Integer codiceIVA) {
        this.codiceIVA = codiceIVA;
    }

    public Integer getImportoIVA() {
        return this.importoIVA;
    }

    public void setImportoIVA(Integer importoIVA) {
        this.importoIVA = importoIVA;
    }

    public int getIdGestionePrimanotaSemplificata() {
        return this.idGestionePrimanotaSemplificata;
    }

    public void setIdGestionePrimanotaSemplificata(int idGestionePrimanotaSemplificata) {
        this.idGestionePrimanotaSemplificata = idGestionePrimanotaSemplificata;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof IntestazionePrimanota) ) return false;
        IntestazionePrimanota castOther = (IntestazionePrimanota) other;
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
