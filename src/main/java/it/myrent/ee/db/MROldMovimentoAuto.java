package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import java.util.Date;
import it.aessepi.utils.db.PersistentInstance;
import java.util.ResourceBundle;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/** @author Hibernate CodeGenerator */
public class MROldMovimentoAuto implements PersistentInstance, Loggable, Comparable<MROldMovimentoAuto> {

    private Integer id;
    private MROldAffiliato affiliato;
    private MROldCausaleMovimento causale;
    private MROldParcoVeicoli veicolo;
    private MROldPrenotazione prenotazione;
    private MROldContrattoNoleggio contratto;
    private Boolean isClosedByApp;
    private MROldNumerazione numerazione;
    private Integer anno;

    private MROldMovimentoAuto plannedMovement;
    /** date **/
    private Date data;
    private String prefisso;
    private Integer numero;
    
    private MROldSede sedeUscita;
    /** timestamp **/
    private Date inizio;
    private Integer kmInizio;
    private Integer combustibileInizio;
    
    private MROldSede sedeRientro;
    /** timestamp **/
    private Date fine;
    private Integer kmFine;
    private Integer combustibileFine;
    
    private String note;
    private MROldAutista autista;
    
    private User userApertura;
    /** timestamp **/
    private Date dataApertura;
    
    private User userChiusura;
    /** timestamp **/
    private Date dataChiusura;

    private Double importo;

    private MROldFornitori fornitore;
    
    private Boolean ultimo;
    private Boolean chiuso; 
    private Boolean annullato;

    private Boolean isDirtyCheckout;
    
    private Set danniRilevati;
    private Set danniPresenti;
    private Integer kilometriTotaliPercorsi;
    
    private MROldDisponibilitaVeicolo disponibilitaSx;
    
    //@Madhvendra
    private MROldIncidentType mrIncidentType;
    private Boolean isAccident;
    private String signature1;
    private String signature2;
    private Double damagesAmount;
    private Double qtyLiter;
    private String authCod;
    private Boolean isDamages;
    private Boolean isDamageCharged;
    private Boolean isTheft;
    private Boolean isTheftCharged;
    private Boolean damagesAcceptedByCustomer;
    private Boolean customerRefusesCheckIn;
    private Boolean keyLeftInKeyBox;

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    public MROldMovimentoAuto() {
        setChiuso(Boolean.FALSE);
        setAnnullato(Boolean.FALSE);
        setUltimo(Boolean.TRUE);
    }
    
    public MROldMovimentoAuto(
            Integer id,
            Date inizio,
            Date fine,
            Integer idVeicolo,
            Integer idContratto,
            Integer idPrenotazione,
            Boolean prenotazioneConfermata
    ) {
        setId(id);
        setInizio(inizio);
        setFine(fine);
        setVeicolo(new MROldParcoVeicoli());
        getVeicolo().setId(idVeicolo);        
        
        if(idContratto != null) {
            setContratto(new MROldContrattoNoleggio());
            getContratto().setId(idContratto);
        }
        
        if(idPrenotazione != null) {
            setPrenotazione(new MROldPrenotazione());
            getPrenotazione().setId(idPrenotazione);
            getPrenotazione().setConfermata(prenotazioneConfermata);
        }  
    }

