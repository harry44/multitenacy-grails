package it.myrent.ee.api.utils;

import com.dogmasystems.ebilling.*;
import com.dogmasystems.ebilling.ws.*;
import it.aessepi.utils.BundleUtils;
import it.myrent.ee.db.*;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by shivangani on 21/01/2019.
 */
public class EInvoicingUtils {

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");

    private static int mancatoInvio;
    public List<String> listaFattureXML;
    public List<String> invoiceIds;
    Session sx;
    public static Boolean sending;
    public static final String RF_01 = "Regime ordinario";
    public static final String RF_02 = "Regime dei contribuenti minimi";
    public static final String RF_03 = "Regime delle nuove iniziative produttive";
    public static final String RF_04 = "Agricoltura e attivit connesse e pesca";
    public static final String RF_05 = "Vendita sali e tabacchi";
    public static final String RF_06 = "Commercio dei fiammiferi";
    public static final String RF_07 = "Editoria";
    public static final String RF_08 = "Gestione di servizi di telefonia pubblica";
    public static final String RF_09 = "Rivendita di documenti di trasporto pubblico e di sosta";
    public static final String RF_10 = "Intrattenimenti, giochi e altre attivit di cui alla tariffa allegata al D.P.R. 640/72";
    public static final String RF_11 = "Agenzie di viaggi e turismo";
    public static final String RF_12 = "Agriturismo";
    public static final String RF_13 = "Vendite a domicilio";
    public static final String RF_14 = "Rivendita di beni usati, di oggetti darte, dantiquariato o da collezione";
    public static final String RF_15 = "Agenzie di vendite allasta di oggetti darte, antiquariato o da collezione";
    public static final String RF_16 = "IVA per cassa P.A.";
    public static final String RF_17 = "IVA per cassa";
    public static final String RF_18 = "Altro";
    public static final String RF_19 = "Regime forfettario";


    public static final String N_1 = "Escluse ex. art. 15";
    public static final String N_2 = "Non soggette";
    public static final String N_3 = "Non imponibili";
    public static final String N_4 = "Esenti";
    public static final String N_5 = "Regime del margine";
    public static final String N_6 = "Inversione contabile";
    public static final String N_7 = "IVA assolta in altro stato UE";

    //Esigibilita IVA
//    public static final String D = bundle.getString("Einvoice.differentLiability");
//    public static final String I = bundle.getString("Einvoice.defredImediate");
//    public static final String S = "Scissione dei pagamenti";

    public static final String D = "Esigibilita differita";
    public static final String I = "Esigibilita immediata";
    public static final String S = "Scissione dei pagamenti";

    public static final String COD_FT_0001 = "COD.FT.0001";
    public static final String COD_FT_0002 = "COD.FT.0002";

    //Paesi UE 24/01/2019
    public static final String[] paesiUE = new String[]{
            "AT", "BE", "BG", "HR", "CY", "CZ", "DK", "EE", "FI", "FR", "DE", "GR", "HU", "IE", "IT", "LV", "LT", "LU", "MT", "NL", "PL", "PT",
            "RO", "SK", "SI", "ES", "SE", "GB"};
    private String telefonoCP;

    public EInvoicingUtils(Session sx, List<String> invoiceIds, boolean sending) {
        this.sx = sx;
        this.invoiceIds = invoiceIds;
        this.mancatoInvio = 0;
        this.listaFattureXML = new ArrayList<String>();
        this.sending = sending;
    }

    public Map createOrSendInvoice() {
        List<MROldDocumentoFiscale> listaFattureDocumenti = new ArrayList();
        List<MROldDocumentoFiscale> listaFattureScartate = new ArrayList();
        int contatoreFatturazioniXMLfallite = 0;
        Iterator itr = invoiceIds.iterator();
        Map result = new HashMap();
        StringBuilder message = new StringBuilder();
        boolean primaFattura = true;
        while (itr.hasNext()) {
//        invoiceIds.stream().forEach(invoiceId->{
            MROldDocumentoFiscale invoice = (MROldDocumentoFiscale) sx.load(MROldDocumentoFiscale.class, Integer.parseInt((String) itr.next()));
            //HEADER
            result.putAll(EInvoicingUtils.validateInvoice(invoice, sx, primaFattura));
            if (result != null && !result.isEmpty()) {
                if (result.containsKey("tempContatoreFatturazioniXMLfallite")) {
                    contatoreFatturazioniXMLfallite += (int) result.get("tempContatoreFatturazioniXMLfallite");
                }
                if (result.containsKey("msg")) {
                    message.append(result.get("msg"));
                    continue;
                }
                if (result.containsKey("listaFattureScartate")) {
                    listaFattureScartate.addAll((List<MROldDocumentoFiscale>) result.get("listaFattureScartate"));
                }
                if (result.containsKey("listaFattureDocumenti")) {
                    listaFattureDocumenti.addAll((List<MROldDocumentoFiscale>) result.get("listaFattureDocumenti"));
                }
                if (result.containsKey("listaFattureXML")) {
                    listaFattureXML.addAll((List<String>) result.get("listaFattureXML"));
                }
            }
            if(primaFattura){
                primaFattura = false;
            }
        }
        try {

            List<String> listaNomiFileCreati = new ArrayList<>();
            int numeroFattureXMLFatte = listaFattureXML.size();
            if (numeroFattureXMLFatte == 0) {
                message.append("</br>");
                message.append(bundle.getString("AnagraficaFattura.msgNessunaFatturaCreata"));
                result.clear();
                result.put("msg", message);
                return result;
            } else {
                Map temp = saveXMLToFile(sx, listaFattureXML, listaFattureDocumenti, sending);
                if (!temp.isEmpty() ) {
                    result.clear();
                    if(temp.containsKey("msg") && !((String) temp.get("msg")).equalsIgnoreCase("Success")){
                        message.append("</br>");
                        message.append((String) temp.get("msg"));
                    }
                    if(temp.containsKey("listaNomiFile")){
                        listaNomiFileCreati.addAll((List) temp.get("listaNomiFile"));
                    }
                } else {
                    result.clear();
                    result.put("msg", "Something went wrong");
                    return result;
                }
                if (!sending) {
                    message.append("</br>");
                    message.append(bundle.getString("AnagraficaFattura.numeroFattureSelezionate") + " " + invoiceIds.size() + "\n" +
                            bundle.getString("AnagraficaFattura.numeroFattureXMLCreate") + " " + listaNomiFileCreati.size());
                } else {
                    message.append("</br>");
                    message.append(bundle.getString("AnagraficaFattura.numeroFattureSelezionate") + " " + invoiceIds.size() + "\n" +
                            bundle.getString("AnagraficaFattura.numeroFattureXMLCreate") + " " + listaNomiFileCreati.size() + "\n" +
                            bundle.getString("AnagraficaFattura.numeroFattureXMLFallite") + " " + contatoreFatturazioniXMLfallite + "\n" +
                            bundle.getString("AnagraficaFattura.numeroFattureInviateSDI") + " " + (listaNomiFileCreati.size() - mancatoInvio) + "\n" +
                            bundle.getString("AnagraficaFattura.numeroFattureMancatoInvio") + " " + mancatoInvio);

                }
                result.put("msg", message);
            }
            List listaResoconto = new ArrayList();
//            listaResoconto.add(listaFattureScartate);
            listaResoconto.addAll(listaNomiFileCreati);
            result.clear();
            result.put("msg", message);
            result.put("list", listaResoconto);
            return result;
        } catch (Exception ex) {
            result.clear();
            message.append("Something went wrong!");
            result.put("msg", message);
            return result;
        }
    }

