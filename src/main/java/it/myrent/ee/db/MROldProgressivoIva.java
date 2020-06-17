/*
 * TabellaProgressiviIVA.java
 *
 * Created on February 27, 2004, 6:39 PM
 */

package it.myrent.ee.db;

/**
 *
 * @author  mauro
 */
public class MROldProgressivoIva {
    
    /** Creates a new instance of TabellaProgressiviIVA */
    public MROldProgressivoIva() {
    }
    
    private Integer id;
    private Integer tipo_registro_iva;
    private Integer n_registro_iva;
    private Integer tipo_documento;
    private Integer numero_protocollo_progressivo;
    
    
    
    
    private void setId(Integer id) {
        this.id = id;
    }
    public Integer getID()
    {
        return id;
        
    }
    
    public Integer getTipo_registro_iva()
    {
        return tipo_registro_iva;
        
    }
    public void setTipo_registro_iva(Integer tipo_registro_iva)
    {
        this.tipo_registro_iva=tipo_registro_iva;
    }
    
    public Integer getN_registro_iva()
    {
        return n_registro_iva;
        
    }
    public void setN_registro_iva(Integer n_registro_iva)
    {
        this.n_registro_iva=n_registro_iva;
    }
    
    public Integer getTipo_documento()
    {
        return tipo_documento;
        
    }
    public void setTipo_documento(Integer tipo_documento)
    {
        this.tipo_documento=tipo_documento;
    }
    
    public Integer getNumero_protocollo_progressivo()
    {
        return numero_protocollo_progressivo;
        
    }
    public void setNumero_protocollo_progressivo(Integer numero_protocollo_progressivo)
    {
        this.numero_protocollo_progressivo=numero_protocollo_progressivo;
    }
    
}
