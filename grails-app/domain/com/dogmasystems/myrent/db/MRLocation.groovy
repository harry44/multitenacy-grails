package com.dogmasystems.myrent.db

import com.dogmasystems.touroperatorportal.Listini
import grails.util.Holders

import java.util.Currency;
import java.util.Date;
import java.util.Map;
import com.dogmasystems.touroperatorportal.Regioni;
import org.apache.commons.lang.builder.CompareToBuilder
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.apache.commons.lang.builder.ToStringBuilder

class MRLocation implements Serializable {
    MRAffiliate affiliate
    String code;
    String departmentCode;
    Boolean isAirport;
    Boolean isRailway;
    Boolean isActive;
    String description;
    String street;
    String number;
    String postalCode;
    String city;
    String province;
    Boolean hasInvoiceEnumeration;
//	private String zipCode;
//	private String city;
//	private String province;
    String phoneNumb1;   //to add
    String phoneNumb2;   //to add
    String mobile;  //to add     mobile
    String country;    //to add country
    String email;      //to add
    String notes;  //to add    notes
    //private Map numerazioni;
    Regioni regione;
    Integer colore;
    Boolean isBookingEnable;
    Boolean isSempreAperto     //to add
    Integer bookingType;       //to add
//	private Currency MRCurrency;

    MRCurrency currency
    Boolean isOutOfHours;       //to add
    Date oraAperturaMattino;
    Date oraChiusuraMattino;
    Date oraAperturaPomeriggio;
    Date oraChiusuraPomeriggio;
    Boolean isAlwaysOpen;     //to add        alwaysOpen
//	private MRBankAccount bankAccount;
    /////////////////////////////////////new elements : added by Saurabh/////////////////////
    Date updated
    MRUser updatedBy
    String eaLivTarEpr//EA_LIV_TAR_EPR
    Integer eaDamage
    Integer eaDamageTheft
    Integer eaIndividualRent
    Integer eaDeflott
    String eaAreaManager//EA_AREA_MANAGER
    Integer eaStandardRate//EA_STANDARD_RATE
    String eaAffiliate//EA_AFFILIATE
    String eaTypology//EA_TYPOLOGY
    //Madhvendra
    MRLocationNetwork locationNetwork
    Boolean virtualPosEnable
    // Added field
    Integer proKey
    String externalID
    Double latitude
    Double longitude
    MRBankAccount bankAccount
    MRNetsAnagrafica netsAnagrafica
    Boolean isVirtualLocation
    MRLocation virtualSede
    Integer minimumLeadTimeInHour
    Integer lastMinuteOfferInHour
    Boolean onlyDropOffOutOfHours
    Boolean disabledStorageGuarantee
    String dropOffAddress
    String locationInfo
    String locationInfoEn
    /////////////////////////////////////////////////////////////////////////////////////////
    public MRLocation() {
        setIsActive(true);
        setIsAirport(false);
        setIsRailway(false);
    }

    public String toString() {
        //getCode()+ " " + getDescription();
        def str = ""
        str += code != null ? code + " - " : ""
        str += description != null ? description : ""
        return str
    }

