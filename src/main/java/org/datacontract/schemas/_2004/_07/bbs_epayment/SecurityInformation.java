
package org.datacontract.schemas._2004._07.bbs_epayment;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SecurityInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SecurityInformation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CustomerIPCountry" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="IPCountryMatchesIssuingCountry" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SecurityInformation", propOrder = {
    "customerIPCountry",
    "ipCountryMatchesIssuingCountry"
})
public class SecurityInformation {

    @XmlElementRef(name = "CustomerIPCountry", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customerIPCountry;
    @XmlElement(name = "IPCountryMatchesIssuingCountry")
    protected Boolean ipCountryMatchesIssuingCountry;

    /**
     * Gets the value of the customerIPCountry property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomerIPCountry() {
        return customerIPCountry;
    }

    /**
     * Sets the value of the customerIPCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomerIPCountry(JAXBElement<String> value) {
        this.customerIPCountry = value;
    }

    /**
     * Gets the value of the ipCountryMatchesIssuingCountry property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIPCountryMatchesIssuingCountry() {
        return ipCountryMatchesIssuingCountry;
    }

    /**
     * Sets the value of the ipCountryMatchesIssuingCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIPCountryMatchesIssuingCountry(Boolean value) {
        this.ipCountryMatchesIssuingCountry = value;
    }

}
