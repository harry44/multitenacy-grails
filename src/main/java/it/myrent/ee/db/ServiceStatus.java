package it.myrent.ee.db;

import it.aessepi.utils.db.PersistentInstance;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 *
 * @author rahul
 */
public class ServiceStatus implements PersistentInstance, Comparable<ServiceStatus>{
    
private  Integer id ;
private String description;
private boolean isOut;


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
    
      public boolean getIsOut() {
        return isOut;
    }

    public void setIsOut(boolean isOut) {
        this.isOut = isOut;
    }

   

    @Override
    public int compareTo(ServiceStatus o) {
        if (o == null) {
            return 1;
        } else {
            return new CompareToBuilder().append(getDescription(), o.getDescription()).
                    append(getId(), o.getId()).
                    toComparison();
        }
    }
    
      @Override
    public String toString() {
        return description != null ? description : new String();
    }


    public boolean equals(Object other) {
        if (!(other instanceof ServiceStatus)) {
            return false;
        }
        ServiceStatus castOther = (ServiceStatus) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }
    
    
    
}
