/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.api.factory;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.MathUtils;
import it.aessepi.utils.Parameters;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.utils.ContabUtils;
import it.myrent.ee.api.utils.NumerazioniUtils;
import it.myrent.ee.api.utils.PrenotazioniUtils;
import it.myrent.ee.api.utils.RigaRimessa;
import it.myrent.ee.db.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.text.MessageFormat;
import java.util.*;

/**
 *
 * @author bogdan
 */
public class PrimanotaFactory {

    private static final Log log = LogFactory.getLog(PrimanotaFactory.class);
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/factory/Bundle");


    public static MROldPrimanota newPrimanota(Session sx, MROldDocumentoFiscale documentoFiscale, MROldSede sedeOperativa, MROldNumerazione numerazioneRegistrazione, Integer numeroRegistrazione) throws HibernateException {

        //fa il refresh
        //sx = HibernateBridge.startNewSession();
       // sx = HibernateBridge.refreshSessionSX(sx);

        if (documentoFiscale.getPrimanota() == null) {
            documentoFiscale.setPrimanota(new MROldPrimanota());
        }

        MROldPrimanota primanota = documentoFiscale.getPrimanota();
        primanota.setEditable(Boolean.FALSE);
        if (primanota.getCausale() == null) {
            if (documentoFiscale instanceof MROldFattura) {
                if (documentoFiscale.getTotaleAcconti() != null && documentoFiscale.getTotaleAcconti() > 0.0) {
                    primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.FATTURA_DI_SALDO));
                } else {
                    primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.FATTURA_DI_VENDITA));
                }
            } else if (documentoFiscale instanceof MROldFatturaAcconto) {
                primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.FATTURA_DI_ACCONTO));
            } else if (documentoFiscale instanceof MROldNotaCredito) {
                primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.NOTA_CREDITO_FATTURA_DI_VENDITA));
            } else if (documentoFiscale instanceof MROldRicevutaFiscale) {
                primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.RICEVUTA_FISCALE_PAGATA));
            }
        }
        primanota.setUser(documentoFiscale.getUser());
        primanota.setUserModifica((User) Parameters.getUser());
        primanota.setUserCreazione((User) Parameters.getUser());
        primanota.setAffiliato(documentoFiscale.getAffiliato());
        primanota.setDataDocumento(documentoFiscale.getData());
        Calendar c = Calendar.getInstance();
        primanota.setDataModifica(c.getTime());
        primanota.setDataRegistrazione(documentoFiscale.getData());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setSezionale(documentoFiscale.getNumerazione());
        primanota.setNumeroProtocollo(documentoFiscale.getNumero());
        primanota.setNumeroDocumento(documentoFiscale.getNumero().toString());
        primanota.setNumeroDocumentoExtra(documentoFiscale.getPrefisso());

        if (primanota.getNumeroRegistrazione() == null) {
            if (numeroRegistrazione != null && numerazioneRegistrazione != null) {
                primanota.setNumerazione(numerazioneRegistrazione);
                primanota.setNumeroRegistrazione(numeroRegistrazione);
            } else {
                primanota.setNumerazione(NumerazioniUtils.getNumerazione(sx, sedeOperativa, MROldNumerazione.PRIMENOTE));
                primanota.setNumeroRegistrazione(NumerazioniUtils.aggiornaProgressivo(
                        sx,
                        primanota.getNumerazione(),
                        primanota.getAnnoCompetenza()));
            }
        }

        primanota.setTotaleDocumento(documentoFiscale.getTotaleFattura());
        primanota.setTotaleImponibile(documentoFiscale.getTotaleImponibile());
        primanota.setTotaleImposta(documentoFiscale.getTotaleIva());

        if (primanota.getRigheImposta() != null) {
            primanota.getRigheImposta().clear();
        } else {
            primanota.setRigheImposta(new ArrayList());
        }

        if (Boolean.TRUE.equals(documentoFiscale.getDocumentoLibero()) || (documentoFiscale!=null && documentoFiscale.getId()!=null)) {
            ImpostaFactory imposteFattura = new ImpostaFactory(documentoFiscale.getFatturaRighe());
            documentoFiscale.setFatturaImposte(imposteFattura.getImposte());
            documentoFiscale.setTotaleRighe(imposteFattura.getTotaleImponibile());
            documentoFiscale.setTotaleAcconti(imposteFattura.getTotaleAcconti());
            documentoFiscale.setTotaleImponibile(imposteFattura.getTotaleImponibile());
            documentoFiscale.setTotaleIva(imposteFattura.getTotaleImposta());
            documentoFiscale.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
        }


        primanota.getRigheImposta().addAll(creaRigheImposta(sx, primanota, documentoFiscale));

        if (primanota.getRighePrimanota() != null) {
            primanota.getRighePrimanota().clear();
        } else {
            primanota.setRighePrimanota(new ArrayList());
        }

        primanota.getRighePrimanota().addAll(creaRigheImputazioni(sx, primanota, documentoFiscale,null));

        return primanota;
    }


    public static MROldPrimanota newPrimanota(Session sx, MROldDocumentoFiscale documentoFiscale, MROldSede sedeOperativa, MROldNumerazione numerazioneRegistrazione, Integer numeroRegistrazione, MROldParcoVeicoli veicolo) throws HibernateException {

        //fa il refresh
        //sx = HibernateBridge.startNewSession();
       // sx = HibernateBridge.refreshSessionSX(sx);

        if (documentoFiscale.getPrimanota() == null) {
            documentoFiscale.setPrimanota(new MROldPrimanota());
        }

        MROldPrimanota primanota = documentoFiscale.getPrimanota();
        primanota.setEditable(Boolean.FALSE);

        if (primanota.getCausale() == null) {
            if (documentoFiscale instanceof MROldFattura) {
                if (documentoFiscale.getTotaleAcconti() != null && documentoFiscale.getTotaleAcconti() > 0.0) {
                    primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.FATTURA_DI_SALDO));
                } else {
                    primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.FATTURA_DI_VENDITA));
                }
            } else if (documentoFiscale instanceof MROldFatturaAcconto) {
                primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.FATTURA_DI_ACCONTO));
            } else if (documentoFiscale instanceof MROldNotaCredito) {
                primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.NOTA_CREDITO_FATTURA_DI_VENDITA));
            } else if (documentoFiscale instanceof MROldRicevutaFiscale) {
                primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.RICEVUTA_FISCALE_PAGATA));
            }
        }
        primanota.setUser(documentoFiscale.getUser());
        primanota.setUserModifica((User) Parameters.getUser());
        primanota.setUserCreazione((User) Parameters.getUser());
        primanota.setAffiliato(documentoFiscale.getAffiliato());
        primanota.setDataDocumento(documentoFiscale.getData());
        Calendar c = Calendar.getInstance();
        primanota.setDataModifica(c.getTime());
        primanota.setDataRegistrazione(documentoFiscale.getData());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setSezionale(documentoFiscale.getNumerazione());
        primanota.setNumeroProtocollo(documentoFiscale.getNumero());
        primanota.setNumeroDocumento(documentoFiscale.getNumero().toString());
        primanota.setNumeroDocumentoExtra(documentoFiscale.getPrefisso());

        if (primanota.getNumeroRegistrazione() == null) {
            if (numeroRegistrazione != null && numerazioneRegistrazione != null) {
                primanota.setNumerazione(numerazioneRegistrazione);
                primanota.setNumeroRegistrazione(numeroRegistrazione);
            } else {
                primanota.setNumerazione(NumerazioniUtils.getNumerazione(sx, sedeOperativa, MROldNumerazione.PRIMENOTE));
                primanota.setNumeroRegistrazione(NumerazioniUtils.aggiornaProgressivo(
                        sx,
                        primanota.getNumerazione(),
                        primanota.getAnnoCompetenza()));
            }
        }

        primanota.setTotaleDocumento(documentoFiscale.getTotaleFattura());
        primanota.setTotaleImponibile(documentoFiscale.getTotaleImponibile());
        primanota.setTotaleImposta(documentoFiscale.getTotaleIva());

        if (primanota.getRigheImposta() != null) {
            primanota.getRigheImposta().clear();
        } else {
            primanota.setRigheImposta(new ArrayList());
        }

        if (Boolean.TRUE.equals(documentoFiscale.getDocumentoLibero()) || (documentoFiscale!=null && documentoFiscale.getId()!=null)) {
            ImpostaFactory imposteFattura = new ImpostaFactory(documentoFiscale.getFatturaRighe());
            documentoFiscale.setFatturaImposte(imposteFattura.getImposte());
            documentoFiscale.setTotaleRighe(imposteFattura.getTotaleImponibile());
            documentoFiscale.setTotaleAcconti(imposteFattura.getTotaleAcconti());
            documentoFiscale.setTotaleImponibile(imposteFattura.getTotaleImponibile());
            documentoFiscale.setTotaleIva(imposteFattura.getTotaleImposta());
            documentoFiscale.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
        }


        primanota.getRigheImposta().addAll(creaRigheImposta(sx, primanota, documentoFiscale));

        if (primanota.getRighePrimanota() != null) {
            primanota.getRighePrimanota().clear();
        } else {
            primanota.setRighePrimanota(new ArrayList());
        }

        primanota.getRighePrimanota().addAll(creaRigheImputazioni(sx, primanota, documentoFiscale, veicolo));

        return primanota;
    }

    public static List creaRigheImputazioni(Session mySession, MROldPrimanota primanotaNuova, MROldDocumentoFiscale documentoFiscale, MROldParcoVeicoli veicolo) throws HibernateException {
        if (documentoFiscale.getId() != null) {
            mySession.merge(documentoFiscale);
//            mySession.close();
//            mySession = HibernateBridge.openNewSession();
//            documentoFiscale = (DocumentoFiscale) mySession.get(DocumentoFiscale.class, documentoFiscale.getId());
//            mySession.refresh(documentoFiscale);
//            mySession.merge(documentoFiscale);
        }
        List righePrimanota = null;
        List righeDocumento = documentoFiscale.getFatturaRighe();

        if (righeDocumento != null && righeDocumento.size() > 0) {
            righePrimanota = new ArrayList();
            //Per una nota credito il cliente va in AVERE, per fatture e ricevute in DARE.
            Boolean segnoIntestatario = (documentoFiscale instanceof MROldNotaCredito) ? MROldCausalePrimanota.AVERE : MROldCausalePrimanota.DARE;

            Boolean segnoAltro = new Boolean(!segnoIntestatario.booleanValue());

            //Aggiungiamo una prima riga dell'intestatario.
            RigaPrimanota rigaIntestazione = new RigaPrimanota();
            rigaIntestazione.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
            rigaIntestazione.setCliente(documentoFiscale.getCliente());
            if (documentoFiscale.getContratto() != null) {
                rigaIntestazione.setDescrizione(
                        MessageFormat.format(bundle.getString("PrimanotaFactory.msgRA0Del1"),
                                NumerazioniUtils.format(documentoFiscale.getContratto().getPrefisso(), documentoFiscale.getContratto().getNumero()),
                                documentoFiscale.getContratto().getData()));
            } else {
                rigaIntestazione.setDescrizione(null);
            }

            rigaIntestazione.setImporto(MathUtils.round(documentoFiscale.getTotaleFattura() + documentoFiscale.getTotaleAcconti()));
            rigaIntestazione.setSegno(segnoIntestatario);

            rigaIntestazione.setPrimanota(primanotaNuova);
            rigaIntestazione.setNumeroRiga(new Integer(righePrimanota.size()));
            righePrimanota.add(rigaIntestazione);

            if (documentoFiscale.getTotaleAcconti() > 0.0) {
                RigaPrimanota rigaGirocontoAnticipi = new RigaPrimanota(
                        primanotaNuova,
                        ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI_C_ANTICIPI),
                        null,
                        null,
                        documentoFiscale.getTotaleAcconti(),
                        segnoAltro,
                        righePrimanota.size());
                righePrimanota.add(rigaGirocontoAnticipi);
            }

            //Se abbiamo una ricevuta fiscale dobbiamo aggiungere le righe per l'incasso.
            //Correzione: se abbiamo una ricevuta fiscale che non proviene da un contratto!
            if (documentoFiscale instanceof MROldRicevutaFiscale && documentoFiscale.getContratto() != null) {

                RigaPrimanota rigaPagamento = new RigaPrimanota();
                rigaPagamento.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
                rigaPagamento.setCliente(documentoFiscale.getCliente());

                rigaPagamento.setDescrizione(null);
                rigaPagamento.setImporto(documentoFiscale.getTotaleFattura());
                rigaPagamento.setSegno(segnoAltro);

                rigaPagamento.setPrimanota(primanotaNuova);
                rigaPagamento.setNumeroRiga(new Integer(righePrimanota.size()));
                righePrimanota.add(rigaPagamento);

                RigaPrimanota rigaIncasso = new RigaPrimanota();
                if (documentoFiscale.getPagamento().getContoIncasso() != null) {
                    rigaIncasso.setConto(documentoFiscale.getPagamento().getContoIncasso());
                } else {
                    rigaIncasso.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CASSA_CONTANTI));
                }

                rigaIncasso.setDescrizione(null);
                rigaIncasso.setImporto(documentoFiscale.getTotaleFattura());
                rigaIncasso.setSegno(segnoIntestatario);

                rigaIncasso.setPrimanota(primanotaNuova);
                rigaIncasso.setNumeroRiga(new Integer(righePrimanota.size()));
                righePrimanota.add(rigaIncasso);
            }

            int startIndex = righePrimanota.size();
            //Queste sono le righe dei conti imputati.
            for (int iR = 0; iR < righeDocumento.size(); iR++) {
                MROldRigaDocumentoFiscale rigaFattura = (MROldRigaDocumentoFiscale) righeDocumento.get(iR);
                if (!ContabUtils.isRigaDescrittiva(rigaFattura)) {
                    RigaPrimanota rigaNuova = new RigaPrimanota();
                    //Per le fatture di acconto non impostiamo i km del veicolo.
                    if (documentoFiscale.getContratto() != null && !(documentoFiscale instanceof MROldFatturaAcconto)) {
                        rigaNuova.setVeicolo(documentoFiscale.getContratto().getMovimento().getVeicolo());
                        if (documentoFiscale.getContratto().getMovimento().getKmFine() != null) {
                            rigaNuova.setKmVeicolo(documentoFiscale.getContratto().getMovimento().getKmFine());
                        } else {
                            rigaNuova.setKmVeicolo(documentoFiscale.getContratto().getMovimento().getKmInizio());
                        }
                    } else if (documentoFiscale.getVeicolo() != null) {
                        rigaNuova.setVeicolo(documentoFiscale.getVeicolo());
                        if (documentoFiscale.getVeicolo().getKm() != null) {
                            rigaNuova.setKmVeicolo(documentoFiscale.getVeicolo().getKm());
                        } else {
                            rigaNuova.setKmVeicolo(new Integer(0));
                        }
                    } else if (rigaFattura.getR2r()!= null && rigaFattura.getR2r().getVehicle()!=null){
                        rigaNuova.setVeicolo(rigaFattura.getR2r().getVehicle());
                    }
                    rigaNuova.setConto(rigaFattura.getCodiceSottoconto());
                    rigaNuova.setCodiceIva(rigaFattura.getCodiceIva());
                    rigaNuova.setDescrizione(rigaFattura.getDescrizione());
                    if (veicolo != null) {
                        rigaNuova.setVeicolo(veicolo);
                    }
                    rigaNuova.setImporto(MathUtils.abs(rigaFattura.getTotaleImponibileRiga()));
                    if (rigaFattura.getTotaleImponibileRiga() >= 0.0) {
                        rigaNuova.setSegno(segnoAltro);
                    } else {
                        rigaNuova.setSegno(!segnoAltro);
                    }
                    rigaNuova.setPrimanota(primanotaNuova);
                    rigaNuova.setNumeroRiga(new Integer(righePrimanota.size()));
                    righePrimanota.add(rigaNuova);
                }
            }
            int stopIndex = righePrimanota.size() - 1;
            arrotondaRigheImputazioni(righePrimanota, startIndex, stopIndex, primanotaNuova, segnoAltro);

            //Aggiungiamo la riga dell'iva.
            RigaPrimanota rigaIva = new RigaPrimanota();
            //rigaIva.setConto(primanotaNuova.getCausale().getRegistroIva().getContoIva());
            MROldCausalePrimanota causale = (MROldCausalePrimanota) mySession.get(MROldCausalePrimanota.class, primanotaNuova.getCausale().getId());
            rigaIva.setConto(causale.getRegistroIva().getContoIva());
            rigaIva.setImporto(documentoFiscale.getTotaleIva());
            rigaIva.setSegno(segnoAltro);

            rigaIva.setPrimanota(primanotaNuova);
            rigaIva.setNumeroRiga(new Integer(righePrimanota.size()));
            righePrimanota.add(rigaIva);
        }
        return righePrimanota;
    }



    private static List creaRigheImposta(Session sx, MROldPrimanota primanotaNuova, MROldDocumentoFiscale documentoFiscale) {
        List righeImposta = new ArrayList();
        for (MROldCodiciIva codiceIva : documentoFiscale.getFatturaImposte().keySet()) {
            MROldRigaImpostaDocumentoFiscale rigaImpostaDocumentoFiscale = documentoFiscale.getFatturaImposte().get(codiceIva);
            MROldCausalePrimanota causale = (MROldCausalePrimanota) sx.get(MROldCausalePrimanota.class, primanotaNuova.getCausale().getId());

            RigaImposta rigaNuova = new RigaImposta(
                    null,
                    righeImposta.size(),
                    primanotaNuova,
                    //primanotaNuova.getCausale().getRegistroIva().getContoIva(),
                    causale.getRegistroIva().getContoIva(),
                    codiceIva,
                    rigaImpostaDocumentoFiscale.getImponibile(),
                    null,
                    rigaImpostaDocumentoFiscale.getImposta(),
                    primanotaNuova.getCausale().getSegnoIva(),
                    false);
            righeImposta.add(rigaNuova);
        }
        return righeImposta;
    }


    /**
     * Crea o aggiorna la primanota di un documento fiscale.
     * Attenzione, il documento fiscale deve avere un numero valido che viene usato come numero protocollo.     
     * @param sx La sessione al database
     * @param documentoFiscale Il documento fiscale per il quale si crea / aggiorna la primanota.
     * @param numeroRegistrazione Se presente, viene usato, altrimenti si genera un nuovo numero.
     * @return La primanota creata / aggiornata.
     * @throws org.hibernate.HibernateException In caso di errore del database.
     */
    public static MROldPrimanota newPrimanota(Session sx, MROldDocumentoFiscale documentoFiscale, MROldSede sedeOperativa, MROldNumerazione numerazioneRegistrazione, Integer numeroRegistrazione, User aUser) throws HibernateException {
        
        //fa il refresh
        
        if (documentoFiscale.getPrimanota() == null) {
            documentoFiscale.setPrimanota(new MROldPrimanota());
        }

        MROldPrimanota primanota = documentoFiscale.getPrimanota();
        primanota.setEditable(Boolean.FALSE);

        if (primanota.getCausale() == null) {
            if (documentoFiscale instanceof MROldFattura) {
                if (documentoFiscale.getTotaleAcconti() != null && documentoFiscale.getTotaleAcconti() > 0.0) {
                    primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.FATTURA_DI_SALDO));
                } else {
                    primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.FATTURA_DI_VENDITA));
                }
            } else if (documentoFiscale instanceof MROldFatturaAcconto) {
                primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.FATTURA_DI_ACCONTO));
            } else if (documentoFiscale instanceof MROldNotaCredito) {
                primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.NOTA_CREDITO_FATTURA_DI_VENDITA));
            } else if (documentoFiscale instanceof MROldRicevutaFiscale) {
                primanota.setCausale(ContabUtils.leggiCausale(sx, MROldCausalePrimanota.RICEVUTA_FISCALE_PAGATA));
            }
        }
        primanota.setUser(documentoFiscale.getUser());
        primanota.setUserModifica(aUser);
        primanota.setAffiliato(documentoFiscale.getAffiliato());
        primanota.setDataDocumento(documentoFiscale.getData());
        Calendar c = Calendar.getInstance();
        primanota.setDataModifica(c.getTime());
        primanota.setDataRegistrazione(documentoFiscale.getData());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setSezionale(documentoFiscale.getNumerazione());
        primanota.setNumeroProtocollo(documentoFiscale.getNumero());
        primanota.setNumeroDocumento(documentoFiscale.getNumero().toString());
        primanota.setNumeroDocumentoExtra(documentoFiscale.getPrefisso());

        if (primanota.getNumeroRegistrazione() == null) {
            if (numeroRegistrazione != null && numerazioneRegistrazione != null) {
                primanota.setNumerazione(numerazioneRegistrazione);
                primanota.setNumeroRegistrazione(numeroRegistrazione);
            } else {
                primanota.setNumerazione(NumerazioniUtils.getNumerazione(sx, sedeOperativa, MROldNumerazione.PRIMENOTE, documentoFiscale.getUser()));
                primanota.setNumeroRegistrazione(NumerazioniUtils.aggiornaProgressivo(
                        sx,
                        primanota.getNumerazione(),
                        primanota.getAnnoCompetenza()));
            }
        }

        primanota.setTotaleDocumento(documentoFiscale.getTotaleFattura());
        primanota.setTotaleImponibile(documentoFiscale.getTotaleImponibile());
        primanota.setTotaleImposta(documentoFiscale.getTotaleIva());

        if (primanota.getRigheImposta() != null) {
            primanota.getRigheImposta().clear();
        } else {
            primanota.setRigheImposta(new ArrayList());
        }

        if (Boolean.TRUE.equals(documentoFiscale.getDocumentoLibero())) {
            ImpostaFactory imposteFattura = new ImpostaFactory(documentoFiscale.getFatturaRighe());
            documentoFiscale.setFatturaImposte(imposteFattura.getImposte());
            documentoFiscale.setTotaleRighe(imposteFattura.getTotaleImponibile());
            documentoFiscale.setTotaleAcconti(imposteFattura.getTotaleAcconti());
            documentoFiscale.setTotaleImponibile(imposteFattura.getTotaleImponibile());
            documentoFiscale.setTotaleIva(imposteFattura.getTotaleImposta());
            documentoFiscale.setTotaleFattura(MathUtils.round(imposteFattura.getTotaleImponibile() + imposteFattura.getTotaleImposta()));
        }
        

        primanota.getRigheImposta().addAll(creaRigheImposta(primanota, documentoFiscale));

        if (primanota.getRighePrimanota() != null) {
            primanota.getRighePrimanota().clear();
        } else {
            primanota.setRighePrimanota(new ArrayList());
        }

        primanota.getRighePrimanota().addAll(creaRigheImputazioni(sx, primanota, documentoFiscale));

        return primanota;
    }

    /**
     * Crea le righe dell'imposta per una fattura, nota credito o ricevuta fiscale
     */
    private static List creaRigheImposta(MROldPrimanota primanotaNuova, MROldDocumentoFiscale documentoFiscale) {
        List righeImposta = new ArrayList();        
        for (MROldCodiciIva codiceIva : documentoFiscale.getFatturaImposte().keySet()) {
            MROldRigaImpostaDocumentoFiscale rigaImpostaDocumentoFiscale = documentoFiscale.getFatturaImposte().get(codiceIva);
            RigaImposta rigaNuova = new RigaImposta(
                    null,
                    righeImposta.size(),
                    primanotaNuova,
                    primanotaNuova.getCausale().getRegistroIva().getContoIva(),
                    codiceIva,
                    rigaImpostaDocumentoFiscale.getImponibile(),
                    null,
                    rigaImpostaDocumentoFiscale.getImposta(),
                    primanotaNuova.getCausale().getSegnoIva(),
                    false);
            righeImposta.add(rigaNuova);
        }
        return righeImposta;
    }

    private static List creaRigheImputazioni(Session mySession, MROldPrimanota primanotaNuova, MROldDocumentoFiscale documentoFiscale) throws HibernateException {
        List righePrimanota = null;
        List righeDocumento = documentoFiscale.getFatturaRighe();

        if (righeDocumento != null && righeDocumento.size() > 0) {
            righePrimanota = new ArrayList();
            //Per una nota credito il cliente va in AVERE, per fatture e ricevute in DARE.
            Boolean segnoIntestatario = (documentoFiscale instanceof MROldNotaCredito) ? MROldCausalePrimanota.AVERE : MROldCausalePrimanota.DARE;

            Boolean segnoAltro = new Boolean(!segnoIntestatario.booleanValue());

            //Aggiungiamo una prima riga dell'intestatario.
            RigaPrimanota rigaIntestazione = new RigaPrimanota();
            rigaIntestazione.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
            rigaIntestazione.setCliente(documentoFiscale.getCliente());
            if (documentoFiscale.getContratto() != null) {
                rigaIntestazione.setDescrizione(
                        MessageFormat.format(bundle.getString("PrimanotaFactory.msgRA0Del1"),
                        NumerazioniUtils.format(documentoFiscale.getContratto().getPrefisso(), documentoFiscale.getContratto().getNumero()),
                        documentoFiscale.getContratto().getData()));
            } else {
                rigaIntestazione.setDescrizione(null);
            }

            rigaIntestazione.setImporto(MathUtils.round(documentoFiscale.getTotaleFattura() + documentoFiscale.getTotaleAcconti()));
            rigaIntestazione.setSegno(segnoIntestatario);

            rigaIntestazione.setPrimanota(primanotaNuova);
            rigaIntestazione.setNumeroRiga(new Integer(righePrimanota.size()));
            righePrimanota.add(rigaIntestazione);

            if (documentoFiscale.getTotaleAcconti() > 0.0) {
                RigaPrimanota rigaGirocontoAnticipi = new RigaPrimanota(
                        primanotaNuova,
                        ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI_C_ANTICIPI),
                        null,
                        null,
                        documentoFiscale.getTotaleAcconti(),
                        segnoAltro,
                        righePrimanota.size());
                righePrimanota.add(rigaGirocontoAnticipi);
            }

            //Se abbiamo una ricevuta fiscale dobbiamo aggiungere le righe per l'incasso.
            //Correzione: se abbiamo una ricevuta fiscale che non proviene da un contratto!
            if (documentoFiscale instanceof MROldRicevutaFiscale && documentoFiscale.getContratto() != null) {

                RigaPrimanota rigaPagamento = new RigaPrimanota();
                rigaPagamento.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
                rigaPagamento.setCliente(documentoFiscale.getCliente());

                rigaPagamento.setDescrizione(null);
                rigaPagamento.setImporto(documentoFiscale.getTotaleFattura());
                rigaPagamento.setSegno(segnoAltro);

                rigaPagamento.setPrimanota(primanotaNuova);
                rigaPagamento.setNumeroRiga(new Integer(righePrimanota.size()));
                righePrimanota.add(rigaPagamento);

                RigaPrimanota rigaIncasso = new RigaPrimanota();
                if (documentoFiscale.getPagamento().getContoIncasso() != null) {
                    rigaIncasso.setConto(documentoFiscale.getPagamento().getContoIncasso());
                } else {
                    rigaIncasso.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CASSA_CONTANTI));
                }

                rigaIncasso.setDescrizione(null);
                rigaIncasso.setImporto(documentoFiscale.getTotaleFattura());
                rigaIncasso.setSegno(segnoIntestatario);

                rigaIncasso.setPrimanota(primanotaNuova);
                rigaIncasso.setNumeroRiga(new Integer(righePrimanota.size()));
                righePrimanota.add(rigaIncasso);
            }

            int startIndex = righePrimanota.size();
            //Queste sono le righe dei conti imputati.
            for (int iR = 0; iR < righeDocumento.size(); iR++) {
                MROldRigaDocumentoFiscale rigaFattura = (MROldRigaDocumentoFiscale) righeDocumento.get(iR);
                if (!ContabUtils.isRigaDescrittiva(rigaFattura)) {
                    RigaPrimanota rigaNuova = new RigaPrimanota();
                    //Per le fatture di acconto non impostiamo i km del veicolo.
                    if (documentoFiscale.getContratto() != null && !(documentoFiscale instanceof MROldFatturaAcconto)) {
                        rigaNuova.setVeicolo(documentoFiscale.getContratto().getMovimento().getVeicolo());
                        if (documentoFiscale.getContratto().getMovimento().getKmFine() != null) {
                            rigaNuova.setKmVeicolo(documentoFiscale.getContratto().getMovimento().getKmFine());
                        } else {
                            rigaNuova.setKmVeicolo(documentoFiscale.getContratto().getMovimento().getKmInizio());
                        }
                    } else if (documentoFiscale.getVeicolo() != null) {
                        rigaNuova.setVeicolo(documentoFiscale.getVeicolo());
                        if (documentoFiscale.getVeicolo().getKm() != null) {
                            rigaNuova.setKmVeicolo(documentoFiscale.getVeicolo().getKm());
                        } else {
                            rigaNuova.setKmVeicolo(new Integer(0));
                        }
                    }
                    rigaNuova.setConto(rigaFattura.getCodiceSottoconto());
                    rigaNuova.setCodiceIva(rigaFattura.getCodiceIva());
                    rigaNuova.setDescrizione(rigaFattura.getDescrizione());
                    rigaNuova.setImporto(MathUtils.abs(rigaFattura.getTotaleImponibileRiga()));
                    if (rigaFattura.getTotaleImponibileRiga() >= 0.0) {
                        rigaNuova.setSegno(segnoAltro);
                    } else {
                        rigaNuova.setSegno(!segnoAltro);
                    }
                    rigaNuova.setPrimanota(primanotaNuova);
                    rigaNuova.setNumeroRiga(new Integer(righePrimanota.size()));
                    righePrimanota.add(rigaNuova);
                }
            }
            int stopIndex = righePrimanota.size() - 1;
            arrotondaRigheImputazioni(righePrimanota, startIndex, stopIndex, primanotaNuova, segnoAltro);

            //Aggiungiamo la riga dell'iva.
            RigaPrimanota rigaIva = new RigaPrimanota();
            rigaIva.setConto(primanotaNuova.getCausale().getRegistroIva().getContoIva());
            rigaIva.setImporto(documentoFiscale.getTotaleIva());
            rigaIva.setSegno(segnoAltro);

            rigaIva.setPrimanota(primanotaNuova);
            rigaIva.setNumeroRiga(new Integer(righePrimanota.size()));
            righePrimanota.add(rigaIva);
        }
        return righePrimanota;
    }

    private static void arrotondaRigheImputazioni(List righeImputazioni, int startIndex, int stopIndex, MROldPrimanota primanota, boolean segnoPositivo) {
        if (startIndex <= stopIndex) {
            double totale = 0d;
            for (int i = startIndex; i <= stopIndex; i++) {
                RigaPrimanota riga = (RigaPrimanota) righeImputazioni.get(i);
                riga.setImporto(MathUtils.roundDouble(riga.getImporto().doubleValue()));
                if (riga.getSegno().equals(segnoPositivo)) {
                    totale = MathUtils.round(totale + riga.getImporto().doubleValue());
                } else {
                    totale = MathUtils.round(totale - riga.getImporto().doubleValue());
                }
            }
            double differenza = 0d;
            if (primanota.getTotaleImponibile() == null) {
                differenza = MathUtils.round(primanota.getTotaleDocumento().doubleValue() - totale);
            } else {
                differenza = MathUtils.round(primanota.getTotaleImponibile().doubleValue() - totale);
            }

            if (differenza != 0) {
                RigaPrimanota primaRiga = (RigaPrimanota) righeImputazioni.get(startIndex);
                log.debug("AggiornamentoPrimenote: Arrotondamento documento causale: " + primanota.getCausale()); //NOI18N
                log.debug("AggiornamentoPrimenote: Arrotondamento documento data: " + primanota.getDataRegistrazione()); //NOI18N
                log.debug("AggiornamentoPrimenote: Arrotondamento documento totale: " + primanota.getTotaleDocumento()); //NOI18N
                log.debug("AggiornamentoPrimenote: Arrotondamento documento numero: " + primanota.getNumeroDocumento()); //NOI18N
                log.debug("AggiornamentoPrimenote: Arrotondamento riga " + startIndex + " di " + differenza); //NOI18N
                primaRiga.setImporto(MathUtils.roundDouble(primaRiga.getImporto().doubleValue() + differenza));
            }
        }
    }

    public static MROldPrimanota creaPrimanotaDiAperturaAnnuale(Session sx, Integer anno, MROldCausalePrimanota causalePrimanota, MROldPrimanota primanotaChiusuraAStatoPatrimoniale) {
        if (primanotaChiusuraAStatoPatrimoniale == null) {
            return null;
        }

        MROldPrimanota primanota = new MROldPrimanota();

//        primanota.setEditable(false);

        primanota.setUser((User) Parameters.getUser());        
        primanota.setAffiliato(MROldAffiliato.getNoleggiatore());
        primanota.setCausale(causalePrimanota);
        GregorianCalendar gregorianCalendar = new GregorianCalendar(anno.intValue(), 00, 02, 00, 00, 00);
        primanota.setDataRegistrazione(gregorianCalendar.getTime());
        primanota.setAnnoCompetenza(anno);
        primanota.setNumerazione(NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE));

        primanota.setTotaleDocumento(primanotaChiusuraAStatoPatrimoniale.getTotaleDocumento());

        if (primanotaChiusuraAStatoPatrimoniale.getRighePrimanota() != null) {
            primanota.setRighePrimanota(new ArrayList());
            for (int i = 0; i < primanotaChiusuraAStatoPatrimoniale.getRighePrimanota().size(); i++) {
                RigaPrimanota tmpRigaOriginale = (RigaPrimanota) primanotaChiusuraAStatoPatrimoniale.getRighePrimanota().get(i);
                RigaPrimanota tmpRigaNuova = new RigaPrimanota();
                tmpRigaNuova.setPrimanota(primanota);
                tmpRigaNuova.setConto(tmpRigaOriginale.getConto());
                tmpRigaNuova.setCliente(tmpRigaOriginale.getCliente());
                tmpRigaNuova.setDataScadenza(tmpRigaOriginale.getDataScadenza());
                tmpRigaNuova.setDescrizione(tmpRigaOriginale.getDescrizione());
                tmpRigaNuova.setFornitore(tmpRigaOriginale.getFornitore());
                tmpRigaNuova.setImporto(tmpRigaOriginale.getImporto());
                tmpRigaNuova.setKmVeicolo(tmpRigaOriginale.getKmVeicolo());
                tmpRigaNuova.setNumeroRiga(new Integer(i));

                tmpRigaNuova.setSegno(new Boolean(!tmpRigaOriginale.getSegno().booleanValue()));
                //tmpRigaNuova.setSegno(tmpRigaOriginale.getSegno());
                tmpRigaNuova.setVeicolo(tmpRigaOriginale.getVeicolo());
                primanota.getRighePrimanota().add(tmpRigaNuova);
            }
        }
        return primanota;
    }

    public static MROldPrimanota creaPrimanotaIncassoCauzione(Session mySession, MROldSede aSede, MROldContrattoNoleggio contratto, MROldPagamento pagamento, Double importo, User user) {
        MROldCausalePrimanota causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.INCASSO_CAUZIONE);

        MROldPrimanota primanota = new MROldPrimanota();

        //User user = (User) Parameters.getUser();
        primanota.setUser(user);
        primanota.setSede(aSede);

