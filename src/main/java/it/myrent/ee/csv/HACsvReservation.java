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
import java.util.Locale;

/**
 * Created by shivangani on 30/08/2019.
 */
public class HACsvReservation extends CsvReservation {

    /**
     *
     * @param csvRecord
     */
    public HACsvReservation(HACsvRecord csvRecord) {
        setCsvRecord(csvRecord);
    }
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d yyyy hh:mm a", Locale.US);

    /**
     *
     * @return
     */
    public HACsvRecord getHACsvRecord() {
        return (HACsvRecord) getCsvRecord();
    }

    /**
     *
     * @param csvRecord
     */
    public void setHACsvRecord(HACsvRecord csvRecord) {
        setCsvRecord(csvRecord);
    }

    /**
     *
     * @param sx
     */
    @Override
    protected void parse(Session sx) {
        try {
            int status = Integer.parseInt(getHACsvRecord().getBookingStatus().split("-")[0]);
            switch (status) {
                case 1:
                    setBookingStatus(STATUS_NEW);
                    break;
                case 2:
                    setBookingStatus(STATUS_MODIFY);
                    break;
                case 3:
                    setBookingStatus(STATUS_CANCEL);
                    break;
            }
        } catch (Exception ex) {
            error("Booking status unparseable: " + getHACsvRecord().getBookingStatus());
            return;
        }

        if (getBookingStatus() == null) {
            error("Booking status unknown: " + getHACsvRecord().getBookingStatus());
            return;
        }

        setVoucherNumber(getHACsvRecord().getBookingReference());
        if (getVoucherNumber() == null) {
            error("Booking reference missing.");
            return;
        }

        setDriverLastName(getHACsvRecord().getDriverName());

        if (getDriverLastName() == null) {
            error("Driver name missing.");
            return;
        }

        try {
            setPickupDate(dateFormat.parse(getHACsvRecord().getRentalStartDate()));
        } catch (Exception ex) {
            error("Pickup date unparseable: " + getHACsvRecord().getRentalStartDate());
            return;
        }

        try {
            setReturnDate(dateFormat.parse(getHACsvRecord().getRentalEndDate()));
        } catch (Exception ex) {
            error("Return date unparseable: " + getHACsvRecord().getRentalEndDate());
            return;
        }

        setRentalDays(Math.max(1, (int) Math.ceil(FormattedDate.numeroGiorni(
                getPickupDate(), getReturnDate(), true))));

        MROldSede[] pickupLocations = LocationSearch.byDescription(sx, getHACsvRecord().getRentalStartLocation());
        pickupLocations = LocationSearch.extractEnabledLocations(pickupLocations);
        if (pickupLocations.length == 0) {
            error("Location unknown: " + getHACsvRecord().getRentalStartLocation());
            return;
        } else if (pickupLocations.length > 1) {
            error("More than one location: " + getHACsvRecord().getRentalStartLocation());
            return;
        } else {
            setPickupLocation(pickupLocations[0]);
        }

        if (getHACsvRecord().getRentalStartLocation().equals(getHACsvRecord().getRentalEndLocation())) {
            setReturnLocation(getPickupLocation());
        } else {
            MROldSede[] returnLocations = LocationSearch.byDescription(sx, getHACsvRecord().getRentalEndLocation());
            returnLocations = LocationSearch.extractEnabledLocations(returnLocations);
            if (returnLocations.length == 0) {
                error("Location unknown: " + getHACsvRecord().getRentalEndLocation());
                return;
            } else if (returnLocations.length > 1) {
                error("More than one location: " + getHACsvRecord().getRentalEndLocation());
                return;
            } else {
                setReturnLocation(returnLocations[0]);
            }
        }

        //Workaround per il gruppo B. (EBMR). Eliminiamo il punto finale.
        setCarGroup(GroupSearch.byNatlCode(sx, getHACsvRecord().getSupplierGroupCode().replaceAll("[.]$", "")));

        //FIXME Non cerchiamo piu' per il codice internazionale.
//        if (getCarGroup() == null) {
//            setCarGroup(searchGroupByIntCode(sx, getHACsvRecord().getHAGroupCode()));
//        }

        if (getCarGroup() == null) {
            error("Group unknown: " + getHACsvRecord().getSupplierGroupCode() + " / " + getHACsvRecord().getHAGroupCode());
            return;
        }

        StringBuilder memo = new StringBuilder("Car group: " + getCarGroup().getCodiceNazionale() + " / " + getCarGroup().getCodiceInternazionale());
        if (getHACsvRecord().getFlightDetails() != null) {
            memo.append("\n");
            memo.append(getHACsvRecord().getFlightDetails());
        }
        if(getHACsvRecord().getRentalExtras() != null) {
            memo.append("\n");
            memo.append("Extras: ").append(getHACsvRecord().getRentalExtras());
        }
        if (getHACsvRecord().getMemorandum() != null) {
            memo.append("\n");
            memo.append("Comments: ").append(getHACsvRecord().getMemorandum());
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
                    success("New reservation: Reservation already exists.");
                } else {
                    newReservation(sx, tx, user);
                }
            } else if (getBookingStatus().equals(STATUS_MODIFY)) {
                if (getPrenotazione() == null) {
                    addWarningMessage("Amend reservation: Reservation not found: adding new.");
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
        HACsvRecord response = (HACsvRecord) getHACsvRecord().clone();
        if (getSuccess()) {
            response.setSupplierConfirmationNumber(getPrenotazione().getCodice());
        } else if (getBookingStatus() != null) {
            response.setSupplierConfirmationNumber(null);
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
