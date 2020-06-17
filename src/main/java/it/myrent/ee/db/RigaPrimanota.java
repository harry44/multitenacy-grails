/*
 * RigaPrimanota.java
 *
 * Created on 16 mai 2006, 10:38
 *
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author jamess
 */
public class RigaPrimanota implements PersistentInstance {
    
    /** Creates a new instance of RigaPrimanota */
    public RigaPrimanota() {
    }

    public RigaPrimanota(Integer id, Integer numeroRiga, MROldPrimanota primanota, MROldPianoDeiConti conto, MROldClienti cliente, MROldFornitori fornitore, MROldParcoVeicoli veicolo, Integer kmVeicolo, MROldPagamento pagamento, Date dataScadenza, String descrizione, Double importo, MROldCodiciIva codiceIva, Boolean segno) {
        this.id = id;
        this.numeroRiga = numeroRiga;
        this.primanota = primanota;
        this.conto = conto;
        this.cliente = cliente;
        this.fornitore = fornitore;
        this.veicolo = veicolo;
        this.kmVeicolo = kmVeicolo;
        this.pagamento = pagamento;
        this.dataScadenza = dataScadenza;
        this.descrizione = descrizione;
        this.importo = importo;
        this.codiceIva = codiceIva;
        this.segno = segno;
    }

    public RigaPrimanota(MROldPrimanota primanota, MROldPianoDeiConti conto, MROldClienti cliente, MROldFornitori fornitore, Double importo, Boolean segno, Integer numeroRiga) {
        this.primanota = primanota;
        this.conto = conto;
        this.cliente = cliente;
        this.fornitore = fornitore;
        this.importo = importo;
        this.segno = segno;
        this.numeroRiga = numeroRiga;        
    }
 
    private Integer id;
    private Integer numeroRiga;
    private MROldPrimanota primanota;
    private MROldPianoDeiConti conto;
    private MROldBusinessPartner cliente;
    private MROldFornitori fornitore;
    private MROldParcoVeicoli veicolo;
    private Integer kmVeicolo;
    private MROldPagamento pagamento;
    private Garanzia garanzia;
    private Date dataScadenza;    
    private String descrizione;
    private Double importo;
    private MROldCodiciIva codiceIva;
    private Boolean segno;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        if(getId() != null) {
            return new HashCodeBuilder().append(getId()).toHashCode();                    
        } else {
            return super.hashCode();
        }
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

    public MROldBusinessPartner getCliente() {
        return cliente;
    }

    public void setCliente(MROldBusinessPartner cliente) {
        this.cliente = cliente;
    }

    public MROldFornitori getFornitore() {
        return fornitore;
    }

    public void setFornitore(MROldFornitori fornitore) {
        this.fornitore = fornitore;
    }

    public MROldParcoVeicoli getVeicolo() {
        return veicolo;
    }

    public void setVeicolo(MROldParcoVeicoli veicolo) {
        this.veicolo = veicolo;
    }

    public Integer getKmVeicolo() {
        return kmVeicolo;
    }

    public void setKmVeicolo(Integer kmVeicolo) {
        this.kmVeicolo = kmVeicolo;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
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

    public MROldPagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(MROldPagamento pagamento) {
        this.pagamento = pagamento;
    }

    public MROldCodiciIva getCodiceIva() {
        return codiceIva;
    }

    public void setCodiceIva(MROldCodiciIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    public Garanzia getGaranzia() {
        return garanzia;
    }

    public void setGaranzia(Garanzia garanzia) {
        this.garanzia = garanzia;
    }
}
