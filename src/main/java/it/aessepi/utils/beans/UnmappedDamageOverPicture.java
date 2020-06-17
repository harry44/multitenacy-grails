/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.utils.beans;

/**
 *
 * @author NicoDev
 */
public class UnmappedDamageOverPicture {
    private Integer x;
    private Integer y;
    private Integer severityCode;
    private Integer movId;
    
    public Integer getX()
    {return this.x;}
    public void setX(Integer x)
    {this.x=x;}
    public void setY(Integer y)
    {this.y=y;}
    public Integer getY()
    {return this.y;}
    public void setMovId(Integer movid)
    {this.movId=movid;}
    public Integer getMovId()
    {return this.movId;}
    public void setSeverityCode(Integer code)
    {this.severityCode=code;}
    public Integer getSeverityCode()
    {return this.severityCode;}
}
