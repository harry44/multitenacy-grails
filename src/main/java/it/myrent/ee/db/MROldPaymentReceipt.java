package it.myrent.ee.db;

import java.util.Date;

/**
 * Created by shivangani on 02/05/2019.
 */
public class MROldPaymentReceipt implements it.aessepi.utils.db.PersistentInstance {
    private Integer id;
    private MROldContrattoNoleggio contratto;
    private String token;
    private String cardType;
    private String transactionNumber;
    private String cardExpiration;
    private double amount;
    private String operationId;
    private String authCode;
    private Date timestamp;
    private String result;
    private String cardPan;
    private String terminalId;
    private String prsId;
    private Integer type;

    public static final Integer TYPE_CHARGE = 0;
    public static final Integer TYPE_CHARGE_FROM_AUTH = 1;
    public static final Integer TYPE_REFUND = 2;

    public MROldPaymentReceipt() {
            }


    public Integer getId() {
            return id;
            }

    public void setId(Integer id) {
            this.id = id;
            }

    public MROldContrattoNoleggio getContratto() {
            return contratto;
            }

    public void setContratto(MROldContrattoNoleggio contratto) {
            this.contratto = contratto;
            }

    public String getToken() {
            return token;
            }

    public void setToken(String token) {
            this.token = token;
            }

    public String getCardType() {
            return cardType;
            }

    public void setCardType(String cardType) {
            this.cardType = cardType;
            }

    public String getTransactionNumber() {
            return transactionNumber;
            }

    public void setTransactionNumber(String transactionNumber) {
            this.transactionNumber = transactionNumber;
            }

    public String getCardExpiration() {
            return cardExpiration;
            }

    public void setCardExpiration(String cardExpiration) {
            this.cardExpiration = cardExpiration;
            }

    public double getAmount() {
            return amount;
            }

    public void setAmount(double amount) {
            this.amount = amount;
            }

    public String getOperationId() {
            return operationId;
            }

    public void setOperationId(String operationId) {
            this.operationId = operationId;
            }

    public String getAuthCode() {
            return authCode;
            }

    public void setAuthCode(String authCode) {
            this.authCode = authCode;
            }

    public Date getTimestamp() {
            return timestamp;
            }

    public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
            }

    public String getResult() {
            return result;
            }

    public void setResult(String result) {
            this.result = result;
            }

    public String getCardPan() {
            return cardPan;
            }

    public void setCardPan(String cardPan) {
            this.cardPan = cardPan;
            }

    public String getTerminalId() {
            return terminalId;
            }

    public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
            }

    public String getPrsId() {
            return prsId;
            }

    public void setPrsId(String prsId) {
            this.prsId = prsId;
            }

    public Integer getType() {
            return type;
            }

    public void setType(Integer type) {
            this.type = type;
            }

    @Override
    public String toString() {
            String retValue = "";
            if (timestamp != null) {
            retValue += "Data: " + timestamp.toString() + ", ";
            }
            if (authCode != null) {
            retValue += "Auth. Code: " + authCode + ", ";
            }
            if (contratto != null && contratto.getNumero() != null) {
            retValue += "Contratto noleggio: " + contratto.getNumero() + ", ";
            }
            retValue += "Importo = " + amount;


            return retValue;
            }
}
