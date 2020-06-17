/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Sumit
 */
public class MessaggioAResXPay implements it.aessepi.utils.db.PersistentInstance{

    private Integer id;
    private String terminalId;
    private String transactionId;
    private String requestType;
    private String response;
    private String authCode;
    private String amount;
    private String currency;
    private String transactionDate;
    private String transactionType;
    private String region;
    private String country;
    private String productType;
    private String liabilityShift;
    private String mac;
    private MROldAffiliato affiliato;
    private MROldClienti cliente;
    private String cartaCredito;
    private String note;
    private MROldContrattoNoleggio contratto;
    private MROldDocumentoFiscale documentoFiscale;
    private Date data;
    private Boolean scaduta;
    private Boolean rinnovo;
    private String pan;
    private String expireDate;
    private String cvv2;
    private List operazioni;
    private Double totaleAnnullato;
    private Double totaleContabilizzato;
    private Double totaleStornato;
    private Double importoRimasto;


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
     * @return the response
     */
    public String getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(String response) {
        this.response = response;
    }

    /**
     * @return the authCode
     */
    public String getAuthCode() {
        return authCode;
    }

    /**
     * @param authCode the authCode to set
     */
    public void setAuthCode(String authCode) {
        this.authCode = authCode;
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
     * @return the transactionDate
     */
    public String getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionDate the transactionDate to set
     */
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * @return the transactionType
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * @param transactionType the transactionType to set
     */
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * @return the region
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the productType
     */
    public String getProductType() {
        return productType;
    }

    /**
     * @param productType the productType to set
     */
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * @return the liabilityShift
     */
    public String getLiabilityShift() {
        return liabilityShift;
    }

    /**
     * @param liabilityShift the liabilityShift to set
     */
    public void setLiabilityShift(String liabilityShift) {
        this.liabilityShift = liabilityShift;
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
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
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

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @return the scaduta
     */
    public Boolean getScaduta() {
        return scaduta;
    }

    /**
     * @param scaduta the scaduta to set
     */
    public void setScaduta(Boolean scaduta) {
        this.scaduta = scaduta;
    }

    /**
     * @return the rinnovo
     */
    public Boolean getRinnovo() {
        return rinnovo;
    }

    /**
     * @param rinnovo the rinnovo to set
     */
    public void setRinnovo(Boolean rinnovo) {
        this.rinnovo = rinnovo;
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
     * @return the operazioni
     */
    public List getOperazioni() {
        return operazioni;
    }

    /**
     * @param operazioni the operazioni to set
     */
    public void setOperazioni(List operazioni) {
        this.operazioni = operazioni;
    }

    /**
     * @return the totaleAnnullato
     */
    public Double getTotaleAnnullato() {
        return totaleAnnullato;
    }

    /**
     * @param totaleAnnullato the totaleAnnullato to set
     */
    public void setTotaleAnnullato(Double totaleAnnullato) {
        this.totaleAnnullato = totaleAnnullato;
    }

    /**
     * @return the totaleContabilizzato
     */
    public Double getTotaleContabilizzato() {
        return totaleContabilizzato;
    }

    /**
     * @param totaleContabilizzato the totaleContabilizzato to set
     */
    public void setTotaleContabilizzato(Double totaleContabilizzato) {
        this.totaleContabilizzato = totaleContabilizzato;
    }

    /**
     * @return the totaleStornato
     */
    public Double getTotaleStornato() {
        return totaleStornato;
    }

    /**
     * @param totaleStornato the totaleStornato to set
     */
    public void setTotaleStornato(Double totaleStornato) {
        this.totaleStornato = totaleStornato;
    }

    /**
     * @return the importoRimasto
     */
    public Double getImportoRimasto() {
        return importoRimasto;
    }

    /**
     * @param importoRimasto the importoRimasto to set
     */
    public void setImportoRimasto(Double importoRimasto) {
        this.importoRimasto = importoRimasto;
    }
}
