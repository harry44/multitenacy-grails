package it.myrent.ee.db;

import it.aessepi.utils.Parameters;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Map;

/** @author Hibernate CodeGenerator */
public class MROldAffiliato implements it.aessepi.utils.db.PersistentInstance {
    
    public static final Boolean NOLEGGIATORE = Boolean.TRUE;
    public static final Boolean AFFILIATO = Boolean.FALSE;

    private Integer id;

    private String ragioneSociale;

    private String via;

    private String numero;

    private String cap;

    private String citta;

    private String provincia;

    private String partitaIva;

    private String telefono1;

    private String telefono2;

    private String cellulare;
    
    private String nazione;
    
    private String email;
    
    private String annotazioni;
    
    private String codiceFiscale;
    
    private Boolean tipoAffiliato;
    
    private Map numerazioni;
    //add these new column for exporting data to web portal
    private String affiliatoExportId;
    private Boolean isExported;
    private Boolean isUpdated;

    private String nomeTrasmittente;
    private String idPaeseTrasmittente;
    private String idCodiceTrasmittente;
    private String emailTrasmittente;
    private String telefonoTrasmittente;
    private String regimeFiscale;
    private String progressivo;
    private String codiceCliente;
    private String passwordCodiceCliente;
    private String codiceAttivazioneFE;
    private Boolean invioTest;
    private String pathSalvataggio;

    private MROldBankAccount bankAccount;
    private String nomeTerzoIntermediario;
    private String idPaeseTerzoIntermediario;
    private String idCodiceTerzoIntermediario;
    private String emailTerzoIntermediario;
    private String telefonoTerzoIntermediario;

    public String getAffiliatoExportId() {
        return affiliatoExportId;
    }

    public void setAffiliatoExportId(String affiliatoExportId) {
        this.affiliatoExportId = affiliatoExportId;
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
    
    public MROldAffiliato() {
        setTipoAffiliato(AFFILIATO);
    }
    
    /**
     *Costruttore per le query dinamiche dentro CalendarioAuto
     */
    public MROldAffiliato(Integer id, Boolean tipoAffiliato) {
        setId(id);
        setTipoAffiliato(tipoAffiliato);
    }
    
    public static MROldAffiliato getNoleggiatore() {
        return ((User)Parameters.getUser()).getAffiliato();
    }

    public Integer getId() {
        return this.id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getRagioneSociale() {
        return this.ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getVia() {
        return this.via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCap() {
        return this.cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCitta() {
        return this.citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getProvincia() {
        return this.provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getPartitaIva() {
        return this.partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }
    
    public String getTelefono1() {
        return this.telefono1;
    }

    public void setTelefono1(String telefono) {
        this.telefono1 = telefono;
    }
    
    public String getTelefono2() {
        return this.telefono2;
    }

    public void setTelefono2(String telefono) {
        this.telefono2 = telefono;
    }
    
    
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNazione() {
        return this.nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }
    
    public String getCellulare() {
        return this.cellulare;
    }

    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }
    
    public String getCodiceFiscale() {
        return this.codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }

    public String getAnnotazioni() {
        return annotazioni;
    }

    public void setAnnotazioni(String annotazioni) {
        this.annotazioni = annotazioni;
    }
    
    public String toString() {
        return ragioneSociale != null ? ragioneSociale : new String();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MROldAffiliato) ) return false;
        MROldAffiliato castOther = (MROldAffiliato) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

    public Boolean getTipoAffiliato() {
        return tipoAffiliato;
    }

    public void setTipoAffiliato(Boolean tipoAffiliato) {
        this.tipoAffiliato = tipoAffiliato;
    }

    public Map getNumerazioni() {
        return numerazioni;
    }

    public void setNumerazioni(Map numerazioni) {
        this.numerazioni = numerazioni;
    }

    public String getNomeTrasmittente() {
        return nomeTrasmittente;
    }

    public void setNomeTrasmittente(String nomeTrasmittente) {
        this.nomeTrasmittente = nomeTrasmittente;
    }

    public String getIdPaeseTrasmittente() {
        return idPaeseTrasmittente;
    }

    public void setIdPaeseTrasmittente(String idPaeseTrasmittente) {
        this.idPaeseTrasmittente = idPaeseTrasmittente;
    }

    public String getIdCodiceTrasmittente() {
        return idCodiceTrasmittente;
    }

    public void setIdCodiceTrasmittente(String idCodiceTrasmittente) {
        this.idCodiceTrasmittente = idCodiceTrasmittente;
    }

    public String getEmailTrasmittente() {
        return emailTrasmittente;
    }

    public void setEmailTrasmittente(String emailTrasmittente) {
        this.emailTrasmittente = emailTrasmittente;
    }

    public String getTelefonoTrasmittente() {
        return telefonoTrasmittente;
    }

    public void setTelefonoTrasmittente(String telefonoTrasmittente) {
        this.telefonoTrasmittente = telefonoTrasmittente;
    }

    public String getRegimeFiscale() {
        return regimeFiscale;
    }

    public void setRegimeFiscale(String regimeFiscale) {
        this.regimeFiscale = regimeFiscale;
    }

    public String getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(String progressivo) {
        this.progressivo = progressivo;
    }

    public String getCodiceCliente() {
        return codiceCliente;
    }

    public void setCodiceCliente(String codiceCliente) {
        this.codiceCliente = codiceCliente;
    }

    public String getPasswordCodiceCliente() {
        return passwordCodiceCliente;
    }

    public void setPasswordCodiceCliente(String passwordCodiceCliente) {
        this.passwordCodiceCliente = passwordCodiceCliente;
    }

    public String getCodiceAttivazioneFE() {
        return codiceAttivazioneFE;
    }

    public void setCodiceAttivazioneFE(String codiceAttivazioneFE) {
        this.codiceAttivazioneFE = codiceAttivazioneFE;
    }

    public Boolean getInvioTest() {
        return invioTest;
    }

    public void setInvioTest(Boolean invioTest) {
        this.invioTest = invioTest;
    }

    public String getPathSalvataggio() {
        return pathSalvataggio;
    }

    public void setPathSalvataggio(String pathSalvataggio) {
        this.pathSalvataggio = pathSalvataggio;
    }

    public MROldBankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(MROldBankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getNomeTerzoIntermediario() {
        return nomeTerzoIntermediario;
    }

    public void setNomeTerzoIntermediario(String nomeTerzoIntermediario) {
        this.nomeTerzoIntermediario = nomeTerzoIntermediario;
    }

    public String getIdPaeseTerzoIntermediario() {
        return idPaeseTerzoIntermediario;
    }

    public void setIdPaeseTerzoIntermediario(String idPaeseTerzoIntermediario) {
        this.idPaeseTerzoIntermediario = idPaeseTerzoIntermediario;
    }

    public String getIdCodiceTerzoIntermediario() {
        return idCodiceTerzoIntermediario;
    }

    public void setIdCodiceTerzoIntermediario(String idCodiceTerzoIntermediario) {
        this.idCodiceTerzoIntermediario = idCodiceTerzoIntermediario;
    }

    public String getEmailTerzoIntermediario() {
        return emailTerzoIntermediario;
    }

    public void setEmailTerzoIntermediario(String emailTerzoIntermediario) {
        this.emailTerzoIntermediario = emailTerzoIntermediario;
    }

    public String getTelefonoTerzoIntermediario() {
        return telefonoTerzoIntermediario;
    }

    public void setTelefonoTerzoIntermediario(String telefonoTerzoIntermediario) {
        this.telefonoTerzoIntermediario = telefonoTerzoIntermediario;
    }

}
