package it.myrent.ee.db;

/** @author Hibernate CodeGenerator */

public class MROldCapoAreaImpl extends UserImpl implements MROldCapoArea {
    
    public MROldCapoAreaImpl() {
        super();
    }
    
    public String getTipo() {
        return CAPO_AREA;
    }

    @Override
    public String getTipoInternazionalizzato() {
        return CAPO_AREA_I18N;
    }
    
    
}
