package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import java.util.Date;


/** @author Hibernate CodeGenerator */
public class MROldScadenze implements it.aessepi.utils.db.PersistentInstance, Comparable {
    
    private Integer id;
    
    private Integer tipoCliente;
    
    private MROldBusinessPartner cliente;
    
    private Integer noFattura;
    
    private MROldDocumentoFiscale fattura;
    
    private Date dataFattura;
    
    private Double importoFattura;
    
    private MROldPagamento pagamento;
    
    private Date dataScadenza;
    
    private Double importoScadenza;
    
    private Boolean saldo = Boolean.FALSE;
    
    private Boolean distinta = Boolean.FALSE;
    
    private MROldFornitori fornitore;
    
    private MROldCoordinateBancarie coordinateBancarie;
    
    private Date dataPresentazione;
    
    private Boolean tipoScadenza;
    
    private String annotazioni;
    
    private Distinta distintaBanca;
    
    private Integer tipoRiga; // RIGA_UNICA, PRIMA_RIGA, ALTRA_RIGA.
    
    public MROldScadenze() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getDataFattura() {
        return this.dataFattura;
    }
    
    public void setDataFattura(Date dataFattura) {
        this.dataFattura = dataFattura;
    }
    
    public Date getDataPresentazione() {
        return dataPresentazione;
    }
    
    public void setDataPresentazione(Date dataPresentazione) {
        this.dataPresentazione = dataPresentazione;
    }
    
    public Date getDataScadenza() {
        return this.dataScadenza;
    }
    
    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }
    
    public Boolean getDistinta() {
        return distinta;
    }
    
    public void setDistinta(Boolean distinta) {
        this.distinta = distinta;
    }
    
    public MROldFornitori getFornitore() {
        return fornitore;
    }
    
    public void setFornitore(MROldFornitori fornitore) {
        this.fornitore = fornitore;
    }
    
    public MROldBusinessPartner getCliente() {
        return cliente;
    }
    
    public void setCliente(MROldBusinessPartner cliente) {
        this.cliente = cliente;
    }
    
    public MROldDocumentoFiscale getFattura(){
        return fattura;
    }
    
    public void setFattura(MROldDocumentoFiscale fattura){
        this.fattura = fattura;
    }
    
    public MROldPagamento getPagamento() {
        return pagamento;
    }
    
    public void setPagamento(MROldPagamento pagamento) {
        this.pagamento = pagamento;
    }
    
    public Double getImportoFattura() {
        return importoFattura;
    }
    
    public void setImportoFattura(Double importoFattura) {
        this.importoFattura = importoFattura;
    }
    
    public Double getImportoScadenza(){
        return importoScadenza;
    }
    
    public void setImportoScadenza(Double importoScadenza) {
        this.importoScadenza = importoScadenza;
    }
    
    public Integer getNoFattura() {
        return noFattura;
    }
    
    public void setNoFattura(Integer noFattura) {
        this.noFattura = noFattura;
    }
    
    public Boolean getSaldo(){
        return saldo;
    }
    
    public void setSaldo(Boolean saldo){
        this.saldo = saldo;
    }
    
    public Integer getTipoCliente(){
        return tipoCliente;
    }
    
    public void setTipoCliente(Integer tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    
    public Boolean getTipoScadenza() {
        return tipoScadenza;
    }
    
    public void setTipoScadenza(Boolean tipoScadenza) {
        this.tipoScadenza = tipoScadenza;
    }
    
    public void setAnnotazioni(String annotazioni) {
        this.annotazioni = annotazioni;
    }
    
    public String getAnnotazioni(){
        return annotazioni;
    }
    
    public boolean equals(Object other) {
        if ( !(other instanceof MROldScadenze) ) return false;
        MROldScadenze castOther = (MROldScadenze) other;
        return new EqualsBuilder()
        .append(this.getId(), castOther.getId())
        .isEquals();
    }
    
    public int compareTo(Object o) {
        int retValue = -1;
        if(o != null && o instanceof MROldScadenze) {
            MROldScadenze other = (MROldScadenze)o;
            if(getDataScadenza() != null && other.getDataScadenza() != null && !getDataScadenza().equals(other.getDataScadenza())) {
                long diff = getDataScadenza().getTime() - other.getDataScadenza().getTime();
                if(diff < 0) {
                    retValue = -1;
                } else if (diff > 0) {
                    retValue = 1;
                }
            } else if(getSaldo() != null && !getSaldo().equals(other.getSaldo())) {
                if(getSaldo().equals(Boolean.FALSE)) {
                    retValue = -1;
                } else {
                    retValue = 1;
                }
            }
        }
        return retValue;
    }
    

    public static final Integer CLIENTI = new Integer(1);
    public static final Integer FORNITORI = new Integer(2);
    public static final Integer ALTRO = new Integer(3);
    
    public static final Boolean SALDO = Boolean.TRUE;
    public static final Boolean ACCONTO = Boolean.FALSE;
    
    public static final Integer RIGA_UNICA = new Integer(0);
    public static final Integer PRIMA_RIGA = new Integer(1);
    public static final Integer ALTRA_RIGA = new Integer(2);

    public Distinta getDistintaBanca() {
        return distintaBanca;
    }

    public void setDistintaBanca(Distinta distintaBanca) {
        this.distintaBanca = distintaBanca;
    }

    public Integer getTipoRiga() {
        return tipoRiga;
    }

    public void setTipoRiga(Integer tipoRiga) {
        this.tipoRiga = tipoRiga;
    }

    public MROldCoordinateBancarie getCoordinateBancarie() {
        return coordinateBancarie;
    }

    public void setCoordinateBancarie(MROldCoordinateBancarie coordinateBancarie) {
        this.coordinateBancarie = coordinateBancarie;
    }
    
}
