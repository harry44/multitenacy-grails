package it.myrent.ee.db;

/** @author Hibernate CodeGenerator */

public class OperatoreImpl extends UserImpl implements Operatore {
    
    public OperatoreImpl() {
        super();
    }
    
    public String getTipo() {
        return OPERATORE;
    }

    public String getTipoInternazionalizzato() {
        return OPERATORE_I18N;
    }
}
