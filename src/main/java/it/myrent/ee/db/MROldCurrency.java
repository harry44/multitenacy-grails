/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import java.util.Date;

/**
 *
 * @author giacomo
 */
public class MROldCurrency implements PersistentInstance {
    private String code;
    private String description;
    private Boolean enabled;
    private Boolean isDefault;
    private Date createDate;
    private Date modifyDate;
    private User createdBy;
    private User lastModifyBy;

    @Override
    public Integer getId() {
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getLastModifyBy() {
        return lastModifyBy;
    }

    public void setLastModifyBy(User lastModifyBy) {
        this.lastModifyBy = lastModifyBy;
    }

    @Override
    public String toString() {
        return getCode();
    }

}
