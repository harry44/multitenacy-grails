package it.myrent.ee.db;
import it.aessepi.utils.BundleUtils;
import java.util.Calendar;
import java.util.Date;

import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Set;

/** @author Hibernate CodeGenerator */
public class MROldParcoVeicoli implements it.aessepi.utils.db.PersistentInstance, Comparable, Loggable,Documentable {

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
    /** identifier field */
    private Integer id;
    private String marca;
    private String modello;
    private String versione;
    private String targa;
    private String extarga;
    private Date dataImmatricolazione;
    private String telaio;
    private String colore;
    private Integer portataUtile;
    private Integer numeroPosti;
    private Double valoreVeicolo;
    private Integer idTariffa;
    private Integer km;
    private String promemoria;
    private MROldProprietarioVeicolo proprietario;
    private Boolean abilitato;
    private Boolean impegnato;
    private Integer capacitaSerbatoio;
    private Integer livelloCombustibile;
    private Boolean pulita;
    private String parcheggio;
    private MROldSede sede;
    private Integer sedeIntrarent;
    private MROldCarburante carburante;
    private MROldAffiliato affiliato;
    private MROldGruppo gruppo;
    private String venditore;
    private Double valoreAcquisto;
    private Date dataAcquisto;
    private String noteAcquisto;
    private String acquirente;
    private Double valoreVendita;
    private Date dataVendita;
    private String noteVendita;
    private MROldDocumentoFiscale fattura;
    private Date dataScadenzaContratto;
    private Date dataProroga1;
    private Date dataProroga2;
    //private Date dataDisponibilita;
    private String codiceVeicolo;
    private String cilindrata;
    private Set danni;
    private Set movimenti;
    private Set disponibilita;
    private Set documenti;
    private Boolean isBulkhead;
    private Boolean isPowerGridAttack;
    private Boolean isSuitableForAbroad;
    private String voltage;
    private MROldVehicleAssistance vehicleAssistance;
//add new 
    private MROldVehicleDetail vehicleDetail;
    //Madhvendra (for Postgres)

    private BookingOnlineStatus bookingOnlineStatus;
    private Set<MROldDanno> dannipic;
    private String operatorName;
    private Boolean isRentToRent=false;
    private Double  amountGoodToBillForR2R;
    private Double  amountServiceToBillForR2R;
    private Double  amountToBillForR2R;
    private Boolean isPromoCar;
    private String pneumatici;
    private String misuraPneumatici;
    private String carrozzeria;
    private String ruotaDiScorta;

    private MROldVehicleUsage vehicleusage;
    private Boolean isAntitheft;

    public Boolean getIsRentToRent() {
        return isRentToRent;
    }

    public void setIsRentToRent(Boolean isRentToRent) {
        this.isRentToRent = isRentToRent;
    }

    public Double getAmountGoodToBillForR2R() {
        return amountGoodToBillForR2R;
    }

    public void setAmountGoodToBillForR2R(Double amountGoodToBillForR2R) {
        this.amountGoodToBillForR2R = amountGoodToBillForR2R;
    }

    public Double getAmountServiceToBillForR2R() {
        return amountServiceToBillForR2R;
    }

    public void setAmountServiceToBillForR2R(Double amountServiceToBillForR2R) {
        this.amountServiceToBillForR2R = amountServiceToBillForR2R;
    }

    public Double getAmountToBillForR2R() {
        return amountToBillForR2R;
    }

