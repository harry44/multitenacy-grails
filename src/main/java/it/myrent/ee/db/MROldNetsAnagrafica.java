package it.myrent.ee.db;

import java.util.Set;

/**
 * Created by Shivangani on 1/9/2018.
 */
public class MROldNetsAnagrafica {
    private String merchantId;
    private String token;
    private String wsdlLink;
    private String notifyUrl;
    private String errorUrl;
    private Boolean inUse;

    private String userId;

    private String shaIn;

    private String refId;

    private Set sedi;
    private Set netsTrans;

    public MROldNetsAnagrafica() {
    }

    public MROldNetsAnagrafica(String merchantId, String token, String wsdlLink, String notifyUrl, String errorUrl, Boolean inUse) {
        this.merchantId = merchantId;
        this.token = token;
        this.wsdlLink = wsdlLink;
        this.notifyUrl = notifyUrl;
        this.errorUrl = errorUrl;
        this.inUse = inUse;
    }

    public String getShaIn() {
        return shaIn;
    }

    public void setShaIn(String shaIn) {
        this.shaIn = shaIn;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getWsdlLink() {
        return wsdlLink;
    }

    public void setWsdlLink(String wsdlLink) {
        this.wsdlLink = wsdlLink;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

//    @Override
//    public Integer getId() {
//        System.out.println("DO NOT USE IT!!! Use getMerchantID()");
//        return null;
//    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public Set getSedi() {
        return sedi;
    }

    public void setSedi(Set sedi) {
        this.sedi = sedi;
    }


    public Set getNetsTrans() {
        return netsTrans;
    }

    public void setNetsTrans(Set netsTrans) {
        this.netsTrans = netsTrans;
    }
}
