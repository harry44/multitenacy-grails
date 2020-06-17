/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author dogma_system
 */
public class LocationOpeningTime implements PersistentInstance{
private Integer id;
private MROldSede locationId;
private Integer dayOfTheWeek;
private Date startTime;
private Date endTime;
private Date specificDate;
private Boolean isClosure;
private Boolean isADate;

    public Boolean getIsADate() {
        return isADate;
    }

    public void setIsADate(Boolean isADate) {
        this.isADate = isADate;
    }

    public MROldSede getLocationId() {
        return locationId;
    }

    public void setLocationId(MROldSede locationId) {
        this.locationId = locationId;
    }

    public Integer getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(Integer dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsClosure() {
        return isClosure;
    }

    public void setIsClosure(Boolean isClosure) {
        this.isClosure = isClosure;
    }

    public Date getSpecificDate() {
        return specificDate;
    }

    public void setSpecificDate(Date specificDate) {
        this.specificDate = specificDate;
    }

  

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Object getDay(){
    Map day=new HashMap();
    day.put(1,"Monday");
    day.put(2,"Tuesday");
    day.put(3,"Wednesday");
    day.put(4,"Thursday");
    day.put(5,"Friday");
    day.put(6,"Saturday");
    day.put(7,"Sunday");
    return day.get(getDayOfTheWeek());
    }

    @Override
     public boolean equals(Object other) {
        if (!(other instanceof LocationOpeningTime)) {
            return false;
        }
        LocationOpeningTime castOther = (LocationOpeningTime) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
}
