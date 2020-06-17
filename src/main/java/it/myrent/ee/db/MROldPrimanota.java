/*
 * Primanota.java
 *
 * Created on 16 mai 2006, 10:28
 *
 */
package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * @author jamess
 */
public class MROldPrimanota implements PersistentInstance {

    /** Creates a new instance of MROldPrimanota */
    public MROldPrimanota() {
        setDataCreazione(new Date());
        setEditable(Boolean.TRUE);
        setEsportato(Boolean.FALSE);
    }

    public MROldPrimanota(User user) {
        this();
        setUserCreazione(user);
        setUserModifica(user);
    }
    private Integer id;
    private Date dataCreazione;
    private User userCreazione;
    private Date dataModifica;
    private User userModifica;
    private MROldNumerazione numerazione;
    private MROldNumerazione sezionale;
    private Integer numeroRegistrazione;
    private Date dataRegistrazione;
    private Date dataEffettuazione;
    private Integer annoCompetenza;
    private String numeroDocumento;
    private String numeroDocumentoExtra;
    private Date dataDocumento;
    private Integer numeroProtocollo;
    private String annotazioni;
    private MROldAffiliato affiliato;
    private User user;
    private MROldSede sede;
    private MROldCausalePrimanota causale;
    private Double totaleDocumento;
    private Double totaleImponibile;
    private Double totaleImposta;
    private Boolean editable;
    private Boolean esportato;
    private Integer fileId;
    private List<RigaPrimanota> righePrimanota;
    private List<RigaImposta> righeImposta;
    private MROldContrattoNoleggio contratto;
    private MROldPrenotazione prenotazione;
    private MROldPartita partita;
    private MROldCurrencyConversionRate conversionRate;
    private MROldDocumentoFiscale fattura;
    //Campo non mappato. Utile per l'importazione dei documenti fiscali di myrent versione < 2.3
    private MROldDocumentoFiscale documentoFiscale;
  //  add these new column for exporting data to web portal
    private String accentryExportId;
    private Boolean isUpdated;
    private Boolean bene;
    private Boolean servizio;



    private Boolean xmlImported;
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    public String toString() {
        return MessageFormat.format(
                bundle.getString("Primanota.msgToStringFormatCausale0Nr1Data2Totale3"),
                getCausale(),
                getNumeroRegistrazione(),
                getDataRegistrazione(),
                getTotaleDocumento());
    }

    public String getAccentryExportId() {
        return accentryExportId;
    }

    public void setAccentryExportId(String accentryExportId) {
        this.accentryExportId = accentryExportId;
    }

