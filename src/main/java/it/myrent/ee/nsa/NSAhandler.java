/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.myrent.ee.nsa;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Shivangani on 10/6/2017.
 */
public class NSAhandler extends DefaultHandler {
    
    private String returnedSearchStatus;
    private String nsaCode;
    private boolean isNSACode;
    private boolean isReturnedSearchStatus;
    private NSAhandlerListener myListener;
    private boolean isDescriptionError;
    private String returnedDescriptionError;

    public NSAhandler() {
        
    }
    
    public interface NSAhandlerListener{
        public void nsaHandlerListener(String NSAcode, String returnedSearchStatus, String descriptionError);
        
    }

    public NSAhandler(NSAhandlerListener myListener) {
        this.myListener = myListener;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        
        if(isNSACode){
            nsaCode = new String(ch, start, length);
            isNSACode = false;
        }
        else if(isReturnedSearchStatus){
            returnedSearchStatus = new String(ch, start, length);
            isReturnedSearchStatus = false;
        }
        else if(isDescriptionError){
            returnedDescriptionError = new String(ch, start, length);
            isDescriptionError = false;
        }
        
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equalsIgnoreCase("out_des_errore")){
            if(returnedSearchStatus != null && returnedDescriptionError != null){
                myListener.nsaHandlerListener(nsaCode, returnedSearchStatus, returnedDescriptionError);
            }
                     
        }
        
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("out_cod_cliente")) {
            isNSACode = true;
	}
        else if(qName.equalsIgnoreCase("out_ret_code")){
            isNSACode = false;
            isDescriptionError = false;
            isReturnedSearchStatus = true;
        }
        else if(qName.equalsIgnoreCase("out_des_errore")){
            isNSACode = false;
            isReturnedSearchStatus = false;
            isDescriptionError = true;
        }
    }
    
    
    
}
