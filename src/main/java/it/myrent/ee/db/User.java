/*
 * User.java
 *
 * Created on 30 iulie 2007, 18:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 *
 * @author jamess
 */
public interface User extends it.aessepi.utils.db.User {
    public void setUserName(String userName);
    public String getUserName();
    public void setPassword(String password);
    public String getPassword();
    public void setLastPassword(MROldPassword password);
    public MROldPassword getLastPassword();
    public void setNomeCognome(String nomeCognome);
    public String getNomeCognome();
    public void setAffiliato(MROldAffiliato affiliato);
    public MROldAffiliato getAffiliato();
    public void setTenant(MROldTenant tenant);
    public MROldTenant getTenant();
    public void setAnnotazioni(String annotazioni);
    public String getAnnotazioni();
    public String getTipo();
    public String getTipoInternazionalizzato();
    public Map getPermessi();
    public void setPermessi(Map permessi);
    public MROldSede getSedeOperativa();
    public void setSedeOperativa(MROldSede sedeOperativa);
    public List getSediGestite();
    public void setSediGestite(List sediGestite);
    public Double getScontoMassimo();
    public void setScontoMassimo(Double scontoMassimo);
    public Set getScontiStraordinari();
    public void setScontiStraordinari(Set scontiStraordinari);
    public boolean isOperatore();
    public boolean isCapoStazione();
    public boolean isCapoArea();
    public boolean isResponsabileNazionale();
    public boolean isAdministrator();
    public Set<MROldFonteCommissione> getFonti();
    public void setFonti(Set<MROldFonteCommissione> fonti);
    public String getUserLang();
    public void setUserLang(String userLang);
    public Boolean getDisattivo();
    public void setDisattivo(Boolean disattivo);
    public Date getDataDisattivazione();
    public void setDataDisattivazione(Date dataDisattivazione);
    public Date getActivationDate();
    public void setActivationDate(Date activationDate);
    public Boolean getDefaultUser();
    public void setDefaultUser(Boolean defaultUser);

    public boolean isDisattivo();
    public boolean isDefaultUser();

    public String getEmail();
    public void setEmail(String aEmail);

    public Boolean getShowAllMenu();
    public void setShowAllMenu(Boolean value);
    public Boolean getCustomerDiscount();
    public void setCustomerDiscount(Boolean customerDiscount);
    public Boolean getIsWebUser();
    public void setIsWebUser(Boolean isWebUser);

    public Set<MROldPagamento> getPayment();

    public void setPayment(Set<MROldPagamento> payment);
    public static final String UNKNOWN = "Sconosciuto"; //NOI18N
    public static final String ADMINISTRATOR = "Administrator"; //NOI18N
    public static final String RESPONSABILE_NAZIONALE = "ResponsabileNazionale"; //NOI18N
    public static final String CAPO_AREA = "MROldCapoArea"; //NOI18N
    public static final String CAPO_STAZIONE = "CapoStazione"; //NOI18N
    public static final String OPERATORE = "Operatore"; //NOI18N
    public static final String PROFILO_UTENTE = "ProfiloUtente"; //NOI18N
    public static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle"); //NOI18N
    
    public static final String UNKNOWN_I18N = bundle.getString("User.UNKNOWN_I18N");
    public static final String ADMINISTRATOR_I18N = bundle.getString("User.ADMINISTRATOR_I18N");
    public static final String RESPONSABILE_NAZIONALE_I18N = bundle.getString("User.RESPONSABILE_NAZIONALE_I18N");
    public static final String CAPO_AREA_I18N = bundle.getString("User.CAPO_AREA_I18N");
    public static final String CAPO_STAZIONE_I18N = bundle.getString("User.CAPO_STAZIONE_I18N");
    public static final String OPERATORE_I18N = bundle.getString("User.OPERATORE_I18N");
    public static final String PROFILO_UTENTE_I18N = bundle.getString("User.PROFILO_UTENTE_I18N");
}
