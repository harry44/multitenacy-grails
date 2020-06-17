/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dogmasystems.myrent.db;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author Madhvendra
 */
public class DepositAndWaiverResSource implements Serializable{

   // Integer id;
    Boolean isEnable;
    Boolean isDepositRequired;
    Double cashDepositAmount;
    Double creditCardDepositAmount;
    Boolean isCreditCardRequired;
    Double defaultTheftWaiverAmount;
    Double defaultDamageWaiverAmount;
    Double theftPercChargedToUser;
    Double damagePercChargedToUser;
    Double defaultCarWashServAmount;
    Double defaultRoadAssistanceAmount;
    Double defaultFireWaiverAmount;

    Double carWashPercChargedToUser;
    Double roadAssistPercChargedToUser;
    Double fireWaiverPercChargedToUser;
    MRReservationSource reservationSourceId

    static constraints = {
        isEnable nullable: true
        isDepositRequired nullable: true
        cashDepositAmount nullable: true
        creditCardDepositAmount nullable: true
        isCreditCardRequired nullable: true
        defaultTheftWaiverAmount nullable: true
        defaultDamageWaiverAmount nullable: true
        theftPercChargedToUser nullable: true
        damagePercChargedToUser nullable: true
        defaultCarWashServAmount nullable: true
        defaultRoadAssistanceAmount nullable: true
        defaultFireWaiverAmount nullable: true
        carWashPercChargedToUser nullable: true
        roadAssistPercChargedToUser nullable: true
        fireWaiverPercChargedToUser nullable: true
    }
    static mapping = {
        cache true
        table name: "deposit_and_waiver_res_source"
        id column: "id", sqlType: "int4" , generator: 'foreign', params: [property: 'reservationSourceId']
        reservationSourceId column: 'id', insertable: false, updateable: false , sqlType: "int4"
        isEnable column: "isEnable"
        isDepositRequired column: "isDepositRequired"
        cashDepositAmount column: "cashDepositAmount"
        creditCardDepositAmount column: "creditCardDepositAmount"
        isCreditCardRequired column: "isCreditCardRequired"
        defaultTheftWaiverAmount column: "defaultTheftWaiverAmount"
        defaultDamageWaiverAmount column: "defaultDamageWaiverAmount"
        theftPercChargedToUser column: "theftPercChargedToUser"
        damagePercChargedToUser column: "damagePercChargedToUser"
        defaultCarWashServAmount column: "defaultCarWashServAmount"
        defaultRoadAssistanceAmount column: "defaultRoadAssistanceAmount"
        defaultFireWaiverAmount column: "defaultFireWaiverAmount"
        carWashPercChargedToUser column: "car_wash_perc_charged"
        roadAssistPercChargedToUser column: "road_assist_perc_charged"
        fireWaiverPercChargedToUser column: "fire_waiver_perc_charged"
        version false
    }

}
