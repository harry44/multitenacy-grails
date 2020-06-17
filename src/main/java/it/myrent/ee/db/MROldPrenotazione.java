package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.beans.FormattedDate;
import java.util.Date;
import it.aessepi.utils.db.PersistentInstance;
import it.myrent.ee.interfaces.DocumentoFirmabileInterface;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/** @author Hibernate CodeGenerator */
public class MROldPrenotazione implements PersistentInstance, Loggable, DocumentoFirmabileInterface,Documentable {
    private Integer id;
    private MROldMovimentoAuto movimento;
    private MROldNumerazione numerazione;
    private String prefisso;
    private Integer numero;
    private Date data;
    private Integer anno;
    private String codice;
    private MROldClienti cliente;
    private MROldConducenti conducente1;
    private MROldConducenti conducente2;
    private MROldConducenti conducente3;
    private MROldSede sedeUscita;
    private Date inizio;
    private MROldSede sedeRientroPrevisto;
    private Date fine;
    private MROldTariffa tariffa;
    private MROldPagamento pagamento;
    private MROldGruppo gruppo;
    private String note;
    private MROldAffiliato affiliato;
    private User userApertura;
    private MROldCommissione commissione;
    private Double scontoPercentuale;
    private Boolean usata;
    private Boolean annullata;
    private Boolean rifiutata;
    private Boolean noShow;
    private Boolean confermata;
    private MROldGruppo gruppoAssegnato;
    private MROldCurrencyConversionRate conversionRate;

    private List<MROldCustomerSplitPayment> customerSplitPayment;
    private Double cauzione;
    private Double noleggio;
    private Double saldoCauzione;
    private Double saldoNoleggio;
    private Double saldoFatture;
    private Double saldoAcconti;
    private Garanzia garanzia1;
    private Garanzia garanzia2;
    private String noteGaranzia;

    private Set<MROldDocumentoFiscale> documentiFiscali;
    private Set<MROldPrimanota> primenote;
    private Set<MROldPartita> partite;

    //MyRent Signature
    private String nomeFirmatario;
    private String luogoFirma;
    private String annotazioniFirma;
    private String nomeUtenteCreatore;
    private Date dataFirmaCerta;
    private Boolean documentoFirmato; //accerta l'avvenuta corretta archiviazione sulla gestione documentale
    private Set documenti;

    private Double noleggioPPay;

    private Double noleggioNoVat;
    private Double noleggioPPayNoVat;

    private List prenotazioneRighe;
    private List prenotazioneRighePPay;
    private MROldRentalType rentalType;
    private String operatorName;
    private Integer cancellingReason;
    //Madhvendra(for Postgres version)
    private Integer id_prenotazioni;
    private Boolean isOnRequest;
    private Boolean isAccepted;
    private AgreementWebCode agreementWebCode;
    private String productNameDesc;
    private Double scontoExtraPrepay;

    private MROldFornitori agent;

    private Date xmlCreated;
    private String xmlCreatedBy;
    private Double xmlCreatedTotalPrice;
    private Double xmlCreatedPriceExtra;
    private Double xmlCreatedRentalPrice;
    private Date xmlDeleted;
    private String xmlDeletedBy;

    private Boolean splitPayment;
    private Boolean privacyMessage1;
    private Boolean privacyMessage2;
    private String splitPaymentAmount;
    private String splitPaymentVat;

    private String splitPaymentPlateNumber;
    private String splitPaymentInsRefNr;
    public Date getPaymentRequesTimestamp() {
        return paymentRequesTimestamp;
    }

    public void setPaymentRequesTimestamp(Date paymentRequesTimestamp) {
        this.paymentRequesTimestamp = paymentRequesTimestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentLinkPaypal() {
        return paymentLinkPaypal;
    }

    public void setPaymentLinkPaypal(String paymentLinkPaypal) {
        this.paymentLinkPaypal = paymentLinkPaypal;
    }

    public String getPaymentLinkUnicredit() {
        return paymentLinkUnicredit;
    }

    public void setPaymentLinkUnicredit(String paymentLinkUnicredit) {
        this.paymentLinkUnicredit = paymentLinkUnicredit;
    }

    private  Date paymentRequesTimestamp;
    private String transactionId;
    private String paymentLinkPaypal;
    private String paymentLinkUnicredit;

    public Integer getId_prenotazioni() {
        return id_prenotazioni;
    }

    public void setId_prenotazioni(Integer id_prenotazioni) {
        this.id_prenotazioni = id_prenotazioni;
    }
    public Boolean getIsOnRequest() {
        return isOnRequest;
    }

    public void setIsOnRequest(Boolean isOnRequest) {
        this.isOnRequest = isOnRequest;
    }

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }
    public AgreementWebCode getAgreementWebCode() {
        return agreementWebCode;
    }

