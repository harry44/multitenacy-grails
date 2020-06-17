/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author rashmi
 */
public class MROldProductNetwork implements it.aessepi.utils.db.PersistentInstance,Comparable<MROldProductNetwork> {
  
    private Integer id;
    private String code;
    private String productDescription;
    private MROldFonteCommissione fonteCommissione;
    private MROldLocationNetwork locationNetwork;

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
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the productDescription
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * @param productDescription the productDescription to set
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     * @return the fonteCommissione
     */
    public MROldFonteCommissione getFonteCommissione() {
        return fonteCommissione;
    }

    /**
     * @param fonteCommissione the fonteCommissione to set
     */
    public void setFonteCommissione(MROldFonteCommissione fonteCommissione) {
        this.fonteCommissione = fonteCommissione;
    }

    /**
     * @return the locationNetwork
     */
    public MROldLocationNetwork getLocationNetwork() {
        return locationNetwork;
    }

    /**
     * @param locationNetwork the locationNetwork to set
     */
    public void setLocationNetwork(MROldLocationNetwork locationNetwork) {
        this.locationNetwork = locationNetwork;
    }

   @Override
    public int compareTo(MROldProductNetwork o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getProductDescription(), o.getProductDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

     public boolean equals(Object other) {
        if (!(other instanceof MROldProductNetwork)) {
            return false;
        }
        MROldProductNetwork castOther = (MROldProductNetwork) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
 @Override
    public String toString() {
      return productDescription != null ? productDescription : new String();
    }

}
