/*
 * NumerazioniUtils.java
 *
 * Created on 05 iunie 2007, 15:30
 *
 */
package it.myrent.ee.api.utils;

import grails.util.Holders;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.Parameters;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.db.*;
import it.myrent.ee.db.MROldAffiliato;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;

/**
 *
 * @author jamess
 */
public class NumerazioniUtils {

    /** Creates a new instance of NumerazioniUtils */
    public NumerazioniUtils() {
    }
    private static final Log log = LogFactory.getLog(NumerazioniUtils.class);
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/utils/Bundle");

    /**
     * Rilegge la numerazione se diversa da null. Usa il metodo
     * <a href="org.hibernate.Session">Session</a><code>.get(Class, Serializable)</code>
     *
     * @return La numerazione riletta dal database.
     * @param sx La sessione usata per leggere la numerazione.
     * @param numerazione La <a href="it.aessepi.myrent.contabilita.db.MROldNumerazione>numerazione</a> da rileggere.
     * @throws org.hibernate.HibernateException Se qualcosa va storto col database...
     */
    public static MROldNumerazione getNumerazione(Session sx, MROldNumerazione numerazione) throws HibernateException {
        if (numerazione != null) {
            return (MROldNumerazione) sx.get(MROldNumerazione.class, numerazione.getId());
        }
        return null;
    }


    /**
     * Carica una numerazione per il documento specificato, accessibile all'utente
     * collegato. Cerca in ordine tra le numerazioni della sede, affiliato e generiche.
     * @param sx
     * @param documento
     * @return
     * @throws HibernateException
     */
    public static MROldNumerazione getNumerazione(Session sx, String documento) throws HibernateException {
        MROldNumerazione numerazione = null;
        if (Parameters.getUser() != null) {
            User user = (User) sx.get(UserImpl.class, Parameters.getUser().getId());
            numerazione = checkPreferencesAffiliato(sx, documento, user);
            if(numerazione==null) {
                if (user.getSedeOperativa() != null) {
                    numerazione = (MROldNumerazione) user.getSedeOperativa().getNumerazioni().get(documento);
                }
                if (numerazione == null) {
                    numerazione = (MROldNumerazione) user.getAffiliato().getNumerazioni().get(documento);
                }
            }
        }
        if (numerazione == null) {
            numerazione = (MROldNumerazione) sx.createQuery(
                    "select n from MROldNumerazioneGenerica n where n.documento = :documento").
                    setParameter("documento", documento)
                    .setCacheable(true).
                    uniqueResult();
        }
        return numerazione;
    }

    public static MROldNumerazione getNumerazione(Session sx, String documento, User user) throws HibernateException {

        MROldNumerazione numerazione = null;
        numerazione = checkPreferencesAffiliato(sx, documento, user);
        if (numerazione == null) {
            if (user != null) {
            User user1 = (User) sx.get(UserImpl.class, user.getId());
                if (user1.getSedeOperativa() != null) {
                    numerazione = (MROldNumerazione) user1.getSedeOperativa().getNumerazioni().get(documento);
                }
                if (numerazione == null) {
                    numerazione = (MROldNumerazione) user1.getAffiliato().getNumerazioni().get(documento);
                }
            }
            if (numerazione == null) {
                numerazione = (MROldNumerazione) sx.createQuery(
                        "select n from MROldNumerazioneGenerica n where n.documento = :documento").
                        setParameter("documento", documento).setCacheable(true).
                        uniqueResult();
            }
        }
        return numerazione;
    }

    /**
     * Carica la numerazione dell'affiliato per il documento specificato.
     * @param sx
     * @param aAffiliato
     * @param documento
     * @return
     * @throws HibernateException
     */
    public static MROldNumerazione getNumerazione(Session sx, MROldAffiliato aAffiliato, String documento) throws HibernateException {
        aAffiliato = (MROldAffiliato) sx.get(MROldAffiliato.class, aAffiliato.getId());
        MROldNumerazione numerazione = (MROldNumerazione) aAffiliato.getNumerazioni().get(documento);
        return numerazione;
    }

