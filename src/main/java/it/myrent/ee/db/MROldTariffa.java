/*
 * Tariffa.java
 *
 * Created on 01 februarie 2007, 16:00
 *
 */
package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.*;


/**
 *
 * @author jamess
 */
public class MROldTariffa implements PersistentInstance {

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    /** Creates a new instance of MROldTariffa */
    public MROldTariffa() {
    }

    public MROldTariffa(
            String descrizione,
            MROldGruppo gruppo,
            MROldCodiciIva codiceIva,
            MROldCodiciIva codiceIvaNonSoggetto,
            MROldCodiciIva codiceIvaExtraPrepay,
            Boolean ivaInclusaExtraPrepay,
            Boolean multiStagione,
            Double depositoContanti,
            Double depositoSenzaAss,
            Double depositoSuperAss,
            Set<MROldImportoExtraPrepay> importiExtraPrepay,
            Set<MROldStagioneTariffa> stagioni,
            Map<MROldOptional, MROldOptionalTariffa> optionalsTariffa,
            Boolean oraTariffaAttiva,
            Date oraTariffa) {

        setDescrizione(descrizione);
        setCodiceIva(codiceIva);
        setCodiceIvaNonSoggetto(codiceIvaNonSoggetto);
        setCodiceIvaExtraPrepay(codiceIvaExtraPrepay);
        setIvaInclusaExtraPrepay(ivaInclusaExtraPrepay);
        setMultistagione(multistagione);
        setDepositoContanti(depositoContanti);
        setDepositoSenzaAss(depositoSenzaAss);
        setDepositoSuperAss(depositoSuperAss);
        setGruppo(gruppo);
        setImportiExtraPrepay(importiExtraPrepay);
        setStagioni(stagioni);
        setOptionalsTariffa(optionalsTariffa);
        setOraRientro(oraTariffa);
        setOraRientroAttiva(oraTariffaAttiva);
    }

    /**
     * Questo costruttore crea un copia profonda di un'istanza <code>MROldTariffa</code>.
     * @param other L'istanza da copiare.
     */
    public MROldTariffa(MROldTariffa other) {
        this(
                other.getDescrizione(),
                other.getGruppo(),
                other.getCodiceIva(),
                other.getCodiceIvaNonSoggetto(),
                other.getCodiceIvaExtraPrepay(),
                other.getIvaInclusaExtraPrepay(),
                other.getMultistagione(),
                other.getDepositoContanti(),
                other.getDepositoSenzaAss(),
                other.getDepositoSuperAss(),
                new LinkedHashSet<MROldImportoExtraPrepay>(0),
                new LinkedHashSet<MROldStagioneTariffa>(0),
                new HashMap<MROldOptional, MROldOptionalTariffa>(0),
                other.getOraRientroAttiva(),
                other.getOraRientro());
        if (other.getStagioni() != null) {
            for (MROldStagioneTariffa stagione : other.getStagioni()) {
                getStagioni().add(new MROldStagioneTariffa(stagione));
            }
        }
        if (other.getOptionalsTariffa() != null) {
            for (Map.Entry<MROldOptional, MROldOptionalTariffa> entry : other.getOptionalsTariffa().entrySet()) {
                getOptionalsTariffa().put(entry.getKey(), new MROldOptionalTariffa(entry.getValue()));
            }
        }
        if(other.getImportiExtraPrepay() != null) {
            for(MROldImportoExtraPrepay importo : other.getImportiExtraPrepay()) {
                getImportiExtraPrepay().add(new MROldImportoExtraPrepay(this, importo));
            }
        }
    }
    
