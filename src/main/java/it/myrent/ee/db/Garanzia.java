/*
 * GaranziaInterface.java
 *
 * Created on 30 iulie 2007, 17:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.db.PersistentInstance;

/**
 *
 * @author jamess
 */
public interface Garanzia extends PersistentInstance {
    public String getTipoGaranzia();
    public String getDescrizione();
    public void setDescrizione(String descrizione);
    public String getIntestatario();
    public void setIntestatario(String intestatario);
    public String getNumero();
    public void setNumero(String numero);
    public Double getImporto();
    public void setImporto(Double importo);
    public boolean isGaranziaCarta();
    public boolean isGaranziaAssegno();
    public String getPan();
    public void setPan(String pan);
    public static final String CARTA_DI_CREDITO = BundleUtils.getBundle("it/myrent/ee/db/Bundle").getString("Garanzia.CARTA_DI_CREDITO"); //NOI18N
    public static final String ASSEGNO = BundleUtils.getBundle("it/myrent/ee/db/Bundle").getString("Garanzia.ASSEGNO"); //NOI18N

}
