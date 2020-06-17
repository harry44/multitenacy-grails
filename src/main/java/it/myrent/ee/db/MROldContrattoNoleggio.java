package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.myrent.ee.interfaces.DocumentoFirmabileInterface;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/** @author Hibernate CodeGenerator */
public class MROldContrattoNoleggio implements it.aessepi.utils.db.PersistentInstance, Loggable, DocumentoFirmabileInterface, Documentable {
    
    private Integer id;
    private MROldMovimentoAuto movimento;
    private MROldConducenti conducente1;
    private MROldConducenti conducente2;
    private MROldConducenti conducente3;
    private MROldClienti cliente;
    private MROldTariffa tariffa;
    private MROldGruppo gruppo;
    private MROldSede sedeUscita;
    private MROldSede sedeRientroPrevisto;
    private MROldAffiliato affiliato;
    private Garanzia garanzia1;
    private Garanzia garanzia2;
    private User userApertura;
    private MROldCommissione commissione;
    private MROldPagamento pagamento;
    /** timestamp */
    private Date inizio;
    /** timestamp */
    private Date fine;
    private MROldNumerazione numerazione;
    private Integer anno;
    private Integer month;
    private Integer monthDuration;
    private Boolean isMonthlyRental;
    /** date */
    private Date data;

    private Date rentalCloseDate;
    private String prefisso;
    private Integer numero;
    private Double cauzione;
    private Double noleggio;
    private Double saldoCauzione;
    private Double saldoNoleggio;
    private Double saldoFatture;
    private Double saldoAcconti;
    private Double scontoTariffa;
    private String note;
    private Boolean chiuso;

    private Boolean splitPayment;
    private String splitPaymentAmount;
    private String splitPaymentVat;
    private List<MROldCustomerSplitPayment> customerSplitPayment;

    private Date dataFirmaContratto;

    private Set<MROldDocumentoFiscale> documentiFiscali;
    private Set<MROldRataContratto> fattureRiepilogative;
    private Set<MROldPrimanota> primenote;
    private Set<MROldPartita> partite;
    private Set movimenti;

    private Integer numeroMovimenti;

    private MROldGruppo gruppoAssegnato;

    private MROldFornitori agent;

    //MyRent Signature
    private String nomeFirmatario;
    private String luogoFirma;
    private String annotazioniFirma;
    private String nomeUtenteCreatore;
    private Date dataFirmaCerta;
    private Boolean documentoFirmato; //accerta l'avvenuta corretta archiviazione sulla gestione documentale
    private Set documenti;
    private MROldCurrencyConversionRate conversionRate;

    private MROldNexiTransactionDeposit nexiDeposit;
    private Double noleggioPPay;
    
    private Double noleggioNoVat;
    private Double noleggioPPayNoVat;

    private List contrattoNoleggioRighe;
    private List contrattoNoleggioRighePPay;
   //@madhvendra
    private MROldRentalType rentalType;
    private Boolean privacyMessage1;
    private Boolean privacyMessage2;
    private MROldGruppo gruppo1;
    private Boolean isEprd;
    private Boolean isExtraServ;
    private Boolean isEprdf;
    
    private Boolean isDepositCash;
    private Boolean isDepositCreditCard;
    private Double totalDeposit;
    private Double totalCCDeposit;
    
	private Double totalDepositG1;
    private Double totalDepositG2;
    private Double totalCashedG1;
    private Double totalCashedG2;
    
    private Double cashRefound;
    private Date cashRefoundDate;
    private Double rentalAmountPaidByCash;
    private Date rentalAmountPaidByCashDate;
    //@Madhvendra
    private Boolean isCancelled=false;
    private Boolean isValid=true;
    private Boolean hasNewDamages=false;
    private Boolean isPaid=false;
    private Boolean isTheft=false;
    private Boolean isFire=false;
    private Boolean isBlackList=false;
    private Double totalDue;
    private Double totalCashPayment=0.0;
    private Double garazia1CCPayment=0.0;
    private Double garazia2CCPayment=0.0;
    private Boolean isPrinted=false;
    private MROldBusinessPartner invoiceTo;
    private Date billedTill;
    
    
    ///Madhvendra(for Pstgres version)
    private Integer id_contratto;
    private AgreementWebCode agreementWebCode;

