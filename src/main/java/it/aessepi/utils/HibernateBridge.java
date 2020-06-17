/*
 * InterfacciaHibernate.java
 *
 * Created on March 3, 2004, 9:41 AM
 */

package it.aessepi.utils;
import java.io.*;
import java.sql.Connection;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author  jamess
 */
public class HibernateBridge {
    
    
    public static String nomeUtente = "myrent"; //NOI18N
    public static String password = "myrent"; //NOI18N
    public static String indirizzo="127.0.0.1:5432"; //NOI18N
    public static String tipoDB="PostGreSQL"; //NOI18N
    public static String nomeDB="myrent"; //NOI18N
    public static String filePath;
    public static String schema = null;
    
    
    private static final ThreadLocal<Session> currentSession = new ThreadLocal<Session>();
    private static SessionFactory myFactory;
    private static Configuration cfg;
    private static boolean threadSafe = false;
    
    public static final String DB_POSTGRESQL = "PostGreSQL"; //NOI18N
    public static final String DB_HYPERSQL = "HyperSQL"; //NOI18N
    public static final String DB_ORACLE = "Oracle";
    
    private static Log log = LogFactory.getLog(HibernateBridge.class);
    
    /** Creates a new instance of HibernateBridge */
    
    private static Session session = null;
    
    private static void setSession(Session s) {
        if(isThreadSafe()) {
            currentSession.set(s);
        } else {
            session = s;
        }
    }
    
    private static Session getSession() {
        if(isThreadSafe()) {
            return currentSession.get();
        } else {
            return session;
        }
    }

    public static boolean isThreadSafe() {
        return threadSafe;
    }

    public static void setThreadSafe(boolean threadSafe) {
        HibernateBridge.threadSafe = threadSafe;
    }
    
    /*
     *
     *Crea una nuova sessione a prescindere da quella esistente
     *
     */
    
    public static Session openNewSession(){
       return myFactory.openSession(); 
    }
    
    public static boolean isSessionOpen() {
        return getSession() != null && getSession().isOpen() && getSession().isConnected();
    }
    
    public static Session startNewSession() throws HibernateException{
        Session s = getSession();
        Session retValue = null;
        if(s == null) {
            log.debug("HibernateBridge : Opening a new session for the thread has none yet."); //NOI18N
            s = myFactory.openSession();
            setSession(s);
            retValue = s;
        } else {
            if(s.isOpen() && s.isConnected()) {
                log.debug("HibernateBridge : Session open and connected. Returning it..."); //NOI18N
                retValue = s;
            } else {
                log.debug("HibernateBridge : Session not open or not connected. Will start new session."); //NOI18N
                setSession(null);
                //closeSession();
                retValue = startNewSession();
            }
        }        
        return retValue;
    }
    
    public static Connection closeSession() {
        log.debug("HibernateBridge : Closing session..."); //NOI18N
        Session s = getSession();
        setSession(null);
        Connection con = null;
        try {
//            if (s != null) {
//                con = s.close();
//                log.debug("HibernateBridge : Session closed successfully"); //NOI18N
//            } else {
//                log.debug("HibernateBridge : Session is null. Nothing to do."); //NOI18N
//            }
        }catch(HibernateException hex) {
            if(log.isDebugEnabled()) {
                log.debug("HibernateBridge : Exception encountered while closing session", hex); //NOI18N
            } else {
                log.warn("HibernateBridge : Exception encountered while closing session"); //NOI18N
            }
        }
        return con;
    }
    
    
    public static void inizializzaHibernate(Configuration c) {
        closeSessionFactory();
        try {
            setConfiguration(c);
            myFactory = c.buildSessionFactory();
        } catch (Exception ex) {
            if(log.isDebugEnabled()) {
                log.debug("HibernateBridge : Exception encountered while building session factory", ex); //NOI18N
            } else {
                log.warn("HibernateBridge : Exception encountered while building session factory"); //NOI18N
            }
        }
    }
    
    public static void inizializzaHibernate(SessionFactory sf) {
        myFactory = sf;
    }
    
    public static void closeSessionFactory() {
        if(myFactory!=null) {
            try {
                myFactory.close();
            }catch (Exception ex){
            }
        }
    }
    
    public static void setConfiguration(Configuration c) {
        cfg = c;
    }
    
    public static Configuration getConfiguration() {
        return cfg;
    }
    
    protected static Configuration updateConfiguration(Configuration myConfiguration, String tipoDB, String schema, String indirizzo, String nomeDB, String nomeUtente, String password) {
        if(schema == null || schema.equals("nessuna") || schema.length() == 0) { //NOI18N
            myConfiguration.setProperty("hibernate.hbm2ddl.auto", ""); //NOI18N
        } else {
            myConfiguration.setProperty("hibernate.hbm2ddl.auto", schema); //NOI18N
        }
        
        if(tipoDB.equals(new String("PostGreSQL"))) { //NOI18N
            myConfiguration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver"); //NOI18N
            myConfiguration.setProperty("hibernate.connection.url", "jdbc:postgresql://"+indirizzo+"/"+nomeDB); //NOI18N
            myConfiguration.setProperty("hibernate.connection.username", nomeUtente); //NOI18N
            myConfiguration.setProperty("hibernate.connection.password", password); //NOI18N
            myConfiguration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"); //NOI18N
            
        } else if(tipoDB.equals(new String("Oracle"))) { //NOI18N
            myConfiguration.setProperty("hibernate.connection.driver_class", "oracle.jdbc.driver.OracleDriver"); //NOI18N
            myConfiguration.setProperty("hibernate.connection.url", "jdbc:oracle:thin:@"+indirizzo+"/"+nomeDB); //NOI18N
            myConfiguration.setProperty("hibernate.connection.username", nomeUtente); //NOI18N
            if (password.equals("trusted")) { //NOI18N
                myConfiguration.setProperty("hibernate.connection.password", ""); //NOI18N
            } else {
                myConfiguration.setProperty("hibernate.connection.password", password); //NOI18N
            }
            myConfiguration.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect"); //NOI18N

        }else if(tipoDB.equals(new String("HyperSQL"))) { //NOI18N
            myConfiguration.setProperty("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver"); //NOI18N
            myConfiguration.setProperty("hibernate.connection.url", "jdbc:hsqldb:"+indirizzo+"/"+nomeDB); //NOI18N
            myConfiguration.setProperty("hibernate.connection.username", nomeUtente); //NOI18N
            if (password.equals("trusted")) { //NOI18N
                myConfiguration.setProperty("hibernate.connection.password", ""); //NOI18N
            } else {
                myConfiguration.setProperty("hibernate.connection.password", password); //NOI18N
            }
            myConfiguration.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect"); //NOI18N
        }
        return myConfiguration;
    }
    
    protected static Configuration updateConfiguration(Configuration myConfiguration, String datasource, String schema) {
        if (schema == null || schema.equals("nessuna") || schema.length() == 0) { //NOI18N
            myConfiguration.setProperty("hibernate.hbm2ddl.auto", ""); //NOI18N
        } else {
            myConfiguration.setProperty("hibernate.hbm2ddl.auto", schema); //NOI18N
        }

        myConfiguration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver"); //NOI18N
        myConfiguration.setProperty("hibernate.connection.datasource", datasource);        //NOI18N
        myConfiguration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"); //NOI18N


        return myConfiguration;
    }
    
}