package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRChartAccount
import com.dogmasystems.myrent.db.MROptionalClass;
import com.dogmasystems.myrent.db.MRRowInvoice
import com.dogmasystems.utils.BundleUtils
import grails.util.Holders;
class Optionals implements Serializable{

//	public static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

	static auditable = true

	String codice
	String descrizione
	String otaCode
	Boolean appListino
	Boolean appGruppo
	Boolean moltiplicabile
	Boolean impGiornaliero
	Boolean impFisso
	Boolean impKm
	Boolean impPercento
	Boolean impNegoziare
	Integer addebitoMax
	Integer addebitoMin
	Double importo
	Double impDanno
	Boolean soggIvaImp
	Boolean soggIvaDan
	Boolean assicurazione
	Boolean pai
	Boolean danni
	Boolean furto
	Boolean franchigia
	Boolean riduzione
	Double percentuale
	Boolean km
	Boolean kmIllim
	Integer kmIncl
	Boolean kmExtra
	Boolean carburante
	Boolean lavaggio
	Boolean suppEta
	Integer etaMin
	Integer etaMax
	Boolean anniPatente
	Integer anniMax
	Boolean guidaAgg
	Boolean fuoriOrario
	Integer oreMax
	Boolean oneriAer
	Boolean oneriFer
	Boolean roadTax
	Boolean oneWay
	Boolean deliveryCollect
	Boolean annullamento
	Boolean noShow
	Boolean accessorio
	Boolean tempoExtra
	Boolean giornataRidotta
	Boolean notteExtra
	Boolean mezzaGiornata
	Boolean giornoFestivo
	Boolean altro
	MRChartAccount pianoDeiContiByIdContab
	MRChartAccount pianoDeiContiByIdContabFr
	MROptionalClass optionalClass
	Boolean bookingOnline
	Boolean pienoPrepagato
	Boolean depositoCauzionale
	String applicability
	String webApplicability
	Boolean isVirtual
	String includeOptionals
	String excludeOptionals
	Boolean isRateOptional
	Boolean isUseRate
	Boolean isDisabled
	Listini rateCommissionId

	static hasMany = [fatturaRigas: MRRowInvoice,
					  optionalsListinis: OptionalsListini,
					  optionalsTariffes: OptionalsTariffe]
	static belongsTo = [MRChartAccount]

	static mappedBy = [fatturaRigas:"optionals"]

	static mapping = {
		cache true
		//datasource 'myrent'
		table name: "optionals"//, schema: "public"
		id generator: 'sequence', params:[sequence:'optionals_seq']
		id column: "id", sqlType: "int4"
		otaCode column: "ota_code"
		optionalClass  sqlType: "int4"
		pianoDeiContiByIdContab column:"id_contab", sqlType: "int4"
		pianoDeiContiByIdContabFr column:"id_contab_fr", sqlType: "int4"
		optionalClass column: "optional_class_id"
		appListino column: "app_listino"
		appGruppo column: "app_gruppo"
		impGiornaliero column: "imp_giornaliero"
		impFisso column: "imp_fisso"
		impKm column: "imp_km"
		impPercento column: "imp_percento"
		impNegoziare column: "imp_negoziare"
		addebitoMax column: "addebito_max"
		addebitoMin column: "addebito_min"
		impDanno column: "imp_danno"
		soggIvaImp column: "sogg_iva_imp"
		soggIvaDan column: "sogg_iva_dan"
		kmIllim column: "km_illim"
		kmIncl column: "km_incl"
		kmExtra column: "km_extra"
		suppEta column: "supp_eta"
		etaMin column: "eta_min"
		etaMax column: "eta_max"
		anniPatente column: "anni_patente"
		anniMax column: "anni_max"
		guidaAgg column: "guida_agg"
		fuoriOrario column: "fuori_orario"
		lavaggio column: "lavaggio"
		oreMax column: "ore_max"
		oneriAer column: "oneri_aer"
		oneriFer column: "oneri_fer"
		roadTax column: "road_tax"
		oneWay column: "one_way"
		deliveryCollect column: "delivery_collect"
		noShow column: "no_show"
		tempoExtra column: "tempo_extra"
		giornataRidotta column: "giornata_ridotta"
		notteExtra column: "notte_extra"
		mezzaGiornata column: "mezza_giornata"
		giornoFestivo column: "giorno_festivo"
		bookingOnline column: "booking_online"
		pienoPrepagato column: "pieno_prepagato"
		depositoCauzionale column: "deposito_cauzionale"
		webApplicability column: "web_applicability"
		isVirtual column: "is_virtual"
		includeOptionals column: "include_optionals"
		excludeOptionals column: "exclude_optionals"
		isRateOptional column: "is_rate_optional"
		isUseRate column: "is_use_rate"
		rateCommissionId column: "rate_commission_id", sqlType: "int4"

			descrizione column:"descrizione"

		version false
	}

