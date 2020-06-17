/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.api.utils;

import it.myrent.ee.api.preferences.Preferenze;
import it.aessepi.utils.DESEncrypter;
import it.myrent.ee.db.MROldSetting;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

/**
 *
 * @author jamess
 */
public class MailUtils {

    public static final DESEncrypter DENCRYPTER = new DESEncrypter("4983)(*4;fe][;"); //NOI18N
    public static final String MAIL_SMTP_HOST = "mail.smtp.host"; //NOI18N
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth"; //NOI18N
    public static final String MAIL_SMTP_USER = "mail.smtp.user"; //NOI18N
    public static final String MAIL_SMTP_PASS = "mail.smtp.pass"; //NOI18N
    public static final String MAIL_FROM = "mail.from"; //NOI18N
    public static final String MAIL_DEBUG = "false";
    public static final String MAIL_PROTOCOL_SMTP = "SMTP";

    public static final String MAIL_PROTOCOL_SMTP_OVER_SSL = "SMTP OVER SSL";
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
    public static final String MAIL_PROTOCOL_SMTP_STARTSSL = "SMTP WITH STARTSSL";

    private static Properties getUserMailPreferences() {
        Properties p = new Properties();
        String mailFrom = Preferenze.getPreferenze().getMailFrom();
        String mailName = Preferenze.getPreferenze().getMailName();
        Boolean mailSmtpAuth = Preferenze.getPreferenze().getMailSmtpAuth();
        String mailSmtpHost = Preferenze.getPreferenze().getMailSmtpHost();
        String mailSmtpUser = Preferenze.getPreferenze().getMailSmtpUser();
        String mailSmtpPass = DENCRYPTER.decrypt(Preferenze.getPreferenze().getMailSmtpPass());

        if (mailFrom == null || mailSmtpHost == null || (mailSmtpAuth && (mailSmtpUser == null || mailSmtpPass == null))) {
            return null;
        }
        p.put(MAIL_FROM, mailFrom);
        p.put(MAIL_SMTP_HOST, mailSmtpHost);
        p.put(MAIL_SMTP_AUTH, mailSmtpAuth.toString());
        if (mailSmtpAuth) {
            p.put(MAIL_SMTP_USER, mailSmtpUser);
            p.put(MAIL_SMTP_PASS, mailSmtpPass);
        }
        return p;
    }


    private static class SMTPAuthenticator extends javax.mail.Authenticator {

        private String username;
        private String password;

        public SMTPAuthenticator(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }

