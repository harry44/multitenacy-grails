package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.myrent.ee.interfaces.DocumentoFirmabileInterface;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.text.MessageFormat;
import java.util.*;

/** @author Hibernate CodeGenerator */
public class MROldDocumentoFiscale implements
        it.aessepi.utils.db.PersistentInstance,
        DocumentoFirmabileInterface,
        MROldProgressivoInterface{

    /** identifier field */
    private Integer id;
    private MROldNumerazione numerazione;
    private String prefisso;
    private Integer numero;
    private Date data;
    private Date inizioFatturazione;
    private Date fineFatturazione;
    private Integer anno;
    private MROldBusinessPartner cliente;
    private Boolean prepagato;
    private Boolean contabilizzato;
    private Boolean riepilogativo;
    private MROldPagamento pagamento;
    private String annotazioni;
    private MROldContrattoNoleggio contratto;
    private Double totaleImponibile;
    private Double totaleIva;
    private Double totaleFattura;
    private Double totaleRighe;
    private Double totaleAcconti;    
    private List fatturaRighe;
    private Map<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale> fatturaImposte = new HashMap<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale>();
    private MROldAffiliato affiliato;
    private User user;
    private Set scadenze = new HashSet();
    private String discriminator;
    private MROldPrimanota primanota;
    private MROldParcoVeicoli veicolo;
    private Set<MROldRataContratto> rate;
    private MROldCurrencyConversionRate conversionRate;
    private Boolean documentoLibero;
    private MROldPrenotazione prenotazione;
    private Set<MROldPrimanota> incassi;
    private String fatturaXML;

    //MyRent Signature
    private String nomeFirmatario;
    private String luogoFirma;
    private String annotazioniFirma;
    private String nomeUtenteCreatore;
    private Date dataFirmaCerta;
    private Boolean documentoFirmato; //accerta l'avvenuta corretta archiviazione sulla gestione documentale
    private Set documenti;
    private Date dataEffettuazione;
    private Boolean bollo;
    private String numContrattoLibero;
    private Date dataContrattoLibero;
    private String codiceCIG;
    private String ordineAcquistoMeccanica;
    private String codiceCIGPO;
    private String numPurchaseOrder;
    private Date datePurchaseOrder;
    private String targaApprontamento;
    private String telaioApprontamento;
    private String targaMeccanica;
    private String contrattoApprontamento;

    private String idComunicazioneFattura;
    private Date dataUltimaComunicazioneFattura;
    private String contractExportCode;
    private String motivoFatturazioneXMLFallita;
    private String dataRicezioneSdi;
    private Long idSdi;
    private String statoInvio;
    private String rifAmministrativo;
    private Rent2Rent rent2rent;

    public MROldDocumentoFiscale(
            MROldAffiliato affiliato,
            User user,
            MROldNumerazione numerazione,
            String prefisso,
            Integer numero,
            Date data,
            Integer anno) {
        setAffiliato(affiliato);
        setUser(user);
        setNumerazione(numerazione);
        setPrefisso(prefisso);
        setNumero(numero);
        setData(data);
        setAnno(anno);
        setContabilizzato(Boolean.FALSE);
        setRiepilogativo(Boolean.FALSE);
        setPrepagato(Boolean.FALSE);
    }

    /** default constructor */
    public MROldDocumentoFiscale() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdComunicazioneFattura() {
        return idComunicazioneFattura;
    }

    public void setIdComunicazioneFattura(String idComunicazioneFattura) {
        this.idComunicazioneFattura = idComunicazioneFattura;
    }

    public Date getData() {
        return this.data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Integer getAnno() {
        return anno;
    }

    public MROldBusinessPartner getCliente() {
        return this.cliente;
    }

    public void setCliente(MROldBusinessPartner cliente) {
        this.cliente = cliente;
    }

    public Double getTotaleImponibile() {
        return this.totaleImponibile;
    }

    public void setTotaleImponibile(Double totaleImponibile) {
        this.totaleImponibile = totaleImponibile;
    }

    public Double getTotaleIva() {
        return this.totaleIva;
    }

    public void setTotaleIva(Double totaleIva) {
        this.totaleIva = totaleIva;
    }

    public Double getTotaleFattura() {
        return this.totaleFattura;
    }

    public void setTotaleFattura(Double totaleFattura) {
        this.totaleFattura = totaleFattura;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Boolean getContabilizzato() {
        return this.contabilizzato;
    }

    public void setContabilizzato(Boolean contabilizzato) {
        this.contabilizzato = contabilizzato;
    }

    public MROldPagamento getPagamento() {
        return this.pagamento;
    }

    public void setPagamento(MROldPagamento pagamento) {
        this.pagamento = pagamento;
    }

    public String getAnnotazioni() {
        return this.annotazioni;
    }

    public void setAnnotazioni(String annotazioni) {
        this.annotazioni = annotazioni;
    }

    public MROldContrattoNoleggio getContratto() {
        return this.contratto;
    }

    public void setContratto(MROldContrattoNoleggio contratto) {
        this.contratto = contratto;
    }

    public void setFatturaRighe(List fatturaRighe) {
        this.fatturaRighe = fatturaRighe;
    }

    public List getFatturaRighe() {
        return this.fatturaRighe;
    }

    public void setScadenze(Set scadenze) {
        this.scadenze = scadenze;
    }

    public Set getScadenze() {
        return scadenze;
    }

    public String getTipoDocumento() {
        return DF;
    }

    public String getTipoDocumentoI18N() {
        if (FT.equals(getTipoDocumento())) {
            return FT_I18N;
        } else if (RF.equals(getTipoDocumento())) {
            return RF_I18N;
        } else if (NC.equals(getTipoDocumento())) {
            return NC_I18N;
        } else if (FTG.equals(getTipoDocumento())) {
            return FTG_I18N;
        } else if (FTP.equals(getTipoDocumento())) {
            return FTP_I18N;
        }
        return DF_I18N;
    }

    public static MROldDocumentoFiscale creaDocumentoFiscale(
            MROldAffiliato affiliato,
            User user,
            String tipoDocumento,
            MROldNumerazione numerazione,
            String prefisso,
            Integer numero,
            Date data,
            Integer anno) {
        if (tipoDocumento.equals(RF) || tipoDocumento.equals(RF_EN) || tipoDocumento.equals(RF_ES)) {
            return new MROldRicevutaFiscale(affiliato, user, numerazione, prefisso, numero, data, anno);
        } else if (tipoDocumento.equals(NC) || tipoDocumento.equals(NC_EN) || tipoDocumento.equals(NC_ES)) {
            return new MROldNotaCredito(affiliato, user, numerazione, prefisso, numero, data, anno);
        } else if (tipoDocumento.equals(FT) || tipoDocumento.equals(FT_EN) || tipoDocumento.equals(FT_ES)) {
            return new MROldFattura(affiliato, user, numerazione, prefisso, numero, data, anno);
        } else if(tipoDocumento.equals(FTA)) {
            return new MROldFatturaAcconto(affiliato, user, numerazione, prefisso, numero, data, anno);
        } else if(tipoDocumento.equals(FTP)) {
            return new FatturaProforma(affiliato, user, numerazione, prefisso, numero, data, anno);
        }
        return null;
    }

    public String toString() {
        return TO_STRING_MESSAGE_FORMAT.format(new Object[]{
                    getTipoDocumentoI18N(),
                    MROldNumerazione.format(getPrefisso(), getNumero()),
                    getData(),
                    getCliente(),
                    getTotaleFattura()
                });
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldDocumentoFiscale)) {
            return false;
        }
        MROldDocumentoFiscale castOther = (MROldDocumentoFiscale) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }
    public static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
    private static final MessageFormat TO_STRING_MESSAGE_FORMAT = new MessageFormat(bundle.getString("DocumentoFiscale.msgDoc0Nr1Del2Cliente3Totale4"));
    public static final String FT = "Fattura"; //NOI18N

    public static final String FTA = "Fattura di Acconto"; //NOI18N
    public static final String FTP = "Fattura Proforma"; //NOI18N
    public static final String FTG = "FatturaGenerica"; //NOI18N
    public static final String NC = "Nota Di Accredito"; //NOI18N
    public static final String DF = "Documento Fiscale"; //NOI18N
    public static final String RF = "Ricevuta Fiscale Pagata"; //NOI18N

    //inglese
    public static final String FT_EN = "Invoice";
    public static final String RF_EN = "Receipt";
    public static final String NC_EN = "Credit memo";

    //spagnolo
    public static final String FT_ES = "Factura";
    public static final String RF_ES = "Recibo Fiscal Pago";
    public static final String NC_ES = "Nota de crédito";

    public static final String FTA_I18N = bundle.getString("DocumentoFiscale.FTA_I18N");
    public static final String FT_I18N = bundle.getString("DocumentoFiscale.FT_I18N");
    public static final String FTG_I18N = bundle.getString("DocumentoFiscale.FTG_I18N");
    public static final String FTP_I18N = bundle.getString("DocumentoFiscale.FTP_I18N");

    public static final String NC_I18N = bundle.getString("DocumentoFiscale.NC_I18N");
    public static final String DF_I18N = bundle.getString("DocumentoFiscale.DF_I18N");
    public static final String RF_I18N = bundle.getString("DocumentoFiscale.RF_I18N");

    //discriminator come salvati nel db
    public static final String FT_DATABASE = "Fattura";
    public static final String FTA_DATABASE = "FatturaAcconto";
    public static final String FTP_DATABASE = "FatturaProforma";
    public static final String RF_DATABASE = "RicevutaFiscale";
    public static final String NC_DATABASE = "NotaCredito";

    public String getTipoNumerazione() {
        return null;
    }

    public MROldAffiliato getAffiliato() {
        return affiliato;
    }

    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }

    public MROldPrimanota getPrimanota() {
        return primanota;
    }

    public void setPrimanota(MROldPrimanota primanota) {
        this.primanota = primanota;
    }

    public MROldParcoVeicoli getVeicolo() {
        return veicolo;
    }

    public void setVeicolo(MROldParcoVeicoli veicolo) {
        this.veicolo = veicolo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Boolean getPrepagato() {
        return prepagato;
    }

    public void setPrepagato(Boolean prepagato) {
        this.prepagato = prepagato;
    }

    public Boolean getRiepilogativo() {
        return riepilogativo;
    }

    public void setRiepilogativo(Boolean riepilogativo) {
        this.riepilogativo = riepilogativo;
    }

    public Set<MROldRataContratto> getRate() {
        return rate;
    }

    public void setRate(Set<MROldRataContratto> rate) {
        this.rate = rate;
    }

    public Double getTotaleAcconti() {
        return totaleAcconti;
    }

    public void setTotaleAcconti(Double totaleAcconti) {
        this.totaleAcconti = totaleAcconti;
    }

    public Double getTotaleRighe() {
        return totaleRighe;
    }

    public void setTotaleRighe(Double totaleRighe) {
        this.totaleRighe = totaleRighe;
    }

    public Map<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale> getFatturaImposte() {
        return fatturaImposte;
    }

    public void setFatturaImposte(Map<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale> fatturaImposte) {
        this.fatturaImposte = fatturaImposte;
    }

    public Date getInizioFatturazione() {
        return inizioFatturazione;
    }

    public void setInizioFatturazione(Date inizioFatturazione) {
        this.inizioFatturazione = inizioFatturazione;
    }

    public Date getFineFatturazione() {
        return fineFatturazione;
    }

    public void setFineFatturazione(Date fineFatturazione) {
        this.fineFatturazione = fineFatturazione;
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

    public MROldCurrencyConversionRate getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(MROldCurrencyConversionRate conversionRate) {
        this.conversionRate = conversionRate;
    }

    public void setDocumentoLibero(Boolean documentoLibero) {
        this.documentoLibero = documentoLibero;
    }

    public Boolean getDocumentoLibero() {
        return documentoLibero;
    }

    public MROldPrenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(MROldPrenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public void setIncassi(Set<MROldPrimanota> incassi) {
        this.incassi = incassi;
    }

    public Set<MROldPrimanota> getIncassi() {
        return incassi;
    }

    public String getFatturaXML() {
        return fatturaXML;
    }

    public void setFatturaXML(String fatturaXML) {
        this.fatturaXML = fatturaXML;
    }

    public Date getDataUltimaComunicazioneFattura() {
        return dataUltimaComunicazioneFattura;
    }

    public void setDataUltimaComunicazioneFattura(Date dataUltimaComunicazioneFattura) {
        this.dataUltimaComunicazioneFattura = dataUltimaComunicazioneFattura;
    }

    public String getContractExportCode() {
        return contractExportCode;
    }

    public void setContractExportCode(String contractExportCode) {
        this.contractExportCode = contractExportCode;
    }

    public String getMotivoFatturazioneXMLFallita() {
        return motivoFatturazioneXMLFallita;
    }

    public void setMotivoFatturazioneXMLFallita(String motivoFatturazioneXMLFallita) {
        this.motivoFatturazioneXMLFallita = motivoFatturazioneXMLFallita;
    }

    public String getDataRicezioneSdi() {
        return dataRicezioneSdi;
    }

    public void setDataRicezioneSdi(String dataRicezioneSdi) {
        this.dataRicezioneSdi = dataRicezioneSdi;
    }

    public Long getIdSdi() {
        return idSdi;
    }

    public void setIdSdi(Long idSdi) {
        this.idSdi = idSdi;
    }

    public String getStatoInvio() {
        return statoInvio;
    }

    public void setStatoInvio(String statoInvio) {
        this.statoInvio = statoInvio;
    }

    public String getRifAmministrativo() {
        return rifAmministrativo;
    }

    public void setRifAmministrativo(String rifAmministrativo) {
        this.rifAmministrativo = rifAmministrativo;
    }


    public Date getDataEffettuazione() {
        return dataEffettuazione;
    }

    public void setDataEffettuazione(Date dataEffettuazione) {
        this.dataEffettuazione = dataEffettuazione;
    }

    public Boolean getBollo() {
        return bollo;
    }

    public void setBollo(Boolean bollo) {
        this.bollo = bollo;
    }

    public String getNumContrattoLibero() {
        return numContrattoLibero;
    }

    public void setNumContrattoLibero(String numContrattoLibero) {
        this.numContrattoLibero = numContrattoLibero;
    }

    public Date getDataContrattoLibero() {
        return dataContrattoLibero;
    }

    public void setDataContrattoLibero(Date dataContrattoLibero) {
        this.dataContrattoLibero = dataContrattoLibero;
    }

    public String getCodiceCIG() {
        return codiceCIG;
    }

    public void setCodiceCIG(String codiceCIG) {
        this.codiceCIG = codiceCIG;
    }

    public String getOrdineAcquistoMeccanica() {
        return ordineAcquistoMeccanica;
    }

    public void setOrdineAcquistoMeccanica(String ordineAcquistoMeccanica) {
        this.ordineAcquistoMeccanica = ordineAcquistoMeccanica;
    }

    public String getCodiceCIGPO() {
        return codiceCIGPO;
    }

    public void setCodiceCIGPO(String codiceCIGPO) {
        this.codiceCIGPO = codiceCIGPO;
    }

    public String getNumPurchaseOrder() {
        return numPurchaseOrder;
    }

    public void setNumPurchaseOrder(String numPurchaseOrder) {
        this.numPurchaseOrder = numPurchaseOrder;
    }

    public Date getDatePurchaseOrder() {
        return datePurchaseOrder;
    }

    public void setDatePurchaseOrder(Date datePurchaseOrder) {
        this.datePurchaseOrder = datePurchaseOrder;
    }

    public String getTargaApprontamento() {
        return targaApprontamento;
    }

    public void setTargaApprontamento(String targaApprontamento) {
        this.targaApprontamento = targaApprontamento;
    }

    public String getTelaioApprontamento() {
        return telaioApprontamento;
    }

    public void setTelaioApprontamento(String telaioApprontamento) {
        this.telaioApprontamento = telaioApprontamento;
    }

    public String getTargaMeccanica() {
        return targaMeccanica;
    }

    public void setTargaMeccanica(String targaMeccanica) {
        this.targaMeccanica = targaMeccanica;
    }

    public String getContrattoApprontamento() {
        return contrattoApprontamento;
    }

    public void setContrattoApprontamento(String contrattoApprontamento) {
        this.contrattoApprontamento = contrattoApprontamento;
    }
    public Rent2Rent getRent2rent() {
        return rent2rent;
    }

    public void setRent2rent(Rent2Rent rent2rent) {
        this.rent2rent = rent2rent;
    }

}
