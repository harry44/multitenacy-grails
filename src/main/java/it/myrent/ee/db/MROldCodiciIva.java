package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


import java.text.MessageFormat;

/** @author Hibernate CodeGenerator */
public class MROldCodiciIva implements it.aessepi.utils.db.PersistentInstance {

    private Integer id;

    private String codice;
    
    private String descrizione;
    
    private Double aliquota;//formatter come percentuale
    
    private Boolean detraibile;
    
    private Double indetraibilita;
    
    private Integer operazione; //IMPONIBILE; ESENTE; NON_IMPONIBILE; NON_SOGGETTO; NON_SOGG_ART_74    
    
    private Boolean monteAcquisti;
    
    private String codiceIvaMonteAcquisti;
    
    private Boolean plafond;
    
    private Boolean importazioneOroArgento;
    
    private Integer esenteArt10Prorata; //ESENTE_ART_10; NO; ESCLUSO_VOLUME_AFFARI
    
    private Boolean bolloSuImponibiliEsenti;
    
    private Double compensazione; //formatter come percentuale.
    
    private Integer tipoAcquistoAgricolo; //AGEVOLATO; AGEVOLATO_ESONERATO; NON_AGEVOLATO; ALTRI; NORMALE
    
    private Integer operazioniIntra; //NO_INTRA; ACQUISTI; VENDITE; ACQUISTI_VENDITE 
    
    private Boolean sistemaMargine;
    
    private String codiceEsterno;

    private String naturaCodiceIVA;

    private String esigibilita;
    
    /** Constants definitions**/
    public static final Integer ESENTE_ART10_SI = new Integer(0);
    public static final Integer ESENTE_ART10_NO = new Integer(1);
    public static final Integer ESENTE_ART10_ESCL_VOL_AFFARI = new Integer(2);
    
    public static final Integer ACQUISTO_AGRICOLO_AGEVOLATO = new Integer(0);
    public static final Integer ACQUISTO_AGRICOLO_AGEVOLATO_ESONERATO = new Integer(1);
    public static final Integer ACQUISTO_AGRICOLO_NON_AGEVOLATO = new Integer(2);
    public static final Integer ACQUISTO_AGRICOLO_ALTRI = new Integer(3);
    public static final Integer ACQUISTO_AGRICOLO_NORMALE = new Integer(4);
    
    public static final Integer OPERAZIONI_INTRA_NO_INTRA = new Integer(0);
    public static final Integer OPERAZIONI_INTRA_ACQUISTI = new Integer(1);
    public static final Integer OPERAZIONI_INTRA_VENDITE = new Integer(2);
    public static final Integer OPERAZIONI_INTRA_ACQUISTI_VENDITE = new Integer(3);
    
    public static final Integer OPERAZIONE_IMPONIBILE = new Integer(0);
    public static final Integer OPERAZIONE_ESENTE = new Integer(1);
    public static final Integer OPERAZIONE_NON_IMPONIBILE = new Integer(2);
    public static final Integer OPERAZIONE_NON_SOGGETTO = new Integer(3);
    public static final Integer OPERAZIONE_NON_SOGG_ART_74 = new Integer(4);

     //add these new column for exporting data to web portal
    private Boolean isExported;
    private Boolean isUpdated;
    private Boolean isDefault;

    public MROldCodiciIva() {
    }