    public MROldMovimentoAuto(
            Integer id,
            Date inizio,
            Date fine,
            Integer idVeicolo,
            Integer idContratto,
            Integer idPrenotazione,
            Boolean prenotazioneConfermata,
            Integer idSedeUscitaPrenotazione,
            Integer idSedeRientroPrevistoPrenotazione,
            Integer idGruppoAssegnatoPrenotazione,
            Integer idGruppoPrenotazione,
            Integer idGruppoAssegnatoContratto,
            Integer idGruppoContratto,
            Boolean contrattoChiuso,
            Integer idSedeUscitaContratto,
            Integer idSedeRientroPrevistoContratto,
            Integer idCausaleMovimento,
            Boolean causaleNoleggio,
            Integer coloreSedeUscitaContratto,
            Integer coloreSedeRientroContratto,
            Integer coloreSedeUscitaPrenotaione,
            Integer coloreSedeRientroPrenotazione,
            Integer idSedeUscitaMovimento,
            Integer idSedeRientroMovimento,
            Integer coloreSedeUscitaMovimento,
            Integer coloreSedeRientroMovimento,
            String codiceNazionaleGruppoContratto,
            String codiceNazionaleGruppoAssegnatoContratto,
            String codiceNazionaleGruppoPrenotazione,
            String codiceNazionaleGruppoAssegnatoPrenotazione
    ) {
        setId(id);
        setCausale(new MROldCausaleMovimento());
        getCausale().setId(idCausaleMovimento);
        getCausale().setNoleggio(causaleNoleggio);
        setInizio(inizio);
        setFine(fine);
        setVeicolo(new MROldParcoVeicoli());
        getVeicolo().setId(idVeicolo);

        setSedeUscita(new MROldSede());
        getSedeUscita().setId(idSedeUscitaMovimento);
        getSedeUscita().setColore(coloreSedeUscitaMovimento);

        setSedeRientro(new MROldSede());
        getSedeRientro().setId(idSedeRientroMovimento);
        getSedeRientro().setColore(coloreSedeRientroMovimento);

        if(idContratto != null) {
            setContratto(new MROldContrattoNoleggio());
            getContratto().setId(idContratto);
            getContratto().setChiuso(contrattoChiuso);
            getContratto().setGruppoAssegnato(new MROldGruppo());
            getContratto().getGruppoAssegnato().setId(idGruppoAssegnatoContratto);
            getContratto().getGruppoAssegnato().setCodiceNazionale(codiceNazionaleGruppoAssegnatoContratto);

            getContratto().setGruppo(new MROldGruppo());
            getContratto().getGruppo().setId(idGruppoContratto);
            getContratto().getGruppo().setCodiceNazionale(codiceNazionaleGruppoContratto);

            getContratto().setSedeUscita(new MROldSede());
            getContratto().getSedeUscita().setId(idSedeUscitaContratto);
            getContratto().getSedeUscita().setColore(coloreSedeUscitaContratto);

            getContratto().setSedeRientroPrevisto(new MROldSede());
            getContratto().getSedeRientroPrevisto().setId(idSedeRientroPrevistoContratto);
            getContratto().getSedeRientroPrevisto().setColore(coloreSedeRientroContratto);
        }

        if(idPrenotazione != null) {
            setPrenotazione(new MROldPrenotazione());
            getPrenotazione().setId(idPrenotazione);
            getPrenotazione().setConfermata(prenotazioneConfermata);
            getPrenotazione().setSedeUscita(new MROldSede());
            getPrenotazione().getSedeUscita().setId(idSedeUscitaPrenotazione);
            getPrenotazione().getSedeUscita().setColore(coloreSedeUscitaPrenotaione);

            getPrenotazione().setSedeRientroPrevisto(new MROldSede());
            getPrenotazione().getSedeRientroPrevisto().setId(idSedeRientroPrevistoPrenotazione);
            getPrenotazione().getSedeRientroPrevisto().setColore(coloreSedeRientroPrenotazione);

            getPrenotazione().setGruppoAssegnato(new MROldGruppo());
            getPrenotazione().getGruppoAssegnato().setId(idGruppoAssegnatoPrenotazione);
            getPrenotazione().getGruppoAssegnato().setCodiceNazionale(codiceNazionaleGruppoAssegnatoPrenotazione);

            getPrenotazione().setGruppo(new MROldGruppo());
            getPrenotazione().getGruppo().setId(idGruppoPrenotazione);
            getPrenotazione().getGruppo().setCodiceNazionale(codiceNazionaleGruppoPrenotazione);
        }
    }
    
