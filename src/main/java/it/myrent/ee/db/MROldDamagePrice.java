/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.db;

/**
 *
 * @author Madhvendra
 */
public class MROldDamagePrice {
        Integer id;
   	MROldDamageSeverity damageSeverity;
	MROldDamageDictionary damageDictionary;
	MROldGruppo groups;
	Double minPrice;
	Double suggestedPrice;
	Double maxPrice;
	Double estimatedPrice;
	Double maxDiscount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MROldDamageSeverity getDamageSeverity() {
        return damageSeverity;
    }

    public void setDamageSeverity(MROldDamageSeverity damageSeverity) {
        this.damageSeverity = damageSeverity;
    }

    public MROldDamageDictionary getDamageDictionary() {
        return damageDictionary;
    }

    public void setDamageDictionary(MROldDamageDictionary damageDictionary) {
        this.damageDictionary = damageDictionary;
    }

    public MROldGruppo getGroups() {
        return groups;
    }

    public void setGroups(MROldGruppo groups) {
        this.groups = groups;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(Double suggestedPrice) {
        this.suggestedPrice = suggestedPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getEstimatedPrice() {
        return estimatedPrice;
    }

    public void setEstimatedPrice(Double estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }

    public Double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }
        
}
