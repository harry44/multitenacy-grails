package it.myrent.ee.db;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


/** @author Hibernate CodeGenerator */

public class UserImpl implements User {
    
    private Integer id;

    private Integer locAccCount;
    private String userName;
    
    private String password;

    private String userLang;
    
    private String nomeCognome;
    
    private MROldSede sedeOperativa;
    
    private MROldAffiliato affiliato;

    private MROldTenant tenant;

    public MROldTenant getTenant() {
        return tenant;
    }

    public void setTenant(MROldTenant tenant) {
        this.tenant = tenant;
    }
    
    private String annotazioni;
    
    private Map permessi;
    
    private List sediGestite;
    
    private Double scontoMassimo;
    
    private Set scontiStraordinari;
    
    private Set<MROldFonteCommissione> fonti;

    private Set<Sessione> sessioni;

    private String email;

    private Boolean disattivo;
    private Date dataDisattivazione;
    private Date activationDate;
    private Boolean defaultUser;
    private Boolean showAllMenu;
    private Boolean customerDiscount;

    private Boolean isWebUser;
    private MROldPassword lastPassword;
    
    public static final StandardToStringStyle TO_STRING_STYLE = new StandardToStringStyle();
    
    static {
        TO_STRING_STYLE.setContentEnd(""); //NOI18N
        TO_STRING_STYLE.setContentStart(""); //NOI18N
        TO_STRING_STYLE.setNullText(""); //NOI18N
        TO_STRING_STYLE.setUseIdentityHashCode(false);
        TO_STRING_STYLE.setUseClassName(false);
        TO_STRING_STYLE.setUseFieldNames(false);
        TO_STRING_STYLE.setFieldSeparator(" "); //NOI18N
    }

    public Boolean getCustomerDiscount() {
        return customerDiscount;
    }

    public void setCustomerDiscount(Boolean customerDiscount) {
        this.customerDiscount = customerDiscount;
    }

    //Madhvendra
    private Set<MROldPagamento> payment;

    public Set<MROldPagamento> getPayment() {
        return payment;
    }

    public void setPayment(Set<MROldPagamento> payment) {
        this.payment = payment;
    }
    public boolean isOperatore() {
        return OPERATORE.equals(getTipo());
    }
    
    public boolean isCapoStazione() {
        return CAPO_STAZIONE.equals(getTipo());
    }
    
    public boolean isCapoArea() {
        return CAPO_AREA.equals(getTipo());
    }
    
    public boolean isResponsabileNazionale() {
        return RESPONSABILE_NAZIONALE.equals(getTipo());
    }
    
    public boolean isAdministrator() {
        return ADMINISTRATOR.equals(getTipo());
    }
    
    public static User nuovoUtente(String tipo) {
        if(tipo.equals(ADMINISTRATOR)) {
            return new AdministratorImpl();
        } else if(tipo.equals(RESPONSABILE_NAZIONALE)) {
            return new ResponsabileNazionaleImpl();
        } else if(tipo.equals(CAPO_AREA)) {
            return new MROldCapoAreaImpl();
        } else if(tipo.equals(CAPO_STAZIONE)) {
            return new CapoStazioneImpl();
        } else if(tipo.equals(OPERATORE)) {
            return new OperatoreImpl();
        } else {
            return null;
        }
    }
    
    public UserImpl() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setNomeCognome(String nomeCognome) {
        this.nomeCognome = nomeCognome;
    }
    
