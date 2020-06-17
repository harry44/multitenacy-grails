/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.api.exception;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author bogdan
 */
public class FatturaVuotaException extends TitledException {

    public FatturaVuotaException(String message) {
        super(message, bundle.getString("fattura_vuota_title"));
    }
}
