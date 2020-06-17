/*
 * TipoAssicurazione.java
 *
 * Created on 12 noiembrie 2004, 11:00
 */

package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.ResourceBundle;


/**
 *
 * @author  jamess
 */
public class TipoAssicurazione implements Serializable, it.aessepi.utils.db.PersistentInstance {

    public static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
    
    /** Creates a new instance of TipoAssicurazione */
    public TipoAssicurazione() {
    }    
    
    public TipoAssicurazione(Integer id, String descrizione) {
        this.id = id;        
        this.descrizione = descrizione;
    }
    
    private Integer id;
    private String descrizione;
    
    private static List listaOggetti;
    
    static {
        listaOggetti = new ArrayList();
        
        listaOggetti.add(new TipoAssicurazione(new Integer(1), bundle.getString("TipoAssicurazione.descrizione.annuale")));
        listaOggetti.add(new TipoAssicurazione(new Integer(2), bundle.getString("TipoAssicurazione.descrizione.semestrale")));
        listaOggetti.add(new TipoAssicurazione(new Integer(3), bundle.getString("TipoAssicurazione.descrizione.quadrimestrale")));
        listaOggetti.add(new TipoAssicurazione(new Integer(4), bundle.getString("TipoAssicurazione.descrizione.trimestrale")));
    }
    
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public static void setListaOggetti(List listaOggetti) {
        TipoAssicurazione.listaOggetti = listaOggetti;
    }
    
    public static List getListaOggetti() {
        return listaOggetti;
    }
    
    public String toString() {
        return getDescrizione() != null ? getDescrizione() : new String();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof TipoAssicurazione) ) return false;
        TipoAssicurazione castOther = (TipoAssicurazione) other;
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
