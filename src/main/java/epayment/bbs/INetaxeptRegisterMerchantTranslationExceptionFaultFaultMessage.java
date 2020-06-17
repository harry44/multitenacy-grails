
package epayment.bbs;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.0.4
 * 2018-01-10T12:51:57.264+05:30
 * Generated source version: 3.0.4
 */

@WebFault(name = "MerchantTranslationException", targetNamespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary")
public class INetaxeptRegisterMerchantTranslationExceptionFaultFaultMessage extends Exception {
    
    private org.datacontract.schemas._2004._07.bbs_epayment.MerchantTranslationException merchantTranslationException;

    public INetaxeptRegisterMerchantTranslationExceptionFaultFaultMessage() {
        super();
    }
    
    public INetaxeptRegisterMerchantTranslationExceptionFaultFaultMessage(String message) {
        super(message);
    }
    
    public INetaxeptRegisterMerchantTranslationExceptionFaultFaultMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public INetaxeptRegisterMerchantTranslationExceptionFaultFaultMessage(String message, org.datacontract.schemas._2004._07.bbs_epayment.MerchantTranslationException merchantTranslationException) {
        super(message);
        this.merchantTranslationException = merchantTranslationException;
    }

    public INetaxeptRegisterMerchantTranslationExceptionFaultFaultMessage(String message, org.datacontract.schemas._2004._07.bbs_epayment.MerchantTranslationException merchantTranslationException, Throwable cause) {
        super(message, cause);
        this.merchantTranslationException = merchantTranslationException;
    }

    public org.datacontract.schemas._2004._07.bbs_epayment.MerchantTranslationException getFaultInfo() {
        return this.merchantTranslationException;
    }
}