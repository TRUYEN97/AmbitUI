/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataModeTest.DataBoxs;

import Model.DataModeTest.ErrorLog;
import Model.ManagerUI.UIStatus.UiStatus;
import MyLoger.MyLoger;
import java.io.File;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class DataBox {

    private final MyLoger loger;
    private final String itemName;
    private boolean testing;
    private boolean isMultistacking;
    private String resultTest;
    private long startTime;
    private Double testTime;

    public DataBox(UiStatus uiStatus, String itemName) {
        this.loger = new MyLoger();
        this.itemName = itemName;
        this.testing = false;
        String fileLogName = String.format("Log\\TestLog\\%s\\%s.txt", uiStatus.getName(), itemName);
        if (!this.loger.begin(new File(fileLogName), true, true)) {
            String mess = "can't delete local function log file of " + itemName;
            JOptionPane.showMessageDialog(null, mess);
            ErrorLog.addError(mess);
        }
    }

    public String getItemName() {
        return itemName;
    }

    public void setResult(String resultTest) {
        if (resultTest == null) {
            this.resultTest = new String();
            return;
        }
        this.resultTest = resultTest;
    }

    public void setMultistacking() {
        this.isMultistacking = true;
    }

    public void addLog(Object str) {
        this.loger.addLog(str);
    }

    public String getResultTest() {
        return resultTest;
    }

    public boolean isTesting() {
        return testing;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        testing = true;
    }

    public double getRunTime() {
        if (testTime != null) {
            return testTime;
        }
        return (System.currentTimeMillis() - this.startTime) / 1000.0;
    }

    public void end() {
        this.loger.close();
        this.testing = false;
        testTime = getRunTime();
    }

    public boolean isMultiStacking() {
        return isMultistacking;
    }

    public String getLog() {

        return this.loger.getLog();
    }

    public MyLoger getLoger() {
        return this.loger;
    }

    
}
