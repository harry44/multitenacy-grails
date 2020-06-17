package com.dogmasystems.myrent.db

import com.dogmasystems.touroperatorportal.Pagamenti

class MRDeviceConfiguration implements Serializable{

    String apiKey;
    String secretkey;
    String physicalIDPOS;
    MRLocation sede;
    String description;
    Pagamenti payment;
    Pagamenti paymentToken;
    String codiceGruppo;
    String chargeApiKey;
    String chargeSecretKey;
    String idCliente;
    String codEseSia;
    String codiceAbi;
    String fetchSecretKey;

    static mapping = {
        cache true
        table name: "device_configuration"//, schema: "public"
        id column: "id", sqlType: "int4"
        id generator:'sequence', params:[sequence:'device_configuration_seq']
        apiKey column:"apikey"
        secretkey  column:"secretkey"
        physicalIDPOS column:"physical_id_pos"
        sede column: "id_sede" , sqlType: "int4"
        description column:"description"
        payment column: "payment" , sqlType: "int4"
        codiceGruppo column:"codice_gruppo"
        chargeApiKey column:"charge_api_key"
        chargeSecretKey column:"charge_secret_key"
        idCliente column:"id_cliente"
        codEseSia column:"cod_ese_sia"
        codiceAbi column:"codice_abi"
        fetchSecretKey column:"fetch_secret_key"
        paymentToken column: "payment_token", sqlType: "int4"
        version false
    }

    static constraints = {

        apiKey nullable: true
        secretkey  nullable: true
        physicalIDPOS nullable: true
        sede nullable: true
        description nullable: true
        payment nullable: true
        codiceGruppo nullable: true
        chargeApiKey nullable: true
        chargeSecretKey nullable: true
        idCliente nullable: true
        codEseSia nullable: true
        codiceAbi nullable: true
        fetchSecretKey nullable: true
        paymentToken nullable: true
    }
}
