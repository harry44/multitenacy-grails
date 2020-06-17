
package org.datacontract.schemas._2004._07.bbs_epayment;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PaymentInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PaymentInfo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}QueryResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AuthenticationInformation" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}AuthenticationInformation" minOccurs="0"/&gt;
 *         &lt;element name="AvtaleGiroInformation" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}AvtaleGiroInformation" minOccurs="0"/&gt;
 *         &lt;element name="CardInformation" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}CardInformation" minOccurs="0"/&gt;
 *         &lt;element name="CustomerInformation" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}CustomerInformation" minOccurs="0"/&gt;
 *         &lt;element name="DnBNorDirectPaymentInformation" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}DnBNorDirectPaymentInformation" minOccurs="0"/&gt;
 *         &lt;element name="Error" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}PaymentError" minOccurs="0"/&gt;
 *         &lt;element name="ErrorLog" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}ArrayOfPaymentError" minOccurs="0"/&gt;
 *         &lt;element name="History" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}ArrayOfTransactionLogLine" minOccurs="0"/&gt;
 *         &lt;element name="InvoiceInformation" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}InvoiceInformation" minOccurs="0"/&gt;
 *         &lt;element name="OrderInformation" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}OrderInformation" minOccurs="0"/&gt;
 *         &lt;element name="Recurring" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}Recurring" minOccurs="0"/&gt;
 *         &lt;element name="SecurityInformation" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}SecurityInformation" minOccurs="0"/&gt;
 *         &lt;element name="Summary" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}FinancialSummary" minOccurs="0"/&gt;
 *         &lt;element name="TerminalInformation" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}TerminalInformation" minOccurs="0"/&gt;
 *         &lt;element name="FraudAnalysis" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}FraudAnalysis" minOccurs="0"/&gt;
 *         &lt;element name="Mobile" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}MobileInformation" minOccurs="0"/&gt;
 *         &lt;element name="PaymentFacilitator" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}PaymentFacilitatorInformation" minOccurs="0"/&gt;
 *         &lt;element name="Wallet" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}WalletInformation" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PaymentInfo", propOrder = {
    "authenticationInformation",
    "avtaleGiroInformation",
    "cardInformation",
    "customerInformation",
    "dnBNorDirectPaymentInformation",
    "error",
    "errorLog",
    "history",
    "invoiceInformation",
    "orderInformation",
    "recurring",
    "securityInformation",
    "summary",
    "terminalInformation",
    "fraudAnalysis",
    "mobile",
    "paymentFacilitator",
    "wallet"
})
public class PaymentInfo
    extends QueryResponse
{

    @XmlElementRef(name = "AuthenticationInformation", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<AuthenticationInformation> authenticationInformation;
    @XmlElementRef(name = "AvtaleGiroInformation", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<AvtaleGiroInformation> avtaleGiroInformation;
    @XmlElementRef(name = "CardInformation", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<CardInformation> cardInformation;
    @XmlElementRef(name = "CustomerInformation", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<CustomerInformation> customerInformation;
    @XmlElementRef(name = "DnBNorDirectPaymentInformation", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<DnBNorDirectPaymentInformation> dnBNorDirectPaymentInformation;
    @XmlElementRef(name = "Error", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<PaymentError> error;
    @XmlElementRef(name = "ErrorLog", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfPaymentError> errorLog;
    @XmlElementRef(name = "History", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfTransactionLogLine> history;
    @XmlElementRef(name = "InvoiceInformation", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<InvoiceInformation> invoiceInformation;
    @XmlElementRef(name = "OrderInformation", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<OrderInformation> orderInformation;
    @XmlElementRef(name = "Recurring", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<Recurring> recurring;
    @XmlElementRef(name = "SecurityInformation", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<SecurityInformation> securityInformation;
    @XmlElementRef(name = "Summary", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<FinancialSummary> summary;
    @XmlElementRef(name = "TerminalInformation", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<TerminalInformation> terminalInformation;
    @XmlElementRef(name = "FraudAnalysis", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<FraudAnalysis> fraudAnalysis;
    @XmlElementRef(name = "Mobile", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<MobileInformation> mobile;
    @XmlElementRef(name = "PaymentFacilitator", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<PaymentFacilitatorInformation> paymentFacilitator;
    @XmlElementRef(name = "Wallet", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<WalletInformation> wallet;

    /**
     * Gets the value of the authenticationInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AuthenticationInformation }{@code >}
     *     
     */
    public JAXBElement<AuthenticationInformation> getAuthenticationInformation() {
        return authenticationInformation;
    }

    /**
     * Sets the value of the authenticationInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AuthenticationInformation }{@code >}
     *     
     */
    public void setAuthenticationInformation(JAXBElement<AuthenticationInformation> value) {
        this.authenticationInformation = value;
    }

    /**
     * Gets the value of the avtaleGiroInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link AvtaleGiroInformation }{@code >}
     *     
     */
    public JAXBElement<AvtaleGiroInformation> getAvtaleGiroInformation() {
        return avtaleGiroInformation;
    }

    /**
     * Sets the value of the avtaleGiroInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link AvtaleGiroInformation }{@code >}
     *     
     */
    public void setAvtaleGiroInformation(JAXBElement<AvtaleGiroInformation> value) {
        this.avtaleGiroInformation = value;
    }

    /**
     * Gets the value of the cardInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CardInformation }{@code >}
     *     
     */
    public JAXBElement<CardInformation> getCardInformation() {
        return cardInformation;
    }

    /**
     * Sets the value of the cardInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CardInformation }{@code >}
     *     
     */
    public void setCardInformation(JAXBElement<CardInformation> value) {
        this.cardInformation = value;
    }

    /**
     * Gets the value of the customerInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link CustomerInformation }{@code >}
     *     
     */
    public JAXBElement<CustomerInformation> getCustomerInformation() {
        return customerInformation;
    }

    /**
     * Sets the value of the customerInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link CustomerInformation }{@code >}
     *     
     */
    public void setCustomerInformation(JAXBElement<CustomerInformation> value) {
        this.customerInformation = value;
    }

    /**
     * Gets the value of the dnBNorDirectPaymentInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DnBNorDirectPaymentInformation }{@code >}
     *     
     */
    public JAXBElement<DnBNorDirectPaymentInformation> getDnBNorDirectPaymentInformation() {
        return dnBNorDirectPaymentInformation;
    }

    /**
     * Sets the value of the dnBNorDirectPaymentInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DnBNorDirectPaymentInformation }{@code >}
     *     
     */
    public void setDnBNorDirectPaymentInformation(JAXBElement<DnBNorDirectPaymentInformation> value) {
        this.dnBNorDirectPaymentInformation = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link PaymentError }{@code >}
     *     
     */
    public JAXBElement<PaymentError> getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link PaymentError }{@code >}
     *     
     */
    public void setError(JAXBElement<PaymentError> value) {
        this.error = value;
    }

    /**
     * Gets the value of the errorLog property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPaymentError }{@code >}
     *     
     */
    public JAXBElement<ArrayOfPaymentError> getErrorLog() {
        return errorLog;
    }

    /**
     * Sets the value of the errorLog property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPaymentError }{@code >}
     *     
     */
    public void setErrorLog(JAXBElement<ArrayOfPaymentError> value) {
        this.errorLog = value;
    }

    /**
     * Gets the value of the history property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTransactionLogLine }{@code >}
     *     
     */
    public JAXBElement<ArrayOfTransactionLogLine> getHistory() {
        return history;
    }

    /**
     * Sets the value of the history property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfTransactionLogLine }{@code >}
     *     
     */
    public void setHistory(JAXBElement<ArrayOfTransactionLogLine> value) {
        this.history = value;
    }

    /**
     * Gets the value of the invoiceInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link InvoiceInformation }{@code >}
     *     
     */
    public JAXBElement<InvoiceInformation> getInvoiceInformation() {
        return invoiceInformation;
    }

    /**
     * Sets the value of the invoiceInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link InvoiceInformation }{@code >}
     *     
     */
    public void setInvoiceInformation(JAXBElement<InvoiceInformation> value) {
        this.invoiceInformation = value;
    }

    /**
     * Gets the value of the orderInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link OrderInformation }{@code >}
     *     
     */
    public JAXBElement<OrderInformation> getOrderInformation() {
        return orderInformation;
    }

    /**
     * Sets the value of the orderInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link OrderInformation }{@code >}
     *     
     */
    public void setOrderInformation(JAXBElement<OrderInformation> value) {
        this.orderInformation = value;
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
     * Gets the value of the securityInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link SecurityInformation }{@code >}
     *     
     */
    public JAXBElement<SecurityInformation> getSecurityInformation() {
        return securityInformation;
    }

    /**
     * Sets the value of the securityInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link SecurityInformation }{@code >}
     *     
     */
    public void setSecurityInformation(JAXBElement<SecurityInformation> value) {
        this.securityInformation = value;
    }

    /**
     * Gets the value of the summary property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FinancialSummary }{@code >}
     *     
     */
    public JAXBElement<FinancialSummary> getSummary() {
        return summary;
    }

    /**
     * Sets the value of the summary property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FinancialSummary }{@code >}
     *     
     */
    public void setSummary(JAXBElement<FinancialSummary> value) {
        this.summary = value;
    }

    /**
     * Gets the value of the terminalInformation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link TerminalInformation }{@code >}
     *     
     */
    public JAXBElement<TerminalInformation> getTerminalInformation() {
        return terminalInformation;
    }

    /**
     * Sets the value of the terminalInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link TerminalInformation }{@code >}
     *     
     */
    public void setTerminalInformation(JAXBElement<TerminalInformation> value) {
        this.terminalInformation = value;
    }

    /**
     * Gets the value of the fraudAnalysis property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link FraudAnalysis }{@code >}
     *     
     */
    public JAXBElement<FraudAnalysis> getFraudAnalysis() {
        return fraudAnalysis;
    }

    /**
     * Sets the value of the fraudAnalysis property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FraudAnalysis }{@code >}
     *     
     */
    public void setFraudAnalysis(JAXBElement<FraudAnalysis> value) {
        this.fraudAnalysis = value;
    }

    /**
     * Gets the value of the mobile property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MobileInformation }{@code >}
     *     
     */
    public JAXBElement<MobileInformation> getMobile() {
        return mobile;
    }

    /**
     * Sets the value of the mobile property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MobileInformation }{@code >}
     *     
     */
    public void setMobile(JAXBElement<MobileInformation> value) {
        this.mobile = value;
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
     * Gets the value of the wallet property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link WalletInformation }{@code >}
     *     
     */
    public JAXBElement<WalletInformation> getWallet() {
        return wallet;
    }

    /**
     * Sets the value of the wallet property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link WalletInformation }{@code >}
     *     
     */
    public void setWallet(JAXBElement<WalletInformation> value) {
        this.wallet = value;
    }

}
