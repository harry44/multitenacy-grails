package it.myrent.ee.csv;

import it.myrent.ee.api.exception.VeicoloNonDisponibileException;
import it.myrent.ee.db.MROldFonteCommissione;
import it.myrent.ee.db.MROldPagamento;
import it.myrent.ee.db.User;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivangani on 30/08/2019.
 */
public class TJCsvConverter extends CsvConverter {

    private static final Log log = LogFactory.getLog(TJCsvConverter.class);

    /**
     *
     * @param fonteCommissione
     * @param pagamento
     * @param user
     */
    protected TJCsvConverter(MROldFonteCommissione fonteCommissione, MROldPagamento pagamento, User user) {
        super(fonteCommissione, pagamento, user);
    }

    /**
     *
     * @param sx
     * @param tx
     * @param csvInputStream
     * @throws IOException
     * @throws VeicoloNonDisponibileException
     */
    public void convert(
            Session sx,
            Transaction tx,
            InputStream csvInputStream)
            throws IOException, VeicoloNonDisponibileException {
        List<TJCsvReservation> reservations = new ArrayList<TJCsvReservation>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream, "ISO-8859-1"));
        CSVParser parser = new CSVParser(reader, getCsvStrategy());
        String[] line = parser.getLine();
        while (line != null) {
            TJCsvRecord csvRecord = null;
            try {
                csvRecord = new TJCsvRecord(line);
            } catch (Exception ex) {
                csvRecord = null;
                log.debug("Parse error for line: " + line);
            }
            if (csvRecord != null) {
                TJCsvReservation csvReservation = new TJCsvReservation(csvRecord);
                csvReservation.process(sx, tx, getFonteCommissione(), getPagamento(), getUser());
                reservations.add(csvReservation);
            }
            line = parser.getLine();
        }
        setPrenotazioni(reservations.toArray(new CsvReservation[0]));
        reader.close();
    }

    /**
     * Saves the CSV response files and returns their names
     * @return response files containing: confirmed, not confirmed, errors.
     */
    public String[] saveCSVResponse(Session sx, String originalFilename) {
        StringBuffer suffix = new StringBuffer();
        if (originalFilename != null) {
            String baseName = originalFilename.replaceAll("[.][^.]*$", ""). // Cut extension.
                    replaceAll("traveljigsaw_[0-9]+_(confirmed|not_confirmed|errors)_?", ""); // Delete old prefix
            if(baseName.length() > 0) {
                suffix.append("_").append(baseName);
            }
        }
        suffix.append(".csv");

        StringBuffer prefix = new StringBuffer().append("traveljigsaw").append("_").append(cookie()).append("_");

        String fileNameConfirmed = new StringBuffer().append(prefix).append("confirmed").append(suffix).toString().trim();
        String fileNameNotConfirmed = new StringBuffer().append(prefix).append("not_confirmed").append(suffix).toString().trim();
        String fileNameErrors = new StringBuffer().append(prefix).append("errors").append(suffix).toString().trim();

        BufferedWriter writerConfirmed = null;
        BufferedWriter writerNotConfirmed = null;
        BufferedWriter writerErrors = null;

        File fileConfirmed = new File(fileNameConfirmed);
        File fileNotConfirmed = new File(fileNameNotConfirmed);
        File fileErrors = new File(fileNameErrors);

        int confirmed = 0;
        int notConfirmed = 0;
        int errors = 0;

        try {
            writerConfirmed = new BufferedWriter(new FileWriter(fileConfirmed));
            writerNotConfirmed = new BufferedWriter(new FileWriter(fileNotConfirmed));
            writerErrors = new BufferedWriter(new FileWriter(fileErrors));

            CSVPrinter printerConfirmed = new CSVPrinter(writerConfirmed);
            CSVPrinter printerNotConfirmed = new CSVPrinter(writerNotConfirmed);
            CSVPrinter printerErrors = new CSVPrinter(writerErrors);

            printerConfirmed.setStrategy(getCsvStrategy());
            printerNotConfirmed.setStrategy(getCsvStrategy());
            printerErrors.setStrategy(getCsvStrategy());

            for (int i = 0; i < getPrenotazioni().length; i++) {
                CsvReservation reservation = getPrenotazioni()[i];
                Boolean success = reservation.getSuccess();
                CsvRecord csvRecord = getPrenotazioni()[i].getCsvResponse();
                String[] csvResponse = csvRecord.toArray();
                if (i == 0) { //|| csvRecord.getFields().length == 1*/) {
                    printerConfirmed.println(csvResponse);
                    printerNotConfirmed.println(csvResponse);
                    printerErrors.println(csvResponse);
                } else if (!success) {
                    printerErrors.println(csvResponse);
                    errors++;
                } else if (reservation.getConfirmed()) {
                    printerConfirmed.println(csvResponse);
                    confirmed++;
                } else if (!reservation.getConfirmed()) {
                    printerNotConfirmed.println(csvResponse);
                    notConfirmed++;
                }
            }
        } catch (Exception ex) {
            fileNameConfirmed = null;
            fileNameNotConfirmed = null;
            fileNameErrors = null;
            log.debug("Could not save response", ex);
            ex.printStackTrace();
        } finally {
            if (writerConfirmed != null) {
                try {
                    writerConfirmed.close();
                    if (confirmed == 0) {
                        fileNameConfirmed = null;
                        fileConfirmed.delete();
                    }
                } catch (Exception ex) {
                }
            }
            if (writerNotConfirmed != null) {
                try {
                    writerNotConfirmed.close();
                    if (notConfirmed == 0) {
                        fileNameNotConfirmed = null;
                        fileNotConfirmed.delete();
                    }
                } catch (Exception ex) {
                }
            }
            if (writerErrors != null) {
                try {
                    writerErrors.close();
                    if (errors == 0) {
                        fileNameErrors = null;
                        fileErrors.delete();
                    }
                } catch (Exception ex) {
                }
            }
        }
        return new String[]{fileNameConfirmed, fileNameNotConfirmed, fileNameErrors};
    }

    /**
     *
     * @param sx
     * @param originalFilename
     * @return
     */
    public String[] saveXLSResponse(Session sx, String originalFilename) {
        return null;
    }
}