    /**
     * Cerca una numerazione per il documento specificato, accessibile alla sede specificata.
     * Vengono scelte, in ordine le numerazioni della sede, affiliato e generiche.
     * @param sx
     * @param aSede
     * @param documento
     * @return
     * @throws HibernateException
     */
    public static MROldNumerazione getNumerazione(Session sx, MROldSede aSede, String documento) throws HibernateException {
        if (aSede == null) {
            return getNumerazione(sx, documento);
        }
        aSede = (MROldSede) sx.get(MROldSede.class, aSede.getId());
        MROldNumerazione numerazione = (MROldNumerazione) aSede.getNumerazioni().get(documento);
        if (numerazione == null) {
            numerazione = (MROldNumerazione) aSede.getAffiliato().getNumerazioni().get(documento);
        }
        if (numerazione == null) {
            numerazione = (MROldNumerazione) sx.createQuery(
                    "select n from MROldNumerazioneGenerica n where n.documento = :documento").
                    setParameter("documento", documento).setCacheable(true).
                    uniqueResult();
        }
        return numerazione;
    }




    public static MROldNumerazione getNumerazione(Session sx, MROldSede aSede, String documento, User user) throws HibernateException {
        MROldNumerazione numerazione = checkPreferencesAffiliato(sx, documento, user);
        if(numerazione==null){
            if (aSede == null) {
                return getNumerazione(sx, documento, user);
            }
            aSede = (MROldSede) sx.get(MROldSede.class, aSede.getId());
            numerazione = (MROldNumerazione) aSede.getNumerazioni().get(documento);
            if (numerazione == null) {
                numerazione = (MROldNumerazione) aSede.getAffiliato().getNumerazioni().get(documento);
            }
            if (numerazione == null) {
                numerazione = (MROldNumerazione) sx.createQuery(
                        "select n from MROldNumerazioneGenerica n where n.documento = :documento").
                        setParameter("documento", documento).setCacheable(true).
                        uniqueResult();
            }
        }

        return numerazione;
    }

    public static MROldNumerazione getNumerazione(Session sx, MROldAffiliato aAffiliato, String documento,User user) throws HibernateException {
        //sx = HibernateBridge.refreshSessionSX(sx);
        aAffiliato=user.getAffiliato();
        aAffiliato = (MROldAffiliato) sx.get(MROldAffiliato.class, aAffiliato.getId(), LockMode.READ);
        MROldNumerazione numerazione = (MROldNumerazione) aAffiliato.getNumerazioni().get(documento);

        return numerazione;
    }

    public static Integer nuovoNumero(Session sx, String documento, Integer anno) throws HibernateException {
        MROldNumerazione numerazione = getNumerazione(sx, documento);
        if (numerazione != null) {
            return nuovoNumero(sx, numerazione, anno);
        }
        return null;
    }

    public static Integer nuovoNumero(Session sx, String documento, Integer anno, User user) throws HibernateException {
        MROldNumerazione numerazione = getNumerazione(sx, documento, user);
        if (numerazione != null) {
            return nuovoNumero(sx, numerazione, anno);
        }
        return null;
    }

    public static Integer nuovoNumero(Session sx, MROldNumerazione numerazione, Integer anno) throws HibernateException {
        numerazione = (MROldNumerazione) sx.get(numerazione.getClass(), numerazione.getId());
        MROldProgressivo progressivo = (MROldProgressivo) sx.get(MROldProgressivo.class, new MROldProgressivo(numerazione, anno));

        if (progressivo == null || progressivo.getProgressivo() == null) {
        /* caso generico ritorno 1 */
            Integer nuovoNumero = new Integer(1);
            nuovoNumero = setNuovoNumeroPerVendite(nuovoNumero, numerazione, anno, Preferenze.getNumerazioneFatturePrefissoAnno(sx));
            sx.saveOrUpdate(new MROldProgressivo(numerazione, anno));
            return nuovoNumero;
        } else {
            return progressivo.getProgressivo() + 1;
        }

    }