//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(user.getAffiliato());
        primanota.setDataRegistrazione(FormattedDate.formattedDate());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setTotaleDocumento(importo);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);

        primanota.setPrenotazione(null);
        primanota.setContratto(contratto);

        primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
        //primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), FormattedDate.annoCorrente()));

        primanota.setRighePrimanota(new ArrayList());

        String descrizioneContratto = MessageFormat.format(
                bundle.getString("PrimanotaFactory.msgRA0Del1"),
                NumerazioniUtils.format(contratto.getPrefisso(), contratto.getNumero()),
                contratto.getData());
        String descrizioneCliente = contratto.getCliente().toString();

        RigaPrimanota rigaCassa = new RigaPrimanota();
        rigaCassa.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CASSA_CAUZIONI));
        rigaCassa.setPagamento(pagamento);
        rigaCassa.setDescrizione(descrizioneContratto + " " + descrizioneCliente); //NOI18N
        rigaCassa.setImporto(importo);
        rigaCassa.setSegno(MROldCausalePrimanota.DARE);
        rigaCassa.setPrimanota(primanota);
        rigaCassa.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCassa);

        RigaPrimanota rigaCliente = new RigaPrimanota();
        rigaCliente.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
        rigaCliente.setCliente(contratto.getCliente());
        rigaCliente.setDescrizione(descrizioneContratto);
        rigaCliente.setImporto(importo);
        rigaCliente.setSegno(MROldCausalePrimanota.AVERE);
        rigaCliente.setPrimanota(primanota);
        rigaCliente.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCliente);

        return primanota;
    }

    public static MROldPrimanota creaPrimanotaIncassoCauzione(Session mySession, MROldSede aSede, MROldPrenotazione prenotazione, MROldPagamento pagamento, Double importo, User user) {
        MROldCausalePrimanota causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.INCASSO_CAUZIONE);

        MROldPrimanota primanota = new MROldPrimanota();

        //User user = (User) Parameters.getUser();
        primanota.setUser(user);
        primanota.setSede(aSede);

