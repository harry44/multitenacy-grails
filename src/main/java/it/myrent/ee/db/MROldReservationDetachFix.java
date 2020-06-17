package it.myrent.ee.db;

import java.util.Date;

/**
 * Created by shivangani on 24/05/2019.
 */
public class MROldReservationDetachFix implements it.aessepi.utils.db.PersistentInstance  {

    private Integer id;
    private MROldSede sede;
    private Date scheduleDate;
    private Integer schedulerDateTo;
    private Integer schedulerDateFrom;
    private Boolean isActivated;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldSede getSede() {
        return sede;
    }

    public void setSede(MROldSede sede) {
        this.sede = sede;
    }

    public Integer getSchedulerDateTo() {
        return schedulerDateTo;
    }

    public void setSchedulerDateTo(Integer schedulerDateTo) {
        this.schedulerDateTo = schedulerDateTo;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }


    public Boolean getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(Boolean isActivated) {
        this.isActivated = isActivated;
    }


    public Integer getSchedulerDateFrom() {
        return schedulerDateFrom;
    }

    public void setSchedulerDateFrom(Integer schedulerDateFrom) {
        this.schedulerDateFrom = schedulerDateFrom;
    }

}
