/*
 * UserUtils.java
 *
 * Created on 14 noiembrie 2007, 11:33
 *
 */
package it.myrent.ee.api.utils;


import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.Parameters;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.db.MROldClienti;
import it.myrent.ee.db.MROldFonteCommissione;
import it.myrent.ee.db.User;
import it.myrent.ee.db.UserImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.ResourceBundle;

/**
 *
 * @author jamess
 */
public class UserUtils {

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");
    private static final Log log = LogFactory.getLog(UserUtils.class);

    /** Creates a new instance of UserUtils */
    private UserUtils() {
    }

//    public static MROldSede sedeOperativa() throws HibernateException {
//        def sx = sessionFactory.getCurrentSession()
//        List sedi = new ArrayList();
//        List sediSelezionate = new ArrayList();
//        User aUser = (User) sx.get(UserImpl.class, Parameters.getUser().getId());
//        if (aUser.isOperatore() || aUser.isCapoStazione()) {
//            sedi.add(aUser.getSedeOperativa());
//        } else if (aUser.isCapoArea()) {
//            sedi.addAll(aUser.getSediGestite());
//        } else {
//            sedi.addAll(SedeController.sediAffiliatoAttive(sx, Affiliato.getNoleggiatore(), true));
//        }
//        if (sedi.size() > 0 || sedi.isEmpty()) {
//            int returnStatus = JDialogSelezione.RET_CANCEL;
//            //while (returnStatus != JDialogSelezione.RET_OK) {
//                JDialogSelezione.seleziona(parent, true, title, sedi, JDialogSelezione.SINGLE_OBJECT_SELECTION);
//                returnStatus = JDialogSelezione.getReturnStatus();
//            //}
//            if (returnStatus == JDialogSelezione.RET_OK) {
//                sediSelezionate = JDialogSelezione.getSelectedElements();
//            }
//
//        }
//        if (!sediSelezionate.isEmpty()) {
//            return (Sede) sediSelezionate.get(0);
//        }
//
//        return null;
//    }

    public static Double scontoMassimoApplicabile(Session sx, User user, Date dataPrenotazione, Date dataRitiro, Integer durata) throws HibernateException {
        Double scontoMassimo = null;
        if(user != null){
            scontoMassimo = user.getScontoMassimo();
        }

        if (scontoMassimo == null) {
            scontoMassimo = 0.0;
        }
        Date aDataPrenotazione = FormattedDate.extractDate(dataPrenotazione);
        Date aDataRitiro = FormattedDate.extractDate(dataRitiro);
        Double scontoStraordinario = 0.0;
        if(user != null){
            scontoStraordinario = (Double) sx.createQuery(
                    "select max(s.percentuale) from MROldSconto s, UserImpl u "
                            + "where u.id = :id "
                            + "and s in elements(u.scontiStraordinari) "
                            + "and :dataPrenotazione between s.inizioSconto and s.fineSconto "
                            + "and :dataRiferimento between s.inizioRiferimento and s.fineRiferimento "
                            + "and s.durataMinima <=  :durata").
                    setParameter("id", user.getId()).
                    setParameter("dataPrenotazione", aDataPrenotazione).
                    setParameter("dataRiferimento", aDataRitiro).
                    setParameter("durata", durata).
                    uniqueResult();
        }

        if (scontoStraordinario != null) {
            scontoMassimo = Math.max(scontoMassimo, scontoStraordinario);
        }
        return scontoMassimo;
    }

    public static Double scontoMassimoApplicabile(Session sx, User user, Date dataRitiro, Integer durata) throws HibernateException {
        return scontoMassimoApplicabile(sx, user, FormattedDate.formattedDate(), dataRitiro, durata);
    }

//    public static Double scontoMassimoApplicabile(Session sx, User aUser, Date dataRitiro, Integer durata) {
//        Double scontoMassimo = null;
//        //Session sx = null;
//        try {
//            //sx = HibernateBridge.startNewSession();
//            scontoMassimo = scontoMassimoApplicabile(sx, aUser, dataRitiro, durata);
//        } catch (Exception ex) {
//            scontoMassimo = new Double(0);
//        } finally {
//            if (sx != null) {
//                try {
//                    sx.close();
//                } catch (Exception sxx) {
//                }
//            }
//        }
//        return scontoMassimo;
//    }

