package com.dogmasystems.touroperatorportal

import com.dogmasystems.myrent.db.MRBusinessPartner
import com.dogmasystems.myrent.db.MRRentalAgreement
import grails.util.Holders

import java.text.DecimalFormat

class Garanzie implements Serializable {

    String tipo
    String descrizione
    String intestatario
    String numero
    Double importo
    Date dataEmissione
    String luogoEmissione
    String contoCorrente
    String banca
    String cab
    String abi
    String nrAut
    Date dataAut
    //String numeroAutorizzazione
    String aliaspan
    String pan
    Integer annoScadenza
    Integer meseScadenza
    String cvv
    MRBusinessPartner clienti
// Added field
    Integer proKey

    static hasMany = [contrattoNoleggiosForIdGaranzia1: MRRentalAgreement,
                      contrattoNoleggiosForIdGaranzia2: MRRentalAgreement,
                      righePrimanotas                 : RighePrimanota]
    static belongsTo = [MRBusinessPartner]

    // TODO you have multiple hasMany references for class(es) [ContrattoNoleggio]
    //      so you'll need to disambiguate them with the 'mappedBy' property:
    static mappedBy = [contrattoNoleggiosForIdGaranzia1: "garanzieByIdGaranzia1",
                       contrattoNoleggiosForIdGaranzia2: "garanzieByIdGaranzia2"]

    static mapping = {
        cache true
        table name: "garanzie"//, schema: "public"
        id generator: 'sequence', params: [sequence: 'garanzie_seq']
        id column: "id", sqlType: "int4"
        dataEmissione sqlType: "date"
        dataAut sqlType: "date"
        clienti column: "id_cliente", sqlType: "int4"
        tipo column: "tipo"
        descrizione column: "descrizione"
        importo column: "importo"
        annoScadenza column: "anno_scadenza"
        meseScadenza column: "mese_scadenza"
        proKey column: "prokey"

        pan column: "pan"


        aliaspan column: "alias_pan"


        cvv column: "cvv"


        nrAut column: "nr_aut"


        numero column: "numero"


        intestatario column: "intestatario"

        version false
    }

    static constraints = {
        descrizione nullable: true
        intestatario nullable: true
        numero nullable: true
        importo nullable: true, scale: 17
        dataEmissione nullable: true
        luogoEmissione nullable: true
        contoCorrente nullable: true
        banca nullable: true
        cab nullable: true
        abi nullable: true
        nrAut nullable: true
        dataAut nullable: true
        annoScadenza nullable: true
        meseScadenza nullable: true
        cvv nullable: true
        clienti nullable: true
        //numeroAutorizzazione nullable :true
        aliaspan nullable: true
        pan nullable: true
        // Added for validation
        proKey nullable: true

    }

    public String getNumeroOscurato() {
        if (getNumero() != null) {
            char[] caratteri = getNumero().toCharArray();
            for (int i = 0; i < caratteri.length - 4; i++) {
                if (caratteri[i] != ' ') {
                    caratteri[i] = '*';
                }
            }
            return new String(caratteri);
        }
        return null;
    }

    @Override
    public String toString() {
        return ((getImporto() != null ? new DecimalFormat("\u00a4 #,##0.00").format(getImporto()) : null) + getDescrizione()).trim() + getNumeroOscurato();
    }
}
