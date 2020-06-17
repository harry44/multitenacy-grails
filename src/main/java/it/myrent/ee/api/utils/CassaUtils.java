package it.myrent.ee.api.utils;

import it.myrent.ee.db.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.Session;

import java.util.*;

/**
 * Created by bharti on 1/23/2018.
 */
public class CassaUtils {

    public static List<RigaRimessa> creaRigheRimessa(Session sx, MROldSede aSede, Date data, boolean soloSaldiDiversiDaZero) {

        if (aSede == null) {
            return new ArrayList<RigaRimessa>();
        }

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(data.getTime());
        calendar.set(calendar.DAY_OF_MONTH, 1);
        calendar.set(calendar.MONTH, calendar.JANUARY);

        List listaDare = calcolaSomma(sx, calendar.getTime(), data, aSede, MROldCausalePrimanota.DARE);
        List listAvere = calcolaSomma(sx, calendar.getTime(), data, aSede, MROldCausalePrimanota.AVERE);

        List<RigaRimessa> righe = new ArrayList<RigaRimessa>();

        Iterator iterator = listaDare.iterator();

        //Dare
        while (iterator.hasNext()) {
            Object[] saldo = (Object[]) iterator.next();
            MROldPianoDeiConti conto = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, (Integer) saldo[0]);
            MROldPagamento pagamento = (MROldPagamento) sx.get(MROldPagamento.class, (Integer) saldo[1]);
            Double importo = (Double) saldo[2];
            Garanzia garanzia = null;
            if (saldo.length == 4 && saldo[3] != null) {
                garanzia = (Garanzia) sx.get(GaranziaImpl.class, (Integer) saldo[3]);
            }

            RigaRimessa riga = new RigaRimessa(pagamento, garanzia, conto, importo, null, null, false, null, null);
            if (conto.getCodiceSottoconto().equals(conto.SOTTOCONTO_CASSA_CAUZIONI)
                    || conto.getCodiceSottoconto().equals(conto.SOTTOCONTO_CASSA_CONTANTI)) {
                riga.setImportoRimessa(0.0);
            } else {
                riga.setImportoRimessa(importo);
            }
            righe.add(riga);
        }

        iterator = listAvere.iterator();

        //Avere
        while (iterator.hasNext()) {
            Object[] saldo = (Object[]) iterator.next();
            MROldPianoDeiConti conto = (MROldPianoDeiConti) sx.get(MROldPianoDeiConti.class, (Integer) saldo[0]);
            MROldPagamento pagamento = (MROldPagamento) sx.get(MROldPagamento.class, (Integer) saldo[1]);
            Double importo = (Double) saldo[2];
            Garanzia garanzia = null;
            if (saldo.length == 4 && saldo[3] != null) {
                garanzia = (Garanzia) sx.get(GaranziaImpl.class, (Integer) saldo[3]);
            }

            RigaRimessa riga = null;
            Iterator<RigaRimessa> it = righe.iterator();
            while (it.hasNext() && riga == null) {
                riga = it.next();
                if (new EqualsBuilder().append(riga.getPagamento(), pagamento).
                        append(riga.getConto(), conto).
                        append(riga.getGaranzia(), garanzia).
                        isEquals()) {
                    riga.setSaldoAttuale(MathUtils.roundDouble(riga.getSaldoAttuale() - importo));
                    if (!conto.getCodiceSottoconto().equals(conto.SOTTOCONTO_CASSA_CAUZIONI)
                            && !conto.getCodiceSottoconto().equals(conto.SOTTOCONTO_CASSA_CONTANTI)) {
                        riga.setImportoRimessa(riga.getSaldoAttuale());
                    }
                } else {
                    riga = null;
                }
            }

            if (riga == null) {
                riga = new RigaRimessa(pagamento, garanzia, conto, importo, null, null, false, null, null);
                if (conto.getCodiceSottoconto().equals(conto.SOTTOCONTO_CASSA_CAUZIONI)
                        || conto.getCodiceSottoconto().equals(conto.SOTTOCONTO_CASSA_CONTANTI)) {
                    riga.setImportoRimessa(0.0);
                } else {
                    riga.setImportoRimessa(importo);
                }
                righe.add(riga);
            }
        }

        if (soloSaldiDiversiDaZero) {
            Iterator<RigaRimessa> it = righe.iterator();
            while (it.hasNext()) {
                RigaRimessa riga = it.next();
                if (riga.getSaldoAttuale().equals(0.0)) {
                    it.remove();
                }
            }
        }
        return righe;
    }

    public static List calcolaSomma(Session sx, Date inizioAnno, Date dataInizio, MROldSede aSede, Boolean segno) {
        List result = sx.createQuery(
                "select r.conto.id, r.pagamento.id, sum(round(r.importo * 100.0)) / 100.0 "
                        + "from RigaPrimanota r "
                        + "left join r.primanota p "
                        + "left join r.conto c "
                        + "where p.sede = :sede "
                        + "and c.codiceMastro = :attivita "
                        + "and c.codiceConto = :cassa "
                        + "and r.primanota.dataRegistrazione >= :inizioAnno "
                        + "and r.primanota.dataRegistrazione <= :dataInizio "
                        + "and r.pagamento.assegno = :false "
                        + "and r.segno = :segno group by r.pagamento.id, r.conto.id").
                setParameter("sede", aSede).
                setParameter("attivita", MROldPianoDeiConti.MASTRO_ATTIVITA).
                setParameter("cassa", MROldPianoDeiConti.CONTO_CASSA).
                setParameter("inizioAnno", inizioAnno). // NOI18N
                setParameter("dataInizio", dataInizio). // NOI18N
                setParameter("segno", segno). // NOI18N
                setParameter("false", false).
                list();
        result.addAll(sx.createQuery(
                "select r.conto.id, r.pagamento.id, sum(round(r.importo * 100.0)) / 100.0, r.garanzia.id "
                        + "from RigaPrimanota r "
                        + "left join r.primanota p "
                        + "left join r.conto c "
                        + "where p.sede = :sede "
                        + "and c.codiceMastro = :attivita "
                        + "and c.codiceConto = :cassa "
                        + "and r.primanota.dataRegistrazione >= :inizioAnno "
                        + "and r.primanota.dataRegistrazione <= :dataInizio "
                        + "and r.pagamento.assegno = :true "
                        + "and r.segno = :segno group by r.pagamento.id, r.conto.id, r.garanzia.id").
                setParameter("sede", aSede).
                setParameter("attivita", MROldPianoDeiConti.MASTRO_ATTIVITA).
                setParameter("cassa", MROldPianoDeiConti.CONTO_CASSA).
                setParameter("inizioAnno", inizioAnno). // NOI18N
                setParameter("dataInizio", dataInizio). // NOI18N
                setParameter("segno", segno). // NOI18N
                setParameter("true", true). // NOI18N
                list());
        return result;

    }
}
