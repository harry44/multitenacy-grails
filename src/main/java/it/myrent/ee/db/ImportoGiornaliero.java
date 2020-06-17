/*
 * Importo.java
 *
 * Created on 19 februarie 2008, 10:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;

/**
 *
 * @author jamess
 */
public interface ImportoGiornaliero extends PersistentInstance, Comparable {
    public Integer getId();
    public void setId(Integer id);    
    public Integer getMinimo();
    public void setMinimo(Integer minimo);
    public Integer getMassimo();
    public void setMassimo(Integer massimo);
    public Boolean getImportoFisso();
    public void setImportoFisso(Boolean importoFisso);
    public Double getImportoBase();
    public void setImportoBase(Double importoBase);
    public Double getImportoExtra();
    public void setImportoExtra(Double importoExtra);
}
