package com.dogmasystems.myrent.db

import com.dogmasystems.touroperatorportal.Pagamenti
import com.dogmasystems.touroperatorportal.Primenote

class MRHeaderInvoice implements Serializable {

    String documentType
    Boolean isPrepaid
    String prefix
    Integer invoiceNumb
    Date invoiceDate
    Date invoiceStartDate
    Date invoiceEndDate
    Integer year
    Double totTaxable
    Double totVat
    Double totInvoice
    Boolean isRecorded
    Boolean isSummary
    String annotations
    Double totRows
    Double totDeposit
    MRAffiliate affiliate
    MRUser users
    Primenote primenote
    MRBusinessPartner client         //Class changed to MRBusinessPartner as MRClient is deleted
    MRVehicle vehicle
    MREnumeration enumerations
    Pagamenti pagamenti
    MRRentalAgreement rentalAgreement
    Integer anno
    Boolean contabilizzato
// Added field
    Integer proKey
    Boolean riepilogativo
    String annotazioni
    String nomeFirmarario
    String luogoFirma
    String annotazioniFirma
    String nomeUtenteCreatore
    Date dataFirmaCerta
    Boolean documentoFirmato

    MRReservation reservation
    String fatturaXml
    String reasonXMLCreationFailed
    String recievedDateSDI
    String sdiStatus
    String codiceCIG;
    String codiceCIGPO;
    String numContrattoLibero;
    Date dataContrattoLibero;
    String numPurchaseOrder
    Date datePurchaseOrder;
    String rifAmministrativo;
    String ordineAcquistoMeccanica;
    String targaMeccanica;
    String targaApprontamento
    String telaioApprontamento
    String contrattoApprontamento
    RentToRent rent2rent

    static hasMany = [fatturaImpostas: MRTaxInvoice,
                      fatturaRigas   : MRRowInvoice,
                      //  partites: Partite,
                      //  rateContrattis: RateContratti,
                      //  scadenzes: Scadenze]
    ]
//	static belongsTo = [Affiliati, Clienti, ContrattoNoleggio, Numerazioni, Pagamenti, ParcoVeicoli, Primenote, Users]
    static belongsTo = [MRAffiliate, MRBusinessPartner, MRRentalAgreement, MREnumeration, MRVehicle, MRUser, MRReservation, Primenote]
    //Class changed to MRBusinessPartner as MRClient is deleted

    static mapping = {
        cache true
        table name: "fattura_intestazione"//, schema: "public"
        id generator: 'sequence', params: [sequence: 'fattura_intestazione_seq']
        id column: "id", sqlType: "int4"
        discriminator column: "tipo_documento"
        version false
        invoiceDate column: "data_fattura", sqlType: "date"
        invoiceStartDate column: "inizio_fatturazione", sqlType: "date"
        invoiceEndDate column: "fine_fatturazione", sqlType: "date"
        affiliate column: "id_affiliato", sqlType: "int4"
        users column: "id_user", sqlType: "int4" //, insertable: false, updateable: false
        client column: "id_cliente", sqlType: "int4"
        vehicle column: "id_veicolo", sqlType: "int4"
        enumerations column: "id_numerazione", sqlType: "int4"
        rentalAgreement column: "id_contratto_noleggio", sqlType: "int4"
        documentType column: "tipo_documento", sqlType: "int4"
        isPrepaid column: "prepagato"
        prefix column: "prefisso"
        invoiceNumb column: "numero_fattura"
        year column: "year"
        totTaxable column: "totale_imponibile"
        totVat column: "totale_iva"
        totInvoice column: "totale_fattura"
        isRecorded column: "isRecorded"
        isSummary column: "isSummary"
        annotations column: "annotations"
        totRows column: "totale_righe"
        totDeposit column: "totale_acconti"
        reservation column: "id_prenotazione", sqlType: "int4"
        pagamenti column: "id_pagamento", sqlType: "int4"
        anno column: "anno"
        primenote column: "id_primanota", sqlType: "int4"
        proKey column: "prokey"
        primenote column: "id_primanota", sqlType: "int4"
        riepilogativo column: "riepilogativo"
        annotazioni column: "annotazioni"
        nomeFirmarario column: "nome_firmarario"
        luogoFirma column: "luogo_firma"
        annotazioniFirma column: "annotazioni_firma"
        nomeUtenteCreatore column: "nome_utente_creatore"
        dataFirmaCerta column: "data_firma_certa", sqlType: "date"
        documentoFirmato column: "documento_firmato"
        fatturaXml column: "fattura_xml"
        reasonXMLCreationFailed column: "motivo_fatturazioneXML_fallita"
        recievedDateSDI column: "data_ricezione_sdi"
        sdiStatus column: "stato_invio"
        codiceCIG column: "codice_cig"
        codiceCIGPO column: "codice_cig_po"
        numContrattoLibero column: "num_contratto_libero"
        dataContrattoLibero column: "data_contratto_libero"
        numPurchaseOrder column: "num_purchase_order"
        datePurchaseOrder column: "data_purchase_order"
        rifAmministrativo column: "rif_amministrativo"
        ordineAcquistoMeccanica column: "ordine_acquisto_meccanica"
        targaMeccanica column: "targa_meccanica"
        targaApprontamento column: "targa_approntamento"
        telaioApprontamento column: "telaio_approntamento"
        contrattoApprontamento column: "contratto_approntamento"
        rent2rent column :"id_rent_to_rent", sqlType: "int4"
        version false
    }

    static constraints = {
        contrattoApprontamento nullable: true
        telaioApprontamento nullable: true
        targaMeccanica nullable: true
        targaApprontamento nullable: true
        isPrepaid nullable: true
        prefix nullable: true
        invoiceNumb nullable: true
        invoiceDate nullable: true
        invoiceStartDate nullable: true
        invoiceEndDate nullable: true
        year nullable: true
        totTaxable nullable: true, scale: 17
        totVat nullable: true, scale: 17
        totInvoice nullable: true, scale: 17
        isRecorded nullable: true
        isSummary nullable: true
        annotations nullable: true
        totRows nullable: true, scale: 17
        totDeposit nullable: true, scale: 17
        anno nullable: true
        contabilizzato nullable: true
        pagamenti nullable: true
        proKey nullable: true
        primenote nullable: true
        riepilogativo nullable: true
        annotazioni nullable: true
        nomeFirmarario nullable: true
        luogoFirma nullable: true
        annotazioniFirma nullable: true
        nomeUtenteCreatore nullable: true
        dataFirmaCerta nullable: true
        enumerations nullable: true
        reservation nullable: true
        users nullable: true
        vehicle nullable: true
        rentalAgreement nullable: true
        client nullable: true
        fatturaXml nullable: true
        reasonXMLCreationFailed nullable: true
        recievedDateSDI nullable: true
        sdiStatus nullable: true
        codiceCIG nullable: true
        codiceCIGPO nullable: true
        numContrattoLibero nullable: true
        dataContrattoLibero nullable: true
        numPurchaseOrder nullable: true
        datePurchaseOrder nullable: true
        rifAmministrativo nullable: true
        ordineAcquistoMeccanica nullable: true
        rent2rent nullable :true
    }
}
