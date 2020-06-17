package com.dogmasystems.myrent.db

import grails.util.Holders
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder

import java.text.MessageFormat

class MRVatCodes implements Serializable{

	String code;
	String description;
	Double aliquot;//formatter come percentuale
	Boolean isDeductible;
	Double nonDeductible;
	Integer operation; //IMPONIBILE; ESENTE; NON_IMPONIBILE; NON_SOGGETTO; NON_SOGG_ART_74
	Boolean isPurchasesMount;
	String vatCodePurchaseMount;
	Boolean plafond;
	Boolean goldSilverImport;
	Integer esenteArt10Prorata; //ESENTE_ART_10; NO; ESCLUSO_VOLUME_AFFARI
	Boolean bolloSuImponibiliEsenti;
	Double compensation; //formatter come percentuale.
	Integer agricPurchaseType; //AGEVOLATO; AGEVOLATO_ESONERATO; NON_AGEVOLATO; ALTRI; NORMALE
	Integer intraOperation; //NO_INTRA; ACQUISTI; VENDITE; ACQUISTI_VENDITE
	Boolean marginSystem;
	String externalCode;
	// Added field
	Integer proKey
    String naturaCodiceIVA;
    String esigibilita;
	Boolean isDefault


    /** Constants definitions**/
	public static final Integer ESENTE_ART10_SI = new Integer(0);
	public static final Integer ESENTE_ART10_NO = new Integer(1);
	public static final Integer ESENTE_ART10_ESCL_VOL_AFFARI = new Integer(2);

	public static final Integer ACQUISTO_AGRICOLO_AGEVOLATO = new Integer(0);
	public static final Integer ACQUISTO_AGRICOLO_AGEVOLATO_ESONERATO = new Integer(1);
	public static final Integer ACQUISTO_AGRICOLO_NON_AGEVOLATO = new Integer(2);
	public static final Integer ACQUISTO_AGRICOLO_ALTRI = new Integer(3);
	public static final Integer ACQUISTO_AGRICOLO_NORMALE = new Integer(4);

	public static final Integer OPERAZIONI_INTRA_NO_INTRA = new Integer(0);
	public static final Integer OPERAZIONI_INTRA_ACQUISTI = new Integer(1);
	public static final Integer OPERAZIONI_INTRA_VENDITE = new Integer(2);
	public static final Integer OPERAZIONI_INTRA_ACQUISTI_VENDITE = new Integer(3);

	public static final Integer OPERAZIONE_IMPONIBILE = new Integer(0);
	public static final Integer OPERAZIONE_ESENTE = new Integer(1);
	public static final Integer OPERAZIONE_NON_IMPONIBILE = new Integer(2);
	public static final Integer OPERAZIONE_NON_SOGGETTO = new Integer(3);
	public static final Integer OPERAZIONE_NON_SOGG_ART_74 = new Integer(4);

	//add these new column for exporting data to web portal
	Boolean isExported;
	Boolean isUpdated;

	public MRVatCodes() {
	}

//	@Override
//	public Long getId() {
//		return this.id;
//	}
//	public void setId(long id){
//		this.id = id;
//	}

//	public String toString() {
//		String retValue = new String();
//		if(getCode() != null && getDescription() != null) {
//			retValue = getCode() + " - " + getDescription(); //NOI18N
//		} else if(getCode() != null) {
//			retValue = getCode();
//		} else if(getDescription() != null) {
//			retValue = getDescription();
//		} else if(getAliquot() != null) {
//			retValue = MessageFormat.format("IVA al {0,number,0.## %}", getAliquot());
//		}
//		return retValue;
//	}

	public boolean equals(Object other) {
		if ( !(other instanceof MRVatCodes) ) return false;
		MRVatCodes castOther = (MRVatCodes) other;
		return new EqualsBuilder()
				.append(this.getId(), castOther.getId())
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(getId())
				.toHashCode();
	}

	public Double getCodiceIva() {
		if(getAliquot() != null) {
			return new Double(getAliquot().doubleValue() * 100);
		} else {
			return new Double(0);
		}
	}

