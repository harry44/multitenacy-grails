/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.aessepi.myrentcs.utils;

import it.myrent.ee.db.*;
import java.util.Date;
import org.hibernate.Session;

/**
 *
 * @author bogdan
 */
public interface FatturazioneFactory {

    /**
     * Cerca un metodo di fatturazione per calcolare il dettaglio della prossima fattura in base agli argomenti passati.
     * @param tariffa La tariffa del noleggio.
     * @param inizio La data di inizio del periodo di fatturazione.
     * @param fine La data di fine del periodo di fatturazione.
     * @param giorniVoucher Eventuali giorni prepagati.
     * @return Il metodo di fatturazione richiesto.
     */
    public Fatturazione getFatturazione(MROldTariffa tariffa, Date inizio, Date fine, Integer giorniVoucher);

    public Fatturazione getFatturazione(Session sx, MROldTariffa tariffa, Date inizio, Date fine, Integer giorniVoucher, Double scontoPercentuale);

    /**
     * Cerca un metodo di fatturazione per calcolare il dettaglio della prossima fattura in base agli argomenti passati.
     * @param sx
     * @param tariffa La tariffa del noleggio.
     * @param inizio La data di inizio del periodo di fatturazione.
     * @param fine La data di fine del periodo di fatturazione.
     * @param giorniVoucher Eventuali giorni prepagati.
     * @return Il metodo di fatturazione richiesto.
     */
    public Fatturazione getFatturazione(Session sx, MROldTariffa tariffa, Date inizio, Date fine, Integer giorniVoucher);

    /**
     * Cerca un metodo di fatturazione per calcolare il dettaglio della prossima fattura in base agli argomenti passati.
     * @param contrattoNoleggio Il contratto di noleggio.
     * @param tariffa La tariffa del noleggio.
     * @param inizio La data di inizio del periodo di fatturazione.
     * @param fine La data di fine del periodo di fatturazione.     
     * @param scontoPercentuale Lo sconto percentuale da applicare alla tariffa.
     * @param giorniVoucher Eventuali giorni prepagati.
     * @return Il metodo di fatturazione richiesto.
     */
    public Fatturazione getFatturazione(MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Double scontoPercentuale, Integer giorniVoucher);

    /**
     * Cerca un metodo di fatturazione per calcolare il dettaglio della prossima fattura in base agli argomenti passati.
     * @param sx
     * @param contrattoNoleggio Il contratto di noleggio.
     * @param tariffa La tariffa del noleggio.
     * @param inizio La data di inizio del periodo di fatturazione.
     * @param fine La data di fine del periodo di fatturazione.
     * @param scontoPercentuale Lo sconto percentuale da applicare alla tariffa.
     * @param giorniVoucher Eventuali giorni prepagati.
     * @return Il metodo di fatturazione richiesto.
     */
    public Fatturazione getFatturazione(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Double scontoPercentuale, Integer giorniVoucher);

    /**
     * Cerca un metodo di fatturazione per calcolare il dettaglio della prossima fattura in base agli argomenti passati.
     * @param sx
     * @param prenotazione la prenotazione.
     * @param tariffa La tariffa del noleggio.
     * @param inizio La data di inizio del periodo di fatturazione.
     * @param fine La data di fine del periodo di fatturazione.
     * @param scontoPercentuale Lo sconto percentuale da applicare alla tariffa.
     * @param giorniVoucher Eventuali giorni prepagati.
     * @return Il metodo di fatturazione richiesto.
     */
    public Fatturazione getFatturazione(Session sx, MROldPrenotazione prenotazione, MROldTariffa tariffa, Date inizio, Date fine, Double scontoPercentuale, Integer giorniVoucher);

    /**
     * Cerca un metodo di fatturazione per calcolare il dettaglio della prossima fattura in base agli argomenti passati.
     * @param contrattoNoleggio Il contratto di noleggio.
     * @param tariffa La tariffa del noleggio.
     * @param inizio La data di inizio del periodo di fatturazione.
     * @param fine La data di fine del periodo di fatturazione.
     * @param km I km totali percorsi.
     * @param litri I litri di carburante mancanti.
     * @param carburante Il tipo di carburante
     * @param scontoPercentuale Lo sconto percentuale da applicare alla tariffa.
     * @param giorniVoucher Eventuali giorni prepagati.
     * @return Il metodo di fatturazione richiesto.
     */
    public Fatturazione getFatturazione(MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher);


    //public Fatturazione getFatturazione(Preventivo preventivo, Tariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, Carburante carburante, Double scontoPercentuale, Integer giorniVoucher);


    /**
     * Cerca un metodo di fatturazione per calcolare il dettaglio della prossima fattura in base agli argomenti passati.
     * @param sx
     * @param contrattoNoleggio Il contratto di noleggio.
     * @param tariffa La tariffa del noleggio.
     * @param inizio La data di inizio del periodo di fatturazione.
     * @param fine La data di fine del periodo di fatturazione.
     * @param km I km totali percorsi.
     * @param litri I litri di carburante mancanti.
     * @param carburante Il tipo di carburante
     * @param scontoPercentuale Lo sconto percentuale da applicare alla tariffa.
     * @param giorniVoucher Eventuali giorni prepagati.
     * @return Il metodo di fatturazione richiesto.
     */
    public Fatturazione getFatturazione(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher);

