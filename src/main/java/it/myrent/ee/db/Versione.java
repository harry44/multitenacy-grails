/*
 * Version.java
 *
 * Created on 16 februarie 2005, 10:00
 */

package it.myrent.ee.db;

import java.io.Serializable;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author  jamess
 */
public class Versione implements Serializable, Comparable {
    
    /** Creates a new instance of Version */
    public Versione() {
    }
    
    public Versione(String aName, String aVersione) {
        setName(aName);
        String[] versioni = aVersione.split("[.]");
        if (versioni.length > 0) {
            try {
                setMajor(Integer.valueOf(versioni[0]));
            } catch (Exception ex) {
            }
            if (versioni.length > 1) {
                try {
                    setMinor(Integer.valueOf(versioni[1]));
                } catch (Exception ex) {
                }
                if (versioni.length > 2) {
                    try {
                        setPatch(Integer.valueOf(versioni[2]));
                    } catch (Exception ex) {}
                }
            }
        }
    }
    
    public Versione(String name, Integer major, Integer minor, Integer patch) {
        setName(name);
        setMajor(major);
        setMinor(minor);
        setPatch(patch);
    }
    private String name;
    private Integer major;
    private Integer minor;
    private Integer patch;
    
    private static final StandardToStringStyle TO_STRING_STYLE = new StandardToStringStyle();
    
    static {
        TO_STRING_STYLE.setUseClassName(false);
        TO_STRING_STYLE.setUseFieldNames(false);
        TO_STRING_STYLE.setUseIdentityHashCode(false);
        TO_STRING_STYLE.setFieldSeparator("."); //NOI18N
        TO_STRING_STYLE.setNullText("0"); //NOI18N
        TO_STRING_STYLE.setContentStart(""); //NOI18N
        TO_STRING_STYLE.setContentEnd(""); //NOI18N
    }
    
    public boolean equals(Object aVersione) {
        if (aVersione != null && aVersione instanceof Versione) {
            return new EqualsBuilder().append(getName(), ((Versione) aVersione).getName()).
                    append(getMajor(), ((Versione) aVersione).getMajor()).
                    append(getMinor(), ((Versione) aVersione).getMinor()).
                    append(getPatch(), ((Versione) aVersione).getPatch()).
                    isEquals();
        }
        return false;
    }
    
    public int hashCode() {
        return new HashCodeBuilder().append(getName()).
                append(getMajor()).
                append(getMinor()).
                append(getPatch()).
                toHashCode();
    }
    
    public int compareTo(Object aVersione) {
        if (aVersione != null && aVersione instanceof Versione) {
            return new CompareToBuilder().append(getName(), ((Versione) aVersione).getName()).
                    append(getMajor(), ((Versione) aVersione).getMajor()).
                    append(getMinor(), ((Versione) aVersione).getMinor()).
                    append(getPatch(), ((Versione) aVersione).getPatch()).
                    toComparison();
        }
        return 1;
    }
    
    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE).append(getName()).
                append(getMajor()).
                append(getMinor()).
                append(getPatch()).toString();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMajor() {
        return major;
    }
    
    public void setMajor(Integer major) {
        this.major = major;
    }
    
    public Integer getMinor() {
        return minor;
    }
    
    public void setMinor(Integer minor) {
        this.minor = minor;
    }
    
    public Integer getPatch() {
        return patch;
    }
    
    public void setPatch(Integer patch) {
        this.patch = patch;
    }
}
