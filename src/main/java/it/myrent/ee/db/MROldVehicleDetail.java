/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.Date;
import java.util.ResourceBundle;

/**
 *
 * @author dogma_system
 */
public class MROldVehicleDetail implements PersistentInstance,Loggable {

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    private Integer id;
    private MROldVehicleStatus vehicleStatusId;
    private Boolean vanHasSideBoard;
    private Double vanMaxLengthExtVehicle;
    private Double vanMaxWidthExtVehicle;
    private Double vanMaxHeighExtVehicle;
    private Double vanMaxLengthIntLoadComp;
    private Double passo;
    private Double vanMaxWidthIntLoadComp;
    private Double vanMaxWidthBetweenWheelArches;
    private Double vanMaxHeightLoadThreshold;
    private Double vanCubage;
    private Double vanMaxHLoadCompRearAccSpace;
    private Double vanMaxHLoadCompSideAccSpace;
    private Double vanDistFromLateralDoorToHead;
    private Double vanRegistrationTyreMeasures;
    private Double vanBuildedTyreMeasures;
    private Equipment equipIdEquipment;
    private String equipMaker;
    private String equipModel;
    private String equipVersion;
    private String equipSerialNumber;
    private BookingOnlineStatus idBookingOnlineStatus;
    private PurchaseType finPurchaseTypeId;
    private String finOwner;
    private String finUser;
    private String finCostCenterCode;
    private Double finMaxTotalKm;
    private Double finUpperLimitKm;
    private Double finMaxUpperLimitKm;
    private Double finChargedForExtraKm;
    private Double finLowerLimitKm;
    private Double finMinLowerLimitKm;
    private Double finRefundForKm;
    private Double finMaxKmRefund;
    private Double finFinancialmonthlyFee;
    private Double finServicesMonthlyFee;
    private Double finOtherMonthlyFee;
    private Double finTotalMonthlyFee;
    private String finFinancialDescription;
    private Boolean maintIsstdMaintenance;
    private Boolean maintIsextraMaintenance;
    private Boolean maintIsTyres;
    private Boolean maintIsGlasses;
    private Boolean maintIsRoadAssistance;
    private String maintIncMaintenanceDescr;
    private Double deductibleDamage;
    private Double deductibleKaskoDamage;
    private Double deductibleTheft;
    private Double deductibleDamageEndAgreement;
    private Double deductibleFineFee;
    private InsuranceCompany insuranceCompanyId;
    private Integer vehicleEntryPurchOrderNum;
    private Date vehicleEntryOrderDate;
    private Integer vehicleEntrySupplierOrderNum;
    private Date vehicleEntrySupplierOrderDate;
    private String vehicleEntryDeliveryNumber;
    private Date vehicleEntryDeliveryDate;
    private MROldSede vehicleEntryAssignedLocationId;
    private SalesStatus salesStatus;
    private Double salesNewPurchVehicleNewTotVal;
    private Double salesEurotaxBluNominalValue;
    private Date salesEurotaxBluNominValDate;
    private Double salesEurotaxYellowNominalValue;
    private Date salesEurotaxYellowNominValDate;
    private Double salesNewMountingPurchaseValue;
    private Double salesRemainingMountingValue;
    private Date salesRemMountValueDate;
    private Double salesRemMountValueTotalBlu;
    private Date salesRemMountValTotBluDate;
    private Double salesRemMounValueTotYellow;
    private Date salesRemMounValTotYellowDate;
    private Double salesEstimatedDamageValue;
    private Date salesEstimateDamageDate;
    private Double salesMinPrice;
    private Double salesMinAdvisedPrice;
    private Double salesMaxPrice;
    private Double salesMaxAdvisedPrice;
    private Double salesPricePubliOnline;
    private Date salesAgreementDate;
    private String salesAgreementNum;
    private String salesNotes;
    private Date salesInvoiceDate;
    private Double salesInvoiceNum;
    private Double salesInvoiceTaxable;
    private Double salesInvoiceTotalAmount;
    private MROldClienti salesCustomerId;
    private Date salesChangeoverOfPropertyDate;
    private Date salesMinutesVehicleDeliveryDate;
    private Double financialAdvance;
    private Double financialFinalRansom;
    private Double financialBuyBackValue;
    private Integer finMonthlyInstallmentNumb;
    private Date finFirstInstallmentExpectDate;
    private Date finFirstInstallmentEffectiDate;
    private Date finLastInstallmentDate;
    private MROldParcoVeicoli parco;
    private MROldFornitori insuranceCompany;
    private MROldTrackingDevice trackingDeviceId;
    private MROldVehicleTransmission transmissionId;
    private Double totalInsAmount;
    private Double keskoInsAmount;
    private Double tax2Cost;
    private Double tax1Cost;
    private Double fireInsAmount;
    private Double theftInsAmount;
    private Double residentVal;
    private Double thirdPartyInsAmount;
    private String infoCarCode;
    private MROldVehicleTransmission transmission; // cambio
    private MROldVehicleTraction traction;
    private MROldVehicleChassis chassis;