    /**
     * Cerca un metodo di fatturazione per calcolare il dettaglio della prossima fattura in base agli argomenti passati.
     * @param sx
     * @param prenotazione la prenotazioine
     * @param tariffa La tariffa del noleggio.
     * @param inizio La data di inizio del periodo di fatturazione.
     * @param fine La data di fine del periodo di fatturazione.
     * @param km I km totali percorsi.
     * @param litri I litri di carburante mancanti.
     * @param carburante Il tipo di carburante
     * @param scontoPercentuale Lo sconto percentuale da applicare alla tariffa.
     * @param giorniVoucher Eventuali giorni prepagati.
     * @return Il metodo di fatturazione richiesto.
     */
    public Fatturazione getFatturazione(Session sx, MROldPrenotazione prenotazione, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher);

    /**
     * Cerca un metodo di fatturazione per calcolare il dettaglio della prossima fattura in base agli argomenti passati.
     * @param sx
     * @param preventivo il preventivo
     * @param tariffa La tariffa del noleggio.
     * @param inizio La data di inizio del periodo di fatturazione.
     * @param fine La data di fine del periodo di fatturazione.
     * @param km I km totali percorsi.
     * @param litri I litri di carburante mancanti.
     * @param carburante Il tipo di carburante
     * @param scontoPercentuale Lo sconto percentuale da applicare alla tariffa.
     * @param giorniVoucher Eventuali giorni prepagati.
     * @return Il metodo di fatturazione richiesto.
     */
    public Fatturazione getFatturazione(Session sx, MROldPreventivo preventivo, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher);

    public static final FatturazioneFactory PREPAGATO_FACTORY = new FatturazioneFactory() {

        public Fatturazione getFatturazione(MROldTariffa tariffa, Date inizio, Date fine, Integer giorniVoucher) {
            return new FatturazionePrepagato(tariffa, inizio, fine, null, null, null, null, giorniVoucher);
        }

        public Fatturazione getFatturazione(MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Double scontoPercentuale, Integer giorniVoucher) {
            return new FatturazionePrepagato(tariffa, inizio, fine, null, null, null, null, giorniVoucher);
        }

        public Fatturazione getFatturazione(MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
            return new FatturazionePrepagato(tariffa, inizio, fine, null, null, null, null, giorniVoucher);
        }


        public Fatturazione getFatturazione(Session sx, MROldTariffa tariffa, Date inizio, Date fine, Integer giorniVoucher) {
            return new FatturazionePrepagato(tariffa, inizio, fine, null, null, null, null, giorniVoucher);
        }

        public Fatturazione getFatturazione(Session sx, MROldTariffa tariffa, Date inizio, Date fine, Integer giorniVoucher, Double scontoExtraPrepay) {
            return new FatturazionePrepagato(tariffa, inizio, fine, null, null, null, scontoExtraPrepay, giorniVoucher);
        }
        public Fatturazione getFatturazione(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Double scontoPercentuale, Integer giorniVoucher) {
            return new FatturazionePrepagato(contrattoNoleggio, tariffa, inizio, fine, null, null, null, null, giorniVoucher);
        }

        public Fatturazione getFatturazione(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
            return new FatturazionePrepagato(contrattoNoleggio, tariffa, inizio, fine, null, null, null, null, giorniVoucher);
        }


        public Fatturazione getFatturazione(Session sx, MROldPrenotazione prenotazione, MROldTariffa tariffa, Date inizio, Date fine, Double scontoPercentuale, Integer giorniVoucher) {
            //return new FatturazionePrepagato(tariffa, inizio, fine, null, null, null, null, giorniVoucher);
            return new FatturazionePrepagato(prenotazione,tariffa, inizio, fine, null, null, null, null, giorniVoucher);
        }


        public Fatturazione getFatturazione(Session sx, MROldPrenotazione prenotazione, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
            return new FatturazionePrepagato(tariffa, inizio, fine, null, null, null, null, giorniVoucher);
        }


        public Fatturazione getFatturazione(Session sx, MROldPreventivo preventivo, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
            return new FatturazionePrepagato(tariffa, inizio, fine, null, null, null, null, giorniVoucher);
        }
    };


