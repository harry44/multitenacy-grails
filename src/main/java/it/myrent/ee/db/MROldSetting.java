/*
 * Setting.java
 *
 * Created on 20 martie 2006, 14:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package it.myrent.ee.db;

/**
 *
 * @author jamess
 */
public class MROldSetting {

    /** Creates a new instance of MROldSetting */
    public MROldSetting() {
    }

    public MROldSetting(String key, String value) {
        setKey(key);
        setValue(value);
    }
    private String key;
    private String value;
    public static final String NOTIFY_EXPIRATION_DAYS = "NotifyExpirationDays";
    public static final String NOTIFY_EXPIRATION_KM = "NotifyExpirationKm";
    public static final String CONTRATTO_INTESTAZIONE_SEDE_USCITA = "contratto.intestazione_sede_uscita"; //NOI18N
    public static final String VEICOLO_AVVISO_SCADENZA_CONTRATTO = "veicolo.avviso_scadenza_contratto"; //NOI18N
    public static final String VEICOLO_GIORNI_AVVISO_SCADENZA_CONTRATTO = "veicolo.giorni_avviso_scadenza_contratto"; //NOI18N
    public static final String VEICOLO_GIORNI_SEMAFORO_SCADENZA_CONTRATTO = "veicolo.giorni_semaforo_scadenza_contratto"; //NOI18N
    public static final String OPERATORI_SOLO_TARIFFE_ABILITATE = "operatori.solo_tariffe_abilitate"; //NOI18N
    public static final String PRENOTAZIONE_SEDE_USCITA_MODIFICABILE = "prenotazione.sede_uscita_modificabile"; //NOI18N
    public static final String OPTIONALS_ABILITA_TEMPO_EXTRA = "optionals.abilita_tempo_extra"; //NOI18N
    public static final String LISTINI_IMPORTI_GIORNALIERI = "listini.importi_giornalieri"; //NOI18N
    public static final String CONTRATTO_DISABILITA_RIMBORSI = "contratto.disabilita_rimborsi"; //NOI18N
    public static final String CONTRATTO_AGGIORNA_DATA_FINE_DA_RIENTRO_EFFETTIVO = "contratto.aggiorna_fine_da_rientro_mezzo"; //NOI18N
    public static final String PREPAGATI_IMPORTI_NASCOSTI = "prepagati.importi_nascosti"; //NOI18N
    public static final String PREPAGATI_FATTURE_RIEPILOGATIVE = "prepagati.fatture_riepilogative"; //NOI18N
    public static final String CLIENTI_UNICITA_CODICE_FISCALE = "clienti.unicita_codice_fiscale"; //NOI18N
    public static final String CONTRATTO_DISABILITA_VERIFICA_SALDO_CHIUSURA = "contratto.disabilita_verifica_saldo_chiusura"; //NOI18N
    public static final String OPTIONALS_ABILITA_SCONTO = "optionals.abilita_sconto"; //NOI18N
    public static final String OPTIONALS_ABILITA_IMPORTI_NEGATIVI = "optionals.abilita_importi_negativi"; //NOI18N
    public static final String OPTIONALS_IMPORTO_MASSIMO_NEGATIVO = "optionals.importo_massimo_negativo"; //NOI18N
    public static final String CLIENTI_APPLICA_SCONTO = "clienti.applica_sconto"; //NOI18N
    public static final String CONTRATTO_MODIFICA_FONTE = "contratto.modifica_fonte"; //NOI18N
    public static final String CONTRATTO_RICEVUTA_FISCALE_UNICA = "contratto.ricevuta_fiscale_unica"; //NOI18N
    public static final String CONTRATTO_RENTAL_ASSISTANCE_PHONE_NUMBER = "contratto.rental_assistance_phone_number"; //NOI18N
    public static final String CONTRATTO_FATTURAZIONE_MENSILE = "contratto.fatturazione_mensile"; //NOI18N
    public static final String CONTRATTO_FORMA_PAGAMENTO_DEFAULT = "contratto.forma_pagamento_default"; //NOI18N
    public static final String CONTRATTO_STAMPA_FATTURA_ESTESO = "contratto.stampa_fattura_esteso"; //NOI18N
    public static final String CONTRATTO_RITARDO_MASSIMO_MINUTI = "contratto.ritardo_massimo_minuti"; //NOI18N

    public static final String FUEL_INVOICED_WITH_VAT_CODE_ZERO = "tab_3.1.1.Fuel_invoiced_with_VAT_code_at_0%";