    public String doSend(org.hibernate.Session session, String[] toRecipients, String[] ccRecipients, String[] bccRecipients, String subject, String message, String[] attachments) {
        boolean success = false;
        String msg = null;
        String nomeMittente = null;
        //String[] bccRecipients = null;

        nomeMittente = Preferenze.getMailFromName(session);

        //String[] toRecipients = getRecipientAddresses(jTextFieldTo.getText());
        //String[] ccRecipients = getRecipientAddresses(jTextFieldCC.getText());

        //if (session != null && session.isOpen()) {
//            bccRecipients = getRecipientAddresses(Preferenze.getEmailbccEmailText(session));
//        } else {
//            bccRecipients = getRecipientAddresses(Preferenze.getEmailbccEmailText());
//        }

        //String subject = jTextFieldSubject.getText();
        //String message = jTextAreaMessage.getText();

//        String[] attachments = new String[jListAllegati.getModel().getSize()];
//        for(int i = 0; i < attachments.length; i++) {
//            attachments[i] = jListAllegati.getModel().getElementAt(i).toString();
//        }

//        Transaction tx = null;
        if(ccRecipients==null){
            ccRecipients = new String[]{};
        }
        if(bccRecipients == null){
            bccRecipients = new String[]{};
        }

        if(attachments == null){
            attachments = new String[]{};
        }
        try {
            success = MailUtils.sendEmail(true, session, nomeMittente, subject, message, attachments, toRecipients, ccRecipients, bccRecipients);
            if (success) {
                //salva i riferimenti all'invio della email
//                if (getEmailDocFis() != null) {
//                    getEmailDocFis().setDataInvio(new Date());
//                    getEmailDocFis().setDestinatari(Arrays.toString(toRecipients));
//                    getEmailDocFis().setDestinatariCc(Arrays.toString(ccRecipients));
//                    getEmailDocFis().setDestinatariBcc(Arrays.toString(bccRecipients));
//                    getEmailDocFis().setEmailMittente(session!=null?Preferenze.getSettingValue(session,Setting.MAIL_FROM):Preferenze.getSettingValue(Setting.MAIL_FROM));
//                    getEmailDocFis().setInviato(true);
//                    getEmailDocFis().setUser( (User) Parameters.getUser() );
//
//                    if (session != null && session.isOpen()) {
//                        tx = session.beginTransaction();
//                    } else {
//                        session = HibernateBridge.startNewSession();
//                        tx = session.beginTransaction();
//                    }
//
//                    session.saveOrUpdate(getEmailDocFis());
//                    tx.commit();
//                }
                msg = "The message was sent successfully";
                //JOptionPane.showMessageDialog(this, bundle.getString("JDialogEmail.msgMessaggioInviato"));
            } else {
                //JOptionPane.showMessageDialog(this, bundle.getString("JDialogEmail.msgMessaggioNonInviatoVerificarePreferenze"));
                msg = "The message has not been sent.\nPlease check user email preferences";
            }
        } catch (Exception ex) {
//            if (tx != null) {
//                tx.rollback();
//            }
//            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//            JOptionPane.showMessageDialog(
//                    this,
//                    MessageFormat.format(bundle.getString("JDialogEmail.msgInvioFallitoMessaggio0"), ex),
//                    bundle.getString("JDialogEmail.msgInvioFallito"),
//                    JOptionPane.ERROR_MESSAGE);
//            log.debug("Invio fallito.", ex); //NOI18N
//            ex.printStackTrace();
            msg = "Send failed. Error message received is: "+ex;
        }
//        if (success) {
//            setVisible(false);
//            dispose();
//        }
        return msg;
    }
    /**
     *
     * @param useSystemPreferences
     * @param sx
     * @param nomeMittente
     * @param oggetto
     * @param messaggio
     * @param attachments
     * @param toRecipients
     * @param ccRecipients
     * @param bccRecipients
     * @return
     * @throws AddressException
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public static boolean sendEmail(boolean useSystemPreferences, org.hibernate.Session sx, String nomeMittente, String oggetto, String messaggio, String[] attachments, String[] toRecipients, String[] ccRecipients, String[] bccRecipients) throws AddressException, MessagingException, UnsupportedEncodingException {
        Properties p = getMailProperties(useSystemPreferences, sx);
        return sendEmail(p, nomeMittente, oggetto, messaggio, attachments, toRecipients, ccRecipients, bccRecipients, sx);
    }

    private static Properties getMailProperties(boolean useSystemPreferences, org.hibernate.Session sx) {
        if (useSystemPreferences) {
            return getSystemMailPreferences(sx);
        }
        return null;
    }

    private static Properties getSystemMailPreferences(org.hibernate.Session sx) {
        Properties p = new Properties();
        String mailFrom = Preferenze.getSettingValue(sx, MROldSetting.MAIL_FROM);
        Boolean mailSmtpAuth = new Boolean(Preferenze.getSettingValue(sx, MROldSetting.MAIL_SMTP_AUTH));
        String mailSmtpHost = Preferenze.getSettingValue(sx, MROldSetting.MAIL_SMTP_HOST);
        String mailSmtpPort = Preferenze.getSettingValue(sx, MROldSetting.MAIL_SMTP_PORT);
        String mailSmtpUser = Preferenze.getSettingValue(sx, MROldSetting.MAIL_SMTP_USER);
        String mailSmtpPass = Preferenze.getSettingValue(sx, MROldSetting.MAIL_SMTP_PASS);
        String mailSmtpProtocol = Preferenze.getSettingValue(sx,MROldSetting.MAIL_SMTP_PROTOCOL);

        p.put("mail.debug", MAIL_DEBUG);

        if (mailFrom == null || mailSmtpHost == null || (mailSmtpAuth && (mailSmtpUser == null || mailSmtpPass == null))) {
            return null;
        }
        p.put(MAIL_FROM, mailFrom);
        p.put(MAIL_SMTP_HOST, mailSmtpHost);
        if (mailSmtpPort != null) {
            p.put(MAIL_SMTP_PORT, mailSmtpPort);
        }


        /* gestione del protocollo: smtp, startssl, smtp over ssl */
        if (mailSmtpProtocol != null && !mailSmtpProtocol.equals(MAIL_PROTOCOL_SMTP)) {
            if (mailSmtpProtocol.equals(MAIL_PROTOCOL_SMTP_OVER_SSL)) {
                p.put("mail.smtp.socketFactory.port", mailSmtpPort);
                p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }
            if (mailSmtpProtocol.equals(MAIL_PROTOCOL_SMTP_STARTSSL)) {
                p.put("mail.smtp.starttls.enable", "true");
                p.put("mail.smtp.socketFactory.port", mailSmtpPort);
                p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }
        }

