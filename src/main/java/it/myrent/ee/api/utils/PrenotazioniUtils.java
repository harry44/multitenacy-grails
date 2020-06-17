/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.api.utils;


import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.db.*;

import java.awt.Component;
import java.io.File;
import java.util.Calendar;
import java.util.Date;


import org.hibernate.Session;

/**
 *
 * @author bogdan
 */
public class PrenotazioniUtils {

    /*
    public static boolean esisteReport(String nomeReport) {
        String fileNameReport = JasperMyTools.nomeFileJasper(nomeReport);
        return (fileNameReport != null && new File(fileNameReport).exists());
    }
    public static void emailVoucher(Component parent, Session sx, Prenotazione prenotazione) throws JRException, MalformedURLException {
        HashMap parameters = new HashMap();
        JDialogAnagraficaAzienda.putParameters(parameters);
        parameters.put("SESSION", sx);
        String nazione = prenotazione.getCliente().getNazione();
        if (nazione == null || !nazione.startsWith("ITA")) {
            parameters.put("REPORT_LOCALE", Locale.ENGLISH);
        }
        String fileName = null, email = null;
        JasperPrint voucher = null;
        if (esisteReport("Reservation")) {
            Booking booking = new Booking(sx, prenotazione);
            voucher = JasperMyTools.creaPagineStampa("Reservation", null, parameters, new Object[]{booking});
            fileName = "confirmed_" + booking.getConfNumber() + ".pdf";
            email = booking.getDriver().getEmail();
        } else {
            String confirmationNumber = null;
            if (prenotazione.getPrefisso() != null) {
                confirmationNumber = new StringBuffer().append(prenotazione.getPrefisso()).append("-").
                        append(prenotazione.getNumero()).append("-").
                        append(prenotazione.getAnno()).
                        toString();
            } else {
                confirmationNumber = new StringBuffer().append(prenotazione.getNumero()).append("-").
                        append(prenotazione.getAnno()).
                        toString();
            }
            if (esisteReport("prenotazione")) {
                voucher = JasperMyTools.creaPagineStampa("prenotazione", null, parameters, new Object[]{prenotazione});
            } else {
                voucher = JasperMyTools.creaPagineStampa("prenotazioni", null, parameters, new Object[]{prenotazione});
            }
            fileName = "confirmed_" + confirmationNumber + ".pdf";
            email = prenotazione.getCliente().getEmail();
        }
        JasperExportManager.exportReportToPdfFile(voucher, System.getProperty("java.io.tmpdir") + File.separator + fileName);
        JDialogEmail.showEmailDialog(
                parent,
                true,
                sx,
                email,
                null,
                "Your booking with us",
                "Welcome!\n\n"
                + "This email is to confirm your booking with us.\n"
                + "Please find all booking details in the attached file.\n"
                + "Thank you for traveling with us!",
                new String[]{fileName});
        FileUtils.deleteFile(fileName);
    }

    public static void printVoucher(JDialog parent, Session sx, Prenotazione[] prenotazioni) throws JRException, IOException, InterruptedException {
        JasperPrint stampe = null;
        HashMap parameters = new HashMap();
        JDialogAnagraficaAzienda.putParameters(parameters);
        parameters.put("SESSION", sx);
        if (esisteReport("Reservation")) {
            Booking[] bookings = new Booking[prenotazioni.length];

            for (int i = 0; i < prenotazioni.length; i++) {
                bookings[i] = new Booking(sx, prenotazioni[i]);
                
                String nazione = prenotazioni[i].getCliente().getNazione();
                if (nazione == null || !nazione.startsWith("ITA")) {
                    parameters.put("REPORT_LOCALE", Locale.ENGLISH);
                }
                JasperPrint pages = JasperMyTools.creaPagineStampa("Reservation", null, parameters, new Object[]{new Booking(sx, prenotazioni[i])});
                if (stampe == null) {
                    stampe = pages;
                } else {
                    stampe.getPages().addAll(pages.getPages());
                }
            }
        } else {
            if(esisteReport("prenotazione")) {
                stampe = JasperMyTools.creaPagineStampa("prenotazione", null, parameters, prenotazioni);
            } else {
                stampe = JasperMyTools.creaPagineStampa("prenotazioni", null, parameters, prenotazioni);
            }
        }
        JasperMyTools.visualizzaPagineStampa(parent, stampe);
    }


    public static Integer numeroGiorniEsatti(Tariffa tariffa, Date dataInizio, Date oraInizio, Date dataFine, Date oraFine) {
        Integer giorniNoleggio = null;
        if (tariffa != null && Boolean.TRUE.equals(tariffa.getOraRientroAttiva()) && tariffa.getOraRientro() != null) {
            oraFine = tariffa.getOraRientro();
        }

        if (dataInizio != null && oraInizio != null && dataFine != null && oraFine != null) {
            Date inizio = FormattedDate.createTimestamp(dataInizio, oraInizio);

            Integer ritardoMassimoMinuti = Preferenze.getContrattoRitardoMassimoMinuti();

            Date fine = FormattedDate.add(FormattedDate.createTimestamp(dataFine,oraFine), Calendar.MINUTE, -ritardoMassimoMinuti.intValue());
            giorniNoleggio =new Integer((int) Math.max(1.0,
                    Math.ceil(FormattedDate.numeroGiorni(inizio,
                    fine,
                    true))));
        } else {
            giorniNoleggio = null;
        }

        return giorniNoleggio;
    }
    */


