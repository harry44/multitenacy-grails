package it.myrent.ee.api.utils;

import it.myrent.ee.db.MROldNetsOperationCall;
import it.myrent.ee.db.MROldNetsTransazione;
import org.hibernate.Session;

import java.util.ArrayList;

/**
 * Created by Shivangani on 1/12/2018.
 */
public class NetsUtils {
    /**
     *
     * @param transazione
     * @return
     */
    public static double getTransazioneTotalCredit(Session sx, MROldNetsTransazione transazione) {
        double credit = 0.00;

        //Session sx = null;
        try {
            //sx = HibernateBridge.startNewSession();

            String hqlOperazioni = ""
                    + "SELECT op "
                    + "FROM MROldNetsOperationCall op "
                    + "WHERE id_transazione = :paramID "
                    + "AND error is null "
                    + "AND type IN ('CREDIT', 'CREDIT_WAIT') ";

            ArrayList<MROldNetsOperationCall> chiamate = (ArrayList<MROldNetsOperationCall>) sx.createQuery(hqlOperazioni)
                    .setParameter("paramID", transazione.getId())
                    .list();

            if(chiamate != null && !chiamate.isEmpty()) {
                for(MROldNetsOperationCall call : chiamate) {
                    credit += call.getAmount();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return credit;
    }

    /**
     *
     * @param transazione
     * @return
     */
    public static double getTransazioneTotalCapture(Session sx, MROldNetsTransazione transazione) {
        double capture = 0.00;

        //Session sx = null;
        try {
            //sx = HibernateBridge.startNewSession();

            String hqlOperazioni = ""
                    + "SELECT op "
                    + "FROM MROldNetsOperationCall op "
                    + "WHERE id_transazione = :paramID "
                    + "AND error is null "
                    + "AND type IN ('CAPTURE', 'CAPTURE_WAIT', 'SALE') ";

            ArrayList<MROldNetsOperationCall> chiamate = (ArrayList<MROldNetsOperationCall>) sx.createQuery(hqlOperazioni)
                    .setParameter("paramID", transazione.getId())
                    .list();

            if(chiamate != null && !chiamate.isEmpty()) {
                for(MROldNetsOperationCall call : chiamate) {
                    capture += call.getAmount();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return capture;
    }
}