    public MROldFornitori getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(MROldFornitori insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public MROldParcoVeicoli getParco() {
        return parco;
    }

    public void setParco(MROldParcoVeicoli parco) {
        this.parco = parco;
    }
    
    public Equipment getEquipIdEquipment() {
        return equipIdEquipment;
    }

    public void setEquipIdEquipment(Equipment equipIdEquipment) {
        this.equipIdEquipment = equipIdEquipment;
    }

    public PurchaseType getFinPurchaseTypeId() {
        return finPurchaseTypeId;
    }

    public void setFinPurchaseTypeId(PurchaseType finPurchaseTypeId) {
        this.finPurchaseTypeId = finPurchaseTypeId;
    }

    public BookingOnlineStatus getIdBookingOnlineStatus() {
        return idBookingOnlineStatus;
    }

    public void setIdBookingOnlineStatus(BookingOnlineStatus idBookingOnlineStatus) {
        this.idBookingOnlineStatus = idBookingOnlineStatus;
    }

    public InsuranceCompany getInsuranceCompanyId() {
        return insuranceCompanyId;
    }

    public void setInsuranceCompanyId(InsuranceCompany insuranceCompanyId) {
        this.insuranceCompanyId = insuranceCompanyId;
    }

    public MROldClienti getSalesCustomerId() {
        return salesCustomerId;
    }

    public void setSalesCustomerId(MROldClienti salesCustomerId) {
        this.salesCustomerId = salesCustomerId;
    }

    public SalesStatus getSalesStatus() {
        return salesStatus;
    }

    public void setSalesStatus(SalesStatus salesStatus) {
        this.salesStatus = salesStatus;
    }

    public MROldSede getVehicleEntryAssignedLocationId() {
        return vehicleEntryAssignedLocationId;
    }

    public void setVehicleEntryAssignedLocationId(MROldSede vehicleEntryAssignedLocationId) {
        this.vehicleEntryAssignedLocationId = vehicleEntryAssignedLocationId;
    }

    public MROldVehicleStatus getVehicleStatusId() {
        return vehicleStatusId;
    }

    public void setVehicleStatusId(MROldVehicleStatus vehicleStatusId) {
        this.vehicleStatusId = vehicleStatusId;
    }

    public Date getFinFirstInstallmentEffectiDate() {
        return finFirstInstallmentEffectiDate;
    }

    public void setFinFirstInstallmentEffectiDate(Date finFirstInstallmentEffectiDate) {
        this.finFirstInstallmentEffectiDate = finFirstInstallmentEffectiDate;
    }

    public Date getFinFirstInstallmentExpectDate() {
        return finFirstInstallmentExpectDate;
    }

    public void setFinFirstInstallmentExpectDate(Date finFirstInstallmentExpectDate) {
        this.finFirstInstallmentExpectDate = finFirstInstallmentExpectDate;
    }

    public Date getFinLastInstallmentDate() {
        return finLastInstallmentDate;
    }

    public void setFinLastInstallmentDate(Date finLastInstallmentDate) {
        this.finLastInstallmentDate = finLastInstallmentDate;
    }

    public Integer getFinMonthlyInstallmentNumb() {
        return finMonthlyInstallmentNumb;
    }

    public void setFinMonthlyInstallmentNumb(Integer finMonthlyInstallmentNumb) {
        this.finMonthlyInstallmentNumb = finMonthlyInstallmentNumb;
    }

    public Double getFinancialAdvance() {
        return financialAdvance;
    }

    public void setFinancialAdvance(Double financialAdvance) {
        this.financialAdvance = financialAdvance;
    }

    public Double getFinancialBuyBackValue() {
        return financialBuyBackValue;
    }

    public void setFinancialBuyBackValue(Double financialBuyBackValue) {
        this.financialBuyBackValue = financialBuyBackValue;
    }

    public Double getFinancialFinalRansom() {
        return financialFinalRansom;
    }

    public void setFinancialFinalRansom(Double financialFinalRansom) {
        this.financialFinalRansom = financialFinalRansom;
    }

    public Double getPasso() {
        return passo;
    }

    public void setPasso(Double passo) {
        this.passo = passo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getDeductibleDamage() {
        return deductibleDamage;
    }

    public void setDeductibleDamage(Double deductibleDamage) {
        this.deductibleDamage = deductibleDamage;
    }

    public Double getDeductibleDamageEndAgreement() {
        return deductibleDamageEndAgreement;
    }

    public void setDeductibleDamageEndAgreement(Double deductibleDamageEndAgreement) {
        this.deductibleDamageEndAgreement = deductibleDamageEndAgreement;
    }

    public Double getDeductibleFineFee() {
        return deductibleFineFee;
    }

    public void setDeductibleFineFee(Double deductibleFineFee) {
        this.deductibleFineFee = deductibleFineFee;
    }

    public Double getDeductibleKaskoDamage() {
        return deductibleKaskoDamage;
    }

    public void setDeductibleKaskoDamage(Double deductibleKaskoDamage) {
        this.deductibleKaskoDamage = deductibleKaskoDamage;
    }

    public Double getDeductibleTheft() {
        return deductibleTheft;
    }

    public void setDeductibleTheft(Double deductibleTheft) {
        this.deductibleTheft = deductibleTheft;
    }

    public String getEquipMaker() {
        return equipMaker;
    }

    public void setEquipMaker(String equipMaker) {
        this.equipMaker = equipMaker;
    }

    public String getEquipModel() {
        return equipModel;
    }

    public void setEquipModel(String equipModel) {
        this.equipModel = equipModel;
    }

    public String getEquipSerialNumber() {
        return equipSerialNumber;
    }

    public void setEquipSerialNumber(String equipSerialNumber) {
        this.equipSerialNumber = equipSerialNumber;
    }

    public String getEquipVersion() {
        return equipVersion;
    }

    public void setEquipVersion(String equipVersion) {
        this.equipVersion = equipVersion;
    }

    public Double getFinChargedForExtraKm() {
        return finChargedForExtraKm;
    }

    public void setFinChargedForExtraKm(Double finChargedForExtraKm) {
        this.finChargedForExtraKm = finChargedForExtraKm;
    }

    public String getFinCostCenterCode() {
        return finCostCenterCode;
    }

    public void setFinCostCenterCode(String finCostCenterCode) {
        this.finCostCenterCode = finCostCenterCode;
    }

    public String getFinFinancialDescription() {
        return finFinancialDescription;
    }

    public void setFinFinancialDescription(String finFinancialDescription) {
        this.finFinancialDescription = finFinancialDescription;
    }

    public Double getFinFinancialmonthlyFee() {
        return finFinancialmonthlyFee;
    }

    public void setFinFinancialmonthlyFee(Double finFinancialmonthlyFee) {
        this.finFinancialmonthlyFee = finFinancialmonthlyFee;
    }

    public Double getFinLowerLimitKm() {
        return finLowerLimitKm;
    }

    public void setFinLowerLimitKm(Double finLowerLimitKm) {
        this.finLowerLimitKm = finLowerLimitKm;
    }

    public Double getFinMaxKmRefund() {
        return finMaxKmRefund;
    }

    public void setFinMaxKmRefund(Double finMaxKmRefund) {
        this.finMaxKmRefund = finMaxKmRefund;
    }

    public Double getFinMaxTotalKm() {
        return finMaxTotalKm;
    }

    public void setFinMaxTotalKm(Double finMaxTotalKm) {
        this.finMaxTotalKm = finMaxTotalKm;
    }

    public Double getFinMaxUpperLimitKm() {
        return finMaxUpperLimitKm;
    }

    public void setFinMaxUpperLimitKm(Double finMaxUpperLimitKm) {
        this.finMaxUpperLimitKm = finMaxUpperLimitKm;
    }

    public Double getFinMinLowerLimitKm() {
        return finMinLowerLimitKm;
    }

    public void setFinMinLowerLimitKm(Double finMinLowerLimitKm) {
        this.finMinLowerLimitKm = finMinLowerLimitKm;
    }

    public Double getFinOtherMonthlyFee() {
        return finOtherMonthlyFee;
    }

    public void setFinOtherMonthlyFee(Double finOtherMonthlyFee) {
        this.finOtherMonthlyFee = finOtherMonthlyFee;
    }

    public String getFinOwner() {
        return finOwner;
    }

    public void setFinOwner(String finOwner) {
        this.finOwner = finOwner;
    }

    public Double getFinRefundForKm() {
        return finRefundForKm;
    }

    public void setFinRefundForKm(Double finRefundForKm) {
        this.finRefundForKm = finRefundForKm;
    }

    public Double getFinServicesMonthlyFee() {
        return finServicesMonthlyFee;
    }

    public void setFinServicesMonthlyFee(Double finServicesMonthlyFee) {
        this.finServicesMonthlyFee = finServicesMonthlyFee;
    }

    public Double getFinTotalMonthlyFee() {
        return finTotalMonthlyFee;
    }

    public void setFinTotalMonthlyFee(Double finTotalMonthlyFee) {
        this.finTotalMonthlyFee = finTotalMonthlyFee;
    }

    public Double getFinUpperLimitKm() {
        return finUpperLimitKm;
    }

    public void setFinUpperLimitKm(Double finUpperLimitKm) {
        this.finUpperLimitKm = finUpperLimitKm;
    }

    public String getFinUser() {
        return finUser;
    }

    public void setFinUser(String finUser) {
        this.finUser = finUser;
    }

    public String getMaintIncMaintenanceDescr() {
        return maintIncMaintenanceDescr;
    }

    public void setMaintIncMaintenanceDescr(String maintIncMaintenanceDescr) {
        this.maintIncMaintenanceDescr = maintIncMaintenanceDescr;
    }

    public Boolean getMaintIsGlasses() {
        return maintIsGlasses;
    }

    public void setMaintIsGlasses(Boolean maintIsGlasses) {
        this.maintIsGlasses = maintIsGlasses;
    }

    public Boolean getMaintIsRoadAssistance() {
        return maintIsRoadAssistance;
    }

    public void setMaintIsRoadAssistance(Boolean maintIsRoadAssistance) {
        this.maintIsRoadAssistance = maintIsRoadAssistance;
    }

    public Boolean getMaintIsTyres() {
        return maintIsTyres;
    }

    public void setMaintIsTyres(Boolean maintIsTyres) {
        this.maintIsTyres = maintIsTyres;
    }

    public Boolean getMaintIsextraMaintenance() {
        return maintIsextraMaintenance;
    }

    public void setMaintIsextraMaintenance(Boolean maintIsextraMaintenance) {
        this.maintIsextraMaintenance = maintIsextraMaintenance;
    }

    public Boolean getMaintIsstdMaintenance() {
        return maintIsstdMaintenance;
    }

    public void setMaintIsstdMaintenance(Boolean maintIsstdMaintenance) {
        this.maintIsstdMaintenance = maintIsstdMaintenance;
    }

    public Date getSalesChangeoverOfPropertyDate() {
        return salesChangeoverOfPropertyDate;
    }

    public void setSalesChangeoverOfPropertyDate(Date salesChangeoverOfPropertyDate) {
        this.salesChangeoverOfPropertyDate = salesChangeoverOfPropertyDate;
    }

    public Date getSalesEstimateDamageDate() {
        return salesEstimateDamageDate;
    }

    public void setSalesEstimateDamageDate(Date salesEstimateDamageDate) {
        this.salesEstimateDamageDate = salesEstimateDamageDate;
    }

    public Double getSalesEstimatedDamageValue() {
        return salesEstimatedDamageValue;
    }

    public void setSalesEstimatedDamageValue(Double salesEstimatedDamageValue) {
        this.salesEstimatedDamageValue = salesEstimatedDamageValue;
    }

    public Date getSalesEurotaxBluNominValDate() {
        return salesEurotaxBluNominValDate;
    }

    public void setSalesEurotaxBluNominValDate(Date salesEurotaxBluNominValDate) {
        this.salesEurotaxBluNominValDate = salesEurotaxBluNominValDate;
    }

    public Double getSalesEurotaxBluNominalValue() {
        return salesEurotaxBluNominalValue;
    }

    public void setSalesEurotaxBluNominalValue(Double salesEurotaxBluNominalValue) {
        this.salesEurotaxBluNominalValue = salesEurotaxBluNominalValue;
    }

    public Date getSalesEurotaxYellowNominValDate() {
        return salesEurotaxYellowNominValDate;
    }

    public void setSalesEurotaxYellowNominValDate(Date salesEurotaxYellowNominValDate) {
        this.salesEurotaxYellowNominValDate = salesEurotaxYellowNominValDate;
    }

    public Double getSalesEurotaxYellowNominalValue() {
        return salesEurotaxYellowNominalValue;
    }

    public void setSalesEurotaxYellowNominalValue(Double salesEurotaxYellowNominalValue) {
        this.salesEurotaxYellowNominalValue = salesEurotaxYellowNominalValue;
    }

    public Double getSalesInvoiceTaxable() {
        return salesInvoiceTaxable;
    }

    public void setSalesInvoiceTaxable(Double salesInvoiceTaxable) {
        this.salesInvoiceTaxable = salesInvoiceTaxable;
    }

    public Double getSalesInvoiceTotalAmount() {
        return salesInvoiceTotalAmount;
    }

    public void setSalesInvoiceTotalAmount(Double salesInvoiceTotalAmount) {
        this.salesInvoiceTotalAmount = salesInvoiceTotalAmount;
    }

   
    public Date getSalesMinutesVehicleDeliveryDate() {
        return salesMinutesVehicleDeliveryDate;
    }

    public void setSalesMinutesVehicleDeliveryDate(Date salesMinutesVehicleDeliveryDate) {
        this.salesMinutesVehicleDeliveryDate = salesMinutesVehicleDeliveryDate;
    }

    public Double getSalesNewMountingPurchaseValue() {
        return salesNewMountingPurchaseValue;
    }

    public void setSalesNewMountingPurchaseValue(Double salesNewMountingPurchaseValue) {
        this.salesNewMountingPurchaseValue = salesNewMountingPurchaseValue;
    }

    public Double getSalesNewPurchVehicleNewTotVal() {
        return salesNewPurchVehicleNewTotVal;
    }

    public void setSalesNewPurchVehicleNewTotVal(Double salesNewPurchVehicleNewTotVal) {
        this.salesNewPurchVehicleNewTotVal = salesNewPurchVehicleNewTotVal;
    }

    public Date getSalesAgreementDate() {
        return salesAgreementDate;
    }

    public void setSalesAgreementDate(Date salesAgreementDate) {
        this.salesAgreementDate = salesAgreementDate;
    }

    public String getSalesAgreementNum() {
        return salesAgreementNum;
    }

    public void setSalesAgreementNum(String salesAgreementNum) {
        this.salesAgreementNum = salesAgreementNum;
    }

    public Date getSalesInvoiceDate() {
        return salesInvoiceDate;
    }

    public void setSalesInvoiceDate(Date salesInvoiceDate) {
        this.salesInvoiceDate = salesInvoiceDate;
    }

    public Double getSalesInvoiceNum() {
        return salesInvoiceNum;
    }

    public void setSalesInvoiceNum(Double salesInvoiceNum) {
        this.salesInvoiceNum = salesInvoiceNum;
    }

    public Double getSalesMaxAdvisedPrice() {
        return salesMaxAdvisedPrice;
    }

    public void setSalesMaxAdvisedPrice(Double salesMaxAdvisedPrice) {
        this.salesMaxAdvisedPrice = salesMaxAdvisedPrice;
    }

    public Double getSalesMaxPrice() {
        return salesMaxPrice;
    }

    public void setSalesMaxPrice(Double salesMaxPrice) {
        this.salesMaxPrice = salesMaxPrice;
    }

    public Double getSalesMinAdvisedPrice() {
        return salesMinAdvisedPrice;
    }

    public void setSalesMinAdvisedPrice(Double salesMinAdvisedPrice) {
        this.salesMinAdvisedPrice = salesMinAdvisedPrice;
    }

    public Double getSalesMinPrice() {
        return salesMinPrice;
    }

    public void setSalesMinPrice(Double salesMinPrice) {
        this.salesMinPrice = salesMinPrice;
    }

    public String getSalesNotes() {
        return salesNotes;
    }

    public void setSalesNotes(String salesNotes) {
        this.salesNotes = salesNotes;
    }

    public Double getSalesPricePubliOnline() {
        return salesPricePubliOnline;
    }

    public void setSalesPricePubliOnline(Double salesPricePubliOnline) {
        this.salesPricePubliOnline = salesPricePubliOnline;
    }

    
    public Date getSalesRemMounValTotYellowDate() {
        return salesRemMounValTotYellowDate;
    }

    public void setSalesRemMounValTotYellowDate(Date salesRemMounValTotYellowDate) {
        this.salesRemMounValTotYellowDate = salesRemMounValTotYellowDate;
    }

    public Double getSalesRemMounValueTotYellow() {
        return salesRemMounValueTotYellow;
    }

    public void setSalesRemMounValueTotYellow(Double salesRemMounValueTotYellow) {
        this.salesRemMounValueTotYellow = salesRemMounValueTotYellow;
    }

    public Date getSalesRemMountValTotBluDate() {
        return salesRemMountValTotBluDate;
    }

    public void setSalesRemMountValTotBluDate(Date salesRemMountValTotBluDate) {
        this.salesRemMountValTotBluDate = salesRemMountValTotBluDate;
    }

    public Date getSalesRemMountValueDate() {
        return salesRemMountValueDate;
    }

    public void setSalesRemMountValueDate(Date salesRemMountValueDate) {
        this.salesRemMountValueDate = salesRemMountValueDate;
    }

    public Double getSalesRemMountValueTotalBlu() {
        return salesRemMountValueTotalBlu;
    }

    public void setSalesRemMountValueTotalBlu(Double salesRemMountValueTotalBlu) {
        this.salesRemMountValueTotalBlu = salesRemMountValueTotalBlu;
    }

    public Double getSalesRemainingMountingValue() {
        return salesRemainingMountingValue;
    }

    public void setSalesRemainingMountingValue(Double salesRemainingMountingValue) {
        this.salesRemainingMountingValue = salesRemainingMountingValue;
    }

    public Double getVanBuildedTyreMeasures() {
        return vanBuildedTyreMeasures;
    }

    public void setVanBuildedTyreMeasures(Double vanBuildedTyreMeasures) {
        this.vanBuildedTyreMeasures = vanBuildedTyreMeasures;
    }

    public Double getVanCubage() {
        return vanCubage;
    }

    public void setVanCubage(Double vanCubage) {
        this.vanCubage = vanCubage;
    }

    public Double getVanDistFromLateralDoorToHead() {
        return vanDistFromLateralDoorToHead;
    }

    public void setVanDistFromLateralDoorToHead(Double vanDistFromLateralDoorToHead) {
        this.vanDistFromLateralDoorToHead = vanDistFromLateralDoorToHead;
    }

    public Boolean getVanHasSideBoard() {
        return vanHasSideBoard;
    }

    public void setVanHasSideBoard(Boolean vanHasSideBoard) {
        this.vanHasSideBoard = vanHasSideBoard;
    }

    public Double getVanMaxHLoadCompRearAccSpace() {
        return vanMaxHLoadCompRearAccSpace;
    }

    public void setVanMaxHLoadCompRearAccSpace(Double vanMaxHLoadCompRearAccSpace) {
        this.vanMaxHLoadCompRearAccSpace = vanMaxHLoadCompRearAccSpace;
    }

    public Double getVanMaxHLoadCompSideAccSpace() {
        return vanMaxHLoadCompSideAccSpace;
    }

    public void setVanMaxHLoadCompSideAccSpace(Double vanMaxHLoadCompSideAccSpace) {
        this.vanMaxHLoadCompSideAccSpace = vanMaxHLoadCompSideAccSpace;
    }

    public Double getVanMaxHeighExtVehicle() {
        return vanMaxHeighExtVehicle;
    }

    public void setVanMaxHeighExtVehicle(Double vanMaxHeighExtVehicle) {
        this.vanMaxHeighExtVehicle = vanMaxHeighExtVehicle;
    }

    public Double getVanMaxHeightLoadThreshold() {
        return vanMaxHeightLoadThreshold;
    }

    public void setVanMaxHeightLoadThreshold(Double vanMaxHeightLoadThreshold) {
        this.vanMaxHeightLoadThreshold = vanMaxHeightLoadThreshold;
    }

    public Double getVanMaxLengthExtVehicle() {
        return vanMaxLengthExtVehicle;
    }

    public void setVanMaxLengthExtVehicle(Double vanMaxLengthExtVehicle) {
        this.vanMaxLengthExtVehicle = vanMaxLengthExtVehicle;
    }

    public Double getVanMaxLengthIntLoadComp() {
        return vanMaxLengthIntLoadComp;
    }

    public void setVanMaxLengthIntLoadComp(Double vanMaxLengthIntLoadComp) {
        this.vanMaxLengthIntLoadComp = vanMaxLengthIntLoadComp;
    }

    public Double getVanMaxWidthBetweenWheelArches() {
        return vanMaxWidthBetweenWheelArches;
    }

    public void setVanMaxWidthBetweenWheelArches(Double vanMaxWidthBetweenWheelArches) {
        this.vanMaxWidthBetweenWheelArches = vanMaxWidthBetweenWheelArches;
    }

    public Double getVanMaxWidthExtVehicle() {
        return vanMaxWidthExtVehicle;
    }

    public void setVanMaxWidthExtVehicle(Double vanMaxWidthExtVehicle) {
        this.vanMaxWidthExtVehicle = vanMaxWidthExtVehicle;
    }

    public Double getVanMaxWidthIntLoadComp() {
        return vanMaxWidthIntLoadComp;
    }

    public void setVanMaxWidthIntLoadComp(Double vanMaxWidthIntLoadComp) {
        this.vanMaxWidthIntLoadComp = vanMaxWidthIntLoadComp;
    }

    public Double getVanRegistrationTyreMeasures() {
        return vanRegistrationTyreMeasures;
    }

    public void setVanRegistrationTyreMeasures(Double vanRegistrationTyreMeasures) {
        this.vanRegistrationTyreMeasures = vanRegistrationTyreMeasures;
    }

    public Date getVehicleEntryDeliveryDate() {
        return vehicleEntryDeliveryDate;
    }

    public void setVehicleEntryDeliveryDate(Date vehicleEntryDeliveryDate) {
        this.vehicleEntryDeliveryDate = vehicleEntryDeliveryDate;
    }

    public String getVehicleEntryDeliveryNumber() {
        return vehicleEntryDeliveryNumber;
    }

    public void setVehicleEntryDeliveryNumber(String vehicleEntryDeliveryNumber) {
        this.vehicleEntryDeliveryNumber = vehicleEntryDeliveryNumber;
    }

    public Date getVehicleEntryOrderDate() {
        return vehicleEntryOrderDate;
    }

    public void setVehicleEntryOrderDate(Date vehicleEntryOrderDate) {
        this.vehicleEntryOrderDate = vehicleEntryOrderDate;
    }

    public Integer getVehicleEntryPurchOrderNum() {
        return vehicleEntryPurchOrderNum;
    }

    public void setVehicleEntryPurchOrderNum(Integer vehicleEntryPurchOrderNum) {
        this.vehicleEntryPurchOrderNum = vehicleEntryPurchOrderNum;
    }

    public Date getVehicleEntrySupplierOrderDate() {
        return vehicleEntrySupplierOrderDate;
    }

    public void setVehicleEntrySupplierOrderDate(Date vehicleEntrySupplierOrderDate) {
        this.vehicleEntrySupplierOrderDate = vehicleEntrySupplierOrderDate;
    }

    public Integer getVehicleEntrySupplierOrderNum() {
        return vehicleEntrySupplierOrderNum;
    }

    public void setVehicleEntrySupplierOrderNum(Integer vehicleEntrySupplierOrderNum) {
        this.vehicleEntrySupplierOrderNum = vehicleEntrySupplierOrderNum;
    }

//    @Override
//    public String toString() {
//       return description != null ? description : new String();
//    }
// @Override
//    public int compareTo(MROldVehicleDetail o) {
//        if (o == null) {
//            return 1;
//        } else {
//            return new CompareToBuilder().append(getDescription(), o.getDescription()).
//                    append(getId(), o.getId()).
//                    toComparison();
//        }
//    }
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MROldVehicleDetail)) {
            return false;
        }
        MROldVehicleDetail castOther = (MROldVehicleDetail) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public String[] getLoggableFields() {
        return new String[]{
                "vehicleStatusId", // NOI18N
                "vehicleEntryAssignedLocationId" // NOI18N
        };

    }

