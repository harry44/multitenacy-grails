/*
 * Danno.java
 *
 * Created on 26 octombrie 2007, 12:44
 *
 */
package it.myrent.ee.db;

import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author jamess
 */
public class MROldDanno {

    /**
     * Creates a new instance of MROldDanno
     */
    public MROldDanno() {
    }

    private Integer id;
    private MROldParcoVeicoli veicolo;
    private MROldMovimentoAuto movimento;
    private Date dataRilevamento;
    private Date dataRiparazione;
    private Boolean riparato;
    private String descrizione;
    /*campi aggiunti*/
    private MROldDamageDictionary damageDictionary;
    private MROldDamageType damageType;
    private MROldDamageSeverity damageSeverity;
    private Set damageInPicture;
    private Set movimenti;
    //@Madhvendra from app
    private Date created;
    private User createdBy;
    private Date updated;
    private User updatedBy;
    private Double appliedPrice;
    private Double cost;
    private Set<MROldParcoVeicoli> dannidip;
    /////////////
    public int hashCode() {
        if (getId() == null) {
            return super.hashCode();
        } else {
            return new HashCodeBuilder().append(getId()).toHashCode();
        }
    }

    public String toString() {
        return getDescrizione();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Set<MROldParcoVeicoli> getDannidip() {
        return dannidip;
    }

    public void setDannidip(Set<MROldParcoVeicoli> dannidip) {
        this.dannidip = dannidip;
    }

    public MROldDamageDictionary getDamageDictionary() {
        return damageDictionary;
    }

    public void setDamageDictionary(MROldDamageDictionary damageDictionary) {
        this.damageDictionary = damageDictionary;
    }

    public MROldDamageType getDamageType() {
        return damageType;
    }

    public void setDamageType(MROldDamageType damageType) {
        this.damageType = damageType;
    }

    public MROldDamageSeverity getDamageSeverity() {
        return damageSeverity;
    }

    public void setDamageSeverity(MROldDamageSeverity damageSeverity) {
        this.damageSeverity = damageSeverity;
    }

    public MROldParcoVeicoli getVeicolo() {
        return veicolo;
    }

    public void setVeicolo(MROldParcoVeicoli veicolo) {
        this.veicolo = veicolo;
    }

    public MROldMovimentoAuto getMovimento() {
        return movimento;
    }

    public void setMovimento(MROldMovimentoAuto movimento) {
        this.movimento = movimento;
    }

    public Date getDataRilevamento() {
        return dataRilevamento;
    }

    public void setDataRilevamento(Date dataRilevamento) {
        this.dataRilevamento = dataRilevamento;
    }

    public Date getDataRiparazione() {
        return dataRiparazione;
    }

    public void setDataRiparazione(Date dataRiparazione) {
        this.dataRiparazione = dataRiparazione;
    }

    public Boolean getRiparato() {
        return riparato;
    }

    public void setRiparato(Boolean riparato) {
        this.riparato = riparato;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Set getDamageInPicture() {
        return this.damageInPicture;
    }

    public void setDamageInPicture(Set damageInPicture) {
        this.damageInPicture = damageInPicture;
    }

    public Set getMovimenti() {
        return movimenti;
    }

    public void setMovimenti(Set movimenti) {
        this.movimenti = movimenti;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public User getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Double getAppliedPrice() {
        return appliedPrice;
    }

    public void setAppliedPrice(Double appliedPrice) {
        this.appliedPrice = appliedPrice;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
    
    
}
