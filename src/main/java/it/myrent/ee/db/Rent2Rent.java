/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * @author Madhvendra
 */
public class Rent2Rent implements PersistentInstance {
    private Integer id;
    private MROldSede location;
    private MROldClienti customer;
    private String nsaExportCode;
    private String exportContractNumber;
    private MROldParcoVeicoli vehicle;
    private Double monthlyFee;
    private Date raStartDate;
    private Date raEndDate;
    private Date deliveredDate;
    private Boolean isVehicleReturned;
    private Date returnedDate;
    private Date lastBilledDate;
    private Date lastBillingDate;
    private Double durationInMonth;
    private Integer kmIncluded;
    private Double extraKmAmount;
    private Double damageExcess;
    private Double theftExcess;
    private Integer tyres;
    private Boolean isMaintenanceIncluded;
    private Boolean isRoadAssistanceIncluded;

    private String prefix;
    private Integer number;
    private Double amountFirstInstallment;
    private Double amountLastInstallment;
    private String note;
    private Boolean isBilledInAdvance;
    private MROldFornitori agent;
    private Integer period;
    private Date contractDate;
    private MROldPagamento pagamento;
    private MROldConducenti conducente1;
    private MROldConducenti conducente2;
    private MROldConducenti conducente3;
    private String publicNote;
    private Double kasko;
    private MROldMovimentoAuto startMovement;
    private MROldMovimentoAuto endMovement;
    private Boolean is30DaysInvoice;



    private Boolean signedCheckOut;




    private Boolean signedCheckIn;

    public Boolean getVehicleReturned() {
        return isVehicleReturned;
    }

    public void setVehicleReturned(Boolean vehicleReturned) {
        isVehicleReturned = vehicleReturned;
    }

    public Boolean getMaintenanceIncluded() {
        return isMaintenanceIncluded;
    }

    public void setMaintenanceIncluded(Boolean maintenanceIncluded) {
        isMaintenanceIncluded = maintenanceIncluded;
    }

    public Boolean getRoadAssistanceIncluded() {
        return isRoadAssistanceIncluded;
    }

    public void setRoadAssistanceIncluded(Boolean roadAssistanceIncluded) {
        isRoadAssistanceIncluded = roadAssistanceIncluded;
    }

    public Boolean getBilledInAdvance() {
        return isBilledInAdvance;
    }

    public void setBilledInAdvance(Boolean billedInAdvance) {
        isBilledInAdvance = billedInAdvance;
    }

    public MROldPagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(MROldPagamento pagamento) {
        this.pagamento = pagamento;
    }

    public MROldConducenti getConducente1() {
        return conducente1;
    }

    public void setConducente1(MROldConducenti conducente1) {
        this.conducente1 = conducente1;
    }

    public MROldConducenti getConducente2() {
        return conducente2;
    }

    public void setConducente2(MROldConducenti conducente2) {
        this.conducente2 = conducente2;
    }

    public MROldConducenti getConducente3() {
        return conducente3;
    }

    public void setConducente3(MROldConducenti conducente3) {
        this.conducente3 = conducente3;
    }

    public String getPublicNote() {
        return publicNote;
    }

    public void setPublicNote(String publicNote) {
        this.publicNote = publicNote;
    }

    public Double getKasko() {
        return kasko;
    }

    public void setKasko(Double kasko) {
        this.kasko = kasko;
    }

    public MROldMovimentoAuto getStartMovement() {
        return startMovement;
    }

    public void setStartMovement(MROldMovimentoAuto startMovement) {
        this.startMovement = startMovement;
    }

    public MROldMovimentoAuto getEndMovement() {
        return endMovement;
    }

    public void setEndMovement(MROldMovimentoAuto endMovement) {
        this.endMovement = endMovement;
    }


    public Boolean getIs30DaysInvoice() {
        return is30DaysInvoice;
    }

