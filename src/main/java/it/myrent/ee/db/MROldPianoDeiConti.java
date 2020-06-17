package it.myrent.ee.db;

import java.text.DecimalFormat;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/** @author Hibernate CodeGenerator */
public class MROldPianoDeiConti implements it.aessepi.utils.db.PersistentInstance, Comparable {

    private Integer id;
    private String descrizione;
    private Integer codiceMastro;
    private Integer codiceConto;
    private Integer codiceSottoconto;
    private Integer codiceDettaglio;
    private String pattern;
    private Integer sezioneBilancio;
    private Boolean editable;
    private String contoEsterno;
    private String sottocontoEsterno;
    public static final DecimalFormat DF_MASTRO = new DecimalFormat("00"); //NOI18N
    public static final DecimalFormat DF_CONTO = new DecimalFormat("00"); //NOI18N
    public static final DecimalFormat DF_SOTTOCONTO = new DecimalFormat("000"); //NOI18N
    public static final DecimalFormat DF_DETTAGLIO = new DecimalFormat("00"); //NOI18N
    public static final String PIANO_DEI_CONTI = "MROldPianoDeiConti"; //NOI18N
    public static final String MASTRO = "Mastro"; //NOI18N
    public static final String CONTO = "Conto"; //NOI18N
    public static final String SOTTOCONTO = "MROldSottoconto"; //NOI18N
    public static final String DETTAGLIO = "Dettaglio"; //NOI18N
    public static final String PIANO_DEI_CONTI_I18N = "MROldPianoDeiConti";
    public static final String MASTRO_I18N = "Mastro";
    public static final String CONTO_I18N = "Conto";
    public static final String SOTTOCONTO_I18N = "MROldSottoconto";
    public static final String DETTAGLIO_I18N = "Dettaglio";
    public static final Integer M1 = 1;
    public static final Integer M2 = 2;
    public static final Integer M3 = 3;
    public static final Integer M4 = 4;
    public static final Integer M5 = 5;
    public static final Integer C60 = 60;
    public static final Integer C35 = 35;
    public static final Integer MASTRO_ATTIVITA = M1;
    public static final Integer MASTRO_PASSIVITA = M2;
    public static final Integer MASTRO_COSTI = M3;
    public static final Integer MASTRO_RICAVI = M4;
    public static final Integer MASTRO_CONTI_ORDINE = M5;
    public static final Integer CONTO_DEBITI_DIVERSI = 30;
    public static final Integer CONTO_FORNITORI = 10;
    public static final Integer CONTO_IVA = 40;
    public static final Integer CONTO_BANCA = 60;
    public static final Integer CONTO_CASSA = 70;
    public static final Integer CONTO_CLIENTI = 80;
    public static final Integer CONTO_CREDITI_DIVERSI = 90;
    public static final Integer CONTO_RICAVI_VENDITE = 10;
    public static final Integer SOTTOCONTO_CLIENTI = 1;
    public static final Integer SOTTOCONTO_IVA_ACQUISTI = 1;
    public static final Integer SOTTOCONTO_IVA_VENDITE = 2;
    public static final Integer SOTTOCONTO_IVA_RIEPILOGATIVO = 3;
    public static final Integer SOTTOCONTO_IVA_SOSPENSIONE = 4;
    public static final Integer SOTTOCONTO_IVA_PAGAMENTI = 5;
    public static final Integer SOTTOCONTO_CASSA_CONTANTI = 1;
    public static final Integer SOTTOCONTO_CASSA_ASSEGNI = 2;
    public static final Integer SOTTOCONTO_CASSA_CAUZIONI = 3;
    public static final Integer SOTTOCONTO_CASSA_CARTE_BANCOMAT = 4;
    public static final Integer SOTTOCONTO_CASSA_VOUCHER = 5;
    public static final Integer SOTTOCONTO_CASSA_A_VISTA = 6;
    public static final Integer SOTTOCONTO_CLIENTI_C_ANTICIPI = 12;
    public static final Integer SOTTOCONTO_BANCA1 = 1;
    public static final Integer SOTTOCONTO_BANCA2 = 2;
    public static final Integer SOTTOCONTO_CHIUSURA_CASSE = 4;
    public static final Integer CONTO_PROFITTI_PERDITE = 10;
    public static final Integer SOTTOCONTO_PROFITTI_PERDITE = 1;
    public static final Integer CONTO_STATO_PATRIMONIALE = 20;
    public static final Integer SOTTOCONTO_STATO_PATRIMONIALE = 1;
    public static final Integer MASTRO_UTILE_ESERCIZIO = 2;
    public static final Integer CONTO_UTILE_ESERCIZIO = 50;
    public static final Integer SOTTOCONTO_UTILE_ESERCIZIO = 3;
    public static final Integer MASTRO_PERDITA_ESERCIZIO = 2;
    public static final Integer CONTO_PERDITA_ESERCIZIO = 50;
    public static final Integer SOTTOCONTO_PERDITA_ESERCIZIO = 4;