    public String getNomeCognome() {
        return nomeCognome;
    }
    
    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }
    
    public MROldAffiliato getAffiliato() {
        return affiliato;
    }
    
    public void setAnnotazioni(String annotazioni) {
        this.annotazioni = annotazioni;
    }
    
    public String getAnnotazioni() {
        return annotazioni;
    }
    
    public String getTipo() {
        return UNKNOWN;
    }

    public String getTipoInternazionalizzato() {
        return UNKNOWN_I18N;
    }
    
    
    
    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE).append(getNomeCognome()).toString().trim();
    }
    
    public boolean equals(Object other) {
        if(other != null && other instanceof User) {
            return new EqualsBuilder().append(getId(), ((User)other).getId()).isEquals();
        }
        return false;
    }
    
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .append(getUserName())
        .append(getPassword())
        .toHashCode();
    }
    
    public Map getPermessi() {
        return permessi;
    }
    
    public void setPermessi(Map permessi) {
        this.permessi = permessi;
    }
    
    public MROldSede getSedeOperativa() {
        return sedeOperativa;
    }
    
    public void setSedeOperativa(MROldSede sedeOperativa) {
        this.sedeOperativa = sedeOperativa;
    }
    
    public List getSediGestite() {
        return sediGestite;
    }
    
    public void setSediGestite(List sediGestite) {
        this.sediGestite = sediGestite;
    }

    public Double getScontoMassimo() {
        return scontoMassimo;
    }

    public void setScontoMassimo(Double scontoMassimo) {
        this.scontoMassimo = scontoMassimo;
    }

    public Set getScontiStraordinari() {
        return scontiStraordinari;
    }

    public void setScontiStraordinari(Set scontiStraordinari) {
        this.scontiStraordinari = scontiStraordinari;
    }

    public Set<MROldFonteCommissione> getFonti() {
        return fonti;
    }

    public void setFonti(Set<MROldFonteCommissione> fonti) {
        this.fonti = fonti;
    }

    /**
     * @return the disattivo
     */
    public Boolean getDisattivo() {
        return disattivo;
    }

    /**
     * @param disattivo the disattivo to set
     */
    public void setDisattivo(Boolean disattivo) {
        this.disattivo = disattivo;
    }

    /**
     * @return the dataDisattivazione
     */
    public Date getDataDisattivazione() {
        return dataDisattivazione;
    }

    /**
     * @param dataDisattivazione the dataDisattivazione to set
     */
    public void setDataDisattivazione(Date dataDisattivazione) {
        this.dataDisattivazione = dataDisattivazione;
    }

    /**
     * @return the defaultUser
     */
    public Boolean getDefaultUser() {
        return defaultUser;
    }

    /**
     * @param defaultUser the defaultUser to set
     */
    public void setDefaultUser(Boolean defaultUser) {
        this.defaultUser = defaultUser;
    }

    /* il comportamento nativo e' che se il flag e' null allora l'utente non e' disattivo */
    public boolean isDisattivo() {
        if (disattivo == null) {
            return false;
        } else {
            return disattivo.booleanValue();
        }
    }

    /* il comportamento nativo e' che se il flag e' null allora l'utente non e' default */
    public boolean isDefaultUser() {
        if (defaultUser == null) {
            return false;
        } else {
            return defaultUser.booleanValue();
        }
    }

    public Set<Sessione> getSessioni() {
        return sessioni;
    }

    public void setSessioni(Set<Sessione> sessioni) {
        this.sessioni = sessioni;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Boolean getShowAllMenu() {
        return showAllMenu;
    }

    @Override
    public void setShowAllMenu(Boolean value) {
        showAllMenu = value;
    }

    public Boolean getIsWebUser() {
        return isWebUser;
    }

    public void setIsWebUser(Boolean isWebUser) {
        this.isWebUser = isWebUser;
    }

    @Override
    public MROldPassword getLastPassword() {
        return lastPassword;
    }

    @Override
    public void setLastPassword(MROldPassword lastPassword) {
        this.lastPassword = lastPassword;
    }

    public Integer getLocAccCount() {
        return locAccCount;
    }

    public void setLocAccCount(Integer locAccCount) {
        this.locAccCount = locAccCount;
    }

    public String getUserLang() {
        return userLang;
    }

    public void setUserLang(String userLang) {
        this.userLang = userLang;
    }


    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }


}
