/*
 * PersistentInstance.java
 *
 * Created on 02 octombrie 2004, 15:17
 */

package it.myrent.ee.db;
import java.io.Serializable;

/**
 *
 * @author  jamess
 */
public interface MROldRigaDocumento extends Serializable{
    
    public Integer getId();
    public void setId(Integer id);
    public String getDescrizione();
    public void setDescrizione(String descrizione);
    public String getUnitaMisura();
    public void setUnitaMisura(String unitaMisura);
    public Double getQuantita();
    public void setQuantita(Double quantita);
    public Double getPrezzoUnitario();
    public void setPrezzoUnitario(Double prezzoUnitario);
    public Double getSconto();
    public void setSconto(Double sconto);
    public Double getTotaleImponibileRiga();
    public void setTotaleImponibileRiga(Double totaleImponibileRiga);
    public Double getTotaleIvaRiga();
    public void setTotaleIvaRiga(Double totaleIvaRiga);
    public Double getTotaleRiga();
    public void setTotaleRiga(Double totaleRiga);
    public void setCodiceIva(MROldCodiciIva codiceIva);
    public MROldCodiciIva getCodiceIva();
    public void setCodiceSottoconto(MROldPianoDeiConti codiceSottoconto);
    public MROldPianoDeiConti getCodiceSottoconto();
    public Object getDocumento();
    public void setDocumento(Object documento);
    public void setNumeroRigaDocumento(Integer numeroRiga);
    public Integer getNumeroRigaDocumento();
    public Boolean isRigaDescrittiva();
}
