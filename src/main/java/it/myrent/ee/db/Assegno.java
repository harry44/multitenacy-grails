/*
 * GaranziaAssegnoInterface.java
 *
 * Created on 30 iulie 2007, 17:22
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
public interface Assegno extends Garanzia {    
    public String getBanca();
    public void setBanca(String banca);
    public String getCab();
    public void setCab(String cab);
    public String getAbi();
    public void setAbi(String abi);
    public String getContoCorrente();
    public void setContoCorrente(String contoCorrente);
    public Date getDataEmissione();
    public void setDataEmissione(Date dataEmissione);
    public String getLuogoEmissione();
    public void setLuogoEmissione(String luogoEmissione);
    public Double getImporto();
    public void setImporto(Double importo);
}
