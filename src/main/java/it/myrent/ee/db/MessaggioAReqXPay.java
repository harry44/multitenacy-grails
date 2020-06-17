/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

/**
 *
 * @author Sumit
 */
public class MessaggioAReqXPay implements it.aessepi.utils.db.PersistentInstance{

     private Integer id;
    private String terminalId;
    private String transactionId;
    private String requestType;
    private String actionCode;
    private String pan;
    private String expireDate;
    private String cvv2;
    private String amount;
    private String currency;
    private String notificationUrl;
    private String resultUrl;
    private String versionCode;
    private String descOrder;
    private String liabylity;
    private String utente;
    private String mac;
    private MROldAffiliato affiliato;
    private MROldClienti cliente;
    private String cartaCredito;
    private MROldContrattoNoleggio contratto;
    private MROldDocumentoFiscale documentoFiscale;



    @Override
    public Integer getId() {
       return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the terminalId
     */
    public String getTerminalId() {
        return terminalId;
    }

    /**
     * @param terminalId the terminalId to set
     */
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    /**
     * @return the transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * @return the requestType
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * @param requestType the requestType to set
     */
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    /**
     * @return the actionCode
     */
    public String getActionCode() {
        return actionCode;
    }

    /**
     * @param actionCode the actionCode to set
     */
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    /**
     * @return the pan
     */
    public String getPan() {
        return pan;
    }

    /**
     * @param pan the pan to set
     */
    public void setPan(String pan) {
        this.pan = pan;
    }

    /**
     * @return the expireDate
     */
    public String getExpireDate() {
        return expireDate;
    }

    /**
     * @param expireDate the expireDate to set
     */
    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    /**
     * @return the cvv2
     */
    public String getCvv2() {
        return cvv2;
    }

    /**
     * @param cvv2 the cvv2 to set
     */
    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the notificationUrl
     */
    public String getNotificationUrl() {
        return notificationUrl;
    }

    /**
     * @param notificationUrl the notificationUrl to set
     */
    public void setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
    }

    /**
     * @return the resultUrl
     */
    public String getResultUrl() {
        return resultUrl;
    }

    /**
     * @param resultUrl the resultUrl to set
     */
    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }

    /**
     * @return the versionCode
     */
    public String getVersionCode() {
        return versionCode;
    }

    /**
     * @param versionCode the versionCode to set
     */
    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * @return the descOrder
     */
    public String getDescOrder() {
        return descOrder;
    }

    /**
     * @param descOrder the descOrder to set
     */
    public void setDescOrder(String descOrder) {
        this.descOrder = descOrder;
    }

    /**
     * @return the liabylity
     */
    public String getLiabylity() {
        return liabylity;
    }

    /**
     * @param liabylity the liabylity to set
     */
    public void setLiabylity(String liabylity) {
        this.liabylity = liabylity;
    }

    /**
     * @return the utente
     */
    public String getUtente() {
        return utente;
    }

    /**
     * @param utente the utente to set
     */
    public void setUtente(String utente) {
        this.utente = utente;
    }

    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @return the affiliato
     */
    public MROldAffiliato getAffiliato() {
        return affiliato;
    }

    /**
     * @param affiliato the affiliato to set
     */
    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }

    /**
     * @return the cliente
     */
    public MROldClienti getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(MROldClienti cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the cartaCredito
     */
    public String getCartaCredito() {
        return cartaCredito;
    }

    /**
     * @param cartaCredito the cartaCredito to set
     */
    public void setCartaCredito(String cartaCredito) {
        this.cartaCredito = cartaCredito;
    }

    /**
     * @return the contratto
     */
    public MROldContrattoNoleggio getContratto() {
        return contratto;
    }

    /**
     * @param contratto the contratto to set
     */
    public void setContratto(MROldContrattoNoleggio contratto) {
        this.contratto = contratto;
    }

    /**
     * @return the documentoFiscale
     */
    public MROldDocumentoFiscale getDocumentoFiscale() {
        return documentoFiscale;
    }

    /**
     * @param documentoFiscale the documentoFiscale to set
     */
    public void setDocumentoFiscale(MROldDocumentoFiscale documentoFiscale) {
        this.documentoFiscale = documentoFiscale;
    }

}
