/*
 * ContrattoUtils.java
 *
 * Created on 11 octombrie 2004, 12:53
 */
package it.aessepi.myrentcs.utils;

//import it.aessepi.myrentcs.beans.MarkDamage;
//import it.aessepi.myrentcs.gui.administrator.JDialogAnagraficaAzienda;
import it.aessepi.utils.MathUtils;
import it.aessepi.utils.Parameters;
import it.myrent.ee.api.preferences.Preferenze;
//import it.aessepi.myrentcs.gui.strumenti.JDialogEmail;
import it.aessepi.utils.DESEncrypter;
import it.myrent.ee.api.utils.AnagraficaUtils;
import it.myrent.ee.api.utils.FatturaUtils;
import it.aessepi.utils.FileUtils;
import it.aessepi.utils.beans.FormattedDate;
import it.aessepi.utils.beans.JasperMyTools;
import it.aessepi.utils.beans.UnmappedDamageImage;
import it.aessepi.utils.beans.UnmappedDamageOverPicture;
import it.aessepi.utils.db.User;
import it.myrent.ee.db.*;
//import it.myrent.ee.HibernateBridge;
import it.myrent.ee.beanutil.DotazioniComparator;
//import it.aessepi.myrentcs.utils.FatturazioneFactory;
//import it.myrent.ee.factory.PartitaFactory;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.xml.bind.DatatypeConverter;
//import net.sf.jasperreports.engine.JRException;
//import net.sf.jasperreports.engine.JasperExportManager;
//import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import java.io.ByteArrayInputStream;
import sun.misc.BASE64Decoder;
import it.myrent.ee.api.utils.*;

/**
 *
 * @author  jamess
 */
public class ContrattoUtils {

    private static Log log = LogFactory.getLog(ContrattoUtils.class);
    /** Creates a new instance of ContrattoUtils */
    public ContrattoUtils() {
    }

    public static List<MROldSede> sediContrattoByUser(Session sx, MROldAffiliato affiliato) {
        List<MROldSede> lista = new ArrayList<MROldSede>();

        String sql = "SELECT DISTINCT s from MROldSede as s "
                   + "left join MROldContrattoNoleggio as c WHERE "
                   + "c.sedeRientroPrevisto.id "
                   + "and (c.chiuso = :false or c.chiuso is null) "
                   + "and c.sedeRientroPrevisto.affiliato.id = :idAffiliato";
        lista = sx.createQuery(sql)
                .setParameter("false", Boolean.FALSE)
                .setParameter("idAffiliato", affiliato.getId())
                .list();

        return lista;
    }
      /////////Added by Saurabh/////////
//    public static void emailVoucher(Component parent, Session sx, MROldContrattoNoleggio contrattoNoleggio) throws JRException, MalformedURLException {
//        HashMap parameters = new HashMap();
//        JDialogAnagraficaAzienda.putParameters(parameters);
//        parameters.put("SESSION", sx);
//        String nazione = contrattoNoleggio.getCliente().getNazione();
//        if (nazione == null || !nazione.startsWith("ITA")) {
//            parameters.put("REPORT_LOCALE", Locale.ENGLISH);
//        }
//        String fileName = null, email = null;
//        JasperPrint voucher = null;
//
//        String confirmationNumber = null;
//        if (contrattoNoleggio.getPrefisso() != null) {
//            confirmationNumber = new StringBuffer().append(contrattoNoleggio.getPrefisso()).append("-").
//                    append(contrattoNoleggio.getNumero()).append("-").
//                    append(contrattoNoleggio.getAnno()).
//                    toString();
//        } else {
//            confirmationNumber = new StringBuffer().append(contrattoNoleggio.getNumero()).append("-").
//                    append(contrattoNoleggio.getAnno()).
//                    toString();
//        }
//
//        /* reading lazy beans */
//        if (contrattoNoleggio != null) {
//            contrattoNoleggio.getGaranzia1();
//            contrattoNoleggio.getGaranzia2();
//        }
//
//
//
//        voucher = JasperMyTools.creaPagineStampa("contratto_email", null, parameters, new Object[]{contrattoNoleggio});
//
//        fileName = "ra_" + confirmationNumber + ".pdf";
//        email = contrattoNoleggio.getCliente().getEmail();
//
//        JasperExportManager.exportReportToPdfFile(voucher, System.getProperty("java.io.tmpdir") + File.separator + fileName);
//        JDialogEmail.showEmailDialog(
//                parent,
//                true,
//                sx,
//                email,
//                null,
//                "Your booking with us",
//                "Welcome!\n\n"
//                + "This email is to confirm your booking with us.\n"
//                + "In the attached file you find your rental agreement.\n"
//                + "Thank you for traveling with us!",
//                new String[]{fileName});
//        FileUtils.deleteFile(fileName);
//    }

    public static Integer numeroGiorniEsatti(MROldTariffa tariffa, Date dataInizio, Date oraInizio, Date dataFine, Date oraFine) {
        Integer giorniNoleggio = null;
        if (tariffa != null && Boolean.TRUE.equals(tariffa.getOraRientroAttiva()) && tariffa.getOraRientro() != null) {
            oraFine = tariffa.getOraRientro();
        }

        if (dataInizio != null && oraInizio != null && dataFine != null && oraFine != null) {
            Date inizio = FormattedDate.createTimestamp(dataInizio, oraInizio);

            Date fine = FormattedDate.createTimestamp(dataFine,oraFine);
            giorniNoleggio =new Integer((int) Math.max(1.0,
                    Math.ceil(FormattedDate.numeroGiorni(inizio,
                    fine,
                    true))));
        } else {
            giorniNoleggio = null;
        }

        return giorniNoleggio;
    }

    public static Integer calcolaGiorniNoleggio(Session sx, MROldTariffa t, Date dataInizio, Date dataFine) {
        Integer giorniNoleggio = null;


        Date aDataInizio =  FormattedDate.extractDate(dataInizio);
        Date aOraInizio = FormattedDate.extractTime(dataInizio);
        Date aDataFine = FormattedDate.extractDate(dataFine);
        Date aOraFine = FormattedDate.extractTime(dataFine);

        if (t != null && Boolean.TRUE.equals(t.getOraRientroAttiva()) && t.getOraRientro() != null) {
            aOraFine = t.getOraRientro();
        }

        if (aDataInizio != null && aOraInizio != null && aDataFine != null && aOraFine != null) {
            Date inizio = FormattedDate.createTimestamp(aDataInizio,
                    aOraInizio);

            Integer ritardoMassimoMinuti = null;
            if (sx != null) {
                ritardoMassimoMinuti = Preferenze.getContrattoRitardoMassimoMinuti(sx);
            } else {
                ritardoMassimoMinuti = Preferenze.getContrattoRitardoMassimoMinuti(sx);
            }
            Date fine = FormattedDate.add(FormattedDate.createTimestamp(aDataFine,
                    aOraFine),
                    Calendar.MINUTE,
                    -ritardoMassimoMinuti.intValue());

            giorniNoleggio = new Integer((int) Math.max(1.0, Math.ceil(FormattedDate.numeroGiorni(inizio,fine,true))));
        } else {
            giorniNoleggio = null;
        }

        return giorniNoleggio;
    }

    public static Set divide(Set set, Comparator c, int parts, int partNumber) {
        if (c == null) {
            c = new DotazioniComparator();
        }
        TreeSet newSet = new TreeSet(c);
        if (set != null && set.size() > 0) {
            int start = start(set.size(), parts, partNumber);
            int stop = stop(set.size(), parts, partNumber);
            newSet.addAll(set);
            Iterator it = newSet.iterator();
            int counter = 0;
            while (it.hasNext()) {
                it.next();
                if (counter < start || counter >= stop) {
                    it.remove();
                }
                counter++;
            }
        }
        return newSet;
    }

    private static int stop(int size, int parts, int partNumber) {
        int partCount = size / parts;
        if (size % parts != 0) {
            partCount = partCount + 1;
        }
        return partCount * (partNumber);
    }

    private static int start(int size, int parts, int partNumber) {
        int partCount = size / parts;
        if (size % parts != 0) {
            partCount = partCount + 1;
        }
        return partCount * (partNumber - 1);
    }

    public static List recuperaClienteDaCodideBarre(Session mySession,String codiceBarre) {
        //Session mySession = null;
        List retValue = null;
        if (codiceBarre != null && !codiceBarre.equals(new String(""))) { //NOI18N
            try {
                //mySession = HibernateBridge.startNewSession();
                Query queryClienti = mySession.createQuery("select c from MROldClienti c where c.barcode = :barcode"); //NOI18N
                queryClienti.setParameter("barcode", codiceBarre); //NOI18N
                retValue = queryClienti.list();
                //mySession.close();
            } catch (HibernateException hex) {
                if (log.isDebugEnabled()) {
                    log.debug("ContrattoUtils : Exception encountered while querying for clienti.", hex); //NOI18N
                } else {
                    log.warn("ContrattoUtils : Exception encountered while querying for clienti. Message is : " + hex.getMessage()); //NOI18N
                }
                if (mySession != null) {
                    //HibernateBridge.closeSession();
                }
            }
        }
        return retValue;
    }

