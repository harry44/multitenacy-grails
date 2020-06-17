package it.myrent.ee.csv;

import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.exception.VeicoloNonDisponibileException;
import it.myrent.ee.api.utils.GroupSearch;
import it.myrent.ee.api.utils.LocationSearch;
import it.myrent.ee.api.utils.ReservationSearch;
import it.myrent.ee.db.MROldFonteCommissione;
import it.myrent.ee.db.MROldPagamento;
import it.myrent.ee.db.MROldSede;
import it.myrent.ee.db.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;

/**
 * Created by shivangani on 30/08/2019.
 */
public class TJCsvReservation extends CsvReservation {

    /**
     *
     * @param csvRecord
     */
    public TJCsvReservation(TJCsvRecord csvRecord) {
        setCsvRecord(csvRecord);
    }
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    /**
     *
     * @return
     */
    public TJCsvRecord getTJCsvRecord() {
        return (TJCsvRecord) getCsvRecord();
    }

    /**
     *
     * @param csvRecord
     */
    public void setTJCsvRecord(TJCsvRecord csvRecord) {
        setCsvRecord(csvRecord);
    }

    /**
     *
     * @param sx
     */
    @Override
    protected void parse(Session sx) {
        /*
        if ("ACCP".equals(getTJCsvRecord().getStatus())) {
            setBookingStatus(STATUS_NEW);
        } else if ("AMEND".equals(getTJCsvRecord().getStatus())) {
            setBookingStatus(STATUS_MODIFY);
        } else if ("CANCEL".equals(getTJCsvRecord().getStatus())) {
            setBookingStatus(STATUS_CANCEL);
        }
         *
         */

        if ("WAIT".equals(getTJCsvRecord().getStatus())) {
            setBookingStatus(STATUS_NEW);
        } else if ("MOD".equals(getTJCsvRecord().getStatus())) {
            setBookingStatus(STATUS_MODIFY);
        } else if ("CANCEL".equals(getTJCsvRecord().getStatus())) {
            setBookingStatus(STATUS_CANCEL);
        }

        if (getBookingStatus() == null) {
            error("Unknown status: " + getTJCsvRecord().getStatus());
            return;
        }

        setVoucherNumber(getTJCsvRecord().getReservationNumber());
        if (getVoucherNumber() == null) {
            error("Booking reference missing.");
            return;
        }

        setDriverLastName(getTJCsvRecord().getLastName());

        if (getDriverLastName() == null) {
            error("Driver last name missing.");
            return;
        }

        setDriverFirstName(getTJCsvRecord().getFirstName());

        try {
            setPickupDate(dateFormat.parse(getTJCsvRecord().getPickupTime()));
        } catch (Exception ex) {
            error("Pickup date unparseable: " + getTJCsvRecord().getPickupTime());
            return;
        }

        try {
            setReturnDate(dateFormat.parse(getTJCsvRecord().getDropoffTime()));
        } catch (Exception ex) {
            error("Return date unparseable: " + getTJCsvRecord().getDropoffTime());
            return;
        }

        try {
            setRentalDays(Integer.valueOf(getTJCsvRecord().getDays()));
        } catch (Exception ex) {
            error("Rental duration days unparseable: " + getTJCsvRecord().getDays());
            return;
        }

        Integer numeroGiorni = Math.max(1, (int) Math.ceil(FormattedDate.numeroGiorni(getPickupDate(), getReturnDate(), true)));
        if (!numeroGiorni.equals(getRentalDays())&& getRentalDays()!=0) {

            addWarningMessage("Duration days not valid: " + getTJCsvRecord().getDays());
            addWarningMessage("Using computed duration: " + numeroGiorni);
            addWarningMessage("Pickup: " +getPickupDate());
            addWarningMessage("Return: " +getReturnDate());
            addWarningMessage("RentalDays: " +getRentalDays());
            addWarningMessage("Duration: "+ new Double(Math.ceil(FormattedDate.numeroGiorni(getPickupDate(), getReturnDate(), true))));
            setRentalDays(numeroGiorni);
        }

        MROldSede[] pickupLocations = LocationSearch.byDescription(sx, getTJCsvRecord().getPickupLocation());
        pickupLocations = LocationSearch.extractEnabledLocations(pickupLocations);

        if (pickupLocations==null || pickupLocations.length==0)
            pickupLocations= LocationSearch.byExactCode(sx, getTJCsvRecord().getPickupLocation());

        if (pickupLocations.length == 0) {
            addWarningMessage("Location unknown: " + getTJCsvRecord().getPickupLocation());
            error("Location unknown: " + getTJCsvRecord().getPickupLocation());
            return;
        } else if (pickupLocations.length > 1) {
            addWarningMessage("More than one location: " + getTJCsvRecord().getPickupLocation());
            error("More than one location: " + getTJCsvRecord().getPickupLocation());
            return;
        } else {
            setPickupLocation(pickupLocations[0]);
        }

        if (getTJCsvRecord().getPickupLocation().equals(getTJCsvRecord().getDropoffLocation())) {
            setReturnLocation(getPickupLocation());
        } else {
            MROldSede[] returnLocations = LocationSearch.byDescription(sx, getTJCsvRecord().getDropoffLocation());
            returnLocations = LocationSearch.extractEnabledLocations(returnLocations);

            if (returnLocations==null || returnLocations.length==0)
                returnLocations= LocationSearch.byExactCode(sx, getTJCsvRecord().getDropoffLocation());




            if (returnLocations.length == 0) {
                addWarningMessage("Location unknown: " + getTJCsvRecord().getDropoffLocation());
                error("Location unknown: " + getTJCsvRecord().getDropoffLocation());
                return;
            } else if (returnLocations.length > 1) {
                addWarningMessage("More than one location: " + getTJCsvRecord().getDropoffLocation());
                error("More than one location: " + getTJCsvRecord().getDropoffLocation());
                return;
            } else {
                setReturnLocation(returnLocations[0]);
            }
        }

        setCarGroup(GroupSearch.byNatlCode(sx, getTJCsvRecord().getSupplierGroup()));

        //FIXME Non cerchiamo piu' per il codice internazionale.
//        if (getCarGroup() == null) {
//            setCarGroup(searchGroupByIntCode(sx, getHACsvRecord().getHAGroupCode()));
//        }

        if (getCarGroup() == null) {
            setCarGroup(GroupSearch.byIntlCode(sx, getTJCsvRecord().getSupplierGroup()));
            if (getCarGroup() == null) {
                error("Group unknown: " + getTJCsvRecord().getSupplierGroup());
                return;
            }
        }

        StringBuilder memo = new StringBuilder("Car group: " + getCarGroup().getCodiceNazionale() + " / " + getCarGroup().getCodiceInternazionale());
        if (getTJCsvRecord().getFlightNumber() != null) {
            memo.append("\n");
            memo.append("Flight number:" + getTJCsvRecord().getFlightNumber());
        }

        if(getTJCsvRecord().getExtras() != null) {
            memo.append("\n");
            memo.append("Extras: ").append(getTJCsvRecord().getExtras());
        }

        if (getTJCsvRecord().getComments() != null) {
            memo.append("\n");
            memo.append("Comments: ").append(getTJCsvRecord().getComments());
        }
        setMemorandum(memo.toString());
    }

