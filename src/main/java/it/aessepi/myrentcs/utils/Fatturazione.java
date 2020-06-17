/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.aessepi.myrentcs.utils;

import it.myrent.ee.api.exception.FatturaVuotaException;
import it.myrent.ee.db.MROldDocumentoFiscale;
import it.myrent.ee.db.MROldRigaDocumentoFiscale;
import it.myrent.ee.db.MROldSede;
import java.util.Date;
import java.util.List;

import it.myrent.ee.db.Rent2Rent;
import org.hibernate.Session;

/**
 *
 * @author bogdan
 */
public interface Fatturazione {

    public Date getInizioPeriodo();
    public Date getFinePeriodo();
    public void setLocationPickUp(MROldSede locationPickUp);
    public Fatturazione setLocationPickUpAndReturnFatturazione(MROldSede locationPickUp);
    public MROldSede getLocationPickUp();
    public List<MROldRigaDocumentoFiscale> calcolaRigheProssimaFattura(Session sx) throws it.myrent.ee.api.exception.TariffaNonValidaException, it.myrent.ee.api.exception.FatturaVuotaException;
    public List<MROldRigaDocumentoFiscale> calcolaRigheProssimaFattura() throws it.myrent.ee.api.exception.BusinessRuleException, it.myrent.ee.api.exception.DatabaseException;
    public List<MROldRigaDocumentoFiscale> ricalcolaRigheUltimaFattura(Session sx, MROldDocumentoFiscale fattura) throws it.myrent.ee.api.exception.TariffaNonValidaException, it.myrent.ee.api.exception.FatturaVuotaException;
    public List<MROldRigaDocumentoFiscale> ricalcolaRigheUltimaFattura(MROldDocumentoFiscale fattura) throws it.myrent.ee.api.exception.BusinessRuleException, it.myrent.ee.api.exception.DatabaseException;
    public List<MROldRigaDocumentoFiscale> calcolaRigheFattura(Session sx);
    public List<MROldRigaDocumentoFiscale> calcolaRigheFattura();
    public List<MROldRigaDocumentoFiscale> anteprimaValoreFatture(Session sx) throws it.myrent.ee.api.exception.TariffaNonValidaException, it.myrent.ee.api.exception.FatturaVuotaException;
    public List<MROldRigaDocumentoFiscale> calculateRowsSplitPayment(Session sx);
    public List<MROldRigaDocumentoFiscale> calcolaRigheProssimaFatturaR2R(Session sx, Rent2Rent r2r, Date startDate, Date endDate) throws FatturaVuotaException;
}
