/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author jamess
 */
public class MROldCoordinateBancarie implements PersistentInstance {

    private Integer id;
    private String banca;
    private String abi;
    private String cab;
    private String conto;
    private String cin;
    private String iban;
    public static final StandardToStringStyle TO_STRING_STYLE = new StandardToStringStyle();

    public MROldCoordinateBancarie() {
    }

    public MROldCoordinateBancarie(MROldCoordinateBancarie other) {
        setBanca(other.getBanca());
        setAbi(other.getAbi());
        setCab(other.getCab());
        setConto(other.getConto());
        setCin(other.getCin());
        setIban(other.getIban());
    }

    public MROldCoordinateBancarie(GaranziaAssegno assegno) {
        setBanca(assegno.getBanca());
        setAbi(assegno.getAbi());
        setCab(assegno.getCab());
        setConto(assegno.getContoCorrente());
    }

    static {
        TO_STRING_STYLE.setUseClassName(false);
        TO_STRING_STYLE.setUseFieldNames(false);
        TO_STRING_STYLE.setUseIdentityHashCode(false);
        TO_STRING_STYLE.setFieldSeparator(", "); //NOI18N
        TO_STRING_STYLE.setNullText("<>"); //NOI18N
        TO_STRING_STYLE.setContentStart(""); //NOI18N
        TO_STRING_STYLE.setContentEnd(""); //NOI18N
    }

    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE).append(getBanca()).append(getConto() != null ? getConto() : getIban()).toString().trim();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAbi() {
        return abi;
    }

    public void setAbi(String abi) {
        this.abi = abi;
    }

    public String getCab() {
        return cab;
    }

    public void setCab(String cab) {
        this.cab = cab;
    }

    public String getConto() {
        return conto;
    }

    public void setConto(String conto) {
        this.conto = conto;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBanca() {
        return banca;
    }

    public void setBanca(String banca) {
        this.banca = banca;
    }
}
