package it.myrent.ee.api.utils;

import it.myrent.ee.db.MROldFonteCommissione;
import it.myrent.ee.db.MROldPrenotazione;
import org.hibernate.Session;

import java.io.Serializable;

/**
 * Created by shivangani on 30/08/2019.
 */
public class ReservationSearch {

    /**
     *
     * @param sx
     * @param fonteCommissione
     * @param voucherNumber
     * @return
     */
    public static MROldPrenotazione byVoucherNumber(Session sx, MROldFonteCommissione fonteCommissione, String voucherNumber) {
        return (MROldPrenotazione) sx.createQuery(
                "select p from MROldPrenotazione p left join p.commissione c "
                        + "where c.fonteCommissione = :fonteCommissione and lower(c.codiceVoucher) = :voucherNumber").
                setParameter("fonteCommissione", fonteCommissione).
                setParameter("voucherNumber", voucherNumber.toLowerCase()).
                setMaxResults(1).
                uniqueResult();
    }

    /**
     *
     * @param sx
     * @param fonteCommissione
     * @param voucherNumberRegExp
     * @return
     */
    public static MROldPrenotazione byRegExpVoucherNumber(Session sx, MROldFonteCommissione fonteCommissione, String voucherNumberRegExp) {
        Object idPrenotazione = sx.createSQLQuery(
                "select id_prenotazione from commissioni where "
                        + "id_fonte_commissione = :idFonteCommissione and "
                        + "codice_voucher ~* :voucherNumberRegExp").
                setParameter("idFonteCommissione", fonteCommissione.getId()).
                setParameter("voucherNumberRegExp", voucherNumberRegExp).
                setMaxResults(1).
                uniqueResult();
        if (idPrenotazione != null) {
            return (MROldPrenotazione) sx.get(MROldPrenotazione.class, (Serializable) idPrenotazione);
        }
        return null;

    }

    /**
     *
     * @param sx
     * @param fonteCommissione
     * @param refCode
     * @return
     */
    public static MROldPrenotazione byRefCode(Session sx, MROldFonteCommissione fonteCommissione, String refCode) {
        return (MROldPrenotazione) sx.createQuery(
                "select p from MROldPrenotazione p left join p.commissione c "
                        + "where c.fonteCommissione = :fonteCommissione and lower(p.codice) = :refCode").
                setParameter("fonteCommissione", fonteCommissione).
                setParameter("refCode", refCode.toLowerCase()).
                setMaxResults(1).
                uniqueResult();
    }

    /**
     *
     * @param sx
     * @param confirmationNumber
     * @return
     */
    public static MROldPrenotazione byConfirmationNumber(Session sx, String confirmationNumber) {
        return (MROldPrenotazione) sx.createQuery("select p from MROldPrenotazione p where lower(p.codice) = :codice").
                setParameter("codice", confirmationNumber.toLowerCase()).
                setMaxResults(1).
                uniqueResult();
    }

    /**
     *
     * @param sx
     * @param confirmationNumber
     * @param lastName
     * @return
     */
    public static MROldPrenotazione byConfirmationNumber(Session sx, String confirmationNumber, String lastName) {
        return (MROldPrenotazione) sx.createQuery("select p from MROldPrenotazione p where lower(p.codice) = :codice and lower(p.cliente.cognome) = :cognome").
                setParameter("codice", confirmationNumber.toLowerCase()).
                setParameter("cognome", lastName.toLowerCase()).
                setMaxResults(1).
                uniqueResult();
    }
}
