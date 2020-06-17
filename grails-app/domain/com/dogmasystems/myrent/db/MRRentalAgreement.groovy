package com.dogmasystems.myrent.db
import com.dogmasystems.touroperatorportal.Commissioni
import com.dogmasystems.touroperatorportal.Garanzie
import com.dogmasystems.touroperatorportal.Pagamenti
import com.dogmasystems.touroperatorportal.Tariffe

class MRRentalAgreement  implements Serializable{

	Date startDate
	Date endDate
	String prefix
	Integer number
	Integer year
	Date data
	Double bail
	Double rental
	Double bailBalance
	Double rentalBalance
	Double invoiceBalance
	Double depositBalance
	//Double scontoTariffa
	String note
	Boolean isClosed
	Date rentalSignatureDate
	String signatoryName
	String signatureLocation
	String signatureAnnotations
	MRBusinessPartner clientiByIdCond3
	MRBusinessPartner clientiByIdCond2
	MRBusinessPartner clientiByIdCond1
	Garanzie garanzieByIdGaranzia1
	Garanzie garanzieByIdGaranzia2
	MRBusinessPartner clientiByIdCliente
	MRLocation locationReturnExpected
	//Tariffe tariffe
	MRAffiliate affiliate
	MRLocation locationPickUpExcepted
	MRUser users
	MREnumeration enumeration
	Tariffe tariffe
	//Pagamenti pagamenti
	Date rentalCloseDate

   //Madhvendra
	MRGroup groups
	MRGroup groupRequest
	MRGroup groupsAssign
	Pagamenti pagamenti
	Double scontoTariffa
	RentalType rentalType
	Integer numeroMovimenti
	Double noleggioPPay
	Double noleggioNoVat
	Double noleggioPPayNoVat
	Boolean isDepositCash
	Boolean isDepositCreditCard
	Double totalDeposit
	Double totalCCDeposit
	Double totalDepositG1
	Double totalDepositG2
	Double totalCashedG1
	Double totalCashedG2
	String nomeUtenteCreatore
	Date dataFirmaCerta
	Boolean documentoFirmato
	Boolean privacyMessage1
	Boolean privacyMessage2
	Boolean isEprd
	Boolean isEprdf
	Boolean isExtraServ
	Double cashRefound
	Date cashRefoundDate
	Double rentalAmountPaidByCash
	Date rentalAmountPaidByCashDate
	MRCurrencyConversionRate conversionRate
	Double garazia1CCPayment
	Double garazia2CCPayment
	Double totalDue
	Double totalCashPayment

	Boolean isCancelled
	Boolean isValid
	Boolean hasNewDamages
	Boolean isPaid
	Boolean isTheft
	Boolean isFire
	Boolean isBlackList
	Boolean isPrinted
	Double scontoExtraPrepay
	Integer proKey
	Integer month
	Integer monthDuration
	String productNameDesc;
	Date billedTill
	MRBusinessPartner clientiByInv
	Boolean isSplitPayment

	Boolean licenceConfirmationVerified1
	Boolean licenceConfirmationVerified2
	Boolean licenceConfirmationVerified3
	Boolean isSignedCheckOut
	Boolean isSignedCheckIn
	String fileNameCheckOut
	String fileNameCheckIn
	Boolean isEmailSent
	String privateNote
	MRBusinessPartner agent
	Boolean isMonthlyRentalRA
	Date estEndRA
	Boolean isEstimateEndRental
	Boolean isProcessed
	static auditable = true
	static hasOne = [commissione : Commissioni];


	static hasMany = [//commissionis: Commissioni,
					  fatturaIntestaziones: MRHeaderInvoice,
					  movimentiAutos: MRVehicleMovement,
					  contrattoRigas: MRRowRentalAgreement,
//	                  partites: Partite,
//	                  primenotes: Primenote,
//	                  rateContrattis: RateContratti
	]
	//static belongsTo = [Affiliati, Clienti, Garanzie, Gruppi, Numerazioni, Pagamenti, Sedi, Tariffe, Users]
	//static belongsTo = [MRGroup, MREnumeration,  MRLocation,  MRUser]


