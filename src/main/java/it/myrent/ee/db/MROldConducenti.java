package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.myrent.ee.interfaces.DocumentoFirmabileInterface;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import java.awt.Image;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.ImageIcon;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MROldConducenti extends MROldBusinessPartner implements it.aessepi.utils.db.PersistentInstance, MROldContribuente,Documentable,DocumentoFirmabileInterface {
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
    private MROldClienti clienteFattura;
    private String documento;
    private String numeroDocumento;
    private String rilasciatoDa;
    private Date dataRilascio;
    private Date dataScadenza;
    private String categoriaPatente;
    private byte[] foto;
    private Boolean isFotoSet;

    //MyRent Signature
    private String nomeFirmatario;
    private String luogoFirma;
    private String annotazioniFirma;
    private String nomeUtenteCreatore;
    private Date dataFirmaCerta;
    private Boolean documentoFirmato; //accerta l'avvenuta corretta archiviazione sulla gestione documentale
    private Set documenti;

    private Boolean informativaFirmata;
    private Date dataInformativa;

    public MROldConducenti() {
        super();
        setConducente(true);
        setIsFotoSet(false);
        setCompleto(false);
    }

    public MROldConducenti(Integer id) {
        super(id);
        setConducente(true);
        setIsFotoSet(false);
        setCompleto(false);
    }

    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE).append(getCognome()).append(getNome()).toString().trim();
    }

    public boolean equals(Object other) {
        if (other != null && other instanceof MROldConducenti) {
            return new EqualsBuilder().append(this.getId(), ((MROldConducenti) other).getId()).isEquals();
        }
        return false;
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
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

    public MROldClienti getClienteFattura() {
        return clienteFattura;
    }

    public void setClienteFattura(MROldClienti clienteFattura) {
        this.clienteFattura = clienteFattura;
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @Override
    public void setDocumenti(Set documenti) {
        this.documenti = documenti;
    }

    @Override
    public Set getDocumenti() {
        return documenti;
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
         return new StringBuffer().append(getNominativo()).toString();
    }

    @Override
    public Class getDocumentableClass() {
        return MROldConducenti.class;
    }

    @Override
    public String getKeywords() {
        return new StringBuffer().
                append(bundle.getString("Conducenti.keywords")).
                append(" ").
                append(getNominativo()).
                toString();
    }

    public Boolean getInformativaFirmata() {
        return informativaFirmata;
    }

    public void setInformativaFirmata(Boolean informativaFirmata) {
        this.informativaFirmata = informativaFirmata;
    }

    public Date getDataInformativa() {
        return dataInformativa;
    }

    public void setDataInformativa(Date dataInformativa) {
        this.dataInformativa = dataInformativa;
    }
}
