package it.aessepi.myrentcs.utils;

import it.aessepi.utils.BundleUtils;
import it.myrent.ee.api.utils.DisponibilitaUtils;
import it.myrent.ee.db.*;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by bharti on 10/16/2019.
 */
public class VeicoloUtils {

    private static final ResourceBundle bundle = BundleUtils.getBundle("it/aessepi/myrentcs/utils/Bundle");

    public static Map exportVehicles(Session sx) {
        Map map = new HashMap<>();
        List<MROldParcoVeicoli> listVeicoli = new ArrayList<MROldParcoVeicoli>();
        listVeicoli = sx.createQuery("from MROldParcoVeicoli order by id").list();

        final String DEL = ";";
        final String NEW_LINE_SEPARATOR = "\r\n";
        NumberFormat formatter = new DecimalFormat("#0.00");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String gruppo = "";
        String segmento = "";
        String marca = "";
        String modello = "";
        String versione = "";
        String targa = "";
        String annoImmatricolazione = "";
        String telaio = "";
        String colore = "";
        String km = "";
        String kmVehicle = "";
        String promemoria = "";
        String capacitaSerbatoio = "";
        String pulita = "";
        String carburante = "";
        String dataScadContratto = "";
        String dataAcq = "";
        String dataVend = "";
        String cambio = "";
        String cilindrata = "";
        String nrPosti = "";
        String carrozzeria = "";
        String trazione = "";
        String ruotaDiScorta = "";
        String potenza = "";
        String sede = "";
        String modalitaAcq = "";
        String costoAcq = "";
        String importoRate = "";
        String numRate = "";
        String parcheggio = "";
        String dataInflo = "";
        String dataInizioNol = "";
        String dataUscitaPrev = "";
        String fornitore = "";
        String dispositivoSat = "";
        String pneus = "";
        String misuraPneus = "";
        String abilitato = "";
        String livelloCarburante = "";

        List<String> rigaString = new ArrayList<String>();
        String header = "ACRISS" + DEL + " GRUPPO " + DEL + "MARCA" + DEL + "MODELLO" + DEL + "VERSIONE" + DEL + "TARGA" + DEL + "ANNO IMMATRICOLAZIONE" + DEL + "TELAIO" + DEL + "COLORE" + DEL + "KM VEICOLO" + DEL + "KM TOTALI" + DEL + "PROMEMORIA" + DEL +
                "CAPACITA SERBATOIO" + DEL + "PULITA" + DEL + "CARBURANTE" + DEL + "FINE CONTRATTO STIMATA" + DEL + "DATA INFLOTTAMENTO" + DEL + "DATA DI USCITA FLOTTA/VENDITA" + DEL + "CAMBIO" + DEL + "CILINDRATA" + DEL + "NUMERO POSTI" + DEL +
                "CARROZZERIA" + DEL + "TRAZIONE" + DEL + "RUOTA DI SCORTA" + DEL + "POTENZA" + DEL + "SEDE" + DEL + "MODALITA ACQUISTO" + DEL + "COSTO ACQUISTO" + DEL + "IMPORTO RATE" + DEL + "NUMERO RATE" + DEL + "DATA INIZIO NOLEGGIO" + DEL + "DATA INIZIO NOLEGGIO" + DEL
                + "DATA USCITA PREVISTA" + DEL + "PARCHEGGIO" + DEL + "PROPRIETARIO" + DEL + "DISPOSITIVO SATELLITARE" + DEL + "PNEUMATICI" + DEL + "MISURAPNEUMATICI" + DEL + "ABILITATO" + DEL + "LIVELLO CARBURANTE " + NEW_LINE_SEPARATOR;
        rigaString.add(header);
        for (MROldParcoVeicoli v : listVeicoli) {
            String total = "";
            MROldGruppo group = v.getGruppo();
            if (group != null) {
                gruppo = v.getGruppo().getCodiceInternazionale();
                segmento = v.getGruppo().getDescrizione();
            }
            if (v.getMarca() != null) {
                marca = v.getMarca();
            }
            if (v.getModello() != null) {
                modello = v.getModello();
            }
            if (v.getVersione() != null) {
                versione = v.getVersione();
            }
            if (v.getTarga() != null) {
                targa = v.getTarga();
            }
            if (v.getDataImmatricolazione() != null) {
                annoImmatricolazione = dateFormat.format(v.getDataImmatricolazione());
            }

            if (v.getTelaio() != null) {
                telaio = v.getTelaio();
            }
            if (v.getColore() != null) {
                colore = v.getColore();
            }

            if (v.getKm() != null) {
                kmVehicle = v.getKm().toString();
            } else {
                kmVehicle = "0";
            }

            if (v.getVehicleDetail() != null && v.getVehicleDetail().getFinMaxTotalKm() != null) {
                km = v.getVehicleDetail().getFinMaxTotalKm().toString();
            } else {
                km = "0";
            }
            if (v.getPromemoria() != null) {
                promemoria = v.getPromemoria();
            } else {
                promemoria = "";
            }
            if (v.getCapacitaSerbatoio() == null) {
                capacitaSerbatoio = "0";
            } else {
                capacitaSerbatoio = v.getCapacitaSerbatoio().toString();
            }
            if (v.getPulita() != null && v.getPulita()) {
                pulita = "TRUE";
            } else {
                pulita = "FALSE";
            }
            if (v.getCarburante() != null) {
                carburante = v.getCarburante().getDescrizione();
            }
            dataScadContratto = dateFormat.format(v.getDataScadenzaContratto());
            dataAcq = dateFormat.format(v.getDataAcquisto());
            dataVend = dateFormat.format(v.getDataVendita());
            cilindrata = v.getCilindrata();
            if (v.getNumeroPosti() != null) {
                nrPosti = v.getNumeroPosti().toString();
            } else {
                nrPosti = "0";

            }

            if (v.getAbilitato() != null && v.getAbilitato()) {
                abilitato = "TRUE";
            } else {
                abilitato = "FALSE";
            }

            if (v.getVehicleDetail() != null && v.getVehicleDetail().getTransmission() != null) {
                cambio = v.getVehicleDetail().getTransmission().getDescription();
            }

            if (v.getVehicleDetail() != null && v.getVehicleDetail().getTraction() != null) {
                trazione = v.getVehicleDetail().getTraction().getDescription();
            }
            if (v.getVehicleDetail() != null && v.getVehicleDetail().getChassis() != null) {
                carrozzeria = v.getVehicleDetail().getChassis().getDescription();
            }
            if (v.getRuotaDiScorta() != null) {
                ruotaDiScorta = v.getRuotaDiScorta();
            }
            if (v.getSede() != null) {
                sede = v.getSede().getDescrizione();
            }

            if (v.getVehicleDetail() != null && v.getVehicleDetail().getFinPurchaseTypeId() != null) {
                modalitaAcq = v.getVehicleDetail().getFinPurchaseTypeId().getDescription();
            }
            if (v.getVehicleDetail() != null && v.getVehicleDetail().getFinTotalMonthlyFee() != null) {
                importoRate = formatter.format(v.getVehicleDetail().getFinTotalMonthlyFee());
            } else {
                importoRate = formatter.format(0d);
            }

            if (v.getVehicleDetail() != null && v.getVehicleDetail().getFinFirstInstallmentEffectiDate() != null) {
                dataInizioNol = dateFormat.format(v.getVehicleDetail().getFinFirstInstallmentEffectiDate());
            } else {
                dataInizioNol = "";
            }
            if (v.getVehicleDetail() != null && v.getVehicleDetail().getFinLastInstallmentDate() != null) {
                dataUscitaPrev = dateFormat.format(v.getVehicleDetail().getFinLastInstallmentDate());
            } else {
                dataUscitaPrev = "";
            }

            if (v.getVehicleDetail() != null && v.getVehicleDetail().getFinFirstInstallmentEffectiDate() != null) {
                dataInflo = dateFormat.format(v.getVehicleDetail().getFinFirstInstallmentEffectiDate());
            } else {
                dataInflo = "";
            }

            if (v.getProprietario() != null) {
                fornitore = v.getProprietario().getRagioneSociale();
            }
            try {
                if (v.getVehicleDetail() != null && v.getVehicleDetail().getTrackingDeviceId() != null && v.getVehicleDetail()
                        .getTrackingDeviceId().getTrackingTypeSystemId() != null) {
                    dispositivoSat = v.getVehicleDetail().getTrackingDeviceId().getTrackingTypeSystemId().getDescription();

                } else {
                    dispositivoSat = "";
                }
            } catch (Exception e) {
                dispositivoSat = "";
            }
            if (v.getPneumatici() != null) {
                pneus = v.getPneumatici();
            }
            if (v.getMisuraPneumatici() != null) {
                misuraPneus = v.getMisuraPneumatici();
            }

            if (v.getParcheggio() != null) {
                parcheggio = v.getParcheggio();
            }

            if (v.getVehicleDetail() != null && v.getVehicleDetail().getFinFirstInstallmentEffectiDate() != null && v.getVehicleDetail().getFinLastInstallmentDate() != null) {
                Calendar startCalendar = new GregorianCalendar();
                startCalendar.setTime(v.getVehicleDetail().getFinFirstInstallmentEffectiDate());
                Calendar endCalendar = new GregorianCalendar();
                endCalendar.setTime(v.getVehicleDetail().getFinLastInstallmentDate());
                int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
                Integer diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
                if (diffMonth != null) {
                    numRate = diffMonth.toString();
                } else {
                    numRate = "N.D.";
                }

            } else {
                numRate = "N.D.";
            }

            if (v.getLivelloCombustibile() != null) {
                livelloCarburante = v.getLivelloCombustibile().toString();
            }

            total += gruppo + DEL + segmento + DEL + marca + DEL + modello + DEL + versione + DEL + targa + DEL + annoImmatricolazione + DEL + telaio + DEL + colore + DEL + kmVehicle + DEL + km
                    + DEL + promemoria + DEL + capacitaSerbatoio + DEL + pulita + DEL + carburante + DEL + dataScadContratto + DEL + dataAcq + DEL + dataVend + DEL + cambio + DEL
                    + cilindrata + DEL + nrPosti + DEL + carrozzeria + DEL + trazione + DEL + ruotaDiScorta + DEL + potenza + DEL + sede + DEL + modalitaAcq + DEL + costoAcq + DEL
                    + importoRate + DEL + numRate + DEL + dataInflo + DEL + dataInizioNol + DEL + dataUscitaPrev + DEL + parcheggio + DEL + fornitore + DEL + dispositivoSat + DEL + pneus + DEL + misuraPneus + DEL + abilitato + DEL + livelloCarburante + NEW_LINE_SEPARATOR;
            rigaString.add(total);
        }
        try {
            File f = File.createTempFile("EXPORT", ".csv");
            PrintWriter writer = new PrintWriter(f);
            StringBuilder sb = new StringBuilder();
            String strz = "";
            for (String string : rigaString) {
                sb.append(string);
                strz += string;
            }

            writer.write(strz);
            writer.close();
            System.out.println("export done!");
            map.put("status", "success");
            map.put("file", f);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", "error");
        }
        return map;
    }

