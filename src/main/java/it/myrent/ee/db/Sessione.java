package it.myrent.ee.db;

import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.StandardToStringStyle;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Sessione implements it.aessepi.utils.db.PersistentInstance {
    
    private Integer id;

    private String sessionId;
    private String remoteIp;
    private String systemUser;
    private Date startTime;
    private Date endTime;
    private User user;

    private Integer operations;
    private Date lastOperationTime;
    
    public static final StandardToStringStyle TO_STRING_STYLE = new StandardToStringStyle();
    
    static {
        TO_STRING_STYLE.setContentEnd(""); //NOI18N
        TO_STRING_STYLE.setContentStart(""); //NOI18N
        TO_STRING_STYLE.setNullText(""); //NOI18N
        TO_STRING_STYLE.setUseIdentityHashCode(false);
        TO_STRING_STYLE.setUseClassName(false);
        TO_STRING_STYLE.setUseFieldNames(false);
        TO_STRING_STYLE.setFieldSeparator(" "); //NOI18N
    }
    
    public Sessione() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(String systemUser) {
        this.systemUser = systemUser;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getOperations() {
        return operations;
    }

    public void setOperations(Integer operations) {
        this.operations = operations;
    }

    public Date getLastOperationTime() {
        return lastOperationTime;
    }

    public void setLastOperationTime(Date lastOperationTime) {
        this.lastOperationTime = lastOperationTime;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this, TO_STRING_STYLE).append(getId()).append(getSessionId()).toString().trim();
    }
    
    @Override
    public boolean equals(Object other) {
        if(other != null && other instanceof User) {
            return new EqualsBuilder().append(getId(), ((User)other).getId()).isEquals();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
        .append(getId())
        .append(getSessionId())
        .toHashCode();
    }

}
