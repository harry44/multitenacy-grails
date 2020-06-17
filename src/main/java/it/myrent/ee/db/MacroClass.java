/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Andrea
 */
public class MacroClass implements it.aessepi.utils.db.PersistentInstance {

    private Integer id;
    private String description;
    private String webDescrption;
    private String name;
    private String exportCode;
    private String webCode;

    /**
     * @return the id
     */
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
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    public String getWebDescrption() {
        return webDescrption;
    }

    public void setWebDescrption(String webDescrption) {
        this.webDescrption = webDescrption;
    }
    
    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the exportCode
     */
    public String getExportCode() {
        return exportCode;
    }

    /**
     * @param exportCode the exportCode to set
     */
    public void setExportCode(String exportCode) {
        this.exportCode = exportCode;
    }

    /**
     * @return the webCode
     */
    public String getWebCode() {
        return webCode;
    }

    /**
     * @param webCode the webCode to set
     */
    public void setWebCode(String webCode) {
        this.webCode = webCode;
    }

    @Override
    public String toString() {
        return getName(); //NOI18N
    }

    @Override
    public boolean equals(Object other) {
        if ( !(other instanceof MacroClass) ) return false;
        MacroClass castOther = (MacroClass) other;
        return new EqualsBuilder()
        .append(this.getId(), castOther.getId())
        .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }

}
