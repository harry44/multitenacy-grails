/*
 * Assicurazione.java
 *
 * Created on 12 noiembrie 2004, 11:00
 */
package it.myrent.ee.db;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import java.util.Date;
import it.aessepi.utils.beans.FormattedDate;

/**
 *
 * @author  jamess
 */
public class Assicurazione implements it.aessepi.utils.db.PersistentInstance {
    
    /** Creates a new instance of Assicurazione */
    public Assicurazione() {
    }
    private Integer id;
    private MROldParcoVeicoli veicolo;
    private Integer numeroRate;
    private Double importoAnnuale;
    private Double importoRca;
    private Double importoFurto;
    private Double importoIncendio;
    private Double importoKasco;
    private Double importoServizioSanitario;
    private Double importoAltro;
    private Double massimaleAnnuale;
    private Double massimaleRca;
    private Double massimaleFurto;
    private Double massimaleIncendio;
    private Double massimaleKasco;
    private Double massimaleServizioSanitario;
    private Double massimaleAltro;
    private Double importoScadenza1;
    private Double importoScadenza2;
    private Double importoScadenza3;
    private Double importoScadenza4;
    private Date dataInizio;
    private Date dataScadenza1;
    private Date dataScadenza2;
    private Date dataScadenza3;
    private Date dataScadenza4;
    private Boolean pagato1;
    private Boolean pagato2;
    private Boolean pagato3;
    private Boolean pagato4;
    private Date dataProssimaScadenza;
    private Double importoProssimaScadenza;
    private Double tasse;
    private String annotazioni;
    private MROldFornitori compagnia;
    private String numeroPolizza;
    private String classeMeritoAttuale;
    
    public Integer getId() {
        return id;
    }
    
    private void setId(Integer id) {
        this.id = id;
    }
    
    public void setVeicolo(MROldParcoVeicoli veicolo) {
        this.veicolo = veicolo;
    }
    
    public MROldParcoVeicoli getVeicolo() {
        return veicolo;
    }
    
    public void setNumeroRate(Integer numeroRate) {
        this.numeroRate = numeroRate;
    }
    
    public Integer getNumeroRate() {
        return numeroRate;
    }
    
    public void setImportoAltro(Double importoAltro) {
        this.importoAltro = importoAltro;
    }
    
    public Double getImportoAltro() {
        return importoAltro;
    }
    
    public void setImportoAnnuale(Double importoAnnuale) {
        this.importoAnnuale = importoAnnuale;
    }
    
    public Double getImportoAnnuale() {
        return importoAnnuale;
    }
    
    public void setImportoFurto(Double importoFurto) {
        this.importoFurto = importoFurto;
    }
    
    public Double getImportoFurto() {
        return importoFurto;
    }
    
    public void setImportoIncendio(Double importoIncendio) {
        this.importoIncendio = importoIncendio;
    }
    
    public Double getImportoIncendio() {
        return importoIncendio;
    }
    
    public void setImportoKasco(Double importoKasco) {
        this.importoKasco = importoKasco;
    }
    
    public Double getImportoKasco() {
        return importoKasco;
    }
    
    public void setImportoRca(Double importoRca) {
        this.importoRca = importoRca;
    }
    
    public Double getImportoRca() {
        return importoRca;
    }
    
    public void setImportoServizioSanitario(Double importoServizioSanitario) {
        this.importoServizioSanitario = importoServizioSanitario;
    }
    
    public Double getImportoServizioSanitario() {
        return importoServizioSanitario;
    }
    
    /*** massimali ***/
    public void setMassimaleAltro(Double massimaleAltro) {
        this.massimaleAltro = massimaleAltro;
    }
    
    public Double getMassimaleAltro() {
        return massimaleAltro;
    }
    
    public void setMassimaleAnnuale(Double massimaleAnnuale) {
        this.massimaleAnnuale = massimaleAnnuale;
    }
    
    public Double getMassimaleAnnuale() {
        return massimaleAnnuale;
    }
    
    public void setMassimaleFurto(Double massimaleFurto) {
        this.massimaleFurto = massimaleFurto;
    }
    
    public Double getMassimaleFurto() {
        return massimaleFurto;
    }
    
    public void setMassimaleIncendio(Double massimaleIncendio) {
        this.massimaleIncendio = massimaleIncendio;
    }
    
    public Double getMassimaleIncendio() {
        return massimaleIncendio;
    }
    
    public void setMassimaleKasco(Double massimaleKasco) {
        this.massimaleKasco = massimaleKasco;
    }
    
    public Double getMassimaleKasco() {
        return massimaleKasco;
    }
    
    public void setMassimaleRca(Double massimaleRca) {
        this.massimaleRca = massimaleRca;
    }
    
