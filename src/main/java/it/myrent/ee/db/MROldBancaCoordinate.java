/*
 * BancaCoordinate.java
 *
 * Created on 20 februarie 2007, 09:52
 *
 */

package it.myrent.ee.db;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author jamess
 */
public class MROldBancaCoordinate implements Serializable {
    
    /** Creates a new instance of MROldBancaCoordinate */
    public MROldBancaCoordinate() {
    }
    
    public MROldBancaCoordinate(String cab, String abi) {
        setCab(cab);
        setAbi(abi);
    }
    
    private String cab;
    private String abi;

    public String getCab() {
        return cab;
    }

    public void setCab(String cab) {
        this.cab = cab;
    }

    public String getAbi() {
        return abi;
    }

    public void setAbi(String abi) {
        this.abi = abi;
    }
    
    public boolean equals(Object other) {
        if(other != null && other instanceof MROldBancaCoordinate) {
            return new EqualsBuilder().append(getAbi(), ((MROldBancaCoordinate)other).getAbi())
            .append(getCab(), ((MROldBancaCoordinate)other).getCab()).isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (this.cab != null ? this.cab.hashCode() : 0);
        hash = 43 * hash + (this.abi != null ? this.abi.hashCode() : 0);
        return hash;
    }
    
}
