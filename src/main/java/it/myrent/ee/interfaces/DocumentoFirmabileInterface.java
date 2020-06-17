/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.interfaces;

import java.util.Date;

/**
 *
 * @author giacomo
 */
public interface DocumentoFirmabileInterface {

    public String getNomeFirmatario();

    public void setNomeFirmatario(String nome);

    public String getLuogoFirma();

    public void setLuogoFirma(String luogo);

    public String getAnnotazioniFirma();

    public void setAnnotazioniFirma(String annotazioni);

    public String getNomeUtenteCreatore();

    public void setNomeUtenteCreatore(String nomeUtente);

    public Date getDataFirmaCerta();

    public void setDataFirmaCerta(Date dataFirma);

    //accerta l'avvenuta corretta archiviazione sulla gestione documentale
    public Boolean getDocumentoFirmato();

    public void setDocumentoFirmato(Boolean firmato);
}
