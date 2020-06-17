package it.myrent.ee.db;

/** @author Hibernate CodeGenerator */

public class ProfiloUtenteImpl extends UserImpl implements ProfiloUtente {
    
    public ProfiloUtenteImpl() {
        super();
    }
    
    public String getTipo() {
        return PROFILO_UTENTE;
    }

    @Override
    public String getTipoInternazionalizzato() {
        return PROFILO_UTENTE_I18N;
    }
    
}
