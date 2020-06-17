/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.api.exception;

/**
 *
 * @author bogdan
 */
public class BusinessRuleException extends TitledException {

    public BusinessRuleException() {
        super(bundle.getString("businessrule_exception_message"), bundle.getString("businessrule_exception_title"));
    }

    public BusinessRuleException(String message) {
        super(message, bundle.getString("businessrule_exception_title"));
    }

    public BusinessRuleException(String message, String title) {
        super(message, title);
    }

    public BusinessRuleException(String message, String title, Throwable cause) {
        super(message, title, cause);
    }

    
    
}
