/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

/**
 *
 * @author Sumit
 */
public class AnagraficaPosVirtuale implements it.aessepi.utils.db.PersistentInstance{
 // Id.
    private Integer id;
    // Key.
    private String key;
    // Terminal id.
    private String terminal_id;
    // Shop user account.
    private String shop_user_account;
    // Notify url.
    private String notify_url;
    // Error url.
    private String error_url;

    private String wsdlUrl;

    /* Constructor */
    public AnagraficaPosVirtuale(){

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
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the terminal_id
     */
    public String getTerminal_id() {
        return terminal_id;
    }

    /**
     * @param terminal_id the terminal_id to set
     */
    public void setTerminal_id(String terminal_id) {
        this.terminal_id = terminal_id;
    }

    /**
     * @return the shop_user_account
     */
    public String getShop_user_account() {
        return shop_user_account;
    }

    /**
     * @param shop_user_account the shop_user_account to set
     */
    public void setShop_user_account(String shop_user_account) {
        this.shop_user_account = shop_user_account;
    }

    /**
     * @return the notify_url
     */
    public String getNotify_url() {
        return notify_url;
    }

    /**
     * @param notify_url the notify_url to set
     */
    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    /**
     * @return the error_url
     */
    public String getError_url() {
        return error_url;
    }

    /**
     * @param error_url the error_url to set
     */
    public void setError_url(String error_url) {
        this.error_url = error_url;
    }

    /**
     * @return the wsdlUrl
     */
    public String getWsdlUrl() {
        return wsdlUrl;
    }

    /**
     * @param wsdlUrl the wsdlUrl to set
     */
    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    
}
