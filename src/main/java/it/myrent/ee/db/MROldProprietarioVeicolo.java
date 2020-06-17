package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/** @author Hibernate CodeGenerator */
public class MROldProprietarioVeicolo implements it.aessepi.utils.db.PersistentInstance {

    /** identifier field */
    private Integer id;
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

    //private Set sedi;
    /** default constructor */
    public MROldProprietarioVeicolo() {
    }

    public MROldProprietarioVeicolo(Integer id, String ragioneSociale) {
        this.id = id;
        this.ragioneSociale = ragioneSociale;
    }

    public Integer getId() {
        return this.id;
    }

    private void setId(Integer id) {
        this.id = id;
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

    public String toString() {
        return ragioneSociale != null ? ragioneSociale : new String();
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldProprietarioVeicolo)) {
            return false;
        }
        MROldProprietarioVeicolo castOther = (MROldProprietarioVeicolo) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }
}
