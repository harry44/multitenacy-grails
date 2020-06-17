
package org.datacontract.schemas._2004._07.bbs_epayment;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Terminal complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Terminal"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AutoAuth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AutoSale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Design" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FeeList" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="FinancialOperation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Language" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Layout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="OrderDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PaymentData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PaymentMethodActionList" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}ArrayOfPaymentMethodAction" minOccurs="0"/&gt;
 *         &lt;element name="PaymentMethodList" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RedirectOnError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RedirectUrl" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="SinglePage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TemplateData" type="{http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary}ArrayOfKeyValuePair" minOccurs="0"/&gt;
 *         &lt;element name="Vat" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="WalletProviderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Terminal", propOrder = {
    "autoAuth",
    "autoSale",
    "design",
    "feeList",
    "financialOperation",
    "language",
    "layout",
    "orderDescription",
    "paymentData",
    "paymentMethodActionList",
    "paymentMethodList",
    "redirectOnError",
    "redirectUrl",
    "singlePage",
    "templateData",
    "vat",
    "walletProviderId"
})
public class Terminal {

    @XmlElementRef(name = "AutoAuth", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> autoAuth;
    @XmlElementRef(name = "AutoSale", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> autoSale;
    @XmlElementRef(name = "Design", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> design;
    @XmlElementRef(name = "FeeList", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> feeList;
    @XmlElementRef(name = "FinancialOperation", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> financialOperation;
    @XmlElementRef(name = "Language", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> language;
    @XmlElementRef(name = "Layout", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> layout;
    @XmlElementRef(name = "OrderDescription", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> orderDescription;
    @XmlElementRef(name = "PaymentData", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> paymentData;
    @XmlElementRef(name = "PaymentMethodActionList", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfPaymentMethodAction> paymentMethodActionList;
    @XmlElementRef(name = "PaymentMethodList", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> paymentMethodList;
    @XmlElementRef(name = "RedirectOnError", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> redirectOnError;
    @XmlElementRef(name = "RedirectUrl", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> redirectUrl;
    @XmlElementRef(name = "SinglePage", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> singlePage;
    @XmlElementRef(name = "TemplateData", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfKeyValuePair> templateData;
    @XmlElementRef(name = "Vat", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> vat;
    @XmlElementRef(name = "WalletProviderId", namespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary", type = JAXBElement.class, required = false)
    protected JAXBElement<String> walletProviderId;

    /**
     * Gets the value of the autoAuth property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAutoAuth() {
        return autoAuth;
    }

    /**
     * Sets the value of the autoAuth property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAutoAuth(JAXBElement<String> value) {
        this.autoAuth = value;
    }

    /**
     * Gets the value of the autoSale property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAutoSale() {
        return autoSale;
    }

    /**
     * Sets the value of the autoSale property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAutoSale(JAXBElement<String> value) {
        this.autoSale = value;
    }

    /**
     * Gets the value of the design property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDesign() {
        return design;
    }

    /**
     * Sets the value of the design property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDesign(JAXBElement<String> value) {
        this.design = value;
    }

    /**
     * Gets the value of the feeList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFeeList() {
        return feeList;
    }

    /**
     * Sets the value of the feeList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFeeList(JAXBElement<String> value) {
        this.feeList = value;
    }

    /**
     * Gets the value of the financialOperation property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getFinancialOperation() {
        return financialOperation;
    }

    /**
     * Sets the value of the financialOperation property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setFinancialOperation(JAXBElement<String> value) {
        this.financialOperation = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLanguage(JAXBElement<String> value) {
        this.language = value;
    }

    /**
     * Gets the value of the layout property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLayout() {
        return layout;
    }

    /**
     * Sets the value of the layout property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLayout(JAXBElement<String> value) {
        this.layout = value;
    }

    /**
     * Gets the value of the orderDescription property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOrderDescription() {
        return orderDescription;
    }

    /**
     * Sets the value of the orderDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOrderDescription(JAXBElement<String> value) {
        this.orderDescription = value;
    }

    /**
     * Gets the value of the paymentData property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPaymentData() {
        return paymentData;
    }

    /**
     * Sets the value of the paymentData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPaymentData(JAXBElement<String> value) {
        this.paymentData = value;
    }

    /**
     * Gets the value of the paymentMethodActionList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPaymentMethodAction }{@code >}
     *     
     */
    public JAXBElement<ArrayOfPaymentMethodAction> getPaymentMethodActionList() {
        return paymentMethodActionList;
    }

    /**
     * Sets the value of the paymentMethodActionList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfPaymentMethodAction }{@code >}
     *     
     */
    public void setPaymentMethodActionList(JAXBElement<ArrayOfPaymentMethodAction> value) {
        this.paymentMethodActionList = value;
    }

    /**
     * Gets the value of the paymentMethodList property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPaymentMethodList() {
        return paymentMethodList;
    }

    /**
     * Sets the value of the paymentMethodList property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPaymentMethodList(JAXBElement<String> value) {
        this.paymentMethodList = value;
    }

    /**
     * Gets the value of the redirectOnError property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRedirectOnError() {
        return redirectOnError;
    }

    /**
     * Sets the value of the redirectOnError property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRedirectOnError(JAXBElement<String> value) {
        this.redirectOnError = value;
    }

    /**
     * Gets the value of the redirectUrl property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getRedirectUrl() {
        return redirectUrl;
    }

    /**
     * Sets the value of the redirectUrl property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setRedirectUrl(JAXBElement<String> value) {
        this.redirectUrl = value;
    }

    /**
     * Gets the value of the singlePage property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSinglePage() {
        return singlePage;
    }

    /**
     * Sets the value of the singlePage property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSinglePage(JAXBElement<String> value) {
        this.singlePage = value;
    }

    /**
     * Gets the value of the templateData property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfKeyValuePair }{@code >}
     *     
     */
    public JAXBElement<ArrayOfKeyValuePair> getTemplateData() {
        return templateData;
    }

    /**
     * Sets the value of the templateData property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfKeyValuePair }{@code >}
     *     
     */
    public void setTemplateData(JAXBElement<ArrayOfKeyValuePair> value) {
        this.templateData = value;
    }

    /**
     * Gets the value of the vat property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getVat() {
        return vat;
    }

    /**
     * Sets the value of the vat property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setVat(JAXBElement<String> value) {
        this.vat = value;
    }

    /**
     * Gets the value of the walletProviderId property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getWalletProviderId() {
        return walletProviderId;
    }

    /**
     * Sets the value of the walletProviderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setWalletProviderId(JAXBElement<String> value) {
        this.walletProviderId = value;
    }

}
