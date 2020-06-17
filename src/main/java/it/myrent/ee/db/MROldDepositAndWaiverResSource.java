/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author Madhvendra
 */
public class MROldDepositAndWaiverResSource implements it.aessepi.utils.db.PersistentInstance{
    
private Integer id;
private Boolean isEnable;
private MROldFonteCommissione reservationSourceId;
private Boolean isDepositRequired;
private Double cashDepositAmount;
private Double creditCardDepositAmount;
private Boolean isCreditCardRequired;
private Double defaultTheftWaiverAmount;
private Double defaultDamageWaiverAmount;
private Double theftPercChargedToUser;
private Double damagePercChargedToUser;
private Double defaultCarWashServAmount;
private Double defaultRoadAssistanceAmount;
private Double defaultFireWaiverAmount;

    private Double carWashPercChargedToUser;
    private Double roadAssistPercChargedToUser;
    private Double fireWaiverPercChargedToUser;
    private Double fuelServicePercChargedToUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(Boolean isEnable) {
        this.isEnable = isEnable;
    }

    public MROldFonteCommissione getReservationSourceId() {
        return reservationSourceId;
    }

    public void setReservationSourceId(MROldFonteCommissione reservationSourceId) {
        this.reservationSourceId = reservationSourceId;
    }

    public Boolean getIsDepositRequired() {
        return isDepositRequired;
    }

    public void setIsDepositRequired(Boolean isDepositRequired) {
        this.isDepositRequired = isDepositRequired;
    }

    public Double getCashDepositAmount() {
        return cashDepositAmount;
    }

    public void setCashDepositAmount(Double cashDepositAmount) {
        this.cashDepositAmount = cashDepositAmount;
    }

    public Double getCreditCardDepositAmount() {
        return creditCardDepositAmount;
    }

    public void setCreditCardDepositAmount(Double creditCardDepositAmount) {
        this.creditCardDepositAmount = creditCardDepositAmount;
    }

    public Boolean getIsCreditCardRequired() {
        return isCreditCardRequired;
    }

    public void setIsCreditCardRequired(Boolean isCreditCardRequired) {
        this.isCreditCardRequired = isCreditCardRequired;
    }

    public Double getDefaultTheftWaiverAmount() {
        return defaultTheftWaiverAmount;
    }

    public void setDefaultTheftWaiverAmount(Double defaultTheftWaiverAmount) {
        this.defaultTheftWaiverAmount = defaultTheftWaiverAmount;
    }

    public Double getDefaultDamageWaiverAmount() {
        return defaultDamageWaiverAmount;
    }

    public void setDefaultDamageWaiverAmount(Double defaultDamageWaiverAmount) {
        this.defaultDamageWaiverAmount = defaultDamageWaiverAmount;
    }

    public Double getTheftPercChargedToUser() {
        return theftPercChargedToUser;
    }

    public void setTheftPercChargedToUser(Double theftPercChargedToUser) {
        this.theftPercChargedToUser = theftPercChargedToUser;
    }

    public Double getDamagePercChargedToUser() {
        return damagePercChargedToUser;
    }

    public void setDamagePercChargedToUser(Double damagePercChargedToUser) {
        this.damagePercChargedToUser = damagePercChargedToUser;
    }

    public Double getDefaultCarWashServAmount() {
        return defaultCarWashServAmount;
    }

    public void setDefaultCarWashServAmount(Double defaultCarWashServAmount) {
        this.defaultCarWashServAmount = defaultCarWashServAmount;
    }

    public Double getDefaultRoadAssistanceAmount() {
        return defaultRoadAssistanceAmount;
    }

    public void setDefaultRoadAssistanceAmount(Double defaultRoadAssistanceAmount) {
        this.defaultRoadAssistanceAmount = defaultRoadAssistanceAmount;
    }

    public Double getDefaultFireWaiverAmount() {
        return defaultFireWaiverAmount;
    }

    public void setDefaultFireWaiverAmount(Double defaultFireWaiverAmount) {
        this.defaultFireWaiverAmount = defaultFireWaiverAmount;
    }


//  @Override
//    public String toString() {
//        return description != null ? description : new String();
//    }

//    @Override
//    public int compareTo(MROldDepositAndWaiverResSource o) {
//        if (o == null) {
//            return 1;
//        } else {
//            return new CompareToBuilder().//append(getDescription(), o.getDescription()).
//                    append(getId(), o.getId()).
//                    toComparison();
//        }
//    }


    public Double getCarWashPercChargedToUser() {
        return carWashPercChargedToUser;
    }

    public void setCarWashPercChargedToUser(Double carWashPercChargedToUser) {
        this.carWashPercChargedToUser = carWashPercChargedToUser;
    }

    public Double getRoadAssistPercChargedToUser() {
        return roadAssistPercChargedToUser;
    }

    public void setRoadAssistPercChargedToUser(Double roadAssistPercChargedToUser) {
        this.roadAssistPercChargedToUser = roadAssistPercChargedToUser;
    }

    public Double getFireWaiverPercChargedToUser() {
        return fireWaiverPercChargedToUser;
    }

    public void setFireWaiverPercChargedToUser(Double fireWaiverPercChargedToUser) {
        this.fireWaiverPercChargedToUser = fireWaiverPercChargedToUser;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MROldDepositAndWaiverResSource)) {
            return false;
        }
        MROldDepositAndWaiverResSource castOther = (MROldDepositAndWaiverResSource) obj;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }


    /**
     * @return the fuelServicePercChargedToUser
     */
    public Double getFuelServicePercChargedToUser() {
        return fuelServicePercChargedToUser;
    }

    /**
     * @param fuelServicePercChargedToUser the fuelServicePercChargedToUser to set
     */
    public void setFuelServicePercChargedToUser(Double fuelServicePercChargedToUser) {
        this.fuelServicePercChargedToUser = fuelServicePercChargedToUser;
    }
}
