/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import java.util.Date;

/**
 *
 * @author jamess
 */
public interface MROldContribuente {

    public Boolean getPersonaFisica();

    public Boolean getDittaIndividuale();

    public String getRagioneSociale();

    public String getCognome();

    public String getNome();

    public String getPartitaIva();

    public String getCodiceFiscale();

    public Date getDataNascita();

    public String getLuogoNascita();

    public Boolean getSesso();

    public static final ObjectToStringConverter TO_STRING_CONVERTER = new ObjectToStringConverter() {
        @Override
        public String getPreferredStringForItem(Object item) {
            String preferredString = new String();
            String[] possibleStrings = getPossibleStringsForItem(item);
            if (possibleStrings != null && possibleStrings.length > 0) {
                if (possibleStrings[0] != null) {
                    preferredString = possibleStrings[0];
                }
                preferredString += " ( "; //NOI18N
                for (int i = 1; i < possibleStrings.length; i++) {
                    if (possibleStrings[i] != null) {
                        preferredString += possibleStrings[i];
                    }
                    if (i < possibleStrings.length - 1) {
                        preferredString += " / "; //NOI18N
                    }
                }
                preferredString += " )"; //NOI18N
            }
            return preferredString.trim();
        }

        @Override
        public String[] getPossibleStringsForItem(Object item) {
            if (item == null || !(item instanceof MROldContribuente)) {
                return null;
            }
            MROldContribuente contribuente = (MROldContribuente) item;
            if (contribuente.getPersonaFisica()) {
                if (contribuente.getDittaIndividuale()) {
                    return new String[]{
                                contribuente.getRagioneSociale(),
                                contribuente.getCognome(),
                                contribuente.getNome(),
                                contribuente.getCodiceFiscale(),
                                contribuente.getPartitaIva()
                            };
                }

                return new String[]{
                            contribuente.getCognome(),
                            contribuente.getNome(),
                            contribuente.getCodiceFiscale()
                        };
            } else {
                return new String[]{
                            contribuente.getRagioneSociale(),
                            contribuente.getCodiceFiscale(),
                            contribuente.getPartitaIva()
                        };
            }
        }
    };
}
