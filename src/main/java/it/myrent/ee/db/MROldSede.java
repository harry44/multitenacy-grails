package it.myrent.ee.db;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/** @author Hibernate CodeGenerator */
public class MROldSede implements it.aessepi.utils.db.PersistentInstance, Comparable<MROldSede> {
    public static final Integer BOOKING_PICKUP = 1;
    public static final Integer BOOKING_DROPOFF = 2;
    public static final Integer BOOKING_BOTH = 3;
    
    private Integer id;
    private String codice;
    private String codiceReparto;
    private Boolean aeroporto;
    private Boolean ferrovia;
    private Boolean hasInvoiceEnumeration;
    private Boolean attiva;
    private String descrizione;
    private String via;
    private String numero;
    private String cap;
    private String citta;
    private String provincia;
    private String telefono1;
    private String telefono2;
    private String cellulare;
    private String nazione;
    private String email;
    private String promemoria;
    private MROldAffiliato affiliato;
    private Map numerazioni;
    private MROldRegione regione;
    private Integer colore;
    private Boolean bookingEnable;
    private Integer bookingType;
    private MROldCurrency currency;
    private Boolean outOfHours;
    private Date oraAperturaMattino;
    private Date oraChiusuraMattino;
    private Date oraAperturaPomeriggio;
    private Date oraChiusuraPomeriggio;
    private Boolean sempreAperto;
    private MROldBankAccount bankAccount;
    private MROldLocationNetwork locationNetwork;

    private MROldNetsAnagrafica merchantId;
    private Set<FreeSell> freeSell;
    private Set<MROldFonteCommissione> fonteCommissione;
    private Boolean virtualPosEnable;
    private Set locationListino;

    private Set nexiDeviceConfiguration;



    private Double latitude;


    private Double longitude;

    private Boolean onlyDropOffOutOfHours;
    private Integer minimumLeadTimeInHour;
    private Integer lastMinuteOfferInHour;
    private Boolean isVirtualLocation;
    private MROldSede virtualSede;
    private Boolean disabledStorageGuarantee;
    private String dropOffAddress;
    private String locationInfo;
    private String locationInfoEn;


    public Set getFonteCommissione() {
        return fonteCommissione;
    }

