/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import it.aessepi.utils.beans.FormattedDate;
import it.aessepi.utils.db.PersistentInstance;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author bogdan
 */
public class MROldStagioneTariffa implements PersistentInstance {

    private Integer id;
    private String descrizione;
    private Boolean ivaInclusa;
    private Boolean multistagione;
    private MROldCodiciIva codiceIva;
    private MROldCodiciIva codiceIvaNonSoggetto;
    private Set<MROldImportoTariffa> importi = new LinkedHashSet<MROldImportoTariffa>(0);
    private Date inizio;
    private Date fine;

    public MROldStagioneTariffa() {
    }

    public MROldStagioneTariffa(
            String descrizione,
            Boolean multistagione,
            Boolean ivaInclusa,
            MROldCodiciIva codiceIva,
            MROldCodiciIva codiceIvaNonSoggetto,
            Date inizio,
            Date fine) {
        this.descrizione = descrizione;
        this.multistagione = multistagione;
        this.ivaInclusa = ivaInclusa;
        this.codiceIva = codiceIva;
        this.codiceIvaNonSoggetto = codiceIvaNonSoggetto;
        this.inizio = inizio;
        this.fine = fine;
    }

    /**
     * Questo costruttore crea una copia profonda di un'istanza <code>MROldStagioneTariffa</code>, con tutti i suoi dati (importi compresi).
     * @param other L'istanza da copiare.
     */
    public MROldStagioneTariffa(MROldStagioneTariffa other) {
        this(other.getDescrizione(),
                other.getMultistagione(),
                other.getIvaInclusa(),
                other.getCodiceIva(),
                other.getCodiceIvaNonSoggetto(),
                other.getInizio(),
                other.getFine());
        if (other.getImporti() != null) {
            for (MROldImportoTariffa importoStagione : other.getImporti()) {
                getImporti().add(new MROldImportoTariffa(this, importoStagione));
            }            
        }
    }

    public MROldStagioneTariffa(MROldValiditaListinoFonte validita, MROldGruppo gruppo, MROldFonteCommissione fonteBase) {
        this(validita.getListino().getDescrizione(),
                fonteBase!=null ? fonteBase.getMultistagione() : validita.getFonteCommissione().getMultistagione(),
                validita.getListino().getIvaInclusa(),
                validita.getListino().getCodiceIva(),
                validita.getListino().getCodiceIvaNonSoggetto(),
                validita.getInizioStagione(),
                validita.getFineStagione());
        for (MROldDurata durata : validita.getListino().getDurate()) {
            if (durata != null) {
                MROldTariffaListino tariffaListino = (MROldTariffaListino) durata.getTariffe().get(gruppo);
                if (tariffaListino != null) {
                    getImporti().add(new MROldImportoTariffa(this, durata, tariffaListino));
                }
            }
        }
    }


    /*
     * new constructor for new listino type
     */
    public MROldStagioneTariffa(MROldValiditaListinoFonte validita, Map<MROldDurata, MROldTariffaListino> durataTariffeListinoListMap, MROldFonteCommissione fonteBase) {
        this(validita.getListino().getDescrizione(),
                fonteBase!=null ? fonteBase.getMultistagione() : validita.getFonteCommissione().getMultistagione(),
                validita.getListino().getIvaInclusa(),
                validita.getListino().getCodiceIva(),
                validita.getListino().getCodiceIvaNonSoggetto(),
                validita.getInizioStagione(),
                validita.getFineStagione());
        for(Map.Entry<MROldDurata, MROldTariffaListino> element : durataTariffeListinoListMap.entrySet()){
            MROldDurata durata = element.getKey();
            MROldTariffaListino tariffaListino = element.getValue();
            getImporti().add(new MROldImportoTariffa(this, durata, tariffaListino));
        }
    }


    
    public MROldStagioneTariffa(Date inizioStagione, Date fineStagione) {
        this.inizio = inizioStagione;
        this.fine    =   fineStagione;
//        if (other.getImporti() != null) {
//            for (MROldImportoTariffa importoStagione : other.getImporti()) {
//                getImporti().add(new MROldImportoTariffa(this, importoStagione));
//            }            
//        }
    }
    
    
     public MROldStagioneTariffa(MROldValiditaListinoFonte validita, MROldGruppo gruppo, String nullString) {
        this(validita.getInizioStagione(),
             validita.getFineStagione());
         Iterator itr = validita.getListino().getDurate().iterator();
//        for (MROldDurata durata : validita.getListino().getDurate()) {
         while(itr.hasNext()){
             MROldDurata durata = (MROldDurata) itr.next();
             MROldTariffaListino tariffaListino = (MROldTariffaListino) durata.getTariffe().get(gruppo);
            if (tariffaListino != null) {
                getImporti().add(new MROldImportoTariffa(this, durata, tariffaListino));
            }
        }
    }

    public boolean inStagione(Date dataRitiro) {
        if (dataRitiro == null) {
            return false;
        }
        dataRitiro = FormattedDate.extractDate(dataRitiro);
        return dataRitiro.compareTo(getInizio()) * dataRitiro.compareTo(getFine()) <= 0;
    }

    public boolean equals(Object o) {
        if ((this == o)
                || (getId() != null
                && o != null
                && o instanceof MROldStagioneTariffa
                && getId().equals(((MROldStagioneTariffa) o).getId()))) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (getId() == null) {
            return super.hashCode();
        }
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public String toString() {
        return new StringBuilder().append(getDescrizione()).toString();
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getInizio() {
        return inizio;
    }

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }

    public Date getFine() {
        return fine;
    }

    public void setFine(Date fine) {
        this.fine = fine;
    }

    public Set<MROldImportoTariffa> getImporti() {
        return importi;
    }

    public void setImporti(Set<MROldImportoTariffa> importi) {
        this.importi = importi;
    }

    public Boolean getIvaInclusa() {
        return ivaInclusa;
    }

    public void setIvaInclusa(Boolean ivaInclusa) {
        this.ivaInclusa = ivaInclusa;
    }

    public MROldCodiciIva getCodiceIva() {
        return codiceIva;
    }

    public void setCodiceIva(MROldCodiciIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    public MROldCodiciIva getCodiceIvaNonSoggetto() {
        return codiceIvaNonSoggetto;
    }

    public void setCodiceIvaNonSoggetto(MROldCodiciIva codiceIvaNonSoggetto) {
        this.codiceIvaNonSoggetto = codiceIvaNonSoggetto;
    }

    public Boolean getMultistagione() {
        return multistagione;
    }

    public void setMultistagione(Boolean multistagione) {
        this.multistagione = multistagione;
    }
}
