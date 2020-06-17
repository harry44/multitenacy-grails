package it.myrent.ee.api.utils;

import javax.swing.*;
import java.util.Date;

/**
 * Created by Shivangani on 10/6/2017.
 */
public abstract class SwingWorkerAssociatorAbstract extends SwingWorker<Boolean,Integer> {
    //private JDialogProgressbar progressbar;
    private Date from;
    private Date to;
    //private JDialog myParent;
    private boolean stopWorking;

    /*public SwingWorkerAssociatorAbstract(Window parent, String loadingMessage) {
        this.progressbar = new JDialogProgressbar(parent, loadingMessage);
        this.stopWorking = false;
    }*/



    public void setDate(Date fromData, Date toData){
        this.from = fromData;
        this.to = toData;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        return false;
    }

    @Override
    protected void done() {
        //progressbar.setVisible(false);
//        if(myParent!= null){
//            myParent.setVisible(false);
//        }
        stopWorking = true;

    }

    public boolean isStopWorking() {
        return stopWorking;
    }

    /*public JDialogProgressbar getProgressbar() {
        return progressbar;
    }
*/
    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

//    public JDialog getMyParent() {
//        return myParent;
//    }
//
//    public void setMyParent(JDialog myParent) {
//        this.myParent = myParent;
//    }

}
