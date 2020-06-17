/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.api.exception;

import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.exception.LockAcquisitionException;
import org.hibernate.exception.SQLGrammarException;

/**
 *
 * @author bogdan
 */
public class DatabaseException extends TitledException {

    public DatabaseException(Throwable cause) {
        super(bundle.getString("database_exception_message"), bundle.getString("database_exception_title"), cause);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, bundle.getString("database_exception_title"), cause);
    }

    public DatabaseException(String message, String title, Throwable cause) {
        super(message, title, cause);
    }

    @Override
    public String getMessage() {
        String message = processHibernateException(getCause());
        if (message != null) {
            return new StringBuffer().append(getMessage()).append("\n").append(message).toString();
        }
        return super.getMessage();
    }
    
    private static final Log log = LogFactory.getLog(DatabaseException.class);

    private static String processHibernateException(Throwable cause) {
        String retValue = null;
        if (cause != null) {
            log.debug("DatabaseException : HibernateException not null. Analyzing type"); //NOI18N
            if (cause instanceof SQLException) {
                retValue = processSQLException((SQLException) cause);
            } else if (cause instanceof ConstraintViolationException) {
                log.debug("DatabaseException : ConstraintViolationException"); //NOI18N
                retValue = processSQLException(((ConstraintViolationException) cause).getSQLException());
            } else if (cause instanceof GenericJDBCException) {
                log.debug("DatabaseException : GenericJDBCException"); //NOI18N
                retValue = processSQLException(((GenericJDBCException) cause).getSQLException());
            } else if (cause instanceof JDBCConnectionException) {
                log.debug("DatabaseException : JDBCConnectionException"); //NOI18N
                retValue = processJDBCConnectionException((JDBCConnectionException) cause);
            } else if (cause instanceof LockAcquisitionException) {
                log.debug("DatabaseException : LockAcquisitionException"); //NOI18N
                retValue = processLockAcquisitionException((LockAcquisitionException) cause);
            } else if (cause instanceof SQLGrammarException) {
                log.debug("DatabaseException : SQLGrammarException"); //NOI18N
                retValue = processSQLGrammarException((SQLGrammarException) cause);
            }
        }
        return retValue;
    }

    private static String processSQLException(SQLException sqlException) {
        log.debug("DatabaseException : Processing SQLException. Checking next exception"); //NOI18N
        if (sqlException != null && sqlException.getNextException() != null) {
            log.debug("DatabaseException : Processing next exception"); //NOI18N
            return processNextSQLException(sqlException.getNextException());
        } else {
            log.debug("DatabaseException : Next exception is null. Exiting..."); //NOI18N
            return null;
        }
    }

    private static String processNextSQLException(SQLException sqlException) {
        String message = sqlException.getMessage();
        log.debug("DatabaseException : message is " + message); //NOI18N
        String tableName = null;
        String errorMessage = null;
        boolean uniqueViolation = false;
        if (message.indexOf("violates foreign key constraint") != -1) { //NOI18N
            log.debug("DatabaseException : violates foreign key constraint found"); //NOI18N
            int newLineIndex = message.indexOf('\n'); //NOI18N
            if (newLineIndex != -1) { // Il messaggio ha piu' righe. Prendiamo solo la prima riga
                message = message.substring(0, newLineIndex);
            }
            String[] words = message.split(" "); //NOI18N
            tableName = words[words.length - 1].trim();
            if (tableName.indexOf("\"") != -1) { //NOI18N
                tableName = tableName.substring(1, tableName.length() - 1);
            }
        } else if (message.indexOf("referential integrity violation") != -1) { //NOI18N
            log.debug("DatabaseException : referential integrity violation found");             //NOI18N
            String[] words = message.split(" "); //NOI18N
            tableName = words[words.length - 1].trim();
        } else if ((message.indexOf("duplicate key") != -1 && message.indexOf("unique") != -1) || message.indexOf("chiave duplicata viola il vincolo") != -1) { //NOI18N
            log.debug("DatabaseException : duplicate key found"); //NOI18N
            uniqueViolation = true;
        }
        if (tableName != null) {
            String humanTableName = TableName.get(tableName);
            if (humanTableName == null) {
                humanTableName = tableName;
            }
            errorMessage = bundle.getString("delete_error_referencedobject") //NOI18N
                    + humanTableName + ".";             //NOI18N
        } else if (uniqueViolation) {
            errorMessage = bundle.getString("error_unique_constraint_violation"); //NOI18N
        }
        return errorMessage;
    }

    private static String processJDBCConnectionException(JDBCConnectionException JDBCConEx) {
        return bundle.getString("error_connection"); //NOI18N
    }

    private static String processLockAcquisitionException(LockAcquisitionException JDBCAcq) {
        return bundle.getString("error_lockacquisition") + "\n" + bundle.getString("information_trylater"); //NOI18N
    }

    private static String processSQLGrammarException(SQLGrammarException sqlGramEx) {
        return bundle.getString("error_sql") + "\n" + bundle.getString("contact"); //NOI18N
    }
}
