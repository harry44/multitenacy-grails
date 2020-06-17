package com.dogmasystems.myrent.db

import com.dogmasystems.touroperatorportal.Listini
import com.dogmasystems.touroperatorportal.Pagamenti
import com.dogmasystems.touroperatorportal.Sconti
import com.dogmasystems.touroperatorportal.ValiditaListiniFonti
import org.apache.commons.lang.builder.CompareToBuilder
import org.apache.commons.lang.builder.EqualsBuilder

//import com.dogmasystems.old.db.User
import org.apache.commons.lang.builder.HashCodeBuilder

//import com.dogmasystems.utils.db.PersistentInstance;
class MRReservationSource implements Serializable, Cloneable {

    String code;
    String companyName;
    String street;
    String number;
    String postalCode;
    String city;
    String province;
    String vatNumber;
    String phoneNumb1;
    String phoneNumb2;
    String cellNumber;
    String bankAccount;
    String nation;
    String email;
    String reminder;
    String taxCode;
    Boolean isDiscountable;
    Pagamenti pagamenti;
    Double percentage;
    String exportCode
    Listini listinoExtraPrepay;

    //MRCatalog listinoExtraPrepay;
    MRReservationSource sourceExtraPPay;
    MRReservationSource masterReservation;
    MRBusinessPartner client;            //Class changed to MRBusinessPartner as MRClient is deleted
    Boolean multiSeason;
    Boolean differedPayment;
    Boolean showAllOptional
    Boolean isApplicabilityWS
//	Set<MRSourceCatalogValidity> listini;
//	Set<User> users;
    Boolean isDeactivated;
    MRSourceClass sourceClass;
    Boolean isvirtual

//Madhvendra
    RentalType rentalType
    Boolean isDisable;
    DepositAndWaiverResSource depositResSourceId;

    Boolean isMonthlyBilled;
    Boolean isSummaryinvoice;
    Boolean isCDWEnabled;
    Boolean isTheftEnabled;
    Boolean isCDWAndTheftEnabled;
    Boolean isRoadAssistanceEnabled;
    Boolean isNotBookingOptionalPrepaid;
    MRLocationNetwork locationNetwork
    MRGroupUpgradeSchema groupUpgradeSchemaId
    Boolean isOnlyShortRentAllowed
    Boolean invoiceOpenClosedContracts
    Boolean activeImportationJob
    Boolean invoiceStartOrStartAndMidOfTheMonth
    Boolean createProformaInvoice
    Double xmlDiscount
    static auditable = true


    public MRReservationSource(Integer id, String ragioneSociale) {
        setId(id);
        setCompanyName(ragioneSociale);
    }


