package com.dogmasystems.myrent.db

class MRVehicleDetail implements Serializable {

    MRVehicleStatus vehicleStatusId
    Boolean vanHasSideBoard
    Double vanMaxLengthExtVehicle
    Double vanMaxWidthExtVehicle
    Double vanMaxHeighExtVehicle
    Double vanMaxLengthIntLoadComp
    Double passo
    Double vanMaxWidthIntLoadComp
    Double vanMaxWidthBetweenWheelArches
    Double vanMaxHeightLoadThreshold
    Double vanCubage
    Double vanMaxHLoadCompRearAccSpace
    Double vanMaxHLoadCompSideAccSpace
    Double vanDistFromLateralDoorToHead
    Double vanRegistrationTyreMeasures
    Double vanBuildedTyreMeasures
    MREquipment equipIdEquipment
    String equipMaker
    String equipModel
    String equipVersion
    String equipSerialNumber
    MRBookingOnlineStatus idBookingOnlineStatus
    MRPurchaseType finPurchaseTypeId
    String finOwner
    String finUser
    String finCostCenterCode
    Double finMaxTotalKm
    Double finUpperLimitKm
    Double finMaxUpperLimitKm
    Double finChargedForExtraKm
    Double finLowerLimitKm
    Double finMinLowerLimitKm
    Double finRefundForKm
    Double finMaxKmRefund
    Double finFinancialmonthlyFee
    Double finServicesMonthlyFee
    Double finOtherMonthlyFee
    Double finTotalMonthlyFee
    String finFinancialDescription
    Boolean maintIsstdMaintenance
    Boolean maintIsextraMaintenance
    Boolean maintIsTyres
    Boolean maintIsGlasses
    Boolean maintIsRoadAssistance
    String maintIncMaintenanceDescr
    Double deductibleDamage
    Double deductibleKaskoDamage
    Double deductibleTheft
    Double deductibleDamageEndAgreement
    Double deductibleFineFee
    MRInsuranceCompany insuranceCompanyId
    Integer vehicleEntryPurchOrderNum
    Date vehicleEntryOrderDate
    Integer vehicleEntrySupplierOrderNum
    Date vehicleEntrySupplierOrderDate
    String vehicleEntryDeliveryNumber
    Date vehicleEntryDeliveryDate
    MRLocation vehicleEntryAssignedLocationId
    MRSalesStatus salesStatus
    Double salesNewPurchVehicleNewTotVal
    Double salesEurotaxBluNominalValue
    Date salesEurotaxBluNominValDate
    Double salesEurotaxYellowNominalValue
    Date salesEurotaxYellowNominValDate
    Double salesNewMountingPurchaseValue
    Double salesRemainingMountingValue
    Date salesRemMountValueDate
    Double salesRemMountValueTotalBlu
    Date salesRemMountValTotBluDate
    Double salesRemMounValueTotYellow
    Date salesRemMounValTotYellowDate
    Double salesEstimatedDamageValue
    Date salesEstimateDamageDate
    Double salesMinPrice
    Double salesMinAdvisedPrice
    Double salesMaxPrice
    Double salesMaxAdvisedPrice
    Double salesPricePubliOnline
    Date salesAgreementDate
    String salesAgreementNum
    String salesNotes
    Date salesInvoiceDate
    Double salesInvoiceNum
    Double salesInvoiceTaxable
    Double salesInvoiceTotalAmount
    MRBusinessPartner salesCustomerId
    Date salesChangeoverOfPropertyDate
    Date salesMinutesVehicleDeliveryDate
    Double financialAdvance
    Double financialFinalRansom
    Double financialBuyBackValue
    Integer finMonthlyInstallmentNumb
    Date finFirstInstallmentExpectDate
    Date finFirstInstallmentEffectiDate
    Date finLastInstallmentDate
    MRVehicle parco
    MRBusinessPartner insuranceCompany
    MRVehicleTransmission vehicleTransmission
    MRTrackingDevice trackingDevice
    Double totalInsAmount
    Double keskoInsAmount
    Double tax2Cost
    Double tax1Cost
    Double fireInsAmount
    Double theftInsAmount
    Double residentVal
    Double thirdPartyInsAmount
    String infoCarCode
    MRVehicleTraction traction
    MRVehicleChassis chassis