//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(user.getAffiliato());
        primanota.setDataRegistrazione(FormattedDate.formattedDate());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setTotaleDocumento(importo);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);
        primanota.setPrenotazione(prenotazione);
        primanota.setContratto(null);

        primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
        //primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), FormattedDate.annoCorrente()));

        primanota.setRighePrimanota(new ArrayList());

        String descrizioneContratto = MessageFormat.format(
                bundle.getString("PrimanotaFactory.msgRA0Del1"),
                NumerazioniUtils.format(prenotazione.getPrefisso(), prenotazione.getNumero()),
                prenotazione.getData());
        String descrizioneCliente = prenotazione.getCliente().toString();

        RigaPrimanota rigaCassa = new RigaPrimanota();
        rigaCassa.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CASSA_CAUZIONI));
        rigaCassa.setPagamento(pagamento);
        rigaCassa.setDescrizione(descrizioneContratto + " " + descrizioneCliente); //NOI18N
        rigaCassa.setImporto(importo);
        rigaCassa.setSegno(MROldCausalePrimanota.DARE);
        rigaCassa.setPrimanota(primanota);
        rigaCassa.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCassa);

        RigaPrimanota rigaCliente = new RigaPrimanota();
        rigaCliente.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
        rigaCliente.setCliente(prenotazione.getCliente());
        rigaCliente.setDescrizione(descrizioneContratto);
        rigaCliente.setImporto(importo);
        rigaCliente.setSegno(MROldCausalePrimanota.AVERE);
        rigaCliente.setPrimanota(primanota);
        rigaCliente.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCliente);

        return primanota;
    }

    public static MROldPrimanota creaPrimanotaRimborsoCauzione(Session mySession, MROldSede aSede, MROldContrattoNoleggio contratto, MROldPagamento pagamento, Double importo, User user) {
        MROldCausalePrimanota causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.RIMBORSO_CAUZIONE);
        if (causale == null) {
            return null;
        }
        MROldPrimanota primanota = new MROldPrimanota();

        //User user = (User) Parameters.getUser();
        primanota.setUser(user);
        primanota.setSede(aSede);

