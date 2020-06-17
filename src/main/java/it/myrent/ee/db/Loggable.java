/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

/**
 *
 * @author jamess
 */
public interface Loggable {
    public String[] getLoggableFields();
    public String[] getLoggableLabels();
    public String getEntityName();
    public Integer getEntityId();
}
