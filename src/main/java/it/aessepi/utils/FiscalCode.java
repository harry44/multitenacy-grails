/*
 * FiscalCode.java
 *
 * Created on 7 gennaio 2005, 11.39
 */

package it.aessepi.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author  leonardo
 */

public class FiscalCode {
    
    /** Creates a new instance of FiscalCode */
    
    private static String vocali="AEIOU"; //NOI18N
    private String cognome, nome, codiceComune;
    private Date datanascita;
    private boolean mf;

    
    public FiscalCode() {
        
    }
    
    public void setNome(String nome){
        this.nome=nome;
    }
    
     public void setCognome(String cognome){
        this.cognome=cognome;
    }
    
      public void setCodiceComune(String codice){
        this.codiceComune=codice;
    }
       public void setDataNascita(Date datanascita){
        this.datanascita=datanascita;
    }
       
        public void setSesso(boolean mf){
        this.mf=mf;
    }
     
     public static String perCognome(String cognome) {
         cognome = rimuoviCaratteriNonValidi(cognome);
         int j=1;
         String code=""; //NOI18N
         while((j<=cognome.length())&&(code.length()<3)){
             if(vocali.indexOf(cognome.substring(j-1,j))==-1){
                 code+=cognome.substring(j-1,j);
                 
             }
             
             j++;
         }
         j=1;
         while((j<=cognome.length())&&(code.length()<3)){
             if(vocali.indexOf(cognome.substring(j-1,j))!=-1){
                 code+=cognome.substring(j-1,j);
                
             }
             
             j++;
         }
         
         if(code.length()<3){
             code+="XXX".substring(0,3-code.length()); //NOI18N
         }
         
    return code;
  }
     
     
     public static String perNome(String nome) {
         nome = rimuoviCaratteriNonValidi(nome);
         int j=1;
         String code=""; //NOI18N
         while((j<=nome.length())&&(code.length()<4)){
             if(vocali.indexOf(nome.substring(j-1,j))==-1){
                 code+=nome.substring(j-1,j);
                
             }
             
             j++;
         }
     
         
         if (code.length()==4){
             return code.substring(0,1)+code.substring(2,4);
         }
          j=1;
         
         while((j<=nome.length())&&(code.length()<3)){
             if(vocali.indexOf(nome.substring(j-1,j))!=-1){
                 code+=nome.substring(j-1,j);
                
             }
             
             j++;
         }
        
         if(code.length()<3){
             code+="XXX".substring(0,3-code.length()); //NOI18N
         }
         
         return code;
         }
     
     private String perData(Date data, boolean mf){
      String[] mesi={"A","B","C","D","E","H","L","M","P","R","S","T"}; //NOI18N
        String code="",str=""; //NOI18N
         try {
         SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy"); //NOI18N
         str=sd.format(data);
         code=str.substring(8,10);
         code+=mesi[Integer.parseInt(str.substring(0,2))-1];
         String tmp=str.substring(3,5);
         //System.out.println(mf);
         if(mf)code+=tmp;
         else code+=String.valueOf(Integer.parseInt(tmp)+40);
         } catch (Exception e){}
         return code;
     }
     
     /*private String perComune(String comune){
         File f1= new File("codici.csv"); //NOI18N
         try{
         BufferedReader in =new BufferedReader(new FileReader(f1));
         //System.out.println(comune);
         
         String[] ss;
         String s;
         while(true){
             s=null;
             s=in.readLine();
             if(s==null){System.out.println("Comune non trovato!!"); //NOI18N
             break;
             }
             ss=s.split(":"); //NOI18N
             //System.out.println(ss[0]+"   "+ss[1]); //NOI18N
             if(ss[0].equalsIgnoreCase(comune))return ss[1];
             
         }
         } catch(IOException e){System.out.println("File csv dei comuni non trovato!!");} //NOI18N
     return ""; //NOI18N
     }
     */
     public static String perControllo(String codice){
         int sum=0,j=0;
         int[] controllo={1,0,5,7,9,13,15,17,19,21,2,4,18,20,11,3,6,8,12,14,16,10,22,25,24,23}; 
  
         for (int i=0;i<7;i++){
             sum+=controllo[Character.isDigit(codice.charAt(i*2))?Integer.parseInt(codice.substring(i*2,i*2+1)):Character.getNumericValue(codice.charAt(i*2))-10];
             sum+=Character.isDigit(codice.charAt(i*2+1))?Character.getNumericValue(codice.charAt(i*2+1)):Character.getNumericValue(codice.charAt(i*2+1))-10;
         }
         sum+=controllo[Character.isDigit(codice.charAt(14))?Integer.parseInt(codice.substring(14,15)):Character.getNumericValue(codice.charAt(14))-10];
       byte[] car={Byte.parseByte(String.valueOf(65+sum%26))};
         return new String(car);
         
     }
    
     private static String rimuoviCaratteriNonValidi(String aString) {
	String[] vocacc={" ","\u00C0","\u00C8","\u00C9","\u00CC","\u00D2","\u00D9","'"},voc={"","A","E","E","I","O","U",""}; //NOI18N
        aString = aString.toUpperCase();
        for (int i=0;i<8;i++){
           aString =aString.replaceAll(vocacc[i],voc[i]);           
        }
        aString = aString.replaceAll("[^A-Z]", ""); //NOI18N
        return aString;


    }
     
     
    public String getCodiceFiscale(){
       String codice=perCognome(cognome);
       codice+=perNome(nome);
       codice+=perData(datanascita,mf);
       codice+=codiceComune.toUpperCase();
       codice+=perControllo(codice);
       return codice;
    }

}
