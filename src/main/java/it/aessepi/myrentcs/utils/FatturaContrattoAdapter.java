/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.aessepi.myrentcs.utils;

import it.myrent.ee.db.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author jamess
 */
public class FatturaContrattoAdapter {

    private MROldContrattoNoleggio contratto;
    private MROldDocumentoFiscale documentoFiscale;
    private MROldPrenotazione prenotazione;

    public FatturaContrattoAdapter(MROldDocumentoFiscale fattura) {
        setDocumentoFiscale(fattura);
        setContratto(fattura.getContratto());
        setPrenotazione(fattura.getPrenotazione());
    }

    public FatturaContrattoAdapter(MROldContrattoNoleggio contratto) {
        setContratto(contratto);
    }

    public void setDocumentoFiscale(MROldDocumentoFiscale documentoFiscale) {
        this.documentoFiscale = documentoFiscale;
    }

    public MROldDocumentoFiscale getDocumentoFiscale() {
        return documentoFiscale;
    }

    public void setContratto(MROldContrattoNoleggio contratto) {
        this.contratto = contratto;
    }

    public MROldContrattoNoleggio getContratto() {
        return contratto;
    }

    public Integer getId() {
        if (getContratto() != null) {
            return getContratto().getId();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getId();
        } else {
            return getContratto().getId();
        }
    }

    public MROldMovimentoAuto getMovimento() {
        if (getContratto() != null) {
            return getContratto().getMovimento();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getMovimento();
        } else {
            return getContratto().getMovimento();
        }
    }

    public MROldConducenti getConducente1() {
        if (getContratto() != null) {
            return getContratto().getConducente1();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getConducente1();
        } else {
            return getContratto().getConducente1();
        }
    }

    public MROldConducenti getConducente2() {
        if (getContratto() != null) {
            return getContratto().getConducente2();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getConducente2();
        } else {
            return getContratto().getConducente2();
        }
    }

    public MROldConducenti getConducente3() {
        if (getContratto() != null) {
            return getContratto().getConducente3();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getConducente3();
        } else {
            return getContratto().getConducente3();
        }
    }

    public MROldClienti getCliente() {
        if (getContratto() != null) {
            return getContratto().getCliente();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getCliente();
        } else {
            return getContratto().getCliente();
        }
    }

    public MROldTariffa getTariffa() {
        if (getContratto() != null) {
            return getContratto().getTariffa();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getTariffa();
        } else {
            return getContratto().getTariffa();
        }
    }

    public MROldGruppo getGruppo() {
        if (getContratto() != null) {
            return getContratto().getGruppo();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getGruppo();
        } else {
            return getContratto().getGruppo();
        }
    }

    public MROldSede getSedeUscita() {
        if (getContratto() != null) {
            return getContratto().getSedeUscita();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getSedeUscita();
        } else {
            return getContratto().getSedeUscita();
        }
    }

    public MROldSede getSedeRientroPrevisto() {
        if (getContratto() != null) {
            return getContratto().getSedeRientroPrevisto();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getSedeRientroPrevisto();
        } else {
            return getContratto().getSedeRientroPrevisto();
        }
    }

    public MROldAffiliato getAffiliato() {
        if (getContratto() != null) {
            return getContratto().getAffiliato();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getAffiliato();
        } else {
            return getContratto().getAffiliato();
        }
    }

    public Garanzia getGaranzia1() {
        if (getContratto() != null) {
            return getContratto().getGaranzia1();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getGaranzia1();
        } else {
            return getContratto().getGaranzia1();
        }
    }

    public Date getInizio() {
        if (getContratto() != null) {
            return getContratto().getInizio();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getInizio();
        } else {
            return getContratto().getInizio();
        }
    }

    public Date getFine() {
        if (getContratto() != null) {
            return getContratto().getFine();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getFine();
        } else {
            return getContratto().getFine();
        }
    }

    public Integer getAnno() {
        if (getContratto() != null) {
            return getContratto().getAnno();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getAnno();
        } else {
            return getContratto().getAnno();
        }
    }

    public Date getData() {
        if (getContratto() != null) {
            return getContratto().getData();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getData();
        } else {
            return getContratto().getData();
        }
    }

    public Integer getNumero() {
        if (getContratto() != null) {
            return getContratto().getNumero();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getNumero();
        } else {
            return getContratto().getNumero();
        }
    }

    public Double getCauzione() {
        if (getContratto() != null) {
            return getContratto().getCauzione();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getCauzione();
        } else {
            return getContratto().getCauzione();
        }
    }

    public Double getScontoTariffa() {
        if (getContratto() != null) {
            return getContratto().getScontoTariffa();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getScontoPercentuale();
        } else {
            return getContratto().getScontoTariffa();
        }
    }

    public String getNote() {
        if (getContratto() != null) {
            String note= getContratto().getNote();
            String str = "";
            if(note!=null && note!="null" && !note.equals("")){
                str = note.replaceAll("\\<.*?\\>", "");
                if(str!=""){
                    str = str.replaceAll("&nbsp;","");
                }
            }
            return str;
        } else if (getContratto() == null && getPrenotazione() != null) {
            String note= getPrenotazione().getNote();
            String str = "";
            if(note!=null && note!="null" && !note.equals("")){
                str = note.replaceAll("\\<.*?\\>", "");
                if(str!=""){
                    str = str.replaceAll("&nbsp;","");
                }
            }
            return str;

        } else {
            return "";
        }
    }

