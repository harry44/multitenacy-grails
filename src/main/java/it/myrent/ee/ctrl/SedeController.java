/**
 * Created by Madhvendra on 14/03/2017.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.ctrl;

import it.myrent.ee.db.MROldAffiliato;
import it.myrent.ee.db.MROldSede;
import it.myrent.ee.db.User;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jamess
 */
public class SedeController {

    private static final Log log = LogFactory.getLog(SedeController.class);

    public static List<MROldSede> sediAffiliatoNonInList(Session sx, MROldAffiliato affiliato, List<MROldSede> sedi) {
        return sx.getNamedQuery("it.myrent.ee.db.MROldSede.sediAffiliatoNotInList"). // NOI18N
                setParameterList("sedi", sedi). // NOI18N
                setParameter("affiliato", affiliato). // NOI18N
                list();
    }

    public static List<MROldSede> sediAffiliato(Session sx, MROldAffiliato affiliato) {
        return sx.getNamedQuery("it.myrent.ee.db.MROldSede.sediAffiliato"). // NOI18N
                setParameter("affiliato", affiliato). // NOI18N
                list();
    }

    public static List<MROldSede> sediAffiliatoAttive(Session sx, MROldAffiliato affiliato, Boolean attive) {
        return sx.getNamedQuery("it.myrent.ee.db.MROldSede.sediAffiliatoAttive"). //NOI18N
                setParameter("attiva", attive). // NOI18N
                setParameter("affiliato", affiliato). //NOI18N
                list();
    }

