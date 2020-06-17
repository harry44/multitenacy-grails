package it.aessepi.myrentcs.utils;

import it.myrent.ee.db.MROldContrattoNoleggio;
import it.myrent.ee.db.MROldPaymentReceipt;

import java.util.Date;

/**
 * Created by shivangani on 02/05/2019.
 */
public class PaymentReceiptAdapter {

    private MROldPaymentReceipt paymentReceipt;


    public PaymentReceiptAdapter (MROldPaymentReceipt paymentReceipt) {
        setPaymentReceipt(paymentReceipt);
    }

    public void setPaymentReceipt (MROldPaymentReceipt paymentReceipt) {
        this.paymentReceipt = paymentReceipt;
    }

    public MROldPaymentReceipt getPaymentReceipt () {
        return paymentReceipt;
    }

    public Integer getId() {
        return getPaymentReceipt().getId();
    }

    public void setId(Integer id) {
        getPaymentReceipt().setId(id);
    }

    public MROldContrattoNoleggio getContratto() {
        return getPaymentReceipt().getContratto();
    }

    public void setContratto(MROldContrattoNoleggio contratto) {
        getPaymentReceipt().setContratto(contratto);
    }

    public String getToken() {
        return getPaymentReceipt().getToken();
    }

    public void setToken(String token) {
        getPaymentReceipt().setToken(token);
    }

    public String getCardType() {
        return getPaymentReceipt().getCardType();
    }

    public void setCardType(String cardType) {
        getPaymentReceipt().setCardType(cardType);
    }

    public String getTransactionNumber() {
        return getPaymentReceipt().getTransactionNumber();
    }

    public void setTransactionNumber(String transactionNumber) {
        getPaymentReceipt().setTransactionNumber(transactionNumber);
    }

    public String getCardExpiration() {
        return getPaymentReceipt().getCardExpiration();
    }

    public void setCardExpiration(String cardExpiration) {
        getPaymentReceipt().setCardExpiration(cardExpiration);
    }

    public double getAmount() {
        return getPaymentReceipt().getAmount();
    }

    public void setAmount(double amount) {
        getPaymentReceipt().setAmount(amount);
    }

    public String getOperationId() {
        return getPaymentReceipt().getOperationId();
    }

    public void setOperationId(String operationId) {
        getPaymentReceipt().setOperationId(operationId);
    }

    public String getAuthCode() {
        return getPaymentReceipt().getAuthCode();
    }

    public void setAuthCode(String authCode) {
        getPaymentReceipt().setAuthCode(authCode);
    }

    public Date getTimestamp() {
        return getPaymentReceipt().getTimestamp();
    }

    public void setTimestamp(Date timestamp) {
        getPaymentReceipt().setTimestamp(timestamp);
    }

    public String getResult() {
        return getPaymentReceipt().getResult();
    }

    public void setResult(String result) {
        getPaymentReceipt().setResult(result);
    }

    public String getCardPan() {
        return getPaymentReceipt().getCardPan();
    }

    public void setCardPan(String cardPan) {
        getPaymentReceipt().setCardPan(cardPan);
    }

    public String getTerminalId() {
        return getPaymentReceipt().getTerminalId();
    }

    public void setTerminalId(String terminalId) {
        getPaymentReceipt().setTerminalId(terminalId);
    }

    public String getPrsId() {
        return getPaymentReceipt().getPrsId();
    }

    public void setPrsId(String prsId) {
        getPaymentReceipt().setPrsId(prsId);
    }

    public Integer getType() {
        return getPaymentReceipt().getType();
    }

    public void setType(Integer type) {
        getPaymentReceipt().setType(type);
    }

    public void setMovimento () {
    }
    public void getMovimento() {
    }
    public void setConducente1 () {
    }
    public void getConducente1() {
    }
    public void setCliente () {
    }
    public void getCliente() {
    }
    public void setTariffa () {
    }
    public void getTariffa() {
    }
    public void setInizio () {
    }
    public void getInizio() {
    }
    public void setFine () {
    }
    public void getFine() {
    }
    public void setConducente2 () {
    }
    public void getConducente2() {
    }
    public void setCommissione () {
    }
    public void getCommissione() {
    }
    public void setNote () {
    }
    public void getNote() {
    }
    public void setNumero () {
    }
    public void getNumero() {
    }
    public void setChiuso () {
    }
    public void getChiuso() {
    }
    public void setPagamento () {
    }
    public void getPagamento() {
    }
    public void setData () {
    }
    public void getData() {
    }
    public void setGaranzia1 () {
    }
    public void getGaranzia1() {
    }
    public void setPrefisso () {
    }
    public void getPrefisso() {
    }
    public void setDocumentoFiscale () {
    }
    public void getDocumentoFiscale() {
    }
    public void setNoleggio () {
    }
    public void getNoleggio() {
    }
    public void setCauzione () {
    }
    public void getCauzione() {
    }
    public void setSaldoNoleggio () {
    }
    public void getSaldoNoleggio() {
    }
    public void setSaldoCauzione () {
    }
    public void getSaldoCauzione() {
    }
    public void setGaranzia2 () {
    }
    public void getGaranzia2() {
    }
    public void setScontoTariffa () {
    }
    public void getScontoTariffa() {
    }
}
