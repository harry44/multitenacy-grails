/*
 * Garanzia.java
 *
 * Created on 16 februarie 2007, 18:22
 *
 */

package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author jamess
 */
public class GaranziaImpl implements Garanzia {
    /** Creates a new instance of Garanzia */
    public GaranziaImpl() {
    }
    private Integer id;
    private String descrizione;
    private String intestatario;
    private String numero;
    private Double importo;
    private String pan;
    private String aliaspan;

    public String getAliaspan() {
        return aliaspan;
    }

    public void setAliaspan(String aliaspan) {
        this.aliaspan = aliaspan;
    }
    //    public static final String GARANZIA_CARTA_DI_CREDITO = "Carta di credito"; //NOI18N
//    public static final String GARANZIA_ASSEGNO = "Assegno"; //NOI18N

    
    
//    public static final String GARANZIA_CARTA_I18N = "Carta di credito";
//    public static final String GARANZIA_ASSEGNO_I18N = "Assegno";

//    public static final String CARTA_DI_CREDITO = "Carta di credito";
//    public static final String ASSEGNO = "Assegno";

    public static final StandardToStringStyle TO_STRING_STYLE = new StandardToStringStyle();
    
    static {
        TO_STRING_STYLE.setUseClassName(false);
        TO_STRING_STYLE.setUseFieldNames(false);
        TO_STRING_STYLE.setUseIdentityHashCode(false);
        TO_STRING_STYLE.setFieldSeparator(", "); //NOI18N
        TO_STRING_STYLE.setNullText("<>"); //NOI18N
        TO_STRING_STYLE.setContentStart(""); //NOI18N
        TO_STRING_STYLE.setContentEnd(""); //NOI18N
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }
    @Override
    public String getTipoGaranzia() {
        return null;
    }

    @Override
    public boolean isGaranziaAssegno() {
        return ASSEGNO.equals(getTipoGaranzia());
    }

    @Override
    public boolean isGaranziaCarta() {
        return CARTA_DI_CREDITO.equals(getTipoGaranzia());
    }
    
    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE)
        .append(getNumero())
        .append(getIntestatario())
        .append(getDescrizione()).toString().trim();
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public boolean equals(Object other) {
        if(other != null && other instanceof Garanzia) {
            return new EqualsBuilder().append(getId(), ((Garanzia) other).getId()).isEquals();
        } else {
            return false;
        }
    }
    
    public String getDescrizione() {
        return descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public String getIntestatario() {
        return intestatario;
    }
    
    public void setIntestatario(String intestatario) {
        this.intestatario = intestatario;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Double getImporto() {
        return importo;
}

    public void setImporto(Double importo) {
        this.importo = importo;
    }

}
