/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.utils.beans;
import java.io.InputStream;

/**
 *
 * @author NicoDev
 */
public class UnmappedDamageImage {
    
    public UnmappedDamageImage(){}
    
    private String damageName;
    private String DamagePath;
    private InputStream damageImage;
    private String severity;
    private String damageType;
    private String dictionary;
    private Integer progressivo;
    private Double damageTotalCost;


    private  String DamageSeveritySubType;



    private Integer danniId;
    
    public void setDamageTotalCost(Double in)
    {this.damageTotalCost=in;}
    public Double getDamageTotalCost()
    {return this.damageTotalCost;}
    public void setProgressivo(Integer progressivo)
    {this.progressivo=progressivo;}
    public Integer getProgressivo()
    {return this.progressivo;}
    public void setDamageName(String damageName)
    {this.damageName=damageName;}
    public String getDamageName()
    {return this.damageName;}
    public void setDamageImage(InputStream damageImage)
    {this.damageImage=damageImage;}
    public InputStream getDamageImage()
    {return this.damageImage;}
    
    public void setDamagePath(String DamagePath)
    {this.DamagePath=DamagePath;}
    public String getDamagePath()
    {return this.DamagePath;}
    
    public void setSeverity(String severity)
    {this.severity=severity;}
    public String getSeverity()
    {return this.severity;}
    
    public void setDamageType(String damageType)
    {this.damageType=damageType;}
    public String getDamageType()
    {return this.damageType;}
    
    public void setDictionary(String dictionary)
    {this.dictionary=dictionary;}
    public String getDictionary()
    {return this.dictionary;}

    public String getDamageSeveritySubType() {
        return DamageSeveritySubType;
    }

    public void setDamageSeveritySubType(String setDamageSeveritySubType) {
        this.DamageSeveritySubType = setDamageSeveritySubType;
    }
    public Integer getDanniId() {
        return danniId;
    }

    public void setDanniId(Integer danniId) {
        this.danniId = danniId;
    }
}
