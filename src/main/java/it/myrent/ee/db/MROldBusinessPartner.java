package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/** @author Hibernate CodeGenerator */
public class MROldBusinessPartner implements it.aessepi.utils.db.PersistentInstance, MROldContribuente {

    public MROldBusinessPartner() {
        setCliente(false);
        setFornitore(false);
        setConducente(false);
        setPersonaFisica(true);
        setDittaIndividuale(false);
        setSesso(SESSO_MASCHILE);
        setSplitPayments(new TreeSet<MROldSplitPayment>());
        setCompleto(false);
    }
    
    public MROldBusinessPartner(Integer id) {
        this();
        setId(id);        
    }
    
    private Integer id;
    private Integer codice;
    
    private Boolean cliente;
    private Boolean fornitore;
    private Boolean conducente;

    /** Dati anagrafici **/
    private Boolean personaFisica;
    private Boolean dittaIndividuale;
    private Boolean completo;

    private Boolean pubblicaAmministrazione;

    private String ragioneSociale;
    private String cognome;
    private String nome;

    private Boolean associazione;

    /** Indirizzo **/
    private String via;
    private String numero;
    private String cap;
    private String citta;
    private String provincia;
    private String nazione;

    /** Contatti **/
    private String telefono1;
    private String telefono2;
    private String cellulare;
    private String email;

    /** Dati fiscali **/
    private String partitaIva;
    private String codiceFiscale;

    /** Dati aggiuntivi pers. fisica **/
    private Date dataNascita;
    private String luogoNascita;
    private String provinciaNascita;
    private String nazioneNascita;
    private Boolean sesso;

    /** Dati contabili **/
    private MROldPagamento pagamento;
    private Set coordinateBancarie;
    private String listaNera;

    /**Codice NSA**/
    private String codiceNSA;
    /** Note **/
    private String promemoria;

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    private String document;

     //add these new column for exporting data to web portal
    private String customerExportId;
    private String supplierExportId;
    private Boolean isUpdated;
    private Map carteDiCredito;
    private String typeOfCustomer;


    private MROldFornitori agent;
    private Set<MROldSplitPayment> splitPayments;


    private Set<MROldSede> location;
    public static final Boolean SESSO_MASCHILE = Boolean.TRUE;
    public static final Boolean SESSO_FEMMINILE = Boolean.FALSE;
    public static final StandardToStringStyle TO_STRING_STYLE = new StandardToStringStyle();