//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(user.getAffiliato());
        primanota.setDataRegistrazione(FormattedDate.formattedDate());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setTotaleDocumento(importo);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);

        primanota.setPrenotazione(null);
        primanota.setContratto(contratto);

        primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
        //primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), FormattedDate.annoCorrente()));

        primanota.setRighePrimanota(new ArrayList());

        String descrizioneContratto = MessageFormat.format(
                bundle.getString("PrimanotaFactory.msgRA0Del1"),
                NumerazioniUtils.format(contratto.getPrefisso(), contratto.getNumero()),
                contratto.getData());
        String descrizioneCliente = contratto.getCliente().toString();

        RigaPrimanota rigaCassa = new RigaPrimanota();
        rigaCassa.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CASSA_CAUZIONI));
        rigaCassa.setPagamento(pagamento);
        rigaCassa.setDescrizione(descrizioneContratto + " " + descrizioneCliente); //NOI18N
        rigaCassa.setImporto(importo);
        rigaCassa.setSegno(MROldCausalePrimanota.AVERE);
        rigaCassa.setPrimanota(primanota);
        rigaCassa.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCassa);

        RigaPrimanota rigaCliente = new RigaPrimanota();
        rigaCliente.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
        rigaCliente.setCliente(contratto.getCliente());
        rigaCliente.setDescrizione(descrizioneContratto);
        rigaCliente.setImporto(importo);
        rigaCliente.setSegno(MROldCausalePrimanota.DARE);
        rigaCliente.setPrimanota(primanota);
        rigaCliente.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCliente);

        return primanota;
    }

    public static MROldPrimanota creaPrimanotaRimborsoCauzione(Session mySession, MROldSede aSede, MROldPrenotazione prenotazione, MROldPagamento pagamento, Double importo, User user) {
        MROldCausalePrimanota causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.RIMBORSO_CAUZIONE);
        if (causale == null) {
            return null;
        }
        MROldPrimanota primanota = new MROldPrimanota();

        //User user = (User) Parameters.getUser();
        primanota.setUser(user);
        primanota.setSede(aSede);