    public static Integer setNuovoNumeroPerVendite(Integer numero, MROldNumerazione numerazione, Integer anno, boolean numerazioneFatturePrefissoAnno) {
        Integer nuovoNumero = numero;

        /* nel caso della numerazione delle fatture di Vendita o Note di Credito  */
        if (numerazione != null && numerazione.getDocumento() != null &&
                (
                        numerazione.getDocumento().equals(MROldNumerazione.VENDITE) ||
                                numerazione.getDocumento().equals(MROldNumerazione.NOTE_CREDITO_VENDITA) ||
                                numerazione.getDocumento().equals(MROldNumerazione.VENDITE_INTRASTAT) ||
                                numerazione.getDocumento().equals(MROldNumerazione.VENDITE_MARGINE)
                )

                ) {

            if (numerazioneFatturePrefissoAnno) {
                /* se la preferenza e' abilitata => la numerazione segue la seguente regola */
                String prefissoAnno = "";
                if (anno != null) {
                    prefissoAnno = anno.toString().substring(2);
                }

                String newNumero = prefissoAnno + StringUtils.leftPad(nuovoNumero.toString(), 6, "0");
                nuovoNumero = new Integer(newNumero);
            }
        }

        return nuovoNumero;
    }


    /**
     * Incrementa il progressivo annuale di questa numerazione di uno. Lo
     * crea se non e' stato impostato ancora. Ritorna il nuovo numero progressivo.
     * @param sx La session al db.
     * @param numerazione La numerazione il cui progressivo incrementare
     * @param anno L'anno del progressivo
     * @throws org.hibernate.HibernateException Se qualcosa va storto...
     */
    public static Integer aggiornaProgressivo(Session sx, MROldNumerazione numerazione, Integer anno) throws HibernateException {
        return aggiornaProgressivo(sx, numerazione, anno, 1);
    }

    /**
     * Incrementa il progressivo annuale di questa numerazione di <code>count</code>. Lo
     * crea se non e' stato impostato ancora. Ritorna il nuovo numero progressivo.
     * @param sx La session al db.
     * @param numerazione La numerazione il cui progressivo incrementare
     * @param anno L'anno del progressivo
     * @param count Di quanto bisogna incrementare il progressivo.
     * @throws org.hibernate.HibernateException Se qualcosa va storto...
     */
    public static Integer aggiornaProgressivo(Session sx, MROldNumerazione numerazione, Integer anno, Integer count) throws HibernateException {
        if (numerazione != null) {
            numerazione = (MROldNumerazione) sx.get(MROldNumerazione.class, numerazione.getId());

            MROldProgressivo progressivo = (MROldProgressivo) sx.get(MROldProgressivo.class, new MROldProgressivo(numerazione, anno));
            if (progressivo == null) {
                progressivo = new MROldProgressivo(numerazione, anno);

                if (progressivo.getProgressivo().equals(new Integer(0))) {
            /* genero il nuovo numero per la nuova normativa, per il 2013 dovrebbe essere 13000000 e primo documento 13000001 */
                    progressivo.setProgressivo(setNuovoNumeroPerVendite(progressivo.getProgressivo(), numerazione, anno, Preferenze.getNumerazioneFatturePrefissoAnno(sx)));
                }
            }
            Integer succNumb = progressivo.getProgressivo() + count;
            if (numerazione.equals(NumerazioniUtils.getNumerazione(sx,
                    MROldNumerazione.RENTTORENT))) {
                progressivo.setProgressivo(checkNumbers(sx, succNumb));
                sx.saveOrUpdate(progressivo);
            } else {
                progressivo.setProgressivo(succNumb);
                sx.saveOrUpdate(progressivo);
            }
            //sx.refresh(progressivo);
            return progressivo.getProgressivo();
        }
        return null;
    }

    public static String format(String prefisso, Integer numero) {
        if (numero != null) {
            if (prefisso != null) {
                return prefisso + " " + new DecimalFormat("#0.#").format(numero); //NOI18N
            } else {
                return new DecimalFormat("#0.#").format(numero); //NOI18N
            }
        }
        return bundle.getString("NumerazioniUtils.msgSN");
    }

