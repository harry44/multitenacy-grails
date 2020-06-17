package it.myrent.ee.db;

import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/** @author Hibernate CodeGenerator */
public class MROldMulta implements it.aessepi.utils.db.PersistentInstance{

    /** identifier field */
    private Integer id;
    private MROldMovimentoAuto movimento;
    private MROldMultaEnte enteEmettente;
    private MROldProprietarioVeicolo proprietario;

    private String guidatore;
    
    private Date dataNotificaVerbale;
    private Integer numeroProtocollo;
    
    private Date dataEmissioneVerbale;
    private String numeroVerbale;
    
    private String nomeEnteEmettente;
    private String cittaEnteEmettente;
    private String indirizzoEnteEmettente;
    
    private String articoloViolato;
    private Date dataInfrazione;
    private Date oraInfrazione;
    private String descrizioneInfrazione;
    
    private Double importoSpeseGestione;
    private Double importoMulta;
    
    private String nomeContatto;
    private String telefonoContatto;
    
    private String cittaEmissioneDocumento;
    private Date dataEmissioneDocumento;
    private String firmaRiga1;
    private String firmaRiga2;
    private String annotazioni;

    private Date dataScadenza;

    private Date uploadDate;

    private String raccomandata;
    private String agenteAccertatore;
    //Madhvendra (For Postgres)
    private Boolean isFineProcessed;
    private Boolean isReadyToSend;
    private Boolean isSent;
    private Rent2Rent r2r;
    
    /** full constructor */
    public MROldMulta() {
        //this.idCliente = idCliente;
        
    }
    
