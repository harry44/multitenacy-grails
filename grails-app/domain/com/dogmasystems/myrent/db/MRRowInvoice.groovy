package com.dogmasystems.myrent.db

import com.dogmasystems.touroperatorportal.Carburanti
import com.dogmasystems.touroperatorportal.Optionals

import java.text.MessageFormat

class MRRowInvoice  implements Serializable{

	Boolean tempoKm
	Boolean tempoExtra
	Boolean franchigia
	Integer invoiceRowNumber
	String description
	String unitsOfMeasurement
	Double quantity
	Double unitPrice
	Double discount
	Double fixedDiscount
	Double taxableRowTotal
	Double vatRowTotal
	Double rowTotal
	MRVatCodes vatCodes
	//Optionals optionals
	Optionals optionals
	Carburanti carburanti
	MRChartAccount chartAccount
	MRHeaderInvoice headerInvoice

	def messageSource
	def locale = Locale.getDefault()

//	static belongsTo = [Carburanti, Codiciiva, MRHeaderInvoice, Optionals, MRChartAccount]
	static belongsTo = [ MRVatCodes, MRHeaderInvoice, MRChartAccount]
//	public static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
//	private static final MessageFormat TO_STRING_MESSAGE_FORMAT = new MessageFormat(bundle.getString("RigaDocumentoFiscale.msgDescrizione0Quantita1Prezzo2Imponibile3Totale4"));
	static mapping = {
		cache true
		table name: "fattura_riga"//, schema: "public"
		id generator:'sequence', params:[sequence:'fattura_riga_seq']
		id column: "id", sqlType: "int4"
		vatCodes column: "id_aliquota_iva", sqlType: "int4"
		//	optionals column: "id_optional", sqlType: "int4"
		carburanti column: "id_carburante", sqlType: "int4"
		chartAccount column: "id_piano_dei_conti", sqlType: "int4"
		headerInvoice column: "id_fattura", sqlType: "int4"
		invoiceRowNumber column: "numero_riga_fattura"
		description column: "descrizione"
		unitsOfMeasurement column: "unita_misura"
		quantity column: "quantita"
		unitPrice column: "prezzo_unitario"
		discount column: "sconto"
		fixedDiscount column: "sconto_fisso"
		taxableRowTotal column: "totale_imponibile_riga"
		vatRowTotal column: "totale_iva_riga"
		rowTotal column: "totale_riga"
		optionals column: "id_optional" , sqlType: "int4"
		tempoExtra column: "tempo_extra"
		tempoKm column: "tempo_km"

		version false
	}

	static constraints = {
		tempoKm nullable: true
		tempoExtra nullable: true
		franchigia nullable: true
		invoiceRowNumber nullable: true
		description nullable: true
		unitsOfMeasurement nullable: true
		quantity nullable: true, scale: 17
		unitPrice nullable: true, scale: 17
		discount nullable: true, scale: 17
		fixedDiscount nullable: true, scale: 17
		taxableRowTotal nullable: true, scale: 17
		vatRowTotal nullable: true, scale: 17
		rowTotal nullable: true, scale: 17
		carburanti nullable: true
		chartAccount nullable: true
		optionals nullable: true
	}

	public String toString() {
		MessageFormat TO_STRING_MESSAGE_FORMAT = new MessageFormat(messageSource.getMessage("RigaDocumentoFiscale.msgDescrizione0Quantita1Prezzo2Imponibile3Totale4", null, "RigaDocumentoFiscale.msgDescrizione0Quantita1Prezzo2Imponibile3Totale4", locale))
		Object[] obj = [description, quantity, unitPrice, taxableRowTotal, rowTotal,
			(headerInvoice?.prefix!=null?headerInvoice?.prefix:"") + " " + headerInvoice?.invoiceNumb ]
		return TO_STRING_MESSAGE_FORMAT.format(obj);
	}
}
