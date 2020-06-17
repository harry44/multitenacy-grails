package it.myrent.ee.db;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

/** @author Hibernate CodeGenerator */
public class MROldMultaEnte implements it.aessepi.utils.db.PersistentInstance, Comparable<MROldMultaEnte> {

    private Integer id;
    private String organo;
    private String ufficio;    
    private String comune;
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
    
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(getOrgano()).append(getComune()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldMultaEnte)) {
            return false;
        }
        MROldMultaEnte castOther = (MROldMultaEnte) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    @Override
    public int compareTo(MROldMultaEnte o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getOrgano(), o.getOrgano()).
                    append(getUfficio(), o.getUfficio()).
                    append(getComune(), o.getComune()).
                    toComparison();
        }
    }

    public Integer getId() {
        return this.id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String getOrgano() {
        return organo;
    }

    public void setOrgano(String organo) {
        this.organo = organo;
    }

    public String getUfficio() {
        return ufficio;
    }

    public void setUfficio(String ufficio) {
        this.ufficio = ufficio;
    }

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getCellulare() {
        return cellulare;
    }

    public void setCellulare(String cellulare) {
        this.cellulare = cellulare;
    }

    public String getNazione() {
        return nazione;
    }

    public void setNazione(String nazione) {
        this.nazione = nazione;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPromemoria() {
        return promemoria;
    }

    public void setPromemoria(String promemoria) {
        this.promemoria = promemoria;
    }

    public static final ObjectToStringConverter TO_STRING_CONVERTER = new ObjectToStringConverter() {

        @Override
        public String getPreferredStringForItem(Object item) {
            String preferredString = new String();
            String[] possibleStrings = getPossibleStringsForItem(item);
            if (possibleStrings != null && possibleStrings.length > 0) {
                if (possibleStrings[0] != null) {
                    preferredString = possibleStrings[0];
                }
                preferredString += " ( "; //NOI18N
                for (int i = 1; i < possibleStrings.length; i++) {
                    if (possibleStrings[i] != null) {
                        preferredString += possibleStrings[i];
                    }
                    if (i < possibleStrings.length - 1) {
                        preferredString += " / "; //NOI18N
                    }
                }
                preferredString += " )"; //NOI18N
            }
            return preferredString.trim();
        }

        @Override
        public String[] getPossibleStringsForItem(Object item) {
            if (item == null || !(item instanceof MROldMultaEnte)) {
                return null;
            } else {
                MROldMultaEnte multaEnte = (MROldMultaEnte) item;
                return new String[]{
                    multaEnte.getOrgano(),
                    multaEnte.getComune(),
                    multaEnte.getCitta()
                };
            }
        }
    };

}
