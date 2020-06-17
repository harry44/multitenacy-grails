/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.aessepi.myrentcs.utils;
import it.myrent.ee.db.*;


import java.util.Date;


/**
 *
 * @author jamess
 */
public class Rent2RentAdapter {

    private Rent2Rent contratto;

    public Rent2RentAdapter(Rent2Rent contratto) {
        setContratto(contratto);
    }

    public void setContratto(Rent2Rent contratto) {
        this.contratto = contratto;
    }

    public Rent2Rent getContratto() {
        return contratto;
    }

    public Integer getId() {
        return getContratto().getId();

    }
    public MROldClienti getCliente() {
        return getContratto().getCustomer();
    }

    public Date getRaStartDate() {
        return getContratto().getRaStartDate();
    }
    public Date getRaEndDate() {
        return getContratto().getRaEndDate();
    }
    public Date getContractDate() {
        return getContratto().getContractDate();
    }

    public Integer getNumero() {
        return getContratto().getNumber();
    }

    public String getNote() {

        return getContratto().getPublicNote();
    }

    public String getPrefix() {
        return getContratto().getPrefix();
    }

    public Double getMonthlyFee() {
        return getContratto().getMonthlyFee();
    }
    public MROldSede getSede() {
        return getContratto().getLocation();
    }

    public Double getAmountFirstInstallment(){
        return getContratto().getAmountFirstInstallment();
    }

    public Double getAmountLastInstallment(){
        return getContratto().getAmountLastInstallment();
    }

    public MROldParcoVeicoli getVeicolo(){
        return getContratto().getVehicle();
    }

    public Double getDamageExcess(){
        return getContratto().getDamageExcess();
    }


    public String getNsaExportCode() {
        return getContratto().getNsaExportCode();
    }

    public String getExportContractNumber() {
        return getContratto().getExportContractNumber();
    }

    public Date getDeliveredDate() {
        return getContratto().getDeliveredDate();
    }

    public Boolean getIsVehicleReturned() {
        return getContratto().getIsVehicleReturned();
    }

    public Double getExtraKmAmount() {
        return getContratto().getExtraKmAmount();
    }

    public Integer getPeriod() {
        return getContratto().getPeriod();
    }

    public Integer getTyres() {
        return getContratto().getTyres();
    }

    public Boolean getIsMaintenanceIncluded() {
        return getContratto().getIsRoadAssistanceIncluded();
    }

    public Boolean getIsRoadAssistanceIncluded() {
        return getContratto().getIsRoadAssistanceIncluded();
    }

    public Boolean getIsBilledInAdvance() {
        return getContratto().getIsBilledInAdvance();
    }

    public MROldFornitori getAgent() {
        return getContratto().getAgent();
    }

    public Integer getKmIncluded(){
        return getContratto().getKmIncluded();
    }

    public Double getTheftExcess(){
        return getContratto().getTheftExcess();
    }

    public Date getReturnedDate(){
        return getContratto().getReturnedDate();
    }
    public Date getLastBilledDate(){
        return getContratto().getLastBilledDate();
    }
    public Date getLastBillingDate(){
        return getContratto().getLastBillingDate();
    }

    public Double getDurationInMonth(){
        return getContratto().getDurationInMonth();
    }

    public MROldConducenti getConducente1() {
        return getContratto().getConducente1();
    }

    public MROldConducenti getConducente2() {
        return getContratto().getConducente2();
    }

    public MROldConducenti getConducente3() {
        return getContratto().getConducente3();
    }

    public String getPublicNote() {

        return getContratto().getPublicNote();
    }

    public MROldPagamento getPagamento() {
        return getContratto().getPagamento();
    }

    public Double getKasko() {
        return getContratto().getKasko();
    }

    public MROldMovimentoAuto getStartMovement() {
        return getContratto().getStartMovement();
    }

    public MROldMovimentoAuto getEndMovement() {
        return getContratto().getEndMovement();
    }


}
