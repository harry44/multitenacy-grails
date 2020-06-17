package it.myrent.ee.db;

import java.util.Date;

/**
 * Created by Shivangani on 1/12/2018.
 */
public class MROldNetsOperationCall {
    public static final String CAPTURE_CALL = "CAPTURE";
    public static final String CAPTURE_CALL_WAIT = "CAPTURE_WAIT";
    public static final String CREDIT_CALL = "CREDIT";
    public static final String CREDIT_CALL_WAIT = "CREDIT_WAIT";
    public static final String SALE_CALL = "SALE";
    public static final String SALE_CALL_WAIT = "SALE_WAIT";

    private Integer id;
    private MROldNetsTransazione transazioneNets;
    private String type;
    private Date date;
    private Double amount;
    private String error;

    private String payId;

    public String getPayIdSub() {
        return payIdSub;
    }

    public void setPayIdSub(String payIdSub) {
        this.payIdSub = payIdSub;
    }

    private String payIdSub;
    private String acceptance;
    private String nccError;
    private String nccErrorPlus;
    private String status;

    public MROldNetsOperationCall() {
    }

    public MROldNetsOperationCall(MROldNetsTransazione transazioneNets, String type, Date date, Double amount, String error) {
        if (type.equals(CAPTURE_CALL) || type.equals(CREDIT_CALL) || type.equals(SALE_CALL)) {
            this.type = type;
            this.transazioneNets = transazioneNets;
            this.date = date;
            this.amount = amount;
            this.error = error;
        } else {
            System.out.println("Wrong init! All fields are null");
            this.type = null;
            this.transazioneNets = null;
            this.date = null;
            this.amount = null;
            this.error = null;
        }
    }

    public MROldNetsTransazione getTransazioneNets() {
        return transazioneNets;
    }

    public void setTransazioneNets(MROldNetsTransazione transazioneNets) {
        this.transazioneNets = transazioneNets;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(String acceptance) {
        this.acceptance = acceptance;
    }

    public String getNccError() {
        return nccError;
    }

    public void setNccError(String nccError) {
        this.nccError = nccError;
    }

    public String getNccErrorPlus() {
        return nccErrorPlus;
    }

    public void setNccErrorPlus(String nccErrorPlus) {
        this.nccErrorPlus = nccErrorPlus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