    private static Map saveXMLToFile(Session sx, List<String> doc, List<MROldDocumentoFiscale> lista, boolean sending) throws IOException {

        final List<String> content = doc;
        List<String> listaNomiFile = new ArrayList<>();
        Map listMap = new HashMap();
        mancatoInvio = 0;
        String path = "";
        String xmlFolder = "";
        MROldDocumentoFiscale docu = new MROldDocumentoFiscale();
        MROldAffiliato a = new MROldAffiliato();
        docu = (MROldDocumentoFiscale) sx.load(MROldDocumentoFiscale.class, lista.get(0).getId());
        a = (MROldAffiliato) sx.load(MROldAffiliato.class, docu.getAffiliato().getId());
        if (!sending) {
            if (a.getPathSalvataggio() != null) {
                path = a.getPathSalvataggio() + File.separator;
            } else {
                listMap.put("msg", bundle.getString("AnagraficaFattura.pathNotSetForSavingXML"));
                return listMap;
            }
        } else {
            //fare in modo di salvare tutti i file per l'invio in una cartella del server che non viene vista da StampaClient
            String homeFolder = System.getProperty("user.home") + File.separator;
            new File(homeFolder + "fatturexmltmp").mkdirs();
            xmlFolder = homeFolder + "fatturexmltmp" + File.separator;
            path = xmlFolder;
        }
        //String filename = chooser.getSelectedFile().getName();
        int i = 0;
        for (String fattura : doc) {
            File file = null;
            String nomeFile = "";
            String nomeFileZip = "";
            String nomeDentroZip = "";
            String progressivo;
            MROldDocumentoFiscale d = new MROldDocumentoFiscale();
            MROldAffiliato affiliato = new MROldAffiliato();
            d = (MROldDocumentoFiscale) sx.load(MROldDocumentoFiscale.class, lista.get(i).getId());
            affiliato = (MROldAffiliato) sx.load(MROldAffiliato.class, d.getAffiliato().getId());
            if (affiliato.getProgressivo() == null) {
                progressivo = "00000";
            } else {
                progressivo = affiliato.getProgressivo();
            }
            if (affiliato.getCodiceFiscale() != null) {
                nomeFile = path + "IT" + affiliato.getCodiceFiscale() + "_" + progressivo + ".xml";
                nomeFileZip = path + "IT" + affiliato.getCodiceFiscale() + "_" + progressivo + ".zip";
                nomeDentroZip = "IT" + affiliato.getCodiceFiscale() + "_" + progressivo + ".xml";
            } else if (affiliato.getPartitaIva() != null) {
                nomeFile = path + "IT" + affiliato.getPartitaIva() + "_" + progressivo + ".xml";
                nomeFileZip = path + "IT" + affiliato.getPartitaIva() + "_" + progressivo + ".zip";
                nomeDentroZip = "IT" + affiliato.getPartitaIva() + "_" + progressivo + ".xml";
            }
//            file = new File(nomeFile);
            listMap.put(nomeDentroZip, nomeFile);
            listaNomiFile.add(nomeDentroZip);

            final Path paths = Paths.get(nomeFile);

            try (
                    final BufferedWriter writer = Files.newBufferedWriter(paths,
                            StandardCharsets.UTF_8, StandardOpenOption.CREATE);) {

                writer.write(fattura);
                writer.flush();
            }

            Boolean inviata = null;
            if (sending) {
                File f = new File(nomeFileZip);
                ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
                ZipEntry e = new ZipEntry(nomeDentroZip);
                out.putNextEntry(e);

                byte[] data = fattura.getBytes();
                out.write(data, 0, data.length);
                out.closeEntry();

                out.close();
                String tempRes = sendXMLInvoice(sx, f, d, affiliato);
                if (tempRes.equals("Success")) {
                    inviata = true;
                } else {
                    listMap.put("msg", tempRes);
                    inviata = false;
                }
            }


            try {
                d.setMotivoFatturazioneXMLFallita(progressivo);
                if (!sending) {
                    d.setFatturaXML("Creata");
                } else if (inviata != null && inviata) {
                    d.setFatturaXML("Inviata");
                } else if (inviata != null && !inviata) {
                    d.setFatturaXML("Mancato invio");
                    mancatoInvio++;
                }
                progressivo = Base36Converter.next(progressivo);
                affiliato.setProgressivo(progressivo);
                sx.saveOrUpdate(d);
                sx.saveOrUpdate(affiliato);
            } catch (Exception ex) {
//                ex.printStackTrace();
            } finally {
//                if (sx != null && sx.isOpen()) {
//                    sx.close();
//                }
            }
            i++;
        }
        if (!xmlFolder.isEmpty()) {
            org.apache.commons.io.FileUtils.deleteDirectory(new File(path));
        }
        listMap.put("listaNomiFile", listaNomiFile);
        return listMap;
    }

        private static String sendXMLInvoice(Session sx, File f, MROldDocumentoFiscale d, MROldAffiliato affiliato) throws MalformedURLException, IOException {
        String documentoNonInviato = "";
        String dataRicezioneSdi = "Non inviato";
        Long idSdi = null;
//        Map result = new HashMap();
        String result = "";
        URL url;
        if (affiliato.getInvioTest() != null && affiliato.getInvioTest()) {
            url = new URL("https://testfe.solutiondocondemand.com/solutiondoc_hub.asmx");
        } else {
            url = new URL("https://hubfe.solutiondocondemand.com/solutiondoc_hub.asmx");
        }
        SolutionDOCHub ws = new SolutionDOCHub(url);
        SolutionDOCHubSoap wsSoap = ws.getSolutionDOCHubSoap();
        byte[] password = null;
        String codiceCliente = null;
        String nomeFile = null;
        if (affiliato.getPasswordCodiceCliente() != null && affiliato.getCodiceCliente() != null) {
            password = affiliato.getPasswordCodiceCliente().getBytes();
            codiceCliente = affiliato.getCodiceCliente();
            nomeFile = wsSoap.getNomeFileZipFatturaPA(codiceCliente, password);
        } else {
            documentoNonInviato = d.getNumero().toString();
        }

        String uploadedFile = null;
        if (f != null && nomeFile != null) {
            Path path = Paths.get(f.getPath());
            byte[] fileContent = Files.readAllBytes(path);
            uploadedFile = wsSoap.uploadFileFatturaPA(codiceCliente, password, nomeFile, fileContent, 0);
        } else {
            documentoNonInviato = d.getNumero().toString();
        }

        Auth paramAuth = new Auth();
        if (codiceCliente != null && password != null) {
            paramAuth.setCustomerCode(codiceCliente);
            paramAuth.setPassword(password);
        }


        Info paramInfo = new Info();
        Email paramEmail = new Email();
        ArrayOfString customerEmail = new ArrayOfString();
        MROldClienti cliente = new MROldClienti();
        cliente = (MROldClienti) sx.load(MROldClienti.class, d.getCliente().getId());
        if (cliente.getEmail() != null) {
            customerEmail.getStrings().add(cliente.getEmail());
            paramEmail.setSend(true);
            paramEmail.setAddress(customerEmail);
        }

        SendInvoice paramInvoice = new SendInvoice();
        if (uploadedFile != null) {
            paramInvoice.setFilename(uploadedFile);
        } else {
            documentoNonInviato = d.getNumero().toString();
        }
        paramInvoice.setToSign(true);
        paramInvoice.setParamInfo(paramInfo);
        paramInvoice.setParamEmail(paramEmail);

        SendInvoiceResponse rispostaInvio = new SendInvoiceResponse();
        String errori = "";
        if (codiceCliente != null && password != null && uploadedFile != null) {
            rispostaInvio = wsSoap.sendElectronicInvoice(paramAuth, paramInvoice);
            List<String> listaErrori = new ArrayList();
            if (rispostaInvio.getResultMessage() != null) {
                listaErrori = rispostaInvio.getResultMessage().getStrings();
                for (int i = 0; i < listaErrori.size(); i++) {
                    errori = errori + listaErrori.get(i) + "\n";
                }
            }


            dataRicezioneSdi = "Non ricevuto";
            if (rispostaInvio.getDateSdi() != null) {
                XMLGregorianCalendar dataRic = rispostaInvio.getDateSdi();
                Calendar calendar = dataRic.toGregorianCalendar();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                formatter.setTimeZone(calendar.getTimeZone());
                dataRicezioneSdi = formatter.format(calendar.getTime());
                idSdi = rispostaInvio.getIdSdi();
            }

            List<String> invioEmail = new ArrayList();
            if (paramEmail.isSend()) {
                if (paramEmail.isSend() && rispostaInvio.getStatusEmail() != null) {
                    invioEmail = rispostaInvio.getStatusEmail();
                }
            }
        } else {
            documentoNonInviato = d.getNumero().toString();
        }

        try {
            d.setDataRicezioneSdi(dataRicezioneSdi);
            if (idSdi != null) {
                d.setIdSdi(idSdi);
            }
            sx.saveOrUpdate(d);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
//            if (sx != null && sx.isOpen()) {
//                sx.close();
//            }
        }


        if (errori.isEmpty() && documentoNonInviato.isEmpty()) {
            result = "Success";
            return result;
        } else {
            if (errori.isEmpty()) {
                String sb = bundle.getString("AnagraficaFattura.msgInvioFattureXML") + "\n" + bundle.getString("AnagraficaFattura.msgErroreInvioFattureXML1") + documentoNonInviato + " " +
                        bundle.getString("AnagraficaFattura.msgErroreInvioFattureXML2");
                result = sb;
            } else {
                result = errori;
            }
            return result;
        }
    }

