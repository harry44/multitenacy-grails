package it.myrent.ee.api.utils;

import it.myrent.ee.db.MROldCodiceFiscale;
import it.myrent.ee.db.MROldComuni;
import it.myrent.ee.db.MROldNazioneISO;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Shivangani on 10/6/2017.
 */
public class FiscalCodeCalculator {
    //
    String cifredYearOfBirth;
    String cifredMonth;
    String cifredDays;
    String cifredPlaceOfBirth ;
    String fiscalCode;
    Session sx;
    //
    MROldComuni comune;

    public FiscalCodeCalculator(String fiscalCode, Session sx){
        setNewFiscalCode(fiscalCode);
        this.fiscalCode = fiscalCode;
        this.sx = sx;
    }

    public void setNewFiscalCode(String fiscalCode){
        if(fiscalCode.length() == 16){
            this.fiscalCode = fiscalCode;
            String strb = fiscalCode;
            cifredYearOfBirth = strb.substring(6, 8);
            cifredMonth = strb.substring(8, 9);
            cifredDays = strb.substring(9, 11);
            cifredPlaceOfBirth = strb.substring(11, 15);
            comune = getPlaceOfBirth();
        }

    }

    private MROldComuni getPlaceOfBirth(){
        if(!cifredPlaceOfBirth.startsWith("Z")){
            MROldComuni focusComune = (MROldComuni) sx.createCriteria(MROldComuni.class).add(Restrictions.
                    eq("codiceStatistico", cifredPlaceOfBirth)).setCacheable(true).setMaxResults(1).uniqueResult();

            return focusComune;
        }
        else {
            return null;
        }

    }
    /**
     *
     * @return a String of 2 letter of the country
     */
    public String getNationality(){
        String codeNation = "IT";
        comune = getPlaceOfBirth();
        if(comune != null){
            String strb = comune.getNazione();
            if(strb != null && strb.length() >= 2){
                codeNation = strb.substring(0, 2);
            }
            else{
                System.out.println("codice nazione iso sbagliato comune.getNazione() = " +comune.getNazione());
            }
        }
        else if(cifredPlaceOfBirth != null && cifredPlaceOfBirth.startsWith("Z")){

            MROldCodiceFiscale focusNazione = (MROldCodiceFiscale) sx.createCriteria(MROldCodiceFiscale.class).setCacheable(true).add(Restrictions.
                    eq("codice", cifredPlaceOfBirth)).setCacheable(true).setMaxResults(1).uniqueResult();
            if(focusNazione != null){
                MROldNazioneISO focusNation = (MROldNazioneISO) sx.createCriteria(MROldNazioneISO.class).add(Restrictions.
                        eq("nome", focusNazione.getLuogo())).setCacheable(true).setMaxResults(1).uniqueResult();
                if(focusNation != null){
                    codeNation = focusNation.getCodice();
                }
                else{
                    System.out.println("searched place is = " + focusNazione.getLuogo() + " cifredPlaceOfBirth = " +cifredPlaceOfBirth);
                }

            }
        }
        return codeNation;
    }

    public String getCap(){
        if(comune != null){
            return comune.getCap();
        }
        return "60020";
    }

    public String getProv(){
        if(comune != null){

            return comune.getProvincia();
        }
        return "AN";
    }

    public String getCity(){
        if(comune != null){
            return comune.getComune();
        }
        return "Polverigi";
    }

    public String getMaleOrFemale(){
        Integer response = 1;
        if(Integer.parseInt(cifredDays) > 31){
            response = 2;
        }
        return String.valueOf(response);
    }

    /**
     *
     * @return a dd/mm/yyyy Format Date
     */
    public String getDateOfBirth(){

        SimpleDateFormat sdf = new SimpleDateFormat("yy");
        String nowDate = sdf.format(new Date());

        if(Integer.parseInt(cifredYearOfBirth) > Integer.parseInt(nowDate)){

            cifredYearOfBirth = "19"+cifredYearOfBirth;
        }
        else{
            cifredYearOfBirth = "20"+cifredYearOfBirth;
        }

        String month = "";
        if(cifredMonth.equals("A")){
            month = "01";
        }
        else if(cifredMonth.equals("B")){
            month = "02";
        }
        else if(cifredMonth.equals("C")){
            month = "03";
        }
        else if(cifredMonth.equals("D")){
            month = "04";
        }
        else if(cifredMonth.equals("E")){
            month = "05";
        }
        else if(cifredMonth.equals("H")){
            month = "06";
        }
        else if(cifredMonth.equals("L")){
            month = "07";
        }
        else if(cifredMonth.equals("M")){
            month = "08";
        }
        else if(cifredMonth.equals("P")){
            month = "09";
        }
        else if(cifredMonth.equals("R")){
            month = "10";
        }
        else if(cifredMonth.equals("S")){
            month = "11";
        }
        else if(cifredMonth.equals("T")){
            month = "12";
        }

        if(getMaleOrFemale().equals("2")){
            Integer focusInt = Integer.parseInt(cifredDays) - 40;
            cifredDays = String.valueOf(focusInt);
            if(cifredDays.length() == 1){
                cifredDays = "0"+cifredDays;
            }
        }

        return cifredDays+"/"+month+"/"+cifredYearOfBirth;
    }


}

