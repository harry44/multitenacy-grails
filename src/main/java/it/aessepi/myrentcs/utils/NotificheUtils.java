/*
 * NotificheUtils.java
 *
 * Created on 16 martie 2007, 15:54
 *
 */
package it.aessepi.myrentcs.utils;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.Parameters;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.db.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;
import it.myrent.ee.db.Rent2Rent;
import org.hibernate.criterion.Restrictions;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 *
 * @author jamess
 */
public class NotificheUtils {

    /** Creates a new instance of NotificheUtils */
    public NotificheUtils() {
    }
    public static final Log log = LogFactory.getLog(NotificheUtils.class);
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");

    public static Integer scadenzeAssicurazioniCount(Integer giorni,Session sx) {
        //Session sx = null;
        Integer retValue = null;
        Date dataAvvisoScadenza = FormattedDate.add(FormattedDate.formattedDate(), Calendar.DAY_OF_MONTH, giorni.intValue());
        try {
            //sx = HibernateBridge.startNewSession();

            User aUser = (User) sx.get(UserImpl.class, Parameters.getUser().getId());

            MROldSede sede = null;
            List sedi = null;

            if (aUser.isOperatore() || aUser.isCapoStazione()) {
                sede = aUser.getSedeOperativa();
            } else if (aUser.isCapoArea()) {
                sedi = aUser.getSediGestite();
            }

            Query queryAssicurazioni;
            StringBuffer queryString = new StringBuffer();
            queryString.append(" select count(x) from Assicurazione x left join x.veicolo where " + //NOI18N
                    "x.veicolo.affiliato.id = :idAffiliato and " + //NOI18N
                    "x.veicolo.abilitato = :abilitato and " + //NOI18N
                    "x.dataInizio = (select max(xx.dataInizio) from Assicurazione xx where xx.veicolo.id = x.veicolo.id) and " + //NOI18N
                    "x.dataProssimaScadenza <= :dataAvvisoScadenza"); //NOI18N
            if (sede != null) {
                queryString.append(" and x.veicolo.sede.id = :idSede"); //NOI18N
                queryAssicurazioni = sx.createQuery(queryString.toString());
                queryAssicurazioni.setParameter("idSede", sede.getId()); //NOI18N
            } else if (sedi != null) {
                queryString.append(" and x.veicolo.sede in ( :sedi )"); //NOI18N
                queryAssicurazioni = sx.createQuery(queryString.toString());
                queryAssicurazioni.setParameterList("sedi", sedi); //NOI18N
            } else {
                queryAssicurazioni = sx.createQuery(queryString.toString());
            }

            queryAssicurazioni.setParameter("idAffiliato", aUser.getAffiliato().getId()); //NOI18N
            queryAssicurazioni.setParameter("abilitato", Boolean.TRUE); //NOI18N
            queryAssicurazioni.setParameter("dataAvvisoScadenza", dataAvvisoScadenza); //NOI18N
            Number count = (Number) queryAssicurazioni.uniqueResult();
            retValue = new Integer(count.intValue());            
        } catch (Exception ex) {
            log.error("NotificheUtils.scadenzeAssicurazioniCount: " + ex.getMessage()); //NOI18N
            log.debug("NotificheUtils.scadenzeAssicurazioniCount: ", ex); //NOI18N
        } finally {
            if(sx != null) {
                try {
                    sx.close();
                } catch (Exception sxx) {
                }
            }
        }
        return retValue;
    }

