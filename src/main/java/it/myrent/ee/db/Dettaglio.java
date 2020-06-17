package it.myrent.ee.db;

import java.text.DecimalFormat;

import java.text.MessageFormat;

/** @author Hibernate CodeGenerator */
public class Dettaglio extends MROldPianoDeiConti {
    
    public Dettaglio() {
    }
    
    public Dettaglio(Integer codiceMastro, Integer codiceConto, Integer codiceSottoconto, Integer codiceDettaglio, String descrizione) {
        super(codiceMastro, codiceConto, codiceSottoconto, codiceDettaglio, descrizione);
    }
    
    public String getTipo() {
        return DETTAGLIO;
    }
    
     public DecimalFormat getFormatter() {
        return DF_DETTAGLIO;
    }
     
     public MROldPianoDeiConti newLastSibling() {
        return new Dettaglio(
                getCodiceMastro(),
                getCodiceConto(),
                getCodiceSottoconto(),
                new Integer(getCodiceDettaglio().intValue() + 1),
                MessageFormat.format("Nuovo {1}", DETTAGLIO.toLowerCase()));
    }
}