    public static Integer numeroGiorniEsatti(Session sx, MROldTariffa tariffa, Date dataInizio, Date oraInizio, Date dataFine, Date oraFine) {
//        if (sx == null) {
////            HibernateBridge.startNewSession();
//        }
        Integer giorniNoleggio = null;
        if (tariffa != null && Boolean.TRUE.equals(tariffa.getOraRientroAttiva()) && tariffa.getOraRientro() != null) {
            oraFine = tariffa.getOraRientro();
        }

        if (dataInizio != null && oraInizio != null && dataFine != null && oraFine != null) {
            Date inizio = FormattedDate.createTimestamp(dataInizio, oraInizio);

            Integer ritardoMassimoMinuti = null;

                ritardoMassimoMinuti = Preferenze.getContrattoRitardoMassimoMinuti(sx);


            Date fine = FormattedDate.add(FormattedDate.createTimestamp(dataFine,oraFine), Calendar.MINUTE, -ritardoMassimoMinuti.intValue());
            giorniNoleggio =new Integer((int) Math.max(1.0,
                    Math.ceil(FormattedDate.numeroGiorni(inizio,
                            fine,
                            true))));
        } else {
            giorniNoleggio = null;
        }

        return giorniNoleggio;
    }

    public static Integer contaFattureEmesseAlBroker(MROldPrenotazione prenotazione,Session sx) {
        //Session sx = null;
        try {
            //sx = HibernateBridge.startNewSession();
            MROldPrenotazione p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, prenotazione.getId());

            Long risultato = (Long) sx.createQuery("SELECT count(d.id) FROM MROldDocumentoFiscale d where d.cliente.id = :idCliente and d.prenotazione.id = :idPrenotazione")
                             .setParameter("idCliente", p.getCommissione().getFonteCommissione().getCliente().getId())
                             .setParameter("idPrenotazione", p.getId())
                             .uniqueResult();
            if (risultato != null) {
                return (Integer) risultato.intValue();
            }
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

        return null;
    }




    public static boolean prenotazionePrepagata(Session sx,MROldPrenotazione p) {
        p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, p.getId());
        MROldCommissione c = (MROldCommissione) sx.get(MROldCommissione.class, p.getCommissione().getId());

