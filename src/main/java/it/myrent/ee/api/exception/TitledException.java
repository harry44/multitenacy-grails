/*
 * DatabaseDeleteException.java
 *
 * Created on 10 octombrie 2005, 10:58
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package it.myrent.ee.api.exception;
import it.aessepi.utils.BundleUtils;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jamess
 */
public class TitledException extends Exception {
    
    private String title;
    protected static final ResourceBundle bundle = BundleUtils.getBundle("it/myrent/ee/api/exception/Bundle");
    protected static Log log = LogFactory.getLog(TitledException.class);

    public TitledException(Throwable cause) {
        super(cause);
    }

    public TitledException(String message, String title) {
        super(message != null ? message : bundle.getString("unknown_error_message"));
        setTitle(title);
    }
    
    public TitledException(String message, String title, Throwable cause) {
        super(message != null ? message : bundle.getString("unknown_error_message"), cause);
        setTitle(title);        
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void showMessage(Component parent) {
        showErrorMessage(parent);
    }

    public void showMessage(Component parent, String title) {
        setTitle(title);
        showMessage(parent);
    }
    
    public void showErrorMessage(Component parent) {
        JOptionPane.showMessageDialog(parent, getMessage(), getTitle(), JOptionPane.ERROR_MESSAGE);
    }

    public void showInformationMessage(Component parent) {
        JOptionPane.showMessageDialog(parent, getMessage(), getTitle(), JOptionPane.INFORMATION_MESSAGE);
    }
}
