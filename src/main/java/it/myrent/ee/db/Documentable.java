/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import java.util.Set;

/**
 * Interfaccia per le entita' alle quali possono essere associati documenti.
 * @author bogdan
 */
public interface Documentable extends PersistentInstance {

    public String getDocumentableName();

    public Class getDocumentableClass();

    public Set getDocumenti();

    public void setDocumenti(Set documenti);

    public String getKeywords();

    public String toString();
}