    public static final MROldPianoDeiConti BANCA1 = new MROldPianoDeiConti(MASTRO_ATTIVITA, CONTO_BANCA, SOTTOCONTO_BANCA1);
    public static final MROldPianoDeiConti BANCA2 = new MROldPianoDeiConti(MASTRO_ATTIVITA, CONTO_BANCA, SOTTOCONTO_BANCA2);
    public static final MROldPianoDeiConti CHIUSURA_CASSA = new MROldPianoDeiConti(MASTRO_ATTIVITA, CONTO_CREDITI_DIVERSI, SOTTOCONTO_CHIUSURA_CASSE);
    public static final MROldPianoDeiConti CASSA_CONTANTI = new MROldPianoDeiConti(MASTRO_ATTIVITA, CONTO_CASSA, SOTTOCONTO_CASSA_CONTANTI);
    public static final MROldPianoDeiConti CASSA_CAUZIONI = new MROldPianoDeiConti(MASTRO_ATTIVITA, CONTO_CASSA, SOTTOCONTO_CASSA_CAUZIONI);
    public static final MROldPianoDeiConti CLIENTI = new MROldPianoDeiConti(MASTRO_ATTIVITA, CONTO_CLIENTI, SOTTOCONTO_CLIENTI);
    public static final MROldPianoDeiConti CLIENTI_C_ANTICIPI = new MROldPianoDeiConti(MASTRO_PASSIVITA, CONTO_DEBITI_DIVERSI, SOTTOCONTO_CLIENTI_C_ANTICIPI);
    public static final MROldPianoDeiConti IVA_C_VENDITA = new MROldPianoDeiConti(MASTRO_PASSIVITA, CONTO_IVA, SOTTOCONTO_IVA_VENDITE);
    public static final MROldPianoDeiConti IVA_C_ACQUISTI = new MROldPianoDeiConti(MASTRO_PASSIVITA, CONTO_IVA, SOTTOCONTO_IVA_ACQUISTI);
    public static final MROldPianoDeiConti RICAVI_VENDITE = new MROldPianoDeiConti(MASTRO_RICAVI, CONTO_RICAVI_VENDITE, null);


    //add these new column for exporting data to web portal
    private Boolean isExported;
    private Boolean isUpdated;
    
    public MROldPianoDeiConti() {
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

    public MROldPianoDeiConti(Integer codiceMastro, Integer codiceConto, Integer codiceSottoconto) {
        this(codiceMastro, codiceConto, codiceSottoconto, null, null);
    }

    public MROldPianoDeiConti(Integer codiceMastro, Integer codiceConto, Integer codiceSottoconto, Integer codiceDettaglio, String descrizione) {
        setCodiceMastro(codiceMastro);
        setCodiceConto(codiceConto);
        setCodiceSottoconto(codiceSottoconto);
        setCodiceDettaglio(codiceDettaglio);
        setPattern(computePattern());
        setDescrizione(descrizione);
        setEditable(Boolean.TRUE);
    }

    public String toString() {
        String returnValue = new String();
        if (getPattern() != null) {
            returnValue = returnValue + getPattern();
        }
        if (getDescrizione() != null) {
            if (returnValue.trim().length() == 0) {
                returnValue = new String();
            } else {
                returnValue = returnValue + " - " + getDescrizione(); //NOI18N
            }
        }
        return returnValue;
    }

    public static String format(Integer value, DecimalFormat df) {
        if (value != null) {
            return df.format(value);
        } else {
            return df.toPattern();
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldPianoDeiConti)) {
            return false;
        }
        MROldPianoDeiConti castOther = (MROldPianoDeiConti) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public int compareTo(Object o) {
        return getPattern().compareTo(((MROldPianoDeiConti) o).getPattern());
    }

    public String getTipo() {
        return PIANO_DEI_CONTI;
    }

    public String computePattern() {
        Integer mastro, conto, sottoconto, dettaglio;
        if (getCodiceMastro() == null) {
            mastro = new Integer(0);
        } else {
            mastro = getCodiceMastro();
        }

        if (getCodiceConto() == null) {
            conto = new Integer(0);
        } else {
            conto = getCodiceConto();
        }

        if (getCodiceSottoconto() == null) {
            sottoconto = new Integer(0);
        } else {
            sottoconto = getCodiceSottoconto();
        }

        if (getCodiceDettaglio() == null) {
            dettaglio = new Integer(0);
        } else {
            dettaglio = getCodiceDettaglio();
        }
        return DF_MASTRO.format(mastro) + DF_CONTO.format(conto) + DF_SOTTOCONTO.format(sottoconto) + DF_DETTAGLIO.format(dettaglio);
    }

    public DecimalFormat getFormatter() {
        return null;
    }

    public MROldPianoDeiConti newLastSibling() {
        return null;
    }

    public MROldPianoDeiConti newFirstChild() {
        return null;
    }

    public Integer getId() {
        return id;
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

    public Integer getCodiceMastro() {
        return codiceMastro;
    }

    public void setCodiceMastro(Integer codiceMastro) {
        this.codiceMastro = codiceMastro;
    }

    public Integer getCodiceConto() {
        return codiceConto;
    }

    public void setCodiceConto(Integer codiceConto) {
        this.codiceConto = codiceConto;
    }

    public Integer getCodiceSottoconto() {
        return codiceSottoconto;
    }

    public void setCodiceSottoconto(Integer codiceSottoconto) {
        this.codiceSottoconto = codiceSottoconto;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getSezioneBilancio() {
        return sezioneBilancio;
    }

    public void setSezioneBilancio(Integer sezioneBilancio) {
        this.sezioneBilancio = sezioneBilancio;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Integer getCodiceDettaglio() {
        return codiceDettaglio;
    }

    public void setCodiceDettaglio(Integer codiceDettaglio) {
        this.codiceDettaglio = codiceDettaglio;
    }

    public String getContoEsterno() {
        return contoEsterno;
    }

    public void setContoEsterno(String contoEsterno) {
        this.contoEsterno = contoEsterno;
    }

    public String getSottocontoEsterno() {
        return sottocontoEsterno;
    }

    public void setSottocontoEsterno(String sottocontoEsterno) {
        this.sottocontoEsterno = sottocontoEsterno;
    }
}