    public static boolean isScontoValido(Double vecchioSconto, Double nuovoSconto, Date dataRitiro, Integer durata, User aUser, Session sx) {
        //User aUser = (User) Parameters.getUser();
        boolean scontoAbilitato = false ;
        if(aUser != null){
            scontoAbilitato = aUser.isOperatore() || aUser.isCapoStazione() || aUser.isResponsabileNazionale();

        }
        boolean scontoValido = true;


        // Lo sconto impostato e' maggiore del vecchio. Verifichiamo se e' valido.
        Double scontoMassimo = null;
                if(aUser != null){
                    scontoMassimo = UserUtils.scontoMassimoApplicabile(
                            sx,
                            aUser,
                            dataRitiro,
                            durata);
                }

        if (scontoAbilitato && nuovoSconto != null && (vecchioSconto == null || nuovoSconto.compareTo(vecchioSconto) > 0)) {
            if (nuovoSconto.compareTo(scontoMassimo) > 0) {
                // Lo sconto applicato supera il massimo applicabile
                if (vecchioSconto != null && vecchioSconto.doubleValue() > scontoMassimo.doubleValue()) {
//                    JOptionPane.showMessageDialog(parent,
//                            MessageFormat.format(bundle.getString("UserUtils.msgScontoMassimoOppureIniziale"), dataRitiro, scontoMassimo, vecchioSconto),
//                            bundle.getString("UserUtils.msgScontoNonApplicabile"),
//                            JOptionPane.WARNING_MESSAGE);
                } else {
//                    JOptionPane.showMessageDialog(parent,
//                            MessageFormat.format(bundle.getString("UserUtils.msgScontoMassimo"), dataRitiro, scontoMassimo),
//                            bundle.getString("UserUtils.msgScontoNonApplicabile"),
//                            JOptionPane.WARNING_MESSAGE);
                }
                scontoValido = false;
            }
        } else {
            if (nuovoSconto != null) {
//                if (nuovoSconto.compareTo(scontoMassimo) > 0) {
//                    JOptionPane.showMessageDialog(parent,
//                            MessageFormat.format(bundle.getString("UserUtils.msgScontoMassimo"), dataRitiro, scontoMassimo),
//                            bundle.getString("UserUtils.msgScontoNonApplicabile"),
//                            JOptionPane.WARNING_MESSAGE);
//                }
            }
        }
        return scontoValido;
    }

