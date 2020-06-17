/*
 * RigaCausale.java
 *
 * Created on 16 mai 2006, 10:43
 *
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author jamess
 */
public class MROldRigaCausale implements PersistentInstance {
    
    /** Creates a new instance of MROldRigaCausale */
    public MROldRigaCausale() {
    }
    
    public MROldRigaCausale(MROldPianoDeiConti conto) {
        setConto(conto);
        setVeicolo(Boolean.FALSE);
        setPagamento(Boolean.FALSE);
        setDataScadenza(Boolean.FALSE);
        setKmVeicolo(Boolean.FALSE);
        setSegno(MROldCausalePrimanota.DARE);
        setAutoCarica(Boolean.FALSE);        
    }
    
    public MROldRigaCausale(RigaPrimanota rigaPrimanota) {
        setConto(rigaPrimanota.getConto());
        setPagamento(new Boolean(rigaPrimanota.getPagamento() != null));
        setVeicolo(new Boolean(rigaPrimanota.getVeicolo() != null));
        setDataScadenza(new Boolean(rigaPrimanota.getDataScadenza() != null));
        setKmVeicolo(new Boolean(rigaPrimanota.getKmVeicolo() != null));
        setSegno(rigaPrimanota.getSegno());
        setAutoCarica(Boolean.FALSE);        
    }
    
    public MROldRigaCausale(MROldRigaCausale altraRiga) {
        setConto(altraRiga.getConto());
        setPagamento(altraRiga.getPagamento());
        setDataScadenza(altraRiga.getDataScadenza());
        setVeicolo(altraRiga.getVeicolo());
        setKmVeicolo(altraRiga.getKmVeicolo());
        setSegno(altraRiga.getSegno());
        setAutoCarica(altraRiga.getAutoCarica());
    }
    
    private Integer id;
    private Integer numeroRiga;
    private MROldPianoDeiConti conto;
    private Boolean pagamento;
    private Boolean veicolo;
    private Boolean dataScadenza;
    private Boolean kmVeicolo;
    private Boolean segno;
    private Boolean autoCarica;   
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public MROldPianoDeiConti getConto() {
        return conto;
    }
    
    public void setConto(MROldPianoDeiConti conto) {
        this.conto = conto;
    }
    
    public Boolean getVeicolo() {
        return veicolo;
    }
    
    public void setVeicolo(Boolean veicolo) {
        this.veicolo = veicolo;
    }
    
    public Boolean getDataScadenza() {
        return dataScadenza;
    }
    
    public void setDataScadenza(Boolean dataScadenza) {
        this.dataScadenza = dataScadenza;
    }
    
    public Boolean getKmVeicolo() {
        return kmVeicolo;
    }
    
    public void setKmVeicolo(Boolean kmVeicolo) {
        this.kmVeicolo = kmVeicolo;
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
    
    public Boolean getAutoCarica() {
        return autoCarica;
    }
    
    public void setAutoCarica(Boolean autoCarica) {
        this.autoCarica = autoCarica;
    }
        
    public String toString() {
        if(getConto() != null) {
            return getConto().toString();
        } else {
            return new String();
        }
    }
    
    public boolean equals(Object other) {
        if(other != null && (other instanceof MROldRigaCausale)) {
            if(getId() == null && ((MROldRigaCausale)other).getId() == null) {
                return new EqualsBuilder().append(toString(), other.toString()).append(getSegno(), ((MROldRigaCausale)other).getSegno()).isEquals();
            } else {
                return new EqualsBuilder().append(getId(), ((MROldRigaCausale)other).getId()).isEquals();
            }
        } else {
            return false;
        }
    }

    public Boolean getPagamento() {
        return pagamento;
    }

    public void setPagamento(Boolean pagamento) {
        this.pagamento = pagamento;
    }
}