    public MROldMovimentoAuto(
            MROldNumerazione numerazione,
            String prefisso,
            Integer numero,
            Date data,
            Integer anno,
            MROldAffiliato affiliato,
            MROldCausaleMovimento causale,
            MROldPrenotazione prenotazione,
            MROldContrattoNoleggio contratto,
            MROldParcoVeicoli veicolo,
            Set danniPresenti,
            MROldSede sedeUscita,
            MROldSede sedeRientro,
            Date inizio,
            Date fine,
            Integer kmInizio,
            Integer combustibileInizio,
            MROldAutista autista,
            String note,
            User userApertura,
            Date dataApertura,
            Boolean isDirtyCheckout
    ) {
        setNumerazione(numerazione);
        setPrefisso(prefisso);
        setNumero(numero);
        setData(data);
        setAnno(anno);
        setAffiliato(affiliato);
        setCausale(causale);
        setPrenotazione(prenotazione);
        setContratto(contratto);
        setVeicolo(veicolo);
        setDanniPresenti(danniPresenti);
        setSedeUscita(sedeUscita);
        setSedeRientro(sedeRientro);
        setInizio(inizio);
        setFine(fine);
        setKmInizio(kmInizio);
        setKmFine(kmFine);
        setCombustibileInizio(combustibileInizio);
        setCombustibileFine(combustibileInizio);
        setAutista(autista);
        setNote(note);
        setUserApertura(userApertura);
        setDataApertura(dataApertura);        
        setChiuso(Boolean.FALSE);
        setAnnullato(Boolean.FALSE);
        setUltimo(Boolean.TRUE);
        setIsDirtyCheckout(isDirtyCheckout);
    }
    
