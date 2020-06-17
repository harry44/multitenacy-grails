package it.myrent.ee.api.utils;

import it.aessepi.utils.DESEncrypter;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.db.User;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shivangani on 7/30/2018.
 */
public class PasswordUtils {

    private static DESEncrypter dencrypter = new DESEncrypter("utenti_myrent"); //NOI18N

    /**
     *
     * @param sx
     * @param user
     * @param password
     * @return
     */
    public static Boolean checkPasswordSecurity(Session sx, User user, String password) {
        if (!Preferenze.getEnableUserSecurity(sx)) {
            return true;
        }

        password = dencrypter.decrypt(password);

        if (checkLunghezzaPassword(sx, user, password)
                && checkCriteri(sx, user, password)
                && checkCriteriBuonSenso(sx, user, password)
                && checkUltimePassword(sx, user, password)) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean checkLunghezzaPassword(Session sx, User user, String password) {
        if (user!=null && user.isAdministrator()) {
            int lenght = Preferenze.getPasswordMinLenghtAdmin(sx);

            if (lenght == 0) {
                return true;
            }

            if (password != null && password.length() >= lenght) {
                return true;
            } else {
                return false;
            }
        } else {
            int lenght = Preferenze.getPasswordMinLenghtUser(sx);

            if (lenght == 0) {
                return true;
            }

            if (password != null && password.length() >= lenght) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static Boolean checkCriteri(Session sx, User user, String password) {
        int criteri_applicati = 0;

        Pattern patternNumber = Pattern.compile("[0-9]+");
        Pattern patternLower = Pattern.compile("[a-z]+");
        Pattern patternUpper = Pattern.compile("[A-Z]+");
        Pattern patternSpecial = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher matcherNumber = patternNumber.matcher(password);
        Matcher matcherLower = patternLower.matcher(password);
        Matcher matcherUpper = patternUpper.matcher(password);
        Matcher matcherSpecial = patternSpecial.matcher(password);

        if (matcherNumber.find()) {//(password.matches(".*\\d.*")) {
            criteri_applicati++;
        }

        if (matcherLower.find()) {
            criteri_applicati++;
        }

        if (matcherUpper.find()) {
            criteri_applicati++;
        }

        if (matcherSpecial.find()) {//(password.matches("[^A-Za-z0-9]+$")) {
            criteri_applicati++;
        }

        if (criteri_applicati >= 3) {
            return true;
        } else {
            return false;
        }
    }

    public static Boolean checkCriteriBuonSenso(Session sx, User user, String password) {
        if (user.getUserName() != null && password.toLowerCase().contains(user.getUserName().toLowerCase())) {
            return false;
        }

        if (user.getNomeCognome() != null && (!user.getNomeCognome().equals(""))  && password.toLowerCase().contains(user.getNomeCognome().toLowerCase())) {
            return false;
        }

        if (user.getEmail() != null &&  (!user.getEmail().equals("")) && password.toLowerCase().contains(user.getEmail().toLowerCase())) {
            return false;
        }

        return true;
    }

    public static Boolean checkUltimePassword(Session sx, User user, String password) {
        int numeroPasswordDaRecuperare = 0;

        if (user.isAdministrator()) {
            numeroPasswordDaRecuperare = Preferenze.getMinCyclesChangePasswordsAdmin(sx);
        } else {
            numeroPasswordDaRecuperare = Preferenze.getMinCyclesChangePasswordsUser(sx);
        }

        if (numeroPasswordDaRecuperare == 0) {
            return true;
        }

        String hqlUltimePassword = ""
                + "SELECT pwd.password "
                + "FROM MROldPassword pwd "
                + "WHERE pwd.utente = :param_utente "
                + "ORDER BY pwd.dataInserimento DESC ";

        ArrayList<String> listaUltimePassword = (ArrayList<String>) sx.createQuery(hqlUltimePassword)
                .setParameter("param_utente", user)
                .setMaxResults(numeroPasswordDaRecuperare)
                .list();

        for (String pass : listaUltimePassword) {
            String passDecrypted = dencrypter.decrypt(pass);

            if (passDecrypted != null && passDecrypted.toLowerCase().contains(password.toLowerCase())) {
                return false;
            }
        }

        return true;
    }
}
