package it.myrent.ee.db;

import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import it.aessepi.utils.db.PersistentInstance;
import java.util.ArrayList;
import java.util.List;

/** @author Hibernate CodeGenerator */
public class MROldPreventivoLungoPeriodo implements PersistentInstance {
    
    /** identifier field */
    private Integer id;
    
    private Integer numeroPreventivo;
    private Integer annoPreventivo;
    private Integer kmPrevisti;
    private Date dataPreventivo;
    private String note;
    private MROldParcoVeicoli veicolo;
    private MROldClienti cliente;
    private Double totaleDocumento;
    private MROldAffiliato affiliato;
    
    private List righePreventivo;
    private Integer numeroGomme;
    private Double franchigiaKasko;
    private Double franchigiaManutenzione;
    private Double cauzione;
    private Boolean importato;
    private Double numeroRate;
    private Double margineOperativo;
    private Double valoreResiduo;
    private Double totaleMensile;
    private Boolean tipoMargineOperativo;
    private Double subNoleggioMensile;
    private Double subServizi;
    
    public static final Boolean MARGINE_OPERATIVO_PERCENTUALE = Boolean.TRUE;
    public static final Boolean MARGINE_OPERATIVO_ASSOLUTO = Boolean.FALSE;
    
    /** default constructor. Ricordarsi di impostare l'affiliato.*/
    public MROldPreventivoLungoPeriodo() {
        this.importato = Boolean.FALSE;
    }
    
    public MROldPreventivoLungoPeriodo(MROldAffiliato affiliato) {
        setAffiliato(affiliato);
        this.importato = Boolean.FALSE;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setNumeroPreventivo(Integer numeroPreventivo) {
        this.numeroPreventivo = numeroPreventivo;
    }
    
    public Integer getNumeroPreventivo() {
        return numeroPreventivo;
    }
    
    public Integer getNumero() {
        return this.numeroPreventivo;
    }
    
    public void setAnnoPreventivo(Integer annoPreventivo) {
        this.annoPreventivo = annoPreventivo;
    }
    
    public Integer getAnnoPreventivo() {
        return annoPreventivo;
    }
    
    public Date getDataPreventivo() {
        return this.dataPreventivo;
    }
    
    public void setDataPreventivo(Date dataPreventivo) {
        this.dataPreventivo = dataPreventivo;
    }
    
    public String getNote() {
        return this.note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    
    public void setVeicolo(MROldParcoVeicoli veicolo) {
        this.veicolo = veicolo;
    }
    
    public MROldParcoVeicoli getVeicolo() {
        return veicolo;
    }
    
    public void setCliente(MROldClienti cliente) {
        this.cliente = cliente;
    }
    
    public MROldClienti getCliente() {
        return cliente;
    }
    
    public String toString() {
        return new ToStringBuilder(this)
        .append("id", getId())
        .toString();
    }
    
    public Double getTotaleDocumento() {
        return this.totaleDocumento;
    }
    
    public void setTotaleDocumento(Double totaleDocumento) {
        this.totaleDocumento = totaleDocumento;
    }
    
    public boolean equals(Object other) {
        if ( !(other instanceof MROldPreventivoLungoPeriodo)) return false;
        MROldPreventivoLungoPeriodo castOther = (MROldPreventivoLungoPeriodo) other;
        
        return new EqualsBuilder()
        .append(this.getId(), castOther.getId())
        .isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }
    
    public MROldAffiliato getAffiliato() {
        return affiliato;
    }
    
    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }
    
    public Integer getNumeroGomme() {
        return numeroGomme;
    }
    
    public void setNumeroGomme(Integer numeroGomme) {
        this.numeroGomme = numeroGomme;
    }
    
    public Double getFranchigiaKasko() {
        return franchigiaKasko;
    }
    
    public void setFranchigiaKasko(Double franchigiaKasko) {
        this.franchigiaKasko = franchigiaKasko;
    }
    
    public Double getFranchigiaManutenzione() {
        return franchigiaManutenzione;
    }
    
    public void setFranchigiaManutenzione(Double franchigiaManutenzione) {
        this.franchigiaManutenzione = franchigiaManutenzione;
    }
    
    public Double getCauzione() {
        return cauzione;
    }
    
    public void setCauzione(Double cauzione) {
        this.cauzione = cauzione;
    }
    
    public Double getNumeroRate() {
        return numeroRate;
    }
    
    public void setNumeroRate(Double numeroRate) {
        this.numeroRate = numeroRate;
    }
    
    public Double getMargineOperativo() {
        return margineOperativo;
    }
    
    public void setMargineOperativo(Double margineOperativo) {
        this.margineOperativo = margineOperativo;
    }
    
    public Double getValoreResiduo() {
        return valoreResiduo;
    }
    
    public void setValoreResiduo(Double valoreResiduo) {
        this.valoreResiduo = valoreResiduo;
    }
    
    public Double getTotaleMensile() {
        return totaleMensile;
    }
    
    public void setTotaleMensile(Double totaleMensile) {
        this.totaleMensile = totaleMensile;
    }
    
    public Double getSubNoleggioMensile() {
        return subNoleggioMensile;
    }
    
    public void setSubNoleggioMensile(Double subNoleggioMensile) {
        this.subNoleggioMensile = subNoleggioMensile;
    }
    
    public Double getSubServizi() {
        return subServizi;
    }
    
    public void setSubServizi(Double subServizi) {
        this.subServizi = subServizi;
    }
    
    public Integer getKmPrevisti() {
        return kmPrevisti;
    }
    
    public void setKmPrevisti(Integer kmPrevisti) {
        this.kmPrevisti = kmPrevisti;
    }
    
    /**
     * Ritorna solo le righe degli optionals.
     */
    public List getRigheOptionalsPreventivo() {
        List retValue = new ArrayList();
        if(getRighePreventivo() != null) {
            for(int i = 0; i < getRighePreventivo().size(); i++) {
                RigaPreventivoLungoPeriodo rigaTmp = (RigaPreventivoLungoPeriodo) getRighePreventivo().get(i);
                if(rigaTmp.getTipoRiga().equals(RigaPreventivoLungoPeriodo.RIGA_OPTIONAL)) {
                    retValue.add(rigaTmp);
                }
            }
        }
        return retValue;
    }
    
    public List getRighePreventivo() {
        return righePreventivo;
    }
    
    public void setRighePreventivo(List righePreventivo) {
        this.righePreventivo = righePreventivo;
    }
    
    public Boolean getImportato() {
        return importato;
    }
    
    public void setImportato(Boolean importato) {
        this.importato = importato;
    }

    public Boolean getTipoMargineOperativo() {
        return tipoMargineOperativo;
    }

    public void setTipoMargineOperativo(Boolean tipoMargineOperativo) {
        this.tipoMargineOperativo = tipoMargineOperativo;
    }
    
}