    public static List scadenzeAssicurazioniList(Integer userId,Integer giorni,Session sx) {
        //Session sx = null;
        List retValue = null;
        Date dataAvvisoScadenza = FormattedDate.add(FormattedDate.formattedDate(), Calendar.DAY_OF_MONTH, giorni.intValue());
        try {
            //sx = HibernateBridge.startNewSession();

            User aUser = (User) sx.get(UserImpl.class, userId);
            MROldSede sede = null;
            List sedi = null;

            if (aUser.isOperatore() || aUser.isCapoStazione()) {
                sede = aUser.getSedeOperativa();
               // System.out.println("location Id"+sede.getId());
            } else if (aUser.isCapoArea()) {
                sedi = aUser.getSediGestite();
            }

            Query queryAssicurazioni;
            StringBuffer queryString = new StringBuffer(
                    " select x from Assicurazione x left join fetch x.veicolo where " + //NOI18N
                    "x.veicolo.affiliato.id = :idAffiliato and " + //NOI18N
                    "x.veicolo.abilitato = :abilitato and " + //NOI18N
                    "x.dataInizio = (select max(xx.dataInizio) from Assicurazione xx where xx.veicolo.id = x.veicolo.id) and " + //NOI18N
                    "x.dataProssimaScadenza <= :dataAvvisoScadenza"); //NOI18N
            if (sede != null) {
                queryString.append(" and x.veicolo.sede.id = :idSede order by x.dataProssimaScadenza asc"); //NOI18N
                queryAssicurazioni = sx.createQuery(queryString.toString());
                queryAssicurazioni.setParameter("idSede", sede.getId()); //NOI18N
            } else if (sedi != null) {
                queryString.append(" and x.veicolo.sede in ( :sedi ) order by x.dataProssimaScadenza asc"); //NOI18N
                queryAssicurazioni = sx.createQuery(queryString.toString());
                queryAssicurazioni.setParameterList("sedi", sedi); //NOI18N
            } else {
                queryString.append(" order by x.dataProssimaScadenza asc"); //NOI18N
                queryAssicurazioni = sx.createQuery(queryString.toString());
            }

            queryAssicurazioni.setParameter("idAffiliato", aUser.getAffiliato().getId()); //NOI18N
            queryAssicurazioni.setParameter("abilitato", Boolean.TRUE); //NOI18N
            queryAssicurazioni.setParameter("dataAvvisoScadenza", dataAvvisoScadenza); //NOI18N
            retValue = queryAssicurazioni.list();
        } catch (Exception ex) {
            log.error("NotificheUtils.scadenzeAssicurazioniList: " + ex.getMessage()); //NOI18N
            log.debug("NotificheUtils.scadenzeAssicurazioniList: ", ex); //NOI18N
        } finally {
//            if (sx != null) {
//                try {
//                    sx.close();
//                } catch (Exception sxx) {
//                }
//            }
        }
        return retValue;
    }

    public static Integer scadenzeContrattiVeicoliCount(Session sx) {
        //Session sx = null;
        Integer retValue = null;
        Integer giorni = null;
        MROldSede sede = null;
        List sedi = null;

        try {
            //sx = HibernateBridge.startNewSession();

            User aUser = (User) sx.get(UserImpl.class, Parameters.getUser().getId());

            if (aUser.isOperatore() || aUser.isCapoStazione()) {
                sede = aUser.getSedeOperativa();
            } else if (aUser.isCapoArea()) {
                sedi = aUser.getSediGestite();
            }

            giorni = Preferenze.getPreferenze().getGiorniAvvisoScadenzaContrattoVeicolo(sx);

            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(FormattedDate.getTodaysTime());
            calendar.add(calendar.DAY_OF_YEAR, giorni.intValue());

            Query queryVeicoli;
            StringBuffer queryString = new StringBuffer(
                    "select count(distinct v) from MROldDisponibilitaVeicolo d " + //NOI18N
                    "left join d.veicolo v " + //NOI18N
                    "left join v.sede " + //NOI18N
                    "left join v.affiliato where " + //NOI18N
                    "v.affiliato.id = :idAffiliato and " + //NOI18N
                    "v.abilitato = :true and " + //NOI18N
                    "d.uscitaParco = :true and " + //NOI18N
                    "d.fine <= :dataAvvisoContratto"); //NOI18N

            if (sede != null) {
                queryString.append(" and v.sede.id = :idSede"); //NOI18N
                queryVeicoli = sx.createQuery(queryString.toString());
                queryVeicoli.setParameter("idSede", sede.getId()); //NOI18N
            } else if (sedi != null) {
                queryString.append(" and v.sede in ( :sedi )"); //NOI18N
                queryVeicoli = sx.createQuery(queryString.toString());
                queryVeicoli.setParameterList("sedi", sedi); //NOI18N
            } else {
                queryVeicoli = sx.createQuery(queryString.toString());
            }

            queryVeicoli.setParameter("idAffiliato", aUser.getAffiliato().getId()); //NOI18N
            queryVeicoli.setParameter("true", Boolean.TRUE); //NOI18N
            queryVeicoli.setParameter("dataAvvisoContratto", calendar.getTime()); //NOI18N
            Number count = (Number) queryVeicoli.uniqueResult();
            retValue = new Integer(count.intValue());
            sx.close();
        } catch (Exception ex) {
            if (sx != null) {
                try {
                    sx.close();
                } catch (Exception sxx) {
                }
            }
            log.error("NotificheUtils.scadenzeContrattiVeicoliCount: " + ex.getMessage()); //NOI18N
            log.debug("NotificheUtils.scadenzeContrattiVeicoliCount: ", ex); //NOI18N
        }
        return retValue;
    }

