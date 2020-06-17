package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import org.apache.commons.lang.builder.CompareToBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/** @author Hibernate CodeGenerator */
public class MROldGruppo implements PersistentInstance, Comparable<MROldGruppo> {

    /** identifier field */
    private Integer id;
    private String codiceNazionale;
    private String codiceInternazionale;
    private String descrizione;
    private String webDescrizione;
    private Double franchigiaDanni;
    private Double franchigiaFurto;
    private Double franchigiaPerditaChiavi;
    private Boolean isFoto1Set;
    private Boolean speciale;
    private Boolean isDisabled;
    private byte[] foto1;
    private MacroClass macroClass;
    String wireframeImage;
    private Set groupListino;
    private Set<FreeSell> freeSell;
    public MROldGruppo() {
    }

    public MROldGruppo(Integer id, String codiceNazionale) {
        setId(id);
        setCodiceNazionale(codiceNazionale);
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Set getGroupListino() {
        return groupListino;
    }

    public void setGroupListino(Set groupListino) {
        this.groupListino = groupListino;
    }
    public String getWebDescrizione() {
        return webDescrizione;
    }

    public void setWebDescrizione(String webDescrizione) {
        this.webDescrizione = webDescrizione;
    }
    
    public String toString() {        
        return (getCodiceNazionale() != null ? getCodiceNazionale() : "") + //NOI18N
                " / " + //NOI18N
                (getCodiceInternazionale() != null ? getCodiceInternazionale() : "") + //NOI18N
                " / " + //NOI18N
                (getDescrizione() != null ? getDescrizione() : ""); //NOI18N
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldGruppo)) {
            return false;
        }
        MROldGruppo castOther = (MROldGruppo) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    @Override
    public int compareTo(MROldGruppo o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getCodiceNazionale(), o.getCodiceNazionale()).
                    append(getCodiceInternazionale(), o.getCodiceInternazionale()).
                    append(getDescrizione(), getDescrizione()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }

    public Image getFoto1Image() {
        Image retValue = null;
        if (getFoto1() != null) {
            try {
                retValue = new ImageIcon(getFoto1()).getImage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return retValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodiceNazionale() {
        return codiceNazionale;
    }

    public void setCodiceNazionale(String codiceNazionale) {
        this.codiceNazionale = codiceNazionale;
    }

    public String getCodiceInternazionale() {
        return codiceInternazionale;
    }

    public void setCodiceInternazionale(String codiceInternazionale) {
        this.codiceInternazionale = codiceInternazionale;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Double getFranchigiaDanni() {
        return franchigiaDanni;
    }

    public void setFranchigiaDanni(Double franchigiaDanni) {
        this.franchigiaDanni = franchigiaDanni;
    }

    public Double getFranchigiaFurto() {
        return franchigiaFurto;
    }

    public void setFranchigiaFurto(Double franchigiaFurto) {
        this.franchigiaFurto = franchigiaFurto;
    }

    public Double getFranchigiaPerditaChiavi() {
        return franchigiaPerditaChiavi;
    }

    public void setFranchigiaPerditaChiavi(Double franchigiaPerditaChiavi) {
        this.franchigiaPerditaChiavi = franchigiaPerditaChiavi;
    }

    public Boolean getIsFoto1Set() {
        return isFoto1Set;
    }

    public void setIsFoto1Set(Boolean isFoto1Set) {
        this.isFoto1Set = isFoto1Set;
    }

    public byte[] getFoto1() {
        return foto1;
    }

    public void setFoto1(byte[] foto1) {
        this.foto1 = foto1;
    }

    public void setSpeciale(Boolean speciale) {
        this.speciale = speciale;
    }

    public Boolean getSpeciale() {
        return speciale;
    }

    public void setMacroClass(MacroClass macroClass) {
        this.macroClass = macroClass;
    }

    public MacroClass getMacroClass() {
        return macroClass;
    }

    public String getWireframeImage() {
        return wireframeImage;
    }

    public void setWireframeImage(String wireframeImage) {
        this.wireframeImage = wireframeImage;
    }

    public Set<FreeSell> getFreeSell() {
        return freeSell;
    }

    public void setFreeSell(Set<FreeSell> freeSell) {
        this.freeSell = freeSell;
    }

}
