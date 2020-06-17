/*s
 * Numerazione.java
 *
 * Created on 04 iunie 2007, 18:32
 *
 */
package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 * @author jamess
 */
public class MROldNumerazione implements PersistentInstance {

    /** Creates a new instance of MROldNumerazione */
    public MROldNumerazione() {
    }

    protected MROldNumerazione(String documento) {
        setDocumento(documento);
    }
    private Integer id;
    private String tipo;
    private String codiceRegistroIva;
    private String codiceTipoDocumento;
    private String documento;
    private String prefisso;
    private String descrizione;
    private MROldRegistroIva registroIva;
    private MROldAffiliato affiliato;
    private MROldSede sede;
    private Set<MROldProgressivo> progressivi;
    /** 
     * Tipi di documento per le numerazioni
     */
    public static final String VENDITE = "Vendite"; //NOI18N
    public static final String NOTE_CREDITO_VENDITA = "Note credito vendita"; //NOI18N
    public static final String ACQUISTI = "Acquisti"; //NOI18N
    public static final String CORRISPETTIVI = "Corrispettivi"; //NOI18N
    public static final String VENDITE_MARGINE = "Vendite sist. margine"; //NOI18N
    public static final String ACQUISTI_MARGINE = "Acquisti sist. margine"; //NOI18N
    public static final String VENDITE_INTRASTAT = "Vendite da acquisti intrastat"; //NOI18N
    public static final String CONTRATTI = "Contratti"; //NOI18N
    public static final String PRENOTAZIONI = "Prenotazioni"; //NOI18N
    public static final String PREVENTIVI = "Preventivi"; //NOI18N
    public static final String PRIMENOTE = "Primenote"; //NOI18N
    public static final String CLIENTI = "Clienti"; //NOI18N
    public static final String PARTITE = "Partite"; //NOI18N
    public static final String PROFORMA = "Proforma";
    public static final String MULTE = "Multe";
    public static final String RENTTORENT = "Rent2Rent"; //NOI18N


    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
    /**
     *  Tipi documento inseriti per avere le chiavi dentro il bundle i18n
     */
    protected static final List<String> DOCUMENTI = Arrays.asList(new String[]{
        VENDITE,
        NOTE_CREDITO_VENDITA,
        ACQUISTI,
        CORRISPETTIVI,
        VENDITE_MARGINE,
        ACQUISTI_MARGINE,
        VENDITE_INTRASTAT,
        CONTRATTI,
        PRENOTAZIONI,
        PREVENTIVI,
        PRIMENOTE,
        CLIENTI,
        PARTITE
    });
    protected static final List<String> DOCUMENTI_I18N = Arrays.asList(new String[]{
        bundle.getString("Numerazione.VENDITE"),
        bundle.getString("Numerazione.NOTE_CREDITO_VENDITA"),
        bundle.getString("Numerazione.ACQUISTI"),
        bundle.getString("Numerazione.CORRISPETTIVI"),
        bundle.getString("Numerazione.VENDITE_SIST._MARGINE"),
        bundle.getString("Numerazione.ACQUISTI_SIST._MARGINE"),
        bundle.getString("Numerazione.VENDITE_DA_ACQUISTI_INTRASTAT"),
        bundle.getString("Numerazione.CONTRATTI"),
        bundle.getString("Numerazione.PRENOTAZIONI"),
        bundle.getString("Numerazione.PREVENTIVI"),
        bundle.getString("Numerazione.PRIMENOTE"),
        bundle.getString("Numerazione.MULTE"),
        bundle.getString("Numerazione.CLIENTI"),
        bundle.getString("Numerazione.PARTITE")
    });

    public String getDocumentoI18N() {
        return getDocumentoI18N(getDocumento());
    }

    public static String getDocumentoI18N(String documento) {
        return DOCUMENTI_I18N.get(DOCUMENTI.indexOf(documento));
    }

    public boolean equals(Object other) {
        if (other != null && other instanceof MROldNumerazione) {
            return new EqualsBuilder().append(getId(), ((MROldNumerazione) other).getId()).isEquals();
        }
        return false;
    }

    public static String format(String prefisso, Integer numero) {
        if (numero != null) {
            if (prefisso != null) {
                return prefisso + " " + new DecimalFormat("#0.#").format(numero); //NOI18N
            } else {
                return new DecimalFormat("#0.#").format(numero); //NOI18N
            }
        }
        return "N.A.";
    }

    public String toString() {
        if (getDocumento() != null) {
            if (getPrefisso() != null) {
                return getPrefisso() + ", " + getDocumentoI18N(); //NOI18N
            } else {
                return getDocumentoI18N();
            }
        }
        return new String();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getPrefisso() {
        return prefisso;
    }

    public void setPrefisso(String prefisso) {
        this.prefisso = prefisso;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public MROldRegistroIva getRegistroIva() {
        return registroIva;
    }

    public void setRegistroIva(MROldRegistroIva registroIva) {
        this.registroIva = registroIva;
    }

    public MROldAffiliato getAffiliato() {
        return affiliato;
    }

    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }

    public MROldSede getSede() {
        return sede;
    }

    public void setSede(MROldSede sede) {
        this.sede = sede;
    }

    public Set<MROldProgressivo> getProgressivi() {
        return progressivi;
    }

    public void setProgressivi(Set<MROldProgressivo> progressivi) {
        this.progressivi = progressivi;
    }

    public String getCodiceRegistroIva() {
        return codiceRegistroIva;
    }

    public void setCodiceRegistroIva(String codiceRegistroIva) {
        this.codiceRegistroIva = codiceRegistroIva;
    }

    public String getCodiceTipoDocumento() {
        return codiceTipoDocumento;
    }

    public void setCodiceTipoDocumento(String codiceTipoDocumento) {
        this.codiceTipoDocumento = codiceTipoDocumento;
    }
}