    public static List scadenzeContrattiVeicoliList(MROldSede sede,Session sx) {
        //Session sx = null;
        List retValue = null;
        Integer giorni = null;        
        List sedi = null;

        try {
            //sx = HibernateBridge.startNewSession();

            User aUser = (User) sx.get(UserImpl.class, Parameters.getUser().getId());
            if (sede == null) {
                if (aUser.isOperatore() || aUser.isCapoStazione()) {
                    sede = aUser.getSedeOperativa();
                } else if (aUser.isCapoArea()) {
                    sedi = aUser.getSediGestite();
                }
            }

            giorni = Preferenze.getGiorniAvvisoScadenzaContrattoVeicolo(sx);

            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(FormattedDate.getTodaysTime());
            calendar.add(calendar.DAY_OF_YEAR, giorni.intValue());

            Query queryVeicoli;

            StringBuffer queryString = new StringBuffer(
                    "select distinct v from MROldDisponibilitaVeicolo d " + //NOI18N
                    "left join d.veicolo v " + //NOI18N
                    "left join fetch v.gruppo " + //NOI18N
                    "left join fetch v.sede " + //NOI18N
                    "left join fetch v.affiliato " + //NOI18N
                    "left join fetch v.proprietario where " + //NOI18N
                    "v.affiliato.id = :idAffiliato and " + //NOI18N
                    "v.abilitato = :true and " + //NOI18N
                    "d.uscitaParco = :true and " + //NOI18N
                    "d.fine <= :dataAvvisoContratto"); //NOI18N
            if (sede != null) {
                queryString.append(" and v.sede.id = :idSede order by v.dataScadenzaContratto asc"); //NOI18N
                queryVeicoli = sx.createQuery(queryString.toString());
                queryVeicoli.setParameter("idSede", sede.getId()); //NOI18N
            } else if (sedi != null) {
                queryString.append(" and v.sede in ( :sedi ) order by v.dataScadenzaContratto asc"); //NOI18N
                queryVeicoli = sx.createQuery(queryString.toString());
                queryVeicoli.setParameterList("sedi", sedi); //NOI18N
            } else {
                queryString.append(" order by v.dataScadenzaContratto asc"); //NOI18N
                queryVeicoli = sx.createQuery(queryString.toString());
            }

            queryVeicoli.setParameter("idAffiliato", aUser.getAffiliato().getId());             //NOI18N
            queryVeicoli.setParameter("dataAvvisoContratto", calendar.getTime()); //NOI18N
            queryVeicoli.setParameter("true", Boolean.TRUE); //NOI18N
            retValue = queryVeicoli.list();
            sx.close();
        } catch (Exception ex) {
            if (sx != null) {
                try {
                    sx.close();
                } catch (Exception sxx) {
                }
            }
            log.error("NotificheUtils.scadenzeContrattiVeicoliList: " + ex.getMessage()); //NOI18N
            log.debug("NotificheUtils.scadenzeContrattiVeicoliList: ", ex); //NOI18N
        }
        return retValue;
    }

    /*public static boolean isVeicoloNelParcoPrimaDel(MROldParcoVeicoli veicolo, Date fine) {
        return veicolo != null &&
                veicolo.getDataScadenzaContratto() != null &&
                veicolo.getDataScadenzaContratto().getTime() >= FormattedDate.formattedDate(fine.getTime()).getTime();
    }*/

    public static boolean isVeicoloInProroga1PrimaDel(MROldParcoVeicoli veicolo, Date fine) {
        return veicolo != null &&
                veicolo.getDataProroga1() != null &&
                veicolo.getDataProroga1().getTime() >= FormattedDate.formattedDate(fine.getTime()).getTime();
    }

    public static boolean isVeicoloInProroga2PrimaDel(MROldParcoVeicoli veicolo, Date fine) {
        return veicolo != null &&
                veicolo.getDataProroga2() != null &&
                veicolo.getDataProroga2().getTime() >= FormattedDate.formattedDate(fine.getTime()).getTime();
    }

    public static boolean isVeicoloNelParcoPrimaDel(MROldParcoVeicoli veicolo, Date fine) {
        return veicolo != null &&
                veicolo.getDataScadenzaContratto() != null &&
                veicolo.getDataScadenzaContratto().getTime() >= FormattedDate.formattedDate(fine.getTime()).getTime();
    }

    public static boolean isVeicoloNelParcoPrimaDel( Boolean flag, MROldParcoVeicoli veicolo, Date fine) {
        boolean valid = isVeicoloNelParcoPrimaDel(veicolo, fine);
        if (!valid) {
            valid = isVeicoloInProroga1PrimaDel(veicolo, fine);
        }
        if (!valid) {
            valid = isVeicoloInProroga2PrimaDel(veicolo, fine);
        }
        return valid;
    }

    public static boolean isVeicoloNelParcoDopoIl(MROldParcoVeicoli veicolo, Date inizio) {
        return veicolo != null &&
                (veicolo.getDataAcquisto() == null ||
                veicolo.getDataAcquisto().getTime() <= FormattedDate.formattedDate(inizio.getTime()).getTime());
    }

