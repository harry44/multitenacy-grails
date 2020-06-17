/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.api.utils;

import it.aessepi.utils.Parameters;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.db.LogEntry;
import it.myrent.ee.db.Loggable;
import it.myrent.ee.db.User;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LazyInitializationException;

/**
 *
 * @author jamess
 */
public class LogUtils {

    private static final Log log = LogFactory.getLog(LogUtils.class);

    /**
     *
     * @param logEntries
     * @param instance
     * @return
     */
    public static Map<String, LogEntry> startRecording(Map<String, LogEntry> logEntries, Loggable instance) {
        if (logEntries == null) {
            logEntries = new HashMap<String, LogEntry>();
        }
        for (int i = 0; i < instance.getLoggableFields().length; i++) {
            Object fieldValue = null;
            try {
             //   fieldValue = PropertyUtils.getProperty(instance, instance.getLoggableFields()[i]);
            } catch (Exception ex) {
                log.debug(ex.toString(), ex);
            }
            if (fieldValue != null && fieldValue instanceof Loggable) {
                startRecording(logEntries, (Loggable) fieldValue);
            } else {
                LogEntry logEntry = new LogEntry();
                logEntry.setEntityName(instance.getEntityName());
                logEntry.setEntityId(instance.getEntityId());
                logEntry.setFieldName(instance.getLoggableFields()[i]);
                logEntry.setFieldLabel(instance.getLoggableLabels()[i]);
                logEntry.setOldValue(valueOf(fieldValue));

                /* impostazione della sessionId */
                //logEntry.setSessionId(Parameters.getSessionId());

                String key = instance.getEntityName() + "." + instance.getLoggableFields()[i];
                logEntries.put(key, logEntry);
            }
        }

        return logEntries;
    }

    private static void updateRecording(Map<String, LogEntry> logEntries, Loggable instance) {
        /* TODO: gestito il caso in cui logEntries e' null */
//        if (logEntries != null) {
            for (int i = 0; i < instance.getLoggableFields().length; i++) {
                Object fieldValue = null;
                try {
                  //  fieldValue = PropertyUtils.getProperty(instance, instance.getLoggableFields()[i]);
                } catch (Exception ex) {
                    log.debug(ex.toString(), ex);
                }

                if (fieldValue != null && fieldValue instanceof Loggable) {
                    updateRecording(logEntries, (Loggable) fieldValue);
                } else {
                    String key = instance.getEntityName() + "." + instance.getLoggableFields()[i];
                    LogEntry logEntry = logEntries.get(key);
                    if (logEntry != null) {
                        logEntry.setNewValue(valueOf(fieldValue));
                    } else {
                        /* codice per registrare anche il log dei nuovi record */
                        /* registro la log entry solo se il nuovo valore e' <> da NULL */
                        if (fieldValue != null) {
                            logEntry = new LogEntry();
                            logEntry.setEntityName(instance.getEntityName());
                            logEntry.setEntityId(instance.getEntityId());
                            logEntry.setFieldName(instance.getLoggableFields()[i]);
                            logEntry.setFieldLabel(instance.getLoggableLabels()[i]);

                            // logEntry.setOldValue(null);
                            logEntry.setNewValue(valueOf(fieldValue));

                            /* impostazione della sessionId */
                            logEntry.setSessionId(Parameters.getSessionId());

                            /* salvo il log entry dentro la mappa */
                            logEntries.put(key, logEntry);
                        }
                    }
                }
            }
//        }
    }

    /**
     *
     * @param logEntries
     * @param instance
     */
    public static void stopRecording(Map<String, LogEntry> logEntries, Loggable instance, User user) {
        updateRecording(logEntries, instance);
        Iterator<LogEntry> entries = logEntries.values().iterator();
        Date changeDate = FormattedDate.formattedTimestamp();
        User changedBy = user;
        while (entries.hasNext()) {
            LogEntry logEntry = entries.next();

            if (logEntry.getOldValue() != null) {
                if (logEntry.getOldValue().equals(logEntry.getNewValue())) {
                    entries.remove();
                } else {
                    logEntry.setChangedBy(changedBy);
                    logEntry.setChangeDate(changeDate);
                }
            } else {
                if (logEntry.getNewValue() == null) {
                    entries.remove();
                } else {
                    logEntry.setChangedBy(changedBy);
                    logEntry.setChangeDate(changeDate);
                }
            }
        }
    }

    private static String valueOf(Object fieldValue) {
        String result = "null";
        try {

            if (fieldValue != null) {
                if (fieldValue instanceof Date) {
                    result = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(fieldValue);
                } else if (fieldValue instanceof Double) {
                    result = new DecimalFormat("#,##0.00").format(fieldValue);
                }
            }
            result = String.valueOf(fieldValue);
        } catch (LazyInitializationException ex) {
            ex.printStackTrace();
        }
        finally{
            return result;
        }
        
    }
}