    @Override
    public boolean equals(Object other) {
        if (other != null && other instanceof MROldTariffa) {
            return new EqualsBuilder().append(getId(), ((MROldTariffa) other).getId()).isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(getDescrizione()).append(getGruppo()).toString();
    }
    private Integer id;
    private MROldGruppo gruppo;
    private String descrizione;
    private Set<MROldImportoExtraPrepay> importiExtraPrepay = new LinkedHashSet<MROldImportoExtraPrepay>(0);
    private Set<MROldStagioneTariffa> stagioni = new LinkedHashSet<MROldStagioneTariffa>(0);
    private Map<MROldOptional, MROldOptionalTariffa> optionalsTariffa = new HashMap<MROldOptional, MROldOptionalTariffa>(0);
    private Double depositoSuperAss;
    private Double depositoSenzaAss;
    private Double depositoContanti;
    private MROldCodiciIva codiceIva;
    private MROldCodiciIva codiceIvaNonSoggetto;
    private MROldCodiciIva codiceIvaExtraPrepay;
    private Boolean ivaInclusaExtraPrepay;
    private Boolean multistagione;
    private Date oraRientro;
    private Boolean oraRientroAttiva;
    private Boolean isDirtyTariffa;

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

    public MROldGruppo getGruppo() {
        return gruppo;
    }

    public void setGruppo(MROldGruppo gruppo) {
        this.gruppo = gruppo;
    }

    public Map<MROldOptional, MROldOptionalTariffa> getOptionalsTariffa() {
        return optionalsTariffa;
    }

    public void setOptionalsTariffa(Map<MROldOptional, MROldOptionalTariffa> optionalsTariffa) {
        this.optionalsTariffa = optionalsTariffa;
    }

    public Double getDepositoSuperAss() {
        return depositoSuperAss;
    }

    public void setDepositoSuperAss(Double depositoSuperAss) {
        this.depositoSuperAss = depositoSuperAss;
    }

    public Double getDepositoSenzaAss() {
        return depositoSenzaAss;
    }

    public void setDepositoSenzaAss(Double depositoSenzaAss) {
        this.depositoSenzaAss = depositoSenzaAss;
    }

    public Double getDepositoContanti() {
        return depositoContanti;
    }

    public void setDepositoContanti(Double depositoContanti) {
        this.depositoContanti = depositoContanti;
    }

    public Set<MROldStagioneTariffa> getStagioni() {
        return stagioni;
    }

    public void setStagioni(Set<MROldStagioneTariffa> stagioni) {
        this.stagioni = stagioni;
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

    public void setImportiExtraPrepay(Set<MROldImportoExtraPrepay> importiExtraPrepay) {
        this.importiExtraPrepay = importiExtraPrepay;
    }

    public Set<MROldImportoExtraPrepay> getImportiExtraPrepay() {
        return importiExtraPrepay;
    }

    public MROldCodiciIva getCodiceIvaExtraPrepay() {
        return codiceIvaExtraPrepay;
    }

    public void setCodiceIvaExtraPrepay(MROldCodiciIva codiceIvaExtraPrepay) {
        this.codiceIvaExtraPrepay = codiceIvaExtraPrepay;
    }

    public Boolean getIvaInclusaExtraPrepay() {
        return ivaInclusaExtraPrepay;
    }

    public void setIvaInclusaExtraPrepay(Boolean ivaInclusaExtraPrepay) {
        this.ivaInclusaExtraPrepay = ivaInclusaExtraPrepay;
    }

    public Boolean getMultistagione() {
        return multistagione;
    }

    public void setMultistagione(Boolean multistagione) {
        this.multistagione = multistagione;
    }

    public void setOraRientro(Date oraRientro) {
        this.oraRientro = oraRientro;
    }

    public Date getOraRientro() {
        return oraRientro;
    }

    public void setOraRientroAttiva(Boolean oraRientroAttiva) {
        this.oraRientroAttiva = oraRientroAttiva;
    }

    public Boolean getOraRientroAttiva() {
        return oraRientroAttiva;
    }

    /*
     * Ritorna una rappresentazione a stringa della tariffa.
     * Serve per la registrazione dei cambiamenti in una tariffa, specialmente
     * le modifiche degli optional di tipo assicurazione
     */
    public String getLoggableInfos() {
        /* giusto per testare la registrazione dell'informazione nello storico modifiche */
        String optionalRepresentation = "";

        if (getOptionalsTariffa() != null) {
            for (Map.Entry<MROldOptional, MROldOptionalTariffa> entry : getOptionalsTariffa().entrySet()) {
                /* logga solamente gli optional selezionati (quindi la differenza nelle entry viene fatta solamente tra quelli selezionati) */
                if (entry.getValue().getSelezionato() != null && entry.getValue().getSelezionato()) {
                    /* aggiunge un capo se stringa non vuota */
                    if (!optionalRepresentation.isEmpty()) {
                        optionalRepresentation += "\n";
                    }
                    optionalRepresentation += entry.getKey().toString() + " - " + getOptionalTariffaDescrizione(entry.getValue());
                }
            }
        }

        /* fare la lista completa degli optional associati alla tariffa */
        return optionalRepresentation;
    }

    public String getOptionalTariffaDescrizione(MROldOptionalTariffa optionalTariffa) {
        String optionalTariffaDescrizione = "";

        if (optionalTariffa != null) {

            if (optionalTariffa.getPrepagato() != null && optionalTariffa.getPrepagato()) {
                optionalTariffaDescrizione += " " + bundle.getString("Optional.prep") + ": " + bundle.getString("Optional.Y");
            } else {
                optionalTariffaDescrizione += " " + bundle.getString("Optional.prep") + ": " + bundle.getString("Optional.N");
            }

            /* valori degli optional */
            if (optionalTariffa.getFranchigia() != null) {
                optionalTariffaDescrizione += " " + bundle.getString("Optional.fr") + ": " + optionalTariffa.getFranchigia();
            }

            if (optionalTariffa.getImporto() != null) {
                optionalTariffaDescrizione += " " + bundle.getString("Optional.imp") + ": " + optionalTariffa.getImporto();
            }

            if (optionalTariffa.getQuantita() != null) {
                optionalTariffaDescrizione += " " + bundle.getString("Optional.qta") + ": " + optionalTariffa.getQuantita();
            }
        }

        return optionalTariffaDescrizione;
    }


    public Boolean getIsDirtyTariffa() {
        return isDirtyTariffa;
    }

    public void setIsDirtyTariffa(Boolean isDirtyTariffa) {
        this.isDirtyTariffa = isDirtyTariffa;
    }
    
}