    /**
     * Verifica se la fonte impostata e' tra le fonti dell'utente oppure
     * quella del cliente.
     * @param parent il dialogo da usare per il messaggio.
     * @param aFonte
     * @param aCliente
     * @return
     */
    public static boolean isFonteValida(Session sx,Component parent, MROldFonteCommissione aFonte, MROldClienti aCliente) {
        User aUser = (User) Parameters.getUser();
        if (!aUser.isOperatore() && !aUser.isCapoStazione() && !aUser.isResponsabileNazionale()) {
            return true;
        }
        boolean fonteValida = false;
        boolean errore = false;
        //Session sx = null;
        Transaction tx = null;
        try {
            //sx = HibernateBridge.startNewSession();
            tx = sx.beginTransaction();
            aFonte = (MROldFonteCommissione) sx.get(MROldFonteCommissione.class, aFonte.getId());
            if (aCliente != null && aCliente.getId() != null) {
                aCliente = (MROldClienti) sx.get(MROldClienti.class, aCliente.getId());
                if (aCliente != null) {
                    fonteValida =
                            aCliente.getFonteCommissione() != null
                            && aCliente.getFonteCommissione().equals(aFonte);
                }
            }
            if (!fonteValida) {
                aUser = (User) sx.get(UserImpl.class, aUser.getId());
                fonteValida = aUser.getFonti().contains(aFonte);
            }
            tx.commit();
            if (!fonteValida) {
                JOptionPane.showMessageDialog(
                        parent,
                        bundle.getString("UserUtils.msgFonteNonValidaMessage"),
                        bundle.getString("UserUtils.msgFonteNonValidaTitle"),
                        JOptionPane.ERROR_MESSAGE,
                        new ImageIcon(UserUtils.class.getResource("/it/aessepi/utils/images/access_denied_48px.png")));
            }
        } catch (Exception ex) {
            errore = true;
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception txx) {
                }
            }
            log.error("UserUtils.isFonteValida: " + ex);
            log.debug("UserUtils.isFonteValida: " + ex, ex);
            //Messaggio.erroreDatabaseLetturaDati(parent, ex, 6, false);
        } finally {
            if (sx != null) {
                try {
                    sx.close();
                } catch (Exception sxx) {
                }
            }
        }
        return fonteValida;
    }

     /**
     * Verifica se la fonte impostata e' tra le fonti dell'utente oppure
     * quella del cliente.
     * @param parent il dialogo da usare per il messaggio.
     * @param aFonte
     * @param aCliente
     * @return
     */
    public static boolean isFonteValida(Session sx, MROldFonteCommissione aFonte, MROldClienti aCliente, User aUser) {
        //User aUser = (User) Parameters.getUser();
        if (aUser!=null) {
//            if (!Preferenze.getEnableReservationSourcesForAreaManager(sx)) {
//                return true;
//            }
            return true;
        }
        if (!aUser.isOperatore() && !aUser.isCapoStazione() && !aUser.isResponsabileNazionale()) {
//            if (!Preferenze.getEnableReservationSourcesForAreaManager(sx)) {
//                return true;
//            }
            return true;
        }
        boolean fonteValida = false;
        boolean errore = false;

        Transaction tx = null;
        try {
            //tx = sx.beginTransaction();
            aFonte = (MROldFonteCommissione) sx.get(MROldFonteCommissione.class, aFonte.getId());
            if (aCliente != null && aCliente.getId() != null) {
                aCliente = (MROldClienti) sx.get(MROldClienti.class, aCliente.getId());
                if (aCliente != null) {
                    fonteValida =
                            aCliente.getFonteCommissione() != null
                            && aCliente.getFonteCommissione().equals(aFonte);
                }
            }
            if (!fonteValida) {
                aUser = (User) sx.get(UserImpl.class, aUser.getId());
                fonteValida = aUser.getFonti().contains(aFonte);
            }
            //tx.commit();
            if (!fonteValida) {
//                JOptionPane.showMessageDialog(
//                        parent,
//                        bundle.getString("UserUtils.msgFonteNonValidaMessage"),
//                        bundle.getString("UserUtils.msgFonteNonValidaTitle"),
//                        JOptionPane.ERROR_MESSAGE,
//                        new ImageIcon(UserUtils.class.getResource("/it/aessepi/utils/images/access_denied_48px.png")));
            }
        } catch (Exception ex) {
            errore = true;
            if (tx != null) {
                try {
                    //tx.rollback();
                } catch (Exception txx) {
                }
            }
            log.error("UserUtils.isFonteValida: " + ex);
            log.debug("UserUtils.isFonteValida: " + ex, ex);
            //Messaggio.erroreDatabaseLetturaDati(parent, ex, 6, false);
        }
        
        return fonteValida;
    }
    /*
    private static List leggiFontiValide(User aUser,Session sx) {
        List fonti = new ArrayList();
         Session sx = null;
        Transaction tx = null;
        try {
            //sx = HibernateBridge.startNewSession();
            tx = sx.beginTransaction();
            fonti = sx.createQuery("select f from FonteCommissione f where :user in elements(f.users)").
                    setParameter("user", aUser).
                    list();
            fonti = sx.createQuery("select f from UserImpl u left join u.fonti where u = :user").
                    setParameter("user", aUser).
                    list();
            tx.commit();
        } catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception txx) {
                }
            }
        } finally {
            if (sx != null) {
                try {
                    sx.close();
                } catch (Exception sxx) {
                }
            }
        }
        return fonti;
    }

    public static List listaUtenti() {
        List lista = null;

        Session sx = null;
        try {
            sx = HibernateBridge.startNewSession();
            lista = sx.createQuery("SELECT u FROM UserImpl u where u.disattivo = :false ORDER BY u.nomeCognome")
                    .setParameter("false", Boolean.FALSE)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sx != null) {
                    sx.close();
                }
            } catch (Exception e) {

            }
        }

        return lista;
    }
    */
}
