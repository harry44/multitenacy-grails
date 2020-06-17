/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import it.aessepi.utils.Parameters;
import it.aessepi.utils.beans.FormattedDate;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author giacomo
 */
public class MROldGestioneDocumentale implements it.aessepi.utils.db.PersistentInstance {

    private Integer id;
    private MROldDocumentoContratto tipo;
    private String keywords;
    private String descrizione;
    private Date dataScadenza;
    private Date dataCaricamento;
    private User cratedBy;
    private Set<MROldClienti> clienti;
    private Set<MROldConducenti> conducenti;
    private Set<MROldParcoVeicoli> veicoli;
    private Set<MROldContrattoNoleggio> contratti;
    private Set<MROldPrenotazione> prenotazioni;
    private Set<MROldDocumentoFiscale> fatture;
    private Set<MROldPreventivo> preventivi;
    private String pathFile;
    private String protocolloArchiviazione;
    private String fullPath;
    //Madhvendra (For Postgres version)
    private MROldMulta multa;
    //
    public MROldGestioneDocumentale() {
        setClienti(new HashSet());
        setConducenti(new HashSet());
        setContratti(new HashSet());
        setVeicoli(new HashSet());
        setPrenotazioni(new HashSet());
        setPreventivi(new HashSet());
        setFatture(new HashSet());

        setDataCaricamento(FormattedDate.formattedDate());
        setCratedBy((User)Parameters.getUser());
    }

    public Set getDocumentables(Documentable intestatario) {
        if (intestatario instanceof MROldClienti) {
            return getClienti();
        } else if (intestatario instanceof MROldConducenti) {
            return getConducenti();
        } else if (intestatario instanceof MROldContrattoNoleggio) {
            return getContratti();
        } else if (intestatario instanceof MROldParcoVeicoli) {
            return getVeicoli();
        } else if ( intestatario instanceof MROldPrenotazione) {
            return getPrenotazioni();
        } else if (intestatario instanceof MROldPreventivo) {
            return getPreventivi();
        } else if (intestatario instanceof MROldDocumentoFiscale) {
            return getFatture();
        }
        return null;
    }

    public String getRelativePath() {
        return FormattedDate.format("yyyy" + File.separator + "MM" + File.separator + "dd", getDataCaricamento());
    }

    public String getFullPath() {
//        return Preferenze.getPreferenze().getPerscorsoDocumenti() + File.separator + getRelativePath() + File.separator + getPathFile();
        return fullPath + File.separator + getRelativePath() + File.separator + getPathFile();
    }

    public MROldMulta getMulta()
    {
        return multa;
    }
    public void setMulta(MROldMulta in)
    {
        this.multa=in;
    }
    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    /**
     * @return the keywords
     */
    public String getKeywords() {
        return keywords;
    }

    /**
     * @param keywords the keywords to set
     */
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**
     * @return the descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * @param descrizione the descrizione to set
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * @return the dataScadenza
     */
    public Date getDataScadenza() {
        return dataScadenza;
    }

    /**
     * @param dataScadenza the dataScadenza to set
     */
    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    /**
     * @return the dataCaricamento
     */
    public Date getDataCaricamento() {
        return dataCaricamento;
    }

    /**
     * @param dataCaricamento the dataCaricamento to set
     */
    public void setDataCaricamento(Date dataCaricamento) {
        this.dataCaricamento = dataCaricamento;
    }

    /**
     * @return the cratedBy
     */
    public User getCratedBy() {
        return cratedBy;
    }

    /**
     * @param cratedBy the cratedBy to set
     */
    public void setCratedBy(User cratedBy) {
        this.cratedBy = cratedBy;
    }

    /**
     * @return the clienti
     */
    public Set getClienti() {
        return clienti;
    }

    /**
     * @param clienti the clienti to set
     */
    public void setClienti(Set clienti) {
        this.clienti = clienti;
    }

    /**
     * @return the conducenti
     */
    public Set getConducenti() {
        return conducenti;
    }

    /**
     * @param conducenti the conducenti to set
     */
    public void setConducenti(Set conducenti) {
        this.conducenti = conducenti;
    }

    /**
     * @return the veicoli
     */
    public Set getVeicoli() {
        return veicoli;
    }

    /**
     * @param veicoli the veicoli to set
     */
    public void setVeicoli(Set veicoli) {
        this.veicoli = veicoli;
    }

    /**
     * @return the contratti
     */
    public Set getContratti() {
        return contratti;
    }

    /**
     * @param contratti the contratti to set
     */
    public void setContratti(Set contratti) {
        this.contratti = contratti;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldGestioneDocumentale)) {
            return false;
        }
        MROldGestioneDocumentale castOther = (MROldGestioneDocumentale) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    /**
     * @return the pathFile
     */
    public String getPathFile() {
        return pathFile;
    }

    /**
     * @param pathFile the pathFile to set
     */
    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    /**
     * @return the tipo
     */
    public MROldDocumentoContratto getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(MROldDocumentoContratto tipo) {
        this.tipo = tipo;
    }

    public void setProtocolloArchiviazione(String protocolloArchiviazione) {
        this.protocolloArchiviazione = protocolloArchiviazione;
    }

    public String getProtocolloArchiviazione() {
        return protocolloArchiviazione;
    }

    public Set<MROldPrenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(Set<MROldPrenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    public Set<MROldDocumentoFiscale> getFatture() {
        return fatture;
    }

    public void setFatture(Set<MROldDocumentoFiscale> fatture) {
        this.fatture = fatture;
    }

    public Set<MROldPreventivo> getPreventivi() {
        return preventivi;
    }

    public void setPreventivi(Set<MROldPreventivo> preventivi) {
        this.preventivi = preventivi;
    }

}
