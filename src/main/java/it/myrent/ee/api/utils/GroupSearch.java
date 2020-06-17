package it.myrent.ee.api.utils;

import it.myrent.ee.db.MROldFonteCommissione;
import it.myrent.ee.db.MROldGruppo;
import it.myrent.ee.db.MROldSede;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by shivangani on 30/08/2019.
 */
public class GroupSearch {

    /**
     *
     * @param sx
     * @param intlCode
     * @return
     */
    public static MROldGruppo byIntlCode(Session sx, String intlCode) {
        if (intlCode == null) {
            return null;
        }
        return (MROldGruppo) sx.createQuery("select g from MROldGruppo g where lower(g.codiceInternazionale) = :intlCode").
                setParameter("intlCode", intlCode.toLowerCase()).
                setMaxResults(1).
                uniqueResult();
    }

    /**
     *
     * @param sx
     * @param natlCode
     * @return
     */
    public static MROldGruppo byNatlCode(Session sx, String natlCode) {
        if (natlCode == null) {
            return null;
        }
        return (MROldGruppo) sx.createQuery("select g from MROldGruppo g where lower(g.codiceNazionale) = :natlCode").
                setParameter("natlCode", natlCode.toLowerCase()).
                setMaxResults(1).
                uniqueResult();
    }

    /**
     *
     * @param sx
     * @param fonte
     * @return
     */
    public static List<MROldGruppo> byRateAndRentalDays(Session sx, MROldFonteCommissione fonte) {
//        return sx.createQuery(
//                "select g from MROldGruppo g where g.id in " +
//                "( " +
//                "select distinct t1.gruppo.id " +
//                "from TariffaListino t1 " +
//                "left join t1.durata d1 " +
//                "left join d1.listino l1 "
//                "where d1.listino = :listino " +
//                "and d1.minimo <= :rentalDays " +
//                ") " +
//                "order by g.codiceNazionale").
//                setParameter("listino", listino).
//                setParameter("rentalDays", rentalDays).
//                list();


        return sx.createQuery(
                "select g from MROldGruppo g where g.id in ( "
                        + "select t.gruppo.id from MROldFonteCommissione f "
                        + "left join f.listini v "
                        + "left join v.listino l "
                        + "left join l.durate d "
                        + "left join d.tariffe t "
                        + "where f = :fonte) "
                        + "order by g.codiceNazionale").
                setParameter("fonte", fonte).
                list();
    }

    /**
     *
     * @param sx
     * @param fonte
     * @param sedeUscita
     * @param gruppoSpeciale
     * @return
     */
    public static List<MROldGruppo> byRateAndRentalDays(Session sx, MROldFonteCommissione fonte, MROldSede sedeUscita, boolean gruppoSpeciale) {
        Query qx = sx.createQuery(
                "select g from MROldGruppo g where g.id in " +
                        "( " +
                        "select g1 from MROldDisponibilitaVeicolo d1 " +
                        "left join d1.veicolo v1 " +
                        "left join v1.gruppo g1 " +
                        "left join d1.sede s1 " +
                        "where " +
                        (gruppoSpeciale ? "s1.regione is not null and s1.regione.id = :idRegione " : "d.sede.id = :idSede ") +
                        "and v1.abilitato = :true " +
                        ") " +
                        "order by g.codiceNazionale ")
                .setParameter("true", Boolean.TRUE);

        if (gruppoSpeciale) {
            qx.setParameter("idRegione", sedeUscita.getRegione().getId());
        } else {
            qx.setParameter("idSede", sedeUscita.getId());
        }


        List<MROldGruppo> lista = qx.list();
        if (lista != null && !lista.isEmpty()) {
            Iterator<MROldGruppo> it = lista.iterator();
            while (it.hasNext()) {
                MROldGruppo g = it.next();
                System.out.println("byRateAndRentalDays gruppo: " + g.getDescrizione());
            }
        }

        return lista;
    }

    /**
     *
     * @param sx
     * @param gruppiRichiesti
     * @param pickupLocation
     * @param inizio
     * @param fine
     * @param onRequest
     * @return
     */
    public static List<MROldGruppo> byAvailability(Session sx, List<MROldGruppo> gruppiRichiesti, MROldSede pickupLocation, Date inizio, Date fine, boolean onRequest) {
        Query qx = sx.createQuery(
                "select g from MROldGruppo g where g.id in " +
                        "( " +
                        "select g1 from DisponibilitaVeicolo d1 " +
                        "left join d1.veicolo v1 " +
                        "left join v1.gruppo g1 " +
                        "left join d1.sede s1 " +
                        "where g1 in (:gruppi) " +
                        "and " +
                        //(onRequest ? "(s1.id <> :idSede and g1.speciale = :true and s1.regione.id = :idRegione)" : "(s1.id = :idSede)") +
                        (onRequest ? "(g1.speciale = :true and s1.regione.id = :idRegione)" : "(s1.id = :idSede)") +
                        "and d1.inizio <= :inizio " +
                        "and d1.fine >= :fine " +
                        "and v1.abilitato = :true " +
                        "and g1 in (:gruppi) " +
                        ") " +
                        "order by g.codiceNazionale ").

                setParameter("inizio", inizio).
                setParameter("fine", fine).
                setParameter("true", true).
                setParameterList("gruppi", gruppiRichiesti);

        if (onRequest) {
            qx.setParameter("idRegione", pickupLocation.getRegione() != null ? pickupLocation.getRegione().getId() : null);
        } else {
            qx.setParameter("idSede", pickupLocation.getId());
        }


        List<MROldGruppo> lista = qx.list();
        if (lista != null && !lista.isEmpty()) {
            Iterator<MROldGruppo> it = lista.iterator();
            while (it.hasNext()) {
                MROldGruppo g = it.next();
                System.out.println("byAvailability gruppo: " + g.getDescrizione());
            }
        }

        return lista;
    }
}
