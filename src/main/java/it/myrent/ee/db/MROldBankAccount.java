/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

/**
 *
 * @author giacomo
 */
public class MROldBankAccount implements it.aessepi.utils.db.PersistentInstance {
    public static final StandardToStringStyle TO_STRING_STYLE = new StandardToStringStyle();
    private Integer id;
    private String bankName;
    private String description;
    private String address1;
    private String address2;
    private String address3;
    private String swiftCode;
    private String accountNumber;
    private String iban;
    private String bban;
    private User createdBy;
    private User modifiedBy;
    private Date created;
    private Date modified;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBban() {
        return bban;
    }

    public void setBban(String bban) {
        this.bban = bban;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE).append(getBankName()).append(getAccountNumber()).toString().trim();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MROldBankAccount)) {
            return false;
        }
        MROldBankAccount castOther = (MROldBankAccount) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public static final ObjectToStringConverter TO_STRING_CONVERTER = new ObjectToStringConverter() {

        @Override
        public String getPreferredStringForItem(Object item) {
            String preferredString = new String();
            String[] possibleStrings = getPossibleStringsForItem(item);
            if (possibleStrings != null && possibleStrings.length > 0) {
                if (possibleStrings[0] != null) {
                    preferredString = possibleStrings[0];
                }
                preferredString += " ( "; //NOI18N
                for (int i = 1; i < possibleStrings.length; i++) {
                    if (possibleStrings[i] != null) {
                        preferredString += possibleStrings[i];
                    }
                    if (i < possibleStrings.length - 1) {
                        preferredString += " / "; //NOI18N
                    }
                }
                preferredString += " )"; //NOI18N
            }
            return preferredString.trim();
        }

        @Override
        public String[] getPossibleStringsForItem(Object item) {
            if (item == null || !(item instanceof MROldBankAccount)) {
                return null;
            }
            MROldBankAccount account = (MROldBankAccount) item;

            return new String[]{
                        account.bankName,
                        account.getAccountNumber(),
                        account.getIban(),
                    };

        }
    };

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    
}
