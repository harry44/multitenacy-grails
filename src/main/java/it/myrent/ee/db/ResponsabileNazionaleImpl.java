package it.myrent.ee.db;

/** @author Hibernate CodeGenerator */

public class ResponsabileNazionaleImpl extends UserImpl implements ResponsabileNazionale {
    
    public ResponsabileNazionaleImpl() {
        super();
    }
    
    public String getTipo() {
        return RESPONSABILE_NAZIONALE;
    }

    public String getTipoInternazionalizzato() {
        return RESPONSABILE_NAZIONALE_I18N;
    }
    
    
}
