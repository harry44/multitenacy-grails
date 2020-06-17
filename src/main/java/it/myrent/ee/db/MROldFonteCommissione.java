package it.myrent.ee.db;


import it.aessepi.utils.BundleUtils;
import it.myrent.ee.beanutil.ValiditaListinoFonteComparator;
import it.aessepi.utils.db.PersistentInstance;

import java.util.*;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/** @author Hibernate CodeGenerator */
public class MROldFonteCommissione implements PersistentInstance, Comparable<MROldFonteCommissione>, Cloneable {

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    private Integer id;
    private String codice;
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
    private String contocorrente;
    private String nazione;
    private String email;
    private String promemoria;
    private String codiceFiscale;
    private String exportCode;
    private Boolean scontabile;
    private MROldPagamento pagamento;
    private Double percentuale;
    private MROldListino listinoExtraPrepay;
    private MROldFonteCommissione fonteExtraPPay;
    private MROldFonteCommissione masterReservation;
    private MROldClienti cliente;
    private Boolean multistagione;
    private Boolean differedPayment;
    private Set<MROldValiditaListinoFonte> listini;

    private Set<MROldSconto> sconti;
    private Set<User> users;
    ///////////////////////////////////////////////////////
    //added by Madhvendra
    private Boolean isDeactivated;

    private Boolean showAllOptionals;
    private Boolean isApplicabilityWS;
    private MROldFonteClass fonteClass;
    //private MROldSede location;
    private Boolean isvirtual;
    private Boolean isNotBookingOptionalPrepaid;
    private MROldGroupUpgradeSchema groupUpgradeSchemaId;
    private MROldLocationNetwork locationNetwork;
    private MROldRentalType rentalType;
    private MROldDepositAndWaiverResSource depositResSourceId=null;

    //add for EuropAssitance
    private Boolean isMonthlyBilled;
    private Boolean isSummaryinvoice;

    private Set<MROldSede> sede;
    
    //added new for Europe
    
    private Boolean isCDWEnabled;
    private Boolean isTheftEnabled;
    private Boolean isCDWAndTheftEnabled;
    private Boolean isRoadAssistanceEnabled;   
    private Set<MROldCCDepositByGroup> depositByGroups=null;
    private Boolean isDisable;


    private Boolean isOnlyShortRentAllowed;
    //Madhvendra(For Postgres version)


    private Boolean invoiceOpenClosedContracts;
    private Boolean activeImportationJob;
    private Boolean invoiceStartOrStartAndMidOfTheMonth;
    private Boolean createProformaInvoice;


    private Set <MROldClienti> clienteReservation;
    private Double xmlDiscount;

    public Set<MROldClienti> getClienteReservation() {
        return clienteReservation;
    }

    public void setClienteReservation(Set<MROldClienti> clienteReservation) {
        this.clienteReservation = clienteReservation;
    }
    //
    public Boolean getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(Boolean isDisable) {
        this.isDisable = isDisable;
    }

/////////////////////////////////////////////////////////////

    public Boolean getIsMonthlyBilled() {
        return isMonthlyBilled;
    }

    public void setIsMonthlyBilled(Boolean isMonthlyBilled) {
        this.isMonthlyBilled = isMonthlyBilled;
    }

    public Boolean getIsSummaryinvoice() {
        return isSummaryinvoice;
    }

    public void setIsSummaryinvoice(Boolean isSummaryinvoice) {
        this.isSummaryinvoice = isSummaryinvoice;
    }

    public Set<MROldCCDepositByGroup> getDepositByGroups() {
        return this.depositByGroups;
    }

    public void setDepositByGroups(Set<MROldCCDepositByGroup> depositByGroups) {
        this.depositByGroups = depositByGroups;
    }
    
    public Set<MROldSede> getSede() {
        return sede;
    }

    public void setSede(Set<MROldSede> sede) {
        this.sede = sede;
    }

    public Boolean getIsCDWEnabled() {
        return isCDWEnabled;
    }

    public void setIsCDWEnabled(Boolean isCDWEnabled) {
        this.isCDWEnabled = isCDWEnabled;
    }

    public Boolean getIsTheftEnabled() {
        return isTheftEnabled;
    }

    public void setIsTheftEnabled(Boolean isTheftEnabled) {
        this.isTheftEnabled = isTheftEnabled;
    }

    public Boolean getIsCDWAndTheftEnabled() {
        return isCDWAndTheftEnabled;
    }

    public void setIsCDWAndTheftEnabled(Boolean isCDWAndTheftEnabled) {
        this.isCDWAndTheftEnabled = isCDWAndTheftEnabled;
    }

    public Boolean getIsRoadAssistanceEnabled() {
        return isRoadAssistanceEnabled;
    }

    public void setIsRoadAssistanceEnabled(Boolean isRoadAssistanceEnabled) {
        this.isRoadAssistanceEnabled = isRoadAssistanceEnabled;
    }
  
    
    public MROldRentalType getRentalType() {
        return rentalType;
    }

    public MROldDepositAndWaiverResSource getDepositResSourceId() {
        return depositResSourceId;
    }