    public Double getMassimaleRca() {
        return massimaleRca;
    }
    
    public void setMassimaleServizioSanitario(Double massimaleServizioSanitario) {
        this.massimaleServizioSanitario = massimaleServizioSanitario;
    }
    
    public Double getMassimaleServizioSanitario() {
        return massimaleServizioSanitario;
    }
    
    /*** end massimali ***/
    public void setImportoScadenza1(Double importoScadenza1) {
        this.importoScadenza1 = importoScadenza1;
    }
    
    public Double getImportoScadenza1() {
        return importoScadenza1;
    }
    
    public void setImportoScadenza2(Double importoScadenza2) {
        this.importoScadenza2 = importoScadenza2;
    }
    
    public Double getImportoScadenza2() {
        return importoScadenza2;
    }
    
    public void setImportoScadenza3(Double importoScadenza3) {
        this.importoScadenza3 = importoScadenza3;
    }
    
    public Double getImportoScadenza3() {
        return importoScadenza3;
    }
    
    public void setImportoScadenza4(Double importoScadenza4) {
        this.importoScadenza4 = importoScadenza4;
    }
    
    public Double getImportoScadenza4() {
        return importoScadenza4;
    }
    
    public void setDataScadenza1(Date dataScadenza1) {
        this.dataScadenza1 = dataScadenza1;
    }
    
    public Date getDataScadenza1() {
        return dataScadenza1;
    }
    
    public void setDataScadenza2(Date dataScadenza2) {
        this.dataScadenza2 = dataScadenza2;
    }
    
    public Date getDataScadenza2() {
        return dataScadenza2;
    }
    
    public void setDataScadenza3(Date dataScadenza3) {
        this.dataScadenza3 = dataScadenza3;
    }
    
    public Date getDataScadenza3() {
        return dataScadenza3;
    }
    
    public void setDataScadenza4(Date dataScadenza4) {
        this.dataScadenza4 = dataScadenza4;
    }
    
    public Date getDataScadenza4() {
        return dataScadenza4;
    }
    
    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }
    
    public Date getDataInizio() {
        return dataInizio;
    }
    
    public void setTasse(Double tasse) {
        this.tasse = tasse;
    }
    
    public Double getTasse() {
        return tasse;
    }
    
    public void setAnnotazioni(String annotazioni) {
        this.annotazioni = annotazioni;
    }
    
    public String getAnnotazioni() {
        return annotazioni;
    }
    
    public String toString() {
        String returnValue = new String();
        if(veicolo != null && dataInizio != null) {
            returnValue = veicolo.getTarga() + ", " + FormattedDate.format(dataInizio); //NOI18N
        }
        return returnValue;
    }
    
    public boolean equals(Object other) {
        if (!(other instanceof Assicurazione)) {
            return false;
        }
        Assicurazione castOther = (Assicurazione) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
    
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }
    public static final int CORRENTE = 0;
    public static final int SCADUTA = 1;

    public Boolean getPagato1() {
        return pagato1;
    }

    public void setPagato1(Boolean pagato1) {
        this.pagato1 = pagato1;
    }

    public Boolean getPagato2() {
        return pagato2;
    }

    public void setPagato2(Boolean pagato2) {
        this.pagato2 = pagato2;
    }

    public Boolean getPagato3() {
        return pagato3;
    }

    public void setPagato3(Boolean pagato3) {
        this.pagato3 = pagato3;
    }

    public Boolean getPagato4() {
        return pagato4;
    }

    public void setPagato4(Boolean pagato4) {
        this.pagato4 = pagato4;
    }

    public Date getDataProssimaScadenza() {
        return dataProssimaScadenza;
    }

    public void setDataProssimaScadenza(Date dataProssimaScadenza) {
        this.dataProssimaScadenza = dataProssimaScadenza;
    }

    public Double getImportoProssimaScadenza() {
        return importoProssimaScadenza;
    }

    public void setImportoProssimaScadenza(Double importoProssimaScadenza) {
        this.importoProssimaScadenza = importoProssimaScadenza;
    }

    public MROldFornitori getCompagnia() {
        return compagnia;
    }

    public void setCompagnia(MROldFornitori compagnia) {
        this.compagnia = compagnia;
    }

    public String getNumeroPolizza() {
        return numeroPolizza;
    }

    public void setNumeroPolizza(String numeroPolizza) {
        this.numeroPolizza = numeroPolizza;
    }

    /**
     * @return the classeMeritoAttuale
     */
    public String getClasseMeritoAttuale() {
        return classeMeritoAttuale;
    }

    /**
     * @param classeMeritoAttuale the classeMeritoAttuale to set
     */
    public void setClasseMeritoAttuale(String classeMeritoAttuale) {
        this.classeMeritoAttuale = classeMeritoAttuale;
    }
}
