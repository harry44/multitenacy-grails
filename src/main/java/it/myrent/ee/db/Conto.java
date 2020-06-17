package it.myrent.ee.db;

import java.text.DecimalFormat;

import java.text.MessageFormat;

/** @author Hibernate CodeGenerator */
public class Conto extends MROldPianoDeiConti {
    
    public Conto() {
    }
    
    public Conto(Integer codiceMastro, Integer codiceConto, String descrizione) {
        super(codiceMastro, codiceConto, new Integer(0), new Integer(0), descrizione);        
    }
    
    public String getTipo() {
        return CONTO;
    }
    
    public DecimalFormat getFormatter() {
        return DF_CONTO;
    }
    
    public MROldPianoDeiConti newLastSibling() {
        return new Conto(
                getCodiceMastro(),
                new Integer(getCodiceConto().intValue() + 1),
                MessageFormat.format("Nuovo {0}", CONTO.toLowerCase()));
    }
    
    public MROldPianoDeiConti newFirstChild() {
        return new MROldSottoconto(
                getCodiceMastro(),
                getCodiceConto(),
                new Integer(1),
                MessageFormat.format("Nuovo {0}", SOTTOCONTO.toLowerCase()));
    }
}
