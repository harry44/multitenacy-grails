package com.dogmasystems.myrent.db

import com.dogmasystems.fleet.db.MRVehicleTransfer
import com.dogmasystems.touroperatorportal.Carburanti
import com.dogmasystems.touroperatorportal.Noleggiatori
import com.dogmasystems.touroperatorportal.VehicleAssistance

class MRVehicle implements Serializable {

    String make
    String brand
    String model
    String versione
    String plateNumber
    String exPlateNumber
    Date registrationDate
    String carChassis
    String colore
    Integer portUtile
    Integer noPosti
    Double valoreVeicolo
    Integer km
    String voltage
    String promemoria
    Integer livelloCombustibile
    Integer capacitaSerbatoio
    Boolean pulita
    String parcheggio
    Boolean isEnabled
    Boolean isReserved
    Boolean isBulkhead
    Boolean isSuitableForAbroad
    Boolean isPowerGridAttack
    Date dataScadenzaContratto
    Date dateRentalExtension1 //dateproroga1
    Date dateRentalExtension2 //dateproroga1
    String venditore
    Double valAcq
    Date dateRentalStart  // dataAcquisto
    String acquirente
    Double valVend
    Date dateFleetExit//dataVend
    String noteVend
    Integer sedeIntrarent
    String cilindrata
    MRAffiliate affiliate
    Carburanti carburanti
    MRLocation location
    MRGroup group
    String codiceVeicolo
    Noleggiatori noleggiatori
    VehicleAssistance idVehicleAssistance
    MRVehicleUsage vehicleusage
    String noteAcquisto
    Integer proKey
    Boolean isRentToRent
    Double amountGoodToBillForR2R
    Double amountServiceToBillForR2R
    Double amountToBillForR2R
    MRVehicleDetail vehicleDetail
    Boolean isPromoCar
    String pneumatici
    String misuraPneumatici
    String carrozzeria
    String ruotaDiScorta
    Boolean isAntitheft

    static hasMany = [
            damages             : MRDamage,
            vehicleAvailabilitys: MRVehicleAvailability,
            headerInvoices      : MRHeaderInvoice,
            vehicleMovements    : MRVehicleMovement,
            vehicleTransfer     : MRVehicleTransfer//,
    ]

    static hasOne = [vehicleDetail: MRVehicleDetail]
    static belongsTo = [MRAffiliate, MRGroup, MRLocation, Noleggiatori]

    static mapping = {
        cache true
        vehicleDetail cache: true
        vehicleMovements cache: true
        sort("id")
        table name: "parco_veicoli"//, schema: "public"
        id generator: 'sequence', params: [sequence: 'parco_veicoli_seq']
        id column: "id", sqlType: "int4"
        dataScadenzaContratto sqlType: "date"
        dateFleetExit column: "data_vend", sqlType: "date"
        dateRentalStart column: "data_acq", sqlType: "date"
        affiliate column: "id_affiliato", sqlType: "int4"
        carburanti column: "id_carburante", sqlType: "int4"
        location column: "id_sede", sqlType: "int4"
        group column: "id_gruppo", sqlType: "int4"
        noleggiatori column: "id_noleggiatore", sqlType: "int4"
        dateRentalExtension1 column: "data_proroga_1", sqlType: "date"
        dateRentalExtension2 column: "data_proroga_2", sqlType: "date"
        model column: "modello"
        brand column: "marca"
        versione column: "versione"
        plateNumber column: "targa"
        idVehicleAssistance column: "id_vehicle_assistance", sqlType: "int4"
        exPlateNumber column: "extarga"
        registrationDate column: "data_immatricolazione", sqlType: "date"
        carChassis column: "telaio"
        livelloCombustibile column: "livello_combustibile"
        km column: "km"
        isEnabled column: "abilitato"
        isReserved column: "impegnato"
        promemoria column: "promemoria"
        parcheggio column: "parcheggio"
        venditore column: "venditore"
        noteAcquisto column: "note_acq"
        acquirente column: "acquirente"
        valVend column: "val_vend"
        valAcq column: "val_acq"
        sedeIntrarent column: "sede_intrarent"
        proKey column: "prokey"
        vehicleusage column: "vehicleusage"
        portUtile column: "port_utile"
        noPosti column: "no_posti"
        capacitaSerbatoio column: "capacita_serbatoio"
        codiceVeicolo column: "codice_veicolo"
        isRentToRent column: "is_rent_to_rent"
        isSuitableForAbroad column: "is_suitable_for_abroad"
        isPowerGridAttack column: "is_power_grid_attack"
        isBulkhead column: "is_bulkhead"
        vehicleDetail cascade: 'all', fetch: 'join', unique: true
        amountGoodToBillForR2R column: "amount_good_bill_for_r_r"
        amountServiceToBillForR2R column: "amount_service_bill_for_r_r"
        amountToBillForR2R column: "amount_to_bill_for_r_r"
        isPromoCar column: "is_promo_car"
        pneumatici column: "pneumatici"
        misuraPneumatici column: "misura_pneumatici"
        carrozzeria column: "carrozzeria"
        ruotaDiScorta column: "ruota_di_scorta"
        isAntitheft column: "antitheft"
        version false
    }

    static constraints = {
        make nullable: true
        brand nullable: true
        model nullable: true
        versione nullable: true
        plateNumber nullable: true
        exPlateNumber nullable: true
        registrationDate nullable: true
        carChassis nullable: true
        colore nullable: true
        portUtile nullable: true
        noPosti nullable: true
        valoreVeicolo nullable: true
        livelloCombustibile nullable: true
        capacitaSerbatoio nullable: true
        pulita nullable: true
        parcheggio nullable: true
        isSuitableForAbroad nullable: true
        dataScadenzaContratto nullable: true
        dateRentalStart nullable: true
        noteVend nullable: true
        cilindrata nullable: true
        affiliate nullable: true
        carburanti nullable: true
        location nullable: true
        group nullable: true
        codiceVeicolo nullable: true
        noleggiatori nullable: true
        idVehicleAssistance nullable: true
        km nullable: true
        isEnabled nullable: true
        isReserved nullable: true
        isBulkhead nullable: true
        isPowerGridAttack nullable: true
        voltage nullable: true
        dateRentalExtension1 nullable: true
        dateRentalExtension2 nullable: true
        dateFleetExit nullable: true
        promemoria nullable: true
        acquirente nullable: true
        vehicleusage nullable: true
        venditore nullable: true
        noteAcquisto nullable: true
        sedeIntrarent nullable: true
        valVend nullable: true
        valAcq nullable: true
        proKey nullable: true
        isRentToRent nullable: true
        amountGoodToBillForR2R nullable: true
        amountServiceToBillForR2R nullable: true
        amountToBillForR2R nullable: true
        isPromoCar nullable: true
        pneumatici nullable: true
        misuraPneumatici nullable: true
        carrozzeria nullable: true
        ruotaDiScorta nullable: true
        isAntitheft nullable: true
    }

    String toString() {
        def fullName = ""
        fullName += plateNumber != null ? plateNumber : ""
        fullName += make != null ? fullName != "" ? " - " + make : make : ""
        fullName += model != null ? fullName != "" ? " - " + model : model : ""
        return fullName.replaceAll(" ", "")
    }
}
