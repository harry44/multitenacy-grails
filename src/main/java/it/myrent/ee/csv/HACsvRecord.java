package it.myrent.ee.csv;

import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Arrays;

/**
 * Created by shivangani on 30/08/2019.
 */
public class HACsvRecord extends CsvRecord {

    /**
     *
     * @param fields
     */
    public HACsvRecord(String[] fields) {
        super();
        setFields(Arrays.copyOf(fields, 23));
        setBookingReference(parseString(getFields()[0]));
        setBookingStatus(parseString(getFields()[1]));
        setSupplierConfirmationNumber(parseString(getFields()[2]));
        setCustomerBookingRef(parseString(getFields()[3]));
        setDriverName(parseString(getFields()[4]));
        setRentalStartDate(parseString(getFields()[5]));
        setRentalEndDate(parseString(getFields()[6]));
        setHARentalStationId(parseString(getFields()[7]));
        setRentalStartLocation(parseString(getFields()[8]));
        setRentalEndLocation(parseString(getFields()[9]));
        setMemorandum(parseString(getFields()[10]));
        setHAGroupCode(parseString(getFields()[11]));
        setHAOptionCode(parseString(getFields()[12]));
        setSupplierGroupCode(parseString(getFields()[13]));
        setDescription(parseString(getFields()[14]));
        setAdditionalInformation(parseString(getFields()[15]));
        setFlightDetails(parseString(getFields()[16]));
        setCollectionMethod(parseString(getFields()[17]));
        setCollectionInformation(parseString(getFields()[18]));
        setReturnMethod(parseString(getFields()[19]));
        setReturnInformation(parseString(getFields()[20]));
        setRentalExtras(parseString(getFields()[21]));
        setSupplierCode(parseString(getFields()[22]));
    }
    private String bookingReference;            // 0
    private String bookingStatus;               // 1
    private String supplierConfirmationNumber;  // 2
    private String customerBookingRef;          // 3
    private String driverName;                  // 4
    private String rentalStartDate;             // 5
    private String rentalEndDate;               // 6
    private String hARentalStationId;           // 7
    private String rentalStartLocation;         // 8
    private String rentalEndLocation;           // 9
    private String memorandum;                  // 10
    private String hAGroupCode;                 // 11
    private String hAOptionCode;                // 12
    private String supplierGroupCode;           // 13
    private String description;                 // 14
    private String additionalInformation;       // 15
    private String flightDetails;               // 16
    private String collectionMethod;            // 17
    private String collectionInformation;       // 18
    private String returnMethod;                // 19
    private String returnInformation;           // 20
    private String rentalExtras;                // 21
    private String supplierCode;                // 22
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
        return new ToStringBuilder(this, toStringStyle).append(getBookingReference()).
                append(getBookingStatus()).
                append(getDriverName()).
                append(getRentalStartDate()).
                toString();
    }

    /**
     *
     * @return
     */
    @Override
    public String[] toArray() {
        getFields()[0] = getBookingReference();
        getFields()[1] = getBookingStatus();
        getFields()[2] = getSupplierConfirmationNumber();
        getFields()[3] = getCustomerBookingRef();
        getFields()[4] = getDriverName();
        getFields()[5] = getRentalStartDate();
        getFields()[6] = getRentalEndDate();
        getFields()[7] = getHARentalStationId();
        getFields()[8] = getRentalStartLocation();
        getFields()[9] = getRentalEndLocation();
        getFields()[10] = getMemorandum();
        getFields()[11] = getHAGroupCode();
        getFields()[12] = getHAOptionCode();
        getFields()[13] = getSupplierGroupCode();
        getFields()[14] = getDescription();
        getFields()[15] = getAdditionalInformation();
        getFields()[16] = getFlightDetails();
        getFields()[17] = getCollectionMethod();
        getFields()[18] = getCollectionInformation();
        getFields()[19] = getReturnMethod();
        getFields()[20] = getReturnInformation();
        getFields()[21] = getRentalExtras();
        getFields()[22] = getSupplierCode();
        return getFields();
    }

    @Override
    public Object clone() {
        return new HACsvRecord(Arrays.copyOf(getFields(), getFields().length));
    }

    /**
     *
     * @return
     */
    public String getBookingReference() {
        return bookingReference;
    }

    /**
     *
     * @param bookingReference
     */
    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }

    /**
     *
     * @return
     */
    public String getBookingStatus() {
        return bookingStatus;
    }

    /**
     *
     * @param bookingStatus
     */
    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    /**
     *
     * @return
     */
    public String getSupplierConfirmationNumber() {
        return supplierConfirmationNumber;
    }

    /**
     *
     * @param supplierConfirmationNumber
     */
    public void setSupplierConfirmationNumber(String supplierConfirmationNumber) {
        this.supplierConfirmationNumber = supplierConfirmationNumber;
    }

    /**
     *
     * @return
     */
    public String getCustomerBookingRef() {
        return customerBookingRef;
    }

    /**
     *
     * @param customerBookingRef
     */
    public void setCustomerBookingRef(String customerBookingRef) {
        this.customerBookingRef = customerBookingRef;
    }

    /**
     *
     * @return
     */
    public String getDriverName() {
        return driverName;
    }

    /**
     *
     * @param driverName
     */
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    /**
     *
     * @return
     */
    public String getRentalStartDate() {
        return rentalStartDate;
    }

    /**
     *
     * @param rentalStartDate
     */
    public void setRentalStartDate(String rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    /**
     *
     * @return
     */
    public String getRentalEndDate() {
        return rentalEndDate;
    }

    /**
     *
     * @param rentalEndDate
     */
    public void setRentalEndDate(String rentalEndDate) {
        this.rentalEndDate = rentalEndDate;
    }

    /**
     *
     * @return
     */
    public String getHARentalStationId() {
        return hARentalStationId;
    }

    /**
     *
     * @param hARentalStationId
     */
    public void setHARentalStationId(String hARentalStationId) {
        this.hARentalStationId = hARentalStationId;
    }

    /**
     *
     * @return
     */
    public String getRentalStartLocation() {
        return rentalStartLocation;
    }

    /**
     *
     * @param rentalStartLocation
     */
    public void setRentalStartLocation(String rentalStartLocation) {
        this.rentalStartLocation = rentalStartLocation;
    }

    /**
     *
     * @return
     */
    public String getRentalEndLocation() {
        return rentalEndLocation;
    }

    /**
     *
     * @param rentalEndLocation
     */
    public void setRentalEndLocation(String rentalEndLocation) {
        this.rentalEndLocation = rentalEndLocation;
    }

    /**
     *
     * @return
     */
    public String getMemorandum() {
        return memorandum;
    }

    /**
     *
     * @param memorandum
     */
    public void setMemorandum(String memorandum) {
        this.memorandum = memorandum;
    }

    /**
     *
     * @return
     */
    public String getHAGroupCode() {
        return hAGroupCode;
    }

    /**
     *
     * @param hAGroupCode
     */
    public void setHAGroupCode(String hAGroupCode) {
        this.hAGroupCode = hAGroupCode;
    }

    /**
     *
     * @return
     */
    public String getHAOptionCode() {
        return hAOptionCode;
    }

    /**
     *
     * @param hAOptionCode
     */
    public void setHAOptionCode(String hAOptionCode) {
        this.hAOptionCode = hAOptionCode;
    }

    /**
     *
     * @return
     */
    public String getSupplierGroupCode() {
        return supplierGroupCode;
    }

    /**
     *
     * @param supplierGroupCode
     */
    public void setSupplierGroupCode(String supplierGroupCode) {
        this.supplierGroupCode = supplierGroupCode;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     *
     * @param additionalInformation
     */
    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    /**
     *
     * @return
     */
    public String getFlightDetails() {
        return flightDetails;
    }

    /**
     *
     * @param flightDetails
     */
    public void setFlightDetails(String flightDetails) {
        this.flightDetails = flightDetails;
    }

    /**
     *
     * @return
     */
    public String getCollectionMethod() {
        return collectionMethod;
    }

    /**
     *
     * @param collectionMethod
     */
    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }

    /**
     *
     * @return
     */
    public String getCollectionInformation() {
        return collectionInformation;
    }

    /**
     *
     * @param collectionInformation
     */
    public void setCollectionInformation(String collectionInformation) {
        this.collectionInformation = collectionInformation;
    }

    /**
     *
     * @return
     */
    public String getReturnMethod() {
        return returnMethod;
    }

    /**
     *
     * @param returnMethod
     */
    public void setReturnMethod(String returnMethod) {
        this.returnMethod = returnMethod;
    }

    /**
     *
     * @return
     */
    public String getReturnInformation() {
        return returnInformation;
    }

    /**
     *
     * @param returnInformation
     */
    public void setReturnInformation(String returnInformation) {
        this.returnInformation = returnInformation;
    }

    /**
     *
     * @return
     */
    public String getRentalExtras() {
        return rentalExtras;
    }

    /**
     *
     * @param rentalExtras
     */
    public void setRentalExtras(String rentalExtras) {
        this.rentalExtras = rentalExtras;
    }

    /**
     *
     * @return
     */
    public String getSupplierCode() {
        return supplierCode;
    }

    /**
     *
     * @param supplierCode
     */
    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    /**
     * Removes any trainling or ending spaces, and if the resulting string is empty, returns null.
     * @param source The string to be verified.
     * @return
     */
    private static String parseString(String source) {
        if (source != null) {
            source = source.trim();
            if (source.length() > 0 && !source.toLowerCase().equals("null") && !source.toLowerCase().equals("from on")) {
                return source;
            }
        }
        return null;
    }
}
