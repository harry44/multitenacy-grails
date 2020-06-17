package it.myrent.ee.db;

import java.text.DecimalFormat;

import java.text.MessageFormat;

/** @author Hibernate CodeGenerator */
public class MROldSottoconto extends MROldPianoDeiConti {
    
    public MROldSottoconto() {
    }
    
    public MROldSottoconto(Integer codiceMastro, Integer codiceConto, Integer codiceSottoconto, String descrizione) {
        super(codiceMastro, codiceConto, codiceSottoconto, new Integer(0), descrizione);
    }
    
    public String getTipo() {
        return SOTTOCONTO;
    }
    
    public DecimalFormat getFormatter() {
        return DF_SOTTOCONTO;
    }
    
    public MROldPianoDeiConti newLastSibling() {
        return new MROldSottoconto(
                getCodiceMastro(),
                getCodiceConto(),
                new Integer(getCodiceSottoconto().intValue() + 1),
                MessageFormat.format("Nuovo {0}", SOTTOCONTO.toLowerCase()));
    }
    
    public MROldPianoDeiConti newFirstChild() {
        return new Dettaglio(
                getCodiceMastro(),
                getCodiceConto(),
                getCodiceSottoconto(),
                new Integer(1),
                MessageFormat.format("Nuovo {0}", DETTAGLIO.toLowerCase()));
    }
}
