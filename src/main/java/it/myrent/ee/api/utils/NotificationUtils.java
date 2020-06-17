package it.myrent.ee.api.utils;

import it.aessepi.utils.beans.FormattedDate;
import it.myrent.ee.db.MROldGruppo;
import it.myrent.ee.db.Notification;
import it.myrent.ee.db.User;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Shivangani on 2/20/2018.
 */
public class NotificationUtils {

    public static final String JOB_COMPLETED = "Job Completed";
    public static final String JOB_FAILED = "Job Failed";
    public static final String JOB_RUNNING = "Job Running";
    public static final String DETACH_SERVICE = "DETACH_SERVICE";
    public static final String FIX_RES_SERVICE = "FIX_RES_SERVICE";
    public static final String UPDATE_TOTAL_RES = "UPDATE_TOTAL_RES";
    public static final String UPDATE_TOTAL_RA = "UPDATE_TOTAL_RA";

    public static void setDetachReservation(User user, Session sx, Date startDate, Date endDate, List locationList, String resultMsg, String errorMsg){

        List<Notification> notificationList = new ArrayList<Notification>();
        Query query = sx.createQuery("select n from Notification n where n.serviceName = :serviceName and n.status = :status and n.commentMsg is null order by n.lastUpdated desc ")
                .setParameter("serviceName", DETACH_SERVICE).setParameter("status", JOB_RUNNING).setMaxResults(1);

        Notification notification = null;
        notificationList = query.list();
        if(notificationList.size()>0 && notificationList!=null){
            notification = notificationList.get(0);
        } else {
            notification = new Notification();
        }
        notification.setUserCreated(user);
        notification.setServiceName(DETACH_SERVICE);
        String inputStr = "Start date : "+ startDate+" end date : "+ endDate;
//        if(locationList!=null && locationList.size()>0){
//            inputStr += " locations : "+ locationList;
//        }
        notification.setInputParam(inputStr);
        if ((errorMsg == "" || errorMsg == null) && resultMsg == null) {
            notification.setStatus(JOB_RUNNING);
        } else if(errorMsg != "" && errorMsg != null){
            notification.setStatus(JOB_FAILED);
            notification.setCommentMsg(errorMsg);
        } else {
            notification.setStatus(JOB_COMPLETED);
            //String outStr = "The request was completed. Not available reservations "+result;
            notification.setOutMsg(resultMsg);
        }
        notification.setLastUpdated(new Date());
        notification.setNotifyFlag(Boolean.FALSE);
        sx.saveOrUpdate(notification);
        sx.flush();
    }

    public static void setFixUnavailReservation(User user, Session sx, Date startDate, Date endDate, List locationList, List groupList, MROldGruppo grpUpgrade, String resultMsg, String errorMsg){

        List<Notification> notificationList = new ArrayList<Notification>();
        Query query = sx.createQuery("select n from Notification n where n.serviceName = :serviceName and n.status = :status and n.commentMsg is null order by n.lastUpdated desc ")
                .setParameter("serviceName", FIX_RES_SERVICE).setParameter("status", JOB_RUNNING).setMaxResults(1);

        Notification notification = null;
        notificationList = query.list();
        if(notificationList.size()>0 && notificationList!=null){
            notification = notificationList.get(0);
        } else {
            notification = new Notification();
        }
        notification.setUserCreated(user);
        notification.setServiceName(FIX_RES_SERVICE);
        String inputStr = "Start date : "+ startDate+" end date : "+ endDate;
//        if(locationList!=null && locationList.size()>0){
//            inputStr += " locations : "+ locationList;
//        }
//        if(groupList!=null && groupList.size()>0){
//            inputStr += " groups : "+ groupList;
//        }
//        if(grpUpgrade!=null){
//            inputStr += " upgraded group : "+ grpUpgrade;
//        }
        notification.setInputParam(inputStr);
        if ((errorMsg == "" || errorMsg == null) && resultMsg == null) {
            notification.setStatus(JOB_RUNNING);
        } else if(errorMsg != "" && errorMsg != null){
            notification.setStatus(JOB_FAILED);
            notification.setCommentMsg(errorMsg);
        } else {
            notification.setStatus(JOB_COMPLETED);
            //String outStr = "The request was completed. Not available reservations "+result;
            notification.setOutMsg(resultMsg);
        }
        notification.setLastUpdated(new Date());
        notification.setNotifyFlag(Boolean.FALSE);
        sx.saveOrUpdate(notification);
        sx.flush();
    }

