/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.api.utils;


import it.myrent.ee.db.MROldCodiceFiscale;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

/**
 *
 * @author bogdan
 */
public class CodiceFiscaleUtils {

    public static final String SPECIAL_STRANGER_FISCAL_CODE = "9999999999999999";

    public static MROldCodiceFiscale[] searchCodiceFiscaleByCodice(Session sx, String codice) {
        if(codice == null) {
            return new MROldCodiceFiscale[0];
        }
        return ((List<MROldCodiceFiscale>)sx.createQuery("select c from MROldCodiceFiscale c where lower(c.codice) = :codice or lower(c.codice2)  = :codice").
                setParameter("codice", codice.toLowerCase()).list()).toArray(new MROldCodiceFiscale[0]);
    }

    public static MROldCodiceFiscale searchCodiceFiscaleByLuogo(Session sx, String luogo) {
        if(luogo == null) {
            return null;
        }
        return (MROldCodiceFiscale) sx.createQuery("select c from MROldCodiceFiscale c where lower(c.luogo) = :luogo").
                setParameter("luogo", luogo.toLowerCase()).
                uniqueResult();
    }

    public static String[] searchLuogoForCodice(Session sx, String codice) {
        MROldCodiceFiscale[] codiceFiscale = searchCodiceFiscaleByCodice(sx, codice);
        String[] luoghi = new String[codiceFiscale.length];
        for(int i = 0; i < codiceFiscale.length; i++) {
            luoghi[i] = codiceFiscale[i].getLuogo();
        }
        return luoghi;
    }

    public static String[] searchCodiceForLuogo(Session sx, String luogo) {
        MROldCodiceFiscale codiceFiscale = searchCodiceFiscaleByLuogo(sx, luogo);
        if(codiceFiscale != null) {
            return new String[]{codiceFiscale.getCodice(), codiceFiscale.getCodice2()};
        }
        return null;
    }

    public static Map leggiCodiciFiscali(Session sx) {
        HashMap<String, String> mapCodiciFiscali = new HashMap();
        Iterator<Map> codiciFiscali = (Iterator<Map>) sx.createQuery("select new map(c.codice as codice, c.luogo as luogo) from MROldCodiceFiscale c").list().iterator();
        while(codiciFiscali.hasNext()) {
            Map<String, String> aCodiceMap = (Map<String, String>) codiciFiscali.next();
            mapCodiciFiscali.put(aCodiceMap.get("codice"), aCodiceMap.get("luogo"));
        }
        return mapCodiciFiscali;
    }
}
