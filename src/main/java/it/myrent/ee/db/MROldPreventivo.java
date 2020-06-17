package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.interfaces.DocumentoFirmabileInterface;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/** @author Hibernate CodeGenerator */
public class MROldPreventivo implements it.aessepi.utils.db.PersistentInstance, DocumentoFirmabileInterface, Loggable,Documentable {
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
    /** identifier field */
    private Integer id;
    
    private MROldNumerazione numerazione;
    private String prefisso;
    private Integer numero;
    private Date data;
    private Integer anno;
    
    private Date inizio;
    private Date fine;
    private Integer kmTotali;    
    private String note;
    private MROldTariffa tariffa;
    private MROldPagamento pagamento;
    private MROldClienti cliente;
    private MROldSede sedeUscita;
    private MROldSede sedeRientroPrevisto;
    private MROldConducenti conducente1;
    private MROldConducenti conducente2;
    private MROldConducenti conducente3;
    private MROldGruppo gruppo;
    private Double scontoPercentuale;
    private MROldCommissione commissione;
    
    private MROldAffiliato affiliato;
    /*
     * Campi non mappati. Usati solo per dati temporanei.
     */
    
    private Double totaleDocumento;
    private Double totaleIva;
    private Double totaleImponibile;
    private List righePreventivo;

    //MyRent Signature
    private String nomeFirmatario;
    private String luogoFirma;
    private String annotazioniFirma;
    private String nomeUtenteCreatore;
    private Date dataFirmaCerta;
    private Boolean documentoFirmato; //accerta l'avvenuta corretta archiviazione sulla gestione documentale
    private Set documenti;
    private MROldCurrencyConversionRate conversionRate;
    
    //@Madhvendra
    private MROldRentalType rentalType;
    private Integer id_preventivo;
    private AgreementWebCode agreementWebCode;

    private MROldFornitori agent;
    //
    /** default constructor. Ricordarsi di impostare l'affiliato. */
    public MROldPreventivo() {
    }
    
    public MROldPreventivo(
            MROldNumerazione numerazione,
            String prefisso,
            Integer numero,
            Date data,
            Integer anno,
            MROldAffiliato affiliato,
            MROldGruppo gruppo,
            MROldSede sedeUscita,
            MROldSede sedeRientroPrevisto,
            Date inizio,
            Date fine
    ) {
        setNumerazione(numerazione);
        setPrefisso(prefisso);
        setNumero(numero);
        setData(data);
        setAnno(anno);
        setAffiliato(affiliato);
        setGruppo(gruppo);
        setSedeUscita(sedeUscita);
        setSedeRientroPrevisto(sedeRientroPrevisto);
        setInizio(inizio);
        setFine(fine);
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getInizio() {
        return this.inizio;
    }
    
    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }
    
    public Date getFine() {
        return this.fine;
    }
    
    public void setFine(Date fine) {
        this.fine = fine;
    }
    
    public Integer getKmTotali() {
        return this.kmTotali;
    }
    public AgreementWebCode getAgreementWebCode() {
        return agreementWebCode;
    }

    public void setAgreementWebCode(AgreementWebCode agreementWebCode) {
        this.agreementWebCode = agreementWebCode;
    }
    public Integer getId_preventivo() {
        return id_preventivo;
    }

    public void setId_preventivo(Integer id_preventivo) {
        this.id_preventivo = id_preventivo;
    }
    public void setKmTotali(Integer kmTotali) {
        this.kmTotali = kmTotali;
    }
    
    public Date getData() {
        return this.data;
    }
    
    public void setData(Date data) {
        this.data = data;
    }
    
