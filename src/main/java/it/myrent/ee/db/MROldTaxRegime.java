package it.myrent.ee.db;

import it.aessepi.utils.BundleUtils;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by shivangani on 11/01/2019.
 */
 public class MROldTaxRegime implements it.aessepi.utils.db.PersistentInstance {
    private Integer id;

    private String description;
    private String codice;

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/db/Bundle");

    public MROldTaxRegime(){

    }

    public MROldTaxRegime(String description) {
        this.description = description;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

//    public static final MROldTaxRegime REGIME_ORDINARIO = new MROldTaxRegime("Regime ordinario");
//    public static final MROldTaxRegime REGIME_DEI_CONTRIBUENTI_MINIMI = new MROldTaxRegime("Regime dei contribuenti minimi");
//    public static final MROldTaxRegime REGIME_DELLE_NUOVE_INIZIATIVE_PRODUTTIVE = new MROldTaxRegime("Regime delle nuove iniziative produttive");
//    public static final MROldTaxRegime AGRICOLTURA_E_ATTIVITA_CONNESSE_E_PESCA = new MROldTaxRegime("Agricoltura e attivita connesse e pesca");
//    public static final MROldTaxRegime VENDITA_SALI_E_TABACCHI = new MROldTaxRegime("Vendita sali e tabacchi");
//    public static final MROldTaxRegime COMMERCIO_DEI_FIAMMIFERI = new MROldTaxRegime("Commercio dei fiammiferi");
//    public static final MROldTaxRegime EDITORIA = new MROldTaxRegime("Editoria");
//    public static final MROldTaxRegime GESTIONE_DI_SERVIZI_DI_TELEFONIA_PUBBLICA = new MROldTaxRegime("Gestione di servizi di telefonia pubblica");
//    public static final MROldTaxRegime RIVENDITA_DI_DOCUMENTI_DI_TRASPORTO_PUBBLICO_E_DI_SOSTA = new MROldTaxRegime("Rivendita di documenti di trasporto pubblico e di sosta");
//    public static final MROldTaxRegime INTRATTENIMENTI_GIOCHI_E_ALTRE_ATTIVITA_DI_CUI_ALLA_TARIFFA_ALLEGATA_AL_D_P_R = new MROldTaxRegime("Intrattenimenti, giochi e altre attivita di cui alla tariffa allegata al D.P.R. 640/72");
//    public static final MROldTaxRegime AGENZIE_DI_VIAGGI_E_TURISMO = new MROldTaxRegime("Agenzie di viaggi e turismo");
//    public static final MROldTaxRegime AGRITURISMO = new MROldTaxRegime("Agriturismo");
//    public static final MROldTaxRegime VENDITE_A_DOMICILIO = new MROldTaxRegime("Vendite a domicilio");
//    public static final MROldTaxRegime RIVENDITA_DI_BENI_USATI_DI_OGGETTI_DARTE_DANTIQUARIATO_O_DA_COLLEZIONE = new MROldTaxRegime("Rivendita di beni usati, di oggetti darte, dantiquariato o da collezione");
//    public static final MROldTaxRegime AGENZIE_DI_VENDITE_ALLASTA_DI_OGGETTI_DARTE_ANTIQUARIATO_O_DA_COLLEZIONE = new MROldTaxRegime("Agenzie di vendite allasta di oggetti darte, antiquariato o da collezione");
//    public static final MROldTaxRegime IVA_PER_CASSA_P_A = new MROldTaxRegime("IVA per cassa P.A.");
//    public static final MROldTaxRegime IVA_PER_CASSA = new MROldTaxRegime("IVA per cassa");
//    public static final MROldTaxRegime ALTRO = new MROldTaxRegime("Altro");
//    public static final MROldTaxRegime REGIME_FORFETTARIO = new MROldTaxRegime("Regime forfettario");
//
//    public static final List<MROldTaxRegime> TAX_REGIME_LIST = Arrays.asList(
//            REGIME_ORDINARIO,
//            REGIME_DEI_CONTRIBUENTI_MINIMI,
//            REGIME_DELLE_NUOVE_INIZIATIVE_PRODUTTIVE,
//            AGRICOLTURA_E_ATTIVITA_CONNESSE_E_PESCA,
//            VENDITA_SALI_E_TABACCHI,
//            COMMERCIO_DEI_FIAMMIFERI,
//            EDITORIA,
//            GESTIONE_DI_SERVIZI_DI_TELEFONIA_PUBBLICA,
//            RIVENDITA_DI_DOCUMENTI_DI_TRASPORTO_PUBBLICO_E_DI_SOSTA,
//            INTRATTENIMENTI_GIOCHI_E_ALTRE_ATTIVITA_DI_CUI_ALLA_TARIFFA_ALLEGATA_AL_D_P_R,
//            AGENZIE_DI_VIAGGI_E_TURISMO,
//            AGRITURISMO,
//            VENDITE_A_DOMICILIO,
//            RIVENDITA_DI_BENI_USATI_DI_OGGETTI_DARTE_DANTIQUARIATO_O_DA_COLLEZIONE,
//            AGENZIE_DI_VENDITE_ALLASTA_DI_OGGETTI_DARTE_ANTIQUARIATO_O_DA_COLLEZIONE,
//            IVA_PER_CASSA_P_A,
//            IVA_PER_CASSA,
//            ALTRO,
//            REGIME_FORFETTARIO
//    );



}

