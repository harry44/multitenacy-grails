package it.myrent.ee.db;

/**
 * Created by saurabhmishra on 25/11/16.
 */
public class MROldFetchTransaction {

    private Integer id;
    private String timestamp;
    private String idTrans;
    private String numOrdine;
    private String stato;
    private String acqbin;
    private String tipopag;
    private String circuito;
    private String importoTrans;
    private String importoAutor;
    private String importoContab;
    private String importoStornato;
    private String esitoTrans;
    private String numaut;
    private String codiceEsercente;
    private String mac;
    private String tAutor;
    private String valuta;
    private String oldTimestamp;
    private String rentalAgreementId;



    public String getAcqbin() {
        return acqbin;
    }

    public void setAcqbin(String acqbin) {
        this.acqbin = acqbin;
    }

    public String getCircuito() {
        return circuito;
    }

    public void setCircuito(String circuito) {
        this.circuito = circuito;
    }

    public String getCodiceEsercente() {
        return codiceEsercente;
    }

    public void setCodiceEsercente(String codiceEsercente) {
        this.codiceEsercente = codiceEsercente;
    }

    public String getEsitoTrans() {
        return esitoTrans;
    }

    public void setEsitoTrans(String esitoTrans) {
        this.esitoTrans = esitoTrans;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdTrans() {
        return idTrans;
    }

    public void setIdTrans(String idTrans) {
        this.idTrans = idTrans;
    }

    public String getImportoAutor() {
        return importoAutor;
    }

    public void setImportoAutor(String importoAutor) {
        this.importoAutor = importoAutor;
    }

    public String getImportoContab() {
        return importoContab;
    }

    public void setImportoContab(String importoContab) {
        this.importoContab = importoContab;
    }

    public String getImportoStornato() {
        return importoStornato;
    }

    public void setImportoStornato(String importoStornato) {
        this.importoStornato = importoStornato;
    }

    public String getImportoTrans() {
        return importoTrans;
    }

    public void setImportoTrans(String importoTrans) {
        this.importoTrans = importoTrans;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNumOrdine() {
        return numOrdine;
    }

    public void setNumOrdine(String numOrdine) {
        this.numOrdine = numOrdine;
    }

    public String getNumaut() {
        return numaut;
    }

    public void setNumaut(String numaut) {
        this.numaut = numaut;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String gettAutor() {
        return tAutor;
    }

    public void settAutor(String tAutor) {
        this.tAutor = tAutor;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTipopag() {
        return tipopag;
    }

    public void setTipopag(String tipopag) {
        this.tipopag = tipopag;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    /**
     * @return the oldTimestamp
     */
    public String getOldTimestamp() {
        return oldTimestamp;
    }

    /**
     * @param oldTimestamp the oldTimestamp to set
     */
    public void setOldTimestamp(String oldTimestamp) {
        this.oldTimestamp = oldTimestamp;
    }

    /**
     * @return the rentalAgreementId
     */
    public String getRentalAgreementId() {
        return rentalAgreementId;
    }

    /**
     * @param rentalAgreementId the rentalAgreementId to set
     */
    public void setRentalAgreementId(String rentalAgreementId) {
        this.rentalAgreementId = rentalAgreementId;
    }
}
