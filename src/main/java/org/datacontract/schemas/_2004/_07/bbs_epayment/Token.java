
package org.datacontract.schemas._2004._07.bbs_epayment;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Token complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Token"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="GenerateToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TokenExpiryDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TokenReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Token", propOrder = {
    "generateToken",
    "tokenExpiryDate",
    "tokenReference"
})
public class Token {

    @XmlElementRef(name = "GenerateToken", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> generateToken;
    @XmlElementRef(name = "TokenExpiryDate", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> tokenExpiryDate;
    @XmlElementRef(name = "TokenReference", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> tokenReference;

    /**
     * Gets the value of the generateToken property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getGenerateToken() {
        return generateToken;
    }

    /**
     * Sets the value of the generateToken property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setGenerateToken(JAXBElement<String> value) {
        this.generateToken = value;
    }

    /**
     * Gets the value of the tokenExpiryDate property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTokenExpiryDate() {
        return tokenExpiryDate;
    }

    /**
     * Sets the value of the tokenExpiryDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTokenExpiryDate(JAXBElement<String> value) {
        this.tokenExpiryDate = value;
    }

    /**
     * Gets the value of the tokenReference property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTokenReference() {
        return tokenReference;
    }

    /**
     * Sets the value of the tokenReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTokenReference(JAXBElement<String> value) {
        this.tokenReference = value;
    }

}
