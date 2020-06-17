/*
 * ScadenzaUtils.java
 *
 * Created on 24 noiembrie 2005, 13:25
 *
 */

package it.myrent.ee.api.utils;

import it.aessepi.utils.BundleUtils;
import it.myrent.ee.db.MROldDocumentoFiscale;
import it.myrent.ee.db.MROldScadenze;
import it.myrent.ee.db.MROldPagamento;
import it.aessepi.utils.beans.FormattedDate;
import java.awt.Component;
import java.text.DecimalFormat;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Giacomo
 */
public class ScadenzaUtils {

    private ScadenzaUtils() {
    }

    public final static int NORMALE = 0;
    public final static int SOLO_IVA = 1;
    public final static int IVA_SPESE = 2;
    public final static int FINE_MESE = 1;
    public final static int DATA_FATTURA = 0;
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");

    public static void creaScadenze(MROldDocumentoFiscale myFattura){
        /**
         * Variabili
         */
        MROldPagamento myPagamento = myFattura.getPagamento();
        if(myFattura.getScadenze() == null) {
            myFattura.setScadenze(new HashSet());
        } else if(myFattura.getScadenze().size() > 0) {
            myFattura.getScadenze().clear();
        }
        double totIva, totImp, totFatt;
        double resto;
        totIva = myFattura.getTotaleIva().doubleValue();
        totImp = myFattura.getTotaleImponibile().doubleValue();
        totFatt = myFattura.getTotaleFattura().doubleValue();


        int noScadenze = myPagamento.getNumeroRate().intValue();
        int intervallo = myPagamento.getIntervallo().intValue();
        int months = intervallo/30;
        int days = intervallo - months*30;

        int tipoPrimaRata;
        String primaRata = ""; //NOI18N

        if(myPagamento.getPrimaRataIva().equals(Boolean.TRUE)) {
            tipoPrimaRata = SOLO_IVA;
        } else if(myPagamento.getPrimaRataIvaSpese().equals(Boolean.TRUE)) {
            tipoPrimaRata = IVA_SPESE;
        } else {
            tipoPrimaRata = NORMALE;
        }

        boolean isFineMese = myPagamento.getFineMese().booleanValue(), isDataFattura = myPagamento.getDataFattura().booleanValue();

        GregorianCalendar scadenza = new GregorianCalendar();
        scadenza.setTime(myFattura.getData());
        scadenza.add(scadenza.DAY_OF_MONTH,myPagamento.getGiorniPrimaScadenza().intValue());
        /**
         * Mesi di esclusione
         */
        boolean primo = false, secondo = false;
        int mese1=0, gg1=0, mese2=0, gg2=0;
        if(myPagamento.getMeseEsclusione1()!=null && myPagamento.getMeseEsclusione1().intValue() != 0) {
            primo = true;
            mese1 = myPagamento.getMeseEsclusione1().intValue();
            gg1 = myPagamento.getGiornoFisso1().intValue();
        }
        if(myPagamento.getMeseEsclusione2()!=null && myPagamento.getMeseEsclusione2().intValue() != 0) {
            secondo = true;
            mese2 = myPagamento.getMeseEsclusione2().intValue();
            gg2 = myPagamento.getGiornoFisso2().intValue();
        }
        if(primo && secondo && mese1 > mese2) {
            int mesex = mese1;
            mese1 = mese2;
            mese2 = mesex;
            mesex = gg1;
            gg1 = gg2;
            gg2 = mesex;
        }
        /*
         * Calcolo della prima scadenza
         */

        MROldScadenze primaScadenza = new MROldScadenze();
        primaScadenza.setDataFattura(myFattura.getData());
        primaScadenza.setDataPresentazione(null);
        //Backup della prima scadenza per calcolare le altre.
        GregorianCalendar calendarPrimaScadenza;
        if(isFineMese) {
            scadenza = xthFineMese(scadenza, 0);
            calendarPrimaScadenza = new GregorianCalendar(scadenza.get(scadenza.YEAR), scadenza.get(scadenza.MONTH), scadenza.get(scadenza.DAY_OF_MONTH));
            scadenza.add(scadenza.DAY_OF_MONTH, myPagamento.getGiorniDopo().intValue());
        } else {
            calendarPrimaScadenza = new GregorianCalendar(scadenza.get(scadenza.YEAR), scadenza.get(scadenza.MONTH), scadenza.get(scadenza.DAY_OF_MONTH));
        }
        primaScadenza.setDataScadenza(scadenza.getTime());

        if(primo) {
            if((scadenza.get(scadenza.MONTH) + 1) == mese1){
                scadenza = xthFineMese(scadenza,1);
                if(gg1 < scadenza.getActualMaximum(scadenza.DAY_OF_MONTH)) {
                    scadenza.set(scadenza.DAY_OF_MONTH, gg1);
                }
                primaScadenza.setDataScadenza(scadenza.getTime());

            }
        }

        if (secondo) {
            if((scadenza.get(scadenza.MONTH) + 1) == mese2){
                scadenza = xthFineMese(scadenza, 1);
                if(gg2 < scadenza.getActualMaximum(scadenza.DAY_OF_MONTH)) {
                    scadenza.set(scadenza.DAY_OF_MONTH, gg2);
                }
                primaScadenza.setDataScadenza(scadenza.getTime());

            }
        }


        primaScadenza.setDistinta(Boolean.FALSE);
        //FIXME Quando avro' impostato le coordinate bancarie nella fattura.
        //FIXME primaScadenza.setBanca(myFattura.getCliente().getBanca());
        primaScadenza.setCliente(myFattura.getCliente());
        primaScadenza.setPagamento(myPagamento);
        primaScadenza.setImportoFattura(myFattura.getTotaleFattura());
        primaScadenza.setNoFattura(myFattura.getNumero());
        primaScadenza.setSaldo(new Boolean(noScadenze == 1));
        primaScadenza.setTipoCliente(new Integer(1));
        primaScadenza.setTipoScadenza(new Boolean(isDataFattura));

        switch(tipoPrimaRata) {
            case NORMALE: {
                primaScadenza.setImportoScadenza(new Double(totFatt/noScadenze));
                resto = totFatt - totFatt/noScadenze;
            } break;
            case SOLO_IVA: {
                primaScadenza.setImportoScadenza(myFattura.getTotaleIva());
                resto = totFatt - totIva;
            }break;
            default: {
                primaScadenza.setImportoScadenza(new Double(totIva + totImp/noScadenze));
                resto = totFatt - totIva - totImp/noScadenze;
            }
        }
        myFattura.getScadenze().add(primaScadenza);

        for(int iS=1 ; iS < noScadenze; iS++) {
            GregorianCalendar newScadenza;
            months = (intervallo*iS)/30;
            days = intervallo*iS - months*30;

            MROldScadenze tmpScadenza = new MROldScadenze();
            tmpScadenza.setDataFattura(myFattura.getData());
            tmpScadenza.setDataPresentazione(null);
            if(isFineMese){
                newScadenza = xthFineMese(calendarPrimaScadenza, days==0?months:months+1);
                newScadenza.add(newScadenza.DAY_OF_MONTH, myPagamento.getGiorniDopo().intValue());
                tmpScadenza.setDataScadenza(newScadenza.getTime());

            }

            else {
                newScadenza = sameDateXMonths(calendarPrimaScadenza, months);
                newScadenza.add(newScadenza.DAY_OF_MONTH, days);
                tmpScadenza.setDataScadenza(newScadenza.getTime());

            }

            if(primo) {
                if((newScadenza.get(newScadenza.MONTH) + 1) == mese1){
                    newScadenza = xthFineMese(newScadenza,1);
                    if(gg1 < newScadenza.getActualMaximum(newScadenza.DAY_OF_MONTH)) {
                        newScadenza.set(newScadenza.DAY_OF_MONTH, gg1);
                    }
                    tmpScadenza.setDataScadenza(newScadenza.getTime());
                }
            }

            if (secondo) {
                if((newScadenza.get(newScadenza.MONTH) + 1) == mese2){
                    newScadenza = xthFineMese(newScadenza, 1);
                    if(gg2 < newScadenza.getActualMaximum(newScadenza.DAY_OF_MONTH)) {
                        newScadenza.set(newScadenza.DAY_OF_MONTH, gg2);
                    }
                    tmpScadenza.setDataScadenza(newScadenza.getTime());

                }
            }

            tmpScadenza.setDistinta(Boolean.FALSE);
            //FIXME Quando avro' impostato le coordinate bancarie nella fattura.
            //FIXME tmpScadenza.setBanca(myFattura.getCliente().getBanca());
            tmpScadenza.setCliente(myFattura.getCliente());
            tmpScadenza.setPagamento(myPagamento);
            tmpScadenza.setImportoFattura(myFattura.getTotaleFattura());
            tmpScadenza.setNoFattura(myFattura.getNumero());
            tmpScadenza.setSaldo(new Boolean(iS == (noScadenze-1)));
            tmpScadenza.setTipoCliente(new Integer(1));
            tmpScadenza.setTipoScadenza(new Boolean(isDataFattura));
            tmpScadenza.setImportoScadenza(new Double(resto/(noScadenze-1)));
            myFattura.getScadenze().add(tmpScadenza);
        }

    }