    private static Map validateInvoice(MROldDocumentoFiscale invoice, Session sx, boolean primaFattura) {
        Map result = new HashMap();
        boolean isValidInvoice = false;
        MROldAffiliato affiliato = new MROldAffiliato();
        MROldClienti cliente = new MROldClienti();
        MROldContrattoNoleggio contratto = new MROldContrattoNoleggio();
        FatturaElettronicaHeaderType fatturaElettronicaHeader = new FatturaElettronicaHeaderType();
        List<MROldDocumentoFiscale> listaFattureDocumenti = new ArrayList();
        List<MROldDocumentoFiscale> listaFattureScartate = new ArrayList();
        List<String> listaFattureXML = new ArrayList();
        int tempContatoreFatturazioniXMLfallite = 0;
        String progressivoInvio = "";

        //Creazione Dati Trasmissione
        DatiTrasmissioneType datiTrasmissione = new DatiTrasmissioneType();
        if (invoice.getAffiliato() != null && invoice.getAffiliato().getId() != null) {
            affiliato = (MROldAffiliato) sx.get(MROldAffiliato.class, invoice.getAffiliato().getId());
        }
        if (invoice.getCliente() != null && invoice.getCliente().getId() != null) {
            cliente = (MROldClienti) sx.get(MROldClienti.class, invoice.getCliente().getId());
        }
        if (invoice.getContratto() != null) {
            contratto = (MROldContrattoNoleggio) sx.get(MROldContrattoNoleggio.class, invoice.getContratto().getId());
        }

        if (cliente == null || affiliato == null) {
            result.put("msg", bundle.getString("AnagraficaFattura.msgErroreAssenzaClienteAffiliato"));
            invoice.setFatturaXML("FALLITA");
            invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.erroreAssenzaClienteAffiliato"));
            sx.saveOrUpdate(invoice);
            tempContatoreFatturazioniXMLfallite++;
            result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
        }
        if (!result.isEmpty() && result.containsKey("msg")) {
            return result;
        }

        String idPaeseTx = null;
        String idCodiceTx = null;
        if (primaFattura) {
            if (affiliato.getProgressivo() == null) {
                progressivoInvio = "00000";
            } else {
                progressivoInvio = affiliato.getProgressivo();
            }
        }

        boolean isPa = false;
        if (cliente.getPubblicaAmministrazione() != null && cliente.getPubblicaAmministrazione()) {
            isPa = true;
        }

        if (affiliato.getIdPaeseTrasmittente() != null && affiliato.getIdCodiceTrasmittente() != null) {
            idPaeseTx = affiliato.getIdPaeseTrasmittente();
            idCodiceTx = affiliato.getIdCodiceTrasmittente();
        } else {
            idPaeseTx = "IT";
            idCodiceTx = "xxxxx";
        }

        String codiceDestinatario = null;
        if (cliente.geteInvoiceCode() != null) {
            codiceDestinatario = cliente.geteInvoiceCode().toUpperCase();
        } else if (cliente.getNazione() != null && !cliente.getNazione().equalsIgnoreCase("ITALIA")) {
            codiceDestinatario = "XXXXXXX";
        } else if (cliente.getNazione() != null && cliente.getNazione().equalsIgnoreCase("ITALIA")) {
            codiceDestinatario = "0000000";
        } else if (cliente.getNazione() == null) {
            result.put("msg", cliente.toString() + " " +  bundle.getString("AnagraficaFattura.msgMancaPaeseAffiliatoCliente"));
            invoice.setFatturaXML("FALLITA");
            invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.errorePaeseCliente"));
            sx.saveOrUpdate(invoice);
            tempContatoreFatturazioniXMLfallite++;
            result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
        }
        if (!result.isEmpty() && result.containsKey("msg")) {
            return result;
        }

        String telefonoTx = null;
        String emailTx = null;
        if (affiliato.getTelefonoTrasmittente() != null) {
            telefonoTx = affiliato.getTelefonoTrasmittente().replace(".", "").replace("+", "").replace(" ", "");
        }
        if (affiliato.getEmailTrasmittente() != null) {
            emailTx = affiliato.getEmailTrasmittente();
        }
        String pecDestinatario = null;
        if (codiceDestinatario != null && codiceDestinatario.equals("0000000")) {
            pecDestinatario = cliente.geteInvoiceEmail();
        }
        datiTrasmissione = XMLInvoiceUtils.createDatiTrasmissione(idPaeseTx, idCodiceTx, progressivoInvio, isPa, codiceDestinatario, telefonoTx,
                emailTx, pecDestinatario);

        //Creazione CedentePrestatore
        CedentePrestatoreType cedentePrestatore = new CedentePrestatoreType();
        result.putAll(EInvoicingUtils.validateAffiliateForInvoice(sx, invoice, affiliato));

        if (!result.isEmpty() && result.containsKey("msg")) {
            return result;
        }
        tempContatoreFatturazioniXMLfallite += (int) result.get("tempContatoreFatturazioniXMLfallite");
        String idPaeseCP = (String) result.get("idPaeseCP");
        String idPaeseCPTemp = (String) result.get("idPaeseCPTemp");
        String idCodiceCP = (String) result.get("idCodiceCP");
        String idCodiceFiscaleCP = (String) result.get("idCodiceFiscaleCP");
        int regimeFiscale = (int) result.get("regimeFiscale");

        String indirizzoViaCP = (String) result.get("indirizzoViaCP");
//        String indirizzoNumCivicoCP = (String) result.get("indirizzoNumCivicoCP");
        String capCP = (String) result.get("capCP");
        String comuneCP = (String) result.get("comuneCP");
        String provinciaCP = (String) result.get("provinciaCP");
        String nazioneCP = (String) result.get("nazioneCP");

        String denominazioneCP = (String) result.get("denominazioneCP");
        String nomeCP = (String) result.get("nomeCP");
        String cognomeCP = (String) result.get("cognomeCP");

        result.clear();
        //end validating affiliate
        String telefonoCP = null;
        String emailCP = null;
        if (affiliato.getTelefono1() != null) {
//            telefonoCP = affiliato.getTelefono1().replace(".", "");
            telefonoCP = affiliato.getTelefono1().replace(".", "").replace("+", "").replace(" ", "");
        }
        if (affiliato.getEmail() != null) {
            emailCP = affiliato.getEmail();
        }

        //Personalizzazione ALD Automotive
        String riferimentoAmministrativo = null;
        if (invoice.getRifAmministrativo() != null && invoice.getRifAmministrativo().equals(EInvoicingUtils.COD_FT_0001)) {
            riferimentoAmministrativo = EInvoicingUtils.COD_FT_0001;
        } else if (invoice.getRifAmministrativo() != null && invoice.getRifAmministrativo().equals(EInvoicingUtils.COD_FT_0002)) {
            riferimentoAmministrativo = EInvoicingUtils.COD_FT_0002;
        }

        if (regimeFiscale == -1) {
            result.put("msg", invoice.toString() + " with " + affiliato.toString() + " " + bundle.getString("AnagraficaFattura.msgAssenzaRegimeFiscaleAffiliato"));
            return result;
        }
        //Creating grantor
        cedentePrestatore = XMLInvoiceUtils.createCedentePrestatore(idPaeseCP, idCodiceCP, idCodiceFiscaleCP, denominazioneCP, nomeCP, cognomeCP, regimeFiscale,
                indirizzoViaCP, null, capCP, comuneCP, provinciaCP, nazioneCP, emailCP, telefonoCP, riferimentoAmministrativo);

        //Creazione CessionarioCommittente


        result.putAll(EInvoicingUtils.validateClientForInvoice(sx, invoice, cliente));

        if (!result.isEmpty() && result.containsKey("msg")) {
            return result;
        }
        String idPaeseCC = (String) result.get("idPaeseCC");
        String idPaeseCCTemp = (String) result.get("idPaeseCCTemp");
        String idCodiceCC = (String) result.get("idCodiceCC");
        String idCodiceFiscaleCC = (String) result.get("idCodiceFiscaleCC");
        String denominazioneCC = (String) result.get("denominazioneCC");
        String nomeCC = (String) result.get("nomeCC");
        String cognomeCC = (String) result.get("cognomeCC");
        tempContatoreFatturazioniXMLfallite += (int) result.get("tempContatoreFatturazioniXMLfallite");
        String indirizzoViaCC = (String) result.get("indirizzoViaCC");
//        String indirizzoNumCivicoCC = (String) result.get("indirizzoNumCivicoCC");
        String capCC = (String) result.get("capCC");
        String comuneCC = (String) result.get("comuneCC");
        String provinciaCC = (String) result.get("provinciaCC");
        String nazioneCC = (String) result.get("nazioneCC");

        result.clear();


        CessionarioCommittenteType cessionarioCommittente = XMLInvoiceUtils.createCessionarioCommittente(idPaeseCC, idCodiceCC, idCodiceFiscaleCC, denominazioneCC,
                nomeCC, cognomeCC, indirizzoViaCC, null, capCC, comuneCC, provinciaCC, nazioneCC);

        //New
        //Creazione Terzo Intermediario
        TerzoIntermediarioSoggettoEmittenteType terzoIntermediario = null;

        String idPaeseTI = null;
        String idCodiceTI = null;
        String denominazioneTI = null;

        if (affiliato.getIdPaeseTerzoIntermediario() != null) {
            idPaeseTI = affiliato.getIdPaeseTerzoIntermediario();
        }

        if (affiliato.getIdCodiceTerzoIntermediario() != null) {
            idCodiceTI = affiliato.getIdCodiceTerzoIntermediario();
        }

        if (affiliato.getNomeTerzoIntermediario() != null) {
            denominazioneTI = affiliato.getNomeTerzoIntermediario();
        }

        if (idPaeseTI != null && idCodiceTI != null && denominazioneTI != null) {
            terzoIntermediario = XMLInvoiceUtils.createTerzoIntermediario(idPaeseTI, idCodiceTI, denominazioneTI);
        }

        fatturaElettronicaHeader = XMLInvoiceUtils.createFatturaElettronicaHeader(datiTrasmissione, cedentePrestatore, cessionarioCommittente, terzoIntermediario);

        //FINE HEADER
        //BODY
        List<FatturaElettronicaBodyType> fatturaElettronicaBody = new ArrayList<FatturaElettronicaBodyType>();
        //DATI GENERALI
        DatiGeneraliType datiGenerali = new DatiGeneraliType();
        //Dati GeneraliDocumento
        DatiGeneraliDocumentoType datiGeneraliDocumento = new DatiGeneraliDocumentoType();
        int tipoDocumento = -1;
        if (invoice.getDiscriminator().equals("Fattura")) {
            tipoDocumento = XMLInvoiceUtils.TD_01;
        } else if (invoice.getDiscriminator().equals("NotaCredito")) {
            tipoDocumento = XMLInvoiceUtils.TD_04;
        } else if (invoice.getDiscriminator().equals("FatturaAcconto")) {
            tipoDocumento = XMLInvoiceUtils.TD_02;
        }
        if (tipoDocumento == -1) {
            result.put("msg", invoice.getPrefisso() + invoice.getNumero() + " " + invoice.getData() + " " + bundle.getString("AnagraficaFattura.msgErroreTipoDocumento"));
            invoice.setFatturaXML("FALLITA");
            invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.erroreTipoDocumento"));
            sx.saveOrUpdate(invoice);
            tempContatoreFatturazioniXMLfallite++;
            listaFattureScartate.add(invoice);
            result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
            return result;
        }
        String divisa = "EUR";
        Date data = null;
        if (invoice.getDataEffettuazione() != null) {
            data = invoice.getDataEffettuazione();
        } else {
            data = invoice.getData();
        }
        String numero = null;
        if (invoice.getPrefisso() != null) {
            numero = invoice.getPrefisso();
        }
        if (invoice.getNumero() != null) {
            if (numero != null) {
                numero += invoice.getNumero().toString();
            } else {
                numero = invoice.getNumero().toString();
            }
        }

        boolean bollo = false;
        if (invoice.getBollo() != null && invoice.getBollo()) {
            bollo = true;
        }

        Double totaleDocumento = invoice.getTotaleFattura();

        datiGeneraliDocumento = XMLInvoiceUtils.createDatiGeneraliDocumento(tipoDocumento, divisa, data, numero, totaleDocumento, bollo);

        //Dati Contratto
        List<DatiDocumentiCorrelatiType> listaDatiContratto = new ArrayList<DatiDocumentiCorrelatiType>();
        String idDocumento = null;
        Date dataContratto = null;
        String codiceCIGcontratto = null;
        if (contratto != null) {
            if (contratto.getPrefisso() != null) {
                idDocumento = contratto.getPrefisso() + " ";
            }
            if (contratto.getNumero() != null) {
                if (idDocumento == null) {
                    idDocumento = contratto.getNumero().toString();
                } else {
                    idDocumento += contratto.getNumero().toString();
                }
            }

            if (idDocumento == null || idDocumento.isEmpty()) {
                if (invoice.getNumContrattoLibero() != null) {
                    idDocumento = invoice.getNumContrattoLibero();
                }
            }

            if (contratto.getData() != null) {
                dataContratto = contratto.getData();
            } else if (invoice.getDataContrattoLibero() != null) {
                dataContratto = invoice.getDataContrattoLibero();
            }

            if (invoice.getCodiceCIG() != null) {
                codiceCIGcontratto = invoice.getCodiceCIG();
            }

            if (dataContratto != null && idDocumento != null) {
                listaDatiContratto = XMLInvoiceUtils.createDatiContratto(listaDatiContratto, idDocumento, null, dataContratto, null, codiceCIGcontratto);
            }
        }

        //Dati Ordine Acquisto
        List<DatiDocumentiCorrelatiType> listaDatiOrdineAcquisto = null;

        String codiceCIGPO = null; //messo solo in caso di futura richiesta CIG in Ordine Acquisto
        if (invoice.getRifAmministrativo() != null && ((String) invoice.getRifAmministrativo()).equals(EInvoicingUtils.COD_FT_0001)) {
            listaDatiOrdineAcquisto = new ArrayList<>();
            idDocumento = invoice.getOrdineAcquistoMeccanica();
            if (idDocumento != null) {
               // listaDatiOrdineAcquisto = XMLInvoiceUtils.createDatiOrdineAcquisto(listaDatiOrdineAcquisto, idDocumento, null, codiceCIGPO, null);
            }
        } else if (invoice.getCodiceCIGPO() != null && invoice.getNumPurchaseOrder() != null && invoice.getDatePurchaseOrder() != null) {
            listaDatiOrdineAcquisto = new ArrayList<>();
            codiceCIGPO = invoice.getCodiceCIGPO();
           // listaDatiOrdineAcquisto = XMLInvoiceUtils.createDatiOrdineAcquisto(listaDatiOrdineAcquisto, invoice.getNumPurchaseOrder(), null, codiceCIGPO, invoice.getDatePurchaseOrder());

        }

        //Dati OrdineAcquisto, Dati Trasporto non fatti
        datiGenerali = XMLInvoiceUtils.createDatiGenerali(datiGeneraliDocumento, listaDatiOrdineAcquisto, listaDatiContratto, null);
        //Dati BeniServizi
        DatiBeniServiziType datiBeniServizi = new DatiBeniServiziType();

        //DatiRiepilogo Summary Data
        result.putAll(EInvoicingUtils.getSummaryData(sx, invoice));
        if (!result.isEmpty() && result.containsKey("msg")) {
            return result;
        }
        boolean isSplitPayment = (result.get("aliquotaIVAperRigaDescrittiva")=="Y");
        List<DatiRiepilogoType> datiRiepilogo = (List<DatiRiepilogoType>) result.get("datiRiepilogo");
        tempContatoreFatturazioniXMLfallite += (int) result.get("tempContatoreFatturazioniXMLfallite");
        Double aliquotaIVAperRigaDescrittiva = (Double) result.get("aliquotaIVAperRigaDescrittiva");
        result.clear();
        //DettaglioLinee line details

        result.putAll(EInvoicingUtils.getInvoiceLine(sx, invoice, contratto, aliquotaIVAperRigaDescrittiva));
        if (!result.isEmpty() && result.containsKey("msg")) {
            return result;
        }
        List<DettaglioLineeType> dettaglioLinee = (List<DettaglioLineeType>) result.get("dettaglioLinee");
        result.clear();
        datiBeniServizi = XMLInvoiceUtils.createDatiBeniServizi(dettaglioLinee, datiRiepilogo);

        //Dati Pagamento Payment data
        result.putAll(EInvoicingUtils.getPaymentData(sx, invoice, affiliato, isSplitPayment));
        if (!result.isEmpty() && result.containsKey("msg")) {
            return result;
        }
        tempContatoreFatturazioniXMLfallite += (int) result.get("tempContatoreFatturazioniXMLfallite");
        List<DatiPagamentoType> datiPagamento = (List<DatiPagamentoType>) result.get("datiPagamento");

        result.clear();
        fatturaElettronicaBody = XMLInvoiceUtils.createFatturaElettronicaBody(fatturaElettronicaBody, 0, datiGenerali,
                datiBeniServizi, datiPagamento);
        //FINE BODY
        FatturaElettronicaType fatturaElettronica = new FatturaElettronicaType();
        fatturaElettronica = XMLInvoiceUtils.createXMLInvoice(fatturaElettronicaHeader, fatturaElettronicaBody);

        String fatturaXML = XMLInvoiceUtils.invoiceToXML(fatturaElettronica);
        if (isPa) {
            fatturaXML = fatturaXML.replace("FPR12", "FPA12");
        }

        fatturaXML = fatturaXML.replaceAll(bundle.getString("latin.char.c.cross"), " ")
                .replace( bundle.getString("latin.char.o.small"), "&#176;").replace( bundle.getString("latin.char.backward"), "&#171;").replace( bundle.getString("latin.char.forward"), "&#187;").replace( bundle.getString("latin.char.pound"), "&#163;")
                .replace(bundle.getString("latin.char.o.cross"), "&#248;").replace(bundle.getString("latin.char.zero.cross") , "&#216;").replace(bundle.getString("latin.small.a.grave"), "&#224;").replace(bundle.getString("latin.small.a.acute"), "&#225;").replace(bundle.getString("latin.small.a.circumflex"), "&#226;")
                .replace(bundle.getString("latin.small.a.tilde"), "&#227;").replace(bundle.getString("latin.small.a.diaeresis"), "&#228;").replace(bundle.getString("latin.small.a.ringAbove"), "&#229;").replace(bundle.getString("latin.small.ae"), "&#230;").replace(bundle.getString("latin.small.c.cedilla"), "&#231;")
                .replace(bundle.getString("latin.small.e.gravel"), "&#232;").replace(bundle.getString("latin.small.e.acute"), "&#233;").replace(bundle.getString("latin.small.e.circumflex"), "&#234;").replace(bundle.getString("latin.small.e.diaeresis"), "&#235;").replace(bundle.getString("latin.small.i.grave"), "&#236;")
                .replace(bundle.getString("latin.small.i.acute"), "&#237;").replace(bundle.getString("latin.small.i.circumflex"), "&#238;").replace(bundle.getString("latin.small.i.diaeresis"), "&#239;").replace(bundle.getString("latin.small.n.tilde"), "&#241;").replace(bundle.getString("latin.small.o.grave"), "&#242;")
                .replace(bundle.getString("latin.small.o.acute"), "&#243;").replace(bundle.getString("latin.small.o.circumflex"), "&#244;").replace(bundle.getString("latin.small.o.tilde"), "&#245;").replace(bundle.getString("latin.small.o.diaeresis"), "&#246;").replace(bundle.getString("latin.small.u.grave"), "&#249;")
                .replace(bundle.getString("latin.small.u.acute"), "&#250;").replace(bundle.getString("latin.small.u.circumflex"), "&#251;").replace(bundle.getString("latin.small.u.diaeresis"), "&#252;").replace(bundle.getString("latin.small.y.diaeresis"), "&#255;").replace(bundle.getString("latin.big.a.acute"), "&#193;")
                .replace(bundle.getString("latin.big.a.grave"), "&#192;").replace(bundle.getString("latin.big.a.circumflex"), "&#194;").replace(bundle.getString("latin.big.a.ring"), "&#197;").replace(bundle.getString("latin.big.a.tilde") , "&#195;").replace(bundle.getString("latin.big.a.diaeresis"), "&#196;")
                .replace(bundle.getString("latin.big.ae"), "&#198;").replace(bundle.getString("latin.big.c.cedilla"), "&#199;").replace(bundle.getString("latin.big.e.grave"), "&#200;").replace(bundle.getString("latin.big.e.acute"), "&#201;").replace(bundle.getString("latin.big.e.circumflex"), "&#202;")
                .replace(bundle.getString("latin.big.e.diaeresis"), "&#203;").replace(bundle.getString("latin.big.i.grave"), "&#204;").replace(bundle.getString("latin.big.i.acute"), "&#205;").replace(bundle.getString("latin.big.i.circumflex"), "&#206;").replace(bundle.getString("latin.big.i.diaeresis"), "&#207;")
                .replace(bundle.getString("latin.big.n.tilde"), "&#209;").replace(bundle.getString("latin.big.o.grave"), "&#210;").replace(bundle.getString("latin.big.o.acute"), "&#211;").replace(bundle.getString("latin.big.o.circumflex"), "&#212;").replace(bundle.getString("latin.big.o.tilde"), "&#213;")
                .replace(bundle.getString("latin.big.o.diaeresis"), "&#214;").replace(bundle.getString("latin.big.u.grave"), "&#217;").replace(bundle.getString("latin.big.u.acute"), "&#218;").replace(bundle.getString("latin.big.u.circumflex"), "&#219;").replace(bundle.getString("latin.big.u.diaeresis"), "&#220;")
                .replace(bundle.getString("latin.char.sharp.s"), "&#223;").replace(bundle.getString("latin.char.euro"), "&#8364;")
                .replace(bundle.getString("latin.left.square.bracket"), " ").replace(bundle.getString("latin.right.square.bracket"), " ").replace(bundle.getString("latin.left.curly.bracket") , " ").replace(bundle.getString("latin.right.curly.bracket"), " ").replace(bundle.getString("latin.char.trademark"), "&Trade")
                .replace(bundle.getString("latin.char.plus"), " ");
//
        fatturaXML = fatturaXML.replaceAll("versione=\"\"", "");
        listaFattureXML.add(fatturaXML);
        listaFattureDocumenti.add(invoice);

        result.put("listaFattureScartate", listaFattureScartate);
        result.put("listaFattureDocumenti", listaFattureDocumenti);
        result.put("listaFattureXML", listaFattureXML);
        result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);