    public String getNote() {
        return this.note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    
    public void setTariffa(MROldTariffa tariffa) {
        this.tariffa = tariffa;
    }
    
    public MROldTariffa getTariffa() {
        return tariffa;
    }
    
    public void setCliente(MROldClienti cliente) {
        this.cliente = cliente;
    }
    
    public MROldClienti getCliente() {
        return cliente;
    }
    
    public void setGruppo(MROldGruppo gruppo) {
        this.gruppo = gruppo;
    }
    
    public MROldGruppo getGruppo() {
        return gruppo;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
        .append("id", getId())
        .toString();
    }
    
    public Double getTotaleDocumento() {
        return this.totaleDocumento;
    }

    public void setTotaleDocumento(Double totaleDocumento) {
        this.totaleDocumento = totaleDocumento;
    }

    
    public boolean equals(Object other) {
        if(other != null && other instanceof MROldPreventivo) {
            return new EqualsBuilder().append(this.getId(), ((MROldPreventivo)other).getId()).isEquals();
        } else {
            return false;
        }
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }
    
    public MROldAffiliato getAffiliato() {
        return affiliato;
    }

    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }

    public Double getTotaleIva() {
        return totaleIva;
    }

    public void setTotaleIva(Double totaleIva) {
        this.totaleIva = totaleIva;
    }

    public Double getTotaleImponibile() {
        return totaleImponibile;
    }

    public void setTotaleImponibile(Double totaleImponibile) {
        this.totaleImponibile = totaleImponibile;
    }

    public List getRighePreventivo() {
        return righePreventivo;
    }

    public void setRighePreventivo(List righePreventivo) {
        this.righePreventivo = righePreventivo;
    }

    public MROldConducenti getConducente1() {
        return conducente1;
    }

    public void setConducente1(MROldConducenti conducente1) {
        this.conducente1 = conducente1;
    }

    public MROldConducenti getConducente2() {
        return conducente2;
    }

    public void setConducente2(MROldConducenti conducente2) {
        this.conducente2 = conducente2;
    }

    public MROldConducenti getConducente3() {
        return conducente3;
    }

    public void setConducente3(MROldConducenti conducente3) {
        this.conducente3 = conducente3;
    }

    public MROldSede getSedeUscita() {
        return sedeUscita;
    }

    public void setSedeUscita(MROldSede sedeUscita) {
        this.sedeUscita = sedeUscita;
    }

    public MROldSede getSedeRientroPrevisto() {
        return sedeRientroPrevisto;
    }

    public void setSedeRientroPrevisto(MROldSede sedeRientroPrevisto) {
        this.sedeRientroPrevisto = sedeRientroPrevisto;
    }

    public Double getScontoPercentuale() {
        return scontoPercentuale;
    }

    public void setScontoPercentuale(Double scontoPercentuale) {
        this.scontoPercentuale = scontoPercentuale;
    }

    public MROldPagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(MROldPagamento pagamento) {
        this.pagamento = pagamento;
    }

    /**
     * one-to-one. Non lazy
     */
    public MROldCommissione getCommissione() {
        return commissione;
    }

    public void setCommissione(MROldCommissione commissione) {
        this.commissione = commissione;
    }

    public MROldNumerazione getNumerazione() {
        return numerazione;
    }

    public void setNumerazione(MROldNumerazione numerazione) {
        this.numerazione = numerazione;
    }

    public String getPrefisso() {
        return prefisso;
    }