    public static void setUpdateTotalRes(User user, Session sx, Date startDate, Date endDate, Boolean checkWithoutRow, String resultMsg, String errorMsg){
        List<Notification> notificationList = new ArrayList<Notification>();
        Query query = sx.createQuery("select n from Notification n where n.serviceName = :serviceName and n.status = :status and n.commentMsg is null order by n.lastUpdated desc ")
                .setParameter("serviceName", UPDATE_TOTAL_RES).setParameter("status", JOB_RUNNING).setMaxResults(1);

        Notification notification = null;
        notificationList = query.list();
        if(notificationList.size()>0 && notificationList!=null){
            notification = notificationList.get(0);
        } else {
            notification = new Notification();
        }
        notification.setUserCreated(user);
        notification.setServiceName(UPDATE_TOTAL_RES);
        String inputStr = "Start date : "+ startDate+" end date : "+ endDate+" checkWithoutRow : "+checkWithoutRow;
        notification.setInputParam(inputStr);
        if ((errorMsg == null || errorMsg.equals("")) && resultMsg == null) {
            notification.setStatus(JOB_RUNNING);
        } else if(errorMsg != null && (!errorMsg.equals(""))){
            notification.setStatus(JOB_FAILED);
            notification.setCommentMsg(errorMsg);
        } else {
            notification.setStatus(JOB_COMPLETED);
            //String outStr = "The request was completed. Not available reservations "+result;
            notification.setOutMsg(resultMsg);
        }
        notification.setLastUpdated(new Date());
        notification.setNotifyFlag(Boolean.FALSE);
        sx.saveOrUpdate(notification);
        sx.flush();
    }

    public static void setUpdateTotalRA(User user, Session sx, Date startDate, Date endDate, Boolean checkWithoutRow, String resultMsg, String errorMsg){
        List<Notification> notificationList = new ArrayList<Notification>();
        Query query = sx.createQuery("select n from Notification n where n.serviceName = :serviceName and n.status = :status and n.commentMsg is null order by n.lastUpdated desc ")
                .setParameter("serviceName", UPDATE_TOTAL_RA).setParameter("status", JOB_RUNNING).setMaxResults(1);

        Notification notification = null;
        notificationList = query.list();
        if(notificationList.size()>0 && notificationList!=null){
            notification = notificationList.get(0);
        } else {
            notification = new Notification();
        }
        notification.setUserCreated(user);
        notification.setServiceName(UPDATE_TOTAL_RA);
        String inputStr = "Start date : "+ startDate+" end date : "+ endDate+" checkWithoutRow : "+checkWithoutRow;
        notification.setInputParam(inputStr);
        if ((errorMsg == "" || errorMsg == null) && resultMsg == null) {
            notification.setStatus(JOB_RUNNING);
        } else if(errorMsg != "" && errorMsg != null){
            notification.setStatus(JOB_FAILED);
            notification.setCommentMsg(errorMsg);
        } else {
            notification.setStatus(JOB_COMPLETED);
            //String outStr = "The request was completed. Not available reservations "+result;
            notification.setOutMsg(resultMsg);
        }
        notification.setLastUpdated(new Date());
        notification.setNotifyFlag(Boolean.FALSE);
        sx.saveOrUpdate(notification);
        sx.flush();
    }

}