    public void setAgreementWebCode(AgreementWebCode agreementWebCode) {
        this.agreementWebCode = agreementWebCode;
    }
    ///
    public Integer getCancellingReason() {
		return cancellingReason;
	}

	public void setCancellingReason(Integer cancellingReason) {
		this.cancellingReason = cancellingReason;
	}

	public String getOperatorName() {
        return this.operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    public MROldPrenotazione() {
        setCodice(DigestUtils.md5Hex(String.valueOf(System.currentTimeMillis())).toUpperCase().substring(0, 8));
    }

    public MROldPrenotazione(
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
            Date fine,
            User userApertura) {
        this();
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
        setUserApertura(userApertura);
        setAnnullata(Boolean.FALSE);
        setRifiutata(Boolean.FALSE);
        setNoShow(Boolean.FALSE);
        setUsata(Boolean.FALSE);
        setConfermata(Boolean.FALSE);
    }

    public MROldPrenotazione(
            MROldNumerazione numerazione,
            String prefisso,
            Integer numero,
            Date data,
            Integer anno,
            MROldAffiliato affiliato,
            MROldGruppo gruppo,
            MROldGruppo gruppoAssegnato,
            MROldSede sedeUscita,
            MROldSede sedeRientroPrevisto,
            Date inizio,
            Date fine,
            User userApertura) {
        this();
        setNumerazione(numerazione);
        setPrefisso(prefisso);
        setNumero(numero);
        setData(data);
        setAnno(anno);
        setAffiliato(affiliato);
        setGruppo(gruppo);
        setGruppoAssegnato(gruppoAssegnato);
        setSedeUscita(sedeUscita);
        setSedeRientroPrevisto(sedeRientroPrevisto);
        setInizio(inizio);
        setFine(fine);
        setUserApertura(userApertura);
        setAnnullata(Boolean.FALSE);
        setRifiutata(Boolean.FALSE);
        setNoShow(Boolean.FALSE);
        setUsata(Boolean.FALSE);
        setConfermata(Boolean.FALSE);
    }

    public String[] getLoggableFields() {
        return new String[]{
                    "commissione", // NOI18N
                    "movimento", // NOI18N
                    "codice", // NOI18N
                    "cliente", // NOI18N
                    "conducente1", // NOI18N
                    "conducente2", // NOI18N
                    "conducente3", // NOI18N
                    "sedeUscita", // NOI18N
                    "inizio", // NOI18N
                    "sedeRientroPrevisto", // NOI18N
                    "fine", // NOI18N
                    "tariffa", // NOI18N
                    "pagamento", // NOI18N
                    "gruppo", // NOI18N
                    "gruppoAssegnato", // NOI18N
                    "scontoPercentuale", // NOI18N
                    "usata", // NOI18N
                    "annullata", // NOI18N
                    "rifiutata", // NOI18N
                    "noShow", // NOI18N
                    "confermata", // NOI18N
                    "note" // NOI18N
                };
    }

    public String[] getLoggableLabels() {
        return new String[]{
                    bundle.getString("Prenotazione.logFonteCommissionabile"),
                    bundle.getString("Prenotazione.logMovimento"),
                    bundle.getString("Prenotazione.logCodice"),
                    bundle.getString("Prenotazione.logCliente"),
                    bundle.getString("Prenotazione.logConducente1"),
                    bundle.getString("Prenotazione.logConducente2"),
                    bundle.getString("Prenotazione.logConducente3"),
                    bundle.getString("Prenotazione.logSedeUscita"),
                    bundle.getString("Prenotazione.logInizio"),
                    bundle.getString("Prenotazione.logSedeRientroPrevisto"),
                    bundle.getString("Prenotazione.logFine"),
                    bundle.getString("Prenotazione.logTariffa"),
                    bundle.getString("Prenotazione.logPagamento"),
                    bundle.getString("Prenotazione.logGruppo"),
                    bundle.getString("Prenotazione.logGruppoAssegnato"),
                    bundle.getString("Prenotazione.logSconto"),
                    bundle.getString("Prenotazione.logUsata"),
                    bundle.getString("Prenotazione.logAnnullata"),
                    bundle.getString("Prenotazione.logRifiutata"),
                    bundle.getString("Prenotazione.logNoShow"),
                    bundle.getString("Prenotazione.logConfermata"),
                    bundle.getString("Prenotazione.logNote")
                };
    }

    @Override
    public String getEntityName() {
        return "MROldPrenotazione"; // NOI18N
    }

    @Override
    public Integer getEntityId() {
        return getId();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(getCliente()).append(getGruppo()).toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other instanceof MROldPrenotazione) {
            return new EqualsBuilder().append(getId(), ((MROldPrenotazione) other).getId()).isEquals();
        } else {
            return false;
        }

    }
    
    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldRentalType getRentalType() {
        return rentalType;
    }

