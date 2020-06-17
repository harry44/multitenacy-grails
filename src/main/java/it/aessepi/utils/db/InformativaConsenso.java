/*
 * InformativaConsenso.java
 *
 * Created on 10 marzo 2005, 15.26
 */

package it.aessepi.utils.db;

import java.io.Serializable;

/**
 * @author leonardo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InformativaConsenso implements Serializable {
    
    protected String tipoAzienda; //possibile scelta tra "azieda" oppure "societa'"
    
    protected String finaliTratt; // finalita' del trattamento dei dati
    
    protected String modaliTratt; //modalita' del trattamento dei dati --> informatico, cartaceo, altro
    
    protected String obbligoTratt; //motivazione per cui e' obbligatorio il trattamento dei dati
    // SE IL TRATTAMENTO NON E' OBBLIGATORIO, DEVE RESTARE NULL
    
    protected String penaConferim; //cosa succede se non vengono comunicati i dati personali
    //PUO' ESSERE INSERITO ANCHE SE NON E' OBBLIGATORIO IL TRATTAMENTO
    
    protected String soggettInfetti; //elenco dei soggetti e categorie di soggetti che entreranno in contatto con i dati
    
    protected String ambitoDiffusione; //ambito di diffusione dei dati
    //FACOLTATIVO --> NULL se e' vuoto
    protected String titolareTratt; //nome e indirizzo dell'azienda titolare del trattamento
    
    protected String indirizzoAzienda; //indirizzo COMPLETO dell'azienda
    //FACOLTATIVO --> NULL se e' vuoto
    protected String ragioneSocialeCliente;
    
    protected String elencoResponsabili;
    
    public InformativaConsenso() {
    }
    public InformativaConsenso(String tipoAzienda, String finaliTratt,
    String modaliTratt, String obbligoTratt, String penaConferim,
    String soggettInfetti, String ambitoDiffusione,
    String titolareTratt, String indirizzoAzienda,
    String ragioneSocialeCliente) {
        this.tipoAzienda = tipoAzienda;
        this.finaliTratt = finaliTratt;
        this.modaliTratt = modaliTratt;
        this.obbligoTratt = obbligoTratt;
        this.penaConferim = penaConferim;
        this.soggettInfetti = soggettInfetti;
        this.ambitoDiffusione = ambitoDiffusione;
        this.titolareTratt = titolareTratt;
        this.indirizzoAzienda = indirizzoAzienda;
        this.ragioneSocialeCliente = ragioneSocialeCliente;
    }
    public String getAmbitoDiffusione() {
        return ambitoDiffusione;
    }
    public void setAmbitoDiffusione(String ambitoDiffusione) {
        this.ambitoDiffusione = ambitoDiffusione;
    }
    public String getFinaliTratt() {
        return finaliTratt;
    }
    public void setFinaliTratt(String finaliTratt) {
        this.finaliTratt = finaliTratt;
    }
    public String getIndirizzoAzienda() {
        return indirizzoAzienda;
    }
    public void setIndirizzoAzienda(String indirizzoAzienda) {
        this.indirizzoAzienda = indirizzoAzienda;
    }
    public String getModaliTratt() {
        return modaliTratt;
    }
    public void setModaliTratt(String modaliTratt) {
        this.modaliTratt = modaliTratt;
    }
    public String getObbligoTratt() {
        return obbligoTratt;
    }
    public void setObbligoTratt(String obbligoTratt) {
        this.obbligoTratt = obbligoTratt;
    }
    public String getPenaConferim() {
        return penaConferim;
    }
    public void setPenaConferim(String penaConferim) {
        this.penaConferim = penaConferim;
    }
    public String getRagioneSocialeCliente() {
        return ragioneSocialeCliente;
    }
    public void setRagioneSocialeCliente(String ragioneSocialeCliente) {
        this.ragioneSocialeCliente = ragioneSocialeCliente;
    }
    public String getSoggettInfetti() {
        return soggettInfetti;
    }
    public void setSoggettInfetti(String soggettInfetti) {
        this.soggettInfetti = soggettInfetti;
    }
    public String getTipoAzienda() {
        return tipoAzienda;
    }
    public void setTipoAzienda(String tipoAzienda) {
        this.tipoAzienda = tipoAzienda;
    }
    public String getTitolareTratt() {
        return titolareTratt;
    }
    public void setTitolareTratt(String titolareTratt) {
        this.titolareTratt = titolareTratt;
    }
    
    public void setElenecoResponsabili(String elencoresponsabili) {
        this.elencoResponsabili = elencoResponsabili;
    }
    
    public String getElencoresponsabili() {
        return elencoResponsabili;
    }
}