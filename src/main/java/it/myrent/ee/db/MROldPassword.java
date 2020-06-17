package it.myrent.ee.db;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shivangani on 7/30/2018.
 */
public class MROldPassword {

    private Long id;
    private User utente;
    private String password;
    private Date dataInserimento;
    private int giorniValidita;
    private Boolean ultima;

    public MROldPassword() {
    }

    public MROldPassword(Long id, User utente, String password, Date dataInserimento, int giorniValidita, Boolean ultima) {
        this.id = id;
        this.utente = utente;
        this.password = password;
        this.dataInserimento = dataInserimento;
        this.giorniValidita = giorniValidita;
        this.ultima = ultima;
    }

    public Long getId() {
        //return toIntExact(id);
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUtente() {
        return utente;
    }

    public void setUtente(User utente) {
        this.utente = utente;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDataInserimento() {
        return dataInserimento;
    }

    public void setDataInserimento(Date dataInserimento) {
        this.dataInserimento = dataInserimento;
    }

    public int getGiorniValidita() {
        return giorniValidita;
    }

    public void setGiorniValidita(int giorniValidita) {
        this.giorniValidita = giorniValidita;
    }

    public Date getDataScadenza() {
        if (giorniValidita == 0) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTime(dataInserimento);
        c.add(Calendar.DATE, giorniValidita);  // number of days to add

        return c.getTime();
    }

    public Boolean isPasswordValid() {
        if (giorniValidita == 0) {
            return true;
        }

        Calendar c = Calendar.getInstance();
        if (c.getTime().after(getDataScadenza())) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean getUltima() {
        return ultima;
    }

    public void setUltima(Boolean ultima) {
        this.ultima = ultima;
    }
}