    public static final FatturazioneFactory DEFAULT_FACTORY = new FatturazioneFactory() {


        public Fatturazione getFatturazione(MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
            if (FatturazioneBreveTermine.SPECIFICATION.isSatisfiedBy(inizio, fine, giorniVoucher)) {
                return new FatturazioneBreveTermine(contrattoNoleggio, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
            } else if (FatturazionePlurimensile.SPECIFICATION.isSatisfiedBy(inizio, fine, giorniVoucher)) {
                return new FatturazionePlurimensile(contrattoNoleggio, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
            }
            return null;
        }


        public Fatturazione getFatturazione(MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Double scontoPercentuale, Integer giorniVoucher) {
            if (FatturazioneBreveTermine.SPECIFICATION.isSatisfiedBy(inizio, fine, giorniVoucher)) {
                return new FatturazioneBreveTermine(contrattoNoleggio, tariffa, inizio, fine, null, null, null, scontoPercentuale, giorniVoucher);
            } else if (FatturazionePlurimensile.SPECIFICATION.isSatisfiedBy(inizio, fine, giorniVoucher)) {
                return new FatturazionePlurimensile(contrattoNoleggio, tariffa, inizio, fine, null, null, null, scontoPercentuale, giorniVoucher);
            }
            return null;
        }


        public Fatturazione getFatturazione(MROldTariffa tariffa, Date inizio, Date fine, Integer giorniVoucher) {
            MROldContrattoNoleggio c = null;
            if (FatturazioneBreveTermine.SPECIFICATION.isSatisfiedBy(inizio, fine, giorniVoucher)) {
                return new FatturazioneBreveTermine(c, tariffa, inizio, fine, null, null, null, null, giorniVoucher);
            } else if (FatturazionePlurimensile.SPECIFICATION.isSatisfiedBy(inizio, fine, giorniVoucher)) {
                return new FatturazionePlurimensile(null, tariffa, inizio, fine, null, null, null, null, giorniVoucher);
            }
            return null;
        }


        public Fatturazione getFatturazione(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
            if (FatturazioneBreveTermine.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazioneBreveTermine(contrattoNoleggio, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
            } else if (FatturazionePlurimensile.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazionePlurimensile(contrattoNoleggio, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
            }
            return null;
        }


        public Fatturazione getFatturazione(Session sx, MROldContrattoNoleggio contrattoNoleggio, MROldTariffa tariffa, Date inizio, Date fine, Double scontoPercentuale, Integer giorniVoucher) {
            if (FatturazioneBreveTermine.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazioneBreveTermine(contrattoNoleggio, tariffa, inizio, fine, null, null, null, scontoPercentuale, giorniVoucher);
            } else if (FatturazionePlurimensile.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazionePlurimensile(contrattoNoleggio, tariffa, inizio, fine, null, null, null, scontoPercentuale, giorniVoucher);
            }
            return null;
        }


        public Fatturazione getFatturazione(Session sx, MROldTariffa tariffa, Date inizio, Date fine, Integer giorniVoucher) {
            MROldContrattoNoleggio c = null;
            if (FatturazioneBreveTermine.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazioneBreveTermine(c, tariffa, inizio, fine, null, null, null, null, giorniVoucher);
            } else if (FatturazionePlurimensile.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazionePlurimensile(null, tariffa, inizio, fine, null, null, null, null, giorniVoucher);
            }
            return null;
        }


        public Fatturazione getFatturazione(Session sx, MROldPrenotazione prenotazione, MROldTariffa tariffa, Date inizio, Date fine, Double scontoPercentuale, Integer giorniVoucher) {
            /*
             * Andrea Mauro
             */
            if (FatturazioneBreveTermine.SPECIFICATION.isSatisfiedBy(sx,inizio, fine, giorniVoucher)) {
                return new FatturazioneBreveTermine(sx,prenotazione, tariffa, inizio, fine, null, null, null, scontoPercentuale, giorniVoucher);
            } else if (FatturazionePlurimensile.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazionePlurimensile(null, tariffa, inizio, fine, null, null, null, null, giorniVoucher);
            }
            return null;
        }

        public Fatturazione getFatturazione(Session sx, MROldPrenotazione prenotazione, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
            /*
             * Andrea
             */
            if (FatturazioneBreveTermine.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazioneBreveTermine(prenotazione, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
            }
            else if (FatturazionePlurimensile.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazionePlurimensile(null, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
            }
            return null;
        }


        public Fatturazione getFatturazione(Session sx, MROldPreventivo preventivo, MROldTariffa tariffa, Date inizio, Date fine, Integer km, Integer litri, MROldCarburante carburante, Double scontoPercentuale, Integer giorniVoucher) {
            if (FatturazioneBreveTermine.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazioneBreveTermine(preventivo, tariffa, inizio, fine, km, litri, carburante, scontoPercentuale, giorniVoucher);
            }else if (FatturazionePlurimensile.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazionePlurimensile(null, tariffa, inizio, fine, null, null, null, null, giorniVoucher);
            }
            return null;
        }

        public Fatturazione getFatturazione(Session sx, MROldTariffa tariffa, Date inizio, Date fine, Integer giorniVoucher, Double scontoPercentuale) {
            MROldContrattoNoleggio c = null;
            if (FatturazioneBreveTermine.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazioneBreveTermine(c, tariffa, inizio, fine, null, null, null, null, giorniVoucher);
            } else if (FatturazionePlurimensile.SPECIFICATION.isSatisfiedBy(sx, inizio, fine, giorniVoucher)) {
                return new FatturazionePlurimensile(null, tariffa, inizio, fine, null, null, null, null, giorniVoucher);
            }
            return null;
        }
    };
}
