package it.myrent.ee.db;

/** @author Hibernate CodeGenerator */

public class AdministratorImpl extends UserImpl implements Administrator {
    
    public AdministratorImpl() {
        super();
    }
    
    public String getTipo() {
        return ADMINISTRATOR;
    }

    @Override
    public String getTipoInternazionalizzato() {
        return ADMINISTRATOR_I18N;
    }
    
}
