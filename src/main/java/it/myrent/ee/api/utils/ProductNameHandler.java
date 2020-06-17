package it.myrent.ee.api.utils;

/**
 * Created by Madhvendra on 04/10/2017.
 */

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ProductNameHandler extends DefaultHandler {

    private boolean isProductName;
    private int productNameCount = 0;
    private productNameHandlerListener listener;
    private boolean isError;

    public interface productNameHandlerListener {
        public void productNameUpdated(String productName);

        public void error(boolean b);
    }

    public ProductNameHandler(productNameHandlerListener myListener) {
        this.listener = myListener;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (isProductName) {
            String focusProductName = new String(ch, start, length);
            listener.productNameUpdated(focusProductName);
            listener.error(false);
            productNameCount++;
            isProductName = false;

        }
        if (isError) {
            listener.error(isError);
            isError = false;
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("returnXml")) {
            if (productNameCount <= 0) {
                listener.error(true);
            }
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

        if (qName.equalsIgnoreCase("c3")) {
            isProductName = true;
        }
//        if (qName.equalsIgnoreCase("severity")){
//            isError = true;
//        }
    }


}
