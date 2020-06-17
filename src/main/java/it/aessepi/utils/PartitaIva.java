/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.utils;

/**
 *
 * @author jamess
 */
public class PartitaIva {

    public static boolean controlloPartitaIva(String s) {
        int i, c, sum = 0;
        if (s.length() != 11) {
            return false;
        }
        for (i = 0; i < 11; i++) {
            if (s.charAt(i) < '0' || s.charAt(i) > '9') {
                return false;
            }
        }
        for (i = 0; i <= 9; i += 2) {
            sum += s.charAt(i) - '0';
        }
        for (i = 1; i <= 9; i += 2) {
            c = 2 * (s.charAt(i) - '0');
            if (c > 9) {
                c = c - 9;
            }
            sum += c;
        }
        if ((10 - sum % 10) % 10 != s.charAt(10) - '0') {
            return false;
        }
        return true;
    }
}
