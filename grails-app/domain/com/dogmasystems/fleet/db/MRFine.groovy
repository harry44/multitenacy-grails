package com.dogmasystems.fleet.db

import com.dogmasystems.myrent.db.MRVehicleMovement
import com.dogmasystems.touroperatorportal.Noleggiatori
import grails.util.Holders

import java.sql.Time

class MRFine implements  Serializable{
   // static auditable = true

    MRVehicleMovement vehicleMovement
    MRFineAuthority fineOrg
    String driver
    Date minutesNotificationDate
    Integer protocolNumber
    Date munutesEmissionDate
    String minutesNumber
    String issuerAuthorityName
    String issuerAuthorityCity
    String issuerAuthorityAddress
    String breachedArticle
    Date infractionDate
    Date infractionTime
    String infractionDescription
    Double operatingExpencesAmount
    Double fineAmount
    String contactName
    String contactPhone
    String documentIssueCity
    Date documentIssueDate
    String signatureRow1
    String signatureRow2
    String annotation
    Date expirationDate
    Noleggiatori rentalId
    String registeredMail
    String assessorOfficer
    Boolean isToBill
    Boolean isShipped
    Boolean isReadyToBeShipped
    Boolean isFineProcessed
    String fineLink
    String nomeEnteEmettente
    String articoloViolato
 //   String descrizioneInfrazione
   // Boolean isReadyToSend;
   // Boolean isSent;
    Date uploadDate


    static constraints = {
       // isSent nullable: true
       // isReadyToSend nullable: true
        vehicleMovement nullable:true
        fineOrg nullable:true
        driver nullable: true
        minutesNotificationDate nullable:true
        protocolNumber nullable:true
        munutesEmissionDate nullable:true
        minutesNumber nullable:true
        issuerAuthorityName nullable: true
        issuerAuthorityCity nullable: true
        issuerAuthorityAddress nullable: true
        breachedArticle nullable: true
        infractionDate nullable: true
        infractionTime nullable: true
        infractionDescription nullable: true
        operatingExpencesAmount nullable: true
        fineAmount nullable: true
        contactName nullable: true
        contactPhone nullable: true
        documentIssueCity nullable: true
        documentIssueDate nullable: true
        signatureRow1 nullable: true
        signatureRow2 nullable: true
        annotation nullable: true
        expirationDate nullable: true
        rentalId nullable: true
        registeredMail nullable: true
        assessorOfficer nullable: true
        isToBill nullable: true
        isShipped nullable: true
        isReadyToBeShipped nullable : true
        isFineProcessed nullable : true
        nomeEnteEmettente nullable: true
        articoloViolato nullable: true
        fineLink nullable: true
        uploadDate nullable: true
       // descrizioneInfrazione nullable: true

    }
    static mapping = {
        cache true
        table name:"multe"//, schema: "public"
        id generator:'sequence', params:[sequence:'multe_seq']
        id column: "id", sqlType: 'int4'
        vehicleMovement column: "id_movimento", sqlType: 'int4'
        fineOrg column: "id_multa_ente", sqlType: 'int4'
       // isSent column: "is_shipped"
       // isReadyToSend column: "is_ready_to_be_shipped"
        isFineProcessed column: "is_fine_processed"
        annotation column: "annotazioni"
        infractionDescription column: "descrizione_infrazione"
        articoloViolato column: "articolo_violato"
        nomeEnteEmettente column: "nome_ente_emettente"
        driver column: "guidatore"
        minutesNotificationDate column: "data_notifica_verbale", sqlType: 'TIMESTAMP'
        protocolNumber  column: "numeroprotocollo"//,sqlType: 'int4'
        munutesEmissionDate column: "data_emissione_verbale", sqlType: 'TIMESTAMP'
        minutesNumber column: "numero_verbale"
        //issuerAuthorityName column: "nome_ente_emettente"//, sqlType: 'text'
        issuerAuthorityCity column: "citta_ente_emettente"
        issuerAuthorityAddress column: "indirizzo_ente_emettente"
       // breachedArticle column: "articolo_violato"//,sqlType: 'text'
        infractionDate column: "data_infrazione", sqlType: 'TIMESTAMP'
//        if(Holders.grailsApplication.config.com.dogmasystems.postgres==true) {
//            infractionTime column: "ora_infrazione", sqlType: 'TIMESTAMP'
//
//        }else{
//            infractionTime column: "ora_infrazione"//, sqlType: 'TIMESTAMP'
//        }
        infractionTime column: "ora_infrazione"//, sqlType:  'TIMESTAMP'
        //infractionDescription column: "descrizione_infrazione"//,sqlType: 'text'
        operatingExpencesAmount column: "importo_spese_gestione"
        fineAmount column: "importo_multa"
        contactName column: "nome_contatto"
        contactPhone column: "telefono_contatto"
        documentIssueCity column: "citta_emissione_documento"
        documentIssueDate column: "data_emissione_documento", sqlType: 'TIMESTAMP'
        signatureRow1 column: "firma_riga1"
        signatureRow2 column: "firma_riga2"
        expirationDate column: "data_scadenza", sqlType: 'TIMESTAMP'
        rentalId column: "id_noleggiatore",sqlType: 'int4'
        registeredMail column: "raccomandata"
        assessorOfficer column: "agente_accertatore"
        uploadDate column: "upload_date", sqlType: 'TIMESTAMP'

        version false;
    }


}
