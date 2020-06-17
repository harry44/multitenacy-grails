/*
 * PersistentObject.java
 *
 * Created on 19 februarie 2007, 15:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.aessepi.utils.db;

import java.io.Serializable;

/**
 *
 * @author jamess
 */
public interface PersistentObject extends Serializable {
    public Serializable getIdentifier();
}
