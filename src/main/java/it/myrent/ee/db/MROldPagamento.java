package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Set;


/** @author Hibernate CodeGenerator */
public class MROldPagamento implements it.aessepi.utils.db.PersistentInstance {

    private Integer id;
    
    private Boolean contanti;
    private Boolean cartaCredito;
    private Boolean assegno;
    private Boolean scadenziario;
    private Boolean differito;
    private Boolean prepagato;

    private MROldMezzoPagamento mezzoPagamento;

    private String descrizione;

    private Integer numeroRate;

    private Integer intervallo;

    private Integer giorniPrimaScadenza;
    
    private Integer meseEsclusione1;
    
    private Integer giornoFisso1;
    
    private Integer meseEsclusione2;
    
    private Integer giornoFisso2;
    
    private Boolean dataFattura;
    
    private Boolean fineMese;
    
    private Boolean primaRataNormale;
    
    private Boolean primaRataIva;
    
    private Boolean primaRataIvaSpese;
    
    private MROldPianoDeiConti contoIncasso;
    
    private Integer giorniDopo;
    
    private String codiceEsportazione;
    //Madhvendra (for Postgres)
    //Madhvendra
    private Set<User> users;

    public String getModalitaPagamento() {
        return modalitaPagamento;
    }

    public void setModalitaPagamento(String modalitaPagamento) {
        this.modalitaPagamento = modalitaPagamento;
    }

    private String modalitaPagamento;

    public String getCondizioniPagamento() {
        return condizioniPagamento;
    }

    public void setCondizioniPagamento(String condizioniPagamento) {
        this.condizioniPagamento = condizioniPagamento;
    }

    private String condizioniPagamento;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    /** default constructor */
    public MROldPagamento() {
    }
    
    public MROldPagamento(Integer id, String descrizione) {
        this.id = id;
        this.descrizione = descrizione;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public MROldMezzoPagamento getMezzoPagamento() {
        return mezzoPagamento;
    }
    
    public void setMezzoPagamento(MROldMezzoPagamento mezzoPagamento) {
        this.mezzoPagamento = mezzoPagamento;
    }
    
    public String getDescrizione() {
        return this.descrizione;
    }
    
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
    public Integer getNumeroRate() {
        return this.numeroRate;
    }
    
    public void setNumeroRate(Integer numeroRate) {
        this.numeroRate = numeroRate;
    }
    
    public Integer getIntervallo() {
        return this.intervallo;
    }
    
    public void setIntervallo(Integer intervallo) {
        this.intervallo = intervallo;
    }
    
    public Integer getGiorniPrimaScadenza() {
        return this.giorniPrimaScadenza;
    }
    
    public void setGiorniPrimaScadenza(Integer giorniPrimaScadenza) {
        this.giorniPrimaScadenza = giorniPrimaScadenza;
    }
    
    public void setMeseEsclusione1(Integer meseEsclusione1) {
        this.meseEsclusione1 = meseEsclusione1;
    }
    
    public Integer getMeseEsclusione1() {
        return meseEsclusione1;
    }
    
    public void setGiornoFisso1(Integer giornoFisso1) {
        this.giornoFisso1 = giornoFisso1;
    }
    
    public Integer getGiornoFisso1() {
        return giornoFisso1;
    }
    
    public void setMeseEsclusione2(Integer meseEsclusione2) {
        this.meseEsclusione2 = meseEsclusione2;
    }
    
    public Integer getMeseEsclusione2() {
        return meseEsclusione2;
    }
    
    public void setGiornoFisso2(Integer giornoFisso2) {
        this.giornoFisso2 = giornoFisso2;
    }
    
    public Integer getGiornoFisso2() {
        return giornoFisso2;
    }
    
    public String toString() {
        return getDescrizione() + ", " + getMezzoPagamento(); //NOI18N
    }
    
    public boolean equals(Object other) {
        if ( !(other instanceof MROldPagamento) ) return false;
        MROldPagamento castOther = (MROldPagamento) other;
        return new EqualsBuilder()
        .append(this.getId(), castOther.getId())
        .isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }

    public MROldPianoDeiConti getContoIncasso() {
        return contoIncasso;
    }

    public void setContoIncasso(MROldPianoDeiConti contoIncasso) {
        this.contoIncasso = contoIncasso;
    }

    public Integer getGiorniDopo() {
        return giorniDopo;
    }

    public void setGiorniDopo(Integer giorniDopo) {
        this.giorniDopo = giorniDopo;
    }

    public Boolean getPrimaRataNormale() {
        return primaRataNormale;
    }

    public void setPrimaRataNormale(Boolean primaRataNormale) {
        this.primaRataNormale = primaRataNormale;
    }

    public Boolean getPrimaRataIva() {
        return primaRataIva;
    }

    public void setPrimaRataIva(Boolean primaRataIva) {
        this.primaRataIva = primaRataIva;
    }

    public Boolean getPrimaRataIvaSpese() {
        return primaRataIvaSpese;
    }

    public void setPrimaRataIvaSpese(Boolean primaRataIvaSpese) {
        this.primaRataIvaSpese = primaRataIvaSpese;
    }

    public Boolean getDataFattura() {
        return dataFattura;
    }

    public void setDataFattura(Boolean dataFattura) {
        this.dataFattura = dataFattura;
    }

    public Boolean getFineMese() {
        return fineMese;
    }

    public void setFineMese(Boolean fineMese) {
        this.fineMese = fineMese;
    }

    public Boolean getContanti() {
        return contanti;
    }

    public void setContanti(Boolean contanti) {
        this.contanti = contanti;
    }

    public Boolean getCartaCredito() {
        return cartaCredito;
    }

    public void setCartaCredito(Boolean cartaCredito) {
        this.cartaCredito = cartaCredito;
    }

    public Boolean getScadenziario() {
        return scadenziario;
    }

    public void setScadenziario(Boolean scadenziario) {
        this.scadenziario = scadenziario;
    }

    public Boolean getDifferito() {
        return differito;
    }

    public void setDifferito(Boolean differito) {
        this.differito = differito;
    }

    public Boolean getPrepagato() {
        return prepagato;
    }

    public void setPrepagato(Boolean prepagato) {
        this.prepagato = prepagato;
    }

    public void setCodiceEsportazione(String codiceEsportazione) {
        this.codiceEsportazione = codiceEsportazione;
    }

    public String getCodiceEsportazione() {
        return codiceEsportazione;
    }

    public String getToString() {
        return toString();
    }

    public Boolean getAssegno() {
        if (assegno == null) {
            return false;
        } else {
            return assegno;
        }
    }

    public void setAssegno(Boolean assegno) {
        this.assegno = assegno;
    }
}
