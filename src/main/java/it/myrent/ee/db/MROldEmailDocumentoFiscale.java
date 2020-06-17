/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import java.util.Date;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author giacomo
 */
public class MROldEmailDocumentoFiscale implements PersistentInstance {
    private Integer id;
    private MROldDocumentoFiscale documentoFiscale;
    private User user;
    private Boolean inviato;
    private String destinatari;
    private String destinatariCc;
    private String destinatariBcc;
    private String emailMittente;
    private Date dataInvio;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldDocumentoFiscale getDocumentoFiscale() {
        return documentoFiscale;
    }

    public void setDocumentoFiscale(MROldDocumentoFiscale documentoFiscale) {
        this.documentoFiscale = documentoFiscale;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getInviato() {
        return inviato;
    }

    public void setInviato(Boolean inviato) {
        this.inviato = inviato;
    }

    public String getDestinatari() {
        return destinatari;
    }

    public void setDestinatari(String destinatari) {
        this.destinatari = destinatari;
    }

    public String getDestinatariCc() {
        return destinatariCc;
    }

    public void setDestinatariCc(String destinatariCc) {
        this.destinatariCc = destinatariCc;
    }

    public String getDestinatariBcc() {
        return destinatariBcc;
    }

    public void setDestinatariBcc(String destinatariBcc) {
        this.destinatariBcc = destinatariBcc;
    }

    public String getEmailMittente() {
        return emailMittente;
    }

    public void setEmailMittente(String emailMittente) {
        this.emailMittente = emailMittente;
    }

    public Date getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(Date dataInvio) {
        this.dataInvio = dataInvio;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }

    @Override
    public String toString() {
        return getEmailMittente();
    }
}
