package it.myrent.ee.csv;

import it.aessepi.myrentcs.utils.FatturazioneFactory;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.exception.FatturaVuotaException;
import it.myrent.ee.api.exception.TariffaNonValidaException;
import it.myrent.ee.api.exception.VeicoloNonDisponibileException;
import it.myrent.ee.api.utils.*;
import it.myrent.ee.db.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

/**
 * Created by shivangani on 30/08/2019.
 */
public abstract class CsvReservation {

    private CsvRecord csvRecord;
    private CsvRecord csvResponse;
    private MROldPrenotazione prenotazione;
    private Boolean success;
    private Boolean confirmed;
    private StringBuilder errorMessages;
    private StringBuilder warningMessages;
    private String confirmationNumber;
    private String voucherNumber;
    private String driverLastName;
    private String driverFirstName;
    private Integer bookingStatus;
    private Integer rentalDays;
    private Date pickupDate;
    private Date returnDate;
    private MROldSede pickupLocation;
    private MROldSede returnLocation;
    private MROldGruppo carGroup;
    private MROldFonteCommissione reservationSource;
    private MROldPagamento paymentType;
    private User user;
    private String memorandum;

    /**
     *
     */
    public static String newline = System.getProperty("line.separator");
    private static final StandardToStringStyle toStringStyle;

    /**
     *
     */
    protected static final Integer STATUS_NEW = 1;

    /**
     *
     */
    protected static final Integer STATUS_MODIFY = 2;

    /**
     *
     */
    protected static final Integer STATUS_CANCEL = 3;

    /**
     *
     */
    protected static final Integer STATUS_RETRIEVE = 3;

    static {
        //log = LogFactory.getLog(HACsvRecord.class);
        toStringStyle = new StandardToStringStyle();
        toStringStyle.setNullText(new String());
        toStringStyle.setUseIdentityHashCode(false);
        toStringStyle.setUseClassName(false);
        toStringStyle.setUseFieldNames(false);
        toStringStyle.setFieldSeparator(", ");
    }

    /**
     *
     */
    public CsvReservation() {
        setErrorMessages(new StringBuilder(0));
        setWarningMessages(new StringBuilder(0));
    }

    /**
     *
     * @param sx
     */
    abstract protected void parse(Session sx);

    /**
     *
     * @param sx
     * @param tx
     * @param fonteCommissione
     * @param pagamento
     * @param user
     * @throws VeicoloNonDisponibileException
     */
    abstract public void process(Session sx, Transaction tx, MROldFonteCommissione fonteCommissione, MROldPagamento pagamento, User user) throws VeicoloNonDisponibileException;

    /**
     *
     * @return
     */
    public CsvRecord getCsvRecord() {
        return csvRecord;
    }

    /**
     *
     * @param csvRecord
     */
    public void setCsvRecord(CsvRecord csvRecord) {
        this.csvRecord = csvRecord;
    }

    /**
     *
     * @return
     */
    public CsvRecord getCsvResponse() {
        return csvResponse;
    }

    /**
     *
     * @param csvResponse
     */
    public void setCsvResponse(CsvRecord csvResponse) {
        this.csvResponse = csvResponse;
    }

    /**
     *
     * @return
     */
    public MROldPrenotazione getPrenotazione() {
        return prenotazione;
    }

