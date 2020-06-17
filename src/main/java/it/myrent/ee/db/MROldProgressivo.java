/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author jamess
 */
public class MROldProgressivo implements Serializable,it.aessepi.utils.db.PersistentInstance{
    private MROldNumerazione numerazione;
    private Integer anno;
    private Integer progressivo;

    public MROldProgressivo() {
        
    }
    
    public MROldProgressivo(MROldNumerazione numerazione, Integer anno) {
        setNumerazione(numerazione);
        setAnno(anno);
        setProgressivo(new Integer(0));
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(getNumerazione()).
                append(getAnno()).
                toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if(other != null && other instanceof MROldProgressivo) {
            return new EqualsBuilder().
                    append(getNumerazione(), ((MROldProgressivo)other).getNumerazione()).
                    append(getAnno(), ((MROldProgressivo)other).getAnno()).
                    isEquals();
        }
        return false;
    }

    @Override
    public String toString() {
        String data=new String();
        if(getNumerazione() !=null){
            if(getNumerazione().getPrefisso()!=null){
            data=getNumerazione().getPrefisso();
            }else{
            data="";
              }
            if(getNumerazione().getDocumento() !=null){
            data+=getNumerazione().getDocumento();
            }else{
            data+="";
            }
            if(getAnno()>0){
            data+=getAnno().toString();
            }else{
            data+="";
            }
        }
        return data;
        }

    public MROldNumerazione getNumerazione() {
        return numerazione;
    }

    public void setNumerazione(MROldNumerazione numerazione) {
        this.numerazione = numerazione;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Integer getProgressivo() {
        return progressivo;
    }

    public void setProgressivo(Integer progressivo) {
        this.progressivo = progressivo;
    }

    @Override
    public Integer getId() {
        return numerazione.getId();
    }
}
