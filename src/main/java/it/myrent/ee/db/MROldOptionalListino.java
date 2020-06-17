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
public class MROldOptionalListino {

    /** Creates a new instance of ImportoOptional */
    public MROldOptionalListino() {
    }

    public MROldOptionalListino(MROldOptionalListino other) {
        if (other != null) {
            setOptional(other.getOptional());
            setListino(other.getListino());
            setGruppo(other.getGruppo());
            setIncluso(other.getIncluso());
            setImporto(other.getImporto());
            setFranchigia(other.getFranchigia());
        }
    }
    private Integer id;
    private MROldOptional optional;
    private MROldListino listino;
    private MROldGruppo gruppo;
    private Boolean incluso;
    private Double importo;
    private Double franchigia;

    public boolean equals(Object other) {
        if (other != null && other instanceof MROldOptionalListino) {
            return new EqualsBuilder().append(this.getId(), ((MROldOptionalListino) other).getId()).isEquals();
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

    public MROldListino getListino() {
        return listino;
    }

    public void setListino(MROldListino listino) {
        this.listino = listino;
    }

    public MROldGruppo getGruppo() {
        return gruppo;
    }

    public void setGruppo(MROldGruppo gruppo) {
        this.gruppo = gruppo;
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
}