    //for particular case of reservation source franchisee
    public static MROldNumerazione getNumerazione(Session sx, MROldSede aSede, String documento, MROldFonteCommissione fonteCommissione, MROldAffiliato affiliato) throws HibernateException {
        MROldNumerazione numerazione = null;
        //Preference for the unique enumeration
        MROldSetting settingUniqueEnumeration = (MROldSetting) sx.get(MROldSetting.class, "same.enum.res.ra");
        if (settingUniqueEnumeration != null && settingUniqueEnumeration.getValue() != null && settingUniqueEnumeration.getValue().equals("true")) {
            if (documento.equals("Prenotazioni") || documento.equals("Contratti")) {
                MROldAffiliato affiliatoMain = (MROldAffiliato) sx.get(MROldAffiliato.class, new Integer(1));
                Map map = affiliatoMain.getNumerazioni();
                if (map.containsKey(documento)) {
                    numerazione = (MROldNumerazione) map.get("Prenotazioni");
                }
            } else {
                MROldSetting setting = (MROldSetting) sx.get(MROldSetting.class, "frenchiseeNumerationPreferences");
                if (setting.getValue().equals("true")) {
                    if (fonteCommissione != null && fonteCommissione.getRentalType() != null) {
                        if (fonteCommissione.getRentalType().getIsFranchiseRental() && affiliato != null) {
                            numerazione = (MROldNumerazione) affiliato.getNumerazioni().get(documento);
                        } else {
                            MROldAffiliato affiliatoMain = (MROldAffiliato) sx.get(MROldAffiliato.class, new Integer(1));
                            numerazione = (MROldNumerazione) affiliatoMain.getNumerazioni().get(documento);
                        }
                    } else {
                        MROldAffiliato affiliatoMain = (MROldAffiliato) sx.get(MROldAffiliato.class, new Integer(1));
                        numerazione = (MROldNumerazione) affiliatoMain.getNumerazioni().get(documento);
                    }
                } else {
                    if (aSede == null) {
                        return getNumerazione(sx, documento);
                    }
                    aSede = (MROldSede) sx.get(MROldSede.class, aSede.getId());
                    numerazione = (MROldNumerazione) aSede.getNumerazioni().get(documento);
                    if (numerazione == null) {
                        numerazione = (MROldNumerazione) aSede.getAffiliato().getNumerazioni().get(documento);
                    }
                    if (numerazione == null) {
                        numerazione = (MROldNumerazione) sx.createQuery(
                                "select n from NumerazioneGenerica n where n.documento = :documento").
                                setParameter("documento", documento).setCacheable(true).
                                uniqueResult();
                    }
                }
            }
        } else {
            MROldSetting setting = (MROldSetting) sx.get(MROldSetting.class, "frenchiseeNumerationPreferences");
            if (setting != null && setting.getValue().equals("true")) {
                if (fonteCommissione != null && fonteCommissione.getRentalType() != null) {
                    if (fonteCommissione.getRentalType().getIsFranchiseRental() && affiliato != null) {
                        numerazione = (MROldNumerazione) affiliato.getNumerazioni().get(documento);
                    } else {
                        MROldAffiliato affiliatoMain = (MROldAffiliato) sx.get(MROldAffiliato.class, new Integer(1));
                        numerazione = (MROldNumerazione) affiliatoMain.getNumerazioni().get(documento);
                    }
                } else {
                    MROldAffiliato affiliatoMain = (MROldAffiliato) sx.get(MROldAffiliato.class, new Integer(1));
                    numerazione = (MROldNumerazione) affiliatoMain.getNumerazioni().get(documento);
                }
            } else {
                if (aSede == null) {
                    return getNumerazione(sx, documento);
                }
                aSede = (MROldSede) sx.get(MROldSede.class, aSede.getId());
                numerazione = (MROldNumerazione) aSede.getNumerazioni().get(documento);
                if (numerazione == null) {
                    numerazione = (MROldNumerazione) aSede.getAffiliato().getNumerazioni().get(documento);
                }
                if (numerazione == null) {
                    numerazione = (MROldNumerazione) sx.createQuery(
                            "select n from NumerazioneGenerica n where n.documento = :documento").
                            setParameter("documento", documento).setCacheable(true).
                            uniqueResult();
                }
            }
        }

        return numerazione;
    }