    static {
        TO_STRING_STYLE.setContentEnd(new String());
        TO_STRING_STYLE.setContentStart(new String());
        TO_STRING_STYLE.setNullText(new String());
        TO_STRING_STYLE.setUseIdentityHashCode(false);
        TO_STRING_STYLE.setUseClassName(false);
        TO_STRING_STYLE.setUseFieldNames(false);
        TO_STRING_STYLE.setFieldSeparator(" "); //NOI18N
    }

    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE).append(getRagioneSociale()).append(getCognome()).append(getNome()).toString().trim();
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldBusinessPartner)) {
            return false;
        }
        MROldBusinessPartner castOther = (MROldBusinessPartner) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public Integer getId() {
        return this.id;
    }

    private void setId(Integer id) {
        this.id = id;
    }
    
    public Map getCarteDiCredito() {
        return carteDiCredito;
    }
     public void setCarteDiCredito(Map carteDiCredito) {
        this.carteDiCredito = carteDiCredito;
    }
    public String getRagioneSociale() {
        return this.ragioneSociale;
    }

    public String getCustomerExportId() {
        return customerExportId;
    }

    public void setCustomerExportId(String customerExportId) {
        this.customerExportId = customerExportId;
    }

    public Boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public String getSupplierExportId() {
        return supplierExportId;
    }

    public void setSupplierExportId(String supplierExportId) {
        this.supplierExportId = supplierExportId;
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

    public String getPromemoria() {
        return this.promemoria;
    }

    public void setPromemoria(String promemoria) {
        this.promemoria = promemoria;
    }

    public void setPagamento(MROldPagamento pagamento) {
        this.pagamento = pagamento;
    }

    public MROldPagamento getPagamento() {
        return pagamento;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getLuogoNascita() {
        return luogoNascita;
    }

    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    public String getProvinciaNascita() {
        return provinciaNascita;
    }

    public void setProvinciaNascita(String provinciaNascita) {
        this.provinciaNascita = provinciaNascita;
    }

    public Boolean getSesso() {
        return sesso;
    }

    public void setSesso(Boolean sesso) {
        this.sesso = sesso;
    }

    public Boolean getPersonaFisica() {
        return personaFisica;
    }

    public void setPersonaFisica(Boolean personaFisica) {
        this.personaFisica = personaFisica;
    }

    public Boolean getDittaIndividuale() {
        return dittaIndividuale;
    }

    public void setDittaIndividuale(Boolean dittaIndividuale) {
        this.dittaIndividuale = dittaIndividuale;
    }

    public Integer getCodice() {
        return codice;
    }

    public void setCodice(Integer codice) {
        this.codice = codice;
    }

    public Set getCoordinateBancarie() {
        return coordinateBancarie;
    }

    public void setCoordinateBancarie(Set coordinateBancarie) {
        this.coordinateBancarie = coordinateBancarie;
    }

    public Boolean getCompleto() {
        return completo;
    }

    public void setCompleto(Boolean completo) {
        this.completo = completo;
    }

    public Boolean getCliente() {
        return cliente;
    }

    public void setCliente(Boolean cliente) {
        this.cliente = cliente;
    }

    public Boolean getFornitore() {
        return fornitore;
    }

    public void setFornitore(Boolean fornitore) {
        this.fornitore = fornitore;
    }

    public Boolean getConducente() {
        return conducente;
    }

    public void setConducente(Boolean conducente) {
        this.conducente = conducente;
    }

    public String getListaNera() {
        return listaNera;
    }

    public void setListaNera(String listaNera) {
        this.listaNera = listaNera;
    }

    /**
     * @return the nazioneNascita
     */
    public String getNazioneNascita() {
        return nazioneNascita;
    }

    /**
     * @param nazioneNascita the nazioneNascita to set
     */
    public void setNazioneNascita(String nazioneNascita) {
        this.nazioneNascita = nazioneNascita;
    }

    public String getNominativo() {
        if (!getPersonaFisica()) {
            return getRagioneSociale();
        } else {
            if (getDittaIndividuale()) {
                return getRagioneSociale();
            } else {
                return getCognome() + " " + getNome();
            }
        }
    }

    public String getCodiceNSA() {
        return codiceNSA;
    }

    public void setCodiceNSA(String codiceNSA) {
        this.codiceNSA = codiceNSA;
    }

    public Set<MROldSplitPayment> getSplitPayments() {
        return splitPayments;
    }

    public void setSplitPayments(Set<MROldSplitPayment> splitPayments) {
        this.splitPayments = splitPayments;
    }
    public String getTypeOfCustomer() {
        return typeOfCustomer;
    }

    public void setTypeOfCustomer(String typeOfCustomer) {
        this.typeOfCustomer = typeOfCustomer;
    }

    public Boolean getPubblicaAmministrazione() {
        return pubblicaAmministrazione;
    }

    public void setPubblicaAmministrazione(Boolean pubblicaAmministrazione) {
        this.pubblicaAmministrazione = pubblicaAmministrazione;
    }

    public Boolean getAssociazione() {
        return associazione;
    }

    public void setAssociazione(Boolean associazione) {
        this.associazione = associazione;
    }


    public MROldFornitori getAgent() {
        return agent;
    }

    public void setAgent(MROldFornitori agent) {
        this.agent = agent;
    }
    public Set<MROldSede> getLocation() {
        return location;
    }

    public void setLocation(Set<MROldSede> location) {
        this.location = location;
    }

}
