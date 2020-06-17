package it.myrent.ee.db;


import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import it.aessepi.utils.db.PersistentInstance;

/** @author Hibernate CodeGenerator */
public class MROldCarburante implements PersistentInstance {
    
    /** identifier field */
    private Integer id;
    
    /** nullable persistent field */
    private String descrizione;
    
    private Double importoUnitario;
    
    private String unitaMisura;
    
    private MROldPianoDeiConti contoRicavo;
    
    /** full constructor */
    public MROldCarburante(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public MROldCarburante(Integer id, String descrizione) {
        this.id = id;
        this.descrizione = descrizione;
    }
    
    public MROldCarburante(Integer id, String descrizione, Double importoUnitario) {
        this.id = id;
        this.descrizione = descrizione;
        this.importoUnitario = importoUnitario;
    }
    
    
    /** default constructor */
    public MROldCarburante() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getDescrizione() {
        return this.descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public Double getImportoUnitario() {
        return importoUnitario;
    }
    
    public void setImportoUnitario(Double importoUnitario) {
        this.importoUnitario = importoUnitario;
    }
    
    public String getUniDescription() {
        return getDescrizione();
    }
    
    public String toString() {
        return descrizione!=null ? descrizione : new String();
    }
    
    public boolean equals(Object other) {
        if ( !(other instanceof MROldCarburante) ) return false;
        MROldCarburante castOther = (MROldCarburante) other;
        return new EqualsBuilder()
        .append(this.getId(), castOther.getId())
        .isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }

    public String getUnitaMisura() {
        return unitaMisura;
    }

    public void setUnitaMisura(String unitaMisura) {
        this.unitaMisura = unitaMisura;
    }

    public MROldPianoDeiConti getContoRicavo() {
        return contoRicavo;
    }

    public void setContoRicavo(MROldPianoDeiConti contoRicavo) {
        this.contoRicavo = contoRicavo;
    }
    
}
