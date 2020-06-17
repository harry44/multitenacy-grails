package it.myrent.ee.db;

import java.util.Date;

/**
 * Created by Shivangani on 2/20/2018.
 */
public class Notification implements it.aessepi.utils.db.PersistentInstance {

    private Integer id;
    private User userCreated;
    private String status;
    private String serviceName;
    private String inputParam;
    private String outMsg;
    private String commentMsg;
    private Boolean notifyFlag;
    private Date lastUpdated;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getInputParam() {
        return inputParam;
    }

    public void setInputParam(String inputParam) {
        this.inputParam = inputParam;
    }

    public String getOutMsg() {
        return outMsg;
    }

    public void setOutMsg(String outMsg) {
        this.outMsg = outMsg;
    }

    public String getCommentMsg() {
        return commentMsg;
    }

    public void setCommentMsg(String commentMsg) {
        this.commentMsg = commentMsg;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(User userCreated) {
        this.userCreated = userCreated;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    public Boolean getNotifyFlag() {
        return notifyFlag;
    }

    public void setNotifyFlag(Boolean notifyFlag) {
        this.notifyFlag = notifyFlag;
    }
}
