/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.db.PersistentInstance;
import java.util.Date;
import java.util.ResourceBundle;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author giacomo
 */
public class MROldCurrencyConversionRate implements PersistentInstance,Loggable {
    private Integer id;
    private MROldCurrency codeFrom;
    private String codeTo;
    private Date rateDate;
    private Double rate;
    private Date modifyDate;
    private User createdBy;
    private User lastModifyBy;
    private Boolean rss;

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    public MROldCurrencyConversionRate() {
        
    }

    public MROldCurrencyConversionRate(Date data, String currencyTo, MROldCurrency currencyFrom, Double rate, User user, Boolean rss) {
        setRateDate(data);
        setCodeTo(currencyTo);
        setRate(rate);
        setCreatedBy(user);
        setLastModifyBy(user);
        setModifyDate(new Date());
        setCodeFrom(currencyFrom);
        setRss(rss);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldCurrency getCodeFrom() {
        return codeFrom;
    }

    public void setCodeFrom(MROldCurrency codeFrom) {
        this.codeFrom = codeFrom;
    }

    public String getCodeTo() {
        return codeTo;
    }

    public void setCodeTo(String codeTo) {
        this.codeTo = codeTo;
    }

    public Date getRateDate() {
        return rateDate;
    }

    public void setRateDate(Date rateDate) {
        this.rateDate = rateDate;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
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
        return getCodeTo();
    }

    public Boolean getRss() {
        return rss;
    }

    public void setRss(Boolean rss) {
        this.rss = rss;
    }

    @Override
    public String[] getLoggableFields() {
        return new String[]{
                    "codeFrom",
                    "codeTo",
                    "rateDate",
                    "rate",
                    "modifyDate",
                    "createdBy",
                    "lastModifyBy",
                    "rss"
                };
    }

    @Override
    public String[] getLoggableLabels() {
        return new String[] {
            bundle.getString("CurrencyConversionRate.codeFrom"),
            bundle.getString("CurrencyConversionRate.codeTo"),
            bundle.getString("CurrencyConversionRate.rateDate"),
            bundle.getString("CurrencyConversionRate.rate"),
            bundle.getString("CurrencyConversionRate.modifyDate"),
            bundle.getString("CurrencyConversionRate.createdBy"),
            bundle.getString("CurrencyConversionRate.lastModifyBy"),
            bundle.getString("CurrencyConversionRate.rss")
        };
    }

    @Override
    public String getEntityName() {
        return "MROldCurrencyConversionRate";
    }

    @Override
    public Integer getEntityId() {
        return getId();
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return new HashCodeBuilder().append(getId()).toHashCode();
        } else {
            return super.hashCode();
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other != null) {
            return new EqualsBuilder().append(getId(), ((MROldCurrencyConversionRate) other).getId()).isEquals();
        }
        return false;
    }
}