    public String toString() {
        String retValue = new String();
        if(getCodice() != null && getDescrizione() != null) {
            retValue = getCodice() + " - " + getDescrizione(); //NOI18N
        } else if(getCodice() != null) {            
            retValue = getCodice();
        } else if(getDescrizione() != null) {
            retValue = getDescrizione();
        } else if(getAliquota() != null) {
            retValue = MessageFormat.format("IVA al {0,number,0.## %}", getAliquota());
        }
        return retValue;
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MROldCodiciIva) ) return false;
        MROldCodiciIva castOther = (MROldCodiciIva) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public Boolean getIsExported() {
        return isExported;
    }

    public void setIsExported(Boolean isExported) {
        this.isExported = isExported;
    }

    public Boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Double getAliquota() {
        return aliquota;
    }

    public void setAliquota(Double aliquota) {
        this.aliquota = aliquota;
    }

    public Boolean getDetraibile() {
        return detraibile;
    }

    public void setDetraibile(Boolean detraibile) {
        this.detraibile = detraibile;
    }

    public Boolean getMonteAcquisti() {
        return monteAcquisti;
    }

    public void setMonteAcquisti(Boolean monteAcquisti) {
        this.monteAcquisti = monteAcquisti;
    }

    public String getCodiceIvaMonteAcquisti() {
        return codiceIvaMonteAcquisti;
    }

    public void setCodiceIvaMonteAcquisti(String codiceIvaMonteAcquisti) {
        this.codiceIvaMonteAcquisti = codiceIvaMonteAcquisti;
    }

    public Boolean getPlafond() {
        return plafond;
    }

    public void setPlafond(Boolean plafond) {
        this.plafond = plafond;
    }

    public Boolean getImportazioneOroArgento() {
        return importazioneOroArgento;
    }

    public void setImportazioneOroArgento(Boolean importazioneOroArgento) {
        this.importazioneOroArgento = importazioneOroArgento;
    }

    public Integer getEsenteArt10Prorata() {
        return esenteArt10Prorata;
    }

    public void setEsenteArt10Prorata(Integer esenteArt10Prorata) {
        this.esenteArt10Prorata = esenteArt10Prorata;
    }

    public Boolean getBolloSuImponibiliEsenti() {
        return bolloSuImponibiliEsenti;
    }

    public void setBolloSuImponibiliEsenti(Boolean bolloSuImponibiliEsenti) {
        this.bolloSuImponibiliEsenti = bolloSuImponibiliEsenti;
    }

    public Double getCompensazione() {
        return compensazione;
    }

    public void setCompensazione(Double compensazione) {
        this.compensazione = compensazione;
    }

    public Integer getTipoAcquistoAgricolo() {
        return tipoAcquistoAgricolo;
    }

    public void setTipoAcquistoAgricolo(Integer tipoAcquistoAgricolo) {
        this.tipoAcquistoAgricolo = tipoAcquistoAgricolo;
    }

    public Integer getOperazioniIntra() {
        return operazioniIntra;
    }

    public void setOperazioniIntra(Integer operazioniIntra) {
        this.operazioniIntra = operazioniIntra;
    }

    
    public Integer getOperazione() {
        return operazione;
    }

    public void setOperazione(Integer operazione) {
        this.operazione = operazione;
    }
    
    public Double getCodiceIva() {
        if(getAliquota() != null) {
            return new Double(getAliquota().doubleValue() * 100);
        } else {
            return new Double(0);
        }
    }
    
    public Double getPercentuale() {
        return getCodiceIva();
    }

    public Boolean getSistemaMargine() {
        return sistemaMargine;
    }

    public void setSistemaMargine(Boolean sistemaMargine) {
        this.sistemaMargine = sistemaMargine;
    }

    public String getCodiceEsterno() {
        return codiceEsterno;
    }

    public void setCodiceEsterno(String codiceEsterno) {
        this.codiceEsterno = codiceEsterno;
    }

    public Double getIndetraibilita() {
        return indetraibilita;
    }

    public void setIndetraibilita(Double indetraibilita) {
        this.indetraibilita = indetraibilita;
    }

    public String getNaturaCodiceIVA() {
        return naturaCodiceIVA;
    }

    public void setNaturaCodiceIVA(String naturaCodiceIVA) {
        this.naturaCodiceIVA = naturaCodiceIVA;
    }

    public String getEsigibilita() {
        return esigibilita;
    }

    public void setEsigibilita(String esigibilita) {
        this.esigibilita = esigibilita;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

}
