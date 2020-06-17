/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.api.factory;

import it.aessepi.utils.MathUtils;
import it.myrent.ee.api.exception.ImportoNegativoException;
import it.myrent.ee.db.MROldCodiciIva;
import it.myrent.ee.db.MROldRigaDocumento;
import it.myrent.ee.db.MROldRigaImpostaDocumentoFiscale;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giacomo
 */
public class ImpostaFactory {
private HashMap<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale> imposte = new HashMap<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale>();
    private Double totaleImponibile = 0.0;
    private Double totaleImposta = 0.0;
    private Double totaleAcconti = 0.0;

    public HashMap<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale> getImposte() {
        return imposte;
    }

    public void setImposte(HashMap<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale> imposte) {
        this.imposte = imposte;
    }

    public ImpostaFactory() {
    }

    public ImpostaFactory(List<MROldRigaDocumento> righeDocumento) {
        this();
        if (righeDocumento != null) {
            Iterator it = righeDocumento.iterator();
            while (it.hasNext()) {
                MROldRigaDocumento tmpRiga = (MROldRigaDocumento) it.next();
                if (tmpRiga!= null && tmpRiga.getCodiceIva() != null) {
                    MROldRigaImpostaDocumentoFiscale castelletto = null;
                    if (!imposte.containsKey(tmpRiga.getCodiceIva())) {
                        imposte.put(tmpRiga.getCodiceIva(), new MROldRigaImpostaDocumentoFiscale(tmpRiga.getCodiceIva()));
                    }
                    castelletto = (MROldRigaImpostaDocumentoFiscale) imposte.get(tmpRiga.getCodiceIva());
                    double imponibile = castelletto.getImponibile().doubleValue() + tmpRiga.getTotaleImponibileRiga().doubleValue();
                    castelletto.setImponibile(new Double(imponibile));
                }
            }
        }
        Iterator it = imposte.values().iterator();
        while (it.hasNext()) {
            MROldRigaImpostaDocumentoFiscale castelletto = (MROldRigaImpostaDocumentoFiscale) it.next();
            castelletto.setImponibile(MathUtils.roundDouble(castelletto.getImponibile().doubleValue()));
            castelletto.setImposta(MathUtils.roundDouble(castelletto.getImponibile().doubleValue() * castelletto.getCodiceIva().getAliquota().doubleValue()));
        }
        calcolaTotali();
    }

    public ImpostaFactory(Map<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale> imposte, Double totaleAcconti, Double totaleImponibile, Double totaleImposta) {
        this();
        getImposte().putAll(imposte);
        setTotaleAcconti(totaleAcconti);
        setTotaleImponibile(totaleImponibile);
        setTotaleImposta(totaleImposta);
    }

    private void calcolaTotali() {
        double tmpTotaleIva = 0.0, tmpTotaleImponibile = 0.0;
        for(MROldCodiciIva aliquota : getImposte().keySet()) {
            MROldRigaImpostaDocumentoFiscale imposta = getImposte().get(aliquota);
            tmpTotaleImponibile = tmpTotaleImponibile + imposta.getImponibile();
            tmpTotaleIva = tmpTotaleIva + imposta.getImposta();
        }
        setTotaleImponibile(tmpTotaleImponibile);
        setTotaleImposta(tmpTotaleIva);
    }

    private void addImponibile(Double imponibile) {
        setTotaleImponibile(MathUtils.round(getTotaleImponibile() + imponibile));
    }
    private void addImposta(Double imposta) {
        setTotaleImposta(MathUtils.round(getTotaleImposta() + imposta));
    }
    private void addAcconto(Double acconto) {
        setTotaleAcconti(MathUtils.round(getTotaleAcconti() + acconto));
    }


    public void add(Map<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale> other) {
        for(MROldCodiciIva aliquota : other.keySet()) {
            if(!getImposte().containsKey(aliquota)) {
                getImposte().put(aliquota, new MROldRigaImpostaDocumentoFiscale(aliquota));
            }
            MROldRigaImpostaDocumentoFiscale imposta = getImposte().get(aliquota);
            MROldRigaImpostaDocumentoFiscale otherImposta = other.get(aliquota);
            imposta.setImponibile(MathUtils.round(imposta.getImponibile() + otherImposta.getImponibile()));
            addImponibile(otherImposta.getImponibile());
            //Va sottratta l'imposta vecchia
            addImposta(- imposta.getImposta());
            // L'imposta va ricalcolata in base al nuovo imponibile.
            imposta.setImposta(MathUtils.round(imposta.getImponibile() * imposta.getCodiceIva().getAliquota()));
            //Va aggiunta l'imposta nuova
            addImposta(+ imposta.getImposta());
        }
    }

    public void add(ImpostaFactory other) {
        add(other.getImposte());
    }

    public void subtract(Map<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale> other) throws ImportoNegativoException {
        for(MROldCodiciIva aliquota : other.keySet()) {
            if(!getImposte().containsKey(aliquota)) {
                getImposte().put(aliquota, new MROldRigaImpostaDocumentoFiscale(aliquota));
            }
            MROldRigaImpostaDocumentoFiscale imposta = getImposte().get(aliquota);
            MROldRigaImpostaDocumentoFiscale otherImposta = other.get(aliquota);
            if(imposta.getImponibile().compareTo(otherImposta.getImponibile()) < 0) {
                throw new ImportoNegativoException();
            }
            imposta.setAcconto(MathUtils.round(imposta.getAcconto() + otherImposta.getImponibile()));
            addAcconto(otherImposta.getImponibile());
            imposta.setImponibile(MathUtils.round(imposta.getImponibile() - otherImposta.getImponibile()));
            addImponibile(- otherImposta.getImponibile());
            //Va sottratta l'imposta vecchia
            addImposta(- imposta.getImposta());
            // L'imposta va ricalcolata in base al nuovo imponibile.
            imposta.setImposta(MathUtils.round(imposta.getImponibile() * imposta.getCodiceIva().getAliquota()));
            //Va aggiunta l'imposta nuova
            addImposta(+ imposta.getImposta());
        }
    }

    public void subtract(ImpostaFactory other) throws ImportoNegativoException {
        subtract(other.getImposte());
    }

    public Double getTotaleAcconti() {
        return totaleAcconti;
    }

    public void setTotaleAcconti(Double totaleAcconti) {
        this.totaleAcconti = totaleAcconti;
    }

    public Double getTotaleImponibile() {
        return totaleImponibile;
    }

    public Double getTotaleImposta() {
        return totaleImposta;
    }

    private void setTotaleImponibile(Double totaleImponibile) {
        this.totaleImponibile = totaleImponibile;
    }

    private void setTotaleImposta(Double totaleImposta) {
        this.totaleImposta = totaleImposta;
    }

    public Object[] toArray() {
       return getImposte().values().toArray();
    }
}
