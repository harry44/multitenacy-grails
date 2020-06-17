/*
 * RigaPrimanota.java
 *
 * Created on 16 mai 2006, 10:38
 *
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author jamess
 */
public class RigaImposta implements PersistentInstance {
    
    /** Creates a new instance of RigaPrimanota */
    public RigaImposta() {
    }

    public RigaImposta(Integer id, Integer numeroRiga, MROldPrimanota primanota, MROldPianoDeiConti conto, MROldCodiciIva codiceIva, Double imponibile, String descrizione, Double importo, Boolean segno, Boolean correzioneIva) {
        this.id = id;
        this.numeroRiga = numeroRiga;
        this.primanota = primanota;
        this.conto = conto;
        this.codiceIva = codiceIva;
        this.imponibile = imponibile;
        this.descrizione = descrizione;
        this.importo = importo;
        this.segno = segno;
        this.correzioneIva = correzioneIva;
    }

 
    private Integer id;
    private Integer numeroRiga;
    private MROldPrimanota primanota;
    private MROldPianoDeiConti conto;
    private MROldCodiciIva codiceIva;
    private Double imponibile;
    private String descrizione;
    private Double importo;
    private Boolean segno;
    private Boolean correzioneIva;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldPrimanota getPrimanota() {
        return primanota;
    }

    public void setPrimanota(MROldPrimanota primanota) {
        this.primanota = primanota;
    }

    public MROldPianoDeiConti getConto() {
        return conto;
    }

    public void setConto(MROldPianoDeiConti conto) {
        this.conto = conto;
    }

    public MROldCodiciIva getCodiceIva() {
        return codiceIva;
    }

    public void setCodiceIva(MROldCodiciIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    public Double getImponibile() {
        return imponibile;
    }

    public void setImponibile(Double imponibile) {
        this.imponibile = imponibile;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }

    public Boolean getSegno() {
        return segno;
    }

    public void setSegno(Boolean segno) {
        this.segno = segno;
    }

    public Integer getNumeroRiga() {
        return numeroRiga;
    }

    public void setNumeroRiga(Integer numeroRiga) {
        this.numeroRiga = numeroRiga;
    }
    
    public boolean equals(Object other) {
        if(other != null && (other instanceof RigaPrimanota)) {
            return new EqualsBuilder().append(getId(), ((RigaPrimanota)other).getId()).isEquals();
        } else {
            return false;
        }
    }

    public Boolean getCorrezioneIva() {
        return correzioneIva;
    }

    public void setCorrezioneIva(Boolean correzioneIva) {
        this.correzioneIva = correzioneIva;
    }
}
