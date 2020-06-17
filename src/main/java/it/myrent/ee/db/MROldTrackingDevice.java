package it.myrent.ee.db;

/**
 * Created by shivangani on 25/07/2019.
 */
public class MROldTrackingDevice implements it.aessepi.utils.db.PersistentInstance{

    private Integer id;

    private String imei;
    private String userName;
    private String password;
    private String mobileNumber;
    private MROldTrackingTypeSystem trackingTypeSystemId;


    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public MROldTrackingTypeSystem getTrackingTypeSystemId() {
        return trackingTypeSystemId;
    }

    public void setTrackingTypeSystemId(MROldTrackingTypeSystem trackingTypeSystemId) {
        this.trackingTypeSystemId = trackingTypeSystemId;
    }

    public String toString() {
        return  getTrackingTypeSystemId().getDescription() + " - " +getImei();
    }

}
