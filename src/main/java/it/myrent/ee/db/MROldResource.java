/*
 * Resource.java
 *
 * Created on 07 noiembrie 2005, 17:10
 *
 */
package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *
 * @author Bogdan Alexandru Constantin
 */
public class MROldResource implements Comparable<MROldResource> {

    /** Creates a new instance of MROldResource */
    public MROldResource() {
    }

    public MROldResource(String descrizione) {
        this.descrizione = descrizione;
    }
    private String descrizione;

    public String toString() {
        if (getDescrizione() != null) {
            return bundle.getString(getDescrizione().replaceAll("\\s", "_"));
        } else {
            return new String();
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldResource)) {
            return false;
        }
        MROldResource castOther = (MROldResource) other;
        return new EqualsBuilder().append(getDescrizione(), castOther.getDescrizione()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getDescrizione()).toHashCode();
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int compareTo(MROldResource o) {
        if (o == null) {
            return 1;
        }
        return new CompareToBuilder().append(toString(), o.toString()).toComparison();
    }
    public static final MROldResource PRIMENOTE = new MROldResource("Primenote"); //NOI18N
    public static final MROldResource SCHEDE_CONTABILI = new MROldResource("Schede contabili"); //NOI18N
    public static final MROldResource ARCHIVI_CONTABILI = new MROldResource("Archivi contabili"); //NOI18N
    public static final MROldResource FATTURAZIONE = new MROldResource("Fatturazione"); //NOI18N
    public static final MROldResource NOLEGGIO = new MROldResource("Noleggio"); //NOI18N
    public static final MROldResource PRENOTAZIONI = new MROldResource("Prenotazioni"); //NOI18N
    public static final MROldResource LISTINI_TARIFFE = new MROldResource("Listini di tariffe"); //NOI18N
    public static final MROldResource ANAGRAFICHE_CLIENTI = new MROldResource("Anagrafiche clienti"); //NOI18N
    public static final MROldResource ANAGRAFICHE_CONDUCENTI = new MROldResource("Anagrafiche conducenti"); //NOI18N
    public static final MROldResource ANAGRAFICHE_FORNITORI = new MROldResource("Anagrafiche fornitori"); //NOI18N
    public static final MROldResource ANAGRAFICHE_SEDI = new MROldResource("Anagrafiche sedi"); //NOI18N
    public static final MROldResource PARCO_VEICOLI = new MROldResource("Parco veicoli"); //NOI18N
    public static final MROldResource GRUPPI_DI_MEZZI = new MROldResource("Gruppi di mezzi"); //NOI18N
    public static final MROldResource OPTIONALS = new MROldResource("Optionals"); //NOI18N
    public static final MROldResource FONTI_COMMISSIONABILI = new MROldResource("Fonti commissionabili"); //NOI18N
    public static final MROldResource CARBURANTI = new MROldResource("Carburanti"); //NOI18N
    public static final MROldResource MOVIMENTI = new MROldResource("Movimenti"); //NOI18N
    public static final MROldResource PROPRIETARI_VEICOLI = new MROldResource("Proprietari Veicoli"); //NOI18N
    public static final MROldResource MULTE = new MROldResource("Multe"); //NOI18N
    public static final MROldResource CAUSALI_MOVIMENTI = new MROldResource("Causali Movimenti"); //NOI18N
    public static final MROldResource SESSIONI_UTENTI = new MROldResource("Sessioni Utenti"); //NOI18N
    public static final MROldResource GESTIONE_CASSA = new MROldResource("Gestione Cassa"); //NOI18N

    public static final List<MROldResource> RESOURCE_LIST = Arrays.asList(
            PRIMENOTE,
            SCHEDE_CONTABILI,
            ARCHIVI_CONTABILI,
            FATTURAZIONE,
            NOLEGGIO,
            PRENOTAZIONI,
            LISTINI_TARIFFE,
            ANAGRAFICHE_CLIENTI,
            ANAGRAFICHE_CONDUCENTI,
            ANAGRAFICHE_FORNITORI,
            ANAGRAFICHE_SEDI,
            PARCO_VEICOLI,
            GRUPPI_DI_MEZZI,
            OPTIONALS,
            FONTI_COMMISSIONABILI,
            CARBURANTI,
            MOVIMENTI,
            PROPRIETARI_VEICOLI,
            MULTE,
            CAUSALI_MOVIMENTI,
            SESSIONI_UTENTI,
            GESTIONE_CASSA
            );
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
}
