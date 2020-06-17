/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

/**
 *
 * @author Sumit
 */
public class PagamentoPosConfirm implements it.aessepi.utils.db.PersistentInstance{
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
     // Addtional info1
     private String addInfo1;
     // Addtional info2
     private String addInfo2;
     // Addtional info3
     private String addInfo3;
     // Addtional info4
     private String addInfo4;
     // Addtional info5
     private String addInfo5;
     // Pending amount
     private Long pendingAmount;
     // contract id foreign key
     private MROldContrattoNoleggio contrattoNoleggio;
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
     * @return the addInfo1
     */
    public String getAddInfo1() {
        return addInfo1;
    }

    /**
     * @param addInfo1 the addInfo1 to set
     */
    public void setAddInfo1(String addInfo1) {
        this.addInfo1 = addInfo1;
    }

    /**
     * @return the addInfo2
     */
    public String getAddInfo2() {
        return addInfo2;
    }

    /**
     * @param addInfo2 the addInfo2 to set
     */
    public void setAddInfo2(String addInfo2) {
        this.addInfo2 = addInfo2;
    }

    /**
     * @return the addInfo3
     */
    public String getAddInfo3() {
        return addInfo3;
    }

    /**
     * @param addInfo3 the addInfo3 to set
     */
    public void setAddInfo3(String addInfo3) {
        this.addInfo3 = addInfo3;
    }

    /**
     * @return the addInfo4
     */
    public String getAddInfo4() {
        return addInfo4;
    }

    /**
     * @param addInfo4 the addInfo4 to set
     */
    public void setAddInfo4(String addInfo4) {
        this.addInfo4 = addInfo4;
    }

    /**
     * @return the addInfo5
     */
    public String getAddInfo5() {
        return addInfo5;
    }

    /**
     * @param addInfo5 the addInfo5 to set
     */
    public void setAddInfo5(String addInfo5) {
        this.addInfo5 = addInfo5;
    }

    /**
     * @return the pendingAmount
     */
    public Long getPendingAmount() {
        return pendingAmount;
    }

    /**
     * @param pendingAmount the pendingAmount to set
     */
    public void setPendingAmount(Long pendingAmount) {
        this.pendingAmount = pendingAmount;
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
