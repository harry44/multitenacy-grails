package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;


/** @author Hibernate CodeGenerator */

public interface InterfaceManutenzione extends Comparable, PersistentInstance {
    
    public Integer getId();
    
    public void setId(Integer id);
    
    public void setCodice(String codice);
    
    public String getCodice();
    
    public void setDescrizione(String descrizione);
    
    public String getDescrizione();
    
}
