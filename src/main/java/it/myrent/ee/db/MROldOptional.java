/*
 * Optional.java
 *
 * Created on 03 ianuarie 2007, 15:14
 *
 */
package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.db.PersistentInstance;

import java.util.ResourceBundle;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author jamess
 */
public class MROldOptional implements Comparable, PersistentInstance {

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    /**
     * Creates a new instance of MROldOptional
     */
    public MROldOptional() {
    }

    private Integer id;
    private String codice;
    private String descrizione;
    private String otaCode;
    private MROldPianoDeiConti contoContabile;
    private MROldPianoDeiConti contoContabileFranchigia;
    private Boolean appListino;
    private Boolean appGruppo;
    private Boolean importoGiornaliero;
    private Boolean importoFisso;
    private Boolean importoKm;
    private Boolean importoPercentuale;
    private Boolean importoDaNegoziare;
    private Integer addebitoMassimo;
    private Integer addebitoMinimo;
    private Double importo;
    private Boolean moltiplicabile;
    private Double importoDanno;
    private Boolean soggIvaImporto;
    private Boolean soggIvaDanno;

    // Tipo optional assicurazione
    private Boolean assicurazione;
    private Boolean pai;
    private Boolean danni;
    private Boolean furto;
    private Boolean franchigia;
    private Boolean riduzione;
    private Double percentuale;

    // Tipo optional km
    private Boolean km;
    private Boolean kmIllimitati;
    private Boolean kmExtra;
    private Integer kmInclusi;

    // Tipo optional carburante extra
    private Boolean carburante;

    // Tipo optional lavaggio
    private Boolean lavaggio;

    // Tipo optional extra eta'
    private Boolean supplementoEta;
    private Integer etaMinima;
    private Integer etaMassima;

    // Tipo optional patente
    private Boolean anniPatente;
    private Integer anniMassimo;

    private Boolean guidatoreAggiuntivo;

    private Boolean fuoriOrario;
    private Integer oreMassimo;

    private Boolean oneriAeroportuali;

    private Boolean oneriFeroviari;

    private Boolean roadTax;

    private Boolean oneWay;

    private Boolean deliveryCollection;

    private Boolean annullamento;

    private Boolean noShow;

    private Boolean accessorio;

    private Boolean tempoExtra;
    private Boolean notteExtra;
    private Boolean mezzaGiornata;
    private Boolean giornataRidotta;
    private Boolean giornoFestivo;

    private Boolean altro;

    //booking online
    private Boolean bookingOnline;
    private Boolean pienoPrepagato;

    private Boolean depositoCauzionale;

    private MROldOptionalClass optionalClass;

    //Andrea
    private String applicability;

    private String webApplicability;
    private Boolean isVirtual;
    private String includeOptionals;
    private String excludeOptionals;
    private Boolean isRateOptional;

    //madhvendra for oracle
    private Boolean isUseRate;
    private MROldListino rateCommissionId;

    public Boolean getIsUseRate() {
        return this.isUseRate;
    }

    public void setIsUseRate(Boolean isUseRate) {
        this.isUseRate = isUseRate;
    }

    public MROldListino getRateCommissionId() {
        return rateCommissionId;
    }

    public void setRateCommissionId(MROldListino rateCommissionId) {
        this.rateCommissionId = rateCommissionId;
    }



    /// harry
    private Boolean isDisabled;
    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getStringTipoImporto() {
        if (Boolean.TRUE.equals(getImportoFisso())) {
            return bundle.getString("Optional.TipoImportoFisso");
        } else if (Boolean.TRUE.equals(getImportoGiornaliero())) {
            return bundle.getString("Optional.TipoImportoGiornaliero");
        } else if (Boolean.TRUE.equals(getImportoPercentuale())) {
            return bundle.getString("Optional.TipoImportoPercentuale");
        } else if (Boolean.TRUE.equals(getImportoKm())) {
            return bundle.getString("Optional.TipoImportoAKm");
        } else if (Boolean.TRUE.equals(getImportoDaNegoziare())) {
            return bundle.getString("Optional.TipoImportoDaNegoziare");
        }
        return null;
    }

