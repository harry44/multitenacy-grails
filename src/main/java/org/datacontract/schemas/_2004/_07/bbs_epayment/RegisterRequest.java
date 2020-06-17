
package org.datacontract.schemas._2004._07.bbs_epayment;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RegisterRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RegisterRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AcquirerInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AvtaleGiro" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}AvtaleGiro" minOccurs="0"/&gt;
 *         &lt;element name="CardInfo" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}CardInfo" minOccurs="0"/&gt;
 *         &lt;element name="Customer" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}Customer" minOccurs="0"/&gt;
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DnBNorDirectPayment" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}DnBNorDirectPayment" minOccurs="0"/&gt;
 *         &lt;element name="Environment" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}Environment" minOccurs="0"/&gt;
 *         &lt;element name="Fraud" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}Fraud" minOccurs="0"/&gt;
 *         &lt;element name="MicroPayment" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}MicroPayment" minOccurs="0"/&gt;
 *         &lt;element name="Order" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}Order" minOccurs="0"/&gt;
 *         &lt;element name="PaymentFacilitator" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}PaymentFacilitatorInformation" minOccurs="0"/&gt;
 *         &lt;element name="Recurring" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}Recurring" minOccurs="0"/&gt;
 *         &lt;element name="ServiceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Terminal" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}Terminal" minOccurs="0"/&gt;
 *         &lt;element name="Token" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}Token" minOccurs="0"/&gt;
 *         &lt;element name="TransactionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TransactionReconRef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegisterRequest", propOrder = {
    "acquirerInfo",
    "avtaleGiro",
    "cardInfo",
    "customer",
    "description",
    "dnBNorDirectPayment",
    "environment",
    "fraud",
    "microPayment",
    "order",
    "paymentFacilitator",
    "recurring",
    "serviceType",
    "terminal",
    "token",
    "transactionId",
    "transactionReconRef"
})
public class RegisterRequest {