	static hasMany = [fatturaImpostas: MRTaxInvoice,
					  fatturaRigas: MRRowInvoice,
//	                  listinisForIdCodIva: Listini,
//	                  listinisForIdCodIvaNo: Listini,
					  //   righeImpostas: RigheImposta
					  //righePrimanotas: RighePrimanota,
//	                  stagioneTariffasForIdCodIva: StagioneTariffa,
//	                  stagioneTariffasForIdCodIvaNo: StagioneTariffa,
//	                  tariffesForIdCodIva: Tariffe,
//	                  tariffesForIdCodIvaExtraPrepay: Tariffe,
//	                  tariffesForIdCodIvaNo: Tariffe]
	]

	// TODO you have multiple hasMany references for class(es) [Listini, StagioneTariffa, Tariffe]
	//      so you'll need to disambiguate them with the 'mappedBy' property:
//	static mappedBy = [listinisForIdCodIva: "codiciivaByIdCodIva",
//	                   listinisForIdCodIvaNo: "codiciivaByIdCodIvaNo",
//	                   stagioneTariffasForIdCodIva: "codiciivaByIdCodIva",
//	                   stagioneTariffasForIdCodIvaNo: "codiciivaByIdCodIvaNo",
//	                   tariffesForIdCodIva: "codiciivaByIdCodIva",
//	                   tariffesForIdCodIvaExtraPrepay: "codiciivaByIdCodIvaExtraPrepay",
//	                   tariffesForIdCodIvaNo: "codiciivaByIdCodIvaNo"]

	static mapping = {
		cache true
		table name: "codiciiva"//, schema: "public"
		id generator:'sequence', params:[sequence:'codiciiva_seq']
		id column: "id", sqlType: "int4"
		version false
		vatCodePurchaseMount column: "c_iva_monte_acq"
		esenteArt10Prorata column: "es_art_10", sqlType: "int4"
		code column:"codice"
		description column:"descrizione"
		aliquot column:"aliquota"
		isDeductible column:"detraibile"
		//monteAcq column:"monte_acq"
		nonDeductible column:"indetraibilita"
		operation  column:"operazione"
		plafond column:"plafond"
		goldSilverImport column:"imp_oro_arg"
		bolloSuImponibiliEsenti column:"bollo_imp_es"
		compensation  column:"compensazione"
		agricPurchaseType column:"tipo_acq_agr"
		intraOperation column:"op_intra"
		marginSystem column:"sist_margine"
		externalCode column:"codice_esterno"
		proKey column: "prokey"
        naturaCodiceIVA column: "natura_codice_iva"
        esigibilita column: "esigibilita"
		isDefault column: "id_default"

	}

	static constraints = {
		code nullable: true, unique: true
		description nullable: true
		aliquot nullable: true, scale: 17
		isDeductible nullable: true
		isExported nullable: true
		isPurchasesMount nullable: true
		isUpdated nullable: true
		externalCode nullable: true
		vatCodePurchaseMount nullable: true
		proKey nullable: true
		nonDeductible nullable: true
 		operation nullable: true
	    plafond nullable: true
		goldSilverImport nullable: true
		esenteArt10Prorata nullable: true
		bolloSuImponibiliEsentinullable: true
		compensation nullable: true
		agricPurchaseType nullable: true
		intraOperation nullable: true
		marginSystem nullable: true
		externalCode nullable: true
		bolloSuImponibiliEsenti nullable: true
        naturaCodiceIVA nullable: true
        esigibilita nullable: true
		isDefault nullable: true


	}
	public String toString() {
		String retValue = new String();
		if(code != null && description!= null) {
			retValue = code + " - " + description; //NOI18N
		} else if(code != null) {
			retValue = code;
		} else if(description != null) {
			retValue = description;
		} else if(aliquot != null) {
			retValue = MessageFormat.format("IVA al {0,number,0.## %}", aliquot);
		}
		return retValue;
	}



}
