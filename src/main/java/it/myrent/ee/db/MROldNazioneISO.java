/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.myrent.ee.db;

/**
 *
 * @author jamess
 */
public class MROldNazioneISO {
    private String nome;
    private String codice;
    private String codice3;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getCodice3() {
        return codice3;
    }

    public void setCodice3(String codice3) {
        this.codice3 = codice3;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
