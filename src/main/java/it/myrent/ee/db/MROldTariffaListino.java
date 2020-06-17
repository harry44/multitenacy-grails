/*
 * TariffaListino.java
 *
 * Created on 22 decembrie 2006, 13:13
 *
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Date;

/**
 *
 * @author jamess
 */
public class MROldTariffaListino implements PersistentInstance {
    
    private Integer id;
    private MROldDurata durata;
    private MROldGruppo gruppo;
    private Double importoBase;
    private Double importoExtra;
    //Madhvendra (for Postgres)
    private MROldSede location;
    private MROldListino listini;
    private Date date;
    /** Creates a new instance of MROldTariffaListino */
    public MROldTariffaListino() {
    }
    public MROldSede getLocation() {
        return location;
    }
    public MROldListino getListini() {
        return listini;
    }

    public void setListini(MROldListino listini) {
        this.listini = listini;
    }
    public void setLocation(MROldSede location) {
        this.location = location;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public String toString() {        
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(getGruppo()).append(getDurata()).toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldDurata getDurata() {
        return durata;
    }

    public void setDurata(MROldDurata durata) {
        this.durata = durata;
    }

    public MROldGruppo getGruppo() {
        return gruppo;
    }

    public void setGruppo(MROldGruppo gruppo) {
        this.gruppo = gruppo;
    }

    public Double getImportoBase() {
        return importoBase;
    }

    public void setImportoBase(Double importoBase) {
        this.importoBase = importoBase;
    }

    public Double getImportoExtra() {
        return importoExtra;
    }

    public void setImportoExtra(Double importoExtra) {
        this.importoExtra = importoExtra;
    }

}
