package it.myrent.ee.api.utils;

import it.aessepi.utils.beans.FormattedDate;
import it.aessepi.utils.beans.QueryData;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.db.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.*;

/**
 * Created by Shivangani on 1/9/2018.
 */
public class RicercheUtils {
    private static Log log = LogFactory.getLog(RicercheUtils.class);

    /**
     * verifica se il documento fiscale ha collegato o meno un contratto di
     * noleggio direttamente o nelle rate
     *
     * @param idDocumento
     * @return
     */
    public static boolean documentoConContratto(Session sx, Integer idDocumento) {
        boolean esiste = false;
        //Session sx = null;

        try {
            String sql = "SELECT count(r.fattura.id) FROM MROldRataContratto r WHERE r.fattura.id = :idFattura";

            Number n = (Number) sx.createQuery(sql)
                    .setParameter("idFattura", idDocumento)
                    .uniqueResult();

            if (n.intValue() > 0) {
                esiste = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return esiste;
    }

    /**
     * Cerca il fornitore collegato alla primanota
     *
     * @param primanota
     * @return
     */
    public static MROldFornitori fornitoreDaPrimanota(MROldPrimanota primanota) {
        MROldFornitori f = null;

        if (primanota != null && primanota.getRighePrimanota() != null && primanota.getRighePrimanota().size() > 0) {
            List lista = primanota.getRighePrimanota();

            for (int i = 0; i < lista.size(); i++) {
                RigaPrimanota rp = (RigaPrimanota) lista.get(i);

                if (rp.getFornitore() != null) {
                    f = rp.getFornitore();
                    break;
                }
            }
        }

        return f;
    }

    /**
     * Conta quante fatture emesse dovranno essere esportate
     * @param affiliato
     * @param dataInizio
     * @param dataFine
     * @param listaCausali
     * @param esportaFattureNonComunicate
     * @return
     */
    public static int numeroFattureVenditaEmesse(Session sx, MROldAffiliato affiliato, Date dataInizio, Date dataFine, List listaCausali, boolean esportaFattureNonComunicate) {
        int totale = 0;

        try {
            //sx = HibernateBridge.startNewSession();

            String sql =
                    "SELECT COUNT(DISTINCT d.id) "
                            + "FROM MROldDocumentoFiscale d "
                            + "WHERE 1 = 1 "
                            + "AND (d.class = Fattura) "
                            + "AND (d.data BETWEEN :inizio AND :fine "
                            + "     OR d.primanota.dataRegistrazione BETWEEN :inizio AND :fine) "
                            //+ "AND d.primanota.totaleDocumento > :zero "
                            + "AND d.affiliato.id = :idAffiliato "
                            + "AND d.primanota.causale.codice IN (:causali) ";

            if(esportaFattureNonComunicate) {
                sql += "AND d.idComunicazioneFattura is null ";
            }

            Number n = (Number) sx.createQuery(sql)
                    .setParameter("idAffiliato", affiliato.getId())
                    .setParameter("inizio", dataInizio)
                    .setParameter("fine", dataFine)
                            //.setParameter("zero", new Double(0.0))
                    .setParameterList("causali", listaCausali)
                    .uniqueResult();

            totale = n.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (sx != null && sx.isOpen()) {
                    //sx.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return totale;
    }

    /**
     * Conta quante note di credito sulle fatture emesse dovranno essere esportate
     * @param affiliato
     * @param inizio
     * @param fine
     * @param listaCausali
     * @param esportaFattureNonComunicate
     * @return
     */
    public static int numeroNoteCreditoFattureEmesse(Session sx, MROldAffiliato affiliato, Date inizio, Date fine, List listaCausali, boolean esportaFattureNonComunicate) {
        int totale = 0;

        //Session sx = null;
        try {
            //sx = HibernateBridge.startNewSession();

            String sql =
                    "SELECT COUNT(DISTINCT d.id) "
                            + "FROM MROldDocumentoFiscale d "
                            + "WHERE 1 = 1 "
                            + "AND d.class = NotaCredito "
                            + "AND (d.data BETWEEN :inizio AND :fine "
                            + "     OR d.primanota.dataRegistrazione BETWEEN :inizio AND :fine) "
                            + "AND d.affiliato.id = :idAffiliato "
                            + "AND d.primanota.causale.codice IN (:causali) ";

            if(esportaFattureNonComunicate) {
                sql += "AND d.idComunicazioneFattura is null ";
            }

            Number n = (Number) sx.createQuery(sql)
                    .setParameter("idAffiliato", affiliato.getId())
                    .setParameter("inizio", inizio)
                    .setParameter("fine", fine)
                    .setParameterList("causali", listaCausali)
                    .uniqueResult();

            totale = n.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (sx != null && sx.isOpen()) {
                    //sx.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return totale;
    }

    /**
     * Conta quante note di credito per le fatture di acquisto dovranno essere esportate
     * @param affiliato
     * @param inizio
     * @param fine
     * @param listaCausali
     * @param esportaFattureNonComunicate
     * @return
     */
    public static int numeroNoteCreditoAcquistoRegistrate(Session sx, MROldAffiliato affiliato, Date inizio, Date fine, List listaCausali, boolean esportaFattureNonComunicate) {
        int totale = 0;

        //Session sx = null;
        try {
            //sx = HibernateBridge.startNewSession();

            String sql =
                    "SELECT COUNT(DISTINCT p.id) "
                            + "FROM MROldPrimanota p "
                            + "WHERE 1 = 1 "
                            + "AND p.dataRegistrazione BETWEEN :inizio AND :fine "
                            + "AND p.affiliato.id = :idAffiliato "
                            + "AND p.causale.codice IN (:causali) ";

            if(esportaFattureNonComunicate) {
                sql += "AND p.idComunicazioneFattura is null ";
            }

            Number n = (Number) sx.createQuery(sql)
                    .setParameter("idAffiliato", affiliato.getId())
                    .setParameter("inizio", inizio)
                    .setParameter("fine", fine)
                    .setParameterList("causali", listaCausali)
                    .uniqueResult();

            totale = n.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (sx != null && sx.isOpen()) {
                    //sx.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return totale;
    }

    /**
     * Conta quante fatture di acquisto dovranno essere esportate
     * @param affiliato
     * @param inizio
     * @param fine
     * @param listaCausali
     * @param esportaFattureNonComunicate
     * @return
     */
    public static int numeroFattureAcquistoRegistrate(Session sx, MROldAffiliato affiliato, Date inizio, Date fine, List listaCausali, boolean esportaFattureNonComunicate) {
        int totale = 0;

        //Session sx = null;
        try {
            //sx = HibernateBridge.startNewSession();

            String sql =
                    "SELECT COUNT(DISTINCT r.id) "
                            + "FROM RigaPrimanota r "
                            + "LEFT JOIN r.primanota p "
                            + "WHERE 1 = 1 "
                            + "AND (p.dataRegistrazione BETWEEN :inizio AND :fine) "
                            //+ "AND p.totaleDocumento > :zero "
                            + "AND p.affiliato.id = :idAffiliato "
                            + "AND p.causale.codice IN (:causali) "
                            + "AND r.fornitore IS NOT NULL ";

            if(esportaFattureNonComunicate) {
                sql += "AND p.idComunicazioneFattura is null ";
            }

            Number n = (Number) sx.createQuery(sql)
                    .setParameter("idAffiliato", affiliato.getId())
                    .setParameter("inizio", inizio)
                    .setParameter("fine", fine)
                            //.setParameter("zero", new Double(0.0))
                    .setParameterList("causali", listaCausali)
                    .uniqueResult();

            totale = n.intValue();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (sx != null && sx.isOpen()) {
                    //sx.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return totale;
    }


    // INIZIO METODI PER TROVARE I DOCUMENTI DA COMUNICARE //

    /**
     * Estra le fatture di vendita che dovranno essere esportate
     * @param affiliato
     * @param inizio
     * @param fine
     * @param listaCausali
     * @param esportaFattureNonComunicate
     * @return
     */
    public static List getListFattureVendita(Session sx, MROldAffiliato affiliato, Date inizio, Date fine, List listaCausali, boolean esportaFattureNonComunicate) {
        List listaFatture = new ArrayList();

        //Session sx = null;
        try {
            //sx = HibernateBridge.startNewSession();

            String sql =
                    "SELECT d "
                            + "FROM DocumentoFiscale d "
                            + "WHERE 1 = 1 "
                            + "AND (d.class = Fattura) "
                            + "AND (d.data BETWEEN :inizio AND :fine "
                            + "     OR d.primanota.dataRegistrazione BETWEEN :inizio AND :fine) "
                            //+ "AND d.primanota.totaleDocumento > :zero "
                            + "AND d.affiliato.id = :idAffiliato "
                            + "AND d.primanota.causale.codice IN (:causali) ";

            if(esportaFattureNonComunicate) {
                sql += "AND d.idComunicazioneFattura is null ";
            }

            listaFatture = sx.createQuery(sql)
                    .setParameter("idAffiliato", affiliato.getId())
                    .setParameter("inizio", inizio)
                    .setParameter("fine", fine)
                            //.setParameter("zero", new Double(0.0))
                    .setParameterList("causali", listaCausali)
                    .list();

            Set<MROldDocumentoFiscale> mySet = new HashSet<MROldDocumentoFiscale>();
            mySet.addAll(listaFatture);
            listaFatture.clear();
            listaFatture.addAll(mySet);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (sx != null && sx.isOpen()) {
                    //sx.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return listaFatture;
    }

    /**
     * Estra le note di credito sulle fatture di vendita che dovranno essere esportate
     * @param affiliato
     * @param inizio
     * @param fine
     * @param listaCausali
     * @param esportaFattureNonComunicate
     * @return
     */
    public static List getListNoteCreditoVendita(Session sx, MROldAffiliato affiliato, Date inizio, Date fine, List listaCausali, boolean esportaFattureNonComunicate) {
        List listaDocumenti = new ArrayList();

        //Session sx = null;
        try {
            //sx = HibernateBridge.startNewSession();

            String sql =
                    "SELECT d "
                            + "FROM MROldDocumentoFiscale d "
                            + "WHERE 1 = 1 "
                            + "AND d.class = NotaCredito "
                            + "AND (d.data BETWEEN :inizio AND :fine "
                            + "     OR d.primanota.dataRegistrazione BETWEEN :inizio AND :fine) "
                            + "AND d.affiliato.id = :idAffiliato "
                            + "AND d.primanota.causale.codice IN (:causali) ";

            if(esportaFattureNonComunicate) {
                sql += "AND d.idComunicazioneFattura is null ";
            }

            listaDocumenti = sx.createQuery(sql)
                    .setParameter("idAffiliato", affiliato.getId())
                    .setParameter("inizio", inizio)
                    .setParameter("fine", fine)
                    .setParameterList("causali", listaCausali)
                    .list();

            Set<MROldDocumentoFiscale> mySet = new HashSet();
            mySet.addAll(listaDocumenti);
            listaDocumenti.clear();
            listaDocumenti.addAll(mySet);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (sx != null && sx.isOpen()) {
                    //sx.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return listaDocumenti;
    }

    /**
     * Estrae le fatture di acquisto che dovranno essere esportate
     * @param affiliato
     * @param inizio
     * @param fine
     * @param listaCausali
     * @param esportaFattureNonComunicate
     * @return
     */
    public static List getListFattureAcquisto(Session sx, MROldAffiliato affiliato, Date inizio, Date fine, List listaCausali, boolean esportaFattureNonComunicate) {
        List listDocumenti = new ArrayList();

        //Session sx = null;
        try {
            //sx = HibernateBridge.startNewSession();

            String sql =
                    "SELECT r.primanota "
                            + "FROM RigaPrimanota r "
                            + "LEFT JOIN r.primanota p "
                            + "WHERE 1 = 1 "
                            //+ "AND p.totaleDocumento > :zero "
                            + "AND (p.dataRegistrazione BETWEEN :inizio AND :fine) "
                            + "AND p.affiliato.id = :idAffiliato "
                            + "AND p.causale.codice IN (:causali) "
                            + "AND r.fornitore IS NOT NULL ";

            if(esportaFattureNonComunicate) {
                sql += "AND p.idComunicazioneFattura is null ";
            }

            listDocumenti = sx.createQuery(sql)
                    //.setParameter("zero", new Double(0.0))
                    .setParameter("idAffiliato", affiliato.getId())
                    .setParameter("inizio", inizio)
                    .setParameter("fine", fine)
                    .setParameterList("causali", listaCausali)
                    .list();

            Set<RigaPrimanota> mySet = new HashSet();
            mySet.addAll(listDocumenti);
            listDocumenti.clear();
            listDocumenti.addAll(mySet);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (sx != null && sx.isOpen()) {
                    //sx.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return listDocumenti;
    }

    /**
     * Estra le note di credito per le fatture di acquisto dovranno essere esportate
     * @param affiliato
     * @param inizio
     * @param fine
     * @param listaCausali
     * @param esportaFattureNonComunicate
     * @return
     */
    public static List getListNoteCreditoAcquisto(Session sx, MROldAffiliato affiliato, Date inizio, Date fine, List listaCausali, boolean esportaFattureNonComunicate) {
        List listaDocumenti = new ArrayList();

        //Session sx = null;
        try {
            //sx = HibernateBridge.startNewSession();

            String sql =
                    "SELECT p "
                            + "FROM MROldPrimanota p "
                            + "WHERE 1 = 1 "
                            + "AND p.dataRegistrazione BETWEEN :inizio AND :fine "
                            + "AND p.affiliato.id = :idAffiliato "
                            + "AND p.causale.codice IN (:causali) ";

            if(esportaFattureNonComunicate) {
                sql += "AND p.idComunicazioneFattura is null ";
            }

            listaDocumenti = sx.createQuery(sql)
                    .setParameter("idAffiliato", affiliato.getId())
                    .setParameter("inizio", inizio)
                    .setParameter("fine", fine)
                    .setParameterList("causali", listaCausali)
                    .list();

            Set<MROldPrimanota> mySet = new HashSet();
            mySet.addAll(listaDocumenti);
            listaDocumenti.clear();
            listaDocumenti.addAll(mySet);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            try {
                if (sx != null && sx.isOpen()) {
                    //sx.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return listaDocumenti;
    }

    public static String getNazioneISO(Session sx, String nazione) {
        if(nazione != null && nazione.length() > 0) {
            try {
                String codiceNazioneSql
                        = "SELECT naz "
                        + "FROM MROldNazioneISO naz "
                        + "WHERE nome = :nazione ";

                MROldNazioneISO nazioneIso = (MROldNazioneISO) sx.createQuery(codiceNazioneSql)
                        .setParameter("nazione", nazione)
                        .list().get(0);

                return nazioneIso.getCodice();
            } catch(Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        return null;
    }

    public static QueryData prenotazioni(
            Date inizio,
            Date fine,
            Boolean abbinate,
            Boolean confermate,
            Boolean annullate,
            Boolean rifiutate,
            Boolean usate,
            Boolean noShow,
            List sedi,
            List gruppi,
            boolean orderByStazioneUscita) {
        QueryData queryData = new QueryData();
        String queryString;

        if (abbinate != null || abbinate==true) {
            queryString = "select p from MROldPrenotazione p left join p.movimento as m "; //NOI18N
        } else {
            queryString = "select p from MROldPrenotazione p "; //NOI18N
        }

        queryString += "where p.inizio >= :inizio ";  //NOI18N
        if (fine != null) {
            queryString += " and p.inizio < :fine "; //NOI18N
        }
        queryData.addParameter("inizio", inizio); //NOI18N
        if (fine != null) {
            queryData.addParameter("fine", fine); //NOI18N
        }
        if (abbinate != null) {
            queryString += "and m is " + (abbinate.booleanValue() ? " not " : "") + " null "; //NOI18N
        }

        //if (confermate != null) {
        queryString += "and p.confermata <> :true "; //NOI18N
        queryData.addParameter("true", true); //NOI18N
        //}

        if (annullate != null) {
            queryString += "and p.annullata = :annullata "; //NOI18N
            queryData.addParameter("annullata", annullate); //NOI18N
        }

        if (rifiutate != null) {
            queryString += "and p.rifiutata = :rifiutata "; //NOI18N
            queryData.addParameter("rifiutata", rifiutate); //NOI18N
        }

        if (noShow != null) {
            queryString += "and p.noShow = :noShow "; //NOI18N
            queryData.addParameter("noShow", noShow); //NOI18N
        }
        if (usate != null) {
            queryString += "and p.usata = :usata "; //NOI18N
            queryData.addParameter("usata", usate); //NOI18N
        }

        if (sedi != null && sedi.size()>0) {
            queryString += "and p.sedeUscita.id in ( :sedi ) "; //NOI18N
            queryData.addParameter("sedi", sedi); //NOI18N
        }

        if (gruppi != null) {
            queryString += "and p.gruppo.id in ( :gruppi ) "; //NOI18N
            queryData.addParameter("gruppi", gruppi); //NOI18N
        }

        queryString +=
                "order by " + //NOI18N
                        (orderByStazioneUscita ? "p.sedeUscita.codice asc, " : "") + //NOI18N
                        "p.inizio asc"; //NOI18N
        queryData.setQueryString(queryString);
        return queryData;
    }

    /**
     * Legge tutti i gruppi inseriti, ordinati per codice nazionale.
     */
    public static List gruppi(Session mySession) {
        List gruppi = new ArrayList();
        try {
            Query queryGruppi = mySession.createQuery("select g from MROldGruppo g order by g.codiceNazionale"); //NOI18N
            gruppi = queryGruppi.list();
            mySession.close();
        } catch (Exception ex) {
            log.error("RicercheUtils: Exception encountered while querying for gruppi. Message is: " + ex.getMessage()); //NOI18N
            log.debug("RicercheUtils: Exception encountered while querying for gruppi.", ex); //NOI18N
        }
        return gruppi;
    }

    /**
     * Legge le fonti commissionabili che hanno contratti prepagati.
     * @return List contentente il risultato della ricerca. Not null.
     */
    public static List fontiConContrattiPrepagati(Session sx) {
        List fonti = new ArrayList();
        try {
            Query queryFonti = sx.createQuery(
                    "select distinct f from MROldCommissione c " + //NOI18N
                            "left join c.fonteCommissione f " + // NOI18N
                            "left join c.contratto ra " + //NOI18N
                            "where ra is not null " + // NOI18N
                            "and c.prepagato = :true " + //NOI18N
                            "order by f.ragioneSociale"); //NOI18N
            queryFonti.setParameter("true", true); //NOI18N
            fonti = queryFonti.list();
        } catch (Exception ex) {
            if (sx != null) {
                try {
                } catch (Exception sxx) {
                }
            }
            log.error("RicercheUtils: Exception encountered while querying for fonti prepagati. Message is: " + ex.getMessage()); //NOI18N
            log.debug("RicercheUtils: Exception encountered while querying for fonti prepagati.", ex); //NOI18N
        }
        return fonti;
    }

    /**
     * Legge le fonti commissionabili che hanno contratti non prepagati.
     * @return List contentente il risultato della ricerca. Not null.
     */
    public static List fontiConContrattiNonPrepagati(Session sx) {
        List fonti = new ArrayList();
        try {
            Query queryFonti = sx.createQuery(
                    "select distinct f from MROldCommissione c " + //NOI18N
                            "left join c.fonteCommissione f " + // NOI18N
                            "left join c.contratto ra " + //NOI18N
                            "left join ra.movimento m " + //NOI18N
                            "where ra is not null " + // NOI18N
                            "and c.prepagato = :false " + // NOI18N
                            "and m.chiuso = :true " + //NOI18N
                            "order by f.ragioneSociale"); //NOI18N
            queryFonti.setParameter("true", true); //NOI18N
            queryFonti.setParameter("false", false); //NOI18N
            fonti = queryFonti.list();
        } catch (Exception ex) {
            if (sx != null) {
                try {
                } catch (Exception sxx) {
                }
            }
            log.error("RicercheUtils: Exception encountered while querying for fonti prepagati. Message is: " + ex.getMessage()); //NOI18N
            log.debug("RicercheUtils: Exception encountered while querying for fonti prepagati.", ex); //NOI18N
        }
        return fonti;
    }

    /**
     * Legge le fonti commissionabili che hanno contratti non prepagati in verifica
     * @return List contentente il risultato della ricerca. Not null.
     */
    public static List fontiConContrattiPrepagatiConExtras(Session sx) {
        List fonti = new ArrayList();
        try {
            Query queryFonti = sx.createQuery(
                    "select distinct f from MROldCommissione c " + //NOI18N
                            "left join c.fonteCommissione f " +
                            "left join c.contratto ra " + //NOI18N
                            "left join ra.movimento m " + //NOI18N
                            "where ra is not null " + // NOI18N
                            "and c.prepagato = :true " + // NOI18N
                            "and m.chiuso = :true " + // NOI18N
                            "and ra.noleggio > :zero " + //NOI18N
                            "order by f.ragioneSociale"); //NOI18N
            queryFonti.setParameter("true", true); //NOI18N
            queryFonti.setParameter("zero", 0.0); //NOI18N
            fonti = queryFonti.list();
        } catch (Exception ex) {
            if (sx != null) {
                try {
                } catch (Exception sxx) {
                }
            }
            log.error("RicercheUtils: Exception encountered while querying for fonti prepagati. Message is: " + ex.getMessage()); //NOI18N
            log.debug("RicercheUtils: Exception encountered while querying for fonti prepagati.", ex); //NOI18N
        }
        return fonti;
    }

    /**
     *
     * @param data
     * @return
     */
    public static boolean fattureSuccessiveAllaData(Date data, Session sx) {
        Number count = null;
        try {
            count = (Number) sx.createQuery(
                    "select count(d) from MROldDocumentoFiscale d " +
                            "where d.data > :data " +
                            "and d.anno = :anno " +
                            "and d.numerazione = :numerazione").
                    setParameter("data", data).
                    setParameter("anno", FormattedDate.annoCorrente(data)).
                    setParameter("numerazione", NumerazioniUtils.getNumerazione(sx, MROldNumerazione.VENDITE)).
                    uniqueResult();
        } catch (Exception ex) {

        }
        return (count != null && count.intValue() > 0);
    }

    /**
     * Crea un oggetto QueryData per la ricerca dei contratti prepagati
     * da fatturare per l'affiliato passato, creati nel periodo compreso tra <code>inizio</code>
     * e <code>fine</code>
     * Imposta anche </code>countQueryString</code>
     * @param inizio La data di inizio del periodo di ricerca
     * @param fine La data di fine del periodo di ricerca
     * @param fonti Lista delle fonti commissionabili da includere nella ricerca
     * @return Un oggetto QueryData da usare per fare la query.
     */
    public static QueryData contrattiPrepagatiNonFatturati(
            Session sx,
            Date inizio,
            Date fine,
            List fonti,
            MROldAffiliato affiliato
    ) {
        QueryData queryData = new QueryData();
        // Aggiungiamo 23h 59m alla data fine.
        fine = FormattedDate.add(fine, GregorianCalendar.DAY_OF_MONTH, 1);
        fine = FormattedDate.add(fine, GregorianCalendar.MINUTE, -1);

        String commonQueryString = null;

        if (Preferenze.getFatturazioneBrookerPrepagati(sx)) {
            commonQueryString = "left join c.commissione as cm " + //NOI18N
                    "left join c.commissione.fonteCommissione as f " + //NOI18N
                    "where c.chiuso = :true " + //NOI18N
                    "and c.movimento.dataChiusura is not null "
                    + "and c.movimento.dataChiusura between :inizio and :fine "
                    + "and f in (:fonti) " + //NOI18N
                    "and cm.prepagato = :true " + // NOI18N
                    (affiliato==null ?" ": "and c.affiliato = :affiliato ") + //NOI18N
                    "and c not in " + //NOI18N
                    "(" + //NOI18N
                    "select distinct d.contratto from MROldDocumentoFiscale d " + //NOI18N
                    "where d.prepagato = :true" + //NOI18N
                    ") " + //NOI18N
                    "and c not in " + //NOI18N
                    "(" + //NOI18N
                    "select distinct r.contratto from MROldRataContratto r " + // NOI18N
                    "left join r.fattura " + //NOI18N
                    "where r.fattura.prepagato = :true" + //NOI18N
                    ") ";
        } else {
            commonQueryString = "left join c.commissione as cm " + //NOI18N
                    "left join c.commissione.fonteCommissione as f " + //NOI18N
                    "where c.inizio between :inizio and :fine " + //NOI18N
                    "and f in (:fonti) " + //NOI18N
                    "and cm.prepagato = :true " + // NOI18N
                    (affiliato==null? " " : "and c.affiliato = :affiliato ") + //NOI18N
                    "and c not in " + //NOI18N
                    "(" + //NOI18N
                    "select distinct d.contratto from MROldDocumentoFiscale d " + //NOI18N
                    "where d.prepagato = :true" + //NOI18N
                    ") " + //NOI18N
                    "and c not in " + //NOI18N
                    "(" + //NOI18N
                    "select distinct r.contratto from MROldRataContratto r " + // NOI18N
                    "left join r.fattura " + //NOI18N
                    "where r.fattura.prepagato = :true" + //NOI18N
                    ") ";
        }

        String queryString = "select c from MROldContrattoNoleggio c " + commonQueryString + " order by c.inizio "; //NOI18N
        String countQueryString = "select count(c) from MROldContrattoNoleggio c " + commonQueryString;

        queryData.addParameter("inizio", inizio); //NOI18N
        queryData.addParameter("fine", fine); //NOI18N
        queryData.addParameter("fonti", fonti); //NOI18N
        queryData.addParameter("true", true); //NOI18N
        if (affiliato!=null)
            queryData.addParameter("affiliato", affiliato); //NOI18N
        queryData.setQueryString(queryString);
        queryData.setCountQueryString(countQueryString);
        return queryData;
    }

    /**
     * Crea un oggetto QueryData per la ricerca dei contratti prepagati
     * con extras da fatturare, creati nel periodo compreso tra
     * <code>inizio</code> e <code>fine</code>. Per contratti non fatturati
     * si intende contratti che non hanno documenti fiscali di saldo,
     * ma solo eventuali fatture di acconto.
     * Imposta anche </code>countQueryString</code>
     * @param inizio La data di inizio del periodo di ricerca
     * @param fine La data di fine del periodo di ricerca
     * @param fonti Lista delle fonti commissionabili da includere nella ricerca
     * @return Un oggetto QueryData da usare per fare la query.
     */
    public static QueryData contrattiPrepagatiConExtrasNonFatturati(
            Date inizio,
            Date fine,
            List fonti,
            MROldAffiliato affiliato) {
        QueryData queryData = new QueryData();

        String commonQueryString = "left join c.commissione as cm " + //NOI18N
                "left join c.commissione.fonteCommissione as f " + //NOI18N
                "left join c.movimento as m " + //NOI18N
                "where c.inizio between :inizio and :fine " + //NOI18N
                "and f in (:fonti) " + //NOI18N
                "and cm.prepagato = :true " + // NOI18N
                "and m.chiuso = :true " + // NOI18N
                "and c.noleggio > :zero " + // NOI18N
                (affiliato==null? " " : "and c.affiliato = :affiliato ") + // NOI18N
                "and c not in " + //NOI18N
                "(" + //NOI18N
                "select distinct d.contratto from MROldDocumentoFiscale d " + //NOI18N
                "where d.prepagato = :false " + //NOI18N
                "and d.class <> MROldFatturaAcconto " + //NOI18N
                ") ";

        String queryString = "select c from MROldContrattoNoleggio c " + //NOI18N
                commonQueryString +
                "order by c.inizio "; //NOI18N
        String countQueryString = "select count(c) from MROldContrattoNoleggio c " +
                commonQueryString;

        queryData.addParameter("inizio", inizio); //NOI18N
        queryData.addParameter("fine", fine); //NOI18N
        queryData.addParameter("fonti", fonti); //NOI18N
        queryData.addParameter("true", true); //NOI18N
        queryData.addParameter("false", false); // NOI18N
        queryData.addParameter("zero", 0.0); // NOI18N
        if (affiliato!=null)
            queryData.addParameter("affiliato", affiliato); // NOI18N

        queryData.setQueryString(queryString);
        queryData.setCountQueryString(countQueryString);
        return queryData;
    }

    /**
     * Crea un oggetto QueryData per la ricerca dei contratti non prepagati
     * da fatturare, creati nel periodo compreso tra <code>inizio</code>
     * e <code>fine</code>. Per contratti non fatturati si intende contratti
     * che non hanno documenti fiscali di saldo, ma solo di acconto.
     * Imposta anche </code>countQueryString</code>
     * @param inizio La data di inizio del periodo di ricerca
     * @param fine La data di fine del periodo di ricerca
     * @param fonti Lista delle fonti commissionabili da includere nella ricerca
     * @return Un oggetto QueryData da usare per fare la query.
     */
    public static QueryData contrattiNonPrepagatiNonFatturati(
            Date inizio,
            Date fine,
            List fonti,
            MROldAffiliato affiliato
    ) {
        QueryData queryData = new QueryData();

        String commonQueryString = " left join c.commissione as cm " + //NOI18N
                "left join c.commissione.fonteCommissione as f " + //NOI18N
                "left join c.movimento as m " + //NOI18N
                "where c.inizio between :inizio and :fine " + //NOI18N
                "and f in (:fonti) " + //NOI18N
                "and cm.prepagato = :false " + // NOI18N
                (affiliato==null? " " : "and c.affiliato = :affiliato ") + // NOI18N
                "and m.chiuso = :true " + // NOI18N
                "and c not in " + //NOI18N
                "(" + //NOI18N
                "select distinct d.contratto from MROldDocumentoFiscale d " + //NOI18N
                "where d.prepagato = :false " + //NOI18N
                "and d.class <> MROldFatturaAcconto " + //NOI18N
                ") ";

        String queryString = "select c from MROldContrattoNoleggio c " + commonQueryString + " order by c.inizio "; //NOI18N
        String countQueryString = "select count(c) from MROldContrattoNoleggio c " + commonQueryString ;

        queryData.addParameter("inizio", inizio); //NOI18N
        queryData.addParameter("fine", fine); //NOI18N
        queryData.addParameter("fonti", fonti); //NOI18N
        queryData.addParameter("true", true); //NOI18N
        queryData.addParameter("false", false); // NOI18N
        if (affiliato!=null)
            queryData.addParameter("affiliato", affiliato); // NOI18N

        queryData.setQueryString(queryString);
        queryData.setCountQueryString(countQueryString);
        return queryData;
    }

    /**
     * Crea un oggetto QueryData per la ricerca delle fatture dei contratti con giorni
     * prepagati, creati nel periodo compreso tra <code>inizio</code>
     * e <code>fine</code>
     * Imposta anche </code>countQueryString</code>
     * @param inizio La data di inizio del periodo di ricerca
     * @param fine La data di fine del periodo di ricerca
     * @param fonti Lista delle fonti commissionabili da includere nella ricerca
     * @return Un oggetto QueryData da usare per fare la query.
     */
    public static QueryData fattureContratti(
            Date inizio,
            Date fine,
            Date dataFattura,
            List fonti,
            boolean contrattiPrepagati,
            boolean fatturePrepagate,
            MROldAffiliato affiliato) {
        QueryData queryData = new QueryData();

//        String commonQueryString = " (select d from DocumentoFiscale d " + //NOI18N
//                "left join d.contratto c " + //NOI18N
//                "left join d.contratto.commissione as cm " + //NOI18N
//                "left join d.contratto.commissione.fonteCommissione as f " + //NOI18N
//                "left join d.cliente as cl " +
//                "where c.inizio between :inizio and :fine " + //NOI18N
//                "and f in (:fonti) " + //NOI18N
//                "and cl.id = f.cliente.id " +
//                "and (cm.prepagato = :contrattiPrepagati " + //NOI18N
//                "or d.prepagato = :fatturePrepagate) " +
//                "and d.affiliato = :affiliato " +
//                ") " + // NOI18N
//                "or d in " + // NOI18N
//                "(select d from DocumentoFiscale d " + // NOI18N
//                "left join d.rate r " + // NOI18N
//                "left join r.contratto as c " + // NOI18N
//                "left join c.commissione as cm " + // NOI18N
//                "left join cm.fonteCommissione as f " + // NOI18N
//                "left join d.cliente as cl " +
//                "where c.inizio between :inizio and :fine " + // NOI18N
//                "and f in (:fonti) " + // NOI18N
//                "and cm.prepagato = :contrattiPrepagati " + // NOI18N
//                "and cl.id = f.cliente.id " +
//                "and d.prepagato = :fatturePrepagate " +
//                /* filtro per affiliato */
//                "and d.affiliato = :affiliato " +
//                ") "//NOI18N
//                ;

        /*
         * query fatta ad hoc per noleggiare.net da mauro e giacomo il 04/08/2014
         */
        String commonQueryString = " (select d from MROldDocumentoFiscale d " + //NOI18N
                "left join d.contratto c " + //NOI18N
                "left join d.contratto.commissione as cm " + //NOI18N
                "left join d.contratto.commissione.fonteCommissione as f " + //NOI18N
                "left join d.cliente as cl " +
                "where " +
//                "c.inizio between :inizio and :fine and " + //NOI18N
                "d.data = :dataFattura " +
                "and f in (:fonti) " + //NOI18N
                // "and cl.id = f.cliente.id " +
                "and d.prepagato = :fatturePrepagate " +
                "and d.affiliato = :affiliato " +
                ") "  // NOI18N
               /* "or d in " + // NOI18N
                "(select d from DocumentoFiscale d " + // NOI18N
                "left join d.rate r " + // NOI18N
                "left join r.contratto as c " + // NOI18N
                "left join c.commissione as cm " + // NOI18N
                "left join cm.fonteCommissione as f " + // NOI18N
                "left join d.cliente as cl " +
                "where c.inizio between :inizio and :fine " + // NOI18N
                "and f in (:fonti) " + // NOI18N
                "and cm.prepagato = :contrattiPrepagati " + // NOI18N
                "and cl.id = f.cliente.id " +
                "and d.prepagato = :fatturePrepagate " +*/
                /* filtro per affiliato */
                /*"and d.affiliato = :affiliato " +
                ") "//NOI18N*/
                ;

        String queryString = "select d from MROldDocumentoFiscale d where d in " + commonQueryString + " order by d.numero "; //NOI18N;
        String countQueryString = "select count(d) from MROldDocumentoFiscale d where d in " + commonQueryString;

//        queryData.addParameter("inizio", inizio); //NOI18N
//        queryData.addParameter("fine", fine); //NOI18N
        queryData.addParameter("fonti", fonti); //NOI18N
        //queryData.addParameter("contrattiPrepagati", contrattiPrepagati); //NOI18N
        queryData.addParameter("fatturePrepagate", fatturePrepagate); //NOI18N
        queryData.addParameter("dataFattura", dataFattura);

        /* ROB - 05/07/2012: filtro per affiliato per la generazione delle fatture prepagati */
        queryData.addParameter("affiliato", affiliato); //NOI18N
        queryData.setQueryString(queryString); //NOI18N
        queryData.setCountQueryString(countQueryString);

        return queryData;
    }


    public static QueryData raApertiAllaData(
            Date inizio,
            Date fine,
            List sedi,
            List gruppi) {
        QueryData queryData = new QueryData();
        String queryString =
                "select m from MROldMovimentoAuto m " + //NOI18N
                        "where m.contratto is not null and " + //NOI18N
                        "(" + //NOI18N
                        "   (m.inizio >= :inizio and m.inizio < :fine) or " + //NOI18N
                        "   (m.fine >= :inizio and m.fine < :fine) or " + //NOI18N
                        "   (m.inizio < :inizio and m.fine >= :fine) " + //NOI18N
                        ") "; //NOI18N
        queryData.addParameter("inizio", inizio); //NOI18N
        fine = FormattedDate.add(fine, Calendar.DAY_OF_MONTH, 1);
        queryData.addParameter("fine", fine); //NOI18N

        if (inizio.getTime() == FormattedDate.formattedDate().getTime()) {
            queryString +=
                    "and m.chiuso = :chiuso "; //NOI18N
            queryData.addParameter("chiuso", Boolean.FALSE); //NOI18N
        }

        queryString +=
                "and m.annullato = :annullato "; //NOI18N
        queryData.addParameter("annullato", Boolean.FALSE); //NOI18N

        if (sedi != null) {
            queryString +=
                    "and m.sedeRientro.id in (:sedi) "; //NOI18N
            queryData.addParameter("sedi", sedi); //NOI18N
        }

        if (gruppi != null) {
            queryString +=
                    "and m.veicolo.gruppo.id in (:gruppi) "; //NOI18N
            queryData.addParameter("gruppi", gruppi); //NOI18N
        }

        queryString += "order by m.sedeRientro.codice asc, m.fine asc"; //NOI18N
        queryData.setQueryString(queryString);
        return queryData;
    }

    public static QueryData usciteRA(
            Date inizio,
            Date fine,
            List sedi,
            List gruppi) {
        QueryData queryData = new QueryData();
        String queryString =
                "select m from MROldMovimentoAuto m " + //NOI18N
                        "where m.contratto is not null " + //NOI18N
                        "and m.inizio >= :inizio " + //NOI18N
                        "and m.inizio < :fine "; //NOI18N

        queryData.addParameter("inizio", inizio); //NOI18N
        queryData.addParameter("fine", fine); //NOI18N

        queryString +=
                "and m.annullato = :annullato "; //NOI18N
        queryData.addParameter("annullato", Boolean.FALSE); //NOI18N

        if (sedi != null) {
            queryString +=
                    "and m.sedeUscita.id in (:sedi) "; //NOI18N
            queryData.addParameter("sedi", sedi); //NOI18N
        }

        if (gruppi != null) {
            queryString +=
                    "and m.veicolo.gruppo.id in (:gruppi) "; //NOI18N
            queryData.addParameter("gruppi", gruppi); //NOI18N
        }

        queryString += "order by m.sedeUscita.codice asc, m.inizio asc"; //NOI18N
        queryData.setQueryString(queryString);
        return queryData;
    }

    public static QueryData rientriRA(
            Date inizio,
            Date fine,
            List sedi,
            List gruppi) {
        QueryData queryData = new QueryData();
        String queryString =
                "select m from MROldMovimentoAuto m " + //NOI18N
                        "where m.contratto is not null " + //NOI18N
                        "and m.fine >= :inizio " + //NOI18N
                        "and m.fine < :fine "; //NOI18N

        queryData.addParameter("inizio", inizio); //NOI18N
        queryData.addParameter("fine", fine); //NOI18N

//        queryString +=
//                "and m.chiuso = :chiuso "; //NOI18N
//
//        queryData.addParameter("chiuso", Boolean.FALSE); //NOI18N

        queryString +=
                "and m.annullato = :annullato "; //NOI18N
        queryData.addParameter("annullato", Boolean.FALSE); //NOI18N

        if (sedi != null) {
            queryString +=
                    "and m.sedeRientro.id in (:sedi) "; //NOI18N
            queryData.addParameter("sedi", sedi); //NOI18N
        }

        if (gruppi != null) {
            queryString +=
                    "and m.veicolo.gruppo.id in (:gruppi) "; //NOI18N
            queryData.addParameter("gruppi", gruppi); //NOI18N
        }

        queryString += "order by m.sedeRientro.codice asc, m.fine asc"; //NOI18N
        queryData.setQueryString(queryString);
        return queryData;
    }

    public static QueryData movimentiApertiAllaData(
            Date inizio,
            Date fine,
            List sedi,
            List gruppi) {
        QueryData queryData = new QueryData();
        String queryString =
                "select m from MROldMovimentoAuto m " + //NOI18N
                        "where m.contratto is null " + //NOI18N
                        "and m.prenotazione is null " + //NOI18N
                        "and (" + //NOI18N
                        "   (m.inizio >= :inizio and m.inizio < :fine) or " + //NOI18N
                        "   (m.fine >= :inizio and m.fine < :fine) or " + //NOI18N
                        "   (m.inizio < :inizio and m.fine >= :fine) " + //NOI18N
                        ") "; //NOI18N
        queryData.addParameter("inizio", inizio); //NOI18N
        queryData.addParameter("fine", fine); //NOI18N

        if (inizio.getTime() == FormattedDate.formattedDate().getTime()) {
            queryString +=
                    "and m.chiuso = :chiuso "; //NOI18N
            queryData.addParameter("chiuso", Boolean.FALSE); //NOI18N
        }

        queryString +=
                "and m.annullato = :annullato "; //NOI18N
        queryData.addParameter("annullato", Boolean.FALSE); //NOI18N

        if (sedi != null) {
            queryString +=
                    "and  m.sedeRientro.id in (:sedi)"; //NOI18N
            queryData.addParameter("sedi", sedi); //NOI18N
        }

        if (gruppi != null) {
            queryString +=
                    "and m.veicolo.gruppo.id in (:gruppi) "; //NOI18N
            queryData.addParameter("gruppi", gruppi); //NOI18N
        }

        queryString += "order by m.sedeRientro.codice asc, m.fine asc"; //NOI18N
        queryData.setQueryString(queryString);
        return queryData;
    }

    public static QueryData usciteMovimenti(
            Date inizio,
            Date fine,
            List sedi,
            List gruppi) {
        QueryData queryData = new QueryData();
        String queryString =
                "select m from MROldMovimentoAuto m " + //NOI18N
                        "where m.contratto is null " + //NOI18N
                        "and m.prenotazione is null " + //NOI18N
                        "and m.inizio >= :inizio " + //NOI18N
                        "and m.inizio < :fine "; //NOI18N

        queryData.addParameter("inizio", inizio); //NOI18N
        queryData.addParameter("fine", fine); //NOI18N

        queryString +=
                "and m.annullato = :annullato "; //NOI18N
        queryData.addParameter("annullato", Boolean.FALSE); //NOI18N

        if (sedi != null) {
            queryString +=
                    "and m.sedeUscita.id in (:sedi) "; //NOI18N
            queryData.addParameter("sedi", sedi); //NOI18N
        }

        if (gruppi != null) {
            queryString +=
                    "and m.veicolo.gruppo.id in (:gruppi) "; //NOI18N
            queryData.addParameter("gruppi", gruppi); //NOI18N
        }

        queryString += "order by m.sedeUscita.codice asc, m.inizio asc"; //NOI18N
        queryData.setQueryString(queryString);
        return queryData;
    }
    public static QueryData rientriMovimenti(
            Date inizio,
            Date fine,
            List sedi,
            List gruppi) {
        QueryData queryData = new QueryData();
        String queryString =
                "select m from MROldMovimentoAuto m " + //NOI18N
                        "where m.contratto is null " + //NOI18N
                        "and m.prenotazione is null " + //NOI18N
                        "and m.fine >= :inizio " + //NOI18N
                        "and m.fine < :fine "; //NOI18N

        queryData.addParameter("inizio", inizio); //NOI18N
        queryData.addParameter("fine", fine); //NOI18N

        queryString +=
                "and m.annullato = :annullato "; //NOI18N
        queryData.addParameter("annullato", Boolean.FALSE); //NOI18N

        if (sedi != null) {
            queryString +=
                    "and m.sedeRientro.id in (:sedi) "; //NOI18N
            queryData.addParameter("sedi", sedi); //NOI18N
        }

        if (gruppi != null) {
            queryString +=
                    "and m.veicolo.gruppo.id in (:gruppi) "; //NOI18N
            queryData.addParameter("gruppi", gruppi); //NOI18N
        }

        queryString += "order by m.sedeRientro.codice asc, m.fine asc"; //NOI18N
        queryData.setQueryString(queryString);
        return queryData;
    }

    public static QueryData veicoliNelParco(
            Date dataRiferimento,
            List sedi,
            List gruppi) {
        QueryData queryData = new QueryData();
        boolean dataOdierna = false;
        if (dataRiferimento == null) {
            dataRiferimento = FormattedDate.formattedDate();
            dataOdierna = true;
        } else {
            dataOdierna = (dataRiferimento.getTime() == FormattedDate.formattedDate().getTime());
        }

        String queryString =
                "select v from MROldParcoVeicoli v where " + //NOI18N
                        "(v.dataAcquisto is null or v.dataAcquisto <= :dataRiferimento) " + //NOI18N
                        "and (v.dataScadenzaContratto >= :dataRiferimento) "; //NOI18N
        queryData.addParameter("dataRiferimento", dataRiferimento); //NOI18N

        if (dataOdierna) {
            queryString += "and v.abilitato = :true "; //NOI18N
            queryData.addParameter("true", Boolean.TRUE); //NOI18N
        }

        if (sedi != null) {
            queryString +=
                    "and v.sede.id in (:sedi) "; //NOI18N
            queryData.addParameter("sedi", sedi); //NOI18N
        }

        if (gruppi != null) {
            queryString +=
                    "and v.gruppo.id in (:gruppi) "; //NOI18N
            queryData.addParameter("gruppi", gruppi); //NOI18N
        }

        queryString += "order by v.sede.codice asc, v.gruppo asc, v.km asc"; //NOI18N
        queryData.setQueryString(queryString);
        return queryData;
    }

    public static QueryData veicoliDisponibili(
            Date inizio,
            List sedi,
            List gruppi) {
        QueryData queryData = new QueryData();

        Date fine = null;
        if (inizio.getTime() != FormattedDate.formattedDate().getTime()) {
            Calendar cFine = new GregorianCalendar();
            cFine.setTimeInMillis(inizio.getTime());
            cFine.add(Calendar.DAY_OF_MONTH, 1);
            fine = cFine.getTime();
        }

        String queryString =
                "select " + //NOI18N
                        "v, " + //NOI18N
                        "(select min(m.inizio) from MROldMovimentoAuto m where m.veicolo.id = v.id and m.inizio >= :inizio) from MROldParcoVeicoli v " + //NOI18N
                        "where " + //NOI18N
                        (fine == null
                                ? "v.abilitato = :true and v.impegnato = :false " //NOI18N
                                : "v not in " + //NOI18N
                                "(" + //NOI18N
                                "   select m.veicolo from MROldMovimentoAuto as m where " + //NOI18N
                                "   m.annullato = :false and (" + //NOI18N
                                "       (m.inizio >= :inizio and m.inizio < :fine) or " + //NOI18N
                                "       (m.fine > :inizio and m.fine <= :fine) or " + //NOI18N
                                "       (m.inizio < :inizio and m.fine > :fine)" + //NOI18N
                                "   )" + //NOI18N
                                ") ") + //NOI18N
                        "and (v.dataAcquisto is null or v.dataAcquisto <= :inizio) " + //NOI18N
                        "and v.dataScadenzaContratto >= :inizio "; //NOI18N

        if (fine == null) {
            queryData.addParameter("inizio", inizio); //NOI18N
            queryData.addParameter("true", Boolean.TRUE); //NOI18N
            queryData.addParameter("false", Boolean.FALSE); //NOI18N
        } else {
            queryData.addParameter("inizio", inizio); //NOI18N
            queryData.addParameter("fine", fine); //NOI18N
            queryData.addParameter("false", Boolean.FALSE); //NOI18N
        }

        if (sedi != null) {
            queryString +=
                    "and v.sede.id in (:sedi) "; //NOI18N
            queryData.addParameter("sedi", sedi); //NOI18N
        }

        if (gruppi != null) {
            queryString +=
                    "and v.gruppo.id in (:gruppi) "; //NOI18N
            queryData.addParameter("gruppi", gruppi); //NOI18N
        }

        queryString += "order by v.sede.codice asc, v.gruppo asc, v.km asc"; //NOI18N
        queryData.setQueryString(queryString);
        return queryData;
    }

    public static QueryData idMovimentiConMezziNonDisponibili(
            Date inizio,
            Date fine,
            List sedi,
            List gruppi) {
        QueryData queryData = new QueryData();
        String selectString = "select m.id "; //NOI18N
        String selectCount = "select count(m.id) "; //NOI18N
        String fromString = "from MROldMovimentoAuto m "; //NOI18N
        // +
        //"left join m.veicolo v left join v.disponibilita d " + //NOI18N
        //"with (d.fine = m.inizio and d.sede <> m.sedeUscita) "; //NOI18N
        String whereString =
                "where m.causale.id = :causalePrenotazione " + //NOI18N
                        "and m.inizio >= :inizio " + //NOI18N
                        "and m.inizio < :fine " + //NOI18N
                        "and " + //NOI18N
                        "(select count(*) from MROldDisponibilitaVeicolo d where " + //NOI18N
                        "d.veicolo = m.veicolo and d.fine = m.inizio and d.sede <> m.sedeUscita) > 0 "; //NOI18N

        queryData.addParameter("inizio", inizio); //NOI18N
        queryData.addParameter("fine", fine); //NOI18N
        queryData.addParameter("causalePrenotazione", MROldCausaleMovimento.CAUSALE_PRENOTAZIONE); //NOI18N

        if (sedi != null) {
            whereString +=
                    "and m.sedeUscita.id in (:sedi) "; //NOI18N
            queryData.addParameter("sedi", sedi); //NOI18N
        }

        if (gruppi != null) {
            whereString +=
                    "and m.veicolo.gruppo.id in (:gruppi) "; //NOI18N
            queryData.addParameter("gruppi", gruppi); //NOI18N
        }

//String groupString = "group by m.id ";  //NOI18N
// +
//"having count(m) > 0 "; //NOI18N
        String orderString =
                "order by m.sedeUscita.codice, m.inizio"; //NOI18N

        queryData.setQueryString(selectString + fromString + whereString + /*groupString + */ orderString);
        queryData.setCountQueryString(selectCount + fromString + whereString /*+ groupString*/);

        return queryData;
    }


    public static List<MROldMovimentoAuto> movimentiConMezziDaTrasferire(Session sx, QueryData queryDataIdMovimenti) throws HibernateException {
        List<MROldMovimentoAuto> movimenti = new ArrayList();
        Iterator ids = queryDataIdMovimenti.iterate(sx);
        while (ids.hasNext()) {
            MROldMovimentoAuto movimento = (MROldMovimentoAuto) sx.get(MROldMovimentoAuto.class, (Integer) ids.next());
            MROldDisponibilitaVeicolo disponibilita = (MROldDisponibilitaVeicolo) sx.createQuery(
                    "select d from MROldDisponibilitaVeicolo d where d.veicolo = :veicolo and " + //NOI18N
                            "d.fine = :inizio order by d.inizio desc"). //NOI18N
                    setParameter("veicolo", movimento.getVeicolo()). //NOI18N
                    setParameter("inizio", movimento.getInizio()). //NOI18N
                    setMaxResults(1).
                    uniqueResult();
            if (disponibilita != null) {
                movimento.setDisponibilitaSx(disponibilita);
                movimenti.add(movimento);
            }
        }
        return movimenti;
    }

    public static QueryData contrattiPrepagatiNonFatturatiAperti(
            Session sx,
            Date inizio,
            Date fine,
            List fonti,
            MROldAffiliato affiliato
    ) {
        QueryData queryData = new QueryData();
        // Aggiungiamo 23h 59m alla data fine.
        fine = FormattedDate.add(fine, GregorianCalendar.DAY_OF_MONTH, 1);
        fine = FormattedDate.add(fine, GregorianCalendar.MINUTE, -1);

        String commonQueryString = null;

        commonQueryString = "left join c.commissione as cm "
                + //NOI18N
                "left join c.commissione.fonteCommissione as f "
                + //NOI18N
                "where c.inizio between :inizio and :fine "
                + //NOI18N
                "and f in (:fonti) "
                + //NOI18N
                "and cm.prepagato = :true "
                + // NOI18N
                (affiliato == null ? " " : "and c.affiliato = :affiliato ")
                + //NOI18N
                "and c not in "
                + //NOI18N
                "("
                + //NOI18N
                "select distinct d.contratto from MROldDocumentoFiscale d "
                + //NOI18N
                "where d.prepagato = :true"
                + //NOI18N
                ") "
                + //NOI18N
                "and c not in "
                + //NOI18N
                "("
                + //NOI18N
                "select distinct r.contratto from MROldRataContratto r "
                + // NOI18N
                "left join r.fattura "
                + //NOI18N
                "where r.fattura.prepagato = :true"
                + //NOI18N
                ") ";

        String queryString = "select c from MROldContrattoNoleggio c " + commonQueryString + " order by c.inizio "; //NOI18N
        String countQueryString = "select count(c) from MROldContrattoNoleggio c " + commonQueryString;

        queryData.addParameter("inizio", inizio); //NOI18N
        queryData.addParameter("fine", fine); //NOI18N
        queryData.addParameter("fonti", fonti); //NOI18N
        queryData.addParameter("true", true); //NOI18N
        if (affiliato != null) {
            queryData.addParameter("affiliato", affiliato); //NOI18N
        }
        queryData.setQueryString(queryString);
        queryData.setCountQueryString(countQueryString);
        return queryData;
    }

    public static QueryData contrattiPrepagatiNonFatturatiChiusi(
            Session sx,
            Date inizio,
            Date fine,
            List fonti,
            MROldAffiliato affiliato
    ) {
        QueryData queryData = new QueryData();
        // Aggiungiamo 23h 59m alla data fine.
        fine = FormattedDate.add(fine, GregorianCalendar.DAY_OF_MONTH, 1);
        fine = FormattedDate.add(fine, GregorianCalendar.MINUTE, -1);

        String commonQueryString = null;

        commonQueryString = "left join c.commissione as cm "
                + //NOI18N
                "left join c.commissione.fonteCommissione as f "
                + //NOI18N
                "where c.chiuso = :true "
                + //NOI18N
                "and c.movimento.dataChiusura is not null "
                + "and c.movimento.dataChiusura between :inizio and :fine "
                + "and f in (:fonti) "
                + //NOI18N
                "and cm.prepagato = :true "
                + // NOI18N
                (affiliato == null ? " " : "and c.affiliato = :affiliato ")
                + //NOI18N
                "and c not in "
                + //NOI18N
                "("
                + //NOI18N
                "select distinct d.contratto from MROldDocumentoFiscale d "
                + //NOI18N
                "where d.prepagato = :true"
                + //NOI18N
                ") "
                + //NOI18N
                "and c not in "
                + //NOI18N
                "("
                + //NOI18N
                "select distinct r.contratto from MROldRataContratto r "
                + // NOI18N
                "left join r.fattura "
                + //NOI18N
                "where r.fattura.prepagato = :true"
                + //NOI18N
                ") ";

        String queryString = "select c from MROldContrattoNoleggio c " + commonQueryString + " order by c.inizio "; //NOI18N
        String countQueryString = "select count(c) from MROldContrattoNoleggio c " + commonQueryString;

        queryData.addParameter("inizio", inizio); //NOI18N
        queryData.addParameter("fine", fine); //NOI18N
        queryData.addParameter("fonti", fonti); //NOI18N
        queryData.addParameter("true", true); //NOI18N
        if (affiliato != null) {
            queryData.addParameter("affiliato", affiliato); //NOI18N
        }
        queryData.setQueryString(queryString);
        queryData.setCountQueryString(countQueryString);
        return queryData;
    }

    public static QueryData contratti(
            Date inizio,
            Date fine,
            boolean dataFattura,
            List sedi,
            List gruppi,
            List fonti) {
        QueryData queryData = new QueryData();
        String select =
                "select c "; //NOI18N

        String selectCount =
                "select count(c) "; //NOI18N

        String from =
                "from MROldContrattoNoleggio c " + //NOI18N
                        "left join c.gruppo as g " + //NOI18N
                        "left join c.sedeUscita as s " +
                        "left join c.commissione.fonteCommissione as f "; //NOI18N
        String where =
                "where " +
                        (dataFattura ? "(c in (select d.contratto from MROldDocumentoFiscale d where d.data between :inizio and :fine) " + //NOI18N
                                "or c in (select r.contratto from MROldRataContratto r where r.fattura.data between :inizio and :fine))" : //NOI18N
                                "c.inizio between :inizio and :fine ") + //NOI18N
                        "and s.id in (:sedi) " + //NOI18N
                        "and g.id in (:gruppi) " + // NOI18N
                        "and f.id in (:fonti) "; //NOI18N

        String orderBy =
                "order by s.codice, g.codiceNazionale"; //NOI18N

        queryData.addParameter("inizio", inizio); //NOI18N
        queryData.addParameter("fine", fine); //NOI18N
        queryData.addParameter("sedi", sedi); //NOI18N
        queryData.addParameter("gruppi", gruppi); //NOI18N
        queryData.addParameter("fonti", fonti); //NOI18N
        queryData.setQueryString(select + from + where + orderBy);
        queryData.setCountQueryString(selectCount + from + where);
        return queryData;
    }

}