    public String[] getLoggableLabels() {
        return new String[]{
                bundle.getString("VehicleDetail.vehicleStatusId"),
                bundle.getString("VehicleDetail.vehicleEntryAssignedLocationId")

        };
    }

    public String getEntityName() {
        return "MROldVehicleDetail"; // NOI18N
    }

    public Integer getEntityId() {
        return getId();
    }

    public MROldTrackingDevice getTrackingDeviceId() {
        return trackingDeviceId;
    }

    public void setTrackingDeviceId(MROldTrackingDevice trackingDeviceId) {
        this.trackingDeviceId = trackingDeviceId;
    }


    public MROldVehicleTransmission getTransmissionId() {
        return transmissionId;
    }

    public void setTransmissionId(MROldVehicleTransmission transmissionId) {
        this.transmissionId = transmissionId;
    }


    public Double getTotalInsAmount() {
        return totalInsAmount;
    }

    public void setTotalInsAmount(Double totalInsAmount) {
        this.totalInsAmount = totalInsAmount;
    }

    public Double getKeskoInsAmount() {
        return keskoInsAmount;
    }

    public void setKeskoInsAmount(Double keskoInsAmount) {
        this.keskoInsAmount = keskoInsAmount;
    }

    public Double getTax2Cost() {
        return tax2Cost;
    }