        p.put(MAIL_SMTP_AUTH, mailSmtpAuth.toString());
        if (mailSmtpAuth) {
            p.put(MAIL_SMTP_USER, mailSmtpUser);
            p.put(MAIL_SMTP_PASS, mailSmtpPass);
        }
        return p;
    }

    private static boolean sendEmail(Properties p, String nomeMittente, String oggetto, String messaggio, String[] attachments, String[] toRecipients, String[] ccRecipients, String[] bccRecipients, org.hibernate.Session sx) throws AddressException, MessagingException, UnsupportedEncodingException {
        if (p == null) {
            return false;
        }
        boolean success = true;
        Session session = null;

        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);

        if (new Boolean(p.getProperty(MAIL_SMTP_AUTH))) {
            Authenticator auth = new SMTPAuthenticator(p.getProperty(MAIL_SMTP_USER), p.getProperty(MAIL_SMTP_PASS));
            session = Session.getInstance(p, auth);
        } else {
            session = Session.getInstance(p);
        }

        Message message = new MimeMessage(session);

        InternetAddress[] toAddress = new InternetAddress[toRecipients.length];
        InternetAddress[] ccAddress = new InternetAddress[ccRecipients.length];
        InternetAddress[] bccAddress = new InternetAddress[bccRecipients.length];

        for (int i = 0; i < toRecipients.length; i++) {
            toAddress[i] = new InternetAddress(toRecipients[i]);
        }
        for (int i = 0; i < ccRecipients.length; i++) {
            ccAddress[i] = new InternetAddress(ccRecipients[i]);
        }
        for (int i = 0; i < bccRecipients.length; i++) {
            bccAddress[i] = new InternetAddress(bccRecipients[i]);
        }
        message.setRecipients(Message.RecipientType.TO, toAddress);
        message.setRecipients(Message.RecipientType.CC, ccAddress);
        message.setRecipients(Message.RecipientType.BCC, bccAddress);
        if (nomeMittente != null) {
            message.setFrom(new InternetAddress(p.getProperty(MAIL_FROM), nomeMittente));
        }
        message.setSubject(oggetto);

        Multipart mp = new MimeMultipart();
        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setText(messaggio, "utf-8");
        mp.addBodyPart(mbp1);

        ////////////////Attached Files:
        for (int i = 0; i < attachments.length; i++) {
            try {
                MimeBodyPart mbp2 = new MimeBodyPart();

                FileDataSource fds = null;
                //File fileAttachment = null;

                System.out.println("File da inviare: " + attachments[i]);
                if (attachments[i].startsWith("/tmp/") || attachments[i].startsWith("C:")) {
                    fds = new FileDataSource(attachments[i]);
                    //fileAttachment = new File(attachments[i]);
                } else {

                    String OS = System.getProperty("os.name").toLowerCase();
                    if (OS.indexOf("mac") >= 0)
                        fds = new FileDataSource(attachments[i]);
                    else
                        fds = new FileDataSource(System.getProperty("java.io.tmpdir") + File.separator +attachments[i]);
                    //Before fds = new FileDataSource(System.getProperty("java.io.tmpdir") + File.separator +attachments[i]);
                    //
                    // Line above commented for Mac and modified in the line below
                    //

                    //fileAttachment = new File(System.getProperty("java.io.tmpdir") + File.separator + attachments[i]);
                }
                System.out.println("FileName "+fds.getName());

                mbp2.setDataHandler(new DataHandler(fds));
                mbp2.setDisposition(Part.ATTACHMENT);
                mbp2.setFileName(fds.getName());
                mp.addBodyPart(mbp2);
                //mbp2.attachFile(fileAttachment);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        // add the Multipart to the message
        message.setContent(mp, "utf-8");

        message.setSentDate(new Date());

        try {
            Thread.currentThread().setContextClassLoader(MailUtils.class.getClassLoader());
            System.out.println("ClassLoader EMAIL: " + MailUtils.class.getClassLoader());
            Transport transport = session.getTransport(toAddress[0]);
            transport.connect(Preferenze.getSettingValue(sx, MROldSetting.MAIL_SMTP_HOST), 465, Preferenze.getSettingValue(sx, MROldSetting.MAIL_SMTP_USER), Preferenze.getSettingValue(sx, MROldSetting.MAIL_SMTP_PASS));

            //transport.connect();
            transport.send(message);
            transport.close();
        } catch (Exception ex) {
            success=false;
            ex.printStackTrace();
        }
        return success;
    }
}