    public void setFonteCommissione(Set fonteCommissione) {
        this.fonteCommissione = fonteCommissione;
    }




    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public MROldSede() {
        setAttiva(true);
        setAeroporto(false);
        setFerrovia(false);
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(getCodice()).append(getDescrizione()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldSede)) {
            return false;
        }
        MROldSede castOther = (MROldSede) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    @Override
    public int compareTo(MROldSede o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getCodice(), o.getCodice()).
                    append(getDescrizione(), o.getDescrizione()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    public MROldSede(Integer id, String codice, String descrizione) {
        this.id = id;
        this.codice = codice;
        this.descrizione = descrizione;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUniDescription() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
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

    public String getPromemoria() {
        return this.promemoria;
    }

    public void setPromemoria(String promemoria) {
        this.promemoria = promemoria;
    }

    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }

    public MROldAffiliato getAffiliato() {
        return affiliato;
    }

    public Boolean getAeroporto() {
        return aeroporto;
    }

    public void setAeroporto(Boolean aeroporto) {
        this.aeroporto = aeroporto;
    }

    public Boolean getFerrovia() {
        return ferrovia;
    }

    public void setFerrovia(Boolean ferrovia) {
        this.ferrovia = ferrovia;
    }

    public Map getNumerazioni() {
        return numerazioni;
    }

    public void setNumerazioni(Map numerazioni) {
        this.numerazioni = numerazioni;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getCodiceReparto() {
        return codiceReparto;
    }

    public void setCodiceReparto(String codiceReparto) {
        this.codiceReparto = codiceReparto;
    }

    public MROldRegione getRegione() {
        return regione;
    }

    public void setRegione(MROldRegione regione) {
        this.regione = regione;
    }

    public Boolean getAttiva() {
        return attiva;
    }

    public void setAttiva(Boolean attiva) {
        this.attiva = attiva;
    }

    public Boolean getBookingEnable() {
        return bookingEnable;
    }

    public void setBookingEnable(Boolean bookingEnable) {
        this.bookingEnable = bookingEnable;
    }

    public Integer getBookingType() {
        return bookingType;
    }

    public void setBookingType(Integer bookingType) {
        this.bookingType = bookingType;
    }

    public void setColore(Integer colore) {
        this.colore = colore;
    }

    public Integer getColore() {
        return colore;
    }

    public MROldCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(MROldCurrency currency) {
        this.currency = currency;
    }

    public Boolean getOutOfHours() {
        return outOfHours;
    }

    public void setOutOfHours(Boolean outOfHours) {
        this.outOfHours = outOfHours;
    }

    public Date getOraAperturaMattino() {
        return oraAperturaMattino;
    }

    public void setOraAperturaMattino(Date oraAperturaMattino) {
        this.oraAperturaMattino = oraAperturaMattino;
    }

    public Date getOraChiusuraMattino() {
        return oraChiusuraMattino;
    }

    public void setOraChiusuraMattino(Date oraChiusuraMattino) {
        this.oraChiusuraMattino = oraChiusuraMattino;
    }

    public Date getOraAperturaPomeriggio() {
        return oraAperturaPomeriggio;
    }

    public void setOraAperturaPomeriggio(Date oraAperturaPomeriggio) {
        this.oraAperturaPomeriggio = oraAperturaPomeriggio;
    }

    public Date getOraChiusuraPomeriggio() {
        return oraChiusuraPomeriggio;
    }

    public void setOraChiusuraPomeriggio(Date oraChiusuraPomeriggio) {
        this.oraChiusuraPomeriggio = oraChiusuraPomeriggio;
    }

    public Boolean getSempreAperto() {
        return sempreAperto;
    }

    public void setSempreAperto(Boolean sempreAperto) {
        this.sempreAperto = sempreAperto;
    }

    public MROldBankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(MROldBankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public MROldLocationNetwork getLocationNetwork() {
        return this.locationNetwork;
    }

    public void setLocationNetwork(MROldLocationNetwork locationNetwork) {
        this.locationNetwork = locationNetwork;
    }

    public Boolean getVirtualPosEnable() {
        return virtualPosEnable;
    }

    public void setVirtualPosEnable(Boolean virtualPosEnable) {
        this.virtualPosEnable = virtualPosEnable;
    }
    public Set getLocationListino() {
        return locationListino;
    }

    public void setLocationListino(Set locationListino) {
        this.locationListino = locationListino;
    }


    public Boolean getHasInvoiceEnumeration() {
        return hasInvoiceEnumeration;
    }

    public void setHasInvoiceEnumeration(Boolean hasInvoiceEnumeration) {
        this.hasInvoiceEnumeration = hasInvoiceEnumeration;
    }

    public Set<FreeSell> getFreeSell() {
        return freeSell;
    }

    public void setFreeSell(Set<FreeSell> freeSell) {
        this.freeSell = freeSell;
    }

    public MROldNetsAnagrafica getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(MROldNetsAnagrafica merchantId) {
        this.merchantId = merchantId;
    }

    public Set getNexiDeviceConfiguration() {
        return nexiDeviceConfiguration;
    }

    public void setNexiDeviceConfiguration(Set nexiDeviceConfiguration) {
        this.nexiDeviceConfiguration = nexiDeviceConfiguration;
    }

    public MROldSede getVirtualSede() {
        return virtualSede;
    }

    public void setVirtualSede(MROldSede virtualSede) {
        this.virtualSede = virtualSede;
    }

    public Boolean getIsVirtualLocation() {
        return isVirtualLocation;
    }

    public void setIsVirtualLocation(Boolean isVirtualLocation) {
        this.isVirtualLocation = isVirtualLocation;
    }

    public Integer getMinimumLeadTimeInHour() {
        return minimumLeadTimeInHour;
    }

    public void setMinimumLeadTimeInHour(Integer minimumLeadTimeInHour) {
        this.minimumLeadTimeInHour = minimumLeadTimeInHour;
    }

    public Integer getLastMinuteOfferInHour() {
        return lastMinuteOfferInHour;
    }

    public void setLastMinuteOfferInHour(Integer lastMinuteOfferInHour) {
        this.lastMinuteOfferInHour = lastMinuteOfferInHour;
    }

    public Boolean getOnlyDropOffOutOfHours() {
        return onlyDropOffOutOfHours;
    }

    public void setOnlyDropOffOutOfHours(Boolean onlyDropOffOutOfHours) {
        this.onlyDropOffOutOfHours = onlyDropOffOutOfHours;
    }
    public Boolean getDisabledStorageGuarantee() {
        return disabledStorageGuarantee;
    }

    public void setDisabledStorageGuarantee(Boolean disabledStorageGuarantee) {
        this.disabledStorageGuarantee = disabledStorageGuarantee;
    }
    public String getDropOffAddress() {
        return dropOffAddress;
    }

    public void setDropOffAddress(String dropOffAddress) {
        this.dropOffAddress = dropOffAddress;
    }
    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    public String getLocationInfoEn() {
        return locationInfoEn;
    }

    public void setLocationInfoEn(String locationInfoEn) {
        this.locationInfoEn = locationInfoEn;
    }
}