    public int compareTo(MRReservationSource o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getCompanyName(), o.getCompanyName()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    public String toString() {
        return companyName != null ? companyName : new String();
    }

    public boolean equals(Object other) {
        if (!(other instanceof MRReservationSource)) {
            return false;
        }
        MRReservationSource castOther = (MRReservationSource) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    static hasOne = [depositResSourceId: DepositAndWaiverResSource]
    /*static hasMany = [clientis: Clienti,
     commissionis: Commissioni,
     userses: Users,
     validitaListiniFontis: ValiditaListiniFonti]*/
    static hasMany = [//commissionis: MRCommission,
                      //clienti: MRBusinessPartner,
                      fontiusers        : MRSourceUser,
                      fontilocations    : MRLocation,
                      listini           : ValiditaListiniFonti,
                      depositByGroups   : MRCCDepositByGroup,
                      discount          : Sconti,
                      clienteReservation: MRBusinessPartner
    ]
//	static belongsTo = [MRClient, MRCatalog, MRPayment]
    static belongsTo = [MRBusinessPartner]        //Class changed to MRBusinessPartner as MRClient is deleted
    //static belongsTo = [Listini, Pagamenti]

    static mapping = {
        cache true

        depositResSourceId cache: true
        fontiusers cache: true
        fontilocations cache: true
        listini cache: true
        discount cache: true

        table name: "fonti_commissioni"//, schema: "public"
        id column: "id", sqlType: "int4"
        id generator: 'sequence', params: [sequence: 'fonti_commissioni_seq']
        code column: "codice", sqlType: "int4"
        street column: "via"
        number column: "numero"
        postalCode column: "cap"
        city column: "citta"
        province column: "provincia"
        vatNumber column: "partita_iva"
        phoneNumb1 column: "telefono1"
        phoneNumb2 column: "telefono2"
        cellNumber column: "cellulare"
        nation column: "nazione"
        sourceExtraPPay column: "id_fonte_commissione_extra_ppay", sqlType: "int4"
        groupUpgradeSchemaId column: "id_group_upgrade_schema", sqlType: "int4"
        client column: "id_cliente", sqlType: "int4"
        pagamenti column: "id_pagamento", sqlType: "int4"
        differedPayment column: "differed_payment"
        taxCode column: "codice_fiscale"
        isDiscountable column: "scontabile"
        percentage column: "percentuale"
        multiSeason column: "multistagione"
        bankAccount column: "cc"
        sourceClass column: "fonte_class_id", sqlType: "int4"
        rentalType column: "rentalType", sqlType: "int4"
        exportCode column: "export_code"
        isMonthlyBilled column: "ismonthlybilled"
        isSummaryinvoice column: "issummaryinvoice"
        isCDWEnabled column: "iscdwenabled"
        isTheftEnabled column: "istheftenabled"
        isCDWAndTheftEnabled column: "iscdwandtheftenabled"
        isRoadAssistanceEnabled column: "isroadassistanceenabled"
        locationNetwork column: "locationnetwork", sqlType: "int4"
        listinoExtraPrepay column: "id_listino_extra_prepay", sqlType: "int4"
        masterReservation column: "id_master_reservation", sqlType: "int4"
        exportCode column: "export_code"
        isDeactivated column: "is_deactivated"
        isNotBookingOptionalPrepaid column: "is_not_booking_optional_prepaid", defaultValue: false
        isDisable column: "isdisable", defaultValue: false
        companyName column: "ragione_sociale"
        email column: "email"
        reminder column: "promemoria"

        fontilocations joinTable: [name: 'reservation_location', key: 'id_reservation_source']
        //clienti joinTable: [name: "clienti_reservation_source", key: 'id_cliente_res']
        clienteReservation joinTable: [name: "clienti_reservation_source", key: "id_cliente_reservation", column: "id_cliente_res"]

        depositResSourceId cascade: 'all', fetch: 'join', unique: true, sqlType: "int4"

//        fontidiscount joinTable:[name:'fonte_discount', key: 'id_reservation_source']
        isApplicabilityWS column: "is_applicability_ws"
        showAllOptional column: "show_all_optionals"
        isOnlyShortRentAllowed column: "isonlyshortrentallowed"
        invoiceOpenClosedContracts column: "invoice_open_closed_contracts"
        activeImportationJob column: "active_omportation_job"
        invoiceStartOrStartAndMidOfTheMonth column: "invoice_start_or_start_and_mid_of_the_month"
        createProformaInvoice column: "creatr_proforma_invoice"
        xmlDiscount column: "xml_discount"
        version false
    }

    static constraints = {
        code nullable: true, unique: true
        companyName nullable: true
        street nullable: true
        number nullable: true
        postalCode nullable: true
        city nullable: true
        province nullable: true
        phoneNumb1 nullable: true
        phoneNumb2 nullable: true
        cellNumber nullable: true
        email nullable: true
        vatNumber nullable: true
        taxCode nullable: true
        nation nullable: true
        reminder nullable: true
        percentage nullable: true, scale: 17
        //	cc nullable: true
        isDiscountable nullable: true
        multiSeason nullable: true
        isDeactivated nullable: true
        sourceClass nullable: true
        sourceExtraPPay nullable: true
        rentalType nullable: true
        bankAccount nullable: true
        isDisable nullable: true
        isNotBookingOptionalPrepaid nullable: true
        isMonthlyBilled nullable: true
        isSummaryinvoice nullable: true
        isCDWEnabled nullable: true
        isTheftEnabled nullable: true
        isCDWAndTheftEnabled nullable: true
        isRoadAssistanceEnabled nullable: true
        locationNetwork nullable: true
        exportCode nullable: true
        groupUpgradeSchemaId nullable: true
        depositResSourceId nullable: true
        isvirtual nullable: true
        client nullable: true
        fontilocations nullable: true
        pagamenti nullable: true
        //reservationSourceId nullable: true
        listinoExtraPrepay nullable: true
        isApplicabilityWS nullable: true
        showAllOptional nullable: true
        isOnlyShortRentAllowed nullable: true
        invoiceOpenClosedContracts nullable: true
        activeImportationJob nullable: true
        invoiceStartOrStartAndMidOfTheMonth nullable: true
        createProformaInvoice nullable: true
        xmlDiscount nullable: true, scale: 17

    }

//	@Override
//	public Long getId() {
//		return this.id;
//	}
//	public void setId(Long id){
//		this.id = id;
//	}

    @Override
    public Object clone() {
        MRReservationSource obj = new MRReservationSource();

        Set<ValiditaListiniFonti> newList = new TreeSet<ValiditaListiniFonti>(new ValiditaListiniFontiComparator());
        Set<ValiditaListiniFonti> oldList = this.getListini();
        Iterator<ValiditaListiniFonti> itr = oldList.iterator();
        ValiditaListiniFonti li; //the new one
        ValiditaListiniFonti valid; // the old one

        while (itr.hasNext()) {
            valid = itr.next();
            li = new ValiditaListiniFonti();

            //li.setId(valid.getId());
            li.setFineofferta(valid.getFineofferta());
            li.setFinestagione(valid.getFinestagione());
            li.setInizioofferta(valid.getInizioofferta());
            li.setIniziostagione(valid.getIniziostagione());
            li.setListini(valid.getListini());
            li.setFontiCommissioni(valid.getFontiCommissioni());
            newList.add(li);
        }
        if (this.getCompanyName() != null) {
            obj.setCompanyName("copy of " + this.getCompanyName());
        }

        if (this.getCode() != null) {
            obj.setCode("copy_" + this.getCode());
        }

        if (obj.getListini() != null)
            obj.getListini().addAll(newList);
        else {
            obj.setListini(new TreeSet<ValiditaListiniFonti>(new ValiditaListiniFontiComparator()));
            obj.setListini(newList);
        }
        obj.setEmail(this.getEmail());
        obj.setPagamenti(this.getPagamenti());
        obj.setPercentage(this.getPercentage());
        obj.setReminder(this.getReminder());
        obj.setIsDiscountable(this.getIsDiscountable());
        obj.setListinoExtraPrepay(this.getListinoExtraPrepay());
        obj.setSourceExtraPPay(this.getSourceExtraPPay());
        obj.setMasterReservation(this.getMasterReservation());
        obj.setClient(this.getClient());
        obj.setMultiSeason(this.getMultiSeason());
        obj.setDifferedPayment(this.getDifferedPayment());
        obj.setShowAllOptional(this.getShowAllOptional());
        obj.setIsApplicabilityWS(this.getIsApplicabilityWS());
        obj.setSourceClass(this.getSourceClass());
        obj.setIsvirtual(this.getIsvirtual());
        obj.setRentalType(this.getRentalType());
        if (this.getDepositResSourceId() != null) {
            obj.setDepositResSourceId(this.getDepositResSourceId());
        } else {
            obj.setDepositResSourceId(new DepositAndWaiverResSource())
        }
        obj.setIsCDWEnabled(this.getIsCDWEnabled());
        obj.setIsTheftEnabled(this.getIsTheftEnabled());
        obj.setIsCDWAndTheftEnabled(this.getIsCDWAndTheftEnabled());
        obj.setIsRoadAssistanceEnabled(this.getIsRoadAssistanceEnabled());
        obj.setLocationNetwork(this.getLocationNetwork());
        obj.setGroupUpgradeSchemaId(this.getGroupUpgradeSchemaId());
        obj.setXmlDiscount(this.getXmlDiscount());
        return obj;
    }

}
