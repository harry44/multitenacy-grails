package it.myrent.ee.db;

/**
 * Created by Bharti on 7/11/2018.
 */
public class MROldVehicleUsage {

    private Integer id;
    private String shortDescription;
    private String longDescription;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @Override
    public String toString() {
        return this.longDescription;
    }
}
