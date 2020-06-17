/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

import java.util.Set;

/**
 *
 * @author dogma_system
 */
public class AgreementWebCode implements it.aessepi.utils.db.PersistentInstance {

    private Integer id;
    private Agreement agreement;
    private String webCode;
    private Boolean isValid;
    //Madhvendra (for Pstgres version)
    private Set<MROldPrenotazione> prenotazione;
    private Set<MROldContrattoNoleggio> contratto;
    private Set<MROldPreventivo> preventivo;
    public Set<MROldContrattoNoleggio> getContratto() {
        return contratto;
    }

    public void setContratto(Set<MROldContrattoNoleggio> contratto) {
        this.contratto = contratto;
    }

    public Set<MROldPrenotazione> getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Set<MROldPrenotazione> prenotazione) {
        this.prenotazione = prenotazione;
    }

    public Set<MROldPreventivo> getPreventivo() {
        return preventivo;
    }

    public void setPreventivo(Set<MROldPreventivo> preventivo) {
        this.preventivo = preventivo;
    }
    //
    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public String getWebCode() {
        return webCode;
    }

    public void setWebCode(String webCode) {
        this.webCode = webCode;
    }


}