    public static boolean isVeicoloNelParcoDopoIl(Component parent, MROldParcoVeicoli veicolo, Date inizio) {
        boolean valid = isVeicoloNelParcoDopoIl(veicolo, inizio);
        if (!valid) {
            JOptionPane.showMessageDialog(
                    parent,
                    bundle.getString("NotificheUtils.msgIngressoMezzoSuccessivoDataInizio"),
                    bundle.getString("NotificheUtils.msgMezzoNonPresenteNelParco"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
        return valid;
    }

    public static boolean isVeicoloNelParco(MROldParcoVeicoli veicolo, Date inizio, Date fine) {
        return isVeicoloNelParcoDopoIl(veicolo, FormattedDate.formattedDate(inizio.getTime())) &&
                isVeicoloNelParcoPrimaDel(Boolean.FALSE, veicolo, FormattedDate.formattedDate(fine.getTime()));
    }
    public static boolean isR2R(MROldParcoVeicoli veicolo,Session sx) {
        boolean status = false;
       // Session sx=null;
        try {
            org.hibernate.Criteria cr = sx.createCriteria(it.myrent.ee.db.Rent2Rent.class).add(org.hibernate.criterion.Restrictions.eq("vehicle",veicolo));
            cr.setCacheable(true);
            List count=cr.list();
//            String selectString =
//                    "select x from it.myrent.ee.db.Rent2Rent x where x.vehicle.id= :veh" ;//NOI18N
//            Query queryR2R = sx.createQuery(selectString).setParameter("veh", veicolo.getId());
//           // Number count = (Number) queryR2R.uniqueResult();
//            List count=queryR2R.list();
            if (count.size()==0){
                status=false;
            }   else {
                status = true;
            }
        } catch (Exception ex) {
            log.error("NotificheUtils.isR2R: " + ex.getMessage()); //NOI18N
            log.debug("NotificheUtils.isR2R: ", ex); //NOI18N
        }

        return  status;
    }

    public static MROldClienti findCustomerFromR2R(MROldParcoVeicoli veicolo,Session sx) {
        MROldClienti cliente = null;
        //Session sx=null;
        try {
           // sx = HibernateBridge.startNewSession();
            Query queryFindCustomer;
            StringBuffer queryString = new StringBuffer();
            queryString.append("select customer from it.myrent.ee.db.Rent2Rent x where x.vehicle=?"); //NOI18N
            queryFindCustomer = sx.createQuery(queryString.toString());
            queryFindCustomer.setParameter("veicolo", veicolo); //NOI18N
            if (queryFindCustomer.uniqueResult() != null) {
                cliente = (MROldClienti) queryFindCustomer.uniqueResult();
            } else {
                cliente = null;
            }


        } catch (HibernateException ex) {
            log.error("NotificheUtils.isR2R: " + ex.getMessage()); //NOI18N
            log.debug("NotificheUtils.isR2R: ", ex); //NOI18N
        } finally {
//            if(sx != null) {
//                try {
//                    sx.close();
//                } catch (HibernateException sxx) {
//                }
//            }
        }
        return cliente;
    }


    public static MROldClienti findCustomerFromMovement(MROldParcoVeicoli veicolo,Session sx) {
        MROldClienti cliente = null;
       // Session sx=null;
        try {
          //  sx = HibernateBridge.startNewSession();
            Query queryFindCustomer;
            StringBuffer queryString = new StringBuffer("select contratto from MROldMovimentoAuto x where x.veicolo = :veicolo and (x.dataApertura < :data and (x.dataChiusura is null or x.dataChiusura > :data))"); //NOI18N
            queryFindCustomer = sx.createQuery(queryString.toString());
            queryFindCustomer.setParameter("veicolo", veicolo); //NOI18N
            queryFindCustomer.setParameter("data", new Date()); //NOI18N
            if (queryFindCustomer.uniqueResult() != null) {
                MROldContrattoNoleggio con = (MROldContrattoNoleggio) queryFindCustomer.uniqueResult();

                if (con.getCliente() != null) {
                    cliente = con.getCliente();
                    Hibernate.initialize(con.getCliente());
                } else {
                    cliente = null;
                }
            } else {
                cliente = null;
            }



        } catch (HibernateException ex) {

            //log.error("NotificheUtils.findCustomerFromMovement: " + ex.getMessage()); //NOI18N
           // log.debug("NotificheUtils.findCustomerFromMovement: ", ex); //NOI18N
        } finally {
//            if(sx != null) {
//                try {
//                    sx.close();
//                } catch (HibernateException sxx) {
//                }
//            }
        }
        return cliente;
    }



}