    public boolean equals(Object other) {
        if (!(other instanceof MRLocation)) {
            return false;
        }
        MRLocation castOther = (MRLocation) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public int compareTo(MRLocation o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getCode(), o.getCode()).
                    append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    static hasMany = [parcoVeicolis    : MRVehicle,
                      locationressource: MRReservationSource,
                      sediUsers        : MRUserLocation,
                      userses          : MRUser,
                      locationlistinis : Listini]
    static belongsTo = [MRAffiliate, MRReservationSource, Listini]

    static mappedBy = [userses: 'location']
    static mapping = {
        cache true
        sort("description")
        table name: "sedi"//, schema:"public"
        id generator: 'sequence', params: [sequence: 'sedi_seq']
        id column: "id", sqlType: "int4"

        affiliate column: "id_affiliato", sqlType: "int4"
        code column: "codice"
        departmentCode column: "codice_reparto"

        isAirport column: "aeroporto"
        isRailway column: "ferrovia"
        isActive column: "attiva"
        postalCode column: "cap"
        city column: "citta"
        number column: "numero"
        province column: "provincia"
        street column: "via"
        email column: "email"
        country column: "nazione"
        phoneNumb1 column: "telefono1"
        phoneNumb2 column: "telefono2"
        isBookingEnable column: "booking_enable"
        bookingType column: "booking_type"
        isOutOfHours column: "out_of_hours"
        isSempreAperto column: "sempre_aperto"
        mobile column: "cellulare"
        regione column: "id_regione", sqlType: "int4"
        oraAperturaMattino column: "ora_apertura_mattino"
        oraChiusuraMattino column: "ora_chiusura_mattino"
        oraAperturaPomeriggio column: "ora_apertura_pomeriggio"
        oraChiusuraPomeriggio column: "ora_chiusura_pomeriggio"
        locationNetwork column: "locationNetwork"
        virtualPosEnable column: "virtual_pos_enable"
        latitude column: "latitude"
        longitude column: "longitude"
        locationressource joinTable: [name: 'reservation_location', key: 'id_location']
        locationlistinis joinTable: [name: 'listini_sedi', key: 'mrlocation_id']
        description column: "descrizione"
        notes column: "promemoria"
        hasInvoiceEnumeration column: "has_invoice_enum"
        proKey column: "prokey", sqlType: "int4"
        currency column: "id_currency", sqlType: "int4"
        bankAccount column: "id_bank_account", sqlType: "int4"
        netsAnagrafica column: "id_merchant"
        isVirtualLocation column: "is_virtual_location"
        virtualSede column: "virtual_sede", sqlType: "int4"

        minimumLeadTimeInHour column: "minimum_lead_time_in_hour"
        lastMinuteOfferInHour column: "last_minute_offer_in_hour"
        onlyDropOffOutOfHours column: "only_drop_off_out_of_hours"
        disabledStorageGuarantee column: "guarantee_storage_disabled"
        dropOffAddress column: "drop_off_address"
        locationInfo column: "location_info"
        locationInfoEn column: "location_info_en"
        version false
    }

    static constraints = {
        locationlistinis nullable: true
        virtualPosEnable nullable: true
        code nullable: true
        departmentCode nullable: true
        isAirport nullable: true
        isRailway nullable: true
        isActive nullable: true
        description nullable: true
        street nullable: true
        number nullable: true
        city nullable: true
        province nullable: true
        postalCode nullable: true
        updated nullable: true
        updatedBy nullable: true
        eaLivTarEpr nullable: true
        eaDamage nullable: true
        eaDamageTheft nullable: true
        eaIndividualRent nullable: true
        eaDeflott nullable: true
        eaAreaManager nullable: true
        eaStandardRate nullable: true
        eaAffiliate nullable: true
        isAlwaysOpen nullable: true
        eaTypology nullable: true
        locationNetwork nullable: true
        //added for importing data from myrent.pro to myrent.ee db
        bookingType nullable: true
        colore nullable: true
        email nullable: true
        isBookingEnable nullable: true
        isOutOfHours nullable: true
        isSempreAperto nullable: true
        mobile nullable: true
        oraAperturaMattino nullable: true
        oraAperturaPomeriggio nullable: true
        oraChiusuraMattino nullable: true
        oraChiusuraPomeriggio nullable: true
        phoneNumb1 nullable: true
        phoneNumb2 nullable: true
        regione nullable: true
        affiliate nullable: true
        // Added for migration
        proKey nullable: true
        country nullable: true
        notes nullable: true
        externalID nullable: true
        hasInvoiceEnumeration nullable: true
        latitude nullable: true
        longitude nullable: true
        bankAccount nullable: true
        currency nullable: true
        netsAnagrafica nullable: true
        isVirtualLocation nullable: true
        virtualSede nullable: true
        minimumLeadTimeInHour nullable: true
        lastMinuteOfferInHour nullable: true
        onlyDropOffOutOfHours nullable: true
        disabledStorageGuarantee nullable: true
        dropOffAddress nullable: true
        locationInfo nullable: true
        locationInfoEn nullable: true
    }
}