    /**
     * It opens a new Session and save immediately in the DB
     * @param sede
     * @param documento
     * @return
     */
    public static MROldNumerazione findOrCreateNumerazioneSede(Session sx, MROldSede sede, String documento){

        sede = (MROldSede) sx.get(MROldSede.class, sede.getId());
        MROldNumerazione numerazione = (MROldNumerazione) sede.getNumerazioni().get(documento);
        if (numerazione == null) {
            try{
                numerazione = new MROldNumerazioneSede(documento);
                numerazione.setPrefisso(sede.getCodice());
                numerazione.setDescrizione(sede.getCodice());
                sede.getNumerazioni().put(documento, numerazione);
                sx.saveOrUpdate(numerazione);
                sx.saveOrUpdate(sede);
            }
            catch(HibernateException e){

            }
            finally{
//                if(sx != null && sx.isOpen()){
//                    sx.close();
//                }
            }

        }
        return numerazione;
    }
    private static MROldNumerazione checkPreferencesAffiliato(Session sx, String documento, User user) {
        MROldNumerazione numerazione = null;
        //User user = (User) Parameters.getUser();
        //sx = HibernateBridge.refreshSessionSX(sx);

        if(Preferenze.getNumerazioneUnicaFattureAffiliato(sx)) {
            if (documento.equals(MROldNumerazione.VENDITE) || documento.equals(MROldNumerazione.VENDITE_MARGINE) || documento.equals(MROldNumerazione.VENDITE_INTRASTAT)
                    || documento.equals(MROldNumerazione.NOTE_CREDITO_VENDITA) /*|| documento.equals(MROldNumerazione.CORRISPETTIVI)*/
                    || documento.equals(MROldNumerazione.ACQUISTI) || documento.equals(MROldNumerazione.ACQUISTI_MARGINE)) {
                numerazione = getNumerazione(sx, user.getAffiliato(), MROldNumerazione.VENDITE);
            }
        }

        return numerazione;
    }

    /**
     * Carica una numerazione per il documento specificato, accessibile all'utente
     * collegato. Cerca in ordine tra le numerazioni della sede, affiliato e generiche.
     * Ignora la preferenza della numerazione unica per affiliato
     * @param sx
     * @param documento
     * @return
     * @throws HibernateException
     */
    public static MROldNumerazione getNumerazioneWithoutAffiliato(Session sx, String documento, MROldSede sede) throws HibernateException {
        MROldNumerazione numerazione = null;

        if(sede != null && sede.getId() != null) {
            sede = (MROldSede) sx.get(MROldSede.class, sede.getId());
            numerazione = (MROldNumerazione) sede.getNumerazioni().get(documento);
        }

        if (numerazione == null) {
            numerazione = (MROldNumerazione) sx.createQuery(
                    "select n from MROldNumerazioneGenerica n where n.documento = :documento and n.sede.id = :idSede").
                    setParameter("documento", documento).
                    setParameter("idSede", sede.getId()).
                    setCacheable(true).
                    uniqueResult();
        }

        return numerazione;
    }

    /**
     *
     * @param sx
     * @param documento
     * @return
     */
    public static MROldNumerazione getNumerazioneAffiliato(Session sx, String documento, User user) {
        MROldNumerazione numerazione = null;
        //sx = HibernateBridge.refreshSessionSX(sx);

        if (documento.equals(MROldNumerazione.VENDITE) || documento.equals(MROldNumerazione.VENDITE_MARGINE) || documento.equals(MROldNumerazione.VENDITE_INTRASTAT)
                || documento.equals(MROldNumerazione.NOTE_CREDITO_VENDITA) /*|| documento.equals(Numerazione.CORRISPETTIVI)*/
                || documento.equals(MROldNumerazione.ACQUISTI) || documento.equals(MROldNumerazione.ACQUISTI_MARGINE)) {
            numerazione = getNumerazione(sx, user.getAffiliato(), MROldNumerazione.VENDITE);
        } else if (documento.equals(MROldNumerazione.PROFORMA)) {
            numerazione = getNumerazione(sx, user.getAffiliato(), MROldNumerazione.PROFORMA);
        } else if (documento.equals(MROldNumerazione.CORRISPETTIVI)) {
            numerazione = getNumerazione(sx, user.getAffiliato(), MROldNumerazione.CORRISPETTIVI);
        }

        return numerazione;
    }

