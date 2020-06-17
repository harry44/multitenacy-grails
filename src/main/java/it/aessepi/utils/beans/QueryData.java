/*
 * QueryData.java
 *
 * Created on 06 februarie 2006, 14:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package it.aessepi.utils.beans;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author jamess
 */
public class QueryData {
    
    
    /** Creates a new instance of QueryData */
    public QueryData() {
    }
    
    public QueryData(String queryString) {
        setQueryString(queryString);
    }
    
    public QueryData(String queryString, int firstResult, int maxResults) {
        setQueryString(queryString);
        setFirstResult(firstResult);
        setMaxResults(maxResults);
    }
    
    public QueryData(QueryData otherQueryData) {
        this(otherQueryData.getQueryString(), otherQueryData.getFirstResult(), otherQueryData.getMaxResults());
        setStaticQueryString(otherQueryData.getStaticQueryString());
        if(otherQueryData.parameterCount() > 0) {
            for(int i = 0; i < otherQueryData.parameterCount(); i++) {
                addParameter(otherQueryData.getNameAt(i), otherQueryData.getValueAt(i));
            }
        }
        
        if(otherQueryData.staticParameterCount() > 0) {
            for(int i = 0; i < otherQueryData.staticParameterCount(); i++) {
                addStaticParameter(otherQueryData.getStaticNameAt(i), otherQueryData.getStaticValueAt(i));
            }
        }
    }
    
    private String queryString;    
    private String staticQueryString;
    private String countQueryString;
    
    private Vector parameterNames = new Vector();
    private Vector parameterValues = new Vector();
    
    private Vector staticParameterNames = new Vector();
    private Vector staticParameterValues = new Vector();
    
    private int firstResult = -1;
    private int maxResults = -1;    
    
    /**
     * Returns the query string of this object
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     *Sets the query string to be <code>queryString</code>
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
    
    /**
     *Adds a new parameter to the parameter list
     */
    public void addParameter(String name, Object value) {
        parameterNames.add(name);
        parameterValues.add(value);
    }
    
    /**
     *Adds a new parameter to the static parameter list
     */
    public void addStaticParameter(String name, Object value) {
        staticParameterNames.add(name);
        staticParameterValues.add(value);
    }
    
    /**
     *Returns the name of the parameter at index
     */
    public String getNameAt(int index) {
        return (String)parameterNames.get(index);
    }
    
    /**
     *Returns the value of the parameter at index
     */
    public Object getValueAt(int index) {
        return parameterValues.get(index);
    }
    
    /**
     *Returns the name of the static parameter at index
     */
    public String getStaticNameAt(int index) {
        return (String)staticParameterNames.get(index);
    }
    
    /**
     *Returns the value of the static parameter at index
     */
    public Object getStaticValueAt(int index) {
        return staticParameterValues.get(index);
    }
    
    /**
     *Returns the number of parameters
     */
    public int parameterCount() {
        return parameterNames.size();
    }
    
    /**
     *Returns the number of static parameters
     */
    public int staticParameterCount() {
        return staticParameterNames.size();
    }
    
    /**
     *Clears the parameter list
     */
    public void clearParameters() {
        parameterNames.clear();
        parameterValues.clear();
    }
    
    /**
     *Clears the static parameter list
     */
    public void clearStaticParameters() {
        staticParameterNames.clear();
        staticParameterValues.clear();
    }

    /**
     *Returns the index of the first result of the query.
     */
    public int getFirstResult() {
        return firstResult;
    }

    /**
     *Sets the index of the first result of the query to be <code>firstResult</code>
     */
    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    /**
     *Returs the maximum number of results of a query
     */
    public int getMaxResults() {
        return maxResults;
    }

    /**
     *Sets the maximum number of results of a query to be <code>maxResults</code>
     */
    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }
    
    /**
     *True if <code>maxResults</code> field has a valid value.
     */
    public boolean isMaxResultsSet() {
        return getMaxResults() > -1;
    }
    
    /**
     *True if <code>firstResult</code> field has a valid value
     */
    public boolean isFirstResultSet() {
        return getFirstResult() > -1;
    }
    
    /**
     *True if <code>countQueryString</code> has a valid value
     */
    public boolean isCountQuerySet() {
        return (getCountQueryString() != null && getCountQueryString().trim().length() > 0);
    }
    
    /**
     *True if <code>staticqueryString</code> has a valid value
     */
    public boolean isStaticQuerySet() {
        return (getStaticQueryString() != null && getStaticQueryString().trim().length() > 0);
    }

    /**
     *Returns the string used by the count query
     */
    public String getCountQueryString() {
        return countQueryString;
    }

    /**
     *Sets the value of the string used by the count query to be <code>countQueryString</code>
     */
    public void setCountQueryString(String countQueryString) {
        this.countQueryString = countQueryString;
    }
    
    /**
     *Returns the string to be used always in the queries. That's why we call is STATIC.
     */
    public String getStaticQueryString() {
        return staticQueryString;
    }
    
    /**
     *Sets the string to be used in all query to be <code>staticQueryString</code>.
     *<br> Do not forget to add the static parameters if they exist.
     *<br> The static query string should have the form :
     *<br><code> where x.field = :parameter </code>
     *<br>The string should have both trailing and ending spaces!
     */
    public void setStaticQueryString(String staticQueryString) {
        this.staticQueryString = staticQueryString;
    }
    
    public Integer count(Session sx) throws HibernateException {
        Integer count = null;
        if(isCountQuerySet()) {
            Query qx = sx.createQuery(getCountQueryString());
            qx.setCacheable(true);
            for(int i = 0; i < parameterCount(); i++) {
                if(getValueAt(i) instanceof Collection) {
                    qx.setParameterList(getNameAt(i), (Collection)getValueAt(i));
                } else {
                    qx.setParameter(getNameAt(i), getValueAt(i));
                }
            }
            for(int i = 0; i < staticParameterCount(); i++) {
                if(getStaticValueAt(i) instanceof Collection) {
                    qx.setParameterList(getStaticNameAt(i), (Collection)getStaticValueAt(i));
                } else {
                    qx.setParameter(getStaticNameAt(i), getStaticValueAt(i));
                }
            }
            count = new Integer(((Number)qx.uniqueResult()).intValue());
        }
        return count;
    }
    
    private Query createQuery(Session sx) throws HibernateException {
        Query qx = sx.createQuery(getQueryString());
        qx.setCacheable(true);
        if(isFirstResultSet()) {
            qx.setFirstResult(getFirstResult());
        }
        
        if(isMaxResultsSet()) {
            qx.setMaxResults(getMaxResults());
        }
        
        for(int i = 0; i < parameterCount(); i++) {
            if(getValueAt(i) instanceof Collection) {
                qx.setParameterList(getNameAt(i), (Collection)getValueAt(i));
            } else {
                qx.setParameter(getNameAt(i), getValueAt(i));
            }
        }
        
        for(int i = 0; i < staticParameterCount(); i++) {
            if(getStaticValueAt(i) instanceof Collection) {
                qx.setParameterList(getStaticNameAt(i), (Collection)getStaticValueAt(i));
            } else {
                qx.setParameter(getStaticNameAt(i), getStaticValueAt(i));
            }
        }
        return qx;
    }
    
    public List query(Session sx) throws HibernateException {


        return createQuery(sx).list();
    }
    
    public Iterator iterate(Session sx) throws HibernateException {
        return createQuery(sx).iterate();
    }
}
