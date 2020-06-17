/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

/**
 *
 * @author andrea
 */
public class MROldDamageInPicture implements it.aessepi.utils.db.PersistentInstance{
    
    private Integer id;
    private Integer position_x;
    private Integer position_y;
    private MROldDanno damage;
    private MROldGestioneDocumentale documentManagement;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getPosition_x() {
        return position_x;
    }

    public void setPosition_x(Integer position_x) {
        this.position_x = position_x;
    }
    
     public Integer getPosition_y() {
        return position_y;
    }

    public void setPosition_y(Integer position_y) {
        this.position_y = position_y;
    }
    
     public MROldDanno getDamage() {
        return this.damage;
    }

    public void setDamage(MROldDanno damage) {
        this.damage = damage;
    
    
    }

    public MROldGestioneDocumentale getDocumentManagement() {
        return documentManagement;
    }

    public void setDocumentManagement(MROldGestioneDocumentale documentManagement) {
        this.documentManagement = documentManagement;
    }



}