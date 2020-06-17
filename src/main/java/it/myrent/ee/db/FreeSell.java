package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;

import java.util.Date;
import java.util.ResourceBundle;
import java.util.Set;

public class FreeSell implements it.aessepi.utils.db.PersistentInstance,Loggable,Comparable<FreeSell>  {

    private Integer id;
    private Boolean isStopSell;
    private Date startDate;
    private Date endDate;
    private MROldGruppo carClass;
    private MROldSede location;

    private Set<MROldGruppo> multiGroup;
    private Set<MROldSede> multiLocation;

    private MROldFonteCommissione fonteCommissione;
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");


    public FreeSell() {
        setIsStopSell(false);
    }

    public FreeSell(Integer id, Boolean isStopSell, Date startDate, Date endDate, MROldGruppo carClass, MROldSede location) {
        this.id = id;
        this.isStopSell = isStopSell;
        this.startDate = startDate;
        this.endDate = endDate;
        this.carClass = carClass;
        this.location = location;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCarClass(MROldGruppo carClass) {
        this.carClass = carClass;
    }

    public MROldGruppo getCarClass() {
        return carClass;
    }

    public void setLocation(MROldSede location) {
        this.location = location;
    }

    public MROldSede getLocation() {
        return location;
    }

    public Boolean getIsStopSell() {
        return isStopSell;
    }

    public void setIsStopSell(Boolean isStopSell) {
        this.isStopSell = isStopSell;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public MROldFonteCommissione getFonteCommissione() {
        return fonteCommissione;
    }

    public void setFonteCommissione(MROldFonteCommissione fonteCommissione) {
        this.fonteCommissione = fonteCommissione;
    }

    public Set<MROldSede> getMultiLocation() {
        return multiLocation;
    }

    public void setMultiLocation(Set<MROldSede> multiLocation) {
        this.multiLocation = multiLocation;
    }

    public Set<MROldGruppo> getMultiGroup() {
        return multiGroup;
    }

    public void setMultiGroup(Set<MROldGruppo> multiGroup) {
        this.multiGroup = multiGroup;
    }

    public String[] getLoggableFields(){
        return new String[]{
                "isStopSell", // NOI18N
                "startDate", // NOI18N
                "endDate", // NOI18N
                "carClass", // NOI18N
                "location", // NOI18N
                "multiGroup", // NOI18N
                "multiLocation" // NOI18N
        };

    }
    public String[] getLoggableLabels(){
        return new String[]{
                bundle.getString("FreeSell.logIsStopSell"),
                bundle.getString("FreeSell.logStartDate"),
                bundle.getString("FreeSell.logEndDate"),
                bundle.getString("FreeSell.logCarClass"),
                bundle.getString("FreeSell.logLocation"),
                bundle.getString("FreeSell.logMultiGroup"),
                bundle.getString("FreeSell.logMultiLocation")
        };
    }
    public String getEntityName(){
        return "FreeSell";
    }
    public Integer getEntityId(){
        return getId();
    }

    public int compareTo(FreeSell o) {
        return getStartDate().compareTo(o.getStartDate());
    }
}