    public static int generaScadenze(Component parent,Session sx, Integer idFattura) {
        MROldDocumentoFiscale myFattura = null;
        Session mySession = null;
        Transaction tx = null;
        /**
         * Codice per la generazione delle scadenze di una fattura
         */
        try {
            myFattura = (MROldDocumentoFiscale)mySession.get(MROldDocumentoFiscale.class, idFattura);
            
            if(myFattura.getScadenze() == null) {
                myFattura.setScadenze(new HashSet());
            }

            creaScadenze(myFattura);
            tx = mySession.beginTransaction();
            mySession.saveOrUpdate(myFattura);
            tx.commit();
            mySession.close();

        } catch (Exception ex) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception transExc) {
                }
            }

            ex.printStackTrace();
            return -1;
        }
        return 0;
    }

    public static String creaStringaStampaScadenze(Set scadenze) {
        String stampaScadenze = null;
        DecimalFormat df = new DecimalFormat("\u20ac #,##0.00");         //NOI18N
        if(scadenze != null && scadenze.size() > 0) {
            TreeSet scadenzeOrdinate = new TreeSet(new ScadenzaComparator());
            scadenzeOrdinate.addAll(scadenze);
            stampaScadenze = ""; //NOI18N
            Iterator it = scadenzeOrdinate.iterator();
            while(it.hasNext()) {
                MROldScadenze s  = (MROldScadenze) it.next();
                if(stampaScadenze.equals("")) { //NOI18N
                    stampaScadenze = FormattedDate.format(s.getDataScadenza()) + " " + df.format(s.getImportoScadenza()); //NOI18N
                } else {
                    stampaScadenze = stampaScadenze + "; " + FormattedDate.format(s.getDataScadenza()) + " " + df.format(s.getImportoScadenza()); //NOI18N
                }
            }
        }
        return stampaScadenze;
    }

    public static boolean esisteDistinta(Set scadenze) {
        boolean esisteDistinta = false;
        if(scadenze != null && scadenze.size() > 0) {
            Iterator it = scadenze.iterator();
            while(it.hasNext() && !esisteDistinta) {
                MROldScadenze s  = (MROldScadenze) it.next();
                if(s.getDistinta() != null && s.getDistinta().booleanValue()) {
                    esisteDistinta = true;
                }
            }
        }
        return esisteDistinta;
    }

    public static GregorianCalendar xthFineMese(GregorianCalendar gregorianCalendar, int months) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(gregorianCalendar.getTimeInMillis());
        if(months > 0) {
            for (int i = 0; i < months; i++) {
                calendar.set(calendar.DAY_OF_MONTH,calendar.getActualMaximum(calendar.DAY_OF_MONTH));
                calendar.add(calendar.DAY_OF_MONTH,1);
                calendar.set(calendar.DAY_OF_MONTH,calendar.getActualMaximum(calendar.DAY_OF_MONTH));
            }
        } else if(months < 0) {
            for (int i = months; i < 0; i++) {
                calendar.set(calendar.DAY_OF_MONTH,1);
                calendar.add(calendar.DAY_OF_MONTH,-1);
            }
        } else {
            calendar.set(calendar.DAY_OF_MONTH,calendar.getActualMaximum(calendar.DAY_OF_MONTH));
        }

        return calendar;
    }

    public static GregorianCalendar sameDateXMonths(GregorianCalendar gregorianCalendar, int months) {
        GregorianCalendar myCalendar = new GregorianCalendar();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(gregorianCalendar.getTimeInMillis());
        myCalendar.setTimeInMillis(gregorianCalendar.getTimeInMillis());
        calendar.set(calendar.DAY_OF_MONTH,1);
        calendar.add(calendar.MONTH, months);
        if(myCalendar.get(myCalendar.DAY_OF_MONTH)>calendar.getActualMaximum(calendar.DAY_OF_MONTH)) {
            calendar.set(calendar.DAY_OF_MONTH,calendar.getActualMaximum(calendar.DAY_OF_MONTH));
        } else {
            calendar.set(calendar.DAY_OF_MONTH,myCalendar.get(myCalendar.DAY_OF_MONTH));
        }

        return calendar;
    }
}
