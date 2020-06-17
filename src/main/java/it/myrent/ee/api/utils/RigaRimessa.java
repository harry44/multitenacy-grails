/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.api.utils;

import it.myrent.ee.db.Garanzia;
import it.myrent.ee.db.MROldPagamento;
import it.myrent.ee.db.MROldPianoDeiConti;

/**
 *
 * @author giacomo
 */
public class RigaRimessa {
    private MROldPagamento pagamento;
    private Garanzia garanzia;
    private MROldPianoDeiConti conto;
    private Double saldoAttuale;
    private Double confermaSaldo;
    private Double importoRimessa;
    private Boolean rimesso;
    private String descrizioneAggiuntiva;
    private String note;

    public RigaRimessa(
            MROldPagamento pagamento,
            Garanzia garanzia,
            MROldPianoDeiConti conto,
            Double saldoAttuale,
            Double confermaSaldo,
            Double importoRimessa,
            Boolean rimesso,
            String descrizioneAggiuntiva,
            String note) {
        setPagamento(pagamento);
        setGaranzia(garanzia);
        setConto(conto);
        setSaldoAttuale(saldoAttuale);
        setConfermaSaldo(confermaSaldo);
        setImportoRimessa(importoRimessa);
        setRimesso(rimesso);
        setDescrizioneAggiuntiva(descrizioneAggiuntiva);
        setNote(note);
    }

    public MROldPagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(MROldPagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Garanzia getGaranzia() {
        return garanzia;
    }

    public void setGaranzia(Garanzia garanzia) {
        this.garanzia = garanzia;
    }

    public MROldPianoDeiConti getConto() {
        return conto;
    }

    public void setConto(MROldPianoDeiConti conto) {
        this.conto = conto;
    }

    public Double getSaldoAttuale() {
        return saldoAttuale;
    }

    public void setSaldoAttuale(Double saldoAttuale) {
        this.saldoAttuale = saldoAttuale;
    }

    public Double getConfermaSaldo() {
        return confermaSaldo;
    }

    public void setConfermaSaldo(Double confermaSaldo) {
        this.confermaSaldo = confermaSaldo;
    }

    public Double getImportoRimessa() {
        return importoRimessa;
    }

    public void setImportoRimessa(Double importoRimessa) {
        this.importoRimessa = importoRimessa;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setRimesso(Boolean rimesso) {
        this.rimesso = rimesso;
    }

    public Boolean getRimesso() {
        return rimesso;
    }

    public String getDescrizioneAggiuntiva() {
        return descrizioneAggiuntiva;
    }

    public void setDescrizioneAggiuntiva(String descrizioneAggiuntiva) {
        this.descrizioneAggiuntiva = descrizioneAggiuntiva;
    }
}
