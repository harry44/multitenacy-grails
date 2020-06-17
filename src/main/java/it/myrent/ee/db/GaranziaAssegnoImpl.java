/*
 * GaranziaAssegno.java
 *
 * Created on 16 februarie 2007, 18:27
 *
 */

package it.myrent.ee.db;

import java.util.Date;

/**
 *
 * @author jamess
 */
public class GaranziaAssegnoImpl extends AssegnoImpl implements GaranziaAssegno {
    
    /** Creates a new instance of GaranziaAssegno */
    public GaranziaAssegnoImpl() {
    }

    public GaranziaAssegnoImpl(MROldCoordinateBancarie coordinate) {
        super(coordinate);
    }
    
    private String numeroAutorizzazione;
    private Date dataAutorizzazione;
    private String pan;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
    @Override
    public String getNumeroAutorizzazione() {
        return numeroAutorizzazione;
    }

    @Override
    public void setNumeroAutorizzazione(String numeroAutorizzazione) {
        this.numeroAutorizzazione = numeroAutorizzazione;
    }

    @Override
    public Date getDataAutorizzazione() {
        return dataAutorizzazione;
    }

    @Override
    public void setDataAutorizzazione(Date dataAutorizzazione) {
        this.dataAutorizzazione = dataAutorizzazione;
    }
}
