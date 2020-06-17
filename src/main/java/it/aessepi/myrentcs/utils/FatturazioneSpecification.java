/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.aessepi.myrentcs.utils;

import java.util.Date;
import org.hibernate.Session;

/**
 *
 * @author bogdan
 */
public interface FatturazioneSpecification {

    public boolean isSatisfiedBy(Date inizio, Date fine, Integer giorniVoucher);
    public boolean isSatisfiedBy(Session sx, Date inizio, Date fine, Integer giorniVoucher);

}