//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(user.getAffiliato());
        primanota.setDataRegistrazione(FormattedDate.formattedDate());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setTotaleDocumento(importo);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);
        primanota.setPrenotazione(prenotazione);
        primanota.setContratto(null);

        primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
        //primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), FormattedDate.annoCorrente()));

        primanota.setRighePrimanota(new ArrayList());

        String descrizioneContratto = MessageFormat.format(
                bundle.getString("PrimanotaFactory.msgRA0Del1"),
                NumerazioniUtils.format(prenotazione.getPrefisso(), prenotazione.getNumero()),
                prenotazione.getData());
        String descrizioneCliente = prenotazione.getCliente().toString();

        RigaPrimanota rigaCassa = new RigaPrimanota();
        rigaCassa.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CASSA_CAUZIONI));
        rigaCassa.setPagamento(pagamento);
        rigaCassa.setDescrizione(descrizioneContratto + " " + descrizioneCliente); //NOI18N
        rigaCassa.setImporto(importo);
        rigaCassa.setSegno(MROldCausalePrimanota.AVERE);
        rigaCassa.setPrimanota(primanota);
        rigaCassa.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCassa);

        RigaPrimanota rigaCliente = new RigaPrimanota();
        rigaCliente.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
        rigaCliente.setCliente(prenotazione.getCliente());
        rigaCliente.setDescrizione(descrizioneContratto);
        rigaCliente.setImporto(importo);
        rigaCliente.setSegno(MROldCausalePrimanota.DARE);
        rigaCliente.setPrimanota(primanota);
        rigaCliente.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCliente);

        return primanota;
    }

    public static MROldPrimanota creaPrimanotaRimessaCassa(Session sx, Date dataRimessa, MROldSede aSede, List<RigaRimessa> righe, User user) {
        MROldCausalePrimanota causale = ContabUtils.leggiCausale(sx, MROldCausalePrimanota.VERSAMENTO_BANCA);
        if (causale == null) {
            return null;
        }

        MROldPrimanota primanota = new MROldPrimanota();

        //User user = (User) Parameters.getUser();
        primanota.setUser(user);
        primanota.setSede(aSede);

//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(user.getAffiliato());
        primanota.setDataRegistrazione(dataRimessa);
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(dataRimessa));

//        primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
//        primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), primanota.getAnnoCompetenza()));

        primanota.setRighePrimanota(new ArrayList());

        MROldPianoDeiConti contoBanca = ContabUtils.leggiSottoconto(sx, MROldPianoDeiConti.BANCA1);

        Iterator<RigaRimessa> rimesse = righe.iterator();
        Double totale = 0.0;

        while (rimesse.hasNext()) {
            RigaRimessa rimessa = rimesse.next();
            if (!rimessa.getRimesso()) {
                continue;
            }
            RigaPrimanota rigaRimessa = new RigaPrimanota();
//            StringBuffer descrizione = new StringBuffer().
//                    append(causale.getDescrizione()).
//                    append(" - ").
//                    append(user.getNomeCognome()).
//                    append(" - ").
//                    append(aSede.toString()).
//                    append(" - ").
//                    append(rimessa.getPagamento());
//            if(rimessa.getDescrizioneAggiuntiva() != null) {
//                descrizione.append("(").append(rimessa.getDescrizioneAggiuntiva()).append(")");
//            }

            rigaRimessa.setDescrizione(rimessa.getDescrizioneAggiuntiva());
            rigaRimessa.setPrimanota(primanota);
            rigaRimessa.setConto(rimessa.getConto());
            rigaRimessa.setPagamento(rimessa.getPagamento());
            rigaRimessa.setGaranzia(rimessa.getGaranzia());
            rigaRimessa.setImporto(rimessa.getImportoRimessa());
            rigaRimessa.setSegno(MROldCausalePrimanota.AVERE);

            rigaRimessa.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
            primanota.getRighePrimanota().add(rigaRimessa);

            RigaPrimanota rigaBanca = new RigaPrimanota();
            rigaBanca.setDescrizione(rimessa.getDescrizioneAggiuntiva());
            rigaBanca.setPrimanota(primanota);
            rigaBanca.setConto(contoBanca);
            rigaBanca.setPagamento(rigaRimessa.getPagamento());
            rigaBanca.setGaranzia(rigaRimessa.getGaranzia());
            rigaBanca.setImporto(rigaRimessa.getImporto());
            rigaBanca.setSegno(MROldCausalePrimanota.DARE);
            rigaBanca.setNumeroRiga(primanota.getRighePrimanota().size());
            primanota.getRighePrimanota().add(rigaBanca);

            totale = MathUtils.roundDouble(totale + rigaRimessa.getImporto());
        }

        primanota.setTotaleDocumento(totale);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);

        return primanota;
    }

    public static MROldPrimanota creaPrimanotaAperturaCassa(Session sx, MROldPrimanota primanotaChiusura, Date dataApertura, MROldSede aSede, User aUser) {
        MROldCausalePrimanota causale = ContabUtils.leggiCausale(sx, MROldCausalePrimanota.APERTURA_CASSA);
        if (causale == null) {
            return null;
        }

        MROldPrimanota primanota = new MROldPrimanota();

       // User user = (User) Parameters.getUser();
        primanota.setUser(aUser);
        primanota.setSede(aSede);

//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(aUser.getAffiliato());
        primanota.setDataRegistrazione(dataApertura);
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(dataApertura));

//        primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
//        primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), primanota.getAnnoCompetenza()));

        primanota.setRighePrimanota(new ArrayList());

        Iterator righeChiusura = primanotaChiusura.getRighePrimanota().iterator();

        while (righeChiusura.hasNext()) {
            RigaPrimanota rigaChiusura = (RigaPrimanota) righeChiusura.next();

            RigaPrimanota rigaApertura = new RigaPrimanota();
//            rigaApertura.setDescrizione(causale.getDescrizione() + " - " + user.getNomeCognome() + " - " + aSede.toString() + " - " + rigaChiusura.getPagamento().toString());
            rigaApertura.setPrimanota(primanota);
            rigaApertura.setConto(rigaChiusura.getConto());
            rigaApertura.setPagamento(rigaChiusura.getPagamento());
            rigaApertura.setImporto(rigaChiusura.getImporto());
            rigaApertura.setSegno(!rigaChiusura.getSegno());

            rigaApertura.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
            primanota.getRighePrimanota().add(rigaApertura);
        }

        primanota.setTotaleDocumento(primanotaChiusura.getTotaleDocumento());
        primanota.setTotaleImponibile(primanotaChiusura.getTotaleImponibile());
        primanota.setTotaleImposta(primanotaChiusura.getTotaleImposta());

        return primanota;
    }

    public static MROldPrimanota creaPrimanotaChiusuraCassa(Session sx, Date dataChiusura, MROldSede aSede, List<RigaRimessa> righeRimesse, User aUser) {
        MROldCausalePrimanota causale = ContabUtils.leggiCausale(sx, MROldCausalePrimanota.CHIUSURA_CASSA);
        if (causale == null) {
            return null;
        }

        MROldPrimanota primanota = new MROldPrimanota();

        //User user = (User) Parameters.getUser();
        primanota.setUser(aUser);
        primanota.setSede(aSede);

//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(aUser.getAffiliato());
        primanota.setDataRegistrazione(dataChiusura);
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(dataChiusura));