    public void setPrefisso(String prefisso) {
        this.prefisso = prefisso;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    /**
     * @return the documenti
     */
    public Set getDocumenti() {
        return documenti;
    }

    /**
     * @param documenti the documenti to set
     */
    public void setDocumenti(Set documenti) {
        this.documenti = documenti;
    }

    @Override
    public void setNomeFirmatario(String nomeFirmatario) {
        this.nomeFirmatario = nomeFirmatario;
    }

    @Override
    public String getNomeFirmatario() {
        return nomeFirmatario;
    }

    @Override
    public void setLuogoFirma(String luogoFirma) {
        this.luogoFirma = luogoFirma;
    }

    @Override
    public String getLuogoFirma() {
        return luogoFirma;
    }

    @Override
    public void setAnnotazioniFirma(String annotazioniFirma) {
        this.annotazioniFirma = annotazioniFirma;
    }

    @Override
    public String getAnnotazioniFirma() {
        return annotazioniFirma;
    }

    @Override
    public void setNomeUtenteCreatore(String nomeUtenteCreatore) {
        this.nomeUtenteCreatore = nomeUtenteCreatore;
    }

    @Override
    public String getNomeUtenteCreatore() {
        return nomeUtenteCreatore;
    }

    @Override
    public void setDataFirmaCerta(Date dataFirmaCerta) {
        this.dataFirmaCerta = dataFirmaCerta;
    }

    @Override
    public Date getDataFirmaCerta() {
        return dataFirmaCerta;
    }

    @Override
    public void setDocumentoFirmato(Boolean documentoFirmato) {
        this.documentoFirmato = documentoFirmato;
    }

    @Override
    public Boolean getDocumentoFirmato() {
        return documentoFirmato;
    }

    @Override
    public String getDocumentableName() {
         return new StringBuffer().append(bundle.getString("Preventivo.testo1")).
                append(" ").
                append(bundle.getString("Preventivo.testo3")).
                append(" ").
                append(getNumero()).
                append(bundle.getString("Preventivo.testo2")).
                append(" ").
                append(FormattedDate.format(getData())).toString();
    }

    @Override
    public Class getDocumentableClass() {
        return MROldPreventivo.class;
    }

    @Override
    public String getKeywords() {
        return new StringBuffer().
                append(bundle.getString("Preventivo.testo1")).
                append(" ").
                append(FormattedDate.format(getData())).
                append(" ").
                append(getNumero()).
                toString();
    }

    public MROldCurrencyConversionRate getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(MROldCurrencyConversionRate conversionRate) {
        this.conversionRate = conversionRate;
    }

    public MROldRentalType getRentalType() {
        return this.rentalType;
    }

    public void setRentalType(MROldRentalType rentalType) {
        this.rentalType = rentalType;
    }

    public MROldFornitori getAgent() {
        return agent;
    }

    public void setAgent(MROldFornitori agent) {
        this.agent = agent;
    }

    public String[] getLoggableFields() {
        return new String[]{
                "conducente1", // NOI18N
                "conducente2", // NOI18N
                "conducente3", // NOI18N
                "cliente", // NOI18N
                "tariffa", // NOI18N
                "gruppo", // NOI18N
                "sedeUscita", // NOI18N
                "sedeRientroPrevisto", // NOI18N
                "commissione", // NOI18N
                "pagamento", // NOI18N
                "inizio", // NOI18N
                "fine", // NOI18N
                "scontoPercentuale", // NOI18N
                "note", // NOI18N
                "data" // NOI18N
        };

    }

    public String[] getLoggableLabels() {
        return new String[]{
                bundle.getString("Preventivo.logConducente1"),
                bundle.getString("Preventivo.logConducente2"),
                bundle.getString("Preventivo.logConducente3"),
                bundle.getString("Preventivo.logCliente"),
                bundle.getString("Preventivo.logTariffa"),
                bundle.getString("Preventivo.logGruppo"),
                bundle.getString("Preventivo.logSedeUscita"),
                bundle.getString("Preventivo.logSedeRientroPrevisto"),
                bundle.getString("Preventivo.logCommissione"),
                bundle.getString("Preventivo.logPagamento"),
                bundle.getString("Preventivo.logInizio"),
                bundle.getString("Preventivo.logFine"),
                bundle.getString("Preventivo.logScontoTariffa"),
                bundle.getString("Preventivo.logNote"),
                bundle.getString("Preventivo.logData")
        };
    }

    public String getEntityName() {
        return "MROldPreventivo"; // NOI18N
    }

    public Integer getEntityId() {
        return getId();
    }
    
}