    public static final String GENERALE_INIZIO_IMPEGNO_ORAMINUTI = "generale.inizio_impegno_oraminuti"; //NOI18N
    public static final String GENERALE_FINE_IMPEGNO_ORE = "generale.fine_impegno_ore"; //NOI18N

    public static final String NUMERAZIONE_FATTURE_PREFISSO_ANNO = "fatture.numerazione_fatture_prefisso_anno";
    public static final String PERMETTI_MODIFICA_MANUALE_FATTURE = "fatture.permetti_modifica_manuale_fatture";
    public static final String MULTE_GIORNI_CACOLO_SCADENZA_DA_NOTIFICA = "multe.giorni_calcolo_scadenza_da_notifica"; //NOI18N
    public static final String PROP_CALENDARIO_TUTTE_SEDI_OPERATORI_CAPOSTAZIONI = "calendario.tutte_sedi_operatori_capostazioni";
    public static final String DONT_ALLOW_DUPLICATED_CUSTOMER_AND_DRIVER = "Dont.allow.duplicate.driver.and.customer";

    public static final String MAIL_SMTP_HOST = "mail.smtp.host"; //NOI18N
    public static final String MAIL_SMTP_PORT = "mail.smtp.port"; //NOI18N
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth"; //NOI18N
    public static final String MAIL_SMTP_USER = "mail.smtp.user"; //NOI18N
    public static final String MAIL_SMTP_PASS = "mail.smtp.pass"; //NOI18N
    public static final String MAIL_FROM = "mail.from"; //NOI18N
    public static final String MAIL_FROM_NAME = "mail.from.name"; //NOI18N
    public static final String MAIL_SMTP_PROTOCOL = "mail.smtp.protocol"; //NOI18N
    public static final String IS_NEXI_TEST = "is_nexi_test"; //NOI18N
    public static final String PROP_OGGETTO_EMAIL_TEXT = "oggetto.email.text";  //NOI18N
    public static final String PROP_OGGETTO_EMAIL_TEXT_MODIFIABLE_USERS = "oggetto.email.modifiable.users";  //NOI18N

    public static final String PROP_MESSAGGIO_EMAIL_TEXT = "messaggio.email.text"; //NOI18N
    public static final String PROP_MESSAGGIO_EMAIL_TEXT_MODIFIABLE_USERS = "messaggio.email.modifiable.users";  //NOI18N
    
    public static final String PROP_EMAILBCC_EMAIL_TEXT = "emailbcc.email.text"; //NOI18N
    public static final String PROP_ALLEGATI_EMAIL_MODIFIABLE_USERS = "allegati.email.modifiable.users"; //NOI18N
    public static final String PROP_TO_EMAIL_MODIFIABLE_USERS = "to.email.modifiable.users"; //NOI18N
    public static final String PROP_CC_EMAIL_MODIFIABLE_USERS = "cc.email.modifiable.users"; //NOI18N

    public static final String PROP_CC_EMAIL_SEDE_INGRESSO = "cc.email.sede_ingresso"; //NOI18N
    public static final String PROP_CC_EMAIL_SEDE_USCITA = "cc.email.sede_uscita"; //NOI18N

    public static final String PERMETTI_ANAGRAFICHE_INCOMPLETE = "permetti.anagrafiche.incomplete"; //NOI18N
    public static final String PERMETTI_CONTRATTI_SENZA_CONDUCENTI = "permetti.contratti.senza.conducentI"; //NOI18N

    public static final String EMAIL_CLIENTE_OBBLIGATORIA = "email.cliente.obbligatoria"; //NOI18N

    public static final String CODICE_FISCALE_9999999999999999 = "codice.fiscale.cliente.straniero"; //NOI18N

    public static final String CAMBIO_VEICOLO_GRUPPO_SPECIALE_MOSTRA_TUTTE_SEDI = "cambio.veicolo.prenotazione.mostra.tutte.sedi";
    public static final String CAMBIO_VEICOLO_GRUPPO_SPECIALE_MOSTRA_TUTTI_GRUPPI = "cambio.veicolo.prenotazione.mostra.tutti.gruppi";

    public static final String APPLICA_NUMERO_GIORNI_MINIMO_OPTIONAL = "applivca.numero.giorni.minimo.optional";

    public static final String PERMETTI_CAMBIO_GRUPPO_SELEZIONATO_CONTRATTO = "permetti.cambio.gruppo.selezionato.contratto";
    public static final String PERMETTI_CAMBIO_GRUPPO_SELEZIONATO_PRENOTAZIONE = "permetti.cambio.gruppo.selezionato.prenotazione";

