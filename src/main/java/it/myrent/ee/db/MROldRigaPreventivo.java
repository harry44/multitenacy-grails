/*
 * RigaPreventivo.java
 *
 * Created on 12 octombrie 2004, 17:22
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import java.util.Date;
/**
 *
 * @author  jamess
 */
public class MROldRigaPreventivo implements MROldRigaDocumento {
    
    /** Creates a new instance of MROldRigaPreventivo */
    /** identifier field */
    private Integer id;

    private MROldPreventivo preventivo;

    private Date dataPreventivo;
    
    private Integer numeroRigaPreventivo;
    
    private String descrizione;

    private String unitaMisura;

    private Double quantita;

    private Double prezzoUnitario;

    private Double sconto;

    private Double totaleImponibileRiga;

    private Double totaleIvaRiga;

    private Double totaleRiga;
    
    private MROldCodiciIva codiceIva;
    
    private MROldPianoDeiConti codiceSottoconto;



    private MROldTariffa tariffa;
    
    public MROldRigaPreventivo(MROldPreventivo preventivo) {
        this.preventivo = preventivo;
        this.dataPreventivo = preventivo.getData();
    }
    
    public MROldRigaPreventivo(MROldRigaDocumentoFiscale tmpRiga, MROldPreventivo preventivo) {
        this.preventivo = preventivo;
        this.dataPreventivo = preventivo.getData();
        this.numeroRigaPreventivo = tmpRiga.getNumeroRigaFattura();
        this.descrizione = tmpRiga.getDescrizione();
        this.unitaMisura = tmpRiga.getUnitaMisura();
        this.quantita = tmpRiga.getQuantita();
        this.prezzoUnitario = tmpRiga.getPrezzoUnitario();
        this.sconto = tmpRiga.getSconto();
        this.totaleImponibileRiga = tmpRiga.getTotaleImponibileRiga();
        this.totaleIvaRiga = tmpRiga.getTotaleIvaRiga();
        this.totaleRiga = tmpRiga.getTotaleRiga();
        this.codiceIva = tmpRiga.getCodiceIva();
        this.codiceSottoconto = tmpRiga.getCodiceSottoconto();        
    }

    /** default constructor */
    
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldPreventivo getPreventivo() {
        return this.preventivo;
    }

    public void setPreventivo(MROldPreventivo preventivo) {
        this.preventivo = preventivo;
    }

    public Date getDataPreventivo() {
        return this.dataPreventivo;
    }

    public void setDataPreventivo(Date dataPreventivo) {
        this.dataPreventivo = dataPreventivo;
    }

    public Integer getNumeroRigaPreventivo() {
        return this.numeroRigaPreventivo;
    }

    public void setNumeroRigaPreventivo(Integer numeroRigaPreventivo) {
        this.numeroRigaPreventivo = numeroRigaPreventivo;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getUnitaMisura() {
        return this.unitaMisura;
    }

    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    public Double getQuantita() {
        return this.quantita;
    }

    public void setQuantita(Double quantita) {
        this.quantita = quantita;
    }

    public Double getPrezzoUnitario() {
        return this.prezzoUnitario;
    }

    public void setPrezzoUnitario(Double prezzoUnitario) {
        this.prezzoUnitario = prezzoUnitario;
    }

    public Double getSconto() {
        return this.sconto;
    }

    public void setSconto(Double sconto) {
        this.sconto = sconto;
    }

    public Double getTotaleImponibileRiga() {
        return this.totaleImponibileRiga;
    }

    public void setTotaleImponibileRiga(Double totaleImponibileRiga) {
        this.totaleImponibileRiga = totaleImponibileRiga;
    }

    public Double getTotaleIvaRiga() {
        return this.totaleIvaRiga;
    }

    public void setTotaleIvaRiga(Double totaleIvaRiga) {
        this.totaleIvaRiga = totaleIvaRiga;
    }

    public Double getTotaleRiga() {
        return this.totaleRiga;
    }

    public void setTotaleRiga(Double totaleRiga) {
        this.totaleRiga = totaleRiga;
    }

    
    public void setCodiceIva(MROldCodiciIva codiceIva){
        this.codiceIva = codiceIva;
    }
    
    public MROldCodiciIva getCodiceIva() {
        return codiceIva;
    }
    
    public void setCodiceSottoconto(MROldPianoDeiConti codiceSottoconto) {
        this.codiceSottoconto = codiceSottoconto;
    }
    
    public MROldPianoDeiConti getCodiceSottoconto() {
        return codiceSottoconto;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MROldRigaPreventivo) ) return false;
        MROldRigaPreventivo castOther = (MROldRigaPreventivo) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }
    
    public Object getDocumento() {
        return preventivo;
    }    
    
    public void setDocumento(Object documento) {
        preventivo = (MROldPreventivo) documento;
    }    
    
    public void setNumeroRigaDocumento(Integer numeroRiga) {
        this.numeroRigaPreventivo = numeroRiga;
    }    
    
    public Integer getNumeroRigaDocumento() {
        return numeroRigaPreventivo;
    }

    public Boolean isRigaDescrittiva() {
        boolean rigaDescrittiva =
                (getDescrizione() != null
                && getQuantita() == null
                && getPrezzoUnitario() == null
                && getSconto() == null
                && getCodiceIva() == null
                && getCodiceSottoconto() == null);
        return rigaDescrittiva;
    }
    public MROldTariffa getTariffa() {
        return tariffa;
    }

    public void setTariffa(MROldTariffa tariffa) {
        this.tariffa = tariffa;
    }
}
