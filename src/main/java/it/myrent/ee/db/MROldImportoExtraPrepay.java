/*
 * TariffaListino.java
 *
 * Created on 22 decembrie 2006, 13:13
 *
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 *
 * @author jamess
 */
public class MROldImportoExtraPrepay implements ImportoGiornaliero {
    
    private Integer id;
    private MROldTariffa tariffa;
    private Integer minimo;
    private Integer massimo;
    private Boolean importoFisso;
    private Double importoBase;
    private Double importoExtra;
    
    /** Creates a new instance of MROldTariffaListino */
    public MROldImportoExtraPrepay() {
    }

    public MROldImportoExtraPrepay(MROldTariffa tariffa, MROldDurata durata, MROldTariffaListino tariffaListino) {
        setTariffa(tariffa);
        setMinimo(durata.getMinimo());
        setMassimo(durata.getMassimo());
        setImportoFisso(durata.getImportoFisso());
        setImportoBase(tariffaListino.getImportoBase());
        setImportoExtra(tariffaListino.getImportoExtra());
    }
    
    public MROldImportoExtraPrepay(MROldTariffa tariffa, MROldImportoExtraPrepay other) {
        setImportoBase(other.getImportoBase());
        setImportoExtra(other.getImportoExtra());
        setImportoFisso(other.getImportoFisso());
        setMassimo(other.getMassimo());
        setMinimo(other.getMinimo());
        setTariffa(tariffa);
    }
    
    public String toString() {
        if(getMinimo().equals(getMassimo())) {
            return MROldDurata.VARIABILE_MIN.format(new Object[]{getMinimo()});
        } else {
            if(Boolean.TRUE.equals(getImportoFisso())) {                
                if(getMassimo() - getMinimo() == 1) {
                    return MROldDurata.FISSO_MIN_MAX.format(new Object[]{getMinimo(), getMassimo()});
                } else if(getMassimo() - getMinimo() == 2) {
                    return MROldDurata.FISSO_MIN_MIDDLE_MAX.format(new Object[]{getMinimo(), getMinimo() + 1, getMassimo()});
                } else {
                    return MROldDurata.FISSO_MIN_DOTS_MAX.format(new Object[]{getMinimo(), getMassimo()});
                }                
            } else {
                return MROldDurata.VARIABILE_MIN_MAX.format(new Object[]{getMinimo(), getMassimo()});
            }
        }
    }
    
    public int compareTo(Object other) {
        if(other != null) {
            MROldImportoExtraPrepay otherImporto = (MROldImportoExtraPrepay) other;
            return new CompareToBuilder().
                    append(this.getMinimo(), otherImporto.getMinimo()).
                    append(this.getMassimo(), otherImporto.getMassimo()).
                    append(this.getImportoFisso(), otherImporto.getImportoFisso()).toComparison();
                    
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

    public MROldTariffa getTariffa() {
        return tariffa;
    }

    public void setTariffa(MROldTariffa tariffa) {
        this.tariffa = tariffa;
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

    public Double getImportoBase() {
        return importoBase;
    }

    public void setImportoBase(Double importoBase) {
        this.importoBase = importoBase;
    }

    public Double getImportoExtra() {
        return importoExtra;
    }

    public void setImportoExtra(Double importoExtra) {
        this.importoExtra = importoExtra;
    }

}