    //Madhvendra & Andrea rate numeration
    
    public static final String FRENCHISEE_NUMERATION_PREFERENCES="frenchiseeNumerationPreferences";
    
    //gestione documentale
    public static final String ABILITA_GESTIONE_DOCUMENTALE = "abilita.gestione.documentale";
    public static final String TIPO_GESTIONE_DOCUMENTALE = "tipo.gestione.documentale";

    //gestione documentale file system
    public static final String PATH_SALVATAGGIO_GESTIONE_DOCUMENTALE_FILE_SYSTEM = "path.salvataggio.gestione.documentale.fs";

    //gestione documentale ftp
    public static final String USERNAME_GESTIONE_DOCUMENTALE_FTPS = "username.gestione.documentale.ftps";
    public static final String PASSWORD_GESTIONE_DOCUMENTALE_FTPS = "password.gestione.documentale.ftps";
    public static final String URL_GESTIONE_DOCUMENTALE_FTPS = "url.gestione.documentale.ftps";
    public static final String PROTOCOLLO_GESTIONE_DOCUMENTALE_FTPS = "protocollo.gestione.documentale.ftps";

    public static final String MOSTRA_TUTTI_CONTRATTI_AFFILIATI_MAIN_WINDOWS = "mostra.tutti.contratti.affiliati.main.window";

    public static final String ABILITA_COLORI_PLANNING_TUTTI_MOVIMENTI = "abilita.colori.planning.tutti.movimenti";

    public static final String ABILITA_MULTI_CURRENCY = "abilita.multi.currency";
    public static final String PERMETTI_AGGIORNAMENTO_CURRENCY = "permetti.aggiornamento.currency";
    public static final String PERMETTI_CREAZIONE_NUOVO_CURRENCY = "permetti.creazione.nuovo.currency";
    public static final String PERMETTI_MODIFICA_CURRENCY_ESISTENTE = "permetti.modifica.currency.esistente";
    public static final String DEFAULT_CURRENCY = "default.currency";

    public static final String COLORA_RITARDO_OLTRE_LIMITE_CONSENTITO = "colora.ritardo.oltre.limite.consentito";

    public static final String FATTURAZIONE_PREPAGATI_BROKER_CONTRATTI_CHIUSI = "fatturazione.brooker.Prepagati.solo.contratti.chiusi";

    public static final String SCORPORO_ONERI_AEROPORTUALI_DALLA_TARIFFA = "scorporo.oneri.aeroportuali.dalla.tariffa";
    
    public static final String CALCOLA_IMPORTO_OPTIONAL_PERCENTUALE_SOLO_SU_TKM = "calcola.importo.optional.percentuali.solo.tkm";

    // @ Madhvendra
    public static final String RENTAL_TYPE_ENABLE_QUOTATION_RESERVATION_NEW_RENTAL = "rentaltype.enable.quotation.reservation.newrental";
    public static final String CUSTOMER_NOT_REQUIRED_QUOTATION_RESERVATION_RENTAL = "customer.not.required.quotation.reservation.rental";
    public static final String USER_CAN_CHANGE_INVOICE_NUMBER= "user.can.change.invoce.number";
    public static final String OPERATOR_CAN_CHANGE_FUEL_LEVEL= "operator.can.change.fuel.level";
    public static final String OPERATOR_CAN_CHANGE_KM= "operator.can.change.km";
    public static final String ADMIN_CAN_CHANGE_FUEL_LEVEL= "admin.can.change.fuel.level";
    public static final String ADMIN_CAN_CHANGE_KM= "admin.can.change.km";
    public static final String USE_DEPOSIT_RULES_INSIDE_RESERVATION_SOURCE="use.deposit.rules.inside.reservation.source";
    ////
    public static final String CREA_FATTURA_LIBERA = "crea.fattura.libera";

    public static final String OPTIONAL_CAUZIONALE = "optional.cauzione.con.valori.ridotti";

    public static final String MODIFICA_PARCHEGGIO_VEICOLO = "permretti.modifica.parcheggio.veicolo.utente";

    public static final String LASCIA_PRENOTAZIONE_ABBINATA_AL_VEICOLO_CANCELLAZIONE_CONTRATTO = "lascia.prenotazione.abbinata.al.veicolo.a.cancellazione.contratto";

    public static final String ISTANZA_MYGUARDIAN = "instanza.myguardian";

    public static final String CODICE_VEICOLO_NON_UNICO = "codice.veicolo.non.unico";

    public static final String DISABILITA_FATTURA_ACCONTO = "disabilita.fattura.acconto";

