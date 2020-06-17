package it.myrent.ee.db;

/**
 * Created by bharti on 10/15/2019.
 */
public class MROldVehicleChassis implements it.aessepi.utils.db.PersistentInstance {

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
