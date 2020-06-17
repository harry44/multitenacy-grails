/*
 * InterfaceStatoDotazioneVeicolo.java
 *
 * Created on 05 mai 2006, 09:48
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
public interface InterfaceStatoDotazioneVeicolo extends PersistentInstance {
    public String getDescrizione();
    public void setDescrizione(String descrizione);
    public Boolean getPresente();
    public void setPresente(Boolean presente);
}
