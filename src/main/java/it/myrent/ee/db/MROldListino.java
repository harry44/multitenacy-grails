package it.myrent.ee.db;


import it.aessepi.utils.db.PersistentInstance;
import java.util.Date;
import java.util.Set;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/** @author Hibernate CodeGenerator */
public class MROldListino implements PersistentInstance {

    private Integer id;
    
    private String descrizione;

    private Integer numeroDocumento;
    
    private MROldCodiciIva codiceIva;
    
    private MROldCodiciIva codiceIvaNonSoggetto;
    
    private Boolean ivaInclusa;
    
    private String annotazioni;

    private  List<MROldDurata> durate;

    private Set tariffe;
    
    private Set optionalsListino;
    
    private Double depositoSuperAss;
    
    private Double depositoSenzaAss;
    
    private Double depositoContanti;

    private MROldSede sede;

    private Date oraRientro;

    private Boolean oraRientroAttiva;
    
    private MROldRateType rateTypeId;
    
    private MROldListino rateCommissionId;
//    private Set users;
    //Madhvendra (for Postgres version)
    private Boolean isCalendar;
    private Boolean isDisable;
    private MROldListino optionalRate;
    private Set listinoGroup;

    private Set listinoLocation;
    public Boolean getIsDisable() {
        return isDisable;
    }

    public void setIsDisable(Boolean isDisable) {
        this.isDisable = isDisable;
    }
    ///
    /** default constructor */
    public MROldListino() {
        
    }
    
    public MROldListino(Integer id, Integer numeroDocumento, String descrizione) {
        this.setId(id);
        this.setNumeroDocumento(numeroDocumento);
        this.setDescrizione(descrizione);
    }

    public void setIsCalendar(Boolean isCalendar) {
        this.isCalendar = isCalendar;
    }
    public void setOptionalRate(MROldListino optionalRate) {
        this.optionalRate = optionalRate;
    }

    public MROldListino getOptionalRate() {
        return optionalRate;
    }
    public Set getListinoGroup() {
        return listinoGroup;
    }

    public void setListinoGroup(Set listinoGroup) {
        this.listinoGroup = listinoGroup;
    }

    public Set getListinoLocation() {
        return listinoLocation;
    }

    public void setListinoLocation(Set listinoLocation) {
        this.listinoLocation = listinoLocation;
    }
    public Boolean getIsCalendar() {
        return isCalendar;
    }
    public MROldRateType getRateTypeId() {
        return this.rateTypeId;
    }

    public void setRateTypeId(MROldRateType rateTypeId) {
        this.rateTypeId = rateTypeId;
    }

    public MROldListino getRateCommissionId() {
        return rateCommissionId;
    }

    public void setRateCommissionId(MROldListino rateCommissionId) {
        this.rateCommissionId = rateCommissionId;
    }

   
    public String toString() {
        if(getNumeroDocumento() != null && getDescrizione() != null) {
            return getNumeroDocumento().toString() + " - " + getDescrizione(); //NOI18N
        } else {
            return new String();
        }        
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MROldListino) ) return false;
        MROldListino castOther = (MROldListino) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Integer numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    
    public String getAnnotazioni() {
        return annotazioni;
    }

    public void setAnnotazioni(String annotazioni) {
        this.annotazioni = annotazioni;
    }

    public List<MROldDurata> getDurate() {
        return durate;
    }

    public void setDurate(List<MROldDurata> durate) {
        this.durate = durate;
    }   

    public Set getTariffe() {
        return tariffe;
    }

    public void setTariffe(Set tariffe) {
        this.tariffe = tariffe;
    }

    public Set getOptionalsListino() {
        return optionalsListino;
    }

    public void setOptionalsListino(Set optionalsListino) {
        this.optionalsListino = optionalsListino;
    }

    public MROldCodiciIva getCodiceIva() {
        return codiceIva;
    }

    public void setCodiceIva(MROldCodiciIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    public Boolean getIvaInclusa() {
        return ivaInclusa;
    }

    public void setIvaInclusa(Boolean ivaInclusa) {
        this.ivaInclusa = ivaInclusa;
    }

    public Double getDepositoSuperAss() {
        return depositoSuperAss;
    }

    public void setDepositoSuperAss(Double depositoSuperAss) {
        this.depositoSuperAss = depositoSuperAss;
    }

    public Double getDepositoSenzaAss() {
        return depositoSenzaAss;
    }

    public void setDepositoSenzaAss(Double depositoSenzaAss) {
        this.depositoSenzaAss = depositoSenzaAss;
    }

    public Double getDepositoContanti() {
        return depositoContanti;
    }

    public void setDepositoContanti(Double depositoContanti) {
        this.depositoContanti = depositoContanti;
    }

    public MROldCodiciIva getCodiceIvaNonSoggetto() {
        return codiceIvaNonSoggetto;
    }

    public void setCodiceIvaNonSoggetto(MROldCodiciIva codiceIvaNonSoggetto) {
        this.codiceIvaNonSoggetto = codiceIvaNonSoggetto;
    }

    public void setSede(MROldSede sede) {
        this.sede = sede;
    }

    public MROldSede getSede() {
        return sede;
    }

    public void setOraRientro(Date oraRientro) {
        this.oraRientro = oraRientro;
    }

    public Date getOraRientro() {
        return oraRientro;
    }

    public void setOraRientroAttiva(Boolean oraRientroAttiva) {
        this.oraRientroAttiva = oraRientroAttiva;
    }

    public Boolean getOraRientroAttiva() {
        return oraRientroAttiva;
    }

//
//    public Set getUsers() {
//        return users;
//    }
//
//    public void setUsers(Set users) {
//        this.users = users;
//    }
}