    public void setIs30DaysInvoice(Boolean is30DaysInvoice) {
        this.is30DaysInvoice = is30DaysInvoice;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldSede getLocation() {
        return location;
    }

    public void setLocation(MROldSede location) {
        this.location = location;
    }

    public MROldClienti getCustomer() {
        return customer;
    }

    public void setCustomer(MROldClienti customer) {
        this.customer = customer;
    }

    public String getNsaExportCode() {
        return nsaExportCode;
    }

    public void setNsaExportCode(String nsaExportCode) {
        this.nsaExportCode = nsaExportCode;
    }

    public String getExportContractNumber() {
        return exportContractNumber;
    }

    public void setExportContractNumber(String exportContractNumber) {
        this.exportContractNumber = exportContractNumber;
    }

    public MROldParcoVeicoli getVehicle() {
        return vehicle;
    }

    public void setVehicle(MROldParcoVeicoli vehicle) {
        this.vehicle = vehicle;
    }

    public Double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(Double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public Date getRaStartDate() {
        return raStartDate;
    }

    public void setRaStartDate(Date raStartDate) {
        this.raStartDate = raStartDate;
    }

    public Date getRaEndDate() {
        return raEndDate;
    }

    public void setRaEndDate(Date raEndDate) {
        this.raEndDate = raEndDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public Boolean getIsVehicleReturned() {
        return isVehicleReturned;
    }

    public void setIsVehicleReturned(Boolean isVehicleReturned) {
        this.isVehicleReturned = isVehicleReturned;
    }

    public Date getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(Date returnedDate) {
        this.returnedDate = returnedDate;
    }

    public Date getLastBilledDate() {
        return lastBilledDate;
    }

    public void setLastBilledDate(Date lastBilledDate) {
        this.lastBilledDate = lastBilledDate;
    }

    public Date getLastBillingDate() {
        return lastBillingDate;
    }

    public void setLastBillingDate(Date lastBillingDate) {
        this.lastBillingDate = lastBillingDate;
    }

    public Double getDurationInMonth() {
        return durationInMonth;
    }

    public void setDurationInMonth(Double durationInMonth) {
        this.durationInMonth = durationInMonth;
    }

    public Integer getKmIncluded() {
        return kmIncluded;
    }

    public void setKmIncluded(Integer kmIncluded) {
        this.kmIncluded = kmIncluded;
    }

    public Double getExtraKmAmount() {
        return extraKmAmount;
    }

    public void setExtraKmAmount(Double extraKmAmount) {
        this.extraKmAmount = extraKmAmount;
    }

    public Double getDamageExcess() {
        return damageExcess;
    }

    public void setDamageExcess(Double damageExcess) {
        this.damageExcess = damageExcess;
    }

    public Double getTheftExcess() {
        return theftExcess;
    }

    public void setTheftExcess(Double theftExcess) {
        this.theftExcess = theftExcess;
    }

    public Integer getTyres() {
        return tyres;
    }

    public void setTyres(Integer tyres) {
        this.tyres = tyres;
    }

    public Boolean getIsMaintenanceIncluded() {
        return isMaintenanceIncluded;
    }

    public void setIsMaintenanceIncluded(Boolean isMaintenanceIncluded) {
        this.isMaintenanceIncluded = isMaintenanceIncluded;
    }

    public Boolean getIsRoadAssistanceIncluded() {
        return isRoadAssistanceIncluded;
    }

    public void setIsRoadAssistanceIncluded(Boolean isRoadAssistanceIncluded) {
        this.isRoadAssistanceIncluded = isRoadAssistanceIncluded;
    }


    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getAmountFirstInstallment() {
        return amountFirstInstallment;
    }

    public void setAmountFirstInstallment(Double amountFirstInstallment) {
        this.amountFirstInstallment = amountFirstInstallment;
    }

    public Double getAmountLastInstallment() {
        return amountLastInstallment;
    }

    public void setAmountLastInstallment(Double amountLastInstallment) {
        this.amountLastInstallment = amountLastInstallment;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getIsBilledInAdvance() {
        return isBilledInAdvance;
    }

    public void setIsBilledInAdvance(Boolean isBilledInAdvance) {
        this.isBilledInAdvance = isBilledInAdvance;
    }

    public MROldFornitori getAgent() {
        return agent;
    }

    public void setAgent(MROldFornitori agent) {
        this.agent = agent;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Boolean getSignedCheckIn() {
        return signedCheckIn;
    }

    public void setSignedCheckIn(Boolean signedCheckIn) {
        this.signedCheckIn = signedCheckIn;
    }

    public Boolean getSignedCheckOut() {
        return signedCheckOut;
    }

    public void setSignedCheckOut(Boolean signedCheckOut) {
        this.signedCheckOut = signedCheckOut;
    }
}
