package it.myrent.ee.csv;

/**
 * Created by shivangani on 30/08/2019.
 */
public class CsvRecord {

    String[] fields;

    /**
     *
     * @return
     */
    public String[] getFields() {
        return fields;
    }

    /**
     *
     * @param fields
     */
    public void setFields(String[] fields) {
        this.fields = fields;
    }

    /**
     *
     * @return
     */
    public String[] toArray() {
        return getFields();
    }
}