    static mapping = {
        cache true
        table name: "vehicle_detail"//, schema: "public"
        id column: "id", sqlType: "int4", generator: 'foreign', params: [property: 'parco']
        parco column: 'id', insertable: false, updateable: false, sqlType: "int4"
        vanHasSideBoard column: "van_has_side_board"
        vanMaxLengthExtVehicle column: "van_max_length_ext_vehicle"
        vanMaxWidthExtVehicle column: "van_max_width_ext_vehicle"
        vanMaxHeighExtVehicle column: "van_max_heigh_ext_vehicle"
        vanMaxLengthIntLoadComp column: "van_max_length_int_load_comp"
        passo column: "passo"
        vanMaxWidthIntLoadComp column: "van_max_width_int_load_comp"
        vanMaxWidthBetweenWheelArches column: "van_max_width_between_wheel_arches"
        vanMaxHeightLoadThreshold column: "van_max_height_load_threshold"
        vanCubage column: "van_cubage"
        vanMaxHLoadCompRearAccSpace column: "van_max_h_load_comp_rear_acc_space"
        vanDistFromLateralDoorToHead column: "van_dist_from_lateral_door_to_head"
        vanMaxHLoadCompSideAccSpace column: "van_max_h_load_comp_side_acc_space"
        vanRegistrationTyreMeasures column: "van_registration_tyre_measures"
        vanBuildedTyreMeasures column: "van_builded_tyre_measures"
        equipIdEquipment column: "equip_id_equipment", sqlType: "int4"
        equipMaker column: "equip_maker"
        equipModel column: "equip_model"
        equipVersion column: "equip_version"
        equipSerialNumber column: "equip_serial_number"
        idBookingOnlineStatus column: "id_booking_online_status", sqlType: "int4"
        finPurchaseTypeId column: "fin_purchase_type_id", sqlType: "int4"
        finOwner column: "fin_owner"
        finUser column: "fin_user"
        finCostCenterCode column: "fin_cost_center_code"
        finMaxTotalKm column: "fin_max_total_km"
        finUpperLimitKm column: "fin_upper_limit_km"
        finMaxUpperLimitKm column: "fin_max_upper_limit_km"
        finChargedForExtraKm column: "fin_charged_for_extra_km"
        finLowerLimitKm column: "fin_lower_limit_km"
        finMinLowerLimitKm column: "fin_min_lower_limit_km"
        finRefundForKm column: "fin_refund_for_km"
        finMaxKmRefund column: "fin_max_km_refund"
        finFinancialmonthlyFee column: "fin_financialmonthly_fee"
        finServicesMonthlyFee column: "fin_services_monthly_fee"
        finOtherMonthlyFee column: "fin_other_monthly_fee"
        finTotalMonthlyFee column: "fin_total_monthly_fee"
        finFinancialDescription column: "fin_financial_description"
        maintIsstdMaintenance column: "maint_is_std_maintenance"
        maintIsextraMaintenance column: "maint_is_extra_maintenance"
        maintIsTyres column: "maint_is_tyres"
        maintIsGlasses column: "maint_is_glasses"
        maintIsRoadAssistance column: "maint_is_road_assistance"
        maintIncMaintenanceDescr column: "maint_inc_maintenance_descr"
        deductibleDamage column: "deductible_damage"
        deductibleKaskoDamage column: "deductible_kasko_damage"
        deductibleTheft column: "deductible_theft"
        deductibleDamageEndAgreement column: "deduct_dam_end_agre"
        deductibleFineFee column: "deductible_fine_fee"
        insuranceCompanyId column: "insurance_company_id", sqlType: "int4"
        vehicleEntryPurchOrderNum column: "vehicle_entry_purch_order_num", sqlType: "int4"
        vehicleEntrySupplierOrderNum column: "vehicle_entry_supplier_order_num", sqlType: "int4"
        vehicleEntryOrderDate column: "vehicle_entry_order_date", sqlType: "date"
        vehicleEntrySupplierOrderDate column: "vehicle_entry_supplier_order_date", sqlType: "date"
        vehicleEntryDeliveryNumber column: "vehicle_entry_delivery_number"
        vehicleEntryDeliveryDate column: "vehicle_entry_delivery_date", sqlType: "date"
        vehicleEntryAssignedLocationId column: "vehicle_entry_assigned_location_id", sqlType: "int4"
        salesStatus column: "sales_status", sqlType: "int4"
        salesNewPurchVehicleNewTotVal column: "sales_new_purch_vehicle_new_tot_val"
        salesEurotaxBluNominalValue column: "sales_eurotax_blu_nominal_value"
        salesEurotaxBluNominValDate column: "sales_eurotax_blu_nomin_val_date", sqlType: "date"
        salesEurotaxYellowNominalValue column: "sales_eurotax_yellow_nominal_value"
        salesEurotaxYellowNominValDate column: "sales_eurotax_yellow_nomin_val_date", sqlType: "date"
        salesNewMountingPurchaseValue column: "sales_new_mounting_purchase_value"
        salesRemainingMountingValue column: "sales_remaining_mounting_value"
        salesRemMountValueDate column: "sales_rem_mount_value_date", sqlType: "date"
        salesRemMountValueTotalBlu column: "sales_rem_mount_value_total_blu"
        salesRemMountValTotBluDate column: "sales_rem_mount_val_tot_blu_date", sqlType: "date"
        salesRemMounValueTotYellow column: "sales_rem_moun_value_tot_yellow"
        salesRemMounValTotYellowDate column: "sales_rem_moun_val_tot_yellow_date", sqlType: "date"
        salesEstimatedDamageValue column: "sales_estimated_damage_value"
        salesEstimateDamageDate column: "sales_estimate_damage_date", sqlType: "date"
        salesMinPrice column: "sales_min_price"
        salesMinAdvisedPrice column: "sales_min_advised_price"
        salesMaxPrice column: "sales_max_price"
        salesMaxAdvisedPrice column: "sales_max_advised_price"
        salesPricePubliOnline column: "sales_price_publi_online"
        salesAgreementDate column: "sales_agreement_date", sqlType: "date"
        salesAgreementNum column: "sales_agreement_num"
        salesNotes column: "sales_notes"
        salesInvoiceDate column: "sales_invoice_date", sqlType: "date"
        salesInvoiceNum column: "sales_invoice_num"
        salesInvoiceTaxable column: "sales_invoice_taxable"
        salesInvoiceTotalAmount column: "sales_invoice_total_amount"
        salesCustomerId column: "sales_customer_id", sqlType: "int4"
        salesChangeoverOfPropertyDate column: "sales_changeover_of_property_date", sqlType: "date"
        salesMinutesVehicleDeliveryDate column: "sales_minutes_vehicle_delivery_date", sqlType: "date"
        financialAdvance column: "financial_Advance"
        financialFinalRansom column: "financial_Final_Ransom"
        financialBuyBackValue column: "financial_Buy_Back_Value"
        finMonthlyInstallmentNumb column: "fin_Monthly_Installment_Numb", sqlType: "int4"
        finFirstInstallmentExpectDate column: "fin_First_Installment_Expect_Date", sqlType: "date"
        finFirstInstallmentEffectiDate column: "fin_First_Installment_Effecti_Date", sqlType: "date"
        finLastInstallmentDate column: "fin_Last_Installment_Date", sqlType: "date"
        vehicleStatusId column: "id_vehiclestatus", sqlType: "int4"
        insuranceCompany column: "insurance_company", sqlType: "int4"
        vehicleTransmission column: "id_transmission", sqlType: "int4"
        trackingDevice column: "id_tracking_device", sqlType: "int4"
        totalInsAmount column: "total_ins_amount"
        keskoInsAmount column: "kesko_ins_amount"
        tax2Cost column: "tax_cost2"
        tax1Cost column: "tax_cost1"
        fireInsAmount column: "fire_ins_amount"
        theftInsAmount column: "theft_ins_amount"
        residentVal column: "resident_val"
        thirdPartyInsAmount column: "third_party_ins_amount"
        infoCarCode column: "info_car_code"
        traction column: "id_traction", sqlType: "int4"
        chassis column: "id_chassis", sqlType: "int4"
        version false
    }

