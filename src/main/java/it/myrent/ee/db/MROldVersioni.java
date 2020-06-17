package it.myrent.ee.db;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class MROldVersioni implements it.aessepi.utils.db.PersistentInstance {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private Integer idMarca;

    /** nullable persistent field */
    private Integer idModello;

    /** nullable persistent field */
    private String descrizioneVersione;

    /** nullable persistent field */
    private String codiceVersione;

    /** nullable persistent field */
    private String serie;

    /** nullable persistent field */
    private String codiceMotore;

    /** nullable persistent field */
    private String codVersione;

    /** nullable persistent field */
    private String motorizzazione;

    /** nullable persistent field */
    private String dataInizio;

    /** nullable persistent field */
    private String dataFine;

    /** nullable persistent field */
    private String cilindrata;

    /** nullable persistent field */
    private String kw;

    /** nullable persistent field */
    private String cv;

    /** full constructor */
    public MROldVersioni(Integer idMarca, Integer idModello, String descrizioneVersione, String codiceVersione, String serie, String codiceMotore, String codVersione, String motorizzazione, String dataInizio, String dataFine, String cilindrata, String kw, String cv) {
        this.idMarca = idMarca;
        this.idModello = idModello;
        this.descrizioneVersione = descrizioneVersione;
        this.codiceVersione = codiceVersione;
        this.serie = serie;
        this.codiceMotore = codiceMotore;
        this.codVersione = codVersione;
        this.motorizzazione = motorizzazione;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.cilindrata = cilindrata;
        this.kw = kw;
        this.cv = cv;
    }

    /** default constructor */
    public MROldVersioni() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdMarca() {
        return this.idMarca;
    }

    public void setIdMarca(Integer idMarca) {
        this.idMarca = idMarca;
    }

    public Integer getIdModello() {
        return this.idModello;
    }

    public void setIdModello(Integer idModello) {
        this.idModello = idModello;
    }

    public String getDescrizioneVersione() {
        return this.descrizioneVersione;
    }

    public void setDescrizioneVersione(String descrizioneVersione) {
        this.descrizioneVersione = descrizioneVersione;
    }

    public String getCodiceVersione() {
        return this.codiceVersione;
    }

    public void setCodiceVersione(String codiceVersione) {
        this.codiceVersione = codiceVersione;
    }

    public String getSerie() {
        return this.serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getCodiceMotore() {
        return this.codiceMotore;
    }

    public void setCodiceMotore(String codiceMotore) {
        this.codiceMotore = codiceMotore;
    }

    public String getCodVersione() {
        return this.codVersione;
    }

    public void setCodVersione(String codVersione) {
        this.codVersione = codVersione;
    }

    public String getMotorizzazione() {
        return this.motorizzazione;
    }

    public void setMotorizzazione(String motorizzazione) {
        this.motorizzazione = motorizzazione;
    }

    public String getDataInizio() {
        return this.dataInizio;
    }

    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    public String getDataFine() {
        return this.dataFine;
    }

    public void setDataFine(String dataFine) {
        this.dataFine = dataFine;
    }

    public String getCilindrata() {
        return this.cilindrata;
    }

    public void setCilindrata(String cilindrata) {
        this.cilindrata = cilindrata;
    }

    public String getKw() {
        return this.kw;
    }

    public void setKw(String kw) {
        this.kw = kw;
    }

    public String getCv() {
        return this.cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MROldVersioni) ) return false;
        MROldVersioni castOther = (MROldVersioni) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

    
   
}