    private static MROldCausaleMovimento findCausaleMov(Session sx, String descrizione) {
        descrizione = "imported movements";
        Query qry = sx.createQuery("from MROldCausaleMovimento where descrizione = :descrizione");
        qry.setParameter("descrizione", descrizione.toUpperCase());
        if (qry.list().isEmpty()) {
            try {
                MROldCausaleMovimento causale = new MROldCausaleMovimento();
                causale.setDescrizione(descrizione.toUpperCase());
                causale.setNoleggio(Boolean.FALSE);
                causale.setPrenotazione(Boolean.FALSE);
                causale.setTrasferimento(Boolean.FALSE);
                causale.setLavaggio(Boolean.FALSE);
                causale.setRifornimento(Boolean.FALSE);
                causale.setRiparazione(Boolean.FALSE);
                causale.setIndisponibilitaFutura(Boolean.FALSE);
                causale.setIsDisabled(Boolean.FALSE);
                sx.saveOrUpdate(causale);
                return causale;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return (MROldCausaleMovimento) qry.uniqueResult();
        }
    }

    private static Date addTwoYears(Date dataUscitaD) {
        Calendar c = Calendar.getInstance();
        c.setTime(dataUscitaD);
        c.add(Calendar.YEAR, 2);
        return c.getTime();
    }

    private static Integer getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    /**
     * @param idVeicolo
     */
    public void ForzaControlloDisponibilitaVeicolo(Session sx, int idVeicolo) {
        int maxDialogWidth = (int) (500);
        try {
            List<MROldMovimentoAuto> movs = ControllaDisponibilitaVeicolo(sx, idVeicolo);
            MROldParcoVeicoli obj = (MROldParcoVeicoli) sx.get(MROldParcoVeicoli.class, idVeicolo);
            if (movs.isEmpty()) {
                obj.setImpegnato(Boolean.FALSE);
                sx.saveOrUpdate(obj);
                if (obj.getImpegnato() == true) {
                    System.out.println(bundle.getString("vehicle unlocked"));
                } else {
                    System.out.println(bundle.getString("vehicle already unlocked"));
                }
            } else {
                MROldMovimentoAuto[] array = new MROldMovimentoAuto[movs.size()];
                movs.toArray(array); // fill the array
                String errorMovements = bundle.getString("veicolo_bloccato_1") + " " + obj.getTarga() + " " + bundle.getString("veicolo_bloccato_2");
                for (int i = 0; i < movs.size(); i++) {
                    errorMovements = errorMovements + "<br/><p style='width:" + maxDialogWidth + "px;'> - " + bundle.getString("veicolo_bloccato_3") + array[i].getId() + " " + bundle.getString("veicolo_bloccato_4") + " " + array[i].getInizio() + " " + bundle.getString("veicolo_bloccato_5") + " " + array[i].getFine() + "</p>";
                }
                errorMovements = errorMovements + bundle.getString("veicolo_bloccato_6");
                System.out.println(errorMovements);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            MROldParcoVeicoli obj = (MROldParcoVeicoli) sx.get(MROldParcoVeicoli.class, idVeicolo);
            DisponibilitaUtils.creaDisponibilitaVeicolo(sx, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List ControllaDisponibilitaVeicolo(Session sx, Integer idVeicolo) {
        try {
            return sx.createQuery(
                    "select mov from MROldMovimentoAuto mov where "
                            + "mov.veicolo.id = :idVeicolo and "
                            + "mov.annullato = false and "
                            + "mov.chiuso = false and"
                            + "(mov.causale.id <> 2 or mov.causale.indisponibilitaFutura <> true)").
                    setParameter("idVeicolo", idVeicolo).
                    list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void blockVehiclesFromImport(Session sx, File file) {
        Map<MROldParcoVeicoli, String> errori = new HashMap<>();
        String cvsSplitBy = ";";
        String line = "";
        Integer contatoreInterno = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH.mm");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i > 0) {

                    String[] linea = line.split(cvsSplitBy);
                    if (linea.length >= 7) {
                        String numeroLocazione = linea[0];
                        String modello = linea[1];
                        String targa = linea[2];
                        String ragioneSociale = linea[3];
                        String statoNoleggio = linea[4];
                        String referente = linea[5];
                        String dataUscita = linea[6];
                        String oraUscita = linea[7];
                        String dataRientroPrevisto = "";
                        if (linea.length > 8) {
                            dataRientroPrevisto = linea[8];
                        }
                        String oraRientroPrevisto = "";
                        if (linea.length > 9) {
                            oraRientroPrevisto = linea[9];
                        }


                        MROldParcoVeicoli veicolo = null;
                        if (targa != null) {
                            try {
                                Query qry = sx.createQuery("select c from MROldParcoVeicoli c where c.targa = :targa");
                                qry.setParameter("targa", targa);
                                veicolo = (MROldParcoVeicoli) qry.uniqueResult();
                                if (veicolo != null) {
                                    MROldCausaleMovimento causaleMov = findCausaleMov(sx, statoNoleggio);
                                    Integer livelloCombustibile = veicolo.getLivelloCombustibile();
                                    MROldAffiliato affiliatoVeicolo = veicolo.getAffiliato();
                                    Date todayDate = Calendar.getInstance().getTime();
                                    MROldSede sedeUscita = veicolo.getSede(), sedeRientro = veicolo.getSede();
//                                    String todayDateS = sdf.format(todayDate);
//                                    String dateHourUscita = todayDateS + "-" + oraUscita;
                                    String dateHourUscita = sdf.format(todayDate);
                                    Date dataUscitaD = sdf.parse(dateHourUscita);
                                    Integer year = getYear(todayDate);
                                    Integer kmStart = veicolo.getKm();
                                    Date dataUscitaMore2 = addTwoYears(dataUscitaD);
                                    if (veicolo.getDataAcquisto().after(dataUscitaD)) {
                                        dataUscitaD = veicolo.getDataAcquisto();
                                    }
                                    if (veicolo.getDataScadenzaContratto().before(dataUscitaMore2)) {
                                        dataUscitaMore2 = veicolo.getDataScadenzaContratto();
                                    }
                                    if (ContrattoUtils.disabbinaVeicolo(sx, veicolo, todayDate, dataUscitaMore2)) {
                                        try {
                                            MROldMovimentoAuto mov = new MROldMovimentoAuto();
                                            mov.setAffiliato(affiliatoVeicolo);
                                            mov.setCausale(causaleMov);
                                            mov.setAnno(year);
                                            mov.setData(todayDate);
                                            mov.setVeicolo(veicolo);
                                            mov.setSedeUscita(sedeUscita);
                                            mov.setSedeRientro(sedeRientro);
                                            mov.setInizio(dataUscitaD);
                                            mov.setKmInizio(kmStart);
                                            mov.setCombustibileInizio(livelloCombustibile);
                                            mov.setFine(dataUscitaMore2);
                                            mov.setCombustibileFine(0);
                                            mov.setDataApertura(todayDate);
                                            sx.saveOrUpdate(mov);
                                            System.out.println("Vehicle movement " + veicolo.getTarga() + " created");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        errori.put(veicolo, "MOVIMENTO ESISTENTE");
                                        System.out.println("There is already an open movement for this vehicle. " + veicolo.getTarga());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                i++;
            }

            if (errori.isEmpty()) {
                System.out.println("Import Successful");
            } else {
                System.out.println("Import Successful, with vehicles not entered");
                System.out.println(errori);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map importCSVVehicles(Session sx, File file, User loginUser, Boolean updateKM, Boolean
            updateLocation, Boolean updateFuel) {
        String title = "IMPORT VEICOLI";
        String description = "Aggiornare anche i seguenti campi?";
        String label1 = "Aggiorna KM";
        String label2 = "Aggiorna Sede";
        String label3 = "Aggiorna Carburante";
        Map<MROldParcoVeicoli, String> errori = new HashMap<>();
        Map map = new HashMap<>();
        String cvsSplitBy = ";";
        String line = "";
        Integer contatoreInterno = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i > 0) {
                    String[] linea = line.split(cvsSplitBy);
                    if (linea.length >= 34) {
                        String acriss = linea[0];
                        String gruppo = linea[1];
                        String marca = linea[2];
                        String modello = linea[3];
                        String versione = linea[4];
                        String targa = linea[5];
                        String dataImmatricolazione = linea[6];
                        String telaio = linea[7];
                        String colore = linea[8];
                        String kmVehicle = linea[9];
                        String kmTotali = linea[10];
                        String promemoria = linea[11];
                        String capacitaSerbatoio = linea[12];
                        String pulita = linea[13];
                        String carburante = linea[14];
                        String fineContrattoStimata = linea[15];
                        String inizioContrattoAcquisto = linea[16];
                        String dataUscitaFlotta = linea[17];
                        String cambio = linea[18];
                        String cilindrata = linea[19];
                        String numeroPosti = linea[20];
                        String carrozzeria = linea[21];
                        String trazione = linea[22];
                        String ruotaDiScorta = linea[23];
                        String potenza = linea[24];
                        String sede = linea[25];
                        String modalitaAcq = linea[26];
                        String costoAcq = linea[27];
                        String importoRate = linea[28];
                        String numeroRate = linea[29];
                        String dataInflo = linea[30];
                        String dataInizioNoleggio = linea[31];
                        String dataUscitaPrevista = linea[32];
                        String parcheggio = linea[33];
                        String proprietario = linea[34];
                        String dispositivoSatellitare = "";
                        if (linea.length > 35) {
                            dispositivoSatellitare = linea[35];
                        }
                        String pneus = "";
                        String misuraPneus = "";
                        String abilitato = "TRUE";
                        String livelloCarburante = "";
                        if (linea.length > 36) {
                            pneus = linea[36];
                        }
                        if (linea.length > 37) {
                            misuraPneus = linea[37];
                        }
                        if (linea.length > 38) {
                            abilitato = linea[38];
                        }

                        if (linea.length > 39) {
                            livelloCarburante = linea[39];
                        }

                        MROldParcoVeicoli veicolo = null;
                        if (targa != null) {
                            try {
                                Query qry = sx.createQuery("select c from MROldParcoVeicoli c where c.targa = :targa");
                                qry.setParameter("targa", targa);
                                veicolo = (MROldParcoVeicoli) qry.uniqueResult();

                                if (veicolo == null) {
                                    veicolo = new MROldParcoVeicoli();
                                    if (veicolo.getVehicleDetail() == null) {
                                        veicolo.setVehicleDetail(new MROldVehicleDetail());
                                        veicolo.getVehicleDetail().setParco(veicolo);
                                    }
                                    if (setVehicleCSV(sx, gruppo, errori, veicolo, promemoria, marca, modello, versione, targa, dataImmatricolazione, telaio, colore, kmTotali, capacitaSerbatoio, pulita, carburante, fineContrattoStimata, inizioContrattoAcquisto, dataUscitaFlotta, cambio, cilindrata, numeroPosti, carrozzeria, trazione, ruotaDiScorta, sede, modalitaAcq, dataInflo, dataInizioNoleggio, dataUscitaPrevista, parcheggio, proprietario, dispositivoSatellitare, pneus, misuraPneus, abilitato, livelloCarburante, kmVehicle, updateKM, updateFuel, updateLocation, loginUser)) {
                                        continue;
                                    }
                                } else {
                                    //veicolo esistente. aggiorno.
                                    if (setVehicleCSV(sx, gruppo, errori, veicolo, promemoria, marca, modello, versione, targa, dataImmatricolazione, telaio, colore, kmTotali, capacitaSerbatoio, pulita, carburante, fineContrattoStimata, inizioContrattoAcquisto, dataUscitaFlotta, cambio, cilindrata, numeroPosti, carrozzeria, trazione, ruotaDiScorta, sede, modalitaAcq, dataInflo, dataInizioNoleggio, dataUscitaPrevista, parcheggio, proprietario, dispositivoSatellitare, pneus, misuraPneus, abilitato, livelloCarburante, kmVehicle, updateKM, updateFuel, updateLocation, loginUser)) {
                                        continue;
                                    }
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
                i++;
            }
            if (errori.isEmpty()) {
                System.out.println("Import Successful");
                map.put("status", "success");
            } else {
                System.out.println("Import Successful, with errors");
                map.put("status", "successWithError");
                map.put("message", "Import Successful, with errors");
                map.put("map", errori);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return map;

    }

    private static boolean setVehicleCSV(Session sx, String gruppo, Map<MROldParcoVeicoli, String> errori, MROldParcoVeicoli veicolo, String promemoria, String marca, String modello, String versione, String targa, String dataImmatricolazione, String telaio, String colore, String kmTotali, String capacitaSerbatoio, String pulita, String carburante, String fineContrattoStimata, String inizioContrattoAcquisto, String dataUscitaFlotta, String cambio, String cilindrata, String numeroPosti, String carrozzeria, String trazione, String ruotaDiScorta, String sede, String modalitaAcq, String dataInflo, String dataInizioNoleggio, String dataUscitaPrevista, String parcheggio, String proprietario, String dispositivoSatellitare, String pneus, String misuraPneus, String abilitato, String livelloCarburante, String kmVehicle, Boolean updateKM, Boolean updateFuel, Boolean updateLocation, User loginUser) throws HibernateException, ParseException {
        Query qry = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        MROldGruppo group = null;
        qry = sx.createQuery("from MROldGruppo where descrizione = :desc");
        qry.setParameter("desc", gruppo);
        if (!qry.list().isEmpty()) {
            group = (MROldGruppo) qry.list().get(0);
        }
        if (group == null) {
            errori.put(veicolo, "GRUPPO MANCANTE");
            return true;
        } else {
            veicolo.setGruppo(group);
        }
        veicolo.setMarca(marca);
        veicolo.setPromemoria(promemoria);
        veicolo.setModello(modello);
        if (versione != null) {
            veicolo.setVersione(versione);
        } else {
            veicolo.setVersione(".");
        }
        if (targa != null) {
            veicolo.setTarga(targa);
        } else {
            errori.put(veicolo, "TARGA MANCANTE");
            return true;
        }

        Date dataImmatricolazioneD = null;
        try {
            dataImmatricolazioneD = sdf.parse(dataImmatricolazione);
        } catch (Exception e) {

        }
        if (dataImmatricolazioneD != null) {
            veicolo.setDataImmatricolazione(dataImmatricolazioneD);
        } else {
            veicolo.setDataImmatricolazione(sdf.parse("01/01/2000"));
        }

        MROldAffiliato affiliate = loginUser.getAffiliato();
        if (affiliate != null) {
            veicolo.setAffiliato(affiliate);
        } else {
            qry = sx.createQuery("from MROldAffiliato where id = 1");
            veicolo.setAffiliato((MROldAffiliato) qry.uniqueResult());
        }

        veicolo.setTelaio(telaio);
        veicolo.setColore(colore);
        Double kmI = null;
        try {
            kmI = Double.parseDouble(kmTotali);
        } catch (Exception e) {
            kmI = 0d;
        }
        if (kmI != null && kmI != 0) {
            veicolo.getVehicleDetail().setFinMaxTotalKm(kmI);
        }

        Integer kmIn = null;
        try {
            kmIn = Integer.parseInt(kmVehicle);
        } catch (Exception e) {
            kmIn = 0;
        }
        if (updateKM != null && updateKM) {
            if (kmI != null && kmI != 0) {
                veicolo.setKm(kmIn);
            }
        }

        if (updateFuel != null && updateFuel) {
            if (livelloCarburante != null) {
                try {
                    veicolo.setLivelloCombustibile(Integer.parseInt(livelloCarburante));
                } catch (Exception e) {
                    System.out.println("carburante catch. not saved");
                }
            }

        }

//      veicolo.getVehicleDetail().setFinMaxTotalKm(kmI);

        Integer capacitaSerbatoioI = null;
        try {
            capacitaSerbatoioI = Integer.parseInt(capacitaSerbatoio);
        } catch (Exception e) {
            capacitaSerbatoioI = 0;
        }
        veicolo.setCapacitaSerbatoio(capacitaSerbatoioI);
        Boolean pulitaB = false;
        if (pulita.equalsIgnoreCase("true") || pulita.equalsIgnoreCase("si")) {
            veicolo.setPulita(!pulitaB);
        } else {
            veicolo.setPulita(pulitaB);
        }
        //Carburante
        MROldCarburante carb = null;
        qry = sx.createQuery("from MROldCarburante where descrizione = :desc");
        qry.setParameter("desc", carburante);
        if (!qry.list().isEmpty()) {
            carb = (MROldCarburante) qry.list().get(0);
        }
        if (carb == null) {
            errori.put(veicolo, "CARBURANTE MANCANTE");
            return true;
        } else {
            veicolo.setCarburante(carb);
        }
        Date fineContrattoStimataD = sdf.parse(fineContrattoStimata);
        if (fineContrattoStimataD != null) {
            veicolo.setDataScadenzaContratto(fineContrattoStimataD);
        } else {
            veicolo.setDataScadenzaContratto(sdf.parse("31/12/2050"));
        }
        Date inizioContrattoAcquistoD = sdf.parse(inizioContrattoAcquisto);
        if (inizioContrattoAcquistoD != null) {
            veicolo.setDataAcquisto(inizioContrattoAcquistoD);
        } else {
            veicolo.setDataAcquisto(sdf.parse("01/01/2000"));
        }
        Date dataUscitaFlottaD = sdf.parse(dataUscitaFlotta);
        if (dataUscitaFlottaD != null) {
            veicolo.setDataVendita(dataUscitaFlottaD);
        } else {
            veicolo.setDataVendita(sdf.parse("31/12/2025"));
        }
        //Cambio
        MROldVehicleTransmission transmission = null;
        qry = sx.createQuery("from MROldVehicleTransmission where description = :desc");
        qry.setParameter("desc", cambio);
        if (!qry.list().isEmpty()) {
            transmission = (MROldVehicleTransmission) qry.list().get(0);
        }
        if (transmission != null) {
            veicolo.getVehicleDetail().setTransmission(transmission);
        }
        veicolo.setCilindrata(cilindrata);
        Integer numPosti = null;
        try {
            numPosti = Integer.parseInt(numeroPosti);
        } catch (Exception e) {
            numPosti = 0;
        }
        veicolo.setNumeroPosti(numPosti);
        //Carrozzeria
        MROldVehicleChassis chassis = null;
        qry = sx.createQuery("from MROldVehicleChassis where description = :desc");
        qry.setParameter("desc", carrozzeria);
        if (!qry.list().isEmpty()) {
            chassis = (MROldVehicleChassis) qry.list().get(0);
        }
        if (chassis != null) {
            veicolo.getVehicleDetail().setChassis(chassis);
        }
        //Trazione
        MROldVehicleTraction traction = null;
        qry = sx.createQuery("from MROldVehicleTraction where description = :desc");
        qry.setParameter("desc", trazione);
        if (!qry.list().isEmpty()) {
            traction = (MROldVehicleTraction) qry.list().get(0);
        }
        if (traction != null) {
            veicolo.getVehicleDetail().setTraction(traction);
        }

        veicolo.setRuotaDiScorta(ruotaDiScorta);
        //Trazione
        MROldSede loc = null;
        qry = sx.createQuery("from MROldSede where descrizione = :desc");
        qry.setParameter("desc", sede);
        if (!qry.list().isEmpty()) {
            loc = (MROldSede) qry.list().get(0);
        }
        if (updateLocation != null && updateLocation) {
            if (loc != null) {
                veicolo.setSede(loc);
            }
        }
        qry = sx.createQuery("from PurchaseType where description = :type");
        qry.setParameter("type", modalitaAcq.toUpperCase());
        PurchaseType purType = (PurchaseType) qry.uniqueResult();
        if (purType != null) {
            veicolo.getVehicleDetail().setFinPurchaseTypeId(purType);
        } else {
            purType = new PurchaseType();
            purType.setDescription(modalitaAcq.toUpperCase());
            sx.saveOrUpdate(purType);
            veicolo.getVehicleDetail().setFinPurchaseTypeId(purType);
        }
        Date dataInflottamento = null;
        Date dataIniNol = null;
        try {
            dataInflottamento = sdf.parse(dataInflo);
        } catch (Exception e) {

        }
        try {
            dataIniNol = sdf.parse(dataInizioNoleggio);
        } catch (Exception e) {

        }
        if (dataInflottamento != null) {
            veicolo.getVehicleDetail().setFinFirstInstallmentEffectiDate(dataInflottamento);
            veicolo.getVehicleDetail().setFinFirstInstallmentExpectDate(dataIniNol);
        } else {
            veicolo.getVehicleDetail().setFinFirstInstallmentEffectiDate(sdf.parse("01/01/2000"));
            veicolo.getVehicleDetail().setFinFirstInstallmentExpectDate(sdf.parse("01/01/2000"));
        }
        Date dataUscita = null;
        try {
            dataUscita = sdf.parse(dataUscitaPrevista);
        } catch (Exception e) {
        }

        if (dataUscita != null) {
            veicolo.getVehicleDetail().setFinLastInstallmentDate(dataUscita);

        } else {
            veicolo.getVehicleDetail().setFinLastInstallmentDate(sdf.parse("31/12/2025"));

        }
        veicolo.setParcheggio(parcheggio);
        //Proprietario
        MROldProprietarioVeicolo prop = null;
        qry = sx.createQuery("from MROldProprietarioVeicolo where ragioneSociale = :desc");
        qry.setParameter("desc", proprietario);
        if (qry.list().isEmpty()) {
            prop = new MROldProprietarioVeicolo();
            prop.setRagioneSociale(proprietario);
            sx.saveOrUpdate(prop);
            //errori.put(veicolo, "PROPRIETARIO MANCANTE");
            //return true;
        } else {
            prop = (MROldProprietarioVeicolo) qry.list().get(0);
        }

        MROldTrackingTypeSystem typeSystem = null;
        MROldTrackingDevice dev = null;
        if (dispositivoSatellitare != null) {
            try {
                Query q = sx.createQuery("from MROldTrackingTypeSystem where description = :desc");
                q.setParameter("desc", dispositivoSatellitare);
                if (q.list().isEmpty()) {
                    typeSystem = new MROldTrackingTypeSystem();
                    typeSystem.setDescription(dispositivoSatellitare);
                    sx.saveOrUpdate(typeSystem);
                } else {
                    typeSystem = (MROldTrackingTypeSystem) q.uniqueResult();
                }
                q = sx.createQuery("from MROldTrackingDevice where trackingTypeSystemId = :device");
                q.setParameter("device", typeSystem);
                if (q.list().isEmpty()) {
                    dev = new MROldTrackingDevice();
                    dev.setTrackingTypeSystemId(typeSystem);
                    sx.saveOrUpdate(dev);
                } else {
                    if (!qry.list().isEmpty()) {
                        dev = (MROldTrackingDevice) q.list().get(0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            veicolo.getVehicleDetail().setTrackingDeviceId(dev);
        }
        veicolo.setPneumatici(pneus);
        veicolo.setMisuraPneumatici(misuraPneus);
        Boolean abilitatoB = true;
        if (abilitato.equalsIgnoreCase("false") || abilitato.equalsIgnoreCase("no")) {
            veicolo.setAbilitato(!abilitatoB);
        } else {
            veicolo.setAbilitato(abilitatoB);
        }
        sx.saveOrUpdate(veicolo);
        System.out.println(veicolo + " SALVATO");
        return false;
    }

    public static void importVehicles(Session sx, File file) {
        try {
            int idGroupIndex = 100;
            int idNoleggiatoreIndex = 100;
            int marcaIndex = 100;
            int modelloIndex = 100;
            int versioneIndex = 100;
            int targaIndex = 100;
            int exTargaIndex = 100;
            int annoimmatricolazioneIndex = 100;
            int telaioIndex = 100;
            int coloreIndex = 100;
            int portutileIndex = 100;
            int nopostiIndex = 100;
            int valoreveicoloIndex = 100;
            int kmIndex = 100;
            int promemoriaIndex = 100;
            int livellocombustibileIndex = 100;
            int capacitaserbatoioIndex = 100;
            int pulitaIndex = 100;
            int parcheggioIndex = 100;
            int idcarburanteIndex = 100;
            int idsedeIndex = 100;
            int idaffiliatoIndex = 100;
            int abilitatoIndex = 100;
            int impegnatoIndex = 100;
            int datascadenzacontrattoIndex = 100;
            int dataproroga1Index = 100;
            int dataproroga2Index = 100;
            int venditoreIndex = 100;
            int valacqIndex = 100;
            int dataacqIndex = 100;
            int noteacqIndex = 100;
            int acquirenteIndex = 100;
            int valvendIndex = 100;
            int datavendIndex = 100;
            int cambioIndex = 100;
            int sedeintrarentIndex = 100;
            int cilindrataIndex = 100;
            int carrozzeriaIndex = 100;
            int trazioneIndex = 100;
            int ruotaDiScortaIndex = 100;
            int franchaiseeIndex = 100;
            int modalitaAcquisto = 100;
            int isRentoRent = 100;
            int costoAcquistoIndex = 100;
            int importoBaseIndex = 50;

            int numeroRateIndex = 51;

            int dataInflottamentoIndex = 52;

            int dataInizioLeasingINdex = 53;

            int kmTotaliIndex = 54;

            int dataUscitaPrevistaIndex = 55;

            int fornitoreIndex = 56;

            int dispositivoSatellitareIndex = 557;

            int pneumaticiIndex = 58;

            int misuraPneumaticiIndex = 59;

            FileInputStream fileInput = new FileInputStream(file);
            String[] header = new String[100];

            HSSFWorkbook workbook = new HSSFWorkbook(fileInput);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            Iterator myItr = sheet.getRow(0).cellIterator();
            int cont = 0;
            while (myItr.hasNext()) {
                HSSFCell cell = ((HSSFCell) myItr.next());

                header[cont] = (cell.getStringCellValue());
                if (header[cont].toLowerCase().contains("id_gruppo")) {
                    idGroupIndex = 1;
                } else if (header[cont].toLowerCase().contains("id_noleggiatore")) {
                    idNoleggiatoreIndex = 2;
                } else if (header[cont].toLowerCase().contains("marca")) {
                    marcaIndex = 3;
                } else if (header[cont].toLowerCase().contains("modello")) {
                    modelloIndex = 4;
                } else if (header[cont].toLowerCase().contains("versione")) {
                    versioneIndex = 5;
                } else if (header[cont].toLowerCase().equals("targa")) {
                    targaIndex = 6;
                } else if (header[cont].toLowerCase().contains("extarga")) {
                    exTargaIndex = 7;
                } else if (header[cont].toLowerCase().contains("anno immatricolazione")) {
                    annoimmatricolazioneIndex = 8;
                } else if (header[cont].toLowerCase().contains("telaio")) {
                    telaioIndex = 9;
                } else if (header[cont].toLowerCase().contains("colore")) {
                    coloreIndex = 10;
                } else if (header[cont].toLowerCase().contains("port_utile")) {
                    portutileIndex = 11;
                } else if (header[cont].toLowerCase().contains("no_posti")) {
                    nopostiIndex = 12;
                } else if (header[cont].toLowerCase().contains("valore veicolo")) {
                    valoreveicoloIndex = 13;
                } else if (header[cont].toLowerCase().contains("km")) {
                    kmIndex = 14;
                } else if (header[cont].toLowerCase().contains("promemoria")) {
                    promemoriaIndex = 15;
//                } else if (header[cont].toLowerCase().contains("livello_combustibile")) {
//                    livellocombustibileIndex = 16;
//                }
                } else if (header[cont].toLowerCase().contains("capacita' serbatoio")) {
                    capacitaserbatoioIndex = 16;
                } else if (header[cont].toLowerCase().contains("pulita")) {
                    pulitaIndex = 17;
                } else if (header[cont].toLowerCase().contains("parcheggio")) {
                    parcheggioIndex = 18;
                } else if (header[cont].toLowerCase().contains("alimentazione")) {
                    idcarburanteIndex = 19;
                }
                if (header[cont].toLowerCase().contains("id_sede")) {
                    idsedeIndex = 20;
                } else if (header[cont].toLowerCase().contains("id_affiliato")) {
                    idaffiliatoIndex = 21;
                } else if (header[cont].toLowerCase().contains("abilitato")) {
                    abilitatoIndex = 22;
                } else if (header[cont].toLowerCase().contains("impegnato")) {
                    impegnatoIndex = 23;

                } else if (header[cont].toLowerCase().contains("data_scadenza_contratto")) {
                    datascadenzacontrattoIndex = 24;
                } else if (header[cont].toLowerCase().contains("data_proroga_1")) {
                    dataproroga1Index = 25;
                } else if (header[cont].toLowerCase().contains("data_proroga_2")) {
                    dataproroga2Index = 26;
                } else if (header[cont].toLowerCase().contains("venditore")) {
                    venditoreIndex = 27;
                } else if (header[cont].toLowerCase().contains("val_acq")) {
                    valacqIndex = 28;
                } else if (header[cont].toLowerCase().contains("data_acq")) {
                    dataacqIndex = 29;
                }
                if (header[cont].toLowerCase().contains("note_acq")) {
                    noteacqIndex = 30;
                } else if (header[cont].toLowerCase().contains("acquirente")) {
                    acquirenteIndex = 31;
                } else if (header[cont].toLowerCase().contains("data_vend")) {
                    datavendIndex = 33;
                } else if (header[cont].toLowerCase().contains("cambio")) {
                    cambioIndex = 37;
                } else if (header[cont].toLowerCase().contains("sede_intrarent")) {
                    sedeintrarentIndex = 35;
                } else if (header[cont].toLowerCase().contains("cilindrata")) {
                    cilindrataIndex = 38;
                } else if (header[cont].toLowerCase().contains("carrozzeria")) {
                    carrozzeriaIndex = 42;
                } else if (header[cont].toLowerCase().contains("trazione")) {
                    trazioneIndex = 43;
                } else if (header[cont].toLowerCase().contains("ruota di scorta")) {
                    ruotaDiScortaIndex = 44;
                }
                if (header[cont].toUpperCase().contains("FRANCHAISEE")) {
                    franchaiseeIndex = 47;
                } else if (header[cont].toLowerCase().contains("modalita' acquisto")) {
                    modalitaAcquisto = 48;
                } else if (header[cont].toLowerCase().contains("costo acquisto")) {
                    costoAcquistoIndex = 49;
                } else if (header[cont].toLowerCase().contains("importo base")) {
                    importoBaseIndex = 50;
                } else if (header[cont].toLowerCase().contains("numero rate")) {
                    numeroRateIndex = 51;
                } else if (header[cont].toLowerCase().contains("data inflottamento")) {
                    dataInflottamentoIndex = 52;
                } else if (header[cont].toLowerCase().contains("data inizio leasing/noleggio")) {
                    dataInizioLeasingINdex = 53;
                } else if (header[cont].toLowerCase().contains("kmtotali")) {
                    kmTotaliIndex = 54;
                } else if (header[cont].toLowerCase().contains("data uscita prevista")) {
                    dataUscitaPrevistaIndex = 55;
                } else if (header[cont].toLowerCase().contains("fornitore")) {
                    fornitoreIndex = 56;
                }
                if (header[cont].toLowerCase().contains("dispositivo satellitare")) {
                    dispositivoSatellitareIndex = 57;
                } else if (header[cont].toLowerCase().contains("pneumatici")) {
                    pneumaticiIndex = 58;
                } else if (header[cont].toLowerCase().contains("misura pneumatici")) {
                    misuraPneumaticiIndex = 59;
                }
                cont++;
            }
            Boolean correct = true;
            List errori = new ArrayList();
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                String targa = "";

                if (sheet.getRow(i).getCell(targaIndex).getStringCellValue() != null) {
                    targa = sheet.getRow(i).getCell(targaIndex).getStringCellValue();
                }

                String groupDescrizione;
                MROldGruppo group = null;
                try {

                    groupDescrizione = (sheet.getRow(i).getCell(idGroupIndex).getStringCellValue());
                    if (groupDescrizione != null) {
                        groupDescrizione = groupDescrizione.replaceAll("\\(", "");
                        groupDescrizione = groupDescrizione.replaceAll("\\)", "");
                        groupDescrizione = groupDescrizione.replaceAll("&", "and");
                        groupDescrizione = groupDescrizione.toUpperCase();
                        System.out.println("GRUPPO ->" + groupDescrizione);
                        if (targa.equals("EM35576")) {
                            System.out.println("debug");
                        }
                        try {
                            Query qry = sx.createQuery("select g from MROldGruppo g where descrizione like :id");
                            qry.setParameter("id", "%" + groupDescrizione + "%");
                            if (qry.list().size() == 1) {
                                group = (MROldGruppo) qry.uniqueResult();
                            } else if (qry.list().size() > 1) {
                                qry = sx.createQuery("select g from MROldGruppo g where descrizione like :id");
                                qry.setParameter("id", "%" + groupDescrizione);
                            }
                            if (qry.list().size() == 1) {
                                group = (MROldGruppo) qry.uniqueResult();
                            }
                            if (group == null && groupDescrizione.contains("SPECIAL CARS")) {
                                group = MROldGruppo.class.newInstance();
                                group.setDescrizione("SPECIAL");
                            }
                            if (group == null && groupDescrizione.contains("STATION WAGON (")) {
                                group = MROldGruppo.class.newInstance();
                                group.setDescrizione("STATION");
                            }
                            if (group == null && groupDescrizione.contains("STATION WAGON A")) {
                                group = MROldGruppo.class.newInstance();
                                group.setDescrizione("STATION A");
                            }
                            if (group == null && groupDescrizione.contains("LUXURY AND RACING A")) {
                                qry = sx.createQuery("select g from MROldGruppo g where descrizione = :id");
                                qry.setParameter("id", "LUXURY AND RACING A");
                                group = (MROldGruppo) qry.uniqueResult();
                            }
                            if (group == null && groupDescrizione.contains("LUXURY AND RACING E")) {
                                qry = sx.createQuery("select g from MROldGruppo g where descrizione = :id");
                                qry.setParameter("id", "LUXURY AND RACING E");
                                group = (MROldGruppo) qry.uniqueResult();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        System.out.println("groupid is  Mandatory");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    group = null;
                }
                String idNoleggiatore;
                MROldProprietarioVeicolo noleggiatreInstance = null;
                idNoleggiatore = (sheet.getRow(i).getCell(fornitoreIndex).getStringCellValue());
                try {
                    Query qry = sx.createQuery("select g from MROldProprietarioVeicolo g where descrizione = :id");
                    qry.setParameter("id", idNoleggiatore);
                    noleggiatreInstance = (MROldProprietarioVeicolo) qry.uniqueResult();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                String marca = "";
                try {
                    if (sheet.getRow(i).getCell(marcaIndex).getStringCellValue() != null) {
                        marca = sheet.getRow(i).getCell(marcaIndex).getStringCellValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String modello = "";
                try {
                    if (sheet.getRow(i).getCell(modelloIndex).getStringCellValue() != null) {
                        modello = sheet.getRow(i).getCell(modelloIndex).getStringCellValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Date dataimmatricolazione = null;
                String immatric = "";
                try {
                    if (sheet.getRow(i).getCell(annoimmatricolazioneIndex) != null && sheet.getRow(i).getCell(annoimmatricolazioneIndex).getDateCellValue() != null) {
                        dataimmatricolazione = sheet.getRow(i).getCell(annoimmatricolazioneIndex).getDateCellValue();
                        if (dataimmatricolazione == null) {
                            Date milleMila = new GregorianCalendar(2000, Calendar.JANUARY, 01).getTime();
                            dataimmatricolazione = milleMila;
                        }
                    } else if (sheet.getRow(i).getCell(annoimmatricolazioneIndex) != null && sheet.getRow(i).getCell(annoimmatricolazioneIndex).getStringCellValue() != null) {
                        immatric = sheet.getRow(i).getCell(annoimmatricolazioneIndex).getStringCellValue();
                        dataimmatricolazione = new SimpleDateFormat("dd/MM/yyyy").parse(immatric);
                    }
                } catch (Exception e) {
                    if (dataimmatricolazione == null) {
                        Date milleMila = new GregorianCalendar(2000, Calendar.JANUARY, 01).getTime();
                        dataimmatricolazione = milleMila;
                    }
                }

                String telaio = "";
                if (sheet.getRow(i).getCell(telaioIndex).getStringCellValue() != null) {
                    try {
                        telaio = sheet.getRow(i).getCell(telaioIndex).getStringCellValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String versione = "";
                if (sheet.getRow(i).getCell(versioneIndex).getStringCellValue() != null) {
                    try {
                        versione = sheet.getRow(i).getCell(versioneIndex).getStringCellValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                String colore = "";
                try {
                    if (sheet.getRow(i).getCell(coloreIndex).getStringCellValue() != null) {
                        colore = sheet.getRow(i).getCell(coloreIndex).getStringCellValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String portutile = "";
                try {
                    if (sheet.getRow(i).getCell(portutileIndex).getStringCellValue() != null) {
                        portutile = sheet.getRow(i).getCell(portutileIndex).getStringCellValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Integer noposti = 0;
                try {
                    noposti = (int) sheet.getRow(i).getCell(nopostiIndex).getNumericCellValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Integer km = null;
                try {

                    km = (int) (sheet.getRow(i).getCell(kmIndex).getNumericCellValue());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Double valoreVeicolo = 0.0;
                try {

                    valoreVeicolo = sheet.getRow(i).getCell(valoreveicoloIndex).getNumericCellValue();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                String promemoria = "";
                try {
                    if (sheet.getRow(i).getCell(promemoriaIndex).getStringCellValue() != null) {
                        promemoria = sheet.getRow(i).getCell(promemoriaIndex).getStringCellValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Integer capacitaserbatoio = 0;
                try {

                    capacitaserbatoio = (int) (sheet.getRow(i).getCell(capacitaserbatoioIndex).getNumericCellValue());
                } catch (Exception e) {
                    capacitaserbatoio = 0;
                }

                Boolean pulita = true;
                String pulita1 = "";
                try {
                    if (sheet.getRow(i).getCell(pulitaIndex).getStringCellValue() != null) {
                        pulita1 = sheet.getRow(i).getCell(pulitaIndex).getStringCellValue();
                        pulita = Boolean.parseBoolean(pulita1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String parcheggio = sheet.getRow(i).getCell(parcheggioIndex).getStringCellValue();

                Integer carburanteId = null;
                MROldCarburante carburanti = null;
                try {
                    carburanteId = (int) (sheet.getRow(i).getCell(idcarburanteIndex).getNumericCellValue());
                } catch (Exception e) {
                    String carburante = (sheet.getRow(i).getCell(idcarburanteIndex).getStringCellValue());
                    try {
                        Query qry = sx.createQuery("select g from MROldCarburante g where descrizione = :id");
                        qry.setParameter("id", carburante.toUpperCase());
                        carburanti = (MROldCarburante) qry.uniqueResult();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                if (carburanteId != null) {
                    try {
                        Query qry = sx.createQuery("select g from MROldCarburante g where id = :id");
                        qry.setParameter("id", carburanteId);
                        carburanti = (MROldCarburante) qry.uniqueResult();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("carburanti is  Mandatory");
                }

                //SEDE ID E' SEMRPE 1 . FRANCHISEID E' SEDE !
                String sedeId;
                MROldSede location = null;
                sedeId = (sheet.getRow(i).getCell(franchaiseeIndex).getStringCellValue());
                String[] sedeSplitted = sedeId.split(" - ");

                if (sedeId != null) {
                    try {
                        Query qry = sx.createQuery("select g from MROldSede g where descrizione like :id");
                        qry.setParameter("id", sedeSplitted[0].toUpperCase() + "%");
                        location = (MROldSede) qry.uniqueResult();
                        if (location == null) {
                            qry = sx.createQuery("select g from MROldSede g where descrizione like :id");
                            qry.setParameter("id", sedeSplitted[1].toUpperCase() + "%");
                            location = (MROldSede) qry.uniqueResult();
                        }
                        if (location == null) {
                            qry = sx.createQuery("select g from MROldSede g where citta like :id");
                            qry.setParameter("id", sedeSplitted[1].toUpperCase() + "%");
                            location = (MROldSede) qry.uniqueResult();
                        }
                        if (location == null && sedeSplitted[1].contains("Mugg")) {
                            qry = sx.createQuery("select g from MROldSede g where citta like :id");
                            qry.setParameter("id", "MUGG" + "%");
                            location = (MROldSede) qry.uniqueResult();
                        }
                        System.out.println("SEDE -> " + location);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("sede is  Mandatory");
                }
                Integer affiliatoId;
                MROldAffiliato affiliate = null;
                affiliatoId = (int) (sheet.getRow(i).getCell(idaffiliatoIndex).getNumericCellValue());
                if (affiliatoId != null) {
                    try {
                        Query qry = sx.createQuery("select g from MROldAffiliato g where id = :id");
                        qry.setParameter("id", affiliatoId);
                        affiliate = (MROldAffiliato) qry.uniqueResult();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    System.out.println("groupid is  Mandatory");
                }

                String impegnato = "";
                try {
                    if (sheet.getRow(i).getCell(impegnatoIndex) != null && sheet.getRow(i).getCell(impegnatoIndex).getStringCellValue() != null) {
                        impegnato = sheet.getRow(i).getCell(impegnatoIndex).getStringCellValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Date dataScadenzaContratto = null;
                String scadenzaContratto = "";
                try {
                    if (sheet.getRow(i).getCell(datascadenzacontrattoIndex) != null && sheet.getRow(i).getCell(datascadenzacontrattoIndex).getDateCellValue() != null) {
                        dataScadenzaContratto = sheet.getRow(i).getCell(datascadenzacontrattoIndex).getDateCellValue();
                        if (dataScadenzaContratto == null) {
                            Date milleMila = new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime();
                            dataScadenzaContratto = milleMila;
                        }
                    } else if (sheet.getRow(i).getCell(datascadenzacontrattoIndex) != null && sheet.getRow(i).getCell(datascadenzacontrattoIndex).getStringCellValue() != null) {
                        scadenzaContratto = sheet.getRow(i).getCell(datascadenzacontrattoIndex).getStringCellValue();
                        dataScadenzaContratto = new SimpleDateFormat("dd/MM/yyyy").parse(scadenzaContratto);
                    }
                } catch (Exception e) {
                    if (dataScadenzaContratto == null) {
                        Date milleMila = new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime();
                        dataScadenzaContratto = milleMila;
                    }
                }
                String venditore = null;
                Double valacq = 0.0;
                if (sheet.getRow(i).getCell(valacqIndex) != null) {
                    try {
                        valacq = sheet.getRow(i).getCell(valacqIndex).getNumericCellValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Date dataacq = null;
                String acq = "";
                try {
                    if (sheet.getRow(i).getCell(dataacqIndex) != null && sheet.getRow(i).getCell(dataacqIndex).getDateCellValue() != null) {
                        dataacq = sheet.getRow(i).getCell(dataacqIndex).getDateCellValue();
                        if (dataacq == null) {
                            Date milleMila = new GregorianCalendar(2000, Calendar.JANUARY, 01).getTime();
                            dataacq = milleMila;
                        }
                    } else if (sheet.getRow(i).getCell(dataacqIndex) != null && sheet.getRow(i).getCell(dataacqIndex).getStringCellValue() != null) {
                        acq = sheet.getRow(i).getCell(dataacqIndex).getStringCellValue();
                        dataacq = new SimpleDateFormat("dd/MM/yyyy").parse(acq);
                    }
                } catch (Exception e) {
                    if (dataacq == null) {
                        Date milleMila = new GregorianCalendar(2000, Calendar.JANUARY, 01).getTime();
                        dataacq = milleMila;
                    }
                }
                String noteacq = "";
                try {
                    if (sheet.getRow(i).getCell(noteacqIndex) != null && sheet.getRow(i).getCell(noteacqIndex).getStringCellValue() != null) {
                        noteacq = sheet.getRow(i).getCell(noteacqIndex).getStringCellValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String valvend;
                try {
                    if (sheet.getRow(i).getCell(valvendIndex) != null && sheet.getRow(i).getCell(valvendIndex).getStringCellValue() != null) {
                        valvend = sheet.getRow(i).getCell(valvendIndex).getStringCellValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String notevend;
                try {
                    if (sheet.getRow(i).getCell(cambioIndex).getStringCellValue() != null) {
                        notevend = sheet.getRow(i).getCell(cambioIndex).getStringCellValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String acquirente;
                try {
                    if (sheet.getRow(i).getCell(acquirenteIndex) != null && sheet.getRow(i).getCell(acquirenteIndex).getStringCellValue() != null) {
                        acquirente = sheet.getRow(i).getCell(acquirenteIndex).getStringCellValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Date datavend = null;
                String vend = "";
                try {
                    if (sheet.getRow(i).getCell(datavendIndex) != null && sheet.getRow(i).getCell(datavendIndex).getDateCellValue() != null) {
                        datavend = sheet.getRow(i).getCell(datavendIndex).getDateCellValue();
                        if (datavend != null && datavend.after(new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime())) {
                            Date milleMila = new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime();
                            datavend = milleMila;
                        }
                        if (datavend == null) {
                            Date milleMila = new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime();
                            datavend = milleMila;
                        }
                    } else if (sheet.getRow(i).getCell(datavendIndex) != null && sheet.getRow(i).getCell(datavendIndex).getStringCellValue() != null) {
                        vend = sheet.getRow(i).getCell(datavendIndex).getStringCellValue();
                        datavend = new SimpleDateFormat("dd/MM/yyyy").parse(vend);
                    }
                } catch (Exception e) {
                    if (datavend == null) {
                        Date milleMila = new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime();
                        datavend = milleMila;
                    }
                }

                String cambio = null;
                try {
                    cambio = sheet.getRow(i).getCell(cambioIndex).getStringCellValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String cilindrata = "";
                Integer cil;
                try {
                    cil = (int) (sheet.getRow(i).getCell(cilindrataIndex).getNumericCellValue());
                    cilindrata = cil.toString();
                } catch (Exception e) {
                    cilindrata = null;
                }

                String codiceveicolo = null;
                Boolean isBulkhead = null;
                try {
                    isBulkhead = sheet.getRow(i).getCell(ruotaDiScortaIndex).getBooleanCellValue();
                } catch (Exception e) {
                    isBulkhead = null;
                }

                Boolean abilitato = null;
                try {
                    abilitato = sheet.getRow(i).getCell(abilitatoIndex).getBooleanCellValue();
                } catch (Exception e) {
                    abilitato = true;
                }

                Boolean isPowerGridAttack = null;
                try {
                    isPowerGridAttack = sheet.getRow(i).getCell(franchaiseeIndex).getBooleanCellValue();
                } catch (Exception e) {
                    isPowerGridAttack = null;
                }

                String modAcq = null;
                try {
                    modAcq = sheet.getRow(i).getCell(modalitaAcquisto).getStringCellValue();
                } catch (Exception e) {
                    modAcq = null;
                }

                Integer numeroRate = 0;
                try {
                    numeroRate = (int) sheet.getRow(i).getCell(numeroRateIndex).getNumericCellValue();
                } catch (Exception e) {
                    numeroRate = null;
                }

                Double costoCanone = null;
                try {
                    costoCanone = sheet.getRow(i).getCell(importoBaseIndex).getNumericCellValue();
                } catch (Exception e) {
                    costoCanone = null;
                }

                Date dataInflottamento = null;
                String fDataInfottamento = "";
                try {
                    if (sheet.getRow(i).getCell(dataInflottamentoIndex) != null && sheet.getRow(i).getCell(dataInflottamentoIndex).getDateCellValue() != null) {
                        dataInflottamento = sheet.getRow(i).getCell(dataInflottamentoIndex).getDateCellValue();
                    } else if (sheet.getRow(i).getCell(dataInflottamentoIndex) != null && sheet.getRow(i).getCell(dataInflottamentoIndex).getStringCellValue() != null) {

                        fDataInfottamento = sheet.getRow(i).getCell(dataInflottamentoIndex).getStringCellValue();
                        dataInflottamento = new SimpleDateFormat("dd/MM/yyyy").parse(fDataInfottamento);
                    }
                } catch (Exception e) {
                    dataInflottamento = null;
                }


                Date dataInizioLeasing = null;
                String fDataInizioLeasing = "";
                try {
                    if (sheet.getRow(i).getCell(dataInizioLeasingINdex) != null && sheet.getRow(i).getCell(dataInizioLeasingINdex).getDateCellValue() != null) {
                        dataInizioLeasing = sheet.getRow(i).getCell(dataInizioLeasingINdex).getDateCellValue();
                    } else if (sheet.getRow(i).getCell(dataInizioLeasingINdex) != null && sheet.getRow(i).getCell(dataInizioLeasingINdex).getStringCellValue() != null) {

                        fDataInizioLeasing = sheet.getRow(i).getCell(dataInizioLeasingINdex).getStringCellValue();
                        dataInizioLeasing = new SimpleDateFormat("dd/MM/yyyy").parse(fDataInizioLeasing);
                    }
                } catch (Exception e) {
                    dataInizioLeasing = null;
                }

                Double importoCanone = 0.0;
                try {
                    importoCanone = sheet.getRow(i).getCell(dataInizioLeasingINdex).getNumericCellValue();
                } catch (Exception e) {
                    importoCanone = null;
                }

                Integer kmTotali = 0;
                try {

                    kmTotali = (int) sheet.getRow(i).getCell(kmTotaliIndex).getNumericCellValue();

                } catch (Exception e) {
                    kmTotali = 0;
                }

                Date dataUscitaPrevista = null;
                String dataUscitaString = "";
                try {
                    if (sheet.getRow(i).getCell(dataUscitaPrevistaIndex) != null && sheet.getRow(i).getCell(dataUscitaPrevistaIndex).getDateCellValue() != null) {
                        dataUscitaPrevista = sheet.getRow(i).getCell(dataUscitaPrevistaIndex).getDateCellValue();
                        if (dataUscitaPrevista == null) {
                            Date milleMila = new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime();
                            dataUscitaPrevista = milleMila;
                        }
                    } else if (sheet.getRow(i).getCell(dataUscitaPrevistaIndex) != null && sheet.getRow(i).getCell(dataUscitaPrevistaIndex).getStringCellValue() != null) {

                        dataUscitaString = sheet.getRow(i).getCell(dataUscitaPrevistaIndex).getStringCellValue();
                        dataUscitaPrevista = new SimpleDateFormat("dd/MM/yyyy").parse(dataUscitaString);
                    }
                } catch (Exception e) {
                    if (dataUscitaPrevista == null) {
                        if (numeroRate != null && dataInflottamento != null) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(dataInflottamento);
                            cal.add(Calendar.MONTH, numeroRate);
                            dataUscitaPrevista = cal.getTime();
                        } else {
                            Date milleMila = new GregorianCalendar(2025, Calendar.DECEMBER, 31).getTime();
                            dataUscitaPrevista = milleMila;
                        }

                    }
                }
                Double dataOrdineAcq = 0.0;
                try {
                    dataOrdineAcq = sheet.getRow(i).getCell(fornitoreIndex).getNumericCellValue();
                } catch (Exception e) {
                    dataOrdineAcq = null;
                }

                Date dataOrdineAFornitore = null;
                String ordineafornitore = "";
                try {
                    if (sheet.getRow(i).getCell(dispositivoSatellitareIndex) != null && sheet.getRow(i).getCell(dispositivoSatellitareIndex).getStringCellValue() != null) {
                        ordineafornitore = sheet.getRow(i).getCell(dispositivoSatellitareIndex).getStringCellValue();
                        dataOrdineAFornitore = new SimpleDateFormat("dd/MM/yyyy").parse(ordineafornitore);
                    }
                } catch (Exception e) {
                    dataOrdineAFornitore = null;
                }

                String dispositivoSatellitare = null;
                MROldTrackingTypeSystem dispositivo = null;
                try {
                    dispositivoSatellitare = sheet.getRow(i).getCell(dispositivoSatellitareIndex).getStringCellValue();
                    Query qry = sx.createQuery("from MROldTrackingTypeSystem where description = :description");
                    qry.setParameter("description", dispositivoSatellitare);
                    dispositivo = (MROldTrackingTypeSystem) qry.uniqueResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String carrozzeria = null;
                try {
                    carrozzeria = sheet.getRow(i).getCell(carrozzeriaIndex).getStringCellValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String ruotaDiScorta = null;
                try {
                    ruotaDiScorta = sheet.getRow(i).getCell(ruotaDiScortaIndex).getStringCellValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String transm = null;
                MROldVehicleTransmission transmission = null;
                try {
                    transm = sheet.getRow(i).getCell(cambioIndex).getStringCellValue();
                    Query qry = sx.createQuery("from MROldVehicleTransmission where description = :description");
                    qry.setParameter("description", transm);
                    transmission = (MROldVehicleTransmission) qry.uniqueResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                String trazione = null;
                MROldVehicleTraction traction = null;
                try {
                    trazione = sheet.getRow(i).getCell(trazioneIndex).getStringCellValue();
                    Query qry = sx.createQuery("from MROldVehicleTraction where description = :description");
                    qry.setParameter("description", trazione);
                    traction = (MROldVehicleTraction) qry.uniqueResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String chassis = null;
                MROldVehicleChassis chass = null;
                try {
                    chassis = sheet.getRow(i).getCell(carrozzeriaIndex).getStringCellValue();
                    Query qry = sx.createQuery("from MROldVehicleChassis where description = :description");
                    qry.setParameter("description", chassis);
                    chass = (MROldVehicleChassis) qry.uniqueResult();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String pneumatici = null;
                try {
                    pneumatici = sheet.getRow(i).getCell(pneumaticiIndex).getStringCellValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String misuraPneumatici = null;
                try {

                    misuraPneumatici = sheet.getRow(i).getCell(misuraPneumaticiIndex).getStringCellValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                MROldParcoVeicoli vehicleInstance;
                if (targa != null && impegnato != null) {
                    try {
                        Query qry = sx.createQuery("select g from MROldParcoVeicoli g where targa = :targa");
                        qry.setParameter("targa", targa);
                        vehicleInstance = (MROldParcoVeicoli) qry.uniqueResult();
                        List<MROldParcoVeicoli> listaToShow = new ArrayList<>();
                        if (vehicleInstance != null) {
                            if (vehicleInstance.getVehicleDetail() == null) {
                                vehicleInstance.setVehicleDetail(new MROldVehicleDetail());
                                vehicleInstance.getVehicleDetail().setParco(vehicleInstance);
                            }
                            errori = setVeicolo(sx, vehicleInstance, targa, datavend, group, noleggiatreInstance, marca, modello, versione, dataimmatricolazione, telaio, colore, noposti, valoreVeicolo, km, promemoria, capacitaserbatoio, pulita, parcheggio, carburanti, location, affiliate, abilitato, impegnato, dataScadenzaContratto, misuraPneumatici, valacq, dataacq, noteacq, cilindrata, codiceveicolo, isBulkhead, isPowerGridAttack, modAcq, costoCanone, importoCanone, errori, kmTotali, dataUscitaPrevista, dispositivo, pneumatici, misuraPneumatici, dataInflottamento, carrozzeria, transmission, ruotaDiScorta, trazione, dispositivoSatellitare, dataInizioLeasing, dataUscitaPrevista, chass, traction);
                            if (errori.isEmpty()) {
                                sx.saveOrUpdate(vehicleInstance);
                                System.out.println(i + "VEHICLE  " + targa + " UPDATED CORRECTLY");
                            } else {
                                System.out.println(i + "VEHICLE  " + targa + " NOT UPDATED CORRECTLY");
                            }
                        } else {
                            MROldParcoVeicoli v = new MROldParcoVeicoli();
                            v.setVehicleDetail(new MROldVehicleDetail());
                            v.getVehicleDetail().setParco(v);

                            errori = setVeicolo(sx, v, targa, datavend, group, noleggiatreInstance, marca, modello, versione, dataimmatricolazione, telaio, colore, noposti, valoreVeicolo, km, promemoria, capacitaserbatoio, pulita, parcheggio, carburanti, location, affiliate, abilitato, impegnato, dataScadenzaContratto, misuraPneumatici, valacq, dataacq, noteacq, cilindrata, codiceveicolo, isBulkhead, isPowerGridAttack, modAcq, costoCanone, importoCanone, errori, kmTotali, dataUscitaPrevista, dispositivo, pneumatici, misuraPneumatici, dataInflottamento, carrozzeria, transmission, ruotaDiScorta, trazione, dispositivoSatellitare, dataInizioLeasing, dataUscitaPrevista, chass, traction);
                            if (errori.isEmpty()) {
                                sx.saveOrUpdate(v);
                                System.out.println(i + "VEHICLE " + targa + " INSERTED CORRECTLY");
                            } else {
                                System.out.println(i + "VEHICLE " + targa + " NOT INSERTED CORRECTLY");
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            if (errori.isEmpty()) {
                System.out.println("Import succeeded");
//                JOptionPane.showMessageDialog(parent, "Veicoli inseriti correttamente", "import riuscito", JOptionPane.PLAIN_MESSAGE);
            } else {
                System.out.println("Import succeeded");
//                JOptionPane.showMessageDialog(parent, "Veicoli non tutti inseriti correttamente", "import riuscito", JOptionPane.PLAIN_MESSAGE);
                String targhe = "";
                for (Object s : errori) {
                    if (s instanceof MROldParcoVeicoli) {
                        targhe += ((MROldParcoVeicoli) s).getTarga();
                        if (((MROldParcoVeicoli) s).getGruppo() == null) {
                            targhe += " | GRUPPO";
                        }
                        if (((MROldParcoVeicoli) s).getDataAcquisto() == null) {
                            targhe += " | DATA ACQ";
                        }
                        if (((MROldParcoVeicoli) s).getDataScadenzaContratto() == null) {
                            targhe += " | DATA SCAD.CONTR.";
                        }
                        if (((MROldParcoVeicoli) s).getDataVendita() == null) {
                            targhe += " | DATA VEND.";
                        }
                        targhe += "\n";
                    }
                }
                System.out.println("License plates not inserted:\n" + targhe);
//                JOptionPane.showMessageDialog(parent, "Targhe non inserite:\n" + targhe, "Errore", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List setVeicolo(Session sx, MROldParcoVeicoli vehicleInstance, String targa, Date datavend, MROldGruppo group, MROldProprietarioVeicolo noleggiatreInstance, String marca, String modello, String versione, Date dataimmatricolazione, String telaio, String colore, Integer noposti, Double valoreVeicolo, Integer km, String promemoria, Integer capacitaserbatoio, Boolean pulita, String parcheggio, MROldCarburante carburanti, MROldSede location, MROldAffiliato affiliate, Boolean abilitato, String impegnato, Date dataScadenzaContratto, String venditore, Double valacq, Date dataacq, String noteacq, String cilindrata, String codiceveicolo, Boolean isBulkhead, Boolean isPowerGridAttack, String modAcq, Double costoCanone, Double importoCanone, List errori, Integer kmTotali, Date fineKPRE, MROldTrackingTypeSystem dispositivo, String pneus, String misuraPneus, Date dataInflottamento, String carrozzeria, MROldVehicleTransmission cambios, String ruotaDiScorta, String traction, String dispositivoSatellitare, Date dataInizioLeasing, Date dataUscitaPrevista, MROldVehicleChassis chass, MROldVehicleTraction trac) {
        Boolean checkError = false;
        errori = new ArrayList();
        vehicleInstance.setTarga(targa);
        if (group != null) {
            if (group.getId() != null) {
                vehicleInstance.setGruppo(group);
            } else if (group.getDescrizione().equals("SPECIAL")) {
                try {
                    group = new MROldGruppo();
                    group.setDescrizione("SPECIAL CARS");
                    group.setCodiceNazionale("SPECIAL");
                    group.setCodiceInternazionale("SPECIAL");
                    group.setWebDescrizione("SPECIAL CARS");
                    group.setIsFoto1Set(false);
                    group.setSpeciale(false);
                    sx.saveOrUpdate(group);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            checkError = true;
        }
        MROldTrackingDevice dev = null;
        MROldTrackingTypeSystem typeSystem = null;
        if (dispositivo == null) {
            if (dispositivoSatellitare != null) {
                try {
                    Query q = sx.createQuery("from MROldTrackingTypeSystem where description = :desc");
                    q.setParameter("desc", dispositivoSatellitare);
                    if (q.list().isEmpty()) {
                        typeSystem = new MROldTrackingTypeSystem();
                        typeSystem.setDescription(dispositivoSatellitare);
                        sx.saveOrUpdate(typeSystem);
                    } else {
                        typeSystem = (MROldTrackingTypeSystem) q.uniqueResult();
                    }
                    q = sx.createQuery("from MROldTrackingDevice where trackingTypeSystemId = :device");
                    q.setParameter("device", typeSystem);
                    if (q.list().isEmpty()) {
                        dev = new MROldTrackingDevice();
                        dev.setTrackingTypeSystemId(typeSystem);
                        sx.saveOrUpdate(dev);
                    } else {
                        dev = (MROldTrackingDevice) q.uniqueResult();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        MROldVehicleTransmission tranx = null;
        if (cambios == null) {
            if (traction != null) {
                try {
                    Query q = sx.createQuery("from MROldVehicleTransmission where description = :desc");
                    q.setParameter("desc", traction);
                    if (q.list().isEmpty()) {
                        tranx = new MROldVehicleTransmission();
                        tranx.setDescription(traction);
                        sx.saveOrUpdate(tranx);
                    } else {
                        tranx = (MROldVehicleTransmission) q.uniqueResult();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        vehicleInstance.setProprietario(noleggiatreInstance);
        vehicleInstance.setMarca(marca);
        vehicleInstance.setModello(modello);
        if (versione != null || versione.equals("")) {
            vehicleInstance.setVersione(versione);
        } else {
            vehicleInstance.setVersione(".");
        }
        vehicleInstance.setTarga(targa);
        if (dataimmatricolazione != null) {
            vehicleInstance.setDataImmatricolazione(dataimmatricolazione);
        } else {
            vehicleInstance.setDataImmatricolazione(null);
        }
        vehicleInstance.setTelaio(telaio);
        vehicleInstance.setColore(colore);

        vehicleInstance.setNumeroPosti(noposti);
        vehicleInstance.setValoreVeicolo(valoreVeicolo);
        vehicleInstance.setKm(km);
        vehicleInstance.setPromemoria(promemoria);
        vehicleInstance.setLivelloCombustibile(0);
        if (capacitaserbatoio != null && capacitaserbatoio != 0) {
            vehicleInstance.setCapacitaSerbatoio(capacitaserbatoio);
        } else {
            vehicleInstance.setCapacitaSerbatoio(0);
        }
        vehicleInstance.setPulita(pulita);
        vehicleInstance.setParcheggio(parcheggio);
        if (carburanti != null) {
            vehicleInstance.setCarburante(carburanti);
        } else {
            Query qry = sx.createQuery("from MROldCarburante where id = 1");
            vehicleInstance.setCarburante((MROldCarburante) qry.uniqueResult());
        }
        if (location != null) {
            vehicleInstance.setSede(location);
        } else {
            vehicleInstance.setSede(null);
        }
        if (affiliate != null) {
            vehicleInstance.setAffiliato(affiliate);
        } else {
            Query qry = sx.createQuery("from MROldAffiliato where id = 1");
            vehicleInstance.setAffiliato((MROldAffiliato) qry.uniqueResult());
        }
        vehicleInstance.setAbilitato(abilitato);
        vehicleInstance.setImpegnato(Boolean.parseBoolean(impegnato));
        if (dataScadenzaContratto != null) {
            vehicleInstance.setDataScadenzaContratto(dataScadenzaContratto);
        } else {
            checkError = true;
        }
        vehicleInstance.setVenditore(venditore);
        vehicleInstance.setValoreAcquisto(valacq);
        if (dataacq != null) {
            vehicleInstance.setDataAcquisto(dataacq);
        } else {
            checkError = true;
        }
        if (datavend != null) {
            vehicleInstance.setDataVendita(datavend);
        } else {
            checkError = true;
        }
        vehicleInstance.setNoteVendita(noteacq);
        if (cilindrata != null) {
            vehicleInstance.setCilindrata(cilindrata.toString());
        }
        if (codiceveicolo != null) {
            vehicleInstance.setCodiceVeicolo(codiceveicolo);
        }
        vehicleInstance.setIsBulkhead(isBulkhead);
        vehicleInstance.setIsPowerGridAttack(isPowerGridAttack);

        if (carrozzeria.toUpperCase().contains("N.D")) {
            vehicleInstance.setCarrozzeria(null);
        } else {
            vehicleInstance.setCarrozzeria(carrozzeria);
        }

        if (carrozzeria.toUpperCase().contains("N.D")) {
            vehicleInstance.getVehicleDetail().setChassis(null);
        } else {
            vehicleInstance.getVehicleDetail().setChassis(chass);
        }

        if (trac != null) {
            vehicleInstance.getVehicleDetail().setTraction(trac);
        } else {
            vehicleInstance.getVehicleDetail().setTraction(null);
        }

        vehicleInstance.getVehicleDetail().setTransmission(cambios);
        vehicleInstance.setAbilitato(true);

        if (ruotaDiScorta.toUpperCase().contains("N.D")) {
            vehicleInstance.setRuotaDiScorta(null);
        } else {
            vehicleInstance.setRuotaDiScorta(ruotaDiScorta);
        }
        Query qry = sx.createQuery("from MROldTrackingDevice where trackingTypeSystemId = :type");
        qry.setParameter("type", dispositivo);
        MROldTrackingDevice deviz = (MROldTrackingDevice) qry.uniqueResult();
        vehicleInstance.getVehicleDetail().setTrackingDeviceId(deviz);
        if (kmTotali != null) {
            Double maxDoubleKm = new Double(kmTotali);

            vehicleInstance.getVehicleDetail().setFinMaxTotalKm(maxDoubleKm);
        }
        vehicleInstance.getVehicleDetail().setFinFirstInstallmentEffectiDate(dataInizioLeasing);
        vehicleInstance.getVehicleDetail().setFinFirstInstallmentExpectDate(dataInizioLeasing);
        vehicleInstance.getVehicleDetail().setFinLastInstallmentDate(dataUscitaPrevista);
        vehicleInstance.getVehicleDetail().setFinTotalMonthlyFee(costoCanone);

        qry = sx.createQuery("from PurchaseType where description = :type");
        qry.setParameter("type", modAcq.toUpperCase());

        PurchaseType purType = (PurchaseType) qry.uniqueResult();
        if (purType != null) {
            vehicleInstance.getVehicleDetail().setFinPurchaseTypeId(purType);
        } else {
            purType = new PurchaseType();
            purType.setDescription(modAcq.toUpperCase());
            sx.saveOrUpdate(purType);
            vehicleInstance.getVehicleDetail().setFinPurchaseTypeId(purType);
        }
        vehicleInstance.getVehicleDetail().setTrackingDeviceId(deviz);
        if (pneus.toUpperCase().contains("N.D")) {
            vehicleInstance.setPneumatici(null);
        } else {
            vehicleInstance.setPneumatici(pneus);
        }

        if (misuraPneus.toUpperCase().contains("N.D")) {
            vehicleInstance.setMisuraPneumatici(null);
        } else {
            vehicleInstance.setMisuraPneumatici(misuraPneus);
        }

        if (checkError) {
            errori.add(vehicleInstance);
        }
        return errori;
    }

}
