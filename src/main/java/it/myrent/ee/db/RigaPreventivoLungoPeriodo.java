package it.myrent.ee.db;

import java.util.ArrayList;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


import it.aessepi.utils.db.PersistentInstance;
import java.util.List;

/** @author Hibernate CodeGenerator */
public class RigaPreventivoLungoPeriodo implements PersistentInstance{
    
    /** identifier field */
    public static final Integer RIGA_PREIMPOSTATA = new Integer(0);
    public static final Integer RIGA_OPTIONAL = new Integer(1);
    public static final Integer RIGA_LIBERA = new Integer(2);
    public static final String RIGA_VALORE_AUTOMEZZO = new String("Valore automezzo nuovo");
    public static final String RIGA_ASSICURAZIONE = new String("Assicurazione");
    public static final String RIGA_BOLLO = new String("Bollo");
    public static final String RIGA_MANUTENZIONE = new String("Manutenzione ordinaria");
    public static final String RIGA_PNEUMATICI = new String("Pneumatici");
    public static final String[] descrizioniRighePreimpostate = new String[]{RIGA_VALORE_AUTOMEZZO, RIGA_ASSICURAZIONE, RIGA_BOLLO, RIGA_MANUTENZIONE, RIGA_PNEUMATICI};
    
    private Integer id;
    
    private MROldPreventivoLungoPeriodo preventivo;
    
    private Integer numeroRiga;
    
    private String descrizione;
    
    private Double imponibile;
    
    private Integer tipoRiga;
    
    /** full constructor */
    public RigaPreventivoLungoPeriodo(MROldPreventivoLungoPeriodo preventivo, Integer numeroRigaDocumento, String descrizione, Double imponibile, Integer tipoRiga) {
        this.preventivo = preventivo;
        this.numeroRiga = numeroRiga;
        this.descrizione = descrizione;
        this.imponibile = imponibile;
        this.tipoRiga = tipoRiga;
    }
    
    /** default constructor */
    public RigaPreventivoLungoPeriodo() {
    }
    
    public RigaPreventivoLungoPeriodo(String descrizione, Integer tipoRiga) {
        this.descrizione = descrizione;
        this.tipoRiga = tipoRiga;
    }
    
    public RigaPreventivoLungoPeriodo(Integer tipoRiga) {
        this.tipoRiga = tipoRiga;
    }
    
    
    public String toString() {
        return new ToStringBuilder(this)
        .append("id", getId())
        .toString();
    }
    
    public boolean equals(Object other) {
        if ( !(other instanceof RigaPreventivoLungoPeriodo) ) return false;
        RigaPreventivoLungoPeriodo castOther = (RigaPreventivoLungoPeriodo) other;
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
    
    public MROldPreventivoLungoPeriodo getPreventivo() {
        return preventivo;
    }
    
    public void setPreventivo(MROldPreventivoLungoPeriodo preventivo) {
        this.preventivo = preventivo;
    }
    
    public Integer getNumeroRiga() {
        return numeroRiga;
    }
    
    public void setNumeroRiga(Integer numeroRiga) {
        this.numeroRiga = numeroRiga;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public Double getImponibile() {
        return imponibile;
    }
    
    public void setImponibile(Double imponibile) {
        this.imponibile = imponibile;
    }
    
    public Integer getTipoRiga() {
        return tipoRiga;
    }
    
    public void setTipoRiga(Integer tipoRiga) {
        this.tipoRiga = tipoRiga;
    }
    
    public static List generaRighePreimpostate(){
        ArrayList retValue = new ArrayList(descrizioniRighePreimpostate.length);
        for (int i = 0; i < descrizioniRighePreimpostate.length; i++){
            retValue.add(new RigaPreventivoLungoPeriodo(descrizioniRighePreimpostate[i], RigaPreventivoLungoPeriodo.RIGA_PREIMPOSTATA));
        }
        return retValue;
    }
    
}