        return c.getPrepagato();
    }
    //metodi usati nel report di stampa per la firma certa
    /*
    public static Pagamento tipoPagamentoReport(Session sx,Integer idPrenotazione) {
        Pagamento p = null;


        try {
            if (sx == null) {
                sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                    sx = HibernateBridge.openNewSession();
                }
            }
            Prenotazione Prenotazione = (Prenotazione) sx.get(Prenotazione.class, idPrenotazione);
            if (Prenotazione.getPagamento() != null) {
                p = (Pagamento) sx.get(Pagamento.class, Prenotazione.getPagamento().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    public static Gruppo gruppoReport(Session sx,Integer idPrenotazione) {
        Gruppo g = null;


        try {
            if (sx == null) {
                sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                    sx = HibernateBridge.openNewSession();
                }
            }
            Prenotazione Prenotazione = (Prenotazione) sx.get(Prenotazione.class, idPrenotazione);
            if (Prenotazione.getGruppo() != null) {
                g = (Gruppo) sx.get(Gruppo.class, Prenotazione.getGruppo().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return g;
    }

    public static Gruppo gruppoAssegnatoReport(Session sx,Integer idPrenotazione) {
        Gruppo g = null;


        try {
            if (sx == null) {
                sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                    sx = HibernateBridge.openNewSession();
                }
            }
            Prenotazione Prenotazione = (Prenotazione) sx.get(Prenotazione.class, idPrenotazione);
            if (Prenotazione.getGruppoAssegnato() != null) {
                g = (Gruppo) sx.get(Gruppo.class, Prenotazione.getGruppoAssegnato().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return g;
    }

    public static Sede sedeReport(Session sx,Integer idPrenotazione,boolean sedeUscita) {
        Sede s = null;


        try {
            if (sx == null) {
                sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                    sx = HibernateBridge.openNewSession();
                }
            }
            Prenotazione Prenotazione = (Prenotazione) sx.get(Prenotazione.class, idPrenotazione);

            if (sedeUscita) {
                if (Prenotazione.getSedeUscita() != null) {
                    s = (Sede) sx.get(Sede.class, Prenotazione.getSedeUscita().getId());
                }
            } else {
                if (Prenotazione.getSedeRientroPrevisto() != null) {
                    s = (Sede) sx.get(Sede.class, Prenotazione.getSedeRientroPrevisto().getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s;
    }

    public static Tariffa tariffaReport(Session sx,Integer idPrenotazione) {
        Tariffa t = null;


        try {
            if (sx == null) {
                sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                    sx = HibernateBridge.openNewSession();
                }
            }
            Prenotazione Prenotazione = (Prenotazione) sx.get(Prenotazione.class, idPrenotazione);
            if (Prenotazione.getTariffa() != null) {
                t = (Tariffa) sx.get(Tariffa.class, Prenotazione.getTariffa().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    public static MovimentoAuto movimentoReport(Session sx,Integer idPrenotazione) {
        MovimentoAuto m = null;


        try {
            if (sx == null) {
                sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                    sx = HibernateBridge.openNewSession();
                }
            }
            Prenotazione prenotazione = (Prenotazione) sx.get(Prenotazione.class, idPrenotazione);
            if (prenotazione.getMovimento() != null) {
                m = (MovimentoAuto) sx.get(MovimentoAuto.class, prenotazione.getMovimento().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return m;
    }

     public static Commissione commissioneReport(Session sx,Integer idPrenotazione) {
        Commissione c = null;


        try {
            if (sx == null) {
                sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                    sx = HibernateBridge.openNewSession();
                }
            }
            Prenotazione prenotazione = (Prenotazione) sx.get(Prenotazione.class, idPrenotazione);
            if (prenotazione.getCommissione() != null) {
                c = (Commissione) sx.get(Commissione.class, prenotazione.getCommissione().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

     public static Conducenti conducenteReport(Session sx,Integer idPrenotazione,int numeroConducente) {
        Conducenti c = null;


        try {
            if (sx == null) {
                sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                    sx = HibernateBridge.openNewSession();
                }
            }
            Prenotazione prenotazione = (Prenotazione) sx.get(Prenotazione.class, idPrenotazione);
            if (numeroConducente == 1) {
                if (prenotazione.getConducente1() != null) {
                    c = (Conducenti) sx.get(Conducenti.class, prenotazione.getConducente1().getId());
                }
            } else if (numeroConducente == 2) {
                if (prenotazione.getConducente2() != null) {
                    c = (Conducenti) sx.get(Conducenti.class, prenotazione.getConducente2().getId());
                }
            } else if (numeroConducente == 3) {
                if (prenotazione.getConducente3() != null) {
                    c = (Conducenti) sx.get(Conducenti.class, prenotazione.getConducente3().getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

     public static Clienti clienteReport(Session sx,Integer idPrenotazione) {
        Clienti c = null;


        try {
            if (sx == null) {
                sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                    sx = HibernateBridge.openNewSession();
                }
            }
            Prenotazione prenotazione = (Prenotazione) sx.get(Prenotazione.class, idPrenotazione);
            if (prenotazione.getCliente() != null) {
                c = (Clienti) sx.get(Clienti.class, prenotazione.getCliente().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    public static Numerazione numerazionReport(Session sx,Integer idPrenotazione) {
        Numerazione n = null;


        try {
            if (sx == null) {
                sx = HibernateBridge.startNewSession();
            } else {
                if (!sx.isOpen()) {
                    sx = HibernateBridge.openNewSession();
                }
            }
            Prenotazione prenotazione = (Prenotazione) sx.get(Prenotazione.class, idPrenotazione);
            if (prenotazione.getCliente() != null) {
                n = (Numerazione) sx.get(Numerazione.class, prenotazione.getNumerazione().getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return n;
    }

    public static boolean prenotazionePrepagata(Session sx,Prenotazione p) {
        p = (Prenotazione) sx.get(Prenotazione.class, p.getId());
        Commissione c = (Commissione) sx.get(Commissione.class, p.getCommissione().getId());

        return c.getPrepagato();
    }

    public static boolean prenotazionePrepagata(Prenotazione p) {
        Session sx = null;
        Commissione c = null;
        try {
            p = (Prenotazione) sx.get(Prenotazione.class, p.getId());
            c = (Commissione) sx.get(Commissione.class, p.getCommissione().getId());
        } catch (Exception e) {

        } finally {
            try {
                if (sx != null) {
                    sx.close();
                }
            } catch(Exception exx) {

            }
        }

        return c!=null?c.getPrepagato():false;
        
    }

    /**
     * method to calculate the due date for invoice from reservation
     * @param reservationDate
     * @return
     */
    /*
    public static Date dueDate(Session sx, Prenotazione reservation) {
        if (reservation != null) {
            reservation = (Prenotazione) sx.get(Prenotazione.class, reservation.getId());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(reservation.getInizio());

            //check if reservation come from tour operator or not
            Commissione c = (Commissione) sx.get(Commissione.class, reservation.getCommissione().getId());
            if (c.getFonteCommissione().getCliente() != null) { // tour operator
                calendar.add(Calendar.DAY_OF_MONTH, -30);
            } else { // indiviadual
                calendar.add(Calendar.DAY_OF_MONTH, -45);
            }

            return calendar.getTime();
        }
        return null;
    }
    */
    /**
     * verifica se esiste un contratto legato alla prenotazione
     * @param sx
     * @param p
     * @return contratto di noleggio
     */

    public static MROldContrattoNoleggio contrattoEsistente(Session sx, MROldPrenotazione p) {
        MROldContrattoNoleggio c = null;

        if (p != null && p.getCommissione() != null) {
            MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, p.getCommissione().getId());
            if (commissione.getContratto() != null) {
                c = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, commissione.getContratto().getId());
            }
        }

        return c;
    }
    /**
     * verifica se esiste una prenotazione legata al contratto
     * @param sx
     * @param c
     * @return contratto di noleggio
     */
    public static MROldPrenotazione prenotazioneEsistente(Session sx, MROldContrattoNoleggio c) {
        MROldPrenotazione p = null;
        if (c != null && c.getCommissione() != null) {
            MROldCommissione commissione = (MROldCommissione) sx.get(MROldCommissione.class, c.getCommissione().getId());
            if (commissione.getPrenotazione() != null) {
                p = (MROldPrenotazione) sx.get(MROldPrenotazione.class, commissione.getPrenotazione().getId());
            }
        }
        return p;
    }
}
