package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Marche implements it.aessepi.utils.db.PersistentInstance {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private String descrizioneMarca;

    /** full constructor */
    public Marche(String descrizioneMarca) {
        this.descrizioneMarca = descrizioneMarca;
    }

    /** default constructor */
    public Marche() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrizioneMarca() {
        return this.descrizioneMarca;
    }

    public void setDescrizioneMarca(String descrizioneMarca) {
        this.descrizioneMarca = descrizioneMarca;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Marche) ) return false;
        Marche castOther = (Marche) other;
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