    public static List<MROldSede> sediAffiliatoAttive(MROldAffiliato affiliato, Boolean attive) {
        Session sx = null;
        List lista = null;
        try {
            lista = sx.getNamedQuery("it.myrent.ee.db.MROldSede.sediAffiliatoAttive"). //NOI18N
                    setParameter("attiva", attive). // NOI18N
                    setParameter("affiliato", affiliato). //NOI18N
                    list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static List<MROldSede> sediGestiteAttive(Session sx, User capoArea, Boolean attive) {
        return sx.getNamedQuery("it.myrent.ee.db.MROldSede.sediGestiteAttive"). // NOI18N
                setParameter("capoArea", capoArea).
                setParameter("attiva", attive). // NOI18N
                list();
    }

    public static List<MROldSede> sediAttiveFromList(Session sx, List sedi, Boolean attive) {
        return sx.getNamedQuery("it.myrent.ee.db.MROldSede.sediAttiveFromList"). // NOI18N
                setParameterList("sedi", sedi).
                setParameter("attiva", attive). // NOI18N
                list();
    }

    public static List<MROldSede> sediAttive(Session sx, User capoArea, Boolean attive) {
        return sx.getNamedQuery("it.myrent.ee.db.MROldSede.sediAttive"). // NOI18N
                setParameter("attiva", attive). // NOI18N
                list();
    }


    /**
     * Legge la sede operativa dell'utente operatore o capo stazione.
     * @param sx La sessione al database
     * @param user L'utente operatore o capo stazione
     * @param attiva Specificare se la stazione e' attiva o meno.
     * @return Una lista contenente la stazione (o niente, se la stazione non e' attiva).
     */
    public static List<MROldSede> sedeOperativaAttiva(Session sx, User user, Boolean attiva) {
        return sx.getNamedQuery("it.myrent.ee.db.MROldSede.sedeOperativaAttiva"). // NOI18N
                setParameter("sede", user.getSedeOperativa()). // NOI18N
                setParameter("attiva", Boolean.TRUE).list(); // NOI18N
    }

    public static List<MROldSede> sediAttive(Session sx, Boolean attive) {
        return sx.getNamedQuery("it.myrent.ee.db.MROldSede.sediAttive"). // NOI18N
                setParameter("attiva", Boolean.TRUE). // NOI18N
                list();
    }

    /**
     * La stazione operativa, le stazioni gestite o tutte le stazioni,
     * a seconda del tipo di utente.<p>
     *
     * 1. Operatore, CapoStazione: sedeOperativa<br>
     * 2. CapoArea: sediGestite<br>
     * 3. ResponsabileNazionale, Administrator: tutte le sedi.<br>
     *
     * @see SedeController#sedeOperativaAttiva
     * @see SedeController#sediGestiteAttive
     * @see SedeController#sediAttive
     * @param sx La sessione al database
     * @param user L'utente per il quale cercare le sedi.
     * @param attive Seleziona sedi attive o no.
     * @return Una lista di sedi (no proxy)
     */
    public static List<MROldSede> sediUser1(Session sx, User user, Boolean attive) {
        List<MROldSede> sedi = new ArrayList<MROldSede>();
        if (user.isOperatore() || user.isCapoStazione()) {
            return sedeOperativaAttiva(sx, user, attive);
        } else if (user.isCapoArea()) {
            return sediGestiteAttive(sx, user, attive);
        } else {
            return sediAttive(sx, attive);
        }
    }

    public static List<MROldSede> sediAffiliatoUser1(Session sx, User user, Boolean attive) {
        List<MROldSede> sedi = new ArrayList<MROldSede>();
        if (user.isOperatore() || user.isCapoStazione()) {
            return sedeOperativaAttiva(sx, user, attive);
        } else if (user.isCapoArea()) {
            return sediGestiteAttive(sx, user, attive);
        } else {
            return sediAffiliatoAttive(sx,user.getAffiliato() ,attive);
        }
    }

    public static List<MROldSede> sediAffiliatoUser(Session sx,User user, Boolean attive) {
        List stazioni = new ArrayList();
        Transaction tx = null;
        try {
            stazioni = sediAffiliatoUser1(sx, user, attive);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("SedeController.sediUser(User, Boolean): " + ex, ex); //NOI18N
        }
        return stazioni;
    }

    /**
     * La stazione operativa, le stazioni gestite o tutte le stazioni,
     * a seconda del tipo di utente.<p>
     * Apre una sessione al database
     * @param  La sessione al database
     * @param user L'utente per il quale cercare le sedi.
     * @return Una lista di sedi (no proxy)
     * @see SedeController#sediUser(Session, User, Boolean)
     */
    public static List<MROldSede> sediUser(Session sx,User user, Boolean attive) {
        List stazioni = new ArrayList();
        try {
            stazioni = sediUser1(sx, user, attive);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("SedeController.sediUser(User, Boolean): " + ex, ex); //NOI18N
        }
        return stazioni;
    }

    /**
     * La stazione operativa, le stazioni gestite, le stazioni dell'affiliato o tutte le stazioni,
     * a seconda del tipo di utente.<p>
     *
     * 1. Operatore, CapoStazione: sedeOperativa<br>
     * 2. CapoArea: sediGestite<br>
     * 3. ResponsabileNazionale: sedi dell'affiliato<br/>
     * 4. Administrator: tutte le sedi.<br>
     *
     * @see SedeController#sedeOperativaAttiva
     * @see SedeController#sediGestiteAttive
     * @see SedeController#sediAttive
     * @see SedeController#sediAffiliatoAttive
     * @param sx La sessione al database
     * @param user L'utente per il quale cercare le sedi.
     * @param attive Seleziona sedi attive o no.
     * @return Una lista di sedi (no proxy)
     */
    public static List<MROldSede> sediUserNewVersion1(Session sx, User user, Boolean attive) {
        List<MROldSede> sedi = new ArrayList<MROldSede>();
        if (user.isOperatore() || user.isCapoStazione()) {
            return sedeOperativaAttiva(sx, user, attive);
        } else if (user.isCapoArea()) {
            return sediGestiteAttive(sx, user, attive);
        } else if (user.isResponsabileNazionale()) {
            return sediAffiliatoAttive(sx, user.getAffiliato(), attive);
        } else {
            return sediAttive(sx, attive);
//            return sediAffiliatoAttive(sx, user.getAffiliato(), attive);
        }
    }

    /**
     * Come sopra ma apre il NewVersion
     *
     * @param  La sessione al database
     * @param user L'utente per il quale cercare le sedi.
     * @return Una lista di sedi (no proxy)
     * @see SedeController#sediUser(Session, User, Boolean)
     */
    public static List<MROldSede> sediUserNewVersion(Session sx,User user, Boolean attive) {
        List stazioni = new ArrayList();
        try {
            stazioni = sediUserNewVersion1(sx, user, attive);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("SedeController.sediUser(User, Boolean): " + ex, ex); //NOI18N
        }
        return stazioni;
    }
}
