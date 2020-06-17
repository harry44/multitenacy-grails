package it.myrent.ee.csv;

import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Arrays;

/**
 * Created by shivangani on 30/08/2019.
 */
public class TJCsvRecord extends CsvRecord {

    /**
     *
     * @param fields
     */
    public TJCsvRecord(String[] fields) {
        super();



        String str = "";
        setFields(Arrays.copyOf(fields, 16));
        setReservationNumber(parseString(getFields()[0]));
        setStatus(parseString(getFields()[1]));
        setSupplierReference(parseString(getFields()[2]));
        setFirstName(parseString(getFields()[3]));
        setLastName(parseString(getFields()[4]));
//        setAge(parseString(getFields()[5]));
//        setCoo(parseString(getFields()[6]));
        setPickupLocation(parseString(getFields()[5]));
        setPickupTime(parseString(getFields()[6]));
        setDropoffLocation(parseString(getFields()[7]));
        setDropoffTime(parseString(getFields()[8]));
        setDays(parseString(getFields()[9]));

        setSupplierGroup(parseString(getFields()[10]));
//        setExtras(parseString(getFields()[11]));
        setCountry(parseString(getFields()[11]));
        setCompany(parseString(getFields()[12]));
        str = parseString(getFields()[13]);
        setFlightNumber((str != null && str != "") ? str : "na");
        str = parseString(getFields()[14]);
        setAccountNo((str != null && str != "") ? str : "na");
        setRateCode(parseString(getFields()[15]));
//        setComments(parseString(getFields()[16]));
    }

    private Boolean isPrepaid=false;
    /**
     * reserv_num - broker reservation number
     */
    private String reservationNumber;
    /**
     * status - reservation status (ACCP, CHECK)
     */
    private String status;
    /**
     * supplier_ref - supplier confirmation number
     */
    private String supplierReference;
    /**
     * firstname - driver's first name
     */
    private String firstName;
    /**
     * lastname - driver's last name
     */
    private String lastName;
    /**
     * age - driver's age
     */
    private String age;
    /**
     * coo - unknown field. seems to be some promotion code.
     */
    private String coo;
    /**
     * pickuploc - pickup location description
     */
    private String pickupLocation;
    /**
     * pick_up_time - pickup date time formatted as dd/mm/yyyy HH:mm
     */
    private String pickupTime;
    /**
     * dropoffloc - drop off location description
     */
    private String dropoffLocation;
    /**
     * drop_off_time - dropoff date time formatted as dd/mm/yyyy HH:mm
     */
    private String dropoffTime;
    /**
     * days - rental duration in days
     */
    private String days;
    /**
     * supplier_group - supplier group code
     */
    private String supplierGroup;
    /**
     * extras - optionals, probably free text.
     */
    private String extras;
    /**
     * country - country (dash region)
     */
    private String country;
    /**
     * company - supplier company name
     */
    private String company;
    /**
     * flight_number Flight information
     */
    private String flightNumber;
    /**
     * account_no - unknown field
     */
    private String accountNo;
    /**
     * rate_code - unknown field
     */
    private String rateCode;
    /**
     * comments
     */
    private String comments;
    private static final StandardToStringStyle toStringStyle;


    static {
        toStringStyle = new StandardToStringStyle();
        toStringStyle.setNullText(new String());
        toStringStyle.setUseIdentityHashCode(false);
        toStringStyle.setUseClassName(false);
        toStringStyle.setUseFieldNames(false);
        toStringStyle.setFieldSeparator(", ");
    }

    @Override
    public String toString() {
        ToStringBuilder toStringBuilder = new ToStringBuilder(this, toStringStyle);
        toStringBuilder.append(getReservationNumber()).
                append(getLastName()).
                append(getFirstName()).
                append(getPickupTime()).
                append(getPickupLocation());
        return toStringBuilder.toString();
    }

    /**
     *
     * @return
     */
    @Override
    public String[] toArray() {

        getFields()[0] = getReservationNumber();
        getFields()[1] = getStatus();
        getFields()[2] = ((getSupplierReference()!="null" && getSupplierReference()!=null) ? getSupplierReference() : "na");
        getFields()[3] = getFirstName();
        getFields()[4] = getLastName();
//        getFields()[5] = getAge();
//        getFields()[6] = getCoo();
        getFields()[5] = getPickupLocation();
        getFields()[6] = getPickupTime();
        getFields()[7] = getDropoffLocation();
        getFields()[8] = getDropoffTime();
        getFields()[9] = getDays();
        getFields()[10] = getSupplierGroup();
//        getFields()[11] = getExtras();
        getFields()[11] = getCountry();
        getFields()[12] = getCompany();
        getFields()[13] = getFlightNumber();
        getFields()[14] = getAccountNo();
        getFields()[15] = getRateCode();
//        getFields()[16] = getComments();

        return getFields();
    }

