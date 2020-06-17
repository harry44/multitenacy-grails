package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Modelli implements it.aessepi.utils.db.PersistentInstance {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private Integer idMarca;

    /** nullable persistent field */
    private String descrizioneModello;

    /** full constructor */
    public Modelli(Integer idMarca, String descrizioneModello) {
        this.idMarca = idMarca;
        this.descrizioneModello = descrizioneModello;
    }

    /** default constructor */
    public Modelli() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMarca() {
        return this.idMarca;
    }

    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }

    public String getDescrizioneModello() {
        return this.descrizioneModello;
    }

    public void setDescrizioneModello(String descrizioneModello) {
        this.descrizioneModello = descrizioneModello;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Modelli) ) return false;
        Modelli castOther = (Modelli) other;
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