    /**
     *
     * @param sx
     * @param tx
     * @param fonteCommissione
     * @param pagamento
     * @param user
     * @throws VeicoloNonDisponibileException
     */
    @Override
    public void process(Session sx, Transaction tx, MROldFonteCommissione fonteCommissione, MROldPagamento pagamento, User user) throws VeicoloNonDisponibileException {
        setReservationSource(fonteCommissione);
        setPaymentType(pagamento);
        setUser(user);
        parse(sx);
        if (getSuccess() == null) {
            retrieveReservation(sx);
            if (getBookingStatus().equals(STATUS_NEW)) {
                if (getPrenotazione() != null) {
                    setConfirmed(getPrenotazione().getConfermata());
                    success("New reservation: Reservation already exists");
                } else {
                    newReservation(sx, tx, user);
                }
            } else if (getBookingStatus().equals(STATUS_MODIFY)) {
                if (getPrenotazione() == null) {
                    addWarningMessage("Amend reservation: Reservation not found, adding new");
                    newReservation(sx, tx, user);
                } else {
                    if(getPrenotazione().getAnnullata()) {
                        error("Amend reservation: Reservation is cancelled.");
                    } else if(getPrenotazione().getRifiutata()) {
                        error("Amend reservation: Reservation is refused.");
                    } else if(getPrenotazione().getUsata()) {
                        error("Amend reservation: Rental has started.");
                    } else {
                        modifyReservation(sx);
                    }
                }
            } else if (getBookingStatus().equals(STATUS_CANCEL)) {
                if (getPrenotazione() == null) {
                    error("Cancel reservation: Reservation not found.");
                } else if (getPrenotazione().getAnnullata()) {
                    success("Cancel reservation: Already cancelled.");
                    setConfirmed(true);
                } else if(getPrenotazione().getUsata()) {
                    error("Cancel reservation: Rental has started.");
                } else {
                    cancelReservation(sx, tx);
                }
            }
        }
        TJCsvRecord response = (TJCsvRecord) getTJCsvRecord().clone();
        if (getSuccess()) {
            response.setSupplierReference(getPrenotazione().getCodice());
        } else if (getBookingStatus() != null) {
            response.setSupplierReference(null);
        }
        setCsvResponse(response);
    }

    @Override
    protected void retrieveReservation(Session sx) {
        super.retrieveReservation(sx);
        if (getPrenotazione() == null) {
            String notAlphaNumeric = new String("[^A-Za-z0-9]*");
            String voucherNumberAlphaNumeric = getVoucherNumber().replaceAll(notAlphaNumeric, "");
            StringBuffer regExp = new StringBuffer("^");
            regExp.append(notAlphaNumeric);
            for (int i = 0; i < voucherNumberAlphaNumeric.length(); i++) {
                regExp.append(voucherNumberAlphaNumeric.charAt(i)).append(notAlphaNumeric);
            }
            regExp.append("$");
            setPrenotazione(ReservationSearch.byRegExpVoucherNumber(sx, getReservationSource(), regExp.toString()));
        }
    }
}