    /**
     *
     */
    public static void aggiornaNumerazioneClienti(Session sx) {
        try {
            MROldNumerazione numerazione = (MROldNumerazione) sx.createQuery("select n from MROldNumerazione n where n.documento = :documento").
                    setParameter("documento", MROldNumerazione.CLIENTI).
                    setMaxResults(1).
                    setCacheable(true).
                    uniqueResult();
            if (numerazione == null) {
                numerazione = new MROldNumerazione();
                numerazione.setDescrizione("Codice cliente");
                numerazione.setDocumento(MROldNumerazione.CLIENTI);
                sx.saveOrUpdate(numerazione);
            }
            Number max = (Number) sx.createQuery("select max(codice) from MROldBusinessPartner").setCacheable(true).uniqueResult();
            Integer progressivo = NumerazioniUtils.nuovoNumero(sx, numerazione, 0);
            if (max != null && max.intValue() != progressivo) {
                NumerazioniUtils.aggiornaProgressivo(sx, numerazione, 0, max.intValue() - progressivo + 1);
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
//            if (tx != null) {
//                try {
//                    tx.rollback();
//                } catch (Exception transExc) {
//                }
//            }
        } finally {
//            if (sx != null) {
//                try {
//                    sx.close();
//                } catch (Exception sxx) {
//                }
//            }
        }
    }

    public static Integer checkAndSetDuplicateNumero(Session sx, String className, String prefisso, Integer id, Integer numero, Integer anno, MROldNumerazione numerazione){
        List lista = new ArrayList();
        Integer newNumero = numero;
        boolean isDuplicate = true;
        while(isDuplicate){
            String sql = "FROM "+className+" cN  WHERE cN.numero = :numero  AND cN.prefisso = :prefisso  AND cN.anno = :anno";
            lista = sx.createQuery(sql)
                    .setParameter("numero", newNumero)
                    .setParameter("prefisso", prefisso)
                    .setParameter("anno", anno)
                    .setCacheable(true)
                    .list();
            if (lista.size() > 1) {
                System.out.println("**** Duplicated "+className);

                //oldRA.setNumero(NumerazioniUtils.aggiornaProgressivo(sx, oldRA.getNumerazione(), oldRA.getAnno()));
                newNumero = NumerazioniUtils.aggiornaProgressivo(sx, numerazione, anno);
                Query queryUpdateProgressivo = sx.createQuery("UPDATE "+className+" c SET c.numero=:numero WHERE c.prefisso=:prefisso AND c.anno=:anno AND c.id=:id"); //NOI18N
                queryUpdateProgressivo.setParameter("numero", newNumero);//throws GenericJDBCException
                queryUpdateProgressivo.setParameter("prefisso", prefisso);
                queryUpdateProgressivo.setParameter("anno", anno);//throws GenericJDBCException
                queryUpdateProgressivo.setParameter("id", id);
                queryUpdateProgressivo.setCacheable(true);
                queryUpdateProgressivo.executeUpdate();
                sx.flush();
            } else{
                isDuplicate = false;
                //newNumero = numero;
            }
        }
        //String sql = "FROM MROldContrattoNoleggio c  WHERE c.numero = :numero  AND c.prefisso = :prefisso  AND c.anno = :anno";
        return newNumero;
    }

    private static Integer checkNumbers(Session sx, Integer numeroSuccessivo) {
        //If exists a r2r contract with number, check if the same number exists. else step 1 more.
        Query qry = sx.createQuery("select number from Rent2Rent where number =:numero");
        qry.setParameter("numero", numeroSuccessivo);
        if (!qry.list().isEmpty()) {
            return checkNumbers(sx, numeroSuccessivo+1);
        } else {
            return numeroSuccessivo;
        }
    }
}
