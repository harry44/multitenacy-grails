/*
 * DatabaseUpdate.java
 *
 * Created on 14 iunie 2006, 10:51
 *
 */
package it.aessepi.myrentcs.utils;

import it.aessepi.utils.BundleUtils;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DatabaseUpdate {


    public DatabaseUpdate() {
    }
    private static final Log log = LogFactory.getLog(DatabaseUpdate.class);
    private static boolean updateSucceeded = true;
    public static Boolean initializeDB( Statement st) {
        Boolean dbUpdate=true;

        try {
            VersionUtils.aggiornaIdentifierSequences(st);

            info( bundle.getString("DatabaseUpdate.msgVerificaNazioni"));
            initializeNazioniISO(st);

            info( bundle.getString("DatabaseUpdate.msgVerificaComuni"));
            initializeComuni(st);

            info(bundle.getString("DatabaseUpdate.msgVerificaCodiciFiscali"));
            initializeCodiciFiscali(st);

            info( bundle.getString("DatabaseUpdate.msgVerificaPianoDeiConti"));
            initializePianoDeiConti(st);

            info(bundle.getString("DatabaseUpdate.msgVerificaCodiciIVA"));
            initializeCodiciIva(st);

            info( bundle.getString("DatabaseUpdate.msgVerificaDocumenti"));
            initializeDocumenti(st);

            info(bundle.getString("DatabaseUpdate.msgVerificaCarteDiCredito"));
            initializeCarteDiCredito(st);

            info(bundle.getString("DatabaseUpdate.msgVerificaPagamenti"));
            initializeMezziPagamenti(st);
            initializePagamenti(st);

            info(bundle.getString("DatabaseUpdate.msgVerificaRegistriIva"));
            initializeRegistriIva(st);

            info(bundle.getString("DatabaseUpdate.msgVerificaCausaliPrimanota"));
            initializeCausaliPrimanota(st);
            initializeRigheCausaliPrimanota(st);

            info(bundle.getString("DatabaseUpdate.msgVerificaRisorse"));
            initializeResources(st);

            info(bundle.getString("DatabaseUpdate.msgVerificaCausaliMovimenti"));
            initializeCausaliMovimento(st);

//            info(parent, bundle.getString("DatabaseUpdate.msgVerificaCurrency"));
//            initializeCurrency(st);

            info(bundle.getString("DatabaseUpdate.msgVerificaMyRentSignature"));
            initializeMyRentSignature(st);

//            Iterator<Plugin> plugins = JDialogStart.getPlugins().values().iterator();
//            while (plugins.hasNext()) {
//                Plugin plugin = plugins.next();
//                info(parent, MessageFormat.format(bundle.getString("DatabaseUpdate.msgVerifica0"), plugin.getPluginName()));
//                plugin.initializeDatabase(st);
//            }

            info(bundle.getString("DatabaseUpdate.msgSalvataggioDati"));

            int[] updates = st.executeBatch();
            log.debug("Updates done: " + updates.length); //NOI18N
        } catch (java.sql.SQLException sqex) {
            dbUpdate=false;
            error(bundle.getString("DatabaseUpdate.msgAggiornamentoDatabaseFallito"));
            sqex.printStackTrace();
            if (sqex.getNextException() != null) {
                sqex.getNextException().printStackTrace();
            }
            log.error("Database update failed."); //NOI18N
            log.debug("SQLException encountered when executing update batch", sqex); //NOI18N
        } catch (Exception ex) {
            dbUpdate=false;
            error(bundle.getString("DatabaseUpdate.msgAggiornamentoDatabaseFallito"));
            log.warn("Unknown exception while updating database."); //NOI18N
            log.debug("Unknown Exception encountered when executing update batch", ex); //NOI18N
        }
        return dbUpdate;
    }

   private static void info(String message) {
        sleep(200);
    }

    private static void error(String message) {
        //JMessage2.showMessage(parent, null, message, 10, true);
    }
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");

    /**
     *
     * @param ms
     */
    public static void sleep(int ms) {
        try {
            Thread.currentThread().sleep(ms);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     */
    public static void sleep() {
        sleep(200);
    }

    private static void initializeNazioniISO(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(nome) FROM nazioni_iso where codice3 is not null"); //NOI18N
        if (rs.next() && rs.getInt(1) != 246) {
            st.addBatch("delete from nazioni_iso");
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/nazioni_iso3", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static void initializeCodiciFiscali(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(luogo) FROM codici_fiscali"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/codici_fiscali", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static void initializeComuni(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(id) FROM comuni"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/comuni", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static void initializePianoDeiConti(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(id) FROM piano_dei_conti"); //NOI18N
        if (rs.next()) {
            int count = rs.getInt(1);
            if (count == 0) {
                Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/piano_dei_conti", ".sql"); //NOI18N
                while (it.hasNext()) {
                    String sql = (String) it.next();
                    st.addBatch(sql);
                }
                it.remove();
            }
        }
    }

    private static void initializeCodiciIva(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(id) FROM codiciiva"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/codiciiva", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static void initializeDocumenti(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(descrizione) FROM documenti"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/documenti", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static void initializeCarteDiCredito(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(descrizione) FROM tipi_carta_di_credito"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/tipi_carta_di_credito", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        } else {
            rs = st.executeQuery("SELECT count(descrizione) FROM tipi_carta_di_credito WHERE descrizione = 'VISA ELECTRON'"); //NOI18N
            if (rs.next() && rs.getInt(1) == 0) {
                String sql = "INSERT INTO tipi_carta_di_credito(descrizione) VALUES('VISA ELECTRON')";
                st.addBatch(sql);
            }
        }
    }

    private static void initializePagamenti(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(id) FROM pagamenti"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/pagamenti", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static void initializeMezziPagamenti(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(id) FROM mezzi_pagamenti"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/mezzi_pagamenti", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static void initializeRegistriIva(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(id) FROM registri_iva"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/registri_iva", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static void initializeCausaliPrimanota(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(codice) FROM causali_primanota"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/causali_primanota", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static void initializeRigheCausaliPrimanota(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(id) FROM righe_causale"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/righe_causale", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static void initializeOptionals(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(id) FROM optionals"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/optionals", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static void initializeResources(Statement st) throws SQLException {
        ResultSet rs1 = st.executeQuery("SELECT * FROM resources"); //NOI18N
        ResultSetMetaData rsmd = rs1.getMetaData();

        boolean id = false;
        for (int i = 1; i <= rsmd.getColumnCount(); i++) {
            if (rsmd.getColumnName(i).equals("id")) { //NOI18N
                log.debug("Trovata la colonna resources.id"); //NOI18N
                id = true;
            }
        }

        if (id) {
            st.addBatch("DROP TABLE resources CASCADE"); //NOI18N
            st.addBatch("DROP TABLE permessi_utente CASCADE"); //NOI18N
            st.addBatch("CREATE TABLE resources (descrizione varchar primary key)"); //NOI18N
            st.addBatch("CREATE TABLE permessi_utente (" + //NOI18N
                    "id integer NOT NULL, " + //NOI18N
                    "permesso integer, " + //NOI18N
                    "resource varchar NOT NULL, " + //NOI18N
                    "CONSTRAINT permessi_utente_pkey PRIMARY KEY (id, resource), " + //NOI18N
                    "CONSTRAINT temporary_user_constraint FOREIGN KEY (id) REFERENCES users (id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION)");             //NOI18N
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/resources", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        } else {
            ResultSet rs = st.executeQuery("SELECT count(descrizione) FROM resources"); //NOI18N
            if (rs.next() && rs.getInt(1) == 0) {
                Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/resources", ".sql"); //NOI18N
                while (it.hasNext()) {
                    String sql = (String) it.next();
                    st.addBatch(sql);
                }
                it.remove();
            }
        }
    }

    private static void initializeCausaliMovimento(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(id) FROM causali_movimento"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/causali_movimento", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static void initializeMyRentSignature(Statement st) throws SQLException {
        ResultSet rs = st.executeQuery("SELECT count(id) FROM documenti_contratto_noleggio"); //NOI18N
        if (rs.next() && rs.getInt(1) == 0) {
            Iterator it = openTextResource("/it/aessepi/myrentcs/utils/sql/documenti_contratto_noleggio", ".sql"); //NOI18N
            while (it.hasNext()) {
                String sql = (String) it.next();
                st.addBatch(sql);
            }
            it.remove();
        }
    }

    private static class FileIterator implements Iterator {

        private BufferedReader reader;
        String currentLine = null;

        public Object next() {
            String retValue = currentLine;
            try {
                currentLine = reader.readLine();
            } catch (Exception ex) {
                log.error("DatabaseUpdate.FileIterator : IOException, unable to read data from stream.", ex); //NOI18N
                currentLine = null;
            }
            return retValue;
        }

        public boolean hasNext() {
            return (currentLine != null);
        }

        public void remove() {
            try {
                reader.close();
            } catch (Exception ex) {
                log.error("DatabaseUpdate.FileIterator : IOException, unable to close stream.", ex); //NOI18N
            }
        }
    }

    private static Iterator openLocalizedTextResource(String resource) {
        InputStream in = DatabaseUpdate.class.getResourceAsStream(resource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        FileIterator it = new FileIterator();
        it.reader = reader;
        it.next();
        return it;
    }

    public static Iterator openTextResource(String basename, String extension) {
        try {
            String resource = basename;
            String language = Locale.getDefault().getLanguage();
            resource += "_" + language; //NOI18N
            if (extension != null) {
                resource += extension;
            }
            return openLocalizedTextResource(resource);
        } catch (Exception ex) {
            if (extension != null) {
                return openLocalizedTextResource(basename + extension);
            } else {
                return openLocalizedTextResource(basename);
            }
        }
    }

}
