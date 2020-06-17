/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.api.utils;

import it.myrent.ee.db.MROldNazioneISO;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.hibernate.Session;

/**
 *
 * @author jamess
 */
public class NazioneISOUtils {
    public static MROldNazioneISO searchNazioneByISOCode(Session sx, String codice) {
        if(codice == null) {
            return null;
        }        
        return (MROldNazioneISO) sx.createQuery("select n from MROldNazioneISO n where lower(n.codice) = :codice").
                setParameter("codice", codice.toLowerCase()).
                uniqueResult();
    }

    public static MROldNazioneISO searchNazioneByISO3Code(Session sx, String codice3) {
        if(codice3 == null) {
            return null;
        }
        return (MROldNazioneISO) sx.createQuery("select n from MROldNazioneISO n where lower(n.codice3) = :codice3").
                setParameter("codice3", codice3.toLowerCase()).
                uniqueResult();
    }

    public static MROldNazioneISO searchNazioneByName(Session sx, String nome) {
        if(nome == null) {
            return null;
        }
        return (MROldNazioneISO) sx.createQuery("select n from MROldNazioneISO n where lower(n.nome) = :nome").
                setParameter("nome", nome.toLowerCase()).
                uniqueResult();
    }

    public static String searchNameForISOCode(Session sx, String codice) {
        MROldNazioneISO nazione = searchNazioneByISOCode(sx, codice);
        if(nazione != null) {
            return nazione.getNome();
        }
        return null;
    }

    public static String searchNameForISO3Code(Session sx, String codice3) {
        MROldNazioneISO nazione = searchNazioneByISO3Code(sx, codice3);
        if(nazione != null) {
            return nazione.getNome();
        }
        return null;
    }

    public static String searchISOCodeForName(Session sx, String nome) {
        MROldNazioneISO nazione = searchNazioneByName(sx, nome);
        if(nazione != null) {
            return nazione.getCodice();
        }
        return null;
    }

    public static String searchISO3CodeForName(Session sx, String nome) {
        MROldNazioneISO nazione = searchNazioneByName(sx, nome);
        if(nazione != null) {
            return nazione.getCodice3();
        }
        return null;
    }

    public static Map<String, String> leggiNazioniISO3(Session sx) {
        HashMap<String, String> mapCodiciISO3 = new HashMap();
        Iterator<Map> codiciISO3 = (Iterator<Map>) sx.createQuery("select new map(n.nome as nome, n.codice3 as codice) from MROldNazioneISO n").list().iterator();
        while(codiciISO3.hasNext()) {
            Map<String, String> aCodiceMap = (Map<String, String>)codiciISO3.next();
            mapCodiciISO3.put(aCodiceMap.get("nome"), aCodiceMap.get("codice"));
        }
        return mapCodiciISO3;
    }

    public static Map<String, String> leggiNazioniISO2(Session sx) {
        HashMap<String, String> mapCodiciISO2 = new HashMap();
        Iterator<Map> codiciISO3 = (Iterator<Map>) sx.createQuery("select new map(n.nome as nome, n.codice as codice) from MROldNazioneISO n").list().iterator();
        while(codiciISO3.hasNext()) {
            Map<String, String> aCodiceMap = (Map<String, String>)codiciISO3.next();
            mapCodiciISO2.put(aCodiceMap.get("nome"), aCodiceMap.get("codice"));
        }
        return mapCodiciISO2;
    }
}
