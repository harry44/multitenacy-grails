package it.myrent.ee.db;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Date;

/**
 * Created by Shivangani on 7/18/2018.
 */
public class MROldTenant implements it.aessepi.utils.db.PersistentInstance{

    private Integer tenantId;
    private String companyName;
    private String companyCode;

    private Boolean isMonthly;
    private Boolean isAlwaysOpenRepForDamage;
    private Boolean isOpenRepIfAccident;
    private Boolean isOpenRepIfNotRentable;
    private Boolean emailForRepMaintRequest;

    private Double openRepForAmtGreaterThan;
    private Date dateCreated;
    private Date lastUpdated;


    private byte[] logo;

    @Override
    public int hashCode() {
        if (getId() != null) {
            return new HashCodeBuilder().append(getId()).toHashCode();
        } else {
            return super.hashCode();
        }
    }

    public Integer getId() {
        return tenantId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Boolean getIsMonthly() {
        return isMonthly;
    }

    public void setIsMonthly(Boolean isMonthly) {
        this.isMonthly = isMonthly;
    }

    public Boolean getIsAlwaysOpenRepForDamage() {
        return isAlwaysOpenRepForDamage;
    }

    public void setIsAlwaysOpenRepForDamage(Boolean isAlwaysOpenRepForDamage) {
        this.isAlwaysOpenRepForDamage = isAlwaysOpenRepForDamage;
    }

    public Boolean getIsOpenRepIfAccident() {
        return isOpenRepIfAccident;
    }

    public void setIsOpenRepIfAccident(Boolean isOpenRepIfAccident) {
        this.isOpenRepIfAccident = isOpenRepIfAccident;
    }

    public Boolean getIsOpenRepIfNotRentable() {
        return isOpenRepIfNotRentable;
    }

    public void setIsOpenRepIfNotRentable(Boolean isOpenRepIfNotRentable) {
        this.isOpenRepIfNotRentable = isOpenRepIfNotRentable;
    }

    public Boolean getEmailForRepMaintRequest() {
        return emailForRepMaintRequest;
    }

    public void setEmailForRepMaintRequest(Boolean emailForRepMaintRequest) {
        this.emailForRepMaintRequest = emailForRepMaintRequest;
    }

    public Double getOpenRepForAmtGreaterThan() {
        return openRepForAmtGreaterThan;
    }

    public void setOpenRepForAmtGreaterThan(Double openRepForAmtGreaterThan) {
        this.openRepForAmtGreaterThan = openRepForAmtGreaterThan;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }


}
