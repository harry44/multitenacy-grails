
package org.datacontract.schemas._2004._07.bbs_epayment;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PaymentFacilitatorInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentFacilitatorInformation"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="MCC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MerchantDescriptor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SubMerchantId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentFacilitatorInformation", propOrder = {
    "mcc",
    "merchantDescriptor",
    "subMerchantId"
})
public class PaymentFacilitatorInformation {

    @XmlElementRef(name = "MCC", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> mcc;
    @XmlElementRef(name = "MerchantDescriptor", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> merchantDescriptor;
    @XmlElementRef(name = "SubMerchantId", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> subMerchantId;

    /**
     * Gets the value of the mcc property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMCC() {
        return mcc;
    }

    /**
     * Sets the value of the mcc property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMCC(JAXBElement<String> value) {
        this.mcc = value;
    }

    /**
     * Gets the value of the merchantDescriptor property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMerchantDescriptor() {
        return merchantDescriptor;
    }

    /**
     * Sets the value of the merchantDescriptor property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMerchantDescriptor(JAXBElement<String> value) {
        this.merchantDescriptor = value;
    }

    /**
     * Gets the value of the subMerchantId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSubMerchantId() {
        return subMerchantId;
    }

    /**
     * Sets the value of the subMerchantId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSubMerchantId(JAXBElement<String> value) {
        this.subMerchantId = value;
    }

}
