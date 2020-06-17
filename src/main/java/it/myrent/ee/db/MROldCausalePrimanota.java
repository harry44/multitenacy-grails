/*
 * CausalePrimanota.java
 *
 * Created on 16 mai 2006, 10:34
 *
 */
package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import java.util.List;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author jamess
 */
public class MROldCausalePrimanota implements PersistentInstance, Comparable<MROldCausalePrimanota> {

    /** Creates a new instance of MROldCausalePrimanota */
    public MROldCausalePrimanota() {
    }
    private Integer codice;
    private String descrizione;
    private Boolean dataDocRequired;
    private Boolean numDocRequired;
    private String causaleEsterna;
    private Boolean iva;
    private MROldRegistroIva registroIva;
    private Boolean segnoIva;
    private Boolean gestioneIvaIndetraibile;
    private Boolean liquidazioneIva;
    private List righeCausale;
    public static final Boolean DARE = Boolean.TRUE;
    public static final Boolean AVERE = Boolean.FALSE;
    public static final Boolean SEGNO_IVA_POSITIVO = Boolean.TRUE;
    public static final Boolean SEGNO_IVA_NEGATIVO = Boolean.FALSE;
    public static final Integer PRIMA_CAUSALE_LIBERA = new Integer(50);
    public static final Integer ULTIMA_CAUSALE_LIBERA = new Integer(98);
    
    public static final Integer FATTURA_DI_VENDITA = 1;
    public static final Integer NOTA_CREDITO_FATTURA_DI_VENDITA = 2;
    public static final Integer FATTURA_DI_ACQUISTO = 3;
    public static final Integer NOTA_CREDITO_FATTURA_DI_ACQUISTO = 4;
    public static final Integer RILEVAZIONE_SPESE_VARIE = 5;
    public static final Integer INCASSO_CLIENTE = 6;
    public static final Integer BONIFICO_DA_CLIENTE = 7;
    public static final Integer PAGAMENTO_FORNITORE_CON_BANCA = 8;
    public static final Integer PAGAMENTO_FORNITORE_CON_CASSA = 9;
    public static final Integer VERSAMENTO_BANCA = 10;
    public static final Integer PRELEVAMENTO_BANCA = 11;
    public static final Integer PAGAMENTO_A_CLIENTE = 12;
    public static final Integer INCASSO_DA_FORNITORE = 13;
    public static final Integer BONIFICO_O_PAGAMENTO_CON_ASSEGNO_A_CLIENTE = 14;
    public static final Integer BONIFICO_DA_FORNITORE = 15;
    public static final Integer PRESENTAZIONE_EFFETTI_IN_BANCA = 16;
    public static final Integer ONERI_BANCA = 17;
    public static final Integer INCASSO_CLIENTE_CARTA_DI_CREDITO = 19;
    public static final Integer FATTURA_ACQUISTO_SIST_DEL_MARGINE = 20;
    public static final Integer FATTURA_VENDITA_SIST_DEL_MARGINE = 21;
    public static final Integer AUTOFATTURA_SIST_DEL_MARGINE = 22;
    public static final Integer RICEVUTA_FISCALE_NON_PAGATA = 32;
    public static final Integer RICEVUTA_FISCALE_PAGATA_RIEMISSIONE = 33;
    public static final Integer RICEVUTA_FISCALE_PAGATA = 34;
    public static final Integer STORNO_IVA_DEL_MARGINE_A_DEBITO = 35;
    public static final Integer CORRISPETTIVI_GIORNALIERI = 36;
    public static final Integer STORNO_IVA_A_DEBITO__SU_CORRISPETTIVI = 37;
    public static final Integer CHIUSURA_PROFFITTI_PERDITE = 38;
    public static final Integer CHIUSURA_STATO_PATRIMONIALE = 39;
    public static final Integer RISULTATO_ESERCIZIO = 40;
    public static final Integer APERTURA_ANNUALE = 41;
    public static final Integer APERTURA_CASSA = 42;
    public static final Integer CHIUSURA_CASSA = 43;
    public static final Integer FATTURA_DI_ACCONTO = 44;
    public static final Integer FATTURA_DI_SALDO = 45;
    public static final Integer INCASSO_CAUZIONE = 52;
    public static final Integer RIMBORSO_CAUZIONE = 53;
    //add these new column for exporting data to web portal
    private Boolean isExported;
    private Boolean isUpdated;
    public int compareTo(MROldCausalePrimanota o) {
        if (o != null) {
            return new CompareToBuilder().append(getCodice(), o.getCodice()).
                    toComparison();
        }
        return 1;
    }

    public Integer getId() {
        return getCodice();
    }

    public Boolean getIsExported() {
        return isExported;
    }

    public void setIsExported(Boolean isExported) {
        this.isExported = isExported;
    }

    public Boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(Boolean isUpdated) {
        this.isUpdated = isUpdated;
    }

    public Integer getCodice() {
        return codice;
    }

    public void setCodice(Integer codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Boolean getDataDocRequired() {
        return dataDocRequired;
    }

    public void setDataDocRequired(Boolean dataDocRequired) {
        this.dataDocRequired = dataDocRequired;
    }

    public Boolean getNumDocRequired() {
        return numDocRequired;
    }

    public void setNumDocRequired(Boolean numDocRequired) {
        this.numDocRequired = numDocRequired;
    }

    public String getCausaleEsterna() {
        return causaleEsterna;
    }

    public void setCausaleEsterna(String causaleEsterna) {
        this.causaleEsterna = causaleEsterna;
    }

    public Boolean getIva() {
        return iva;
    }

    public void setIva(Boolean iva) {
        this.iva = iva;
    }

    public Boolean getSegnoIva() {
        return segnoIva;
    }

    public void setSegnoIva(Boolean segnoIva) {
        this.segnoIva = segnoIva;
    }

    public List getRigheCausale() {
        return righeCausale;
    }

    public void setRigheCausale(List righeCausale) {
        this.righeCausale = righeCausale;
    }

    public String toString() {
        return getCodice() +
                " - " + //NOI18N
                getDescrizione();
    }

    public boolean equals(Object other) {
        if (other != null && (other instanceof MROldCausalePrimanota)) {
            return new EqualsBuilder().append(getCodice(), ((MROldCausalePrimanota) other).getCodice()).isEquals();
        } else {
            return false;
        }
    }

    public MROldRegistroIva getRegistroIva() {
        return registroIva;
    }

    public void setRegistroIva(MROldRegistroIva registroIva) {
        this.registroIva = registroIva;
    }

    public Boolean getGestioneIvaIndetraibile() {
        return gestioneIvaIndetraibile;
    }

    public void setGestioneIvaIndetraibile(Boolean gestioneIvaIndetraibile) {
        this.gestioneIvaIndetraibile = gestioneIvaIndetraibile;
    }

    public Boolean getLiquidazioneIva() {
        return liquidazioneIva;
    }

    public void setLiquidazioneIva(Boolean liquidazioneIva) {
        this.liquidazioneIva = liquidazioneIva;
    }
}
