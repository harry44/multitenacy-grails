/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.aessepi.myrentcs.utils;

/**
 *
 * @author mauro
 */
public class FormulaCalcoloNoleggio {

    /***
     * Nuova formula
     *
     *
     *      *
     *
     * Costanti

    totaleOptionalFissiEGiornalieriConIvaApplicabile = si = y5
    totaleOptionalFissiEGiornalieriSenzaIvaApplicabile = s = y4
    totaleQuotePercentualiConIvaApplicabile = spi = y3
    totaleQuotePercentualiSenzaIvaApplicabile = sp = y2
    oneriAeroportuali = 0.16 = y1
    importoTariffa = y0

    Incognite
    totaleImponibileConIvaApplicabileAnteOneriAeroportuali = a
    totaleImponibileConIvaApplicabile = b
    totaleImponibileSenzaIvaApplicabile = c
    importoTempoKm = d

    totaleImponibileConIvaApplicabileAnteOneriAeroportuali = totaleOptionalFissiEGiornalieriConIvaApplicabile + totaleQuotePercentualiConIvaApplicabile * (totaleImponibileConIvaApplicabileAnteOneriAeroportuali - totaleOptionalFissiEGiornalieriConIvaApplicabile) + importoTempoKm;
    totaleImponibileConIvaApplicabile = (totaleImponibileConIvaApplicabileAnteOneriAeroportuali * (1 + oneriAeroportuali));
    totaleImponibileSenzaIvaApplicabile = totaleOptionalFissiEGiornalieriSenzaIvaApplicabile + totaleQuotePercentualiSenzaIvaApplicabile * (totaleImponibileSenzaIvaApplicabile - totaleOptionalFissiEGiornalieriSenzaIvaApplicabile)
    importoTariffa = totaleImponibileConIvaApplicabile *( aliquotaIva +1 )+ totaleImponibileSenzaIvaApplicabile;

    a= y5 +y3 * (a – y5) + d
    b = a*(1 + y1)
    c = y4 +y2 * (c - y4)
    y0 = b ( aliquotaiva + 1) + c

    Risolto, si ottiene:
    a = (y5 * (1 -y3)  + d) / (1 – y3)
    b = (1 + y1) * (y5 * (1 -y3)  + d) / (1 – y3)
    c =  y4
    d =

    Per avere d, risolvere
    y0 = b ( aliquotaiva + 1) + c
    sostituendo b e c.

    Abbiamo quindi
    y0 = ( (1+y1) * (y5 * (1 – y3) + d) * 1/ (1 – y3) ) * ( aliquotaiva +1 ) + y4
    d = {[ (y0 -y4) / (1 + aliquota) ] * ( 1 – y3) + y5 * ( y3 * y1 + y3 – y1 –1 )} * 1/( 1 + y1 )

     *
     *
     * 
     *
     */
    /**
     * Vecchia formula
     * x= (importoTariffa - si * (1.0 + spi + sp + iva * spi + iva) + s * (1.0 + spi + sp + iva * spi)) / (1.0 + spi + sp + iva * spi + iva) / numeroGiorni;
     */
    public static double calcolaImportoGiornalieroNewWay(double importoTariffa, double oneriAeroportuali,
            double totaleQuotePercentualiSenzaIvaApplicabile, double totaleQuotePercentualiConIvaApplicabile,
            double totaleOptionalFissiEGiornalieriSenzaIvaApplicabile, double totaleOptionalFissiEGiornalieriConIvaApplicabile,
            double aliquotaIva, double numeroGiorni) {
        double y0, y1, y2, y3, y4, y5, d;
        y5 = totaleOptionalFissiEGiornalieriConIvaApplicabile;
        y4 = totaleOptionalFissiEGiornalieriSenzaIvaApplicabile;
        y3 = totaleQuotePercentualiConIvaApplicabile;
        y2 = totaleQuotePercentualiSenzaIvaApplicabile;
        y1 = oneriAeroportuali;
        y0 = importoTariffa;

        d = (((y0 -y4)/(1 + aliquotaIva)) * (1 - y3) + y5 * ( y3 * y1 + y3 - y1 -1 )) * (1/ ( 1 + y1 )) * (1 / numeroGiorni);

        return d;
    }

    public static double calcolaImportoGiornalieroOldWay(double importoTariffa, double numeroGiorni, double s, double si, 
            double sp, double spi, double iva) {

        double x;
        x= (importoTariffa - si * (1.0 + spi + sp + iva * spi + iva) + s * (1.0 + spi + sp + iva * spi)) / (1.0 + spi + sp + iva * spi + iva) / numeroGiorni;
        return x;
    }


    /**
     * (Session sx, Tariffa tariffa, boolean ivaInclusa, double aliquotaIva, double importoTariffa, double numeroGiorni, boolean giornoExtra) throws HibernateException

     * @param myTariffa
     * @return
     */

 
}
