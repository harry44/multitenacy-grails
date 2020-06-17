package it.myrent.ee.plugin;

import it.aessepi.myrentcs.utils.ContabUtils;
import it.aessepi.utils.BundleUtils;
import it.aessepi.utils.MathUtils;
import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.api.preferences.Preferenze;
import it.myrent.ee.db.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Bharti on 9/6/2019.
 */
public class EsportazioneFatturato {
    private static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/utils/Bundle");

    /**
     * Esportazione del fatturato in un array a due dimensioni.
     * Compila i nomi delle colonne nell'array delle descrizioni,
     * che viene ripulito ogni volta.
     * @param contratti
     * @param sedi
     * @param optionals
     * @param descrizioni Array da riempire con i nomi delle colonne.
     * @param prepagato
     * @return
     * @throws HibernateException
     */
    public Object[][] esportazioneFatturatoSedeGruppo(Session sx,
            Iterator contratti,
            List sedi,
            List optionals,
            List descrizioni,
            Boolean prepagato,
            Date inizioPeriodo,
            Date finePeriodo)
            throws HibernateException {
        descrizioni.clear();
        descrizioni.add(bundle.getString("EsportazioneFatturato.msgNoli"));
        descrizioni.add(bundle.getString("EsportazioneFatturato.msgGiorni"));
        descrizioni.add(bundle.getString("EsportazioneFatturato.msgTempoKms"));
        descrizioni.add(bundle.getString("EsportazioneFatturato.msgCarburante"));
        descrizioni.add(bundle.getString("EsportazioneFatturato.msgFranchigie"));
        for (int i = 0; i < optionals.size(); i++) {
            MROldOptional optional = (MROldOptional) optionals.get(i);
            if (!descrizioni.contains(optional.getCodice())) {
                descrizioni.add(optional.getCodice());
            }
        }
        descrizioni.add(bundle.getString("EsportazioneFatturato.msgNA"));
        descrizioni.add(bundle.getString("EsportazioneFatturato.msgImponibile"));
        descrizioni.add(bundle.getString("EsportazioneFatturato.msgImposta"));
        descrizioni.add(bundle.getString("EsportazioneFatturato.msgTotale"));
        Object[][] fatturati = new Object[sedi.size()][descrizioni.size()];
        int index = 0;
        Set fattureTotali = new HashSet();
        while (contratti.hasNext()) {
//            if (getProgress() != null) {
//                getProgress().setLabel(MessageFormat.format(bundle.getString("EsportazioneFatturato.msgConteggioRA0Di1"), index + 1, getProgress().getMaximum()));
//            }

            MROldContrattoNoleggio contratto = (MROldContrattoNoleggio) contratti.next();

            int sede = sedi.indexOf(contratto.getSedeUscita());

            Integer noli = (Integer) fatturati[sede][0];
            if (noli == null) {
                noli = new Integer(0);
            }

            fatturati[sede][0] = new Integer(noli.intValue() + 1);

            Integer giorni = (Integer) fatturati[sede][1];
            if (giorni == null) {
                giorni = new Integer(0);
            }

            int giorniPrepagato = 0;
            if (contratto.getCommissione().getGiorniVoucher() != null) {
                giorniPrepagato = contratto.getCommissione().getGiorniVoucher().intValue();
            }

            Date inizio = contratto.getInizio();
            Integer ritardoMassimoMinuti = Preferenze.getContrattoRitardoMassimoMinuti(sx);
            Date fine = FormattedDate.add(contratto.getFine(), Calendar.MINUTE, -ritardoMassimoMinuti.intValue());
            int giorniNoleggio = (int) Math.max(1.0, Math.ceil(FormattedDate.numeroGiorni(inizio, fine, true)));

            fatturati[sede][1] = new Integer(giorni.intValue() + giorniNoleggio);

            Set fattureFiltrate = new HashSet();

            Iterator fattureContratto = contratto.getDocumentiFiscali().iterator();

            while (fattureContratto.hasNext()) {
                MROldDocumentoFiscale aFattura = (MROldDocumentoFiscale) fattureContratto.next();
                if (prepagato == null || prepagato.equals(aFattura.getPrepagato())) {
                    if (!fattureTotali.contains(aFattura)) {
                        if (inizioPeriodo == null
                                || finePeriodo == null
                                || (aFattura.getData().getTime() >= inizioPeriodo.getTime()
                                && aFattura.getData().getTime() <= finePeriodo.getTime())) {
                            fattureFiltrate.add(aFattura);
                        }
                    }
                }
            }

            Iterator<MROldRataContratto> fattureRiepilogative = contratto.getFattureRiepilogative().iterator();
            while (fattureRiepilogative.hasNext()) {
                MROldDocumentoFiscale aFattura = fattureRiepilogative.next().getFattura();
                if (prepagato == null || prepagato.equals(aFattura.getPrepagato())) {
                    if (!fattureTotali.contains(aFattura)) {
                        if (inizioPeriodo == null
                                || finePeriodo == null
                                || (aFattura.getData().getTime() >= inizioPeriodo.getTime()
                                && aFattura.getData().getTime() <= finePeriodo.getTime())) {
                            fattureFiltrate.add(aFattura);
                        }
                    }
                }
            }

            fattureTotali.addAll(fattureFiltrate);

            Iterator elencoFinaleFatture = fattureFiltrate.iterator();

            while (elencoFinaleFatture.hasNext()) {
                MROldDocumentoFiscale fattura = (MROldDocumentoFiscale) elencoFinaleFatture.next();

                Iterator righe = fattura.getFatturaRighe().iterator();
                Double totaleImponibile = fattura.getTotaleImponibile();
                Double totaleIva = fattura.getTotaleIva();
                Double totaleFattura = fattura.getTotaleFattura();

                while (righe.hasNext()) {
                    MROldRigaDocumentoFiscale riga = (MROldRigaDocumentoFiscale) righe.next();
                    if (!ContabUtils.isRigaDescrittiva(riga)) {
                        int colonna = -1;

                        if (riga.getTempoKm()!=null&& (riga.getTempoKm().equals(Boolean.TRUE) || riga.getTempoExtra().equals(Boolean.TRUE))) {
                            colonna = 2;
                        } else if (riga.getCarburante() != null) {
                            colonna = 3;
                        } else if (riga.getFranchigia().equals(Boolean.TRUE)) {
                            colonna = 4;
                        } else if (riga.getOptional() != null) {
                            colonna = descrizioni.indexOf(riga.getOptional().getCodice());
                        }

                        if (colonna == -1) {
                            colonna = descrizioni.indexOf(bundle.getString("EsportazioneFatturato.msgNA"));
                        }

                        Double importo = (Double) fatturati[sede][colonna];
                        if (importo == null) {
                            importo = new Double(0);
                        }

                        if (riga.getFattura().getTipoDocumento().equals(MROldDocumentoFiscale.NC)) {
                            fatturati[sede][colonna] =
                                    MathUtils.roundDouble(importo.doubleValue() - riga.getTotaleImponibileRiga().doubleValue(), 5);
                        } else {
                            fatturati[sede][colonna] =
                                    MathUtils.roundDouble(importo.doubleValue() + riga.getTotaleImponibileRiga().doubleValue(), 5);
                        }
                    }
                }

                Double imponibile = (Double) fatturati[sede][descrizioni.size() - 3];
                if (imponibile == null) {
                    imponibile = new Double(0);
                }

                if (fattura.getTipoDocumento().equals(MROldDocumentoFiscale.NC)) {
                    fatturati[sede][descrizioni.size() - 3] =
                            MathUtils.roundDouble(imponibile.doubleValue() - totaleImponibile.doubleValue(), 2);
                } else {
                    fatturati[sede][descrizioni.size() - 3] =
                            MathUtils.roundDouble(imponibile.doubleValue() + totaleImponibile.doubleValue(), 2);
                }

                Double imposta = (Double) fatturati[sede][descrizioni.size() - 2];
                if (imposta == null) {
                    imposta = new Double(0);
                }
                if (fattura.getTipoDocumento().equals(MROldDocumentoFiscale.NC)) {
                    fatturati[sede][descrizioni.size() - 2] =
                            MathUtils.roundDouble(imposta.doubleValue() - totaleIva.doubleValue(), 2);
                } else {
                    fatturati[sede][descrizioni.size() - 2] =
                            MathUtils.roundDouble(imposta.doubleValue() + totaleIva.doubleValue(), 2);
                }

                Double totale = (Double) fatturati[sede][descrizioni.size() - 1];
                if (totale == null) {
                    totale = new Double(0);
                }
                if (fattura.getTipoDocumento().equals(MROldDocumentoFiscale.NC)) {
                    fatturati[sede][descrizioni.size() - 1] =
                            MathUtils.roundDouble(totale.doubleValue() - totaleFattura.doubleValue(), 2);
                } else {
                    fatturati[sede][descrizioni.size() - 1] =
                            MathUtils.roundDouble(totale.doubleValue() + totaleFattura.doubleValue(), 2);
                }
            }
//            if (getProgress() != null) {
//                if (index < getProgress().getMaximum()) {
//                    getProgress().aggiornaProgressBar(++index);
//                }
//            }
        }
        return fatturati;
    }