//        primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
//        primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), primanota.getAnnoCompetenza()));

        primanota.setRighePrimanota(new ArrayList());

        Double totale = 0.0;

        Iterator righe = righeRimesse.iterator();

        while (righe.hasNext()) {
            RigaRimessa rigaRimessa = (RigaRimessa) righe.next();
            Double saldo = MathUtils.round(rigaRimessa.getSaldoAttuale());
            if (rigaRimessa.getRimesso()) {
                saldo = MathUtils.round(rigaRimessa.getSaldoAttuale() - rigaRimessa.getImportoRimessa());
            }

            if (saldo == 0.0) {
                continue;
            }

            RigaPrimanota rigaCassa = new RigaPrimanota();
//            rigaCassa.setDescrizione(
//                    causale.getDescrizione() + " - " +
//                    user.getNomeCognome() + " - " +
//                    aSede.toString() + " - " +
//                    rigaRimessa.getPagamento().toString());
            rigaCassa.setPrimanota(primanota);
            rigaCassa.setConto(rigaRimessa.getConto());
            rigaCassa.setPagamento(rigaRimessa.getPagamento());
            rigaCassa.setImporto(saldo);
            rigaCassa.setSegno(MROldCausalePrimanota.AVERE);

            rigaCassa.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
            primanota.getRighePrimanota().add(rigaCassa);

            MROldPianoDeiConti contoChiusura = ContabUtils.leggiSottoconto(sx, MROldPianoDeiConti.CHIUSURA_CASSA);

            RigaPrimanota rigaChiusura = new RigaPrimanota();
//            rigaChiusura.setDescrizione(
//                    causale.getDescrizione() + " - " +
//                    user.getNomeCognome() + " - " +
//                    aSede.toString() + " - " +
//                    rigaRimessa.getPagamento().toString());
            rigaChiusura.setPrimanota(primanota);
            rigaChiusura.setConto(contoChiusura);
            rigaChiusura.setPagamento(rigaRimessa.getPagamento());
            rigaChiusura.setImporto(saldo);
            rigaChiusura.setSegno(MROldCausalePrimanota.DARE);

            rigaChiusura.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
            primanota.getRighePrimanota().add(rigaChiusura);

            totale = MathUtils.roundDouble(totale + saldo);

        }

        primanota.setTotaleDocumento(totale);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);

        return primanota;
    }

    public static MROldPrimanota creaPrimanotaIncassoCliente(Session mySession, MROldSede aSede, MROldContrattoNoleggio contratto, MROldPagamento pagamento, Garanzia garanzia, Double importo,User user) {
        MROldCausalePrimanota causale;
        if (Boolean.TRUE.equals(pagamento.getContanti())) {
            causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.INCASSO_CLIENTE);
        } else {
            causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.INCASSO_CLIENTE_CARTA_DI_CREDITO);
        }

        if (causale == null) {
            return null;
        }

        MROldPrimanota primanota = new MROldPrimanota();
        primanota.setUser(user);
        primanota.setSede(aSede);

//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(user.getAffiliato());
        primanota.setDataRegistrazione(FormattedDate.formattedDate());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setTotaleDocumento(importo);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);

        primanota.setPrenotazione(null);
        primanota.setContratto(contratto);

        //primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
        //primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), FormattedDate.annoCorrente()));

        primanota.setRighePrimanota(new ArrayList());

        String descrizioneContratto = MessageFormat.format(
                bundle.getString("PrimanotaFactory.msgRA0Del1"),
                NumerazioniUtils.format(contratto.getPrefisso(), contratto.getNumero()),
                contratto.getData());
        String descrizioneCliente = contratto.getCliente().toString();

        RigaPrimanota rigaIncasso = new RigaPrimanota();
        rigaIncasso.setConto(pagamento.getContoIncasso());
        rigaIncasso.setPagamento(pagamento);
        rigaIncasso.setGaranzia(garanzia);
        rigaIncasso.setDescrizione(descrizioneContratto + " " + descrizioneCliente); //NOI18N
        rigaIncasso.setImporto(importo);
        rigaIncasso.setSegno(MROldCausalePrimanota.DARE);
        rigaIncasso.setPrimanota(primanota);
        rigaIncasso.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaIncasso);

        RigaPrimanota rigaCliente = new RigaPrimanota();
        rigaCliente.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
        rigaCliente.setCliente(contratto.getCliente());
        rigaCliente.setDescrizione(descrizioneContratto);
        rigaCliente.setImporto(importo);
        rigaCliente.setSegno(MROldCausalePrimanota.AVERE);
        rigaCliente.setPrimanota(primanota);
        rigaCliente.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCliente);

        //mySession.save(primanota);
       // mySession.flush();
        return primanota;
    }

    public static MROldPrimanota creaPrimanotaIncassoClientePrepagato(Session mySession, MROldSede aSede, MROldPrenotazione prenotazione, MROldPagamento pagamento, Garanzia garanzia, Double importo) {
        MROldCausalePrimanota causale;
        if (Boolean.TRUE.equals(pagamento.getContanti())) {
            causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.INCASSO_CLIENTE);
        } else {
            causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.INCASSO_CLIENTE_CARTA_DI_CREDITO);
        }

        if (causale == null) {
            return null;
        }

        MROldPrimanota primanota = new MROldPrimanota();

        User user = (User) Parameters.getUser();
        primanota.setUser(user);
        primanota.setSede(aSede);

//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(MROldAffiliato.getNoleggiatore());
        primanota.setDataRegistrazione(FormattedDate.formattedDate());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setTotaleDocumento(importo);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);

        //primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
        //primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), FormattedDate.annoCorrente()));

        primanota.setRighePrimanota(new ArrayList());

        String descrizioneContratto = MessageFormat.format(
                bundle.getString("PrimanotaFactory.msgRA0Del1"),
                NumerazioniUtils.format(prenotazione.getPrefisso(), prenotazione.getNumero()),
                prenotazione.getData());
        String descrizioneCliente = prenotazione.getCommissione().getFonteCommissione().getCliente().toString();

        RigaPrimanota rigaIncasso = new RigaPrimanota();
        rigaIncasso.setConto(pagamento.getContoIncasso());
        rigaIncasso.setPagamento(pagamento);
        rigaIncasso.setGaranzia(garanzia);
        rigaIncasso.setDescrizione(descrizioneContratto + " " + descrizioneCliente); //NOI18N
        rigaIncasso.setImporto(importo);
        rigaIncasso.setSegno(MROldCausalePrimanota.DARE);
        rigaIncasso.setPrimanota(primanota);
        rigaIncasso.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaIncasso);

        RigaPrimanota rigaCliente = new RigaPrimanota();
        rigaCliente.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
        rigaCliente.setCliente(prenotazione.getCommissione().getFonteCommissione().getCliente());
        rigaCliente.setDescrizione(descrizioneContratto);
        rigaCliente.setImporto(importo);
        rigaCliente.setSegno(MROldCausalePrimanota.AVERE);
        rigaCliente.setPrimanota(primanota);
        rigaCliente.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCliente);

        return primanota;
    }

    public static MROldPrimanota creaPrimanotaIncassoCliente(Session mySession, MROldSede aSede, MROldPrenotazione prenotazione, MROldPagamento pagamento, Garanzia garanzia, Double importo, User user) {
        MROldCausalePrimanota causale;
        if (Boolean.TRUE.equals(pagamento.getContanti())) {
            causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.INCASSO_CLIENTE);
        } else {
            causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.INCASSO_CLIENTE_CARTA_DI_CREDITO);
        }

        if (causale == null) {
            return null;
        }

        MROldPrimanota primanota = new MROldPrimanota();

        //User user = (User) Parameters.getUser();
        primanota.setUser(user);
        primanota.setSede(aSede);
//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(user.getAffiliato());
        primanota.setDataRegistrazione(FormattedDate.formattedDate());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setTotaleDocumento(importo);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);

        primanota.setContratto(PrenotazioniUtils.contrattoEsistente(mySession, prenotazione));
        primanota.setPrenotazione(prenotazione);
        //primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
        //primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), FormattedDate.annoCorrente()));

        primanota.setRighePrimanota(new ArrayList());

        String descrizioneContratto = MessageFormat.format(
                bundle.getString("PrimanotaFactory.msgRA0Del1"),
                NumerazioniUtils.format(prenotazione.getPrefisso(), prenotazione.getNumero()),
                prenotazione.getData());
        String descrizioneCliente = prenotazione.getCliente().toString();

        RigaPrimanota rigaIncasso = new RigaPrimanota();
        rigaIncasso.setConto(pagamento.getContoIncasso());
        rigaIncasso.setPagamento(pagamento);
        rigaIncasso.setGaranzia(garanzia);
        rigaIncasso.setDescrizione(descrizioneContratto + " " + descrizioneCliente); //NOI18N
        rigaIncasso.setImporto(importo);
        rigaIncasso.setSegno(MROldCausalePrimanota.DARE);
        rigaIncasso.setPrimanota(primanota);
        rigaIncasso.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaIncasso);

        RigaPrimanota rigaCliente = new RigaPrimanota();
        rigaCliente.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
        rigaCliente.setCliente(prenotazione.getCliente());
        rigaCliente.setDescrizione(descrizioneContratto);
        rigaCliente.setImporto(importo);
        rigaCliente.setSegno(MROldCausalePrimanota.AVERE);
        rigaCliente.setPrimanota(primanota);
        rigaCliente.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCliente);

        return primanota;
    }

    public static MROldPrimanota creaPrimanotaIncassoCliente(Session mySession, MROldSede aSede, MROldClienti cliente, MROldPagamento pagamento, Garanzia garanzia, Double importo, String descrizioneAggiuntiva, User user) {
        MROldCausalePrimanota causale;
        if (Boolean.TRUE.equals(pagamento.getContanti())) {
            causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.INCASSO_CLIENTE);
        } else {
            causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.INCASSO_CLIENTE_CARTA_DI_CREDITO);
        }

        if (causale == null) {
            return null;
        }

        MROldPrimanota primanota = new MROldPrimanota();

        //User user = (User) Parameters.getUser();
        primanota.setUser(user);
        primanota.setSede(aSede);

        primanota.setCausale(causale);
        primanota.setAffiliato(user.getAffiliato());
        primanota.setDataRegistrazione(FormattedDate.formattedDate());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setTotaleDocumento(importo);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);

        //primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
        //primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), FormattedDate.annoCorrente()));

        primanota.setRighePrimanota(new ArrayList());

        RigaPrimanota rigaIncasso = new RigaPrimanota();
        rigaIncasso.setConto(pagamento.getContoIncasso());
        rigaIncasso.setPagamento(pagamento);
        rigaIncasso.setGaranzia(garanzia);
        rigaIncasso.setDescrizione(new StringBuffer().append(descrizioneAggiuntiva).append(" ").append(cliente).toString()); //NOI18N
        rigaIncasso.setImporto(importo);
        rigaIncasso.setSegno(MROldCausalePrimanota.DARE);
        rigaIncasso.setPrimanota(primanota);
        rigaIncasso.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaIncasso);

        RigaPrimanota rigaCliente = new RigaPrimanota();
        rigaCliente.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
        rigaCliente.setCliente(cliente);
        rigaCliente.setDescrizione(descrizioneAggiuntiva);
        rigaCliente.setImporto(importo);
        rigaCliente.setSegno(MROldCausalePrimanota.AVERE);
        rigaCliente.setPrimanota(primanota);
        rigaCliente.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCliente);

        return primanota;
    }

    public static MROldPrimanota creaPrimanotaRilevazioneSpese(Session mySession, MROldSede aSede, MROldPagamento pagamento, Double importo, MROldPianoDeiConti contoSpesa, String descrizioneAggiuntiva, User user) {
        MROldCausalePrimanota causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.RILEVAZIONE_SPESE_VARIE);

        if (causale == null) {
            return null;
        }

        MROldPrimanota primanota = new MROldPrimanota();

        /*User user = (User) Parameters.getUser();*/
        primanota.setUser(user);
        primanota.setSede(aSede);

