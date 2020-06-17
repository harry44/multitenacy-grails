/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;
import java.util.Date;

/**
 *
 * @author NicoDev
 */
public class MrEventExpiration implements it.aessepi.utils.db.PersistentInstance{
    private Integer id;
    private Integer version;
    private Date dateCompleted;
    private Date dateCreated;
    private String description;
    private Date eventDate;
    private Double eventFuel;
    private Integer eventKm;
    private Integer eventTypeId;
    private Boolean isAck;
    private Boolean isActive;
    private Boolean isCustom;
    private Boolean isDate;
    private Boolean isFuel;
    private Boolean isDone;
    private Boolean isMeter;
    private Boolean isTime;
    private MROldParcoVeicoli veicolo;
    private MROldDocumentSeverity documentSeverity;
    private MROldDamageSeverity damageSeverity;
    private Date lastUpdated;
    private Integer tenantId;
    private Date eventTime;

    public MROldDamageSeverity getDamageSeverity()
    {
        return this.damageSeverity;
    }
    public void setDamageSeverity(MROldDamageSeverity damageSeverity)
    {
        this.damageSeverity=damageSeverity;
    }
    public Integer getTenantId()
    {
        return this.tenantId;
    }
    public void setTenantId(Integer tenantId)
    {
        this.tenantId=tenantId;
    }
    public Date getLastUpdated()
    {
        return this.lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated=lastUpdated;
    }
    public Integer getId()
    {
        return this.id;
    }
    public void setId(Integer id)
    {
        this.id=id;
    }
    public Integer getVersion()
    {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getDateCompleted()
    {
        return this.dateCompleted;
    }
    public void setDateCompleted(Date in)
    {
        this.dateCompleted = in;
    }
    public Date getDateCreated()
    {
        return this.dateCreated;
    }
    public void setDateCreated(Date in)
    {
        this.dateCreated = in;
    }
    public Date getEventDate()
    {
        return this.eventDate;
    }
    public void setEventDate(Date in)
    {
        this.eventDate = in;
    }
    public Integer getEventKm()
    {
        return this.eventKm;
    }
    public void setEventKm(Integer eventKm)
    {
        this.eventKm=eventKm;
    }
    public Integer getEventTypeId()
    {
        return this.eventTypeId;
    }
    public void setEventTypeId(Integer eventTypeId)
    {
        this.eventTypeId=eventTypeId;
    }
    public String getDescription()
    {
        return this.description;
    }
    public void setDescription(String desc)
    {
        this.description=desc;
    }
     public Double getEventFuel()
    {
        return this.eventFuel;
    }
    public void setEventFuel(Double in)
    {
        this.eventFuel=in;
    }
    public Boolean getIsAck ()
    {
        return isAck;
    }
    public void setIsAck (Boolean in)
    {
        this.isAck=in;
    }
     public Boolean getIsActive ()
    {
        return isActive;
    }
    public void setIsTime (Boolean in)
    {
        this.isTime=in;
    }
    public Boolean getIsTime ()
    {
        return isTime;
    }
    public void setIsActive (Boolean in)
    {
        this.isActive=in;
    }
    public Boolean getIsCustom ()
    {
        return isCustom;
    }
    public void setIsCustom (Boolean in)
    {
        this.isCustom=in;
    }
     public Boolean getIsDate ()
    {
        return isDate;
    }
    public void setIsDate (Boolean in)
    {
        this.isDate=in;
    }
     public Boolean getIsFuel ()
    {
        return isFuel;
    }
    public void setIsFuel (Boolean in)
    {
        this.isFuel=in;
    }
     public Boolean getIsDone ()
    {
        return isDone;
    }
    public void setIsDone (Boolean in)
    {
        this.isDone=in;
    }
     public Boolean getIsMeter ()
    {
        return isMeter;
    }
    public void setIsMeter (Boolean in)
    {
        this.isMeter=in;
    }
    public void setVeicolo(MROldParcoVeicoli in)
    {
        this.veicolo=in;
    }
    public MROldParcoVeicoli getVeicolo()
    {
        return this.veicolo;
    }
    public void  setDocumentSeverity(MROldDocumentSeverity in)
    {
        this.documentSeverity=in;
    }
    public MROldDocumentSeverity getDocumentSeverity()
    {
      return this.documentSeverity;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }
}