    private Double scontoExtraPrepay=0.0;
    private String productNameDesc;

    private String splitPaymentPlateNumber;
    private String splitPaymentInsRefNr;


    private Boolean licenceConfirmationVerified1;
    private Boolean licenceConfirmationVerified2;
    private Boolean licenceConfirmationVerified3;


    public Boolean getIsSignedCheckOut() {
        return isSignedCheckOut;
    }

    public void setIsSignedCheckOut(Boolean signedCheckOut) {
        isSignedCheckOut = signedCheckOut;
    }

    private Boolean isSignedCheckOut;
    private Boolean isSignedCheckIn;
    private Boolean isProcessed;
    private String fileNameCheckOut;
    private String fileNameCheckIn;
    private Boolean isEmailSent;
    private Boolean isEstimateEndRental;
    private Date estEndRA;
    private String privateNote;
    private Boolean toCharge;
    private Boolean isChargeWait;
    //
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
    private static final MessageFormat TO_STRING_MESSAGE_FORMAT = new MessageFormat(bundle.getString("ContrattoNoleggio.msgNr0Data1Cliente2"));

    /** default constructor */
    public MROldContrattoNoleggio() {
    }
    
    

    public MROldContrattoNoleggio(
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
        setChiuso(Boolean.FALSE);
    }

    public MROldContrattoNoleggio(
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
        setChiuso(Boolean.FALSE);
    }

    public String toString() {
        return TO_STRING_MESSAGE_FORMAT.format(new Object[]{
                    MROldNumerazione.format(getPrefisso(), getNumero()),
                    getData(),
                    getCliente()
                });
    }

    public boolean equals(Object other) {
        if (other != null) {
            return new EqualsBuilder().append(getId(), ((MROldContrattoNoleggio) other).getId()).isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return new HashCodeBuilder().append(getId()).toHashCode();
        } else {
            return super.hashCode();
        }
    }

    public String[] getLoggableFields() {
        return new String[]{
                    "movimento", // NOI18N
                    "conducente1", // NOI18N
                    "conducente2", // NOI18N
                    "conducente3", // NOI18N
                    "cliente", // NOI18N
                    "tariffa", // NOI18N
                    "gruppo", // NOI18N
                    "gruppoAssegnato", // NOI18N
                    "sedeUscita", // NOI18N
                    "sedeRientroPrevisto", // NOI18N
                    "garanzia1", // NOI18N
                    "garanzia2", // NOI18N
                    "commissione", // NOI18N
                    "pagamento", // NOI18N
                    "inizio", // NOI18N
                    "fine", // NOI18N
                    "scontoTariffa", // NOI18N
                    "chiuso", // NOI18N
                    "note", // NOI18N
                    "tariffa.loggableInfos", // NOI18N
                    "dataFirmaContratto"
                };

    }

    public String[] getLoggableLabels() {
        return new String[]{
                    bundle.getString("ContrattoNoleggio.logUltimoMovimento"),
                    bundle.getString("ContrattoNoleggio.logConducente1"),
                    bundle.getString("ContrattoNoleggio.logConducente2"),
                    bundle.getString("ContrattoNoleggio.logConducente3"),
                    bundle.getString("ContrattoNoleggio.logCliente"),
                    bundle.getString("ContrattoNoleggio.logTariffa"),
                    bundle.getString("ContrattoNoleggio.logGruppo"),
                    bundle.getString("ContrattoNoleggio.logGruppoAssegnato"),
                    bundle.getString("ContrattoNoleggio.logSedeUscita"),
                    bundle.getString("ContrattoNoleggio.logSedeRientroPrevisto"),
                    bundle.getString("ContrattoNoleggio.logGaranzia1"),
                    bundle.getString("ContrattoNoleggio.logGaranzia2"),
                    bundle.getString("ContrattoNoleggio.logCommissione"),
                    bundle.getString("ContrattoNoleggio.logPagamento"),
                    bundle.getString("ContrattoNoleggio.logInizio"),
                    bundle.getString("ContrattoNoleggio.logFine"),
                    bundle.getString("ContrattoNoleggio.logScontoTariffa"),
                    bundle.getString("ContrattoNoleggio.logChiuso"),
                    bundle.getString("ContrattoNoleggio.logNote"),
                    bundle.getString("ContrattoNoleggio.optionalSelezionati"),
                    bundle.getString("ContrattoNoleggio.logDataFirmaContratto")
                };
    }

