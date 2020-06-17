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

public class MROldDocumentSeverity implements it.aessepi.utils.db.PersistentInstance
{
    private Integer id;
    private Date dateCreated;
    private String description;
    private String lang;
    private Date lastUpdated;
    private Integer damageSeverity;
    private Integer tenantId;
    
    public Integer getId()
    {
        return this.id;
    }
    public void setId(int id)
    {
        this.id=id;
    }
    public Date getDateCreated()
    {
        return this.dateCreated;
    }
    public void setDateCreated(Date in)
    {
        this.dateCreated = in;
    }
    public String getDescription()
    {
        return this.description;
    }
    public void setDescription(String desc)
    {
        this.description=desc;
    }
    public String getLang()
    {
        return this.lang;
    }
    public void setLang(String lang)
    {
        this.lang=lang;
    }
     public Date getLastUpdated()
    {
        return this.lastUpdated;
    }
    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated=lastUpdated;
    }
    public void  setDamageSeverity(Integer in)
    {
        this.damageSeverity=in;
    }
    public Integer getDamageSeverity()
    {
      return this.damageSeverity;
    }
    public Integer getTenantId()
    {
        return this.tenantId;
    }
    public void setTenantId(int tenantId)
    {
        this.tenantId=tenantId;
    }
}
