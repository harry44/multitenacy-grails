package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import java.util.ResourceBundle;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/** @author Hibernate CodeGenerator */
public class MROldCommissione implements it.aessepi.utils.db.PersistentInstance, Loggable {

    private Integer id;
    private MROldFonteCommissione fonteCommissione;
    private MROldPreventivo preventivo;
    private MROldPrenotazione prenotazione;
    private MROldContrattoNoleggio contratto;
    private Boolean prepagato;
    private String codiceVoucher;
    private Integer giorniVoucher;

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    public MROldCommissione() {
    }

    public MROldCommissione(
            MROldPreventivo preventivo,
            MROldPrenotazione prenotazione,
            MROldContrattoNoleggio contratto,
            MROldFonteCommissione fonteCommissione,
            Boolean prepagato,
            String codiceVoucher,
            Integer giorniVoucher) {
        setPreventivo(preventivo);
        setPrenotazione(prenotazione);
        setContratto(contratto);
        setFonteCommissione(fonteCommissione);
        setPrepagato(prepagato);
        setCodiceVoucher(codiceVoucher);
        setGiorniVoucher(giorniVoucher);
    }


    public String[] getLoggableFields() {
        return new String[]{
                    "fonteCommissione", // NOI18N
                    "prepagato", // NOI18N
                    "codiceVoucher", // NOI18N
                    "giorniVoucher" // NOI18N
                };
    }

    public String[] getLoggableLabels() {
        return new String[]{
                    bundle.getString("Commissione.logReservationSource"),
                    bundle.getString("Commissione.logPrepaid"),
                    bundle.getString("Commissione.logVoucherNumber"),
                    bundle.getString("Commissione.logPrepaidDays")
                };
    }

    public String getEntityName() {
        return "MROldCommissione";
    }

    public Integer getEntityId() {
        return getId();
    }

    public Integer getId() {
        return this.id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public String toString() {
        if (getFonteCommissione() != null) {
            return getFonteCommissione().toString();
        } else {
            return new String();
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldCommissione)) {
            return false;
        }
        MROldCommissione castOther = (MROldCommissione) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public MROldFonteCommissione getFonteCommissione() {
        return fonteCommissione;
    }

    public void setFonteCommissione(MROldFonteCommissione fonteCommissione) {
        this.fonteCommissione = fonteCommissione;
    }

    public MROldPrenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(MROldPrenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public MROldContrattoNoleggio getContratto() {
        return contratto;
    }

    public void setContratto(MROldContrattoNoleggio contratto) {
        this.contratto = contratto;
    }

    public MROldPreventivo getPreventivo() {
        return preventivo;
    }

    public void setPreventivo(MROldPreventivo preventivo) {
        this.preventivo = preventivo;
    }

    public Boolean getPrepagato() {
        return prepagato;
    }

    public void setPrepagato(Boolean prepagato) {
        this.prepagato = prepagato;
    }

    public String getCodiceVoucher() {
        return codiceVoucher;
    }

    public void setCodiceVoucher(String codiceVoucher) {
        this.codiceVoucher = codiceVoucher;
    }

    public Integer getGiorniVoucher() {
        return giorniVoucher;
    }

    public void setGiorniVoucher(Integer giorniVoucher) {
        this.giorniVoucher = giorniVoucher;
    }
}