    public String getEntityName() {
        return "MROldContrattoNoleggio"; // NOI18N
    }

    public Integer getEntityId() {
        return getId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_contratto() {
        return id_contratto;
    }

    public void setId_contratto(Integer id_contratto) {
        this.id_contratto = id_contratto;
    }
    public AgreementWebCode getAgreementWebCode() {
        return agreementWebCode;
    }

    public void setAgreementWebCode(AgreementWebCode agreementWebCode) {
        this.agreementWebCode = agreementWebCode;
    }

    public MROldMovimentoAuto getMovimento() {
        return movimento;
    }

    public void setMovimento(MROldMovimentoAuto movimento) {
        this.movimento = movimento;
    }

    public MROldConducenti getConducente1() {
        return conducente1;
    }

    public void setConducente1(MROldConducenti conducente1) {
        this.conducente1 = conducente1;
    }

    public MROldRentalType getRentalType() {
        return rentalType;
    }

    public void setRentalType(MROldRentalType rentalType) {
        this.rentalType = rentalType;
    }

    public Double getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(Double totalDue) {
        this.totalDue = totalDue;
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

    public MROldClienti getCliente() {
        return cliente;
    }

    public void setCliente(MROldClienti cliente) {
        this.cliente = cliente;
    }

    public MROldTariffa getTariffa() {
        return tariffa;
    }

    public void setTariffa(MROldTariffa tariffa) {
        this.tariffa = tariffa;
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

    public MROldSede getSedeRientroPrevisto() {
        return sedeRientroPrevisto;
    }

    public void setSedeRientroPrevisto(MROldSede sedeRientroPrevisto) {
        this.sedeRientroPrevisto = sedeRientroPrevisto;
    }

    public MROldAffiliato getAffiliato() {
        return affiliato;
    }

    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }

    public Garanzia getGaranzia1() {
        return garanzia1;
    }

    public void setGaranzia1(Garanzia garanzia1) {
        this.garanzia1 = garanzia1;
    }

    public Date getInizio() {
        return inizio;
    }

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }

    public Date getFine() {
        return fine;
    }

    public void setFine(Date fine) {
        this.fine = fine;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Double getCauzione() {
        return cauzione;
    }

    public void setCauzione(Double cauzione) {
        this.cauzione = cauzione;
    }

    public Double getScontoTariffa() {
        return scontoTariffa;
    }

    public void setScontoTariffa(Double scontoTariffa) {
        this.scontoTariffa = scontoTariffa;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getChiuso() {
        return chiuso;
    }

    public void setChiuso(Boolean chiuso) {
        this.chiuso = chiuso;
    }

    public User getUserApertura() {
        return userApertura;
    }

    public void setUserApertura(User userApertura) {
        this.userApertura = userApertura;
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

    public MROldPagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(MROldPagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Set<MROldPrimanota> getPrimenote() {
        return primenote;
    }

    public void setPrimenote(Set<MROldPrimanota> primenote) {
        this.primenote = primenote;
    }

    public String getPrefisso() {
        return prefisso;
    }

    public void setPrefisso(String prefisso) {
        this.prefisso = prefisso;
    }

    public MROldNumerazione getNumerazione() {
        return numerazione;
    }

    public void setNumerazione(MROldNumerazione numerazione) {
        this.numerazione = numerazione;
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

    public Double getNoleggio() {
        return noleggio;
    }

    public void setNoleggio(Double noleggio) {
        this.noleggio = noleggio;
    }

    public Set getMovimenti() {
        return movimenti;
    }

    public void setMovimenti(Set movimenti) {
        this.movimenti = movimenti;
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

    public Double getSaldoFatture() {
        return saldoFatture;
    }

    public void setSaldoFatture(Double saldoFatture) {
        this.saldoFatture = saldoFatture;
    }

    public Set<MROldRataContratto> getFattureRiepilogative() {
        return fattureRiepilogative;
    }

    public void setFattureRiepilogative(Set<MROldRataContratto> fattureRiepilogative) {
        this.fattureRiepilogative = fattureRiepilogative;
    }

    public Double getSaldoAcconti() {
        return saldoAcconti;
    }

    public void setSaldoAcconti(Double saldoAcconti) {
        this.saldoAcconti = saldoAcconti;
    }

    public Set<MROldPartita> getPartite() {
        return partite;
    }

    public void setPartite(Set<MROldPartita> partite) {
        this.partite = partite;
    }

    /**
     * @return the dataFirmaContratto
     */
    public Date getDataFirmaContratto() {
        return dataFirmaContratto;
    }

    /**
     * @param dataFirmaContratto the dataFirmaContratto to set
     */
    public void setDataFirmaContratto(Date dataFirmaContratto) {
        this.dataFirmaContratto = dataFirmaContratto;
    }

    public Integer getNumeroMovimenti() {
        return numeroMovimenti;
    }

    public void setNumeroMovimenti(Integer numeroMovimenti) {
        this.numeroMovimenti = numeroMovimenti;
    }

    public MROldGruppo getGruppoAssegnato() {
        return gruppoAssegnato;
    }

    public void setGruppoAssegnato(MROldGruppo gruppoAssegnato) {
        this.gruppoAssegnato = gruppoAssegnato;
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
        return "Contratto n" + getNumero().toString() + " del " + new SimpleDateFormat("dd/MM/yyyy").format(getData());
    }

    @Override
    public Class getDocumentableClass() {
        return MROldContrattoNoleggio.class;
    }

    @Override
    public String getKeywords() {
         return new StringBuffer().
                append(bundle.getString("ContrattoNoleggio.keywords1")).
                append(" ").
                append(getNumero()).
                append(" ").
                append(bundle.getString("ContrattoNoleggio.keywords2")).
                append(" ").
                append(new SimpleDateFormat("dd/MM/yyyy").format(getData())).
                toString();
    }

    public MROldCurrencyConversionRate getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(MROldCurrencyConversionRate conversionRate) {
        this.conversionRate = conversionRate;
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
     * @return the contrattoNoleggioRighe
     */
    public List getContrattoNoleggioRighe() {
        return contrattoNoleggioRighe;
    }

    /**
     * @param contrattoNoleggioRighe the contrattoNoleggioRighe to set
     */
    public void setContrattoNoleggioRighe(List contrattoNoleggioRighe) {
        this.contrattoNoleggioRighe = contrattoNoleggioRighe;
    }

    /**
     * @return the contrattoNoleggioRighePPay
     */
    public List getContrattoNoleggioRighePPay() {
        return contrattoNoleggioRighePPay;
    }

    /**
     * @param contrattoNoleggioRighePPay the contrattoNoleggioRighePPay to set
     */
    public void setContrattoNoleggioRighePPay(List contrattoNoleggioRighePPay) {
        this.contrattoNoleggioRighePPay = contrattoNoleggioRighePPay;
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

    public MROldGruppo getGruppo1() {
        return this.gruppo1;
    }

    public void setGruppo1(MROldGruppo gruppo1) {
        this.gruppo1 = gruppo1;
    }

    public Boolean getIsEprd() {
        return isEprd;
    }

    public void setIsEprd(Boolean isEprd) {
        this.isEprd = isEprd;
    }

    public Boolean getIsExtraServ() {
        return isExtraServ;
    }

    public void setIsExtraServ(Boolean isExtraServ) {
        this.isExtraServ = isExtraServ;
    }

    public Boolean getIsEprdf() {
        return isEprdf;
    }

    public void setIsEprdf(Boolean isEprdf) {
        this.isEprdf = isEprdf;
    }
    public void setIsDepositCash(Boolean in){
        this.isDepositCash=in;
    }
    public Boolean getIsDepositCash()
    {return this.isDepositCash;}
    
    public void setIsDepositCreditCard(Boolean in){
        this.isDepositCreditCard=in;
    }
    public Boolean getIsDepositCreditCard()
    {return this.isDepositCreditCard;}
    
    public void setTotalDeposit(Double in){
        this.totalDeposit=in;
    }
    public Double getTotalDeposit()
    {return this.totalDeposit;}
    public void setTotalDepositG1(Double in){
        this.totalDepositG1=in;
    }
    public Double getTotalDepositG1()
    {return this.totalDepositG1;}
    public void setTotalDepositG2(Double in){
        this.totalDepositG2=in;
    }
    public Double getTotalDepositG2()
    {return this.totalDepositG2;}
    
    public void setTotalCashedG2(Double in){
        this.totalCashedG2=in;
    }
    public Double getTotalCashedG2()
    {return this.totalCashedG2;}
    
    public void setTotalCashedG1(Double in){
        this.totalCashedG1=in;
    }
    public Double getTotalCashedG1()
    {return this.totalCashedG1;}
    
    public void setCashRefound(Double in){
        this.cashRefound=in;
    }
    public Double getCashRefound()
    {return this.cashRefound;}
    
    public void setCashRefoundDate(Date in){
        this.cashRefoundDate=in;
    }
    public Date getCashRefoundDate()
    {return this.cashRefoundDate;}
    
    public void setRentalAmountPaidByCash(Double in){
        this.rentalAmountPaidByCash=in;
    }
    public Double getRentalAmountPaidByCash()
    {return this.rentalAmountPaidByCash;}
    
    public void setRentalAmountPaidByCashDate(Date in){
        this.rentalAmountPaidByCashDate=in;
    }
    public Date getRentalAmountPaidByCashDate()
    {return this.rentalAmountPaidByCashDate;}
    
    public Double getTotalCCDeposit() {
		return totalCCDeposit;
	}

	public void setTotalCCDeposit(Double totalCCDeposit) {
		this.totalCCDeposit = totalCCDeposit;
	}
    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public Boolean getHasNewDamages() {
        return hasNewDamages;
    }

    public void setHasNewDamages(Boolean hasNewDamages) {
        this.hasNewDamages = hasNewDamages;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Boolean getIsTheft() {
        return isTheft;
    }

    public void setIsTheft(Boolean isTheft) {
        this.isTheft = isTheft;
    }

    public Boolean getIsFire() {
        return isFire;
    }

    public void setIsFire(Boolean isFire) {
        this.isFire = isFire;
    }
    public Boolean getIsBlackList() {
        return isBlackList;
    }

    public void setIsBlackList(Boolean isBlackList) {
        this.isBlackList = isBlackList;
    }

    public Double getTotalCashPayment() {
        return totalCashPayment;
    }

    public void setTotalCashPayment(Double totalCashPayment) {
        this.totalCashPayment = totalCashPayment;
    }

    public Double getGarazia1CCPayment() {
        return garazia1CCPayment;
    }

    public void setGarazia1CCPayment(Double garazia1CCPayment) {
        this.garazia1CCPayment = garazia1CCPayment;
    }

    public Double getGarazia2CCPayment() {
        return garazia2CCPayment;
    }

    public void setGarazia2CCPayment(Double garazia2CCPayment) {
        this.garazia2CCPayment = garazia2CCPayment;
    }

    public Boolean getIsPrinted() {
        return isPrinted;
    }

    public void setIsPrinted(Boolean isPrinted) {
        this.isPrinted = isPrinted;
    }

    public Double getScontoExtraPrepay() {
        return scontoExtraPrepay;
    }

    public void setScontoExtraPrepay(Double scontoExtraPrepay) {
        this.scontoExtraPrepay = scontoExtraPrepay;
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

    public MROldBusinessPartner getInvoiceTo() {
        return invoiceTo;
    }

    public void setInvoiceTo(MROldBusinessPartner invoiceTo) {
        this.invoiceTo = invoiceTo;
    }

    public Date getBilledTill() {
        return billedTill;
    }

    public void setBilledTill(Date billedTill) {
        this.billedTill = billedTill;
    }

    public Boolean getSplitPayment() {
        return splitPayment;
    }

    public void setSplitPayment(Boolean splitPayment) {
        this.splitPayment = splitPayment;
    }

    public List<MROldCustomerSplitPayment> getCustomerSplitPayment() {
        return customerSplitPayment;
    }

    public void setCustomerSplitPayment(List<MROldCustomerSplitPayment> customerSplitPayment) {
        this.customerSplitPayment = customerSplitPayment;
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

    public Boolean getLicenceConfirmationVerified1() {
        return licenceConfirmationVerified1;
    }

    public void setLicenceConfirmationVerified1(Boolean licenceConfirmationVerified1) {
        this.licenceConfirmationVerified1 = licenceConfirmationVerified1;
    }

    public Boolean getLicenceConfirmationVerified3() {
        return licenceConfirmationVerified3;
    }

    public void setLicenceConfirmationVerified3(Boolean licenceConfirmationVerified3) {
        this.licenceConfirmationVerified3 = licenceConfirmationVerified3;
    }

    public Boolean getLicenceConfirmationVerified2() {
        return licenceConfirmationVerified2;
    }

    public void setLicenceConfirmationVerified2(Boolean licenceConfirmationVerified2) {
        this.licenceConfirmationVerified2 = licenceConfirmationVerified2;
    }

    public String getFileNameCheckOut() {
        return fileNameCheckOut;
    }

    public void setFileNameCheckOut(String fileNameCheckOut) {
        this.fileNameCheckOut = fileNameCheckOut;
    }

    public String getFileNameCheckIn() {
        return fileNameCheckIn;
    }

    public void setFileNameCheckIn(String fileNameCheckIn) {
        this.fileNameCheckIn = fileNameCheckIn;
    }

    public Boolean getIsSignedCheckIn() {
        return isSignedCheckIn;
    }

    public void setIsSignedCheckIn(Boolean signedCheckIn) {
        isSignedCheckIn = signedCheckIn;
    }

    public Date getRentalCloseDate() {
        return rentalCloseDate;
    }

    public void setRentalCloseDate(Date rentalCloseDate) {
        this.rentalCloseDate = rentalCloseDate;
    }

    public MROldNexiTransactionDeposit getNexiDeposit() {
        return nexiDeposit;
    }

    public void setNexiDeposit(MROldNexiTransactionDeposit nexiDeposit) {
        this.nexiDeposit = nexiDeposit;
    }


    public Boolean getIsEmailSent() {
        return isEmailSent;
    }

    public void setIsEmailSent(Boolean isEmailSent) {
        this.isEmailSent = isEmailSent;
    }
    public String getPrivateNote() {
        return privateNote;
    }

    public void setPrivateNote(String privateNote) {
        this.privateNote = privateNote;
    }
    public MROldFornitori getAgent() {
        return agent;
    }

    public void setAgent(MROldFornitori agent) {
        this.agent = agent;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getMonthDuration() {
        return monthDuration;
    }

    public void setMonthDuration(Integer monthDuration) {
        this.monthDuration = monthDuration;
    }

    public Boolean getIsMonthlyRental() {
        return isMonthlyRental;
    }

    public void setIsMonthlyRental(Boolean isMonthlyRental) {
        this.isMonthlyRental = isMonthlyRental;
    }

    public Boolean getIsEstimateEndRental() {
        return isEstimateEndRental;
    }

    public void setIsEstimateEndRental(Boolean isEstimateEndRental) {
        this.isEstimateEndRental = isEstimateEndRental;
    }

    public Date getEstEndRA() {
        return estEndRA;
    }

    public void setEstEndRA(Date estEndRA) {
        this.estEndRA = estEndRA;
    }


    public Boolean getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(Boolean isProcessed) {
        this.isProcessed = isProcessed;
    }


    public Boolean getToCharge() {
        return toCharge;
    }

    public void setToCharge(Boolean toCharge) {
        this.toCharge = toCharge;
    }

    public Boolean getIsChargeWait() {
        return isChargeWait;
    }

    public void setIsChargeWait(Boolean isChargeWait) {
        this.isChargeWait = isChargeWait;
    }
}
