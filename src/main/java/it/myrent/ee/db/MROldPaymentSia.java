/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

/**
 *
 * @author rashmi
 */
public class MROldPaymentSia implements it.aessepi.utils.db.PersistentInstance {

    private Integer id;
    private String idPagamento;
    private String valuta;
    private String codiceCircuito;
    private String pan;
    private String datascad;
    private String aliaspan;
    private String aliaspanrev;
    private String aliaspanDatascad;
    private String aliaspanTail;
    private String esitoTrans;
    private String esitoTrans99;
    private String numtrans;
    private String numaut;
    private String acqbin;
    private String tipopag;
    private String statopag;
    private String idLoguitran;
    private String idLogapixml;
    private String tcontab;
    private String idTrans;
    private String imptot;
    private String accInfo;
    private String accReg;
    private String numord;
    private String reqrefnum;
    private String idNegozio;
    private String importo;
    private String messagio;
    private String mac;
    private String esitoMultiTrans;
    private Integer idWarranty;
    private String idRentalAgreement;
    private String orderDescription;

    public String getIdRentalAgreement() {
        return idRentalAgreement;
    }

    public void setIdRentalAgreement(String idRentalAgreement) {
        this.idRentalAgreement = idRentalAgreement;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the idPagamento
     */
    public String getIdPagamento() {
        return idPagamento;
    }

    /**
     * @param idPagamento the idPagamento to set
     */
    public void setIdPagamento(String idPagamento) {
        this.idPagamento = idPagamento;
    }

    /**
     * @return the valuta
     */
    public String getValuta() {
        return valuta;
    }

    /**
     * @param valuta the valuta to set
     */
    public void setValuta(String valuta) {
        this.valuta = valuta;
    }

    /**
     * @return the codiceCircuito
     */
    public String getCodiceCircuito() {
        return codiceCircuito;
    }

    /**
     * @param codiceCircuito the codiceCircuito to set
     */
    public void setCodiceCircuito(String codiceCircuito) {
        this.codiceCircuito = codiceCircuito;
    }

    /**
     * @return the pan
     */
    public String getPan() {
        return pan;
    }

    /**
     * @param pan the pan to set
     */
    public void setPan(String pan) {
        this.pan = pan;
    }

    /**
     * @return the datascad
     */
    public String getDatascad() {
        return datascad;
    }

    /**
     * @param datascad the datascad to set
     */
    public void setDatascad(String datascad) {
        this.datascad = datascad;
    }

    /**
     * @return the aliaspan
     */
    public String getAliaspan() {
        return aliaspan;
    }

    /**
     * @param aliaspan the aliaspan to set
     */
    public void setAliaspan(String aliaspan) {
        this.aliaspan = aliaspan;
    }

    /**
     * @return the aliaspanrev
     */
    public String getAliaspanrev() {
        return aliaspanrev;
    }

    /**
     * @param aliaspanrev the aliaspanrev to set
     */
    public void setAliaspanrev(String aliaspanrev) {
        this.aliaspanrev = aliaspanrev;
    }

    /**
     * @return the aliaspanDatascad
     */
    public String getAliaspanDatascad() {
        return aliaspanDatascad;
    }

    /**
     * @param aliaspanDatascad the aliaspanDatascad to set
     */
    public void setAliaspanDatascad(String aliaspanDatascad) {
        this.aliaspanDatascad = aliaspanDatascad;
    }

    /**
     * @return the aliaspanTail
     */
    public String getAliaspanTail() {
        return aliaspanTail;
    }

    /**
     * @param aliaspanTail the aliaspanTail to set
     */
    public void setAliaspanTail(String aliaspanTail) {
        this.aliaspanTail = aliaspanTail;
    }

    /**
     * @return the numtrans
     */
    public String getNumtrans() {
        return numtrans;
    }

    /**
     * @param numtrans the numtrans to set
     */
    public void setNumtrans(String numtrans) {
        this.numtrans = numtrans;
    }

    /**
     * @return the numaut
     */
    public String getNumaut() {
        return numaut;
    }

    /**
     * @param numaut the numaut to set
     */
    public void setNumaut(String numaut) {
        this.numaut = numaut;
    }

    /**
     * @return the acqbin
     */
    public String getAcqbin() {
        return acqbin;
    }

    /**
     * @param acqbin the acqbin to set
     */
    public void setAcqbin(String acqbin) {
        this.acqbin = acqbin;
    }

    /**
     * @return the tipopag
     */
    public String getTipopag() {
        return tipopag;
    }

    /**
     * @param tipopag the tipopag to set
     */
    public void setTipopag(String tipopag) {
        this.tipopag = tipopag;
    }

    /**
     * @return the statopag
     */
    public String getStatopag() {
        return statopag;
    }

    /**
     * @param statopag the statopag to set
     */
    public void setStatopag(String statopag) {
        this.statopag = statopag;
    }

    /**
     * @return the idLoguitran
     */
    public String getIdLoguitran() {
        return idLoguitran;
    }

    /**
     * @param idLoguitran the idLoguitran to set
     */
    public void setIdLoguitran(String idLoguitran) {
        this.idLoguitran = idLoguitran;
    }

    /**
     * @return the idLogapixml
     */
    public String getIdLogapixml() {
        return idLogapixml;
    }

    /**
     * @param idLogapixml the idLogapixml to set
     */
    public void setIdLogapixml(String idLogapixml) {
        this.idLogapixml = idLogapixml;
    }

    /**
     * @return the messagio
     */
    public String getMessagio() {
        return messagio;
    }

    /**
     * @param messagio the messagio to set
     */
    public void setMessagio(String messagio) {
        this.messagio = messagio;
    }

    /**
     * @return the mac
     */
    public String getMac() {
        return mac;
    }

    /**
     * @param mac the mac to set
     */
    public void setMac(String mac) {
        this.mac = mac;
    }

    /**
     * @return the tcontab
     */
    public String getTcontab() {
        return tcontab;
    }

    /**
     * @param tcontab the tcontab to set
     */
    public void setTcontab(String tcontab) {
        this.tcontab = tcontab;
    }

    /**
     * @return the idTrans
     */
    public String getIdTrans() {
        return idTrans;
    }

    /**
     * @param idTrans the idTrans to set
     */
    public void setIdTrans(String idTrans) {
        this.idTrans = idTrans;
    }

    /**
     * @return the imptot
     */
    public String getImptot() {
        return imptot;
    }

    /**
     * @param imptot the imptot to set
     */
    public void setImptot(String imptot) {
        this.imptot = imptot;
    }

    /**
     * @return the accInfo
     */
    public String getAccInfo() {
        return accInfo;
    }

    /**
     * @param accInfo the accInfo to set
     */
    public void setAccInfo(String accInfo) {
        this.accInfo = accInfo;
    }

    /**
     * @return the accReg
     */
    public String getAccReg() {
        return accReg;
    }

    /**
     * @param accReg the accReg to set
     */
    public void setAccReg(String accReg) {
        this.accReg = accReg;
    }

    /**
     * @return the numord
     */
    public String getNumord() {
        return numord;
    }

    /**
     * @param numord the numord to set
     */
    public void setNumord(String numord) {
        this.numord = numord;
    }

    /**
     * @return the reqrefnum
     */
    public String getReqrefnum() {
        return reqrefnum;
    }

    /**
     * @param reqrefnum the reqrefnum to set
     */
    public void setReqrefnum(String reqrefnum) {
        this.reqrefnum = reqrefnum;
    }

    /**
     * @return the idNegozio
     */
    public String getIdNegozio() {
        return idNegozio;
    }

    /**
     * @param idNegozio the idNegozio to set
     */
    public void setIdNegozio(String idNegozio) {
        this.idNegozio = idNegozio;
    }

    /**
     * @return the importo
     */
    public String getImporto() {
        return importo;
    }

    /**
     * @param importo the importo to set
     */
    public void setImporto(String importo) {
        this.importo = importo;
    }

    /**
     * @return the esitoTrans
     */
    public String getEsitoTrans() {
        return esitoTrans;
    }

    /**
     * @param esitoTrans the esitoTrans to set
     */
    public void setEsitoTrans(String esitoTrans) {
        this.esitoTrans = esitoTrans;
    }

    /**
     * @return the esitoTrans99
     */
    public String getEsitoTrans99() {
        return esitoTrans99;
    }

    /**
     * @param esitoTrans99 the esitoTrans99 to set
     */
    public void setEsitoTrans99(String esitoTrans99) {
        this.esitoTrans99 = esitoTrans99;
    }

    /**
     * @return the esitoMultiTrans
     */
    public String getEsitoMultiTrans() {
        return esitoMultiTrans;
    }

    /**
     * @param esitoMultiTrans the esitoMultiTrans to set
     */
    public void setEsitoMultiTrans(String esitoMultiTrans) {
        this.esitoMultiTrans = esitoMultiTrans;
    }

    /**
     * @return the idWarranty
     */
    public Integer getIdWarranty() {
        return idWarranty;
    }

    /**
     * @param idWarranty the idWarranty to set
     */
    public void setIdWarranty(Integer idWarranty) {
        this.idWarranty = idWarranty;
    }

    /**
     * @return the orderDescription
     */
    public String getOrderDescription() {
        return orderDescription;
    }

    /**
     * @param orderDescription the orderDescription to set
     */
    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }


}
