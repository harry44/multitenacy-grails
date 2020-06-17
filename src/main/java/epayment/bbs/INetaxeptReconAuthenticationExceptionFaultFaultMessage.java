
package epayment.bbs;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 3.0.4
 * 2018-01-10T12:51:57.397+05:30
 * Generated source version: 3.0.4
 */

@WebFault(name = "AuthenticationException", targetNamespace = "http://schemas.datacontract.org/2004/07/BBS.EPayment.ServiceLibrary")
public class INetaxeptReconAuthenticationExceptionFaultFaultMessage extends Exception {
    
    private org.datacontract.schemas._2004._07.bbs_epayment.AuthenticationException authenticationException;

    public INetaxeptReconAuthenticationExceptionFaultFaultMessage() {
        super();
    }
    
    public INetaxeptReconAuthenticationExceptionFaultFaultMessage(String message) {
        super(message);
    }
    
    public INetaxeptReconAuthenticationExceptionFaultFaultMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public INetaxeptReconAuthenticationExceptionFaultFaultMessage(String message, org.datacontract.schemas._2004._07.bbs_epayment.AuthenticationException authenticationException) {
        super(message);
        this.authenticationException = authenticationException;
    }

    public INetaxeptReconAuthenticationExceptionFaultFaultMessage(String message, org.datacontract.schemas._2004._07.bbs_epayment.AuthenticationException authenticationException, Throwable cause) {
        super(message, cause);
        this.authenticationException = authenticationException;
    }

    public org.datacontract.schemas._2004._07.bbs_epayment.AuthenticationException getFaultInfo() {
        return this.authenticationException;
    }
}