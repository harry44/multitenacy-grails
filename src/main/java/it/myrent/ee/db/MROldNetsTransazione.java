package it.myrent.ee.db;

import java.util.Date;

/**
 * Created by Shivangani on 1/9/2018.
 */
public class MROldNetsTransazione {
    public final static String STATE_REGISTER = "REGISTER";
    public final static String STATE_CHECK = "CHECK";
    public final static String STATE_SALE = "SALE";
    public final static String STATE_SALE_WAIT = "SALE_WAIT";
    public final static String STATE_AUTHORIZED = "AUTHORIZED";
    public final static String STATE_AUTHORIZED_WAIT = "AUTHORIZED_WAIT";
    public final static String STATE_ANNULL = "ANNULL";
    public final static String STATE_ANNULL_WAIT = "ANNULL_WAIT";
    public final static String STATE_CAPTURE = "CAPTURE";
    public final static String STATE_CAPTUR = "CAPTUR";
    public final static String STATE_CAPTURE_WAIT = "CAPTURE_WAIT";
    public final static String STATE_CAPTUR_WAIT = "CAPTUR_WAIT";
    public final static String STATE_FULL_CREDITED = "FULL_CREDITED";
    public final static String STATE_CREDITED = "CREDITED";
    public final static String STATE_CREDITED_WAIT = "CREDITED_WAIT";
    public final static String STATE_FULL_CREDITED_WAIT = "FULL_CREDITED_WAIT";
    public final static String FLEX_CHECKOUT= "FLEX_CHECKOUT";

    //Info base
    private Integer id;
    private String idTransazione;
    private MROldContrattoNoleggio contratto;
    private MROldNetsAnagrafica merchantId;
    private String state;
    private Double amountTotal;
    private Double amountAuth;
    private Date startTransactionDate;
    private Date lastUpdateDate;
    private String cardType;
    private String cardNumber;
    private String currency;

    private String shaSign;

    private String storePermanently;

    //Per le transazioni ricorrenti
    private String panHash;
    private int recurringFrequency;
    private Date recurringExpiryDate;

    public MROldNetsTransazione() {
    }

    public MROldNetsTransazione(String idTransazione, MROldContrattoNoleggio contratto, String state, Double amountTotal, Double amountAuth, Date startTransactionDate, Date lastUpdateDate, String cardType, String cardNumber, String panHash, int recurringFrequency, Date recurringExpiryDate, String currency) {
        this.idTransazione = idTransazione;
        this.contratto = contratto;
        this.state = state;
        this.amountTotal = amountTotal;
        this.amountAuth = amountAuth;
        this.startTransactionDate = startTransactionDate;
        this.lastUpdateDate = lastUpdateDate;
        this.cardType = cardType;
        this.cardNumber = cardNumber;
        this.panHash = panHash;
        this.recurringFrequency = recurringFrequency;
        this.recurringExpiryDate = recurringExpiryDate;
        this.currency = currency;
    }

    public String getStorePermanently() {
        return storePermanently;
    }

    public void setStorePermanently(String storePermanently) {
        this.storePermanently = storePermanently;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
//        if (state.equals(STATE_REGISTER)
//                || state.equals(STATE_CHECK)
//                || state.equals(STATE_SALE)
//                || state.equals(STATE_AUTHORIZED)
//                || state.equals(STATE_ANNULL)
//                || state.equals(STATE_CAPTURE)
//                || state.equals(STATE_FULL_CREDITED)
//                || state.equals(FLEX_CHECKOUT)
//                || state.equals(STATE_FULL_CREDITED_WAIT)
//                || state.equals(STATE_SALE_WAIT)
//                || state.equals(STATE_CAPTURE_WAIT)
//                || state.equals(STATE_ANNULL_WAIT)
//                || state.equals(STATE_AUTHORIZED_WAIT)
//                || state.equals(STATE_CREDITED)
//                || state.equals(STATE_CREDITED_WAIT)) {
            this.state = state;
//        } else {
//            System.out.println("Errore nello stato della transazione!");
//            this.state = null;
//        }
    }

    //    public NetsAnagrafica getAnagraficaNets() {
//        return anagraficaNets;
//    }
//
//    public void setAnagraficaNets(NetsAnagrafica anagraficaNets) {
//        this.anagraficaNets = anagraficaNets;
//    }
    public Date getStartTransactionDate() {
        return startTransactionDate;
    }

    public void setStartTransactionDate(Date startTransactionDate) {
        this.startTransactionDate = startTransactionDate;
    }

    public String getIdTransazione() {
        return idTransazione;
    }

    public void setIdTransazione(String idTransazione) {
        this.idTransazione = idTransazione;
    }

    public MROldContrattoNoleggio getContratto() {
        return contratto;
    }

    public void setContratto(MROldContrattoNoleggio contratto) {
        this.contratto = contratto;
    }

    public Double getAmountTotal() {
        return amountTotal;
    }

    public void setAmountTotal(Double amountTotal) {
        this.amountTotal = amountTotal;
    }

    public Double getAmountAuth() {
        return amountAuth;
    }

    public void setAmountAuth(Double amountAuth) {
        this.amountAuth = amountAuth;
    }

    public String getPanHash() {
        return panHash;
    }

    public void setPanHash(String panHash) {
        this.panHash = panHash;
    }

    public int getRecurringFrequency() {
        return recurringFrequency;
    }

    public void setRecurringFrequency(int recurringFrequency) {
        this.recurringFrequency = recurringFrequency;
    }

    public Date getRecurringExpiryDate() {
        return recurringExpiryDate;
    }

    public void setRecurringExpiryDate(Date recurringExpiryDate) {
        this.recurringExpiryDate = recurringExpiryDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getShaSign() {
        return shaSign;
    }

    public void setShaSign(String shaSign) {
        this.shaSign = shaSign;
    }


    public MROldNetsAnagrafica getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(MROldNetsAnagrafica merchantId) {
        this.merchantId = merchantId;
    }

}
