/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.db.PersistentInstance;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author bogdan
 */
public class MROldRigaImpostaDocumentoFiscale implements PersistentInstance {

    private Integer id;
    private MROldCodiciIva codiceIva;
    private Double acconto;
    private Double imponibile;
    private Double imposta;

    public static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
    private static final MessageFormat TO_STRING_MESSAGE_FORMAT = new MessageFormat(bundle.getString("RigaImpostaDocumentoFiscale.msgImponibile0Aliquota1Imposta2"));

    public MROldRigaImpostaDocumentoFiscale() {
        this.acconto = 0.0;
        this.imponibile = 0.0;
        this.imposta = 0.0;
    }

    public MROldRigaImpostaDocumentoFiscale(MROldCodiciIva codiceIva) {
        this();
        this.codiceIva = codiceIva;
    }

     public String toString() {
        return TO_STRING_MESSAGE_FORMAT.format(new Object[]{
                    getImponibile(),
                    getCodiceIva().getAliquota(),
                    getImponibile()
                });
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldRigaImpostaDocumentoFiscale)) {
            return false;
        }
        MROldRigaImpostaDocumentoFiscale castOther = (MROldRigaImpostaDocumentoFiscale) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public MROldCodiciIva getCodiceIva() {
        return codiceIva;
    }

    public void setCodiceIva(MROldCodiciIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    public Double getImponibile() {
        return imponibile;
    }

    public void setImponibile(Double imponibile) {
        this.imponibile = imponibile;
    }

    public Double getImposta() {
        return imposta;
    }

    public void setImposta(Double imposta) {
        this.imposta = imposta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAcconto() {
        return acconto;
    }

    public void setAcconto(Double acconto) {
        this.acconto = acconto;
    }
}
