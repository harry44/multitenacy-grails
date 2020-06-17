package it.myrent.ee.db;

/**
 * Created by shivangani on 14/02/2019.
 */
public class MROldNexiTransaction {

    private Integer id;
    private String idPrs;
    private String idCol;
    private String codAbi;
    private String esercenteSia;
    private String stabilimentoSia;
    private String numeroCassa;
    private String idTerminale;
    private String numeroTransazione;
    private String dataOra;
    private String dataTransazione;
    private String dataValuta;
    private String codAutorizzazione;
    private String segno;
    private String divisa;
    private Double importo;
    private String esito;
    private String descrizioneEsito;
    private String codCompagnia;
    private String desCompagnia;
    private String panCarta;
    private String codiceConvenzione;
    private String tipoOperazione;
    private String abiCarta;
    private String contatorePos;
    private Boolean autorizzazioneSelfService;
    private String tipoTransazione;
    private String rrn;
    private String oraTransazione;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOraTransazione() {
        return oraTransazione;
    }

    public void setOraTransazione(String oraTransazione) {
        this.oraTransazione = oraTransazione;
    }

    public String getIdPrs() {
        return idPrs;
    }

    public void setIdPrs(String idPrs) {
        this.idPrs = idPrs;
    }

    public String getIdCol() {
        return idCol;
    }

    public void setIdCol(String idCol) {
        this.idCol = idCol;
    }

    public String getCodAbi() {
        return codAbi;
    }

    @Override
    public String toString() {
        return "Cod Auth: "+codAutorizzazione +", PAN: "+panCarta+ ", IMPORTO: "+importo+" "+divisa;
    }

    public void setCodAbi(String codAbi) {
        this.codAbi = codAbi;
    }

    public String getEsercenteSia() {
        return esercenteSia;
    }

    public void setEsercenteSia(String esercenteSia) {
        this.esercenteSia = esercenteSia;
    }

    public String getStabilimentoSia() {
        return stabilimentoSia;
    }

    public void setStabilimentoSia(String stabilimentoSia) {
        this.stabilimentoSia = stabilimentoSia;
    }

    public String getNumeroCassa() {
        return numeroCassa;
    }

    public void setNumeroCassa(String numeroCassa) {
        this.numeroCassa = numeroCassa;
    }

    public String getIdTerminale() {
        return idTerminale;
    }

    public void setIdTerminale(String idTerminale) {
        this.idTerminale = idTerminale;
    }

    public String getNumeroTransazione() {
        return numeroTransazione;
    }

    public void setNumeroTransazione(String numeroTransazione) {
        this.numeroTransazione = numeroTransazione;
    }

    public String getDataOra() {
        return dataOra;
    }

    public void setDataOra(String dataOra) {
        this.dataOra = dataOra;
    }

    public String getDataTransazione() {
        return dataTransazione;
    }

    public void setDataTransazione(String dataTransazione) {
        this.dataTransazione = dataTransazione;
    }

    public String getDataValuta() {
        return dataValuta;
    }

    public void setDataValuta(String dataValuta) {
        this.dataValuta = dataValuta;
    }

    public String getCodAutorizzazione() {
        return codAutorizzazione;
    }

    public void setCodAutorizzazione(String codAutorizzazione) {
        this.codAutorizzazione = codAutorizzazione;
    }

    public String getSegno() {
        return segno;
    }

    public void setSegno(String segno) {
        this.segno = segno;
    }

    public String getDivisa() {
        return divisa;
    }

    public void setDivisa(String divisa) {
        this.divisa = divisa;
    }

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }

    public String getEsito() {
        return esito;
    }

    public void setEsito(String esito) {
        this.esito = esito;
    }

    public String getDescrizioneEsito() {
        return descrizioneEsito;
    }

    public void setDescrizioneEsito(String descrizioneEsito) {
        this.descrizioneEsito = descrizioneEsito;
    }

    public String getCodCompagnia() {
        return codCompagnia;
    }

    public void setCodCompagnia(String codCompagnia) {
        this.codCompagnia = codCompagnia;
    }

    public String getDesCompagnia() {
        return desCompagnia;
    }

    public void setDesCompagnia(String desCompagnia) {
        this.desCompagnia = desCompagnia;
    }

    public String getPanCarta() {
        return panCarta;
    }

    public void setPanCarta(String panCarta) {
        this.panCarta = panCarta;
    }

    public String getCodiceConvenzione() {
        return codiceConvenzione;
    }

    public void setCodiceConvenzione(String codiceConvenzione) {
        this.codiceConvenzione = codiceConvenzione;
    }

    public String getTipoOperazione() {
        return tipoOperazione;
    }

    public void setTipoOperazione(String tipoOperazione) {
        this.tipoOperazione = tipoOperazione;
    }

    public String getAbiCarta() {
        return abiCarta;
    }

    public void setAbiCarta(String abiCarta) {
        this.abiCarta = abiCarta;
    }

    public String getContatorePos() {
        return contatorePos;
    }

    public void setContatorePos(String contatorePos) {
        this.contatorePos = contatorePos;
    }

    public Boolean getAutorizzazioneSelfService() {
        return autorizzazioneSelfService;
    }

    public void setAutorizzazioneSelfService(Boolean autorizzazioneSelfService) {
        this.autorizzazioneSelfService = autorizzazioneSelfService;
    }

    public String getTipoTransazione() {
        return tipoTransazione;
    }

    public void setTipoTransazione(String tipoTransazione) {
        this.tipoTransazione = tipoTransazione;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }
}
