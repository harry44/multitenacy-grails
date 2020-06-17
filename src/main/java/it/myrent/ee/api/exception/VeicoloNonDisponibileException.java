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
public class VeicoloNonDisponibileException extends Exception {

    public VeicoloNonDisponibileException(String message) {
        super(message);
    }

    public void showMessage(Component parent, String title) {
        JOptionPane.showMessageDialog(parent, getMessage(), title, JOptionPane.ERROR_MESSAGE);
    }
}