    public boolean equals(Object other) {
        if ( !(other instanceof MROldConducenti) ) return false;
        MROldConducenti castOther = (MROldConducenti) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }
    public Boolean getIsReadyToSend()
    {
        return isReadyToSend;
    }
    public void setIsReadyToSend(Boolean ready)
    {
        this.isReadyToSend=ready;
    }
    public Boolean getIsSent()
    {
        return isSent;
    }
    public void setIsSent(Boolean sent)
    {
        this.isSent=sent;
    }
    public Boolean getIsFineProcessed()
    {
        return isFineProcessed;
    }
    public void setIsFineProcessed(Boolean isFineProcessed)
    {
        this.isFineProcessed=isFineProcessed;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMovimento(MROldMovimentoAuto movimento) {
        this.movimento = movimento;
    }
    
    public MROldMovimentoAuto getMovimento() {
        return movimento;
    }

    public MROldMultaEnte getEnteEmettente() {
        return enteEmettente;
    }

    public void setEnteEmettente(MROldMultaEnte enteEmettente) {
        this.enteEmettente = enteEmettente;
    }
    
    public void setDataNotificaVerbale(Date dataNotificaVerbale) {
        this.dataNotificaVerbale = dataNotificaVerbale;
    }
    
    public Date getDataNotificaVerbale() {
        return dataNotificaVerbale;
    }

    public Integer getNumeroProtocollo() {
        return numeroProtocollo;
    }

    public void setNumeroProtocollo(Integer numeroProtocollo) {
        this.numeroProtocollo = numeroProtocollo;
    }
    
    public void setDataEmissioneVerbale(Date dataEmissioneVerbale) {
        this.dataEmissioneVerbale = dataEmissioneVerbale;
    }
    
    public Date getDataEmissioneVerbale() {
        return dataEmissioneVerbale;
    }
    
    public void setNumeroVerbale(String numeroVerbale) {
        this.numeroVerbale = numeroVerbale;
    }
    
    public String getNumeroVerbale() {
        return numeroVerbale;
    }
    
    public void setNomeEnteEmettente(String nomeEnteEmettente) {
        this.nomeEnteEmettente = nomeEnteEmettente;
    }
    
    public String getNomeEnteEmettente() {
        return nomeEnteEmettente;
    }
    
    public void setCittaEnteEmettente(String cittaEnteEmettente) {
        this.cittaEnteEmettente = cittaEnteEmettente;
    }
    
    public String getCittaEnteEmettente() {
        return cittaEnteEmettente;
    }

    public String getIndirizzoEnteEmettente() {
        return indirizzoEnteEmettente;
    }

    public void setIndirizzoEnteEmettente(String indirizzoEnteEmettente) {
        this.indirizzoEnteEmettente = indirizzoEnteEmettente;
    }
    
    public void setArticoloViolato(String articoloViolato) {
        this.articoloViolato = articoloViolato;
    }
    
    public String getArticoloViolato() {
        return articoloViolato;
    }
    
    public void setDataInfrazione(Date dataInfrazione) {
        this.dataInfrazione = dataInfrazione;
    }
    
    public Date getDataInfrazione() {
        return dataInfrazione;
    }
    
    public void setOraInfrazione(Date oraInfrazione) {
        this.oraInfrazione = oraInfrazione;
    }
    
    public Date getOraInfrazione() {
        return oraInfrazione;
    }
    
    public void setDescrizioneInfrazione(String descrizioneInfrazione) {
        this.descrizioneInfrazione = descrizioneInfrazione;
    }
    
    public String getDescrizioneInfrazione() {
        return descrizioneInfrazione;
    }
    
    public void setImportoSpeseGestione(Double importoSpeseGestione){
        this.importoSpeseGestione = importoSpeseGestione;
    }
    
    public Double getImportoSpeseGestione(){
        return importoSpeseGestione;
    }

    public void setImportoMulta(Double importoMulta) {
        this.importoMulta = importoMulta;
    }

    public Double getImportoMulta() {
        return importoMulta;
    }

    public void setNomeContatto(String nomeContatto){
        this.nomeContatto = nomeContatto;
    }
    
    public String getNomeContatto(){
        return nomeContatto;
    }
    
    public void setTelefonoContatto(String telefonoContatto){
        this.telefonoContatto = telefonoContatto;
    }
    
    public String getTelefonoContatto(){
        return telefonoContatto;
    }
    
    
  public void setCittaEmissioneDocumento(String cittaEmissioneDocumento){
      this.cittaEmissioneDocumento = cittaEmissioneDocumento;
  }
  
   public String getCittaEmissioneDocumento(){
      return cittaEmissioneDocumento;
  }
    
     public void setDataEmissioneDocumento(Date dataEmissioneDocumento){
      this.dataEmissioneDocumento = dataEmissioneDocumento;
  }
  
   public Date getDataEmissioneDocumento(){
      return dataEmissioneDocumento;
  }
   
   
    public void setFirmaRiga1(String firmaRiga1){
        this.firmaRiga1 = firmaRiga1;
    }
    
    public String getFirmaRiga1(){
        return firmaRiga1;
    }
    
     public void setFirmaRiga2(String firmaRiga2){
        this.firmaRiga2 = firmaRiga2;
    }
    
    public String getFirmaRiga2(){
        return firmaRiga2;
    }
    
    public void setAnnotazioni(String annotazioni) {
        this.annotazioni = annotazioni;
    }
    
    public String getAnnotazioni() {
        return annotazioni;
    }
    
    public String toString() {
        return new String();
    }

    public String getGuidatore() {
        return guidatore;
    }

    public void setGuidatore(String guidatore) {
        this.guidatore = guidatore;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    /**
     * @return the proprietario
     */
    public MROldProprietarioVeicolo getProprietario() {
        return proprietario;
    }

    /**
     * @param proprietario the proprietario to set
     */
    public void setProprietario(MROldProprietarioVeicolo proprietario) {
        this.proprietario = proprietario;
    }

    public String getRaccomandata() {
        return raccomandata;
    }

    public void setRaccomandata(String raccomandata) {
        this.raccomandata = raccomandata;
    }

    public String getAgenteAccertatore() {
        return agenteAccertatore;
    }

    public void setAgenteAccertatore(String agenteAccertatore) {
        this.agenteAccertatore = agenteAccertatore;
    }

    public Rent2Rent getR2r() {
        return r2r;
    }

    public void setR2r(Rent2Rent r2r) {
        this.r2r = r2r;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }
}