    public String getStringImporto() {
        if (Boolean.TRUE.equals(getImportoFisso())) {
            return new String();
        } else if (Boolean.TRUE.equals(getImportoGiornaliero())) {
            return bundle.getString("Optional.ImportoAlGiorno");
        } else if (Boolean.TRUE.equals(getImportoPercentuale())) {
            return bundle.getString("Optional.ImportoPercentualeNolo");
        } else if (Boolean.TRUE.equals(getImportoKm())) {
            return bundle.getString("Optional.ImportoAKm");
        } else if (Boolean.TRUE.equals(getImportoDaNegoziare())) {
            return bundle.getString("Optional.ImportoDaNegoziare");
        }
        return null;
    }

    public String getUnitaMisura() {
        if (Boolean.TRUE.equals(getImportoFisso())) {
            return new String();
        } else if (Boolean.TRUE.equals(getImportoGiornaliero())) {
            return bundle.getString("Optional.UnitaMisuraGG");
        } else if (Boolean.TRUE.equals(getImportoPercentuale())) {
            return bundle.getString("Optional.UnitaMisuraPercentualeNolo");
        } else if (Boolean.TRUE.equals(getImportoKm())) {
            return bundle.getString("Optional.UnitaMisuraKM");
        } else if (Boolean.TRUE.equals(getImportoDaNegoziare())) {
            return new String();
        }
        return null;
    }

