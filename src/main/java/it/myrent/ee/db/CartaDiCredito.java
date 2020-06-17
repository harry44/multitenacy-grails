/*
 * CartaDiCreditoInterface.java
 *
 * Created on 30 iulie 2007, 17:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.myrent.ee.db;

/**
 *
 * @author jamess
 */
public interface CartaDiCredito extends Garanzia {
    public Integer getAnnoScadenza();
    public void setAnnoScadenza(Integer annoScadenza);
    public Integer getMeseScadenza();
    public void setMeseScadenza(Integer meseScadenza);
    public String getCvv();
    public void setCvv(String cvv);
    public String getNumeroOscurato();
    public String getPan();
    public void setPan(String pan);
    public String getAliaspan();
    public void setAliaspan(String aliaspan);
    public String getNumeroAutorizzazione();
    public void setNumeroAutorizzazione(String numeroAutorizzazione);
}
