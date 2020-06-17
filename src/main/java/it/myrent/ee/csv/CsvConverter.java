package it.myrent.ee.csv;

import it.myrent.ee.api.exception.VeicoloNonDisponibileException;
import it.myrent.ee.api.utils.MailUtils;
import it.myrent.ee.db.MROldFonteCommissione;
import it.myrent.ee.db.MROldPagamento;
import it.myrent.ee.db.User;
import org.apache.commons.csv.CSVStrategy;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by shivangani on 30/08/2019.
 */
public abstract class CsvConverter {

    /**
     *
     * @param fonteCommissione
     * @param pagamento
     * @param user
     */
    protected CsvConverter(MROldFonteCommissione fonteCommissione, MROldPagamento pagamento, User user) {
        setFonteCommissione(fonteCommissione);
        setPagamento(pagamento);
        setUser(user);
    }
    private MROldFonteCommissione fonteCommissione;
    private MROldPagamento pagamento;
    private User user;
    private CsvReservation[] prenotazioni;
    private CSVStrategy csvStrategy = CSVStrategy.EXCEL_STRATEGY;

    /**
     *
     * @return
     */
    public CsvReservation[] getPrenotazioni() {
        return prenotazioni;
    }

    /**
     *
     * @param prenotazioni
     */
    public void setPrenotazioni(CsvReservation[] prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    /**
     *
     * @return
     */
    public MROldFonteCommissione getFonteCommissione() {
        return fonteCommissione;
    }

    /**
     *
     * @param fonteCommissione
     */
    public void setFonteCommissione(MROldFonteCommissione fonteCommissione) {
        this.fonteCommissione = fonteCommissione;
    }

    /**
     *
     * @return
     */
    public MROldPagamento getPagamento() {
        return pagamento;
    }

    /**
     *
     * @param pagamento
     */
    public void setPagamento(MROldPagamento pagamento) {
        this.pagamento = pagamento;
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
    public CSVStrategy getCsvStrategy() {
        return csvStrategy;
    }

    /**
     *
     * @param csvStrategy
     */
    public void setCsvStrategy(CSVStrategy csvStrategy) {
        this.csvStrategy = csvStrategy;
    }

    /**
     *
     * @param sx
     * @param tx
     * @param csvInputStream
     * @throws IOException
     * @throws VeicoloNonDisponibileException
     */
    public abstract void convert(
            Session sx,
            Transaction tx,
            InputStream csvInputStream) throws IOException, VeicoloNonDisponibileException;

    /**
     *
     * @param sx
     * @param originalFilename
     * @return
     */
    public abstract String[] saveCSVResponse(Session sx, String originalFilename);

    /**
     *
     * @param sx
     * @param originalFilename
     * @return
     */
    public abstract String[] saveXLSResponse(Session sx, String originalFilename);

    /**
     *
     * @return
     */
    public static String cookie() {
        return new DecimalFormat("00000").format(new Random().nextInt((int)Math.pow(2, 16)));
    }

    /**
     *
     * @param fonteCommissione
     * @param pagamento
     * @param user
     * @return
     */
    public static CsvConverter newInstance(
            MROldFonteCommissione fonteCommissione,
            MROldPagamento pagamento,
            User user) {
        if (fonteCommissione.getRagioneSociale().toLowerCase().matches("holiday\\s*autos")) {
            return new HACsvConverter(fonteCommissione, pagamento, user);
        } else if (fonteCommissione.getRagioneSociale().toLowerCase().matches("travel\\s*jigsaw\\s*h")) {
            return new TJCsvConverter(fonteCommissione, pagamento, user);
        } else if (fonteCommissione.getRagioneSociale().toLowerCase().contains("traveljigsaw")) {
            return new TJCsvConverter(fonteCommissione, pagamento, user);
        } else {
            return new TJCsvConverter(fonteCommissione, pagamento, user);
        }

    }

    /**
     *
     * @param sx
     * @param attachments
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws AddressException
     */
    public boolean sendConfirmationToBroker(Session sx, String[] attachments) throws MessagingException, UnsupportedEncodingException, AddressException {
        String nomeMittente = "Booking Service " + getUser().getAffiliato();
        String oggetto = "Bookings - " + getFonteCommissione() + " - confirmed";
        String messaggio =
                "Dear Partner,\n\n" +
                        "We confirm your reservation request.\n\n" +
                        "Please find the list of the confirmed reservations together with\n" +
                        "their confirmation numbers in the attached document.\n\n" +
                        "Thank you!";

//        //TODO Indirizzo booking office.
//        String[] toRecipients = new String[]{Preferenze.getSettingValue(sx, Setting.MAIL_FROM)};
////        if (getFonteCommissione().getEmail() != null) {
////            toRecipients = getFonteCommissione().getEmail().split(",");
////        }
//        //TODO Indirizzo raffaella.
//        String[] ccRecipients = new String[]{"raffaella.tavazza@locauto.it"};
//        //TODO Indirizzo assistenza
//        String[] bccRecipients = new String[]{"locauto@myrent.it"};

        //TODO Remove test addresses
        String[] toRecipients = new String[]{"mauro.chiarugi@dogmasystems.it"};
        String[] ccRecipients = new String[]{};
        String[] bccRecipients = new String[]{};

        return MailUtils.sendEmail(
                true,
                sx,
                nomeMittente,
                oggetto,
                messaggio,
                attachments,
                toRecipients,
                ccRecipients,
                bccRecipients);

    }

    /**
     *
     * @param sx
     * @param attachments
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws AddressException
     */
    public boolean sendNotConfirmedToBookingOffice(Session sx, String[] attachments) throws MessagingException, UnsupportedEncodingException, AddressException {
        String nomeMittente = "Booking Service " + getUser().getAffiliato();
        String oggetto = "Bookings - " + getFonteCommissione() + " - not confirmed";
        String messaggio =
                "This message contains reservations that were not confirmed, and must be confirmed by an operator.\n\n" +
                        "Please edit the supplier reservation number (or reference) field in the attached file and:\n" +
                        " - LEAVE UNCHANGED if the reservation is CONFIRMED.\n" +
                        " - DELETE the reservation number if the reservation is NOT CONFIRMED.\n" +
                        "Finally, send the resulting file by email to the broker.\n\n" +
                        "Thank you!";
//        //TODO Indirizzo booking office.
//        String[] toRecipients = new String[]{Preferenze.getSettingValue(sx, Setting.MAIL_FROM)};
//        String[] ccRecipients = new String[]{"raffaella.tavazza@locauto.it"};
//        //TODO Indirizzo assistenza
//        String[] bccRecipients = new String[]{"locauto@myrent.it"};


        //TODO Remove test addresses
        String[] toRecipients = new String[]{"mauro.chiarugi@dogmasystems.it"};
        String[] ccRecipients = new String[]{};
        String[] bccRecipients = new String[]{};

        return MailUtils.sendEmail(
                true,
                sx,
                nomeMittente,
                oggetto,
                messaggio,
                attachments,
                toRecipients,
                ccRecipients,
                bccRecipients);
    }

    /**
     *
     * @param sx
     * @param attachments
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws AddressException
     */
    public boolean sendErrorsToBookingOffice(Session sx, String[] attachments) throws MessagingException, UnsupportedEncodingException, AddressException {
        String nomeMittente = "Booking Service " + getUser().getAffiliato();
        String oggetto = "Bookings - " + getFonteCommissione() + " - errors";
        String messaggio =
                "This message contains reservations that were not inserted due to errors, " +
                        "and must be inserted and confirmed by an operator.\n\n" +
                        "Please edit the supplier reservation number (or reference) field in the attached file and:\n" +
                        " - FILL IN the confirmation number using the format 'NUMBER-YEAR' if the reservation is CONFIRMED.\n" +
                        " - LEAVE BLANK if the reservation is NOT CONFIRMED.\n\n" +
                        "In order to avoid confirmation number duplication, please use the format NUMBER-YEAR for " +
                        "the confirmation numbers. For example, the reservation number 8129 made on March 30th 2009 should " +
                        "have the confirmation number: 8129-2009.\n\n" +
                        "Finally, send the resulting file by email to the broker.\n\n" +
                        "Thank you!";
//        // TODO Indirizzo booking office.
//        String[] toRecipients = new String[]{Preferenze.getSettingValue(sx, Setting.MAIL_FROM)};
//        String[] ccRecipients = new String[]{"raffaella.tavazza@locauto.it"};
//        // TODO Indirizzo assistenza
//        String[] bccRecipients = new String[]{"locauto@myrent.it"};

        //TODO Remove test addresses
        String[] toRecipients = new String[]{"mauro.chiarugi@dogmasystems.it"};
        String[] ccRecipients = new String[]{};
        String[] bccRecipients = new String[]{};

        return MailUtils.sendEmail(
                true,
                sx,
                nomeMittente,
                oggetto,
                messaggio,
                attachments,
                toRecipients,
                ccRecipients,
                bccRecipients);
    }
}
