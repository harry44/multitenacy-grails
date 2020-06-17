package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;

import java.util.Date;

/**
 * Created by Shivangani on 9/24/2018.
 */
public class MROldNexiTransactionDeposit implements PersistentInstance {

    private Integer id;
    private String esito;
    private String idOperazione;
    private String timeStamp;
    private String mac;
    private String numeroMerchant;
    private String numeroContratto;
    private String codiceGruppo;
    private String dataAttivazione;
    private String scadenzaCarta;
    private String codTrans;
    private String POS;
    private String codiceFiscale;
    private String hashPan;
    private String tipoCarta;
    private String statoPrimoPag;
    private Date transactionDate;
    private MROldContrattoNoleggio contratto;

    private String authCode;
    private String authAmount;
    private String date;
    private String description;
    private Boolean isCharge;
    private Boolean isDeposit;
    private Boolean isCancel;
    private Boolean isRefund;
    private Boolean isImmCharge;
    private String codiceAutorizzazione;
    private String ora;
    private String regione;
    private String nazione;
    private String tipoProdotto;
    private String ppo;
    private String codiceConvenzione;
    private String brand;
    private String tipoTransazione;

    public Boolean getIsCharge() {
        return isCharge;
    }

    public void setIsCharge(Boolean isCharge) {
        this.isCharge = isCharge;
    }

    public Boolean getIsDeposit() {
        return isDeposit;
    }

    public void setIsDeposit(Boolean isDeposit) {
        this.isDeposit = isDeposit;
    }

    public Boolean getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(Boolean isCancel) {
        this.isCancel = isCancel;
    }

    public Boolean getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Boolean isRefund) {
        this.isRefund = isRefund;
    }

    public Boolean getIsImmCharge() {
        return isImmCharge;
    }

    public void setIsImmCharge(Boolean isImmCharge) {
        this.isImmCharge = isImmCharge;
    }

    public String getCodiceAutorizzazione() {
        return codiceAutorizzazione;
    }

    public void setCodiceAutorizzazione(String codiceAutorizzazione) {
        this.codiceAutorizzazione = codiceAutorizzazione;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public String getNazione() {
        return nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    public String getTipoProdotto() {
        return tipoProdotto;
    }

    public void setTipoProdotto(String tipoProdotto) {
        this.tipoProdotto = tipoProdotto;
    }

    public String getPpo() {
        return ppo;
    }

    public void setPpo(String ppo) {
        this.ppo = ppo;
    }

    public String getCodiceConvenzione() {
        return codiceConvenzione;
    }

    public void setCodiceConvenzione(String codiceConvenzione) {
        this.codiceConvenzione = codiceConvenzione;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTipoTransazione() {
        return tipoTransazione;
    }

    public void setTipoTransazione(String tipoTransazione) {
        this.tipoTransazione = tipoTransazione;
    }


    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getAuthAmount() {
        return authAmount;
    }

    public void setAuthAmount(String authAmount) {
        this.authAmount = authAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEsito() {
        return esito;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setEsito(String esito) {
        this.esito = esito;
    }

    public String getIdOperazione() {
        return idOperazione;
    }

    public void setIdOperazione(String idOperazione) {
        this.idOperazione = idOperazione;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public MROldContrattoNoleggio getContratto() {
        return contratto;
    }

    public void setContratto(MROldContrattoNoleggio contratto) {
        this.contratto = contratto;
    }


    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNumeroMerchant() {
        return numeroMerchant;
    }

    public void setNumeroMerchant(String numeroMerchant) {
        this.numeroMerchant = numeroMerchant;
    }

    public String getNumeroContratto() {
        return numeroContratto;
    }

    public void setNumeroContratto(String numeroContratto) {
        this.numeroContratto = numeroContratto;
    }

    public String getCodiceGruppo() {
        return codiceGruppo;
    }

    public void setCodiceGruppo(String codiceGruppo) {
        this.codiceGruppo = codiceGruppo;
    }

    public String getDataAttivazione() {
        return dataAttivazione;
    }

    public void setDataAttivazione(String dataAttivazione) {
        this.dataAttivazione = dataAttivazione;
    }

    public String getScadenzaCarta() {
        return scadenzaCarta;
    }

    public void setScadenzaCarta(String scadenzaCarta) {
        this.scadenzaCarta = scadenzaCarta;
    }

    public String getCodTrans() {
        return codTrans;
    }

    public void setCodTrans(String codTrans) {
        this.codTrans = codTrans;
    }

    public String getPOS() {
        return POS;
    }

    public void setPOS(String POS) {
        this.POS = POS;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getHashPan() {
        return hashPan;
    }

    public void setHashPan(String hashPan) {
        this.hashPan = hashPan;
    }

    public String getTipoCarta() {
        return tipoCarta;
    }

    public void setTipoCarta(String tipoCarta) {
        this.tipoCarta = tipoCarta;
    }

    public String getStatoPrimoPag() {
        return statoPrimoPag;
    }

    public void setStatoPrimoPag(String statoPrimoPag) {
        this.statoPrimoPag = statoPrimoPag;
    }

    @Override
    public String toString() {
        return tipoCarta + ", scad.  " + scadenzaCarta + ", importo: " + authAmount;
    }
}