    public static List getKm(MROldTariffa tariffa) {
        List km = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getKm().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        km.add(optionalTariffa);
                    }
                }
            }
        }
        return km;
    }

    public static MROldOptionalTariffa getOptionalKm(MROldTariffa tariffa) {
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getKm().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        return optionalTariffa;
                    }
                }
            }
        }
        return null;
    }

    public static List getKmInclusi(Session sx ,MROldTariffa tariffa) {
        List lista = new ArrayList();

        MROldTariffa tmpTariffa = (MROldTariffa) sx.get(MROldTariffa.class, tariffa.getId());
        if (tmpTariffa != null && tmpTariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tmpTariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getKm().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tmpTariffa.getOptionalsTariffa().get(optional);

                    if (Boolean.TRUE.equals(optionalTariffa.getIncluso())) {
                        lista.add(optionalTariffa);
                    }
                }
            }
        }

        return lista;
    }

    public static List getKmInclusi(MROldTariffa tariffa) {
        List lista = new ArrayList();

        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getKm().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);

                    if (Boolean.TRUE.equals(optionalTariffa.getIncluso())) {
                        lista.add(optionalTariffa);
                    }
                }
            }
        }

        return lista;
    }


    public static List getOneWay(MROldTariffa tariffa) {
        List lista = new ArrayList();

        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getOneWay().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);

                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE) || Boolean.TRUE.equals(optionalTariffa.getIncluso())) {
                        lista.add(optionalTariffa);
                    }
                }
            }
        }

        return lista;
    }


    public static List getOneWay(Session sx, MROldTariffa tariffa) {
        if (sx == null) {
            //sx = HibernateBridge.startNewSession();
        } else {
            if (!sx.isOpen()) {
              //  sx = HibernateBridge.openNewSession();
            }
        }

        List lista = new ArrayList();

        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            MROldTariffa tmpTariffa = (MROldTariffa) sx.get(MROldTariffa.class, tariffa.getId());
            
            Iterator optionals = tmpTariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getOneWay().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tmpTariffa.getOptionalsTariffa().get(optional);

                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE) || Boolean.TRUE.equals(optionalTariffa.getIncluso())) {
                        lista.add(optionalTariffa);
                    }
                }
            }
        }

        return lista;
    }

    public static List getAirport(MROldTariffa tariffa) {
        List lista = new ArrayList();

        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getOneriAeroportuali().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);

                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE) || Boolean.TRUE.equals(optionalTariffa.getIncluso())) {
                        lista.add(optionalTariffa);
                    }
                }
            }
        }

        return lista;
    }

    public static Double getAdditionalDrivers(MROldTariffa tariffa) {
        Double totale = 0.0;

        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getGuidatoreAggiuntivo().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);

                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        totale += optionalTariffa.getImporto();
                    }
                }
            }
        }

        return totale;
    }

    public static Integer getNumberAdditionalDrivers(MROldTariffa tariffa) {
        Integer totale = 0;

        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getGuidatoreAggiuntivo().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);

                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        totale ++;
                    }
                }
            }
        }

        return totale;
    }

    public static List getAccessori(MROldTariffa tariffa) {
        List accessori = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAccessorio().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        accessori.add(optionalTariffa);
                    }
                }
            }
        }
        return accessori;
    }

    public static List getExtras(MROldTariffa tariffa) {
        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAssicurazione().equals(Boolean.FALSE) && optional.getImporto() > 0.0 && !optional.getCodice().toUpperCase().equals("BASE CHARGE")) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        extras.add(optionalTariffa);
                    }
                }
            }
        }
        return extras;
    }

    public static List getAssicurazioneSelezionata(MROldTariffa tariffa) {
        List lista = new ArrayList();

        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAssicurazione().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        lista.add(optional);
                    }
                }
            }
        }


        return lista;
    }


    public static List getAssicurazioniSelezionate(MROldTariffa tariffa) {
        List lista = new ArrayList();

        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAssicurazione().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getIncluso() || optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        lista.add(optionalTariffa);
                    }
                }
            }
        }


        return lista;
    }

    public static List getOptionalsRientro(MROldTariffa tariffa) {
        List lista = new ArrayList();

        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                if (optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                    lista.add(optionalTariffa);
                }
            }
        }

        return lista;
    }

    public static List<MROldDanno> getDanniRilevati(Session sx,MROldMovimentoAuto movimento,boolean uscita) {
        List<MROldDanno> lista = new ArrayList<MROldDanno>();
        //Session sx = null;
        try {
            //sx = HibernateBridge.startNewSession();
            MROldMovimentoAuto m = (MROldMovimentoAuto) sx.get(MROldMovimentoAuto.class, movimento.getId());

            Set danni = new HashSet();
            if (uscita) {
                danni.addAll(m.getDanniPresenti());
            } else {
                danni.addAll(m.getDanniRilevati());
            }

            if (danni != null && !danni.isEmpty()) {
                Iterator it = danni.iterator();
                while (it.hasNext()) {
                    lista.add((MROldDanno) it.next());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        
        return lista;
    }

    public static List getCDW(MROldTariffa tariffa) {
        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAssicurazione().equals(Boolean.TRUE) && optional.getDanni().equals(Boolean.TRUE) && optional.getFranchigia().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        extras.add(optionalTariffa);
                    }
                }
            }
        }
        return extras;
    }

     public static List getCDW(Session sx, MROldTariffa tariffa) {
        if (sx == null) {
            //sx = HibernateBridge.startNewSession();
        } else {
            if (!sx.isOpen()) {
              //  sx = HibernateBridge.openNewSession();
            }
        }
        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            MROldTariffa tmpTariffa = (MROldTariffa) sx.get(MROldTariffa.class, tariffa.getId());
            Iterator optionals = tmpTariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAssicurazione().equals(Boolean.TRUE) && optional.getDanni().equals(Boolean.TRUE) && optional.getFranchigia().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tmpTariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        extras.add(optionalTariffa);
                    }
                }
            }
        }
        return extras;
    }

    public static List getTP(MROldTariffa tariffa) {
        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAssicurazione().equals(Boolean.TRUE) && optional.getFurto().equals(Boolean.TRUE) && optional.getFranchigia().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        extras.add(optionalTariffa);
                    }
                }
            }
        }
        return extras;
    }

    public static List getPAI(MROldTariffa tariffa) {
        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAssicurazione().equals(Boolean.TRUE) && optional.getPai().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        extras.add(optionalTariffa);
                    }
                }
            }
        }
        return extras;
    }

    public static List getSuper(MROldTariffa tariffa) {
        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAssicurazione().equals(Boolean.TRUE) &&
                        (optional.getDanni().equals(Boolean.TRUE) || optional.getFurto().equals(Boolean.TRUE)) &&
                        optional.getRiduzione().equals(Boolean.TRUE) &&
                        optional.getPercentuale().doubleValue() < 100.0) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        extras.add(optionalTariffa);
                    }
                }
            }
        }
        return extras;
    }

    public static List getSuperPlus(MROldTariffa tariffa) {
        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAssicurazione().equals(Boolean.TRUE) &&
                        (optional.getDanni().equals(Boolean.TRUE) || optional.getFurto().equals(Boolean.TRUE)) &&
                        optional.getRiduzione().equals(Boolean.TRUE) &&
                        optional.getPercentuale().doubleValue() >= 100.0) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        extras.add(optionalTariffa);
                    }
                }
            }
        }
        return extras;
    }

    public static List getExtrasByCode(MROldTariffa tariffa, String codice) {
        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator<MROldOptional> optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = optionals.next();
                if (optional.getCodice().equals(codice)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        extras.add(optionalTariffa);
                    }
                }
            }
        }
        return extras;
    }

     public static List getExtrasByStartWithCode(MROldTariffa tariffa, String parteCodice) {
        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator<MROldOptional> optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = optionals.next();
                if (optional.getCodice().toLowerCase().startsWith(parteCodice.toLowerCase())) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        extras.add(optionalTariffa);
                    }
                }
            }
        }
        return extras;
    }

    public static List getAllExtrasByStartWithCode(MROldTariffa tariffa, String parteCodice) {
        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator<MROldOptional> optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = optionals.next();
                if (optional.getCodice().toLowerCase().startsWith(parteCodice.toLowerCase())) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    extras.add(optionalTariffa);
                }
            }
        }
        return extras;
    }

    public static MROldOptionalTariffa getExtraByCode(MROldTariffa tariffa, String codice) {
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator<MROldOptional> optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = optionals.next();
                if (optional.getCodice().equals(codice)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) {
                        return optionalTariffa;
                    }
                }
            }
        }
        return null;
    }

    public static MROldOptionalTariffa getExtraByCodes(MROldTariffa tariffa,String start,String end) {
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator<MROldOptional> optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = optionals.next();
                if (optional.getCodice().toLowerCase().startsWith(start.toLowerCase()) && optional.getCodice().toLowerCase().endsWith(end.toLowerCase())) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if (optionalTariffa.getSelezionato() || optionalTariffa.getIncluso() || optionalTariffa.getSelezionatoRientro()) {
                        return optionalTariffa;
                    }
                }
            }
        }
        return null;
    }
    
    public static MROldOptionalTariffa getRefueling(MROldTariffa tariffa) {
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator<MROldOptional> optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = optionals.next();
                if (optional.getCarburante()) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    
                    return optionalTariffa;
                    
                }
            }
        }
        return null;
    }
    

    public static List getOptionalsFiltratiParticolari(MROldTariffa tariffa) {
        List lista = new ArrayList();

        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();

                if (Boolean.FALSE.equals(optional.getOneriAeroportuali()) && Boolean.FALSE.equals(optional.getGuidatoreAggiuntivo()) &&
                    Boolean.FALSE.equals(optional.getOneWay()) && Boolean.FALSE.equals(optional.getKm()) && Boolean.FALSE.equals(optional.getDanni())) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);

                    if ( (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) && optionalTariffa.getImporto() > 0.0 &&
                          !optionalTariffa.getOptional().getCodice().equals("GREEN CARD") && !optionalTariffa.getOptional().getCodice().equals("BASE CHARGE")) {
                        System.out.println(optional.toString());
                        lista.add(optionalTariffa);
                    }
                }
            }
        }

        return lista;
    }

    public static List getOptionalsFiltratiParticolari(MROldTariffa tariffa, Boolean prepagato) {
        List lista = new ArrayList();

        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();

                if (Boolean.FALSE.equals(optional.getOneriAeroportuali()) && Boolean.FALSE.equals(optional.getGuidatoreAggiuntivo()) &&
                    Boolean.FALSE.equals(optional.getOneWay()) && Boolean.FALSE.equals(optional.getKm()) && Boolean.FALSE.equals(optional.getDanni())) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);

                    if ( (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) && optionalTariffa.getImporto() > 0.0 &&
                          !optionalTariffa.getOptional().getCodice().equals("GREEN CARD") && !optionalTariffa.getOptional().getCodice().equals("BASE CHARGE") && prepagato.equals(optionalTariffa.getPrepagato())) {
                        System.out.println(optional.toString());
                        lista.add(optionalTariffa);
                    }
                }
            }
        }

        return lista;
    }

    public static Double calcolaFranchigiaContratto(MROldTariffa tariffa,List optionals) {
        Double franchigia = 0.0;

        if (tariffa != null && tariffa.getDepositoSenzaAss() != null) {
            franchigia += tariffa.getDepositoSenzaAss();
        }

        if (optionals != null && optionals.size() > 0) {
            Iterator it = optionals.iterator();
            while (it.hasNext()) {
                MROldOptionalTariffa ot = (MROldOptionalTariffa) it.next();
                MROldOptional o = ot.getOptional();
                if (o.getImportoDanno() != null) {
                    franchigia += o.getImportoDanno();
                }
            }
        }

        return franchigia;
    }

    public static Double getSoloFranchigiaContratto(MROldTariffa tariffa, Double franchigiaTotale) {
        Double franchigia = 0.0;
        if (tariffa != null && tariffa.getOptionalsTariffa() != null && franchigiaTotale > 0) {
            Iterator<MROldOptional> optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = optionals.next();

                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                if (!optional.getAssicurazione() && (optional.getAccessorio() || optional.getAltro()) && optionalTariffa.getSelezionato().equals(Boolean.TRUE) && optionalTariffa.getFranchigia() != null) {
                    franchigia += optionalTariffa.getFranchigia();
                }

            }

            franchigia = franchigiaTotale - franchigia;
        }
        return franchigia;
    }

    public static boolean getAbbattimentoFranchigia(MROldTariffa tariffa) {
        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAssicurazione().equals(Boolean.TRUE) && (optional.getDanni().equals(Boolean.TRUE) || optional.getFurto().equals(Boolean.TRUE))) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if ( (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) && !optionalTariffa.getIncluso() ) {
                        extras.add(optionalTariffa);
                    }
                }
            }
        }

        if (extras.size() > 0) {
            return true;
        }

        return false;
    }

    public static MROldOptionalTariffa getAbbattimentoFranchigiaDanni(MROldTariffa tariffa) {
        List<MROldOptionalTariffa> extras = new ArrayList<MROldOptionalTariffa>();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAssicurazione().equals(Boolean.TRUE) && optional.getDanni().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if ( (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) && !optionalTariffa.getIncluso() ) {
                        extras.add(optionalTariffa);
                    }
                }
            }
        }

        if (!extras.isEmpty()) {
            return extras.get(0);
        }
        return null;
    }

    public static MROldOptionalTariffa getAbbattimentoFranchigiaFurto(MROldTariffa tariffa) {
        List<MROldOptionalTariffa> extras = new ArrayList<MROldOptionalTariffa>();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getAssicurazione().equals(Boolean.TRUE) && optional.getFurto().equals(Boolean.TRUE)) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    if ( (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) && !optionalTariffa.getIncluso() ) {
                        extras.add(optionalTariffa);
                    }
                }
            }
        }

        if (!extras.isEmpty()) {
            return extras.get(0);
        }
        return null;
    }


    public static String getUser(Session sx, MROldMovimentoAuto ma) {
        String user = null;

        if (sx == null || !sx.isOpen()) {
            //sx = HibernateBridge.startNewSession();
        }

        if (ma != null && ma.getId() != null) {
            MROldMovimentoAuto m = (MROldMovimentoAuto) sx.get(MROldMovimentoAuto.class, ma.getId());
            User u = m.getUserApertura();
            if (u != null) {
                user = u.getNomeCognome();
            }
        }

        return user;
    }


    public static List listaRighePreventivo(Session sx,MROldTariffa tariffa, Date inizio, Date fine, Integer kmTotali, Double scontoPercentuale) {
        List righeFattura = null;
        List righePreventivo = null;
        MROldPreventivo aPreventivo = new MROldPreventivo();
        try {

            try {
                righeFattura = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                        sx,
                        aPreventivo,
                        tariffa,
                        inizio,
                        fine,
                        kmTotali,
                        null,
                        null,
                        scontoPercentuale,
                        null).calcolaRigheProssimaFattura(sx);
            } catch (Exception ex) {
                righeFattura = null;
            }

            Double[] totali = FatturaUtils.calcolaImportiRigheFattura(righeFattura);
            aPreventivo.setTotaleImponibile(totali[0]);
            aPreventivo.setTotaleIva(totali[1]);
            aPreventivo.setTotaleDocumento(totali[2]);
            aPreventivo.setInizio(inizio);
            aPreventivo.setFine(fine);

            int righe = righeFattura.size();

            if (righe > 0) {
                righePreventivo = new ArrayList();
                for (int i = 0; i < righe; i++) {
                    MROldRigaDocumentoFiscale tmpRigaFattura = (MROldRigaDocumentoFiscale) righeFattura.get(i);
                    MROldRigaPreventivo tmpRigaPreventivo = new MROldRigaPreventivo(tmpRigaFattura, aPreventivo);
                    tmpRigaPreventivo.setNumeroRigaPreventivo(new Integer(i));
                    righePreventivo.add(tmpRigaPreventivo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return righePreventivo;
    }

    public static List listaRighePreventivo(Session sx,MROldSede locationPickUp, MROldTariffa tariffa, Date inizio, Date fine, Integer kmTotali, Double scontoPercentuale, Boolean prepagato, int ggVoucher) {
        List righeFattura = null;
        List righePreventivo = null;
        MROldPreventivo aPreventivo = new MROldPreventivo();
        if (locationPickUp!=null)
            aPreventivo.setSedeUscita(locationPickUp);

        Integer numeroGiorni = null;

        if (prepagato) {
            if (ggVoucher!=-1)
                numeroGiorni = ggVoucher;
            else
                numeroGiorni = numeroGiorniEsatti(tariffa, FormattedDate.extractDate(inizio), FormattedDate.extractTime(inizio), FormattedDate.extractDate(fine), FormattedDate.extractTime(fine));

        }
        try {

//            if (sx == null || (sx != null && !sx.isOpen())) {
//                sx = HibernateBridge.startNewSession();
//            }
            try {
                /*
                 * Andrea
                 */
                MROldSede sedePickUp = locationPickUp;
                righeFattura = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                        sx,
                        aPreventivo,
                        tariffa,
                        inizio,
                        fine,
                        kmTotali,
                        null,
                        null,
                        scontoPercentuale,
                        numeroGiorni).setLocationPickUpAndReturnFatturazione(sedePickUp).calcolaRigheProssimaFattura(sx);
            } catch (Exception ex) {
                righeFattura = null;
            }



//            if (numeroGiorni != null) {
//                try {
//                    List listaPrepagato = FatturazioneFactory.PREPAGATO_FACTORY.getFatturazione(sx,tariffa, inizio, fine, numeroGiorni).calcolaRigheProssimaFattura();
//                } catch (Exception tex) {
//                    tex.printStackTrace();
//                }
//            }

            Double[] totali = FatturaUtils.calcolaImportiRigheFattura(righeFattura);
            aPreventivo.setTotaleImponibile(totali[0]);
            aPreventivo.setTotaleIva(totali[1]);
            aPreventivo.setTotaleDocumento(totali[2]);
            aPreventivo.setInizio(inizio);
            aPreventivo.setFine(fine);


            int righe =0;
            if (righeFattura!=null)
                righe = righeFattura.size();

            if (righe > 0) {
                righePreventivo = new ArrayList();
                for (int i = 0; i < righe; i++) {
                    MROldRigaDocumentoFiscale tmpRigaFattura = (MROldRigaDocumentoFiscale) righeFattura.get(i);
                    MROldRigaPreventivo tmpRigaPreventivo = new MROldRigaPreventivo(tmpRigaFattura, aPreventivo);
                    tmpRigaPreventivo.setNumeroRigaPreventivo(new Integer(i));
                    righePreventivo.add(tmpRigaPreventivo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return righePreventivo;
    }


    public static List listaRighePreventivo(Session sx, MROldContrattoNoleggio contratto, MROldTariffa tariffa, Date inizio, Date fine, Integer kmTotali, Double scontoPercentuale, Boolean prepagato, Boolean report) {
        MROldSede locationPickUp = contratto.getSedeUscita();

        List righeFattura = null;
        List righePreventivo = null;
        MROldPreventivo aPreventivo = new MROldPreventivo();
        if (locationPickUp != null) {
            aPreventivo.setSedeUscita(locationPickUp);
        }

        Integer numeroGiorni = null;

        try {
            /*
            * Andrea
            */
            if (prepagato) {
                numeroGiorni = numeroGiorniEsatti(tariffa, FormattedDate.extractDate(inizio), FormattedDate.extractTime(inizio), FormattedDate.extractDate(fine), FormattedDate.extractTime(fine));
            }
            MROldSede sedePickUp = locationPickUp;
            righeFattura = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                        sx,
                        contratto,
                        tariffa,
                        inizio,
                        fine,
                        kmTotali,
                        null,
                        null,
                        scontoPercentuale,
                        numeroGiorni).setLocationPickUpAndReturnFatturazione(sedePickUp).calcolaRigheProssimaFattura(sx);
            }catch (Exception ex) {
                righeFattura = null;
            }
            Double[] totali = FatturaUtils.calcolaImportiRigheFattura(righeFattura);
            aPreventivo.setTotaleImponibile(totali[0]);
            aPreventivo.setTotaleIva(totali[1]);
            aPreventivo.setTotaleDocumento(totali[2]);
            aPreventivo.setInizio(inizio);
            aPreventivo.setFine(fine);

            int righe = 0;
            if (righeFattura != null) {
                righe = righeFattura.size();
            }

            if (righe > 0) {
                righePreventivo = new ArrayList();
                for (int i = 0; i < righe; i++) {
                    MROldRigaDocumentoFiscale tmpRigaFattura = (MROldRigaDocumentoFiscale) righeFattura.get(i);
                    MROldRigaPreventivo tmpRigaPreventivo = new MROldRigaPreventivo(tmpRigaFattura, aPreventivo);
                    tmpRigaPreventivo.setNumeroRigaPreventivo(new Integer(i));
                    righePreventivo.add(tmpRigaPreventivo);
                }
            }

        return righePreventivo;
    }



    public static List listaRighePreventivo(Session sx,MROldTariffa tariffa, Date inizio, Date fine, Integer kmTotali, Double scontoPercentuale, Boolean prepagato) {
        return listaRighePreventivo(sx, null, tariffa, inizio, fine, kmTotali, scontoPercentuale, prepagato);
    }

    //////////*Added by Saurabh *////////////////////////////////////
    public static List listaRighePreventivo(Session sx,MROldSede locationPickUp, MROldTariffa tariffa, Date inizio, Date fine, Integer kmTotali, Double scontoPercentuale, Boolean prepagato) {
        List righeFattura = null;
        List righePreventivo = null;
        MROldPreventivo aPreventivo = new MROldPreventivo();
        if (locationPickUp!=null)
            aPreventivo.setSedeUscita(locationPickUp);

        Integer numeroGiorni = null;

        if (prepagato) {
            numeroGiorni = numeroGiorniEsatti(tariffa, FormattedDate.extractDate(inizio), FormattedDate.extractTime(inizio), FormattedDate.extractDate(fine), FormattedDate.extractTime(fine));
        }
        try {

            if (sx == null || (sx != null && !sx.isOpen())) {
                //  sx = HibernateBridge.startNewSession();
            }
            try {/*
                 * Andrea
                 */
                MROldSede sedePickUp = locationPickUp;
                righeFattura = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                        sx,
                        aPreventivo,
                        tariffa,
                        inizio,
                        fine,
                        kmTotali,
                        null,
                        null,
                        scontoPercentuale,
                        numeroGiorni).setLocationPickUpAndReturnFatturazione(sedePickUp).calcolaRigheProssimaFattura(sx);
            } catch (Exception ex) {
                righeFattura = null;
            }

            if (numeroGiorni != null) {
                try {
                    List listaPrepagato = FatturazioneFactory.PREPAGATO_FACTORY.getFatturazione(sx,tariffa, inizio, fine, numeroGiorni).calcolaRigheProssimaFattura();
                } catch (Exception tex) {
                    tex.printStackTrace();
                }
            }

            Double[] totali = FatturaUtils.calcolaImportiRigheFattura(righeFattura);
            aPreventivo.setTotaleImponibile(totali[0]);
            aPreventivo.setTotaleIva(totali[1]);
            aPreventivo.setTotaleDocumento(totali[2]);
            aPreventivo.setInizio(inizio);
            aPreventivo.setFine(fine);


            int righe =0;
            if (righeFattura!=null)
                righe = righeFattura.size();

            if (righe > 0) {
                righePreventivo = new ArrayList();
                for (int i = 0; i < righe; i++) {
                    MROldRigaDocumentoFiscale tmpRigaFattura = (MROldRigaDocumentoFiscale) righeFattura.get(i);
                    MROldRigaPreventivo tmpRigaPreventivo = new MROldRigaPreventivo(tmpRigaFattura, aPreventivo);
                    tmpRigaPreventivo.setNumeroRigaPreventivo(new Integer(i));
                    righePreventivo.add(tmpRigaPreventivo);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return righePreventivo;
    }

    public static Double importoGaranzia(Session sx,Garanzia garanzia) {
        Double importo = 0.0;
        if (garanzia != null && garanzia.isGaranziaCarta()) {
            try {
                garanzia = (Garanzia) sx.get(GaranziaImpl.class, garanzia.getId());
                importo = garanzia.getImporto();

            } catch (Exception e) {
            }
        }
        return importo;
    }

    public static String descrizioneGaranzia(Session sx,Garanzia garanzia) {
        String descrizione = "";
        //Session sx = null;
        if (garanzia != null && garanzia.isGaranziaCarta()) {
            try {
                //sx = HibernateBridge.startNewSession();

                garanzia = (Garanzia) sx.get(GaranziaImpl.class, garanzia.getId());
                descrizione = garanzia.getNumero();

            } catch (Exception e) {
            } finally {
                try {
                    if (sx != null) {
                        //sx.close();
                    }
                } catch (Exception ex) {
                }
            }
        }

        return descrizione;
    }



    public static String numeroAutorizzazioneGaranzia(Session sx,Garanzia garanzia) {
        String aut = "Aut. n. ";
        //Session sx = null;

        if (garanzia != null && garanzia.isGaranziaCarta()) {
            try {
                GaranziaAssegno gc = (GaranziaAssegno) garanzia;
                aut += gc.getNumeroAutorizzazione();
                //garanzia.getNumeroAutorizzazione()

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (sx != null) {
                      //  sx.close();
                    }
                } catch (Exception ex) {
                }
            }
        }
        return aut;
    }



     public static String expireGaranzia(Session sx,Garanzia garanzia) {
        String data = null;
        //Session sx = null;

        if (garanzia != null && garanzia.isGaranziaCarta()) {
            try {
                //sx = HibernateBridge.startNewSession();

                garanzia = (Garanzia) sx.get(GaranziaImpl.class, garanzia.getId());


                GaranziaCarta gc = (GaranziaCarta) garanzia;

                data = gc.getMeseScadenza().toString() + "/" + gc.getAnnoScadenza().toString();

            } catch (Exception e) {
            } finally {
                try {
                    if (sx != null) {
                        sx.close();
                    }
                } catch (Exception ex) {
                }
            }
        }
        return data;
    }



     public static String cvvGaranzia(Session sx,Garanzia garanzia) {
        String data = null;
        //Session sx = null;

        if (garanzia != null && garanzia.isGaranziaCarta()) {
            try {
                //sx = HibernateBridge.startNewSession();

                garanzia = (Garanzia) sx.get(GaranziaImpl.class, garanzia.getId());


                GaranziaCarta gc = (GaranziaCarta) garanzia;

                data = gc.getCvv();

            } catch (Exception e) {
            } finally {
                try {
                    if (sx != null) {
                        //sx.close();
                    }
                } catch (Exception ex) {
                }
            }
        }
        return data;
    }



     //metodi per la stampa del contratto con MyRent Signature

     public static MROldPartita leggiPartita(Session sx, MROldDocumentoFiscale fattura) {
        MROldPartita p = null;

        try {
             if (sx == null) {
                 //sx = HibernateBridge.startNewSession();
             } else {
                 if (!sx.isOpen()) {
                    // sx = HibernateBridge.openNewSession();
                 }
             }

             p = (MROldPartita) sx.createQuery("select p from MROldPartita p where p.fattura = :fattura").
                setParameter("fattura", fattura).
                setMaxResults(1).
                uniqueResult();
         } catch (Exception e) {
             e.printStackTrace();
         }

        return p;
    }

     public static MROldDocumentoFiscale fatturaReport(Session sx,Integer idContratto) {
        MROldDocumentoFiscale d = null;


         try {
             if (sx == null) {
                 //sx = HibernateBridge.startNewSession();
             } else {
                 if (!sx.isOpen()) {
                     //sx = HibernateBridge.openNewSession();
                 }
             }

             MROldContrattoNoleggio c = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);

             Object df[] = null;
             if (c.getDocumentiFiscali() != null && !c.getDocumentiFiscali().isEmpty()) {
                 df = c.getDocumentiFiscali().toArray();
                 d = (MROldDocumentoFiscale) df[df.length - 1];
                 d = (MROldDocumentoFiscale) sx.get(MROldDocumentoFiscale.class, d.getId());
             }
         } catch (Exception e) {
             e.printStackTrace();
         }

        return d;
    }

     public static MROldTariffa tariffaReport(Session sx,Integer idContratto) {
        MROldTariffa t = null;


        try {
            if (sx == null) {
                //sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                  //  sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto.getTariffa() != null) {
                t = (MROldTariffa) sx.get(MROldTariffa.class, contratto.getTariffa().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

     public static MROldMovimentoAuto movimentoReport(Session sx,Integer idContratto) {
        MROldMovimentoAuto m = null;


        try {
            if (sx == null) {
                //sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                   // sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto.getMovimento() != null) {
                m = (MROldMovimentoAuto) sx.get(MROldMovimentoAuto.class, contratto.getMovimento().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return m;
    }

     public static MROldCommissione commissioneReport(Session sx,Integer idContratto) {
        MROldCommissione c = null;


        try {
            if (sx == null) {
                //sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                  //  sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto.getCommissione() != null) {
                c = (MROldCommissione) sx.get(MROldCommissione.class, contratto.getCommissione().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    public static MROldPagamento pagamentoReport(Session sx,Integer idContratto) {
        MROldPagamento p = null;


        try {
            if (sx == null) {
                //sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                    //sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto.getPagamento() != null) {
                p = (MROldPagamento) sx.get(MROldPagamento.class, contratto.getPagamento().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

     public static MROldConducenti conducenteReport(Session sx,Integer idContratto,int numeroConducente) {
        MROldConducenti c = null;


        try {
            if (sx == null) {
                //sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                    //sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (numeroConducente == 1) {
                if (contratto.getConducente1() != null) {
                    c = (MROldConducenti) sx.get(MROldConducenti.class, contratto.getConducente1().getId());
                }
            } else if (numeroConducente == 2) {
                if (contratto.getConducente2() != null) {
                    c = (MROldConducenti) sx.get(MROldConducenti.class, contratto.getConducente2().getId());
                }
            } else if (numeroConducente == 3) {
                if (contratto.getConducente3() != null) {
                    c = (MROldConducenti) sx.get(MROldConducenti.class, contratto.getConducente3().getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

     public static MROldClienti clienteReport(Session sx,Integer idContratto) {
        MROldClienti c = null;


        try {
            if (sx == null) {
               // sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                 //   sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto.getCliente() != null) {
                c = (MROldClienti) sx.get(MROldClienti.class, contratto.getCliente().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }
     
     
    public static MROldFonteCommissione getReservationSource(Session sx,Integer idContratto) {
        MROldFonteCommissione f = null;


        try {
            if (sx == null) {
               // sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                   // sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto.getCommissione()!= null && contratto.getCommissione().getFonteCommissione() != null) {
                f = (MROldFonteCommissione) sx.get(MROldFonteCommissione.class, contratto.getCommissione().getFonteCommissione().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    } 
    
    public static MROldDepositAndWaiverResSource getDepositAndWaiverResSource(Session sx,Integer idContratto) {
        MROldDepositAndWaiverResSource f = null;


        try {
            if (sx == null) {
                //sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                  //  sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto.getCommissione()!= null && contratto.getCommissione().getFonteCommissione() != null &&
                    contratto.getCommissione().getFonteCommissione().getDepositResSourceId()!=null) {
                f = (MROldDepositAndWaiverResSource) sx.get(MROldDepositAndWaiverResSource.class, contratto.getCommissione().getFonteCommissione().getDepositResSourceId().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return f;
    } 
    
    public static MROldRentalType getRentalType(Session sx,Integer idContratto) {
        MROldRentalType r = null;


        try {
            if (sx == null) {
                //sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                  //  sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto.getRentalType() != null && !contratto.getRentalType().equals("")) {
                r = (MROldRentalType) sx.get(MROldRentalType.class, contratto.getRentalType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }




     public static Garanzia garanzia1Report(Session sx,Integer idContratto) {
        Garanzia g = null;


        try {
            if (sx == null) {
                //sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                  //  sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto.getGaranzia1() != null) {

                System.out.println("||| Contratto Noleggio |||");
                System.out.println("ID: " + contratto.getGaranzia1().getId());

                if (contratto.getGaranzia1() instanceof GaranziaCarta)
                    g = (Garanzia) sx.get(GaranziaCartaImpl.class, contratto.getGaranzia1().getId());
                else
                    g = (Garanzia) sx.get(GaranziaImpl.class, contratto.getGaranzia1().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return g;
    }

     public static Garanzia garanzia2Report(Session sx,Integer idContratto) {
        Garanzia g = null;


        try {
            if (sx == null) {
                //sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                  //  sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto.getGaranzia2() != null) {

                if (contratto.getGaranzia2() instanceof GaranziaCarta)
                    g = (Garanzia) sx.get(GaranziaCartaImpl.class, contratto.getGaranzia2().getId());
                else
                    g = (Garanzia) sx.get(GaranziaImpl.class, contratto.getGaranzia2().getId());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return g;
    }

     public static MROldSede sedeUscitaReport(Session sx,Integer idContratto) {
        MROldSede s = null;


        try {
            if (sx == null) {
                //sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                  //  sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto.getSedeUscita() != null) {
                s = (MROldSede) sx.get(MROldSede.class, contratto.getSedeUscita().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

      public static MROldSede sedeRientroPrevistoReport(Session sx,Integer idContratto) {
        MROldSede s = null;


        try {
            if (sx == null) {
                //sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                  //  sx = HibernateBridge.openNewSession();
                }
            }
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto.getSedeRientroPrevisto() != null) {
                s = (MROldSede) sx.get(MROldSede.class, contratto.getSedeRientroPrevisto().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

   public static String getExtraAmount(Session sx, MROldTariffa rate, Double days) {
       Double amount = 0.0;
       if (sx != null && sx.isOpen() && rate != null) {
           try {
           String query = "select it.importo_extra from importi_tariffe as it inner join stagione_tariffa as st on st.id = it.id_stagione inner join tariffe as t on t.id = st.id_tariffa where (it.minimo = :days or it.massimo = :days) and t.id = :idRate";

           List<Double> lista = sx.createSQLQuery(query).setParameter("idRate", rate.getId()).setParameter("days", days).list();

           if (lista != null && !lista.isEmpty()) {
               amount = lista.get(0);
           }
           } catch (Exception e) {
               log.error("ContrattoUtils.getExtraAmount - " + e.getMessage());
               e.printStackTrace();
               amount = 0.0;
           }
       }

       return new DecimalFormat("###0.00").format(amount);
   }
   public InputStream getSpaccatoWithDamagesNewAndOld(Session sx,int idMov) throws IOException
    { 
        Graphics2D g=null;
        ImageIcon originalCar = null;
        BufferedImage source=null;
        try 
        {
           MROldMovimentoAuto mov = (MROldMovimentoAuto) sx.load(MROldMovimentoAuto.class, idMov);
           int idVeicolo=mov.getVeicolo().getId();
           if(mov.getVeicolo().getGruppo().getWireframeImage()==null)
           {originalCar=new ImageIcon(ImageIO.read(getClass().getResource("/it/aessepi/myrentcs/images/zcarimg_big.jpg")));}
           else
           { originalCar=new ImageIcon(ImageIO.read( new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(mov.getVeicolo().getGruppo().getWireframeImage()))));}
           int realWidth = originalCar.getIconWidth();
           int realHeight = originalCar.getIconHeight();
           source=new BufferedImage(originalCar.getIconWidth(),originalCar.getIconHeight(),BufferedImage.TYPE_INT_RGB);
           g = source.createGraphics();
           originalCar.paintIcon(null, g, 0,0);
           BufferedImage addDamageImage=null;

           Iterator it = mov.getDanniPresenti().iterator();
           while (it.hasNext()) 
           {
               MROldDanno damage = (MROldDanno) it.next();
               if (damage.getDamageInPicture() != null) {
                if (damage.getDamageInPicture().size() != 0) 
                {
                    Set<MROldDamageInPicture> damageInPictureSet = damage.getDamageInPicture();
                    MROldDamageInPicture damageInPicture = damageInPictureSet.iterator().next();
                    Integer x = (damageInPicture.getPosition_x()* realWidth / 508)-120;
                    Integer y = (damageInPicture.getPosition_y() * realHeight / 274)+40;
                    byte[] imageByte;
                    BASE64Decoder decoder = new BASE64Decoder();
                    imageByte = decoder.decodeBuffer(damage.getDamageSeverity().getSeverityOldImage());
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
//                    if( damage!=null && damage.getMovimento()!=null && damage.getMovimento().getId()!=null && damage.getMovimento().getId()!=idMov )
//                    {
                        addDamageImage=ImageIO.read(bis);
//                    }
//                    else if(damage.getDamageSeverity().getId()==2 &&  damage.getMovimento().getId()!=idMov)
//                    {addDamageImage=ImageIO.read(getClass().getResource("/it/aessepi/myrentcs/images/halfcimg_old.png"));}
//                    else if(damage.getDamageSeverity().getId()==3 &&  damage.getMovimento().getId()!=idMov)
//                    {addDamageImage=ImageIO.read(getClass().getResource("/it/aessepi/myrentcs/images/qtrimg_old.png"));}
                    g.drawImage(addDamageImage,x,y, 45, 45, null);
                }
           } 
               
           }
           it = mov.getDanniRilevati().iterator();
           while (it.hasNext()) 
           {
               MROldDanno damage = (MROldDanno) it.next();
               if (damage.getRiparato() == null || !damage.getRiparato()) {
                   if (damage.getDamageInPicture() != null && damage.getDamageSeverity() != null && damage.getDamageSeverity().getSeverityOldImage() != null) {
               if (damage.getDamageInPicture() != null) {
                if (damage.getDamageInPicture().size() != 0) {
                    Set<MROldDamageInPicture> damageInPictureSet = damage.getDamageInPicture();
                    MROldDamageInPicture damageInPicture = damageInPictureSet.iterator().next();
                    Integer x = (damageInPicture.getPosition_x() * realWidth / 508) - 120;
                    Integer y = (damageInPicture.getPosition_y() * realHeight / 274) + 40;
                    byte[] imageByte;
                    BASE64Decoder decoder = new BASE64Decoder();
                    imageByte = decoder.decodeBuffer(damage.getDamageSeverity().getSeverityImage());
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
//                    if( damage.getMovimento().getId()==idMov )
//                    {
                    addDamageImage = ImageIO.read(bis);
//                    }
//                    }
//                    else if(damage.getDamageSeverity().getId()==2 &&  damage.getMovimento().getId()==idMov)
//                    {addDamageImage=ImageIO.read(getClass().getResource("/it/aessepi/myrentcs/images/halfcimg.png"));}
//                    else if(damage.getDamageSeverity().getId()==3 &&  damage.getMovimento().getId()==idMov)
//                    {addDamageImage=ImageIO.read(getClass().getResource("/it/aessepi/myrentcs/images/qtrimg.png"));}
                    g.drawImage(addDamageImage, x, y, 45, 45, null);
                }
               }
                }
           }
           }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        if(source!=null)
        {   
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(source, "jpg", baos);
        //ImageIO.write(source, "jpg", new File("C:\\Users\\NicoDev\\Desktop\\provaImg.jpg"));
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
        }
        return null;
        
    }
   public InputStream getSpaccatoWithDamagesOld(Session sx,int idMov) throws IOException
    {
        Graphics2D g=null;
        ImageIcon originalCar = null;
        BufferedImage source=null;
        try 
        {
           MROldMovimentoAuto mov = (MROldMovimentoAuto) sx.load(MROldMovimentoAuto.class, idMov);
           int idVeicolo=mov.getVeicolo().getId();
           if(mov.getVeicolo().getGruppo().getWireframeImage()==null)
           {
               originalCar=new ImageIcon(ImageIO.read(getClass().getResource("/it/aessepi/myrentcs/images/zcarimg_big.jpg")));
           }
           else
           {
               originalCar=new ImageIcon(ImageIO.read( new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(mov.getVeicolo().getGruppo().getWireframeImage()))));
           }
           source=new BufferedImage(originalCar.getIconWidth(),originalCar.getIconHeight(),BufferedImage.TYPE_INT_RGB);
           g = source.createGraphics();
           originalCar.paintIcon(null, g, 0,0);
       BufferedImage addDamageImage=null;
           Iterator it = mov.getDanniPresenti().iterator();
            Integer realHeight = originalCar.getIconHeight();
            Integer realWidth = originalCar.getIconWidth();

           while (it.hasNext()) 
           {
               MROldDanno damage = (MROldDanno) it.next();
               if (damage.getRiparato() == null || !damage.getRiparato()) {
                   if (damage.getDamageInPicture() != null && damage.getDamageSeverity() != null && damage.getDamageSeverity().getSeverityOldImage() != null) {
                       if (damage.getDamageInPicture().size() != 0) {
                           Set<MROldDamageInPicture> damageInPictureSet = damage.getDamageInPicture();
                           MROldDamageInPicture damageInPicture = damageInPictureSet.iterator().next();
                           Integer x = damageInPicture.getPosition_x();
                           Integer y = damageInPicture.getPosition_y();
                           byte[] imageByte;
                           BASE64Decoder decoder = new BASE64Decoder();
                           imageByte = decoder.decodeBuffer(damage.getDamageSeverity().getSeverityOldImage());
                           ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                           Integer finalX = (x * realWidth / 508) - 120;
                           Integer finalY = (y * realHeight / 274) + 40;
//                    }
//                           if (damage.getMovimento() != null && damage.getMovimento().getId() != idMov) {
                               addDamageImage=(ImageIO.read(bis));
//                           }
//                           System.out.println("------finalX-------"+finalX);
//                           System.out.println("------x-------"+x);
//                           System.out.println("------y-------"+y);
//                           System.out.println("-----Y------"+finalY);
//                           System.out.println("----"+addDamageImage);
                           g.drawImage(addDamageImage, finalX, finalY, 45, 45, null);
                       }
                   }
               }
           }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        if(source!=null)
        {   
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(source, "jpg", baos);
        ImageIO.write(source, "jpg", new File("myfiledamages.jpg"));
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
        }
        return null;
        
    }
    
   public UnmappedDamageImage[] getMovementDamageImages(Session sx, int idMov)
    {       
        UnmappedDamageImage[] UnmappedDamageImageArray=null;
        try{
                String sqlQuery = "SELECT GestDoc.path_file as DamagePath,"
                                    +"d.descrizione as damageName,"
                                    +"damDic.description as dictionary,"
                                    +"damageType.description as damageType,"
                                    +"severity.description as severity "
                                    +"From ((((gestione_documentale GestDoc "
                                    +"left Join mrdamage_in_picture DamInPic on GestDoc.id=DamInPic.id_gestione_documentale) "
                                    +"left JOIN danni d on d.id=DamInPic.id_danno) "
                                    +"left JOIN damage_dictionary damDic on damDic.id = d.id_damagedictionary)"
                                    +"left JOIN mrdamage_severity severity on severity.id = d.id_damageseverity) "
                                    +"left JOIN mrdamage_type damageType on damageType.id = d.id_damagetype "
                                    + "where d.id_movimento="+idMov;
                List<UnmappedDamageImage> results = sx.createSQLQuery(sqlQuery).addScalar("severity").addScalar("damageType").addScalar("dictionary").addScalar("damageName").addScalar("DamagePath").setResultTransformer( Transformers.aliasToBean(UnmappedDamageImage.class)).list();

                if(!results.isEmpty())
                {
//                    int response = JOptionPane.showConfirmDialog(null, "Print damage photos? / Stampare Foto danno?","",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                    if(response == JOptionPane.YES_OPTION)
//                    {
                        UnmappedDamageImageArray = new UnmappedDamageImage[results.size()];
                        UnmappedDamageImageArray = results.toArray(new UnmappedDamageImage[results.size()]);
                        String username = Preferenze.getSettingValue(sx, MROldSetting.USERNAME_GESTIONE_DOCUMENTALE_FTPS);
                        DESEncrypter cryp = new DESEncrypter(",.-pR0T0cOll0+++effeT1ùùP1...3Ss€''''?@");
                        String password = cryp.decrypt(Preferenze.getSettingValue(sx,MROldSetting.PASSWORD_GESTIONE_DOCUMENTALE_FTPS));
                        String urlFtps = cryp.decrypt(Preferenze.getSettingValue(sx,MROldSetting.URL_GESTIONE_DOCUMENTALE_FTPS));
                        for(int i=0;i<results.size();i++)
                        {

                            FileSecureUpload downLoadFile=new FileSecureUpload();
                            byte[] bytes;
                            bytes=downLoadFile.download(urlFtps + "/" + "Damages", username, password, UnmappedDamageImageArray[i].getDamagePath());
//                            String completeUrl="ftp://"+username+":"+password+"@"+urlFtps+"/"+"Damages"+"/"+UnmappedDamageImageArray[i].getDamagePath()+";type=i";
//                            URL url = new URL(completeUrl);
//                            URLConnection conn = url.openConnection();
                            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                            UnmappedDamageImageArray[i].setDamageImage(in);
                        }
                    //}
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        return UnmappedDamageImageArray;         
    }
  public UnmappedDamageImage[] getMovementDamageImages(Session sx, int idMov,MROldGruppo gruppi)
    {
        UnmappedDamageImage[] UnmappedDamageImageArray=null;
        try{
                String sqlQuery = "SELECT d.id as danniId,GestDoc.path_file as DamagePath,"
                                    +"d.descrizione as damageName,"
                                    +"concat(damDic.description,' / ',damDic.description_en) as dictionary,"
                                    +"concat(damageType.description ,' / ', damageType.description_en) as damageType,"
                                    +"concat(severity.description ,' / ' ,severity.description_en)  as severity ,"
                                     + "d.applied_price as ap "
                                    +"From ((((gestione_documentale GestDoc "
                                    +"left Join mrdamage_in_picture DamInPic on GestDoc.id=DamInPic.id_gestione_documentale) "
                                    +"left JOIN danni d on d.id=DamInPic.id_danno) "
                                    +"left JOIN damage_dictionary damDic on damDic.id = d.id_damagedictionary)"
                                    +"left JOIN mrdamage_severity severity on severity.id = d.id_damageseverity) "
                                    +"left JOIN mrdamage_type damageType on damageType.id = d.id_damagetype "
                                    + "where d.id_movimento="+idMov;
                List<UnmappedDamageImage> results = sx.createSQLQuery(sqlQuery).addScalar("severity").addScalar("damageType").addScalar("dictionary").addScalar("damageName").addScalar("DamagePath").addScalar("danniId").setResultTransformer( Transformers.aliasToBean(UnmappedDamageImage.class)).list();

                if(!results.isEmpty())
                {
                    UnmappedDamageImageArray = results.toArray(new UnmappedDamageImage[results.size()]);
                    for (int i = 0; i < results.size(); i++) {

//
                        List danniList=sx.createQuery("select d from MROldDanno d where d.id=:danniId").setParameter("danniId", UnmappedDamageImageArray[i].getDanniId()).list();
//
                        if(danniList.size()!=0){

                             MROldDanno d = (MROldDanno)danniList.get(0);

                            List<MROldDamagePrice> damagePrice = new ArrayList<MROldDamagePrice>();
//                List damagePrice=new List();

                            Query queryClienti = sx.createQuery("select dp from MROldDamagePrice dp where dp.groups=:group AND dp.damageDictionary=:damageDic AND dp.damageSeverity=:damageSev"); //NOI18N

                            queryClienti.setParameter("damageSev", d.getDamageSeverity());
                            queryClienti.setParameter("damageDic", d.getDamageDictionary());
                            queryClienti.setParameter("group", gruppi);//NOI18N
                            List retValue = queryClienti.list();

                            Iterator ret = retValue.iterator();
                            while (ret.hasNext()) {
                                MROldDamagePrice dp = (MROldDamagePrice) ret.next();
                                if (dp.getDamageDictionary().getIsEnable() == true) {
                                    damagePrice.add(dp);
                                }

                            }

                            if (!damagePrice.isEmpty()) {
                                if (d.getId() != null) {
//
                                }
                                if (Objects.equals(damagePrice.get(0).getMaxPrice(), d.getAppliedPrice()) && Objects.equals(damagePrice.get(0).getMinPrice(), d.getAppliedPrice())) {

//             UnmappedDamageImageArray[i].setDamageSeveritySubType("ALL");
                                } else if (Objects.equals(damagePrice.get(0).getMaxPrice(), d.getAppliedPrice())) {
                                    UnmappedDamageImageArray[i].setDamageSeveritySubType("Grave / Severe");
                                } else if (Objects.equals(damagePrice.get(0).getMinPrice(), d.getAppliedPrice())) {

                                    UnmappedDamageImageArray[i].setDamageSeveritySubType("Lieve / Small");
                                } else if (d.getId() != null) {
//                 UnmappedDamageImageArray[i].setDamageSeveritySubType("NṅO");
                                }
//
                            }

                        }
//

                    };
//                    int response = JOptionPane.showConfirmDialog(null, "Print damage photos? / Stampare Foto danno?","",JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//                    if(response == JOptionPane.YES_OPTION)
//                    {
                        UnmappedDamageImageArray = new UnmappedDamageImage[results.size()];
                        UnmappedDamageImageArray = results.toArray(new UnmappedDamageImage[results.size()]);
                        String username = Preferenze.getSettingValue(sx, MROldSetting.USERNAME_GESTIONE_DOCUMENTALE_FTPS);
                        DESEncrypter cryp = new DESEncrypter(",.-pR0T0cOll0+++effeT1ùùP1...3Ss€''''?@");
                        String password = cryp.decrypt(Preferenze.getSettingValue(sx,MROldSetting.PASSWORD_GESTIONE_DOCUMENTALE_FTPS));
                        String urlFtps = cryp.decrypt(Preferenze.getSettingValue(sx,MROldSetting.URL_GESTIONE_DOCUMENTALE_FTPS));
                        for(int i=0;i<results.size();i++)
                        {

                            FileSecureUpload downLoadFile=new FileSecureUpload();
                            byte[] bytes;
                            bytes=downLoadFile.download(urlFtps + "/" + "Damages", username, password, UnmappedDamageImageArray[i].getDamagePath());
//                            String completeUrl="ftp://"+username+":"+password+"@"+urlFtps+"/"+"Damages"+"/"+UnmappedDamageImageArray[i].getDamagePath()+";type=i";
//                            URL url = new URL(completeUrl);
//                            URLConnection conn = url.openConnection();
                            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
                            UnmappedDamageImageArray[i].setDamageImage(in);
                        }
                    //}
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        return UnmappedDamageImageArray;
    }


   public UnmappedDamageImage[] getMovementDamageDetails(Session sx,int idMov)
   { 
       UnmappedDamageImage[] UnmappedDamageImageArray=null;     
        try{    
                String sqlQuery = "SELECT GestDoc.path_file as DamagePath,"
                                    +"d.descrizione as damageName,"
                                    +"damDic.description as dictionary,"
                                    +"damageType.description as damageType,"
                                    +"severity.description as severity "
                                    +"From ((((gestione_documentale GestDoc "
                                    +"left Join mrdamage_in_picture DamInPic on GestDoc.id=DamInPic.id_gestione_documentale) "
                                    +"left JOIN danni d on d.id=DamInPic.id_danno) "
                                    +"left JOIN damage_dictionary damDic on damDic.id = d.id_damagedictionary)"
                                    +"left JOIN mrdamage_severity severity on severity.id = d.id_damageseverity) "
                                    +"left JOIN mrdamage_type damageType on damageType.id = d.id_damagetype "
                                    + "where d.id_movimento="+idMov;
                List<UnmappedDamageImage> results = sx.createSQLQuery(sqlQuery).addScalar("severity").addScalar("damageType").addScalar("dictionary").addScalar("damageName").addScalar("DamagePath").setResultTransformer( Transformers.aliasToBean(UnmappedDamageImage.class)).list();

            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        return UnmappedDamageImageArray;   
   }


    public static Boolean isOptionalAvailable(MROldTariffa tariffa, String codice){
        List extras = new ArrayList();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator<MROldOptional> optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = optionals.next();
                if (optional.getCodice().startsWith(codice)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String transformCodiceVoucherToNumOrd(String codiceVouch, String rentalType){

        String numOrder = "";
        if (codiceVouch != null && codiceVouch.startsWith("20")
                && rentalType != null
                && rentalType.equalsIgnoreCase("s")) {
            try {
                if (codiceVouch.length() == 14) {
                    StringBuilder temp = new StringBuilder("000000000");
                    temp = temp.replace(0, 2, codiceVouch.substring(2, 4));
                    temp = temp.replace(2, 9, codiceVouch.substring(7, 14));
                    //                    codiceVouch = codiceVouch.replaceFirst("20", "");
                    //                    codiceVouch = codiceVouch.replaceAll("000", "");
                    numOrder = temp.toString();

                }

            } catch (Exception e) {
            }
        } else {
            numOrder = codiceVouch;
        }
        return numOrder;
    }


    public static Double getFranchigiaTP(MROldTariffa tariffa) {
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getCodice()!=null && "TP".equals(optional.getCodice())) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    //if ( (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) && !optionalTariffa.getIncluso() ) {
                    if ( optionalTariffa.getFranchigia() !=null){
                        return optionalTariffa.getFranchigia();
                    }
                    // }
                }
            }
        }
        return null;
    }
    public static Double getFranchigiaTLW(MROldTariffa tariffa) {
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                if (optional.getCodice()!=null && "TLW".equals(optional.getCodice())) {
                    MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                    // if ( (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) && !optionalTariffa.getIncluso() ) {
                    if ( optionalTariffa.getFranchigia() !=null){
                        return optionalTariffa.getFranchigia();
                    }
                    //  }
                }
            }
        }
        return null;
    }

    public static List<MROldNexiTransactionDeposit> recuperaNexiTransactionDeposit (Session sx, Integer idContratto) {
        List<MROldNexiTransactionDeposit> listaNTDeposit = null;
        try {
            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            if (contratto != null) {
                String queryStr = "SELECT ntd FROM MROldNexiTransactionDeposit ntd WHERE ntd.contratto = :contratto";
                Query query = sx.createQuery(queryStr);

                query.setParameter("contratto", contratto);

                listaNTDeposit = query.list();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return listaNTDeposit;
    }
    private Map leggiFattureContrattiAgente(Session sx,MROldFornitori agente, MROldAffiliato affiliato, Date dataInizio, Date dataFine, Boolean ricercaFatture) {
        //TODO Includere anche le fatture riepilogative.
        Map fatture = new HashMap();
//        Session mySession = null;
        try {
//            mySession = HibernateBridge.startNewSession();
            Query queryFatture;
            Query queryFattureR2R;
            String queryString = "select f from MROldDocumentoFiscale f where f.affiliato.id = :id_affiliato and f.contratto.agent.id = :id_agent";
            String queryR2RString = "select r from MROldRigaDocumentoFiscale r left join r.fattura where r.fattura.affiliato.id = :id_affiliato and r.fattura.contratto is null and r.fattura.prenotazione is null "
                    + "and r.agent.id = :id_agent";
            String queryData = "";
            String queryDataR2R = "";
            String campoData;
            String campoDataR2R;

            AnagraficaAzienda obj = (AnagraficaAzienda) Parameters.getAnagraficaAzienda();
            //dodification done for galdieri ( sistem rental )
            //second modification by nicolo,set a control using the "partita iva" of the customer
            //if system rental modifies it, that could be a problem
            if(obj!=null) {
                if (obj.getPartitaIva().equals("02406230652")) {
                    queryString += " and f.contratto.veicolo.campoCustomUno is null";
                }
            }
            //end galdieri


            if(ricercaFatture.equals(Boolean.TRUE)) {
                campoData = "f.data";
                campoDataR2R = "r.fattura.data";
            } else {
                campoData = "f.contratto.data";
                campoDataR2R = "r.fattura.rent2rent.contractDate";
            }

            if(dataInizio != null && dataFine != null) {
                queryData = " and " + campoData + " between :dataInizio and :dataFine";
                queryDataR2R = " and " + campoDataR2R + " between :dataInizio and :dataFine";
            } else if(dataInizio != null) {
                queryData = " and " + campoData + " >= :dataInizio";
                queryDataR2R = " and " + campoDataR2R + " >= :dataInizio";
            } else if(dataFine != null) {
                queryData = " and " + campoData + " <= :dataFine";
                queryDataR2R = " and " + campoDataR2R + " <= :dataFine";
            }



            queryFatture = sx.createQuery(queryString.concat(queryData).concat(" order by f.data, f.numero"));
            queryFattureR2R = sx.createQuery(queryR2RString.concat(queryDataR2R).concat(" order by r.fattura.data, r.fattura.numero"));

            if(dataInizio != null) {
                queryFatture.setParameter("dataInizio", dataInizio);
                queryFattureR2R.setParameter("dataInizio", dataInizio);
            }
            if(dataFine != null) {
                queryFatture.setParameter("dataFine", dataFine);
                queryFattureR2R.setParameter("dataFine", dataFine);
            }
            queryFatture.setParameter("id_agent", agente.getId());
            queryFatture.setParameter("id_affiliato", affiliato.getId());
            queryFattureR2R.setParameter("id_agent", agente.getId());
            queryFattureR2R.setParameter("id_affiliato", affiliato.getId());

            List elencoFatture = queryFatture.list();
            List elencoFattureR2R = queryFattureR2R.list();
            Iterator it = elencoFattureR2R.iterator();
            while (it.hasNext()) {
                elencoFatture.add(it.next());
            }
//            mySession.close();
            if(elencoFatture != null) {
                for (int i = 0; i < elencoFatture.size(); i++) {
                    if (elencoFatture.get(i) instanceof MROldDocumentoFiscale) {
                        MROldDocumentoFiscale fattura = (MROldDocumentoFiscale) elencoFatture.get(i);
                        if (fatture.get(fattura.getContratto()) == null) {
                            fatture.put(fattura.getContratto(), new ArrayList());
                        }
                        ((List) fatture.get(fattura.getContratto())).add(fattura);
                    } else {
                        MROldRigaDocumentoFiscale rigaFattura = (MROldRigaDocumentoFiscale) elencoFatture.get(i);
                        if (fatture.get(rigaFattura.getFattura().getRent2rent()) == null) {
                            fatture.put(rigaFattura.getFattura().getRent2rent(), new ArrayList());
                        }

                        MROldDocumentoFiscale tmp = new MROldDocumentoFiscale();

                        tmp.setData(rigaFattura.getFattura().getData());
                        tmp.setCliente(rigaFattura.getFattura().getCliente());
                        tmp.setNumero(rigaFattura.getFattura().getNumero());
                        tmp.setTotaleImponibile(rigaFattura.getTotaleImponibileRiga());


                        tmp.setFatturaRighe(new <MROldRigaDocumentoFiscale> ArrayList());

                        tmp.getFatturaRighe().add(rigaFattura);
                        ((List) fatture.get(rigaFattura.getFattura().getRent2rent())).add(tmp);
                    }
                }
//                mySession.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
//            if(mySession != null) HibernateBridge.closeSession();
        }
        return fatture;
    }

    public static List getExtrasNotIncluded(MROldTariffa tariffa) {
        List<MROldOptionalTariffa> extras = new ArrayList<MROldOptionalTariffa>();
        if (tariffa != null && tariffa.getOptionalsTariffa() != null) {
            Iterator optionals = tariffa.getOptionalsTariffa().keySet().iterator();
            while (optionals.hasNext()) {
                MROldOptional optional = (MROldOptional) optionals.next();
                MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) tariffa.getOptionalsTariffa().get(optional);
                if ( (optionalTariffa.getSelezionato().equals(Boolean.TRUE) || optionalTariffa.getSelezionatoRientro().equals(Boolean.TRUE)) && !optionalTariffa.getIncluso() && optionalTariffa.getPrepagato().equals(Boolean.FALSE)) {
                    extras.add(optionalTariffa);
                }
            }
        }

       return extras;
    }


    public static List listaRigheNotIncPreventivo(Session sx,MROldSede locationPickUp, MROldTariffa tariffa, Date inizio, Date fine, Integer kmTotali, Double scontoPercentuale, Boolean prepagato) {
        List righeFattura = null;
        List newrigheFattura = null;
        List righePreventivo = null;
        MROldPreventivo aPreventivo = new MROldPreventivo();
        if (locationPickUp!=null)
            aPreventivo.setSedeUscita(locationPickUp);

        Integer numeroGiorni = null;
        //MROldTariffa newtariffa = calculateNotIncTariffa(sx, tariffa);
        if (prepagato) {
            numeroGiorni = numeroGiorniEsatti(tariffa, FormattedDate.extractDate(inizio), FormattedDate.extractTime(inizio), FormattedDate.extractDate(fine), FormattedDate.extractTime(fine));
        }
        try {

            if (sx == null || (sx != null && !sx.isOpen())) {
                //  sx = HibernateBridge.startNewSession();
            }
            try {/*
                 * Andrea
                 */
                MROldSede sedePickUp = locationPickUp;
                righeFattura = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                        sx,
                        aPreventivo,
                        tariffa,
                        inizio,
                        fine,
                        kmTotali,
                        null,
                        null,
                        scontoPercentuale,
                        numeroGiorni).setLocationPickUpAndReturnFatturazione(sedePickUp).calcolaRigheProssimaFattura(sx);
            } catch (Exception ex) {
                righeFattura = null;
            }

            if (numeroGiorni != null) {
                try {
                    List listaPrepagato = FatturazioneFactory.PREPAGATO_FACTORY.getFatturazione(sx,tariffa, inizio, fine, numeroGiorni).calcolaRigheProssimaFattura();
                } catch (Exception tex) {
                    tex.printStackTrace();
                }
            }

            Double[] totali = FatturaUtils.calcolaImportiRigheFattura(righeFattura);
            aPreventivo.setTotaleImponibile(totali[0]);
            aPreventivo.setTotaleIva(totali[1]);
            aPreventivo.setTotaleDocumento(totali[2]);
            aPreventivo.setInizio(inizio);
            aPreventivo.setFine(fine);


            int righe =0;
            if (righeFattura!=null)
                righe = righeFattura.size();

            List<MROldOptionalTariffa> incRateList = getExtrasNotIncluded(tariffa);
            if (righe > 0) {
                righePreventivo = new ArrayList();
                Double amount = 0.0;
                for (int i = 0; i < righe; i++) {
                    MROldRigaDocumentoFiscale tmpRigaFattura = (MROldRigaDocumentoFiscale) righeFattura.get(i);
                    if (tmpRigaFattura != null) {
                        if(checkRateIsIncluded(tmpRigaFattura, incRateList)) {
                            MROldRigaPreventivo tmpRigaPreventivo = new MROldRigaPreventivo(tmpRigaFattura, aPreventivo);
                            tmpRigaPreventivo.setNumeroRigaPreventivo(new Integer(i));
                            righePreventivo.add(tmpRigaPreventivo);
                        } else {
                            if (!(tmpRigaFattura.getDescrizione().toLowerCase().contains("giorni extra") ||
                                    tmpRigaFattura.getDescrizione().toLowerCase().contains("extra rate") ||
                                    tmpRigaFattura.getDescrizione().toLowerCase().contains("giorni tariffa") ||
                                    tmpRigaFattura.getDescrizione().toLowerCase().contains("base rate"))){
                                amount += tmpRigaFattura.getTotaleRiga();
                            }
                            continue;
                        }
                    }


                }
                for (int i = 0; i < righePreventivo.size(); i++) {
                    MROldRigaPreventivo tmpRigaPreventivo = (MROldRigaPreventivo) righePreventivo.get(i);
                    if (tmpRigaPreventivo.getDescrizione().toLowerCase().contains("giorni tariffa") ||
                            tmpRigaPreventivo.getDescrizione().toLowerCase().contains("base rate")){
                        amount += tmpRigaPreventivo.getTotaleRiga();
                        tmpRigaPreventivo.setTotaleRiga(amount);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return righePreventivo;
    }

    public static boolean checkRateIsIncluded(MROldRigaDocumentoFiscale tmpRigaFattura, List<MROldOptionalTariffa> notIncOptional){

        if (tmpRigaFattura.getDescrizione().toLowerCase().contains("giorni extra") ||
                tmpRigaFattura.getDescrizione().toLowerCase().contains("extra rate") ||
                tmpRigaFattura.getDescrizione().toLowerCase().contains("giorni tariffa") ||
                tmpRigaFattura.getDescrizione().toLowerCase().contains("base rate")){
            return true;
        } else {
            for (int i = 0; i < notIncOptional.size(); i++) {
                MROldOptionalTariffa opt = notIncOptional.get(i);
                if ((tmpRigaFattura.getDescrizione().toLowerCase().contains("giorni extra") ||
                        tmpRigaFattura.getDescrizione().toLowerCase().contains("extra rate") ||
                        tmpRigaFattura.getDescrizione().toLowerCase().contains("giorni tariffa") ||
                        tmpRigaFattura.getDescrizione().toLowerCase().contains("base rate")) ||
                        opt.getOptional().getDescrizione().equals(tmpRigaFattura.getDescrizione()))
                    return true;
            }
            return false;
        }
    }

    public static String getSuffissoPlurimensile(Session sx, MROldMovimentoAuto movimento) {

        MROldContrattoNoleggio c = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, movimento.getContratto().getId());
        if (c.getIsMonthlyRental() != null && c.getIsMonthlyRental()) {
            List<MROldMovimentoAuto> listaMovimenti = sx.createQuery("select mov from MROldMovimentoAuto mov where mov.contratto = :contratto order by mov.inizio")
                    .setParameter("contratto", c).list();
            Integer contatore = 1;
            for (MROldMovimentoAuto mov : listaMovimenti) {
                if (!mov.equals(movimento)) {
                    contatore++;
                } else {
                    break;
                }
            }
            if (contatore == 1) {
                return "";
            } else {
                return " / " + contatore.toString();
            }
        } else {
            return "";
        }
    }

    public static MROldTariffa getTariffaGruppoAssegnato (Session sx,Integer idContratto) {
        MROldTariffa tariffaGruppoAssegnato = null;
        MROldContrattoNoleggio c = null;
//        Session sx = HibernateBridge.getSession();
        boolean sessioneRiaperta = false;
//        if (sx != null && !sx.isOpen()) {
//            sx = HibernateBridge.startNewSession();
//            sessioneRiaperta = true;
//
//        }
        try {
            if (idContratto != null) {
                c = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, idContratto);
            }
            if (c != null && c.getId() != null) {
                c = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, c.getId());
                MROldFonteCommissione fonte = null;
                Date dataPrenotazione = null;
                if (c.getCommissione() != null) {
                    MROldCommissione commissione = c.getCommissione();
                    if (commissione.getFonteCommissione() != null) {
                        fonte = commissione.getFonteCommissione();
                    }
                    if (commissione.getPrenotazione() != null) {
                        MROldPrenotazione prenotazione = commissione.getPrenotazione();
                        dataPrenotazione = prenotazione.getData();
                    }
                    if (dataPrenotazione == null) {
                        dataPrenotazione = c.getData();
                    }
                }
                Boolean aeroporto = null;
                Boolean ferrovia = null;
                MROldSede locationUscita = null;
                if (c.getSedeUscita() != null) {
                    locationUscita = c.getSedeUscita();
                    aeroporto = locationUscita.getAeroporto();
                    ferrovia = locationUscita.getFerrovia();
                }
                Boolean conducente2 = c.getConducente2() != null;
                Boolean conducente3 = c.getConducente3() != null;
                Date dataNascita1 = null;
                Date dataNascita2 = null;
                Date dataNascita3 = null;
                if (c.getConducente1() != null) {
                    MROldConducenti driver1 = c.getConducente1();
                    dataNascita1 = driver1.getDataNascita();
                }
                if (Boolean.TRUE.equals(conducente2)) {
                    MROldConducenti driver2 = c.getConducente2();
                    dataNascita2 = driver2.getDataNascita();
                }
                if (Boolean.TRUE.equals(conducente3)) {
                    MROldConducenti driver3 = c.getConducente2();
                    dataNascita3 = driver3.getDataNascita();
                }
                tariffaGruppoAssegnato = TariffeUtils.creaTariffa(sx, fonte, c.getGruppoAssegnato(), aeroporto, ferrovia, conducente2, conducente3,
                        dataNascita1, dataNascita2, dataNascita3, c.getInizio(), c.getFine(), dataPrenotazione, locationUscita);
                if (tariffaGruppoAssegnato.getOptionalsTariffa() == null || tariffaGruppoAssegnato.getOptionalsTariffa().isEmpty()) {
                    tariffaGruppoAssegnato = c.getTariffa();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tariffaGruppoAssegnato;
    }

    public static boolean disabbinaVeicolo (Session sx, MROldParcoVeicoli veicolo, Date inizio, Date fine) {
        boolean retValue = true;
        try {
            if (!NotificheUtils.isVeicoloNelParcoPrimaDel(veicolo, fine) || !NotificheUtils.isVeicoloNelParcoDopoIl(veicolo, inizio)) {
                return false;
            }
            Query queryMovimentiPrenotazione = sx.createQuery("SELECT m FROM MROldMovimentoAuto m WHERE m.veicolo = :veicolo AND m.causale.id = :prenotazione "
                    + "AND ((m.inizio <= :inizioPrenotazione AND m.fine >= :inizioPrenotazione) "
                    + "OR (m.inizio <= :finePrenotazione AND m.fine >= :finePrenotazione) "
                    + "OR (m.inizio >= :inizioPrenotazione AND m.fine <= :finePrenotazione)) "
                    + "ORDER BY m.inizio");
            queryMovimentiPrenotazione.setParameter("veicolo", veicolo);
            queryMovimentiPrenotazione.setParameter("prenotazione", 2);
            queryMovimentiPrenotazione.setParameter("finePrenotazione", fine);
            queryMovimentiPrenotazione.setParameter("inizioPrenotazione", inizio);
            Query queryMovimentiNoRez = sx.createQuery("SELECT m FROM MROldMovimentoAuto m WHERE m.veicolo = :veicolo AND m.causale.id <> :prenotazione "
                    + "AND ((m.inizio <= :inizioPrenotazione AND m.fine >= :inizioPrenotazione) "
                    + "OR (m.inizio <= :finePrenotazione AND m.fine >= :finePrenotazione) "
                    + "OR (m.inizio >= :inizioPrenotazione AND m.fine <= :finePrenotazione)) "
                    + "ORDER BY m.inizio");
            queryMovimentiNoRez.setParameter("veicolo", veicolo);
            queryMovimentiNoRez.setParameter("prenotazione", 2);
            queryMovimentiNoRez.setParameter("finePrenotazione", fine);
            queryMovimentiNoRez.setParameter("inizioPrenotazione", inizio);

            List<MROldMovimentoAuto> listaMovimentiDaDisabbinare = queryMovimentiPrenotazione.list();
            List<MROldMovimentoAuto> listaMovimentiNoRez = queryMovimentiNoRez.list();
            if (listaMovimentiNoRez.size() > 0) {
                retValue = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retValue;

    }
    public String durataObj(MROldDurata durObj,String lang){
        Locale local=Locale.getDefault();
//        System.out.println ("lang----1------"+lang);
        if(lang.contains("_")){
            lang=lang.split("_")[0];
        }else{
            lang=lang;
        }
//        System.out.println ("lang-----2-----"+lang);
        Locale.setDefault(new Locale(lang,""));

//        System.out.println ("local----------"+Locale.getDefault());
       ResourceBundle bundle = ResourceBundle.getBundle("it/myrent/ee/db/Bundle", Locale.getDefault());
//    bundle.getLocale();
        MessageFormat VARIABILE_MIN = new MessageFormat(bundle.getString("Durata.VARIABLE_MIN0"));
      MessageFormat VARIABILE_MIN_MAX = new MessageFormat(bundle.getString("Durata.VARIABLE_MIN0_MAX1"));
         MessageFormat FISSO_MIN_MAX = new MessageFormat(bundle.getString("Durata.FISSO_MIN0_MAX1"));
         MessageFormat FISSO_MIN_MIDDLE_MAX = new MessageFormat(bundle.getString("Durata.FISSO_MIN0_MIDDLE1_MAX2"));
         MessageFormat FISSO_MIN_DOTS_MAX = new MessageFormat(bundle.getString("Durata.FISSO_MIN0_DOTS_MAX0"));
        Integer min=durObj.getMinimo();
        Integer max=durObj.getMassimo();
        if(min.equals(max)) {
            Locale.setDefault(local);
            return VARIABILE_MIN.format(new Object[]{min});
        } else {
            if(Boolean.TRUE.equals(durObj.getImportoFisso())) {
                if(max - min == 1) {
                    Locale.setDefault(local);
                    return FISSO_MIN_MAX.format(new Object[]{min , max});
                } else if(max - min == 2) {
                    Locale.setDefault(local);
                    return FISSO_MIN_MIDDLE_MAX.format(new Object[]{min, min + 1, max});
                } else {
                    Locale.setDefault(local);
                    return FISSO_MIN_DOTS_MAX.format(new Object[]{min, max});
                }
            } else {
                Locale.setDefault(local);
                return VARIABILE_MIN_MAX.format(new Object[]{min, max});
            }
        }

    }
 }
