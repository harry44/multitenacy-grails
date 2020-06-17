package com.dogmasystems.myrent.db

//import com.dogmasystems.utils.db.PersistentInstance;

import java.text.DecimalFormat

import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

class MRChartAccount implements Serializable{

	String type
	String description
	String pattern
	Integer mastroCode
	Integer accountCode
	Integer subAccountCode
	Integer detailCode
	Integer balanceSection
	Boolean isEditable
	String externalAccount
	String externalSubAccount
	//add these new column for exporting data to web portal
	Boolean isExported
	Boolean isUpdated
//Add the column to contain id of piano_dei_conti table of pro database
	Integer proKey
//
//	@Override
//	public Long getId() {
//		return this.id;
//	}
//	public Long setId(Long id) {
//		this.id = id ;
//   }
//

	static hasMany = [//arburantis: Carburanti,
					  clientis: MRBusinessPartner,		//Class changed to MRBusinessPartner as MRClient is deleted
					  // distintes: Distinte,
					  fatturaRigas: MRRowInvoice,
//	                  optionalsesForIdContab: Optionals,
//	                  optionalsesForIdContabFr: Optionals,
//	                  pagamentis: Pagamenti,
					  registriIvas:  MRVatCodes,
					  //   righeCausales: RigheCausale,
					  //  righeImpostas: RigheImposta,
					  //righePrimanotas: RighePrimanota
	]

	// TODO you have multiple hasMany references for class(es) [Optionals]
	//      so you'll need to disambiguate them with the 'mappedBy' property:
//	static mappedBy = [optionalsesForIdContab: "pianoDeiContiByIdContab",
//	                   optionalsesForIdContabFr: "pianoDeiContiByIdContabFr"]

	static mapping = {
		cache true
		table name:'piano_dei_conti'//, schema:'public'
		id generator:'sequence', params:[sequence:'piano_dei_conti_seq']
		id column: "id", sqlType: "int4"
		type column: "tipo"
		description column: "descrizione"
		pattern column: "pattern"
		mastroCode column: "codice_mastro"
		accountCode column: "codice_conto"
		subAccountCode column: "codice_sottoconto"
		detailCode column: "codice_dettaglio"
		balanceSection column: "sezione_bilancio"
		isEditable column: "editable"
		externalAccount column: "conto_esterno"
		externalSubAccount column: "sottoconto_esterno"
		isExported column: "isexported"
		isUpdated column: "isupdated"

		registriIvas column: "mrchart_acc_registri_ivas_id"
		proKey column: "prokey"
		version false
	}

	static constraints = {
		description nullable: true
		pattern nullable: true, unique: true
		mastroCode nullable: true
		accountCode nullable: true
		subAccountCode nullable: true
		detailCode nullable: true
		balanceSection nullable: true
		isEditable nullable: true
		externalAccount nullable: true
		externalSubAccount nullable: true
		isExported nullable: true
		isUpdated nullable: true
		proKey nullable: true
	}

	public String toString() {
		String returnValue = new String();
		if (pattern != null) {
			returnValue = returnValue + pattern;
		}
		if (description != null) {
			if (returnValue.trim().length() == 0) {
				returnValue = new String();
			} else {
				returnValue = returnValue + " - " + description; //NOI18N
			}
		}
		return returnValue;
	}
}