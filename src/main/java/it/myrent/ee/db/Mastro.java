package it.myrent.ee.db;

import java.text.DecimalFormat;

import java.text.MessageFormat;

/** @author Hibernate CodeGenerator */
public class Mastro extends MROldPianoDeiConti {
    
    public Mastro() {
    }
    
    public Mastro(Integer codiceMastro, String descrizione) {
        super(codiceMastro, new Integer(0), new Integer(0), new Integer(0), descrizione);
    }
    
    public String getTipo() {
        return MASTRO;
    }
    
    public DecimalFormat getFormatter() {
        return DF_MASTRO;
    }
    
    public MROldPianoDeiConti newLastSibling() {
        return new Mastro(
                new Integer(getCodiceMastro().intValue() + 1),
                MessageFormat.format("Nuovo {0}", MASTRO.toLowerCase()));
    }
    
    public MROldPianoDeiConti newFirstChild() {
        return new Conto(
                getCodiceMastro(),
                new Integer(1),
                MessageFormat.format("Nuovo {0}", CONTO.toLowerCase()));
    }    
}
