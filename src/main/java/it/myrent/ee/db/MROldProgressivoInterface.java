package it.myrent.ee.db;

/**
 * Created by Shivangani on 11/14/2017.
 */
public interface MROldProgressivoInterface {

    public Integer getId();
    public String getPrefisso();
    public void setPrefisso(String prefisso);
    public Integer getAnno();
    public void setAnno(Integer anno);
    public void setNumero(Integer numero);
    public void setNumerazione(MROldNumerazione numerazione);
    public MROldNumerazione getNumerazione();
    public Integer getNumero();
}
