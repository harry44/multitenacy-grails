/*
 * GaranziaCarta.java
 *
 * Created on 16 februarie 2007, 18:36
 *
 */

package it.myrent.ee.db;

import it.aessepi.utils.beans.FormattedDate;
import java.text.DecimalFormat;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author jamess
 */
public class GaranziaCartaImpl extends CartaDiCreditoImpl implements GaranziaCarta {
    
    /** Creates a new instance of GaranziaCarta */
    public GaranziaCartaImpl() {
    }
    
    public GaranziaCartaImpl(CartaDiCredito other) {
        super(other);
        setDataAutorizzazione(FormattedDate.formattedDate());
    }
    
    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE)
        .append(getImporto() != null ? new DecimalFormat("\u00a4 #,##0.00").format(getImporto()) : null)
        .append(getDescrizione())
        .append(getNumeroOscurato())
        .toString().trim();
    }
    
    private Date dataAutorizzazione;
    private String numeroAutorizzazione;
    private Double importo;

    public Date getDataAutorizzazione() {
        return dataAutorizzazione;
    }

    public void setDataAutorizzazione(Date dataAutorizzazione) {
        this.dataAutorizzazione = dataAutorizzazione;
    }

    public String getNumeroAutorizzazione() {
        return numeroAutorizzazione;
    }

    public void setNumeroAutorizzazione(String numeroAutorizzazione) {
        this.numeroAutorizzazione = numeroAutorizzazione;
    }

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }
}