	static mapping = {
		cache true
		//movimentiAutos cache:true
		commissione cache: true
		tariffe cache: true


        sort id:"desc"

		table name: "contratto_noleggio"//, schema: "public"
		id column: "id", sqlType: "int4"
		id generator:'sequence', params:[sequence:'contratto_noleggio_seq']
		data column:"data", sqlType: "date"
		startDate column:"inizio"
		endDate column:"fine"
		rentalSignatureDate column:"data_firma_contratto", sqlType:"date"
		prefix column:"prefisso"
		number column:"numero"
		pagamenti column: "id_pagamento", sqlType: "int4"
		year column:"anno"
		tariffe column: "id_tariffa", sqlType: "int4"
		bail column:"cauzione"
		rental column:"noleggio"
		bailBalance column:"saldo_cauzione"
		rentalBalance column:"saldo_noleggio"
		invoiceBalance column:"saldo_fatture"
		depositBalance column:"saldo_acconti"
		scontoExtraPrepay column:"sconto_extraprepay"
		rentalCloseDate column: "rental_close_date"
		note column:"note"
		locationPickUpExcepted column: "id_sede_uscita", sqlType: "int4"
		locationReturnExpected column: "id_sede_rientro_previsto", sqlType: "int4"
		affiliate column: "id_affiliato", sqlType: "int4"
		users column: "id_user_apertura", sqlType: "int4"
		productNameDesc column: "product_name_desc"
		signatoryName column: "nome_firmarario"
		signatureLocation column: "luogo_firma"
		signatureAnnotations column: "annotazioni_firma"
		enumeration column: "id_numerazione", sqlType: "int4"
//		pagamenti column: "id_pagamento", sqlType: "int4"
		groups column: "id_gruppo", sqlType: "int4"
		groupRequest column: "id_gruppo1", sqlType: "int4"
		isClosed column: "chiuso"
		groupsAssign column: "id_gruppo_assegnato", sqlType: "int4"
		clientiByIdCond3 column: "id_cond_3", sqlType: "int4"
		clientiByIdCond2 column: "id_cond_2", sqlType: "int4"
		clientiByIdCond1 column: "id_cond_1", sqlType: "int4"
		garanzieByIdGaranzia1 column: "id_garanzia_1", sqlType: "int4"
		garanzieByIdGaranzia2 column: "id_garanzia_2", sqlType: "int4"
		clientiByIdCliente column: "id_cliente", sqlType: "int4"
		commissione cascade: 'save-update'
		rentalType column: "id_rental_type"
		numeroMovimenti formula:"( select count(*) from movimenti_auto ma where ma.id_contratto = id )"
		noleggioPPay column: "noleggio_ppay"
		noleggioNoVat column: "noleggio_no_vat"
		noleggioPPayNoVat column: "noleggio_ppay_no_vat"
		isDepositCash column: "isDepositCash"
		isDepositCreditCard column: "isDepositCreditCard"
		totalDeposit column: "totalDeposit"
		totalCCDeposit column: "totalCCDeposit"
		totalDepositG1 column: "totalDepositG1"
		totalDepositG2 column: "totalDepositG2"
		totalCashedG1 column: "totalCashedG1"
		totalCashedG2 column: "totalCashedG2"
		nomeUtenteCreatore column: "nome_utente_creatore"
		dataFirmaCerta column: "data_firma_certa"
		documentoFirmato column: "documento_firmato"
		privacyMessage1 column: "privacy1"
		privacyMessage2 column: "privacy2"


		cashRefound column: "cashrefound"
		cashRefoundDate column: "cashrefounddate"
		rentalAmountPaidByCash column: "rentalamountpaidbycash"
		rentalAmountPaidByCashDate column: "rentalamountpaidbycashdate"
		conversionRate column: "id_conversion_rate"
		isCancelled column: "is_cancelled" ,defaultValue: "false"
		isValid column: "is_valid",defaultValue: "false"
		hasNewDamages column: "has_new_damages",defaultValue: "false"
		isPaid column: "is_paid",defaultValue: "false"
		isTheft column: "is_theft",defaultValue: "false"
		isFire column: "is_fire",defaultValue: "false"
		isBlackList column: "is_black_list",defaultValue: "false"
		garazia1CCPayment column: "garazia1CCPayment"
		garazia2CCPayment column: "garazia2CCPayment"
		totalDue column: "total_due"
		totalCashPayment column: "totalCashPayment"
		isSplitPayment column: "split_payment"
		proKey column: "prokey"
		month column: "month", sqlType: "int4"
		monthDuration column: "month_duration", sqlType: "int4"
		licenceConfirmationVerified1 column: "licenceConfirmationVerified1"
		licenceConfirmationVerified2 column: "licenceConfirmationVerified2"
		licenceConfirmationVerified3 column: "licenceConfirmationVerified3"
		isEmailSent column: "is_email_sent"
		privateNote column: "private_note"
		clientiByInv column: "id_businesspartner_to_invoice" , sqlType: "int4"
		agent column: "agent_id" , sqlType: "int4"
		isMonthlyRentalRA column: "is_monthly_rental"
		isEstimateEndRental column: "is_est_end_rental"
		estEndRA column: "est_end_ra"
		isProcessed column: "is_processed"
		version false
	}

