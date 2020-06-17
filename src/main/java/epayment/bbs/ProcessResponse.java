
package epayment.bbs;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProcessResult" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}ProcessResponse" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "processResult"
})
@XmlRootElement(name = "ProcessResponse")
public class ProcessResponse {

    @XmlElementRef(name = "ProcessResult", namespace = "http://BBS.EPayment", type = JAXBElement.class, required = false)
    protected JAXBElement<org.datacontract.schemas._2004._07.bbs_epayment.ProcessResponse> processResult;

    /**
     * Gets the value of the processResult property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.bbs_epayment.ProcessResponse }{@code >}
     *     
     */
    public JAXBElement<org.datacontract.schemas._2004._07.bbs_epayment.ProcessResponse> getProcessResult() {
        return processResult;
    }

    /**
     * Sets the value of the processResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link org.datacontract.schemas._2004._07.bbs_epayment.ProcessResponse }{@code >}
     *     
     */
    public void setProcessResult(JAXBElement<org.datacontract.schemas._2004._07.bbs_epayment.ProcessResponse> value) {
        this.processResult = value;
    }

}
