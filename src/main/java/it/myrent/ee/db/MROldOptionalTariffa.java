/*
 * ImportoOptional.java
 *
 * Created on 03 ianuarie 2007, 16:46
 *
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author jamess
 */
public class MROldOptionalTariffa {
    
    /** Creates a new instance of ImportoOptional */
    public MROldOptionalTariffa() {
    }
    
    public MROldOptionalTariffa(MROldOptionalTariffa other) {
        setFranchigia(other.getFranchigia());
        setImporto(other.getImporto());
        setQuantita(other.getQuantita());
        setIncluso(other.getIncluso());        
        setOptional(other.getOptional());
        setSelezionato(other.getSelezionato());
        setPrepagato(other.getPrepagato());
        setSelezionatoRientro(other.getSelezionatoRientro());
        setSelezionatoFranchigia(other.getSelezionatoFranchigia());        
    }
    
    private Integer id;
    private MROldOptional optional;
    private Boolean incluso;
    private Boolean selezionato;
    private Boolean prepagato;
    private Boolean selezionatoRientro;
    private Boolean selezionatoFranchigia;    
    private Double importo;
    private Double franchigia;
    private Double quantita;

    public boolean equals(Object other) {
        if(other != null && other instanceof MROldOptionalTariffa) {
            return new EqualsBuilder().append(this.getId(), ((MROldOptionalTariffa)other).getId()).isEquals();
        } else {
            return false;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldOptional getOptional() {
        return optional;
    }

    public void setOptional(MROldOptional optional) {
        this.optional = optional;
    }

    public Boolean getIncluso() {
        return incluso;
    }

    public void setIncluso(Boolean incluso) {
        this.incluso = incluso;
    }

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }

    public Double getFranchigia() {
        return franchigia;
    }

    public void setFranchigia(Double franchigia) {
        this.franchigia = franchigia;
    }

    public Boolean getSelezionato() {
        return selezionato;
    }

    public void setSelezionato(Boolean selezionato) {
        this.selezionato = selezionato;
    }

    public Boolean getSelezionatoRientro() {
        return selezionatoRientro;
    }

    public void setSelezionatoRientro(Boolean selezionatoRientro) {
        this.selezionatoRientro = selezionatoRientro;
    }

    public Boolean getSelezionatoFranchigia() {
        return selezionatoFranchigia;
    }

    public void setSelezionatoFranchigia(Boolean selezionatoFranchigia) {
        this.selezionatoFranchigia = selezionatoFranchigia;
    }

    public Boolean getPrepagato() {
        return prepagato;
    }

    public void setPrepagato(Boolean prepagato) {
        this.prepagato = prepagato;
    }

    public Double getQuantita() {
        return quantita;
    }

    public void setQuantita(Double quantita) {
        this.quantita = quantita;
    }
}
