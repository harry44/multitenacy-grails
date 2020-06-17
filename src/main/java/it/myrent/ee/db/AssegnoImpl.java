/*
 * GaranziaAssegno.java
 *
 * Created on 16 februarie 2007, 18:27
 *
 */
package it.myrent.ee.db;

import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author jamess
 */
public class AssegnoImpl extends GaranziaImpl implements Assegno {

    /** Creates a new instance of GaranziaAssegno */
    public AssegnoImpl() {
    }

    public AssegnoImpl(MROldCoordinateBancarie coordinate) {
        setBanca(coordinate.getBanca());
        setAbi(coordinate.getAbi());
        setCab(coordinate.getCab());
        setContoCorrente(coordinate.getConto() != null ? coordinate.getConto() : coordinate.getIban());
    }
    private String banca;
    private String cab;
    private String abi;
    private String contoCorrente;
    private Date dataEmissione;
    private String luogoEmissione;
    private Double importo;
    private String pan;

    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE)
        .append(getNumero())
        .append(getDescrizione())
        .append(getIntestatario())
        .append(getBanca())
        .toString().trim();
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
    @Override
    public String getTipoGaranzia() {
        return ASSEGNO;
    }

    public String getBanca() {
        return banca;
    }

    public void setBanca(String banca) {
        this.banca = banca;
    }

    public String getCab() {
        return cab;
    }

    public void setCab(String cab) {
        this.cab = cab;
    }

    public String getAbi() {
        return abi;
    }

    public void setAbi(String abi) {
        this.abi = abi;
    }

    public String getContoCorrente() {
        return contoCorrente;
    }

    public void setContoCorrente(String contoCorrente) {
        this.contoCorrente = contoCorrente;
    }

    public Date getDataEmissione() {
        return dataEmissione;
    }

    public void setDataEmissione(Date dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    public String getLuogoEmissione() {
        return luogoEmissione;
    }

    public void setLuogoEmissione(String luogoEmissione) {
        this.luogoEmissione = luogoEmissione;
    }

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }
}