	static constraints = {
		codice nullable: true
		descrizione nullable: true
		otaCode nullable: true
		appListino nullable: true
		appGruppo nullable: true
		moltiplicabile nullable: true
		impGiornaliero nullable: true
		impFisso nullable: true
		impKm nullable: true
		impPercento nullable: true
		impNegoziare nullable: true
		addebitoMax nullable: true
		importo nullable: true, scale: 17
		impDanno nullable: true, scale: 17
		soggIvaImp nullable: true
		soggIvaDan nullable: true
		assicurazione nullable: true
		pai nullable: true
		danni nullable: true
		furto nullable: true
		franchigia nullable: true
		riduzione nullable: true
		percentuale nullable: true, scale: 17
		km nullable: true
		kmIllim nullable: true
		kmIncl nullable: true
		kmExtra nullable: true
		carburante nullable: true
		lavaggio nullable: true
		suppEta nullable: true
		etaMin nullable: true
		etaMax nullable: true
		anniPatente nullable: true
		anniMax nullable: true
		guidaAgg nullable: true
		fuoriOrario nullable: true
		oreMax nullable: true
		oneriAer nullable: true
		oneriFer nullable: true
		roadTax nullable: true
		oneWay nullable: true
		deliveryCollect nullable: true
		annullamento nullable: true
		noShow nullable: true
		accessorio nullable: true
		tempoExtra nullable: true
		giornataRidotta nullable: true
		notteExtra nullable: true
		mezzaGiornata nullable: true
		giornoFestivo nullable: true
		altro nullable: true
		bookingOnline nullable: true
		pienoPrepagato nullable: true
		depositoCauzionale nullable: true
		webApplicability nullable: true
		isVirtual nullable: true
		includeOptionals nullable: true
		excludeOptionals nullable: true
		isRateOptional nullable: true
		isUseRate nullable: true
		applicability nullable: true
		rateCommissionId nullable: true
		optionalClass nullable: true
		pianoDeiContiByIdContabFr nullable: true
		isDisabled nullable:true,default:false
		addebitoMin nullable: true
	}

	public String getStringTipoImporto() {
		if (Boolean.TRUE.equals(impFisso)) {
			return message(code:"Optional.TipoImportoFisso");
		} else if (Boolean.TRUE.equals(impGiornaliero)) {
			return message(code:"Optional.TipoImportoGiornaliero");
		} else if (Boolean.TRUE.equals(impPercento)) {
			return message(code:"Optional.TipoImportoPercentuale");
		} else if (Boolean.TRUE.equals(impKm)) {
			return message(code:"Optional.TipoImportoAKm");
		} else if (Boolean.TRUE.equals(impNegoziare)) {
			return message(code:"Optional.TipoImportoDaNegoziare");
		}
		return null;
	}

	public String getStringImporto() {
		if (Boolean.TRUE.equals(impFisso)) {
			return new String();
		} else if (Boolean.TRUE.equals(impGiornaliero)) {
			return message(code:"Optional.ImportoAlGiorno");
		} else if (Boolean.TRUE.equals(impPercento)) {
			return message(code:"Optional.ImportoPercentualeNolo");
		} else if (Boolean.TRUE.equals(impKm)) {
			return message(code:"Optional.ImportoAKm");
		} else if (Boolean.TRUE.equals(impNegoziare)) {
			return message(code:"Optional.ImportoDaNegoziare");
		}
		return null;
	}

	public String getStringTipoOptional() {
		if (Boolean.TRUE.equals(getAppListino())) {
			return message(code:"Optional.Listino");
		} else if (Boolean.TRUE.equals(getAppGruppo())) {
			return message(code:"Optional.Gruppo");
		}
		return null;
	}
}