    public void setRentalType(MROldRentalType rentalType) {
        this.rentalType = rentalType;
    }

    
    public MROldClienti getCliente() {
        return cliente;
    }

    public void setCliente(MROldClienti cliente) {
        this.cliente = cliente;
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

    public MROldGruppo getGruppo() {
        return gruppo;
    }

    public void setGruppo(MROldGruppo gruppo) {
        this.gruppo = gruppo;
    }

    public MROldSede getSedeUscita() {
        return sedeUscita;
    }

    public void setSedeUscita(MROldSede sedeUscita) {
        this.sedeUscita = sedeUscita;
    }

    public Date getInizio() {
        return inizio;
    }

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }

    public MROldSede getSedeRientroPrevisto() {
        return sedeRientroPrevisto;
    }

    public void setSedeRientroPrevisto(MROldSede sedeRientroPrevisto) {
        this.sedeRientroPrevisto = sedeRientroPrevisto;
    }

    public Date getFine() {
        return fine;
    }

    public void setFine(Date fine) {
        this.fine = fine;
    }

    public MROldTariffa getTariffa() {
        return tariffa;
    }

    public void setTariffa(MROldTariffa tariffa) {
        this.tariffa = tariffa;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String annotazioni) {
        this.note = annotazioni;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public MROldAffiliato getAffiliato() {
        return affiliato;
    }

    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }

    /**
     * one-to-one. Non lazy
     */
    public MROldMovimentoAuto getMovimento() {
        return movimento;
    }

    public void setMovimento(MROldMovimentoAuto movimento) {
        this.movimento = movimento;
    }

    public Boolean getAnnullata() {
        return annullata;
    }

    public void setAnnullata(Boolean annullata) {
        this.annullata = annullata;
    }

    public Boolean getRifiutata() {
        return rifiutata;
    }

    public void setRifiutata(Boolean rifiutata) {
        this.rifiutata = rifiutata;
    }

    public Boolean getNoShow() {
        return noShow;
    }

    public void setNoShow(Boolean noShow) {
        this.noShow = noShow;
    }

    public User getUserApertura() {
        return userApertura;
    }

    public void setUserApertura(User userApertura) {
        this.userApertura = userApertura;
    }

    /**
     * one-to-one. Non lazy.
     */
    public MROldCommissione getCommissione() {
        return commissione;
    }

    public void setCommissione(MROldCommissione commissione) {
        this.commissione = commissione;
    }

    public Double getScontoPercentuale() {
        return scontoPercentuale;
    }

    public void setScontoPercentuale(Double scontoPercentuale) {
        this.scontoPercentuale = scontoPercentuale;
    }

    public Boolean getConfermata() {
        return confermata;
    }

    public void setConfermata(Boolean confermata) {
        this.confermata = confermata;
    }

