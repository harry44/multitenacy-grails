package it.myrent.ee.api.utils;

/**
 * Created by Madhvendra on 04/10/2017.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

        import java.io.IOException;
        import java.io.StringReader;
        import java.util.logging.Level;
        import java.util.logging.Logger;

        import javax.xml.parsers.ParserConfigurationException;
        import javax.xml.parsers.SAXParser;
        import javax.xml.parsers.SAXParserFactory;

        import it.myrent.ee.db.MROldContrattoNoleggio;
        import it.myrent.ee.db.MROldPrenotazione;
        import org.xml.sax.InputSource;
        import org.xml.sax.SAXException;

/**
 *
 * @author paolo
 */
public class ProductNameMerger {

    MROldContrattoNoleggio contrattoNoleggio;
    MROldPrenotazione prenotazione;

    SAXParserFactory factory;
    SAXParser saxParser;
    ProductNameHandler handler;
    InputSource inputSource;
    ProductNameHandler.productNameHandlerListener handlerListener;
    ProductNameHandler.productNameHandlerListener myListener;

    public interface productNameDescriptionListener{
        public void productName(String productName);
    }

    public ProductNameHandler.productNameHandlerListener getMyListener() {
        return myListener;
    }

    public void setMyListener(ProductNameHandler.productNameHandlerListener myListener) {
        this.myListener = myListener;
    }





    public ProductNameMerger(String xmlString, MROldContrattoNoleggio cn, MROldPrenotazione pr, ProductNameHandler.productNameHandlerListener listener) throws ParserConfigurationException, SAXException {
        this.myListener = listener;
        this.contrattoNoleggio = cn;
        this.prenotazione = pr;
        this.inputSource =  new InputSource(new StringReader(xmlString));
        inputSource.setEncoding("utf-8");

        this.handlerListener = new ProductNameHandler.productNameHandlerListener() {
            @Override
            public void productNameUpdated(String productName) {
                if(myListener != null){
                    myListener.productNameUpdated(productName);
                }
                if(contrattoNoleggio != null){
                    contrattoNoleggio.setProductNameDesc(productName);
                }
                if (prenotazione != null){
                    prenotazione.setProductNameDesc(productName);
                }
            }

            @Override
            public void error(boolean b) {
                if(myListener != null){
                    myListener.error(b);
                }
            }
        };

        this.factory = SAXParserFactory.newInstance();
        this.saxParser = this.factory.newSAXParser();
        this.handler = new ProductNameHandler(handlerListener);

        try {

            saxParser.parse(inputSource, handler);
        } catch (SAXException ex) {
            Logger.getLogger(ProductNameMerger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProductNameMerger.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public MROldContrattoNoleggio getUpdatedContrattoNoleggio(){
        return contrattoNoleggio;
    }

    public MROldPrenotazione getUpdatedPrenotazione(){
        return prenotazione;
    }

}
