/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

/**
 *
 * @author Sumit
 */
public class PagamentoPosVerify implements it.aessepi.utils.db.PersistentInstance{
// Id.
     private Integer id;
     // Error.
     private Boolean error;
     // Request outcome.
     private String rc;
     // Error description.
     private String errorDesc;
     // Transaction id.
     private Long transactionID;
     // Auth code.
     private String authCode;
     // Enr status.
     private String enrStatus;
     // Authorization status.
     private String authStatus;
     // Credit card brand.
     private String brand;
     // contract id foreign key
     private MROldContrattoNoleggio contrattoNoleggio;


    @Override
    public Integer getId() {
        return id;
    }
/* Constructor */
     public PagamentoPosVerify(){

    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the error
     */
    public Boolean getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(Boolean error) {
        this.error = error;
    }

    /**
     * @return the rc
     */
    public String getRc() {
        return rc;
    }

    /**
     * @param rc the rc to set
     */
    public void setRc(String rc) {
        this.rc = rc;
    }

    /**
     * @return the errorDesc
     */
    public String getErrorDesc() {
        return errorDesc;
    }

    /**
     * @param errorDesc the errorDesc to set
     */
    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    /**
     * @return the transactionID
     */
    public Long getTransactionID() {
        return transactionID;
    }

    /**
     * @param transactionID the transactionID to set
     */
    public void setTransactionID(Long transactionID) {
        this.transactionID = transactionID;
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
     * @return the enrStatus
     */
    public String getEnrStatus() {
        return enrStatus;
    }

    /**
     * @param enrStatus the enrStatus to set
     */
    public void setEnrStatus(String enrStatus) {
        this.enrStatus = enrStatus;
    }

    /**
     * @return the authStatus
     */
    public String getAuthStatus() {
        return authStatus;
    }

    /**
     * @param authStatus the authStatus to set
     */
    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    /**
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @param brand the brand to set
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return the contrattoNoleggio
     */
    public MROldContrattoNoleggio getContrattoNoleggio() {
        return contrattoNoleggio;
    }

    /**
     * @param contrattoNoleggio the contrattoNoleggio to set
     */
    public void setContrattoNoleggio(MROldContrattoNoleggio contrattoNoleggio) {
        this.contrattoNoleggio = contrattoNoleggio;
    }

}
