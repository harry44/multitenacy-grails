/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.MathUtils;
import it.aessepi.utils.db.PersistentInstance;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author bogdan
 */
public class MROldPartita implements PersistentInstance {

    private Integer id;
    private Integer numero;
    private Date data;
    private MROldNumerazione numerazione;
    private MROldAffiliato affiliato;
    private MROldBusinessPartner cliente;
    private MROldContrattoNoleggio contratto;
    private MROldPrenotazione prenotazione;
    private MROldDocumentoFiscale fattura;
    private MROldPrimanota primanota;
    private MROldPartita partitaSaldo;
    private Double importo;
    private Double saldo;
    private Boolean chiusa;    
    private Set<MROldPrimanota> contropartite = new HashSet<MROldPrimanota>();
    private Set<MROldPartita> partiteAcconto = new HashSet<MROldPartita>();
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    @Override
    public String toString() {
        if (getFattura() != null) {
            return getFattura().toString();
        }
        return getPrimanota().toString();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MROldPartita)) {
            return false;
        }
        MROldPartita castOther = (MROldPartita) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public MROldNumerazione getNumerazione() {
        return numerazione;
    }

    public void setNumerazione(MROldNumerazione numerazione) {
        this.numerazione = numerazione;
    }

    public MROldAffiliato getAffiliato() {
        return affiliato;
    }

    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }

    public MROldBusinessPartner getCliente() {
        return cliente;
    }

    public void setCliente(MROldBusinessPartner cliente) {
        this.cliente = cliente;
    }

    public MROldContrattoNoleggio getContratto() {
        return contratto;
    }

    public void setContratto(MROldContrattoNoleggio contratto) {
        this.contratto = contratto;
    }

    public MROldDocumentoFiscale getFattura() {
        return fattura;
    }

    public void setFattura(MROldDocumentoFiscale fattura) {
        this.fattura = fattura;
    }

    public MROldPrimanota getPrimanota() {
        return primanota;
    }

    public void setPrimanota(MROldPrimanota primanota) {
        this.primanota = primanota;
    }

    public void setContropartite(Set<MROldPrimanota> contropartite) {
        this.contropartite = contropartite;
    }

    public Set<MROldPrimanota> getContropartite() {
        return contropartite;
    }

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }

    public Boolean getChiusa() {
        return chiusa;
    }

    public void setChiusa(Boolean chiusa) {
        this.chiusa = chiusa;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public MROldPartita getPartitaSaldo() {
        return partitaSaldo;
    }

    public void setPartitaSaldo(MROldPartita partitaSaldo) {
        this.partitaSaldo = partitaSaldo;
    }

    public Set<MROldPartita> getPartiteAcconto() {
        return partiteAcconto;
    }

    public void setPartiteAcconto(Set<MROldPartita> partiteAcconto) {
        this.partiteAcconto = partiteAcconto;
    }

    public void aggiornaSaldo() {
        setSaldo(calcolaSaldo());
    }

    public void aggiornaChiusura() {
        setChiusa(calcolaChiusura());
    }

    public Double calcolaSaldo() {
        Double saldo = 0.0;
        for (MROldPrimanota primanota : getContropartite()) {
            if(primanota.getCausale().getCodice().equals(MROldCausalePrimanota.PAGAMENTO_A_CLIENTE) || primanota.getCausale().getCodice().equals(MROldCausalePrimanota.BONIFICO_O_PAGAMENTO_CON_ASSEGNO_A_CLIENTE)) {
                saldo = MathUtils.round(saldo - primanota.getTotaleDocumento());
            } else {
                saldo = MathUtils.round(saldo + primanota.getTotaleDocumento());
            }            
        }
        return saldo;
    }

    public boolean calcolaChiusura() {
        return MathUtils.round(MathUtils.abs(getImporto() - getSaldo())) < 0.3;
    }

    public boolean isSaldoValido() {
        return MathUtils.round(getImporto() - getSaldo()) > -0.3;
    }

    public MROldPrenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(MROldPrenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }
}
