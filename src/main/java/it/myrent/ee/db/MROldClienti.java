package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/** @author Hibernate CodeGenerator */
public class MROldClienti extends MROldBusinessPartner implements it.aessepi.utils.db.PersistentInstance, MROldContribuente,Documentable {

    public MROldClienti() {
        super();
        setCliente(true);
        setRaggruppaEffetti(true);
        setIsFotoSet(false);
    }
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");
    public static final String NORMAL = bundle.getString("Customer.normal");
    public static final String WORKSHOP = bundle.getString("Customer.workshop");
    public static final String INSURANCE_COMPANY = bundle.getString("Customer.insurance_company");

    /** Dati aggiuntivi cliente **/
    private byte[] foto;
    private Boolean isFotoSet;
    private Boolean raggruppaEffetti;
    private String barcode;
    private MROldFonteCommissione fonteCommissione;
       
    private Double sconto;

    private String documento;
    private String numeroDocumento;
    private String rilasciatoDa;
    private Date dataRilascio;
    private Date dataScadenza;
    private String categoriaPatente;

    private String eInvoiceCode;
    private String eInvoiceEmail;

    private String typeOfCustomer;

    private Set documenti;

    private Boolean informativaFirmata;
    private Date dataInformativa;
    //Madhvendra (for postgres version)
    private Set <MROldFonteCommissione> reservationClienti;
    private Map carteDiCredito;
    private Set<MROldSplitPayment> splitPayments;


    private Date releaseDate1;

    private Date expiryDate1;


    private String documentNumb1;
    private String licenseType;
    private String issueBy1;



    private String document1;

    public Map getCarteDiCredito() {
        return carteDiCredito;
    }

    public void setCarteDiCredito(Map carteDiCredito) {
        this.carteDiCredito = carteDiCredito;
    }
    public Set<MROldFonteCommissione> getReservationClienti() {
        return reservationClienti;
    }

    public void setReservationClienti(Set<MROldFonteCommissione> reservationClienti) {
        this.reservationClienti = reservationClienti;
    }
    //
    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE).append(getRagioneSociale()).append(getCognome()).append(getNome()).toString().trim();
    }

    public boolean equals(Object other) {
        if (!(other instanceof MROldClienti)) {
            return false;
        }
        MROldClienti castOther = (MROldClienti) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }
    
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setIsFotoSet(Boolean isFotoSet) {
        this.isFotoSet = isFotoSet;
    }

    public Boolean getIsFotoSet() {
        return isFotoSet;
    }

    public Image getFotoImage() {
        try {
            return new ImageIcon(getFoto()).getImage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Boolean getRaggruppaEffetti() {
        return raggruppaEffetti;
    }

    public void setRaggruppaEffetti(Boolean raggruppaEffetti) {
        this.raggruppaEffetti = raggruppaEffetti;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public MROldFonteCommissione getFonteCommissione() {
        return fonteCommissione;
    }

    public void setFonteCommissione(MROldFonteCommissione fonteCommissione) {
        this.fonteCommissione = fonteCommissione;
    }


    public Double getSconto() {
        return sconto;
    }

    public void setSconto(Double sconto) {
        this.sconto = sconto;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getRilasciatoDa() {
        return rilasciatoDa;
    }

    public void setRilasciatoDa(String rilasciatoDa) {
        this.rilasciatoDa = rilasciatoDa;
    }

    public Date getDataRilascio() {
        return dataRilascio;
    }

    public void setDataRilascio(Date dataRilascio) {
        this.dataRilascio = dataRilascio;
    }

    public Date getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(Date dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public String getCategoriaPatente() {
        return categoriaPatente;
    }

    public void setCategoriaPatente(String categoriaPatente) {
        this.categoriaPatente = categoriaPatente;
    }

    @Override
    public void setDocumenti(Set documenti) {
        this.documenti = documenti;
    }

    @Override
    public Set getDocumenti() {
        return documenti;
    }

    @Override
    public String getDocumentableName() {
         return new StringBuffer().append(getNominativo()).toString();
    }

    @Override
    public Class getDocumentableClass() {
        return MROldClienti.class;
    }

    @Override
    public String getKeywords() {
        return new StringBuffer().
                append(bundle.getString("Clienti.keywords")).
                append(" ").
                append(getNominativo()).
                toString();
    }

    public Boolean getInformativaFirmata() {
        return informativaFirmata;
    }

    public void setInformativaFirmata(Boolean informativaFirmata) {
        this.informativaFirmata = informativaFirmata;
    }

    public Date getDataInformativa() {
        return dataInformativa;
    }

    public void setDataInformativa(Date dataInformativa) {
        this.dataInformativa = dataInformativa;
    }

    public Set<MROldSplitPayment> getSplitPayments() {
        return splitPayments;
    }

    public void setSplitPayments(Set<MROldSplitPayment> splitPayments) {
        this.splitPayments = splitPayments;
    }

    public String getTypeOfCustomer() {
        return typeOfCustomer;
    }

    public void setTypeOfCustomer(String typeOfCustomer) {
        this.typeOfCustomer = typeOfCustomer;
    }

    public String geteInvoiceCode() {
        return eInvoiceCode;
    }

    public void seteInvoiceCode(String eInvoiceCode) {
        this.eInvoiceCode = eInvoiceCode;
    }

    public String geteInvoiceEmail() {
        return eInvoiceEmail;
    }

    public void seteInvoiceEmail(String eInvoiceEmail) {
        this.eInvoiceEmail = eInvoiceEmail;
    }

    public Date getReleaseDate1() {
        return releaseDate1;
    }

    public void setReleaseDate1(Date releaseDate1) {
        this.releaseDate1 = releaseDate1;
    }

    public Date getExpiryDate1() {
        return expiryDate1;
    }

    public void setExpiryDate1(Date expiryDate1) {
        this.expiryDate1 = expiryDate1;
    }

    public String getDocumentNumb1() {
        return documentNumb1;
    }

    public void setDocumentNumb1(String documentNumb1) {
        this.documentNumb1 = documentNumb1;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getIssueBy1() {
        return issueBy1;
    }

    public void setIssueBy1(String issueBy1) {
        this.issueBy1 = issueBy1;
    }
    public String getDocument1() {
        return document1;
    }

    public void setDocument1(String document1) {
        this.document1 = document1;
    }

    public boolean isInternalCustomer(){
        if (this.getTypeOfCustomer() == null)
            return false;
        else
            return this.getTypeOfCustomer().equalsIgnoreCase(this.WORKSHOP);
    }
}