	static constraints = {
		isEmailSent nullable: true
		isMonthlyRentalRA nullable: true
		productNameDesc nullable: true
		endDate nullable: true
		 startDate nullable: true
		 prefix nullable: true
		 number nullable: true
		 year nullable: true
		 data nullable: true
		 bail nullable: true
		 rental nullable: true
		 bailBalance nullable: true
		 rentalBalance nullable: true
		 invoiceBalance nullable: true
		 depositBalance nullable: true
		//Double scontoTariffa
		 note nullable: true
		 isClosed nullable: true
		 rentalSignatureDate nullable: true
		 signatoryName nullable: true
		 signatureLocation nullable: true
		 signatureAnnotations nullable: true
		 clientiByIdCond3 nullable: true
		 clientiByIdCond2 nullable: true
		 clientiByIdCond1 nullable: true
		 garanzieByIdGaranzia1 nullable: true
		 garanzieByIdGaranzia2 nullable: true
		 clientiByIdCliente nullable: true
		 locationReturnExpected nullable: true
		//Tariffe tariffe
		 affiliate nullable: true
		 locationPickUpExcepted nullable: true
		 users nullable: true
		 enumeration nullable: true
		 tariffe nullable: true
		//Pagamenti pagamenti
		 groups nullable: true
		groupRequest nullable: true
		 groupsAssign nullable: true
		 pagamenti nullable: true
		 scontoTariffa nullable: true
		 totalCCDeposit nullable: true

		//Madhvendra
		rentalType nullable: true
		numeroMovimenti nullable: true
		noleggioPPay nullable: true
		noleggioNoVat nullable: true
		noleggioPPayNoVat nullable: true
		isDepositCash nullable:  true
		isDepositCreditCard nullable: true
		totalDeposit nullable: true
		totalCCDeposit nullable: true
		totalDepositG1 nullable: true
		totalDepositG2 nullable: true
		totalCashedG1 nullable: true
		totalCashedG2 nullable: true
		nomeUtenteCreatore nullable: true
		dataFirmaCerta nullable: true
		documentoFirmato nullable: true
		privacyMessage1 nullable: true
		privacyMessage2 nullable: true
		isEprd nullable: true
		isEprdf nullable: true
		isExtraServ nullable: true
		cashRefound nullable: true
		cashRefoundDate nullable: true
		rentalAmountPaidByCash nullable: true
		rentalAmountPaidByCashDate nullable: true
		conversionRate nullable: true
		groupRequest nullable: true
		garazia1CCPayment nullable: true
		garazia2CCPayment nullable: true
		totalDue nullable: true
		totalCashPayment nullable: true
		isCancelled nullable: true
		isValid nullable: true
		hasNewDamages nullable: true
		isPaid nullable: true
		isTheft nullable: true
		isFire nullable: true
		isBlackList nullable: true
		isPrinted nullable: true
		scontoExtraPrepay nullable: true
		proKey nullable: true
		commissione nullable: true
		clientiByInv nullable: true
		billedTill nullable: true
		isSplitPayment nullable: true
		licenceConfirmationVerified1 nullable: true
		licenceConfirmationVerified2 nullable: true
		licenceConfirmationVerified3 nullable: true
		 isSignedCheckOut nullable: true
		 isSignedCheckIn nullable: true
		 fileNameCheckOut nullable: true
		 fileNameCheckIn nullable:true
		rentalCloseDate nullable: true
		privateNote nullable: true
		agent nullable: true
		month nullable: true
		monthDuration nullable: true
		isEstimateEndRental nullable: true
		estEndRA nullable: true
		isProcessed nullable: true
	}

    String toString(){
        def fullName = ""
        fullName += prefix != null ? prefix : ""
        fullName += number != null ? fullName != "" ? " - " + number : number  : ""
        fullName += clientiByIdCliente != null ? fullName != "" ? " - " + clientiByIdCliente : clientiByIdCliente  : ""
        return fullName
    }

	MRVehicleMovement getLastMovement(){
		if(movimentiAutos != null ){
			Iterator itr = movimentiAutos.iterator()
			while(itr.hasNext()){
				MRVehicleMovement mrVehicleMovement = (MRVehicleMovement)itr.next()
				if(mrVehicleMovement.isLast){
					return mrVehicleMovement
				}
			}
			/*movimentiAutos.each {
             if(it.isLast != null && it.isLast){
              return it
             }
            }*/
		}
	}

//	public void setLastMovement(MRVehicleMovement lastMovement ){
//
//	}
}