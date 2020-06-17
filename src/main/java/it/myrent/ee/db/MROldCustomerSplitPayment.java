package it.myrent.ee.db;

import java.util.List;

/**
 * Created by Shivangani on 1/23/2018.
 */
public class MROldCustomerSplitPayment {

    private Integer id;
    private MROldContrattoNoleggio contratto;

    private MROldPrenotazione prenotazione;
    private Integer ordineCliente;
    private MROldClienti cliente;
    private List<MROldRigaDocumentoFiscaleSplitPayment> righeSplitPayment;

    public MROldCustomerSplitPayment(Integer id, MROldContrattoNoleggio contratto, Integer ordineCliente, MROldClienti cliente, List<MROldRigaDocumentoFiscaleSplitPayment> righeSplitPayment) {
        this.id = id;
        this.contratto = contratto;
        this.ordineCliente = ordineCliente;
        this.cliente = cliente;
        this.righeSplitPayment = righeSplitPayment;
    }

    public MROldCustomerSplitPayment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrdineCliente() {
        return ordineCliente;
    }

    public void setOrdineCliente(Integer ordineCliente) {
        this.ordineCliente = ordineCliente;
    }

    public MROldContrattoNoleggio getContratto() {
        return contratto;
    }

    public void setContratto(MROldContrattoNoleggio contratto) {
        this.contratto = contratto;
    }

    public MROldClienti getCliente() {
        return cliente;
    }

    public void setCliente(MROldClienti cliente) {
        this.cliente = cliente;
    }

    public List<MROldRigaDocumentoFiscaleSplitPayment> getRigheSplitPayment() {
        return righeSplitPayment;
    }

    public void setRigheSplitPayment(List<MROldRigaDocumentoFiscaleSplitPayment> righeSplitPayment) {
        this.righeSplitPayment = righeSplitPayment;
    }

    public MROldPrenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(MROldPrenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }
}

