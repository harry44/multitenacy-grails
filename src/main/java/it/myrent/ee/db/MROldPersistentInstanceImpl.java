/*
 * PersistentInstanceImpl.java
 *
 * Created on 11 octombrie 2005, 12:25
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author jamess
 */
public class MROldPersistentInstanceImpl implements PersistentInstance{
    
    /** Creates a new instance of MROldPersistentInstanceImpl */
    public MROldPersistentInstanceImpl(Integer id, String descrizione) {
        this.setId(id);
        this.setDescrizione(descrizione);
    }
    
    private Integer id;
    private String descrizione;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public String toString() {
        return getDescrizione() != null ? getDescrizione() : new String();
    }
    
    public boolean equals(Object other) {
        if ( !(other instanceof MROldPersistentInstanceImpl) ) return false;
        MROldPersistentInstanceImpl castOther = (MROldPersistentInstanceImpl) other;
        return new EqualsBuilder()
        .append(this.getId(), castOther.getId())
        .isEquals();
    }
}
