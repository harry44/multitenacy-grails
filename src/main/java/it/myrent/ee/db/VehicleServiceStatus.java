
package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.db.PersistentInstance;
import java.util.Date;
import java.util.ResourceBundle;

public class VehicleServiceStatus implements PersistentInstance{
    
    // private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
    
    private Integer id;
    private Date startDate;
    private Date endDate;        
    private ServiceStatus serviceStatus;
    private MROldParcoVeicoli veicoli;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ServiceStatus getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public MROldParcoVeicoli getVeicoli() {
        return veicoli;
    }

    public void setVeicoli(MROldParcoVeicoli veicoli) {
        this.veicoli = veicoli;
    } 
    
    
}