    public static final String MOSTRA_MENU_MULTE_OPERATORE = "mostra.menu.multe.operatore";

    public static final String ADMINISTRATOR_MODIFICA_PRENOTAZIONI_PREPAGATE = "administrator.modifica.prenotazioni.prepagate";

    public static final String RICEVUTA_FISCALE_COME_FATTURA_SALDO = "ricevuta.fiscale.come.fattura.saldo";

    public static final String PERMETTI_CREAZIONE_RICEVUTA_FISCALE = "permetti.creazione.ricevuta.fiscale";

    public static final String PERMETTI_VERSAMENTI_PARZIALI_IN_CASSA_CENTREALE = "permetti.versamenti.parziali.cassa.centrale";

    public static final String NON_MOSTRARE_PDF_QUANDO_CREA_FATTURA_BROKER = "non.mostrare.pdf.creazione.fattura.broker";

    public static final String FATTURA_TUTTA_LA_DURATA_DEL_NOLEGGIO = "fattura.tutta.la.durata.del.noleggio";

    public static final String NON_AGGIORNARE_AUTOMATICAMENTE_ORA_RIENTRO_DA_PRENOTAZIONE_A_CONTRATTO = "non.aggiornare.automaticamente.ora.rientro.da.prenorazione.a.ra";

    public static final String ENABLE_RESERVATION_SOURCES_AREA_MANAGER = "enable.reservation.sources.area.manager";
    public static final String NUMERAZIONE_UNICA_FATTURE_AFFILIATO = "fatture.numerazione_unica_fatture_affiliato"; //NOI18N
    public static final String NUMERAZIONE_NOTE_CREDITO_FATTURE_UNICA_PER_SEDE = "tab_6.1.1.Use_unique_sequence_(by_location)_for_invoices_and_credit_notes";
    /*
     * Andrea
     */
    public static final String RECALCULATE_INVOICE = "recalculate.invoice";
    public static final String SELECTED_LOCATION ="SelectedLocationTOInvoice";
    public static final String PROP_COD_IVA = "codice.iva"; //NOI18N
    /*
     * Andrea
     */
    public static final String INVOICELIST_OPERATOR = "invoicelist.operator";
    public static final String ENABLE_MONTLY_RENTAL = "rental.invCreationAftrRA.enable";
    /*
     * Andrea
     */
    public static final String OPERATOR_CANCEL_RESERVATION = "operator.cancelreservation";
    public static final String INVOICE_ENUMERATION_FROM_USER_AFFILIATE = "invoice.enumeration.user.affiliate";
    public static final String RESTRICTED_PAYMENT_TO_OPR_SM = "restricted.payment.to.oprsm";
        public static final String DETACH_FIX_RES = "detach.fix.reservation";

    // VPOS
    public static final String VPOS_ABILITATO = "virtual.pos.abilitato";
    public static final String INGENICO_ABILITATO = "permretti.ingenico.activation.utente";

    //nicolò useclosingletter
    public static final String Use_Closing_Letter = "Use.Closing.Letter";
    public static final String ALLOW_PROFORMA = "allow.proform.creation";
    public static final String PASSWORD_EXPIRATION_DAYS_ADMIN = "password.exipration.days.admin";
    public static final String PASSWORD_EXPIRATION_DAYS_USER = "password.exipration.days.user";
    public static final String MIN_CYCLES_CHANGE_PASSWORDS_USER = "min.cycles.change.passwords.user";
    public static final String MIN_CYCLES_CHANGE_PASSWORDS_ADMIN = "min.cycles.change.passwords.admin";
    public static final String PASSWORD_MIN_LENGHT_USER = "password.min.lenght.user";
    public static final String PASSWORD_MIN_LENGHT_ADMIN = "password.min.lenght.admin";
    public static final String ENABLE_USER_SECURITY = "enable.user.security";
    public static final String USER_ALLOWED_RS_PREPAID = "user.allowed.rs.prepaid"; //NOI18N
    public static final String IS_PREPAID_ALLOWED_MOV_CLOSE = "is.prepaid.allowed.mov.closed";
    public static final String INVOICE_EDIT_MODE = "is.invoice.edit.mode";
    public static final String INVOICE_RA_CLOSE = "is.invoice.ra.close";
    public static final String RA_CLOSE_EMAIL = "ra.close.email";
    public static final String ra_close_email_url = "ra.close.email.url";
    public static final String CHARGE_FUEL="charge_fuel";
    public static final String THRESHOLD="threshold";
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