    public Boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return new HashCodeBuilder().append(getId()).toHashCode();
        } else {
            return super.hashCode();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(Date dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public Date getDataModifica() {
        return dataModifica;
    }

    public void setDataModifica(Date dataModifica) {
        this.dataModifica = dataModifica;
    }

    public User getUserCreazione() {
        return userCreazione;
    }

    public void setUserCreazione(User userCreazione) {
        this.userCreazione = userCreazione;
    }

    public User getUserModifica() {
        return userModifica;
    }

    public void setUserModifica(User userModifica) {
        this.userModifica = userModifica;
    }

    public Integer getNumeroRegistrazione() {
        return numeroRegistrazione;
    }

    public void setNumeroRegistrazione(Integer numeroRegistrazione) {
        this.numeroRegistrazione = numeroRegistrazione;
    }

    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public Integer getAnnoCompetenza() {
        return annoCompetenza;
    }

    public void setAnnoCompetenza(Integer annoCompetenza) {
        this.annoCompetenza = annoCompetenza;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getNumeroDocumentoExtra() {
        return numeroDocumentoExtra;
    }

    public void setNumeroDocumentoExtra(String numeroDocumentoExtra) {
        this.numeroDocumentoExtra = numeroDocumentoExtra;
    }

    public Date getDataDocumento() {
        return dataDocumento;
    }

    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    public Integer getNumeroProtocollo() {
        return numeroProtocollo;
    }

    public void setNumeroProtocollo(Integer numeroProtocollo) {
        this.numeroProtocollo = numeroProtocollo;
    }

    public String getAnnotazioni() {
        return annotazioni;
    }

    public void setAnnotazioni(String annotazioni) {
        this.annotazioni = annotazioni;
    }

    public MROldAffiliato getAffiliato() {
        return affiliato;
    }

    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }

    public MROldCausalePrimanota getCausale() {
        return causale;
    }

    public void setCausale(MROldCausalePrimanota causale) {
        this.causale = causale;
    }

    public Double getTotaleDocumento() {
        return totaleDocumento;
    }

    public void setTotaleDocumento(Double totaleDocumento) {
        this.totaleDocumento = totaleDocumento;
    }

    public Double getTotaleImponibile() {
        return totaleImponibile;
    }

    public void setTotaleImponibile(Double totaleImponibile) {
        this.totaleImponibile = totaleImponibile;
    }

    public Double getTotaleImposta() {
        return totaleImposta;
    }

    public void setTotaleImposta(Double totaleImposta) {
        this.totaleImposta = totaleImposta;
    }

    public List<RigaPrimanota> getRighePrimanota() {
        return righePrimanota;
    }

    public void setRighePrimanota(List<RigaPrimanota> righePrimanota) {
        this.righePrimanota = righePrimanota;
    }

    public List<RigaImposta> getRigheImposta() {
        return righeImposta;
    }

    public void setRigheImposta(List<RigaImposta> righeImposta) {
        this.righeImposta = righeImposta;
    }

    public boolean equals(Object other) {
        if (other != null && (other instanceof MROldPrimanota)) {
            return new EqualsBuilder().append(getId(), ((MROldPrimanota) other).getId()).isEquals();
        } else {
            return false;
        }
    }

    public MROldDocumentoFiscale getDocumentoFiscale() {
        return documentoFiscale;
    }

    public void setDocumentoFiscale(MROldDocumentoFiscale documentoFiscale) {
        this.documentoFiscale = documentoFiscale;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MROldSede getSede() {
        return sede;
    }

    public void setSede(MROldSede sede) {
        this.sede = sede;
    }

    public MROldNumerazione getNumerazione() {
        return numerazione;
    }

    public void setNumerazione(MROldNumerazione numerazione) {
        this.numerazione = numerazione;
    }

    public MROldNumerazione getSezionale() {
        return sezionale;
    }

    public void setSezionale(MROldNumerazione sezionale) {
        this.sezionale = sezionale;
    }

    public MROldContrattoNoleggio getContratto() {
        return contratto;
    }

    public void setContratto(MROldContrattoNoleggio contratto) {
        this.contratto = contratto;
    }

    public Boolean getEsportato() {
        return esportato;
    }

    public void setEsportato(Boolean esportato) {
        this.esportato = esportato;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public MROldPartita getPartita() {
        return partita;
    }

    public void setPartita(MROldPartita partita) {
        this.partita = partita;
    }

    public MROldCurrencyConversionRate getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(MROldCurrencyConversionRate conversionRate) {
        this.conversionRate = conversionRate;
    }

    public MROldPrenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(MROldPrenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public void setFattura(MROldDocumentoFiscale fattura) {
        this.fattura = fattura;
    }

    public MROldDocumentoFiscale getFattura() {
        return fattura;
    }
    public Boolean getXmlImported() {
        return xmlImported;
    }

    public void setXmlImported(Boolean xmlImported) {
        this.xmlImported = xmlImported;
    }

    public Date getDataEffettuazione() {
        return dataEffettuazione;
    }

    public void setDataEffettuazione(Date dataEffettuazione) {
        this.dataEffettuazione = dataEffettuazione;
    }


    public Boolean getBene() {
        return bene;
    }

    public void setBene(Boolean bene) {
        this.bene = bene;
    }

    public Boolean getServizio() {
        return servizio;
    }

    public void setServizio(Boolean servizio) {
        this.servizio = servizio;
    }

}