    public void setAmountToBillForR2R(Double amountToBillForR2R) {
        this.amountToBillForR2R = amountToBillForR2R;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Set<MROldDanno> getDannipic() {
        return dannipic;
    }

    public void setDannipic(Set<MROldDanno> dannipic) {
        this.dannipic = dannipic;
    }
    public MROldVehicleDetail getVehicleDetail() {
        return vehicleDetail;
    }
    /**
     * @return the bookingOnlineStatus
     */
    public BookingOnlineStatus getBookingOnlineStatus() {
        return bookingOnlineStatus;
    }

    /**
     * @param bookingOnlineStatus the bookingOnlineStatus to set
     */
    public void setBookingOnlineStatus(BookingOnlineStatus bookingOnlineStatus) {
        this.bookingOnlineStatus = bookingOnlineStatus;
    }

    public void setVehicleDetail(MROldVehicleDetail vehicleDetail) {
        this.vehicleDetail = vehicleDetail;
    }


    public Boolean getIsPromoCar() {
        return isPromoCar;
    }

    public void setIsPromoCar(Boolean isPromoCar) {
        this.isPromoCar = isPromoCar;
    }

    /**
     *Costruttore per le query dinamiche del CalendarioAuto
     */
    public MROldParcoVeicoli(
            Integer id,
            String targa,
            String marca,
            String modello,
            String versione,
            String parcheggio,
            Integer km,
            Integer livelloCombustibile,
            Integer capacitaSerbatoio,
            Boolean pulita,
            Boolean impegnato,
            Date dataAcquisto,
            Date dataVendita,
            Date dataScadenzaContratto,
            Date dataProroga1,
            Date dataProroga2,
            Integer idGruppo,
            String codiceNazionaleGruppo,
            Integer idAffiliato,
            Boolean tipoAffiliato,
            Integer idSede,
            String codiceSede,
            Integer idAffiliatoSede) {
        setId(id);
        setTarga(targa);
        setMarca(marca);
        setModello(modello);
        setVersione(versione);
        setParcheggio(parcheggio);
        setKm(km);
        setLivelloCombustibile(livelloCombustibile);
        setCapacitaSerbatoio(capacitaSerbatoio);
        setPulita(pulita);
        setImpegnato(impegnato);
        setDataAcquisto(dataAcquisto);
        setDataVendita(dataVendita);
        setDataScadenzaContratto(dataScadenzaContratto);
        setDataProroga1(dataProroga1);
        setDataProroga2(dataProroga2);
        setGruppo(new MROldGruppo(idGruppo, codiceNazionaleGruppo));
        setAffiliato(new MROldAffiliato(idAffiliato, tipoAffiliato));

        setSede(new MROldSede(idSede, codiceSede, null));
        getSede().setAffiliato(new MROldAffiliato(idAffiliatoSede, Boolean.FALSE));
    }

    public MROldParcoVeicoli() {
        setImpegnato(Boolean.FALSE);
        setAbilitato(Boolean.TRUE);
        setPulita(Boolean.TRUE);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarca() {
        return this.marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModello() {
        return this.modello;
    }

    public void setModello(String modello) {
        this.modello = modello;
    }

    public String getVersione() {
        return this.versione;
    }

    public void setVersione(String versione) {
        this.versione = versione;
    }

    public String getTarga() {
        return this.targa;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public String getExtarga() {
        return this.extarga;
    }

    public void setExtarga(String extarga) {
        this.extarga = extarga;
    }

    public Date getDataImmatricolazione() {
        return this.dataImmatricolazione;
    }

    public void setDataImmatricolazione(Date dataImmatricolazione) {
        this.dataImmatricolazione = dataImmatricolazione;
    }

    public String getTelaio() {
        return this.telaio;
    }

    public void setTelaio(String telaio) {
        this.telaio = telaio;
    }

    public String getColore() {
        return this.colore;
    }

    public void setColore(String colore) {
        this.colore = colore;
    }

    public Integer getIdTariffa() {
        return this.idTariffa;
    }

    public void setIdTariffa(Integer idTariffa) {
        this.idTariffa = idTariffa;
    }

    public Integer getKm() {
        return this.km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public String getPromemoria() {
        return this.promemoria;
    }

    public void setPromemoria(String promemoria) {
        this.promemoria = promemoria;
    }

    public void setAbilitato(Boolean abilitato) {
        this.abilitato = abilitato;
    }

    public Boolean getAbilitato() {
        return abilitato;
    }

    public void setCapacitaSerbatoio(Integer capacitaSerbatoio) {
        this.capacitaSerbatoio = capacitaSerbatoio;
    }

    public Integer getCapacitaSerbatoio() {
        return capacitaSerbatoio;
    }

    public void setLivelloCombustibile(Integer livelloCombustibile) {
        this.livelloCombustibile = livelloCombustibile;
    }

    public Integer getLivelloCombustibile() {
        return livelloCombustibile;
    }

    public void setSede(MROldSede sede) {
        this.sede = sede;
    }

    public MROldSede getSede() {
        return sede;
    }

    public MROldProprietarioVeicolo getProprietario() {
        return proprietario;
    }

    public void setProprietario(MROldProprietarioVeicolo proprietario) {
        this.proprietario = proprietario;
    }

    public MROldCarburante getCarburante() {
        return carburante;
    }

    public void setCarburante(MROldCarburante carburante) {
        this.carburante = carburante;
    }

    public String toString() {
        String returnValue = new String();
        //returnValue = id != null ? returnValue + id + " ": returnValue;
        returnValue = targa != null ? returnValue + targa + " " : returnValue; //NOI18N
        returnValue = marca != null ? returnValue + marca : returnValue; //NOI18N
        returnValue = modello != null ? returnValue + " " + modello : returnValue; //NOI18N
        returnValue = versione != null ? returnValue + " " + versione : returnValue; //NOI18N
        return returnValue.trim().equals("") ? "" : returnValue; //NOI18N
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldParcoVeicoli)) {
            return false;
        }
        MROldParcoVeicoli castOther = (MROldParcoVeicoli) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public int compareTo(Object o) {
        if (o == null) {
            return 1; //define null minor than everything
        }
        if (o instanceof MROldParcoVeicoli) {
            MROldParcoVeicoli v = (MROldParcoVeicoli) o;
            if (v.getId() != null && getId() != null) {
                return getId().compareTo(v.getId());
            } else {
                return getId() != null ? 1 : -1;
            }
        }
        return -1;
    }

    public MROldAffiliato getAffiliato() {
        return affiliato;
    }

    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }

    public Integer getPortataUtile() {
        return portataUtile;
    }

    public void setPortataUtile(Integer portataUtile) {
        this.portataUtile = portataUtile;
    }

    public Integer getNumeroPosti() {
        return numeroPosti;
    }

    public void setNumeroPosti(Integer numeroPosti) {
        this.numeroPosti = numeroPosti;
    }

    public Double getValoreVeicolo() {
        return valoreVeicolo;
    }

    public void setValoreVeicolo(Double valoreVeicolo) {
        this.valoreVeicolo = valoreVeicolo;
    }

    public String getVenditore() {
        return venditore;
    }

    public void setVenditore(String venditore) {
        this.venditore = venditore;
    }

    public Double getValoreAcquisto() {
        return valoreAcquisto;
    }

    public void setValoreAcquisto(Double valoreAcquisto) {
        this.valoreAcquisto = valoreAcquisto;
    }

    public Date getDataAcquisto() {
        return dataAcquisto;
    }

    public void setDataAcquisto(Date dataAcquisto) {
        this.dataAcquisto = dataAcquisto;
    }

    public String getNoteAcquisto() {
        return noteAcquisto;
    }

    public void setNoteAcquisto(String noteAcquisto) {
        this.noteAcquisto = noteAcquisto;
    }

    public String getAcquirente() {
        return acquirente;
    }

    public void setAcquirente(String acquirente) {
        this.acquirente = acquirente;
    }

    public Double getValoreVendita() {
        return valoreVendita;
    }

    public void setValoreVendita(Double valoreVendita) {
        this.valoreVendita = valoreVendita;
    }

    public Date getDataVendita() {
        return dataVendita;
    }

    public void setDataVendita(Date dataVendita) {
        this.dataVendita = dataVendita;
    }

    public String getNoteVendita() {
        return noteVendita;
    }

    public void setNoteVendita(String noteVendita) {
        this.noteVendita = noteVendita;
    }

    public MROldDocumentoFiscale getFattura() {
        return fattura;
    }

    public void setFattura(MROldDocumentoFiscale fattura) {
        this.fattura = fattura;
    }

    public MROldGruppo getGruppo() {
        return gruppo;
    }

    public void setGruppo(MROldGruppo gruppo) {
        this.gruppo = gruppo;
    }

    public Date getDataScadenzaContratto() {
        return dataScadenzaContratto;
    }

    public void setDataScadenzaContratto(Date dataScadenzaContratto) {
        this.dataScadenzaContratto = dataScadenzaContratto;
    }

    public Date getDataProroga1() {
        return dataProroga1;
    }

    public void setDataProroga1(Date dataProroga1) {
        this.dataProroga1 = dataProroga1;
    }

    public Date getDataProroga2() {
        return dataProroga2;
    }

    public void setDataProroga2(Date dataProroga2) {
        this.dataProroga2 = dataProroga2;
    }

    public Boolean getPulita() {
        return pulita;
    }

    public void setPulita(Boolean pulita) {
        this.pulita = pulita;
    }

    public String getParcheggio() {
        return parcheggio;
    }

    public void setParcheggio(String parcheggio) {
        this.parcheggio = parcheggio;
    }

    public Boolean getImpegnato() {
        return impegnato;
    }

    public void setImpegnato(Boolean impegnato) {
        this.impegnato = impegnato;
    }

    public Integer getSedeIntrarent() {
        return sedeIntrarent;
    }

    public void setSedeIntrarent(Integer sedeIntrarent) {
        this.sedeIntrarent = sedeIntrarent;
    }

    public String getCilindrata() {
        return cilindrata;
    }

    public void setCilindrata(String cilindrata) {
        this.cilindrata = cilindrata;
    }

    public Set getDanni() {
        return danni;
    }

    public void setDanni(Set danni) {
        this.danni = danni;
    }

    public Set getMovimenti() {
        return movimenti;
    }

    public void setMovimenti(Set movimenti) {
        this.movimenti = movimenti;
    }

    public Set getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(Set disponibilita) {
        this.disponibilita = disponibilita;
    }

    /**
     * The day the vehicle will expire, at 00:00h (first moment ot the day).
     * @return
     */
    public Date fineDisponibilitaDate() {
        Date fineDisponibilita = getDataScadenzaContratto();
        if (getDataProroga1() != null && getDataProroga1().getTime() > fineDisponibilita.getTime()) {
            fineDisponibilita = getDataProroga1();
        }
        if (getDataProroga2() != null && getDataProroga2().getTime() > fineDisponibilita.getTime()) {
            fineDisponibilita = getDataProroga2();
        }
        if (getDataVendita().getTime() < fineDisponibilita.getTime()) {
            fineDisponibilita = getDataVendita();
        }
        return fineDisponibilita;
    }

    /**
     * Same as fineDisponibilitaDate but at 23:59h (end of the day)
     * @return
     */
    public Date fineDisponibilitaTimestamp() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(fineDisponibilitaDate().getTime());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        return calendar.getTime();
    }

    public String getCodiceVeicolo() {
        return codiceVeicolo;
    }

    public void setCodiceVeicolo(String codiceVeicolo) {
        this.codiceVeicolo = codiceVeicolo;
    }

    @Override
    public void setDocumenti(Set documenti) {
        this.documenti = documenti;
    }

    @Override
    public Set getDocumenti() {
        return documenti;
    }

    @Override
    public String getDocumentableName() {
        return getTarga();
    }

    @Override
    public Class getDocumentableClass() {
        return MROldParcoVeicoli.class;
    }

    @Override
    public String getKeywords() {
        return new StringBuffer().append(bundle.getString("ParcoVeicoli.keywords")).
                append(" ").
                append(getMarca()).
                append(" ").
                append(getModello()).
                append(" ").
                append(getTarga()).
                toString();
    }

    /**
     * @return the isBulkhead
     */
    public Boolean getIsBulkhead() {
        return isBulkhead;
    }

    /**
     * @param isBulkhead the isBulkhead to set
     */
    public void setIsBulkhead(Boolean isBulkhead) {
        this.isBulkhead = isBulkhead;
    }

    /**
     * @return the isPowerGridAttack
     */
    public Boolean getIsPowerGridAttack() {
        return isPowerGridAttack;
    }

    /**
     * @param isPowerGridAttack the isPowerGridAttack to set
     */
    public void setIsPowerGridAttack(Boolean isPowerGridAttack) {
        this.isPowerGridAttack = isPowerGridAttack;
    }

    /**
     * @return the isSuitableForAbroad
     */
    public Boolean getIsSuitableForAbroad() {
        return isSuitableForAbroad;
    }

    /**
     * @param isSuitableForAbroad the isSuitableForAbroad to set
     */
    public void setIsSuitableForAbroad(Boolean isSuitableForAbroad) {
        this.isSuitableForAbroad = isSuitableForAbroad;
    }

    /**
     * @return the voltage
     */
    public String getVoltage() {
        return voltage;
    }

    /**
     * @param voltage the voltage to set
     */
    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    /**
     * @return the vehicleAssistance
     */
    public MROldVehicleAssistance getVehicleAssistance() {
        return vehicleAssistance;
    }

    /**
     * @param vehicleAssistance the vehicleAssistance to set
     */
    public void setVehicleAssistance(MROldVehicleAssistance vehicleAssistance) {
        this.vehicleAssistance = vehicleAssistance;
    }

    public MROldVehicleUsage getVehicleusage() {
        return vehicleusage;
    }

    public void setVehicleusage(MROldVehicleUsage vehicleusage) {
        this.vehicleusage = vehicleusage;
    }

    public String[] getLoggableFields() {
        return new String[]{
                "targa", // NOI18N
                "marca", // NOI18N
                "modello", // NOI18N
                "versione", // NOI18N
                "km", // NOI18N
                "livelloCombustibile", // NOI18N
                "capacitaSerbatoio", // NOI18N
                "sede", // NOI18N
                "gruppo", // NOI18N
                "pulita", // NOI18N
                "codiceVeicolo", // NOI18N
                "extarga", // NOI18N
                "dataImmatricolazione", // NOI18N
                "cilindrata", // NOI18N
                "colore", // NOI18N
                "valoreVeicolo", // NOI18N
                "portataUtile", // NOI18N
                "numeroPosti", // NOI18N
                "telaio", // NOI18N
                "parcheggio", // NOI18N
                "isPromoCar", // NOI18N
                "promemoria", // NOI18N
                "vehicleusage", // NOI18N
                "dataAcquisto", //
                "dataScadenzaContratto", // NOI18N
                "dataProroga1", // NOI18N
                "dataProroga2", // NOI18N
                "dataVendita", // NOI18N
                "vehicleDetail", // NOI18N
                "affiliato", // NOI18N
                "carburante", // NOI18N
                "proprietario" // NOI18N
        };
    }

    public String[] getLoggableLabels() {
        return new String[]{
                bundle.getString("ParcoVeicoli.targa"),
                bundle.getString("ParcoVeicoli.marca"),
                bundle.getString("ParcoVeicoli.modello"),
                bundle.getString("ParcoVeicoli.versione"),
                bundle.getString("ParcoVeicoli.km"),
                bundle.getString("ParcoVeicoli.livelloCombustibile"),
                bundle.getString("ParcoVeicoli.capacitaSerbatoio"),
                bundle.getString("ParcoVeicoli.sede"), //
                bundle.getString("ParcoVeicoli.gruppo"),
                bundle.getString("ParcoVeicoli.pulita"),
                bundle.getString("ParcoVeicoli.codiceVeicolo"),
                bundle.getString("ParcoVeicoli.extarga"),
                bundle.getString("ParcoVeicoli.dataImmatricolazione"),
                bundle.getString("ParcoVeicoli.cilindrata"),
                bundle.getString("ParcoVeicoli.colore"),
                bundle.getString("ParcoVeicoli.valoreVeicolo"),
                bundle.getString("ParcoVeicoli.portataUtile"),
                bundle.getString("ParcoVeicoli.numeroPosti"),
                bundle.getString("ParcoVeicoli.telaio"),
                bundle.getString("ParcoVeicoli.parcheggio"),
                bundle.getString("ParcoVeicoli.isPromoCar"),
                bundle.getString("ParcoVeicoli.promemoria"),
                bundle.getString("ParcoVeicoli.vehicleusage"),
                bundle.getString("ParcoVeicoli.dataAcquisto"),
                bundle.getString("ParcoVeicoli.dataScadenzaContratto"),
                bundle.getString("ParcoVeicoli.dataProroga1"),
                bundle.getString("ParcoVeicoli.dataProroga2"),
                bundle.getString("ParcoVeicoli.dataVendita"),
                bundle.getString("ParcoVeicoli.vehicleDetail"),
                bundle.getString("ParcoVeicoli.affiliato"),
                bundle.getString("ParcoVeicoli.carburante"),
                bundle.getString("ParcoVeicoli.proprietario")
        };
    }

    public String getEntityName() {
        return "MROldParcoVeicoli"; // NOI18N
    }

    public Integer getEntityId() {
        return getId();
    }


    public String getPneumatici() {
        return pneumatici;
    }

    public void setPneumatici(String pneumatici) {
        this.pneumatici = pneumatici;
    }

    public String getMisuraPneumatici() {
        return misuraPneumatici;
    }

    public void setMisuraPneumatici(String misuraPneumatici) {
        this.misuraPneumatici = misuraPneumatici;
    }

    public String getCarrozzeria() {
        return carrozzeria;
    }

    public void setCarrozzeria(String carrozzeria) {
        this.carrozzeria = carrozzeria;
    }

    public String getRuotaDiScorta() {
        return ruotaDiScorta;
    }

    public void setRuotaDiScorta(String ruotaDiScorta) {
        this.ruotaDiScorta = ruotaDiScorta;
    }

    public Boolean getIsAntitheft() {
        return isAntitheft;
    }

    public void setIsAntitheft(Boolean isAntitheft) {
        this.isAntitheft = isAntitheft;
    }
}
