/*
 * CastellettoIva.java
 *
 * Created on 16 martie 2006, 15:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.myrent.ee.api.utils;


import it.myrent.ee.db.MROldCodiciIva;

/**
 *
 * @author jamess
 */
public class CastellettoIva {
    
    
    /** Creates a new instance of CastellettoIva */
    public CastellettoIva() {        
        setImponibile(new Double(0));
        setImposta(new Double(0));
    }
    
    public CastellettoIva(MROldCodiciIva codiceIva) {
        this();
        setCodiceIva(codiceIva);
    }
    
    private Double imponibile;
    private MROldCodiciIva codiceIva;
    private Double imposta;

    public Double getImponibile() {
        return imponibile;
    }

    public void setImponibile(Double imponibile) {
        this.imponibile = imponibile;
    }

    public MROldCodiciIva getCodiceIva() {
        return codiceIva;
    }

    public void setCodiceIva(MROldCodiciIva codiceIva) {
        this.codiceIva = codiceIva;
    }

    public Double getImposta() {
        return imposta;
    }

    public void setImposta(Double imposta) {
        this.imposta = imposta;
    }
    
}
