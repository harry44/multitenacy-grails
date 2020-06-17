package it.myrent.ee.db;

/**
 * Created by Shivangani on 9/20/2018.
 */
public class MROldDeviceConfiguration implements it.aessepi.utils.db.PersistentInstance {

    private Integer id;
    private String ApiKey;
    private String Secretkey;
    private String PhysicalIDPOS;
    private MROldSede sede;
    private String Description;
    private MROldPagamento payment;
    private MROldPagamento paymentToken;
    private String codiceGruppo;
    private String chargeApiKey;
    private String chargeSecretKey;

    private String idCliente;
    private String codEseSia;
    private String codiceAbi;
    private String fetchSecretKey;

    public String getChargeApiKey() {
        return chargeApiKey;
    }

    public void setChargeApiKey(String chargeApiKey) {
        this.chargeApiKey = chargeApiKey;
    }

    public String getChargeSecretKey() {
        return chargeSecretKey;
    }

    public void setChargeSecretKey(String chargeSecretKey) {
        this.chargeSecretKey = chargeSecretKey;
    }



    public String getCodiceGruppo() {
        return codiceGruppo;
    }

    public void setCodiceGruppo(String codiceGruppo) {
        this.codiceGruppo = codiceGruppo;
    }


    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public MROldPagamento getPayment() {
        return payment;
    }

    public void setPayment(MROldPagamento payment) {
        this.payment = payment;
    }


    public MROldSede getSede() {
        return sede;
    }

    public void setSede(MROldSede sede) {
        this.sede = sede;
    }


    public String getApiKey() {
        return ApiKey;
    }

    public void setApiKey(String ApiKey) {
        this.ApiKey = ApiKey;
    }

    public String getSecretkey() {
        return Secretkey;
    }

    public void setSecretkey(String Secretkey) {
        this.Secretkey = Secretkey;
    }

    public String getPhysicalIDPOS() {
        return PhysicalIDPOS;
    }

    public void setPhysicalIDPOS(String PhysicalIDPOS) {
        this.PhysicalIDPOS = PhysicalIDPOS;
    }

    @Override
    public String toString() {
        return  Description + "," + ApiKey ;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getCodEseSia() {
        return codEseSia;
    }

    public void setCodEseSia(String codEseSia) {
        this.codEseSia = codEseSia;
    }

    public String getCodiceAbi() {
        return codiceAbi;
    }

    public void setCodiceAbi(String codiceAbi) {
        this.codiceAbi = codiceAbi;
    }

    public String getFetchSecretKey() {
        return fetchSecretKey;
    }

    public void setFetchSecretKey(String fetchSecretKey) {
        this.fetchSecretKey = fetchSecretKey;
    }

    public MROldPagamento getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(MROldPagamento paymentToken) {
        this.paymentToken = paymentToken;
    }

}