    public void setDepositResSourceId(MROldDepositAndWaiverResSource depositResSourceId) {
        this.depositResSourceId = depositResSourceId;
    }

    public void setRentalType(MROldRentalType rentalType) {
        this.rentalType = rentalType;
    }

    
    public MROldGroupUpgradeSchema getGroupUpgradeSchemaId() {
        return groupUpgradeSchemaId;
    }

    public void setGroupUpgradeSchemaId(MROldGroupUpgradeSchema groupUpgradeSchemaId) {
        this.groupUpgradeSchemaId = groupUpgradeSchemaId;
    }
   
    
    public MROldFonteCommissione getMasterReservation() {
        return masterReservation;
    }

    public void setMasterReservation(MROldFonteCommissione masterReservation) {
        this.masterReservation = masterReservation;
    }

    public Boolean getIsvirtual() {
        return isvirtual;
    }

    public void setIsvirtual(Boolean isvirtual) {
        this.isvirtual = isvirtual;
    }
   
    public MROldFonteCommissione() {
    }

    public MROldFonteCommissione(Integer id, String ragioneSociale) {
        setId(id);
        setRagioneSociale(ragioneSociale);
    }

    public int compareTo(MROldFonteCommissione o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getRagioneSociale(), o.getRagioneSociale()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    @Override
    public Object clone() {

        MROldFonteCommissione obj = new MROldFonteCommissione();

       Set<MROldValiditaListinoFonte> newList = new TreeSet<MROldValiditaListinoFonte>(new ValiditaListinoFonteComparator());
        Set<MROldValiditaListinoFonte> oldList = this.getListini();
        Iterator<MROldValiditaListinoFonte> itr = oldList.iterator();
        MROldValiditaListinoFonte li; //the new one
        MROldValiditaListinoFonte valid; // the old one

        while (itr.hasNext()) {
            valid = itr.next();

            li = new MROldValiditaListinoFonte();

            //li.setId(valid.getId());
            li.setFineOfferta(valid.getFineOfferta());
            li.setFineStagione(valid.getFineStagione());
            li.setInizioOfferta(valid.getInizioOfferta());
            li.setInizioStagione(valid.getInizioStagione());
            li.setListino(valid.getListino());
            li.setFonteCommissione(valid.getFonteCommissione());
            
            newList.add(li);
        }
        if (this.getCodice() != null) {
            obj.setCodice("copy_" + this.getCodice());
        }

        if (this.getRagioneSociale() != null) {
            obj.setRagioneSociale("copy of " + this.getRagioneSociale());
        }
        obj.setPercentuale(this.getPercentuale());
        obj.setScontabile(this.getScontabile());
        obj.setMultistagione(this.getMultistagione());
        obj.setEmail(this.getEmail());
        obj.setPromemoria(this.getPromemoria());
        obj.setListinoExtraPrepay(this.getListinoExtraPrepay());
        obj.setFonteExtraPPay(this.getFonteExtraPPay());
        obj.setPagamento(this.getPagamento());
        obj.setCliente(this.getCliente());
        if (obj.getListini()!=null)
            obj.getListini().addAll(newList);
        else {
            obj.setListini(new TreeSet<MROldValiditaListinoFonte>(new ValiditaListinoFonteComparator()));
            obj.setListini(newList);
        }

        obj.setDifferedPayment(this.getDifferedPayment());
        return obj;
    }

    public Integer getId() {
        return this.id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
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

    public String getContocorrente() {
        return this.contocorrente;
    }

    public void setContocorrente(String contocorrente) {
        this.contocorrente = contocorrente;
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

    public String toString() {
        return ragioneSociale != null ? ragioneSociale : new String();
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldFonteCommissione)) {
            return false;
        }
        MROldFonteCommissione castOther = (MROldFonteCommissione) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public Double getPercentuale() {
        return percentuale;
    }

    public void setPercentuale(Double percentuale) {
        this.percentuale = percentuale;
    }

    public Boolean getScontabile() {
        return scontabile;
    }

    public void setScontabile(Boolean scontabile) {
        this.scontabile = scontabile;
    }

    public MROldListino getListinoExtraPrepay() {
        return listinoExtraPrepay;
    }

    public void setListinoExtraPrepay(MROldListino listinoExtraPrepay) {
        this.listinoExtraPrepay = listinoExtraPrepay;
    }

    public MROldFonteCommissione getFonteExtraPPay() {
        return fonteExtraPPay;
    }

    public void setFonteExtraPPay(MROldFonteCommissione aFonteExtraPPay) {
        fonteExtraPPay = aFonteExtraPPay;
    }

    public MROldClienti getCliente() {
        return cliente;
    }

    public void setCliente(MROldClienti cliente) {
        this.cliente = cliente;
    }

    public Set<MROldValiditaListinoFonte> getListini() {
        return listini;
    }

    public void setListini(Set<MROldValiditaListinoFonte> listini) {
        this.listini = listini;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Boolean getMultistagione() {
        return multistagione;
    }

    public void setMultistagione(Boolean multistagione) {
        this.multistagione = multistagione;
    }

    public Boolean getDifferedPayment() {
        return differedPayment;
    }

    public void setDifferedPayment(Boolean differedPayment) {
        this.differedPayment = differedPayment;
    }

    /**
     * @return the isDeactivated
     */
    public Boolean getIsDeactivated() {
        return isDeactivated;
    }

    /**
     * @param isDeactivated the isDeactivated to set
     */
    public void setIsDeactivated(Boolean isDeactivated) {
        this.isDeactivated = isDeactivated;
    }

    /**
     * @return the fonteClass
     */
    public MROldFonteClass getFonteClass() {
        return fonteClass;
    }

    /**
     * @param fonteClass the fonteClass to set
     */
    public void setFonteClass(MROldFonteClass fonteClass) {
        this.fonteClass = fonteClass;
    }

    public MROldLocationNetwork getLocationNetwork() {
        return locationNetwork;
    }

    public void setLocationNetwork(MROldLocationNetwork locationNetwork) {
        this.locationNetwork = locationNetwork;
    }

    /**
     * @return the isNotBookingOptionalPrepaid
     */
    public Boolean getIsNotBookingOptionalPrepaid() {
        return isNotBookingOptionalPrepaid;
    }

    /**
     * @param isNotBookingOptionalPrepaid the isNotBookingOptionalPrepaid to set
     */
    public void setIsNotBookingOptionalPrepaid(Boolean isNotBookingOptionalPrepaid) {
        this.isNotBookingOptionalPrepaid = isNotBookingOptionalPrepaid;
    }

    public String getExportCode() {
        return exportCode;
    }

    public void setExportCode(String exportCode) {
        this.exportCode = exportCode;
    }

    public Boolean getShowAllOptionals() {
        return showAllOptionals;
    }

    public void setShowAllOptionals(Boolean showAllOptionals) {
        this.showAllOptionals = showAllOptionals;
    }

    public Boolean getIsApplicabilityWS() {
        return isApplicabilityWS;
    }

    public void setIsApplicabilityWS(Boolean isApplicabilityWS) {
        this.isApplicabilityWS = isApplicabilityWS;
    }

    public Set<MROldSconto> getSconti() {
        return sconti;
    }

    public void setSconti(Set<MROldSconto> sconti) {
        this.sconti = sconti;
    }
    public Boolean getIsOnlyShortRentAllowed() {
        return isOnlyShortRentAllowed;
    }

    public void setIsOnlyShortRentAllowed(Boolean isOnlyShortRentAllowed) {
        this.isOnlyShortRentAllowed = isOnlyShortRentAllowed;
    }
    public Boolean getInvoiceOpenClosedContracts() {
        return invoiceOpenClosedContracts;
    }

    public void setInvoiceOpenClosedContracts(Boolean invoiceOpenClosedContracts) {
        this.invoiceOpenClosedContracts = invoiceOpenClosedContracts;
    }

    public Boolean getActiveImportationJob() {
        return activeImportationJob;
    }

    public void setActiveImportationJob(Boolean activeImportationJob) {
        this.activeImportationJob = activeImportationJob;
    }

    public Boolean getInvoiceStartOrStartAndMidOfTheMonth() {
        return invoiceStartOrStartAndMidOfTheMonth;
    }

    public void setInvoiceStartOrStartAndMidOfTheMonth(Boolean invoiceStartOrStartAndMidOfTheMonth) {
        this.invoiceStartOrStartAndMidOfTheMonth = invoiceStartOrStartAndMidOfTheMonth;
    }


    public Boolean getCreateProformaInvoice() {
        return createProformaInvoice;
    }

    public void setCreateProformaInvoice(Boolean createProformaInvoice) {
        this.createProformaInvoice = createProformaInvoice;
    }
   /* public String[] getLoggableFields() {
        return new String[]{
                "codice", // NOI18N
                "ragioneSociale", // NOI18N
                "percentuale", // NOI18N
                "cliente", // NOI18N
                "fonteClass", // NOI18N
                "rentalType", // NOI18N
                "locationNetwork", // NOI18N
                "groupUpgradeSchemaId" // NOI18N
        };
    }

    public String[] getLoggableLabels() {
        return new String[]{
                bundle.getString("FonteCommissione.codice"),
                bundle.getString("FonteCommissione.ragioneSociale"),
                bundle.getString("FonteCommissione.percentuale"),
                bundle.getString("FonteCommissione.cliente"),
                bundle.getString("FonteCommissione.fonteClass"),
                bundle.getString("FonteCommissione.rentalType"),
                bundle.getString("FonteCommissione.locationNetwork"),
                bundle.getString("FonteCommissione.groupUpgradeSchemaId")
        };
    }

    public String getEntityName() {
        return "MROldFonteCommissione"; // NOI18N
    }

    public Integer getEntityId() {
        return getId();
    }*/

    public Double getXmlDiscount() {
        return xmlDiscount;
    }

    public void setXmlDiscount(Double xmlDiscount) {
        this.xmlDiscount = xmlDiscount;
    }



}
