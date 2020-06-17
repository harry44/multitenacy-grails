/*
 * GaranziaCartaInterface.java
 *
 * Created on 30 iulie 2007, 17:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import java.util.Date;

/**
 *
 * @author jamess
 */
public interface GaranziaCarta extends CartaDiCredito {
    public Date getDataAutorizzazione();
    public void setDataAutorizzazione(Date dataAutorizzazione);
    public String getNumeroAutorizzazione();
    public void setNumeroAutorizzazione(String numeroAutorizzazione);
    public String getPan();
    public void setPan(String pan);
    public String getAliaspan();
    public void setAliaspan(String aliaspan);
}
