/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

/**
 *
 * @author Sumit
 */
public class PagamentoPosStatus {
  private Integer id;
    private Double depositAmount;
    private Double chargeAmount;
    private Double refundAmount;
    private MROldContrattoNoleggio contrattoNoleggio;
    private PagamentoPosVirtuale pagamentoPosVirtuale;
    private PagamentoPosVerify pagamentoPosVerify;
    private PagamentoPosConfirm pagamentoPosConfirm;
    private PagamentoPosVoidAuth pagamentoPosVoidAuth;

    private String shopId;


     public PagamentoPosStatus() {
    }

    /**
     * @return the id
     */
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
     * @return the depositAmount
     */
    public Double getDepositAmount() {
        return depositAmount;
    }

    /**
     * @param depositAmount the depositAmount to set
     */
    public void setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
    }

    /**
     * @return the chargeAmount
     */
    public Double getChargeAmount() {
        return chargeAmount;
    }

    /**
     * @param chargeAmount the chargeAmount to set
     */
    public void setChargeAmount(Double chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    /**
     * @return the refundAmount
     */
    public Double getRefundAmount() {
        return refundAmount;
    }

    /**
     * @param refundAmount the refundAmount to set
     */
    public void setRefundAmount(Double refundAmount) {
        this.refundAmount = refundAmount;
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

    /**
     * @return the pagamentoPosVirtuale
     */
    public PagamentoPosVirtuale getPagamentoPosVirtuale() {
        return pagamentoPosVirtuale;
    }

    /**
     * @param pagamentoPosVirtuale the pagamentoPosVirtuale to set
     */
    public void setPagamentoPosVirtuale(PagamentoPosVirtuale pagamentoPosVirtuale) {
        this.pagamentoPosVirtuale = pagamentoPosVirtuale;
    }

    /**
     * @return the pagamentoPosVerify
     */
    public PagamentoPosVerify getPagamentoPosVerify() {
        return pagamentoPosVerify;
    }

    /**
     * @param pagamentoPosVerify the pagamentoPosVerify to set
     */
    public void setPagamentoPosVerify(PagamentoPosVerify pagamentoPosVerify) {
        this.pagamentoPosVerify = pagamentoPosVerify;
    }

    /**
     * @return the pagamentoPosConfirm
     */
    public PagamentoPosConfirm getPagamentoPosConfirm() {
        return pagamentoPosConfirm;
    }

    /**
     * @param pagamentoPosConfirm the pagamentoPosConfirm to set
     */
    public void setPagamentoPosConfirm(PagamentoPosConfirm pagamentoPosConfirm) {
        this.pagamentoPosConfirm = pagamentoPosConfirm;
    }

    /**
     * @return the pagamentoPosVoidAuth
     */
    public PagamentoPosVoidAuth getPagamentoPosVoidAuth() {
        return pagamentoPosVoidAuth;
    }

    /**
     * @param pagamentoPosVoidAuth the pagamentoPosVoidAuth to set
     */
    public void setPagamentoPosVoidAuth(PagamentoPosVoidAuth pagamentoPosVoidAuth) {
        this.pagamentoPosVoidAuth = pagamentoPosVoidAuth;
    }

    /**
     * @return the shopId
     */
    public String getShopId() {
        return shopId;
    }

    /**
     * @param shopId the shopId to set
     */
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

     
}