    static constraints = {
        vehicleTransmission nullable: true
        trackingDevice nullable: true
        vehicleStatusId nullable: true
        vanHasSideBoard nullable: true
        vanMaxLengthExtVehicle nullable: true
        vanMaxWidthExtVehicle nullable: true
        vanMaxHeighExtVehicle nullable: true
        vanMaxLengthIntLoadComp nullable: true
        passo nullable: true
        vanMaxWidthIntLoadComp nullable: true
        vanMaxWidthBetweenWheelArches nullable: true
        vanMaxHeightLoadThreshold nullable: true
        vanCubage nullable: true
        vanMaxHLoadCompRearAccSpace nullable: true
        vanMaxHLoadCompSideAccSpace nullable: true
        vanDistFromLateralDoorToHead nullable: true
        vanRegistrationTyreMeasures nullable: true
        vanBuildedTyreMeasures nullable: true
        equipIdEquipment nullable: true
        equipMaker nullable: true
        equipModel nullable: true
        equipVersion nullable: true
        equipSerialNumber nullable: true
        idBookingOnlineStatus nullable: true
        finPurchaseTypeId nullable: true
        finOwner nullable: true
        finUser nullable: true
        finCostCenterCode nullable: true
        finMaxTotalKm nullable: true
        finUpperLimitKm nullable: true
        finMaxUpperLimitKm nullable: true
        finChargedForExtraKm nullable: true
        finLowerLimitKm nullable: true
        finMinLowerLimitKm nullable: true
        finRefundForKm nullable: true
        finMaxKmRefund nullable: true
        finFinancialmonthlyFee nullable: true
        finServicesMonthlyFee nullable: true
        finOtherMonthlyFee nullable: true
        finTotalMonthlyFee nullable: true
        finFinancialDescription nullable: true
        maintIsstdMaintenance nullable: true
        maintIsextraMaintenance nullable: true
        maintIsTyres nullable: true
        maintIsGlasses nullable: true
        maintIsRoadAssistance nullable: true
        maintIncMaintenanceDescr nullable: true
        deductibleDamage nullable: true
        deductibleKaskoDamage nullable: true
        deductibleTheft nullable: true
        deductibleDamageEndAgreement nullable: true
        deductibleFineFee nullable: true
        insuranceCompanyId nullable: true
        vehicleEntryPurchOrderNum nullable: true
        vehicleEntryOrderDate nullable: true
        vehicleEntrySupplierOrderNum nullable: true
        vehicleEntrySupplierOrderDate nullable: true
        vehicleEntryDeliveryNumber nullable: true
        vehicleEntryDeliveryDate nullable: true
        vehicleEntryAssignedLocationId nullable: true
        salesStatus nullable: true
        salesNewPurchVehicleNewTotVal nullable: true
        salesEurotaxBluNominalValue nullable: true
        salesEurotaxBluNominValDate nullable: true
        salesEurotaxYellowNominalValue nullable: true
        salesEurotaxYellowNominValDate nullable: true
        salesNewMountingPurchaseValue nullable: true
        salesRemainingMountingValue nullable: true
        salesRemMountValueDate nullable: true
        salesRemMountValueTotalBlu nullable: true
        salesRemMountValTotBluDate nullable: true
        salesRemMounValueTotYellow nullable: true
        salesRemMounValTotYellowDate nullable: true
        salesEstimatedDamageValue nullable: true
        salesEstimateDamageDate nullable: true
        salesMinPrice nullable: true
        salesMinAdvisedPrice nullable: true
        salesMaxPrice nullable: true
        salesMaxAdvisedPrice nullable: true
        salesPricePubliOnline nullable: true
        salesAgreementDate nullable: true
        salesAgreementNum nullable: true
        salesNotes nullable: true
        salesInvoiceDate nullable: true
        salesInvoiceNum nullable: true
        salesInvoiceTaxable nullable: true
        salesInvoiceTotalAmount nullable: true
        salesCustomerId nullable: true
        salesChangeoverOfPropertyDate nullable: true
        salesMinutesVehicleDeliveryDate nullable: true
        financialAdvance nullable: true
        financialFinalRansom nullable: true
        financialBuyBackValue nullable: true
        finMonthlyInstallmentNumb nullable: true
        finFirstInstallmentExpectDate nullable: true
        finFirstInstallmentEffectiDate nullable: true
        finLastInstallmentDate nullable: true
        insuranceCompany nullable: true
        totalInsAmount nullable: true
        keskoInsAmount nullable: true
        tax2Cost nullable: true
        tax1Cost nullable: true
        fireInsAmount nullable: true
        theftInsAmount nullable: true
        residentVal nullable: true
        thirdPartyInsAmount nullable: true
        infoCarCode nullable: true
        traction nullable: true
        chassis nullable: true
    }
}