    public String getStringTipoOptional() {
        if (Boolean.TRUE.equals(getAppListino())) {
            return bundle.getString("Optional.Listino");
        } else if (Boolean.TRUE.equals(getAppGruppo())) {
            return bundle.getString("Optional.Gruppo");
        }
        return null;
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof MROldOptional) {
            return new EqualsBuilder().append(getId(), ((MROldOptional) obj).getId()).isEquals();
        } else {
            return false;
        }
    }

    public String toString() {
        return getDescrizione() != null ? getDescrizione() : new String();
    }

    public int compareTo(Object o) {
        if (o != null && o instanceof MROldOptional) {
            return new CompareToBuilder().append(getDescrizione(), ((MROldOptional) o).getDescrizione()).toComparison();
        } else {
            return new CompareToBuilder().append(getDescrizione(), null).toComparison();
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Boolean getAppListino() {
        return appListino;
    }

    public void setAppListino(Boolean appListino) {
        this.appListino = appListino;
    }

    public Boolean getAppGruppo() {
        return appGruppo;
    }

    public void setAppGruppo(Boolean appGruppo) {
        this.appGruppo = appGruppo;
    }

    public Boolean getImportoGiornaliero() {
        return importoGiornaliero;
    }

    public void setImportoGiornaliero(Boolean importoGiornaliero) {
        this.importoGiornaliero = importoGiornaliero;
    }

    public Boolean getImportoFisso() {
        return importoFisso;
    }

    public void setImportoFisso(Boolean importoFisso) {
        this.importoFisso = importoFisso;
    }

    public Boolean getImportoKm() {
        return importoKm;
    }

    public void setImportoKm(Boolean importoKm) {
        this.importoKm = importoKm;
    }

    public Boolean getImportoPercentuale() {
        return importoPercentuale;
    }

    public void setImportoPercentuale(Boolean importoPercentuale) {
        this.importoPercentuale = importoPercentuale;
    }

    public Boolean getImportoDaNegoziare() {
        return importoDaNegoziare;
    }

    public void setImportoDaNegoziare(Boolean importoDaNegoziare) {
        this.importoDaNegoziare = importoDaNegoziare;
    }

    public Integer getAddebitoMassimo() {
        return addebitoMassimo;
    }

    public void setAddebitoMassimo(Integer addebitoMassimo) {
        this.addebitoMassimo = addebitoMassimo;
    }

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }

    public Boolean getAssicurazione() {
        return assicurazione;
    }

    public void setAssicurazione(Boolean assicurazione) {
        this.assicurazione = assicurazione;
    }

    public Boolean getPai() {
        return pai;
    }

    public void setPai(Boolean pai) {
        this.pai = pai;
    }

    public Boolean getDanni() {
        return danni;
    }

    public void setDanni(Boolean danni) {
        this.danni = danni;
    }

    public Boolean getFurto() {
        return furto;
    }

    public void setFurto(Boolean furto) {
        this.furto = furto;
    }

    public Boolean getFranchigia() {
        return franchigia;
    }

    public void setFranchigia(Boolean franchigia) {
        this.franchigia = franchigia;
    }

    public Boolean getRiduzione() {
        return riduzione;
    }

    public void setRiduzione(Boolean riduzione) {
        this.riduzione = riduzione;
    }

    public Double getPercentuale() {
        return percentuale;
    }

    public void setPercentuale(Double percentuale) {
        this.percentuale = percentuale;
    }

    public Boolean getKm() {
        return km;
    }

    public void setKm(Boolean km) {
        this.km = km;
    }

    public Boolean getKmIllimitati() {
        return kmIllimitati;
    }

    public void setKmIllimitati(Boolean kmIllimitati) {
        this.kmIllimitati = kmIllimitati;
    }

    public Integer getKmInclusi() {
        return kmInclusi;
    }

    public void setKmInclusi(Integer kmInclusi) {
        this.kmInclusi = kmInclusi;
    }

    public Boolean getKmExtra() {
        return kmExtra;
    }

    public void setKmExtra(Boolean kmExtra) {
        this.kmExtra = kmExtra;
    }

    public Boolean getCarburante() {
        return carburante;
    }

    public void setCarburante(Boolean carburante) {
        this.carburante = carburante;
    }

    public Boolean getLavaggio() {
        return lavaggio;
    }

    public void setLavaggio(Boolean lavaggio) {
        this.lavaggio = lavaggio;
    }

    public Boolean getSupplementoEta() {
        return supplementoEta;
    }

    public void setSupplementoEta(Boolean supplementoEta) {
        this.supplementoEta = supplementoEta;
    }

    public Integer getEtaMinima() {
        return etaMinima;
    }

    public void setEtaMinima(Integer etaMinima) {
        this.etaMinima = etaMinima;
    }

    public Integer getEtaMassima() {
        return etaMassima;
    }

    public void setEtaMassima(Integer etaMassima) {
        this.etaMassima = etaMassima;
    }

    public Boolean getAnniPatente() {
        return anniPatente;
    }

    public void setAnniPatente(Boolean anniPatente) {
        this.anniPatente = anniPatente;
    }

    public Integer getAnniMassimo() {
        return anniMassimo;
    }

    public void setAnniMassimo(Integer anniMinimo) {
        this.anniMassimo = anniMassimo;
    }

    public Boolean getGuidatoreAggiuntivo() {
        return guidatoreAggiuntivo;
    }

    public void setGuidatoreAggiuntivo(Boolean guidatoreAggiuntivo) {
        this.guidatoreAggiuntivo = guidatoreAggiuntivo;
    }

    public Boolean getFuoriOrario() {
        return fuoriOrario;
    }

    public void setFuoriOrario(Boolean fuoriOrario) {
        this.fuoriOrario = fuoriOrario;
    }

    public Integer getOreMassimo() {
        return oreMassimo;
    }

    public void setOreMassimo(Integer oreMassimo) {
        this.oreMassimo = oreMassimo;
    }

    public Boolean getOneriAeroportuali() {
        return oneriAeroportuali;
    }

    public void setOneriAeroportuali(Boolean oneriAeroportuali) {
        this.oneriAeroportuali = oneriAeroportuali;
    }

    public Boolean getOneriFeroviari() {
        return oneriFeroviari;
    }

    public void setOneriFeroviari(Boolean oneriFeroviari) {
        this.oneriFeroviari = oneriFeroviari;
    }

    public Boolean getOneWay() {
        return oneWay;
    }

    public void setOneWay(Boolean oneWay) {
        this.oneWay = oneWay;
    }

    public Boolean getDeliveryCollection() {
        return deliveryCollection;
    }

    public void setDeliveryCollection(Boolean deliveryCollection) {
        this.deliveryCollection = deliveryCollection;
    }

    public Boolean getAnnullamento() {
        return annullamento;
    }

    public void setAnnullamento(Boolean annullamento) {
        this.annullamento = annullamento;
    }

    public Boolean getNoShow() {
        return noShow;
    }

    public void setNoShow(Boolean noShow) {
        this.noShow = noShow;
    }

    public Boolean getAltro() {
        return altro;
    }

    public void setAltro(Boolean altro) {
        this.altro = altro;
    }

    public Double getImportoDanno() {
        return importoDanno;
    }

    public void setImportoDanno(Double importoDanno) {
        this.importoDanno = importoDanno;
    }

    public Boolean getAccessorio() {
        return accessorio;
    }

    public void setAccessorio(Boolean accessorio) {
        this.accessorio = accessorio;
    }

    public Boolean getSoggIvaImporto() {
        return soggIvaImporto;
    }

    public void setSoggIvaImporto(Boolean ivaImporto) {
        this.soggIvaImporto = ivaImporto;
    }

    public Boolean getSoggIvaDanno() {
        return soggIvaDanno;
    }

    public void setSoggIvaDanno(Boolean ivaDanno) {
        this.soggIvaDanno = ivaDanno;
    }

    public MROldPianoDeiConti getContoContabile() {
        return contoContabile;
    }

    public void setContoContabile(MROldPianoDeiConti contoContabile) {
        this.contoContabile = contoContabile;
    }

    public MROldPianoDeiConti getContoContabileFranchigia() {
        return contoContabileFranchigia;
    }

    public void setContoContabileFranchigia(MROldPianoDeiConti contoContabileFranchigia) {
        this.contoContabileFranchigia = contoContabileFranchigia;
    }

    public Boolean getRoadTax() {
        return roadTax;
    }

    public void setRoadTax(Boolean roadTax) {
        this.roadTax = roadTax;
    }

    public Boolean getTempoExtra() {
        return tempoExtra;
    }

    public void setTempoExtra(Boolean tempoExtra) {
        this.tempoExtra = tempoExtra;
    }

    public Boolean getNotteExtra() {
        return notteExtra;
    }

    public void setNotteExtra(Boolean notteExtra) {
        this.notteExtra = notteExtra;
    }

    public Boolean getMezzaGiornata() {
        return mezzaGiornata;
    }

    public void setMezzaGiornata(Boolean mezzaGiornata) {
        this.mezzaGiornata = mezzaGiornata;
    }

    public Boolean getGiornataRidotta() {
        return giornataRidotta;
    }

    public void setGiornataRidotta(Boolean giornataRidotta) {
        this.giornataRidotta = giornataRidotta;
    }

    public Boolean getMoltiplicabile() {
        return moltiplicabile;
    }

    public void setMoltiplicabile(Boolean moltiplicabile) {
        this.moltiplicabile = moltiplicabile;
    }

    public Boolean getGiornoFestivo() {
        return giornoFestivo;
    }

    public void setGiornoFestivo(Boolean giornoFestivo) {
        this.giornoFestivo = giornoFestivo;
    }

    public String getOtaCode() {
        return otaCode;
    }

    public void setOtaCode(String otaCode) {
        this.otaCode = otaCode;
    }

    public Boolean getBookingOnline() {
        return bookingOnline;
    }

    public void setBookingOnline(Boolean bookingOnline) {
        this.bookingOnline = bookingOnline;
    }

    public Integer getAddebitoMinimo() {
        return addebitoMinimo;
    }

    public void setAddebitoMinimo(Integer addebitoMinimo) {
        this.addebitoMinimo = addebitoMinimo;
    }

    public Boolean getPienoPrepagato() {
        return pienoPrepagato;
    }

    public void setPienoPrepagato(Boolean pienoPrepagato) {
        this.pienoPrepagato = pienoPrepagato;
    }

    public void setDepositoCauzionale(Boolean depositoCauzionale) {
        this.depositoCauzionale = depositoCauzionale;
    }

    public Boolean getDepositoCauzionale() {
        return depositoCauzionale;
    }

    /**
     * @return the optionalClass
     */
    public MROldOptionalClass getOptionalClass() {
        return optionalClass;
    }

    /**
     * @param optionalClass the optionalClass to set
     */
    public void setOptionalClass(MROldOptionalClass optionalClass) {
        this.optionalClass = optionalClass;
    }

    //Andrea
    public String getApplicability() {
        return applicability;
    }

    public void setApplicability(String applicability) {
        this.applicability = applicability;
    }

    public Boolean getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(Boolean isVirtual) {
        this.isVirtual = isVirtual;
    }

    public String getIncludeOptionals() {
        return includeOptionals;
    }

    public void setIncludeOptionals(String includeOptionals) {
        this.includeOptionals = includeOptionals;
    }

    public String getExcludeOptionals() {
        return excludeOptionals;
    }

    public void setExcludeOptionals(String excludeOptionals) {
        this.excludeOptionals = excludeOptionals;
    }

    public Boolean getIsRateOptional() {
        return isRateOptional;
    }

    public void setIsRateOptional(Boolean isRateOptional) {
        this.isRateOptional = isRateOptional;
    }

    public String getWebApplicability() {
        return webApplicability;
    }

    public void setWebApplicability(String webApplicability) {
        this.webApplicability = webApplicability;
    }
}