//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(user.getAffiliato());
        primanota.setDataRegistrazione(FormattedDate.formattedDate());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setTotaleDocumento(importo);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);

        //primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
        //primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), FormattedDate.annoCorrente()));

        primanota.setRighePrimanota(new ArrayList());

        RigaPrimanota rigaCassa = new RigaPrimanota();
        rigaCassa.setConto(pagamento.getContoIncasso());
        rigaCassa.setPagamento(pagamento);
        rigaCassa.setDescrizione(contoSpesa.getDescrizione() + " - " + descrizioneAggiuntiva); //NOI18N
        rigaCassa.setImporto(importo);
        rigaCassa.setSegno(MROldCausalePrimanota.AVERE);
        rigaCassa.setPrimanota(primanota);
        rigaCassa.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCassa);

        RigaPrimanota rigaSpesa = new RigaPrimanota();
        rigaSpesa.setConto(contoSpesa);
        rigaSpesa.setDescrizione(descrizioneAggiuntiva);
        rigaSpesa.setImporto(importo);
        rigaSpesa.setSegno(MROldCausalePrimanota.DARE);
        rigaSpesa.setPrimanota(primanota);
        rigaSpesa.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaSpesa);

        return primanota;
    }

    public static MROldPrimanota creaPrimanotaRimborsoCliente(Session mySession, MROldSede aSede, MROldContrattoNoleggio contratto, MROldPagamento pagamento, Double importo, User user) {
        MROldCausalePrimanota causale;
        if (Boolean.TRUE.equals(pagamento.getContanti())) {
            causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.PAGAMENTO_A_CLIENTE);
        } else {
            causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.BONIFICO_O_PAGAMENTO_CON_ASSEGNO_A_CLIENTE);
        }

        if (causale == null) {
            return null;
        }

        MROldPrimanota primanota = new MROldPrimanota();

        //User user = (User) Parameters.getUser();
        primanota.setUser(user);
        primanota.setSede(aSede);

//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(user.getAffiliato());
        primanota.setDataRegistrazione(FormattedDate.formattedDate());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setTotaleDocumento(importo);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);
        primanota.setContratto(contratto);

        primanota.setPrenotazione(null);

        //primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
        //primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), FormattedDate.annoCorrente()));

        primanota.setRighePrimanota(new ArrayList());

        String descrizioneContratto = MessageFormat.format(
                bundle.getString("PrimanotaFactory.msgRA0Del1"),
                NumerazioniUtils.format(contratto.getPrefisso(), contratto.getNumero()),
                contratto.getData());
        String descrizioneCliente = contratto.getCliente().toString();

        RigaPrimanota rigaRimborso = new RigaPrimanota();
        rigaRimborso.setConto(pagamento.getContoIncasso());
        rigaRimborso.setPagamento(pagamento);
        rigaRimborso.setDescrizione(descrizioneContratto + " " + descrizioneCliente); //NOI18N
        rigaRimborso.setImporto(importo);
        rigaRimborso.setSegno(MROldCausalePrimanota.AVERE);
        rigaRimborso.setPrimanota(primanota);
        rigaRimborso.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaRimborso);

        RigaPrimanota rigaCliente = new RigaPrimanota();
        rigaCliente.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
        rigaCliente.setCliente(contratto.getCliente());
        rigaCliente.setDescrizione(descrizioneContratto);
        rigaCliente.setImporto(importo);
        rigaCliente.setSegno(MROldCausalePrimanota.DARE);
        rigaCliente.setPrimanota(primanota);
        rigaCliente.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCliente);

        return primanota;
    }

    public static MROldPrimanota creaPrimanotaRimborsoCliente(Session mySession, MROldSede aSede, MROldPrenotazione prenotazione, MROldPagamento pagamento, Double importo, User user) {
        MROldCausalePrimanota causale;
        if (Boolean.TRUE.equals(pagamento.getContanti())) {
            causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.PAGAMENTO_A_CLIENTE);
        } else {
            causale = ContabUtils.leggiCausale(mySession, MROldCausalePrimanota.BONIFICO_O_PAGAMENTO_CON_ASSEGNO_A_CLIENTE);
        }

        if (causale == null) {
            return null;
        }

        MROldPrimanota primanota = new MROldPrimanota();

        //User user = (User) Parameters.getUser();
        primanota.setUser(user);
        primanota.setSede(aSede);

