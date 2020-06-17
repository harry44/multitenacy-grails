/*
 * Sconto.java
 *
 * Created on 08 iunie 2007, 05:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import java.util.Date;

/**
 *
 * @author jamess
 */
public class MROldSconto {
    
    /** Creates a new instance of MROldSconto */
    public MROldSconto() {
    }
    
    private Integer id;
    private Double percentuale;
    private Integer durataMinima;

    private Integer durataMax;
    private Date inizioSconto;
    private Date fineSconto;
    private Date inizioRiferimento;
    private Date fineRiferimento;

    private MROldFonteCommissione fonteCommissione;

    private MROldGruppo gruppo;
    private MROldSede sede;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPercentuale() {
        return percentuale;
    }

    public void setPercentuale(Double percentuale) {
        this.percentuale = percentuale;
    }

    public Date getInizioSconto() {
        return inizioSconto;
    }

    public void setInizioSconto(Date inizioSconto) {
        this.inizioSconto = inizioSconto;
    }

    public Date getFineSconto() {
        return fineSconto;
    }

    public void setFineSconto(Date fineSconto) {
        this.fineSconto = fineSconto;
    }

    public Date getInizioRiferimento() {
        return inizioRiferimento;
    }

    public void setInizioRiferimento(Date inizioRiferimento) {
        this.inizioRiferimento = inizioRiferimento;
    }

    public Date getFineRiferimento() {
        return fineRiferimento;
    }

    public void setFineRiferimento(Date fineRiferimento) {
        this.fineRiferimento = fineRiferimento;
    }

    public Integer getDurataMinima() {
        return durataMinima;
    }

    public void setDurataMinima(Integer durataMinima) {
        this.durataMinima = durataMinima;
    }

    public MROldSede getSede() {
        return sede;
    }

    public void setSede(MROldSede sede) {
        this.sede = sede;
    }

    public MROldGruppo getGruppo() {
        return gruppo;
    }

    public void setGruppo(MROldGruppo gruppo) {
        this.gruppo = gruppo;
    }

    public MROldFonteCommissione getFonteCommissione() {
        return fonteCommissione;
    }

    public void setFonteCommissione(MROldFonteCommissione fonteCommissione) {
        this.fonteCommissione = fonteCommissione;
    }

    public Integer getDurataMax() {
        return durataMax;
    }

    public void setDurataMax(Integer durataMax) {
        this.durataMax = durataMax;
    }
}
