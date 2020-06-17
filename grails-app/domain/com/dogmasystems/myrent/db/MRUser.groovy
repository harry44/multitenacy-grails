package com.dogmasystems.myrent.db

import com.dogmasystems.Tenant
import com.dogmasystems.old.db.User
import com.dogmasystems.touroperatorportal.Sconti
import com.dogmasystems.touroperatorportal.Settings
import com.dogmasystems.utils.db.User
import grails.util.Holders
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.apache.commons.lang.builder.StandardToStringStyle
import org.apache.commons.lang.builder.ToStringBuilder

//class MRUser implements com.dogmasystems.old.db.User{
class MRUser implements Serializable{

	String tipo
    String userName;
    String password;
    String nameSurname;
    String annotations;
    String userLang;
    //private Map grants;
    //List managedLocations;
    Double maxDiscount;
    //private Set extraDiscounts;
    // Set<MRReservationSource> reservationSources;
    //private Set<MRSession> sessions;
    String email;
    Boolean isDisabled
    Date deactivationDate;
    Date activationDate;
	MRAffiliate affiliate;
    Boolean customerDiscount
    Boolean isWebUser
    Boolean defaultUser
    //private Boolean defaultUser;
    //private Boolean showAllMenu;
    Integer proKey
    Integer locAccCount
    Boolean showAllMenu
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

    static belongsTo = ['affiliate':MRAffiliate, 'location':MRLocation, 'tenant':com.dogmasystems.Tenant]

    static hasMany = [//commissionis: MRCommission,
                      discount : Sconti,
                      fontiusers: MRSourceUser,
                      reservationId : MRReservation
    ]

    static mappedBy = [reservationId : "dswpUser"]

    static mapping = {
        cache true
       // reservationId cache: true
        location cache: true
        sort("id")
        table name: "users"//, schema: "public"
        id generator:'sequence', params:[sequence:'users_seq']
        id column: "id", sqlType: "int4"
        deactivationDate column:"data_disattivazione", sqlType: "date" , insertable: false, updateable: false
        affiliate column:"id_affiliato", sqlType: "int4"
        tenant column: "id_tenant", sqlType: "int4"
        location column:"id_sede", sqlType: "int4"
        userName column:"user_name"
        password column:"password"
        email column:"email"
        customerDiscount column: "customer_discount"
        maxDiscount column:"sconto_massimo", insertable: false, updateable: false
        isDisabled column:"disattivo"
        nameSurname column:"nome_cognome" , insertable: false, updateable: false
        annotations column:"annotazioni"
        isWebUser column : "is_web_user"
        defaultUser column: "default_user"
        proKey column: "prokey"
        locAccCount column: "loc_acc_count"
        showAllMenu column: "show_all_menu"
        activationDate column: "activation_date"
        version false
    }

    static constraints = {
        userName nullable: true
        password nullable: true
        nameSurname nullable: true
        	annotations nullable: true
        maxDiscount nullable: true, scale: 17
        isDisabled nullable: true
        deactivationDate nullable: true
        activationDate nullable: true
        customerDiscount nullable: true
        defaultUser nullable: true
        proKey nullable: true
        locAccCount nullable: true
        email nullable: true
        location nullable: true
        isWebUser nullable: true
        defaultUser nullable: true
        showAllMenu nullable: true
        affiliate nullable: true
        reservationId nullable: true
        tenant nullable: true
        userLang nullable: true
    }


//    public String toString() {
//        return new ToStringBuilder(this, TO_STRING_STYLE).append(getId()).append(getNameSurname()).toString().trim();
//    }

    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE).append(getNameSurname()).toString().trim();
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

    Set<com.dogmasystems.Role> getAuthorities() {
        Set role = []
        if(this.tipo.equals("Administrator")  || this.tipo.equals("ResponsabileNazionale")){
            role.add(com.dogmasystems.Role.findByAuthority('ROLE_ADMIN'))
        } else if (this.tipo.equals("CapoStazione")){
            role.add(com.dogmasystems.Role.findByAuthority('ROLE_LOCATION_OPERATOR'))
        } else if (this.tipo.equals("CapoArea")){
            role.add(com.dogmasystems.Role.findByAuthority('ROLE_LOCATION_OPERATOR'))
        } else if (this.tipo.equals("Operator")){
            role.add(com.dogmasystems.Role.findByAuthority('ROLE_LOCATION_OPERATOR'))
        }

        return role
    }

    static MRUser create(String userName, String password, Tenant tenant, String tipo, com.dogmasystems.myrent.db.MRAffiliate affiliate, boolean flush = false) {

        def instance = new MRUser(userName: userName, password: password, tenant: tenant, tipo: tipo, affiliate: affiliate, isWebUser: true)

        instance.save(flush: flush, insert: true)
        MRPassword mrPassword = new MRPassword()
        mrPassword.setPassword(password)
        mrPassword.setUltima(true)
        mrPassword.setUtente(instance)
        mrPassword.setDataInserimento(new Date())
        def userService = Holders.applicationContext.getBean("userService")
        userService.setPermission(instance)
        String dayExp = Settings.get("password.exipration.days.admin")?.getValue()
        if(dayExp != "" && dayExp!=null && dayExp!="null"){
            mrPassword.setGiorniValidita(new Integer(dayExp));
        } else {
            mrPassword.setGiorniValidita(0)
        }
        mrPassword.save(flush: flush, insert: true)
        instance
    }
}

