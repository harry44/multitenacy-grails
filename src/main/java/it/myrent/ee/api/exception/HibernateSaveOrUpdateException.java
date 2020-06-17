/*
 * HibernateDeleteException.java
 *
 * Created on 10 octombrie 2005, 10:58
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package it.myrent.ee.api.exception;

/**
 *
 * @author jamess
 */
public class HibernateSaveOrUpdateException extends DatabaseException{
    
    public HibernateSaveOrUpdateException(Throwable cause) {
        super(bundle.getString("save_error_message"), bundle.getString("save_error_title"), cause);
    }
}