    /**
     *
     * @param prenotazione
     */
    public void setPrenotazione(MROldPrenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    /**
     *
     * @param success
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     *
     * @return
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     *
     * @return
     */
    public StringBuilder getErrorMessages() {
        return errorMessages;
    }

    /**
     *
     * @param errorMessages
     */
    public void setErrorMessages(StringBuilder errorMessages) {
        this.errorMessages = errorMessages;
    }

    /**
     *
     * @return
     */
    public StringBuilder getWarningMessages() {
        return warningMessages;
    }

    /**
     *
     * @param warningMessages
     */
    public void setWarningMessages(StringBuilder warningMessages) {
        this.warningMessages = warningMessages;
    }

    /**
     *
     * @param message
     */
    protected void addErrorMessage(String message) {
        if (getErrorMessages().length() > 0) {
            getErrorMessages().append("\n");
        }
        getErrorMessages().append(message);
    }

    /**
     *
     * @param message
     */
    protected void addWarningMessage(String message) {
        if (getWarningMessages().length() > 0) {
            getWarningMessages().append("\n");
        }
        getWarningMessages().append(message);
    }

    /**
     * Adds <code>message</code> to the error message buffer
     * and sets <code>success</code> to <code>false</code>
     * @param message The error message
     */
    protected void error(String message) {
        addErrorMessage(message);
        setSuccess(false);
    }

    /**
     * Adds <code>message</code> to the warning message buffer if not null
     * and sets <code>success</code> to <code>true</code>.
     * @param message The warning message, may be null.
     */
    protected void success(String message) {
        if (message != null) {
            addWarningMessage(message);
        }
        setSuccess(true);
    }

//    protected void warning(String message) {
//        addWarningMessage(message);
//        setSuccess(true);
//    }

    /**
     *
     * @return
     */
    public String getVoucherNumber() {
        return voucherNumber;
    }

    /**
     *
     * @param voucherNumber
     */
    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    /**
     *
     * @return
     */
    public Integer getBookingStatus() {
        return bookingStatus;
    }

    /**
     *
     * @param bookingStatus
     */
    public void setBookingStatus(Integer bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    /**
     *
     * @return
     */
    public Date getPickupDate() {
        return pickupDate;
    }

    /**
     *
     * @param pickupDate
     */
    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    /**
     *
     * @return
     */
    public Date getReturnDate() {
        return returnDate;
    }

    /**
     *
     * @param returnDate
     */
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    /**
     *
     * @return
     */
    public MROldSede getPickupLocation() {
        return pickupLocation;
    }

    /**
     *
     * @param pickupLocation
     */
    public void setPickupLocation(MROldSede pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    /**
     *
     * @return
     */
    public MROldSede getReturnLocation() {
        return returnLocation;
    }

    /**
     *
     * @param returnLocation
     */
    public void setReturnLocation(MROldSede returnLocation) {
        this.returnLocation = returnLocation;
    }

    /**
     *
     * @return
     */
    public MROldGruppo getCarGroup() {
        return carGroup;
    }

    /**
     *
     * @param carGroup
     */
    public void setCarGroup(MROldGruppo carGroup) {
        this.carGroup = carGroup;
    }

    /**
     *
     * @return
     */
    public MROldFonteCommissione getReservationSource() {
        return reservationSource;
    }

    /**
     *
     * @param reservationSource
     */
    public void setReservationSource(MROldFonteCommissione reservationSource) {
        this.reservationSource = reservationSource;
    }

    /**
     *
     * @return
     */
    public MROldPagamento getPaymentType() {
        return paymentType;
    }

    /**
     *
     * @param paymentType
     */
    public void setPaymentType(MROldPagamento paymentType) {
        this.paymentType = paymentType;
    }

    /**
     *
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     *
     * @return
     */
    public String getDriverLastName() {
        return driverLastName;
    }

    /**
     *
     * @param driverLastName
     */
    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    /**
     *
     * @return
     */
    public String getDriverFirstName() {
        return driverFirstName;
    }

    /**
     *
     * @param driverFirstName
     */
    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    /**
     *
     * @return
     */
    public String getMemorandum() {
        return memorandum;
    }

    /**
     *
     * @param memorandum
     */
    public void setMemorandum(String memorandum) {
        this.memorandum = memorandum;
    }

    /**
     *
     * @return
     */
    public Integer getRentalDays() {
        return rentalDays;
    }

    /**
     *
     * @param rentalDays
     */
    public void setRentalDays(Integer rentalDays) {
        this.rentalDays = rentalDays;
    }

    /**
     *
     * @return
     */
    public Boolean getConfirmed() {
        return confirmed;
    }

    /**
     *
     * @param confirmed
     */
    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    private MROldTariffa estimatedRate(Session sx, MROldFonteCommissione fonte, MROldSede pickupLocation, MROldGruppo gruppo, Date inizio, Date fine, Date dataPrenotazione) {
        String parameter = null;
        Boolean is_touringcars_rate = (parameter != null) ? new Boolean(parameter) : Boolean.FALSE;
        //return TariffeUtils.creaTariffa(
        //        sx, fonte, gruppo, pickupLocation.getAeroporto(), pickupLocation.getFerrovia(), false, false,
        //        null, null, null, inizio, fine, dataPrenotazione, pickupLocation, fonte.getCodice());


        // commentato fonte.getCodice - last parameter

        return TariffeUtils.creaMROldTariffa(
                sx, fonte, gruppo, pickupLocation.getAeroporto(), pickupLocation.getFerrovia(), false, false,
                null, null, null, inizio, fine, dataPrenotazione, pickupLocation);
    }

    //Copy and paste from Andrea Online Booking
    private MROldPrenotazione newReservationFromOnlineBooking(
            Session sx,
            Transaction tx,
            MROldTariffa tariffa,
            MROldClienti cliente,
            MROldGruppo gruppo,
            MROldSede pickupLocation,
            Date pickupDate,
            MROldSede returnLocation,
            Date returnDate,
            String voucherNumber,
            Double scontoMassimo,
            Integer rentalDays,
            String note,
            User user) {

        /*Iterator itE = tariffa.getOptionalsTariffa().values().iterator();
        while (itE.hasNext()) {
        OptionalTariffa optionalTariffa = (OptionalTariffa) itE.next();

        if (optionalTariffa.getOptional().getImportoDaNegoziare()) {
        System.out.println("OPTIONAL "+optionalTariffa.getOptional().getDescrizione()+" IMPORTO "+optionalTariffa.getImporto());
        }
        }*/

        MROldPrenotazione aPrenotazione = null;
        try {
            //FonteCommissione fonte = FonteCommissioneSearch.byAccessCode(sx, FonteCommissioneSearch.getCodiceFonteCommissionabile());

            MROldFonteCommissione fonte = getReservationSource();

            if (fonte != null && fonte.getPromemoria() != null) {
                String[] arrayCodiciOptional = fonte.getPromemoria().split("#");
                if (arrayCodiciOptional != null && arrayCodiciOptional.length > 0) {
                }
                for (int i = 0; i < arrayCodiciOptional.length; i++) {
                    Iterator it = tariffa.getOptionalsTariffa().values().iterator();
                    while (it.hasNext()) {
                        MROldOptionalTariffa optionalTariffa = (MROldOptionalTariffa) it.next();

                        if (optionalTariffa.getOptional().getCodice().equals(arrayCodiciOptional[i])) {
                            optionalTariffa.setSelezionato(Boolean.TRUE);
                            sx.saveOrUpdate(optionalTariffa);
                        }
                    }
                }

            }

            aPrenotazione = ImpegniUtils.generaPrenotazione(sx, gruppo, pickupLocation, pickupDate, returnDate, user);
            aPrenotazione.setNumerazione(NumerazioniUtils.getNumerazione(sx, pickupLocation, MROldNumerazione.PRENOTAZIONI, user));
            aPrenotazione.setNumero(NumerazioniUtils.nuovoNumero(sx, aPrenotazione.getNumerazione(), aPrenotazione.getAnno()));
            aPrenotazione.setPrefisso(aPrenotazione.getNumerazione().getPrefisso());
            aPrenotazione.setAffiliato(pickupLocation.getAffiliato());

            aPrenotazione.setSedeRientroPrevisto(returnLocation);

            MROldCommissione commissione = new MROldCommissione(
                    null,
                    aPrenotazione,
                    null,
                    getReservationSource(),
                    (voucherNumber != null && (rentalDays!=0)),
                    voucherNumber != null && (rentalDays!=0) ?voucherNumber:null,
                    voucherNumber != null && (rentalDays!=0) ? rentalDays : null);

            //--------------------------set if prepagato --------------------------------------------------------------------------------------//

            commissione.setPrepagato(fonte.getCliente() != null && (rentalDays!=0));
            if (fonte.getCliente() != null) {
                commissione.setCodiceVoucher(voucherNumber);
                commissione.setPrepagato(Boolean.TRUE);
                commissione.setGiorniVoucher(rentalDays);
                if (rentalDays==0 || (rentalDays==null)||rentalDays.equals(0)){
                    commissione.setGiorniVoucher(null);
                    commissione.setCodiceVoucher(null);
                    commissione.setPrepagato(Boolean.FALSE);
                }

            } else{
                commissione.setGiorniVoucher(null);
                commissione.setCodiceVoucher(null);
                commissione.setPrepagato(Boolean.FALSE);
            }

            //----------------------------------------------------------------------------

            aPrenotazione.setCommissione(commissione);

            aPrenotazione.setScontoPercentuale(scontoMassimo);
            //get note from string, format: note---3type---3

            aPrenotazione.setNote(note);
            //aPrenotazione.setPagamento(getFonteCommissione().getPagamento());


            //Commento l'impostazione del pagamento
            aPrenotazione.setPagamento(paymentType); //imposta il pagamento in base ai dati passati


            aPrenotazione.setUserApertura(getUser());
            aPrenotazione.setCliente(cliente);
            aPrenotazione.setTariffa(tariffa);


            sx.saveOrUpdate(aPrenotazione.getCliente());
            MROldNumerazione numerazioneClienti = NumerazioniUtils.getNumerazione(sx, MROldNumerazione.CLIENTI, user);
            aPrenotazione.getCliente().setCodice(NumerazioniUtils.aggiornaProgressivo(sx, numerazioneClienti, 0));
            NumerazioniUtils.aggiornaProgressivo(sx, aPrenotazione.getNumerazione(), aPrenotazione.getAnno());
            sx.saveOrUpdate(aPrenotazione);
            MROldPrenotazione p = aPrenotazione;

            MROldCausaleMovimento causalePrenotazione = MovimentoAutoUtils.leggiCausalePrenotazione(sx);

            // Search available vehicles at least one day before and one day after the requested period.
            Date inizioDisponibilita = FormattedDate.add(p.getInizio(), Calendar.DAY_OF_MONTH, -1);
            Date fineDisponibilita = FormattedDate.add(p.getFine(), Calendar.DAY_OF_MONTH, 1);
            MROldParcoVeicoli v = DisponibilitaUtils.cercaVeicoloDisponibile(sx, p.getSedeUscita(), p.getGruppo(), inizioDisponibilita, fineDisponibilita);
            if (v != null) {
                p.setMovimento(new MROldMovimentoAuto(
                        null, // Numerazione
                        null, // Prefisso
                        null, // Numero
                        FormattedDate.formattedDate(), // Data
                        FormattedDate.annoCorrente(), // Anno
                        p.getAffiliato(),
                        causalePrenotazione,
                        p, // Prenotazione
                        null, // Contratto
                        v,
                        null, // Danni
                        p.getSedeUscita(),
                        p.getSedeRientroPrevisto(),
                        p.getInizio(),
                        p.getFine(),
                        null, // Km inizio
                        null, // Combustibile uscita
                        null, // Autista
                        null, // Note
                        p.getUserApertura(),
                        FormattedDate.formattedTimestamp(),
                        v.getPulita()));
                sx.saveOrUpdate(p);
            }


            p.setCodice(voucherNumber);


            calcolaTotaleNoleggioPPay(sx, p);
            calcolaTotaleNoleggioNoPPay(sx, p);

            if (p.getNoleggio() == null) {
                p.setNoleggio(0.0);
            }

            if (p.getNoleggioPPay() == null) {
                p.setNoleggioPPay(0.0);
            }

            DisponibilitaUtils.aggiornaDisponibilita(sx, tx, null, p.getMovimento());
        } catch (VeicoloNonDisponibileException ex) {
            /*throw new RequestProcessorException(
            CodeProcessor.EWT_UNKNOWN,
            CodeProcessor.ERR_DURING_PROCESSING_PLEASE_RETRY,
            CodeProcessor.NOT_PROCESSED,
            "Unable to commit transaction. Please resend all data.");
             *
             *
             */

            System.out.println("Eccezione: Veicolo non disponibile");
            ex.printStackTrace();

        } catch (Exception e) {
            System.out.println("Eccezione: Veicolo non disponibile");
            e.printStackTrace();
        }

//            try {
//                DisponibilitaUtils.aggiornaDisponibilita(sx, tx, null, p.getMovimento());
//            } catch (VeicoloNonDisponibileException ex) {
//                throw new RequestProcessorException(
//                        CodeProcessor.EWT_UNKNOWN,
//                        CodeProcessor.ERR_DURING_PROCESSING_PLEASE_RETRY,
//                        CodeProcessor.NOT_PROCESSED,
//                        "Unable to commit transaction. Please resend all data.");
//            }


        return aPrenotazione;
    }

    private void calcolaTotaleNoleggioPPay(Session sx, MROldPrenotazione p) throws TariffaNonValidaException {
        Boolean isPrepagato = p.getCommissione().getPrepagato();
        Integer giorniPrepagati = (Integer) p.getCommissione().getGiorniVoucher();

        if (isPrepagato!=null && isPrepagato && giorniPrepagati != null) {
            List listaPrepagato = null;

            Double aScontoExtraPrepay = 0.0;
            if(p.getScontoExtraPrepay() != null) {
                aScontoExtraPrepay = p.getScontoExtraPrepay();
            }

            try {
                listaPrepagato = FatturazioneFactory.PREPAGATO_FACTORY.getFatturazione(
                        sx,
                        p.getTariffa(),
                        p.getInizio(),
                        p.getFine(),
                        giorniPrepagati,
                        aScontoExtraPrepay)
                        .calcolaRigheProssimaFattura(sx);
            } catch (FatturaVuotaException fvex) {
                System.out.println("JSchedaPrenotazioni.calcolaTotaleNoleggioPPay - Errore per fattura vuota");
                fvex.printStackTrace();
                listaPrepagato = new ArrayList();
            }

            if (listaPrepagato != null && !listaPrepagato.isEmpty()) {
                Double[] importi = FatturaUtils.calcolaImportiRigheFattura(listaPrepagato);
                p.setNoleggioPPay(importi[2]);
            }
        }
    }

    private void calcolaTotaleNoleggioNoPPay(Session sx, MROldPrenotazione p) throws TariffaNonValidaException {
        Boolean isPrepagato = p.getCommissione().getPrepagato();
        Integer giorniPrepagati = (Integer) p.getCommissione().getGiorniVoucher();

        if (isPrepagato==null && isPrepagato ==false) {
            List listaNoPrepagato = null;

            try {
                listaNoPrepagato = FatturazioneFactory.DEFAULT_FACTORY.getFatturazione(
                        sx,
                        p.getTariffa(),
                        p.getInizio(),
                        p.getFine(),
                        giorniPrepagati,
                        p.getScontoPercentuale())
                        .calcolaRigheProssimaFattura(sx);
            } catch (FatturaVuotaException fvex) {
                System.out.println("JSchedaPrenotazioni.calcolaTotaleNoleggioPPay - Errore per fattura vuota");
                fvex.printStackTrace();
                listaNoPrepagato = new ArrayList();
            }

            if (listaNoPrepagato != null && !listaNoPrepagato.isEmpty()) {
                Double[] importi = FatturaUtils.calcolaImportiRigheFattura(listaNoPrepagato);
                p.setNoleggio(importi[2]);
            }
        }
    }

    /**
     *
     * @param sx
     * @param tx
     * @throws VeicoloNonDisponibileException
     */
    protected void newReservation(Session sx, Transaction tx, User user) throws VeicoloNonDisponibileException {

        MROldClienti client = new MROldClienti();
        client.setCognome(getDriverLastName());
        client.setNome(getDriverFirstName());

        MROldTariffa estimatedRate = estimatedRate(sx, getReservationSource(), pickupLocation, getCarGroup(), getPickupDate(), getReturnDate(), new Date());

        MROldImportoTariffa minimoTariffa=null;

        if (estimatedRate != null) {
            minimoTariffa = FatturaUtils.trovaMinimoTariffa(estimatedRate);
        }

        if (estimatedRate == null || minimoTariffa == null) {
            System.out.println("Dear customer. We were unable to find any rates for the requested period / category.");
            //log.err("Dear customer. We were unable to find any rates for the requested period / category.");
            error("We were unable to find any rates for the requested period / category.");
            return;

        } else if (minimoTariffa.getMinimo() > rentalDays && rentalDays!=0) {
            error("Dear customer, your request does not meet the minimum period required for this rental. Please contact us for rentals shorter than " + minimoTariffa.getMinimo() + " days.");
            return;
        }


        String arrivalDetails = getMemorandum();
        MROldPrenotazione prenotazione = newReservationFromOnlineBooking(
                sx,
                tx,
                estimatedRate,
                client,
                getCarGroup(),
                getPickupLocation(),
                getPickupDate(),
                getReturnLocation(),
                getReturnDate(),
                voucherNumber,
                null,
                rentalDays,
                arrivalDetails,
                user);

        setPrenotazione(prenotazione);
        setConfirmed(prenotazione.getConfermata());
        setSuccess(true);
    }

    /**
     *
     * @param sx
     */
    protected void modifyReservation(Session sx) {
        if (!getPrenotazione().getGruppo().equals(getCarGroup())) { // GRUPPO
            error("Amend reservation: Change group not implemented.");
        } else if (!getPrenotazione().getSedeUscita().equals(getPickupLocation())) { // PICKUP LOCATION
            error("Amend reservation: Change pickup location not implemented.");
        } else if (!getPrenotazione().getSedeRientroPrevisto().equals(getReturnLocation())) { // RETURN LOCATION
            error("Amend reservation: Change return location not implemented.");
        } else if (getPrenotazione().getInizio().getTime() != getPickupDate().getTime()) { // PICKUP DATE
            error("Amend reservation: Change pickup date not implemented.");
        } else if (getPrenotazione().getFine().getTime() != getReturnDate().getTime()) { // RETURN DATE
            error("Amend reservation: Change return date not implemented.");
        } else if (!new EqualsBuilder().append(getDriverLastName(), getPrenotazione().getCliente().getCognome()).//CUSTOMER LAST NAME
                append(getDriverFirstName(), getPrenotazione().getCliente().getNome()).isEquals()) { // CUSTOMER FIRST NAME
            error("Amend reservation: Change customer not implemented.");
        } else if (!new EqualsBuilder().append(getMemorandum(), getPrenotazione().getNote()).isEquals()) { // EXTRAS, FLIGHT, NOTES, etc
            error("Amend reservation: Change notes not implemented.");
        } else {
            // At this point no modifications required. Confirm.
            setConfirmed(getPrenotazione().getConfermata());
            setSuccess(true);
        }
    }

    /**
     *
     * @param sx
     * @param tx
     * @throws VeicoloNonDisponibileException
     */
    protected void cancelReservation(Session sx, Transaction tx) throws VeicoloNonDisponibileException {
        MROldMovimentoAuto movimentoDaCancellare = getPrenotazione().getMovimento();
        MROldMovimentoAuto movimentoIniziale = null;
        if (movimentoDaCancellare != null) {
            movimentoIniziale = new MROldMovimentoAuto(
                    movimentoDaCancellare.getVeicolo(),
                    movimentoDaCancellare.getSedeUscita(),
                    movimentoDaCancellare.getSedeRientro(),
                    movimentoDaCancellare.getInizio(),
                    movimentoDaCancellare.getFine(),
                    movimentoDaCancellare.getAnnullato());
            getPrenotazione().setMovimento(null);
        }
        getPrenotazione().setNoShow(false);
        getPrenotazione().setRifiutata(false);
        getPrenotazione().setAnnullata(true);
        //Aggiungiamo nelle annotazioni la data e ora dell'annullamento.
        StringBuffer annotazioni = new StringBuffer("Cancellata il " + FormattedDate.format("dd MMM yyyy HH:mm", new Date()));
        if (getPrenotazione().getNote() != null) {
            annotazioni.append("\n").append(getPrenotazione().getNote());
        }
        getPrenotazione().setNote(annotazioni.toString());
        sx.saveOrUpdate(getPrenotazione());
        if (movimentoDaCancellare != null) {
            sx.delete(movimentoDaCancellare);
        }
        DisponibilitaUtils.aggiornaDisponibilita(sx, tx, movimentoIniziale, null);
        setConfirmed(true);
        setSuccess(true);
    }

//    protected void refuseReservation(Session sx, Transaction tx) throws VeicoloNonDisponibileException {
//        MovimentoAuto movimentoDaCancellare = getPrenotazione().getMovimento();
//        MovimentoAuto movimentoIniziale = null;
//        if (movimentoDaCancellare != null) {
//            movimentoIniziale = new MovimentoAuto(
//                    movimentoDaCancellare.getVeicolo(),
//                    movimentoDaCancellare.getSedeUscita(),
//                    movimentoDaCancellare.getSedeRientro(),
//                    movimentoDaCancellare.getInizio(),
//                    movimentoDaCancellare.getFine(),
//                    movimentoDaCancellare.getAnnullato());
//            getPrenotazione().setMovimento(null);
//        }
//        getPrenotazione().setNoShow(false);
//        getPrenotazione().setAnnullata(false);
//        getPrenotazione().setRifiutata(true);
//        //Aggiungiamo nelle annotazioni la data e ora dell'annullamento.
//        StringBuffer annotazioni = new StringBuffer("Rifiutata il " + FormattedDate.format("dd MMM yyyy HH:mm", new Date()));
//        if(getPrenotazione().getNote() != null) {
//            annotazioni.append("\n").append(getPrenotazione().getNote());
//        }
//        getPrenotazione().setNote(annotazioni.toString());
//        sx.saveOrUpdate(getPrenotazione());
//        if(movimentoDaCancellare != null) {
//            sx.delete(movimentoDaCancellare);
//        }
//        DisponibilitaUtils.aggiornaDisponibilita(sx, tx, movimentoIniziale, null);
//        setConfirmed(true);
//        setSuccess(true);
//    }
    /**
     * This method searches the reservation by voucher number.
     * If found, the reservation is loaded. A success status is set if the
     * booking status is <code>STATUS_RETRIEVE</code>, making this method safe to call
     * in case of other booking status operations.
     * @param sx
     */
    protected void retrieveReservation(Session sx) {
        setPrenotazione(ReservationSearch.byVoucherNumber(sx, getReservationSource(), getVoucherNumber()));
        if (STATUS_RETRIEVE.equals(getBookingStatus())) {
            setSuccess(true);
        }
    }
}
