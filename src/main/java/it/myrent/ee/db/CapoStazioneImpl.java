package it.myrent.ee.db;

/** @author Hibernate CodeGenerator */

public class CapoStazioneImpl extends UserImpl implements CapoStazione {
    
    public CapoStazioneImpl() {
        super();
    }
    
    public String getTipo() {
        return CAPO_STAZIONE;
    }

    @Override
    public String getTipoInternazionalizzato() {
        return CAPO_STAZIONE_I18N;
    }
    
    
    
}
