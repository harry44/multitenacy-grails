package it.myrent.ee.db;

/**
 * Created by Shivangani on 11/28/2017.
 */
public class MROldSplitPayment {

    private Integer id;
    private MROldBusinessPartner businessPartner;
    private MROldCodiciIva standardVATcode;
    private MROldCodiciIva splitVATcode;


    public MROldSplitPayment() {
    }

    public MROldSplitPayment(MROldBusinessPartner businessPartner, MROldCodiciIva standardVATcode, MROldCodiciIva splitVATcode) {
        this.businessPartner = businessPartner;
        this.standardVATcode = standardVATcode;
        this.splitVATcode = splitVATcode;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MROldSplitPayment){
            MROldSplitPayment focusSplit  = (MROldSplitPayment) obj;
            if(this.standardVATcode.getId().equals(focusSplit.getStandardVATcode().getId())
                    && this.splitVATcode.getId().equals(focusSplit.getSplitVATcode().getId())
                    &&  this.businessPartner.getId().equals(focusSplit.getBusinessPartner().getId())){
                return true;
            }
        }
        return false;
    }





    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldBusinessPartner getBusinessPartner() {
        return businessPartner;
    }

    public void setBusinessPartner(MROldBusinessPartner businessPartner) {
        this.businessPartner = businessPartner;
    }



    public MROldCodiciIva getStandardVATcode() {
        return standardVATcode;
    }

    public void setStandardVATcode(MROldCodiciIva standardVATcode) {
        this.standardVATcode = standardVATcode;
    }

    public MROldCodiciIva getSplitVATcode() {
        return splitVATcode;
    }

    public void setSplitVATcode(MROldCodiciIva splitVATcode) {
        this.splitVATcode = splitVATcode;
    }
}