    /**
     *
     * @param inizio
     * @param fine
     * @param fatturati
     * @param sedi
     * @param gruppi
     * @param colonne
     * @param file
     * @return
     */
    public Workbook esportazioneXlsFatturatoSedeGruppoNew(Date inizio, Date fine, Object[][] fatturati, List sedi, List gruppi, List colonne) {
        Workbook workbook = null;
        try {
            workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet(bundle.getString("EsportazioneFatturato.msgFATTURATO"));

            int row = 0;
            int col = 0;
            Row titolo = sheet.createRow(row++);
            titolo.createCell((short) col++, Cell.CELL_TYPE_STRING).setCellValue(MessageFormat.format(bundle.getString("EsportazioneFatturato.msgFATTURATO_STAZIONI_DATA0_DATA1"), inizio, fine));
            for (int c = 0; c < colonne.size(); c++) {
                titolo.createCell((short) col++, Cell.CELL_TYPE_STRING);
            }
            col = 0;
            Row nomiColonne = sheet.createRow(row++);
            nomiColonne.createCell((short) col++, Cell.CELL_TYPE_STRING).setCellValue(bundle.getString("EsportazioneFatturato.msgStazione"));
            for (int c = 0; c < colonne.size(); c++) {
                nomiColonne.createCell((short) col++, Cell.CELL_TYPE_STRING).setCellValue(colonne.get(c).toString());
            }
            for (int s = 0; s < sedi.size(); s++) {
                col = 0;
                Row sede = sheet.createRow(row++);
                sede.createCell((short) col++, Cell.CELL_TYPE_STRING).setCellValue(sedi.get(s).toString());
                for (int c = 0; c < colonne.size(); c++) {
                    Number importo = (Number) fatturati[s][c];

                    if (importo != null && importo.doubleValue() != 0.0) {
                        sede.createCell((short) col++, Cell.CELL_TYPE_NUMERIC).setCellValue(importo.doubleValue());
                    } else {
                        sede.createCell((short) col++, Cell.CELL_TYPE_NUMERIC);
                    }
                }
            }
            sheet.autoSizeColumn((short) 0);

            CellRangeAddress crs = new CellRangeAddress(0, (short) 0, 0, (short) colonne.size());

            sheet.addMergedRegion(crs);

            Row totali = sheet.createRow(row++);
            col = 0;
            totali.createCell((short) col++, Cell.CELL_TYPE_STRING).setCellValue(bundle.getString("EsportazioneFatturato.msgTOTALI"));
            for (int c = 0; c < colonne.size(); c++) {
                int colIndex = col++;
                Cell aTotale = totali.createCell((short) colIndex, Cell.CELL_TYPE_FORMULA);
                String colName = columnNameForIndex(colIndex);
                aTotale.setCellFormula(new String("SUM(" + colName + "3:" + colName + (row - 1) + ")")); //NOI18N
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return workbook;
    }

    public static String columnNameForIndex(int index) {
        char[] letters = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'}; //NOI18N
        String colname = ""; //NOI18N
        if (index < letters.length) {
            return new String(new char[]{letters[index]});
        } else {
            String firstLetter = Character.toString(letters[index % letters.length]);
            String secondLetter = Character.toString(letters[index % letters.length]);
            String zeroLetter ="A";
            if (index<702)
                return firstLetter + secondLetter;
            else
                return zeroLetter + firstLetter + secondLetter;
        }
    }
}
