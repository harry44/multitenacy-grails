/*
 * RegistroIva.java
 *
 * Created on 23 mai 2006, 10:30
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;

import it.aessepi.utils.db.PersistentInstance;

/**
 *
 * @author  jamess
 */
public class MROldRegistroIva implements PersistentInstance {
    
    /** Creates a new instance of MROldRegistroIva */
    public MROldRegistroIva() {
    }    
    
    private Integer id;
    private String descrizione;
    private Boolean calcoloIva;
    private MROldPianoDeiConti contoIva;
    private Boolean segnoLiquidazioneIva;
    
    public static final Boolean CALCOLO_IVA_IMPONIBILE = Boolean.TRUE;
    public static final Boolean CALCOLO_IVA_SCORPORO = Boolean.FALSE;
    
    public static final Boolean SEGNO_LIQUIDAZIONE_DEBITO = Boolean.FALSE;
    public static final Boolean SEGNO_LIQUIDAZIONE_CREDITO = Boolean.TRUE;

        //add these new column for exporting data to web portal
    private String exportId;
    private Boolean isExported;
    private Boolean isUpdated;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getExportId() {
        return exportId;
    }

    public void setExportId(String exportId) {
        this.exportId = exportId;
    }

    public Boolean getIsExported() {
        return isExported;
    }

    public void setIsExported(Boolean isExported) {
        this.isExported = isExported;
    }

    public Boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public String toString() {
        if(getDescrizione() != null) {
            return getDescrizione();
        } else {
            return new String();
        }        
    }
    
    public boolean equals(Object other) {
        if (other != null && (other instanceof MROldRegistroIva)) {
            return new EqualsBuilder().append(this.getId(), ((MROldRegistroIva)other).getId()).isEquals();
        } else {
            return false;
        }
    }

    public Boolean getCalcoloIva() {
        return calcoloIva;
    }

    public void setCalcoloIva(Boolean calcoloIva) {
        this.calcoloIva = calcoloIva;
    }

    public MROldPianoDeiConti getContoIva() {
        return contoIva;
    }

    public void setContoIva(MROldPianoDeiConti contoIva) {
        this.contoIva = contoIva;
    }

    public Boolean getSegnoLiquidazioneIva() {
        return segnoLiquidazioneIva;
    }

    public void setSegnoLiquidazioneIva(Boolean segnoLiquidazioneIva) {
        this.segnoLiquidazioneIva = segnoLiquidazioneIva;
    }
}
