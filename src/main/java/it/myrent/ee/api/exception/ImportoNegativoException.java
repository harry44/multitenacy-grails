/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.api.exception;

/**
 *
 * @author bogdan
 */
public class ImportoNegativoException extends BusinessRuleException {

    public ImportoNegativoException() {
        super(bundle.getString("importo_negativo_exception_message"), bundle.getString("importo_negativo_exception_title"));
    }

    public ImportoNegativoException(String message) {
       super(message, bundle.getString("importo_negativo_exception_title"));
    }

    public ImportoNegativoException(String message, String title) {
        super(message, title);
    }

}
