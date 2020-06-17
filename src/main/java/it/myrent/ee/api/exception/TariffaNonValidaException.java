/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.api.exception;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author jamess
 */
public class TariffaNonValidaException extends Exception {

    public TariffaNonValidaException(String message) {
        super(message);
    }

    public String showMessage() {
        //JOptionPane.showMessageDialog(parent, getMessage(), title, JOptionPane.ERROR_MESSAGE);
        return getMessage();
    }
}