    @Override
    public Object clone() {
        return new TJCsvRecord(Arrays.copyOf(getFields(), getFields().length));
    }

    /**
     * Removes any trainling or ending spaces, and if the resulting string is empty, returns null.
     * @param source The string to be verified.
     * @return
     */
    private static String parseString(String source) {
        if (source != null) {
            source = source.trim();
            if (source.length() > 0) {
                return source;
            }
        }
        return null;
    }

    /**
     * reserv_num - broker reservation number
     * @return the reservationNumber
     */
    public String getReservationNumber() {
        return reservationNumber;
    }

    /**
     * reserv_num - broker reservation number
     * @param reservationNumber the reservationNumber to set
     */
    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    /**
     * status - reservation status (ACCP, CHECK)
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * status - reservation status (ACCP, CHECK)
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * supplier_ref - supplier confirmation number
     * @return the supplierReference
     */
    public String getSupplierReference() {
        return supplierReference;
    }

    /**
     * supplier_ref - supplier confirmation number
     * @param supplierReference the supplierReference to set
     */
    public void setSupplierReference(String supplierReference) {
        this.supplierReference = supplierReference;
    }

    /**
     * firstname - driver's first name
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * firstname - driver's first name
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * lastname - driver's last name
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * lastname - driver's last name
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * age - driver's age
     * @return the age
     */
    public String getAge() {
        return age;
    }

    /**
     * age - driver's age
     * @param age the age to set
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * coo - unknown field. seems to be some promotion code.
     * @return the coo
     */
    public String getCoo() {
        return coo;
    }

    /**
     * coo - unknown field. seems to be some promotion code.
     * @param coo the coo to set
     */
    public void setCoo(String coo) {
        this.coo = coo;
    }

    /**
     * pickuploc - pickup location description
     * @return the pickupLocation
     */
    public String getPickupLocation() {
        return pickupLocation;
    }

    /**
     * pickuploc - pickup location description
     * @param pickupLocation the pickupLocation to set
     */
    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    /**
     * pick_up_time - pickup date time formatted as dd/mm/yyyy HH:mm
     * @return the pickupTime
     */
    public String getPickupTime() {
        return pickupTime;
    }

    /**
     * pick_up_time - pickup date time formatted as dd/mm/yyyy HH:mm
     * @param pickupTime the pickupTime to set
     */
    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    /**
     * dropoffloc - drop off location description
     * @return the dropoffLocation
     */
    public String getDropoffLocation() {
        return dropoffLocation;
    }

    /**
     * dropoffloc - drop off location description
     * @param dropoffLocation the dropoffLocation to set
     */
    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    /**
     * drop_off_time - dropoff date time formatted as dd/mm/yyyy HH:mm
     * @return the dropoffTime
     */
    public String getDropoffTime() {
        return dropoffTime;
    }

    /**
     * drop_off_time - dropoff date time formatted as dd/mm/yyyy HH:mm
     * @param dropoffTime the dropoffTime to set
     */
    public void setDropoffTime(String dropoffTime) {
        this.dropoffTime = dropoffTime;
    }

    /**
     * days - rental duration in days
     * @return the days
     */
    public String getDays() {
        return days;
    }

    /**
     * days - rental duration in days
     * @param days the days to set
     */
    public void setDays(String days) {
        this.days = days;
    }

    /**
     * supplier_group - supplier group code
     * @return the supplierGroup
     */
    public String getSupplierGroup() {
        return supplierGroup;
    }

    /**
     * supplier_group - supplier group code
     * @param supplierGroup the supplierGroup to set
     */
    public void setSupplierGroup(String supplierGroup) {
        this.supplierGroup = supplierGroup;
    }

    /**
     * extras - optionals, probably free text.
     * @return the extras
     */
    public String getExtras() {
        return extras;
    }

    /**
     * extras - optionals, probably free text.
     * @param extras the extras to set
     */
    public void setExtras(String extras) {
        this.extras = extras;
    }

    /**
     * country - country (dash region)
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * country - country (dash region)
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * company - supplier company name
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * company - supplier company name
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * flight_number Flight information
     * @return the flightNumber
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * flight_number Flight information
     * @param flightNumber the flightNumber to set
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * account_no - unknown field
     * @return the accountNo
     */
    public String getAccountNo() {
        return accountNo;
    }

    /**
     * account_no - unknown field
     * @param accountNo the accountNo to set
     */
    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    /**
     * rate_code - unknown field
     * @return the rateCode
     */
    public String getRateCode() {
        return rateCode;
    }

    /**
     * rate_code - unknown field
     * @param rateCode the rateCode to set
     */
    public void setRateCode(String rateCode) {
        this.rateCode = rateCode;
    }

    /**
     * comments
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * comments
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
}