    @XmlElementRef(name = "AcquirerInfo", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> acquirerInfo;
    @XmlElementRef(name = "AvtaleGiro", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<AvtaleGiro> avtaleGiro;
    @XmlElementRef(name = "CardInfo", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<CardInfo> cardInfo;
    @XmlElementRef(name = "Customer", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<Customer> customer;
    @XmlElementRef(name = "Description", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> description;
    @XmlElementRef(name = "DnBNorDirectPayment", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<DnBNorDirectPayment> dnBNorDirectPayment;
    @XmlElementRef(name = "Environment", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<Environment> environment;
    @XmlElementRef(name = "Fraud", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<Fraud> fraud;
    @XmlElementRef(name = "MicroPayment", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<MicroPayment> microPayment;
    @XmlElementRef(name = "Order", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<Order> order;
    @XmlElementRef(name = "PaymentFacilitator", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<PaymentFacilitatorInformation> paymentFacilitator;
    @XmlElementRef(name = "Recurring", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<Recurring> recurring;
    @XmlElementRef(name = "ServiceType", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> serviceType;
    @XmlElementRef(name = "Terminal", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<Terminal> terminal;
    @XmlElementRef(name = "Token", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<Token> token;
    @XmlElementRef(name = "TransactionId", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> transactionId;
    @XmlElementRef(name = "TransactionReconRef", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> transactionReconRef;

    /**
     * Gets the value of the acquirerInfo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAcquirerInfo() {
        return acquirerInfo;
    }

    /**
     * Sets the value of the acquirerInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAcquirerInfo(JAXBElement<String> value) {
        this.acquirerInfo = value;
    }

    /**
     * Gets the value of the avtaleGiro property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AvtaleGiro }{@code >}
     *     
     */
    public JAXBElement<AvtaleGiro> getAvtaleGiro() {
        return avtaleGiro;
    }

    /**
     * Sets the value of the avtaleGiro property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AvtaleGiro }{@code >}
     *     
     */
    public void setAvtaleGiro(JAXBElement<AvtaleGiro> value) {
        this.avtaleGiro = value;
    }

    /**
     * Gets the value of the cardInfo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CardInfo }{@code >}
     *     
     */
    public JAXBElement<CardInfo> getCardInfo() {
        return cardInfo;
    }

    /**
     * Sets the value of the cardInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CardInfo }{@code >}
     *     
     */
    public void setCardInfo(JAXBElement<CardInfo> value) {
        this.cardInfo = value;
    }

    /**
     * Gets the value of the customer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Customer }{@code >}
     *     
     */
    public JAXBElement<Customer> getCustomer() {
        return customer;
    }

    /**
     * Sets the value of the customer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Customer }{@code >}
     *     
     */
    public void setCustomer(JAXBElement<Customer> value) {
        this.customer = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDescription(JAXBElement<String> value) {
        this.description = value;
    }

    /**
     * Gets the value of the dnBNorDirectPayment property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DnBNorDirectPayment }{@code >}
     *     
     */
    public JAXBElement<DnBNorDirectPayment> getDnBNorDirectPayment() {
        return dnBNorDirectPayment;
    }

    /**
     * Sets the value of the dnBNorDirectPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DnBNorDirectPayment }{@code >}
     *     
     */
    public void setDnBNorDirectPayment(JAXBElement<DnBNorDirectPayment> value) {
        this.dnBNorDirectPayment = value;
    }

    /**
     * Gets the value of the environment property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Environment }{@code >}
     *     
     */
    public JAXBElement<Environment> getEnvironment() {
        return environment;
    }

    /**
     * Sets the value of the environment property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Environment }{@code >}
     *     
     */
    public void setEnvironment(JAXBElement<Environment> value) {
        this.environment = value;
    }

    /**
     * Gets the value of the fraud property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Fraud }{@code >}
     *     
     */
    public JAXBElement<Fraud> getFraud() {
        return fraud;
    }

    /**
     * Sets the value of the fraud property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Fraud }{@code >}
     *     
     */
    public void setFraud(JAXBElement<Fraud> value) {
        this.fraud = value;
    }

    /**
     * Gets the value of the microPayment property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MicroPayment }{@code >}
     *     
     */
    public JAXBElement<MicroPayment> getMicroPayment() {
        return microPayment;
    }

    /**
     * Sets the value of the microPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MicroPayment }{@code >}
     *     
     */
    public void setMicroPayment(JAXBElement<MicroPayment> value) {
        this.microPayment = value;
    }

    /**
     * Gets the value of the order property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Order }{@code >}
     *     
     */
    public JAXBElement<Order> getOrder() {
        return order;
    }

    /**
     * Sets the value of the order property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Order }{@code >}
     *     
     */
    public void setOrder(JAXBElement<Order> value) {
        this.order = value;
    }

    /**
     * Gets the value of the paymentFacilitator property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PaymentFacilitatorInformation }{@code >}
     *     
     */
    public JAXBElement<PaymentFacilitatorInformation> getPaymentFacilitator() {
        return paymentFacilitator;
    }

    /**
     * Sets the value of the paymentFacilitator property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PaymentFacilitatorInformation }{@code >}
     *     
     */
    public void setPaymentFacilitator(JAXBElement<PaymentFacilitatorInformation> value) {
        this.paymentFacilitator = value;
    }

    /**
     * Gets the value of the recurring property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Recurring }{@code >}
     *     
     */
    public JAXBElement<Recurring> getRecurring() {
        return recurring;
    }

    /**
     * Sets the value of the recurring property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Recurring }{@code >}
     *     
     */
    public void setRecurring(JAXBElement<Recurring> value) {
        this.recurring = value;
    }

    /**
     * Gets the value of the serviceType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getServiceType() {
        return serviceType;
    }

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setServiceType(JAXBElement<String> value) {
        this.serviceType = value;
    }

    /**
     * Gets the value of the terminal property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Terminal }{@code >}
     *     
     */
    public JAXBElement<Terminal> getTerminal() {
        return terminal;
    }

    /**
     * Sets the value of the terminal property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Terminal }{@code >}
     *     
     */
    public void setTerminal(JAXBElement<Terminal> value) {
        this.terminal = value;
    }

    /**
     * Gets the value of the token property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Token }{@code >}
     *     
     */
    public JAXBElement<Token> getToken() {
        return token;
    }

    /**
     * Sets the value of the token property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Token }{@code >}
     *     
     */
    public void setToken(JAXBElement<Token> value) {
        this.token = value;
    }

    /**
     * Gets the value of the transactionId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the value of the transactionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTransactionId(JAXBElement<String> value) {
        this.transactionId = value;
    }

    /**
     * Gets the value of the transactionReconRef property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTransactionReconRef() {
        return transactionReconRef;
    }

    /**
     * Sets the value of the transactionReconRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTransactionReconRef(JAXBElement<String> value) {
        this.transactionReconRef = value;
    }

}
