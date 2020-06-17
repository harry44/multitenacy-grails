/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author jamess
 */
public class MROldRegione implements PersistentInstance {

    private Integer id;
    private String descrizione;

    public MROldRegione() {

    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MROldRegione) {
            return new EqualsBuilder().append(getId(), ((MROldRegione) obj).getId()).
                    isEquals();
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return getDescrizione();
    }
    
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
    
    
        
}
