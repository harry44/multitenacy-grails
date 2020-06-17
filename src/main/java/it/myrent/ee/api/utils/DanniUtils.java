package it.myrent.ee.api.utils;
import it.aessepi.utils.beans.UnmappedDamageImage;
import it.myrent.ee.db.MROldDamagePrice;
import it.myrent.ee.db.MROldGruppo;
import it.myrent.ee.db.MROldParcoVeicoli;
import it.myrent.ee.db.MROldDanno;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import java.util.*;

/**
 * Created by Madhvendra on 04/01/2017.
 */
public class DanniUtils {

    /** Creates a new instance of DanniUtils */
    private DanniUtils() {
    }

    /**
     * Ritorna i danni presenti nel veicolo (solo quelli non riparati)
     */
    public static Set danniPresentiVeicolo(Session sx, MROldParcoVeicoli aVeicolo) throws HibernateException {
        aVeicolo = (MROldParcoVeicoli) sx.get(MROldParcoVeicoli.class, aVeicolo.getId());
        Set danniPresenti = new HashSet();
        Iterator danni = aVeicolo.getDanni().iterator();
        while(danni.hasNext()) {
            MROldDanno aDanno = (MROldDanno) danni.next();
            if(aDanno.getRiparato()==null||aDanno.getRiparato().equals(Boolean.FALSE)) {
                danniPresenti.add(aDanno);
            }
        }
        return danniPresenti;
    }

    public static String creaStringaDannoPerStampa(Set danni) {

        StringBuilder danno = new StringBuilder();
        if (danni != null && danni.size() > 0) {
            Iterator it = danni.iterator();
            while (it.hasNext()) {
                MROldDanno d = (MROldDanno) it.next();
                danno.append(d.getDescrizione());
                if (it.hasNext()) {
                    danno.append(",");
                }
            }
        }


        return danno.toString();
    }

    public static UnmappedDamageImage[] MovementDamageDetails(Session sx, MROldGruppo gruppi, Set danni)
    {
        UnmappedDamageImage[] UnmappedDamageImageArray= new UnmappedDamageImage[danni.size()];
        if (danni != null && danni.size() > 0) {
            Iterator it = danni.iterator();
            for(int i=0;i<danni.size();i++)
            {
                try{
                    MROldDanno d = (MROldDanno) it.next();
                    UnmappedDamageImageArray[i] = new UnmappedDamageImage();

                    String sql = "SELECT * FROM mrdamage_price WHERE id_gruppi ="+gruppi.getId()+" AND id_damagedictionary="+d.getDamageDictionary().getId()+" AND id_damageseverity="+d.getDamageSeverity().getId();
                    SQLQuery query = sx.createSQLQuery(sql);
                    query.addEntity(MROldDamagePrice.class);

                    List results = query.list();

                    for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                        MROldDamagePrice damagePrice = (MROldDamagePrice) iterator.next();




                    if(damagePrice.getDamageDictionary()!=null) {
                        if (damagePrice.getDamageDictionary().getIsEnable() == true) {
                            if (Objects.equals(damagePrice.getMaxPrice(), d.getAppliedPrice()) && Objects.equals(damagePrice.getMinPrice(), d.getAppliedPrice())) {

//             UnmappedDamageImageArray[i].setDamageSeveritySubType("ALL");
                            } else if (Objects.equals(damagePrice.getMaxPrice(), d.getAppliedPrice())) {
                                UnmappedDamageImageArray[i].setDamageSeveritySubType("Grave / Severe");
                            } else if (Objects.equals(damagePrice.getMinPrice(), d.getAppliedPrice())) {

                                UnmappedDamageImageArray[i].setDamageSeveritySubType("Lieve / Small");
                            } else if (d.getId() != null) {
//                 UnmappedDamageImageArray[i].setDamageSeveritySubType("NO");
                            }
                        }
                    }

                    }



                    if(d.getDamageSeverity() != null && d.getDamageSeverity().getDescription() != null)
                    {
                        UnmappedDamageImageArray[i].setSeverity(d.getDamageSeverity().getDescription()+" / "+d.getDamageSeverity().getDescriptionEn());
                    }
                    if(d.getDamageType() != null && d.getDamageType().getDescription() != null)
                    {
                        UnmappedDamageImageArray[i].setDamageType(d.getDamageType().getDescription()+" / "+d.getDamageType().getDescriptionEn());
                    }
                    if(d.getDamageDictionary() != null && d.getDamageDictionary().getDescription() != null)
                    {
                        UnmappedDamageImageArray[i].setDictionary(d.getDamageDictionary().getDescription()+"/"+d.getDamageDictionary().getDescriptionEn());
                    }
                    if(d.getDescrizione() !=null)
                    {
                        UnmappedDamageImageArray[i].setDamageName(d.getDescrizione());
                    }
                    if(d.getId()!=null)
                    {UnmappedDamageImageArray[i].setDamageTotalCost(d.getAppliedPrice());}
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
        return UnmappedDamageImageArray;
    }
    public static UnmappedDamageImage[] MovementDamageDetails(Set danni) {
        UnmappedDamageImage[] UnmappedDamageImageArray = new UnmappedDamageImage[danni.size()];
        if (danni != null && danni.size() > 0) {
            Iterator it = danni.iterator();
            for (int i = 0; i < danni.size(); i++) {
                try {
                    MROldDanno d = (MROldDanno) it.next();
                    UnmappedDamageImageArray[i] = new UnmappedDamageImage();
                    if (d.getDamageSeverity() != null && d.getDamageSeverity().getDescription() != null) {
                        UnmappedDamageImageArray[i].setSeverity(d.getDamageSeverity().getDescription());
                    }
                    if (d.getDamageType() != null && d.getDamageType().getDescription() != null) {
                        UnmappedDamageImageArray[i].setDamageType(d.getDamageType().getDescription());
                    }
                    if (d.getDamageDictionary() != null && d.getDamageDictionary().getDescription() != null) {
                        UnmappedDamageImageArray[i].setDictionary(d.getDamageDictionary().getDescription());
                    }
                    if (d.getDescrizione() != null) {
                        UnmappedDamageImageArray[i].setDamageName(d.getDescrizione());
                    }
                    if (d.getId() != null) {
                        UnmappedDamageImageArray[i].setDamageTotalCost(d.getAppliedPrice());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return UnmappedDamageImageArray;
    }
}
