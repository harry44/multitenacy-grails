/*
 * VersionUtils.java
 *
 * Created il 07 marzo 2005, 16:27
 */
package it.aessepi.myrentcs.utils;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.Parameters;
import it.aessepi.utils.beans.FormattedDate;
import it.aessepi.utils.security.PermissionAction;
import it.myrent.ee.api.factory.PrimanotaFactory;
import it.myrent.ee.api.utils.DisponibilitaUtils;
import it.myrent.ee.api.utils.NumerazioniUtils;
import it.myrent.ee.db.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class VersionUtils {

    private static Log log = LogFactory.getLog(VersionUtils.class);
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");

    private VersionUtils() {
    }

    public static Versione queryVersione(String name, Statement st) throws SQLException {
        Versione aVersione = null;
        ResultSet rs = st.executeQuery("select v.major as major, v.minor as minor, v.patch as patch from versioni v where v.name = '" + name + "' order by v.major desc, v.minor desc, v.patch desc");
        if (rs.next()) {
            Integer major = rs.getInt("major");
            Integer minor = rs.getInt("minor");
            Integer patch = rs.getInt("patch");
            aVersione = new Versione(name, major, minor, patch);
        }
        return aVersione;
    }

    public static Versione queryVersione(Statement st) throws SQLException {
        Versione aVersione = null;
        ResultSet rs = st.executeQuery("select v.major as major, v.minor as minor, v.patch as patch from versioni v order by v.major desc, v.minor desc, v.patch desc"); //NOI18N
        if (rs.next()) {
            Integer major = rs.getInt("major");
            Integer minor = rs.getInt("minor");
            Integer patch = rs.getInt("patch");
            aVersione = new Versione(Parameters.getProgram(), major, minor, patch);
        }
        return aVersione;
    }

    public static boolean aggiornaDB(Statement stColonne, Session sx) {
        boolean errore = false;
        boolean aggiornatoPianoDeiConti = false;
        try {
            int[] updates;
            //aggiornaColonneNuove(stColonne);
           // updates = stColonne.executeBatch();
           // log.debug("Colonne nuove updates done: " + updates.length);
            aggiornaAnagraficheComplete(stColonne);
            updates = stColonne.executeBatch();
            log.debug("Anagrafiche updates done: " + updates.length); //NOI18N
            /* Statement stCreazioneTabelle = cx.createStatement();
            aggiornaCreazioneTabelle(stCreazioneTabelle, oldVersion);
            updates = stCreazioneTabelle.executeBatch();
            log.debug("Deleted table updates done: " + updates.length); //NOI18N
            stCreazioneTabelle.close();*/
            aggiornaTabelleEliminate(stColonne);
            updates = stColonne.executeBatch();
            log.debug("Deleted table updates done: " + updates.length); //NOI18N

//            Statement stPlugins = cx.createStatement();
//            Iterator<Plugin> plugins = JDialogStart.getPlugins().values().iterator();
//            while (plugins.hasNext()) {
//                Plugin plugin = plugins.next();
//                plugin.aggiornaDatabase(stPlugins, queryVersione(plugin.getPluginName(), stPlugins), new Versione(plugin.getPluginName(), plugin.getPluginVersion()));
//            }
            updates = stColonne.executeBatch();
            log.debug("Plugin updates done: " + updates.length); //NOI18N
            aggiornaIdentifierSequences(stColonne);

        } catch (java.sql.SQLException sqex) {
                sqex.printStackTrace();
            errore = true;
            log.error("Database update failed."); //NOI18N
            log.debug("SQLException encountered when executing update batch", sqex); //NOI18N
        } catch (Exception ex) {
            log.warn("Unknown exception while updating database."); //NOI18N
            log.debug("Unknown Exception encountered when executing update batch", ex); //NOI18N
        }

        if (!errore) {
            verificaRisorsePermessi(sx);

            // Aggiorniamo il piano dei conti
            verificaContoCarteDiCredito(sx);
            verificaContoStatoPatrimoniale(sx);
            verificaContoVoucher(sx);
            verificaContoAVista(sx);
            verificaContoChiusuraCasse(sx);
            verificaContoAnticipiClienti(sx);

            //Aggiorniamo le causali
            verificaCausaleAperturaStatoPatrimoniale(sx);
            verificaCausaleChiusuraProffittiEPerdite(sx);
            verificaCausaleChiusuraStatoPatrimoniale(sx);
            verificaCausaleRisultatoEsercizio(sx);
            verificaCausaleAperturaCassa(sx);
            verificaCausaleChiusuraCassa(sx);
            verificaCausaleFatturaDiAcconto(sx);
            verificaCausaleFatturaDiSaldo(sx);
                verificaTariffeCondivise(sx);
                aggiornaIsWarningBlackList(sx);


            //Aggiorniamo le numerazioni
            verificaNumerazioneClienti(sx);
            verificaNumerazioniPartite(sx);
            verificaProgressiviPartite(sx);
            aggiornaQuantitaFattureScontate(sx);
            aggiornaPrimenotePrenotazioniContratti(sx);
            aggiornaNumerazioneClienti(sx);
            aggiornaDataVenditaAndDisponibilitaVeicoliDisabilitati(sx);
                    aggiornaPrimenote(sx);
            verificaChiudiPartiteAperte(sx);
            verificaCreaPartiteAperte(sx);
        }

        if (!errore) {
//            Session sx = null;
//            Transaction tx = null;
//            try {
//                sx = HibernateBridge.startNewSession();
//                tx = sx.beginTransaction();
//                if (sx.get(Versione.class, newVersion) == null) {
//                    sx.save(newVersion);
//                }
//                Iterator<Plugin> plugins = JDialogStart.getPlugins().values().iterator();
//                while (plugins.hasNext()) {
//                    Plugin plugin = plugins.next();
//                    Versione versionePlugin = new Versione(plugin.getPluginName(), plugin.getPluginVersion());
//                    if (sx.get(Versione.class, versionePlugin) == null) {
//                        sx.save(versionePlugin);
//                    }
//                }
//                tx.commit();
//            } catch (Exception ex) {
//                if (tx != null) {
//                    try {
//                        tx.rollback();
//                    } catch (Exception txx) {
//                    }
//                }
//                log.error("Impossibile salvare la nuova versione: " + ex.getMessage());
//                log.debug("Impossibile salvare la nuova versione: ", ex);
//                errore = true;
//            } finally {
//                if (sx != null) {
//                    try {
//                        sx.close();
//                    } catch (Exception sxx) {
//                    }
//                }

        }

        return !errore;
    }

    public static void aggiornaPrimenotePrenotazioniContratti(Session sx) {
        try {
            List<MROldCommissione> listaFatture = sx.createQuery("SELECT c FROM Commissione c WHERE c.contratto is not null and c.prenotazione is not null and (c.prepagato is null or c.prepagato = :false)")
                    .setParameter("false", Boolean.FALSE).list();
            if (listaFatture != null && !listaFatture.isEmpty()) {
                Iterator<MROldCommissione> it = listaFatture.iterator();
                while (it.hasNext()) {
                    MROldCommissione commissione = it.next();
                    List<MROldDocumentoFiscale> fatture = sx.createQuery("SELECT d FROM DocumentoFiscale d WHERE d.prenotazione.id = :p and d.contratto is null")
                            .setParameter("p", commissione.getPrenotazione().getId()).list();

                    SQLQuery query = sx.createSQLQuery("UPDATE fattura_intestazione SET id_contratto_noleggio = " + commissione.getContratto().getId() + " WHERE id_contratto_noleggio is null and id_prenotazione = " + commissione.getPrenotazione().getId());
                    query.executeUpdate();

                    query = sx.createSQLQuery("UPDATE primenote SET id_contratto = " + commissione.getContratto().getId() + " WHERE id_contratto is null and id_prenotazione = " + commissione.getPrenotazione().getId());
                    query.executeUpdate();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void aggiornaIdentifierSequences(Statement st) throws SQLException { // Aggiornamento per tutte le versioni
        String[] tabelle = new String[]{
            "affiliati", //NOI18N
            "anagrafica_azienda", //NOI18N
            "assicurazioni", //NOI18N
            "autisti", //NOI18N
            "carburanti", //NOI18N
            "causali_movimento", //NOI18N
            "clienti", //NOI18N
            "codiciiva", //NOI18N
            "commissioni", //NOI18N
            "comuni", //NOI18N
            "contratto_noleggio", //NOI18N
            "coordinate_bancarie", //NOI18N
            "danni", //NOI18N
            "distinte", //NOI18N
            "durate", //NOI18N
            "fattura_intestazione", //NOI18N
            "fattura_riga", //NOI18N
            "fattura_imposta", //NOI18N
            "fonti_commissioni", //NOI18N
            "garanzie", //NOI18N
            "gruppi", //NOI18N
            "importi_tariffe", //NOI18N            
            "listini", //NOI18N
            "log_entries", //NOI18N
            "mezzi_pagamenti", //NOI18N
            "movimenti_auto", //NOI18N
            "multe", //NOI18N
            "multe_enti", //NOI18N
            "noleggiatori", //NOI18N
            "numerazioni", //NOI18N
            "optionals_listini", //NOI18N
            "optionals", //NOI18N
            "optionals_tariffe", //NOI18N
            "pagamenti", //NOI18N
            "parco_veicoli", //NOI18N
            "partite", //NOI18N
            "piano_dei_conti", //NOI18N
            "prenotazioni", //NOI18N
            "preventivi", //NOI18N
            "primenote", //NOI18N
            "rate_contratti", //NOI18N
            "regioni", //NOI18N
            "registri_iva", //NOI18N
            "righe_causale", //NOI18N
            "righe_imposta", //NOI18N
            "righe_primanota", //NOI18N
            "scadenze", //NOI18N
            "sconti", //NOI18N
            "sedi", //NOI18N
            "stagione_tariffa", //NOI18N
            "tariffe_listini", //NOI18N
            "tariffe", //NOI18N
            "users", //NOI18N
            "validita_listini_fonti",
            "sessioni" //NOI18N
        };
        for (int i = 0; i < tabelle.length; i++) {
            st.execute("SELECT setval('" + tabelle[i] + "_seq', max(id)) from " + tabelle[i]); //NOI18N
        }
    }

    public static void verificaRisorsePermessi(Session sx) {
        try {
            List<MROldResource> resourceList = sx.createQuery("select r from Resource r").list(); //NOI18N

            for (MROldResource resource : MROldResource.RESOURCE_LIST) {
                if (!resourceList.contains(resource)) {
                    sx.save(resource);
                    resourceList.add(resource);
                }
            }
            PermissionAction readOnlyPerms = PermissionAction.READ.or(PermissionAction.PRINT);
            PermissionAction allPerms = PermissionAction.ALL;
            List<User> users = sx.createQuery("select u from UserImpl u").list(); //NOI18N
            for (User user : users) {
                boolean modified = false;
                for (MROldResource resource : resourceList) {
                    if (user.getPermessi().get(resource) == null) {
                        if (user.isAdministrator() || user.isResponsabileNazionale()) {
                            user.getPermessi().put(resource, allPerms.getPermessi());
                        } else {
                            user.getPermessi().put(resource, readOnlyPerms.getPermessi());
                        }
                        modified = true;
                    }
                }
                if (modified) {
                    sx.update(user);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaCausaleChiusuraProffittiEPerdite( Session sx) {

        try {
            MROldCausalePrimanota causale = (MROldCausalePrimanota) sx.get(MROldCausalePrimanota.class, MROldCausalePrimanota.CHIUSURA_PROFFITTI_PERDITE);
            if (causale == null) {
                causale = new MROldCausalePrimanota();
                causale.setCodice(MROldCausalePrimanota.CHIUSURA_PROFFITTI_PERDITE);
                causale.setDataDocRequired(false);
                causale.setDescrizione(bundle.getString("VersionUtils.msgChiusuraProffittiPerdite"));
                causale.setGestioneIvaIndetraibile(false);
                causale.setIva(false);
                causale.setLiquidazioneIva(true);
                causale.setNumDocRequired(false);
                causale.setSegnoIva(false);
                causale.setRigheCausale(new ArrayList());

                MROldRigaCausale aRigaCosti = new MROldRigaCausale();
                aRigaCosti.setAutoCarica(false);
                aRigaCosti.setConto(ContabUtils.leggiMastro(sx, MROldPianoDeiConti.M3));
                aRigaCosti.setDataScadenza(false);
                aRigaCosti.setKmVeicolo(false);
                aRigaCosti.setNumeroRiga(causale.getRigheCausale().size());
                aRigaCosti.setPagamento(false);
                aRigaCosti.setSegno(null);
                aRigaCosti.setVeicolo(false);

                causale.getRigheCausale().add(aRigaCosti);

                MROldRigaCausale aRigaRicavi = new MROldRigaCausale();
                aRigaRicavi.setAutoCarica(false);
                aRigaRicavi.setConto(ContabUtils.leggiMastro(sx, MROldPianoDeiConti.M4));
                aRigaRicavi.setDataScadenza(false);
                aRigaRicavi.setKmVeicolo(false);
                aRigaRicavi.setNumeroRiga(causale.getRigheCausale().size());
                aRigaRicavi.setPagamento(false);
                aRigaRicavi.setSegno(null);
                aRigaRicavi.setVeicolo(false);

                causale.getRigheCausale().add(aRigaRicavi);

                MROldRigaCausale aRigaProfittiPerdite = new MROldRigaCausale();
                aRigaProfittiPerdite.setAutoCarica(false);
                aRigaProfittiPerdite.setConto(ContabUtils.leggiSottoconto(
                        sx,
                        MROldPianoDeiConti.MASTRO_CONTI_ORDINE,
                        MROldPianoDeiConti.CONTO_PROFITTI_PERDITE,
                        MROldPianoDeiConti.SOTTOCONTO_PROFITTI_PERDITE));
                aRigaProfittiPerdite.setDataScadenza(false);
                aRigaProfittiPerdite.setKmVeicolo(false);
                aRigaProfittiPerdite.setNumeroRiga(causale.getRigheCausale().size());
                aRigaProfittiPerdite.setPagamento(false);
                aRigaProfittiPerdite.setSegno(null);
                aRigaProfittiPerdite.setVeicolo(false);

                causale.getRigheCausale().add(aRigaProfittiPerdite);

                sx.saveOrUpdate(causale);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaCausaleChiusuraStatoPatrimoniale(Session sx) {
        try {
            MROldCausalePrimanota causale = (MROldCausalePrimanota) sx.get(MROldCausalePrimanota.class, MROldCausalePrimanota.CHIUSURA_STATO_PATRIMONIALE);
            if (causale == null) {
                causale = new MROldCausalePrimanota();
                causale.setCodice(MROldCausalePrimanota.CHIUSURA_STATO_PATRIMONIALE);
                causale.setDataDocRequired(false);
                causale.setDescrizione(bundle.getString("VersionUtils.msgChiusuraStatoPatrimoniale"));
                causale.setGestioneIvaIndetraibile(false);
                causale.setIva(false);
                causale.setLiquidazioneIva(true);
                causale.setNumDocRequired(false);
                causale.setSegnoIva(false);
                causale.setRigheCausale(new ArrayList());

                MROldRigaCausale aRigaAttivita = new MROldRigaCausale();
                aRigaAttivita.setAutoCarica(false);
                aRigaAttivita.setConto(ContabUtils.leggiMastro(sx, MROldPianoDeiConti.M1));
                aRigaAttivita.setDataScadenza(false);
                aRigaAttivita.setKmVeicolo(false);
                aRigaAttivita.setNumeroRiga(causale.getRigheCausale().size());
                aRigaAttivita.setPagamento(false);
                aRigaAttivita.setSegno(null);
                aRigaAttivita.setVeicolo(false);

                causale.getRigheCausale().add(aRigaAttivita);

                MROldRigaCausale aRigaPassivita = new MROldRigaCausale();
                aRigaPassivita.setAutoCarica(false);
                aRigaPassivita.setConto(ContabUtils.leggiMastro(sx, MROldPianoDeiConti.M2));
                aRigaPassivita.setDataScadenza(false);
                aRigaPassivita.setKmVeicolo(false);
                aRigaPassivita.setNumeroRiga(causale.getRigheCausale().size());
                aRigaPassivita.setPagamento(false);
                aRigaPassivita.setSegno(null);
                aRigaPassivita.setVeicolo(false);

                causale.getRigheCausale().add(aRigaPassivita);

                MROldRigaCausale aRigaStatoPatrimoniale = new MROldRigaCausale();
                aRigaStatoPatrimoniale.setAutoCarica(false);
                aRigaStatoPatrimoniale.setConto(ContabUtils.leggiSottoconto(
                        sx,
                        MROldPianoDeiConti.MASTRO_CONTI_ORDINE,
                        MROldPianoDeiConti.CONTO_STATO_PATRIMONIALE,
                        MROldPianoDeiConti.SOTTOCONTO_STATO_PATRIMONIALE));
                aRigaStatoPatrimoniale.setDataScadenza(false);
                aRigaStatoPatrimoniale.setKmVeicolo(false);
                aRigaStatoPatrimoniale.setNumeroRiga(causale.getRigheCausale().size());
                aRigaStatoPatrimoniale.setPagamento(false);
                aRigaStatoPatrimoniale.setSegno(null);
                aRigaStatoPatrimoniale.setVeicolo(false);

                causale.getRigheCausale().add(aRigaStatoPatrimoniale);

                sx.saveOrUpdate(causale);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaContoStatoPatrimoniale(Session sx) {
        try {
            MROldPianoDeiConti contoStatoPatrimoniale = ContabUtils.leggiConto(
                    sx,
                    MROldPianoDeiConti.MASTRO_CONTI_ORDINE,
                    MROldPianoDeiConti.CONTO_STATO_PATRIMONIALE);
            if (contoStatoPatrimoniale == null) {
                contoStatoPatrimoniale = new Conto(MROldPianoDeiConti.M5, MROldPianoDeiConti.CONTO_STATO_PATRIMONIALE, bundle.getString("VersionUtils.msgC_STATO_PATRIMONIALE"));
                sx.saveOrUpdate(contoStatoPatrimoniale);
            }

            MROldPianoDeiConti sottocontoStatoPatrimoniale = ContabUtils.leggiSottoconto(
                    sx,
                    MROldPianoDeiConti.MASTRO_CONTI_ORDINE,
                    MROldPianoDeiConti.CONTO_STATO_PATRIMONIALE,
                    MROldPianoDeiConti.SOTTOCONTO_STATO_PATRIMONIALE);
            if (sottocontoStatoPatrimoniale == null) {
                sottocontoStatoPatrimoniale = new MROldSottoconto(
                        MROldPianoDeiConti.M5,
                        MROldPianoDeiConti.CONTO_STATO_PATRIMONIALE,
                        MROldPianoDeiConti.SOTTOCONTO_STATO_PATRIMONIALE,
                        bundle.getString("VersionUtils.msgC_STATO_PATRIMONIALE"));
                sx.saveOrUpdate(sottocontoStatoPatrimoniale);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaContoCarteDiCredito(Session sx) {
        try {
            MROldPianoDeiConti sottocontoCarteBancomat = ContabUtils.leggiSottoconto(
                    sx,
                    MROldPianoDeiConti.MASTRO_ATTIVITA,
                    MROldPianoDeiConti.CONTO_CASSA,
                    MROldPianoDeiConti.SOTTOCONTO_CASSA_CARTE_BANCOMAT);
            if (sottocontoCarteBancomat == null) {
                sottocontoCarteBancomat = new MROldSottoconto(
                        MROldPianoDeiConti.MASTRO_ATTIVITA,
                        MROldPianoDeiConti.CONTO_CASSA,
                        MROldPianoDeiConti.SOTTOCONTO_CASSA_CARTE_BANCOMAT,
                        bundle.getString("VersionUtils.msgCarteDiCredito"));
                sx.saveOrUpdate(sottocontoCarteBancomat);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaContoVoucher(Session sx) {
        try {
            MROldPianoDeiConti sottocontoVoucher = ContabUtils.leggiSottoconto(
                    sx,
                    MROldPianoDeiConti.MASTRO_ATTIVITA,
                    MROldPianoDeiConti.CONTO_CASSA,
                    MROldPianoDeiConti.SOTTOCONTO_CASSA_VOUCHER);
            if (sottocontoVoucher == null) {
                sottocontoVoucher = new MROldSottoconto(
                        MROldPianoDeiConti.MASTRO_ATTIVITA,
                        MROldPianoDeiConti.CONTO_CASSA,
                        MROldPianoDeiConti.SOTTOCONTO_CASSA_VOUCHER,
                        bundle.getString("VersionUtils.msgVoucher"));
                sx.saveOrUpdate(sottocontoVoucher);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaContoAVista(Session sx) {
        try {
            MROldPianoDeiConti sottocontoAVista = ContabUtils.leggiSottoconto(
                    sx,
                    MROldPianoDeiConti.MASTRO_ATTIVITA,
                    MROldPianoDeiConti.CONTO_CASSA,
                    MROldPianoDeiConti.SOTTOCONTO_CASSA_A_VISTA);
            if (sottocontoAVista == null) {
                sottocontoAVista = new MROldSottoconto(
                        MROldPianoDeiConti.MASTRO_ATTIVITA,
                        MROldPianoDeiConti.CONTO_CASSA,
                        MROldPianoDeiConti.SOTTOCONTO_CASSA_A_VISTA,
                        bundle.getString("VersionUtils.msgAVista"));
                sx.saveOrUpdate(sottocontoAVista);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaContoChiusuraCasse(Session sx ) {
        try {
            MROldPianoDeiConti contoCreditiDiversi = ContabUtils.leggiConto(
                    sx,
                    MROldPianoDeiConti.MASTRO_ATTIVITA,
                    MROldPianoDeiConti.CONTO_CREDITI_DIVERSI);
            if (contoCreditiDiversi == null) {
                contoCreditiDiversi = new Conto(
                        MROldPianoDeiConti.MASTRO_ATTIVITA,
                        MROldPianoDeiConti.CONTO_CREDITI_DIVERSI,
                        bundle.getString("VersionUtils.msgCreditiDiversi"));
                sx.saveOrUpdate(contoCreditiDiversi);
            }

            MROldPianoDeiConti sottocontoChiusuraCasse = ContabUtils.leggiSottoconto(
                    sx,
                    MROldPianoDeiConti.MASTRO_ATTIVITA,
                    MROldPianoDeiConti.CONTO_CREDITI_DIVERSI,
                    MROldPianoDeiConti.SOTTOCONTO_CHIUSURA_CASSE);
            if (sottocontoChiusuraCasse == null) {
                sottocontoChiusuraCasse = new MROldSottoconto(
                        MROldPianoDeiConti.MASTRO_ATTIVITA,
                        MROldPianoDeiConti.CONTO_CREDITI_DIVERSI,
                        MROldPianoDeiConti.SOTTOCONTO_CHIUSURA_CASSE,
                        bundle.getString("VersionUtils.msgContoChiusuraCasse"));
                sx.saveOrUpdate(sottocontoChiusuraCasse);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaContoAnticipiClienti(Session sx) {
        try {
            MROldPianoDeiConti contoDebitiDiversi = ContabUtils.leggiConto(
                    sx,
                    MROldPianoDeiConti.MASTRO_PASSIVITA,
                    MROldPianoDeiConti.CONTO_DEBITI_DIVERSI);
            if (contoDebitiDiversi == null) {
                contoDebitiDiversi = new Conto(
                        MROldPianoDeiConti.MASTRO_PASSIVITA,
                        MROldPianoDeiConti.CONTO_DEBITI_DIVERSI,
                        bundle.getString("VersionUtils.msgDebitiDiversi"));
                sx.saveOrUpdate(contoDebitiDiversi);
            }

            MROldPianoDeiConti sottocontoClientiContoAnticipi = ContabUtils.leggiSottoconto(
                    sx,
                    MROldPianoDeiConti.MASTRO_PASSIVITA,
                    MROldPianoDeiConti.CONTO_DEBITI_DIVERSI,
                    MROldPianoDeiConti.SOTTOCONTO_CLIENTI_C_ANTICIPI);
            if (sottocontoClientiContoAnticipi == null) {
                sottocontoClientiContoAnticipi = new MROldSottoconto(
                        MROldPianoDeiConti.MASTRO_PASSIVITA,
                        MROldPianoDeiConti.CONTO_DEBITI_DIVERSI,
                        MROldPianoDeiConti.SOTTOCONTO_CLIENTI_C_ANTICIPI,
                        bundle.getString("VersionUtils.msgClientiContoAnticipi"));
                sx.saveOrUpdate(sottocontoClientiContoAnticipi);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaCausaleAperturaStatoPatrimoniale(Session sx) {
        try {
            MROldCausalePrimanota causale = (MROldCausalePrimanota) sx.get(MROldCausalePrimanota.class, MROldCausalePrimanota.APERTURA_ANNUALE);
            if (causale == null) {
                causale = new MROldCausalePrimanota();
                causale.setCodice(MROldCausalePrimanota.APERTURA_ANNUALE);
                causale.setDataDocRequired(false);
                causale.setDescrizione(bundle.getString("VersionUtils.msgAperturaAnnuale"));
                causale.setGestioneIvaIndetraibile(false);
                causale.setIva(false);
                causale.setLiquidazioneIva(true);
                causale.setNumDocRequired(false);
                causale.setSegnoIva(false);
                causale.setRigheCausale(new ArrayList());

                MROldRigaCausale aRigaAttivita = new MROldRigaCausale();
                aRigaAttivita.setAutoCarica(false);
                aRigaAttivita.setConto(ContabUtils.leggiMastro(sx, MROldPianoDeiConti.M1));
                aRigaAttivita.setDataScadenza(false);
                aRigaAttivita.setKmVeicolo(false);
                aRigaAttivita.setNumeroRiga(causale.getRigheCausale().size());
                aRigaAttivita.setPagamento(false);
                aRigaAttivita.setSegno(null);
                aRigaAttivita.setVeicolo(false);

                causale.getRigheCausale().add(aRigaAttivita);

                MROldRigaCausale aRigaPassivita = new MROldRigaCausale();
                aRigaPassivita.setAutoCarica(false);
                aRigaPassivita.setConto(ContabUtils.leggiMastro(sx, MROldPianoDeiConti.M2));
                aRigaPassivita.setDataScadenza(false);
                aRigaPassivita.setKmVeicolo(false);
                aRigaPassivita.setNumeroRiga(causale.getRigheCausale().size());
                aRigaPassivita.setPagamento(false);
                aRigaPassivita.setSegno(null);
                aRigaPassivita.setVeicolo(false);

                causale.getRigheCausale().add(aRigaPassivita);

                MROldRigaCausale aRigaStatoPatrimoniale = new MROldRigaCausale();
                aRigaStatoPatrimoniale.setAutoCarica(false);
                aRigaStatoPatrimoniale.setConto(ContabUtils.leggiSottoconto(
                        sx,
                        MROldPianoDeiConti.MASTRO_CONTI_ORDINE,
                        MROldPianoDeiConti.CONTO_STATO_PATRIMONIALE,
                        MROldPianoDeiConti.SOTTOCONTO_STATO_PATRIMONIALE));
                aRigaStatoPatrimoniale.setDataScadenza(false);
                aRigaStatoPatrimoniale.setKmVeicolo(false);
                aRigaStatoPatrimoniale.setNumeroRiga(causale.getRigheCausale().size());
                aRigaStatoPatrimoniale.setPagamento(false);
                aRigaStatoPatrimoniale.setSegno(null);
                aRigaStatoPatrimoniale.setVeicolo(false);
                causale.getRigheCausale().add(aRigaStatoPatrimoniale);
                sx.saveOrUpdate(causale);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaCausaleRisultatoEsercizio(Session sx) {
        try {
            MROldCausalePrimanota causale = (MROldCausalePrimanota) sx.get(MROldCausalePrimanota.class, MROldCausalePrimanota.RISULTATO_ESERCIZIO);
            if (causale == null) {
                causale = new MROldCausalePrimanota();
                causale.setCodice(MROldCausalePrimanota.RISULTATO_ESERCIZIO);
                causale.setDataDocRequired(false);
                causale.setDescrizione(bundle.getString("VersionUtils.msgRisultatoEsercizio"));
                causale.setGestioneIvaIndetraibile(false);
                causale.setIva(false);
                causale.setLiquidazioneIva(true);
                causale.setNumDocRequired(false);
                causale.setSegnoIva(false);
                causale.setRigheCausale(new ArrayList());
                MROldRigaCausale aRigaProfittiPerdite = new MROldRigaCausale();
                aRigaProfittiPerdite.setAutoCarica(false);
                aRigaProfittiPerdite.setConto(ContabUtils.leggiSottoconto(
                        sx,
                        MROldPianoDeiConti.MASTRO_CONTI_ORDINE,
                        MROldPianoDeiConti.CONTO_PROFITTI_PERDITE,
                        MROldPianoDeiConti.SOTTOCONTO_PROFITTI_PERDITE));
                aRigaProfittiPerdite.setDataScadenza(false);
                aRigaProfittiPerdite.setKmVeicolo(false);
                aRigaProfittiPerdite.setNumeroRiga(causale.getRigheCausale().size());
                aRigaProfittiPerdite.setPagamento(false);
                aRigaProfittiPerdite.setSegno(null);
                aRigaProfittiPerdite.setVeicolo(false);

                causale.getRigheCausale().add(aRigaProfittiPerdite);

                MROldRigaCausale aRigaUtileEsercizio = new MROldRigaCausale();
                aRigaUtileEsercizio.setAutoCarica(false);
                aRigaUtileEsercizio.setConto(ContabUtils.leggiSottoconto(
                        sx,
                        MROldPianoDeiConti.MASTRO_UTILE_ESERCIZIO,
                        MROldPianoDeiConti.CONTO_UTILE_ESERCIZIO,
                        MROldPianoDeiConti.SOTTOCONTO_UTILE_ESERCIZIO));
                aRigaUtileEsercizio.setDataScadenza(false);
                aRigaUtileEsercizio.setKmVeicolo(false);
                aRigaUtileEsercizio.setNumeroRiga(causale.getRigheCausale().size());
                aRigaUtileEsercizio.setPagamento(false);
                aRigaUtileEsercizio.setSegno(null);
                aRigaUtileEsercizio.setVeicolo(false);

                causale.getRigheCausale().add(aRigaUtileEsercizio);

                MROldRigaCausale aRigaPerditaEsercizio = new MROldRigaCausale();
                aRigaPerditaEsercizio.setAutoCarica(false);
                aRigaPerditaEsercizio.setConto(ContabUtils.leggiSottoconto(
                        sx,
                        MROldPianoDeiConti.MASTRO_PERDITA_ESERCIZIO,
                        MROldPianoDeiConti.CONTO_PERDITA_ESERCIZIO,
                        MROldPianoDeiConti.SOTTOCONTO_PERDITA_ESERCIZIO));
                aRigaPerditaEsercizio.setDataScadenza(false);
                aRigaPerditaEsercizio.setKmVeicolo(false);
                aRigaPerditaEsercizio.setNumeroRiga(causale.getRigheCausale().size());
                aRigaPerditaEsercizio.setPagamento(false);
                aRigaPerditaEsercizio.setSegno(null);
                aRigaPerditaEsercizio.setVeicolo(false);

                causale.getRigheCausale().add(aRigaPerditaEsercizio);

                sx.saveOrUpdate(causale);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaCausaleAperturaCassa(Session sx) {
        try {
            MROldCausalePrimanota causale = (MROldCausalePrimanota) sx.get(MROldCausalePrimanota.class, MROldCausalePrimanota.APERTURA_CASSA);
            if (causale == null) {
                causale = new MROldCausalePrimanota();
                causale.setCodice(MROldCausalePrimanota.APERTURA_CASSA);
                causale.setDataDocRequired(false);
                causale.setDescrizione(bundle.getString("VersionUtils.msgAperturaCassa"));
                causale.setGestioneIvaIndetraibile(false);
                causale.setIva(false);
                causale.setLiquidazioneIva(true);
                causale.setNumDocRequired(false);
                causale.setSegnoIva(false);
                causale.setRigheCausale(new ArrayList());

                MROldRigaCausale aRigaCassa = new MROldRigaCausale();
                aRigaCassa.setAutoCarica(false);
                aRigaCassa.setConto(ContabUtils.leggiConto(sx, MROldPianoDeiConti.M1, MROldPianoDeiConti.CONTO_CASSA));
                aRigaCassa.setDataScadenza(false);
                aRigaCassa.setKmVeicolo(false);
                aRigaCassa.setNumeroRiga(causale.getRigheCausale().size());
                aRigaCassa.setPagamento(true);
                aRigaCassa.setSegno(null);
                aRigaCassa.setVeicolo(false);

                causale.getRigheCausale().add(aRigaCassa);

                MROldRigaCausale aRigaChiusuraCasse = new MROldRigaCausale();
                aRigaChiusuraCasse.setAutoCarica(false);
                aRigaChiusuraCasse.setConto(ContabUtils.leggiSottoconto(
                        sx,
                        MROldPianoDeiConti.MASTRO_ATTIVITA,
                        MROldPianoDeiConti.CONTO_CREDITI_DIVERSI,
                        MROldPianoDeiConti.SOTTOCONTO_CHIUSURA_CASSE));
                aRigaChiusuraCasse.setDataScadenza(false);
                aRigaChiusuraCasse.setKmVeicolo(false);
                aRigaChiusuraCasse.setNumeroRiga(causale.getRigheCausale().size());
                aRigaChiusuraCasse.setPagamento(true);
                aRigaChiusuraCasse.setSegno(null);
                aRigaChiusuraCasse.setVeicolo(false);

                causale.getRigheCausale().add(aRigaChiusuraCasse);

                sx.saveOrUpdate(causale);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaCausaleFatturaDiAcconto(Session sx ) {
        try {
            MROldCausalePrimanota causale = (MROldCausalePrimanota) sx.get(MROldCausalePrimanota.class, MROldCausalePrimanota.FATTURA_DI_ACCONTO);
            if (causale == null) {
                causale = new MROldCausalePrimanota();
                causale.setCodice(MROldCausalePrimanota.FATTURA_DI_ACCONTO);
                causale.setDataDocRequired(true);
                causale.setDescrizione(bundle.getString("VersionUtils.msgFatturaDiAcconto"));
                causale.setGestioneIvaIndetraibile(false);
                causale.setIva(true);
                causale.setLiquidazioneIva(true);
                causale.setNumDocRequired(true);
                causale.setSegnoIva(MROldCausalePrimanota.SEGNO_IVA_POSITIVO);
                causale.setRigheCausale(new ArrayList());
                MROldCausalePrimanota fatturaVendita = (MROldCausalePrimanota) sx.get(MROldCausalePrimanota.class, MROldCausalePrimanota.FATTURA_DI_VENDITA);
                causale.setRegistroIva(fatturaVendita.getRegistroIva());

                MROldRigaCausale aRigaCliente = new MROldRigaCausale();
                aRigaCliente.setAutoCarica(true);
                aRigaCliente.setConto(ContabUtils.leggiConto(sx, MROldPianoDeiConti.CLIENTI));
                aRigaCliente.setDataScadenza(false);
                aRigaCliente.setKmVeicolo(false);
                aRigaCliente.setNumeroRiga(causale.getRigheCausale().size());
                aRigaCliente.setPagamento(false);
                aRigaCliente.setSegno(MROldCausalePrimanota.DARE);
                aRigaCliente.setVeicolo(false);

                causale.getRigheCausale().add(aRigaCliente);

                MROldRigaCausale aRigaAnticipo = new MROldRigaCausale();
                aRigaAnticipo.setAutoCarica(false);
                aRigaAnticipo.setConto(ContabUtils.leggiSottoconto(sx, MROldPianoDeiConti.CLIENTI_C_ANTICIPI));
                aRigaAnticipo.setDataScadenza(false);
                aRigaAnticipo.setKmVeicolo(false);
                aRigaAnticipo.setNumeroRiga(causale.getRigheCausale().size());
                aRigaAnticipo.setPagamento(false);
                aRigaAnticipo.setSegno(MROldCausalePrimanota.AVERE);
                aRigaAnticipo.setVeicolo(false);

                causale.getRigheCausale().add(aRigaAnticipo);

                MROldRigaCausale aRigaImposta = new MROldRigaCausale();
                aRigaImposta.setAutoCarica(false);
                aRigaImposta.setConto(ContabUtils.leggiSottoconto(sx, MROldPianoDeiConti.IVA_C_VENDITA));
                aRigaImposta.setDataScadenza(false);
                aRigaImposta.setKmVeicolo(false);
                aRigaImposta.setNumeroRiga(causale.getRigheCausale().size());
                aRigaImposta.setPagamento(false);
                aRigaImposta.setSegno(MROldCausalePrimanota.AVERE);
                aRigaImposta.setVeicolo(false);

                causale.getRigheCausale().add(aRigaImposta);

                sx.saveOrUpdate(causale);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaCausaleFatturaDiSaldo(Session sx ) {
        try {
            MROldCausalePrimanota causale = (MROldCausalePrimanota) sx.get(MROldCausalePrimanota.class, MROldCausalePrimanota.FATTURA_DI_SALDO);
            if (causale == null) {
                causale = new MROldCausalePrimanota();
                causale.setCodice(MROldCausalePrimanota.FATTURA_DI_SALDO);
                causale.setDataDocRequired(true);
                causale.setDescrizione(bundle.getString("VersionUtils.msgFatturaDiSaldo"));
                causale.setGestioneIvaIndetraibile(false);
                causale.setIva(true);
                causale.setLiquidazioneIva(true);
                causale.setNumDocRequired(true);
                causale.setSegnoIva(MROldCausalePrimanota.SEGNO_IVA_POSITIVO);
                causale.setRigheCausale(new ArrayList());
                MROldCausalePrimanota fatturaVendita = (MROldCausalePrimanota) sx.get(MROldCausalePrimanota.class, MROldCausalePrimanota.FATTURA_DI_VENDITA);
                causale.setRegistroIva(fatturaVendita.getRegistroIva());

                MROldRigaCausale aRigaCliente = new MROldRigaCausale();
                aRigaCliente.setAutoCarica(true);
                aRigaCliente.setConto(ContabUtils.leggiConto(sx, MROldPianoDeiConti.CLIENTI));
                aRigaCliente.setDataScadenza(false);
                aRigaCliente.setKmVeicolo(false);
                aRigaCliente.setNumeroRiga(causale.getRigheCausale().size());
                aRigaCliente.setPagamento(false);
                aRigaCliente.setSegno(MROldCausalePrimanota.DARE);
                aRigaCliente.setVeicolo(false);

                causale.getRigheCausale().add(aRigaCliente);

                MROldRigaCausale aRigaGirocontoAnticipo = new MROldRigaCausale();
                aRigaGirocontoAnticipo.setAutoCarica(true);
                aRigaGirocontoAnticipo.setConto(ContabUtils.leggiSottoconto(sx, MROldPianoDeiConti.CLIENTI_C_ANTICIPI));
                aRigaGirocontoAnticipo.setDataScadenza(false);
                aRigaGirocontoAnticipo.setKmVeicolo(false);
                aRigaGirocontoAnticipo.setNumeroRiga(causale.getRigheCausale().size());
                aRigaGirocontoAnticipo.setPagamento(false);
                aRigaGirocontoAnticipo.setSegno(MROldCausalePrimanota.DARE);
                aRigaGirocontoAnticipo.setVeicolo(false);

                causale.getRigheCausale().add(aRigaGirocontoAnticipo);

                MROldRigaCausale aRigaRicavi = new MROldRigaCausale();
                aRigaRicavi.setAutoCarica(true);
                aRigaRicavi.setConto(ContabUtils.leggiConto(sx, MROldPianoDeiConti.RICAVI_VENDITE));
                aRigaRicavi.setDataScadenza(false);
                aRigaRicavi.setKmVeicolo(true);
                aRigaRicavi.setNumeroRiga(causale.getRigheCausale().size());
                aRigaRicavi.setPagamento(false);
                aRigaRicavi.setSegno(MROldCausalePrimanota.AVERE);
                aRigaRicavi.setVeicolo(true);

                MROldRigaCausale aRigaImposta = new MROldRigaCausale();
                aRigaImposta.setAutoCarica(true);
                aRigaImposta.setConto(ContabUtils.leggiSottoconto(sx, MROldPianoDeiConti.IVA_C_VENDITA));
                aRigaImposta.setDataScadenza(false);
                aRigaImposta.setKmVeicolo(false);
                aRigaImposta.setNumeroRiga(causale.getRigheCausale().size());
                aRigaImposta.setPagamento(false);
                aRigaImposta.setSegno(MROldCausalePrimanota.AVERE);
                aRigaImposta.setVeicolo(false);

                causale.getRigheCausale().add(aRigaImposta);

                sx.saveOrUpdate(causale);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaCausaleChiusuraCassa(Session sx) {
        try {
            MROldCausalePrimanota causale = (MROldCausalePrimanota) sx.get(MROldCausalePrimanota.class, MROldCausalePrimanota.CHIUSURA_CASSA);
            if (causale == null) {
                causale = new MROldCausalePrimanota();
                causale.setCodice(MROldCausalePrimanota.CHIUSURA_CASSA);
                causale.setDataDocRequired(false);
                causale.setDescrizione(bundle.getString("VersionUtils.msgChiusuraCassa"));
                causale.setGestioneIvaIndetraibile(false);
                causale.setIva(false);
                causale.setLiquidazioneIva(true);
                causale.setNumDocRequired(false);
                causale.setSegnoIva(false);
                causale.setRigheCausale(new ArrayList());

                MROldRigaCausale aRigaCassa = new MROldRigaCausale();
                aRigaCassa.setAutoCarica(false);
                aRigaCassa.setConto(ContabUtils.leggiConto(sx, MROldPianoDeiConti.M1, MROldPianoDeiConti.CONTO_CASSA));
                aRigaCassa.setDataScadenza(false);
                aRigaCassa.setKmVeicolo(false);
                aRigaCassa.setNumeroRiga(causale.getRigheCausale().size());
                aRigaCassa.setPagamento(true);
                aRigaCassa.setSegno(null);
                aRigaCassa.setVeicolo(false);

                causale.getRigheCausale().add(aRigaCassa);

                MROldRigaCausale aRigaChiusuraCasse = new MROldRigaCausale();
                aRigaChiusuraCasse.setAutoCarica(false);
                aRigaChiusuraCasse.setConto(ContabUtils.leggiSottoconto(
                        sx,
                        MROldPianoDeiConti.MASTRO_ATTIVITA,
                        MROldPianoDeiConti.CONTO_CREDITI_DIVERSI,
                        MROldPianoDeiConti.SOTTOCONTO_CHIUSURA_CASSE));
                aRigaChiusuraCasse.setDataScadenza(false);
                aRigaChiusuraCasse.setKmVeicolo(false);
                aRigaChiusuraCasse.setNumeroRiga(causale.getRigheCausale().size());
                aRigaChiusuraCasse.setPagamento(true);
                aRigaChiusuraCasse.setSegno(null);
                aRigaChiusuraCasse.setVeicolo(false);

                causale.getRigheCausale().add(aRigaChiusuraCasse);

                sx.saveOrUpdate(causale);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaTariffeCondivise(Session sx) {
        try {
            Iterator preventivi = sx.createQuery("select p from Preventivo p where p.tariffa in (select p.tariffa from Prenotazione p)").iterate();
            while (preventivi.hasNext()) {
                MROldPreventivo p = (MROldPreventivo) preventivi.next();
                p.setTariffa(new MROldTariffa(p.getTariffa()));
                sx.saveOrUpdate(p.getTariffa());
                sx.saveOrUpdate(p);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void aggiornaIsWarningBlackList(Session sx) {
        try {
            Query updateIsWarningTrue = sx.createQuery("update Clienti c set c.isWarning = true where c.listaNera is not null");
            Query updateIsWarningFalse = sx.createQuery("update Clienti c set c.isWarning = false where c.listaNera is null");
            int editTrue = updateIsWarningTrue.executeUpdate();
            int editFalse = updateIsWarningFalse.executeUpdate();
            System.out.println("Inizializzati per isWarning: " + editTrue + " clienti con lista nera piena e " + editFalse + " clienti con lista nera vuota");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            Query updateIsWarningTrue = sx.createQuery("update Conducenti c set c.isWarning = true where c.listaNera is not null");
            Query updateIsWarningFalse = sx.createQuery("update Conducenti c set c.isWarning = false where c.listaNera is null");
            int editTrue = updateIsWarningTrue.executeUpdate();
            int editFalse = updateIsWarningFalse.executeUpdate();
            System.out.println("Inizializzati per isWarning: " + editTrue + " conducenti con lista nera piena e " + editFalse + " conducenti con lista nera vuota");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void verificaNumerazioneClienti(Session sx) {
        try {
            MROldNumerazione numerazione = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.CLIENTI);
            if (numerazione == null) {
                numerazione = new MROldNumerazioneGenerica();
                numerazione.setDescrizione("Codice cliente");
                numerazione.setDocumento(MROldNumerazione.CLIENTI);
                sx.saveOrUpdate(numerazione);
                //Creiamo anche il progressivo con l'ultimo id inserito, per simulare il vecchio funzionamento.
                Number max = (Number) sx.createQuery("select max(c.id) from Clienti c").uniqueResult();
                if (max != null) {
                    NumerazioniUtils.aggiornaProgressivo(sx, numerazione, 0, max.intValue());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void verificaNumerazioniPartite(Session sx) {
        try {
            List<MROldAffiliato> affiliati = (List<MROldAffiliato>) sx.createQuery("select a from Affiliato a").list();
            for (int i = 0; i < affiliati.size(); i++) {
                MROldAffiliato affiliato = affiliati.get(i);
                if (affiliato.getNumerazioni().get(MROldNumerazione.PARTITE) == null) {
                    MROldNumerazione numerazione = new MROldNumerazioneAffiliato();
                    numerazione.setDescrizione("Partite contabili");
                    numerazione.setDocumento(MROldNumerazione.PARTITE);
                    numerazione.setAffiliato(affiliato);
                    affiliato.getNumerazioni().put(MROldNumerazione.PARTITE, numerazione);
                    sx.saveOrUpdate(affiliato);
                }
            }
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
    public static void verificaProgressiviPartite(Session sx) {
        try {
            List<MROldAffiliato> affiliati = (List<MROldAffiliato>) sx.createQuery("select a from Affiliato a").list();
            for (int i = 0; i < affiliati.size(); i++) {
                MROldAffiliato affiliato = affiliati.get(i);
                MROldNumerazione numerazione = NumerazioniUtils.getNumerazione(sx, affiliato, MROldNumerazione.PARTITE);
                NumerazioniUtils.aggiornaProgressivo(sx, numerazione, 0, 0);
            }
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
    public static void verificaCreaPartiteAperte(Session sx) {
        try {
            List<MROldAffiliato> affiliati = (List<MROldAffiliato>) sx.createQuery("select a from Affiliato a").list();
            for (int i = 0; i < affiliati.size(); i++) {
                MROldAffiliato affiliato = affiliati.get(i);
                MROldNumerazione numerazione = NumerazioniUtils.getNumerazione(sx, affiliato, MROldNumerazione.PARTITE);
                Integer numero = NumerazioniUtils.nuovoNumero(sx, numerazione, 0);
                Iterator<MROldDocumentoFiscale> fatture = (Iterator<MROldDocumentoFiscale>) sx.createQuery(
                        "select d from DocumentoFiscale d "
                        + "where d not in (select p.fattura from Partita p) "
                        + "and d.affiliato = :affiliato "
                        + "order by d.data, d.id").
                        setParameter("affiliato", affiliato).iterate();
                int count = 0;
                while (fatture.hasNext()) {
                    MROldDocumentoFiscale fattura = fatture.next();
                    MROldPartita partita = new MROldPartita();
                    partita.setNumerazione(numerazione);
                    partita.setNumero(numero + count);
                    partita.setData(fattura.getData());
                    partita.setAffiliato(affiliato);
                    partita.setFattura(fattura);
                    partita.setCliente(fattura.getCliente());
                    partita.setContratto(fattura.getContratto());
                    partita.setPrimanota(fattura.getPrimanota());
                    partita.setImporto(fattura.getTotaleFattura());
                    partita.setChiusa(false);
                    sx.saveOrUpdate(partita);
                    count++;
                }
                NumerazioniUtils.aggiornaProgressivo(sx, numerazione, 0, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void verificaChiudiPartiteAperte(Session sx) {
        try {
            Iterator<MROldPartita> partite = (Iterator<MROldPartita>) sx.createQuery("select p from Partita p where p.contratto is not null and p.chiusa = :false order by p.data, p.numero").setParameter("false", false).iterate();
            while (partite.hasNext()) {
                MROldPartita partita = partite.next();
                MROldPrimanota controPartita = (MROldPrimanota) sx.createQuery(
                        "select pn from Primanota pn "
                        + "where pn.contratto = :contratto "
                        + "and pn.causale.codice in (:codice1, :codice2) "
                        + "and pn.dataRegistrazione = :data "
                        + "and pn.totaleDocumento = :importo "
                        + "and pn.partita is null "
                        + "order by pn.numeroRegistrazione").
                        setParameter("contratto", partita.getContratto()).
                        setParameter("codice1", MROldCausalePrimanota.INCASSO_CLIENTE).
                        setParameter("codice2", MROldCausalePrimanota.INCASSO_CLIENTE_CARTA_DI_CREDITO).
                        setParameter("data", partita.getData()).
                        setParameter("importo", partita.getImporto()).
                        setMaxResults(1).
                        uniqueResult();
                if (controPartita != null) {
                    partita.getContropartite().add(controPartita);
                    partita.setChiusa(true);
                    sx.saveOrUpdate(partita);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void aggiornaPrimenote(Session sx) {
        try {
            Iterator fatture = sx.createQuery("select d from DocumentoFiscale d order by d.id"). //NOI18N
                    iterate();
            while (fatture.hasNext()) {
                MROldDocumentoFiscale d = (MROldDocumentoFiscale) fatture.next();
                //PrimanotaFactory.newPrimanota(sx, d,null,null,null,null);
                sx.saveOrUpdate(d);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void aggiornaDataVenditaAndDisponibilitaVeicoliDisabilitati(Session sx) {
        try {
            Iterator veicoli = sx.createQuery("select v from ParcoVeicoli v where v.abilitato = :false"). //NOI18N
                    setParameter("false", false). //NOI18N
                    iterate();
            while (veicoli.hasNext()) {
                MROldParcoVeicoli veicolo = (MROldParcoVeicoli) veicoli.next();
                if (veicolo.getTarga().equals("XXX" + veicolo.getId())) { //NOI18N
                    //Eliminiamo tutte le disponibilita'.
                    DisponibilitaUtils.eliminaDisponibilitaVeicolo(sx,null,veicolo);
                } else {
                    Date ultimoMovimento = (Date) sx.createQuery(
                            "select max(fine) from MovimentoAuto m where "
                            + //NOI18N
                            "m.causale.prenotazione = :false and "
                            + //NOI18N
                            "m.veicolo = :veicolo and "
                            + //NOI18N
                            "m.annullato = :false"). //NOI18N
                            setParameter("veicolo", veicolo). //NOI18N
                            setParameter("false", false). //NOI18N
                            uniqueResult();
                    if (ultimoMovimento == null) {
                        ultimoMovimento = veicolo.getDataAcquisto();
                    } else {
                        ultimoMovimento = FormattedDate.formattedDate(ultimoMovimento.getTime());
                        ultimoMovimento = FormattedDate.add(ultimoMovimento, Calendar.DAY_OF_MONTH, 1);
                    }
                    veicolo.setDataVendita(ultimoMovimento);
                    sx.saveOrUpdate(veicolo);
                    DisponibilitaUtils.creaDisponibilitaVeicolo(sx, veicolo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     */
    public static void aggiornaQuantitaFattureScontate(Session sx) {
        try {
            Iterator righeScontate = sx.createQuery(
                    "select r from RigaDocumentoFiscale r "
                    + "left join r.fattura f "
                    + "where r.totaleRiga < 0 and r.prezzoUnitario = 0 and r.quantita = 0 "
                    + "and f.prepagato = :false ").
                    setParameter("false", Boolean.FALSE).iterate();
            while (righeScontate.hasNext()) {
                MROldRigaDocumentoFiscale rigaScontata = (MROldRigaDocumentoFiscale) righeScontate.next();
                Query qx = sx.createQuery(
                        "select r from RigaDocumentoFiscale r "
                        + "left join r.fattura f "
                        + " where f.contratto = :contratto "
                        + " and f.id < :id "
                        + " and abs(r.totaleImponibileRiga + :totaleImponibile) < 0.01 "
                        + " and abs(r.totaleIvaRiga + :totaleIva) < 0.01 "
                        + " and r.tempoKm = :tempoKm "
                        + " and r.tempoExtra = :tempoExtra "
                        + " and r.franchigia = :franchigia "
                        + (rigaScontata.getCarburante() != null ? " and r.carburante = :carburante " : " and r.carburante is null ")
                        + (rigaScontata.getOptional() != null ? " and r.optional = :optional " : " and r.optional is null ")).
                        setParameter("contratto", rigaScontata.getFattura().getContratto()).
                        setParameter("id", rigaScontata.getFattura().getId()).
                        setParameter("totaleImponibile", rigaScontata.getTotaleImponibileRiga()).
                        setParameter("totaleIva", rigaScontata.getTotaleIvaRiga()).
                        setParameter("tempoKm", rigaScontata.getTempoKm()).
                        setParameter("tempoExtra", rigaScontata.getTempoExtra()).
                        setParameter("franchigia", rigaScontata.getFranchigia());

                if (rigaScontata.getCarburante() != null) {
                    qx.setParameter("carburante", rigaScontata.getCarburante());
                }
                if (rigaScontata.getOptional() != null) {
                    qx.setParameter("optional", rigaScontata.getOptional());
                }

                MROldRigaDocumentoFiscale rigaOriginale = (MROldRigaDocumentoFiscale) qx.setMaxResults(1).uniqueResult();
                if (rigaOriginale != null) {
                    rigaScontata.setQuantita(rigaOriginale.getQuantita());
                    sx.saveOrUpdate(rigaScontata);
                } else {
                    log.debug("Impossibile trovare una corrispondenza per la riga " + rigaScontata);
                }
            }
            log.debug("Aggiornamento completato.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void aggiornaColonneNuove(Statement st) throws SQLException {

            st.addBatch("UPDATE optionals SET tempo_extra = FALSE WHERE tempo_extra IS NULL");
            st.addBatch("UPDATE optionals SET giornata_ridotta = FALSE WHERE giornata_ridotta IS NULL");
            st.addBatch("UPDATE optionals SET notte_extra = FALSE WHERE notte_extra IS NULL");
            st.addBatch("UPDATE optionals SET mezza_giornata = FALSE WHERE mezza_giornata IS NULL");
            st.addBatch("UPDATE optionals SET giorno_festivo = FALSE WHERE giorno_festivo IS NULL");
            st.addBatch("UPDATE optionals SET moltiplicabile = FALSE WHERE moltiplicabile IS NULL");
            st.addBatch("UPDATE sedi SET attiva = TRUE WHERE attiva IS NULL");
            st.addBatch("UPDATE prenotazioni SET rifiutata = false WHERE rifiutata IS NULL");
//            st.addBatch("UPDATE contratto_noleggio SET "
//                    + "saldo_fatture = (select sum(round(totale_fattura * 100.0)) / 100.0 "
//                    + "from fattura_intestazione "
//                    + "where fattura_intestazione.id_contratto_noleggio = contratto_noleggio.id "
//                    + "and fattura_intestazione.prepagato = false)");
//            st.addBatch("UPDATE contratto_noleggio SET saldo_fatture = 0.0 where saldo_fatture is null");
//            st.addBatch("UPDATE fattura_intestazione SET riepilogativo = FALSE WHERE riepilogativo is null");
//            st.addBatch("UPDATE multe SET numeroProtocollo = id where numeroProtocollo is null");
 //           st.addBatch("UPDATE clienti SET id_fonte_commissione = null");
//            st.addBatch(
//                    "update multe set importo_multa = "
//                    + "to_number(replace(substring(annotazioni from '[0-9]+[.,]?[0-9]*'), ',', '.'), '999999.999999') "
//                    + "where annotazioni ~ '[0-9]+[.,]?[0-9]*'"
//                    + " and importo_multa is null");
//            ResultSet rs = st.executeQuery("SELECT * FROM listini");
////            ResultSetMetaData rsmd = rs.getMetaData();
////            boolean aggiornare = false;
////            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
////                if (rsmd.getColumnName(i).equals("data_inizio")) {
////                    log.debug("Trovata la colonna listini.data_inizio");
////                    aggiornare = true;
////                }
////            }

//            if (aggiornare) {
//                st.addBatch("update validita_listini_fonti set iniziostagione = inizio");
//                st.addBatch("update validita_listini_fonti set finestagione = fine");
//                st.addBatch("update validita_listini_fonti set inizioofferta = listini.data_inizio from listini where validita_listini_fonti.id_listino = listini.id");
//                st.addBatch("update validita_listini_fonti set fineofferta = listini.data_fine from listini where validita_listini_fonti.id_listino = listini.id");
//                st.addBatch("alter table listini drop column data_inizio");
//                st.addBatch("alter table listini drop column data_fine");
//                st.addBatch("alter table validita_listini_fonti drop column inizio");
//                st.addBatch("alter table validita_listini_fonti drop column fine");
//            }

//            st.addBatch("UPDATE clienti SET cliente = false where cliente is null");
//            st.addBatch("UPDATE clienti SET fornitore = false where fornitore is null");
//            st.addBatch("UPDATE clienti SET conducente = false where conducente is null");
//            st.addBatch("UPDATE clienti SET cliente = true where cliente = false and conducente = false and fornitore = false");
//
//            st.addBatch("alter table righe_primanota drop constraint fkb63afd376cb82a5c");
//            st.addBatch("alter table scadenze drop constraint fkca66ea876cb82a5c");
//            st.addBatch("alter table coordinate_bancarie drop constraint fkc3d000286cb82a5c");
//
//            st.addBatch("alter table prenotazioni drop constraint fk7155466af94362ee");
//            st.addBatch("alter table prenotazioni drop constraint fk7155466af94362ef");
//            st.addBatch("alter table prenotazioni drop constraint fk7155466af94362f0");
//
//            st.addBatch("alter table preventivi drop constraint fkf72ed404f94362ee");
//            st.addBatch("alter table preventivi drop constraint fkf72ed404f94362ef");
//            st.addBatch("alter table preventivi drop constraint fkf72ed404f94362f0");
//
//            st.addBatch("alter table contratto_noleggio drop constraint fkcc919fb1f94362ee");
//            st.addBatch("alter table contratto_noleggio drop constraint fkcc919fb1f94362ef");
//            st.addBatch("alter table contratto_noleggio drop constraint fkcc919fb1f94362f0");
//
//            st.addBatch("alter table clienti add column id_fornitore int4");
//            st.addBatch("alter table clienti add column id_conducente int4");
//
//            // Uso del codice solo per i clienti
//            st.addBatch("insert into clienti (id, id_fornitore, pers_fisica, ditta_ind, ragione_sociale, cognome, nome, via, numero, nazione, cap, citta, provincia, telefono1, telefono2, cellulare, email, partita_iva, codice_fiscale, data_nascita, luogo_nascita, prov_nascita, sesso, promemoria, id_pagamento, codicesottoconto, ritenutaacconto, completo, cliente, fornitore, conducente) select nextval('clienti_seq'), id, pers_fisica, ditta_ind, ragione_sociale, cognome, nome, via, numero, nazione, cap, citta, provincia, telefono1, telefono2, cellulare, email, partita_iva, codice_fiscale, data_nascita, luogo_nascita, prov_nascita, sesso, promemoria, id_pagamento, codicesottoconto, ritenutaacconto, false, false, true, false from fornitori");
//            st.addBatch("insert into clienti (id, id_conducente, pers_fisica, ditta_ind, cognome, nome, via, numero, nazione, cap, citta, provincia, telefono1, data_nascita, luogo_nascita, prov_nascita, documento, numero_documento, rilasciato_da, data_rilascio, data_scadenza, promemoria, foto, is_foto_set, categoria_patente, id_cliente_fattura, completo, cliente, fornitore, conducente) select nextval('clienti_seq'), id, true, false, cognome, nome, via, numero, nazione, cap, citta, provincia, telefono, data_nascita, luogo_nascita, provincia_nascita, documento, numero_documento, rilasciato_da, data_rilascio, data_scadenza, annotazioni, foto, is_foto_set, categoria_patente, id_cliente, false, false, false, true from conducenti");
//
//            st.addBatch("update righe_primanota set id_fornitore = clienti.id from clienti where righe_primanota.id_fornitore = clienti.id_fornitore");
//            st.addBatch("update scadenze set id_fornitore = clienti.id from clienti where scadenze.id_fornitore = clienti.id_fornitore");
//            st.addBatch("update coordinate_bancarie set id_fornitore = clienti.id from clienti where coordinate_bancarie.id_fornitore = clienti.id_fornitore");
//
//            st.addBatch("update prenotazioni set id_cond_1 = clienti.id from clienti where prenotazioni.id_cond_1 = clienti.id_conducente");
//            st.addBatch("update prenotazioni set id_cond_2 = clienti.id from clienti where prenotazioni.id_cond_2 = clienti.id_conducente");
//            st.addBatch("update prenotazioni set id_cond_3 = clienti.id from clienti where prenotazioni.id_cond_3 = clienti.id_conducente");
//
//            st.addBatch("update preventivi set id_cond_1 = clienti.id from clienti where preventivi.id_cond_1 = clienti.id_conducente");
//            st.addBatch("update preventivi set id_cond_2 = clienti.id from clienti where preventivi.id_cond_2 = clienti.id_conducente");
//            st.addBatch("update preventivi set id_cond_3 = clienti.id from clienti where preventivi.id_cond_3 = clienti.id_conducente");
//
//            st.addBatch("update contratto_noleggio set id_cond_1 = clienti.id from clienti where contratto_noleggio.id_cond_1 = clienti.id_conducente");
//            st.addBatch("update contratto_noleggio set id_cond_2 = clienti.id from clienti where contratto_noleggio.id_cond_2 = clienti.id_conducente");
//            st.addBatch("update contratto_noleggio set id_cond_3 = clienti.id from clienti where contratto_noleggio.id_cond_3 = clienti.id_conducente");
//
//            st.addBatch("alter table clienti drop column id_fornitore");
//            st.addBatch("alter table clienti drop column id_conducente");
//
//            st.addBatch("alter table righe_primanota add constraint fkb63afd376cb82a5c foreign key(id_fornitore) references clienti(id)");
//            st.addBatch("alter table scadenze add constraint fkca66ea876cb82a5c foreign key(id_fornitore) references clienti(id)");
//            st.addBatch("alter table coordinate_bancarie add constraint fkc3d000286cb82a5c foreign key(id_fornitore) references clienti(id)");
//
//            st.addBatch("alter table prenotazioni add constraint fk7155466af94362ee foreign key (id_cond_1) references clienti(id)");
//            st.addBatch("alter table prenotazioni add constraint fk7155466af94362ef foreign key (id_cond_2) references clienti(id)");
//            st.addBatch("alter table prenotazioni add constraint fk7155466af94362f0 foreign key (id_cond_3) references clienti(id)");
//
//            st.addBatch("alter table preventivi add constraint fkf72ed404f94362ee foreign key (id_cond_1) references clienti(id)");
//            st.addBatch("alter table preventivi add constraint fkf72ed404f94362ef foreign key (id_cond_2) references clienti(id)");
//            st.addBatch("alter table preventivi add constraint fkf72ed404f94362f0 foreign key (id_cond_3) references clienti(id)");
//
//            st.addBatch("alter table contratto_noleggio add constraint fkcc919fb1f94362ee foreign key (id_cond_1) references clienti(id)");
//            st.addBatch("alter table contratto_noleggio add constraint fkcc919fb1f94362ef foreign key (id_cond_2) references clienti(id)");
//            st.addBatch("alter table contratto_noleggio add constraint fkcc919fb1f94362f0 foreign key (id_cond_3) references clienti(id)");
//
//            //Aggiornamento conducenti duplicati.
//            st.addBatch("update clienti cli set conducente = true, documento = cond.documento, numero_documento = cond.numero_documento, rilasciato_da = cond.rilasciato_da, data_rilascio = cond.data_rilascio, data_scadenza = cond.data_scadenza, categoria_patente = cond.categoria_patente from clienti cond where cli.pers_fisica = true and cli.numero_documento is null and cli.id = cond.id_cliente_fattura and cli.cognome ilike cond.cognome and cli.nome ilike cond.nome and (cli.data_nascita is null or cond.data_nascita is null or cli.data_nascita = cond.data_nascita) and (cli.luogo_nascita is null or cond.luogo_nascita is null or cli.luogo_nascita ilike cond.luogo_nascita)");
//
//            st.addBatch("update contratto_noleggio set id_cond_1 = cli.id from clienti cond, clienti cli where id_cond_1 = cond.id and cli.conducente = true and cli.id = cond.id_cliente_fattura and cli.cognome ilike cond.cognome and cli.nome ilike cond.nome and (cli.data_nascita is null or cond.data_nascita is null or cli.data_nascita = cond.data_nascita) and (cli.luogo_nascita is null or cond.luogo_nascita is null or cli.luogo_nascita ilike cond.luogo_nascita)");
//            st.addBatch("update contratto_noleggio set id_cond_2 = cli.id from clienti cond, clienti cli where id_cond_2 = cond.id and cli.conducente = true and cli.id = cond.id_cliente_fattura and cli.cognome ilike cond.cognome and cli.nome ilike cond.nome and (cli.data_nascita is null or cond.data_nascita is null or cli.data_nascita = cond.data_nascita) and (cli.luogo_nascita is null or cond.luogo_nascita is null or cli.luogo_nascita ilike cond.luogo_nascita)");
//            st.addBatch("update contratto_noleggio set id_cond_3 = cli.id from clienti cond, clienti cli where id_cond_3 = cond.id and cli.conducente = true and cli.id = cond.id_cliente_fattura and cli.cognome ilike cond.cognome and cli.nome ilike cond.nome and (cli.data_nascita is null or cond.data_nascita is null or cli.data_nascita = cond.data_nascita) and (cli.luogo_nascita is null or cond.luogo_nascita is null or cli.luogo_nascita ilike cond.luogo_nascita)");
//
//            st.addBatch("update preventivi set id_cond_1 = cli.id from clienti cond, clienti cli where id_cond_1 = cond.id and cli.conducente = true and cli.id = cond.id_cliente_fattura and cli.cognome ilike cond.cognome and cli.nome ilike cond.nome and (cli.data_nascita is null or cond.data_nascita is null or cli.data_nascita = cond.data_nascita) and (cli.luogo_nascita is null or cond.luogo_nascita is null or cli.luogo_nascita ilike cond.luogo_nascita)");
//            st.addBatch("update preventivi set id_cond_2 = cli.id from clienti cond, clienti cli where id_cond_2 = cond.id and cli.conducente = true and cli.id = cond.id_cliente_fattura and cli.cognome ilike cond.cognome and cli.nome ilike cond.nome and (cli.data_nascita is null or cond.data_nascita is null or cli.data_nascita = cond.data_nascita) and (cli.luogo_nascita is null or cond.luogo_nascita is null or cli.luogo_nascita ilike cond.luogo_nascita)");
//            st.addBatch("update preventivi set id_cond_3 = cli.id from clienti cond, clienti cli where id_cond_3 = cond.id and cli.conducente = true and cli.id = cond.id_cliente_fattura and cli.cognome ilike cond.cognome and cli.nome ilike cond.nome and (cli.data_nascita is null or cond.data_nascita is null or cli.data_nascita = cond.data_nascita) and (cli.luogo_nascita is null or cond.luogo_nascita is null or cli.luogo_nascita ilike cond.luogo_nascita)");
//
//            st.addBatch("update prenotazioni set id_cond_1 = cli.id from clienti cond, clienti cli where id_cond_1 = cond.id and cli.conducente = true and cli.id = cond.id_cliente_fattura and cli.cognome ilike cond.cognome and cli.nome ilike cond.nome and (cli.data_nascita is null or cond.data_nascita is null or cli.data_nascita = cond.data_nascita) and (cli.luogo_nascita is null or cond.luogo_nascita is null or cli.luogo_nascita ilike cond.luogo_nascita)");
//            st.addBatch("update prenotazioni set id_cond_2 = cli.id from clienti cond, clienti cli where id_cond_2 = cond.id and cli.conducente = true and cli.id = cond.id_cliente_fattura and cli.cognome ilike cond.cognome and cli.nome ilike cond.nome and (cli.data_nascita is null or cond.data_nascita is null or cli.data_nascita = cond.data_nascita) and (cli.luogo_nascita is null or cond.luogo_nascita is null or cli.luogo_nascita ilike cond.luogo_nascita)");
//            st.addBatch("update prenotazioni set id_cond_3 = cli.id from clienti cond, clienti cli where id_cond_3 = cond.id and cli.conducente = true and cli.id = cond.id_cliente_fattura and cli.cognome ilike cond.cognome and cli.nome ilike cond.nome and (cli.data_nascita is null or cond.data_nascita is null or cli.data_nascita = cond.data_nascita) and (cli.luogo_nascita is null or cond.luogo_nascita is null or cli.luogo_nascita ilike cond.luogo_nascita)");
//
//            st.addBatch("delete from clienti cond using clienti cli where cli.conducente = true and cli.id = cond.id_cliente_fattura and cli.cognome ilike cond.cognome and cli.nome ilike cond.nome and (cli.data_nascita is null or cond.data_nascita is null or cli.data_nascita = cond.data_nascita) and (cli.luogo_nascita is null or cond.luogo_nascita is null or cli.luogo_nascita ilike cond.luogo_nascita)");
//
//            // Uso del codice per ogni business partner
////            st.addBatch("drop sequence codice_seq");
//            st.addBatch("drop table fornitori");
//            st.addBatch("drop table conducenti");
//            st.addBatch("update tariffe set descrizione = l.descrizione from listini l where tariffe.id_listino = l.id");
//            st.addBatch("insert into stagione_tariffa (id, descrizione, iva_inclusa, id_cod_iva, id_cod_iva_no, id_tariffa) "
//                    + "(select t.id, l.descrizione, t.iva_inclusa, t.id_cod_iva, t.id_cod_iva_no, t.id from tariffe t left join listini l on t.id_listino = l.id)");
//            st.addBatch("update importi_tariffe set id_stagione = id_tariffa where id_stagione is null");
//            st.addBatch("update importi_tariffe set id_tariffa = null");
//            st.addBatch("update stagione_tariffa set inizio = (select min(data) from contratto_noleggio) where inizio is null");
////            st.addBatch("update stagione_tariffa set inizio = (select min(iniziostagione) from validita_listini_fonti) where inizio is null");
//            st.addBatch("update stagione_tariffa set fine = (select max(finestagione) from validita_listini_fonti) where fine is null");
//            st.addBatch("update fonti_commissioni set multistagione = false where multistagione is null");
//            st.addBatch("update tariffe set multistagione = false where multistagione is null");
//            st.addBatch("update stagione_tariffa set multistagione = false where multistagione is null");
//            st.addBatch("update fattura_intestazione set totale_acconti = 0 where totale_acconti is null and tipo_documento = 'Fattura'");
//            st.addBatch("update fattura_intestazione set totale_righe = totale_imponibile where totale_righe is null and tipo_documento = 'Fattura'");
//            st.addBatch("update contratto_noleggio set saldo_acconti = 0 where saldo_acconti is null");
//            st.addBatch("update partite set id_primanota = id_partita");
//            st.addBatch("alter table partite drop column id_partita");
//            st.addBatch("UPDATE partite SET saldo = (select sum(round(cast (totale_documento AS numeric), 2)) from primenote "
//                    + "where primenote.id_partita = partite.id)");
//            st.addBatch("update primenote set data_creazione = data_reg where data_creazione is null");
//            st.addBatch("update primenote set data_modifica = data_reg where data_modifica is null");
//            st.addBatch("update primenote set id_user_modifica = id_user where id_user_modifica is null");
//            st.addBatch("update primenote set id_user_creazione = id_user where id_user_creazione is null");
//            st.addBatch("update garanzie set tipo = 'GaranziaAssegno' where tipo = 'Assegno'");
//            st.addBatch("update garanzie set tipo = 'GaranziaCarta' where tipo = 'Carta'");
//            st.addBatch("update pagamenti set assegno = false where assegno is null");
//            st.addBatch("update sconti set durata_minima = 0 where durata_minima is null");
//            st.addBatch("update parco_veicoli set id_noleggiatore = 1 where id_noleggiatore is null");
//            st.addBatch("update contratto_noleggio set data_firma_contratto = inizio where data_firma_contratto is null");
//            st.addBatch("update permessi_utente set permesso = 0 where id != 1 and resource = 'Sessioni Utenti';");
//            st.addBatch("DELETE FROM comuni WHERE prefisso is not null AND codicefiscale is not null AND codicestatistico is not null ");
//            ResultSet rs1 = st.executeQuery("SELECT max(id) FROM comuni"); //NOI18N
//            Integer maxId = new Integer(50000); // inizializzazione cautelativa per le insert
//            if (rs1.next()) {
//                maxId = rs1.getInt(1);
//            }
//            /* generazione degli statement di caricamento, rimpiazzando con l'id */
//            Iterator it = DatabaseUpdate.openTextResource("/it/aessepi/myrentcs/utils/sql/comuni_2012_09_13", ".sql"); //NOI18N
//            while (it.hasNext()) {
//                String sql = (String) it.next();
//
//                /* rimpiazzo del token PROG_ID con l'id effettivo */
//                sql = sql.replace("PROG_ID", new Integer(++maxId).toString());
//                /* aggiunta del batch di aggiornamento */
//                st.addBatch(sql);
//            }
//            it.remove();

            st.addBatch("drop index if exists log_entries_index_session_id;");
            st.addBatch("create index log_entries_index_session_id on log_entries (session_id);");

            st.addBatch("drop index if exists movimenti_auto_index_id_contratto;");
            st.addBatch("create index movimenti_auto_index_id_contratto on movimenti_auto (id_contratto);");
            st.addBatch("update codici_fiscali set luogo = 'JESOLO' where luogo = 'IESOLO';");
            st.addBatch("alter table fattura_riga alter column descrizione type text;");
            st.addBatch("update users set sconto_massimo = 100 where sconto_massimo is null and (tipo = 'ResponsabileNazionale' OR tipo = 'CapoArea')");
            st.addBatch("insert into fonti_users ("
                    + "select distinct u.id as id_user, fc.id as id_fonte "
                    + "from users u, fonti_commissioni fc "
                    + "where (tipo = 'ResponsabileNazionale' or tipo = 'CapoArea') "
                    + "and (u.id, fc.id) not in (select * from fonti_users)"
                    + ");");
            st.addBatch("update multe "
                    + " set id_noleggiatore = subquery.id_noleggiatore "
                    + " from ("
                    + "     select p.id_noleggiatore, ma.id as ma_id_movimento"
                    + "     from parco_veicoli p, movimenti_auto ma"
                    + "     where ma.id_veicolo = p.id"
                    + " ) as subquery"
                    + " where id_movimento = subquery.ma_id_movimento"
                    + ";");
            st.addBatch("INSERT INTO codici_fiscali (luogo, codice, codice2) VALUES ('LEGNAGO', 'E512', NULL);");
            st.addBatch("INSERT INTO settings (key) VALUES('optionals.no_flag_prepaid_for_operators');");

    }
    public static void aggiornaCreazioneTabelle(Statement st) throws SQLException {
        /* if (oldVersion != null && oldVersion.compareTo(new Versione(Parameters.getProgram(), 2, 38, 6)) <= 0) {
            try
            {
                st.addBatch("CREATE TABLE public.mrservice_status(id bigint NOT NULL,"+
                            "version bigint NOT NULL,description character varying(255) NOT NULL,"+
                            "is_out boolean NOT NULL,CONSTRAINT mrservice_status_pkey PRIMARY KEY (id)" +
                            ") WITH (OIDS=FALSE);");
                //st.addBatch("ALTER TABLE public.mrservice_status OWNER TO myrent;"); 
            }
            catch(Exception e)
            {
                System.out.println("Unable to create table, may it be already created? Exception:");
                e.printStackTrace();
            }
            try
            {
                st.addBatch("CREATE SEQUENCE public.mrservicestatus_seq  INCREMENT 1 MINVALUE 1 " +
                            "MAXVALUE 9223372036854775807 START 1 CACHE 1;");
                //st.addBatch("ALTER TABLE public.mrservicestatus_seq  OWNER TO myrent;"); 
            }
            catch(Exception e)
            {
                System.out.println("Unable to create table, may it be already created? Exception:");
                e.printStackTrace();
            } 
            try
            {
                st.addBatch("CREATE TABLE public.mrvehicle_service_status(id bigint NOT NULL, "+
                            "version bigint NOT NULL,end_date timestamp without time zone NOT NULL,"+ 
                            "mr_service_status_id bigint NOT NULL,mr_vehicle_id bigint NOT NULL,"+ 
                            "start_date timestamp without time zone NOT NULL,CONSTRAINT mrvehicle_service_status_pkey "
                           + " PRIMARY KEY (id),CONSTRAINT fk_9ttffofm6iavdakyr33nn7vgk FOREIGN KEY (mr_vehicle_id)"
                           + " REFERENCES public.parco_veicoli (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION, "
                           + " CONSTRAINT fk_l012vbq9tf8anxmrhmm0po9fl FOREIGN KEY (mr_service_status_id)" 
                           + " REFERENCES public.mrservice_status (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE "
                           + " NO ACTION ) WITH ( OIDS=FALSE);");
                //st.addBatch("ALTER TABLE public.mrvehicle_service_status OWNER TO myrent;"); 
            }
            catch(Exception e)
            {
                System.out.println("Unable to create table, may it be already created? Exception:");
                e.printStackTrace();
            }
        }
         */
    }

    public static void aggiornaTabelleEliminate(Statement st) throws SQLException {
            st.addBatch("ALTER TABLE FONTI_COMMISSIONI DROP COLUMN ID_LISTINO"); //NOI18N
            st.addBatch("DROP TABLE LISTINI_USERS"); //NOI18N
            st.addBatch("ALTER TABLE USERS DROP COLUMN ID_LISTINO"); //NOI18N
            st.addBatch("ALTER TABLE LISTINI DROP COLUMN ANNO_DOCUMENTO"); //NOI18N
            st.addBatch("DROP TABLE NAZIONI");
            st.addBatch("alter table tariffe drop column id_listino");
            st.addBatch("alter table tariffe drop column id_listino_extra_prepay");
            st.addBatch("alter table tariffe drop column iva_inclusa");
            st.addBatch("alter table importi_tariffe drop column id_tariffa");
    }

    public static void aggiornaAnagraficheComplete(Statement st) throws SQLException {
            st.addBatch("UPDATE clienti SET completo = TRUE FROM contratto_noleggio WHERE clienti.id = contratto_noleggio.id_cliente"); //NOI18N
            st.addBatch("UPDATE clienti SET completo = FALSE WHERE completo IS NULL");
            st.addBatch("UPDATE conducenti SET completo = TRUE FROM contratto_noleggio WHERE conducenti.id = contratto_noleggio.id_cond_1 or conducenti.id = contratto_noleggio.id_cond_2 or conducenti.id = contratto_noleggio.id_cond_3"); //NOI18N
            st.addBatch("UPDATE conducenti SET completo = FALSE WHERE completo IS NULL");
            st.addBatch("UPDATE sedi SET virtual_pos_enable = false");
            try {
                st.addBatch("INSERT INTO password (id_utente, password, data_inserimento, giorni_validita, ultima) "
                        + "SELECT us.id, us.password, now(), 2, true "
                        + "FROM users us "
                        + "LEFT JOIN password pa ON us.id = pa.id_utente "
                        + "WHERE pa.id_utente IS NULL and us.tipo <> 'Profile' and us.password is not null; ");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    public static void aggiornaNumerazioneClienti(Session sx) {
        try {
            MROldNumerazione numerazione = (MROldNumerazione) sx.createQuery("select n from Numerazione n where n.documento = :documento").
                    setParameter("documento", MROldNumerazione.CLIENTI).
                    setMaxResults(1).
                    uniqueResult();
            if (numerazione == null) {
                numerazione = new MROldNumerazione();
                numerazione.setDescrizione("Codice cliente");
                numerazione.setDocumento(MROldNumerazione.CLIENTI);
                sx.saveOrUpdate(numerazione);
            }
            Number max = (Number) sx.createQuery("select max(codice) from BusinessPartner").uniqueResult();
            Integer progressivo = NumerazioniUtils.nuovoNumero(sx, numerazione, 0);
            if (max != null && max.intValue() != progressivo) {
                NumerazioniUtils.aggiornaProgressivo(sx, numerazione, 0, max.intValue() - progressivo + 1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