        return result;
    }

    private static Map validateAffiliateForInvoice(Session sx, MROldDocumentoFiscale invoice, MROldAffiliato affiliato) {
        String idPaeseCP = null;
        String idPaeseCPTemp = null;
        String idCodiceCP = null;
        String idCodiceFiscaleCP = null;
        int tempContatoreFatturazioniXMLfallite = 0;
        String indirizzoViaCP = "";
        //String indirizzoNumCivicoCP = "";
        String capCP = "";
        String comuneCP = "";
        String provinciaCP = null;
        String nazioneCP = "";

        Map result = new HashMap();

        if (affiliato.getNazione() != null) {
            try {
                String queryString = "SELECT n FROM MROldNazioneISO n WHERE n.nome = :nazione";
                Query query = sx.createQuery(queryString);
                query.setParameter("nazione", affiliato.getNazione().toUpperCase());
                List<MROldNazioneISO> nazione = new ArrayList();
                nazione = query.list();
                idPaeseCPTemp = nazione.get(0).getCodice();
            } catch (Exception ex) {
//                ex.printStackTrace();
            } finally {
//                if (sx != null && sx.isOpen()) {
//                    sx.close();
//                }
            }
        }

        if (affiliato.getPartitaIva() != null) { //Se partita IVA assente del CedentePrestatore allora associazione e quindi prendo solo il Codice Fiscale
            if (idPaeseCPTemp != null) {
                idPaeseCP = idPaeseCPTemp;
            } else {
//                    JMessage.showMessage(this, true, bundle.getString("JAnagraficaFattura.msgCreazioneFattureXML"),
//                            affiliato.toString() + " " + bundle.getString("JAnagraficaFattura.msgErrorePaeseAffiliatoCliente"), 7, true);
                result.put("msg", invoice.toString() + " with " + affiliato.toString() + " " + bundle.getString("AnagraficaFattura.msgErrorePaeseAffiliatoCliente"));
                invoice.setFatturaXML("FALLITA");
                invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.errorePaeseAffiliato"));
                sx.saveOrUpdate(invoice);
                tempContatoreFatturazioniXMLfallite++;
//                listaFattureScartate.add(invoice);
                //continue;
                result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
                return result;
            }
            if (affiliato.getPartitaIva() != null) {
                idCodiceCP = affiliato.getPartitaIva();
            } else {
                result.put("msg", invoice.toString() + " with " + affiliato.toString() + " " + bundle.getString("AnagraficaFattura.msgErroreCodiceFiscaleAffiliatoCliente"));
                invoice.setFatturaXML("FALLITA");
                invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.erroreCodiceFiscaleAffiliato"));
                sx.saveOrUpdate(invoice);
                tempContatoreFatturazioniXMLfallite++;
//                listaFattureScartate.add(invoice);
                result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
                return result;
            }
        } else if (affiliato.getCodiceFiscale() != null) {
            idCodiceFiscaleCP = affiliato.getCodiceFiscale();
        } else {
            result.put("msg", invoice.toString() + " with " + affiliato.toString() + " " + bundle.getString("AnagraficaFattura.msgErroreCodiceFiscaleAffiliatoCliente"));
            invoice.setFatturaXML("FALLITA");
            invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.erroreCodiceFiscaleAffiliato"));
            sx.saveOrUpdate(invoice);
            tempContatoreFatturazioniXMLfallite++;
//            listaFattureScartate.add(invoice);
            result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
            return result;
        }

        String denominazioneCP = null;
        if (affiliato.getRagioneSociale() == null || affiliato.getRagioneSociale().isEmpty()) {
            result.put("msg", invoice.toString() + " with " + affiliato.toString() + " " + bundle.getString("AnagraficaFattura.msgAssenzaRagioneSocialeAffiliato"));
            invoice.setFatturaXML("FALLITA");
            invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.assenzaRagioneSocialeAffiliato"));
            sx.saveOrUpdate(invoice);
            tempContatoreFatturazioniXMLfallite++;
//            listaFattureScartate.add(invoice);
            result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
            return result;
        } else {
            denominazioneCP = affiliato.getRagioneSociale();
        }

            /*nome e cognome del cedente prestatore non sono presenti sul nostro database, sostituiti da ragione sociale*/
        String nomeCP = null;
        String cognomeCP = null;

        int regimeFiscale = -1;
        if (affiliato.getRegimeFiscale() != null) {
            String regFiscale = affiliato.getRegimeFiscale();
            regFiscale = regFiscale.replaceAll("[^\\p{ASCII}]", "");
//            if(regFiscale.equals(RF_10)){
//                regimeFiscale = XMLInvoiceUtils.RF_10;
//            } else if(regFiscale.equals(RF_14)){
//                regimeFiscale = XMLInvoiceUtils.RF_14;
//            } else if(regFiscale.replace(bundle.getString("latin.small.l.caron"), "l").re.equals(RF_15)){
//                regimeFiscale = XMLInvoiceUtils.RF_15;
//            } else if(regFiscale.equals(RF_04)){
//                regimeFiscale = XMLInvoiceUtils.RF_04;
//            } else {
                switch (regFiscale) {
                    case RF_01:
                        regimeFiscale = XMLInvoiceUtils.RF_01;
                        break;
                    case RF_02:
                        regimeFiscale = XMLInvoiceUtils.RF_02;
                        break;
                    case RF_03:
                        regimeFiscale = XMLInvoiceUtils.RF_03;
                        break;
                    case RF_04:
                        regimeFiscale = XMLInvoiceUtils.RF_04;
                        break;
                    case RF_05:
                        regimeFiscale = XMLInvoiceUtils.RF_05;
                        break;
                    case RF_06:
                        regimeFiscale = XMLInvoiceUtils.RF_06;
                        break;
                    case RF_07:
                        regimeFiscale = XMLInvoiceUtils.RF_07;
                        break;
                    case RF_08:
                        regimeFiscale = XMLInvoiceUtils.RF_08;
                        break;
                    case RF_09:
                        regimeFiscale = XMLInvoiceUtils.RF_09;
                        break;
                    case RF_10:
                        regimeFiscale = XMLInvoiceUtils.RF_10;
                        break;
                    case RF_11:
                        regimeFiscale = XMLInvoiceUtils.RF_11;
                        break;
                    case RF_12:
                        regimeFiscale = XMLInvoiceUtils.RF_12;
                        break;
                    case RF_13:
                        regimeFiscale = XMLInvoiceUtils.RF_13;
                        break;
                    case RF_14:
                        regimeFiscale = XMLInvoiceUtils.RF_14;
                        break;
                    case RF_15:
                        regimeFiscale = XMLInvoiceUtils.RF_15;
                        break;
                    case RF_16:
                        regimeFiscale = XMLInvoiceUtils.RF_16;
                        break;
                    case RF_17:
                        regimeFiscale = XMLInvoiceUtils.RF_17;
                        break;
                    case RF_18:
                        regimeFiscale = XMLInvoiceUtils.RF_18;
                        break;
                    case RF_19:
                        regimeFiscale = XMLInvoiceUtils.RF_19;
                        break;
                }

        } else {

            result.put("msg", invoice.toString() + " with " + affiliato.toString() + " " + bundle.getString("AnagraficaFattura.msgAssenzaRegimeFiscaleAffiliato"));
            invoice.setFatturaXML("FALLITA");
            invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.assenzaRegimeFiscaleAffiliato"));
            sx.saveOrUpdate(invoice);
            tempContatoreFatturazioniXMLfallite++;
//            listaFattureScartate.add(invoice);
            result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
            return result;
        }

        if (affiliato.getVia() == null || affiliato.getCitta() == null || affiliato.getNazione() == null
                || (affiliato.getNazione().equalsIgnoreCase("Italia") && affiliato.getCap() == null)) {
            result.put("msg", invoice.toString() + " with " + affiliato.toString() + " " + bundle.getString("AnagraficaFattura.msgAssenzaInfoSedeAffiliato"));
            invoice.setFatturaXML("FALLITA");
            invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.assenzaInfoSedeAffiliato"));
            sx.saveOrUpdate(invoice);
            tempContatoreFatturazioniXMLfallite++;
            result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
//            listaFattureScartate.add(invoice);
            return result;
        } else {
            indirizzoViaCP = affiliato.getVia();
            if (affiliato.getNumero() != null) {
                indirizzoViaCP += ", " + affiliato.getNumero();
            }
            comuneCP = affiliato.getCitta();
            nazioneCP = idPaeseCP;
            if (nazioneCP.equalsIgnoreCase("IT")) {
                provinciaCP = affiliato.getProvincia();
                capCP = affiliato.getCap();
            } else {
                provinciaCP = "EE";
                capCP = "00000";
            }
        }
        result.put("idPaeseCPTemp", idPaeseCPTemp);
        result.put("idCodiceCP", idCodiceCP);
        result.put("idCodiceFiscaleCP", idCodiceFiscaleCP);
        result.put("idPaeseCP", idPaeseCP);
        result.put("regimeFiscale", regimeFiscale);
        result.put("indirizzoViaCP", indirizzoViaCP);
//        result.put("indirizzoNumCivicoCP", indirizzoNumCivicoCP);
        result.put("capCP", capCP);
        result.put("comuneCP", comuneCP);
        result.put("provinciaCP", provinciaCP);
        result.put("nazioneCP", nazioneCP);
        result.put("denominazioneCP", denominazioneCP);
        result.put("nomeCP", nomeCP);
        result.put("cognomeCP", cognomeCP);
        result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);

        return result;
    }

    private static Map validateClientForInvoice(Session sx, MROldDocumentoFiscale invoice, MROldClienti cliente) {

        Map result = new HashMap();
        String idPaeseCC = null;
        String idPaeseCCTemp = null;
        String idCodiceCC = null;
        String idCodiceFiscaleCC = null;
        String denominazioneCC = null;
        String nomeCC = null;
        String cognomeCC = null;
        int tempContatoreFatturazioniXMLfallite = 0;
        String indirizzoViaCC = "";
//        String indirizzoNumCivicoCC = "";
        String capCC = "";
        String comuneCC = "";
        String provinciaCC = null;
        String nazioneCC = "";

        if (cliente.getNazione() != null) {
            try {
                String queryString = "SELECT n FROM MROldNazioneISO n WHERE n.nome = :nazione";
                Query query = sx.createQuery(queryString);
                query.setParameter("nazione", cliente.getNazione());
                List<MROldNazioneISO> nazione = new ArrayList();
                nazione = query.list();
                idPaeseCCTemp = nazione.get(0).getCodice();
            } catch (Exception ex) {
                result.put("msg5", "Something went wrong");
//                    ex.printStackTrace();
            } finally {
//                    if (sx != null && sx.isOpen()) {
//                        sx.close();
//                    }
            }
        }
        boolean isUE = false;
        if (idPaeseCCTemp != null) {
            for (int i = 0; i < paesiUE.length; i++) {
                if (paesiUE[i].equals(idPaeseCCTemp)) {
                    isUE = true;
                }
            }
        }
        boolean associazione = false;
        if (cliente.getAssociazione() != null && cliente.getAssociazione()) {
            associazione = true;
        }

        if (cliente.getPersonaFisica() != null && cliente.getDittaIndividuale() != null &&
                !(cliente.getPersonaFisica() && !cliente.getDittaIndividuale()) && (!associazione || (associazione && cliente.getPartitaIva() != null))) {
            if (idPaeseCCTemp != null) {
                idPaeseCC = idPaeseCCTemp;
            } else {
                result.put("msg", invoice.toString() + " with " + cliente.toString() + " " + bundle.getString("AnagraficaFattura.msgErrorePaeseAffiliatoCliente"));
                invoice.setFatturaXML("FALLITA");
                invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.errorePaeseCliente"));
                sx.saveOrUpdate(invoice);
                tempContatoreFatturazioniXMLfallite++;
                result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
//                listaFattureScartate.add(invoice);
                return result;
            }
            if (cliente.getPartitaIva() != null && isUE) {
                idCodiceCC = cliente.getPartitaIva();
            } else {
                idCodiceCC = "99999999999";
            }

//            } else {
//                result.put("msg", invoice.toString() + " with " + cliente.toString() + " " + bundle.getString("AnagraficaFattura.msgErroreCodiceFiscaleAffiliatoCliente"));
//                invoice.setFatturaXML("FALLITA");
//                invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.erroreCodiceFiscaleCliente"));
//                sx.saveOrUpdate(invoice);
//                tempContatoreFatturazioniXMLfallite++;
////                listaFattureScartate.add(invoice);
//                return result;
//            }
        } else if (cliente.getCodiceFiscale() != null && idPaeseCCTemp != null && idPaeseCCTemp.equalsIgnoreCase("IT")) {
            idCodiceFiscaleCC = cliente.getCodiceFiscale();
        } else if (idPaeseCCTemp != null) {
            idPaeseCC = idPaeseCCTemp;
            idCodiceCC = "99999999999";
        } else {
            result.put("msg", invoice.toString() + " with " + cliente.toString() + " " + bundle.getString("AnagraficaFattura.msgErroreCodiceFiscaleAffiliatoCliente"));
            invoice.setFatturaXML("FALLITA");
            invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.erroreCodiceFiscaleCliente"));
            sx.saveOrUpdate(invoice);
            tempContatoreFatturazioniXMLfallite++;
            result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
//            listaFattureScartate.add(invoice);
            return result;
        }

        if (cliente.getRagioneSociale() != null) {
            denominazioneCC = cliente.getRagioneSociale();
        } else if (cliente.getNome() != null && cliente.getCognome() != null) {
            nomeCC = cliente.getNome();
            cognomeCC = cliente.getCognome();
        }
        if (cliente.getRagioneSociale() == null && (cliente.getNome() == null || cliente.getCognome() == null)) {
            result.put("msg", invoice.toString() + " with " + cliente.toString() + " " + bundle.getString("AnagraficaFattura.msgAssenzaNomeCognomeRagSocialeCliente"));
            invoice.setFatturaXML("FALLITA");
            invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.assenzaInfoAnagraficaCliente"));
            tempContatoreFatturazioniXMLfallite++;
            result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
//            listaFattureScartate.add(invoice);
            return result;
        }

        if (cliente.getVia() == null || cliente.getCitta() == null || cliente.getNazione() == null
                || (cliente.getNazione().equalsIgnoreCase("Italia") && cliente.getCap() == null)) {
            result.put("msg", invoice.toString() + " with " + cliente.toString() + " " + bundle.getString("AnagraficaFattura.msgAssenzaInfoSedeAffiliato"));
            invoice.setFatturaXML("FALLITA");
            invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.assenzaInfoSedeCliente"));
            sx.saveOrUpdate(invoice);
            tempContatoreFatturazioniXMLfallite++;
            result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
//            listaFattureScartate.add(invoice);
            return result;
        } else {
            indirizzoViaCC = cliente.getVia();
            if (cliente.getNumero() != null) {
                indirizzoViaCC += ", " + cliente.getNumero();
            }
            //indirizzoNumCivicoCC = cliente.getNumero();
            comuneCC = cliente.getCitta();
            nazioneCC = idPaeseCCTemp;
            if (nazioneCC.equalsIgnoreCase("IT")) {
                provinciaCC = cliente.getProvincia();
                capCC = cliente.getCap();
            } else {
                provinciaCC = "EE";
                capCC = "00000";
            }
        }
        result.put("idPaeseCC", idPaeseCC);
        result.put("idPaeseCCTemp", idPaeseCCTemp);
        result.put("idCodiceCC", idCodiceCC);
        result.put("idCodiceFiscaleCC", idCodiceFiscaleCC);
        result.put("denominazioneCC", denominazioneCC);
        result.put("nomeCC", nomeCC);
        result.put("cognomeCC", cognomeCC);
        result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
        result.put("indirizzoViaCC", indirizzoViaCC);
//        result.put("indirizzoNumCivicoCC", indirizzoNumCivicoCC);
        result.put("capCC", capCC);
        result.put("comuneCC", comuneCC);
        result.put("provinciaCC", provinciaCC);
        result.put("nazioneCC", nazioneCC);
        return result;
    }

    private static Map getSummaryData(Session sx, MROldDocumentoFiscale invoice) {
        List<DatiRiepilogoType> datiRiepilogo = new ArrayList<DatiRiepilogoType>();
        boolean isSplitPayment = false;
        Double aliquotaIVAperRigaDescrittiva = -1.00d; //mi servira per dare unaliquota IVA alla riga descrittiva in Dettaglio Linee
        Map result = new HashMap();
        int tempContatoreFatturazioniXMLfallite = 0;
        try {
            invoice = (MROldDocumentoFiscale) sx.load(MROldDocumentoFiscale.class, invoice.getId());
            Map<MROldCodiciIva, MROldRigaImpostaDocumentoFiscale> righeImposta = invoice.getFatturaImposte();
            boolean naturaAssente = false;
            if (righeImposta != null && righeImposta.values().size() > 0) {
                for (MROldCodiciIva cod : righeImposta.keySet()) {
                    MROldRigaImpostaDocumentoFiscale rImposta = righeImposta.get(cod);
                    Double aliquotaIVA = 0.00;
                    if (rImposta.getCodiceIva() != null && rImposta.getCodiceIva().getAliquota() != null) {
                        aliquotaIVA = rImposta.getCodiceIva().getAliquota() * 100;
                    }
                    int naturaIVAcod = -1;
                    if (cod.getNaturaCodiceIVA() != null) {
                        String naturaIVA = cod.getNaturaCodiceIVA();
                        switch (naturaIVA) {
                            case N_1:
                                naturaIVAcod = XMLInvoiceUtils.N_1;
                                break;
                            case N_2:
                                naturaIVAcod = XMLInvoiceUtils.N_2;
                                break;
                            case N_3:
                                naturaIVAcod = XMLInvoiceUtils.N_3;
                                break;
                            case N_4:
                                naturaIVAcod = XMLInvoiceUtils.N_4;
                                break;
                            case N_5:
                                naturaIVAcod = XMLInvoiceUtils.N_5;
                                break;
                            case N_6:
                                naturaIVAcod = XMLInvoiceUtils.N_6;
                                break;
                            case N_7:
                                naturaIVAcod = XMLInvoiceUtils.N_7;
                                break;
                        }
                    }
                    int esigibilitaCod = -1;
                    if (cod.getEsigibilita() != null) {
                        String esigibilita = cod.getEsigibilita();
                        esigibilita = esigibilita.replaceAll("[^\\p{ASCII}]", "a");
                        if(esigibilita.equals(D)){
                            esigibilitaCod = XMLInvoiceUtils.D;
                        } else if(esigibilita.equals(I)){
                            esigibilitaCod = XMLInvoiceUtils.I;
                        } else if(esigibilita.equals(S)){
                            esigibilitaCod = XMLInvoiceUtils.S;
                            isSplitPayment = true;
                        }
                    }
                    System.out.println("naturaIVAcod:" + naturaIVAcod);

                    if (aliquotaIVA == 0 && naturaIVAcod == -1) {
                        result.put("msg", cod.getDescrizione() + " " +bundle.getString("AnagraficaFattura.msgErroreNaturaIva"));
                        naturaAssente = true;
                    } else {
                        if (aliquotaIVA != 0 && aliquotaIVAperRigaDescrittiva == -1.00d) {
                            aliquotaIVAperRigaDescrittiva = aliquotaIVA;
                        }
                        datiRiepilogo = XMLInvoiceUtils.createDatiRiepilogo(datiRiepilogo, naturaIVAcod, aliquotaIVA, rImposta.getImponibile(),
                                rImposta.getImposta(), esigibilitaCod);
                    }
                }
            }

            if (naturaAssente) {
                invoice.setFatturaXML("FALLITA");
                invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.erroreNaturaIva"));
                sx.saveOrUpdate(invoice);
                tempContatoreFatturazioniXMLfallite++;
                result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
//                listaFattureScartate.add(invoice);
                return result;
            }

        } catch (Exception ex) {
            result.put("msg", "Something went wrong!");
//                ex.printStackTrace();
        } finally {
//                if (sx != null && sx.isOpen()) {
//                    sx.close();
//                }
        }
        result.put("isSplitPayment",isSplitPayment?"Y":"N");
        result.put("datiRiepilogo", datiRiepilogo);
        result.put("aliquotaIVAperRigaDescrittiva", aliquotaIVAperRigaDescrittiva);
        result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
        return result;
    }

    private static Map getInvoiceLine(Session sx, MROldDocumentoFiscale invoice, MROldContrattoNoleggio contratto, Double aliquotaIVAperRigaDescrittiva) {

        Map result = new HashMap();
        List<DettaglioLineeType> dettaglioLinee = new ArrayList<DettaglioLineeType>();

        List<MROldRigaDocumentoFiscale> righeFattura = new ArrayList();
        try {
            //Personalizzazione ALD Automotive (Inserimento in ogni riga dei AltriDatiGestionali)
            String targaMeccanica = null;
            String targaApprontamento = null;
            String telaioApprontamento = null;
            String contrattoApprontamento = null;
            List<String> altriDatiGestionali = null;
            if (invoice.getRifAmministrativo() != null && (((String) invoice.getRifAmministrativo()).equals(EInvoicingUtils.COD_FT_0001)
                    && invoice.getTargaMeccanica() != null)) {
                altriDatiGestionali = new ArrayList<>();
                targaMeccanica = invoice.getTargaMeccanica();
                altriDatiGestionali.add(EInvoicingUtils.COD_FT_0001);
                altriDatiGestionali.add(targaMeccanica);
            } else if (invoice.getRifAmministrativo() != null && (((String) invoice.getRifAmministrativo()).equals(EInvoicingUtils.COD_FT_0002)
                    && invoice.getTargaApprontamento() != null && invoice.getTelaioApprontamento() != null && invoice.getContrattoApprontamento() != null)) {
                altriDatiGestionali = new ArrayList<>();
                targaApprontamento = invoice.getTargaApprontamento();
                telaioApprontamento = invoice.getTelaioApprontamento();
                contrattoApprontamento = invoice.getContrattoApprontamento();
                altriDatiGestionali.add(EInvoicingUtils.COD_FT_0002);
                altriDatiGestionali.add(targaApprontamento);
                altriDatiGestionali.add(telaioApprontamento);
                altriDatiGestionali.add(contrattoApprontamento);
            }
            int id_fattura = invoice.getId();
            String queryString = "SELECT r FROM MROldRigaDocumentoFiscale r WHERE r.fattura.id= :id_fattura ORDER BY r.numeroRigaFattura";
            Query query = sx.createQuery(queryString);
            query.setParameter("id_fattura", id_fattura);
            righeFattura = query.list();
            if (righeFattura != null && righeFattura.size() > 0) {
                for (int i = 0; i < righeFattura.size(); i++) {
                    if (righeFattura.get(i).getPrezzoUnitario() != null) {
                        MROldRigaDocumentoFiscale r = (MROldRigaDocumentoFiscale) righeFattura.get(i);
                        Double aliquotaIva = r.getCodiceIva().getAliquota() * 100;
                        int naturaIVAcod = -1;
                        if (r.getCodiceIva().getNaturaCodiceIVA() != null) {
                            String naturaIVA = r.getCodiceIva().getNaturaCodiceIVA();
                            switch (naturaIVA) {
                                case N_1:
                                    naturaIVAcod = XMLInvoiceUtils.N_1;
                                    break;
                                case N_2:
                                    naturaIVAcod = XMLInvoiceUtils.N_2;
                                    break;
                                case N_3:
                                    naturaIVAcod = XMLInvoiceUtils.N_3;
                                    break;
                                case N_4:
                                    naturaIVAcod = XMLInvoiceUtils.N_4;
                                    break;
                                case N_5:
                                    naturaIVAcod = XMLInvoiceUtils.N_5;
                                    break;
                                case N_6:
                                    naturaIVAcod = XMLInvoiceUtils.N_6;
                                    break;
                                case N_7:
                                    naturaIVAcod = XMLInvoiceUtils.N_7;
                                    break;
                            }
                        }
                        ScontoMaggiorazioneType sconto = new ScontoMaggiorazioneType();
                        Double totale = r.getTotaleImponibileRiga();
                        if (r.getSconto() != null && r.getSconto() > 0) {
                            sconto.setTipo(TipoScontoMaggiorazioneType.SC);
                            BigDecimal scontoPercentuale = BigDecimal.valueOf(r.getSconto()).setScale(2, RoundingMode.HALF_DOWN);
                            sconto.setPercentuale(scontoPercentuale);
                            Double scontoPerCalcolo = scontoPercentuale.doubleValue() / 100;
                            totale = r.getQuantita() * r.getPrezzoUnitario() * (1 - scontoPerCalcolo);
                        } else if (r.getScontoFisso() != null && r.getScontoFisso() > 0) {
                            sconto.setTipo(TipoScontoMaggiorazioneType.SC);
                            BigDecimal importoSconto = BigDecimal.valueOf(r.getScontoFisso() / r.getQuantita()).setScale(2, RoundingMode.HALF_DOWN);
                            sconto.setImporto(importoSconto);
                        }
                        dettaglioLinee = XMLInvoiceUtils.createDettaglioLinee(dettaglioLinee, r.getNumeroRigaFattura(), -1, r.getDescrizione(),
                                r.getQuantita(), r.getPrezzoUnitario(), totale, aliquotaIva, naturaIVAcod, sconto, altriDatiGestionali);
                    } else {
                        String descrizione = righeFattura.get(i).getDescrizione();
                        Integer numeroRiga = righeFattura.get(i).getNumeroRigaFattura();
                        dettaglioLinee = XMLInvoiceUtils.createDettaglioLinee(dettaglioLinee, numeroRiga, -1, descrizione,
                                0, 0, 0, aliquotaIVAperRigaDescrittiva, -1, null, null);
                    }

                }
            }

            //Inserire come riga descrittiva inizio e fine fatturazione, dove presente
            if (invoice.getInizioFatturazione() != null && invoice.getFineFatturazione() != null) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String dataInizio = df.format(invoice.getInizioFatturazione());
                String dataFine = df.format(invoice.getFineFatturazione());
                String rigaDescrittiva = "Inizio fatturazione: " + dataInizio + ", fine fatturazione: " + dataFine;
                dettaglioLinee = XMLInvoiceUtils.createDettaglioLinee(dettaglioLinee, dettaglioLinee.size(), -1, rigaDescrittiva, 0, 0, 0, aliquotaIVAperRigaDescrittiva, -1, null, null);
            }

            //Inserire come riga descrittiva la targa del veicolo noleggiato, dove presente
            MROldMovimentoAuto m = new MROldMovimentoAuto();
            if (contratto != null && contratto.getMovimento() != null) {
                m = (MROldMovimentoAuto) sx.get(MROldMovimentoAuto.class, contratto.getMovimento().getId());
                if (m.getVeicolo().getTarga() != null) {
                    String rigaDescrittiva = "Veicolo noleggiato targa: " + m.getVeicolo().getTarga();
                    dettaglioLinee = XMLInvoiceUtils.createDettaglioLinee(dettaglioLinee, dettaglioLinee.size(), -1, rigaDescrittiva, 0, 0, 0, aliquotaIVAperRigaDescrittiva, -1, null, null);
                }
            }

        } catch (Exception ex) {
            result.put("msg", "Something went wrong!");
//                ex.printStackTrace();
        } finally {
//                if (sx != null && sx.isOpen()) {
//                    sx.close();
//                }
        }
        result.put("dettaglioLinee", dettaglioLinee);

        return result;
    }

    private static Map getPaymentData(Session sx, MROldDocumentoFiscale invoice, MROldAffiliato affiliato, boolean isSplitPayment) {

        Map result = new HashMap();
        int tempContatoreFatturazioniXMLfallite = 0;
        List<DatiPagamentoType> datiPagamento = new ArrayList<DatiPagamentoType>();

        if (invoice.getPagamento() != null) {

            //Condizioni Pagamento
            List<Integer> listaCondizioniPagamento = new ArrayList<Integer>();
            int condizioniPagamento = -1;
            String paymentCondition = null;
            MROldPagamento pagamento = new MROldPagamento();
            try {
                pagamento = (MROldPagamento) sx.load(MROldPagamento.class, invoice.getPagamento().getId());
                if (pagamento.getCondizioniPagamento() != null) {
                    paymentCondition = pagamento.getCondizioniPagamento();
                    switch (paymentCondition) {
                        case "Pagamento a rate":
                            condizioniPagamento = XMLInvoiceUtils.TP_01;
                            break;
                        case "Pagamento completo":
                            condizioniPagamento = XMLInvoiceUtils.TP_02;
                            break;
                        case "Anticipo":
                            condizioniPagamento = XMLInvoiceUtils.TP_03;
                            break;
                    }
                }

                if (condizioniPagamento == -1) {
                    result.put("msg", pagamento + " " + bundle.getString("AnagraficaFattura.msgErroreCondizioniPagamento"));
                    invoice.setFatturaXML("FALLITA");
                    invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.erroreCondizioniPagamento"));
                    sx.saveOrUpdate(invoice);
                    tempContatoreFatturazioniXMLfallite++;
                    result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
//                    listaFattureScartate.add(invoice);
                }

            } catch (Exception ex) {
                result.put("msg", "Something went wrong!");
//                    ex.printStackTrace();
            } finally {
//                    if (sx != null && sx.isOpen()) {
//                        sx.close();
//                    }
            }
            if (condizioniPagamento == -1) {
                return result;
            }

            listaCondizioniPagamento.add(condizioniPagamento);

            //Dettaglio Pagamento
            List<DettaglioPagamentoType> dettaglioPagamento = new ArrayList<DettaglioPagamentoType>();

            Double importoPagamento;
            if (isSplitPayment) {
                importoPagamento = invoice.getTotaleImponibile();
            } else {
                importoPagamento = invoice.getTotaleFattura();
            }
            int modalitaPagamento = -1;
            try {
                if (pagamento != null) {
                    if (pagamento.getModalitaPagamento() != null) {
                        String paymentModality = pagamento.getModalitaPagamento();
                        paymentModality = paymentModality.replaceAll("[^\\p{ASCII}]", "");
                        switch (paymentModality) {
                            case "Contanti":
                                modalitaPagamento = XMLInvoiceUtils.MP_01;
                                break;
                            case "Assegno":
                                modalitaPagamento = XMLInvoiceUtils.MP_02;
                                break;
                            case "Assegno circolare":
                                modalitaPagamento = XMLInvoiceUtils.MP_03;
                                break;
                            case "Contanti presso Tesoreria":
                                modalitaPagamento = XMLInvoiceUtils.MP_04;
                                break;
                            case "Bonifico":
                                modalitaPagamento = XMLInvoiceUtils.MP_05;
                                break;
                            case "Vaglia cambiario":
                                modalitaPagamento = XMLInvoiceUtils.MP_06;
                                break;
                            case "Bollettino bancario":
                                modalitaPagamento = XMLInvoiceUtils.MP_07;
                                break;
                            case "Carta di pagamento":
                                modalitaPagamento = XMLInvoiceUtils.MP_08;
                                break;
                            case "RID":
                                modalitaPagamento = XMLInvoiceUtils.MP_09;
                                break;
                            case "RID utente":
                                modalitaPagamento = XMLInvoiceUtils.MP_10;
                                break;
                            case "RID veloce":
                                modalitaPagamento = XMLInvoiceUtils.MP_11;
                                break;
                            case "RIBA":
                                modalitaPagamento = XMLInvoiceUtils.MP_12;
                                break;
                            case "MAV":
                                modalitaPagamento = XMLInvoiceUtils.MP_13;
                                break;
                            case "Quietanza erario":
                                modalitaPagamento = XMLInvoiceUtils.MP_14;
                                break;
                            case "Giroconto su conti di contabilit speciale":
                                modalitaPagamento = XMLInvoiceUtils.MP_15;
                                break;
                            case "Domiciliazione bancaria":
                                modalitaPagamento = XMLInvoiceUtils.MP_16;
                                break;
                            case "Domiciliazione postale":
                                modalitaPagamento = XMLInvoiceUtils.MP_17;
                                break;
                            case "Bollettino di c/c postale":
                                modalitaPagamento = XMLInvoiceUtils.MP_18;
                                break;
                            case "SEPA Direct Debit":
                                modalitaPagamento = XMLInvoiceUtils.MP_19;
                                break;
                            case "SEPA Direct Debit CORE":
                                modalitaPagamento = XMLInvoiceUtils.MP_20;
                                break;
                            case "SEPA Direct Debit B2B":
                                modalitaPagamento = XMLInvoiceUtils.MP_21;
                                break;
                            case "Trattenuta su somme gi riscosse":
                                modalitaPagamento = XMLInvoiceUtils.MP_22;
                                break;
                        }
                    }
                }
                if (modalitaPagamento == -1) {
                    result.put("msg", invoice.getPrefisso() + invoice.getNumero() + " " + invoice.getData() + " " + bundle.getString("AnagraficaFattura.msgErroreModalitaPagamento"));
                    invoice.setFatturaXML("FALLITA");
                    invoice.setMotivoFatturazioneXMLFallita(bundle.getString("AnagraficaFattura.erroreModalitaPagamento"));
                    sx.saveOrUpdate(invoice);
                    tempContatoreFatturazioniXMLfallite++;
                    result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
//                    listaFattureScartate.add(invoice);
                }

            } catch (Exception ex) {
                result.put("msg", "Something went wrong!");
//                    ex.printStackTrace();
            } finally {
//                    if (sx != null && sx.isOpen()) {
//                        sx.close();
//                    }
            }
            if (modalitaPagamento == -1) {
                return result;
            }

            String iban = null;
            invoice = (MROldDocumentoFiscale) sx.get(MROldDocumentoFiscale.class, invoice.getId());
            User user = null;
            MROldBankAccount bankAccount = null;
            if (invoice.getUser() != null) {
                user = invoice.getUser();
            }
            if (user.getSedeOperativa() != null) {
                MROldSede sede = user.getSedeOperativa();
                if (sede.getBankAccount() != null) {
                    bankAccount = (MROldBankAccount) sx.get(MROldBankAccount.class, sede.getBankAccount().getId());
                    iban = bankAccount.getIban();
                }
            } else if (affiliato.getBankAccount() != null && affiliato.getBankAccount().getId() != null) {
                bankAccount = (MROldBankAccount) sx.get(MROldBankAccount.class, affiliato.getBankAccount().getId());
                iban = bankAccount.getIban();
            }

          //  dettaglioPagamento = XMLInvoiceUtils.createDettaglioPagamento(dettaglioPagamento, 0, modalitaPagamento, importoPagamento, iban, null, null, null);

            datiPagamento = XMLInvoiceUtils.createDatiPagamento(datiPagamento, 0, listaCondizioniPagamento.get(0), dettaglioPagamento);

        }
        result.put("datiPagamento", datiPagamento);
        result.put("tempContatoreFatturazioniXMLfallite", tempContatoreFatturazioniXMLfallite);
        return result;
    }

    public Map recoverStatus() {
        Map result = new HashMap();
        StringBuilder sb = new StringBuilder();
        try {
            Iterator it = invoiceIds.iterator();
            URL urlTest = new URL("https://testfe.solutiondocondemand.com/solutiondoc_hub.asmx");
            SolutionDOCHub ws = new SolutionDOCHub(urlTest);
            SolutionDOCHubSoap wsSoap = ws.getSolutionDOCHubSoap();
            byte[] password = null;
            String codiceCliente = null;
            MROldAffiliato affiliato = new MROldAffiliato();
            ElectronicInvoiceOutcomeFilter paramFilter = new ElectronicInvoiceOutcomeFilter();
            Auth paramAuth = new Auth();
            ElectronicInvoiceOutcomeResponse recuperaStato = new ElectronicInvoiceOutcomeResponse();
            ArrayOfElectronicInvoiceOutcome arrayRispostaStato = new ArrayOfElectronicInvoiceOutcome();
            while (it.hasNext()) {
                sb.append("</br>");
                MROldDocumentoFiscale invoice = (MROldDocumentoFiscale) sx.load(MROldDocumentoFiscale.class, Integer.parseInt((String) it.next()));
                affiliato = (MROldAffiliato) sx.get(MROldAffiliato.class, invoice.getAffiliato().getId());
                if (affiliato.getPasswordCodiceCliente() != null && affiliato.getCodiceCliente() != null) {
                    password = affiliato.getPasswordCodiceCliente().getBytes();
                    codiceCliente = affiliato.getCodiceCliente();
                    paramAuth.setCustomerCode(codiceCliente);
                    paramAuth.setPassword(password);
                    if (invoice.getIdSdi() != null) {
                        Long idSdi = invoice.getIdSdi();
                        paramFilter.setIdSdi(idSdi);
                        paramFilter.setDataFullOutcomes(true);
                        recuperaStato = wsSoap.getElectronicInvoiceOutcomes(paramAuth, paramFilter);
                        arrayRispostaStato = recuperaStato.getElectronicInvoiceOutcomes();
                        String statoInvio;
                        if (arrayRispostaStato != null && arrayRispostaStato.getElectronicInvoiceOutcomes() != null &&
                                arrayRispostaStato.getElectronicInvoiceOutcomes().size() > 0) {
                            statoInvio = arrayRispostaStato.getElectronicInvoiceOutcomes().get(0).getTipoMessaggio().toString();
                                invoice.setStatoInvio(statoInvio);
                                sx.saveOrUpdate(invoice);
                            sb.append(bundle.getString("AnagraficaFattura.statusUpdated"));
                        } else {
                            sb.append(bundle.getString("AnagraficaFattura.msgFatturaXMLRispostaNonDisponibile1") + " " + invoice.getPrefisso() + "_" + invoice.getNumero() + " " +
                                    bundle.getString("AnagraficaFattura.msgFatturaXMLRispostaNonDisponibile2"));
                            invoice.setStatoInvio("N/A");
                            sx.saveOrUpdate(invoice);
                        }
                    } else {
                        sb.append(bundle.getString("AnagraficaFattura.msgFatturaXMLnonPresenteInSDI1") + " " + invoice.getPrefisso() + "_" + invoice.getNumero() + " " +
                                bundle.getString("AnagraficaFattura.msgFatturaXMLnonPresenteInSDI2"));
                    }
                }
            }
        } catch (Exception ex) {
            result.put("msg","Something went wrong");
            return result;
        }
        result.put("msg",sb);
        return result;
    }
}
