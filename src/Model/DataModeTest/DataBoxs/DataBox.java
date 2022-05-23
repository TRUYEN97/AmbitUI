/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model.DataModeTest.DataBoxs;

import Model.DataModeTest.ErrorLog;
import Model.DataSource.Setting.Setting;
import Model.ManagerUI.UIStatus.UiStatus;
import MyLoger.MyLoger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class DataBox {

    private final MyLoger loger;
    private final String itemFunction;
    private final List<ItemTest> itemTests;
    private boolean testing;
    private boolean isMultistacking;
    private String resultTest;
    private long startTime;
    private Double testTime;

    public DataBox(UiStatus uiStatus, String itemFunction) {
        this.loger = new MyLoger();
        this.itemTests = new ArrayList<>();
        this.itemFunction = itemFunction;
        this.testing = false;
        String localFunctionsFile = Setting.getInstance().getFunctionsLocalLog();
        String fileLogName = String.format("%s\\%s\\%s.txt",
                localFunctionsFile, uiStatus.getName(), itemFunction);
        if (!this.loger.begin(new File(fileLogName), true, true)) {
            String mess = "can't delete local function log file of " + itemFunction;
            JOptionPane.showMessageDialog(null, mess);
            ErrorLog.addError(mess);
        }
    }

    public String getItemFunction() {
        return itemFunction;
    }

    public boolean addItemtest(ItemTest itemTest) {
        return this.itemTests.add(itemTest);
    }

    public List<ItemTest> getListItemTest() {
        return new ArrayList<>(itemTests);
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

    public boolean isPass() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