    public MROldPagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(MROldPagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Boolean getUsata() {
        return usata;
    }

    public void setUsata(Boolean usata) {
        this.usata = usata;
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

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public void setGruppoAssegnato(MROldGruppo gruppoAssegnato) {
        this.gruppoAssegnato = gruppoAssegnato;
    }

    public MROldGruppo getGruppoAssegnato() {
        return gruppoAssegnato;
    }

    /**
     * @return the documenti
     */
    @Override
    public Set getDocumenti() {
        return documenti;
    }

    /**
     * @param documenti the documenti to set
     */
    @Override
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
         return new StringBuffer().append(bundle.getString("Prenotazione.testo1")).
                append(" ").
                append(bundle.getString("Prenotazione.testo3")).
                append(" ").
                append(getNumero()).
                append(bundle.getString("Prenotazione.testo2")).
                append(" ").
                append(FormattedDate.format(getData())).toString();
    }

    @Override
    public Class getDocumentableClass() {
        return MROldPrenotazione.class;
    }

    @Override
    public String getKeywords() {
        return new StringBuffer().
                append(bundle.getString("Prenotazione.testo1")).
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

    public Double getCauzione() {
        return cauzione;
    }

    public void setCauzione(Double cauzione) {
        this.cauzione = cauzione;
    }

    public Double getNoleggio() {
        return noleggio;
    }

    public void setNoleggio(Double noleggio) {
        this.noleggio = noleggio;
    }

    public Double getSaldoCauzione() {
        return saldoCauzione;
    }

    public void setSaldoCauzione(Double saldoCauzione) {
        this.saldoCauzione = saldoCauzione;
    }

    public Double getSaldoNoleggio() {
        return saldoNoleggio;
    }

    public void setSaldoNoleggio(Double saldoNoleggio) {
        this.saldoNoleggio = saldoNoleggio;
    }

    public Double getSaldoFatture() {
        return saldoFatture;
    }

    public void setSaldoFatture(Double saldoFatture) {
        this.saldoFatture = saldoFatture;
    }

    public Double getSaldoAcconti() {
        return saldoAcconti;
    }

    public void setSaldoAcconti(Double saldoAcconti) {
        this.saldoAcconti = saldoAcconti;
    }

    public Garanzia getGaranzia1() {
        return garanzia1;
    }

    public void setGaranzia1(Garanzia garanzia1) {
        this.garanzia1 = garanzia1;
    }

    public Garanzia getGaranzia2() {
        return garanzia2;
    }

    public void setGaranzia2(Garanzia garanzia2) {
        this.garanzia2 = garanzia2;
    }

    public Set<MROldDocumentoFiscale> getDocumentiFiscali() {
        return documentiFiscali;
    }

    public void setDocumentiFiscali(Set<MROldDocumentoFiscale> documentiFiscali) {
        this.documentiFiscali = documentiFiscali;
    }

    public Set<MROldPrimanota> getPrimenote() {
        return primenote;
    }

    public void setPrimenote(Set<MROldPrimanota> primenote) {
        this.primenote = primenote;
    }

    public Set<MROldPartita> getPartite() {
        return partite;
    }

    public void setPartite(Set<MROldPartita> partite) {
        this.partite = partite;
    }

    public String getNoteGaranzia() {
        return noteGaranzia;
    }

    public void setNoteGaranzia(String noteGaranzia) {
        this.noteGaranzia = noteGaranzia;
    }

    public void setNoleggioPPay(Double noleggioPPay) {
        this.noleggioPPay = noleggioPPay;
    }

    public Double getNoleggioPPay() {
        return noleggioPPay;
    }


    /**
     * @return the noleggioNoVat
     */
    public Double getNoleggioNoVat() {
        return noleggioNoVat;
    }

    /**
     * @param noleggioNoVat the noleggioNoVat to set
     */
    public void setNoleggioNoVat(Double noleggioNoVat) {
        this.noleggioNoVat = noleggioNoVat;
    }

    /**
     * @return the noleggioPPayNoVat
     */
    public Double getNoleggioPPayNoVat() {
        return noleggioPPayNoVat;
    }

    /**
     * @param noleggioPPayNoVat the noleggioPPayNoVat to set
     */
    public void setNoleggioPPayNoVat(Double noleggioPPayNoVat) {
        this.noleggioPPayNoVat = noleggioPPayNoVat;
    }

    /**
     * @return the prenotazioneRighe
     */
    public List getPrenotazioneRighe() {
        return prenotazioneRighe;
    }

    /**
     * @param prenotazioneRighe the prenotazioneRighe to set
     */
    public void setPrenotazioneRighe(List prenotazioneRighe) {
        this.prenotazioneRighe = prenotazioneRighe;
    }

    /**
     * @return the prenotazioneRighePPay
     */
    public List getPrenotazioneRighePPay() {
        return prenotazioneRighePPay;
    }

    /**
     * @param prenotazioneRighePPay the prenotazioneRighePPay to set
     */
    public void setPrenotazioneRighePPay(List prenotazioneRighePPay) {
        this.prenotazioneRighePPay = prenotazioneRighePPay;
    }
    public String getProductNameDesc() {
        return productNameDesc;
    }

    /**
     * @param productNameDesc the productNameDesc to set
     */
    public void setProductNameDesc(String productNameDesc) {
        this.productNameDesc = productNameDesc;
    }

    public Double getScontoExtraPrepay() {
        return scontoExtraPrepay;
    }

    public void setScontoExtraPrepay(Double scontoExtraPrepay) {
        this.scontoExtraPrepay = scontoExtraPrepay;
    }

    public List<MROldCustomerSplitPayment> getCustomerSplitPayment() {
        return customerSplitPayment;
    }

    public void setCustomerSplitPayment(List<MROldCustomerSplitPayment> customerSplitPayment) {
        this.customerSplitPayment = customerSplitPayment;
    }

    public Boolean getSplitPayment() {
        return splitPayment;
    }

    public void setSplitPayment(Boolean splitPayment) {
        this.splitPayment = splitPayment;
    }

    public String getSplitPaymentAmount() {
        return splitPaymentAmount;
    }

    public void setSplitPaymentAmount(String splitPaymentAmount) {
        this.splitPaymentAmount = splitPaymentAmount;
    }

    public String getSplitPaymentVat() {
        return splitPaymentVat;
    }

    public void setSplitPaymentVat(String splitPaymentVat) {
        this.splitPaymentVat = splitPaymentVat;
    }

    public String getSplitPaymentPlateNumber() {
        return splitPaymentPlateNumber;
    }

    public void setSplitPaymentPlateNumber(String splitPaymentPlateNumber) {
        this.splitPaymentPlateNumber = splitPaymentPlateNumber;
    }

    public String getSplitPaymentInsRefNr() {
        return splitPaymentInsRefNr;
    }

    public void setSplitPaymentInsRefNr(String splitPaymentInsRefNr) {
        this.splitPaymentInsRefNr = splitPaymentInsRefNr;
    }


    public Boolean getPrivacyMessage1() {
        return privacyMessage1;
    }

    public void setPrivacyMessage1(Boolean privacyMessage1) {
        this.privacyMessage1 = privacyMessage1;
    }

    public Boolean getPrivacyMessage2() {
        return privacyMessage2;
    }

    public void setPrivacyMessage2(Boolean privacyMessage2) {
        this.privacyMessage2 = privacyMessage2;
    }

    public Date getXmlCreated() {
        return xmlCreated;
    }

    public void setXmlCreated(Date xmlCreated) {
        this.xmlCreated = xmlCreated;
    }

    public String getXmlCreatedBy() {
        return xmlCreatedBy;
    }

    public void setXmlCreatedBy(String xmlCreatedBy) {
        this.xmlCreatedBy = xmlCreatedBy;
    }

    public Double getXmlCreatedTotalPrice() {
        return xmlCreatedTotalPrice;
    }

    public void setXmlCreatedTotalPrice(Double xmlCreatedTotalPrice) {
        this.xmlCreatedTotalPrice = xmlCreatedTotalPrice;
    }

    public Double getXmlCreatedPriceExtra() {
        return xmlCreatedPriceExtra;
    }

    public void setXmlCreatedPriceExtra(Double xmlCreatedPriceExtra) {
        this.xmlCreatedPriceExtra = xmlCreatedPriceExtra;
    }

    public Double getXmlCreatedRentalPrice() {
        return xmlCreatedRentalPrice;
    }

    public void setXmlCreatedRentalPrice(Double xmlCreatedRentalPrice) {
        this.xmlCreatedRentalPrice = xmlCreatedRentalPrice;
    }

    public Date getXmlDeleted() {
        return xmlDeleted;
    }

    public void setXmlDeleted(Date xmlDeleted) {
        this.xmlDeleted = xmlDeleted;
    }

    public String getXmlDeletedBy() {
        return xmlDeletedBy;
    }

    public void setXmlDeletedBy(String xmlDeletedBy) {
        this.xmlDeletedBy = xmlDeletedBy;
    }

    public MROldFornitori getAgent() {
        return agent;
    }

    public void setAgent(MROldFornitori agent) {
        this.agent = agent;
    }
}