    /**
     * Costruttore da usare solamente per creare un contenitore da passare al metodo di aggiornamento
     * della disponibilita' dei veicoli. Vedi <code>DisponibilitaUtils.aggiornaMovimento</code>
     * @param veicolo
     * @param sedeUscita
     * @param sedeRientro
     * @param inizio
     * @param fine
     * @param annullato
     */
    public MROldMovimentoAuto(MROldParcoVeicoli veicolo, MROldSede sedeUscita, MROldSede sedeRientro, Date inizio, Date fine, Boolean annullato) {
        setVeicolo(veicolo);
        setSedeUscita(sedeUscita);
        setSedeRientro(sedeRientro);
        setInizio(inizio);
        setFine(fine);
    }
    
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(getVeicolo()).toString();
    }
    
    public boolean equals(Object other) {
        if(other != null && other instanceof MROldMovimentoAuto) {
            return new EqualsBuilder().append(getId(), ((MROldMovimentoAuto)other).getId()).isEquals();
        } else {
            return false;
        }
    }
    public Boolean getIsDamageCharged() {
        return isDamageCharged;
    }

    /**
     * @param isDamageCharged the isDamageCharged to set
     */
    public void setIsDamageCharged(Boolean isDamageCharged) {
        this.isDamageCharged = isDamageCharged;
    }
    public Boolean getIsTheft() {
        return isTheft;
    }

    /**
     * @param isTheft the isTheft to set
     */
    public void setIsTheft(Boolean isTheft) {
        this.isTheft = isTheft;
    }

    /**
     * @return the isTheftCharged
     */
    public Boolean getIsTheftCharged() {
        return isTheftCharged;
    }

    /**
     * @param isTheftCharged the isTheftCharged to set
     */
    public void setIsTheftCharged(Boolean isTheftCharged) {
        this.isTheftCharged = isTheftCharged;
    }
    /**
     * @return the damagesAmount
     */
    public Double getDamagesAmount() {
        return damagesAmount;
    }

    /**
     * @param damagesAmount the damagesAmount to set
     */
    public void setDamagesAmount(Double damagesAmount) {
        this.damagesAmount = damagesAmount;
    }
    public Double getQtyLiter() {
        return qtyLiter;
    }

    public String getAuthCod() {
        return authCod;
    }

    /**
     * @param authCod the authCod to set
     */
    public void setAuthCod(String authCod) {
        this.authCod = authCod;
    }
    public Boolean getIsDamages() {
        return isDamages;
    }

    /**
     * @param isDamages the isDamages to set
     */
    public void setIsDamages(Boolean isDamages) {
        this.isDamages = isDamages;
    }
    /**
     * @param qtyLiter the qtyLiter to set
     */
    public void setQtyLiter(Double qtyLiter) {
        this.qtyLiter = qtyLiter;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldAffiliato getAffiliato() {
        return affiliato;
    }

    public void setAffiliato(MROldAffiliato affiliato) {
        this.affiliato = affiliato;
    }

    public MROldParcoVeicoli getVeicolo() {
        return veicolo;
    }

    public void setVeicolo(MROldParcoVeicoli veicolo) {
        this.veicolo = veicolo;
    }

    public MROldPrenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(MROldPrenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public MROldContrattoNoleggio getContratto() {
        return contratto;
    }

    public void setContratto(MROldContrattoNoleggio contratto) {
        this.contratto = contratto;
    }

    public MROldSede getSedeUscita() {
        return sedeUscita;
    }

    public void setSedeUscita(MROldSede sedeUscita) {
        this.sedeUscita = sedeUscita;
    }

    public Date getInizio() {
        return inizio;
    }

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }

    public Integer getKmInizio() {
        return kmInizio;
    }

    public void setKmInizio(Integer kmInizio) {
        this.kmInizio = kmInizio;
    }

    public Integer getCombustibileInizio() {
        return combustibileInizio;
    }

    public void setCombustibileInizio(Integer combustibileInizio) {
        this.combustibileInizio = combustibileInizio;
    }

    public MROldSede getSedeRientro() {
        return sedeRientro;
    }

    public void setSedeRientro(MROldSede sedeRientro) {
        this.sedeRientro = sedeRientro;
    }

    public Date getFine() {
        return fine;
    }

    public void setFine(Date fine) {
        this.fine = fine;
    }

    public Integer getKmFine() {
        return kmFine;
    }

    public void setKmFine(Integer kmFine) {
        this.kmFine = kmFine;
    }

    public Integer getCombustibileFine() {
        return combustibileFine;
    }

    public void setCombustibileFine(Integer combustibileFine) {
        this.combustibileFine = combustibileFine;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public MROldAutista getAutista() {
        return autista;
    }

    public void setAutista(MROldAutista autista) {
        this.autista = autista;
    }

    public User getUserApertura() {
        return userApertura;
    }

    public void setUserApertura(User userApertura) {
        this.userApertura = userApertura;
    }

    public Date getDataApertura() {
        return dataApertura;
    }

    public void setDataApertura(Date dataApertura) {
        this.dataApertura = dataApertura;
    }

    public User getUserChiusura() {
        return userChiusura;
    }

    public void setUserChiusura(User userChiusura) {
        this.userChiusura = userChiusura;
    }

    public Date getDataChiusura() {
        return dataChiusura;
    }

    public void setDataChiusura(Date dataChiusura) {
        this.dataChiusura = dataChiusura;
    }

    public Double getImporto() {
        return importo;
    }

    public void setImporto(Double importo) {
        this.importo = importo;
    }

    public MROldFornitori getFornitore() {
        return fornitore;
    }

    public void setFornitore(MROldFornitori fornitore) {
        this.fornitore = fornitore;
    }

    public MROldCausaleMovimento getCausale() {
        return causale;
    }

    public void setCausale(MROldCausaleMovimento causale) {
        this.causale = causale;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public MROldNumerazione getNumerazione() {
        return numerazione;
    }

    public void setNumerazione(MROldNumerazione numerazione) {
        this.numerazione = numerazione;
    }

    public String getPrefisso() {
        return prefisso;
    }

    public void setPrefisso(String prefisso) {
        this.prefisso = prefisso;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Boolean getUltimo() {
        return ultimo;
    }

    public void setUltimo(Boolean ultimo) {
        this.ultimo = ultimo;
    }

    public Boolean getChiuso() {
        return chiuso;
    }

    public void setChiuso(Boolean chiuso) {
        this.chiuso = chiuso;
    }

    public Boolean getAnnullato() {
        return annullato;
    }

    public void setAnnullato(Boolean annullato) {
        this.annullato = annullato;
    }

    public Set getDanniPresenti() {
        return danniPresenti;
    }

    public void setDanniPresenti(Set danniPresenti) {
        this.danniPresenti = danniPresenti;
    }

    public Set getDanniRilevati() {
        return danniRilevati;
    }

    public void setDanniRilevati(Set danniRilevati) {
        this.danniRilevati = danniRilevati;
    }

    public MROldDisponibilitaVeicolo getDisponibilitaSx() {
        return disponibilitaSx;
    }

    public void setDisponibilitaSx(MROldDisponibilitaVeicolo disponibilitaSx) {
        this.disponibilitaSx = disponibilitaSx;
    }

    public String[] getLoggableFields() {
        return new String[]{
                    "importo", // NOI18N
                    "sedeRientro", // NOI18N
                    "fine", // NOI18N
                    "kmFine", // NOI18N
                    "combustibileFine", // NOI18N
                    "chiuso" // NOI18N
                };
    }

    public String[] getLoggableLabels() {
        return new String[]{
                    bundle.getString("MovimentoAuto.logImporto"),
                    bundle.getString("MovimentoAuto.logSedeRientro"),
                    bundle.getString("MovimentoAuto.logFine"),
                    bundle.getString("MovimentoAuto.logKmFine"),
                    bundle.getString("MovimentoAuto.logCombustibileFine"),
                    bundle.getString("MovimentoAuto.logChiuso")
                };
    }

    public String getEntityName() {
        return "MROldMovimentoAuto"; // NOI18N
    }

    public Integer getEntityId() {
        return getId();
    }


    public int compareTo(MROldMovimentoAuto o) {
        return getInizio().compareTo(o.getInizio());
    }

    public MROldIncidentType getMrIncidentType() {
        return this.mrIncidentType;
    }

    public void setMrIncidentType(MROldIncidentType mrIncidentType) {
        this.mrIncidentType = mrIncidentType;
    }

    public Boolean getIsAccident() {
        return isAccident;
    }

    public void setIsAccident(Boolean isAccident) {
        this.isAccident = isAccident;
    }
    
    public Integer getKilometriTotaliPercorsi()
    { Integer kmPercorsi = 0;
        if(this.getKmFine() != null && this.getKmInizio() != null){
            kmPercorsi = this.getKmFine()-this.getKmInizio();
        }
        return kmPercorsi;
    }
    
    public Integer getGiorniExtra(Integer voucher, Double totGiorni)
    {return (totGiorni.intValue()-voucher>=0?totGiorni.intValue()-voucher:0);}    
    
      public Boolean getIsClosedByApp() {
        return isClosedByApp;
    }
      public void setIsClosedByApp(Boolean isClosedByApp) {
        this.isClosedByApp = isClosedByApp;
    }
       public String getSignature1() {
        return signature1;
    }

    public void setSignature1(String Signature1) {
        this.signature1 = Signature1;
    }
      public String getSignature2() {
        return signature2;
    }

    public void setSignature2(String Signature2) {
        this.signature2 = Signature2;
    }

    public Boolean getIsDirtyCheckout() {
        return isDirtyCheckout;
    }

    public void setIsDirtyCheckout(Boolean isDirtyCheckout) {
        this.isDirtyCheckout = isDirtyCheckout;
    }

    public Boolean getDamagesAcceptedByCustomer() {
        return damagesAcceptedByCustomer;
    }

    public void setDamagesAcceptedByCustomer(Boolean damagesAcceptedByCustomer) {
        this.damagesAcceptedByCustomer = damagesAcceptedByCustomer;
    }

    public Boolean getCustomerRefusesCheckIn() {
        return customerRefusesCheckIn;
    }

    public void setCustomerRefusesCheckIn(Boolean customerRefusesCheckIn) {
        this.customerRefusesCheckIn = customerRefusesCheckIn;
    }

    public Boolean getKeyLeftInKeyBox() {
        return keyLeftInKeyBox;
    }

    public void setKeyLeftInKeyBox(Boolean keyLeftInKeyBox) {
        this.keyLeftInKeyBox = keyLeftInKeyBox;
    }


    public MROldMovimentoAuto getPlannedMovement() {
        return plannedMovement;
    }

    public void setPlannedMovement(MROldMovimentoAuto plannedMovement) {
        this.plannedMovement = plannedMovement;
    }
}