    public void setTax2Cost(Double tax2Cost) {
        this.tax2Cost = tax2Cost;
    }

    public Double getTax1Cost() {
        return tax1Cost;
    }

    public void setTax1Cost(Double tax1Cost) {
        this.tax1Cost = tax1Cost;
    }

    public Double getFireInsAmount() {
        return fireInsAmount;
    }

    public void setFireInsAmount(Double fireInsAmount) {
        this.fireInsAmount = fireInsAmount;
    }

    public Double getTheftInsAmount() {
        return theftInsAmount;
    }

    public void setTheftInsAmount(Double theftInsAmount) {
        this.theftInsAmount = theftInsAmount;
    }

    public Double getResidentVal() {
        return residentVal;
    }

    public void setResidentVal(Double residentVal) {
        this.residentVal = residentVal;
    }

    public Double getThirdPartyInsAmount() {
        return thirdPartyInsAmount;
    }

    public void setThirdPartyInsAmount(Double thirdPartyInsAmount) {
        this.thirdPartyInsAmount = thirdPartyInsAmount;
    }

    public String getInfoCarCode() {
        return infoCarCode;
    }

    public void setInfoCarCode(String infoCarCode) {
        this.infoCarCode = infoCarCode;
    }

    public MROldVehicleTransmission getTransmission() {
        return transmission;
    }

    public void setTransmission(MROldVehicleTransmission transmission) {
        this.transmission = transmission;
    }

    public MROldVehicleTraction getTraction() {
        return traction;
    }

    public void setTraction(MROldVehicleTraction traction) {
        this.traction = traction;
    }

    public MROldVehicleChassis getChassis() {
        return chassis;
    }

    public void setChassis(MROldVehicleChassis chassis) {
        this.chassis = chassis;
    }
}
