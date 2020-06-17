/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

/**
 *
 * @author Sumit
 */
public class PagamentoPosVirtuale implements it.aessepi.utils.db.PersistentInstance{
  // Id.
     private Integer id;
     // Error.
     private Boolean error;
     // Error description.
     private String errorDesc;
     // Payment id.
     private String paymentID;
     // Redirect url.
     private String redirectURL;
     // contract id foreign key
     private MROldContrattoNoleggio contrattoNoleggio;

     /* Constructor */
     public PagamentoPosVirtuale(){

    }

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
     * @return the paymentID
     */
    public String getPaymentID() {
        return paymentID;
    }

    /**
     * @param paymentID the paymentID to set
     */
    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    /**
     * @return the redirectURL
     */
    public String getRedirectURL() {
        return redirectURL;
    }

    /**
     * @param redirectURL the redirectURL to set
     */
    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
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