    public Boolean getChiuso() {
        if (getContratto() != null) {
            return getContratto().getChiuso();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return Boolean.FALSE;
        } else {
            return getContratto().getChiuso();
        }
    }

    public User getUserApertura() {
        if (getContratto() != null) {
            return getContratto().getUserApertura();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getUserApertura();
        } else {
            return getContratto().getUserApertura();
        }
    }

    public MROldCommissione getCommissione() {
        if (getContratto() != null) {
            return getContratto().getCommissione();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getCommissione();
        } else {
            return getContratto().getCommissione();
        }
    }

    public MROldPagamento getPagamento() {
        if (getContratto() != null) {
            return getContratto().getPagamento();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getPagamento();
        } else {
            return getContratto().getPagamento();
        }
    }

    public Set getPrimenote() {
        if (getContratto() != null) {
            return getContratto().getPrimenote();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getPrimenote();
        } else {
            return getContratto().getPrimenote();
        }
    }

    public String getPrefisso() {
        if (getContratto() != null) {
            return getContratto().getPrefisso();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getPrefisso();
        } else {
            return getContratto().getPrefisso();
        }
    }

    public MROldNumerazione getNumerazione() {
        if (getContratto() != null) {
            return getContratto().getNumerazione();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getNumerazione();
        } else {
            return getContratto().getNumerazione();
        }
    }

    public Double getSaldoCauzione() {
        if (getContratto() != null) {
            return getContratto().getSaldoCauzione();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getSaldoCauzione();
        } else {
            return getContratto().getSaldoCauzione();
        }
    }

    public Double getSaldoNoleggio() {
        if (getContratto() != null) {
            return getContratto().getSaldoNoleggio();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getSaldoNoleggio();
        } else {
            return getContratto().getSaldoNoleggio();
        }
    }

    public Double getNoleggio() {
        if (getContratto() != null) {
            return getContratto().getNoleggio();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getNoleggio();
        } else {
            return getContratto().getNoleggio();
        }
    }

    public Set getMovimenti() {
        if (getContratto() != null) {
            return getContratto().getMovimenti();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return new HashSet();
        } else {
            return getContratto().getMovimenti();
        }
    }

    public Garanzia getGaranzia2() {
        if (getContratto() != null) {
            return getContratto().getGaranzia2();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getGaranzia2();
        } else {
            return getContratto().getGaranzia2();
        }
    }

    public Set getDocumentiFiscali() {
        if (getContratto() != null) {
            return getContratto().getDocumentiFiscali();
        } else if (getContratto() == null && getPrenotazione() != null) {
            return getPrenotazione().getDocumentiFiscali();
        } else {
            return getContratto().getDocumentiFiscali();
        }
    }

    public void setPrenotazione(MROldPrenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public MROldPrenotazione getPrenotazione() {
        return prenotazione;
    }

    public Boolean getPrivacy1() {
        if (getContratto() != null) {
            return getContratto().getPrivacyMessage1();
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean getPrivacy2() {
        if (getContratto() != null) {
            return getContratto().getPrivacyMessage2();
        } else {
            return Boolean.FALSE;
        }
    }

    public Boolean getIsAccident() {
        if (getContratto() == null && getMovimento() == null) {
            return Boolean.FALSE;
        } else if (getContratto() != null && getMovimento() == null) {
            return Boolean.FALSE;
        }else if (getContratto() == null && getMovimento() != null) {
            return Boolean.FALSE;
        } else {
            return getContratto().getMovimento().getIsAccident();
        }
    }

    public Boolean getDamagesAcceptedByCustomer() {
        if (getContratto() == null && getMovimento() == null) {
            return Boolean.FALSE;
        } else if (getContratto() != null && getMovimento() == null) {
            return Boolean.FALSE;
        }else if (getContratto() == null && getMovimento() != null) {
            return Boolean.FALSE;
        } else {
            return getContratto().getMovimento().getDamagesAcceptedByCustomer();
        }
    }

    public Boolean getKeyLeftInKeyBox() {
        if (getContratto() == null && getMovimento() == null) {
            return Boolean.FALSE;
        } else if (getContratto() != null && getMovimento() == null) {
            return Boolean.FALSE;
        }else if (getContratto() == null && getMovimento() != null) {
            return Boolean.FALSE;
        } else {
            return getContratto().getMovimento().getKeyLeftInKeyBox();
        }
    }


    public Boolean getCustomerRefusesCheckIn() {
        if (getContratto() == null && getMovimento() == null) {
            return Boolean.FALSE;
        } else if (getContratto() != null && getMovimento() == null) {
            return Boolean.FALSE;
        }else if (getContratto() == null && getMovimento() != null) {
            return Boolean.FALSE;
        } else {
            return getContratto().getMovimento().getCustomerRefusesCheckIn();
        }
    }
}