//        primanota.setEditable(Boolean.FALSE);
        primanota.setCausale(causale);
        primanota.setAffiliato(user.getAffiliato());
        primanota.setDataRegistrazione(FormattedDate.formattedDate());
        primanota.setAnnoCompetenza(FormattedDate.annoCorrente(primanota.getDataRegistrazione()));
        primanota.setTotaleDocumento(importo);
        primanota.setTotaleImponibile(null);
        primanota.setTotaleImposta(null);
        primanota.setPrenotazione(prenotazione);
        primanota.setContratto(PrenotazioniUtils.contrattoEsistente(mySession, prenotazione));

        //primanota.setNumerazione(NumerazioniUtils.getNumerazione(mySession, MROldNumerazione.PRIMENOTE));
        //primanota.setNumeroRegistrazione(NumerazioniUtils.nuovoNumero(mySession, primanota.getNumerazione(), FormattedDate.annoCorrente()));

        primanota.setRighePrimanota(new ArrayList());

        String descrizioneContratto = MessageFormat.format(
                bundle.getString("PrimanotaFactory.msgRA0Del1"),
                NumerazioniUtils.format(prenotazione.getPrefisso(), prenotazione.getNumero()),
                prenotazione.getData());
        String descrizioneCliente = prenotazione.getCliente().toString();

        RigaPrimanota rigaRimborso = new RigaPrimanota();
        rigaRimborso.setConto(pagamento.getContoIncasso());
        rigaRimborso.setPagamento(pagamento);
        rigaRimborso.setDescrizione(descrizioneContratto + " " + descrizioneCliente); //NOI18N
        rigaRimborso.setImporto(importo);
        rigaRimborso.setSegno(MROldCausalePrimanota.AVERE);
        rigaRimborso.setPrimanota(primanota);
        rigaRimborso.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaRimborso);

        RigaPrimanota rigaCliente = new RigaPrimanota();
        rigaCliente.setConto(ContabUtils.leggiSottoconto(mySession, MROldPianoDeiConti.CLIENTI));
        rigaCliente.setCliente(prenotazione.getCliente());
        rigaCliente.setDescrizione(descrizioneContratto);
        rigaCliente.setImporto(importo);
        rigaCliente.setSegno(MROldCausalePrimanota.DARE);
        rigaCliente.setPrimanota(primanota);
        rigaCliente.setNumeroRiga(new Integer(primanota.getRighePrimanota().size()));
        primanota.getRighePrimanota().add(rigaCliente);

        return primanota;
    }

    /**
     * Crea la primanota di chiusura annuale. Non imposta il numero di registrazione e non salva nel database.
     * @param sx
     * @param anno L'anno di competenza
     * @param causalePrimanota La causale da usare
     * @param mastro1
     * @param mastro2
     * @param sottoConto
     * @return
     */
    public static MROldPrimanota creaPrimanotaDiChiusura(Session sx, Integer anno, MROldCausalePrimanota causalePrimanota, Integer mastro1, Integer mastro2, MROldPianoDeiConti sottoConto) {
        MROldPrimanota primanota = new MROldPrimanota();

//        primanota.setEditable(false);

        primanota.setUser((User) Parameters.getUser());
        primanota.setAffiliato(MROldAffiliato.getNoleggiatore());
        primanota.setCausale(causalePrimanota);
        GregorianCalendar gregorianCalendar = new GregorianCalendar(anno.intValue(), 11, 31, 00, 00, 00);
        primanota.setDataRegistrazione(gregorianCalendar.getTime());
        primanota.setAnnoCompetenza(anno);
        primanota.setNumerazione(NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE));

        primanota.setRighePrimanota(new ArrayList());
        double totaleDocumento = 0;
        Double importoDare;
        Double importoAvere;
        Double importoDifferenza;
        int rowCount = 0;


        List conti = ContabUtils.leggiConti(sx, mastro1, mastro2);
        List clienti = ContabUtils.leggiClientiMovimentati(sx, anno);
        List fornitori = ContabUtils.leggiFornitoriMovimentati(sx, anno);
        List importiDare = ContabUtils.leggiSumRighePrimanoteDaChiudere(sx, anno, MROldCausalePrimanota.DARE, clienti, fornitori, conti);
        List importiAvere = ContabUtils.leggiSumRighePrimanoteDaChiudere(sx, anno, MROldCausalePrimanota.AVERE, clienti, fornitori, conti);

        for (int iP = 0; iP < conti.size(); iP++) {
            MROldPianoDeiConti conto = (MROldPianoDeiConti) conti.get(iP);

            if (ContabUtils.isContoClienti(conto)) {
                for (int iC = 0; iC < clienti.size(); iC++) {
                    MROldClienti cliente = (MROldClienti) clienti.get(iC);

//                    importoDare = leggiSumRighePrimanoteDaChiudere(sx, anno, conto, MROldCausalePrimanota.DARE, cliente, null);
//                    importoAvere = leggiSumRighePrimanoteDaChiudere(sx, anno, conto, MROldCausalePrimanota.AVERE, cliente, null);
//
//                    if (importoDare == null) {
//                        importoDare = new Double(0);
//                    }
//                    if (importoAvere == null) {
//                        importoAvere = new Double(0);
//                    }

                    importoDare = new Double(0);
                    importoAvere = new Double(0);
                    for (int iD = 0; iD < importiDare.size(); iD++) {
                        Object[] importo = (Object[]) importiDare.get(iD);
                        if (cliente.getId().equals(importo[1])) {
                            if (importo[0] != null) {
                                importoDare = (Double) importo[0];
                            }
                            break;
                        }
                    }
                    for (int iA = 0; iA < importiAvere.size(); iA++) {
                        Object[] importo = (Object[]) importiAvere.get(iA);
                        if (cliente.getId().equals(importo[1])) {
                            if (importo[0] != null) {
                                importoAvere = (Double) importo[0];
                            }
                            break;
                        }
                    }


                    importoDifferenza = MathUtils.roundDouble(importoDare.doubleValue() - importoAvere.doubleValue());
                    if (importoDifferenza.equals(new Double(0))) {
                        continue;
                    }

                    RigaPrimanota tmpRigaNuova;
                    RigaPrimanota tmpRigaSottoconto;
                    if (importoDifferenza.doubleValue() > 0) {
                        tmpRigaNuova = new RigaPrimanota(primanota, conto, cliente, null, importoDifferenza, Boolean.FALSE, new Integer(rowCount * 2));
                        tmpRigaSottoconto = new RigaPrimanota(primanota, sottoConto, null, null, importoDifferenza, Boolean.TRUE, new Integer((rowCount * 2) + 1));
                    } else {
                        importoDifferenza = new Double(importoDifferenza.doubleValue() * -1);
                        tmpRigaNuova = new RigaPrimanota(primanota, conto, cliente, null, importoDifferenza, Boolean.TRUE, new Integer(rowCount * 2));
                        tmpRigaSottoconto = new RigaPrimanota(primanota, sottoConto, null, null, importoDifferenza, Boolean.FALSE, new Integer((rowCount * 2) + 1));
                    }
                    primanota.getRighePrimanota().add(tmpRigaNuova);
                    primanota.getRighePrimanota().add(tmpRigaSottoconto);
                    rowCount++;
                    totaleDocumento = totaleDocumento + importoDifferenza.doubleValue();
                }

            } else if (ContabUtils.isContoFornitori(conto)) {


                for (int f = 0; f < fornitori.size(); f++) {
                    MROldFornitori fornitore = (MROldFornitori) fornitori.get(f);
//                    importoDare = leggiSumRighePrimanoteDaChiudere(sx, anno, conto, MROldCausalePrimanota.DARE, null, fornitore);
//                    importoAvere = leggiSumRighePrimanoteDaChiudere(sx, anno, conto, MROldCausalePrimanota.AVERE, null, fornitore);

//                    if (importoDare == null) {
//                        importoDare = new Double(0);
//                    }
//                    if (importoAvere == null) {
//                        importoAvere = new Double(0);
//                    }

                    importoDare = new Double(0);
                    importoAvere = new Double(0);
                    for (int iD = 0; iD < importiDare.size(); iD++) {
                        Object[] importo = (Object[]) importiDare.get(iD);
                        if (fornitore.getId().equals(importo[2])) {
                            if (importo[0] != null) {
                                importoDare = (Double) importo[0];
                            }
                            break;
                        }
                    }
                    for (int iA = 0; iA < importiAvere.size(); iA++) {
                        Object[] importo = (Object[]) importiAvere.get(iA);
                        if (fornitore.getId().equals(importo[2])) {
                            if (importo[0] != null) {
                                importoAvere = (Double) importo[0];
                            }
                            break;
                        }
                    }

                    importoDifferenza = MathUtils.roundDouble(importoDare.doubleValue() - importoAvere.doubleValue());
                    if (importoDifferenza.equals(new Double(0))) {
                        continue;
                    }

                    RigaPrimanota tmpRigaNuova;
                    RigaPrimanota tmpRigaSottoconto;
                    if (importoDifferenza.doubleValue() >= 0) {
                        tmpRigaNuova = new RigaPrimanota(primanota, conto, null, fornitore, importoDifferenza, Boolean.FALSE, new Integer(rowCount * 2));
                        tmpRigaSottoconto = new RigaPrimanota(primanota, sottoConto, null, null, importoDifferenza, Boolean.TRUE, new Integer((rowCount * 2) + 1));
                    } else {
                        importoDifferenza = new Double(importoDifferenza.doubleValue() * -1);
                        tmpRigaNuova = new RigaPrimanota(primanota, conto, null, fornitore, importoDifferenza, Boolean.TRUE, new Integer(rowCount * 2));
                        tmpRigaSottoconto = new RigaPrimanota(primanota, sottoConto, null, null, importoDifferenza, Boolean.FALSE, new Integer((rowCount * 2) + 1));
                    }
                    primanota.getRighePrimanota().add(tmpRigaNuova);
                    primanota.getRighePrimanota().add(tmpRigaSottoconto);
                    rowCount++;
                    totaleDocumento = totaleDocumento + importoDifferenza.doubleValue();
                }
            } else {
//                importoDare = leggiSumRighePrimanoteDaChiudere(sx, anno, conto, MROldCausalePrimanota.DARE, null, null);
//                importoAvere = leggiSumRighePrimanoteDaChiudere(sx, anno, conto, MROldCausalePrimanota.AVERE, null, null);
//
//                if (importoDare == null) {
//                    importoDare = new Double(0);
//                }
//                if (importoAvere == null) {
//                    importoAvere = new Double(0);
//                }

                importoDare = new Double(0);
                importoAvere = new Double(0);
                for (int iD = 0; iD < importiDare.size(); iD++) {
                    Object[] importo = (Object[]) importiDare.get(iD);
                    if (conto.getId().equals(importo[3])) {
                        if (importo[0] != null) {
                            importoDare = (Double) importo[0];
                        }
                        break;
                    }
                }
                for (int iA = 0; iA < importiAvere.size(); iA++) {
                    Object[] importo = (Object[]) importiAvere.get(iA);
                    if (conto.getId().equals(importo[3])) {
                        if (importo[0] != null) {
                            importoAvere = (Double) importo[0];
                        }
                        break;
                    }
                }

                importoDifferenza = MathUtils.roundDouble(importoDare.doubleValue() - importoAvere.doubleValue());
                if (importoDifferenza.equals(new Double(0))) {
                    continue;
                }

                RigaPrimanota tmpRigaNuova;
                RigaPrimanota tmpRigaSottoconto;
                if (importoDifferenza.doubleValue() >= 0) {
                    tmpRigaNuova = new RigaPrimanota(primanota, conto, null, null, importoDifferenza, Boolean.FALSE, new Integer(rowCount * 2));
                    tmpRigaSottoconto = new RigaPrimanota(primanota, sottoConto, null, null, importoDifferenza, Boolean.TRUE, new Integer((rowCount * 2) + 1));
                } else {
                    importoDifferenza = new Double(importoDifferenza.doubleValue() * -1);
                    tmpRigaNuova = new RigaPrimanota(primanota, conto, null, null, importoDifferenza, Boolean.TRUE, new Integer(rowCount * 2));
                    tmpRigaSottoconto = new RigaPrimanota(primanota, sottoConto, null, null, importoDifferenza, Boolean.FALSE, new Integer((rowCount * 2) + 1));
                }
                primanota.getRighePrimanota().add(tmpRigaNuova);
                primanota.getRighePrimanota().add(tmpRigaSottoconto);
                rowCount++;
                totaleDocumento = totaleDocumento + importoDifferenza.doubleValue();
            }
        }

        primanota.setTotaleDocumento(new Double(totaleDocumento));

        return primanota;
    }

    public static MROldPrimanota creaPrimaNotaDiRisultatoEsercizio(Session sx, Integer anno, MROldCausalePrimanota causalePrimanota, MROldPianoDeiConti sottoContoDare, MROldPianoDeiConti sottoContoAvere, Double importo) {
        MROldPrimanota primanota = new MROldPrimanota();

//        primanota.setEditable(true);

        primanota.setUser((User) Parameters.getUser());
        primanota.setAffiliato(MROldAffiliato.getNoleggiatore());
        primanota.setCausale(causalePrimanota);
        GregorianCalendar gregorianCalendar = new GregorianCalendar(anno.intValue(), 11, 31, 00, 00, 00);
        primanota.setDataRegistrazione(gregorianCalendar.getTime());
        primanota.setAnnoCompetenza(anno);
        primanota.setNumerazione(NumerazioniUtils.getNumerazione(sx, MROldNumerazione.PRIMENOTE));

        primanota.setRighePrimanota(new ArrayList());

        RigaPrimanota tmpRigaNuovaDare = new RigaPrimanota();
        tmpRigaNuovaDare.setPrimanota(primanota);
        tmpRigaNuovaDare.setNumeroRiga(new Integer(0));
        tmpRigaNuovaDare.setConto(sottoContoDare);
        tmpRigaNuovaDare.setImporto(MathUtils.absDouble(importo));
        tmpRigaNuovaDare.setSegno(causalePrimanota.DARE);
        primanota.getRighePrimanota().add(tmpRigaNuovaDare);

        RigaPrimanota tmpRigaNuovaAvere = new RigaPrimanota();
        tmpRigaNuovaAvere.setPrimanota(primanota);
        tmpRigaNuovaAvere.setNumeroRiga(new Integer(1));
        tmpRigaNuovaAvere.setConto(sottoContoAvere);
        tmpRigaNuovaAvere.setImporto(MathUtils.absDouble(importo));
        tmpRigaNuovaAvere.setSegno(MROldCausalePrimanota.AVERE);
        primanota.getRighePrimanota().add(tmpRigaNuovaAvere);

        primanota.setTotaleDocumento(MathUtils.absDouble(importo));

        return primanota;
    }
}
