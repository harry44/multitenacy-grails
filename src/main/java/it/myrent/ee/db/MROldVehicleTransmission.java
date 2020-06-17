package it.myrent.ee.db;

/**
 * Created by shivangani on 25/07/2019.
 */
public class MROldVehicleTransmission implements it.aessepi.utils.db.PersistentInstance{

    private Integer id;
    private String description;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
