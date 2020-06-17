package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.db.PersistentInstance;
import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

public class MROldDurata implements PersistentInstance, Comparable{
    
    private Integer id;
    private MROldListino listino;
    private Integer minimo;
    private Integer massimo;
    private Boolean importoFisso;
    private Map<MROldGruppo, MROldTariffaListino> tariffe;
    
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
    public static final MessageFormat VARIABILE_MIN = new MessageFormat(bundle.getString("Durata.VARIABLE_MIN0"));
    public static final MessageFormat VARIABILE_MIN_MAX = new MessageFormat(bundle.getString("Durata.VARIABLE_MIN0_MAX1"));
    public static final MessageFormat FISSO_MIN_MAX = new MessageFormat(bundle.getString("Durata.FISSO_MIN0_MAX1"));
    public static final MessageFormat FISSO_MIN_MIDDLE_MAX = new MessageFormat(bundle.getString("Durata.FISSO_MIN0_MIDDLE1_MAX2"));
    public static final MessageFormat FISSO_MIN_DOTS_MAX = new MessageFormat(bundle.getString("Durata.FISSO_MIN0_DOTS_MAX0"));
    
    
    public MROldDurata() {
    }
    
    public String toString() {
        if(getMinimo().equals(getMassimo())) {
            return VARIABILE_MIN.format(new Object[]{getMinimo()});
        } else {
            if(Boolean.TRUE.equals(getImportoFisso())) {                
                if(getMassimo() - getMinimo() == 1) {
                    return FISSO_MIN_MAX.format(new Object[]{getMinimo(), getMassimo()});
                } else if(getMassimo() - getMinimo() == 2) {
                    return FISSO_MIN_MIDDLE_MAX.format(new Object[]{getMinimo(), getMinimo() + 1, getMassimo()});
                } else {
                    return FISSO_MIN_DOTS_MAX.format(new Object[]{getMinimo(), getMassimo()});
                }                
            } else {
                return VARIABILE_MIN_MAX.format(new Object[]{getMinimo(), getMassimo()});
            }
        }
    }
    
    public boolean equals(Object other) {
        if(other != null && other instanceof MROldDurata) {
            MROldDurata otherDurata = (MROldDurata) other;
            if(getId() == null || otherDurata.getId() == null) {
                return super.equals(other);
            } else {
                return new EqualsBuilder().append(this.getId(), otherDurata.getId()).isEquals();
            }
        } else {
            return false;
        }
    }
    
    public int compareTo(Object other) {
        if(other != null) {
            MROldDurata otherDurata = (MROldDurata) other;
            return new CompareToBuilder().
                    append(this.getMinimo(), otherDurata.getMinimo()).
                    append(this.getMassimo(), otherDurata.getMassimo()).
                    append(this.getImportoFisso(), otherDurata.getImportoFisso()).toComparison();
                    
        } else {
            return 1;
        }        
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public MROldListino getListino() {
        return listino;
    }
    
    public void setListino(MROldListino listino) {
        this.listino = listino;
    }
    
    public Integer getMinimo() {
        return minimo;
    }
    
    public void setMinimo(Integer minimo) {
        this.minimo = minimo;
    }
    
    public Integer getMassimo() {
        return massimo;
    }
    
    public void setMassimo(Integer massimo) {
        this.massimo = massimo;
    }
    
    public Boolean getImportoFisso() {
        return importoFisso;
    }
    
    public void setImportoFisso(Boolean importoFisso) {
        this.importoFisso = importoFisso;
    }
    
    public Map<MROldGruppo, MROldTariffaListino> getTariffe() {
        return tariffe;
    }
    
    public void setTariffe(Map<MROldGruppo, MROldTariffaListino> tariffe) {
        this.tariffe = tariffe;
    }
}
