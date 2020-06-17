/*
 * Classe usata per generare e utilizzare gli id di sessione utente.
 * Ho intenzione di utilizzare
 */

package it.aessepi.utils;

import java.util.UUID;

/**
 *
 * @author Roberto Rossi
 */
public class SessionUtils {

    /*
     * Crea un sessionId attraverso l'algoritmo Java di creazione degli UUID
     */
    public static String createSessionId() {
        return UUID.randomUUID().toString();
    }

}
