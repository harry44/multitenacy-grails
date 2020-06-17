package it.myrent.ee.db;

import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import java.awt.Image;
import javax.swing.ImageIcon;
import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MROldAutista implements it.aessepi.utils.db.PersistentInstance {
    
    /** identifier field */
    private Integer id;
    
    private String nome;
    private String cognome;
    
    private String cap;
    private String provincia;
    private String citta;
    private String nazione;
    private String via;
    private String numero;
    
    private String telefono;
    
    private String luogoNascita;
    private String provinciaNascita;
    private String nazioneNascita;
    private Date dataNascita;
    
    private String documento;
    private String numeroDocumento;
    private String rilasciatoDa;
    private Date dataRilascio;
    private Date dataScadenza;
    private String categoriaPatente;
    
    private String proMemoria;
    private byte[] foto;
    private Boolean isFotoSet;

    /** Dati fiscali **/
    private String codiceFiscale;
    
    public static final StandardToStringStyle TO_STRING_STYLE = new StandardToStringStyle();
    
    static {
        TO_STRING_STYLE.setContentEnd(new String());
        TO_STRING_STYLE.setContentStart(new String());
        TO_STRING_STYLE.setNullText(new String());
        TO_STRING_STYLE.setUseIdentityHashCode(false);
        TO_STRING_STYLE.setUseClassName(false);
        TO_STRING_STYLE.setUseFieldNames(false);
        TO_STRING_STYLE.setFieldSeparator(" "); //NOI18N
    }
    
    public MROldAutista() {
    }
    
    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE)
        .append(getCognome())
        .append(getNome())
        .toString();
    }
    
    public boolean equals(Object other) {
        if(other != null && other instanceof MROldAutista) {
            return new EqualsBuilder()
            .append(this.getId(), ((MROldAutista)other).getId())
            .isEquals();
        }
        return false;
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
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
    
    public String getTelefono() {
        return this.telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getLuogoNascita() {
        return this.luogoNascita;
    }
    
    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }
    
    public String getProvinciaNascita() {
        return this.provinciaNascita;
    }
    
    public void setProvinciaNascita(String provinciaNascita) {
        this.provinciaNascita = provinciaNascita;
    }
    
    public Date getDataNascita() {
        return this.dataNascita;
    }
    
    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }
    
    public String getNumeroDocumento() {
        return this.numeroDocumento;
    }
    
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }
    
    public String getRilasciatoDa() {
        return this.rilasciatoDa;
    }
    
    public void setRilasciatoDa(String rilasciatoDa) {
        this.rilasciatoDa = rilasciatoDa;
    }
    
    public Date getDataRilascio() {
        return this.dataRilascio;
    }
    
    public void setDataRilascio(Date dataRilascio) {
        this.dataRilascio = dataRilascio;
    }
    
    public Date getDataScadenza() {
        return this.dataScadenza;
    }
    
    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }
    
    public void setProMemoria(String proMemoria) {
        this.proMemoria = proMemoria;
    }
    
    public String getProMemoria() {
        return this.proMemoria;
    }
    
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    
    public byte[] getFoto() {
        return foto;
    }
    
    public void setIsFotoSet(Boolean isFotoSet) {
        this.isFotoSet = isFotoSet;
    }
    
    public Boolean getIsFotoSet() {
        return isFotoSet;
    }
    
    public Image getFotoImage() {
        try {
            return new ImageIcon(getFoto()).getImage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public String getCategoriaPatente() {
        return categoriaPatente;
    }
    
    public void setCategoriaPatente(String categoriaPatente) {
        this.categoriaPatente = categoriaPatente;
    }
    
    public String getNazione() {
        return nazione;
    }
    
    public void setNazione(String nazione) {
        this.nazione = nazione;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getCognome() {
        return cognome;
    }
    
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * @return the nazioneNascita
     */
    public String getNazioneNascita() {
        return nazioneNascita;
    }

    /**
     * @param nazioneNascita the nazioneNascita to set
     */
    public void setNazioneNascita(String nazioneNascita) {
        this.nazioneNascita = nazioneNascita;
    }

    /**
     * @return the codiceFiscale
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * @param codiceFiscale the codiceFiscale to set
     */
    public void setCodiceFiscale(String codiceFiscale) {
        this.codiceFiscale = codiceFiscale;
    }
    
}
